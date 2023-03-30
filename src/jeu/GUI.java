package jeu;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
// import com.apple.eawt.Application;       // Import nécessaire pour Mac (ancienne version -> n'est plus supporté)
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

public class GUI implements ActionListener {
    private Jeu jeu;
    private JFrame fenetre;
    private JTextField entree;
    private JTextArea txt_heure;
    private JTextArea texte;
    private JLayeredPane layers = new JLayeredPane(); // Ajout des plans dans une layeredPane

    // Application application = Application.getApplication(); // Nécessaire pour
    // set l'icone sur les Mac

    // Couches superposées qui composent l'image de notre jeu (calques)

    private JLabel image1 = new JLabel(); // Arrière Plan -> zones du jeu
    private JLabel image2 = new JLabel(); // Neuvieme Plan -> Personnage / Garde / Item / Rien
    private JLabel image3 = new JLabel(); // Huitieme Plan -> Personnage / Garde / Item / Rien
    private JLabel image4 = new JLabel(); // Septieme Plan -> Personnage / Garde / Item / Rien
    private JLabel image5 = new JLabel(); // Sixieme Plan -> Personnage / Garde / Item / Rien
    private JLabel image6 = new JLabel(); // Cinquieme Plan -> Personnage / Garde / Item / Rien
    private JLabel image7 = new JLabel(); // Quatrieme Plan -> Personnage / Garde / Item / Rien
    private JLabel image8 = new JLabel(); // Troisieme Plan -> Personnage / Garde / Item / Rien
    private JLabel image9 = new JLabel(); // Deuxieme Plan -> Personnage / Garde / Item / Rien
    private JLabel image10 = new JLabel(); // Premier Plan -> Celui du héros
    private JPanel panel = new JPanel();

    public GUI(Jeu j) throws FileNotFoundException, FontFormatException, IOException {
        jeu = j;
        creerGUI();

    }

    public void afficher(String s) {
        texte.append(s);
        texte.setCaretPosition(texte.getDocument().getLength());
    }

    public void afficher() {
        afficher("\n");
    }

    public void afficheImage(String nomImage) {
        URL imageURL = this.getClass().getClassLoader().getResource("jeu/images/" + nomImage);
        if (imageURL != null) {
            image1.setIcon(new ImageIcon(imageURL));
        }
    }

    // Affiche les autres couches (Objets / Personnages / Gardes)
    public void afficheAutre(String nomAutre, int Plan, int x, int y) {
        URL imageURL = this.getClass().getClassLoader().getResource("jeu/images/sprites/" + nomAutre + ".png");
        if (imageURL != null) {
            switch (Plan) {
                case 2:
                    image2.setIcon(new ImageIcon(imageURL));
                    image2.setBounds(x, y, 50, 50);
                    break;
                case 3:
                    image3.setIcon(new ImageIcon(imageURL));
                    image3.setBounds(x, y, 50, 50);
                    break;
                case 4:
                    image4.setIcon(new ImageIcon(imageURL));
                    image4.setBounds(x, y, 50, 50);
                    break;
                case 5:
                    image5.setIcon(new ImageIcon(imageURL));
                    image5.setBounds(x, y, 50, 50);
                    break;
                case 6:
                    image6.setIcon(new ImageIcon(imageURL));
                    image6.setBounds(x, y, 50, 50);
                    break;
                case 7:
                    image7.setIcon(new ImageIcon(imageURL));
                    image7.setBounds(x, y, 35, 32);
                    break;
                case 8:
                    image8.setIcon(new ImageIcon(imageURL));
                    image8.setBounds(x, y, 40, 70);
                    break;
                case 9:
                    image9.setIcon(new ImageIcon(imageURL));
                    image9.setBounds(x, y, 40, 70);
                    break;
            }
        }
    }

    public void enleveJoueur() {
        image10.removeAll();
    }

