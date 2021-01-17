// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.util.OperationNotSupportedException;
import org.newdawn.slick.opengl.ImageData;
import java.io.IOException;
import org.newdawn.slick.util.ResourceLoader;
import org.newdawn.slick.opengl.ImageDataFactory;
import java.nio.ByteBuffer;
import org.newdawn.slick.opengl.LoadableImageData;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import org.newdawn.slick.opengl.renderer.Renderer;
import org.newdawn.slick.opengl.renderer.SGL;

public class BigImage extends Image
{
    protected static SGL GL;
    private static Image lastBind;
    private Image[][] images;
    private int xcount;
    private int ycount;
    private int realWidth;
    private int realHeight;
    
    static {
        BigImage.GL = Renderer.get();
    }
    
    public static final int getMaxSingleImageSize() {
        final IntBuffer buffer = BufferUtils.createIntBuffer(16);
        BigImage.GL.glGetInteger(3379, buffer);
        return buffer.get(0);
    }
    
    private BigImage() {
        this.inited = true;
    }
    
    public BigImage(final String ref) throws SlickException {
        this(ref, 2);
    }
    
    public BigImage(final String ref, final int filter) throws SlickException {
        this.build(ref, filter, getMaxSingleImageSize());
    }
    
    public BigImage(final String ref, final int filter, final int tileSize) throws SlickException {
        this.build(ref, filter, tileSize);
    }
    
    public BigImage(final LoadableImageData data, final ByteBuffer imageBuffer, final int filter) {
        this.build(data, imageBuffer, filter, getMaxSingleImageSize());
    }
    
    public BigImage(final LoadableImageData data, final ByteBuffer imageBuffer, final int filter, final int tileSize) {
        this.build(data, imageBuffer, filter, tileSize);
    }
    
    public Image getTile(final int x, final int y) {
        return this.images[x][y];
    }
    
    private void build(final String ref, final int filter, final int tileSize) throws SlickException {
        try {
            final LoadableImageData data = ImageDataFactory.getImageDataFor(ref);
            final ByteBuffer imageBuffer = data.loadImage(ResourceLoader.getResourceAsStream(ref), false, null);
            this.build(data, imageBuffer, filter, tileSize);
        }
        catch (IOException e) {
            throw new SlickException("Failed to load: " + ref, e);
        }
    }
    
    private void build(final LoadableImageData data, final ByteBuffer imageBuffer, final int filter, final int tileSize) {
        final int dataWidth = data.getTexWidth();
        final int dataHeight = data.getTexHeight();
        final int width = data.getWidth();
        this.width = width;
        this.realWidth = width;
        final int height = data.getHeight();
        this.height = height;
        this.realHeight = height;
        if (dataWidth <= tileSize && dataHeight <= tileSize) {
            this.images = new Image[1][1];
            final ImageData tempData = new ImageData() {
                @Override
                public int getDepth() {
                    return data.getDepth();
                }
                
                @Override
                public int getHeight() {
                    return dataHeight;
                }
                
                @Override
                public ByteBuffer getImageBufferData() {
                    return imageBuffer;
                }
                
                @Override
                public int getTexHeight() {
                    return dataHeight;
                }
                
                @Override
                public int getTexWidth() {
                    return dataWidth;
                }
                
                @Override
                public int getWidth() {
                    return dataWidth;
                }
            };
            this.images[0][0] = new Image(tempData, filter);
            this.xcount = 1;
            this.ycount = 1;
            this.inited = true;
            return;
        }
        this.xcount = (this.realWidth - 1) / tileSize + 1;
        this.ycount = (this.realHeight - 1) / tileSize + 1;
        this.images = new Image[this.xcount][this.ycount];
        final int components = data.getDepth() / 8;
        for (int x = 0; x < this.xcount; ++x) {
            for (int y = 0; y < this.ycount; ++y) {
                final int finalX = (x + 1) * tileSize;
                final int finalY = (y + 1) * tileSize;
                final int imageWidth = Math.min(this.realWidth - x * tileSize, tileSize);
                final int imageHeight = Math.min(this.realHeight - y * tileSize, tileSize);
                final int xSize = tileSize;
                final int ySize = tileSize;
                final ByteBuffer subBuffer = BufferUtils.createByteBuffer(tileSize * tileSize * components);
                final int xo = x * tileSize * components;
                final byte[] byteData = new byte[xSize * components];
                for (int i = 0; i < ySize; ++i) {
                    final int yo = (y * tileSize + i) * dataWidth * components;
                    imageBuffer.position(yo + xo);
                    imageBuffer.get(byteData, 0, xSize * components);
                    subBuffer.put(byteData);
                }
                subBuffer.flip();
                final ImageData imgData = new ImageData() {
                    @Override
                    public int getDepth() {
                        return data.getDepth();
                    }
                    
                    @Override
                    public int getHeight() {
                        return imageHeight;
                    }
                    
                    @Override
                    public int getWidth() {
                        return imageWidth;
                    }
                    
                    @Override
                    public ByteBuffer getImageBufferData() {
                        return subBuffer;
                    }
                    
                    @Override
                    public int getTexHeight() {
                        return ySize;
                    }
                    
                    @Override
                    public int getTexWidth() {
                        return xSize;
                    }
                };
                this.images[x][y] = new Image(imgData, filter);
            }
        }
        this.inited = true;
    }
    
