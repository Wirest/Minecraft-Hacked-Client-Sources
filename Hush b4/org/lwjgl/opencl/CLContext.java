// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opencl;

import org.lwjgl.opencl.api.Filter;
import org.lwjgl.opencl.api.CLImageFormat;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Drawable;
import java.nio.IntBuffer;
import java.util.List;

public final class CLContext extends CLObjectChild<CLPlatform>
{
    private static final CLContextUtil util;
    private final CLObjectRegistry<CLCommandQueue> clCommandQueues;
    private final CLObjectRegistry<CLMem> clMems;
    private final CLObjectRegistry<CLSampler> clSamplers;
    private final CLObjectRegistry<CLProgram> clPrograms;
    private final CLObjectRegistry<CLEvent> clEvents;
    private long contextCallback;
    private long printfCallback;
    
    CLContext(final long pointer, final CLPlatform platform) {
        super(pointer, platform);
        if (this.isValid()) {
            this.clCommandQueues = new CLObjectRegistry<CLCommandQueue>();
            this.clMems = new CLObjectRegistry<CLMem>();
            this.clSamplers = new CLObjectRegistry<CLSampler>();
            this.clPrograms = new CLObjectRegistry<CLProgram>();
            this.clEvents = new CLObjectRegistry<CLEvent>();
        }
        else {
            this.clCommandQueues = null;
            this.clMems = null;
            this.clSamplers = null;
            this.clPrograms = null;
            this.clEvents = null;
        }
    }
    
    public CLCommandQueue getCLCommandQueue(final long id) {
        return this.clCommandQueues.getObject(id);
    }
    
    public CLMem getCLMem(final long id) {
        return this.clMems.getObject(id);
    }
    
    public CLSampler getCLSampler(final long id) {
        return this.clSamplers.getObject(id);
    }
    
    public CLProgram getCLProgram(final long id) {
        return this.clPrograms.getObject(id);
    }
    
    public CLEvent getCLEvent(final long id) {
        return this.clEvents.getObject(id);
    }
    
    public static CLContext create(final CLPlatform platform, final List<CLDevice> devices, final IntBuffer errcode_ret) throws LWJGLException {
        return create(platform, devices, null, null, errcode_ret);
    }
    
    public static CLContext create(final CLPlatform platform, final List<CLDevice> devices, final CLContextCallback pfn_notify, final IntBuffer errcode_ret) throws LWJGLException {
        return create(platform, devices, pfn_notify, null, errcode_ret);
    }
    
    public static CLContext create(final CLPlatform platform, final List<CLDevice> devices, final CLContextCallback pfn_notify, final Drawable share_drawable, final IntBuffer errcode_ret) throws LWJGLException {
        return CLContext.util.create(platform, devices, pfn_notify, share_drawable, errcode_ret);
    }
    
    public static CLContext createFromType(final CLPlatform platform, final long device_type, final IntBuffer errcode_ret) throws LWJGLException {
        return CLContext.util.createFromType(platform, device_type, null, null, errcode_ret);
    }
    
    public static CLContext createFromType(final CLPlatform platform, final long device_type, final CLContextCallback pfn_notify, final IntBuffer errcode_ret) throws LWJGLException {
        return CLContext.util.createFromType(platform, device_type, pfn_notify, null, errcode_ret);
    }
    
    public static CLContext createFromType(final CLPlatform platform, final long device_type, final CLContextCallback pfn_notify, final Drawable share_drawable, final IntBuffer errcode_ret) throws LWJGLException {
        return CLContext.util.createFromType(platform, device_type, pfn_notify, share_drawable, errcode_ret);
    }
    
    public int getInfoInt(final int param_name) {
        return CLContext.util.getInfoInt(this, param_name);
    }
    
    public List<CLDevice> getInfoDevices() {
        return CLContext.util.getInfoDevices(this);
    }
    
    public List<CLImageFormat> getSupportedImageFormats(final long flags, final int image_type) {
        return this.getSupportedImageFormats(flags, image_type, null);
    }
    
    public List<CLImageFormat> getSupportedImageFormats(final long flags, final int image_type, final Filter<CLImageFormat> filter) {
        return CLContext.util.getSupportedImageFormats(this, flags, image_type, filter);
    }
    
    CLObjectRegistry<CLCommandQueue> getCLCommandQueueRegistry() {
        return this.clCommandQueues;
    }
    
    CLObjectRegistry<CLMem> getCLMemRegistry() {
        return this.clMems;
    }
    
    CLObjectRegistry<CLSampler> getCLSamplerRegistry() {
        return this.clSamplers;
    }
    
    CLObjectRegistry<CLProgram> getCLProgramRegistry() {
        return this.clPrograms;
    }
    
    CLObjectRegistry<CLEvent> getCLEventRegistry() {
        return this.clEvents;
    }
    
    private boolean checkCallback(final long callback, final int result) {
        if (result == 0 && (callback == 0L || this.isValid())) {
            return true;
        }
        if (callback != 0L) {
            CallbackUtil.deleteGlobalRef(callback);
        }
        return false;
    }
    
    void setContextCallback(final long callback) {
        if (this.checkCallback(callback, 0)) {
            this.contextCallback = callback;
        }
    }
    
    void setPrintfCallback(final long callback, final int result) {
        if (this.checkCallback(callback, result)) {
            this.printfCallback = callback;
        }
    }
    
    void releaseImpl() {
        if (this.release() > 0) {
            return;
        }
        if (this.contextCallback != 0L) {
            CallbackUtil.deleteGlobalRef(this.contextCallback);
        }
        if (this.printfCallback != 0L) {
            CallbackUtil.deleteGlobalRef(this.printfCallback);
        }
    }
    
    static {
        util = (CLContextUtil)CLPlatform.getInfoUtilInstance(CLContext.class, "CL_CONTEXT_UTIL");
    }
    
    interface CLContextUtil extends InfoUtil<CLContext>
    {
        List<CLDevice> getInfoDevices(final CLContext p0);
        
        CLContext create(final CLPlatform p0, final List<CLDevice> p1, final CLContextCallback p2, final Drawable p3, final IntBuffer p4) throws LWJGLException;
        
        CLContext createFromType(final CLPlatform p0, final long p1, final CLContextCallback p2, final Drawable p3, final IntBuffer p4) throws LWJGLException;
        
        List<CLImageFormat> getSupportedImageFormats(final CLContext p0, final long p1, final int p2, final Filter<CLImageFormat> p3);
    }
}
