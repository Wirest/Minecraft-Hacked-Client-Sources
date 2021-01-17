// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.examples.scroller;

import org.newdawn.slick.Game;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.Log;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Animation;
import org.newdawn.slick.tiled.TiledMap;
import org.newdawn.slick.BasicGame;

public class Scroller extends BasicGame
{
    private static final int TANK_SIZE = 32;
    private static final int TILE_SIZE = 32;
    private static final float TANK_MOVE_SPEED = 0.003f;
    private static final float TANK_ROTATE_SPEED = 0.2f;
    private float playerX;
    private float playerY;
    private int widthInTiles;
    private int heightInTiles;
    private int topOffsetInTiles;
    private int leftOffsetInTiles;
    private TiledMap map;
    private Animation player;
    private float ang;
    private float dirX;
    private float dirY;
    private boolean[][] blocked;
    
    public Scroller() {
        super("Scroller");
        this.playerX = 15.0f;
        this.playerY = 16.0f;
    }
    
    public void init(final GameContainer container) throws SlickException {
        final SpriteSheet sheet = new SpriteSheet("testdata/scroller/sprites.png", 32, 32);
        this.map = new TiledMap("testdata/scroller/map.tmx");
        this.blocked = new boolean[this.map.getWidth()][this.map.getHeight()];
        for (int x = 0; x < this.map.getWidth(); ++x) {
            for (int y = 0; y < this.map.getHeight(); ++y) {
                final int tileID = this.map.getTileId(x, y, 0);
                final String value = this.map.getTileProperty(tileID, "blocked", "false");
                if ("true".equals(value)) {
                    this.blocked[x][y] = true;
                }
            }
        }
        this.widthInTiles = container.getWidth() / 32;
        this.heightInTiles = container.getHeight() / 32;
        this.topOffsetInTiles = this.heightInTiles / 2;
        this.leftOffsetInTiles = this.widthInTiles / 2;
        this.player = new Animation();
        for (int frame = 0; frame < 7; ++frame) {
            this.player.addFrame(sheet.getSprite(frame, 1), 150);
        }
        this.player.setAutoUpdate(false);
        this.updateMovementVector();
        Log.info("Window Dimensions in Tiles: " + this.widthInTiles + "x" + this.heightInTiles);
    }
    
    public void update(final GameContainer container, final int delta) throws SlickException {
        if (container.getInput().isKeyDown(203)) {
            this.ang -= delta * 0.2f;
            this.updateMovementVector();
        }
        if (container.getInput().isKeyDown(205)) {
            this.ang += delta * 0.2f;
            this.updateMovementVector();
        }
        if (container.getInput().isKeyDown(200) && this.tryMove(this.dirX * delta * 0.003f, this.dirY * delta * 0.003f)) {
            this.player.update(delta);
        }
        if (container.getInput().isKeyDown(208) && this.tryMove(-this.dirX * delta * 0.003f, -this.dirY * delta * 0.003f)) {
            this.player.update(delta);
        }
    }
    
    private boolean blocked(final float x, final float y) {
        return this.blocked[(int)x][(int)y];
    }
    
    private boolean tryMove(final float x, final float y) {
        final float newx = this.playerX + x;
        final float newy = this.playerY + y;
        if (!this.blocked(newx, newy)) {
            this.playerX = newx;
            this.playerY = newy;
            return true;
        }
        if (!this.blocked(newx, this.playerY)) {
            this.playerX = newx;
            return true;
        }
        if (this.blocked(this.playerX, newy)) {
            return false;
        }
        this.playerY = newy;
        return true;
    }
    
    private void updateMovementVector() {
        this.dirX = (float)Math.sin(Math.toRadians(this.ang));
        this.dirY = (float)(-Math.cos(Math.toRadians(this.ang)));
    }
    
    public void render(final GameContainer container, final Graphics g) throws SlickException {
        final int playerTileX = (int)this.playerX;
        final int playerTileY = (int)this.playerY;
        final int playerTileOffsetX = (int)((playerTileX - this.playerX) * 32.0f);
        final int playerTileOffsetY = (int)((playerTileY - this.playerY) * 32.0f);
        this.map.render(playerTileOffsetX - 16, playerTileOffsetY - 16, playerTileX - this.leftOffsetInTiles - 1, playerTileY - this.topOffsetInTiles - 1, this.widthInTiles + 3, this.heightInTiles + 3);
        g.translate((float)(400 - (int)(this.playerX * 32.0f)), (float)(300 - (int)(this.playerY * 32.0f)));
        this.drawTank(g, this.playerX, this.playerY, this.ang);
        g.resetTransform();
    }
    
    public void drawTank(final Graphics g, final float xpos, final float ypos, final float rot) {
        final int cx = (int)(xpos * 32.0f);
        final int cy = (int)(ypos * 32.0f);
        g.rotate((float)cx, (float)cy, rot);
        this.player.draw((float)(cx - 16), (float)(cy - 16));
        g.rotate((float)cx, (float)cy, -rot);
    }
    
    public static void main(final String[] argv) {
        try {
            final AppGameContainer container = new AppGameContainer(new Scroller(), 800, 600, false);
            container.start();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