    @Override
    public void bind() {
        throw new OperationNotSupportedException("Can't bind big images yet");
    }
    
    @Override
    public Image copy() {
        throw new OperationNotSupportedException("Can't copy big images yet");
    }
    
    @Override
    public void draw() {
        this.draw(0.0f, 0.0f);
    }
    
    @Override
    public void draw(final float x, final float y, final Color filter) {
        this.draw(x, y, (float)this.width, (float)this.height, filter);
    }
    
    @Override
    public void draw(final float x, final float y, final float scale, final Color filter) {
        this.draw(x, y, this.width * scale, this.height * scale, filter);
    }
    
    @Override
    public void draw(final float x, final float y, final float width, final float height, final Color filter) {
        final float sx = width / this.realWidth;
        final float sy = height / this.realHeight;
        BigImage.GL.glTranslatef(x, y, 0.0f);
        BigImage.GL.glScalef(sx, sy, 1.0f);
        float xp = 0.0f;
        float yp = 0.0f;
        for (int tx = 0; tx < this.xcount; ++tx) {
            yp = 0.0f;
            for (int ty = 0; ty < this.ycount; ++ty) {
                final Image image = this.images[tx][ty];
                image.draw(xp, yp, (float)image.getWidth(), (float)image.getHeight(), filter);
                yp += image.getHeight();
                if (ty == this.ycount - 1) {
                    xp += image.getWidth();
                }
            }
        }
        BigImage.GL.glScalef(1.0f / sx, 1.0f / sy, 1.0f);
        BigImage.GL.glTranslatef(-x, -y, 0.0f);
    }
    
    @Override
    public void draw(final float x, final float y, final float x2, final float y2, final float srcx, final float srcy, final float srcx2, final float srcy2) {
        final int srcwidth = (int)(srcx2 - srcx);
        final int srcheight = (int)(srcy2 - srcy);
        final Image subImage = this.getSubImage((int)srcx, (int)srcy, srcwidth, srcheight);
        final int width = (int)(x2 - x);
        final int height = (int)(y2 - y);
        subImage.draw(x, y, (float)width, (float)height);
    }
    
    @Override
    public void draw(final float x, final float y, final float srcx, final float srcy, final float srcx2, final float srcy2) {
        final int srcwidth = (int)(srcx2 - srcx);
        final int srcheight = (int)(srcy2 - srcy);
        this.draw(x, y, (float)srcwidth, (float)srcheight, srcx, srcy, srcx2, srcy2);
    }
    
    @Override
    public void draw(final float x, final float y, final float width, final float height) {
        this.draw(x, y, width, height, Color.white);
    }
    
    @Override
    public void draw(final float x, final float y, final float scale) {
        this.draw(x, y, scale, Color.white);
    }
    
    @Override
    public void draw(final float x, final float y) {
        this.draw(x, y, Color.white);
    }
    
