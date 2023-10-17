import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.valueOf;


/**
 * Graphical user interface
 */
public class GUI extends JPanel implements ActionListener, Runnable  {
    private Main main;

    public boolean isFirstClick = true;

    private JPanel grille;
    public Case[][] lab;
    private JButton butRelancer;

    private JMenuBar menuBar;
    private JMenu menuPartie;
    private JMenu mChangerNiveau;
    private JMenuItem mMultijoueur;
    private JMenuItem EASY;
    private JMenuItem MEDIUM;
    private JMenuItem HARD;
    private JMenuItem CUSTOM;

    private JMenuItem Quit;

    private JPanel nbMinesPanel;
    private JLabel nbMinesLabel;

    private JLabel scoreJoueurLabel;
    private JPanel scoreJoueurPanel;

    static final int PORT = 2323;
    private Socket sock;

    private DataOutputStream oos;
    private DataInputStream ois;

    private Color[] colors = {Color.BLACK, Color.BLUE, Color.GREEN, Color.RED, Color.MAGENTA, Color.ORANGE, Color.PINK, Color.CYAN};

    private final static int DIM=50 ;

    private boolean isMultiplayerloose = false;

    private int numClient = 0;

    private int nbPlayer = 0; //nb de player
    private int numPlayer;
    private int nbLignes = 0;

    private int nbMinesLeft = 0;
    private boolean isReady = false;

    private int[] score = {0,0,0,0,0,0,0,0,0,0};





    /**
     *
     */
    GUI(Main main) {

        this.main = main;
        setLayout(new BorderLayout());
        int Dimx = main.getChamp().getDim()[0];
        int Dimy = main.getChamp().getDim()[1];
        nbMinesLeft = main.getChamp().getNbMines();
        grille = new JPanel(new GridLayout(Dimx, Dimy));

        lab = new Case[Dimx][Dimy];
        for (int i = 0; i < Dimx; i++) {
            for (int j = 0; j < Dimy; j++) {
                if (main.getChamp().getTable(i, j) != "X") {
                    lab[i][j] = new Case(valueOf(main.getChamp().getTable(i, j)), false, this, i ,j);

                }
                else
                    lab[i][j] = new Case(-1, true , this, i ,j);

                // lab[i][j] = new JLabel(new ImageIcon(new ImageIcon(getClass().getResource("/Bombe.png")).getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
                grille.add(lab[i][j]);

            }
        }
        add(grille, BorderLayout.CENTER);
        butRelancer = new JButton("Relancer une partie");
        butRelancer.addActionListener(this);
        add(butRelancer, BorderLayout.SOUTH);

        nbMinesPanel = new JPanel();
        nbMinesLabel = new JLabel("Nombre de mines restantes : " + nbMinesLeft);
        nbMinesPanel.add(nbMinesLabel);
        add(nbMinesPanel, BorderLayout.NORTH);

        JMenuBar menuBar = new JMenuBar();
        JMenu menuPartie = new JMenu("Partie");
        menuBar.add(menuPartie);

        mChangerNiveau = new JMenu("Nouveau Niveau");
        menuPartie.add(mChangerNiveau);
        mChangerNiveau.addActionListener(this);

        mMultijoueur = new JMenuItem("Multijoueur");
        menuPartie.add(mMultijoueur);
        mMultijoueur.addActionListener(this);


        EASY = new JMenuItem("EASY");
        MEDIUM = new JMenuItem("MEDIUM");
        HARD = new JMenuItem("HARD");
        CUSTOM = new JMenuItem("CUSTOM");
        mChangerNiveau.add(EASY);
        mChangerNiveau.add(MEDIUM);
        mChangerNiveau.add(HARD);
        mChangerNiveau.add(CUSTOM);
        EASY.addActionListener(this);
        MEDIUM.addActionListener(this);
        HARD.addActionListener(this);
        CUSTOM.addActionListener(this);


        Quit = new JMenuItem("Quitter");
        menuPartie.add(Quit);
        Quit.addActionListener(this);





        main.setJMenuBar(menuBar);


    }
    /**
     * Fonction de notre client pour le mode multijoueur
     */
    public void Client1() {
        try {
            this.main.nouvellePartie(Levels.MEDIUM);
            sock = new Socket("localhost", PORT);
            System.out.println("Connexion établie");

            oos = new DataOutputStream(sock.getOutputStream());
            ois = new DataInputStream(sock.getInputStream());
            System.out.println("j'attend mon numéro");
            numClient = ois.readInt();
            System.out.println("Je suis le client numéro : " + numClient);


            int reponse = JOptionPane.showConfirmDialog(this, "En attente d'autres joueurs, voulez-vous lancer ?", "Multiplayer game", JOptionPane.OK_CANCEL_OPTION);
            if (reponse == JOptionPane.YES_OPTION) //traitement de la rep.
            {
                oos.writeUTF("READY");
                isReady = true;
            }

            Thread monThread = new Thread(this);
            monThread.start();

        } catch (IOException e) {
            e.printStackTrace();

        }
    }
    public Main getmain(){
        return (main);
    }
    /**
     * @param e
     * fonction action performed sur les bouttons
     */
    public void actionPerformed(ActionEvent e) {

        if ((e.getSource() == butRelancer)) {

            relancerPartie(Levels.EASY);
            isMultiplayerloose = false;
        }
        if ((e.getSource() == EASY)) {
            relancerPartie(Levels.EASY);
            isMultiplayerloose = false;

        }
        if ((e.getSource() == MEDIUM)) {

            relancerPartie(Levels.MEDIUM);
            isMultiplayerloose = false;
        }
        if ((e.getSource() == HARD)) {

            relancerPartie(Levels.HARD);
            isMultiplayerloose = false;
        }
        if ((e.getSource() == CUSTOM)) {

            relancerPartie(Levels.CUSTOM);
            isMultiplayerloose = false;

        }
        if (e.getSource() == mMultijoueur) {
            mMultijoueur.setEnabled(false);

            if (numClient%3 == 0)
                nbLignes = numClient/3;
            else
                nbLignes = numClient/3 + 1;

            System.out.println("nbLignes : " + nbLignes);

            scoreJoueurPanel = new JPanel(new GridLayout( nbLignes, 3));
            for (int i = 0; i < numClient; i++) {

                scoreJoueurLabel = new JLabel("Joueur " + (i+1) + " : 0");
                scoreJoueurPanel.add(scoreJoueurLabel);
            }
            add(scoreJoueurPanel, BorderLayout.SOUTH);
            Client1();
            this.getmain().setMultiplayer(true);
            getmain().setTitle("XMines - Multijoueur joueur " + numClient);
            getmain().setResizable(false);
            butRelancer.setEnabled(false);
            butRelancer.setVisible(false);
            remove(butRelancer);
            revalidate();
        }
        isFirstClick = true;
        if ((e.getSource() == Quit)) {
            main.dispose();
        }

    }