    public void afficheJoueur(URL URL, int x, int y) {
        URL imageURL = URL;
        if (imageURL != null) {
            image10.setIcon(new ImageIcon(imageURL));
            image10.setBounds(x, y, 27, 32);
        }
    }

    public void afficheJoueur(String direction, int x, int y) {
        URL imageURL = null;
        if (direction == "NONE") {
            imageURL = this.getClass().getClassLoader().getResource("jeu/images/sprites/null.png");
        }
        if (direction == "NORD" || direction == "N") {
            imageURL = this.getClass().getClassLoader().getResource("jeu/images/sprites/heros/monte.png");
        }
        if (direction == "SUD" || direction == "S") {
            imageURL = this.getClass().getClassLoader().getResource("jeu/images/sprites/heros/descend.png");
        }
        if (direction == "EST" || direction == "E") {
            imageURL = this.getClass().getClassLoader().getResource("jeu/images/sprites/heros/droite.png");
        }
        if (direction == "OUEST" || direction == "O") {
            imageURL = this.getClass().getClassLoader().getResource("jeu/images/sprites/heros/gauche.png");
        }
        if (imageURL != null) {
            image10.setIcon(new ImageIcon(imageURL));
            image10.setBounds(x, y, 27, 32);
        }
    }

    public void reafficherJoueur()
    {
        layers.remove(image10);
        layers.add(image10);
    }

    public void cacher(int Plan)
    {
        if (Plan == 1)
        {
            layers.remove(image1);
        }
        if (Plan == 2)
        {
            layers.remove(image2);
        }
        if (Plan == 3)
        {
            layers.remove(image3);
        }
        if (Plan == 4)
        {  
            layers.remove(image4);
        }
        if (Plan == 5)
        {
            layers.remove(image5);
        }
        if (Plan == 6)
        {
            layers.remove(image6);
        }
        if (Plan == 7)
        {
            layers.remove(image7);
        }
        if (Plan == 8)
        {
            layers.remove(image8);
        }
        if (Plan == 9)
        {
            layers.remove(image9);
        }
        if (Plan == 10)
        {
            layers.remove(image10);
        }
        
    }

    public void refreshLayers() {
        layers.removeAll();
        layers.add(image10, 1);
        layers.add(image9, 2);
        layers.add(image8, 3);
        layers.add(image7, 4);
        layers.add(image6, 5);
        layers.add(image5, 6);
        layers.add(image4, 7);
        layers.add(image3, 8);
        layers.add(image2, 9);
        layers.add(image1, 10);
    }

    public void enable(boolean ok) {
        entree.setEditable(ok);
        if (!ok)
            entree.getCaret().setBlinkRate(0);
    }

    public void afficheBarre() {
        txt_heure.setForeground(Color.white);
    }

