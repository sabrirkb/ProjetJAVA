package jeu;
import java.util.ArrayList;
import java.util.List;

public enum Commande {
	NORD("N", "N (aller à la sortie nord)"), 
	SUD("S", "S (aller à la sortie sud)"), 
	EST("E", "E (aller à la sortie est)"), 
	OUEST("O", "O (aller à la sortie ouest)"), 
	AIDE("?", "? (aide)"), 
	QUITTER("Q", "Q (quitter)");

	private String abreviation;
	private String description;
	private Commande(String c, String d ) { 
		abreviation = c;
		description = d; 
	}
	@Override
	public String toString() { 
		return name();
	}
	
	public static List<String> toutesLesDescriptions() { 
		ArrayList<String> resultat = new ArrayList<String>();
		for(Commande c : values()) {
			resultat.add( c.description);
		}
		return resultat;
	}
	
	public static List<String> toutesLesAbreviations() { 
		ArrayList<String> resultat = new ArrayList<String>();
		for(Commande c : values()) {
			resultat.add( c.abreviation);
		}
		return resultat;
	}
	
	public static List<String> tousLesNoms() { 
		ArrayList<String> resultat = new ArrayList<String>();
		for(Commande c : values()) {
			resultat.add( c.name());
		}
		return resultat;
	}

}
