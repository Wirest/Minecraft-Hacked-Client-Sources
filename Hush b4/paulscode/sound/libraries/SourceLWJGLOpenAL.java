// 
// Decompiled by Procyon v0.5.36
// 

package paulscode.sound.libraries;

import java.util.LinkedList;
import paulscode.sound.Channel;
import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;
import paulscode.sound.SoundSystemConfig;
import javax.sound.sampled.AudioFormat;
import paulscode.sound.SoundBuffer;
import paulscode.sound.FilenameURL;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import paulscode.sound.Source;

public class SourceLWJGLOpenAL extends Source
{
    private ChannelLWJGLOpenAL channelOpenAL;
    private IntBuffer myBuffer;
    private FloatBuffer listenerPosition;
    private FloatBuffer sourcePosition;
    private FloatBuffer sourceVelocity;
    
    public SourceLWJGLOpenAL(final FloatBuffer listenerPosition, final IntBuffer myBuffer, final boolean priority, final boolean toStream, final boolean toLoop, final String sourcename, final FilenameURL filenameURL, final SoundBuffer soundBuffer, final float x, final float y, final float z, final int attModel, final float distOrRoll, final boolean temporary) {
        super(priority, toStream, toLoop, sourcename, filenameURL, soundBuffer, x, y, z, attModel, distOrRoll, temporary);
        this.channelOpenAL = (ChannelLWJGLOpenAL)this.channel;
        if (this.codec != null) {
            this.codec.reverseByteOrder(true);
        }
        this.listenerPosition = listenerPosition;
        this.myBuffer = myBuffer;
        this.libraryType = LibraryLWJGLOpenAL.class;
        this.pitch = 1.0f;
        this.resetALInformation();
    }
    
    public SourceLWJGLOpenAL(final FloatBuffer listenerPosition, final IntBuffer myBuffer, final Source old, final SoundBuffer soundBuffer) {
        super(old, soundBuffer);
        this.channelOpenAL = (ChannelLWJGLOpenAL)this.channel;
        if (this.codec != null) {
            this.codec.reverseByteOrder(true);
        }
        this.listenerPosition = listenerPosition;
        this.myBuffer = myBuffer;
        this.libraryType = LibraryLWJGLOpenAL.class;
        this.pitch = 1.0f;
        this.resetALInformation();
    }
    
    public SourceLWJGLOpenAL(final FloatBuffer listenerPosition, final AudioFormat audioFormat, final boolean priority, final String sourcename, final float x, final float y, final float z, final int attModel, final float distOrRoll) {
        super(audioFormat, priority, sourcename, x, y, z, attModel, distOrRoll);
        this.channelOpenAL = (ChannelLWJGLOpenAL)this.channel;
        this.listenerPosition = listenerPosition;
        this.libraryType = LibraryLWJGLOpenAL.class;
        this.pitch = 1.0f;
        this.resetALInformation();
    }
    
    @Override
    public void cleanup() {
        super.cleanup();
    }
    
    public void changeSource(final FloatBuffer listenerPosition, final IntBuffer myBuffer, final boolean priority, final boolean toStream, final boolean toLoop, final String sourcename, final FilenameURL filenameURL, final SoundBuffer soundBuffer, final float x, final float y, final float z, final int attModel, final float distOrRoll, final boolean temporary) {
        super.changeSource(priority, toStream, toLoop, sourcename, filenameURL, soundBuffer, x, y, z, attModel, distOrRoll, temporary);
        this.listenerPosition = listenerPosition;
        this.myBuffer = myBuffer;
        this.pitch = 1.0f;
        this.resetALInformation();
    }
    
