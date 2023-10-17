import java.io.*;
import java.net.Socket;

public class Client1 {
    static final int PORT = 2323;
    private Socket sock;


    Client1() {
        try {
            sock = new Socket("localhost", PORT);
            System.out.println("Connexion établie");



            DataOutputStream oos = new DataOutputStream(sock.getOutputStream());
            DataInputStream ois = new DataInputStream(sock.getInputStream());
            System.out.println("j'attend mon numéro");
            System.out.println("Je suis le client numéro : " + ois.readInt());

            oos.writeUTF("Cyprien");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        new Main(Levels.MEDIUM);

    }

}