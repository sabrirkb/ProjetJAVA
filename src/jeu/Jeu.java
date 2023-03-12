package jeu;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Jeu {

    public static GUI gui;
    private Zone zoneCourante;
    private Horloge Temps = new Horloge();
    private List<Objets> Inventaire;
    private boolean debutJeu = true;
    private boolean coffreOuvert = false;
    private boolean celluleOuverte = false;
    private boolean armeRecuperee = false;
    private boolean indiceCodetenu = false;
    private boolean battreMarco = false;
    private boolean sceneBagarre = false;
    private boolean quitMenu = false;
    private boolean mainMenu = false;
    private Zone[] zones;
    private Zone ancienneZone;
    private int tentatives = 3;

    public Jeu() throws InterruptedException {

        // --------------------------------
        // Inclure un menu de départ -> reprendre une sauvegarde / créer une nouvelle
        // partie
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
    public void StartTime() throws InterruptedException {
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
    public void setGUI(GUI g) {
        gui = g;
        afficherLocalisation();
        gui.afficheImage(zoneCourante.nomImage());
    }

    private void creerCarte() {

        // CRÉATION D'UN TABLEAU QUI CONTIENT LES ZONES ET CINÉMATIQUES(*) DE NOTRE JEU
        // (*) : une cinématique se définit comme un enchaînement de zones, dont les
        // messages et/ou
        // les images varient lorsque le joueur tape les actions [SUIVANT] / [OK]

        zones = new Zone[50]; // Il n'y a pas réellement 50 "zones", on choisit un tableau
                              // large car les scènes variantes (jour/nuit) et cinématiques
                              // sont également inclues dans ce tableau.

        // EXEMPLES DE CREATION D'UNE NOUVELLE ZONE
        // zone[x] = new Zone("description de la zone", "src/image.png");
        // zones[0] = new Zone("le couloir", "Couloir.jpg" );
        // zones[1] = new Zone("l'escalier", "Escalier.jpg" );
        // zones[2] = new Zone("la grande salle", "GrandeSalle.jpg" );
        // zones[3] = new Zone("la salle à manger", "SalleAManger.jpg" );

        // EXEMPLES D'AJOUT D'UNE SORTIE DANS UNE ZONE EXISTANTE
        //
        // Fonction :
        // zone[x].ajouteSortie(Sortie.DIRECTION, zone[y]);
        //
        // Avec zones[x] -> la zone qui contient la direction 'DIRECTION'
        // (NORD/SUD/EST/OUEST)
        // et zones[y] -> la zone vers laquelle pointe la direction
        //
        // Exemples :
        // zones[0].ajouteSortie(Sortie.EST, zones[1]);
        // -> La zone [0] contient maintenant une sortie vers l'EST qui pointe vers la
        // zone [1]
        // zones[1].ajouteSortie(Sortie.OUEST, zones[0]);
        // -> La zone [1] contient maintenant une sortie vers l'OUEST qui pointe vers la
        // zone [0]

        // EXEMPLE DE CHANGEMENT DE LA ZONE COURANTE (QUI S'AFFICHE SUR LE JEU)
        // zoneCourante = zones[1];

        // ZONES DE NOTRE JEU

        // AJOUT DES SORTIES AUX ZONES DU JEU
        // zones[1].ajouteSortie(Sortie.NORD, zones[3]);
        // zones[1].ajouteSortie(Sortie.SUD, zones[x]);
        // Ajouter la sortie sud à la zone [1] UNIQUEMENT en fin de jeu
        // avec 'zones[x]' la zone affichant l'interface de fin du jeu

        // zones[2].ajouteSortie(Sortie.NORD, zones[4]);
        // zones[2].ajouteSortie(Sortie.SUD, zones[x]); -> idem, mais le joueur s'enfuit
        // la nuit

        // zones[0] = new Zone("le menu principal.", "/interface/menuPrincipal.png");
        // zones[0].ajouteAction(Action.NOUVEAU, zones[11]);
        // zones[0].ajouteAction(Action.REPRENDRE, zones[10]);
        // zones[0].ajouteAction(Action.QUITTER, zones[10]);

        zones[1] = new Zone("l'île de Mors Insula (jour).", "/exterieur/ile/ileJournee.png");

        zones[2] = new Zone("l'île de Mors Insula (nuit).", "/exterieur/ile/ileNuit.png");

        zones[3] = new Zone("cour (jour).", "/exterieur/cour/courJournee.png");

        zones[4] = new Zone("cour (nuit).", "/exterieur/cour/courNuit.png");

        zones[5] = new Zone("la cour (heure de promenade).", "/exterieur/cour/courPromenade.png");

        zones[6] = new Zone("le réfectoire (jour).", "/interieur/refectoire/refectoireJournee.png");

        zones[7] = new Zone("le réfectoire (nuit).", "/interieur/refectoire/refectoireNuit.png");

        zones[8] = new Zone("le réfectoire (heure du repas).", "/interieur/refectoire/refectoireRepas.png");

        // EXEMPLE D'UNE CINEMATIQUE
        // La cinématique débute lorsque le joueur arrive jusque la zone [9] :
        zones[9] = new Cinematique("Vous êtes dans le réfectoire.\nUne bagarre vient d'éclater entre Bart "
                + "et Marco… Après quelques minutes, les gardes viennent séparer les deux "
                + "protagonistes et les conduisent dans leurs cellules respectives.",
                "/interieur/refectoire/refectoireRepas.png");

        zones[10] = new Cinematique("Le repas suit son cours lorsqu'un détenu vous murmure :"
                + "\n« J'ai vu un garde oublier l'une de ses clés sur le plan de travail de la cuisine."
                + " Je m'en occuperais volontiers, mais je suis trop vieux pour tenter une évasion… »",
                "/interieur/refectoire/refectoireRepas.png");

        /////////////// CINEMATIQUE DE DEPART ///////////////
        zones[11] = new Cinematique("Percival Lebrave était un Noble chevalier du royaume d'Aeternum. Un matin, alors "
                + "qu'il venait de se lever, il se fit arrêter par la garde royale, qui l'accusa à tort "
                + "d'avoir volé le trésor du Roi et le condamna à purger sa peine sur une île tristement "
                + "renommée : Mors Insula.", "/cinematiques/arrestation.png");
        //
        zones[12] = new Cinematique("Durant le voyage jusqu'à l'île, il entend les gardes discuter  "
                + "à propos du sort qui lui est réservé. Il comprend rapidement "
                + "qu'il va devoir trouver le moyen de s'échapper s'il ne veut "
                + "pas finir sa vie à croupir derrière les barreaux… ", "/cinematiques/traversee.png");
        //
        zones[13] = new Cinematique("Après quelques heures de voyage, le bateau accoste sur l'île. "
                + "Percival est brièvement escorté par la garde royale jusqu'à sa cellule.",
                "/exterieur/ile/ileJournee.png");
        //
        zones[14] = new Cinematique("Garde royale: « Voici la cour. C'est ici que tu pourras passeras du temps "
                + "durant tes heures de promenade. »", "/exterieur/cour/courPromenade.png");
        //
        zones[15] = new Cinematique("Garde royale: « Et voilà ta cellule, petit voleur. J'espère que t'as prévu "
                + "de quoi t'occuper… Tu risques de trouver le temps long ici, haha! "
                + "T'en fais pas, on reviendra s'occuper de toi bientôt. »", "/interieur/cellule/celluleJour.png");
        /////////////////////////////////////////////////////

        zones[16] = new Zone("votre cellule (jour). Vous devez attendre que les gardes vous autorisent à sortir…",
                "/interieur/cellule/celluleJour.png"); // Zone cellule porte fermée (jour)

        zones[17] = new Zone(
                "votre cellule. C'est l'heure de la promenade. Vous êtes libre de vous rendre ou non dans la cour.",
                "/interieur/cellule/celluleJour.png"); // Zone cellule porte ouverte

        zones[18] = new Zone(
                "votre cellule. C'est l'heure du repas. Vous êtes libre de vous rendre ou non au réfectoire.",
                "/interieur/cellule/celluleJour.png"); // Zone cellule porte ouverte

        zones[19] = new Zone(
                "votre cellule. C'est l'heure de la douche. Vous êtes escorté par les gardes jusque dans les douches.",
                "/interieur/cellule/celluleJour.png"); // Zone cellule porte ouverte

        zones[20] = new Zone("le couloir (jour).", "/interieur/couloir/couloirJour.png");

        zones[21] = new Zone("le couloir (nuit).", "/interieur/couloir/couloirNuit.png");

        zones[22] = new Zone("votre cellule (nuit). Vous devez attendre que les gardes vous autorisent à sortir…",
                "/interieur/cellule/celluleNuit.png"); // Zone cellule porte fermée (nuit)

        zones[23] = new Zone("les douches (jour).",
                "/interieur/douches/douchesJour.png");

        zones[24] = new Zone("les douches (nuit).",
                "/interieur/douches/douchesNuit.png");

        zones[25] = new Cinematique(
                "Vous arrivez dans les douches. Vous vous dirigez vers l'un des seaux disponibles et commencez à vous laver.",
                "/interieur/douches/heureDeLaDouche.png");

        zones[26] = new Zone(
                "la salle des gardes (jour). Vous devriez sortir d'ici avant que quelqu'un ne vous voit!",
                "/interieur/salleDesGardes/salleGardesJour.png");

        zones[27] = new Zone(
                "la salle des gardes (nuit). Vous devriez sortir d'ici avant de réveiller les gardes!",
                "/interieur/salleDesGardes/salleGardesNuit.png");

        zones[28] = new Cinematique("Vous avez été repéré par la garde royale! Ces derniers vous reconduisent de force"
                + "dans votre cellule après vous avoir donné un avertissement…\n\nAttention: il vous reste "
                + tentatives
                + " avertissement(s) avant de vous faire exécuter!",
                "/interieur/cellule/celluleJour.png");

        zones[29] = new Zone("la cuisine (jour).", "/interieur/cuisine/cuisineJour.png");

        zones[30] = new Zone("la cuisine (nuit).", "/interieur/cuisine/cuisineNuit.png");

        zones[31] = new Cinematique(
                "Vous terminez de vous laver et êtes reconduit par les gardes dans vos cellules respectives.",
                "/interieur/douches/heureDeLaDouche.png");

        zones[32] = new Cinematique(
                "Vous arrivez dans les douches. Vous vous dirigez vers l'un des seaux disponibles et commencez à vous laver,"
                        + "\nlorsqu'un mystérieux personnage se dirige vers vous…",
                "/interieur/douches/heureDeLaDouche.png");

        zones[33] = new Cinematique(
                "Marco : « Salut le nouveau… La forme? Moi, c'est Marco. J'espère qu'on va bien s'entendre toi et moi, "
                    + "sinon tu vas vraiment regretter ton séjour sur cette île, haha! »",
                    "/interieur/douches/heureDeLaDouche.png");

        // zones[3].ajouteSortie(Sortie.OUEST, zones[9]); // Affecte le déclenchement de
        // la cinématique à zones[3]
        // si le joueur prend la sortie 'OUEST'

        // AFFECTATION DE LA ZONE COURANTE - DEBUT DU JEU -> ZONE COURANTE = CINEMATIQUE
        // DE DEPART (pour l'instant zones[11])
        zoneCourante = zones[11];

        // AJOUT DES SORTIES ET DES COMMANDES AUX ZONES/CINEMATIQUES DU JEU
        zones[1].ajouteSortie(Sortie.NORD, zones[3]);

        zones[2].ajouteSortie(Sortie.NORD, zones[4]);

        zones[3].ajouteSortie(Sortie.NORD, zones[20]);
        zones[3].ajouteSortie(Sortie.OUEST, zones[6]);
        zones[3].ajouteSortie(Sortie.EST, zones[26]);

        zones[4].ajouteSortie(Sortie.OUEST, zones[7]);

        zones[6].ajouteSortie(Sortie.NORD, zones[3]);
        zones[6].ajouteSortie(Sortie.OUEST, zones[29]);

        zones[7].ajouteSortie(Sortie.NORD, zones[4]);

        zones[8].ajouteSortie(Sortie.NORD, zones[3]);

        zones[9].ajouteAction(Action.SUIVANT, zones[10]); // Ajoute la scène zones[10] à la scène zones[9] (toujours
        // créer les scènes AVANT de les lier)

        zones[10].ajouteAction(Action.OK, zones[8]); // Affiche la zones[8] en fin de cinématique

        zones[11].ajouteAction(Action.OK, zones[12]);

        zones[12].ajouteAction(Action.OK, zones[13]);

        zones[13].ajouteAction(Action.OK, zones[14]);

        zones[14].ajouteAction(Action.OK, zones[15]);

        zones[15].ajouteAction(Action.OK, zones[16]);

        zones[16].ajouteAction(Action.DORMIR, zones[16]);

        zones[17].ajouteSortie(Sortie.SUD, zones[20]); // Si heure promenade -> Sortie SUD (couloir) autorisée (jour)
        zones[17].ajouteAction(Action.DORMIR, zones[16]);

        zones[18].ajouteAction(Action.DORMIR, zones[16]);
        zones[18].ajouteSortie(Sortie.SUD, zones[20]); // Si heure repas -> Sortie SUD (couloir) autorisée (jour)

        zones[19].ajouteAction(Action.OK, zones[25]); // Si heures douche -> [OK] dirige vers douches (jour)

        zones[20].ajouteSortie(Sortie.EST, zones[3]);
        zones[20].ajouteSortie(Sortie.OUEST, zones[23]);
        zones[20].ajouteSortie(Sortie.NORD, zones[16]);

        zones[21].ajouteSortie(Sortie.SUD, zones[20]);

        zones[22].ajouteAction(Action.DORMIR, zones[16]);

        zones[23].ajouteSortie(Sortie.SUD, zones[20]);

        zones[25].ajouteAction(Action.OK, zones[31]);

        zones[26].ajouteSortie(Sortie.OUEST, zones[3]);

        zones[29].ajouteSortie(Sortie.EST, zones[6]);

        zones[31].ajouteAction(Action.OK, zones[16]);

    }

    private void afficherLocalisation() {

        gui.afficher(zoneCourante.descriptionLongue());
        gui.afficher();
    }

    private void afficherMessageDeBienvenue() {
        gui.afficher("Bienvenue sur la prison de Mors Insula, cher Percival!");
        gui.afficher();
        gui.afficher("Tapez '?' pour obtenir de l'aide à tout moment.\n");
        gui.afficher();
        gui.afficheImage(zoneCourante.nomImage());
    }

    public void traiterCommande(String commandeLue) {
        gui.clearText();
        gui.afficher("> " + commandeLue + "\n\n");
        switch (commandeLue.toUpperCase()) {
            case "?":
            case "AIDE":
            case "HELP":
                afficherAide();
                break;
            case "N":
            case "NORD":
                allerEn("NORD");
                break;
            case "L":
            case "LOCALISATION":
            case "LOCALISATIONS":
            case "LOCALISER":
            case "LOC":
            case "LOCAL":
                afficherLocalisation();
                break;
            case "S":
            case "SUD":
                allerEn("SUD");
                break;
            case "E":
            case "EST":
                allerEn("EST");
                break;
            case "O":
            case "OUEST":
                allerEn("OUEST");
                break;
            case "Q":
            case "QUITTER":
            case "QUIT":
                quitMenu = true;
                gui.afficher("Voulez-vous vraiment quitter ? \n[YES] - [NO]");
                break;
            case "I":
            case "INVENTAIRE":
            case "INV":
                checkInventaire();
                break;
            case "YES":
                if (quitMenu) {
                    terminer();
                } else {
                    gui.clearText();
                    gui.afficher("Commande inconnue.\nTapez '?' pour obtenir de l'aide.");
                }
                break;
            case "NO":
                if (quitMenu) {
                    quitMenu = false;
                    afficherLocalisation();
                } else {
                    gui.clearText();
                    gui.afficher("Commande inconnue.\nTapez '?' pour obtenir de l'aide.");
                }
                break;
            case "COFFRE":
                ouvrirCoffre();
                break;
            case "SUIVANT":
            case "SUIV":
                nextScene("SUIVANT");
                break;
            case "OK":
                nextScene("OK");
                break;
            case "DORMIR":
                if( (zoneCourante == zones[16] ) || (zoneCourante == zones[17] ) || (zoneCourante == zones[18] ) )
                {
                    dormir();
                    gui.afficher("\n" + zoneCourante.descriptionLongue());
                }
                else
                {
                    gui.afficher("Impossible de dormir pour le moment.\n\nTapez 'Localiser' pour obtenir les commandes disponibles.\n");
                }
                break;
            case "TEMPS":
            case "T":
            case "TIME":
                gui.afficher(Temps.getTime());
                gui.afficher("Tapez '?' pour obtenir de l'aide à tout moment.\n");
                break;
            case "NOUVEAU":
                if (zoneCourante == zones[0]) {
                    zoneCourante = zones[11];
                } else {
                    gui.clearText();
                    gui.afficher("Commande inconnue.\nTapez '?' pour obtenir de l'aide.");
                }
                break;
            case "REPRENDRE":
                // if (zoneCourante == zones[0]) {
                //
                // } else {
                gui.clearText();
                gui.afficher("Commande inconnue.\nTapez '?' pour obtenir de l'aide.");
                // }
                break;
            default:
                gui.clearText();
                gui.afficher("Commande inconnue.\nTapez '?' pour obtenir de l'aide.");
                break;
        }
    }

    private void ouvrirCoffre() {
        boolean clePossedee = false;
        if (Inventaire != null) {
            for (Objets objet : Inventaire) {
                if (objet == Objets.CLE1) {
                    clePossedee = true;
                } else {
                    clePossedee = false;
                }
            }
        }
        if (clePossedee == true /* && zoneCourante = zones[x] */) {
            coffreOuvert = true;
            // zoneActuelle = zones[x]; -> Cinématique d'ouverture du coffre / récupération
            // clé cellule
            // Ajouter CLE2 à l'inventaire en fin de cinématique (Inventaire.add(CLE2);)
        } else {
            gui.afficher("Vous n'êtes pas dans la salle des gardes ou ne possédez pas la clé du coffre.");
        }
    }

    private void ouvrirCellule() {
        boolean clePossedee = false;
        if (Inventaire != null) {
            for (Objets objet : Inventaire) {
                if (objet == Objets.CLE2) {
                    clePossedee = true;
                } else {
                    clePossedee = false;
                }
            }
        }
        if (clePossedee == true /* && zoneCourante = zones[16/17/18/22] */) {
            celluleOuverte = true;
            // zoneActuelle = zones[x]; -> Cellule ouverte (jour ou nuit)
        } else {
            gui.afficher("Vous n'êtes pas dans votre cellule ou ne possédez pas la clé pour vous échapper.");
        }
    }

    public void dormir() {
        if (zoneCourante == zones[16] || zoneCourante == zones[17]
                || zoneCourante == zones[18] || zoneCourante == zones[19] || zoneCourante == zones[22]) // Si on se
                                                                                                        // trouve dans
                                                                                                        // la cellule
        {
            String Message = Temps.skip();
            if (Temps.getHeure() == 8) // Heure du repas (petit-déjeuner)
            {
                zoneCourante = zones[18];
            }
            if (Temps.getHeure() == 10) // Heure de la promenade (matin)
            {
                zoneCourante = zones[17];
            }
            if (Temps.getHeure() == 12) // Heure du repas (déjeuner)
            {
                zoneCourante = zones[18];
            }
            if (Temps.getHeure() == 15) // Heure de la promenade (soir)
            {
                zoneCourante = zones[17];
            }
            if (Temps.getHeure() == 18) // Heure de la douche
            {
                zoneCourante = zones[19];
            }
            if (Temps.getHeure() == 20) // Heure du repas (dîner)
            {
                zoneCourante = zones[18];
            }
            gui.afficher(Temps.getTime());
            gui.afficher(Message);
        } else // Sinon on n'est pas autorisé à dormir
        {
            gui.afficher("Vous ne pouvez dormir que lorsque vous êtes dans votre cellule.\n");
        }
    }

    public void checkInventaire() {
        gui.afficher("Voici le contenu de votre inventaire :\n");
        gui.afficher();
        if (this.Inventaire != null) {
            for (Objets objet : Inventaire) {
                gui.afficher(objet.name().toString());
            }
        } else {
            gui.afficher("Aucun objet possédé.\n");
        }
    }

    // Vérifie si l'heure actuelle doit changer la zone actuelle sur une scène de
    // jour ou une scène de nuit
    private void checkJourNuit() {
        if (Temps.getHeure() < 20 && Temps.getHeure() >= 8) {
            if (zoneCourante == zones[2]) // Si la zone courante est une zone de type "jour" ,
            {
                zoneCourante = zones[1]; // on repasse zone courante à la même scène de type "nuit" .
                gui.afficheImage(zoneCourante.nomImage());
            }

            // if (zoneCourante == zones[4]) // Faire de même pour les autres zones sauf les
            // zones
            // { // de cinématiques et d'événements (repas, bagarre, etc.)
            // zoneCourante = zones[3];
            // }
            // etc
        } else if (Temps.getHeure() >= 20 && Temps.getHeure() < 8) {
            if (zoneCourante == zones[1]) {
                zoneCourante = zones[2];
                gui.afficheImage(zoneCourante.nomImage());
            }
            // if (zoneCourante == zones[3])
            // {
            // zoneCourante = zones[4];
            // }
            // etc
        }
    }

    private void afficherAide() {
        gui.afficher("Vous êtes perdu? Les commandes autorisées sont : ");
        for (String str : Commande.toutesLesDescriptions()) {
            gui.afficher("\n" + str.toString());
        }
    }

    private void allerEn(String direction) {
        Zone nouvelle = zoneCourante.obtientSortie(direction);
        if (nouvelle == null) {
            gui.afficher("Il n'y a pas de sortie dans la direction : " + direction + ".");
            gui.afficher();
            gui.afficher("Tapez 'Localiser' pour obtenir les commandes disponibles.\n");
        } else {
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
    private void updatePositionJoueur(String direction) {
        if (ancienneZone == zones[12] && direction == "NORD") {
            gui.afficheJoueur("NORD", 258, 343);
        }
        if ((ancienneZone == zones[1] || ancienneZone == zones[2]) && direction == "NORD") {
            gui.afficheJoueur("NORD", 258, 325);
        }
        if ((ancienneZone == zones[3] || ancienneZone == zones[4]) && direction == "OUEST") {
            gui.afficheJoueur("SUD", 450, 50);
        }
        if ((ancienneZone == zones[3] || ancienneZone == zones[4]) && direction == "OUEST") {
            gui.afficheJoueur("SUD", 450, 50);
        }
        if ((ancienneZone == zones[3] || ancienneZone == zones[4]) && direction == "OUEST") {
            gui.afficheJoueur("SUD", 450, 50);
        }
        if ((ancienneZone == zones[8] || ancienneZone == zones[6] || ancienneZone == zones[7]) && direction == "NORD") {
            gui.afficheJoueur("EST", 100, 250);
        }
        //
        // etc...
        //

    }

    private void nextScene(String uneAction) {
        Zone nouvelle = zoneCourante.obtientSortie(uneAction);
        zoneCourante = nouvelle;
        if (zoneCourante == zones[16] && debutJeu == true) {
            afficherMessageDeBienvenue();
            gui.afficher(Temps.getTime());
            debutJeu = false;
        }
        gui.afficher(zoneCourante.descriptionLongue());
        gui.afficher();
        gui.afficheImage(zoneCourante.nomImage());
        if (zoneCourante == zones[13]) {
            ancienneZone = zones[12];
            gui.refreshLayers();
            gui.afficheJoueur("NORD", 258, 343);
            updatePositionJoueur("NORD"); // Initialisation du joueur
        }
    }

    private void terminer() {
        gui.afficher("Fermeture du jeu...\nAu revoir!");
        gui.enable(false);
        Timer chrono = new Timer();
        chrono.schedule(new TimerTask() {
            @Override
            public void run() {
                System.exit(0);
            }
        }, 3500, 1000);
    }
}
