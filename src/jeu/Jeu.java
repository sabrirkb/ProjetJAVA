package jeu;
public class Jeu {
	
    private GUI gui; 
	private Zone zoneCourante;
    
    public Jeu() {

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

    public void setGUI( GUI g) { gui = g; afficherMessageDeBienvenue(); }
    
    private void creerCarte() {
        Zone [] zones = new Zone [15];

        // EXEMPLES DE CREATION D'UNE NOUVELLE ZONE
        // zone[x] = new Zone("description de la zone", "src/image.png");
        // zones[0] = new Zone("le couloir", "Couloir.jpg" );
        // zones[1] = new Zone("l'escalier", "Escalier.jpg" );
        // zones[2] = new Zone("la grande salle", "GrandeSalle.jpg" );
        // zones[3] = new Zone("la salle à manger", "SalleAManger.jpg" );


        // EXEMPLES D'AJOUT D'UNE SORTIE DANS UNE ZONE EXISTANTE
        // zone[x].ajouteSortie(Sortie.DIRECTION, zone[y]);
        // zones[0].ajouteSortie(Sortie.EST, zones[1]);
        // zones[1].ajouteSortie(Sortie.OUEST, zones[0]);
        // zones[1].ajouteSortie(Sortie.EST, zones[2]);
        // zones[2].ajouteSortie(Sortie.OUEST, zones[1]);
        // zones[3].ajouteSortie(Sortie.NORD, zones[1]);
        // zones[1].ajouteSortie(Sortie.SUD, zones[3]);


        // EXEMPLE DE CHANGEMENT DE LA ZONE COURANTE (QUI S'AFFICHE SUR LE JEU)
        // zoneCourante = zones[1];   

        // zone[0] = new Zone(description: "menu principal", image: "/interface/menuPrincipal.png");
        zones[1] = new Zone("l'île de XXXX (jour).", "/exterieur/ile/ileJournee.png");
        zones[2] = new Zone("l'île de XXXX (nuit).", "/exterieur/ile/ileNuit.png");
        zones[3] = new Zone("cour (jour).", "/exterieur/cour/courJournee.png");
        zones[4] = new Zone("cour (nuit).", "/exterieur/cour/courNuit.png");
        zones[5] = new Zone("la cour (heure de promenade).", "/exterieur/cour/courPromenade.png");
        zones[6] = new Zone("le refectoire (jour).", "/interieur/refectoire/refectoireJournee.png");
        zones[7] = new Zone("le refectoire (nuit).", "/interieur/refectoire/refectoireNuit.png");
        zones[8] = new Zone("le refectoire (heure du repas).", "/interieur/refectoire/refectoireRepas.png");

        zones[1].ajouteSortie(Sortie.NORD, zones[3]);
        // zones[1].ajouteSortie(Sortie.SUD, zones[x]); -> ajouter la sortie sud UNIQUEMENT en fin de jeu
        // et mettre à jour zones[x] vers zone interface de fin du jeu

        zones[2].ajouteSortie(Sortie.NORD, zones[4]);
        // zones[2].ajouteSortie(Sortie.SUD, zones[x]); -> idem, mais le joueur s'enfuit la nuit

        // Mettre la zone de depart sur zones[0] lorsque
        // l'interface du menu principal sera créée
        zoneCourante = zones[2]; // -> Pour l'instant, on se contentera de mettre zoneCourante exterieur prison
    }

    private void afficherLocalisation() {
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
    }
    
    public void traiterCommande(String commandeLue) {
    	gui.afficher( "> "+ commandeLue + "\n");
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
       	default : 
            gui.afficher("Commande inconnue");
            break;
        }
    }

    private void afficherAide() {
        gui.afficher("Etes-vous perdu ?");
        gui.afficher();
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
        	gui.afficher(zoneCourante.descriptionLongue());
        	gui.afficher();
        	gui.afficheImage(zoneCourante.nomImage());
        }
    }
    
    private void terminer() {
    	gui.afficher( "Au revoir...");
    	gui.enable( false);
    }
}
