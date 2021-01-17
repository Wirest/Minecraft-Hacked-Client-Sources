// 
// Decompiled by Procyon v0.5.36
// 

package net.java.games.input;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

final class OSXHIDQueue
{
    private final Map map;
    private final long queue_address;
    private boolean released;
    
    public OSXHIDQueue(final long address, final int queue_depth) throws IOException {
        this.map = new HashMap();
        this.queue_address = address;
        try {
            this.createQueue(queue_depth);
        }
        catch (IOException e) {
            this.release();
            throw e;
        }
    }
    
    public final synchronized void setQueueDepth(final int queue_depth) throws IOException {
        this.checkReleased();
        this.stop();
        this.close();
        this.createQueue(queue_depth);
    }
    
    private final void createQueue(final int queue_depth) throws IOException {
        this.open(queue_depth);
        try {
            this.start();
        }
        catch (IOException e) {
            this.close();
            throw e;
        }
    }
    
    public final OSXComponent mapEvent(final OSXEvent event) {
        return this.map.get(new Long(event.getCookie()));
    }
    
    private final void open(final int queue_depth) throws IOException {
        nOpen(this.queue_address, queue_depth);
    }
    
    private static final native void nOpen(final long p0, final int p1) throws IOException;
    
    private final void close() throws IOException {
        nClose(this.queue_address);
    }
    
    private static final native void nClose(final long p0) throws IOException;
    
    private final void start() throws IOException {
        nStart(this.queue_address);
    }
    
    private static final native void nStart(final long p0) throws IOException;
    
    private final void stop() throws IOException {
        nStop(this.queue_address);
    }
    
    private static final native void nStop(final long p0) throws IOException;
    
    public final synchronized void release() throws IOException {
        if (!this.released) {
            this.released = true;
            try {
                this.stop();
                this.close();
            }
            finally {
                nReleaseQueue(this.queue_address);
            }
        }
    }
    
    private static final native void nReleaseQueue(final long p0) throws IOException;
    
    public final void addElement(final OSXHIDElement element, final OSXComponent component) throws IOException {
        nAddElement(this.queue_address, element.getCookie());
        this.map.put(new Long(element.getCookie()), component);
    }
    
    private static final native void nAddElement(final long p0, final long p1) throws IOException;
    
    public final void removeElement(final OSXHIDElement element) throws IOException {
        nRemoveElement(this.queue_address, element.getCookie());
        this.map.remove(new Long(element.getCookie()));
    }
    
    private static final native void nRemoveElement(final long p0, final long p1) throws IOException;
    
    public final synchronized boolean getNextEvent(final OSXEvent event) throws IOException {
        this.checkReleased();
        return nGetNextEvent(this.queue_address, event);
    }
    
    private static final native boolean nGetNextEvent(final long p0, final OSXEvent p1) throws IOException;
    
    private final void checkReleased() throws IOException {
        if (this.released) {
            throw new IOException("Queue is released");
        }
    }
}
