// 
// Decompiled by Procyon v0.5.36
// 

package net.java.games.input;

import java.io.IOException;

abstract class LinuxForceFeedbackEffect implements Rumbler
{
    private final LinuxEventDevice device;
    private final int ff_id;
    private final WriteTask write_task;
    private final UploadTask upload_task;
    
    public LinuxForceFeedbackEffect(final LinuxEventDevice device) throws IOException {
        this.write_task = new WriteTask();
        this.upload_task = new UploadTask();
        this.device = device;
        this.ff_id = this.upload_task.doUpload(-1, 0.0f);
    }
    
    protected abstract int upload(final int p0, final float p1) throws IOException;
    
    protected final LinuxEventDevice getDevice() {
        return this.device;
    }
    
    public final synchronized void rumble(final float intensity) {
        try {
            if (intensity > 0.0f) {
                this.upload_task.doUpload(this.ff_id, intensity);
                this.write_task.write(1);
            }
            else {
                this.write_task.write(0);
            }
        }
        catch (IOException e) {
            ControllerEnvironment.logln("Failed to rumble: " + e);
        }
    }
    
    public final String getAxisName() {
        return null;
    }
    
    public final Component.Identifier getAxisIdentifier() {
        return null;
    }
    
    private final class UploadTask extends LinuxDeviceTask
    {
        private int id;
        private float intensity;
        
        public final int doUpload(final int id, final float intensity) throws IOException {
            this.id = id;
            this.intensity = intensity;
            LinuxEnvironmentPlugin.execute(this);
            return this.id;
        }
        
        protected final Object execute() throws IOException {
            this.id = LinuxForceFeedbackEffect.this.upload(this.id, this.intensity);
            return null;
        }
    }
    
    private final class WriteTask extends LinuxDeviceTask
    {
        private int value;
        
        public final void write(final int value) throws IOException {
            this.value = value;
            LinuxEnvironmentPlugin.execute(this);
        }
        
        protected final Object execute() throws IOException {
            LinuxForceFeedbackEffect.this.device.writeEvent(21, LinuxForceFeedbackEffect.this.ff_id, this.value);
            return null;
        }
    }
}
