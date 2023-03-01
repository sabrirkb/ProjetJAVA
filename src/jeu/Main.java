package jeu;

import java.awt.FontFormatException;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {
	public static void main(String[] args) throws InterruptedException, FileNotFoundException, FontFormatException, IOException {
		Jeu jeu = new Jeu();
		GUI gui = new GUI( jeu);
		jeu.setGUI( gui);
		jeu.StartTime(); // Démarre le timer qui permet de gérer le temps in-game
		gui.refreshLayers();
		gui.afficheJoueur("NONE", 0, 0);
	}
}
