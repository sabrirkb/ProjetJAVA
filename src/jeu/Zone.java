package jeu;
import java.util.HashMap;

public class Zone 
{
    private String description;
    private String nomImage;
    private HashMap<String,Zone> sorties;   

    public Zone(String description, String image) {
        this.description = description;
        nomImage = image;
        sorties = new HashMap<>();
    }

    public void ajouteSortie(Sortie sortie, Zone zoneVoisine) {
        sorties.put(sortie.name(), zoneVoisine);
    }

    public String nomImage() {
        return nomImage;
    }
     
    public String toString() {
        return description;
    }

    public String descriptionLongue()  {
        return "Vous êtes dans " + description + "\nSorties : " + sorties();
    }

    private String sorties() {
        return sorties.keySet().toString();
    }

    public Zone obtientSortie(String direction) {
    	return sorties.get(direction);
    }
}

