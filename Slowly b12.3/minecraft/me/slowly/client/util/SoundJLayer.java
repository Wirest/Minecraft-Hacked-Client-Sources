/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.slowly.client.util;

import java.io.InputStream;
import java.net.URL;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Control;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Line;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.Port;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.AudioDevice;
import javazoom.jl.player.FactoryRegistry;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackListener;

public class SoundJLayer
extends PlaybackListener
implements Runnable {
    private String filePath;
    private AdvancedPlayer player;
    private Thread playerThread;

    public void play(int channel) {
        Runnable run = () -> {
            try {
                String urlAsString = "";
                if (channel == 1) {
                    urlAsString = "http://stream01.iloveradio.de/iloveradio1.mp3";
                } else if (channel == 2) {
                    urlAsString = "http://wdr-1live-live.icecast.wdr.de/wdr/1live/live/mp3/128/stream.mp3";
                } else if (channel == 3) {
                    urlAsString = "http://listen.uturnradio.com/dubstep";
                } else if (channel == 4) {
                    urlAsString = "http://mp3.stream.tb-group.fm/hb.mp3";
                } else {
                    return;
                }
                this.player = new AdvancedPlayer(new URL(urlAsString).openStream(), FactoryRegistry.systemRegistry().createAudioDevice());
                this.player.setPlayBackListener(this);
                this.playerThread = new Thread((Runnable)this, "AudioPlayerThread");
                this.playerThread.start();
            }
            catch (Exception urlAsString) {
                // empty catch block
            }
        };
        new Thread(run).start();
    }

    public static void stop() {
        try {
            AdvancedPlayer.close();
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public static void setVolume(float vol) {
        try {
            Mixer.Info[] infos;
            Mixer.Info[] arrinfo = infos = AudioSystem.getMixerInfo();
            int n = arrinfo.length;
            int n2 = 0;
            while (n2 < n) {
                Mixer.Info info = arrinfo[n2];
                Mixer mixer = AudioSystem.getMixer(info);
                if (mixer.isLineSupported(Port.Info.SPEAKER)) {
                    Port port = (Port)mixer.getLine(Port.Info.SPEAKER);
                    port.open();
                    if (port.isControlSupported(FloatControl.Type.VOLUME)) {
                        FloatControl volume = (FloatControl)port.getControl(FloatControl.Type.VOLUME);
                        volume.setValue(vol / 100.0f);
                    }
                    port.close();
                }
                ++n2;
            }
        }
        catch (Exception infos) {
            // empty catch block
        }
    }

    @Override
    public void run() {
        Runnable run = () -> {
            try {
                this.player.play();
            }
            catch (JavaLayerException e) {
                e.printStackTrace();
            }
        };
        new Thread(run).start();
    }
}

