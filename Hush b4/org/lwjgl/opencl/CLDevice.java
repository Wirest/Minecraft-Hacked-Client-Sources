// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opencl;

import org.lwjgl.PointerBuffer;

public final class CLDevice extends CLObjectChild<CLDevice>
{
    private static final InfoUtil<CLDevice> util;
    private final CLPlatform platform;
    private final CLObjectRegistry<CLDevice> subCLDevices;
    private Object caps;
    
    CLDevice(final long pointer, final CLPlatform platform) {
        this(pointer, null, platform);
    }
    
    CLDevice(final long pointer, final CLDevice parent) {
        this(pointer, parent, parent.getPlatform());
    }
    
    CLDevice(final long pointer, final CLDevice parent, final CLPlatform platform) {
        super(pointer, parent);
        if (this.isValid()) {
            this.platform = platform;
            platform.getCLDeviceRegistry().registerObject(this);
            this.subCLDevices = new CLObjectRegistry<CLDevice>();
            if (parent != null) {
                parent.subCLDevices.registerObject(this);
            }
        }
        else {
            this.platform = null;
            this.subCLDevices = null;
        }
    }
    
    public CLPlatform getPlatform() {
        return this.platform;
    }
    
    public CLDevice getSubCLDevice(final long id) {
        return this.subCLDevices.getObject(id);
    }
    
    public String getInfoString(final int param_name) {
        return CLDevice.util.getInfoString(this, param_name);
    }
    
    public int getInfoInt(final int param_name) {
        return CLDevice.util.getInfoInt(this, param_name);
    }
    
    public boolean getInfoBoolean(final int param_name) {
        return CLDevice.util.getInfoInt(this, param_name) != 0;
    }
    
    public long getInfoSize(final int param_name) {
        return CLDevice.util.getInfoSize(this, param_name);
    }
    
    public long[] getInfoSizeArray(final int param_name) {
        return CLDevice.util.getInfoSizeArray(this, param_name);
    }
    
    public long getInfoLong(final int param_name) {
        return CLDevice.util.getInfoLong(this, param_name);
    }
    
    void setCapabilities(final Object caps) {
        this.caps = caps;
    }
    
    Object getCapabilities() {
        return this.caps;
    }
    
    @Override
    int retain() {
        if (this.getParent() == null) {
            return this.getReferenceCount();
        }
        return super.retain();
    }
    
    @Override
    int release() {
        if (this.getParent() == null) {
            return this.getReferenceCount();
        }
        try {
            return super.release();
        }
        finally {
            if (!this.isValid()) {
                this.getParent().subCLDevices.unregisterObject(this);
            }
        }
    }
    
    CLObjectRegistry<CLDevice> getSubCLDeviceRegistry() {
        return this.subCLDevices;
    }
    
    void registerSubCLDevices(final PointerBuffer devices) {
        for (int i = devices.position(); i < devices.limit(); ++i) {
            final long pointer = devices.get(i);
            if (pointer != 0L) {
                new CLDevice(pointer, this);
            }
        }
    }
    
    static {
        util = CLPlatform.getInfoUtilInstance(CLDevice.class, "CL_DEVICE_UTIL");
    }
}
