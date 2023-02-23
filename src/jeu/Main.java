package jeu;
public class Main {
	public static void main(String[] args) {
		Jeu jeu = new Jeu();
		GUI gui = new GUI( jeu);
		jeu.setGUI( gui);
	}
}
