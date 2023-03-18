package jeu;

import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Audio implements LineListener {

    // Streams des sons brefs
    AudioInputStream bruitDePas;
    AudioInputStream dialogue;
    AudioInputStream next;
    AudioInputStream confirm;
    AudioInputStream inventory;
    AudioInputStream menuON;
    AudioInputStream menuOFF;
    AudioInputStream error;

    // Streams des sons ambiants (boucles)
    AudioInputStream themePrincipal;
    AudioInputStream ambiantMenu;

    // Clips sons brefs
    Clip clipNext = initClip();
    Clip clipAudioPas = initClip();
    Clip clipDialogue = initClip();
    Clip clipAudioConfirm = initClip();
    Clip clipAudioResume = initClip();
    Clip clipMenuON = initClip();
    Clip clipMenuOFF = initClip();
    Clip clipError = initClip();

    // Clips sons ambiants
    Clip clipAudioAmbiantMenu = initClip();

    public Audio() {
        try {
            this.setAudio();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Clip initClip() {
        Clip newClip = null;
        try {
            newClip = AudioSystem.getClip();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
        return newClip;
    }

    public void setAudio() throws UnsupportedAudioFileException, IOException {

        // Récupération des sons brefs
        bruitDePas = AudioSystem
                .getAudioInputStream(getClass().getClassLoader().getResourceAsStream("jeu/audio/soundFX/walk.wav"));
        dialogue = AudioSystem
                .getAudioInputStream(getClass().getClassLoader().getResourceAsStream("jeu/audio/soundFX/dialogue.wav"));
        next = AudioSystem
                .getAudioInputStream(getClass().getClassLoader().getResourceAsStream("jeu/audio/soundFX/page.wav"));
        confirm = AudioSystem
                .getAudioInputStream(getClass().getClassLoader().getResourceAsStream("jeu/audio/soundFX/confirm.wav"));
        inventory = AudioSystem
                .getAudioInputStream(getClass().getClassLoader().getResourceAsStream("jeu/audio/soundFX/page.wav")); // A
                                                                                                                     // CHANGER
        menuON = AudioSystem
                .getAudioInputStream(getClass().getClassLoader().getResourceAsStream("jeu/audio/soundFX/menuOpen.wav"));
        menuOFF = AudioSystem
                .getAudioInputStream(
                        getClass().getClassLoader().getResourceAsStream("jeu/audio/soundFX/menuClose.wav"));
        menuOFF = AudioSystem
                .getAudioInputStream(
                        getClass().getClassLoader().getResourceAsStream("jeu/audio/soundFX/menuClose.wav"));
        error = AudioSystem
                .getAudioInputStream(
                        getClass().getClassLoader().getResourceAsStream("jeu/audio/soundFX/error.wav"));

        // Récupération des sons ambiants
        // themePrincipal = AudioSystem
        //        .getAudioInputStream(getClass().getClassLoader().getResourceAsStream("jeu/audio/ambiant/main.wav")); // A CREER                                                                                                           // CREER
        //ambiantMenu = AudioSystem
        //        .getAudioInputStream(getClass().getClassLoader().getResourceAsStream("jeu/audio/ambiant/menu.wav")); // A CREER
                                                                                                                  

    }

    // Méthode de lecture des sons brefs
    public void jouer(Clip unClip, AudioInputStream unStream)
            throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        // On calcule la longueur en milliscondes du fichier audio
        long dureeEnMilis = 1000 * unStream.getFrameLength() / (long) unStream.getFormat().getFrameRate();
        // Si l'audioclip n'est pas déjà ouvert, on en ouvre un nouveau
        // avec l'audioStream passé en paramètre de la méthode
        if (!unClip.isOpen()) {
            unClip.open(unStream);
            unClip.start();
        }
        // On set la frame sur 0 (début de l'audio)
        // et on démarre le clip (le son se joue)
        unClip.setFramePosition(0);
        unClip.start();
        // On execute un timer qui patiente la durée de l'audio
        // avant de poursuivre la lecture du code
        try {
            Thread.sleep(dureeEnMilis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Enfin, on arrête l'audio après la fin du timer
        // NB: Si le timer précédent ne s'est pas executé, le son ne se lancera pas
        unClip.stop();
    }

    // Méthode de lecture des sons ambiants
    public void ambiance(Clip unClip, AudioInputStream unStream)
            throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        // Idem que jouer() …
        if (!unClip.isOpen()) {
            unClip.open(unStream);
            unClip.start();
        }
        unClip.setFramePosition(0);
        unClip.start();
        unClip.loop(99999); // … sauf qu'on boucle l'audio au lieu de le stopper
                            // l'audio sera arrêter lors de l'appel de la méthode stopAmbiance()
    }

    // Appels sons brefs

    public void jouerAudioDialogue() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        this.jouer(clipDialogue, dialogue);
    }

    public void jouerAudioConfirm() throws UnsupportedAudioFileException, IOException, LineUnavailableException {

        this.jouer(clipAudioConfirm, confirm);
    }

    public void jouerAudioPas() throws UnsupportedAudioFileException, IOException, LineUnavailableException {

        this.jouer(clipAudioPas, bruitDePas);
    }

    public void jouerAudioNext() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        this.jouer(clipNext, next);
    }

    public void jouerAudioMenuON() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        this.jouer(clipMenuON, menuON);
    }

    public void jouerAudioMenuOFF() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        this.jouer(clipMenuOFF, menuOFF);
    }

    public void jouerAudioError() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        this.jouer(clipError, error);
    }

    // Appels sons ambiants

    // ...

    // Méthode d'arrêt sons ambiants
    private void stopAmbiance(Clip unClip) {
        unClip.stop();
    }

    // Exemple d'arrêt d'un son d'ambiance
    public void stopAmbianceMachin() {
        // this.stopAmbiance(ClipMachin);
    }

    @Override
    public void update(LineEvent event) {
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

}