package jeu;
import java.util.HashMap;

public class Zone 
{
    protected String description;
    private String nomImage;
    protected HashMap<String,Zone> sorties;   

    public Zone(String description, String image) {
        this.description = description;
        nomImage = image;
        sorties = new HashMap<>();
    }

    public void ajouteSortie(Sortie sortie, Zone zoneVoisine) {
        sorties.put(sortie.name(), zoneVoisine);
    }

    // Permet d'ajouter les actions aux cinématiques
    public void ajouteAction(Action action, Zone sceneSuivante) {
        sorties.put(action.name(), sceneSuivante);
    }

    public String nomImage() {
        return nomImage;
    }
     
    public String toString() {
        return description;
    }

    public String descriptionLongue()  {
        return "Vous êtes dans " + description + "\nDirections disponibles : " + sorties();
    }

    private String sorties() {
        return sorties.keySet().toString();
    }

    public Zone obtientSortie(String direction) {
    	return sorties.get(direction);
    }
}

