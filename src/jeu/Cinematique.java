package jeu;

public class Cinematique extends Zone
{
    public Cinematique(String description, String image) {
        super(description, image);
    }

    @Override
    public String descriptionLongue()  {
        return "" + super.description + "\n\nCommandes disponibles : " + actions();
    }

    private String actions() {
        return super.sorties.keySet().toString();
    } 

}