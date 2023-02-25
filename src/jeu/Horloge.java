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
        return "Il est " + this.heure + "h0" + this.minutes + ".\n";
        if (this.heure < 10)
        return "Il est 0" + this.heure + "h" + this.minutes + ".\n";
        if (this.heure < 10 && this.minutes < 10)
        return "Il est 0" + this.heure + "h0" + this.minutes + ".\n";
        else
        return "Il est " + this.heure + "h" + this.minutes + ".\n";
    }
    
}
