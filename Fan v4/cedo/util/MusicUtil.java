package cedo.util;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStream;

public class MusicUtil {
    public static AudioInputStream audioInputStream;
    public static Long currentPosition;
    public static Clip clip;

    public static void playMusic(String filePath) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        if (clip != null) {
            clip.stop();
        }
        InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation(filePath)).getInputStream();
        audioInputStream = AudioSystem.getAudioInputStream(is);
        clip = AudioSystem.getClip();


        clip.open(audioInputStream);
        try {
            clip.start();
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception ex) {
            System.out.println("Error with playing sound.");
            ex.printStackTrace();
        }

    }

    public static void pause() {
        if (clip != null) {
            currentPosition = clip.getMicrosecondPosition();
            clip.stop();
            clip = null;
            System.out.println(currentPosition);
        }
    }

}
