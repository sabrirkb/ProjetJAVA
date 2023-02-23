package jeu;

public class Cinematique extends Zone
{
    public Cinematique(String description, String image) {
        super(description, image);
    }

    @Override
    public String descriptionLongue()  {
        return "" + super.description + "\nActions possibles : " + actions();
    }

    private String actions() {
        return super.sorties.keySet().toString();
    } 

}