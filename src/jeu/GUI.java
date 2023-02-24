package jeu;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;

public class GUI implements ActionListener
{
    private Jeu jeu;
    private JFrame fenetre;
    private JTextField entree;
    private JTextArea texte;
    private JLabel image;

    public GUI(Jeu j) {
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
            //JLabel imageJoueur = new JLabel();
            //imageJoueur.setIcon( new ImageIcon( imageURL ));
            //JPanel panel2 = new JPanel();
            //panel2.add(imageJoueur, BorderLayout.NORTH);
            //fenetre.getContentPane().add(panel2, BorderLayout.CENTER);
            //fenetre.pack();
        }
    }

    public void enable(boolean ok) {
        entree.setEditable(ok);
        if ( ! ok )
            entree.getCaret().setBlinkRate(0);
    }

    private void creerGUI() {
        // TITRE DU JEU A MODIFIER
        fenetre = new JFrame("Le titre de notre jeu");
        
        entree = new JTextField(34);

        texte = new JTextArea();
        texte.setEditable(false);
        JScrollPane listScroller = new JScrollPane(texte);
        listScroller.setPreferredSize(new Dimension(200, 200));
        listScroller.setMinimumSize(new Dimension(100,100));

        JPanel panel = new JPanel();
        image = new JLabel();

        panel.setLayout(new BorderLayout());
        panel.add(image, BorderLayout.NORTH);
        panel.add(listScroller, BorderLayout.CENTER);
        panel.add(entree, BorderLayout.SOUTH);

        fenetre.getContentPane().add(panel, BorderLayout.CENTER);
        
        fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        entree.addActionListener(this);

        fenetre.pack();
        fenetre.setVisible(true);
        entree.requestFocus();
    }

    public void actionPerformed(ActionEvent e) {
        executerCommande();
    }

    private void executerCommande() {
        String commandeLue = entree.getText();
        entree.setText("");
        jeu.traiterCommande( commandeLue);
    }
}