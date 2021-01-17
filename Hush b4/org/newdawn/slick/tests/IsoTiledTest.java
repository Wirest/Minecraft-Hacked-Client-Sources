// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.tests;

import org.newdawn.slick.Game;
import org.newdawn.slick.util.Bootstrap;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.tiled.TiledMap;
import org.newdawn.slick.BasicGame;

public class IsoTiledTest extends BasicGame
{
    private TiledMap tilemap;
    
    public IsoTiledTest() {
        super("Isometric Tiled Map Test");
    }
    
    @Override
    public void init(final GameContainer container) throws SlickException {
        this.tilemap = new TiledMap("testdata/isoexample.tmx", "testdata/");
    }
    
    @Override
    public void update(final GameContainer container, final int delta) throws SlickException {
    }
    
    @Override
    public void render(final GameContainer container, final Graphics g) throws SlickException {
        this.tilemap.render(350, 150);
    }
    
    public static void main(final String[] argv) {
        Bootstrap.runAsApplication(new IsoTiledTest(), 800, 600, false);
    }
}
