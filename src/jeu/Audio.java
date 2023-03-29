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
    AudioInputStream menuON;
    AudioInputStream menuOFF;
    AudioInputStream error;
    AudioInputStream mort;

    // Streams des sons ambiants (boucles)
    AudioInputStream themePrincipal;
    AudioInputStream nuitExterieur;
    AudioInputStream nuitInterieur;
    AudioInputStream theEnd;

    // Clips sons brefs
    Clip clipNext = initClip();
    Clip clipAudioPas = initClip();
    Clip clipDialogue = initClip();
    Clip clipAudioConfirm = initClip();
    Clip clipAudioResume = initClip();
    Clip clipMenuON = initClip();
    Clip clipMenuOFF = initClip();
    Clip clipError = initClip();
    Clip clipMort = initClip();

    // Clips sons ambiants
    Clip clipAudioThemePrincipal = initClip();
    Clip clipAudioNuitExterieur = initClip();
    Clip clipAudioNuitInterieur = initClip();
    Clip clipAudioTheEnd = initClip();

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
        menuON = AudioSystem
                .getAudioInputStream(getClass().getClassLoader().getResourceAsStream("jeu/audio/soundFX/menuOpen.wav"));
        menuOFF = AudioSystem
                .getAudioInputStream(
                        getClass().getClassLoader().getResourceAsStream("jeu/audio/soundFX/menuClose.wav"));
        error = AudioSystem
                .getAudioInputStream(
                        getClass().getClassLoader().getResourceAsStream("jeu/audio/soundFX/error.wav"));
                        mort = AudioSystem
                        .getAudioInputStream(
                                getClass().getClassLoader().getResourceAsStream("jeu/audio/soundFX/die.wav"));

        // Récupération des sons ambiants
        themePrincipal = AudioSystem
                .getAudioInputStream(getClass().getClassLoader().getResourceAsStream("jeu/audio/ambiant/main.wav"));
        nuitExterieur = AudioSystem
                .getAudioInputStream(
                        getClass().getClassLoader().getResourceAsStream("jeu/audio/ambiant/nightTimeExterior.wav"));
        nuitInterieur = AudioSystem
                .getAudioInputStream(
                        getClass().getClassLoader().getResourceAsStream("jeu/audio/ambiant/nightTimeInterior.wav"));
        theEnd = AudioSystem
                .getAudioInputStream(
                        getClass().getClassLoader().getResourceAsStream("jeu/audio/ambiant/end.wav"));

    }

    // Méthode de lecture des sons brefs
    public void jouer(Clip unClip, AudioInputStream unStream)
            throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        if (!unClip.isOpen()) {
            unClip.open(unStream);
            unClip.start();
        }
        // On set la frame sur 0 (début de l'audio)
        // et on démarre le clip (le son se joue)
        unClip.setFramePosition(0);
        unClip.start();
    }

    // Méthode de lecture des sons ambiants
    public void ambiance(Clip unClip, AudioInputStream unStream)
            throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        // Idem que jouer() …
        if (!unClip.isOpen()) {
            unClip.open(unStream);
            unClip.start();
        }
        if (!unClip.isRunning()) {
            unClip.setFramePosition(0);
            unClip.start();
        }
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

    public void jouerAudioMort() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        this.jouer(clipMort, mort);
    }



    // Appels sons ambiants

    public void jouerAmbiantThemePrincipal()
            throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        this.ambiance(clipAudioThemePrincipal, themePrincipal);
    }

    public void jouerAmbiantNuitExterieur()
            throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        this.ambiance(clipAudioNuitExterieur, nuitExterieur);
    }

    public void jouerAmbiantNuitInterieur()
            throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        this.ambiance(clipAudioNuitInterieur, nuitInterieur);
    }

    public void jouerAmbiantEnd()
            throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        this.ambiance(clipAudioTheEnd, theEnd);
    }



    // Méthode d'arrêt sons ambiants
    private void stopAmbiance(Clip unClip) {
        unClip.stop();
    }


    // Exemple d'arrêt d'un son d'ambiance
    public void stopAmbianceThemePrincipal() {
        this.stopAmbiance(clipAudioThemePrincipal);
    }

    public void stopAmbianceNuitExterieur() {
        this.stopAmbiance(clipAudioNuitExterieur);
    }

    public void stopAmbianceNuitInterieur() {
        this.stopAmbiance(clipAudioNuitInterieur);
    }

    public void stopAmbianceTheEnd() {
        this.stopAmbiance(clipAudioTheEnd);
    }

    @Override
    public void update(LineEvent event) {
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

}