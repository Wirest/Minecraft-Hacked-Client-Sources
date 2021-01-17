// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.tests;

import org.newdawn.slick.Game;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.GameContainer;
import java.util.ArrayList;
import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.BasicGame;

public class FontPerformanceTest extends BasicGame
{
    private AngelCodeFont font;
    private String text;
    private ArrayList lines;
    private boolean visible;
    
    public FontPerformanceTest() {
        super("Font Performance Test");
        this.text = "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Proin bibendum. Aliquam ac sapien a elit congue iaculis. Quisque et justo quis mi mattis euismod. Donec elementum, mi quis aliquet varius, nisi leo volutpat magna, quis ultricies eros augue at risus. Integer non magna at lorem sodales molestie. Integer diam nulla, ornare sit amet, mattis quis, euismod et, mauris. Proin eget tellus non nisl mattis laoreet. Nunc at nunc id elit pretium tempor. Duis vulputate, nibh eget rhoncus eleifend, tellus lectus sollicitudin mi, rhoncus tincidunt nisi massa vitae ipsum. Praesent tellus diam, luctus ut, eleifend nec, auctor et, orci. Praesent eu elit. Pellentesque ante orci, volutpat placerat, ornare eget, cursus sit amet, eros. Duis pede sapien, euismod a, volutpat pellentesque, convallis eu, mauris. Nunc eros. Ut eu risus et felis laoreet viverra. Curabitur a metus.";
        this.lines = new ArrayList();
        this.visible = true;
    }
    
    @Override
    public void init(final GameContainer container) throws SlickException {
        this.font = new AngelCodeFont("testdata/perffont.fnt", "testdata/perffont.png");
        for (int j = 0; j < 2; ++j) {
            for (int lineLen = 90, i = 0; i < this.text.length(); i += lineLen) {
                if (i + lineLen > this.text.length()) {
                    lineLen = this.text.length() - i;
                }
                this.lines.add(this.text.substring(i, i + lineLen));
            }
            this.lines.add("");
        }
    }
    
    @Override
    public void render(final GameContainer container, final Graphics g) {
        g.setFont(this.font);
        if (this.visible) {
            for (int i = 0; i < this.lines.size(); ++i) {
                this.font.drawString(10.0f, (float)(50 + i * 20), this.lines.get(i), (i > 10) ? Color.red : Color.green);
            }
        }
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
            this.visible = !this.visible;
        }
    }
    
    public static void main(final String[] argv) {
        try {
            final AppGameContainer container = new AppGameContainer(new FontPerformanceTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e) {
            e.printStackTrace();
        }
    }
}
