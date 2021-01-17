// 
// Decompiled by Procyon v0.5.36
// 

package paulscode.sound.libraries;

import paulscode.sound.Vector3D;
import paulscode.sound.SoundSystemConfig;
import java.util.LinkedList;
import paulscode.sound.Channel;
import javax.sound.sampled.AudioFormat;
import paulscode.sound.SoundBuffer;
import paulscode.sound.FilenameURL;
import paulscode.sound.ListenerData;
import paulscode.sound.Source;

public class SourceJavaSound extends Source
{
    protected ChannelJavaSound channelJavaSound;
    public ListenerData listener;
    private float pan;
    
    public SourceJavaSound(final ListenerData listener, final boolean priority, final boolean toStream, final boolean toLoop, final String sourcename, final FilenameURL filenameURL, final SoundBuffer soundBuffer, final float x, final float y, final float z, final int attModel, final float distOrRoll, final boolean temporary) {
        super(priority, toStream, toLoop, sourcename, filenameURL, soundBuffer, x, y, z, attModel, distOrRoll, temporary);
        this.channelJavaSound = (ChannelJavaSound)this.channel;
        this.pan = 0.0f;
        this.libraryType = LibraryJavaSound.class;
        this.listener = listener;
        this.positionChanged();
    }
    
    public SourceJavaSound(final ListenerData listener, final Source old, final SoundBuffer soundBuffer) {
        super(old, soundBuffer);
        this.channelJavaSound = (ChannelJavaSound)this.channel;
        this.pan = 0.0f;
        this.libraryType = LibraryJavaSound.class;
        this.listener = listener;
        this.positionChanged();
    }
    
    public SourceJavaSound(final ListenerData listener, final AudioFormat audioFormat, final boolean priority, final String sourcename, final float x, final float y, final float z, final int attModel, final float distOrRoll) {
        super(audioFormat, priority, sourcename, x, y, z, attModel, distOrRoll);
        this.channelJavaSound = (ChannelJavaSound)this.channel;
        this.pan = 0.0f;
        this.libraryType = LibraryJavaSound.class;
        this.listener = listener;
        this.positionChanged();
    }
    
    @Override
    public void cleanup() {
        super.cleanup();
    }
    
    @Override
    public void changeSource(final boolean priority, final boolean toStream, final boolean toLoop, final String sourcename, final FilenameURL filenameURL, final SoundBuffer soundBuffer, final float x, final float y, final float z, final int attModel, final float distOrRoll, final boolean temporary) {
        super.changeSource(priority, toStream, toLoop, sourcename, filenameURL, soundBuffer, x, y, z, attModel, distOrRoll, temporary);
        if (this.channelJavaSound != null) {
            this.channelJavaSound.setLooping(toLoop);
        }
        this.positionChanged();
    }
    
    @Override
    public void listenerMoved() {
        this.positionChanged();
    }
    
    @Override
    public void setVelocity(final float x, final float y, final float z) {
        super.setVelocity(x, y, z);
        this.positionChanged();
    }
    
    @Override
    public void setPosition(final float x, final float y, final float z) {
        super.setPosition(x, y, z);
        this.positionChanged();
    }
    
    @Override
    public void positionChanged() {
        this.calculateGain();
        this.calculatePan();
        this.calculatePitch();
    }
    
    @Override
    public void setPitch(final float value) {
        super.setPitch(value);
        this.calculatePitch();
    }
    
    @Override
    public void setAttenuation(final int model) {
        super.setAttenuation(model);
        this.calculateGain();
    }
    
    @Override
    public void setDistOrRoll(final float dr) {
        super.setDistOrRoll(dr);
        this.calculateGain();
    }
    
    @Override
    public void play(final Channel c) {
        if (!this.active()) {
            if (this.toLoop) {
                this.toPlay = true;
            }
            return;
        }
        if (c == null) {
            this.errorMessage("Unable to play source, because channel was null");
            return;
        }
        boolean newChannel = this.channel != c;
        if (this.channel != null && this.channel.attachedSource != this) {
            newChannel = true;
        }
        final boolean wasPaused = this.paused();
        final boolean wasStopped = this.stopped();
        super.play(c);
        this.channelJavaSound = (ChannelJavaSound)this.channel;
        if (newChannel) {
            if (this.channelJavaSound != null) {
                this.channelJavaSound.setLooping(this.toLoop);
            }
            if (!this.toStream) {
                if (this.soundBuffer == null) {
                    this.errorMessage("No sound buffer to play");
                    return;
                }
                this.channelJavaSound.attachBuffer(this.soundBuffer);
            }
        }
        this.positionChanged();
        if (wasStopped || !this.playing()) {
            if (this.toStream && !wasPaused) {
                this.preLoad = true;
            }
            this.channel.play();
        }
    }
    
