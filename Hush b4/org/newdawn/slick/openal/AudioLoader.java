// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.openal;

import java.net.URL;
import java.io.IOException;
import java.io.InputStream;

public class AudioLoader
{
    private static final String AIF = "AIF";
    private static final String WAV = "WAV";
    private static final String OGG = "OGG";
    private static final String MOD = "MOD";
    private static final String XM = "XM";
    private static boolean inited;
    
    static {
        AudioLoader.inited = false;
    }
    
    private static void init() {
        if (!AudioLoader.inited) {
            SoundStore.get().init();
            AudioLoader.inited = true;
        }
    }
    
    public static Audio getAudio(final String format, final InputStream in) throws IOException {
        init();
        if (format.equals("AIF")) {
            return SoundStore.get().getAIF(in);
        }
        if (format.equals("WAV")) {
            return SoundStore.get().getWAV(in);
        }
        if (format.equals("OGG")) {
            return SoundStore.get().getOgg(in);
        }
        throw new IOException("Unsupported format for non-streaming Audio: " + format);
    }
    
    public static Audio getStreamingAudio(final String format, final URL url) throws IOException {
        init();
        if (format.equals("OGG")) {
            return SoundStore.get().getOggStream(url);
        }
        if (format.equals("MOD")) {
            return SoundStore.get().getMOD(url.openStream());
        }
        if (format.equals("XM")) {
            return SoundStore.get().getMOD(url.openStream());
        }
        throw new IOException("Unsupported format for streaming Audio: " + format);
    }
    
    public static void update() {
        init();
        SoundStore.get().poll(0);
    }
}
