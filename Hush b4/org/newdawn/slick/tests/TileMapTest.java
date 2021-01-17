// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.tests;

import org.newdawn.slick.Game;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.tiled.TiledMap;
import org.newdawn.slick.BasicGame;

public class TileMapTest extends BasicGame
{
    private TiledMap map;
    private String mapName;
    private String monsterDifficulty;
    private String nonExistingMapProperty;
    private String nonExistingLayerProperty;
    private int updateCounter;
    private static int UPDATE_TIME;
    private int originalTileID;
    
    static {
        TileMapTest.UPDATE_TIME = 1000;
    }
    
    public TileMapTest() {
        super("Tile Map Test");
        this.updateCounter = 0;
        this.originalTileID = 0;
    }
    
    @Override
    public void init(final GameContainer container) throws SlickException {
        this.map = new TiledMap("testdata/testmap.tmx", "testdata");
        this.mapName = this.map.getMapProperty("name", "Unknown map name");
        this.monsterDifficulty = this.map.getLayerProperty(0, "monsters", "easy peasy");
        this.nonExistingMapProperty = this.map.getMapProperty("zaphod", "Undefined map property");
        this.nonExistingLayerProperty = this.map.getLayerProperty(1, "beeblebrox", "Undefined layer property");
        this.originalTileID = this.map.getTileId(10, 10, 0);
    }
    
    @Override
    public void render(final GameContainer container, final Graphics g) {
        this.map.render(10, 10, 4, 4, 15, 15);
        g.scale(0.35f, 0.35f);
        this.map.render(1400, 0);
        g.resetTransform();
        g.drawString("map name: " + this.mapName, 10.0f, 500.0f);
        g.drawString("monster difficulty: " + this.monsterDifficulty, 10.0f, 550.0f);
        g.drawString("non existing map property: " + this.nonExistingMapProperty, 10.0f, 525.0f);
        g.drawString("non existing layer property: " + this.nonExistingLayerProperty, 10.0f, 575.0f);
    }
    
    @Override
    public void update(final GameContainer container, final int delta) {
        this.updateCounter += delta;
        if (this.updateCounter > TileMapTest.UPDATE_TIME) {
            this.updateCounter -= TileMapTest.UPDATE_TIME;
            final int currentTileID = this.map.getTileId(10, 10, 0);
            if (currentTileID != this.originalTileID) {
                this.map.setTileId(10, 10, 0, this.originalTileID);
            }
            else {
                this.map.setTileId(10, 10, 0, 1);
            }
        }
    }
    
    @Override
    public void keyPressed(final int key, final char c) {
        if (key == 1) {
            System.exit(0);
        }
    }
    
    public static void main(final String[] argv) {
        try {
            final AppGameContainer container = new AppGameContainer(new TileMapTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e) {
            e.printStackTrace();
        }
    }
}
