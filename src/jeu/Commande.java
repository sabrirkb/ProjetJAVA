package jeu;
import java.util.ArrayList;
import java.util.List;

public enum Commande {
	LOCALISER("L", "Localiser\t(L)  :  Indique votre position"),
	NORD("N", "Nord\t\t(N)  :  Aller à la sortie nord"), 
	SUD("S", "Sud\t\t(S)  :  Aller à la sortie sud"), 
	EST("E", "Est\t\t(E)  :  Aller à la sortie est"), 
	OUEST("O", "Ouest\t\t(O)  :  Aller à la sortie ouest"), 
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
