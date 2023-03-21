package jeu;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;

public class Save implements Serializable {

    ArrayList<Objets> Inventaire = new ArrayList<Objets>();
    boolean cinematiqueDeDepart;
    int heure;
    int minutes;
    boolean coffreOuvert;
    boolean celluleOuverte;
    boolean armeRecuperee;
    boolean indiceCodetenu;
    boolean battreMarco;
    boolean sceneBagarre;
    boolean nightTime = false;
    boolean activerAlerteNuit = true;
    boolean nightAlertOn = false;
    boolean carteTrouvee = false;
    int indexZoneCourante;
    int tentatives;
    int posXJoueur;
    int posYJoueur;
    URL directionJoueur;
    ArrayList<Integer> posX_autres = new ArrayList<Integer>();
    ArrayList<Integer> posY_autres = new ArrayList<Integer>();
    ArrayList<URL> URL_autres = new ArrayList<URL>();

    public Save() {
    }

    public Save(ArrayList<Objets> unInventaire, boolean uneCinematiqueDeDepart, int uneHeure, int desMinutes,
            boolean unCoffreOuvert, boolean uneCelluleOuverte, boolean uneArmeRecuperee, boolean unIndiceCodetenu,
            boolean bastonMarco,
            boolean uneSceneBagarre, boolean unNightTime, boolean setNightAlert, boolean alerteNuitActivee,
            boolean uneCarteTrouvee, int desTentatives,
            int unIndexZoneCourante, int unePosXJoueur, int unePosYJoueur, URL uneDirectionJoueur,
            ArrayList<Integer> desPosX_autres, ArrayList<Integer> desPosY_autres, ArrayList<URL> desURL_autres) {
        this.Inventaire = unInventaire;
        this.cinematiqueDeDepart = uneCinematiqueDeDepart;
        this.heure = uneHeure;
        this.minutes = desMinutes;
        this.coffreOuvert = unCoffreOuvert;
        this.celluleOuverte = uneCelluleOuverte;
        this.armeRecuperee = uneArmeRecuperee;
        this.indiceCodetenu = unIndiceCodetenu;
        this.battreMarco = bastonMarco;
        this.sceneBagarre = uneSceneBagarre;
        this.nightTime = unNightTime;
        this.activerAlerteNuit = setNightAlert;
        this.nightAlertOn = alerteNuitActivee;
        this.carteTrouvee = uneCarteTrouvee;
        this.tentatives = desTentatives;
        this.indexZoneCourante = unIndexZoneCourante;
        this.posXJoueur = unePosXJoueur;
        this.posYJoueur = unePosYJoueur;
        this.directionJoueur = uneDirectionJoueur;
        this.posX_autres = desPosX_autres;
        this.posY_autres = desPosY_autres;
        this.URL_autres = desURL_autres;
    }

    public void enregistrer() throws IOException {
        int newSave = 0;
        ArrayList<File> listOfFiles = new ArrayList<>();
        File directory = new File("src/jeu/saves/");
        if (directory.listFiles(this.getSaveFileFilter()) != null)
        {
            for (File f : directory.listFiles(this.getSaveFileFilter())) {
                listOfFiles.add(f);
            }
            if (listOfFiles.size() != 0) {
                newSave = listOfFiles.size();
            }
        } else { }
        newSave = newSave + 1;
        try
        {
            FileOutputStream fichierDeSauvegarde = new FileOutputStream("src/jeu/saves/" + newSave + ".dat");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fichierDeSauvegarde);
            objectOutputStream.writeObject(this);
            objectOutputStream.close();
        }
        catch (Exception e)
        {

        }

    }

    public File getLesSauvegardes() {
        return new File("src/jeu/saves/");
    }

    public Save recuperer(int index) throws ClassNotFoundException, IOException {

        Save sauvegarde = new Save();
        try
        {
            FileInputStream fichierDeSauvegarde = new FileInputStream("src/jeu/saves/" + index + ".dat");
            ObjectInputStream objectInputStream = new ObjectInputStream(fichierDeSauvegarde);
            sauvegarde = (Save) objectInputStream.readObject();
            return sauvegarde;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            return null;
        }
 
    }

    public FilenameFilter getSaveFileFilter()
    {
        FilenameFilter filtreFichierSave = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return !name.equals(".DS_Store");
            }
        };
        return filtreFichierSave;
    }


}
