// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.tests;

import org.newdawn.slick.Game;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.ResourceLoader;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Music;
import org.newdawn.slick.Sound;
import org.newdawn.slick.BasicGame;

public class SoundURLTest extends BasicGame
{
    private Sound sound;
    private Sound charlie;
    private Sound burp;
    private Music music;
    private Music musica;
    private Music musicb;
    private Sound engine;
    private int volume;
    
    public SoundURLTest() {
        super("Sound URL Test");
        this.volume = 1;
    }
    
    @Override
    public void init(final GameContainer container) throws SlickException {
        this.sound = new Sound(ResourceLoader.getResource("testdata/restart.ogg"));
        this.charlie = new Sound(ResourceLoader.getResource("testdata/cbrown01.wav"));
        this.engine = new Sound(ResourceLoader.getResource("testdata/engine.wav"));
        final Music music = new Music(ResourceLoader.getResource("testdata/restart.ogg"), false);
        this.musica = music;
        this.music = music;
        this.musicb = new Music(ResourceLoader.getResource("testdata/kirby.ogg"), false);
        this.burp = new Sound(ResourceLoader.getResource("testdata/burp.aif"));
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
        g.drawString("Press + or - to change volume of music", 100.0f, 270.0f);
        g.setColor(Color.blue);
        g.drawString("Music Volume Level: " + this.volume / 10.0f, 150.0f, 300.0f);
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
        if (key == 18) {
            if (this.engine.playing()) {
                this.engine.stop();
            }
            else {
                this.engine.loop();
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
    }
    
    private void setVolume() {
        if (this.volume > 10) {
            this.volume = 10;
        }
        else if (this.volume < 0) {
            this.volume = 0;
        }
        this.music.setVolume(this.volume / 10.0f);
    }
    
    public static void main(final String[] argv) {
        try {
            final AppGameContainer container = new AppGameContainer(new SoundURLTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e) {
            e.printStackTrace();
        }
    }
}
