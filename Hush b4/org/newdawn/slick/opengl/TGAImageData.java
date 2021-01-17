// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.opengl;

import org.lwjgl.BufferUtils;
import java.nio.ByteOrder;
import java.io.DataInputStream;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.io.InputStream;

public class TGAImageData implements LoadableImageData
{
    private int texWidth;
    private int texHeight;
    private int width;
    private int height;
    private short pixelDepth;
    
    private short flipEndian(final short signedShort) {
        final int input = signedShort & 0xFFFF;
        return (short)(input << 8 | (input & 0xFF00) >>> 8);
    }
    
    @Override
    public int getDepth() {
        return this.pixelDepth;
    }
    
    @Override
    public int getWidth() {
        return this.width;
    }
    
    @Override
    public int getHeight() {
        return this.height;
    }
    
    @Override
    public int getTexWidth() {
        return this.texWidth;
    }
    
    @Override
    public int getTexHeight() {
        return this.texHeight;
    }
    
    @Override
    public ByteBuffer loadImage(final InputStream fis) throws IOException {
        return this.loadImage(fis, true, null);
    }
    
    @Override
    public ByteBuffer loadImage(final InputStream fis, final boolean flipped, final int[] transparent) throws IOException {
        return this.loadImage(fis, flipped, false, transparent);
    }
    
