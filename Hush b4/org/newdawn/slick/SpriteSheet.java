// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick;

import org.newdawn.slick.opengl.Texture;
import java.io.InputStream;
import java.io.IOException;
import java.net.URL;

public class SpriteSheet extends Image
{
    private int tw;
    private int th;
    private int margin;
    private Image[][] subImages;
    private int spacing;
    private Image target;
    
    public SpriteSheet(final URL ref, final int tw, final int th) throws SlickException, IOException {
        this(new Image(ref.openStream(), ref.toString(), false), tw, th);
    }
    
    public SpriteSheet(final Image image, final int tw, final int th) {
        super(image);
        this.margin = 0;
        this.target = image;
        this.tw = tw;
        this.th = th;
        this.initImpl();
    }
    
    public SpriteSheet(final Image image, final int tw, final int th, final int spacing, final int margin) {
        super(image);
        this.margin = 0;
        this.target = image;
        this.tw = tw;
        this.th = th;
        this.spacing = spacing;
        this.margin = margin;
        this.initImpl();
    }
    
    public SpriteSheet(final Image image, final int tw, final int th, final int spacing) {
        this(image, tw, th, spacing, 0);
    }
    
    public SpriteSheet(final String ref, final int tw, final int th, final int spacing) throws SlickException {
        this(ref, tw, th, null, spacing);
    }
    
    public SpriteSheet(final String ref, final int tw, final int th) throws SlickException {
        this(ref, tw, th, null);
    }
    
    public SpriteSheet(final String ref, final int tw, final int th, final Color col) throws SlickException {
        this(ref, tw, th, col, 0);
    }
    
    public SpriteSheet(final String ref, final int tw, final int th, final Color col, final int spacing) throws SlickException {
        super(ref, false, 2, col);
        this.margin = 0;
        this.target = this;
        this.tw = tw;
        this.th = th;
        this.spacing = spacing;
    }
    
    public SpriteSheet(final String name, final InputStream ref, final int tw, final int th) throws SlickException {
        super(ref, name, false);
        this.margin = 0;
        this.target = this;
        this.tw = tw;
        this.th = th;
    }
    
    @Override
    protected void initImpl() {
        if (this.subImages != null) {
            return;
        }
        final int tilesAcross = (this.getWidth() - this.margin * 2 - this.tw) / (this.tw + this.spacing) + 1;
        int tilesDown = (this.getHeight() - this.margin * 2 - this.th) / (this.th + this.spacing) + 1;
        if ((this.getHeight() - this.th) % (this.th + this.spacing) != 0) {
            ++tilesDown;
        }
        this.subImages = new Image[tilesAcross][tilesDown];
        for (int x = 0; x < tilesAcross; ++x) {
            for (int y = 0; y < tilesDown; ++y) {
                this.subImages[x][y] = this.getSprite(x, y);
            }
        }
    }
    
    public Image getSubImage(final int x, final int y) {
        this.init();
        if (x < 0 || x >= this.subImages.length) {
            throw new RuntimeException("SubImage out of sheet bounds: " + x + "," + y);
        }
        if (y < 0 || y >= this.subImages[0].length) {
            throw new RuntimeException("SubImage out of sheet bounds: " + x + "," + y);
        }
        return this.subImages[x][y];
    }
    
    public Image getSprite(final int x, final int y) {
        this.target.init();
        this.initImpl();
        if (x < 0 || x >= this.subImages.length) {
            throw new RuntimeException("SubImage out of sheet bounds: " + x + "," + y);
        }
        if (y < 0 || y >= this.subImages[0].length) {
            throw new RuntimeException("SubImage out of sheet bounds: " + x + "," + y);
        }
        return this.target.getSubImage(x * (this.tw + this.spacing) + this.margin, y * (this.th + this.spacing) + this.margin, this.tw, this.th);
    }
    
    public int getHorizontalCount() {
        this.target.init();
        this.initImpl();
        return this.subImages.length;
    }
    
    public int getVerticalCount() {
        this.target.init();
        this.initImpl();
        return this.subImages[0].length;
    }
    
    public void renderInUse(final int x, final int y, final int sx, final int sy) {
        this.subImages[sx][sy].drawEmbedded((float)x, (float)y, (float)this.tw, (float)this.th);
    }
    
    @Override
    public void endUse() {
        if (this.target == this) {
            super.endUse();
            return;
        }
        this.target.endUse();
    }
    
    @Override
    public void startUse() {
        if (this.target == this) {
            super.startUse();
            return;
        }
        this.target.startUse();
    }
    
    @Override
    public void setTexture(final Texture texture) {
        if (this.target == this) {
            super.setTexture(texture);
            return;
        }
        this.target.setTexture(texture);
    }
}
