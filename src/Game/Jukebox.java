package Game;

import javafx.scene.media.AudioClip;


public class Jukebox {
    private boolean mute = false;
    public enum SFX {
        cannon_shoot_v1("res/SFX/cannon_shoot.wav"),
        powerdown_v1("res/SFX/Powerup_1.wav"),
        stickybombexplosion_v1("res/SFX/stickybombexplosion.wav"),
        timeAlmostOverBlip_v1("res/SFX/time_almost_over_blip.wav");

        private String value;

        private SFX(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public static void playSFX(String SFXtoPlay) {
            new AudioClip(getResource(SFXtoPlay)).play();
    }

    static String getResource(String path) {
        return Jukebox.class.getResource(path).toExternalForm();
    }
}