    @Override
    public ByteBuffer loadImage(final InputStream fis, boolean flipped, boolean forceAlpha, final int[] transparent) throws IOException {
        if (transparent != null) {
            forceAlpha = true;
        }
        byte red = 0;
        byte green = 0;
        byte blue = 0;
        byte alpha = 0;
        final BufferedInputStream bis = new BufferedInputStream(fis, 100000);
        final DataInputStream dis = new DataInputStream(bis);
        final short idLength = (short)dis.read();
        final short colorMapType = (short)dis.read();
        final short imageType = (short)dis.read();
        final short cMapStart = this.flipEndian(dis.readShort());
        final short cMapLength = this.flipEndian(dis.readShort());
        final short cMapDepth = (short)dis.read();
        final short xOffset = this.flipEndian(dis.readShort());
        final short yOffset = this.flipEndian(dis.readShort());
        if (imageType != 2) {
            throw new IOException("Slick only supports uncompressed RGB(A) TGA images");
        }
        this.width = this.flipEndian(dis.readShort());
        this.height = this.flipEndian(dis.readShort());
        this.pixelDepth = (short)dis.read();
        if (this.pixelDepth == 32) {
            forceAlpha = false;
        }
        this.texWidth = this.get2Fold(this.width);
        this.texHeight = this.get2Fold(this.height);
        final short imageDescriptor = (short)dis.read();
        if ((imageDescriptor & 0x20) == 0x0) {
            flipped = !flipped;
        }
        if (idLength > 0) {
            bis.skip(idLength);
        }
        byte[] rawData = null;
        if (this.pixelDepth == 32 || forceAlpha) {
            this.pixelDepth = 32;
            rawData = new byte[this.texWidth * this.texHeight * 4];
        }
        else {
            if (this.pixelDepth != 24) {
                throw new RuntimeException("Only 24 and 32 bit TGAs are supported");
            }
            rawData = new byte[this.texWidth * this.texHeight * 3];
        }
        if (this.pixelDepth == 24) {
            if (flipped) {
                for (int i = this.height - 1; i >= 0; --i) {
                    for (int j = 0; j < this.width; ++j) {
                        blue = dis.readByte();
                        green = dis.readByte();
                        red = dis.readByte();
                        final int ofs = (j + i * this.texWidth) * 3;
                        rawData[ofs] = red;
                        rawData[ofs + 1] = green;
                        rawData[ofs + 2] = blue;
                    }
                }
            }
            else {
                for (int i = 0; i < this.height; ++i) {
                    for (int j = 0; j < this.width; ++j) {
                        blue = dis.readByte();
                        green = dis.readByte();
                        red = dis.readByte();
                        final int ofs = (j + i * this.texWidth) * 3;
                        rawData[ofs] = red;
                        rawData[ofs + 1] = green;
                        rawData[ofs + 2] = blue;
                    }
                }
            }
        }
        else if (this.pixelDepth == 32) {
            if (flipped) {
                for (int i = this.height - 1; i >= 0; --i) {
                    for (int j = 0; j < this.width; ++j) {
                        blue = dis.readByte();
                        green = dis.readByte();
                        red = dis.readByte();
                        if (forceAlpha) {
                            alpha = -1;
                        }
                        else {
                            alpha = dis.readByte();
                        }
                        final int ofs = (j + i * this.texWidth) * 4;
                        rawData[ofs] = red;
                        rawData[ofs + 1] = green;
                        rawData[ofs + 2] = blue;
                        rawData[ofs + 3] = alpha;
                        if (alpha == 0) {
                            rawData[ofs + 2] = 0;
                            rawData[ofs] = (rawData[ofs + 1] = 0);
                        }
                    }
                }
            }
            else {
                for (int i = 0; i < this.height; ++i) {
                    for (int j = 0; j < this.width; ++j) {
                        blue = dis.readByte();
                        green = dis.readByte();
                        red = dis.readByte();
                        if (forceAlpha) {
                            alpha = -1;
                        }
                        else {
                            alpha = dis.readByte();
                        }
                        final int ofs = (j + i * this.texWidth) * 4;
                        if (ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN) {
                            rawData[ofs] = red;
                            rawData[ofs + 1] = green;
                            rawData[ofs + 2] = blue;
                            rawData[ofs + 3] = alpha;
                        }
                        else {
                            rawData[ofs] = red;
                            rawData[ofs + 1] = green;
                            rawData[ofs + 2] = blue;
                            rawData[ofs + 3] = alpha;
                        }
                        if (alpha == 0) {
                            rawData[ofs + 2] = 0;
                            rawData[ofs] = (rawData[ofs + 1] = 0);
                        }
                    }
                }
            }
        }
        fis.close();
        if (transparent != null) {
            for (int i = 0; i < rawData.length; i += 4) {
                boolean match = true;
                for (int c = 0; c < 3; ++c) {
                    if (rawData[i + c] != transparent[c]) {
                        match = false;
                    }
                }
                if (match) {
                    rawData[i + 3] = 0;
                }
            }
        }
        final ByteBuffer scratch = BufferUtils.createByteBuffer(rawData.length);
        scratch.put(rawData);
        final int perPixel = this.pixelDepth / 8;
        if (this.height < this.texHeight - 1) {
            final int topOffset = (this.texHeight - 1) * (this.texWidth * perPixel);
            final int bottomOffset = (this.height - 1) * (this.texWidth * perPixel);
            for (int x = 0; x < this.texWidth * perPixel; ++x) {
                scratch.put(topOffset + x, scratch.get(x));
                scratch.put(bottomOffset + this.texWidth * perPixel + x, scratch.get(this.texWidth * perPixel + x));
            }
        }
        if (this.width < this.texWidth - 1) {
            for (int y = 0; y < this.texHeight; ++y) {
                for (int k = 0; k < perPixel; ++k) {
                    scratch.put((y + 1) * (this.texWidth * perPixel) - perPixel + k, scratch.get(y * (this.texWidth * perPixel) + k));
                    scratch.put(y * (this.texWidth * perPixel) + this.width * perPixel + k, scratch.get(y * (this.texWidth * perPixel) + (this.width - 1) * perPixel + k));
                }
            }
        }
        scratch.flip();
        return scratch;
    }
    
    private int get2Fold(final int fold) {
        int ret;
        for (ret = 2; ret < fold; ret *= 2) {}
        return ret;
    }
    
    @Override
    public ByteBuffer getImageBufferData() {
        throw new RuntimeException("TGAImageData doesn't store it's image.");
    }
    
    @Override
    public void configureEdging(final boolean edging) {
    }
}
