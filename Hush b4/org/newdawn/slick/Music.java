// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick;

import org.newdawn.slick.openal.AudioImpl;
import org.newdawn.slick.util.Log;
import java.io.InputStream;
import java.net.URL;
import org.newdawn.slick.openal.SoundStore;
import java.util.ArrayList;
import org.newdawn.slick.openal.Audio;

public class Music
{
    private static Music currentMusic;
    private Audio sound;
    private boolean playing;
    private ArrayList listeners;
    private float volume;
    private float fadeStartGain;
    private float fadeEndGain;
    private int fadeTime;
    private int fadeDuration;
    private boolean stopAfterFade;
    private boolean positioning;
    private float requiredPosition;
    
    public static void poll(final int delta) {
        if (Music.currentMusic != null) {
            SoundStore.get().poll(delta);
            if (!SoundStore.get().isMusicPlaying()) {
                if (!Music.currentMusic.positioning) {
                    final Music oldMusic = Music.currentMusic;
                    Music.currentMusic = null;
                    oldMusic.fireMusicEnded();
                }
            }
            else {
                Music.currentMusic.update(delta);
            }
        }
    }
    
    public Music(final String ref) throws SlickException {
        this(ref, false);
    }
    
    public Music(final URL ref) throws SlickException {
        this(ref, false);
    }
    
    public Music(final InputStream in, final String ref) throws SlickException {
        this.listeners = new ArrayList();
        this.volume = 1.0f;
        this.requiredPosition = -1.0f;
        SoundStore.get().init();
        try {
            if (ref.toLowerCase().endsWith(".ogg")) {
                this.sound = SoundStore.get().getOgg(in);
            }
            else if (ref.toLowerCase().endsWith(".wav")) {
                this.sound = SoundStore.get().getWAV(in);
            }
            else if (ref.toLowerCase().endsWith(".xm") || ref.toLowerCase().endsWith(".mod")) {
                this.sound = SoundStore.get().getMOD(in);
            }
            else {
                if (!ref.toLowerCase().endsWith(".aif") && !ref.toLowerCase().endsWith(".aiff")) {
                    throw new SlickException("Only .xm, .mod, .ogg, and .aif/f are currently supported.");
                }
                this.sound = SoundStore.get().getAIF(in);
            }
        }
        catch (Exception e) {
            Log.error(e);
            throw new SlickException("Failed to load music: " + ref);
        }
    }
    
    public Music(final URL url, final boolean streamingHint) throws SlickException {
        this.listeners = new ArrayList();
        this.volume = 1.0f;
        this.requiredPosition = -1.0f;
        SoundStore.get().init();
        final String ref = url.getFile();
        try {
            if (ref.toLowerCase().endsWith(".ogg")) {
                if (streamingHint) {
                    this.sound = SoundStore.get().getOggStream(url);
                }
                else {
                    this.sound = SoundStore.get().getOgg(url.openStream());
                }
            }
            else if (ref.toLowerCase().endsWith(".wav")) {
                this.sound = SoundStore.get().getWAV(url.openStream());
            }
            else if (ref.toLowerCase().endsWith(".xm") || ref.toLowerCase().endsWith(".mod")) {
                this.sound = SoundStore.get().getMOD(url.openStream());
            }
            else {
                if (!ref.toLowerCase().endsWith(".aif") && !ref.toLowerCase().endsWith(".aiff")) {
                    throw new SlickException("Only .xm, .mod, .ogg, and .aif/f are currently supported.");
                }
                this.sound = SoundStore.get().getAIF(url.openStream());
            }
        }
        catch (Exception e) {
            Log.error(e);
            throw new SlickException("Failed to load sound: " + url);
        }
    }
    
