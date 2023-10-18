import static java.lang.Integer.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
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
    

    static final int PORT = 10000;
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
    private List<String> playerNamesList = new ArrayList<>();





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
    public void Client() {
        try {
            this.main.nouvellePartie(Levels.MEDIUM);
            sock = new Socket("localhost", PORT);
            System.out.println("Connexion établie");

            oos = new DataOutputStream(sock.getOutputStream());
            ois = new DataInputStream(sock.getInputStream());
            System.out.println("j'attend mon numéro");
            numClient = ois.readInt();
            System.out.println("Je suis le client numéro : " + numClient);
            String playerName = showPlayerNameDialog();

            if (playerName != null && !playerName.trim().isEmpty()) {
            oos.writeUTF("NOM"); // Envoyer le message "NOM"
            oos.writeUTF(playerName.trim()); // Ensuite, envoyer le nom du joueur
            System.out.println("Je suis le client numéro : " + playerName);
            }



            int reponse = JOptionPane.showConfirmDialog(this, "En attente d'autres joueurs, voulez-vous lancer ?", "Multiplayer game", JOptionPane.OK_CANCEL_OPTION);
            if (reponse == JOptionPane.YES_OPTION) //traitement de la rep.
            {
                oos.writeUTF("READY");
                isReady = true;
            }

            oos.writeUTF("GETNAMES"); // Demander au serveur d'envoyer la liste des noms

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

      // Si le bouton "butRelancer" est cliqué
if ((e.getSource() == butRelancer)) {
    // Relance le jeu avec le niveau de difficulté "EASY"
    relancerPartie(Levels.EASY);
    // Réinitialise l'état de défaite en mode multijoueur
    isMultiplayerloose = false;
}

// Si EASY sélectionnée
if ((e.getSource() == EASY)) {
    relancerPartie(Levels.EASY);
    isMultiplayerloose = false;
}

// Si MEDIUM sélectionnée
if ((e.getSource() == MEDIUM)) {
    relancerPartie(Levels.MEDIUM);
    isMultiplayerloose = false;
}

// Si HARD sélectionnée
if ((e.getSource() == HARD)) {
    relancerPartie(Levels.HARD);
    isMultiplayerloose = false;
}

// Si CUSTOM sélectionnée
if ((e.getSource() == CUSTOM)) {
    relancerPartie(Levels.CUSTOM);
    isMultiplayerloose = false;
}

// Si mMultijoueur sélectionnée
if (e.getSource() == mMultijoueur) {
    mMultijoueur.setEnabled(false);
    
    
    
    // Calcule nombre lignes pour afficher scores des joueurs
    if (numClient%3 == 0)
        nbLignes = numClient/3;
    else
        nbLignes = numClient/3 + 1;

    System.out.println("nbLignes : " + nbLignes);
   
    // Crée panel afficher scores joueurs
    scoreJoueurPanel = new JPanel(new GridLayout( nbLignes, 3));
    for (int i = 0; i < numClient; i++) {
        scoreJoueurLabel = new JLabel("Joueur " + (i+1) + " : 0");
        scoreJoueurPanel.add(scoreJoueurLabel);
    }
    add(scoreJoueurPanel, BorderLayout.SOUTH);

    // Initialise client mode multijoueur
    Client();
    this.getmain().setMultiplayer(true);
    getmain().setTitle("XMines - Multijoueur joueur " + numClient);
    getmain().setResizable(false);
    butRelancer.setEnabled(false);
    butRelancer.setVisible(false);
    remove(butRelancer);
    revalidate();
}

// Réinitialise état premier clic
isFirstClick = true;

// Si l'option Quit ferme la fenêtre principale
if ((e.getSource() == Quit)) {
    main.dispose();
}

    }
    /**
     * Fonction de notre serveur pour le mode multijoueur
     */
    private String showPlayerNameDialog() {
        String playerName = null;
        while (playerName == null || playerName.trim().isEmpty()) {
            playerName = JOptionPane.showInputDialog("Veuillez entrer votre nom de joueur:");
            if (playerName == null) {
                // L'utilisateur a annulé la boîte de dialogue
                return null;
            }
        }
        return playerName.trim();
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
 * Rafraîchit la grille de jeu en fonction du niveau de difficulté choisi.
 *
 * @param levels Le niveau de difficulté choisi.
 */
public void refreshGrid(Levels levels) {
    // Récupère les dimensions de la grille de jeu.
    int Dimx = main.getChamp().getDim()[0];
    int Dimy = main.getChamp().getDim()[1];
    
    // Récupère le nombre de mines pour le niveau choisi.
    int nbMines = main.getChamp().getNbMines();
    
    // Supprime tous les composants de la grille actuelle.
    grille.removeAll();
    
    // Définit la nouvelle disposition de la grille en fonction des dimensions récupérées.
    grille.setLayout(new GridLayout(Dimx, Dimy));

    // Met à jour le label indiquant le nombre de mines restantes.
    nbMinesLabel.setText("Nombre de mines restantes : " + nbMines);
    
    // Ajoute le label au panel dédié.
    nbMinesPanel.add(nbMinesLabel);
    
    // Ajoute le panel au haut de la fenêtre principale.
    add(nbMinesPanel, BorderLayout.NORTH);

    // Initialise le tableau de cases.
    lab = new Case[Dimx][Dimy];
    
    // Remplit la grille avec les nouvelles cases.
    for (int i = 0; i < Dimx; i++) {
        for (int j = 0; j < Dimy; j++) {
            // Si la case ne contient pas de mine...
            if (main.getChamp().getTable(i, j) != "X") {
                // Crée une nouvelle case sans mine avec la valeur correspondante.
                lab[i][j] = new Case(valueOf(main.getChamp().getTable(i, j)), false , this ,i,j );
            } else {
                // Sinon, crée une nouvelle case contenant une mine.
                lab[i][j] = new Case(0, true, this ,i,j);
            }
            // Ajoute la case à la grille.
            grille.add(lab[i][j]);
        }
    }
    
    // Ajuste la taille de la fenêtre principale pour s'adapter à la nouvelle grille.
    main.pack();
}
/**revele une case avec ses coordonnées */
public void revealOneCase(int x, int y) {
        lab[x][y].isRevealed = true;
        lab[x][y].repaint();
    }
   
    /**
 * Propagation pour révéler les cases vides adjacentes.
 *
 * @param x Coordonnée x de la case cliquée.
 * @param y Coordonnée y de la case cliquée.
 */
public void revealempty(int x, int y) {
    // Marque la case actuelle comme révélée.
    lab[x][y].isRevealed = true;

    // Parcourt les cases adjacentes.
    for (int i = x-1; i < x+2; i++) {
        for (int j = y-1; j < y+2; j++) {
            // Vérifie que les coordonnées sont à l'intérieur des limites de la grille.
            if (i >= 0 && j >=0 && i < lab.length && j < lab.length){
                // Si la case adjacente n'est pas une mine...
                if(!lab[i][j].getisMine() == true ){
                    // Marque la case adjacente comme révélée.
                    lab[i][j].isRevealed = true;
                    // Si la case adjacente est vide (elle n'a pas de mines autour)...
                    if(lab[i][j].getNumber() == 0)
                        // Vérifie que ce n'est pas la case initialement cliquée.
                        if (!(i == x && j == y))
                            // Vérifie que la case n'a pas déjà été traitée.
                            if (!lab[i][j].getIsDone()){
                                // Marque la case comme traitée.
                                lab[i][j].setIsDone();
                                // Appelle récursivement la fonction pour cette case.
                                revealempty(i, j);
                            }
                }
            }
        }
    }
}

    /**
 * La méthode run est exécutée lorsqu'un thread est démarré.
 * Elle gère la communication entre les joueurs en mode multijoueur.
 */
public void run(){
    try
    {
        // Boucle infinie pour écouter continuellement les messages entrants.
        while(true)
        {
            System.out.println("j'attend un message");
            
            // Lit le type de message reçu.
            switch (ois.readUTF())
            {
                // Si le message concerne une case spécifique...
                case "CASE":
                    System.out.println("je recois une case");
                    
                    // Lit les coordonnées de la case.
                    int x = ois.readInt();
                    int y = ois.readInt();

                    System.out.println("x : " + x + " y : " + y);
                    
                    // Met à jour l'état de la case.
                    lab[x][y].isRevealed = true;
                    lab[x][y].setNumber(ois.readInt());
                    boolean isMine = ois.readBoolean();
                    lab[x][y].setisMine(isMine);

                    // Lit le numéro du joueur qui a envoyé le message.
                    numPlayer = ois.readInt();

                    // Met à jour le score du joueur.
                    if (!isMine)
                        score[numPlayer-1] += 1;
                    setScoreBoard(nbPlayer);

                    System.out.println(score[numPlayer-1]);
                    
                    // Si la case est une mine et que le joueur actuel est le client, affiche un message de défaite.
                    if (isMine && numPlayer == numClient ) {
                        JOptionPane.showMessageDialog(this, "Tu as perdu");
                        isMultiplayerloose = true;
                    }
                    System.out.println("j'ai set up ma case");
                    
                    // Met à jour la couleur de la case pour indiquer le joueur qui l'a révélée.
                    lab[x][y].setColorrevealed(colors[numPlayer]);

                    // Rafraîchit l'affichage de la case.
                    lab[x][y].repaint();
                    break;
                
                // Si un nouveau joueur rejoint la partie...
                case "NEWPLAYER":
                    nbPlayer = ois.readInt();
                    setScoreBoard(nbPlayer);
                    break;

                //Nom des joueurs
                case "NAMESLIST":
                    int numberOfNames = ois.readInt();
                    playerNamesList.clear(); // Effacez la liste actuelle
                    for (int i = 0; i < numberOfNames; i++) {
                    String playerName = ois.readUTF();
                    playerNamesList.add(playerName); // Ajoutez chaque nom à la liste
                    }
                    setScoreBoard(playerNamesList.size()); // Mettez à jour la barre de score avec les nouveaux noms
        break;
            }
        }
    }
    // Gère les exceptions liées à la communication.
    catch (IOException e) { e.printStackTrace(); }
}
// Ces méthodes sont des "getters". Elles permettent d'accéder à des attributs privés de la classe.

public DataOutputStream getOos(){
    return (oos);  // Retourne l'objet DataOutputStream associé à cette classe.
}

public DataInputStream getOis(){
    return (ois);  // Retourne l'objet DataInputStream associé à cette classe.
}

public Case getCase(int x, int y){
    return (lab[x][y]);  // Retourne l'objet Case à la position (x, y) du tableau lab.
}

public boolean getisMultiplayerloose(){
    return (isMultiplayerloose);  // Retourne la valeur de l'attribut isMultiplayerloose.
}

// Cette méthode révèle toutes les cases du tableau lab.
public void revealAll(){
    for (int i = 0; i < lab.length; i++) {
        for (int j = 0; j < lab[0].length; j++) {
            lab[i][j].isRevealed = true;  // Chaque case est marquée comme révélée.
            lab[i][j].repaint();  // La case est redessinée pour refléter son nouvel état.
        }
    }
}

// Cette méthode met à jour le tableau des scores des joueurs.
public void setScoreBoard(int nbPlayer) {
    try {
        // Calcul du nombre de lignes nécessaires pour afficher tous les joueurs.
        if (nbPlayer % 3 == 0)
            nbLignes = nbPlayer / 3;
        else
            nbLignes = nbPlayer / 3 + 1;

        scoreJoueurPanel.removeAll();  // Supprime tous les éléments du panel des scores.
        scoreJoueurPanel.setLayout(new GridLayout(nbLignes, 3));  // Définit la disposition du panel.

        // Ajoute un label pour chaque joueur avec son score.
        for (int i = 0; i < nbPlayer; i++) {
            String playerName = (i < playerNamesList.size()) ? playerNamesList.get(i) : ("Joueur " + (i + 1));
            if (numClient == i + 1) {
                scoreJoueurLabel = new JLabel("(Vous) " + playerName + ": " + score[i]);
                scoreJoueurLabel.setFont(new Font("Arial", Font.BOLD, 12));  // Met en gras le score du joueur actuel.
            } else
                scoreJoueurLabel = new JLabel(playerName + ": " + score[i]);
            scoreJoueurPanel.add(scoreJoueurLabel);  // Ajoute le label au panel.
        }


        add(scoreJoueurPanel, BorderLayout.SOUTH);  // Ajoute le panel au composant principal.
        revalidate();  // Met à jour l'affichage.
        repaint();     // Redessine le composant.
    }
    catch (Exception e) { e.printStackTrace(); }  // Gère les exceptions éventuelles.
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


    public class LooseFrame extends JFrame {   //NouveauCadre hérite de JFrame
        private Panel TextLoose;
        public LooseFrame(){   //on définit les attributs de la classe
            super("Loose");  //on définit le titre
            setSize(100,100); // on définit la taille
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //mode de fermeture
            setLayout(new BorderLayout());
            TextLoose = new Panel();
            TextLoose.add(new JLabel("You Loose"));
            add(TextLoose, BorderLayout.CENTER);
            pack();
    
           
        }
}
}






