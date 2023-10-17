/**
 * 2022
 * Class Field for demineur
 */
import java.util.Random;
public class Field {
    private static final int NBMINES[] = {3, 25, 35};
    private static final int DIMFIELD[][] = {{8,8}, {12,12}, {16, 16}};

    private static int nbMines = 0;
    public boolean fieldTable[][];

    private Levels level;

    /**
     * Constructor with lvl
     *
     * @param levels
     */

    public Field(Levels levels) {
        fieldTable = new boolean[DIMFIELD[levels.ordinal()][0]][DIMFIELD[levels.ordinal()][1]];
        nbMines = NBMINES[levels.ordinal()];
        level = levels;
    }

    /**
     * Place mines on the field
     */
    public void placesMines() // Place mines on the field
    {
        Random gene = new Random();
        System.out.println(nbMines);
        int compteur = nbMines;
        while (compteur != 0) {
            int posAleax = gene.nextInt(0, fieldTable.length);
            int posAleay = gene.nextInt(0, fieldTable[0].length);
            if (!fieldTable[posAleax][posAleay]) {
                fieldTable[posAleax][posAleay] = true;
                compteur--;
            }
        }

    }

    public void nouvellePartie(Levels levels){

        System.out.println(levels);
        for (int i = 0; i< fieldTable.length;i++) {
            for (int j = 0; j < fieldTable[0].length; j++) {
                fieldTable = new boolean[DIMFIELD[levels.ordinal()][0]][DIMFIELD[levels.ordinal()][1]];
            }
        }
        nbMines = NBMINES[levels.ordinal()];
        Random gene = new Random();
        System.out.println(nbMines);
        int compteur = nbMines;


        System.out.println(fieldTable.length);


        while (compteur != 0) {
            int posAleax = gene.nextInt(0, fieldTable.length);
            int posAleay = gene.nextInt(0, fieldTable[0].length);
            if (!fieldTable[posAleax][posAleay]) {
                fieldTable[posAleax][posAleay] = true;
                compteur--;
            }
        }


    }
    public void nouvellePartie(Levels levels, int posx, int posy){

        System.out.println(levels);
        for (int i = 0; i< fieldTable.length;i++) {
            for (int j = 0; j < fieldTable[0].length; j++) {
                fieldTable = new boolean[DIMFIELD[levels.ordinal()][0]][DIMFIELD[levels.ordinal()][1]];
            }
        }
        nbMines = NBMINES[levels.ordinal()];
        Random gene = new Random();
        System.out.println(nbMines);
        int compteur = nbMines;


        System.out.println(fieldTable.length);

        fieldTable[posx][posy] = false;
        while (compteur != 0) {
            int posAleax = gene.nextInt(0, fieldTable.length);
            int posAleay = gene.nextInt(0, fieldTable[0].length);
            System.out.println("try");
            System.out.println(compteur);
            System.out.println(posAleax + " " + posAleay);
            if (fieldTable[posAleax][posAleay] == false)
            {
                if (!((posAleax == posx) && (posAleay == posy))) {
                    fieldTable[posAleax][posAleay] = true;
                    compteur--;
                }
            }
        }


    }


    /**
     * Print the field
     */
    public void affText() {
        for (int la = 0; la < fieldTable.length; la++) {
            for (int lo = 0; lo < fieldTable[0].length; lo++) {
                if (fieldTable[la][lo] == true) {
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
     * Geter for dimension
     * @return (long,larg)
     */
    public int[] getDim()
    {
        int[] dim ={fieldTable.length,fieldTable[1].length};
        return(dim);
    }
    public String getTable(int x, int y)
    {
        String c;
        if (fieldTable[x][y] == true) {
            c = "X";
        }
        else
            c = String.valueOf(NbMinesAround(x, y));
        return(c);
    }

    public int getNbMines()
    {
        return(nbMines);
    }
    public Levels getLevel()
    {
        return(level);
    }

    public void setLevel(Levels levels)
    {
        level = levels;
    }

    /**
     * Count nb of mines around the case x;y
     * @param posx
     * @param posy
     * @return
     */
    public int NbMinesAround(int posx, int posy) {

        int nb = 0;
        int borneInfx = posx ==0 ? 0 : posx-1;
        int borneInfy = posy ==0 ? 0 : posy-1;
        int borneSuppx = posx == fieldTable.length-1 ? fieldTable.length-1 : posx+1;
        int borneSuppy = posy == fieldTable[0].length-1 ? fieldTable[0].length-1 : posy+1;


        for (int la = borneInfx ; la <= borneSuppx; la++) {
            for (int lo = borneInfy; lo <= borneSuppy; lo++) {
                    if ((fieldTable[la][lo] == true)) {
                        nb += 1;
                    }
                }
            }

        /**for (int la = posx-1; la < posx+1; la++) {
            for (int lo = posx-1; lo < posy+1; lo++) {
                if ((la >0) && (lo>0) && (la < fieldTable.length)&&(lo < fieldTable[0].length)) {
                    if ((fieldTable[lo][la] == true) && ((lo != 0) || (la != 0))) {
                        nb += 1;
                    }
                }
            }
        }*/
        return (nb);
    }

}