    @Override
    public void drawEmbedded(final float x, final float y, final float width, final float height) {
        final float sx = width / this.realWidth;
        final float sy = height / this.realHeight;
        float xp = 0.0f;
        float yp = 0.0f;
        for (int tx = 0; tx < this.xcount; ++tx) {
            yp = 0.0f;
            for (int ty = 0; ty < this.ycount; ++ty) {
                final Image image = this.images[tx][ty];
                if (BigImage.lastBind == null || image.getTexture() != BigImage.lastBind.getTexture()) {
                    if (BigImage.lastBind != null) {
                        BigImage.lastBind.endUse();
                    }
                    (BigImage.lastBind = image).startUse();
                }
                image.drawEmbedded(xp + x, yp + y, (float)image.getWidth(), (float)image.getHeight());
                yp += image.getHeight();
                if (ty == this.ycount - 1) {
                    xp += image.getWidth();
                }
            }
        }
    }
    
    @Override
    public void drawFlash(final float x, final float y, final float width, final float height) {
        final float sx = width / this.realWidth;
        final float sy = height / this.realHeight;
        BigImage.GL.glTranslatef(x, y, 0.0f);
        BigImage.GL.glScalef(sx, sy, 1.0f);
        float xp = 0.0f;
        float yp = 0.0f;
        for (int tx = 0; tx < this.xcount; ++tx) {
            yp = 0.0f;
            for (int ty = 0; ty < this.ycount; ++ty) {
                final Image image = this.images[tx][ty];
                image.drawFlash(xp, yp, (float)image.getWidth(), (float)image.getHeight());
                yp += image.getHeight();
                if (ty == this.ycount - 1) {
                    xp += image.getWidth();
                }
            }
        }
        BigImage.GL.glScalef(1.0f / sx, 1.0f / sy, 1.0f);
        BigImage.GL.glTranslatef(-x, -y, 0.0f);
    }
    
    @Override
    public void drawFlash(final float x, final float y) {
        this.drawFlash(x, y, (float)this.width, (float)this.height);
    }
    
    @Override
    public void endUse() {
        if (BigImage.lastBind != null) {
            BigImage.lastBind.endUse();
        }
        BigImage.lastBind = null;
    }
    
    @Override
    public void startUse() {
    }
    
    @Override
    public void ensureInverted() {
        throw new OperationNotSupportedException("Doesn't make sense for tiled operations");
    }
    
    @Override
    public Color getColor(final int x, final int y) {
        throw new OperationNotSupportedException("Can't use big images as buffers");
    }
    
    @Override
    public Image getFlippedCopy(final boolean flipHorizontal, final boolean flipVertical) {
        final BigImage image = new BigImage();
        image.images = this.images;
        image.xcount = this.xcount;
        image.ycount = this.ycount;
        image.width = this.width;
        image.height = this.height;
        image.realWidth = this.realWidth;
        image.realHeight = this.realHeight;
        if (flipHorizontal) {
            final Image[][] images = image.images;
            image.images = new Image[this.xcount][this.ycount];
            for (int x = 0; x < this.xcount; ++x) {
                for (int y = 0; y < this.ycount; ++y) {
                    image.images[x][y] = images[this.xcount - 1 - x][y].getFlippedCopy(true, false);
                }
            }
        }
        if (flipVertical) {
            final Image[][] images = image.images;
            image.images = new Image[this.xcount][this.ycount];
            for (int x = 0; x < this.xcount; ++x) {
                for (int y = 0; y < this.ycount; ++y) {
                    image.images[x][y] = images[x][this.ycount - 1 - y].getFlippedCopy(false, true);
                }
            }
        }
        return image;
    }
    
    @Override
    public Graphics getGraphics() throws SlickException {
        throw new OperationNotSupportedException("Can't use big images as offscreen buffers");
    }
    
    @Override
    public Image getScaledCopy(final float scale) {
        return this.getScaledCopy((int)(scale * this.width), (int)(scale * this.height));
    }
    
    @Override
    public Image getScaledCopy(final int width, final int height) {
        final BigImage image = new BigImage();
        image.images = this.images;
        image.xcount = this.xcount;
        image.ycount = this.ycount;
        image.width = width;
        image.height = height;
        image.realWidth = this.realWidth;
        image.realHeight = this.realHeight;
        return image;
    }
    
