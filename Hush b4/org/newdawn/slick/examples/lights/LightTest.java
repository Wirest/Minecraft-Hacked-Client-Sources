// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.examples.lights;

import org.newdawn.slick.Game;
import org.newdawn.slick.util.Bootstrap;
import org.newdawn.slick.Image;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.GameContainer;
import java.util.ArrayList;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.BasicGame;

public class LightTest extends BasicGame
{
    private static final int WIDTH = 15;
    private static final int HEIGHT = 15;
    private boolean lightingOn;
    private boolean colouredLights;
    private SpriteSheet tiles;
    private int[][] map;
    private float[][][] lightValue;
    private ArrayList lights;
    private Light mainLight;
    
    public LightTest() {
        super("Light Test");
        this.lightingOn = true;
        this.colouredLights = false;
        this.map = new int[15][15];
        this.lightValue = new float[16][16][3];
        this.lights = new ArrayList();
    }
    
    public void init(final GameContainer container) throws SlickException {
        this.tiles = new SpriteSheet("testdata/tiles.png", 32, 32);
        this.generateMap();
    }
    
    private void generateMap() {
        for (int y = 0; y < 15; ++y) {
            for (int x = 0; x < 15; ++x) {
                this.map[x][y] = 0;
                if (Math.random() > 0.8) {
                    this.map[x][y] = 1 + (int)(Math.random() * 7.0);
                }
            }
        }
        this.lights.clear();
        this.mainLight = new Light(8.0f, 7.0f, 4.0f, Color.white);
        this.lights.add(this.mainLight);
        this.lights.add(new Light(2.0f, 2.0f, 2.0f, Color.red));
        this.lights.add(new Light(2.0f, 11.0f, 1.5f, Color.yellow));
        this.lights.add(new Light(12.0f, 2.0f, 3.0f, Color.green));
        this.updateLightMap();
    }
    
    private void updateLightMap() {
        for (int y = 0; y < 16; ++y) {
            for (int x = 0; x < 16; ++x) {
                for (int component = 0; component < 3; ++component) {
                    this.lightValue[x][y][component] = 0.0f;
                }
                for (int i = 0; i < this.lights.size(); ++i) {
                    final float[] effect = this.lights.get(i).getEffectAt((float)x, (float)y, this.colouredLights);
                    for (int component2 = 0; component2 < 3; ++component2) {
                        final float[] array = this.lightValue[x][y];
                        final int n = component2;
                        array[n] += effect[component2];
                    }
                }
                for (int component = 0; component < 3; ++component) {
                    if (this.lightValue[x][y][component] > 1.0f) {
                        this.lightValue[x][y][component] = 1.0f;
                    }
                }
            }
        }
    }
    
    public void update(final GameContainer container, final int delta) throws SlickException {
        if (container.getInput().isKeyPressed(38)) {
            this.lightingOn = !this.lightingOn;
        }
        if (container.getInput().isKeyPressed(46)) {
            this.colouredLights = !this.colouredLights;
            this.updateLightMap();
        }
    }
    
    public void mouseDragged(final int oldx, final int oldy, final int newx, final int newy) {
        this.mousePressed(0, newx, newy);
    }
    
    public void mousePressed(final int button, final int x, final int y) {
        this.mainLight.setLocation((x - 64) / 32.0f, (y - 50) / 32.0f);
        this.updateLightMap();
    }
    
    public void render(final GameContainer container, final Graphics g) throws SlickException {
        g.setColor(Color.white);
        g.drawString("Lighting Example", 440.0f, 5.0f);
        g.drawString("Press L to toggle light", 80.0f, 560.0f);
        g.drawString("Press C to toggle coloured lights", 80.0f, 575.0f);
        g.drawString("Click or Drag to move the main light", 80.0f, 545.0f);
        g.translate(64.0f, 50.0f);
        this.tiles.startUse();
        for (int y = 0; y < 15; ++y) {
            for (int x = 0; x < 15; ++x) {
                final int tile = this.map[x][y];
                final Image image = this.tiles.getSubImage(tile % 4, tile / 4);
                if (this.lightingOn) {
                    image.setColor(0, this.lightValue[x][y][0], this.lightValue[x][y][1], this.lightValue[x][y][2], 1.0f);
                    image.setColor(1, this.lightValue[x + 1][y][0], this.lightValue[x + 1][y][1], this.lightValue[x + 1][y][2], 1.0f);
                    image.setColor(2, this.lightValue[x + 1][y + 1][0], this.lightValue[x + 1][y + 1][1], this.lightValue[x + 1][y + 1][2], 1.0f);
                    image.setColor(3, this.lightValue[x][y + 1][0], this.lightValue[x][y + 1][1], this.lightValue[x][y + 1][2], 1.0f);
                }
                else {
                    final float light = 1.0f;
                    image.setColor(0, light, light, light, 1.0f);
                    image.setColor(1, light, light, light, 1.0f);
                    image.setColor(2, light, light, light, 1.0f);
                    image.setColor(3, light, light, light, 1.0f);
                }
                image.drawEmbedded((float)(x * 32), (float)(y * 32), 32.0f, 32.0f);
            }
        }
        this.tiles.endUse();
    }
    
    public static void main(final String[] argv) {
        Bootstrap.runAsApplication(new LightTest(), 600, 600, false);
    }
}
