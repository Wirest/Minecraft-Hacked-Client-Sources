// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.opengl;

import org.newdawn.slick.util.Log;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.io.InputStream;
import java.util.ArrayList;

public class CompositeImageData implements LoadableImageData
{
    private ArrayList sources;
    private LoadableImageData picked;
    
    public CompositeImageData() {
        this.sources = new ArrayList();
    }
    
    public void add(final LoadableImageData data) {
        this.sources.add(data);
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
    public ByteBuffer loadImage(final InputStream is, final boolean flipped, final boolean forceAlpha, final int[] transparent) throws IOException {
        final CompositeIOException exception = new CompositeIOException();
        ByteBuffer buffer = null;
        final BufferedInputStream in = new BufferedInputStream(is, is.available());
        in.mark(is.available());
        int i = 0;
        while (i < this.sources.size()) {
            in.reset();
            try {
                final LoadableImageData data = this.sources.get(i);
                buffer = data.loadImage(in, flipped, forceAlpha, transparent);
                this.picked = data;
                break;
            }
            catch (Exception e) {
                Log.warn(this.sources.get(i).getClass() + " failed to read the data", e);
                exception.addException(e);
                ++i;
            }
        }
        if (this.picked == null) {
            throw exception;
        }
        return buffer;
    }
    
    private void checkPicked() {
        if (this.picked == null) {
            throw new RuntimeException("Attempt to make use of uninitialised or invalid composite image data");
        }
    }
    
    @Override
    public int getDepth() {
        this.checkPicked();
        return this.picked.getDepth();
    }
    
    @Override
    public int getHeight() {
        this.checkPicked();
        return this.picked.getHeight();
    }
    
    @Override
    public ByteBuffer getImageBufferData() {
        this.checkPicked();
        return this.picked.getImageBufferData();
    }
    
    @Override
    public int getTexHeight() {
        this.checkPicked();
        return this.picked.getTexHeight();
    }
    
    @Override
    public int getTexWidth() {
        this.checkPicked();
        return this.picked.getTexWidth();
    }
    
    @Override
    public int getWidth() {
        this.checkPicked();
        return this.picked.getWidth();
    }
    
    @Override
    public void configureEdging(final boolean edging) {
        for (int i = 0; i < this.sources.size(); ++i) {
            this.sources.get(i).configureEdging(edging);
        }
    }
}
