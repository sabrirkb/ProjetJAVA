package jeu;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Jeu {

    public static GUI gui;
    private Zone zoneCourante;
    private Horloge Temps = new Horloge();
    private ArrayList<Objets> Inventaire = new ArrayList<Objets>();
    private boolean cinematiqueDeDepart = true; // déclare si la cinématique au début du jeu est active
    private boolean coffreOuvert = false; // si le coffre de la salle des garde est ouvert
    private boolean celluleOuverte = false; // si la cellule est déverouillée
    private boolean armeRecuperee = false; // augmentera les chances de battre Marco
    private boolean indiceCodetenu = false; // permettra de savoir qu'il faut être gentil avec Marco
    private boolean battreMarco = false; // choix du joueur d'être gentil ou méchant avec Marco
    private boolean sceneBagarre = false; // si la scène de bagarre s'est passé ou non au réfectoire
    private boolean quitMenu = false; // si le menu quitter est ouvert
    private boolean pauseMenu = false; // si le menu pause est ouvert
    private boolean isReprendreActive = false; // si le menu reprise de partie est actif
    private boolean nightTime = false; // s'il fait nuit
    private boolean activerAlerteNuit = true; // active ou non les alertes précisant les rondes de nuit des gardes
    private boolean nightAlertOn = false; // définit si l'alerte ci-dessus est active ou non
    private boolean carteTrouvee = false; // si la carte du jeu a été trouvée par le joueur (salle des gardes) -> 1/4
                                          // chance de prendre un avertissement (variable aléatoire)
    private Zone[] zones; // tableau comportant les zones / cinématiques du jeu
    private Zone ancienneZone; // conserve la zone précédente
    private Zone temporaryPauseZone; // servira à conserver la zone durant le menu pause
    private int temporaryPauseHeure; // servira à conserver l'heure durant le menu pause
    private int temporaryPauseMinutes; // servira à conserver les minutes durant le menu pause
    private int tentatives = 3; // Nb max d'avertissements -> au bout de 3 -> game over
    private Audio leSon = new Audio();

    ///// VARIABLES SUPPLEMENTAIRES NECESSAIRES AUX SAUVEGARDES /////
    int posXJoueur;
    int posYJoueur;
    URL URLJoueur;
    ArrayList<Integer> posX_autres = new ArrayList<Integer>();
    ArrayList<Integer> posY_autres = new ArrayList<Integer>();
    ArrayList<URL> URL_autres = new ArrayList<URL>();
    // AJOUTER VARIABLE DE SAVE POUR SON AMBIANT
    ////////////////////////////////////////////////////////////////

    /****************************************************************************************
     * ATTENTION : TOUTE NOUVELLE VARIABLE NÉCESSAIRE AU DEROULEMENT DU JEU DOIT
     * ETRE REMISE
     * À ZÉRO DANS LA METHODE retourMenuPrincipal(), ET SES VALEURS DOIVENT ETRE
     * ENREGISTRÉES
     * DANS LA METHODE sauvegarderPartie(), ET REMPLACÉES DANS LA METHODE
     * reprendrePartie()
     * PAR LES VALEURS RECUPERÉES DANS LE FICHIER DE SAUVEGARDE
     ****************************************************************************************/

    public Jeu() throws InterruptedException {

        /*
         * --------------------------------
         * Inclure un menu de départ -> reprendre une sauvegarde / créer une nouvelle
         * partie
         * La fonction init(); affichera l'interface du menu principal;
         */
        creerCarte();
        gui = null;
        /*
         * --------------------------------
         */
    }

    // DÉMARRE LE TIMER QUI PERMET À L'HORLOGE DU JEU DE TOURNER
    public void StartTime() throws InterruptedException {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!pauseMenu && !quitMenu && !cinematiqueDeDepart) {
                    Temps.addTime();
                    try {
                        checkJourNuit();
                        checkEvent();
                    } catch (UnsupportedAudioFileException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (LineUnavailableException e) {
                        e.printStackTrace();
                    }
                    gui.updateTxtHeure(Temps.getHeure(), Temps.getMinutes(), tentatives);
                }
            }
        }, 0, 700); // réduire 'period: 700' pour accélérer le temps in-game
    }

    // INITIALISATION
    public void setGUI(GUI g) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        gui = g;

       // gui.afficheAutre("NULL", 2, 40, 40);
       // gui.afficheAutre("NULL", 3, 40, 80);
       // gui.afficheAutre("NULL", 4, 40, 120);
       // gui.afficheAutre("NULL", 5, 40, 160);
       // gui.afficheAutre("NULL", 6, 40, 180);
       // gui.afficheAutre("NULL", 7, 80, 40);
       // gui.afficheAutre("NULL", 8, 80, 80);
       // gui.afficheAutre("NULL", 9, 80, 160);

        afficherLocalisation();
        gui.afficheImage(zoneCourante.nomImage());
        leSon.jouerAmbiantThemePrincipal();
    }

    private void creerCarte() {

        // CRÉATION D'UN TABLEAU QUI CONTIENT LES ZONES ET CINÉMATIQUES(*) DE NOTRE JEU
        // (*) : une cinématique se définit comme un enchaînement de zones, dont les
        // messages / images varient lorsque le joueur tape les actions [SUIVANT] / [OK]

        zones = new Zone[150]; // Il n'y a pas réellement 150 "zones", on choisit un tableau large
                               // pour y inclure des scènes variantes (jour/nuit) et des cinématiques.

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

        zones[0] = new Cinematique("\nBienvenue dans le menu principal. \n"
                + "\nTapez 'Nouveau' pour commencer une partie"
                + "\nTapez 'Reprendre' pour charger une sauvegarde"
                + "\nTapez 'Quitter' pour fermer le jeu", "/interface/menuPrincipal.png");
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
                // l'event ayant mené à la zone -> 'OK' = clearTexte + description zone
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
        zones[4].ajouteSortie(Sortie.EST, zones[27]);
        zones[4].ajouteSortie(Sortie.NORD, zones[21]);

        zones[6].ajouteSortie(Sortie.NORD, zones[3]);
        zones[6].ajouteSortie(Sortie.OUEST, zones[29]);

        zones[7].ajouteSortie(Sortie.NORD, zones[4]);
        zones[7].ajouteSortie(Sortie.OUEST, zones[30]);

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

        zones[21].ajouteSortie(Sortie.OUEST, zones[24]);
        zones[21].ajouteSortie(Sortie.EST, zones[4]);
        zones[21].ajouteSortie(Sortie.NORD, zones[22]);

        zones[22].ajouteAction(Action.DORMIR, zones[16]);

        zones[23].ajouteSortie(Sortie.SUD, zones[20]);

        zones[24].ajouteSortie(Sortie.SUD, zones[21]);

        zones[25].ajouteAction(Action.SUIVANT, zones[31]);

        zones[26].ajouteSortie(Sortie.OUEST, zones[3]);

        zones[27].ajouteSortie(Sortie.OUEST, zones[4]);

        zones[29].ajouteSortie(Sortie.EST, zones[6]);

        zones[30].ajouteSortie(Sortie.EST, zones[7]);

        zones[31].ajouteAction(Action.OK, zones[16]);

        zones[34].ajouteAction(Action.OK, zones[35]);

        zones[35].ajouteAction(Action.QUITTER, zones[0]);

        // AFFECTATION DE LA ZONE COURANTE - DEBUT DU JEU -> ZONE COURANTE = MENU
        // PRINCIPAL
        // DE DEPART (zones[0])
        zoneCourante = zones[0];

        // Temps.setTime(21, 30); // -> permet de changer l'heure de départ du jeu

    }

    private void afficherLocalisation() {

        if (zoneCourante == zones[0]) {
            gui.afficher(zoneCourante.description);
        } else {
            gui.afficher(zoneCourante.descriptionLongue());
            gui.afficher();
        }
    }

    private void afficherMessageDeBienvenue() {
        gui.afficher("Bienvenue sur la prison de Mors Insula, cher Percival!");
        gui.afficher("\nTapez '?' pour obtenir de l'aide à tout moment.\n");
        gui.afficher();
        gui.afficheImage(zoneCourante.nomImage());
    }

    public void traiterCommande(String commandeLue)
            throws UnsupportedAudioFileException, InterruptedException, IOException, LineUnavailableException {
        gui.clearText();
        gui.afficher("> " + commandeLue + "\n\n");
        switch (commandeLue.toUpperCase()) {

            /*
             * RAJOUTER UNE COMMANDE AFFICHER CARTE
             * -> Si carteTrouvee == true -> zone[x] correspondant à l'affichage de la map
             * -> else { "Commande non disponible" + liste des commandes }
             */
            case "MAP":
            case "M":
            case "CARTE":
            case "C":
            if (nightAlertOn)
                {
                    nightAlertOn = false;
                }
                if (carteTrouvee) {
                    // zoneCourante = zone[x]
                } else {
                    gui.afficher("Vous ne possédez pas de carte.");
                    gui.afficher("\n\nTapez 'Localiser' pour obtenir les détails de la zone courante.\n\n");
                    gui.afficher(zoneCourante.commandesDispo());
                    leSon.jouerAudioError();
                }
                break;

            case "?":
            case "AIDE":
            case "HELP":
            if (nightAlertOn)
                {
                    nightAlertOn = false;
                }
                afficherAide();
                break;
            case "N":
            case "NORD":
            if (nightAlertOn)
                {
                    nightAlertOn = false;
                }
                allerEn("NORD");
                break;
            case "L":
            case "LOCALISATION":
            case "LOCALISATIONS":
            case "LOCALISER":
            case "LOC":
            case "LOCAL":
            if (nightAlertOn)
                {
                    nightAlertOn = false;
                }
                afficherLocalisation();
                break;
            case "S":
            case "SUD":
            if (nightAlertOn)
                {
                    nightAlertOn = false;
                }
                allerEn("SUD");
                break;
            case "E":
            case "EST":
            if (nightAlertOn)
                {
                    nightAlertOn = false;
                }
                allerEn("EST");
                break;
            case "O":
            case "OUEST":
            if (nightAlertOn)
                {
                    nightAlertOn = false;
                }
                allerEn("OUEST");
                break;
            case "OUVRIR":
                if (zoneCourante == zones[16]) {
                    this.ouvrirCellule();
                }
                // if (zoneCourante == zones[x] && !inventaire.contains(cle1))a
                // {
                // this.ouvrirCoffre();
                // }
            case "Q":
            case "QUITTER":
            case "QUIT":
            if (nightAlertOn)
                {
                    nightAlertOn = false;
                }
                quitMenu = true;
                gui.afficher(
                        "Attention! Vous êtes sur le point de quitter le jeu. Assurez-vous d'avoir sauvegardé votre partie!\n");
                gui.afficher("\nVoulez-vous vraiment quitter ? \n[YES] - [NO]");
                leSon.jouerAudioError();
                break;
            case "I":
            case "INVENTAIRE":
            case "INV":
            if (nightAlertOn)
                {
                    nightAlertOn = false;
                }
                checkInventaire();
                break;
            case "YES":
            if (nightAlertOn)
                {
                    nightAlertOn = false;
                }
                if (quitMenu) {
                    if (zoneCourante != zones[0]) // Si on ne se trouve pas dans le menu principal
                    {
                        this.retourMenuPrincipal();
                        break;
                    }
                    if (zoneCourante == zones[0]) {
                        leSon.jouerAudioConfirm();
                        terminer();
                        break;
                    }
                    break;
                }
                if (!quitMenu) {
                    gui.afficher("La commande " + commandeLue + " n'est pas disponible.");
                    gui.afficher("\n\nTapez 'Localiser' pour obtenir les détails de la zone courante.\n\n");
                    gui.afficher(zoneCourante.commandesDispo());
                    leSon.jouerAudioError();
                    break;
                }
                break;
            case "NO":
            if (nightAlertOn)
                {
                    nightAlertOn = false;
                }
                if (quitMenu) {
                    leSon.jouerAudioConfirm();
                    quitMenu = false;
                    afficherLocalisation();
                } else {
                    gui.afficher("La commande " + commandeLue + " n'est pas disponible.");
                    gui.afficher("\n\nTapez 'Localiser' pour obtenir les détails de la zone courante.\n\n");
                    gui.afficher(zoneCourante.commandesDispo());
                    leSon.jouerAudioError();
                }
                break;
            case "COFFRE":
            if (nightAlertOn)
                {
                    nightAlertOn = false;
                }
                ouvrirCoffre();
                break;
            case "SUIVANT":
            case "SUIV":
            if (nightAlertOn)
                {
                    nightAlertOn = false;
                }
                nextScene("SUIVANT");
                break;
            case "OK":
                if (nightAlertOn)   // Si le joueur tape 'OK' pour effacer l'alerte de nuit
                {                   
                    nightAlertOn = false;
                    gui.afficher(zoneCourante.descriptionLongue()); // et on réaffiche la description de la zone ;
                    leSon.jouerAudioConfirm();
                    break;
                }
                if (pauseMenu) {
                    gui.afficher(zoneCourante.descriptionLongue());
                    leSon.jouerAudioMenuOFF();
                    pauseMenu = false;
                    break;
                } else {                            // Sinon,
                    nextScene("OK"); }    // on passe à la scène suivante
                break;
            case "DORMIR":
                if (nightAlertOn)
                {
                    nightAlertOn = false;
                }
                if ((zoneCourante == zones[16]) || (zoneCourante == zones[17])
                        || (zoneCourante == zones[18] || zoneCourante == zones[22])) {
                    dormir();
                    gui.afficher("\n" + zoneCourante.descriptionLongue());
                } else {
                    gui.afficher(
                            "Impossible de dormir pour le moment.");
                    gui.afficher("\n\nTapez 'Localiser' pour obtenir les détails de la zone courante.\n\n");
                    gui.afficher(zoneCourante.commandesDispo());
                    leSon.jouerAudioError();
                }
                break;
            case "TEMPS":
            case "T":
            case "TIME":
            if (nightAlertOn)
                {
                    nightAlertOn = false;
                }
                gui.afficher(Temps.getTime());
                gui.afficher("Tapez '?' pour obtenir de l'aide à tout moment.\n");
                break;
            case "NOUVEAU":
                if (zoneCourante == zones[0]) {
                    leSon.stopAmbianceThemePrincipal();
                    leSon.jouerAudioConfirm();
                    zoneCourante = zones[11];
                    gui.afficheImage(zoneCourante.nomImage());
                    gui.afficher(zoneCourante.descriptionLongue());
                } else {
                    gui.afficher("La commande " + commandeLue + " n'est pas disponible.");
                    gui.afficher("\n\nTapez 'Localiser' pour obtenir les détails de la zone courante.\n\n");
                    gui.afficher(zoneCourante.commandesDispo());
                }
                break;
            case "REPRENDRE":
                if (zoneCourante == zones[0]) {
                    isReprendreActive = true;
                    gui.afficher(this.getPartiesSauvegardees());
                    gui.afficher("\n\nTapez le numéro de la partie à reprendre, ou 'RETOUR' pour revenir"
                            + " au menu principal.");
                    leSon.jouerAudioConfirm();
                } else {
                    gui.afficher("La commande " + commandeLue + " n'est pas disponible.");
                    gui.afficher("\n\nTapez 'Localiser' pour obtenir les détails de la zone courante.\n\n");
                    gui.afficher(zoneCourante.commandesDispo());
                    leSon.jouerAudioError();
                }
                break;
            case "RETOUR":
                if (zoneCourante == zones[0] && isReprendreActive) {
                    isReprendreActive = false;
                    gui.afficher(zoneCourante.description);
                    leSon.jouerAudioConfirm();
                }
                // if (zoneCourante != zones[0] && !isReprendreActive) {
                // isReprendreActive = false;
                // gui.afficher(zoneCourante.description); }
                else {
                    gui.afficher("La commande " + commandeLue + " n'est pas disponible.");
                    gui.afficher("\n\nTapez 'Localiser' pour obtenir les détails de la zone courante.\n\n");
                    gui.afficher(zoneCourante.commandesDispo());
                    leSon.jouerAudioError();
                }
                break;
            case "1":
            case "2":
            case "3":
            case "4":
            case "5":
            case "6":
            case "7":
            case "8":
            case "9":
                if (zoneCourante == zones[0]) {
                    try {
                        int indexPartie = Integer.parseInt(commandeLue);
                        this.reprendrePartie(indexPartie);
                        leSon.jouerAudioConfirm();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    gui.afficher("La commande " + commandeLue + " n'est pas disponible.");
                    gui.afficher("\n\nTapez 'Localiser' pour obtenir les détails de la zone courante.\n\n");
                    gui.afficher(zoneCourante.commandesDispo());
                    leSon.jouerAudioError();
                }
                break;
            case "PAUSE":
            case "P":
            if (nightAlertOn)
                {
                    nightAlertOn = false;
                }
                if (!cinematiqueDeDepart) {
                    pauseMenu = true;
                    gui.cacherBarre();
                    temporaryPauseHeure = Temps.getHeure();
                    temporaryPauseMinutes = Temps.getMinutes();
                    temporaryPauseZone = zoneCourante;
                    // SET LA ZONE COURANTE SUR ZONE <PAUSE> -> à créer
                    // gui.afficheImage(zoneCourante.nomImage());
                    gui.afficher("Vous êtes dans le menu pause.\n\nListe des commmandes disponibles :\n");
                    gui.afficher("\nJouer\t\t (J) : Reprendre la partie");
                    gui.afficher("\nSauvegarder   (SAVE) : Sauvegarder la partie");
                    gui.afficher("\nQuitter\t\t (Q) : Quitter le jeu");
                    leSon.jouerAudioMenuON();
                } else {
                    gui.afficher("La commande " + commandeLue + " n'est pas disponible.");
                    gui.afficher("\n\nTapez 'Localiser' pour obtenir les détails de la zone courante.\n\n");
                    gui.afficher(zoneCourante.commandesDispo());
                    leSon.jouerAudioError();
                }
                break;
            case "JOUER":
            case "J":
                if (pauseMenu) {
                    gui.afficheBarre();
                    Temps.setHeure(temporaryPauseHeure);
                    Temps.setMinutes(temporaryPauseMinutes);
                    zoneCourante = temporaryPauseZone;
                    gui.afficheImage(zoneCourante.nomImage());
                    afficherLocalisation();
                    pauseMenu = false;
                    leSon.jouerAudioMenuOFF();
                } else {
                    gui.afficher("La commande " + commandeLue + " n'est pas disponible.");
                    gui.afficher("\n\nTapez 'Localiser' pour obtenir les détails de la zone courante.\n\n");
                    gui.afficher(zoneCourante.commandesDispo());
                    leSon.jouerAudioError();
                }
                break;
            case "SAUVEGARDER":
            case "SAVE":
                if (pauseMenu || quitMenu) {
                    // SET LA ZONE COURANTE SUR ZONE <SAUVEGARDE> -> à créer
                    gui.afficher("Sauvegarde en cours…\n");
                    try {
                        this.sauvegarderPartie();
                        gui.clearText();
                        gui.afficher("\nSauvegarde effectuée avec succès, retour au menu pause…");
                        Temps.setHeure(temporaryPauseHeure);
                        Temps.setMinutes(temporaryPauseMinutes);
                        // Thread.currentThread().wait(3000);
                        traiterCommande("PAUSE");
                        leSon.jouerAudioConfirm();
                    } catch (Exception e) {
                        gui.clearText();
                        gui.afficher("\nErreur durant la sauvegarde, retour au menu pause…\n"
                                + "\nMessage d'erreur: " + e.getMessage());
                        Temps.setHeure(temporaryPauseHeure);
                        Temps.setMinutes(temporaryPauseMinutes);
                        // this.wait(3000);
                        traiterCommande("PAUSE");
                        leSon.jouerAudioError();
                    }
                } else {
                    gui.afficher("La commande " + commandeLue + " n'est pas disponible.");
                    gui.afficher("\n\nTapez 'Localiser' pour obtenir les détails de la zone courante.\n\n");
                    gui.afficher(zoneCourante.commandesDispo());
                    leSon.jouerAudioError();
                }
                break;
            default:
                if (zoneCourante == zones[0]) {
                    gui.afficher("Commande inconnue.\n");
                    gui.afficher(zoneCourante.description);
                    leSon.jouerAudioError();
                } else {
                    gui.afficher("Commande inconnue.\nTapez '?' pour obtenir de l'aide.");
                    gui.afficher("\n\nTapez 'Localiser' pour obtenir les détails de la zone courante.\n\n");
                    gui.afficher(zoneCourante.commandesDispo());
                    leSon.jouerAudioError();
                }
                break;
        }
    }

    private void ouvrirCoffre() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
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
            leSon.jouerAudioError();
        }
    }

    private void ouvrirCellule() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
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
            leSon.jouerAudioError();
        }
    }

    public void dormir() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
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
            if (Temps.getHeure() == 22) // Heure du repas (dîner)
            {
                zoneCourante = zones[22];
            }
            gui.afficher(Message);
        } else // Sinon on n'est pas autorisé à dormir
        {
            gui.afficher("Vous ne pouvez dormir que lorsque vous êtes dans votre cellule.\n");
            leSon.jouerAudioError();
        }
    }

    public void checkInventaire() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        pauseMenu = true;
        leSon.jouerAudioMenuON();
        gui.afficher("Voici le contenu de votre inventaire :\n");
        gui.afficher();
        if (this.Inventaire.size() != 0) {
            for (Objets objet : Inventaire) {
                gui.afficher(objet.name().toString() + " ");
            }
        } else {
            gui.afficher("Aucun objet possédé.");
            leSon.jouerAudioError();
        }
        gui.afficher("\n\nCommandes : OK");
    }

    // Vérifie l'heure actuelle et change l'affichage de la zone courante si
    // nécessaire (zone de Jour / zone de Nuit)
    private void checkJourNuit() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        int heureCourante = Temps.getHeure();
        if (heureCourante < 22 && heureCourante >= 8) {
            // [ 8h <= heureCourante < 22 ]
            // L'heure courante est >= 8h et < 22h (il fait jour)

            // Arrêt des sons ambiants de nuit
            leSon.stopAmbianceNuitExterieur();
            leSon.stopAmbianceNuitInterieur();

            nightTime = false;
            activerAlerteNuit = true; // On programme l'alerte pour le soir

            if (zoneCourante == zones[2]) {
                zoneCourante = zones[1];
            }
            // Si le joueur se trouve dans la zone [4] (cour nuit) et qu'il est plus de 8h,
            // la zone courante passe à zone [3] (cour jour)
            if (zoneCourante == zones[4]) {
                zoneCourante = zones[3];
            }

            if (zoneCourante == zones[22]) {
                zoneCourante = zones[18];

            }

            // if (zoneCourante == zones[4]) // Faire de même pour les autres zones sauf les
            // zones
            // { // de cinématiques et d'événements (repas, bagarre, etc.)
            // zoneCourante = zones[3];
            // }
            // etc
            gui.afficheImage(zoneCourante.nomImage());
        }
        if ((heureCourante >= 22 && heureCourante <= 24) || (heureCourante >= 0 && heureCourante < 8)) {
            // [ 22h <= heureCourante <= 24 ] OU [ 0 <= heureCourante < 8]
            // L'heure courante est >= 22h et < 8h (il fait nuit)

            // Arrêt des sons ambiant de jour
            // …

            nightTime = true;
            if (!cinematiqueDeDepart && activerAlerteNuit) {
                nightAlertOn = true;
                gui.clearText();
                gui.afficher("\nLa nuit tombe sur l'île de Mors Insula… ");
                gui.afficher("Les gardes exigent à tous les détenus de rejoindre leurs cellules respectives!\n");
                gui.afficher("\nDes rondes de nuit seront effectuées jusqu'à 8 heures du matin. ");
                gui.afficher("Tout détenu qui sera repéré de nuit en dehors de sa cellule sera sanctionné!\n");
                gui.afficher("\n" + zoneCourante.commandesDispo() + "OK");
                leSon.jouerAudioDialogue();
                activerAlerteNuit = false;
            }
            // Si le joueur se trouve dans la zone [1] (ext ile jour) et qu'il est plus de
            // 22h, la zone courante passe à zone [2] (ext ile nuit)
            if (zoneCourante == zones[1]) {
                zoneCourante = zones[2];
            }
            // Si le joueur se trouve dans la zone [3] (cour jour) et qu'il est plus de 22h,
            // la zone courante passe à zone [4] (cour nuit)
            if (zoneCourante == zones[3]) {
                zoneCourante = zones[4];
            }
            // Si le joueur se trouve dans la zone [6] (refectoire jour) et qu'il est plus
            // de 22h, la zone courante passe à zone [7] (refectoire nuit)
            if (zoneCourante == zones[6]) {
                zoneCourante = zones[7];
            }
            // Si le joueur se trouve dans la zone [16,17,18,19,22] (cellule jour) et qu'il
            // est plus de
            // 22h, la zone courante passe à zone [22] (cellule nuit)
            if (zoneCourante == zones[16] || zoneCourante == zones[17]
                    || zoneCourante == zones[18] || zoneCourante == zones[19]) {
                zoneCourante = zones[22];
            }
            // Si le joueur se trouve dans la zone [20] (couloir jour) et qu'il est plus de
            // 22h, la zone courante passe à zone [21] (couloir nuit)
            if (zoneCourante == zones[20]) {
                zoneCourante = zones[21];
            }
            // Si le joueur se trouve dans la zone [23] (douches jour) et qu'il est plus de
            // 22h, la zone courante passe à zone [24] (douches nuit)
            if (zoneCourante == zones[23]) {
                zoneCourante = zones[24];
            }
            // Si le joueur se trouve dans la zone [26] (salleDesGardes jour) et qu'il est
            // plus de
            // 22h, la zone courante passe à zone [27] (salleDesGardes nuit)
            if (zoneCourante == zones[26]) {
                zoneCourante = zones[27];
            }
            // Si le joueur se trouve dans la zone [29] (cuisine jour) et qu'il est plus de
            // 22h, la zone courante passe à zone [30] (cuisine nuit)
            if (zoneCourante == zones[29]) {
                zoneCourante = zones[30];
            }

            gui.afficheImage(zoneCourante.nomImage());

            // Gestion des son ambiants de nuit
            if (zoneCourante == zones[2] || zoneCourante == zones[4]) {
                leSon.stopAmbianceNuitInterieur();
                leSon.jouerAmbiantNuitExterieur();
            }
            if (zoneCourante == zones[7] || zoneCourante == zones[22] || zoneCourante == zones[21]
                    || zoneCourante == zones[24] || zoneCourante == zones[27] || zoneCourante == zones[30]) {
                leSon.stopAmbianceNuitExterieur();
                leSon.jouerAmbiantNuitInterieur();
            }
        }
    }

    // Définit les zones en fonctions de l'heure du jeu, de la zone courante
    // ainsi qu'en fonction des valeurs des booléens d'événement
    private void checkEvent() {

        // EXEMPLE DE DECLENCHEMENT D'UN EVENEMENT
        // if (zoneCourante == zones[20] && Temps.getHeure() == 12 && Temps.getMinutes()
        // == 30)
        // {
        // tentatives = tentatives -1;
        // }

        if (tentatives <= 0 && zoneCourante != zones[34] && zoneCourante != zones[35] && zoneCourante != zones[0]) {
            gui.clearText();
            gui.cacherBarre();
            zoneCourante = zones[34];
            gui.afficheImage(zoneCourante.nomImage());
            gui.afficher(zoneCourante.descriptionLongue());
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
            gui.afficher("\n\nTapez 'Localiser' pour obtenir les détails de la zone courante.\n\n");
            gui.afficher(zoneCourante.commandesDispo());
            leSon.jouerAudioError();
        } else {
            
            // Lors du déplacement du joueur, on cache toutes les couches (layers) du GUI (sauf joueur et zone)
            gui.cacher(2); gui.cacher(3); gui.cacher(4); gui.cacher(5);
            gui.cacher(6); gui.cacher(7); gui.cacher(8); gui.cacher(9);
            // Ces couches seront de nouveau instanciées dans la méthode checkEvent si nécessaire

            ancienneZone = zoneCourante;
            zoneCourante = nouvelle;
            gui.afficher(zoneCourante.descriptionLongue());
            gui.afficher();
            gui.afficheImage(zoneCourante.nomImage());
            updatePositionJoueur(direction);
            leSon.jouerAudioPas();
        }
    }

    // Contient la liste des positions (sprites et coordonnées x,y)
    // du joueur en fonction de sa provenance, de sa direction, des événements ou
    // des cinématiques
    private void updatePositionJoueur(String commandeLue) {

        // Si le joueur était en zone exterieur_île (1;2) et qu'il se rend vers le NORD
        if ((ancienneZone == zones[1] || ancienneZone == zones[2]) && commandeLue == "NORD") {
            gui.afficheJoueur("NORD", 258, 325); // on demande à la GUI de l'afficher
                                                 // avec l'image du joueur qui regarde le "NORD",
                                                 // en position x:258 y:325 sur l'interface graphique
        }
        // Si le joueur était en zone cour (3;4;5) et qu'il se rend vers l'OUEST
        if ((ancienneZone == zones[3] || ancienneZone == zones[4] || ancienneZone == zones[5])
                && commandeLue == "OUEST") {
            gui.afficheJoueur("SUD", 450, 50);
        }
        // Si le joueur était en zone cour (3;4;5) et qu'il se rend vers l'EST
        if ((ancienneZone == zones[3] || ancienneZone == zones[4] || ancienneZone == zones[5])
                && commandeLue == "EST") {
            gui.afficheJoueur("EST", 30, 200);
        }
        // Si le joueur était en zone cour (3;4;5) et qu'il se rend vers le NORD
        if ((ancienneZone == zones[3] || ancienneZone == zones[4] || ancienneZone == zones[5])
                && commandeLue == "NORD") {
            gui.afficheJoueur("OUEST", 480, 210);
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
            gui.afficheJoueur("SUD", 160, 180);
        }
        // Si le joueur était en zone couloir (20;21) et qu'il se rend vers l'EST
        if ((ancienneZone == zones[20] || ancienneZone == zones[21]) && commandeLue == "EST") {
            gui.afficheJoueur("SUD", 260, 120);
        }
        // Si le joueur était en zone couloir (20;21) et qu'il se rend vers le NORD
        if ((ancienneZone == zones[20] || ancienneZone == zones[21]) && commandeLue == "NORD") {
            gui.afficheJoueur("SUD", 260, 170);
        }
        // Si le joueur était en zone couloir (20;21) et qu'il se rend vers l'OUEST
        if ((ancienneZone == zones[20] || ancienneZone == zones[21]) && commandeLue == "OUEST") {
            gui.afficheJoueur("NORD", 260, 340);
        }
        // Si le joueur était en zone douches (23;24) et qu'il se rend vers le SUD
        if ((ancienneZone == zones[23] || ancienneZone == zones[24]) && commandeLue == "SUD") {
            gui.afficheJoueur("EST", 30, 210);
        }
        // Si le joueur était en zone salleDesGardes (26;27) et qu'il se rend vers le
        // OUEST
        if ((ancienneZone == zones[26] || ancienneZone == zones[27]) && commandeLue == "OUEST") {
            gui.afficheJoueur("OUEST", 450, 210);
        }
        // Si le joueur était en zone cuisine (29;30) et qu'il se rend vers l'EST
        if ((ancienneZone == zones[29] || ancienneZone == zones[30]) && commandeLue == "EST") {
            gui.afficheJoueur("EST", 30, 330);
        }

    }

    private void nextScene(String uneAction)
            throws UnsupportedAudioFileException, InterruptedException, IOException, LineUnavailableException {

        // Lecture du son de transition
        if (uneAction == "SUIVANT") {
            leSon.jouerAudioNext();
        }
        if (uneAction == "OK") {
            leSon.jouerAudioConfirm();
        }

        // Affichage de la nouvelle zone
        Zone nouvelle = zoneCourante.obtientSortie(uneAction);
        if (nouvelle == null) {
            gui.afficher("La commande " + uneAction + " n'est pas disponible.");
            gui.afficher("\n\nTapez 'Localiser' pour obtenir les détails de la zone courante.\n\n");
            gui.afficher(zoneCourante.commandesDispo());
            leSon.jouerAudioError();
        } else {
            zoneCourante = nouvelle;
            if (zoneCourante == zones[16] && cinematiqueDeDepart == true) {
                gui.afficheBarre();
                afficherMessageDeBienvenue();
                cinematiqueDeDepart = false;
            }
            if (zoneCourante == zones[12])
            {
                gui.afficheAutre("null", 2, 150,150); // préchargement pour la scène suivante
                gui.afficheAutre("null", 3, 150,150); // préchargement pour la scène suivante
                gui.afficheAutre("null", 4, 150,150); // préchargement pour la scène suivante
                gui.afficheAutre("null", 5, 150,150); // préchargement pour la scène suivante
                gui.afficheAutre("items/bateau/nord", 7, 250,250); // préchargement pour la scène suivante
                gui.cacher(7); 
                //gui.afficheAutre("null", 8, 150,150); // préchargement pour la scène suivante
                //gui.afficheAutre("null", 9, 150,150); // préchargement pour la scène suivante
            }
            if (zoneCourante == zones[13]) {
                gui.afficheJoueur("NORD", 258, 300);
                gui.afficheAutre("garde/descend", 2, 225,230); // affiche garde
                gui.afficheAutre("garde/descend", 3, 325, 230); // affiche garde
                gui.afficheAutre("garde/monte", 4, 290, 290); // affiche garde
                gui.afficheAutre("items/bateau/nord", 7, 215, 355); // affiche bateau
                updatePositionJoueur("NORD");   // Initialisation du joueur
                gui.refreshLayers();    // Rafraîchit la layeredPane (Classe GUI)
            }
            if (zoneCourante == zones[14]) {
                gui.cacher(2); 
                gui.cacher(3); 
                gui.afficheAutre("garde/monte", 4, 220,320); // Changement position du garde
                gui.cacher(5);
                gui.cacher(7);
                gui.afficheJoueur("NORD", 250, 330); // Changement position joueur
            }
            if (zoneCourante == zones[15]) {
                gui.afficheJoueur("SUD", 260, 170); // Changement position joueur
                gui.afficheAutre("garde/descend", 2, 225,230);  // affiche garde
                gui.afficheAutre("garde/descend", 3, 325, 230); // affiche garde
                gui.afficheAutre("garde/monte", 4, 150,150); // affiche garde
                gui.afficheAutre("items/porte/porteDroite", 7, 0,0); // affiche porte pnj
                gui.afficheAutre("items/porte/porteGauche", 8, 250,250); // préchargement pour la scène suivante
                gui.cacher(8); 
            }

            if (zoneCourante == zones[16]) {
                gui.afficheJoueur("SUD", 260, 170); // Changement position joueur
                gui.afficheAutre("garde/descend", 2, 225,230);  // affiche garde
                gui.afficheAutre("garde/descend", 3, 325, 230); // affiche garde
                gui.afficheAutre("items/porte/porteDroite", 7, 150,150); // affiche porte joueur
                gui.afficheAutre("items/porte/porteGauche", 8, 150,150); // affiche porte pnj
                gui.refreshLayers();
            }
            
            else {
            }
            gui.afficher(zoneCourante.descriptionLongue());
            gui.afficheImage(zoneCourante.nomImage());

            // Lecture des sons correspondant aux zones
            if (zoneCourante == zones[14] || zoneCourante == zones[15]) {
                Thread.sleep(0500);
                leSon.jouerAudioDialogue();
            }
        }
    }

    private void retourMenuPrincipal() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        zoneCourante = zones[0];
        gui.afficheImage(zoneCourante.nomImage());
        gui.afficher(zoneCourante.description);
        gui.afficheJoueur("NONE", 50, 50);

        // RESET DE TOUTES LES VARIABLES
        if (Inventaire != null) {
            Inventaire.clear();
        }
        cinematiqueDeDepart = true;
        Temps.setHeure(10);
        Temps.setMinutes(30);
        coffreOuvert = false;
        celluleOuverte = false;
        armeRecuperee = false;
        indiceCodetenu = false;
        battreMarco = false;
        sceneBagarre = false;
        quitMenu = false;
        pauseMenu = false;
        nightTime = false;
        activerAlerteNuit = true;
        nightAlertOn = false;
        carteTrouvee = false;
        tentatives = 3;
        gui.cacherBarre(); // On cache la barre du temps
        leSon.jouerAudioConfirm();
        // Arrêt de tous les sons d'ambiance
        leSon.stopAmbianceNuitExterieur();
        leSon.stopAmbianceNuitInterieur();
        leSon.jouerAmbiantThemePrincipal(); // On joue le son d'ambiance du menu principal
    }

    private void sauvegarderPartie() throws IOException {

        posXJoueur = gui.getPosXJoueur();
        posYJoueur = gui.getPosYJoueur();
        // URLJoueur = gui.getURLJoueur(); -> UNE FOIS QUE LA FONCTION MARCHERA,
        // REMPLACER LA LIGNE SUIVANTE :
        URLJoueur = this.getClass().getClassLoader().getResource("jeu/images/sprites/heros/descend.png");

        for (int i = 0; i <= 7; i++) {
            posX_autres.add(gui.getPosX_autre(i));
            posY_autres.add(gui.getPosY_autre(i));
            // URL_autres[i] = gui.getURL_autre(i); -> UNE FOIS QUE LA FONCTION MARCHERA,
            // REMPLACER LA LIGNE SUIVANTE :
            URL_autres.add(this.getClass().getClassLoader().getResource("jeu/images/sprites/null.png"));
        }

        int indexZoneCourante = 0;
        int i = 0;
        for (Zone uneZone : zones) {
            if (zoneCourante == uneZone) {
                indexZoneCourante = i;
            }
            i = i + 1;
        }

        Save sauvegarde = new Save(Inventaire, cinematiqueDeDepart, temporaryPauseHeure, temporaryPauseMinutes,
                coffreOuvert, celluleOuverte, armeRecuperee, indiceCodetenu, battreMarco,
                sceneBagarre, nightTime, activerAlerteNuit, nightAlertOn, carteTrouvee, tentatives,
                indexZoneCourante, posXJoueur, posYJoueur, URLJoueur, posX_autres, posY_autres, URL_autres);

        sauvegarde.enregistrer();
    }

    private String getPartiesSauvegardees() {
        Save sauvegarde = new Save();
        int nbSaves = 0;
        Date dateSauvegarde;
        String pattern = "dd/MM/yyyy - HH:mm";
        SimpleDateFormat formatDate = new SimpleDateFormat(pattern);
        List<File> listOfFiles = new ArrayList<File>();
        File directory = new File("src/jeu/saves/");
        String retour = "Liste des parties sauvegardées : \n\n";
        if (directory.listFiles(sauvegarde.getSaveFileFilter()) != null) {
            for (File f : directory.listFiles(sauvegarde.getSaveFileFilter())) {
                listOfFiles.add(f);
            }
            listOfFiles.sort(Comparator.comparingLong(File::lastModified));
            for (File f : listOfFiles) {
                nbSaves = nbSaves + 1;
                dateSauvegarde = new Date(f.lastModified());
                String laDate = formatDate.format(dateSauvegarde).toString();
                retour += "< " + nbSaves + " > " + laDate + "\t";
            }
        }
        if (nbSaves == -1) {
            retour = "Aucune partie sauvegardée.";
        }
        return retour;
    }

    private void reprendrePartie(int indexPartie) throws ClassNotFoundException, IOException {

        Save sauvegarde = new Save();
        try {
            sauvegarde = sauvegarde.recuperer(indexPartie);
            Inventaire = sauvegarde.Inventaire; // FAUX : RECUP ENUM OBJETS ET LES REMETTRE DANS INV ?
            cinematiqueDeDepart = sauvegarde.cinematiqueDeDepart;
            Temps.setHeure(sauvegarde.heure);
            Temps.setMinutes(sauvegarde.minutes);
            coffreOuvert = sauvegarde.coffreOuvert;
            celluleOuverte = sauvegarde.celluleOuverte;
            armeRecuperee = sauvegarde.armeRecuperee;
            indiceCodetenu = sauvegarde.indiceCodetenu;
            battreMarco = sauvegarde.battreMarco;
            sceneBagarre = sauvegarde.sceneBagarre;
            nightTime = sauvegarde.nightTime;
            activerAlerteNuit = sauvegarde.activerAlerteNuit;
            nightAlertOn = sauvegarde.nightAlertOn;
            carteTrouvee = sauvegarde.carteTrouvee;
            tentatives = sauvegarde.tentatives;
            zoneCourante = zones[sauvegarde.indexZoneCourante];
            gui.afficheImage(zoneCourante.nomImage());
            gui.afficher(zoneCourante.descriptionLongue());
            gui.afficheBarre();
            gui.enleveJoueur();
            gui.afficheJoueur(sauvegarde.directionJoueur, sauvegarde.posXJoueur, sauvegarde.posYJoueur);
            leSon.stopAmbianceThemePrincipal();
        } catch (Exception e) {
            gui.clearText();
            gui.afficher("\nCette sauvegarde n'existe pas.\n\nCommandes : RETOUR");
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
