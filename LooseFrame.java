import org.w3c.dom.Text;
/**
 * @author 2022
 * @version 8.0
 * Useless class
 */
import javax.swing.*;
import java.awt.*;

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

       // setVisible(true);  //on rend la frame visible
    }

}