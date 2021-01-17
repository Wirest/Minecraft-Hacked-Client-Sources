// 
// Decompiled by Procyon v0.5.36
// 

package tv.twitch.broadcast;

import java.util.HashMap;
import java.util.Map;

public class FrameBuffer
{
    private static Map<Long, FrameBuffer> s_OutstandingBuffers;
    protected long m_NativeAddress;
    protected int m_Size;
    protected StreamAPI m_API;
    
    public static FrameBuffer lookupBuffer(final long l) {
        return FrameBuffer.s_OutstandingBuffers.get(l);
    }
    
    protected static void registerBuffer(final FrameBuffer frameBuffer) {
        if (frameBuffer.getAddress() != 0L) {
            FrameBuffer.s_OutstandingBuffers.put(frameBuffer.getAddress(), frameBuffer);
        }
    }
    
    protected static void unregisterBuffer(final FrameBuffer frameBuffer) {
        FrameBuffer.s_OutstandingBuffers.remove(frameBuffer.getAddress());
    }
    
    FrameBuffer(final StreamAPI api, final int size) {
        this.m_NativeAddress = 0L;
        this.m_Size = 0;
        this.m_API = null;
        this.m_NativeAddress = api.allocateFrameBuffer(size);
        if (this.m_NativeAddress == 0L) {
            return;
        }
        this.m_API = api;
        this.m_Size = size;
        registerBuffer(this);
    }
    
    public boolean getIsValid() {
        return this.m_NativeAddress != 0L;
    }
    
    public int getSize() {
        return this.m_Size;
    }
    
    public long getAddress() {
        return this.m_NativeAddress;
    }
    
    public void free() {
        if (this.m_NativeAddress != 0L) {
            unregisterBuffer(this);
            this.m_API.freeFrameBuffer(this.m_NativeAddress);
            this.m_NativeAddress = 0L;
        }
    }
    
    @Override
    protected void finalize() {
        this.free();
    }
    
    static {
        FrameBuffer.s_OutstandingBuffers = new HashMap<Long, FrameBuffer>();
    }
}
