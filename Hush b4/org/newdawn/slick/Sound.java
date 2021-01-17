// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick;

import java.net.URL;
import org.newdawn.slick.util.Log;
import org.newdawn.slick.openal.SoundStore;
import java.io.InputStream;
import org.newdawn.slick.openal.Audio;

public class Sound
{
    private Audio sound;
    
    public Sound(final InputStream in, final String ref) throws SlickException {
        SoundStore.get().init();
        try {
            if (ref.toLowerCase().endsWith(".ogg")) {
                this.sound = SoundStore.get().getOgg(in);
            }
            else if (ref.toLowerCase().endsWith(".wav")) {
                this.sound = SoundStore.get().getWAV(in);
            }
            else if (ref.toLowerCase().endsWith(".aif")) {
                this.sound = SoundStore.get().getAIF(in);
            }
            else {
                if (!ref.toLowerCase().endsWith(".xm") && !ref.toLowerCase().endsWith(".mod")) {
                    throw new SlickException("Only .xm, .mod, .aif, .wav and .ogg are currently supported.");
                }
                this.sound = SoundStore.get().getMOD(in);
            }
        }
        catch (Exception e) {
            Log.error(e);
            throw new SlickException("Failed to load sound: " + ref);
        }
    }
    
    public Sound(final URL url) throws SlickException {
        SoundStore.get().init();
        final String ref = url.getFile();
        try {
            if (ref.toLowerCase().endsWith(".ogg")) {
                this.sound = SoundStore.get().getOgg(url.openStream());
            }
            else if (ref.toLowerCase().endsWith(".wav")) {
                this.sound = SoundStore.get().getWAV(url.openStream());
            }
            else if (ref.toLowerCase().endsWith(".aif")) {
                this.sound = SoundStore.get().getAIF(url.openStream());
            }
            else {
                if (!ref.toLowerCase().endsWith(".xm") && !ref.toLowerCase().endsWith(".mod")) {
                    throw new SlickException("Only .xm, .mod, .aif, .wav and .ogg are currently supported.");
                }
                this.sound = SoundStore.get().getMOD(url.openStream());
            }
        }
        catch (Exception e) {
            Log.error(e);
            throw new SlickException("Failed to load sound: " + ref);
        }
    }
    
    public Sound(final String ref) throws SlickException {
        SoundStore.get().init();
        try {
            if (ref.toLowerCase().endsWith(".ogg")) {
                this.sound = SoundStore.get().getOgg(ref);
            }
            else if (ref.toLowerCase().endsWith(".wav")) {
                this.sound = SoundStore.get().getWAV(ref);
            }
            else if (ref.toLowerCase().endsWith(".aif")) {
                this.sound = SoundStore.get().getAIF(ref);
            }
            else {
                if (!ref.toLowerCase().endsWith(".xm") && !ref.toLowerCase().endsWith(".mod")) {
                    throw new SlickException("Only .xm, .mod, .aif, .wav and .ogg are currently supported.");
                }
                this.sound = SoundStore.get().getMOD(ref);
            }
        }
        catch (Exception e) {
            Log.error(e);
            throw new SlickException("Failed to load sound: " + ref);
        }
    }
    
    public void play() {
        this.play(1.0f, 1.0f);
    }
    
    public void play(final float pitch, final float volume) {
        this.sound.playAsSoundEffect(pitch, volume * SoundStore.get().getSoundVolume(), false);
    }
    
    public void playAt(final float x, final float y, final float z) {
        this.playAt(1.0f, 1.0f, x, y, z);
    }
    
    public void playAt(final float pitch, final float volume, final float x, final float y, final float z) {
        this.sound.playAsSoundEffect(pitch, volume * SoundStore.get().getSoundVolume(), false, x, y, z);
    }
    
    public void loop() {
        this.loop(1.0f, 1.0f);
    }
    
    public void loop(final float pitch, final float volume) {
        this.sound.playAsSoundEffect(pitch, volume * SoundStore.get().getSoundVolume(), true);
    }
    
    public boolean playing() {
        return this.sound.isPlaying();
    }
    
    public void stop() {
        this.sound.stop();
    }
}
