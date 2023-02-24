package jeu;

import java.util.Timer;
import java.util.TimerTask;

public class Jeu {
	
    public static GUI gui; 
	private Zone zoneCourante;
    private Horloge Temps = new Horloge();
    
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
            }
          }, 0, 700); // réduire 'period: 700' pour accélérer le temps in-game
    }

    // INITIALISATION
    public void setGUI( GUI g) { gui = g; afficherMessageDeBienvenue(); }
    private void creerCarte() {

        // CREE UN TABLEAU QUI CONTIENT LES ZONES ET CINEMATIQUES(*) DE NOTRE JEU
        // (*) : une cinématique se définit comme un enchaînement de zones, dont les messages et/ou
        // les images varient lorsque le joueur tape les actions [SUIVANT] / [OK]
        Zone [] zones = new Zone [15]; 


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
        zones[6] = new Zone("le refectoire (jour).", "/interieur/refectoire/refectoireJournee.png");
        zones[7] = new Zone("le refectoire (nuit).", "/interieur/refectoire/refectoireNuit.png");
        zones[8] = new Zone("le refectoire (heure du repas).", "/interieur/refectoire/refectoireRepas.png");


        // AJOUT DES SORTIES AUX ZONES DU JEU
        zones[1].ajouteSortie(Sortie.NORD, zones[3]);
        // zones[1].ajouteSortie(Sortie.SUD, zones[x]); 
        // Ajouter la sortie sud à la zone [1] UNIQUEMENT en fin de jeu
        // avec 'zones[x]' la zone affichant l'interface de fin du jeu
        zones[2].ajouteSortie(Sortie.NORD, zones[4]);
        // zones[2].ajouteSortie(Sortie.SUD, zones[x]); -> idem, mais le joueur s'enfuit la nuit


        // EXEMPLE D'UNE CINEMATIQUE
        // La cinématique débute lorsque le joueur arrive jusque la zone [9] :
        zones[9] = new Cinematique( "Vous êtes dans le réfectoire.\nVous assistez au combat qui vient d'éclater entre Bart et Marco."
                                   + "\nLes gardes viennent rapidement séparer les deux protagonistes.", 
                                "/interieur/refectoire/refectoireRepas.png");
        zones[10] = new Cinematique("Le repas suit son cours lorsqu'un détenu vous murmure :"
                                   + "\n« L'un des gardes a laissé une de ses clé sur le plan de travail de la cuisine."
                                   + "\n« Je m'en occuperais volontiers, mais je suis trop vieux pour tenter une évasion. »", 
                                "/interieur/refectoire/refectoireRepas.png");
        zones[9].ajouteAction(Action.SUIVANT, zones[10]); // Ajoute la scène zones[10] à la scène zones[9] (toujours créer les scènes AVANT de les lier)
        zones[10].ajouteAction(Action.OK, zones[8]); // Affiche la zones[8] en fin de cinématique
        zones[4].ajouteSortie(Sortie.EST, zones[9]); // Affecte le déclenchement de la cinématique à zones[4] 
                                                     // si le joueur prend la sortie 'EST'
        zones[8].ajouteSortie(Sortie.NORD, zones[4]);


        // ZONE AFFICHEE LORSQUE LE JOUEUR CREE UNE NOUVELLE PARTIE
        // Mettre la zone de depart sur zones[0] lorsque
        // l'interface du menu principal sera créée
        zoneCourante = zones[2]; // -> Pour l'instant, on se contentera de mettre zoneCourante exterieur prison


        // CREATION DES SPRITES DU JOUEUR
        String JoueurMonte = "/sprites/heros/monte.png";
        String JoueurDescend = "/sprites/heros/descend.png";
        String JoueurGauche = "/sprites/heros/gauche.png";
        String JoueurDroite = "/sprites/heros/droite.png";

        // CREATION DES SPRITES DES NPC


        // CREATION DES SPRITES DES OBJETS


        // INITIALISATION DU JEU
        // gui.afficheJoueur("NORD");
        // gui.afficheObjet(garde, SUD);
        // gui.afficheObjet(garde, SUD);

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
        gui.afficheJoueur("NORD");
    }
    
    public void traiterCommande(String commandeLue) {
    	gui.afficher( "\n > "+ commandeLue + "\n");
        switch (commandeLue.toUpperCase()) {
        case "?" : case "AIDE" : 
            afficherAide(); 
        	break;
        case "N" : case "NORD" :
        	allerEn( "NORD"); 
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
        case "Q" : case "QUITTER" :
        	terminer();
        	break;
        case "SUIVANT" :
        	nextScene(  "SUIVANT");
        	break;
        case "OK" :
        	nextScene( "OK");
        	break;
        case "TEMPS" : case "T" :
        	gui.afficher(Temps.getTime());
        	break;
       	default : 
            gui.afficher("Commande inconnue");
            break;
        }
    }

    private void afficherAide() {
        gui.afficher("Vous êtes perdu ?\n");
        gui.afficher(Temps.getTime());
        gui.afficher("Les commandes autorisées sont :");
        gui.afficher();
        gui.afficher(Commande.toutesLesDescriptions().toString());
        gui.afficher();
    }

    private void allerEn(String direction) {
    	Zone nouvelle = zoneCourante.obtientSortie( direction);
    	if ( nouvelle == null ) {
        	gui.afficher( "Pas de sortie " + direction);
    		gui.afficher();
    	}
        else {
        	zoneCourante = nouvelle;
            gui.afficher(Temps.getTime());
        	gui.afficher(zoneCourante.descriptionLongue());
        	gui.afficher();
        	gui.afficheImage(zoneCourante.nomImage());
            //if (zoneCourante == Jeu.zones[0])
            //{
                gui.afficheJoueur(direction); // affichage du joueur
            //}
        }
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
    	gui.afficher( "Au revoir...");
    	gui.enable( false);
    }
}
