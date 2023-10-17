import java.sql.SQLOutput;
import java.util.Random;
import javax.swing.*;


/**
 * 2022
 * Beginning of demineur
 * @version 8.0
 * @author Cyprien
 * @see <a href="http://www.laurent-freund.fr/cours">laurent-freund.fr/cours</a>
 */
public class Main extends JFrame{
    private Field field;
    private GUI gui;
    private boolean isMultiplayer = false;

    Main(Levels levels){
        field = new Field(levels);
        field.nouvellePartie(levels);
        //Field f2 = new Field(Levels.MEDIUM);
        setTitle("XMines");
        gui = new GUI(this);
        setContentPane(gui);
        pack();
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);


    }
    Field getChamp(){
        return(field);
    }

    public void nouvellePartie(Levels levels){
        field.nouvellePartie(levels);
        field.setLevel(levels);
        field.affText();

    }
    /**
     * New game with starting position
     * */
    public void nouvellePartie(Levels levels, int posx, int posy){
        field.nouvellePartie(levels,posx, posy);
        field.setLevel(levels);
        field.affText();
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