    private void creerGUI() throws FileNotFoundException, FontFormatException, IOException {
        URL gameIcon = this.getClass().getClassLoader().getResource("jeu/images/icon.png");
        ImageIcon imgIcon = new ImageIcon(gameIcon); // Set l'icone sur les PC
        // Set l'icone sur les Mac
        // TITRE DU JEU (A MODIFIER SI NECESSAIRE)
        fenetre = new JFrame("The Escape of Percival : Mors Insula island");
        fenetre.setResizable(false);
        fenetre.setIconImage(imgIcon.getImage());

        entree = new JTextField(34);

        FileInputStream fontURL = new FileInputStream("src/jeu/fonts/TerminalVector.ttf");
        Font police = Font.createFont(Font.TRUETYPE_FONT, fontURL);
        float size = police.getSize() + 13f;

        txt_heure = new JTextArea();
        txt_heure.setEditable(false);
        txt_heure.setBackground(Color.black);
        txt_heure.setForeground(Color.black);
        txt_heure.setAlignmentX(Component.CENTER_ALIGNMENT);
        txt_heure.setFont(police.deriveFont(size));
        txt_heure.setText("00 : 00");
        txt_heure.setLineWrap(true);
        txt_heure.setWrapStyleWord(true);
        txt_heure.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.BLACK));
        txt_heure.setPreferredSize(new Dimension(542, 25));

        texte = new JTextArea();
        texte.setEditable(false);
        texte.setBackground(Color.black);
        texte.setForeground(Color.white);
        texte.setLineWrap(true);
        texte.setWrapStyleWord(true);
        texte.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.BLACK));
        texte.setFont(police.deriveFont(size));
        texte.setPreferredSize(new Dimension(542, 190));

        JScrollPane listScroller = new JScrollPane(texte);
        listScroller.setAutoscrolls(true);
        listScroller.setPreferredSize(new Dimension(542, 210));
        listScroller.setBorder(BorderFactory.createMatteBorder(2, 0, 2, 0, Color.WHITE));

        layers.setVisible(true);
        layers.setOpaque(false);
        layers.setBackground(Color.black);
        layers.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.WHITE));
        layers.setLayout(new OverlayLayout(layers));
        layers.setAlignmentX(Component.CENTER_ALIGNMENT);

        layers.add(image10, 1);
        layers.add(image9, 2);
        layers.add(image8, 3);
        layers.add(image7, 4);
        layers.add(image6, 5);
        layers.add(image5, 6);
        layers.add(image4, 7);
        layers.add(image3, 8);
        layers.add(image2, 9);
        layers.add(image1, 10);

        layers.setPreferredSize(new Dimension(542, 400));

        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(layers); // Ajout de la layeredPane au Panel de la fenetre
        panel.add(txt_heure);
        panel.add(listScroller);
        panel.add(entree);
        panel.setPreferredSize(new Dimension(542, 400));
        panel.setBackground(Color.black);

        fenetre.getContentPane().add(panel, BorderLayout.CENTER);

        fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        entree.addActionListener(this);

        fenetre.setPreferredSize(new Dimension(542, 685));
        fenetre.pack();
        fenetre.setVisible(true);
        fenetre.setBackground(Color.black);

        entree.requestFocus();
        entree.setBackground(Color.black);
        entree.setForeground(Color.white);
        entree.setFont(police.deriveFont(size));
        entree.setCaretColor(Color.WHITE);
        entree.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.BLACK));

    }

    public void actionPerformed(ActionEvent e) {
        try {
            executerCommande();
        } catch (UnsupportedAudioFileException e1) {
            e1.printStackTrace();
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (LineUnavailableException e1) {
            e1.printStackTrace();
        }
    }

    private void executerCommande()
            throws UnsupportedAudioFileException, InterruptedException, IOException, LineUnavailableException {
        String commandeLue = entree.getText();
        entree.setText("");
        jeu.traiterCommande(commandeLue);
    }

    public void updateTxtHeure(int heures, int minutes, int tentatives) {
        int lesAvertissements = tentatives - 3;

        if (lesAvertissements > 3)
        {
            lesAvertissements = 3;
        }

        if (tentatives > 0)
        {
            txt_heure.setForeground(Color.WHITE);
        }

        if (heures >= 10 && minutes < 10) {
            txt_heure.setText("\t| " + heures + " : 0" + minutes + " |\t\tAvertissement(s): " + (0 - (lesAvertissements))
                    + " / 3");
        }
        if (heures < 10 && minutes >= 10) {
            txt_heure.setText("\t| " + "0" + heures + " : " + minutes + " |\t\tAvertissement(s): "
                    + (0 - (lesAvertissements)) + " / 3");
        }
        if (heures < 10 && minutes < 10) {
            txt_heure.setText("\t| " + "0" + heures + " : 0" + minutes + " |\t\tAvertissement(s): "
                    + (0 - (lesAvertissements)) + " / 3");
        }
        if (heures >= 10 && minutes >= 10) {
            txt_heure.setText(
                    "\t| " + heures + " : " + minutes + " |\t\tAvertissement(s): " + (0 - (lesAvertissements)) + " / 3");
        }

        if (tentatives == 2)
        {
            txt_heure.setForeground(Color.YELLOW);
        }

        if (tentatives == 1)
        {
            txt_heure.setForeground(Color.ORANGE);
        }

        if (tentatives <= 0)
        {
            txt_heure.setForeground(Color.RED);
        }
    }

    public void clearText() {
        texte.setText("");
    }

    public void cacherBarre() {
        txt_heure.setForeground(Color.BLACK);
    }

    public int getPosXJoueur() {
        return image10.getX();
    }

    public int getPosYJoueur() {
        return image10.getY();
    }

    public void getURLJoueur() // CHANGER LE VOID EN URL
    {
        // URL URLJoueur = new URL("null");
        Icon joueurIcon = (ImageIcon) image10.getIcon();
        // URLJoueur = joueurIcon.getUrl(); -> TROUVER L'URL DE L'IMAGE
        // return URLJoueur;
    }

    public int getPosX_autre(int i) {
        int X = 0;
        if (i == 0) {
            X = image2.getX();
        }
        if (i == 1) {
            X = image3.getX();
        }
        if (i == 2) {
            X = image4.getX();
        }
        if (i == 3) {
            X = image5.getX();
        }
        if (i == 4) {
            X = image6.getX();
        }
        if (i == 5) {
            X = image7.getX();
        }
        if (i == 6) {
            X = image8.getX();
        }
        if (i == 7) {
            X = image9.getX();
        }
        return X;
    }

    public int getPosY_autre(int i) {
        int Y = 0;
        if (i == 0) {
            Y = image2.getY();
        }
        if (i == 1) {
            Y = image3.getY();
        }
        if (i == 2) {
            Y = image4.getY();
        }
        if (i == 3) {
            Y = image5.getY();
        }
        if (i == 4) {
            Y = image6.getY();
        }
        if (i == 5) {
            Y = image7.getY();
        }
        if (i == 6) {
            Y = image8.getY();
        }
        if (i == 7) {
            Y = image9.getY();
        }
        return Y;
    }

    public void getURL_autre(int i) {
        // URL URLAutre = new URL("null");

        if (i == 0) {
            Icon autreIcon = (ImageIcon) image2.getIcon();
            // URLAutre = autreIcon.getUrl(); -> TROUVER L'URL DE L'IMAGE
        }
        if (i == 1) {
            Icon autreIcon = (ImageIcon) image3.getIcon();
            // URLAutre = autreIcon.getUrl(); -> TROUVER L'URL DE L'IMAGE
        }
        if (i == 2) {
            Icon autreIcon = (ImageIcon) image4.getIcon();
            // URLAutre = autreIcon.getUrl(); -> TROUVER L'URL DE L'IMAGE
        }
        if (i == 3) {
            Icon autreIcon = (ImageIcon) image5.getIcon();
            // URLAutre = autreIcon.getUrl(); -> TROUVER L'URL DE L'IMAGE
        }
        if (i == 4) {
            Icon autreIcon = (ImageIcon) image6.getIcon();
            // URLAutre = autreIcon.getUrl(); -> TROUVER L'URL DE L'IMAGE
        }
        if (i == 5) {
            Icon autreIcon = (ImageIcon) image7.getIcon();
            // URLAutre = autreIcon.getUrl(); -> TROUVER L'URL DE L'IMAGE
        }
        if (i == 6) {
            Icon autreIcon = (ImageIcon) image8.getIcon();
            // URLAutre = autreIcon.getUrl(); -> TROUVER L'URL DE L'IMAGE
        }
        if (i == 7) {
            Icon autreIcon = (ImageIcon) image9.getIcon();
            // URLAutre = autreIcon.getUrl(); -> TROUVER L'URL DE L'IMAGE
        }
        // return URLAutre;
    }
}