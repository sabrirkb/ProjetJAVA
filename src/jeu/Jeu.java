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
    private boolean sceneBagarre = false; // si la scène de bagarre du réfectoire s'est déclenchée
    private int PV_Marco = 100; // Points de vie de départ de Marco en cas de bagarre avec le Joueur
    private int PV_Joueur = 100; // Points de vie de départ du joueur en cas de bagarre avec Marco
    private boolean bagarreFinie = false; // si la scène de bagarre est terminée ou non
    private boolean indice2Codetenu = false;
    private boolean quitMenu = false; // si le menu quitter est ouvert
    private boolean pauseMenu = false; // si le menu pause est ouvert
    private boolean mapMenu = false; // si la carte est ouverte
    private boolean noteMenu = false; // si le joueur consulte la note du codétenu
    private boolean cinematiqueActive = false; // si une cinématique se déclenche
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
    private int temporaryPauseXJoueur; // conservie position X Joueur durant menu pause
    private int temporaryPauseYJoueur; // conserve position Y Joueur durant menu pause
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

        creerCarte();
        gui = null;

    }

    // DÉMARRE LE TIMER QUI PERMET À L'HORLOGE DU JEU DE TOURNER ET APPELLE LES
    // EVENEMENTS
    public void StartTime() throws InterruptedException {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!pauseMenu && !quitMenu && !cinematiqueDeDepart && !mapMenu && !noteMenu
                        && !cinematiqueActive && !(zoneCourante == zones[35]) && !(zoneCourante == zones[34])) {
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

        afficherLocalisation();
        gui.afficheImage(zoneCourante.nomImage());
        this.stopSonsAmbiants();
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

        zones[1] = new Zone("l'île de Mors Insula.", "/exterieur/ile/ileJournee.png");

        zones[2] = new Zone("l'île de Mors Insula.", "/exterieur/ile/ileNuit.png");

        zones[3] = new Zone("la cour.", "/exterieur/cour/courJournee.png");

        zones[4] = new Zone("la cour.", "/exterieur/cour/courNuit.png");

        zones[5] = new Zone("la cour.", "/exterieur/cour/courPromenade.png");

        zones[6] = new Zone("le réfectoire.", "/interieur/refectoire/refectoireJournee.png");

        zones[7] = new Zone("le réfectoire.", "/interieur/refectoire/refectoireNuit.png");

        zones[8] = new Zone("le réfectoire.", "/interieur/refectoire/refectoireRepas.png");

        // EXEMPLE D'UNE CINEMATIQUE
        // La cinématique débute lorsque le joueur arrive jusque la zone [9] :
        zones[9] = new Cinematique("Vous êtes dans le réfectoire.\n\nUne bagarre vient d'éclater entre Bart "
                + "et Marco… Après quelques minutes, les gardes viennent séparer les deux "
                + "protagonistes et les conduisent dans leurs cellules respectives.",
                "/interieur/refectoire/refectoireRepas.png");

        zones[10] = new Cinematique("\nLe repas suit son cours lorsqu'un détenu vous murmure :\n"
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

        zones[16] = new Zone("votre cellule. Vous devez attendre que les gardes vous autorisent à sortir…",
                "/interieur/cellule/celluleJour_fermee.png"); 

        zones[17] = new Zone(
                "votre cellule. C'est l'heure de la promenade. Les gardes ouvrent votre cellule pour vous laisser vous rendre dans la cour.",
                "/interieur/cellule/celluleJour.png"); 

        zones[18] = new Zone(
                "votre cellule. C'est l'heure du repas. Les gardes ouvrent votre cellule pour vous laisser vous rendre au réfectoire.",
                "/interieur/cellule/celluleJour.png"); 

        zones[19] = new Cinematique(
                "Vous êtes dans votre cellule. C'est l'heure de la douche. Vous êtes escorté par les gardes jusque dans les douches.",
                "/interieur/cellule/celluleJour.png");

        zones[20] = new Zone("le couloir.", "/interieur/couloir/couloirJour.png");

        zones[21] = new Zone("le couloir.", "/interieur/couloir/couloirNuit.png");

        zones[22] = new Zone("votre cellule. Vous devez attendre que les gardes vous autorisent à sortir…",
                "/interieur/cellule/celluleNuit_fermee.png"); 

        zones[23] = new Zone("les douches.",
                "/interieur/douches/douchesJour.png");

        zones[24] = new Zone("les douches.",
                "/interieur/douches/douchesNuit.png");

        zones[25] = new Cinematique(
                "Vous arrivez dans les douches. Vous vous dirigez vers l'un des seaux disponibles et commencez à vous laver.",
                "/interieur/douches/heureDeLaDouche.png");

        zones[26] = new Zone(
                "la salle des gardes. Vous devriez sortir d'ici avant que quelqu'un ne vous voit!",
                "/interieur/salleDesGardes/salleGardesJour.png");

        zones[27] = new Zone(
                "la salle des gardes. Vous devriez sortir d'ici avant de réveiller les gardes!",
                "/interieur/salleDesGardes/salleGardesNuit.png");

        zones[28] = new Cinematique(
                "\nVous avez été repéré par la garde royale! Ces derniers vous reconduisent de force"
                        + " dans votre cellule après vous avoir donné un avertissement…\n",
                "/cinematiques/avertissement.png");

        zones[29] = new Zone("la cuisine.", "/interieur/cuisine/cuisineJour.png");

        zones[30] = new Zone("la cuisine.", "/interieur/cuisine/cuisineNuit.png");

        zones[31] = new Cinematique(
                "Vous terminez de vous laver et êtes reconduit par les gardes dans vos cellules respectives.",
                "/interieur/douches/heureDeLaDouche.png");

        zones[32] = new Cinematique(
                "\nVous arrivez dans les douches. Vous vous dirigez vers l'un des seaux disponibles et commencez à vous laver,"
                        + "\nlorsqu'un mystérieux personnage se dirige vers vous…",
                "/interieur/douches/heureDeLaDouche.png");

        zones[33] = new Cinematique(
                "Marco : « Hé le nouveau… La forme? Moi, c'est Marco. J'espère qu'on va bien s'entendre toi et moi, "
                        + "sinon tu vas vraiment regretter ton séjour sur cette île, haha! »\n"
                        + "\nIntimidé, vous hésitez entre informer Marco sur le guet–apens ou ne rien faire…",
                "/interieur/douches/DouchesMarco.png");

        zones[34] = new Cinematique("Percival Lebrave a désobéit de trop nombreuses fois à la garde royale."
                + " Il a déshonoré le royaume d'Aeternum. Son mauvais comportement le mènera à sa propre perte.\n"
                + "\nQue ton âme repose en paix, jeune Percival. \nTu vas nous manquer.",
                "/cinematiques/gameOver.png");

        zones[35] = new Cinematique("\nMerci d'avoir joué.\n\n",
                "/cinematiques/credits.png");

        zones[36] = new Cinematique("Vous sortez de votre cellule pour vous diriger vers le couloir "
                + "lorsqu'un codétenu vous arrête… \n\nCodétenu : « Pssst! Hé, toi! T'es nouveau? J'ai besoin d'un p'tit service. "
                + "Tous les jours, à 18 heures, les gardes passent pour nous conduire dans les douches…",
                "/interieur/couloir/couloirJour_indice.png");

        zones[37] = new Cinematique("…René prévoit de donner une leçon à Marco pendant le prochain repas de midi dans "
                + "le réfectoire. Quand tu croiseras Marco dans les douches, donne lui cette info. Mais ne lui parle pas de moi, "
                + "t'as bien compris ?", "/interieur/couloir/couloirJour_indice.png");

        zones[38] = new Cinematique("Au fait, avant que j'oublie… Tiens, prends ça. C'est une note sur "
                + "laquelle j'ai marqué à quelle heure les gardes passent la nuit dans chaque pièce. "
                + "Surtout, te fais pas chopper… ils sont pas là pour plaisanter, ici! »",
                "/interieur/couloir/couloirJour_indice.png");

        zones[39] = new Cinematique("Vous prenez la note et la placez dans votre inventaire tandis "
                + "que le mystérieux codétenu disparaît furtivement.\n\n"
                + "Tapez 'Inventaire' à tout moment pour consulter votre inventaire.",
                "/interieur/couloir/couloirJour.png");

        zones[40] = new Cinematique("C'est l'heure de la douche. Tous les détenus sont escortés par "
                + "les gardes jusque dans les douches.", "/interieur/couloir/couloirJour.png");

        zones[41] = new Cinematique("", "/cinematiques/map.png");

        zones[42] = new Cinematique("\nVous arrivez dans la salle des gardes. Sur la table, vous trouvez une carte "
                + "de la prison. Vous la récupérez rapidement et la placez dans votre inventaire.\n\nTapez 'Carte' à tout moment pour ouvrir la carte.",
                "/interieur/salleDesGardes/salleGardesNuit_Map.png"); 

        zones[43] = new Cinematique("Marco : « Bah alors, t'as perdu la parole, petit vaurien ? "
                + "Haha! C'est ça, baisse les yeux quand j'te cause! Tu sais quoi? Rendez-vous ce soir au réfectoire, "
                + "qu'on puisse célébrer ton arrivée, si tu vois c'que j'veux dire… trouillard! »",
                "/interieur/douches/DouchesMarco.png");

        zones[44] = new Cinematique("Marco vous donne une petite tape sur l'épaule avant de faire "
                + "demi-tour et de sortir des douches…",
                "/interieur/douches/DouchesMarco.png");

        zones[45] = new Cinematique("Vous dites à Marco qu'il est attendu au réfectoire par René pour "
                + "une rixe sans précédent.\n\nMarco : « Attends… t'es sûr de toi là? J'te préviens, si jamais tu oses "
                + "mentir au gran' Marco, c'est toi qu'on aura au menu ce soir! »",
                "/interieur/douches/DouchesMarco.png"); 

        zones[46] = new Cinematique("Marco : « J'espère que t'as bien saisi l'info, petit vaurien. »"
                + "\n\nVous acquiescez timidement, tandis que Marco fait demi-tour et sort des douches…",
                "/interieur/douches/DouchesMarco.png"); 

        zones[47] = new Cinematique("Ce soir, exceptionnellement, les gardes vous forcent à dîner et vous escortent jusque dans le "
                + "réfectoire de la prison…",
                "/interieur/refectoire/refectoireRepas_Premiere_Journee.png"); 

        zones[48] = new Cinematique(
                "Vous avancez vers l'une des tables, lorsque Marco, défiguré, surgit devant vous…\n\n"
                        + "Marco : « Alors, moucheron… tu m'as oublié? Viens par là, que j'te montre à toi aussi qui est le patron de cette taule! »"
                        + "\n\nUn combat éclate entre vous et Marco…",
                "/interieur/refectoire/BagarreReneMarco.png"); 

        zones[49] = new Cinematique("Marco vous colle une giffle.",
                "/interieur/refectoire/BagarreReneMarco.png"); 

        zones[50] = new Cinematique("Quelle tactique doit utiliser Percival ?",
                "/interieur/refectoire/BagarreReneMarco.png"); 

        zones[62] = new Cinematique("",
                "/interieur/refectoire/BagarreReneMarco.png"); 

        zones[63] = new Cinematique("Marco esquive votre coup.",
                "/interieur/refectoire/BagarreReneMarco.png"); 

        zones[51] = new Cinematique("Vous esquivez le prochain coup et reprenez des forces…",
                "/interieur/refectoire/BagarreReneMarco.png"); 

        zones[52] = new Cinematique("Marco dégaine un rasoir et le plante dans votre avant-bras.",
                "/interieur/refectoire/BagarreReneMarco.png"); 

        zones[53] = new Cinematique("Vous sortez votre couteau et essayez de blesser Marco.",
                "/interieur/refectoire/BagarreReneMarco.png"); 

        zones[54] = new Cinematique("Votre coup est très efficace.",
                "/interieur/refectoire/BagarreReneMarco.png"); 

        zones[55] = new Cinematique("Marco vous assène un gros coup de poing dans le ventre.",
                "/interieur/refectoire/BagarreReneMarco.png"); 

        zones[56] = new Cinematique("Votre coup blesse légèrement Marco.",
                "/interieur/refectoire/BagarreReneMarco.png"); 

        zones[57] = new Cinematique("Vous essayez de vous défendre. Marco enchaîne avec un coup et vous blesse un peu plus légèrement.",
                "/interieur/refectoire/BagarreReneMarco.png"); 

        zones[58] = new Cinematique("\nMarco tombe au sol, ensanglanté…\n\nLes gardes arrivent rapidement et "
                + "conduisent Marco jusque dans leur salle afin de le soigner. Suite à ce combat, les gardes vous"
                + " adressent un avertissement.",
                "/interieur/refectoire/refectoireRepas_Marco_tombe.png"); 

        zones[59] = new Cinematique(
                "\nVous tombez au sol, ensanglanté…\n\nLes gardes arrivent rapidement pour vous séparer. "
                        + "Vous luttez tant bien que mal pour rester éveillé, mais vous vous sentez de plus en plus faible…",
                "/interieur/refectoire/refectoireRepas_Heros_tombe.png"); 

        zones[60] = new Cinematique(
                "Malgré ses efforts, Percival ne parviendra pas à se remettre d'aplomb.",
                "/interieur/refectoire/refectoireRepas_heros_mort.png"); 

        zones[61] = new Cinematique("Percival Lebrave décédera des suites du combat avec Marco."
                + " La garde royale viendront enterrer son corps dans la forêt de Mors Insula.\n"
                + "\nQue ton âme repose en paix, jeune Percival. \nTu vas nous manquer.",
                "/cinematiques/gameOver.png");

        zones[64] = new Cinematique(
                "\nVous arrivez dans la cuisine. Sur le plan de travail, vous apercevez un objet qui ressemble à une clé.\n"
                        + "\nCurieux, vous décidez de prendre la clé et de la placer dans votre inventaire.",
                "/interieur/cuisine/cuisineJour_Clef.png"); 

        zones[65] = new Cinematique("", "/cinematiques/note.png");

        zones[66] = new Cinematique(
                "\nVous vous dirigez vers le coffre. La clé récupérée dans la cuisine semble l'ouvrir. "
                + "À l'intérieur, vous y trouvez une autre clé… Intruigué, vous la placez rapidement dans votre inventaire.\n\n"
                + "Tapez 'Inventaire' à tout moment pour consulter votre inventaire.",
                "/cinematiques/gameOver.png"); // IMAGE A MODIFIER

        zones[67] = new Cinematique("\nVous prenez la clé de votre inventaire et essayez d'ouvrir votre cellule."
        + " Cela semble fonctionner! \n\nVous pouvez tenter une évasion, mais faites attention aux gardes!",
        "/interieur/cellule/celluleNuit.png"); 

        zones[68] = new Zone("votre cellule. La porte est ouverte.\n\nVous pouvez tenter une évasion,"
        + " mais faites attention aux gardes!","/interieur/cellule/celluleNuit.png"); 

        zones[69] = new Cinematique("\nPercival Lebrave sort de la prison. "
        + "Il aperçoit une barque amarrée sur le pont. Il court aussitôt vers celle-ci et monte "
        + "à bord. Il est enfin temps de lever l'encre!\n\nÀ toi la liberté, jeune Percival!"
        ,"/exterieur/ile/ileNuit.png"); 

        zones[70] = new Cinematique("","/cinematiques/fuite.png");

        zones[71] = new Cinematique("\nVous arrivez dans les douches. En marchant, vous trouvez un couteau au sol… "
        + "Surpris, vous décidez de prendre ce dernier et le placez rapidement dans votre inventaire.\n\n"
        + "Tapez 'Inventaire' à tout moment pour consulter votre inventaire.",
        "/interieur/douches/douchesJour.png");


        // zones[3].ajouteSortie(Sortie.OUEST, zones[9]); // Affecte le déclenchement de
        // la cinématique à zones[3]
        // si le joueur prend la sortie 'OUEST'

        // AJOUT DES SORTIES ET DES COMMANDES AUX ZONES/CINEMATIQUES DU JEU

        zones[1].ajouteSortie(Sortie.NORD, zones[3]);

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
        zones[8].ajouteSortie(Sortie.OUEST, zones[29]);

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

        zones[32].ajouteAction(Action.SUIVANT, zones[33]);

        zones[33].ajouteAction(Action.INFORMER, zones[45]);
        zones[33].ajouteAction(Action.RIEN, zones[43]);

        zones[36].ajouteAction(Action.SUIVANT, zones[37]);

        zones[37].ajouteAction(Action.SUIVANT, zones[38]);

        zones[38].ajouteAction(Action.SUIVANT, zones[39]);

        zones[39].ajouteAction(Action.OK, zones[20]);

        zones[40].ajouteAction(Action.SUIVANT, zones[25]);

        zones[42].ajouteAction(Action.OK, zones[26]);

        zones[43].ajouteAction(Action.SUIVANT, zones[44]);

        zones[44].ajouteAction(Action.OK, zones[31]);

        zones[45].ajouteAction(Action.SUIVANT, zones[46]);

        zones[46].ajouteAction(Action.OK, zones[31]);

        zones[47].ajouteAction(Action.SUIVANT, zones[8]);

        zones[48].ajouteAction(Action.SUIVANT, zones[49]);

        zones[49].ajouteAction(Action.SUIVANT, zones[50]);

        zones[50].ajouteAction(Action.ATTAQUE, zones[50]);
        zones[50].ajouteAction(Action.DEFENSE, zones[50]);
        zones[50].ajouteAction(Action.ARME, zones[50]);

        zones[58].ajouteAction(Action.OK, zones[10]);

        zones[59].ajouteAction(Action.SUIVANT, zones[60]);

        zones[60].ajouteAction(Action.SUIVANT, zones[61]);

        zones[64].ajouteAction(Action.OK, zones[29]);

        zones[66].ajouteAction(Action.OK, zones[26]);

        zones[67].ajouteAction(Action.OK, zones[68]);

        zones[68].ajouteAction(Action.DORMIR, zones[16]);
        zones[68].ajouteSortie(Sortie.SUD, zones[20]);

        zones[69].ajouteAction(Action.OK, zones[70]);

        zones[71].ajouteAction(Action.OK, zones[23]);

        // AFFECTATION DE LA ZONE COURANTE -> DEBUT DU JEU -> ZONE COURANTE = MENU
        // PRINCIPAL (zones[0])
        zoneCourante = zones[0];

        // Temps.setTime(14, 15); // Permettra de changer l'heure de départ du jeu
        // lorsque
        // nous effectuerons les TIC (Tests d'Intégration Continus)

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
        cinematiqueActive = false;
    }

    public void traiterCommande(String commandeLue)
            throws UnsupportedAudioFileException, InterruptedException, IOException, LineUnavailableException {
        gui.clearText();
        gui.afficher("> " + commandeLue + "\n\n");
        switch (commandeLue.toUpperCase()) {

            case "ATTAQUE":
                if (sceneBagarre && zoneCourante == zones[50]) {
                    effectuerAttaque();
                    leSon.jouerAudioConfirm();
                    break;
                } else {
                    gui.afficher("La commande " + commandeLue + " n'est pas disponible.");
                    gui.afficher("\n\nTapez 'Localiser' pour obtenir les détails de la zone courante.\n\n");
                    gui.afficher(zoneCourante.commandesDispo());
                    leSon.jouerAudioError();
                    break;
                }

            case "DEFENSE":
                if (sceneBagarre && zoneCourante == zones[50]) {
                    effectuerDefense();
                    leSon.jouerAudioConfirm();
                    break;
                } else {
                    gui.afficher("La commande " + commandeLue + " n'est pas disponible.");
                    gui.afficher("\n\nTapez 'Localiser' pour obtenir les détails de la zone courante.\n\n");
                    gui.afficher(zoneCourante.commandesDispo());
                    leSon.jouerAudioError();
                    break;
                }

            case "ARME":
                if (sceneBagarre && armeRecuperee && zoneCourante == zones[50]) {
                    effectuerArme();
                    leSon.jouerAudioConfirm();
                    break;
                } else if (sceneBagarre && !armeRecuperee && zoneCourante == zones[50]) {
                    gui.afficher("\nVous ne possédez pas d'arme pour attaquer Marco.");
                    gui.afficher("\nQuelle tactique doit utiliser Percival ?\n\n");
                    gui.afficher(zoneCourante.commandesDispo());
                    leSon.jouerAudioError();
                    break;
                } else {
                    gui.afficher("La commande " + commandeLue + " n'est pas disponible.");
                    gui.afficher("\n\nTapez 'Localiser' pour obtenir les détails de la zone courante.\n\n");
                    gui.afficher(zoneCourante.commandesDispo());
                    leSon.jouerAudioError();
                    break;
                }

            case "MAP":
            case "M":
            case "CARTE":
            case "C":
                if (nightAlertOn) {
                    gui.afficher("La commande " + commandeLue + " n'est pas disponible.\n");
                    gui.afficher("\nCommandes disponibles : OK");
                    break;
                }
                if (carteTrouvee) {
                    mapMenu = true;
                    temporaryPauseXJoueur = gui.getPosXJoueur(); // on conserve la position x du joueur
                    temporaryPauseYJoueur = gui.getPosYJoueur(); // on conserve la position y du joueur
                    gui.afficheJoueur("NONE", 150, 150); // On cache le joueur
                    temporaryPauseHeure = Temps.getHeure();
                    temporaryPauseMinutes = Temps.getMinutes();
                    temporaryPauseZone = zoneCourante;
                    zoneCourante = zones[41]; // On set la zone courante sur la zone <Map>
                    gui.afficheImage(zoneCourante.nomImage()); // on affiche l'image correspondant à la zone
                    gui.afficher("Vous ouvrez la carte du jeu.\n\nListe des commmandes disponibles :\n");
                    gui.afficher("\nJOUER");
                    leSon.jouerAudioMenuON();
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
                if (nightAlertOn) {
                    gui.afficher("La commande " + commandeLue + " n'est pas disponible.\n");
                    gui.afficher("\nCommandes disponibles : OK");
                    break;
                }
                afficherAide();
                break;
            case "N":
            case "NORD":
                if (nightAlertOn) {
                    gui.afficher("La commande " + commandeLue + " n'est pas disponible.\n");
                    gui.afficher("\nCommandes disponibles : OK");
                    break;
                }
                if (cinematiqueActive || pauseMenu || mapMenu || noteMenu || nightAlertOn) 
                {
                    gui.afficher("Commande indisponible durant une cinématique.\n\n");
                    gui.afficher(zoneCourante.commandesDispo());
                    leSon.jouerAudioError();
                    break;
                }
                else
                    allerEn("NORD");
                break;
            case "L":
            case "LOCALISATION":
            case "LOCALISATIONS":
            case "LOCALISER":
            case "LOC":
            case "LOCAL":
                if (nightAlertOn) {
                    gui.afficher("La commande " + commandeLue + " n'est pas disponible.\n");
                    gui.afficher("\nCommandes disponibles : OK");
                    break;
                }
                if (pauseMenu || mapMenu || noteMenu || nightAlertOn) 
                {
                    gui.afficher("Commande indisponible durant une cinématique.\n\n");
                    gui.afficher(zoneCourante.commandesDispo());
                    leSon.jouerAudioError();
                    break;
                }
                else
                    afficherLocalisation();
                break;
            case "S":
            case "SUD":
                if (nightAlertOn) {
                    gui.afficher("La commande " + commandeLue + " n'est pas disponible.\n");
                    gui.afficher("\nCommandes disponibles : OK");
                    break;
                }
                if (cinematiqueActive || pauseMenu || mapMenu || noteMenu || nightAlertOn) 
                {
                    gui.afficher("Commande indisponible durant une cinématique.\n\n");
                    gui.afficher(zoneCourante.commandesDispo());
                    leSon.jouerAudioError();
                    break;
                }
                else
                    allerEn("SUD");
                break;
            case "E":
            case "EST":
                if (nightAlertOn) {
                    gui.afficher("La commande " + commandeLue + " n'est pas disponible.\n");
                    gui.afficher("\nCommandes disponibles : OK");
                    break;
                }
                if (cinematiqueActive || pauseMenu || mapMenu || noteMenu || nightAlertOn) 
                {
                    gui.afficher("Commande indisponible durant une cinématique.\n\n");
                    gui.afficher(zoneCourante.commandesDispo());
                    leSon.jouerAudioError();
                    break;
                }
                else
                    allerEn("EST");
                break;
            case "O":
            case "OUEST":
                if (nightAlertOn) {
                    gui.afficher("La commande " + commandeLue + " n'est pas disponible.\n");
                    gui.afficher("\nCommandes disponibles : OK");
                    break;
                }
                if (cinematiqueActive || pauseMenu || mapMenu || noteMenu || nightAlertOn) 
                {
                    gui.afficher("Commande indisponible durant une cinématique.\n\n");
                    gui.afficher(zoneCourante.commandesDispo());
                    leSon.jouerAudioError();
                    break;
                }
                else
                    allerEn("OUEST");
                break;
            case "FUIR": case "FUITE": case "EVASION":
            if (cinematiqueActive || pauseMenu || mapMenu || noteMenu || nightAlertOn) 
                {
                    gui.afficher("Commande indisponible durant une cinématique.\n\n");
                    gui.afficher(zoneCourante.commandesDispo());
                    leSon.jouerAudioError();
                    break;
                }
                if (zoneCourante == zones[4] && (Inventaire.contains(Objets.CLE2))) {
                    this.fuir();
                    break;
                }
                else {
                    gui.afficher("La commande " + commandeLue + " n'est pas disponible.");
                    gui.afficher("\n\nTapez 'Localiser' pour obtenir les détails de la zone courante.\n\n");
                    gui.afficher(zoneCourante.commandesDispo());
                    leSon.jouerAudioError();
                    break;
                }
            case "OUVRIR":
                if (cinematiqueActive || pauseMenu || mapMenu || noteMenu || nightAlertOn) 
                {
                    gui.afficher("Commande indisponible durant une cinématique.\n\n");
                    gui.afficher(zoneCourante.commandesDispo());
                    leSon.jouerAudioError();
                    break;
                }
                if (zoneCourante == zones[22] && (Inventaire.contains(Objets.CLE2))) {
                    this.ouvrirCellule();
                    break;
                }
                if (zoneCourante == zones[26] && Inventaire.contains(Objets.CARTE) && Inventaire.contains(Objets.CLE1) && !(Inventaire.contains(Objets.CLE2)))
                {
                    this.ouvrirCoffre();
                    break;
                }
                else {
                    gui.afficher("La commande " + commandeLue + " n'est pas disponible.");
                    gui.afficher("\n\nTapez 'Localiser' pour obtenir les détails de la zone courante.\n\n");
                    gui.afficher(zoneCourante.commandesDispo());
                    leSon.jouerAudioError();
                    break;
                }
            case "Q":
            case "QUITTER":
            case "QUIT":
                if (nightAlertOn) {
                    gui.afficher("La commande " + commandeLue + " n'est pas disponible.\n");
                    gui.afficher("\nCommandes disponibles : OK");
                    break;
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
                if (nightAlertOn) {
                    gui.afficher("La commande " + commandeLue + " n'est pas disponible.\n");
                    gui.afficher("\nCommandes disponibles : OK");
                    break;
                }
                if (!cinematiqueActive || !pauseMenu || !mapMenu || !noteMenu || !nightAlertOn) 
                {
                    temporaryPauseHeure = Temps.getHeure();
                    temporaryPauseMinutes = Temps.getMinutes();
                    temporaryPauseZone = zoneCourante;
                    checkInventaire();
                }
                else {
                    gui.afficher("La commande " + commandeLue + " n'est pas disponible.");
                    gui.afficher("\n\nTapez 'Localiser' pour obtenir les détails de la zone courante.\n\n");
                    gui.afficher(zoneCourante.commandesDispo());
                    leSon.jouerAudioError();
                }
                break;
            case "YES":
                if (nightAlertOn) {
                    gui.afficher("La commande " + commandeLue + " n'est pas disponible.\n");
                    gui.afficher("\nCommandes disponibles : OK");
                    break;
                }
                if (quitMenu) {
                    if (zoneCourante != zones[0]) // Si on ne se trouve pas dans le menu principal
                    {
                        this.retourMenuPrincipal();
                        break;
                    }
                    if (zoneCourante == zones[0]) {
                        zoneCourante = zones[35];
                        gui.afficheImage(zoneCourante.nomImage());
                        gui.afficher(zoneCourante.description);
                        gui.cacher(10);
                        gui.cacherBarre();
                        leSon.jouerAudioConfirm();

                        Timer chrono = new Timer();
                        chrono.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                terminer();
                            }
                        }, 1500);

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
                if (nightAlertOn) {
                    gui.afficher("La commande " + commandeLue + " n'est pas disponible.\n");
                    gui.afficher("\nCommandes disponibles : OK");
                    break;
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
            case "SUIVANT":
            case "SUIV":
                if ((zoneCourante == zones[19] || zoneCourante == zones[40]) && !battreMarco && !sceneBagarre && Inventaire.contains(Objets.NOTE)) {
                    Timer chrono = new Timer();
                    chrono.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            cinematiqueActive = true;
                            zoneCourante = zones[32];
                            gui.clearText();
                            gui.afficheImage(zoneCourante.nomImage());
                            gui.afficher(zoneCourante.descriptionLongue());
                            try {
                                leSon.jouerAudioDialogue();
                            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                                e.printStackTrace();
                            }
                        }
                    }, 050);
                }
                if (zoneCourante.getClass() == Cinematique.class && zoneCourante.sorties.containsKey("SUIVANT")) {
                    if (nightAlertOn) {
                        gui.afficher("La commande " + commandeLue + " n'est pas disponible.\n");
                        gui.afficher("\nCommandes disponibles : OK");
                        break;
                    }
                    nextScene("SUIVANT");
                    break;
                } else {
                    gui.afficher("La commande " + commandeLue + " n'est pas disponible.");
                    gui.afficher("\n\nTapez 'Localiser' pour obtenir les détails de la zone courante.\n\n");
                    gui.afficher(zoneCourante.commandesDispo());
                    leSon.jouerAudioError();
                    break;
                }

            case "INFORMER":
            case "INFO":
                if (zoneCourante.getClass() == Cinematique.class && zoneCourante.sorties.containsKey("INFORMER")) {
                    if (nightAlertOn) {
                        gui.afficher("La commande " + commandeLue + " n'est pas disponible.\n");
                        gui.afficher("\nCommandes disponibles : OK");
                        break;
                    }
                    nextScene("INFORMER");
                    break;
                } else {
                    gui.afficher("La commande " + commandeLue + " n'est pas disponible.");
                    gui.afficher("\n\nTapez 'Localiser' pour obtenir les détails de la zone courante.\n\n");
                    gui.afficher(zoneCourante.commandesDispo());
                    leSon.jouerAudioError();
                    break;
                }

            case "RIEN":
                if (zoneCourante.getClass() == Cinematique.class && zoneCourante.sorties.containsKey("RIEN")) {
                    if (nightAlertOn) {
                        gui.afficher("La commande " + commandeLue + " n'est pas disponible.\n");
                        gui.afficher("\nCommandes disponibles : OK");
                    break;
                    }
                    nextScene("RIEN");
                    break;
                } else {
                    gui.afficher("La commande " + commandeLue + " n'est pas disponible.");
                    gui.afficher("\n\nTapez 'Localiser' pour obtenir les détails de la zone courante.\n\n");
                    gui.afficher(zoneCourante.commandesDispo());
                    leSon.jouerAudioError();
                    break;
                }

            case "OK":

            if (zoneCourante == zones[71]) {
                cinematiqueActive = false;
            }

            if (zoneCourante == zones[69]) {
                cinematiqueActive = false;
            }

            if (zoneCourante == zones[67]) {
                cinematiqueActive = false;
            }

            if (zoneCourante == zones[66]) {
                cinematiqueActive = false;
            }

            if (zoneCourante == zones[39]) {
                cinematiqueActive = false;
            }

                if (zoneCourante == zones[42]) {
                    cinematiqueActive = false;
                }

                if (!sceneBagarre) {
                    cinematiqueActive = false;
                }

                if (nightAlertOn) {
                    cinematiqueActive = false;
                    nightAlertOn = false;
                }

                if (zoneCourante == zones[10]) {
                    cinematiqueActive = false;
                }

                if (zoneCourante == zones[31]) {
                    cinematiqueActive = false;
                    leSon.stopAmbianceDouches();
                }

                if (zoneCourante == zones[44]) {
                    battreMarco = true;
                }

                if (nightAlertOn) // Si le joueur tape 'OK' pour effacer l'alerte de nuit
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
                }
                if (zoneCourante == zones[34] || zoneCourante == zones[61])
                {
                    zoneCourante = zones[35];
                    gui.afficheImage(zoneCourante.nomImage());
                    gui.afficher(zoneCourante.description);
                    gui.afficheJoueur("NONE", 150, 150);
                }
                if (zoneCourante == zones[35]) {
                    gui.afficheJoueur("NONE", 150, 150);
                    gui.cacherBarre();
                    gui.clearText();
                    gui.afficheImage(zoneCourante.nomImage());
                    gui.afficher(zoneCourante.description);
                    gui.afficher("Retour au menu principal…");

                    Timer chrono = new Timer();
                    chrono.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            try {
                                retourMenuPrincipal();
                                leSon.stopAmbianceTheEnd();
                            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                                terminer();
                            }
                        }
                    }, 7000);

                    break;

                } else { // Sinon, on tente de passer à la scène suivante
                    try {
                        nextScene("OK");
                        break;
                    } catch (Exception e) {
                        gui.afficher("La commande " + commandeLue + " n'est pas disponible.");
                        gui.afficher("\n\nTapez 'Localiser' pour obtenir les détails de la zone courante.\n\n");
                        gui.afficher(zoneCourante.commandesDispo());
                        leSon.jouerAudioError();
                        break;
                    }
                }

            case "DORMIR":
                if (nightAlertOn) {
                    gui.afficher("Impossible de dormir pour le moment.\n");
                    gui.afficher("\nCommandes disponibles : OK");
                    break;
                }
                if ((zoneCourante == zones[16]) || (zoneCourante == zones[17])
                        || (zoneCourante == zones[18] || zoneCourante == zones[22])) {
                    dormir();
                    leSon.jouerAudioDormir();
                    gui.afficher("\n" + zoneCourante.descriptionLongue());
                } else {
                    gui.afficher(
                            "Impossible de dormir pour le moment.");
                    gui.afficher("\n\nTapez 'Localiser' pour obtenir les détails de la zone courante.\n\n");
                    gui.afficher(zoneCourante.commandesDispo());
                    leSon.jouerAudioError();
                }
                break;

            case "NOTE":
                if (indiceCodetenu) {
                    // zoneCourante = zones[] -> A CREER : IMAGE PAPIER MANUSCRIT -> HEURE GARDES DE
                    // NUIT
                    noteMenu = true;
                    temporaryPauseXJoueur = gui.getPosXJoueur(); // on conserve la position x du joueur
                    temporaryPauseYJoueur = gui.getPosYJoueur(); // on conserve la position y du joueur
                    gui.afficheJoueur("NONE", 150, 150); // On cache le joueur
                    temporaryPauseHeure = Temps.getHeure();
                    temporaryPauseMinutes = Temps.getMinutes();
                    temporaryPauseZone = zoneCourante;
                    zoneCourante = zones[65]; // On set la zone courante sur la zone <Note>
                    gui.afficheImage(zoneCourante.nomImage()); // on affiche l'image correspondant à la zone
                    gui.afficher("Vous lisez la note du codétenu.\n\nListe des commmandes disponibles :\n");
                    gui.afficher("\nJOUER");
                    leSon.jouerAudioMenuON();
                } else {
                    gui.afficher("La commande " + commandeLue + " n'est pas disponible.");
                    gui.afficher("\n\nTapez 'Localiser' pour obtenir les détails de la zone courante.\n\n");
                    gui.afficher(zoneCourante.commandesDispo());
                    leSon.jouerAudioError();
                }
                break;

            case "TEMPS":
            case "T":
            case "TIME":
                if (nightAlertOn) {
                    gui.afficher("La commande " + commandeLue + " n'est pas disponible.\n");
                    gui.afficher("\nCommandes disponibles : OK");
                    break;
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
                    cinematiqueActive = false;
                    isReprendreActive = true;
                    gui.afficher(this.getPartiesSauvegardees());
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
                if (nightAlertOn) {
                    gui.afficher("La commande " + commandeLue + " n'est pas disponible.\n");
                    gui.afficher("\nCommandes disponibles : OK");
                    break;
                }
                if (!cinematiqueDeDepart) {
                    pauseMenu = true;
                    gui.cacherBarre();
                    temporaryPauseXJoueur = gui.getPosXJoueur(); // on conserve la position x du joueur
                    temporaryPauseYJoueur = gui.getPosYJoueur(); // on conserve la position y du joueur
                    temporaryPauseHeure = Temps.getHeure();
                    temporaryPauseMinutes = Temps.getMinutes();
                    temporaryPauseZone = zoneCourante;
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
                if (nightAlertOn)
                {
                    gui.afficher("La commande " + commandeLue + " n'est pas disponible pour le moment.\n");
                    gui.afficher("\nCommandes disponibles : OK");
                    break;
                }
                if ((pauseMenu || mapMenu || noteMenu || cinematiqueActive) && zoneCourante != zones[28]) {
                    gui.afficheJoueur("SUD", temporaryPauseXJoueur, temporaryPauseYJoueur);
                    gui.afficheBarre();
                    Temps.setHeure(temporaryPauseHeure);
                    Temps.setMinutes(temporaryPauseMinutes);
                    zoneCourante = temporaryPauseZone;
                    gui.afficheImage(zoneCourante.nomImage());
                    afficherLocalisation();
                    pauseMenu = false;
                    mapMenu = false;
                    noteMenu = false;
                    cinematiqueActive = false;
                    nightAlertOn = false;
                    leSon.jouerAudioMenuOFF();
                }
                else {
                    gui.afficher("La commande " + commandeLue + " n'est pas disponible.");
                    gui.afficher("\n\nTapez 'Localiser' pour obtenir les détails de la zone courante.\n\n");
                    gui.afficher(zoneCourante.commandesDispo());
                    leSon.jouerAudioError();
                }
                break;
            case "SAUVEGARDER":
            case "SAVE":
                if (pauseMenu || quitMenu) {
                    // Si nb sauvegardes > 10 -> message d'erreur + retour menu pause + break case;
                    if (this.getNbSauvegardes() >= 9) {
                        gui.clearText();
                        gui.afficher("\nVous ne pouvez pas sauvegarder plus de 9 parties!\n"
                                + "Supprimez une sauvegarde depuis le répertoire 'Saves' ou réessayez.\n"
                                + "\nRetour au menu pause…\n");
                        leSon.jouerAudioError();
                        Timer chrono2 = new Timer();
                        chrono2.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                try {
                                    Temps.setHeure(temporaryPauseHeure);
                                    Temps.setMinutes(temporaryPauseMinutes);
                                    traiterCommande("PAUSE");
                                } catch (UnsupportedAudioFileException | IOException
                                        | LineUnavailableException
                                        | InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, 7500);
                        break;
                    }
                    // Sinon on affiche le message suivant et on tente de sauvegarder dans le
                    // try/catch
                    gui.afficher("Sauvegarde en cours. Veuillez patienter…\n");
                    leSon.jouerAudioMenuON();
                    try {
                        Timer chrono2 = new Timer();
                        chrono2.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                try {
                                    sauvegarderPartie();
                                    gui.afficher("\nSauvegarde effectuée avec succès.\nRetour au menu pause…");
                                    Temps.setHeure(temporaryPauseHeure);
                                    Temps.setMinutes(temporaryPauseMinutes);
                                    leSon.jouerAudioMenuSuccess();

                                    Timer chrono2 = new Timer();
                                    chrono2.schedule(new TimerTask() {
                                        @Override
                                        public void run() {
                                            try {
                                                leSon.jouerAudioConfirm();
                                                traiterCommande("PAUSE");
                                            } catch (UnsupportedAudioFileException | IOException
                                                    | LineUnavailableException
                                                    | InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }, 2500);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }, 1500);
                        break;
                    } catch (Exception e) {
                        gui.clearText();
                        gui.afficher("\nErreur durant la sauvegarde, retour au menu pause…\n"
                                + "\nMessage d'erreur: " + e.getMessage());
                        leSon.jouerAudioError();
                        Timer chrono2 = new Timer();
                        chrono2.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                try {
                                    Temps.setHeure(temporaryPauseHeure);
                                    Temps.setMinutes(temporaryPauseMinutes);
                                    traiterCommande("PAUSE");
                                } catch (UnsupportedAudioFileException | IOException
                                        | LineUnavailableException
                                        | InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, 2500);
                        break;
                    }
                } else {
                    gui.afficher("La commande " + commandeLue + " n'est pas disponible.");
                    gui.afficher("\n\nTapez 'Localiser' pour obtenir les détails de la zone courante.\n\n");
                    gui.afficher(zoneCourante.commandesDispo());
                    leSon.jouerAudioError();
                    break;
                }
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
        if ((Inventaire.contains(Objets.CLE1)) && zoneCourante == zones[26] && Temps.getHeure() >= 8 && Temps.getHeure() < 11)
        {
                cinematiqueActive = true;
                zoneCourante = zones[66];
                gui.clearText();
                gui.afficheImage(zoneCourante.nomImage());
                gui.afficher(zoneCourante.descriptionLongue());
                leSon.joueurAudioPorteOuverte();
                leSon.jouerAudioAchievement();
                Inventaire.add(Objets.CLE2);
        } else {
            gui.afficher("Vous ne possédez pas la clé du coffre.");
            gui.afficher("\nCommandes disponibles : " + zoneCourante.commandesDispo());
            leSon.jouerAudioError();
        }
    }

    private void ouvrirCellule() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        if ((Inventaire.contains(Objets.CLE2)) && zoneCourante == zones[22]
            && Temps.getHeure() >= 22 || (Temps.getHeure() >= 0 && Temps.getHeure() < 8))
        {
                cinematiqueActive = true;
                zoneCourante = zones[67];
                gui.clearText();
                gui.afficheImage(zoneCourante.nomImage());
                gui.afficher(zoneCourante.descriptionLongue());
                leSon.joueurAudioPorteOuverte();
                leSon.jouerAudioAchievement();
                Inventaire.add(Objets.CLE2);
        } else {
            gui.afficher("Vous ne possédez pas la clé de la prison.");
            gui.afficher("\nCommandes disponibles : " + zoneCourante.commandesDispo());
            leSon.jouerAudioError();
        }
    }

    private void fuir() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        if ((Inventaire.contains(Objets.CLE2)) && zoneCourante == zones[4]
            && Temps.getHeure() >= 22 || (Temps.getHeure() >= 0 && Temps.getHeure() < 8))
        {
                cinematiqueActive = true;
                zoneCourante = zones[2];
                gui.clearText();
                gui.afficheImage(zoneCourante.nomImage());
                gui.afficher("\nVous êtes sur " + zoneCourante.description);
                leSon.joueurAudioPorteOuverte();
                leSon.jouerAudioAchievement();
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() 
                    {
                        zoneCourante = zones[69];
                        gui.clearText();
                        gui.afficheImage(zoneCourante.nomImage());
                        gui.afficher(zoneCourante.descriptionLongue());
                    }
                }, 2500);
        } else {
            gui.afficher("Vous ne possédez pas la clé de la prison.");
            gui.afficher("\nCommandes disponibles : " + zoneCourante.commandesDispo());
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
                leSon.joueurAudioPorteOuverte();
                leSon.jouerAmbiantInterieurVide();
            }
            if (Temps.getHeure() == 10) // Heure de la promenade (matin)
            {
                zoneCourante = zones[17];
                leSon.joueurAudioPorteOuverte();
                leSon.jouerAmbiantInterieurVide();
            }
            if (Temps.getHeure() == 12) // Heure du repas (déjeuner)
            {
                zoneCourante = zones[18];
                leSon.joueurAudioPorteOuverte();
                leSon.jouerAmbiantInterieurVide();
            }
            if (Temps.getHeure() == 15) // Heure de la promenade (soir)
            {
                zoneCourante = zones[17];
                leSon.joueurAudioPorteOuverte();
                leSon.jouerAmbiantInterieurVide();
            }
            if (Temps.getHeure() == 18) // Heure de la douche
            {
                zoneCourante = zones[19];
                leSon.joueurAudioPorteOuverte();
                leSon.jouerAmbiantInterieurVide();
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() 
                    {
                        cinematiqueActive = true;
                    }
                }, 800);
            }
            if (Temps.getHeure() == 20) // Heure du repas (dîner)
            {
                zoneCourante = zones[18];
                leSon.joueurAudioPorteOuverte();
                leSon.jouerAmbiantInterieurVide();
            }
            if (Temps.getHeure() == 22) // Heure de dormir (nuit)
            {
                zoneCourante = zones[22];
                leSon.jouerAudioPorteClaque();
                leSon.jouerAmbiantInterieurVide();
            }
            gui.afficher(Message);
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

            // Si le joueur se trouve dans la zone [2] (exterieur ile nuit) et qu'il est
            // plus de 8h,
            // la zone courante passe à zone [3] (exterieur ile jour)
            if (zoneCourante == zones[2]) {
                zoneCourante = zones[1];
            }
            // Si le joueur se trouve dans la zone [4] (cour nuit) et qu'il est plus de 8h,
            // la zone courante passe à zone [3] (cour jour)
            if (zoneCourante == zones[4]) {
                zoneCourante = zones[3];
            }

            // Si le joueur se trouve dans la zone [7] (refectoire nuit) et qu'il est plus
            // de 8h, la zone courante passe à zone [6] (refectoire jour)
            if (zoneCourante == zones[7]) {
                zoneCourante = zones[6];
            }
            // Si le joueur se trouve dans la zone [22] (cellule nuit) et qu'il
            // est plus de
            // 8h, la zone courante passe à zone [16] (cellule jour)
            if (zoneCourante == zones[22]) {
                zoneCourante = zones[16];
                leSon.jouerAmbiantInterieurVide();
            }
            // Si le joueur se trouve dans la zone [21] (couloir nuit) et qu'il est plus de
            // 8h, la zone courante passe à zone [20] (couloir jour)
            if (zoneCourante == zones[21]) {
                zoneCourante = zones[20];
                leSon.jouerAmbiantInterieurVide();
            }
            // Si le joueur se trouve dans la zone [24] (douches nuit) et qu'il est plus de
            // 22h, la zone courante passe à zone [23] (douches jour)
            if (zoneCourante == zones[24]) {
                zoneCourante = zones[23];
                leSon.jouerAmbiantInterieurVide();
            }
            // Si le joueur se trouve dans la zone [27] (salleDesGardes nuit) et qu'il est
            // plus de
            // 22h, la zone courante passe à zone [26] (salleDesGardes jour)
            if (zoneCourante == zones[27]) {
                zoneCourante = zones[26];
                leSon.jouerAmbiantInterieurVide();
            }
            // Si le joueur se trouve dans la zone [30] (cuisine nuit) et qu'il est plus de
            // 22h, la zone courante passe à zone [29] (cuisine jour)
            if (zoneCourante == zones[30]) {
                zoneCourante = zones[29];
            }

            gui.afficheImage(zoneCourante.nomImage());
        }
        if ((heureCourante >= 22 && heureCourante <= 24) || (heureCourante >= 0 && heureCourante < 8)) {
            // [ 22h <= heureCourante <= 24 ] OU [ 0 <= heureCourante < 8]
            // L'heure courante est >= 22h et < 8h (il fait nuit)

            // Arrêt des sons ambiant de jour
            // …

            nightTime = true;
            if (!cinematiqueDeDepart && activerAlerteNuit) {
                cinematiqueActive = true;
                nightAlertOn = true;
                gui.clearText();
                gui.afficher("\nLa nuit tombe sur l'île de Mors Insula… ");
                gui.afficher("Les gardes exigent à tous les détenus de rejoindre leurs cellules respectives!\n");
                gui.afficher("\nDes rondes de nuit seront effectuées jusqu'à 8 heures du matin. ");
                gui.afficher("Tout détenu qui sera repéré de nuit en dehors de sa cellule sera sanctionné!\n");
                gui.afficher("\nCommandes disponibles : OK");
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
                    || zoneCourante == zones[18] || (zoneCourante == zones[19] && !cinematiqueActive)) {
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
                // Le son -> stop ambiance exterieur jour (à créer)
                leSon.jouerAmbiantNuitExterieur();
            }
            if (zoneCourante == zones[7] || zoneCourante == zones[22] || zoneCourante == zones[21]
                    || zoneCourante == zones[24] || zoneCourante == zones[27] || zoneCourante == zones[30]) {
                leSon.stopAmbianceInterieurVide();
                leSon.jouerAmbiantNuitInterieur();
            }
        }
    }


    // GESTION DES ÉVÉNEMENTS DU JEU
    // La fonction ci-après
    // définit réaffecte les zones du jeu en fonctions de l'heure du jeu, 
    // et de la zone courante ainsi qu'en fonction des valeurs des booléens d'événement

    private void checkEvent() throws UnsupportedAudioFileException, IOException, LineUnavailableException {


        if (zoneCourante == zones[23] && !(Inventaire.contains(Objets.COUTEAU) && !cinematiqueActive))
        {
            cinematiqueActive = true;
            zoneCourante = zones[71];
            gui.clearText();
            gui.afficheImage(zoneCourante.nomImage());
            gui.afficher(zoneCourante.descriptionLongue());
            Inventaire.add(Objets.COUTEAU);
            armeRecuperee = true;
            leSon.jouerAudioAchievement();
        }

        if (zoneCourante == zones[70] && !(cinematiqueActive))
        {
            cinematiqueActive = true;
            gui.clearText();
            gui.afficheImage(zoneCourante.nomImage());
            gui.afficheJoueur("SUD", 250, 250);
            gui.afficher(zoneCourante.description);

            Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() 
                    {
                        afficherSceneFinale();
                    }
                }, 2000);
        }

        if (zoneCourante == zones[69])
        {
            cinematiqueActive = false;
            gui.clearText();
            gui.afficheImage(zoneCourante.nomImage());
            gui.afficheJoueur("SUD", 250, 250);
            gui.afficher(zoneCourante.descriptionLongue());
        }

        if (zoneCourante == zones[4] && (Inventaire.contains(Objets.CLE2)) && !(nightAlertOn) && !(cinematiqueActive))
        {
            gui.clearText();
            gui.afficher("\n\n");
            gui.afficher(zoneCourante.descriptionLongue() + "FUIR");
        }

        if (zoneCourante == zones[22] && (Inventaire.contains(Objets.CLE2)) && !(nightAlertOn) && !(cinematiqueActive))
        {
            gui.clearText();
            gui.afficher("\n\n");
            gui.afficher(zoneCourante.descriptionLongue() + "OUVRIR");
        }

        if (zoneCourante == zones[26] && Inventaire.contains(Objets.CLE1) && !coffreOuvert && !(Inventaire.contains(Objets.CLE2)))
        {
            gui.clearText();
            gui.afficher("\n\n");
            gui.afficher(zoneCourante.descriptionLongue() + "OUVRIR");
        }

        if (zoneCourante == zones[25]) {
            leSon.jouerAmbiantDouches();
            gui.afficheJoueur("NORD", 260, 340);
        }

        if (zoneCourante == zones[16] || zoneCourante == zones[18]
                || zoneCourante == zones[17] || zoneCourante == zones[22]) {

            if (zoneCourante != zones[22]) { // On joue le son d'ambiance de la journée SAUF si
                leSon.jouerAmbiantInterieurVide(); // la zone courante correspond à la cellule de nuit
            }
            if (Temps.getHeure() == 8 && Temps.getMinutes() == 0) // Heure du repas (petit-déjeuner)
            {
                zoneCourante = zones[18];
                gui.clearText();
                gui.afficher("\n\n");
                gui.afficher(zoneCourante.descriptionLongue());
                leSon.joueurAudioPorteOuverte();
            }
            if (Temps.getHeure() == 10 && Temps.getMinutes() == 0) // Heure de la promenade (matin)
            {
                zoneCourante = zones[17];
                gui.clearText();
                gui.afficher("\n\n");
                gui.afficher(zoneCourante.descriptionLongue());
                leSon.joueurAudioPorteOuverte();
            }
            if (Temps.getHeure() == 12 && Temps.getMinutes() == 0) // Heure du repas (déjeuner)
            {
                zoneCourante = zones[18];
                gui.clearText();
                gui.afficher("\n\n");
                gui.afficher(zoneCourante.descriptionLongue());
                leSon.joueurAudioPorteOuverte();
            }
            if (Temps.getHeure() == 15 && Temps.getMinutes() == 0) // Heure de la promenade (soir)
            {
                zoneCourante = zones[17];
                gui.clearText();
                gui.afficher("\n\n");
                gui.afficher(zoneCourante.descriptionLongue());
                leSon.joueurAudioPorteOuverte();
            }
            if (Temps.getHeure() == 18 && Temps.getMinutes() == 0) // Heure de la douche
            {
                zoneCourante = zones[19];
                gui.clearText();
                gui.afficher("\n\n");
                gui.afficher(zoneCourante.descriptionLongue());
                leSon.joueurAudioPorteOuverte();
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() 
                    {
                        cinematiqueActive = true;
                    }
                }, 800);
            }
            if (Temps.getHeure() == 20 && Temps.getMinutes() == 0) // Heure du repas (dîner)
            {
                zoneCourante = zones[18];
                gui.clearText();
                gui.afficher("\n\n");
                gui.afficher(zoneCourante.descriptionLongue());
                leSon.joueurAudioPorteOuverte();
            }
            if (Temps.getHeure() == 22 && Temps.getMinutes() == 0) // Heure de dormir (nuit)
            {
                zoneCourante = zones[22];
                // On n'affiche pas la description de la zone puisque l'alerte de nuit se lance
                leSon.jouerAudioPorteClaque();
            }
        }

        if (Temps.getHeure() == 18 && Temps.getMinutes() == 0 && !(zoneCourante == zones[19])) // Heure de la douche
        {
            cinematiqueActive = true;
            stopSonsAmbiants();
            zoneCourante = zones[40];
            gui.clearText();
            gui.afficher("\n\n");
            gui.afficher(zoneCourante.descriptionLongue());
        }

        if (zoneCourante == zones[20] && !cinematiqueActive && !(Inventaire.contains(Objets.NOTE))) {
            zoneCourante = zones[36];
            Inventaire.add(Objets.NOTE);
            indiceCodetenu = true;
            cinematiqueActive = true;
            leSon.jouerAmbiantInterieurVide();
            gui.clearText();
            gui.afficher("\n\n");
            gui.afficheImage(zoneCourante.nomImage());
            gui.afficher(zoneCourante.descriptionLongue());
        }

        if (zoneCourante == zones[26] && !carteTrouvee && (Temps.getHeure() >= 8) && (Temps.getHeure() < 11)) {
            cinematiqueActive = true;
            zoneCourante = zones[42];
            gui.clearText();
            gui.afficheImage(zoneCourante.nomImage());
            gui.afficher(zoneCourante.descriptionLongue());
            leSon.jouerAudioAchievement();
            Inventaire.add(Objets.CARTE);
            carteTrouvee = true;
        }

        if (Temps.getHeure() == 20 && Temps.getMinutes() == 1 && !sceneBagarre && indiceCodetenu && Inventaire.contains(Objets.NOTE))
        // Événement -> repas (force à aller au réfectoire) après discussion douches Marco
        {
            stopSonsAmbiants();
            zoneCourante = zones[47];
            gui.clearText();
            gui.afficher("\n\n");
            gui.afficher(zoneCourante.descriptionLongue());
        }

        if (zoneCourante == zones[20]) {
            leSon.jouerAmbiantInterieurVide();
        }

        if (zoneCourante == zones[8] && !sceneBagarre) {
            cinematiqueActive = true;
            if (battreMarco) {
                sceneBagarre = true;
                zoneCourante = zones[48];
                gui.clearText();
                gui.afficher("\n");
                gui.afficher(zoneCourante.descriptionLongue());
            }
            if (!battreMarco) {
                sceneBagarre = true;
                zoneCourante = zones[9];
                gui.clearText();
                gui.afficher("\n\n");
                gui.afficher(zoneCourante.descriptionLongue());
            }
        }

        if (zoneCourante == zones[58] && !bagarreFinie) { // RAJOUTER VAR
            tentatives = tentatives - 1;
            bagarreFinie = true;
        }

        if (zoneCourante == zones[61] && bagarreFinie && !cinematiqueActive) { // RAJOUTER VAR
            cinematiqueActive = true;
            this.afficherSceneMort();
        }

        if (zoneCourante == zones[8] && sceneBagarre && !indice2Codetenu) {
            indice2Codetenu = true;
        }
 
        if ((zoneCourante == zones[29] || zoneCourante == zones[30]) && indice2Codetenu && sceneBagarre
                && !(Inventaire.contains(Objets.CLE1))) {
            zoneCourante = zones[64];
            indice2Codetenu = false;
            gui.clearText();
            gui.afficheImage(zoneCourante.nomImage());
            gui.afficher(zoneCourante.descriptionLongue());
            Inventaire.add(Objets.CLE1);
            leSon.jouerAudioAchievement();
        }


        /* ----------------- ÉVÉNEMENTS DONNANT UN AVERTISSEMENT AU JOUEUR -------------*/

        if  // SI LE JOUEUR SE TROUVE SOIT DANS :
        (
            ( zoneCourante == zones[26]                                 // ZONE SALLE DES GARDES (DE JOUR OU NUIT)
            && ( Temps.getHeure() >= 11                                 // DE 11H LE MATIN
            && Temps.getHeure() < 22 ) )                                // À 22H LE SOIR 
                                                                        // (PUISQUE APRÈS ILS FONT LEUR RONDE)
                                                                        
            || ( zoneCourante == zones[4]                               // ZONE COUR (DE NUIT)
            && ( Temps.getHeure() == 22                                 // S'IL EST 22 HEURES ET QUELQUES (JUSQUE 23H)
            || ( Temps.getHeure() >= 5 && Temps.getHeure() < 7 ) ) )    // OU S'IL EST ENTRE 5H ET 7H DU MATIN

            || ( zoneCourante == zones[7] || zoneCourante == zones[30]  // ZONE = REFECTOIRE OU CUISINE, SI :
            && ( Temps.getHeure() >= 23 || Temps.getHeure() >= 0        // HEURE > 23 JOUR J OU HEURE >= 0 JOUR J+1
            && Temps.getHeure() < 2 )                                   // ET JUSQU'À' 2H DU MATIN ;
            || (Temps.getHeure() >= 7 && Temps.getHeure() < 8) )        // OU DE 7H A 8H DU MATIN

            || ( zoneCourante == zones[24]                              // ZONE DOUCHES (DE NUIT)
            && Temps.getHeure() >= 2                                    // SI HEURE ENTRE 2H DU MATIN
            && ( Temps.getHeure() <= 3 && Temps.getMinutes() <= 30 ) )  // ET 3 HEURES ET 30 MINUTES                                

            || ( zoneCourante == zones[20]                              // ZONE COULOIR (DE NUIT)
            && ( ( Temps.getHeure() >= 4 && Temps.getMinutes() >= 30 )  // SI HEURE ENTRE 4 H ET 30 MINUTES
            && Temps.getHeure() < 5 ) )                                 // ET JUSQU'À 5H DU MATIN

            // ET SI LES MENUS QUI METTENT LE TEMPS EN PAUSE NE SONT PAS ACTIFS
            && ( !cinematiqueActive && !pauseMenu && !noteMenu && !mapMenu )
        )
        {   // ALORS :
            
            zoneCourante = zones[28]; // On passe la zone sur cinématique d'avertissement (évite que l'event boucle)

            // On patiente environ 0.1 seconde (delay: 100ms);
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    // Affiche zone d'avertissement + texte + cache le joueur + (nb tentatives -1)
                    tentatives = tentatives -1; 
                    gui.afficheJoueur("NONE", 50, 50);
                    gui.clearText();
                    gui.afficheImage(zoneCourante.nomImage());
                    gui.afficher(zoneCourante.description);
                    gui.afficher("\nAttention: il vous reste " + tentatives 
                    + " avertissement(s) avant de vous faire exécuter!");
                    
                    Timer timer3 = new Timer();
                    timer3.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            cinematiqueActive = true; // On active le booléen cinématique (arrêt du temps)
                        }
                    }, 800); 
                }
            }, 100);

            // On patiente environ 10 secondes et on retourne le joueur dans sa cellule
            Timer timer2 = new Timer();
            timer2.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (!cinematiqueDeDepart && zoneCourante != zones[0] && zoneCourante != zones[34] && zoneCourante != zones[35])
                    {
                        // Affiche zone d'avertissement + texte + cache le joueur + (nb tentatives -1)
                        zoneCourante = zones[16];
                        cinematiqueActive = false;
                        Timer timer2 = new Timer();
                        timer2.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                // Repatienter au cas où la zone doit s'update via le check Jour/Nuit
                                gui.clearText();
                                gui.afficheJoueur("SUD", 260, 170);
                                gui.afficheImage(zoneCourante.nomImage());
                                gui.afficher(zoneCourante.descriptionLongue());
                            }
                        }, 800);
                    } 
                }
            }, 9200);
        }

        /* ----------------------–--- FIN ÉVTS. DONNANT AVERTISSEMENT JOUEUR --------------------- */


        // Cet évenement renvoie la zone Game Over lorsque le nombre max de tentatives
        // est atteint
        if (tentatives < 0 && (zoneCourante != zones[34] && zoneCourante != zones[35]) && zoneCourante != zones[0]) {
            zoneCourante = zones[34]; // On change la zone courante à la zone game over
            gui.clearText(); // On clear le texte
            gui.cacherBarre(); // On cache la barre qui contient l'heure et le nb d'avertissements
            cinematiqueActive = true;
            this.afficherSceneMort();
        }

    }

    private void checkFinBagarre() {
        if (PV_Marco <= 0 && !bagarreFinie) {
            bagarreFinie = false;
            zoneCourante = zones[58];
            cinematiqueActive = false;
            gui.clearText();
            gui.afficheImage(zoneCourante.nomImage());
            gui.afficher(zoneCourante.descriptionLongue());
        }

        if (PV_Joueur <= 0 && !bagarreFinie) {
            bagarreFinie = true;
            zoneCourante = zones[59];
            cinematiqueActive = false;
            gui.clearText();
            gui.afficheImage(zoneCourante.nomImage());
            gui.afficher(zoneCourante.descriptionLongue());
        }
    }

    private void effectuerAttaque() throws UnsupportedAudioFileException, IOException, LineUnavailableException {

        int random = (int) (Math.random() * 100);

        zoneCourante = zones[62];
        String descriptionScene = zoneCourante.description;

        if (random <= 30) {
            descriptionScene += "Vous envoyez un violent coup de tête à Marco.\n" + zones[54].description;
            PV_Marco = PV_Marco - 12; // ATTAQUE TRÈS FORTE : -12 PV POUR MARCO
        }
        if (random > 30 && random <= 65) {
            descriptionScene += "Vous envoyez un rapide coup de poing dans la tête de Marco.\n" + zones[54].description;
            PV_Marco = PV_Marco - 9; // ATTAQUE FORTE : -9 PV POUR MARCO
        }
        if (random > 65 && random <= 85) {
            descriptionScene += "Vous donnez un vif coup de genoux dans le ventre de Marco.\n" + zones[56].description;
            PV_Marco = PV_Marco - 4; // ATTAQUE MOYENNE : -4 PV POUR MARCO
        }
        if (random > 85) {
            descriptionScene += "Vous donnez un coup de pied dans l'entre-jambe de Marco.\n" + zones[63].description;
            // MARCO ESQUIVE L'ATTAQUE : PAS DE RETRAIT DE PV POUR MARCO
        }

        gui.clearText();
        gui.afficher("Marco : " + PV_Marco + " PV\t\t\tPercival : " + PV_Joueur + " PV\n\n\n");
        gui.afficheImage(zoneCourante.nomImage());
        gui.afficher(descriptionScene);

        if (PV_Joueur > 0 && PV_Marco > 0) {
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        effectuerMarco();
                        leSon.jouerAudioDialogue();
                    } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                        e.printStackTrace();
                    }
                }
            }, 3500);
        }

        checkFinBagarre();
    }

    private void effectuerDefense() throws UnsupportedAudioFileException, IOException, LineUnavailableException {

        int random = (int) (Math.random() * 100);

        if (random <= 40) {
            zoneCourante = zones[51];
            PV_Joueur = PV_Joueur + 7; // DEFENSE : JOUEUR RECUPERE 7 PV
        }
        if (random > 40) {
            zoneCourante = zones[57];
            PV_Joueur = PV_Joueur - 2; // DEFENSE : JOUEUR SE DEFEND AU PROCHAIN COUP DE MARCO -> -2 PV
        }

        gui.clearText();
        gui.afficher("Marco : " + PV_Marco + " PV\t\t\tPercival : " + PV_Joueur + " PV\n\n\n");
        gui.afficheImage(zoneCourante.nomImage());
        gui.afficher(zoneCourante.description);

        if (PV_Joueur > 0 && PV_Marco > 0) {
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        effectuerMarco();
                    } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                        e.printStackTrace();
                    }
                }
            }, 3500);
        }

        checkFinBagarre();
    }

    private void effectuerArme() throws UnsupportedAudioFileException, IOException, LineUnavailableException {

        int random = (int) (Math.random() * 100);

        zoneCourante = zones[53];
        String descriptionScene = zoneCourante.description;

        if (random <= 77) {
            descriptionScene += "\n" + zones[56].description;
            PV_Marco = PV_Marco - 11;
            // ATTAQUE FORTE : -11 PV POUR MARCO
        }
        if (random > 77) {
            descriptionScene += "\n" + zones[54].description;
            PV_Marco = PV_Marco - 19;
            // ATTAQUE TRES FORTE : -19 PV POUR MARCO
        }

        gui.clearText();
        gui.afficher("Marco : " + PV_Marco + " PV\t\t\tPercival : " + PV_Joueur + " PV\n\n\n");
        gui.afficheImage(zoneCourante.nomImage());
        gui.afficher(descriptionScene);

        Timer timer = new Timer();
        if (PV_Joueur > 0 && PV_Marco > 0) {
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        effectuerMarco();
                    } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                        e.printStackTrace();
                    }
                }
            }, 3500);
        }

        checkFinBagarre();
    }

    private void effectuerMarco() throws UnsupportedAudioFileException, IOException, LineUnavailableException {

        int random = (int) (Math.random() * 100);
        if (random <= 80) {
            MarcoAttaque();
        }
        if (random > 80) {
            MarcoArme();
        }
        gui.clearText();
        gui.afficher("Marco : " + PV_Marco + " PV\t\t\tPercival : " + PV_Joueur + " PV\n\n\n");
        gui.afficheImage(zoneCourante.nomImage());
        gui.afficher(zoneCourante.description);

        if (PV_Marco > 0 && PV_Joueur > 0) {
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    zoneCourante = zones[50];
                    gui.clearText();
                    gui.afficher("Marco : " + PV_Marco + " PV\t\t\tPercival : " + PV_Joueur + " PV\n\n\n");
                    gui.afficheImage(zoneCourante.nomImage());
                    gui.afficher(zoneCourante.descriptionLongue());
                    try {
                        leSon.jouerAudioDialogue();
                    } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                        e.printStackTrace();
                    }
                }
            }, 3500);
        }

        checkFinBagarre();
    }

    private void MarcoAttaque() {
        int random = (int) (Math.random() * 100);

        if (random <= 50) {
            zoneCourante = zones[55];
            PV_Joueur = PV_Joueur - 9; // ATTAQUE FORTE : -9 PV POUR JOUEUR
        }
        if (random > 50) {
            zoneCourante = zones[49];
            PV_Joueur = PV_Joueur - 4; // ATTAQUE MOYENNE : -4 PV POUR JOUEUR
        }
    }

    private void MarcoArme() {
        int random = (int) (Math.random() * 100);

        zoneCourante = zones[52];
        if (random <= 33) {
            PV_Joueur = PV_Joueur - 11;
            // ATTAQUE FORTE : -11 PV POUR JOUEUR
        }
        if (random > 33) {
            PV_Joueur = PV_Joueur - 17;
            // ATTAQUE TRES FORTE : -17 PV POUR JOUEUR
        }
    }

    private void stopSonsAmbiants() { // FAIRE ATTENTION A CE QUE CETTE METHODE NE SOIT PAS APPELEE PLUSIEURS FOIS
                                      // DANS LES BOUCLES
        leSon.stopAmbianceDouches();
        leSon.stopAmbianceNuitExterieur();
        leSon.stopAmbianceNuitInterieur();
        leSon.stopAmbianceTheEnd();
        leSon.stopAmbianceThemePrincipal();
        leSon.stopAmbianceInterieurVide();
    }

    private void afficherSceneMort() {
        Timer chrono = new Timer();
        chrono.schedule(new TimerTask() {
            @Override
            public void run() {
                cinematiqueActive = true;
                gui.cacherBarre();
                tentatives = 3;
                gui.afficheJoueur("NULL", 50, 50);
                gui.afficheImage(zoneCourante.nomImage()); // On affiche l'image correspondant à la zone
                gui.afficher(zoneCourante.description); // On affiche la description de la zone
                gui.afficher("\n\nCommandes disponibles : OK");
                try {
                    stopSonsAmbiants();
                    leSon.jouerAudioMort();
                    leSon.jouerAmbiantEnd();
                } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                    e.printStackTrace();
                }
            }
        }, 500);
    }

    private void afficherSceneFinale() {
        Timer chrono = new Timer();
        chrono.schedule(new TimerTask() {
            @Override
            public void run() {
                cinematiqueActive = true;
                gui.cacherBarre();
                tentatives = 3;
                zoneCourante = zones[35];
                gui.afficheJoueur("NULL", 50, 50);
                gui.afficheImage(zoneCourante.nomImage());
                gui.afficher("\nFélicitations! Vous êtes arrivé jusqu'au bout du jeu. Nous espérons qu'il vous aura plu."
                + " Vous pouvez tenter différents scénarii en crééant une nouvelle partie.");
                gui.afficher("\n\nCommandes disponibles : OK");
                try {
                    stopSonsAmbiants();
                    leSon.jouerAudioAchievement();
                    leSon.jouerAmbiantEnd();
                } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                    e.printStackTrace();
                }
            }
        }, 500);
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

            // Lors du déplacement du joueur, on cache toutes les couches (layers) du GUI
            // (sauf joueur et zone)
            gui.cacher(2);
            gui.cacher(3);
            gui.cacher(4);
            gui.cacher(5);
            gui.cacher(6);
            gui.cacher(7);
            gui.cacher(8);
            gui.cacher(9);
            // Ces couches seront de nouveau instanciées dans la méthode checkEvent si
            // nécessaire

            stopSonsAmbiants();
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
            try {
                leSon.jouerAudioPorteClaque();
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                e.printStackTrace();
            }
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
                leSon.jouerAudioPorteClaque();
                cinematiqueDeDepart = false;
            }
            if (zoneCourante == zones[12]) {
                gui.afficheAutre("null", 2, 150, 150); // préchargement pour la scène suivante
                gui.afficheAutre("null", 3, 150, 150); // préchargement pour la scène suivante
                gui.afficheAutre("null", 4, 150, 150); // préchargement pour la scène suivante
                gui.afficheAutre("null", 5, 150, 150); // préchargement pour la scène suivante
                gui.afficheAutre("items/bateau/nord", 7, 250, 250); // préchargement pour la scène suivante
                gui.cacher(7);
            }
            if (zoneCourante == zones[13]) {
                gui.afficheJoueur("NORD", 258, 300);
                gui.afficheAutre("garde/descend", 2, 225, 230); // affiche garde
                gui.afficheAutre("garde/descend", 3, 325, 230); // affiche garde
                gui.afficheAutre("garde/monte", 4, 290, 290); // affiche garde
                gui.afficheAutre("items/bateau/nord", 7, 215, 355); // affiche bateau
                updatePositionJoueur("NORD"); // Initialisation du joueur
                gui.refreshLayers(); // Rafraîchit la layeredPane (Classe GUI)
            }
            if (zoneCourante == zones[14]) {
                gui.cacher(2);
                gui.cacher(3);
                gui.cacher(5);
                gui.cacher(7);
                gui.afficheAutre("garde/monte", 4, 220, 320); // Changement position du garde
                gui.afficheJoueur("NORD", 250, 330); // Changement position joueur
            }
            if (zoneCourante == zones[15]) {

                gui.afficheJoueur("SUD", 260, 170); // Changement position joueur
                gui.afficheAutre("garde/monte", 4, 250, 150); // affiche garde
                gui.afficheAutre("items/porte/porteDroite", 8, 200, 200); // affiche porte pnj
                gui.afficheAutre("items/porte/porteGauche", 9, 300, 200); // préchargement scène suivante
                gui.cacher(9);

            }
            if (zoneCourante == zones[16]) {

                gui.afficheJoueur("SUD", 260, 170); // Changement position joueur
                gui.afficheAutre("items/porte/porteDroite", 8, 150, 150); // affiche porte joueur
                gui.afficheAutre("items/porte/porteGauche", 9, 150, 150); // affiche porte pnj
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

        gui.clearText();
        zoneCourante = zones[0];
        gui.afficheImage(zoneCourante.nomImage());
        gui.afficher(zoneCourante.description);
        gui.afficheJoueur("NONE", 50, 50);

        // RESET DE TOUTES LES VARIABLES UTILES AU JEU
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
        PV_Marco = 100;
        PV_Joueur = 100;
        bagarreFinie = false;
        quitMenu = false;
        pauseMenu = false;
        nightTime = false;
        activerAlerteNuit = true;
        nightAlertOn = false;
        carteTrouvee = false;
        tentatives = 3;
        gui.cacherBarre(); // On cache la barre du temps
        leSon.jouerAudioConfirm();
        // Arrêt de tous les sons d'ambiance du jeu
        this.stopSonsAmbiants();
        leSon.jouerAmbiantThemePrincipal(); // On joue le son d'ambiance du menu principal
    }

    private void sauvegarderPartie() throws IOException {

        posXJoueur = gui.getPosXJoueur();
        posYJoueur = gui.getPosYJoueur();
        URLJoueur = this.getClass().getClassLoader().getResource("jeu/images/sprites/heros/descend.png");

        // LA SOLUTION CI-DESSOUS EST ABANDONNÉE => LES AUTRES PNJ/ITEMS SONT
        // DIRECTEMENT DESSINÉS SUR LES ZONES
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
        // Mettre a jour cette fonction -> la position des items/pnj ne sera pas
        // sauvegardée
        // puisque ces derniers sont directement dessinés sur les zones

        sauvegarde.enregistrer();
    }

    @SuppressWarnings("unused") // supprime le warn "unused" dans la console : "variable 'f' non-utilisée"
    private int getNbSauvegardes() {
        Save sauvegarde = new Save();
        int retour = 0;
        File directory = new File("src/jeu/saves/");
        for (File f : directory.listFiles(sauvegarde.getSaveFileFilter())) {
            retour = retour + 1;
        }
        return retour;
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
            retour += "\n\nTapez le numéro de la partie à reprendre, ou 'RETOUR' pour revenir"
                    + " au menu principal.";
        }
        if (nbSaves == 0) {
            retour = "Aucune partie sauvegardée."
                    + "\n\nTapez 'RETOUR' pour revenir au menu principal.";
        }
        return retour;
    }

    private void reprendrePartie(int indexPartie) throws ClassNotFoundException, IOException {

        Save sauvegarde = new Save();
        try {
            sauvegarde = sauvegarde.recuperer(indexPartie);
            Inventaire = sauvegarde.Inventaire;
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
        gui.afficher("Le jeu va maintenant se fermer…\nAu revoir!");
        gui.enable(false);
        Timer chrono = new Timer();
        chrono.schedule(new TimerTask() {
            @Override
            public void run() {
                System.exit(0);
            }
        }, 5500);
    }
}
