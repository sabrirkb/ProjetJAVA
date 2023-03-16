package jeu;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

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
    private boolean pauseMenu = false;
    private boolean nightTime = false;
    private boolean alerteNuit = true;
    private Zone[] zones;
    private Zone ancienneZone;
    private Zone temporaryPauseZone;
    private int temporaryPauseHeure;
    private int temporaryPauseMinutes;
    private int tentatives = 3; 
    private Audio leSon = new Audio();

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
                if (!pauseMenu && !quitMenu && !debutJeu) {
                    Temps.addTime();
                    checkJourNuit();
                    gui.updateTxtHeure(Temps.getHeure(), Temps.getMinutes(), tentatives);
                }
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

        zones[0] = new Cinematique("\nBienvenue dans le menu principal.", "/interface/menuPrincipal.png");
        // zones[0].ajouteAction(Action.NOUVEAU, zones[11]); -> GERER DANS LE SWITCH DES
        // COMMANDES
        // zones[0].ajouteAction(Action.REPRENDRE, zones[10]); -> GERER DANS LE SWITCH
        // DES COMMANDES
        // zones[0].ajouteAction(Action.QUITTER, zones[10]); -> GERER DANS LE SWITCH DES
        // COMMANDES

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
        zones[12] = new Cinematique("Durant le voyage jusqu'à l'île, il entend les gardes discuter "
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
                "/interieur/cellule/celluleJour.png"); // Zone cellule -> ajouter porte fermée (jour)

        zones[17] = new Zone(
                "votre cellule. C'est l'heure de la promenade. Les gardes ouvrent votre cellule pour vous laisser vous rendre dans la cour.",
                "/interieur/cellule/celluleJour.png"); // Zone cellule -> porte ouverte

        zones[18] = new Zone(
                "votre cellule. C'est l'heure du repas. Les gardes ouvrent votre cellule pour vous laisser vous rendre au réfectoire.",
                "/interieur/cellule/celluleJour.png"); // Zone cellule -> porte ouverte

        zones[19] = new Cinematique(
                "Vous êtes dans votre cellule. C'est l'heure de la douche. Vous êtes escorté par les gardes jusque dans les douches.",
                "/interieur/cellule/celluleJour.png"); // Zone cellule -> porte ouverte

        zones[20] = new Zone("le couloir (jour).", "/interieur/couloir/couloirJour.png");

        zones[21] = new Zone("le couloir (nuit).", "/interieur/couloir/couloirNuit.png");

        zones[22] = new Zone("votre cellule (nuit). Vous devez attendre que les gardes vous autorisent à sortir…",
                "/interieur/cellule/celluleNuit.png"); // Zone cellule -> ajouter porte fermée (nuit)

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
                + " dans votre cellule après vous avoir donné un avertissement…\n",
                // \nAttention: il vous reste "
                // + this.getTentatives()
                // + " avertissement(s) avant de vous faire exécuter!", -> gui.afficher() dans
                // l'event ayant mené à la zone
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

        zones[34] = new Cinematique("Percival Lebrave a désobéit de trop nombreuses fois à la garde royale."
                + " Il a déshonoré le royaume d'Aeternum. Son mauvais comportement le mènera à sa propre perte.\n"
                + "\nQue ton âme repose en paix, jeune Percival. \nTu vas nous manquer.",
                "/interieur/cellule/celluleJour.png"); // IMAGE GAME OVER [OK] -> GENERIQUE DE FIN -> Delay 5s ->
                                                       // quit();

        zones[35] = new Cinematique("Merci d'avoir joué. Le jeu va maintenant se fermer…",
                "/interieur/cellule/celluleJour.png");

        // zones[3].ajouteSortie(Sortie.OUEST, zones[9]); // Affecte le déclenchement de
        // la cinématique à zones[3]
        // si le joueur prend la sortie 'OUEST'

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

        zones[11].ajouteAction(Action.SUIVANT, zones[12]);

        zones[12].ajouteAction(Action.SUIVANT, zones[13]);

        zones[13].ajouteAction(Action.SUIVANT, zones[14]);

        zones[14].ajouteAction(Action.SUIVANT, zones[15]);

        zones[15].ajouteAction(Action.OK, zones[16]);

        zones[16].ajouteAction(Action.DORMIR, zones[16]);

        zones[17].ajouteSortie(Sortie.SUD, zones[20]); // Si heure promenade -> Sortie SUD (couloir) autorisée (jour)
        zones[17].ajouteAction(Action.DORMIR, zones[16]);

        zones[18].ajouteAction(Action.DORMIR, zones[16]);
        zones[18].ajouteSortie(Sortie.SUD, zones[20]); // Si heure repas -> Sortie SUD (couloir) autorisée (jour)

        zones[19].ajouteAction(Action.SUIVANT, zones[25]); // Si heures douche -> [OK] dirige vers douches (jour)

        zones[20].ajouteSortie(Sortie.EST, zones[3]);
        zones[20].ajouteSortie(Sortie.OUEST, zones[23]);
        zones[20].ajouteSortie(Sortie.NORD, zones[16]);

        zones[21].ajouteSortie(Sortie.SUD, zones[20]);

        zones[22].ajouteAction(Action.DORMIR, zones[16]);

        zones[23].ajouteSortie(Sortie.SUD, zones[20]);

        zones[25].ajouteAction(Action.SUIVANT, zones[31]);

        zones[26].ajouteSortie(Sortie.OUEST, zones[3]);

        zones[29].ajouteSortie(Sortie.EST, zones[6]);

        zones[31].ajouteAction(Action.OK, zones[16]);

        // AFFECTATION DE LA ZONE COURANTE - DEBUT DU JEU -> ZONE COURANTE = CINEMATIQUE
        // DE DEPART (pour l'instant zones[11])
        zoneCourante = zones[11];
        // Temps.setTime(21, 30); // -> permet de changer l'heure de départ du jeu

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

    public void traiterCommande(String commandeLue) throws UnsupportedAudioFileException, InterruptedException, IOException, LineUnavailableException {
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
                gui.afficher(
                        "Attention! Vous êtes sur le point de quitter le jeu. Assurez-vous d'avoir sauvegardé votre partie!\n");
                gui.afficher("\nVoulez-vous vraiment quitter ? \n[YES] - [NO]");
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
                    gui.afficher("La commande " + commandeLue + " n'est pas disponible.");
                    gui.afficher();
                    gui.afficher("Tapez 'Localiser' pour obtenir les commandes disponibles.\n");
                }
                break;
            case "NO":
                if (quitMenu) {
                    quitMenu = false;
                    afficherLocalisation();
                } else {
                    gui.afficher("La commande " + commandeLue + " n'est pas disponible.");
                    gui.afficher();
                    gui.afficher("Tapez 'Localiser' pour obtenir les commandes disponibles.\n");
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
                if ((zoneCourante == zones[16]) || (zoneCourante == zones[17]) || (zoneCourante == zones[18])) {
                    dormir();
                    gui.afficher("\n" + zoneCourante.descriptionLongue());
                } else {
                    gui.afficher(
                            "Impossible de dormir pour le moment.\n\nTapez 'Localiser' pour obtenir les commandes disponibles.\n");
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
                    gui.afficher("La commande " + commandeLue + " n'est pas disponible.");
                    gui.afficher();
                    gui.afficher("Tapez 'Localiser' pour obtenir les commandes disponibles.\n");
                }
                break;
            case "REPRENDRE":
                if (zoneCourante == zones[0]) {
                    // AFFICHER SELECTIONNEUR DE FICHIER ET LE LIRE / RECUPERER VARIABLES
                    // zoneCourante == zonePause
                } else {
                    gui.afficher("La commande " + commandeLue + " n'est pas disponible.");
                    gui.afficher();
                    gui.afficher("Tapez 'Localiser' pour obtenir les commandes disponibles.\n");
                }
                break;
            case "PAUSE":
            case "P":
                if (!debutJeu) {
                    pauseMenu = true;
                    gui.cacherBarre();
                    temporaryPauseHeure = Temps.getHeure();
                    temporaryPauseMinutes = Temps.getMinutes();
                    temporaryPauseZone = zoneCourante;
                    // SET LA ZONE COURANTE SUR ZONE <PAUSE> -> à créer
                    gui.afficher("Vous êtes dans le menu pause.\n\nListe des commmandes disponibles :\n");
                    gui.afficher("\nJouer\t\t (J) : Reprendre la partie");
                    gui.afficher("\nSauvegarder   (SAVE) : Sauvegarder la partie");
                    gui.afficher("\nQuitter\t\t (Q) : Quitter le jeu");
                } else {
                    gui.afficher("La commande " + commandeLue + " n'est pas disponible.");
                    gui.afficher();
                    gui.afficher("Tapez 'Localiser' pour obtenir les commandes disponibles.\n");
                }
                break;
            case "JOUER":
            case "J":
                if (pauseMenu) {
                    gui.afficheBarre();
                    Temps.setHeure(temporaryPauseHeure);
                    Temps.setMinutes(temporaryPauseMinutes);
                    zoneCourante = temporaryPauseZone;
                    afficherLocalisation();
                    pauseMenu = false;
                } else {
                    gui.afficher("La commande " + commandeLue + " n'est pas disponible.");
                    gui.afficher();
                    gui.afficher("Tapez 'Localiser' pour obtenir les commandes disponibles.\n");
                }
                break;
            case "SAUVEGARDER":
            case "SAVE":
                if (pauseMenu || quitMenu) {
                    // SET LA ZONE COURANTE SUR ZONE <SAUVEGARDE> -> à créer
                    gui.afficher("Sauvegarde en cours…\n");
                    try {
                        // SAUVEGARDE DE TOUTES LES VARIABLES DE CHAQUE FICHIER DANS UN REPERTOIRE
                        // [...]
                        // Wait 3 secondes
                        gui.afficher("\nSauvegarde effectuée avec succès, retour au menu pause…");
                        // Wait 3 secondes
                        Temps.setHeure(temporaryPauseHeure);
                        Temps.setMinutes(temporaryPauseMinutes);
                        traiterCommande("PAUSE");
                    } catch (Exception e) {
                        gui.clearText();
                        gui.afficher("\nErreur durant la sauvegarde, retour au menu pause…\n \n" + e.getMessage());
                        // WAIT 3 secondes
                        Temps.setHeure(temporaryPauseHeure);
                        Temps.setMinutes(temporaryPauseMinutes);
                        traiterCommande("PAUSE");
                    }
                } else {
                    gui.afficher("La commande " + commandeLue + " n'est pas disponible.");
                    gui.afficher();
                    gui.afficher("Tapez 'Localiser' pour obtenir les commandes disponibles.\n");
                }
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

    // Vérifie l'heure actuelle et change l'affichage de la zone courante si
    // nécessaire (zone de Jour / zone de Nuit)
    private void checkJourNuit() {
        int heureCourante = Temps.getHeure();
        if (heureCourante < 22 && heureCourante >= 8) {
            // [ 8h <= heureCourante < 22 ]
            // L'heure courante est >= 8h et < 22h

            nightTime = false;
            alerteNuit = true;

            if (zoneCourante == zones[2]) // Si la zone courante est une zone de type "nuit" ,
            {
                zoneCourante = zones[1]; // on repasse zone courante à la même zone, mais de type "jour" .
                gui.afficheImage(zoneCourante.nomImage());
            }

            // if (zoneCourante == zones[4]) // Faire de même pour les autres zones sauf les
            // zones
            // { // de cinématiques et d'événements (repas, bagarre, etc.)
            // zoneCourante = zones[3];
            // }
            // etc
        }
        if ((heureCourante >= 22 && heureCourante <= 24) || (heureCourante >= 0 && heureCourante < 8)) {
            // [ 22h <= heureCourante <= 24 ] OU [ 0 <= heureCourante < 8]
            // L'heure courante est >= 22h et < 8h

            nightTime = true;
            if (!debutJeu && alerteNuit) {
                gui.clearText();
                gui.afficher("\nLa nuit tombe sur l'île de Mors Insula… ");
                gui.afficher("Les gardes exigent à tous les détenus de rejoindre leurs cellules respectives!\n");
                gui.afficher("\nDes rondes de nuit seront effectuées jusqu'à 8 heures du matin. ");
                gui.afficher("Tout détenu qui sera repéré de nuit en dehors de sa cellule sera sanctionné!\n");
                alerteNuit = false;
            }
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

    private void allerEn(String direction) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        Zone nouvelle = zoneCourante.obtientSortie(direction);
        if (nouvelle == null) {
            gui.afficher("Il n'y a pas de sortie dans la direction : " + direction + ".");
            gui.afficher();
            gui.afficher("Tapez 'Localiser' pour obtenir les commandes disponibles.\n");
        } else {
            leSon.jouerAudioPas();
            ancienneZone = zoneCourante;
            zoneCourante = nouvelle;
            gui.afficher(zoneCourante.descriptionLongue());
            gui.afficher();
            gui.afficheImage(zoneCourante.nomImage());
            updatePositionJoueur(direction);
        }
    }

    // Contient la liste des positions (sprite et coordonnées x,y)
    // du joueur en fonction de sa provenance, de sa direction, des événements ou des cinématiques
    private void updatePositionJoueur(String commandeLue) {

        // Si le joueur était en zone exterieur_île (1;2) et qu'il se rend vers le NORD
        if ((ancienneZone == zones[1] || ancienneZone == zones[2]) && commandeLue == "NORD") {
            gui.afficheJoueur("NORD", 258, 325);    // on demande à la GUI de l'afficher
                                                                    // avec l'image du joueur qui regarde le "NORD",
                                                                    // en position x:258 y:325 sur l'interface graphique
        }
        // Si le joueur était en zone cour (3;4;5) et qu'il se rend vers l'OUEST
        if ((ancienneZone == zones[3] || ancienneZone == zones[4] || ancienneZone == zones[5])
                && commandeLue == "OUEST") {
            gui.afficheJoueur("SUD", 450, 50);
        }
        // Si le joueur était en zone cour (3;4;5) et qu'il se rend vers le NORD
        if ((ancienneZone == zones[3] || ancienneZone == zones[4] || ancienneZone == zones[5])
                && commandeLue == "NORD") {
            gui.afficheJoueur("OEST", 25, 230);
        }
        // Si le joueur était en zone réfectoire (6;7;8) et qu'il se rend vers le NORD
        if ((ancienneZone == zones[6] || ancienneZone == zones[7] || ancienneZone == zones[8])
                && commandeLue == "NORD") {
            gui.afficheJoueur("EST", 100, 250);
        }
        // Si le joueur était en zone réfectoire (6;7;8) et qu'il se rend vers l'OUEST
        if ((ancienneZone == zones[6] || ancienneZone == zones[7] || ancienneZone == zones[8])
                && commandeLue == "OUEST") {
            gui.afficheJoueur("OUEST", 380, 200);
        }
        // Si le joueur était en zone cellule (17;18) et qu'il se rend vers le SUD
        if ((ancienneZone == zones[17] || ancienneZone == zones[18]) && commandeLue == "SUD") {
            gui.afficheJoueur("SUD", 160, 170);
        }
        // Si le joueur était en zone cuisine (29;30) et qu'il se rend vers l'EST
        if ((ancienneZone == zones[29] || ancienneZone == zones[30]) && commandeLue == "EST") {
            gui.afficheJoueur("EST", 25, 330);
        }

        //
        // etc...
        //

    }

    private void nextScene(String uneAction) throws UnsupportedAudioFileException, InterruptedException, IOException, LineUnavailableException {
        Zone nouvelle = zoneCourante.obtientSortie(uneAction);
        if (nouvelle == null) {
            gui.afficher("La commande " + uneAction + " n'est pas disponible.");
            gui.afficher();
            gui.afficher("Tapez 'Localiser' pour obtenir les commandes disponibles.\n");
        } else {
            leSon.jouerAudioNext();
            zoneCourante = nouvelle;
            if (zoneCourante == zones[16] && debutJeu == true) {
                gui.afficheBarre();
                afficherMessageDeBienvenue();
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
            if (zoneCourante == zones[14]) {
                ancienneZone = zones[13];
                gui.refreshLayers();
                gui.afficheJoueur("NORD", 258, 343); // Changement position joueur
            }
            if (zoneCourante == zones[15]) {
                ancienneZone = zones[14];
                gui.refreshLayers();
                gui.afficheJoueur("NORD", 258, 343); // Changement position joueur
            }
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
