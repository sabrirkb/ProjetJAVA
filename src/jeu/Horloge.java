package jeu;

public class Horloge {
    
    private int heure = 0;
    private int minutes = 0;

    public Horloge() { 
        this.heure = 10;
        this.minutes = 30;
    }

    public void addTime()
    {
        this.minutes += 1; 
            if (this.minutes >= 60)
            {
                this.minutes = 0;
                this.heure += 1;
            }
            if (this.heure >= 23)
            {
                this.minutes = 0;
                this.heure = 0;
            }
            else {}
    }

    public String skip()
    {
        // Si on passe le temps entre 20h et 8h du matin, le personnage "dort" et se réveille
        // à huit heures du matin.
        if ((this.heure >= 20 && this.heure <=24) || (this.heure >= 0 && this.heure < 8))
        {
            this.heure = 8; // Heure du repas
            this.minutes = 0;
            return "Vous vous réveillez après avoir dormi quelques heures…\n";
        }
        if (this.heure >= 8 && this.heure < 10)
        {
            this.heure = 10; // Heure de promenade
            this.minutes = 0;
            return "Vous vous réveillez après avoir fait une sieste…\n";
        }
        if (this.heure >= 10 && this.heure < 12)
        {
            this.heure = 12; // Heure du repas
            this.minutes = 0;
            return "Vous vous réveillez après avoir fait une sieste…\n";
        }
        if (this.heure >= 12 && this.heure < 15)
        {
            this.heure = 15; // Heure de promenade
            this.minutes = 0;
            return "Vous vous réveillez après avoir fait une sieste…\n";
        }
        if (this.heure >= 15 && this.heure < 18)
        {
            this.heure = 18; // Heure de la douche
            this.minutes = 0;
            return "Vous vous réveillez après avoir fait une sieste…\n";
        }
        else
        {
            this.heure = 20; // Heure du repas
            this.minutes = 0;
            return "Vous vous réveillez après avoir fait une sieste…\n";
        }
    }

    public int getHeure()
    {
        return this.heure;
    }

    public int getMinutes()
    {
        return this.minutes;
    }

    public String getTime()
    {
        if (this.minutes < 10)
        return "Il est " + this.heure + "h0" + this.minutes + ".\n\n";
        if (this.heure < 10 && this.minutes >= 10)
        return "Il est 0" + this.heure + "h" + this.minutes + ".\n\n";
        if (this.heure < 10 && this.minutes < 10)
        return "Il est 0" + this.heure + "h0" + this.minutes + ".\n\n";
        else
        return "Il est " + this.heure + "h" + this.minutes + ".\n\n";
    }
    
}