    public void relancerPartie(Levels levels) {
        main.nouvellePartie(levels);
        refreshGrid(levels);
        nbMinesLeft = main.getChamp().getNbMines();
    }
    public void relancerPartie(Levels levels, int posx, int posy) {
        main.nouvellePartie(levels, posx, posy);
        System.out.println("yes");
        refreshGrid(levels);
        lab[posx][posy].repaint();
        nbMinesLeft = main.getChamp().getNbMines();
    }

    /**
     * @param levels
     * actualise l'état de la grille
     */
    public void refreshGrid(Levels levels) {
        int Dimx = main.getChamp().getDim()[0];
        int Dimy = main.getChamp().getDim()[1];
        int nbMines = main.getChamp().getNbMines();
        grille.removeAll();
        grille.setLayout(new GridLayout(Dimx, Dimy));


        nbMinesLabel.setText("Nombre de mines restantes : " + nbMines);
        nbMinesPanel.add(nbMinesLabel);
        add(nbMinesPanel, BorderLayout.NORTH);

        lab = new Case[Dimx][Dimy];
        for (int i = 0; i < Dimx; i++) {
            for (int j = 0; j < Dimy; j++) {
                if (main.getChamp().getTable(i, j) != "X") {
                    lab[i][j] = new Case(valueOf(main.getChamp().getTable(i, j)), false , this ,i,j );

                } else
                    lab[i][j] = new Case(0, true, this ,i,j);

                grille.add(lab[i][j]);
            }
        }
        main.pack();
    }

    public void revealOneCase(int x, int y) {
        lab[x][y].isRevealed = true;
        lab[x][y].repaint();
    }
    /*public void revealempty(int posx, int posy){
        System.out.println("in");
        if (posx < 0 || posx >= lab.length || posy < 0 || posy >= lab[0].length) {
            return;
        }

        if (lab[posx][posy].isRevealed) {
            return;
        }
        if (lab[posx][posy].getNumber() != 0) {
            lab[posx][posy].isRevealed = true;
            return;
        }
        lab[posx][posy].isRevealed = true;

        revealempty(posx + 1, posy);
        revealempty(posx - 1, posy);
        revealempty(posx + 1, posy+1);
        revealempty(posx - 1, posy-1);
        revealempty(posx, posy + 1);
        revealempty(posx, posy - 1);
        revealempty(posx-1, posy + 1);
        revealempty(posx+1, posy - 1);
    }*/