    public Music(final String ref, final boolean streamingHint) throws SlickException {
        this.listeners = new ArrayList();
        this.volume = 1.0f;
        this.requiredPosition = -1.0f;
        SoundStore.get().init();
        try {
            if (ref.toLowerCase().endsWith(".ogg")) {
                if (streamingHint) {
                    this.sound = SoundStore.get().getOggStream(ref);
                }
                else {
                    this.sound = SoundStore.get().getOgg(ref);
                }
            }
            else if (ref.toLowerCase().endsWith(".wav")) {
                this.sound = SoundStore.get().getWAV(ref);
            }
            else if (ref.toLowerCase().endsWith(".xm") || ref.toLowerCase().endsWith(".mod")) {
                this.sound = SoundStore.get().getMOD(ref);
            }
            else {
                if (!ref.toLowerCase().endsWith(".aif") && !ref.toLowerCase().endsWith(".aiff")) {
                    throw new SlickException("Only .xm, .mod, .ogg, and .aif/f are currently supported.");
                }
                this.sound = SoundStore.get().getAIF(ref);
            }
        }
        catch (Exception e) {
            Log.error(e);
            throw new SlickException("Failed to load sound: " + ref);
        }
    }
    
    public void addListener(final MusicListener listener) {
        this.listeners.add(listener);
    }
    
    public void removeListener(final MusicListener listener) {
        this.listeners.remove(listener);
    }
    
    private void fireMusicEnded() {
        this.playing = false;
        for (int i = 0; i < this.listeners.size(); ++i) {
            this.listeners.get(i).musicEnded(this);
        }
    }
    
    private void fireMusicSwapped(final Music newMusic) {
        this.playing = false;
        for (int i = 0; i < this.listeners.size(); ++i) {
            this.listeners.get(i).musicSwapped(this, newMusic);
        }
    }
    
    public void loop() {
        this.loop(1.0f, 1.0f);
    }
    
    public void play() {
        this.play(1.0f, 1.0f);
    }
    
    public void play(final float pitch, final float volume) {
        this.startMusic(pitch, volume, false);
    }
    
    public void loop(final float pitch, final float volume) {
        this.startMusic(pitch, volume, true);
    }
    
    private void startMusic(final float pitch, float volume, final boolean loop) {
        if (Music.currentMusic != null) {
            Music.currentMusic.stop();
            Music.currentMusic.fireMusicSwapped(this);
        }
        Music.currentMusic = this;
        if (volume < 0.0f) {
            volume = 0.0f;
        }
        if (volume > 1.0f) {
            volume = 1.0f;
        }
        this.sound.playAsMusic(pitch, volume, loop);
        this.playing = true;
        this.setVolume(volume);
        if (this.requiredPosition != -1.0f) {
            this.setPosition(this.requiredPosition);
        }
    }
    
    public void pause() {
        this.playing = false;
        AudioImpl.pauseMusic();
    }
    
    public void stop() {
        this.sound.stop();
    }
    
    public void resume() {
        this.playing = true;
        AudioImpl.restartMusic();
    }
    
    public boolean playing() {
        return Music.currentMusic == this && this.playing;
    }
    
    public void setVolume(float volume) {
        if (volume > 1.0f) {
            volume = 1.0f;
        }
        else if (volume < 0.0f) {
            volume = 0.0f;
        }
        this.volume = volume;
        if (Music.currentMusic == this) {
            SoundStore.get().setCurrentMusicVolume(volume);
        }
    }
    
    public float getVolume() {
        return this.volume;
    }
    
    public void fade(final int duration, final float endVolume, final boolean stopAfterFade) {
        this.stopAfterFade = stopAfterFade;
        this.fadeStartGain = this.volume;
        this.fadeEndGain = endVolume;
        this.fadeDuration = duration;
        this.fadeTime = duration;
    }
    
    void update(final int delta) {
        if (!this.playing) {
            return;
        }
        if (this.fadeTime > 0) {
            this.fadeTime -= delta;
            if (this.fadeTime < 0) {
                this.fadeTime = 0;
                if (this.stopAfterFade) {
                    this.stop();
                    return;
                }
            }
            final float offset = (this.fadeEndGain - this.fadeStartGain) * (1.0f - this.fadeTime / (float)this.fadeDuration);
            this.setVolume(this.fadeStartGain + offset);
        }
    }
    
    public boolean setPosition(final float position) {
        if (this.playing) {
            this.requiredPosition = -1.0f;
            this.positioning = true;
            this.playing = false;
            final boolean result = this.sound.setPosition(position);
            this.playing = true;
            this.positioning = false;
            return result;
        }
        this.requiredPosition = position;
        return false;
    }
    
    public float getPosition() {
        return this.sound.getPosition();
    }
}
