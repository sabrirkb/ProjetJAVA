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
    private JLabel image;
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
        	image.setIcon( new ImageIcon( imageURL ));
            fenetre.pack();
        }
   }

    public void afficheObjet( String nomObjet) {
        URL imageURL = this.getClass().getClassLoader().getResource("jeu/images/" + nomObjet);
        if( imageURL != null ) {
            image.setIcon( new ImageIcon( imageURL ));
            fenetre.pack();
        }
    }

    public void afficheJoueur( String direction) {
        URL imageURL = null;
        JLabel imageJoueur = new JLabel();
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
            
            imageJoueur.setIcon( new ImageIcon( imageURL ));
            //panel.add(imageJoueur, BorderLayout.NORTH);
        }
    }

    public void enable(boolean ok) {
        entree.setEditable(ok);
        if ( ! ok )
            entree.getCaret().setBlinkRate(0);
    }

    private void creerGUI() throws FileNotFoundException, FontFormatException, IOException {
        // TITRE DU JEU A MODIFIER
        fenetre = new JFrame("Le titre de notre jeu");
        
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

        image = new JLabel();
        image.setBackground(Color.black);

        panel.setLayout(new BorderLayout());
        panel.add(image, BorderLayout.NORTH);
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