/**
 * 2022
 * Class Matrix for demineur
 */
import java.util.Random;
public class Matrix {
     // Constantes nombre de mines en fonction du niveau de difficulté
     private static final int NBMINES[] = {3, 25, 35};
     // Constantes dimensions de la grille en fonction du niveau de difficulté.
     private static final int DIMMatrix[][] = {{8,8}, {12,12}, {16, 16}};
 
     // Variable pour le nombre de mines.
     private static int nbMines = 0;
     // Tableau pour représenter la grille. True si une mine est présente, sinon False.
     public boolean MatrixTable[][];
 
     // Variable pour le niveau de difficulté.
     private Levels level;

    /**
     * Constructeur pour initialiser la grille en fonction du niveau de difficulté
     *
     * @param levels
     */

    public Matrix(Levels levels) {
        MatrixTable = new boolean[DIMMatrix[levels.ordinal()][0]][DIMMatrix[levels.ordinal()][1]];
        nbMines = NBMINES[levels.ordinal()];
        level = levels;
    }

    /**
     * Place mines on the Matrix
     */
    public void placesMines() // Place mines on the Matrix
    {
        Random gene = new Random();
        System.out.println(nbMines);
        int compteur = nbMines;
        while (compteur != 0) {
            int posAleax = gene.nextInt(0, MatrixTable.length);
            int posAleay = gene.nextInt(0, MatrixTable[0].length);
            // Si la case n'a pas mine, placer une et -- sur le compteur.
            if (!MatrixTable[posAleax][posAleay]) {
                MatrixTable[posAleax][posAleay] = true;
                compteur--;
            }
        }

    }
    /*Création d'une partie en fonction du niv de difficulté */
    public void nouvellePartie(Levels levels){

        System.out.println(levels);
        // Réinitialise grille
        for (int i = 0; i< MatrixTable.length;i++) {
            for (int j = 0; j < MatrixTable[0].length; j++) {
                MatrixTable = new boolean[DIMMatrix[levels.ordinal()][0]][DIMMatrix[levels.ordinal()][1]];
            }
        }
        nbMines = NBMINES[levels.ordinal()];
        Random gene = new Random();
        // verif nb de mines
        System.out.println(nbMines);
        int compteur = nbMines;

        //Verif taille matrice
        System.out.println(MatrixTable.length);

        // Place mines aléatoirement sur la grille
        while (compteur != 0) {
            int posAleax = gene.nextInt(0, MatrixTable.length);
            int posAleay = gene.nextInt(0, MatrixTable[0].length);
            if (!MatrixTable[posAleax][posAleay]) {
                MatrixTable[posAleax][posAleay] = true;
                compteur--;
            }
        }


    }
    
   /**
 * Initialise une nouvelle partie du jeu de démineur.
 *
 * @param levels Le niveau de difficulté de la partie.
 * @param posx La coordonnée x de la case spécifique.
 * @param posy La coordonnée y de la case spécifique.
 */
public void nouvellePartie(Levels levels, int posx, int posy){

    // difficulté choisi.
    System.out.println(levels);

    // Réinitialise le tableau(false).
    for (int i = 0; i< MatrixTable.length;i++) {
        for (int j = 0; j < MatrixTable[0].length; j++) {
            MatrixTable = new boolean[DIMMatrix[levels.ordinal()][0]][DIMMatrix[levels.ordinal()][1]];
        }
    }

    // Initialise le nombre de mines.
    nbMines = NBMINES[levels.ordinal()];

    // Génère un nombre aléatoire.
    Random gene = new Random();

    // Affiche le nombre de mines.
    System.out.println(nbMines);

    // Initialise un compteur avec le nombre de mines.
    int compteur = nbMines;

    // Affiche la taille du tableau MatrixTable.
    System.out.println(MatrixTable.length);

    // Place une mine à la position spécifiée par posx et posy.
    MatrixTable[posx][posy] = false;

    // Tant qu'il reste des mines à placer...
    while (compteur != 0) {
        // Génère des coordonnées aléatoires pour placer une mine.
        int posAleax = gene.nextInt(0, MatrixTable.length);
        int posAleay = gene.nextInt(0, MatrixTable[0].length);

        // Affiche les tentatives de placement de mines.
        System.out.println("try");
        System.out.println(compteur);
        System.out.println(posAleax + " " + posAleay);

        // Si la case générée aléatoirement ne contient pas déjà une mine...
        if (MatrixTable[posAleax][posAleay] == false)
        {
            // Assure que la mine n'est pas placée sur la case spécifiée par posx et posy.
            if (!((posAleax == posx) && (posAleay == posy))) {
                // Place une mine à ces coordonnées.
                MatrixTable[posAleax][posAleay] = true;
                compteur--; // Décrémente le compteur de mines.
            }
        }
    }
}


    


    /**
     * Print the Matrix in the console
     */
    public void affText() {
        for (int la = 0; la < MatrixTable.length; la++) {
            for (int lo = 0; lo < MatrixTable[0].length; lo++) {
                if (MatrixTable[la][lo] == true) {
                    System.out.print("X");
                }
                else
                {
                    System.out.print(NbMinesAround(la, lo));
                }
            }
            System.out.println();
        }
    }

    /**
 * Récupère les dimensions de la matrice.
 * @return Un tableau contenant la longueur et la largeur de la matrice.
 */
public int[] getDim() {
    int[] dim = {MatrixTable.length, MatrixTable[1].length};
    return dim;
}

   /**
 * Récupère la valeur de la case aux coordonnées (x, y) de la matrice.
 * @param x Coordonnée x de la case.
 * @param y Coordonnée y de la case.
 * @return "X" si la case contient une mine, sinon renvoie le nombre de mines autour de cette case.
 */
public String getTable(int x, int y) {
    String c;
    if (MatrixTable[x][y] == true) {
        c = "X";
    } else {
        c = String.valueOf(NbMinesAround(x, y));
    }
    return c;
}

 /**
 * Récupère le nombre total de mines.
 * @return Le nombre de mines.
 */
public int getNbMines() {
    return nbMines;
}
/**
 * Récupère le niveau de difficulté actuel.
 * @return Le niveau de difficulté.
 */
public Levels getLevel() {
    return level;
}

/**
 * Définit le niveau de difficulté.
 * @param levels Le niveau de difficulté à définir.
 */
public void setLevel(Levels levels) {
    level = levels;
}
/**
 * Compte le nombre de mines autour de la case spécifiée par (posx, posy).
 * @param posx Coordonnée x de la case.
 * @param posy Coordonnée y de la case.
 * @return Le nombre de mines autour de la case.
 */
public int NbMinesAround(int posx, int posy) {
    int nb = 0;
    int borneInfx = posx == 0 ? 0 : posx - 1;
    int borneInfy = posy == 0 ? 0 : posy - 1;
    int borneSuppx = posx == MatrixTable.length - 1 ? MatrixTable.length - 1 : posx + 1;
    int borneSuppy = posy == MatrixTable[0].length - 1 ? MatrixTable[0].length - 1 : posy + 1;

    for (int la = borneInfx; la <= borneSuppx; la++) {
        for (int lo = borneInfy; lo <= borneSuppy; lo++) {
            if (MatrixTable[la][lo] == true) {
                nb += 1;
            }
        }
    }
    return nb;
}

}
