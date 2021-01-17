// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opencl.api;

public final class CLImageFormat
{
    public static final int STRUCT_SIZE = 8;
    private final int channelOrder;
    private final int channelType;
    
    public CLImageFormat(final int channelOrder, final int channelType) {
        this.channelOrder = channelOrder;
        this.channelType = channelType;
    }
    
    public int getChannelOrder() {
        return this.channelOrder;
    }
    
    public int getChannelType() {
        return this.channelType;
    }
}
