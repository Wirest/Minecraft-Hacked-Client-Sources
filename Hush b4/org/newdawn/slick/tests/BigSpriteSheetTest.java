// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.tests;

import org.newdawn.slick.Game;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.BigImage;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.Image;
import org.newdawn.slick.BasicGame;

public class BigSpriteSheetTest extends BasicGame
{
    private Image original;
    private SpriteSheet bigSheet;
    private boolean oldMethod;
    
    public BigSpriteSheetTest() {
        super("Big SpriteSheet Test");
        this.oldMethod = true;
    }
    
    @Override
    public void init(final GameContainer container) throws SlickException {
        this.original = new BigImage("testdata/bigimage.tga", 2, 256);
        this.bigSheet = new SpriteSheet(this.original, 16, 16);
    }
    
    @Override
    public void render(final GameContainer container, final Graphics g) {
        if (this.oldMethod) {
            for (int x = 0; x < 43; ++x) {
                for (int y = 0; y < 27; ++y) {
                    this.bigSheet.getSprite(x, y).draw((float)(10 + x * 18), (float)(50 + y * 18));
                }
            }
        }
        else {
            this.bigSheet.startUse();
            for (int x = 0; x < 43; ++x) {
                for (int y = 0; y < 27; ++y) {
                    this.bigSheet.renderInUse(10 + x * 18, 50 + y * 18, x, y);
                }
            }
            this.bigSheet.endUse();
        }
        g.drawString("Press space to toggle rendering method", 10.0f, 30.0f);
        container.getDefaultFont().drawString(10.0f, 100.0f, "TEST");
    }
    
    public static void main(final String[] argv) {
        try {
            final AppGameContainer container = new AppGameContainer(new BigSpriteSheetTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void update(final GameContainer container, final int delta) throws SlickException {
        if (container.getInput().isKeyPressed(57)) {
            this.oldMethod = !this.oldMethod;
        }
    }
}
