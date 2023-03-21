package jeu;
import java.io.Serializable;
import java.util.HashMap;

public class Zone implements Serializable
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
        return "Vous êtes dans " + description + "\n\nCommandes disponibles : " + sorties();
    }
    
    private String sorties()
    {
        String result = "";
        try {
            for (String key : sorties.keySet())
            {
                result += key + " ";
            }
        } catch (Exception e) {
            result = "aucune commande disponible…";
        }
        return result;
    }

    public Zone obtientSortie(String direction) {
    	return sorties.get(direction);
    }

    public String commandesDispo()
    {
        return "Commandes disponibles : " + sorties();
    }
}

