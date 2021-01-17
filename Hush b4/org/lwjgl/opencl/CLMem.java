// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opencl;

import java.nio.ByteBuffer;
import org.lwjgl.opencl.api.CLBufferRegion;
import java.nio.IntBuffer;
import java.nio.Buffer;
import org.lwjgl.opencl.api.CLImageFormat;

public final class CLMem extends CLObjectChild<CLContext>
{
    private static final CLMemUtil util;
    
    CLMem(final long pointer, final CLContext context) {
        super(pointer, context);
        if (this.isValid()) {
            context.getCLMemRegistry().registerObject(this);
        }
    }
    
    public static CLMem createImage2D(final CLContext context, final long flags, final CLImageFormat image_format, final long image_width, final long image_height, final long image_row_pitch, final Buffer host_ptr, final IntBuffer errcode_ret) {
        return CLMem.util.createImage2D(context, flags, image_format, image_width, image_height, image_row_pitch, host_ptr, errcode_ret);
    }
    
    public static CLMem createImage3D(final CLContext context, final long flags, final CLImageFormat image_format, final long image_width, final long image_height, final long image_depth, final long image_row_pitch, final long image_slice_pitch, final Buffer host_ptr, final IntBuffer errcode_ret) {
        return CLMem.util.createImage3D(context, flags, image_format, image_width, image_height, image_depth, image_row_pitch, image_slice_pitch, host_ptr, errcode_ret);
    }
    
    public CLMem createSubBuffer(final long flags, final int buffer_create_type, final CLBufferRegion buffer_create_info, final IntBuffer errcode_ret) {
        return CLMem.util.createSubBuffer(this, flags, buffer_create_type, buffer_create_info, errcode_ret);
    }
    
    public int getInfoInt(final int param_name) {
        return CLMem.util.getInfoInt(this, param_name);
    }
    
    public long getInfoSize(final int param_name) {
        return CLMem.util.getInfoSize(this, param_name);
    }
    
    public long getInfoLong(final int param_name) {
        return CLMem.util.getInfoLong(this, param_name);
    }
    
    public ByteBuffer getInfoHostBuffer() {
        return CLMem.util.getInfoHostBuffer(this);
    }
    
    public long getImageInfoSize(final int param_name) {
        return CLMem.util.getImageInfoSize(this, param_name);
    }
    
    public CLImageFormat getImageFormat() {
        return CLMem.util.getImageInfoFormat(this);
    }
    
    public int getImageChannelOrder() {
        return CLMem.util.getImageInfoFormat(this, 0);
    }
    
    public int getImageChannelType() {
        return CLMem.util.getImageInfoFormat(this, 1);
    }
    
    public int getGLObjectType() {
        return CLMem.util.getGLObjectType(this);
    }
    
    public int getGLObjectName() {
        return CLMem.util.getGLObjectName(this);
    }
    
    public int getGLTextureInfoInt(final int param_name) {
        return CLMem.util.getGLTextureInfoInt(this, param_name);
    }
    
    static CLMem create(final long pointer, final CLContext context) {
        CLMem clMem = context.getCLMemRegistry().getObject(pointer);
        if (clMem == null) {
            clMem = new CLMem(pointer, context);
        }
        else {
            clMem.retain();
        }
        return clMem;
    }
    
    @Override
    int release() {
        try {
            return super.release();
        }
        finally {
            if (!this.isValid()) {
                this.getParent().getCLMemRegistry().unregisterObject(this);
            }
        }
    }
    
    static {
        util = (CLMemUtil)CLPlatform.getInfoUtilInstance(CLMem.class, "CL_MEM_UTIL");
    }
    
    interface CLMemUtil extends InfoUtil<CLMem>
    {
        CLMem createImage2D(final CLContext p0, final long p1, final CLImageFormat p2, final long p3, final long p4, final long p5, final Buffer p6, final IntBuffer p7);
        
        CLMem createImage3D(final CLContext p0, final long p1, final CLImageFormat p2, final long p3, final long p4, final long p5, final long p6, final long p7, final Buffer p8, final IntBuffer p9);
        
        CLMem createSubBuffer(final CLMem p0, final long p1, final int p2, final CLBufferRegion p3, final IntBuffer p4);
        
        ByteBuffer getInfoHostBuffer(final CLMem p0);
        
        long getImageInfoSize(final CLMem p0, final int p1);
        
        CLImageFormat getImageInfoFormat(final CLMem p0);
        
        int getImageInfoFormat(final CLMem p0, final int p1);
        
        int getGLObjectType(final CLMem p0);
        
        int getGLObjectName(final CLMem p0);
        
        int getGLTextureInfoInt(final CLMem p0, final int p1);
    }
}
