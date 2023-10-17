import javax.sound.midi.Soundbank;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.sql.SQLOutput;

public class Case extends JPanel implements MouseListener {
    private int number = 0;

    private int posx;
    private int posy;
    private final static int DIM = 50;
    private boolean isDone = false;

    private Color colorrevealed = Color.lightGray;
    private Color colorhidden = Color.gray;

    private boolean isMine = false;
    private boolean isFlag = false;
    public boolean isRevealed = false;

    private GUI gui;

    private ImageIcon flag = new ImageIcon(new ImageIcon(getClass().getResource("./drapeau-bleu.png")).getImage().getScaledInstance(DIM, DIM, Image.SCALE_SMOOTH));
    private ImageIcon mine = new ImageIcon(new ImageIcon(getClass().getResource("./mine-rouge.png")).getImage().getScaledInstance(DIM, DIM, Image.SCALE_SMOOTH));

    public Case(int number, boolean isMine, GUI gui, int posx, int posy) {
        this.posx = posx;
        this.posy = posy;
        this.gui = gui;
        this.number = number;
        this.isMine = isMine;
        setPreferredSize(new Dimension(DIM, DIM)); // taille de la case
        addMouseListener(this); // ajout listener souris
    }

    @Override

    /**
     * paint the case
     * @param g
     */
    public void paintComponent(Graphics gc) {

        super.paintComponent(gc); // appel méthode mère (efface le dessin précedent)


        if (isRevealed == true) {
            gc.setColor(colorrevealed);
            gc.fillRect(0, 0, DIM, DIM);
            if (isMine == true) {
                gc.drawImage(mine.getImage(), 0, 0, this);
                System.out.println("MINES");
                //gc.setColor(Color.RED);
                // gc.fillRect(0, 0, DIM, DIM);
            } else {

                System.out.println("PASMINES");
                gc.setColor(colorrevealed);
                gc.fillRect(0, 0, DIM, DIM);
                gc.setColor(Color.BLACK);
                if (number != 0) {
                    gc.drawString(Integer.toString(number), 20, 30);
                }
            }


        } else if (isFlag == true) {
            gc.drawImage(flag.getImage(), 0, 0, this);
            //gc.setColor(Color.BLACK);
            // gc.fillRect(0, 0, DIM, DIM);
        }


        gc.setColor(new Color(23, 19, 19));
        gc.drawRect(0, 0, DIM, DIM);

    }

    /**
     * What happen on click
     */
    public void mousePressed(MouseEvent e) {
        if (gui.getmain().isMultiplayer()) {
            if (!gui.getisMultiplayerloose()) {
                if (isRevealed == false) {
                    if (SwingUtilities.isLeftMouseButton(e)) {

                        try {
                            gui.getOos().writeUTF("POS");
                            gui.getOos().writeInt(posx);
                            gui.getOos().writeInt(posy);

                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
            }
        } else {
            if (SwingUtilities.isLeftMouseButton(e)) {
                if (gui.isFirstClick) {
                    System.out.println("Premier clic");
                    gui.relancerPartie(gui.getmain().getChamp().getLevel(), posx, posy);
                    gui.isFirstClick = false;

                    gui.revealempty(posx, posy);
                    //gui.revealOneCase(posx, posy);

                    gui.lab[posx][posy].isRevealed = true;
                    repaint();

                    return;
                }
                System.out.println("Left click");

                repaint();
                if (!isFlag) {
                    if (isMine == true) {
                        System.out.println("BOOM");
                        gui.revealAll();
                        int reponse = JOptionPane.showConfirmDialog(null, // centre
                                "Do you want to play again ?", // blabla
                                "End Game", // titre
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.QUESTION_MESSAGE); // type d icône
                        if (reponse == JOptionPane.YES_OPTION) //traitement de la rep.
                        {
                            gui.relancerPartie(gui.getmain().getChamp().getLevel());
                            gui.isFirstClick = true;
                        } else if (reponse == JOptionPane.NO_OPTION) {
                            System.out.println("NO");
                        }
                    } else {
                        //gui.revealOneCase(posx, posy);
                        gui.revealempty(posx, posy);
                    }
                }

            }
            if (SwingUtilities.isRightMouseButton(e)) {

                if (isFlag) {
                    isFlag = false;
                    gui.setNbMinesLeft(gui.getNbMinesLeft() + 1);
                    gui.getNbMinesLabel().setText("Nombre de mines restantes : " + gui.getNbMinesLeft());
                    repaint();
                } else if (!isRevealed) {
                    if (gui.getNbMinesLeft() > 0) {
                        System.out.println("Right click");
                        isFlag = true;
                        gui.setNbMinesLeft(gui.getNbMinesLeft() - 1);
                        gui.getNbMinesLabel().setText("Nombre de mines restantes : " + gui.getNbMinesLeft());
                        repaint();
                    }
                }
            }
        }
    }

    /** getters
    and
    setters */
    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public boolean getisMine() {
        return isMine;
    }

    public void setisMine(boolean mine) {
        isMine = mine;
    }

    public void setColorrevealed(Color colorrevealed) {
        this.colorrevealed = colorrevealed;
    }

    public int getPosx() {
        return posx;
    }

    public int getPosy() {
        return posy;
    }

    public void mouseClicked(MouseEvent e) {

    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public boolean getIsDone() {
        return isDone;
    }

    public void setIsDone() {
        this.isDone = true;
    }

  /*  public boolean isgameover() {
        for (int i = 0; i < gui.getmain().getChamp().getDim()[0]; i++) {
            for (int j = 0; j < gui.getmain().getChamp().getDim()[1]; j++) {
                if (gui.lab[i][j].getisMine() == false && gui.lab[i][j].isRevealed == false) {
                    return false;
                }
            }
        }
        return true;
    }
*/

}
