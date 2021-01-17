// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opencl;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import org.lwjgl.PointerBuffer;
import org.lwjgl.opencl.api.Filter;
import java.util.List;

public final class CLPlatform extends CLObject
{
    private static final CLPlatformUtil util;
    private static final FastLongMap<CLPlatform> clPlatforms;
    private final CLObjectRegistry<CLDevice> clDevices;
    private Object caps;
    
    CLPlatform(final long pointer) {
        super(pointer);
        if (this.isValid()) {
            CLPlatform.clPlatforms.put(pointer, this);
            this.clDevices = new CLObjectRegistry<CLDevice>();
        }
        else {
            this.clDevices = null;
        }
    }
    
    public static CLPlatform getCLPlatform(final long id) {
        return CLPlatform.clPlatforms.get(id);
    }
    
    public CLDevice getCLDevice(final long id) {
        return this.clDevices.getObject(id);
    }
    
    static <T extends CLObject> InfoUtil<T> getInfoUtilInstance(final Class<T> clazz, final String fieldName) {
        InfoUtil<T> instance = null;
        try {
            final Class<?> infoUtil = Class.forName("org.lwjgl.opencl.InfoUtilFactory");
            instance = (InfoUtil<T>)infoUtil.getDeclaredField(fieldName).get(null);
        }
        catch (Exception ex) {}
        return instance;
    }
    
    public static List<CLPlatform> getPlatforms() {
        return getPlatforms(null);
    }
    
    public static List<CLPlatform> getPlatforms(final Filter<CLPlatform> filter) {
        return CLPlatform.util.getPlatforms(filter);
    }
    
    public String getInfoString(final int param_name) {
        return CLPlatform.util.getInfoString(this, param_name);
    }
    
    public List<CLDevice> getDevices(final int device_type) {
        return this.getDevices(device_type, null);
    }
    
    public List<CLDevice> getDevices(final int device_type, final Filter<CLDevice> filter) {
        return CLPlatform.util.getDevices(this, device_type, filter);
    }
    
    void setCapabilities(final Object caps) {
        this.caps = caps;
    }
    
    Object getCapabilities() {
        return this.caps;
    }
    
    static void registerCLPlatforms(final PointerBuffer platforms, final IntBuffer num_platforms) {
        if (platforms == null) {
            return;
        }
        final int pos = platforms.position();
        for (int count = Math.min(num_platforms.get(0), platforms.remaining()), i = 0; i < count; ++i) {
            final long id = platforms.get(pos + i);
            if (!CLPlatform.clPlatforms.containsKey(id)) {
                new CLPlatform(id);
            }
        }
    }
    
    CLObjectRegistry<CLDevice> getCLDeviceRegistry() {
        return this.clDevices;
    }
    
    void registerCLDevices(final PointerBuffer devices, final IntBuffer num_devices) {
        final int pos = devices.position();
        for (int count = Math.min(num_devices.get(num_devices.position()), devices.remaining()), i = 0; i < count; ++i) {
            final long id = devices.get(pos + i);
            if (!this.clDevices.hasObject(id)) {
                new CLDevice(id, this);
            }
        }
    }
    
    void registerCLDevices(final ByteBuffer devices, final PointerBuffer num_devices) {
        final int pos = devices.position();
        for (int count = Math.min((int)num_devices.get(num_devices.position()), devices.remaining()) / PointerBuffer.getPointerSize(), i = 0; i < count; ++i) {
            final int offset = pos + i * PointerBuffer.getPointerSize();
            final long id = PointerBuffer.is64Bit() ? devices.getLong(offset) : devices.getInt(offset);
            if (!this.clDevices.hasObject(id)) {
                new CLDevice(id, this);
            }
        }
    }
    
    static {
        util = (CLPlatformUtil)getInfoUtilInstance(CLPlatform.class, "CL_PLATFORM_UTIL");
        clPlatforms = new FastLongMap<CLPlatform>();
    }
    
    interface CLPlatformUtil extends InfoUtil<CLPlatform>
    {
        List<CLPlatform> getPlatforms(final Filter<CLPlatform> p0);
        
        List<CLDevice> getDevices(final CLPlatform p0, final int p1, final Filter<CLDevice> p2);
    }
}
