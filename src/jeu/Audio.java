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

    AudioInputStream bruitDePas;
    AudioInputStream dialogue;
    AudioInputStream next;
    AudioInputStream ok;
    AudioInputStream menu;
    AudioInputStream resume;
    AudioInputStream inventory;
    Clip myAudioClip = initClip();

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
        bruitDePas = AudioSystem
                .getAudioInputStream(getClass().getClassLoader().getResourceAsStream("jeu/audio/soundFX/walk.wav"));
        dialogue = AudioSystem
                .getAudioInputStream(getClass().getClassLoader().getResourceAsStream("jeu/audio/soundFX/walk.wav"));
        next = AudioSystem
                .getAudioInputStream(getClass().getClassLoader().getResourceAsStream("jeu/audio/soundFX/page.wav"));
        ok = AudioSystem
                .getAudioInputStream(getClass().getClassLoader().getResourceAsStream("jeu/audio/soundFX/walk.wav"));
        menu = AudioSystem
                .getAudioInputStream(getClass().getClassLoader().getResourceAsStream("jeu/audio/soundFX/walk.wav"));
        resume = AudioSystem
                .getAudioInputStream(getClass().getClassLoader().getResourceAsStream("jeu/audio/soundFX/page.wav"));
        inventory = AudioSystem
                .getAudioInputStream(getClass().getClassLoader().getResourceAsStream("jeu/audio/soundFX/page.wav"));

    }

    public void jouerAudioPas() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        this.jouer(bruitDePas);
    }

    public void jouerAudioNext() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        this.jouer(next);
    }

    public void jouer(AudioInputStream myStream)
            throws UnsupportedAudioFileException, LineUnavailableException, IOException {

        // Calcule la longueur en ms du fichier audio
        long dureeEnMilis = 1000 * myStream.getFrameLength() / (long) myStream.getFormat().getFrameRate();

        // si l'audioclip n'est pas ouvert, on en ouvre un
        // avec l'audioStream passé en paramètre de la méthode
        if (!myAudioClip.isOpen()) {
            myAudioClip.open(myStream);
        }

        // On démarre le clip audio (le son se joue)
        myAudioClip.start();

        // On patiente la durée de l'audio
            try {
                Thread.sleep(dureeEnMilis);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        
        // Enfin, on arrête l'audio et on replace le clip à 0s
        // NB: Si le timer précédent ne s'est pas executé, le son ne se lancera pas
        myAudioClip.stop();
        myAudioClip.setFramePosition(0);
    }

    @Override
    public void update(LineEvent event) {
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

}