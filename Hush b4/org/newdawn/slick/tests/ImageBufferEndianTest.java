// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.tests;

import java.nio.ByteOrder;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Game;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.ImageBuffer;
import org.newdawn.slick.BasicGame;

public class ImageBufferEndianTest extends BasicGame
{
    private ImageBuffer redImageBuffer;
    private ImageBuffer blueImageBuffer;
    private Image fromRed;
    private Image fromBlue;
    private String endian;
    
    public ImageBufferEndianTest() {
        super("ImageBuffer Endian Test");
    }
    
    public static void main(final String[] args) {
        try {
            final AppGameContainer container = new AppGameContainer(new ImageBufferEndianTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void render(final GameContainer container, final Graphics g) throws SlickException {
        g.setColor(Color.white);
        g.drawString("Endianness is " + this.endian, 10.0f, 100.0f);
        g.drawString("Image below should be red", 10.0f, 200.0f);
        g.drawImage(this.fromRed, 10.0f, 220.0f);
        g.drawString("Image below should be blue", 410.0f, 200.0f);
        g.drawImage(this.fromBlue, 410.0f, 220.0f);
    }
    
    @Override
    public void init(final GameContainer container) throws SlickException {
        if (ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN) {
            this.endian = "Big endian";
        }
        else if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
            this.endian = "Little endian";
        }
        else {
            this.endian = "no idea";
        }
        this.fillImageBufferWithColor(this.redImageBuffer = new ImageBuffer(100, 100), Color.red, 100, 100);
        this.fillImageBufferWithColor(this.blueImageBuffer = new ImageBuffer(100, 100), Color.blue, 100, 100);
        this.fromRed = this.redImageBuffer.getImage();
        this.fromBlue = this.blueImageBuffer.getImage();
    }
    
    private void fillImageBufferWithColor(final ImageBuffer buffer, final Color c, final int width, final int height) {
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                buffer.setRGBA(x, y, c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
            }
        }
    }
    
    @Override
    public void update(final GameContainer container, final int delta) throws SlickException {
    }
}
