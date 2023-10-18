import javax.swing.JFrame;


/**
 * Main class
 */
public class Main extends JFrame{
    private Matrix Matrix;
    private GUI gui;
    private boolean isMultiplayer = false;

    Main(Levels levels){
        Matrix = new Matrix(levels);
        Matrix.nouvellePartie(levels);
        
        setTitle("XMines");
        gui = new GUI(this);
        setContentPane(gui);
        pack();
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);


    }
    Matrix getChamp(){
        return(Matrix);
    }

    public void nouvellePartie(Levels levels){
        Matrix.nouvellePartie(levels);
        Matrix.setLevel(levels);
        Matrix.affText();

    }

    /**
     * New game with starting position
     * évite de tomber sur une mine au premier coup
     * */
    
    public void nouvellePartie(Levels levels, int posx, int posy){
        Matrix.nouvellePartie(levels,posx, posy);
        Matrix.setLevel(levels);
        Matrix.affText();
    }

    /**
     * launch a demin
     * @param args unused
     */
    public static void main(String[] args) {
        System.out.println("Xmines");
        new Main(Levels.MEDIUM) ;

    }

    public void setMultiplayer(boolean multiplayer) {
        isMultiplayer = multiplayer;
    }
    public boolean isMultiplayer() {
        return isMultiplayer;
    }

    public GUI getGui() {
        return gui;
    }
}