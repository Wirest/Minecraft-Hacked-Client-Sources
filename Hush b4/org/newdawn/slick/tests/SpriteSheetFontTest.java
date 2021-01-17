// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.tests;

import org.newdawn.slick.Game;
import org.newdawn.slick.util.Log;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheetFont;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Font;
import org.newdawn.slick.BasicGame;

public class SpriteSheetFontTest extends BasicGame
{
    private Font font;
    private static AppGameContainer container;
    
    public SpriteSheetFontTest() {
        super("SpriteSheetFont Test");
    }
    
    @Override
    public void init(final GameContainer container) throws SlickException {
        final SpriteSheet sheet = new SpriteSheet("testdata/spriteSheetFont.png", 32, 32);
        this.font = new SpriteSheetFont(sheet, ' ');
    }
    
    @Override
    public void render(final GameContainer container, final Graphics g) {
        g.setBackground(Color.gray);
        this.font.drawString(80.0f, 5.0f, "A FONT EXAMPLE", Color.red);
        this.font.drawString(100.0f, 50.0f, "A MORE COMPLETE LINE");
    }
    
    @Override
    public void update(final GameContainer container, final int delta) throws SlickException {
    }
    
    @Override
    public void keyPressed(final int key, final char c) {
        if (key == 1) {
            System.exit(0);
        }
        if (key == 57) {
            try {
                SpriteSheetFontTest.container.setDisplayMode(640, 480, false);
            }
            catch (SlickException e) {
                Log.error(e);
            }
        }
    }
    
    public static void main(final String[] argv) {
        try {
            (SpriteSheetFontTest.container = new AppGameContainer(new SpriteSheetFontTest())).setDisplayMode(800, 600, false);
            SpriteSheetFontTest.container.start();
        }
        catch (SlickException e) {
            e.printStackTrace();
        }
    }
}