    @Override
    public Image getSubImage(final int x, final int y, final int width, final int height) {
        final BigImage image = new BigImage();
        image.width = width;
        image.height = height;
        image.realWidth = width;
        image.realHeight = height;
        image.images = new Image[this.xcount][this.ycount];
        float xp = 0.0f;
        float yp = 0.0f;
        final int x2 = x + width;
        final int y2 = y + height;
        int startx = 0;
        int starty = 0;
        boolean foundStart = false;
        for (int xt = 0; xt < this.xcount; ++xt) {
            yp = 0.0f;
            starty = 0;
            foundStart = false;
            for (int yt = 0; yt < this.ycount; ++yt) {
                final Image current = this.images[xt][yt];
                final int xp2 = (int)(xp + current.getWidth());
                final int yp2 = (int)(yp + current.getHeight());
                final int targetX1 = (int)Math.max((float)x, xp);
                final int targetY1 = (int)Math.max((float)y, yp);
                final int targetX2 = Math.min(x2, xp2);
                final int targetY2 = Math.min(y2, yp2);
                final int targetWidth = targetX2 - targetX1;
                final int targetHeight = targetY2 - targetY1;
                if (targetWidth > 0 && targetHeight > 0) {
                    final Image subImage = current.getSubImage((int)(targetX1 - xp), (int)(targetY1 - yp), targetX2 - targetX1, targetY2 - targetY1);
                    foundStart = true;
                    image.images[startx][starty] = subImage;
                    ++starty;
                    image.ycount = Math.max(image.ycount, starty);
                }
                yp += current.getHeight();
                if (yt == this.ycount - 1) {
                    xp += current.getWidth();
                }
            }
            if (foundStart) {
                ++startx;
                final BigImage bigImage = image;
                ++bigImage.xcount;
            }
        }
        return image;
    }
    
    @Override
    public Texture getTexture() {
        throw new OperationNotSupportedException("Can't use big images as offscreen buffers");
    }
    
    @Override
    protected void initImpl() {
        throw new OperationNotSupportedException("Can't use big images as offscreen buffers");
    }
    
    @Override
    protected void reinit() {
        throw new OperationNotSupportedException("Can't use big images as offscreen buffers");
    }
    
    @Override
    public void setTexture(final Texture texture) {
        throw new OperationNotSupportedException("Can't use big images as offscreen buffers");
    }
    
    public Image getSubImage(final int offsetX, final int offsetY) {
        return this.images[offsetX][offsetY];
    }
    
    public int getHorizontalImageCount() {
        return this.xcount;
    }
    
    public int getVerticalImageCount() {
        return this.ycount;
    }
    
    @Override
    public String toString() {
        return "[BIG IMAGE]";
    }
    
    @Override
    public void destroy() throws SlickException {
        for (int tx = 0; tx < this.xcount; ++tx) {
            for (int ty = 0; ty < this.ycount; ++ty) {
                final Image image = this.images[tx][ty];
                image.destroy();
            }
        }
    }
    
    @Override
    public void draw(final float x, final float y, final float x2, final float y2, final float srcx, final float srcy, final float srcx2, final float srcy2, final Color filter) {
        final int srcwidth = (int)(srcx2 - srcx);
        final int srcheight = (int)(srcy2 - srcy);
        final Image subImage = this.getSubImage((int)srcx, (int)srcy, srcwidth, srcheight);
        final int width = (int)(x2 - x);
        final int height = (int)(y2 - y);
        subImage.draw(x, y, (float)width, (float)height, filter);
    }
    
    @Override
    public void drawCentered(final float x, final float y) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void drawEmbedded(final float x, final float y, final float x2, final float y2, final float srcx, final float srcy, final float srcx2, final float srcy2, final Color filter) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void drawEmbedded(final float x, final float y, final float x2, final float y2, final float srcx, final float srcy, final float srcx2, final float srcy2) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void drawFlash(final float x, final float y, final float width, final float height, final Color col) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void drawSheared(final float x, final float y, final float hshear, final float vshear) {
        throw new UnsupportedOperationException();
    }
}
