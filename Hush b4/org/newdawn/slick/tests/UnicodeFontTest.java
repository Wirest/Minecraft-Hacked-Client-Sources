// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.tests;

import java.io.IOException;
import org.newdawn.slick.Game;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.font.effects.ColorEffect;
import java.awt.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.BasicGame;

public class UnicodeFontTest extends BasicGame
{
    private UnicodeFont unicodeFont;
    
    public UnicodeFontTest() {
        super("Font Test");
    }
    
    @Override
    public void init(final GameContainer container) throws SlickException {
        container.setShowFPS(false);
        this.unicodeFont = new UnicodeFont("c:/windows/fonts/arial.ttf", 48, false, false);
        this.unicodeFont.getEffects().add(new ColorEffect(Color.white));
        container.getGraphics().setBackground(org.newdawn.slick.Color.darkGray);
    }
    
    @Override
    public void render(final GameContainer container, final Graphics g) {
        g.setColor(org.newdawn.slick.Color.white);
        final String text = "This is UnicodeFont!\nIt rockz. Kerning: T,";
        this.unicodeFont.drawString(10.0f, 33.0f, text);
        g.setColor(org.newdawn.slick.Color.red);
        g.drawRect(10.0f, 33.0f, (float)this.unicodeFont.getWidth(text), (float)this.unicodeFont.getLineHeight());
        g.setColor(org.newdawn.slick.Color.blue);
        final int yOffset = this.unicodeFont.getYOffset(text);
        g.drawRect(10.0f, (float)(33 + yOffset), (float)this.unicodeFont.getWidth(text), (float)(this.unicodeFont.getHeight(text) - yOffset));
        this.unicodeFont.addGlyphs("~!@!#!#$%___--");
    }
    
    @Override
    public void update(final GameContainer container, final int delta) throws SlickException {
        this.unicodeFont.loadGlyphs(1);
    }
    
    public static void main(final String[] args) throws SlickException, IOException {
        Input.disableControllers();
        final AppGameContainer container = new AppGameContainer(new UnicodeFontTest());
        container.setDisplayMode(512, 600, false);
        container.setTargetFrameRate(20);
        container.start();
    }
}
