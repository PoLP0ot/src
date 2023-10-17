import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.lang.System.in;


public class Serveur implements Runnable {
    static final int PORT = 2323;
    private Socket sock;
    private ServerSocket server;

    private Main main;
    private List<Socket> clients = new ArrayList<Socket>();
    private List<DataOutputStream> oos = new ArrayList<DataOutputStream>();
    private List<DataInputStream> ois = new ArrayList<DataInputStream>();

    private List<Boolean> isReady = new ArrayList<Boolean>();
    private boolean allReady = false;

    private Color[] colors = {Color.BLACK, Color.BLUE, Color.GREEN, Color.RED, Color.MAGENTA, Color.ORANGE, Color.PINK, Color.CYAN};


    /**
     * Constructeur
     */
    Serveur(Main main) {
        this.main = main;
        try {
            System.out.println("Serveur en attente de connexion");
            server = new ServerSocket(PORT);
            Thread monThread = new Thread(this);
            monThread.start();
        }
            catch(IOException e){
                e.printStackTrace();
            }
        }
    /**
     * Méthode qui permet de lancer le serveur
     */
    public static void main(String[] args) {
        Main m = new Main(Levels.MEDIUM);
        m.setTitle("Xmines - serveur");
        m.setMultiplayer(true);
       // m.nouvellePartie(Levels.HARD);
        new Serveur(m);

    }
    //On crée un thread qui attend les clients. Chaque client est traité dans un thread séparé. Une fois que le client est crée, on rappelle le thread pour pouvoir en accueillir un nouveau, et on continue le premier pour éxecuter le code client
    /**
     * Run de notre thread pour un client
     */
    public void run()
    {
        try {
            sock = server.accept();
            clients.add(sock);
            System.out.println("Connexion établie avec 1 client");
            System.out.println("Nombre de clients connectés : " + clients.size());
            Thread monThread = new Thread(this);
            monThread.start();

            oos.add(new DataOutputStream(sock.getOutputStream()));
            ois.add(new DataInputStream(sock.getInputStream()));
            isReady.add(false);

            int numClient = clients.size();



            try {
                System.out.println("j'écris le numéro du client" + numClient);
                oos.get(numClient-1).writeInt(numClient);


                for (int i = 0; i < numClient; i++) {
                    oos.get(i).writeUTF("NEWPLAYER");
                    oos.get(i).writeInt(numClient);
                }



                while (true) {
                    if (allReady ==false) {
                        if (!isReady.contains(false)) {
                            allReady = true;
                            System.out.println("Tous les joueurs sont prêts");
                        }
                    }


                    System.out.println("j'attends un message");
                    switch (ois.get(numClient - 1).readUTF()) {
                        case "READY":
                            System.out.println("j'ai reçu un message");
                            isReady.set(numClient - 1, true);
                            System.out.println(numClient + " est prêt");
                            break;

                        case "NOM":
                            System.out.println("j'ai recu un nom");
                            break;

                        case "POS":
                            if (allReady) {
                                int x = ois.get(numClient - 1).readInt();
                                int y = ois.get(numClient - 1).readInt();
                                System.out.println("j'ai reçu la position : " + x + " " + y);
                                this.getMain().getGui().getCase(x, y).setColorrevealed(colors[numClient]);
                                this.getMain().getGui().revealOneCase(x, y);

                                Case c = this.getMain().getGui().getCase(x, y);
                                sendAllCases(c, numClient);
                            }
                            break;

                    }


                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    /**
     * Méthode qui permet d'envoyer toutes les cases à tous les clients
     */
    public void sendAllCases(Case c, int numClient) {
        for (int i = 0; i < clients.size(); i++) {
            try {
                oos.get(i).writeUTF("CASE");
                oos.get(i).writeInt(c.getPosx());
                oos.get(i).writeInt(c.getPosy());
                oos.get(i).writeInt(c.getNumber());
                oos.get(i).writeBoolean(c.getisMine());
                oos.get(i).writeInt(numClient);


            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
    public Main getMain(){
        return(main);
    }

}
