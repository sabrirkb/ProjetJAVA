package jeu;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Jeu {
	
    public static GUI gui; 
	private Zone zoneCourante;
    private Horloge Temps = new Horloge();
    private List<Objets> Inventaire;
    private boolean coffreOuvert = false;
    private boolean celluleOuverte = false;
    private boolean indiceCodetenu = false;
    private boolean rdvMarco = false;
    private boolean sceneBagarre = false;
    private Zone [] zones;
    private Zone ancienneZone;
    
    public Jeu() throws InterruptedException {

        // --------------------------------
        // Inclure un menu de départ -> reprendre une sauvegarde / créer une nouvelle partie
        // La fonction init(); affichera l'interface du menu principal;
        // --------------------------------
        // Si le joueur décide de créer une nouvelle partie, on fera appel à la
        // fonction newGame() qui executera le code suivant :
        // 
            creerCarte(); // -> Inclure dans la fonction newGame()
            gui = null; // -> Inclure dans la fonction newGame()
        //
        // --------------------------------
    }

    // DÉMARRE LE TIMER QUI PERMET À L'HORLOGE DU JEU DE TOURNER
    public void StartTime() throws InterruptedException
    {
        Timer timer = new Timer();
		timer.schedule(new TimerTask() {
            @Override
            public void run() {
              Temps.addTime();
              checkJourNuit();
            }
          }, 0, 700); // réduire 'period: 700' pour accélérer le temps in-game
    }

    // INITIALISATION
    public void setGUI( GUI g) { gui = g; afficherMessageDeBienvenue(); }
    private void creerCarte() {

        // CRÉATION D'UN TABLEAU QUI CONTIENT LES ZONES ET CINÉMATIQUES(*) DE NOTRE JEU
        // (*) : une cinématique se définit comme un enchaînement de zones, dont les messages et/ou
        // les images varient lorsque le joueur tape les actions [SUIVANT] / [OK]

                zones = new Zone [50];  // Il n'y a pas réellement 50 zones,
                                        // les scènes variantes (jour/nuit) et cinématiques
                                        // sont également inclues dans le tableau.

        // EXEMPLES DE CREATION D'UNE NOUVELLE ZONE
        // zone[x] = new Zone("description de la zone", "src/image.png");
        // zones[0] = new Zone("le couloir", "Couloir.jpg" );
        // zones[1] = new Zone("l'escalier", "Escalier.jpg" );
        // zones[2] = new Zone("la grande salle", "GrandeSalle.jpg" );
        // zones[3] = new Zone("la salle à manger", "SalleAManger.jpg" );


        // EXEMPLES D'AJOUT D'UNE SORTIE DANS UNE ZONE EXISTANTE
        //
        //  Fonction :
        //      zone[x].ajouteSortie(Sortie.DIRECTION, zone[y]);
        //
        //  Avec zones[x] -> la zone qui contient la direction 'DIRECTION' (NORD/SUD/EST/OUEST)
        //  et zones[y] -> la zone vers laquelle pointe la direction
        //
        //  Exemples :
        //  zones[0].ajouteSortie(Sortie.EST, zones[1]);
        //    -> La zone [0] contient maintenant une sortie vers l'EST qui pointe vers la zone [1]
        //  zones[1].ajouteSortie(Sortie.OUEST, zones[0]);
        //    -> La zone [1] contient maintenant une sortie vers l'OUEST qui pointe vers la zone [0]


        // EXEMPLE DE CHANGEMENT DE LA ZONE COURANTE (QUI S'AFFICHE SUR LE JEU)
        // zoneCourante = zones[1];   


        // ZONES DE NOTRE JEU
        // zone[0] = new Zone(description: "menu principal", image: "/interface/menuPrincipal.png");
        zones[1] = new Zone("l'île de XXXX (jour).", "/exterieur/ile/ileJournee.png");
        zones[2] = new Zone("l'île de XXXX (nuit).", "/exterieur/ile/ileNuit.png");
        zones[3] = new Zone("cour (jour).", "/exterieur/cour/courJournee.png");
        zones[4] = new Zone("cour (nuit).", "/exterieur/cour/courNuit.png");
        zones[5] = new Zone("la cour (heure de promenade).", "/exterieur/cour/courPromenade.png");
        zones[6] = new Zone("le réfectoire (jour).", "/interieur/refectoire/refectoireJournee.png");
        zones[7] = new Zone("le réfectoire (nuit).", "/interieur/refectoire/refectoireNuit.png");
        zones[8] = new Zone("le réfectoire (heure du repas).", "/interieur/refectoire/refectoireRepas.png");
        // zones[11] = new Zone("le réfectoire (heure du repas).", "/interieur/refectoire/refectoireRepas.png");
        // zones[12] = new Zone("le réfectoire (heure du repas).", "/interieur/refectoire/refectoireRepas.png");
        // zones[13] = new Zone("le réfectoire (heure du repas).", "/interieur/refectoire/refectoireRepas.png");
        // zones[14] = new Zone("le réfectoire (heure du repas).", "/interieur/refectoire/refectoireRepas.png");
        // zones[15] = new Zone("le réfectoire (heure du repas).", "/interieur/refectoire/refectoireRepas.png");
        // zones[16] = new Zone("le réfectoire (heure du repas).", "/interieur/refectoire/refectoireRepas.png");
        // zones[17] = new Zone("le réfectoire (heure du repas).", "/interieur/refectoire/refectoireRepas.png");

        // AJOUT DES SORTIES AUX ZONES DU JEU
        zones[1].ajouteSortie(Sortie.NORD, zones[3]);
        // zones[1].ajouteSortie(Sortie.SUD, zones[x]); 
        // Ajouter la sortie sud à la zone [1] UNIQUEMENT en fin de jeu
        // avec 'zones[x]' la zone affichant l'interface de fin du jeu

        zones[2].ajouteSortie(Sortie.NORD, zones[4]);
        // zones[2].ajouteSortie(Sortie.SUD, zones[x]); -> idem, mais le joueur s'enfuit la nuit


        // EXEMPLE D'UNE CINEMATIQUE
        // La cinématique débute lorsque le joueur arrive jusque la zone [9] :
        zones[9] = new Cinematique( "Vous êtes dans le réfectoire.\nUne bagarre vient d'éclater entre Bart "
                                   + "et Marco… Après quelques minutes, les gardes viennent séparer les deux "
                                   + "protagonistes et les conduisent dans leurs cellules respectives.", 
                                "/interieur/refectoire/refectoireRepas.png");
        zones[10] = new Cinematique("Le repas suit son cours lorsqu'un détenu vous murmure :"
                                   + "\n« L'un des gardes a laissé une de ses clé sur le plan de travail de la cuisine."
                                   + " Je m'en occuperais volontiers, mais je suis trop vieux pour tenter une évasion. »", 
                                "/interieur/refectoire/refectoireRepas.png");
        zones[9].ajouteAction(Action.SUIVANT, zones[10]); // Ajoute la scène zones[10] à la scène zones[9] (toujours créer les scènes AVANT de les lier)
        zones[10].ajouteAction(Action.OK, zones[8]); // Affiche la zones[8] en fin de cinématique
        zones[3].ajouteSortie(Sortie.OUEST, zones[9]); // Affecte le déclenchement de la cinématique à zones[3] 
                                                       // si le joueur prend la sortie 'OUEST'

        
        zones[8].ajouteSortie(Sortie.NORD, zones[3]);

        zones[4].ajouteSortie(Sortie.OUEST, zones[7]);
        zones[7].ajouteSortie(Sortie.NORD, zones[4]);


        // ZONE AFFICHEE LORSQUE LE JOUEUR CREE UNE NOUVELLE PARTIE
        // Mettre la zone de depart sur zones[0] lorsque
        // l'interface du menu principal sera créée
        zoneCourante = zones[1]; // -> Pour l'instant, on se contentera de mettre zoneCourante exterieur prison

    }

    private void afficherLocalisation() {
            gui.afficher();
            gui.afficher(Temps.getTime());
            gui.afficher( zoneCourante.descriptionLongue());
            gui.afficher();
    }

    private void afficherMessageDeBienvenue() {
    	gui.afficher("Bienvenue !");
    	gui.afficher();
        gui.afficher("Tapez '?' pour obtenir de l'aide.");
        gui.afficher();
        afficherLocalisation();
        gui.afficheImage(zoneCourante.nomImage());
        gui.afficheJoueur("NORD", 258, 343); // Initialisation du joueur
    }
    
    public void traiterCommande(String commandeLue) {
        gui.clearText();
    	gui.afficher( "> "+ commandeLue + "\n\n");
        switch (commandeLue.toUpperCase()) {
        case "?" : case "AIDE" : case "HELP" :
            afficherAide(); 
        	break;
        case "N" : case "NORD" :
        	allerEn( "NORD"); 
        	break;
        case "D" : case "DIRECTION" : case "DIRECTIONS" :
        	afficherLocalisation(); 
        	break;
        case "S" : case "SUD" :
        	allerEn( "SUD"); 
        	break;
        case "E" : case "EST" :
        	allerEn( "EST"); 
        	break;
        case "O" : case "OUEST" :
        	allerEn( "OUEST"); 
        	break;
        case "Q" : case "QUITTER" : case "QUIT" :
            gui.afficher("Voulez-vous vraiment quitter ? \n[YES] - [NO]");
        	break;
        case "I" : case "INVENTAIRE" : case "INV" :
        	checkInventaire();
        	break;
        case "YES" :
        	terminer();
            break;
        case "NO" :
        	afficherLocalisation();
            break;
        case "COFFRE" :
        	ouvrirCoffre();
            break;
        case "SUIVANT" : case "SUIV" :
        	nextScene(  "SUIVANT");
        	break;
        case "OK" :
        	nextScene( "OK");
        	break;
        case "TEMPS" : case "T" : case "TIME" :
        	gui.afficher(Temps.getTime());
        	break;
       	default : 
            gui.clearText();
            gui.afficher("Commande inconnue.\nTapez '?' pour obtenir de l'aide.");
            break;
        }
    }

    private void ouvrirCoffre()
    {
        boolean clePossedee = false;
        if (Inventaire != null)
        {
            for (Objets objet : Inventaire)
            {
                if (objet == Objets.CLE1)
                {
                    clePossedee = true;
                } 
                else 
                { 
                    clePossedee = false;
                }
            }
        }
        if (clePossedee == true /*&& zoneCourante = zones[x]*/)
        {
            coffreOuvert = true;
            // zoneActuelle = zones[x]; -> Cinématique d'ouverture du coffre / récupération clé cellule
            // Ajouter CLE2 à l'inventaire en fin de cinématique (Inventaire.add(CLE2);)
        }
        else
        {
            gui.afficher("Vous n'êtes pas dans la salle des gardes ou ne possédez pas la clé du coffre.");
        }
    }

    public void checkInventaire()
    {
        gui.afficher("Voici le contenu de votre inventaire :\n");
        gui.afficher();
        if(this.Inventaire != null)
        {
            for (Objets objet : Inventaire)
            {
                gui.afficher(objet.name().toString());
            }
        }
        else
        {
            gui.afficher("Aucun objet\n");
        }
    }

    // Vérifie si l'heure actuelle doit changer la zone actuelle sur une scène de jour ou une scène de nuit
    private void checkJourNuit()
    {
        if (Temps.getHeure() < 20 && Temps.getHeure() >= 8)
        {
            if (zoneCourante == zones[2]) // Si la zone courante est une zone de type "jour" ,
            {
                zoneCourante = zones[1]; // on repasse zone courante à la même scène de type "nuit" .
                gui.afficheImage(zoneCourante.nomImage());
                gui.clearText();
                gui.afficher("Il fait maintenant jour.\n");
            }

            //  if (zoneCourante == zones[4])   // Faire de même pour les autres zones sauf les zones
            //  {                               // de cinématiques et d'événements (repas, bagarre, etc.)
            //    zoneCourante = zones[3];
            //  }
            // etc
        }
        else if (Temps.getHeure() >= 20 && Temps.getHeure() < 8)
        {
            if(zoneCourante == zones[1])
            {
                zoneCourante = zones[2];
                gui.afficheImage(zoneCourante.nomImage());
                gui.clearText();
                gui.afficher("Il fait maintenant nuit.\n");
            }
            //  if (zoneCourante == zones[3])
            //  {
            //    zoneCourante = zones[4];
            //  }
            // etc
        }
    }

    private void afficherAide() {
        gui.afficher("Vous êtes perdu? Les commandes autorisées sont : ");
        for( String str : Commande.toutesLesDescriptions() )
        {
            gui.afficher("\n" + str.toString());
        }
    }

    private void allerEn(String direction) {
    	Zone nouvelle = zoneCourante.obtientSortie( direction);
    	if ( nouvelle == null ) {
        	gui.afficher( "Il n'y a pas de sortie vers le " + direction + ".");
    		gui.afficher();
    	}
        else {
            ancienneZone = zoneCourante;
        	zoneCourante = nouvelle;
            gui.afficher(Temps.getTime());
        	gui.afficher(zoneCourante.descriptionLongue());
        	gui.afficher();
        	gui.afficheImage(zoneCourante.nomImage());
            updatePositionJoueur(direction);
        }
    }

    // Contient la liste des positions du joueur en fonction de sa
    // provenance, de sa direction, des événements ou cinématiques
    private void updatePositionJoueur(String direction)
    {
        if ((ancienneZone == zones[1] || ancienneZone == zones[2]) && direction == "NORD")
            {
                gui.afficheJoueur("NORD", 258, 325);
            }
        if ((ancienneZone == zones[3] || ancienneZone == zones[4]) && direction == "OUEST")
            {
                gui.afficheJoueur("SUD", 450, 50);
            }
        if ((ancienneZone == zones[8] || ancienneZone == zones[6] || ancienneZone == zones[7]) && direction == "NORD")
            {
                gui.afficheJoueur("EST", 100, 250);
            }
    // 
    //  etc...
    //
    
    }

    private void nextScene(String uneAction) {
    	Zone nouvelle = zoneCourante.obtientSortie( uneAction);
        zoneCourante = nouvelle;
        gui.afficher(Temps.getTime());
        gui.afficher(zoneCourante.descriptionLongue());
        gui.afficher();
        gui.afficheImage(zoneCourante.nomImage());
    }
    
    private void terminer() {
    	gui.afficher( "Fermeture du jeu...\nAu revoir!");
    	gui.enable( false);
        Timer chrono = new Timer();
        chrono.schedule(new TimerTask() {
            @Override
            public void run() {
              System.exit(0);
            }
          }, 3500, 1000);
    }
}