    @Override
    public boolean incrementSoundSequence() {
        if (!this.toStream) {
            this.errorMessage("Method 'incrementSoundSequence' may only be used for streaming sources.");
            return false;
        }
        synchronized (this.soundSequenceLock) {
            if (this.soundSequenceQueue != null && this.soundSequenceQueue.size() > 0) {
                this.filenameURL = this.soundSequenceQueue.remove(0);
                if (this.codec != null) {
                    this.codec.cleanup();
                }
                this.codec = SoundSystemConfig.getCodec(this.filenameURL.getFilename());
                if (this.codec != null) {
                    this.codec.reverseByteOrder(true);
                    if (this.codec.getAudioFormat() == null) {
                        this.codec.initialize(this.filenameURL.getURL());
                    }
                    final AudioFormat audioFormat = this.codec.getAudioFormat();
                    if (audioFormat == null) {
                        this.errorMessage("Audio Format null in method 'incrementSoundSequence'");
                        return false;
                    }
                    int soundFormat = 0;
                    if (audioFormat.getChannels() == 1) {
                        if (audioFormat.getSampleSizeInBits() == 8) {
                            soundFormat = 4352;
                        }
                        else {
                            if (audioFormat.getSampleSizeInBits() != 16) {
                                this.errorMessage("Illegal sample size in method 'incrementSoundSequence'");
                                return false;
                            }
                            soundFormat = 4353;
                        }
                    }
                    else {
                        if (audioFormat.getChannels() != 2) {
                            this.errorMessage("Audio data neither mono nor stereo in method 'incrementSoundSequence'");
                            return false;
                        }
                        if (audioFormat.getSampleSizeInBits() == 8) {
                            soundFormat = 4354;
                        }
                        else {
                            if (audioFormat.getSampleSizeInBits() != 16) {
                                this.errorMessage("Illegal sample size in method 'incrementSoundSequence'");
                                return false;
                            }
                            soundFormat = 4355;
                        }
                    }
                    this.channelOpenAL.setFormat(soundFormat, (int)audioFormat.getSampleRate());
                    this.preLoad = true;
                }
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void listenerMoved() {
        this.positionChanged();
    }
    
    @Override
    public void setPosition(final float x, final float y, final float z) {
        super.setPosition(x, y, z);
        if (this.sourcePosition == null) {
            this.resetALInformation();
        }
        else {
            this.positionChanged();
        }
        this.sourcePosition.put(0, x);
        this.sourcePosition.put(1, y);
        this.sourcePosition.put(2, z);
        if (this.channel != null && this.channel.attachedSource == this && this.channelOpenAL != null && this.channelOpenAL.ALSource != null) {
            AL10.alSource(this.channelOpenAL.ALSource.get(0), 4100, this.sourcePosition);
            this.checkALError();
        }
    }
    
    @Override
    public void positionChanged() {
        this.calculateDistance();
        this.calculateGain();
        if (this.channel != null && this.channel.attachedSource == this && this.channelOpenAL != null && this.channelOpenAL.ALSource != null) {
            AL10.alSourcef(this.channelOpenAL.ALSource.get(0), 4106, this.gain * this.sourceVolume * Math.abs(this.fadeOutGain) * this.fadeInGain);
            this.checkALError();
        }
        this.checkPitch();
    }
    
    private void checkPitch() {
        if (this.channel != null && this.channel.attachedSource == this && LibraryLWJGLOpenAL.alPitchSupported() && this.channelOpenAL != null && this.channelOpenAL.ALSource != null) {
            AL10.alSourcef(this.channelOpenAL.ALSource.get(0), 4099, this.pitch);
            this.checkALError();
        }
    }
    
    @Override
    public void setLooping(final boolean lp) {
        super.setLooping(lp);
        if (this.channel != null && this.channel.attachedSource == this && this.channelOpenAL != null && this.channelOpenAL.ALSource != null) {
            if (lp) {
                AL10.alSourcei(this.channelOpenAL.ALSource.get(0), 4103, 1);
            }
            else {
                AL10.alSourcei(this.channelOpenAL.ALSource.get(0), 4103, 0);
            }
            this.checkALError();
        }
    }
    
    @Override
    public void setAttenuation(final int model) {
        super.setAttenuation(model);
        if (this.channel != null && this.channel.attachedSource == this && this.channelOpenAL != null && this.channelOpenAL.ALSource != null) {
            if (model == 1) {
                AL10.alSourcef(this.channelOpenAL.ALSource.get(0), 4129, this.distOrRoll);
            }
            else {
                AL10.alSourcef(this.channelOpenAL.ALSource.get(0), 4129, 0.0f);
            }
            this.checkALError();
        }
    }
    
    @Override
    public void setDistOrRoll(final float dr) {
        super.setDistOrRoll(dr);
        if (this.channel != null && this.channel.attachedSource == this && this.channelOpenAL != null && this.channelOpenAL.ALSource != null) {
            if (this.attModel == 1) {
                AL10.alSourcef(this.channelOpenAL.ALSource.get(0), 4129, dr);
            }
            else {
                AL10.alSourcef(this.channelOpenAL.ALSource.get(0), 4129, 0.0f);
            }
            this.checkALError();
        }
    }
    
    @Override
    public void setVelocity(final float x, final float y, final float z) {
        super.setVelocity(x, y, z);
        (this.sourceVelocity = BufferUtils.createFloatBuffer(3).put(new float[] { x, y, z })).flip();
        if (this.channel != null && this.channel.attachedSource == this && this.channelOpenAL != null && this.channelOpenAL.ALSource != null) {
            AL10.alSource(this.channelOpenAL.ALSource.get(0), 4102, this.sourceVelocity);
            this.checkALError();
        }
    }
    
    @Override
    public void setPitch(final float value) {
        super.setPitch(value);
        this.checkPitch();
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
        super.play(c);
        this.channelOpenAL = (ChannelLWJGLOpenAL)this.channel;
        if (newChannel) {
            this.setPosition(this.position.x, this.position.y, this.position.z);
            this.checkPitch();
            if (this.channelOpenAL != null && this.channelOpenAL.ALSource != null) {
                if (LibraryLWJGLOpenAL.alPitchSupported()) {
                    AL10.alSourcef(this.channelOpenAL.ALSource.get(0), 4099, this.pitch);
                    this.checkALError();
                }
                AL10.alSource(this.channelOpenAL.ALSource.get(0), 4100, this.sourcePosition);
                this.checkALError();
                AL10.alSource(this.channelOpenAL.ALSource.get(0), 4102, this.sourceVelocity);
                this.checkALError();
                if (this.attModel == 1) {
                    AL10.alSourcef(this.channelOpenAL.ALSource.get(0), 4129, this.distOrRoll);
                }
                else {
                    AL10.alSourcef(this.channelOpenAL.ALSource.get(0), 4129, 0.0f);
                }
                this.checkALError();
                if (this.toLoop && !this.toStream) {
                    AL10.alSourcei(this.channelOpenAL.ALSource.get(0), 4103, 1);
                }
                else {
                    AL10.alSourcei(this.channelOpenAL.ALSource.get(0), 4103, 0);
                }
                this.checkALError();
            }
            if (!this.toStream) {
                if (this.myBuffer == null) {
                    this.errorMessage("No sound buffer to play");
                    return;
                }
                this.channelOpenAL.attachBuffer(this.myBuffer);
            }
        }
        if (!this.playing()) {
            if (this.toStream && !wasPaused) {
                if (this.codec == null) {
                    this.errorMessage("Decoder null in method 'play'");
                    return;
                }
                if (this.codec.getAudioFormat() == null) {
                    this.codec.initialize(this.filenameURL.getURL());
                }
                final AudioFormat audioFormat = this.codec.getAudioFormat();
                if (audioFormat == null) {
                    this.errorMessage("Audio Format null in method 'play'");
                    return;
                }
                int soundFormat = 0;
                if (audioFormat.getChannels() == 1) {
                    if (audioFormat.getSampleSizeInBits() == 8) {
                        soundFormat = 4352;
                    }
                    else {
                        if (audioFormat.getSampleSizeInBits() != 16) {
                            this.errorMessage("Illegal sample size in method 'play'");
                            return;
                        }
                        soundFormat = 4353;
                    }
                }
                else {
                    if (audioFormat.getChannels() != 2) {
                        this.errorMessage("Audio data neither mono nor stereo in method 'play'");
                        return;
                    }
                    if (audioFormat.getSampleSizeInBits() == 8) {
                        soundFormat = 4354;
                    }
                    else {
                        if (audioFormat.getSampleSizeInBits() != 16) {
                            this.errorMessage("Illegal sample size in method 'play'");
                            return;
                        }
                        soundFormat = 4355;
                    }
                }
                this.channelOpenAL.setFormat(soundFormat, (int)audioFormat.getSampleRate());
                this.preLoad = true;
            }
            this.channel.play();
            if (this.pitch != 1.0f) {
                this.checkPitch();
            }
        }
    }
    
    @Override
    public boolean preLoad() {
        if (this.codec == null) {
            return false;
        }
        this.codec.initialize(this.filenameURL.getURL());
        final LinkedList<byte[]> preLoadBuffers = new LinkedList<byte[]>();
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
        this.positionChanged();
        this.channel.preLoadBuffers(preLoadBuffers);
        this.preLoad = false;
        return true;
    }
    
    private void resetALInformation() {
        this.sourcePosition = BufferUtils.createFloatBuffer(3).put(new float[] { this.position.x, this.position.y, this.position.z });
        this.sourceVelocity = BufferUtils.createFloatBuffer(3).put(new float[] { this.velocity.x, this.velocity.y, this.velocity.z });
        this.sourcePosition.flip();
        this.sourceVelocity.flip();
        this.positionChanged();
    }
    
    private void calculateDistance() {
        if (this.listenerPosition != null) {
            final double dX = this.position.x - this.listenerPosition.get(0);
            final double dY = this.position.y - this.listenerPosition.get(1);
            final double dZ = this.position.z - this.listenerPosition.get(2);
            this.distanceFromListener = (float)Math.sqrt(dX * dX + dY * dY + dZ * dZ);
        }
    }
    
    private void calculateGain() {
        if (this.attModel == 2) {
            if (this.distanceFromListener <= 0.0f) {
                this.gain = 1.0f;
            }
            else if (this.distanceFromListener >= this.distOrRoll) {
                this.gain = 0.0f;
            }
            else {
                this.gain = 1.0f - this.distanceFromListener / this.distOrRoll;
            }
            if (this.gain > 1.0f) {
                this.gain = 1.0f;
            }
            if (this.gain < 0.0f) {
                this.gain = 0.0f;
            }
        }
        else {
            this.gain = 1.0f;
        }
    }
    
    private boolean checkALError() {
        switch (AL10.alGetError()) {
            case 0: {
                return false;
            }
            case 40961: {
                this.errorMessage("Invalid name parameter.");
                return true;
            }
            case 40962: {
                this.errorMessage("Invalid parameter.");
                return true;
            }
            case 40963: {
                this.errorMessage("Invalid enumerated parameter value.");
                return true;
            }
            case 40964: {
                this.errorMessage("Illegal call.");
                return true;
            }
            case 40965: {
                this.errorMessage("Unable to allocate memory.");
                return true;
            }
            default: {
                this.errorMessage("An unrecognized error occurred.");
                return true;
            }
        }
    }
}
