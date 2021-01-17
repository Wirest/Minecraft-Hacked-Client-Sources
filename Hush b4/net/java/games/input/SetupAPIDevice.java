// 
// Decompiled by Procyon v0.5.36
// 

package net.java.games.input;

final class SetupAPIDevice
{
    private final String device_instance_id;
    private final String device_name;
    
    public SetupAPIDevice(final String device_instance_id, final String device_name) {
        this.device_instance_id = device_instance_id;
        this.device_name = device_name;
    }
    
    public final String getName() {
        return this.device_name;
    }
    
    public final String getInstanceId() {
        return this.device_instance_id;
    }
}