    /*
     * Propagation
     */
    public void revealempty(int x, int y) {
        lab[x][y].isRevealed = true;

        for (int i = x-1; i < x+2; i++) {
            for (int j = y-1; j < y+2; j++) {
                //if verify that we don't count the case outside the field
                if (i >= 0 && j >=0 && i < lab.length && j < lab.length){
                    if(!lab[i][j].getisMine() == true ){

                        lab[i][j].isRevealed = true;
                        if(lab[i][j].getNumber() == 0)
                            if (!(i == x && j == y))
                                if (!lab[i][j].getIsDone()){
                                    lab[i][j].setIsDone();
                                    revealempty(i, j);
                                }
                    }
                }
            }
        }
    }
    public void run(){
        try
        {
            while(true)
            {
                System.out.println("j'attend un message");
                switch (ois.readUTF())
                {
                    case "CASE":
                        System.out.println("je recois une case");
                        int x = ois.readInt();
                        int y = ois.readInt();

                        System.out.println("x : " + x + " y : " + y);
                        lab[x][y].isRevealed = true;
                        lab[x][y].setNumber(ois.readInt());
                        boolean isMine = ois.readBoolean();

                        lab[x][y].setisMine(isMine);

                        numPlayer = ois.readInt();

                        if (!isMine)
                            score[numPlayer-1] += 1;
                        setScoreBoard(nbPlayer);

                        System.out.println(score[numPlayer-1]);
                        if (isMine && numPlayer == numClient ) {
                            JOptionPane.showMessageDialog(this, "Tu as perdu");
                            isMultiplayerloose = true;
                        }
                        System.out.println("j'ai set up ma case");
                        lab[x][y].setColorrevealed(colors[numPlayer]);


                        lab[x][y].repaint();
                        break;
                    case "NEWPLAYER":
                        nbPlayer = ois.readInt();
                        setScoreBoard(nbPlayer);
                        break;
                        //scoreJoueurPanel.removeAll();
                }
            }
        }
        catch (IOException e) { e.printStackTrace(); }
    }

    public DataOutputStream getOos(){
        return (oos);
    }
    public DataInputStream getOis(){
        return (ois);
    }

    public Case getCase(int x, int y){
        return (lab[x][y]);
    }
    public boolean getisMultiplayerloose(){
        return (isMultiplayerloose);
    }

    public void revealAll(){
        for (int i = 0; i < lab.length; i++) {
            for (int j = 0; j < lab[0].length; j++) {
                lab[i][j].isRevealed = true;
                lab[i][j].repaint();
            }
        }
    }
    public void setScoreBoard(int nbPlayer)
    {
        try {
            if (nbPlayer % 3 == 0)
                nbLignes = nbPlayer / 3;
            else
                nbLignes = nbPlayer / 3 + 1;
            scoreJoueurPanel.removeAll();
            scoreJoueurPanel.setLayout(new GridLayout(nbLignes, 3));
            for (int i = 0; i < nbPlayer; i++) {
                if (numClient == i + 1) {
                    scoreJoueurLabel = new JLabel("(Vous) Joueur " + (i + 1) + ": " + score[i]);
                    scoreJoueurLabel.setFont(new Font("Arial", Font.BOLD, 12));
                } else
                    scoreJoueurLabel = new JLabel("Joueur " + (i + 1) + ": " + score[i]);
                scoreJoueurPanel.add(scoreJoueurLabel);
            }
            add(scoreJoueurPanel, BorderLayout.SOUTH);
            revalidate();
            repaint();
        }
        catch (Exception e) { e.printStackTrace(); }
    }
    public boolean getisReady(){
        return (isReady);
    }
    public JLabel getNbMinesLabel(){
        return (nbMinesLabel);
    }
    public void setNbMinesPanel(JPanel nbMinesPanel){
        this.nbMinesPanel = nbMinesPanel;
    }

    public int getNbMinesLeft(){
        return (nbMinesLeft);
    }
    public void setNbMinesLeft(int nbMinesLeft){
        this.nbMinesLeft = nbMinesLeft;
    }
}






