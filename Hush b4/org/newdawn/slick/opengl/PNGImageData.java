// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.opengl;

import org.lwjgl.BufferUtils;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class PNGImageData implements LoadableImageData
{
    private int width;
    private int height;
    private int texHeight;
    private int texWidth;
    private PNGDecoder decoder;
    private int bitDepth;
    private ByteBuffer scratch;
    
    @Override
    public int getDepth() {
        return this.bitDepth;
    }
    
    @Override
    public ByteBuffer getImageBufferData() {
        return this.scratch;
    }
    
    @Override
    public int getTexHeight() {
        return this.texHeight;
    }
    
    @Override
    public int getTexWidth() {
        return this.texWidth;
    }
    
    @Override
    public ByteBuffer loadImage(final InputStream fis) throws IOException {
        return this.loadImage(fis, false, null);
    }
    
    @Override
    public ByteBuffer loadImage(final InputStream fis, final boolean flipped, final int[] transparent) throws IOException {
        return this.loadImage(fis, flipped, false, transparent);
    }
    
    @Override
    public ByteBuffer loadImage(final InputStream fis, final boolean flipped, boolean forceAlpha, final int[] transparent) throws IOException {
        if (transparent != null) {
            forceAlpha = true;
            throw new IOException("Transparent color not support in custom PNG Decoder");
        }
        final PNGDecoder decoder = new PNGDecoder(fis);
        if (!decoder.isRGB()) {
            throw new IOException("Only RGB formatted images are supported by the PNGLoader");
        }
        this.width = decoder.getWidth();
        this.height = decoder.getHeight();
        this.texWidth = this.get2Fold(this.width);
        this.texHeight = this.get2Fold(this.height);
        final int perPixel = decoder.hasAlpha() ? 4 : 3;
        this.bitDepth = (decoder.hasAlpha() ? 32 : 24);
        decoder.decode(this.scratch = BufferUtils.createByteBuffer(this.texWidth * this.texHeight * perPixel), this.texWidth * perPixel, (perPixel == 4) ? PNGDecoder.RGBA : PNGDecoder.RGB);
        if (this.height < this.texHeight - 1) {
            final int topOffset = (this.texHeight - 1) * (this.texWidth * perPixel);
            final int bottomOffset = (this.height - 1) * (this.texWidth * perPixel);
            for (int x = 0; x < this.texWidth; ++x) {
                for (int i = 0; i < perPixel; ++i) {
                    this.scratch.put(topOffset + x + i, this.scratch.get(x + i));
                    this.scratch.put(bottomOffset + this.texWidth * perPixel + x + i, this.scratch.get(bottomOffset + x + i));
                }
            }
        }
        if (this.width < this.texWidth - 1) {
            for (int y = 0; y < this.texHeight; ++y) {
                for (int j = 0; j < perPixel; ++j) {
                    this.scratch.put((y + 1) * (this.texWidth * perPixel) - perPixel + j, this.scratch.get(y * (this.texWidth * perPixel) + j));
                    this.scratch.put(y * (this.texWidth * perPixel) + this.width * perPixel + j, this.scratch.get(y * (this.texWidth * perPixel) + (this.width - 1) * perPixel + j));
                }
            }
        }
        if (!decoder.hasAlpha() && forceAlpha) {
            final ByteBuffer temp = BufferUtils.createByteBuffer(this.texWidth * this.texHeight * 4);
            for (int x2 = 0; x2 < this.texWidth; ++x2) {
                for (int y2 = 0; y2 < this.texHeight; ++y2) {
                    final int srcOffset = y2 * 3 + x2 * this.texHeight * 3;
                    final int dstOffset = y2 * 4 + x2 * this.texHeight * 4;
                    temp.put(dstOffset, this.scratch.get(srcOffset));
                    temp.put(dstOffset + 1, this.scratch.get(srcOffset + 1));
                    temp.put(dstOffset + 2, this.scratch.get(srcOffset + 2));
                    if (x2 < this.getHeight() && y2 < this.getWidth()) {
                        temp.put(dstOffset + 3, (byte)(-1));
                    }
                    else {
                        temp.put(dstOffset + 3, (byte)0);
                    }
                }
            }
            this.bitDepth = 32;
            this.scratch = temp;
        }
        if (transparent != null) {
            for (int k = 0; k < this.texWidth * this.texHeight * 4; k += 4) {
                boolean match = true;
                for (int c = 0; c < 3; ++c) {
                    if (this.toInt(this.scratch.get(k + c)) != transparent[c]) {
                        match = false;
                    }
                }
                if (match) {
                    this.scratch.put(k + 3, (byte)0);
                }
            }
        }
        this.scratch.position(0);
        return this.scratch;
    }
    
    private int toInt(final byte b) {
        if (b < 0) {
            return 256 + b;
        }
        return b;
    }
    
    private int get2Fold(final int fold) {
        int ret;
        for (ret = 2; ret < fold; ret *= 2) {}
        return ret;
    }
    
    @Override
    public void configureEdging(final boolean edging) {
    }
    
    @Override
    public int getWidth() {
        return this.width;
    }
    
    @Override
    public int getHeight() {
        return this.height;
    }
}
