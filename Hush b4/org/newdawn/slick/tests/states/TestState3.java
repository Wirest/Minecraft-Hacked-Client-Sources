// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.tests.states;

import org.newdawn.slick.state.transition.Transition;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.Font;
import org.newdawn.slick.state.BasicGameState;

public class TestState3 extends BasicGameState
{
    public static final int ID = 3;
    private Font font;
    private String[] options;
    private int selected;
    private StateBasedGame game;
    
    public TestState3() {
        this.options = new String[] { "Start Game", "Credits", "Highscores", "Instructions", "Exit" };
    }
    
    @Override
    public int getID() {
        return 3;
    }
    
    @Override
    public void init(final GameContainer container, final StateBasedGame game) throws SlickException {
        this.font = new AngelCodeFont("testdata/demo2.fnt", "testdata/demo2_00.tga");
        this.game = game;
    }
    
    @Override
    public void render(final GameContainer container, final StateBasedGame game, final Graphics g) {
        g.setFont(this.font);
        g.setColor(Color.blue);
        g.drawString("This is State 3", 200.0f, 50.0f);
        g.setColor(Color.white);
        for (int i = 0; i < this.options.length; ++i) {
            g.drawString(this.options[i], (float)(400 - this.font.getWidth(this.options[i]) / 2), (float)(200 + i * 50));
            if (this.selected == i) {
                g.drawRect(200.0f, (float)(190 + i * 50), 400.0f, 50.0f);
            }
        }
    }
    
    @Override
    public void update(final GameContainer container, final StateBasedGame game, final int delta) {
    }
    
    @Override
    public void keyReleased(final int key, final char c) {
        if (key == 208) {
            ++this.selected;
            if (this.selected >= this.options.length) {
                this.selected = 0;
            }
        }
        if (key == 200) {
            --this.selected;
            if (this.selected < 0) {
                this.selected = this.options.length - 1;
            }
        }
        if (key == 2) {
            this.game.enterState(1, new FadeOutTransition(Color.black), new FadeInTransition(Color.black));
        }
        if (key == 3) {
            this.game.enterState(2, new FadeOutTransition(Color.black), new FadeInTransition(Color.black));
        }
    }
}
