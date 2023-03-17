package jeu;
import java.util.ArrayList;
import java.util.List;

public enum Commande {
	LOCALISER("L", "Localiser\t(L)  :  Indique votre position"),
	NORD("N/S", "Nord/Sud\t(N/S)  :  Aller à la sortie nord/sud"), 
	EST("E/O", "Est/Ouest\t(E/O)  :  Aller à la sortie est/ouest"),
	CARTE("C", "Map\t\t(M)  :  Afficher la carte"),  
	TEMPS("T", "Temps\t\t(T)  :  Afficher le temps"), 
	INVENTAIRE("I", "Inventaire\t(I)  :  Afficher l'inventaire"), 
	AIDE("?", "Aide\t\t(?)  :  Aide"), 
	PAUSE("P", "Pause\t\t(P)  :  Affiche le menu pause");

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
