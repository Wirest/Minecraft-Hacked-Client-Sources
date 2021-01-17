// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.tests;

import org.newdawn.slick.Game;
import org.newdawn.slick.opengl.SlickCallable;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.Log;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Color;
import org.newdawn.slick.AppGameContainer;
import java.util.ArrayList;
import org.newdawn.slick.BasicGame;

public class TestBox extends BasicGame
{
    private ArrayList games;
    private BasicGame currentGame;
    private int index;
    private AppGameContainer container;
    
    public TestBox() {
        super("Test Box");
        this.games = new ArrayList();
    }
    
    public void addGame(final Class game) {
        this.games.add(game);
    }
    
    private void nextGame() {
        if (this.index == -1) {
            return;
        }
        ++this.index;
        if (this.index >= this.games.size()) {
            this.index = 0;
        }
        this.startGame();
    }
    
    private void startGame() {
        try {
            this.currentGame = this.games.get(this.index).newInstance();
            this.container.getGraphics().setBackground(Color.black);
            this.currentGame.init(this.container);
            this.currentGame.render(this.container, this.container.getGraphics());
        }
        catch (Exception e) {
            Log.error(e);
        }
        this.container.setTitle(this.currentGame.getTitle());
    }
    
    @Override
    public void init(final GameContainer c) throws SlickException {
        if (this.games.size() == 0) {
            (this.currentGame = new BasicGame("NULL") {
                @Override
                public void init(final GameContainer container) throws SlickException {
                }
                
                @Override
                public void update(final GameContainer container, final int delta) throws SlickException {
                }
                
                @Override
                public void render(final GameContainer container, final Graphics g) throws SlickException {
                }
            }).init(c);
            this.index = -1;
        }
        else {
            this.index = 0;
            this.container = (AppGameContainer)c;
            this.startGame();
        }
    }
    
    @Override
    public void update(final GameContainer container, final int delta) throws SlickException {
        this.currentGame.update(container, delta);
    }
    
    @Override
    public void render(final GameContainer container, final Graphics g) throws SlickException {
        SlickCallable.enterSafeBlock();
        this.currentGame.render(container, g);
        SlickCallable.leaveSafeBlock();
    }
    
    @Override
    public void controllerButtonPressed(final int controller, final int button) {
        this.currentGame.controllerButtonPressed(controller, button);
    }
    
    @Override
    public void controllerButtonReleased(final int controller, final int button) {
        this.currentGame.controllerButtonReleased(controller, button);
    }
    
    @Override
    public void controllerDownPressed(final int controller) {
        this.currentGame.controllerDownPressed(controller);
    }
    
    @Override
    public void controllerDownReleased(final int controller) {
        this.currentGame.controllerDownReleased(controller);
    }
    
    @Override
    public void controllerLeftPressed(final int controller) {
        this.currentGame.controllerLeftPressed(controller);
    }
    
    @Override
    public void controllerLeftReleased(final int controller) {
        this.currentGame.controllerLeftReleased(controller);
    }
    
    @Override
    public void controllerRightPressed(final int controller) {
        this.currentGame.controllerRightPressed(controller);
    }
    
    @Override
    public void controllerRightReleased(final int controller) {
        this.currentGame.controllerRightReleased(controller);
    }
    
    @Override
    public void controllerUpPressed(final int controller) {
        this.currentGame.controllerUpPressed(controller);
    }
    
    @Override
    public void controllerUpReleased(final int controller) {
        this.currentGame.controllerUpReleased(controller);
    }
    
    @Override
    public void keyPressed(final int key, final char c) {
        this.currentGame.keyPressed(key, c);
        if (key == 28) {
            this.nextGame();
        }
    }
    
    @Override
    public void keyReleased(final int key, final char c) {
        this.currentGame.keyReleased(key, c);
    }
    
    @Override
    public void mouseMoved(final int oldx, final int oldy, final int newx, final int newy) {
        this.currentGame.mouseMoved(oldx, oldy, newx, newy);
    }
    
    @Override
    public void mousePressed(final int button, final int x, final int y) {
        this.currentGame.mousePressed(button, x, y);
    }
    
    @Override
    public void mouseReleased(final int button, final int x, final int y) {
        this.currentGame.mouseReleased(button, x, y);
    }
    
    @Override
    public void mouseWheelMoved(final int change) {
        this.currentGame.mouseWheelMoved(change);
    }
    
    public static void main(final String[] argv) {
        try {
            final TestBox box = new TestBox();
            box.addGame(AnimationTest.class);
            box.addGame(AntiAliasTest.class);
            box.addGame(BigImageTest.class);
            box.addGame(ClipTest.class);
            box.addGame(DuplicateEmitterTest.class);
            box.addGame(FlashTest.class);
            box.addGame(FontPerformanceTest.class);
            box.addGame(FontTest.class);
            box.addGame(GeomTest.class);
            box.addGame(GradientTest.class);
            box.addGame(GraphicsTest.class);
            box.addGame(ImageBufferTest.class);
            box.addGame(ImageReadTest.class);
            box.addGame(ImageTest.class);
            box.addGame(KeyRepeatTest.class);
            box.addGame(MusicListenerTest.class);
            box.addGame(PackedSheetTest.class);
            box.addGame(PedigreeTest.class);
            box.addGame(PureFontTest.class);
            box.addGame(ShapeTest.class);
            box.addGame(SoundTest.class);
            box.addGame(SpriteSheetFontTest.class);
            box.addGame(TransparentColorTest.class);
            final AppGameContainer container = new AppGameContainer(box);
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e) {
            e.printStackTrace();
        }
    }
}
