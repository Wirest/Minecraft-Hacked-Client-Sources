// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.tests;

import org.newdawn.slick.Game;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import java.io.IOException;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.util.ResourceLoader;
import org.newdawn.slick.openal.SoundStore;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.Music;
import org.newdawn.slick.Sound;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.BasicGame;

public class SoundTest extends BasicGame
{
    private GameContainer myContainer;
    private Sound sound;
    private Sound charlie;
    private Sound burp;
    private Music music;
    private Music musica;
    private Music musicb;
    private Audio engine;
    private int volume;
    private int[] engines;
    
    public SoundTest() {
        super("Sound And Music Test");
        this.volume = 10;
        this.engines = new int[3];
    }
    
    @Override
    public void init(final GameContainer container) throws SlickException {
        SoundStore.get().setMaxSources(32);
        this.myContainer = container;
        this.sound = new Sound("testdata/restart.ogg");
        this.charlie = new Sound("testdata/cbrown01.wav");
        try {
            this.engine = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("testdata/engine.wav"));
        }
        catch (IOException e) {
            throw new SlickException("Failed to load engine", e);
        }
        final Music music = new Music("testdata/SMB-X.XM");
        this.musica = music;
        this.music = music;
        this.musicb = new Music("testdata/kirby.ogg", true);
        this.burp = new Sound("testdata/burp.aif");
        this.music.play();
    }
    
    @Override
    public void render(final GameContainer container, final Graphics g) {
        g.setColor(Color.white);
        g.drawString("The OGG loop is now streaming from the file, woot.", 100.0f, 60.0f);
        g.drawString("Press space for sound effect (OGG)", 100.0f, 100.0f);
        g.drawString("Press P to pause/resume music (XM)", 100.0f, 130.0f);
        g.drawString("Press E to pause/resume engine sound (WAV)", 100.0f, 190.0f);
        g.drawString("Press enter for charlie (WAV)", 100.0f, 160.0f);
        g.drawString("Press C to change music", 100.0f, 210.0f);
        g.drawString("Press B to burp (AIF)", 100.0f, 240.0f);
        g.drawString("Press + or - to change global volume of music", 100.0f, 270.0f);
        g.drawString("Press Y or X to change individual volume of music", 100.0f, 300.0f);
        g.drawString("Press N or M to change global volume of sound fx", 100.0f, 330.0f);
        g.setColor(Color.blue);
        g.drawString("Global Sound Volume Level: " + container.getSoundVolume(), 150.0f, 390.0f);
        g.drawString("Global Music Volume Level: " + container.getMusicVolume(), 150.0f, 420.0f);
        g.drawString("Current Music Volume Level: " + this.music.getVolume(), 150.0f, 450.0f);
    }
    
    @Override
    public void update(final GameContainer container, final int delta) {
    }
    
    @Override
    public void keyPressed(final int key, final char c) {
        if (key == 1) {
            System.exit(0);
        }
        if (key == 57) {
            this.sound.play();
        }
        if (key == 48) {
            this.burp.play();
        }
        if (key == 30) {
            this.sound.playAt(-1.0f, 0.0f, 0.0f);
        }
        if (key == 38) {
            this.sound.playAt(1.0f, 0.0f, 0.0f);
        }
        if (key == 28) {
            this.charlie.play(1.0f, 1.0f);
        }
        if (key == 25) {
            if (this.music.playing()) {
                this.music.pause();
            }
            else {
                this.music.resume();
            }
        }
        if (key == 46) {
            this.music.stop();
            if (this.music == this.musica) {
                this.music = this.musicb;
            }
            else {
                this.music = this.musica;
            }
            this.music.loop();
        }
        for (int i = 0; i < 3; ++i) {
            if (key == 2 + i) {
                if (this.engines[i] != 0) {
                    System.out.println("Stop " + i);
                    SoundStore.get().stopSoundEffect(this.engines[i]);
                    this.engines[i] = 0;
                }
                else {
                    System.out.println("Start " + i);
                    this.engines[i] = this.engine.playAsSoundEffect(1.0f, 1.0f, true);
                }
            }
        }
        if (c == '+') {
            ++this.volume;
            this.setVolume();
        }
        if (c == '-') {
            --this.volume;
            this.setVolume();
        }
        if (key == 21) {
            int vol = (int)(this.music.getVolume() * 10.0f);
            if (--vol < 0) {
                vol = 0;
            }
            this.music.setVolume(vol / 10.0f);
        }
        if (key == 45) {
            int vol = (int)(this.music.getVolume() * 10.0f);
            if (++vol > 10) {
                vol = 10;
            }
            this.music.setVolume(vol / 10.0f);
        }
        if (key == 49) {
            int vol = (int)(this.myContainer.getSoundVolume() * 10.0f);
            if (--vol < 0) {
                vol = 0;
            }
            this.myContainer.setSoundVolume(vol / 10.0f);
        }
        if (key == 50) {
            int vol = (int)(this.myContainer.getSoundVolume() * 10.0f);
            if (++vol > 10) {
                vol = 10;
            }
            this.myContainer.setSoundVolume(vol / 10.0f);
        }
    }
    
    private void setVolume() {
        if (this.volume > 10) {
            this.volume = 10;
        }
        else if (this.volume < 0) {
            this.volume = 0;
        }
        this.myContainer.setMusicVolume(this.volume / 10.0f);
    }
    
    public static void main(final String[] argv) {
        try {
            final AppGameContainer container = new AppGameContainer(new SoundTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e) {
            e.printStackTrace();
        }
    }
}