    @Override
    public boolean preLoad() {
        if (this.codec == null) {
            return false;
        }
        boolean noNextBuffers = false;
        synchronized (this.soundSequenceLock) {
            if (this.nextBuffers == null || this.nextBuffers.isEmpty()) {
                noNextBuffers = true;
            }
        }
        final LinkedList<byte[]> preLoadBuffers = new LinkedList<byte[]>();
        if (this.nextCodec != null && !noNextBuffers) {
            this.codec = this.nextCodec;
            this.nextCodec = null;
            synchronized (this.soundSequenceLock) {
                while (!this.nextBuffers.isEmpty()) {
                    this.soundBuffer = this.nextBuffers.remove(0);
                    if (this.soundBuffer != null && this.soundBuffer.audioData != null) {
                        preLoadBuffers.add(this.soundBuffer.audioData);
                    }
                }
            }
        }
        else {
            this.codec.initialize(this.filenameURL.getURL());
            for (int i = 0; i < SoundSystemConfig.getNumberStreamingBuffers(); ++i) {
                this.soundBuffer = this.codec.read();
                if (this.soundBuffer == null) {
                    break;
                }
                if (this.soundBuffer.audioData == null) {
                    break;
                }
                preLoadBuffers.add(this.soundBuffer.audioData);
            }
            this.channelJavaSound.resetStream(this.codec.getAudioFormat());
        }
        this.positionChanged();
        this.channel.preLoadBuffers(preLoadBuffers);
        this.preLoad = false;
        return true;
    }
    
    public void calculateGain() {
        final float distX = this.position.x - this.listener.position.x;
        final float distY = this.position.y - this.listener.position.y;
        final float distZ = this.position.z - this.listener.position.z;
        this.distanceFromListener = (float)Math.sqrt(distX * distX + distY * distY + distZ * distZ);
        switch (this.attModel) {
            case 2: {
                if (this.distanceFromListener <= 0.0f) {
                    this.gain = 1.0f;
                    break;
                }
                if (this.distanceFromListener >= this.distOrRoll) {
                    this.gain = 0.0f;
                    break;
                }
                this.gain = 1.0f - this.distanceFromListener / this.distOrRoll;
                break;
            }
            case 1: {
                if (this.distanceFromListener <= 0.0f) {
                    this.gain = 1.0f;
                    break;
                }
                final float tweakFactor = 5.0E-4f;
                float attenuationFactor = this.distOrRoll * this.distanceFromListener * this.distanceFromListener * tweakFactor;
                if (attenuationFactor < 0.0f) {
                    attenuationFactor = 0.0f;
                }
                this.gain = 1.0f / (1.0f + attenuationFactor);
                break;
            }
            default: {
                this.gain = 1.0f;
                break;
            }
        }
        if (this.gain > 1.0f) {
            this.gain = 1.0f;
        }
        if (this.gain < 0.0f) {
            this.gain = 0.0f;
        }
        this.gain *= this.sourceVolume * SoundSystemConfig.getMasterGain() * Math.abs(this.fadeOutGain) * this.fadeInGain;
        if (this.channel != null && this.channel.attachedSource == this && this.channelJavaSound != null) {
            this.channelJavaSound.setGain(this.gain);
        }
    }
    
    public void calculatePan() {
        Vector3D side = this.listener.up.cross(this.listener.lookAt);
        side.normalize();
        final float x = this.position.dot(this.position.subtract(this.listener.position), side);
        final float z = this.position.dot(this.position.subtract(this.listener.position), this.listener.lookAt);
        side = null;
        final float angle = (float)Math.atan2(x, z);
        this.pan = (float)(-Math.sin(angle));
        if (this.channel != null && this.channel.attachedSource == this && this.channelJavaSound != null) {
            if (this.attModel == 0) {
                this.channelJavaSound.setPan(0.0f);
            }
            else {
                this.channelJavaSound.setPan(this.pan);
            }
        }
    }
    
    public void calculatePitch() {
        if (this.channel != null && this.channel.attachedSource == this && this.channelJavaSound != null) {
            if (SoundSystemConfig.getDopplerFactor() == 0.0f) {
                this.channelJavaSound.setPitch(this.pitch);
            }
            else {
                final float SS = 343.3f;
                final Vector3D SV = this.velocity;
                final Vector3D LV = this.listener.velocity;
                final float DV = SoundSystemConfig.getDopplerVelocity();
                final float DF = SoundSystemConfig.getDopplerFactor();
                final Vector3D SL = this.listener.position.subtract(this.position);
                float vls = SL.dot(LV) / SL.length();
                float vss = SL.dot(SV) / SL.length();
                vss = this.min(vss, SS / DF);
                vls = this.min(vls, SS / DF);
                float newPitch = this.pitch * (SS * DV - DF * vls) / (SS * DV - DF * vss);
                if (newPitch < 0.5f) {
                    newPitch = 0.5f;
                }
                else if (newPitch > 2.0f) {
                    newPitch = 2.0f;
                }
                this.channelJavaSound.setPitch(newPitch);
            }
        }
    }
    
    public float min(final float a, final float b) {
        if (a < b) {
            return a;
        }
        return b;
    }
}
