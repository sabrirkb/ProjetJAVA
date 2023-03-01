package jeu;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

public class GUI implements ActionListener
{
    private Jeu jeu;
    private JFrame fenetre;
    private JTextField entree;
    private JTextArea texte;
    private JLayeredPane layers = new JLayeredPane(); // Ajout des plans dans une layeredPane

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

    public void afficheImage( String nomImage) {
	   	URL imageURL = this.getClass().getClassLoader().getResource("jeu/images/" + nomImage);
	   	if( imageURL != null ) {
        	image1.setIcon( new ImageIcon( imageURL ));
        }
    }

    // Affiche les autres couches (Objets / Personnages / Gardes)
    public void afficheAutre( String nomAutre, int Plan, int x, int y) {
        URL imageURL = this.getClass().getClassLoader().getResource("jeu/images/" + nomAutre);
        if( imageURL != null ) {
            switch(Plan)
            {
                case 2 :
                    image2.setIcon( new ImageIcon( imageURL ));
                    image2.setBounds(x, y, 27, 32);
                    break;
                case 3:
                    image3.setIcon( new ImageIcon( imageURL ));
                    image3.setBounds(x, y, 27, 32);
                    break;
                case 4 :
                    image4.setIcon( new ImageIcon( imageURL ));
                    image4.setBounds(x, y, 27, 32);
                    break;
                case 5 :
                    image5.setIcon( new ImageIcon( imageURL ));
                    image5.setBounds(x, y, 27, 32);
                    break;
                case 6 :
                    image6.setIcon( new ImageIcon( imageURL ));
                    image6.setBounds(x, y, 27, 32);
                    break;
                case 7 :
                    image7.setIcon( new ImageIcon( imageURL ));
                    image7.setBounds(x, y, 27, 32);
                    break;
                case 8 :
                    image8.setIcon( new ImageIcon( imageURL ));
                    image8.setBounds(x, y, 27, 32);
                    break;
                case 9 :
                    image9.setIcon( new ImageIcon( imageURL ));
                    image9.setBounds(x, y, 27, 32);
                    break;
            }
        }
    }

    public void afficheJoueur() { 
        image10.removeAll();
    }

    public void afficheJoueur( String direction, int x, int y) {
        URL imageURL = null;
        if (direction == "NONE" || direction == "N")
        {
            imageURL = this.getClass().getClassLoader().getResource("jeu/images/sprites/null.png");
        }
        if (direction == "NORD" || direction == "N")
        {
            imageURL = this.getClass().getClassLoader().getResource("jeu/images/sprites/heros/monte.png");
        }
        if (direction == "SUD" || direction == "S")
        {
            imageURL = this.getClass().getClassLoader().getResource("jeu/images/sprites/heros/descend.png");
        }
        if (direction == "EST" || direction == "E")
        {
            imageURL = this.getClass().getClassLoader().getResource("jeu/images/sprites/heros/droite.png");
        }
        if (direction == "OUEST" || direction == "O")
        {
            imageURL = this.getClass().getClassLoader().getResource("jeu/images/sprites/heros/gauche.png");
        }
        if( imageURL != null ) {
            image10.setIcon( new ImageIcon( imageURL ));
            image10.setBounds(x, y, 27, 32);
        }
    }

    public void refreshLayers()
    {
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
        if ( ! ok )
            entree.getCaret().setBlinkRate(0);
    }

    private void creerGUI() throws FileNotFoundException, FontFormatException, IOException {
        // TITRE DU JEU A MODIFIER
        fenetre = new JFrame("Le titre de notre jeu");
        fenetre.setResizable(false);
        
        entree = new JTextField(34);

        texte = new JTextArea();
        texte.setEditable(false);
        texte.setBackground(Color.black);
        texte.setForeground(Color.white);
        texte.setLineWrap(true);
        texte.setWrapStyleWord(true);
        texte.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.BLACK));


        FileInputStream fontURL = new FileInputStream("src/jeu/fonts/TerminalVector.ttf");
        Font police = Font.createFont(Font.TRUETYPE_FONT, fontURL);
        float size = police.getSize() + 13f;
        texte.setFont(police.deriveFont(size));
        JScrollPane listScroller = new JScrollPane(texte);
        listScroller.setPreferredSize(new Dimension(200, 200));
        listScroller.setMinimumSize(new Dimension(100,100));
        listScroller.setBorder(BorderFactory.createMatteBorder(2, 0, 2, 0, Color.WHITE));

        layers.setVisible(true);
        layers.setOpaque(false);
        layers.setBackground(Color.black);
        layers.setLayout(new OverlayLayout(layers));
        
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

        layers.setPreferredSize(new Dimension(542, 416));

        panel.setOpaque(false);
        panel.setLayout(new BorderLayout());
        panel.add(layers, BorderLayout.NORTH); // Ajout de la layeredPane au Panel de la fenetre
        panel.add(listScroller, BorderLayout.CENTER);
        panel.add(entree, BorderLayout.SOUTH);
        panel.setBackground(Color.black);

        fenetre.getContentPane().add(panel, BorderLayout.CENTER);
        
        fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        entree.addActionListener(this);

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
        executerCommande();
    }

    private void executerCommande() {
        String commandeLue = entree.getText();
        entree.setText("");
        jeu.traiterCommande( commandeLue);
    }

    public void clearText()
    {
        texte.setText("");
    }
}