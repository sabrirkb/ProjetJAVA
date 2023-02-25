package jeu;

import java.awt.FontFormatException;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {
	public static void main(String[] args) throws InterruptedException, FileNotFoundException, FontFormatException, IOException {
		Jeu jeu = new Jeu();
		GUI gui = new GUI( jeu);
		jeu.setGUI( gui);
		jeu.StartTime();
		gui.refreshLayers();
		gui.afficheJoueur("NORD", 258, 343); // Initialisation joueur (au cas ou le premier fail)
	}
}
