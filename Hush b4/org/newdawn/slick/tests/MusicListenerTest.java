// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.tests;

import org.newdawn.slick.Game;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Music;
import org.newdawn.slick.MusicListener;
import org.newdawn.slick.BasicGame;

public class MusicListenerTest extends BasicGame implements MusicListener
{
    private boolean musicEnded;
    private boolean musicSwapped;
    private Music music;
    private Music stream;
    
    public MusicListenerTest() {
        super("Music Listener Test");
        this.musicEnded = false;
        this.musicSwapped = false;
    }
    
    @Override
    public void init(final GameContainer container) throws SlickException {
        this.music = new Music("testdata/restart.ogg", false);
        this.stream = new Music("testdata/restart.ogg", false);
        this.music.addListener(this);
        this.stream.addListener(this);
    }
    
    @Override
    public void update(final GameContainer container, final int delta) throws SlickException {
    }
    
    @Override
    public void musicEnded(final Music music) {
        this.musicEnded = true;
    }
    
    @Override
    public void musicSwapped(final Music music, final Music newMusic) {
        this.musicSwapped = true;
    }
    
    @Override
    public void render(final GameContainer container, final Graphics g) throws SlickException {
        g.drawString("Press M to play music", 100.0f, 100.0f);
        g.drawString("Press S to stream music", 100.0f, 150.0f);
        if (this.musicEnded) {
            g.drawString("Music Ended", 100.0f, 200.0f);
        }
        if (this.musicSwapped) {
            g.drawString("Music Swapped", 100.0f, 250.0f);
        }
    }
    
    @Override
    public void keyPressed(final int key, final char c) {
        if (key == 50) {
            this.musicEnded = false;
            this.musicSwapped = false;
            this.music.play();
        }
        if (key == 31) {
            this.musicEnded = false;
            this.musicSwapped = false;
            this.stream.play();
        }
    }
    
    public static void main(final String[] argv) {
        try {
            final AppGameContainer container = new AppGameContainer(new MusicListenerTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e) {
            e.printStackTrace();
        }
    }
}
