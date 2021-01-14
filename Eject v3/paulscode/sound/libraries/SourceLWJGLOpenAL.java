package paulscode.sound.libraries;

import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;
import paulscode.sound.*;

import javax.sound.sampled.AudioFormat;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.LinkedList;

public class SourceLWJGLOpenAL
        extends Source {
    private ChannelLWJGLOpenAL channelOpenAL = (ChannelLWJGLOpenAL) this.channel;
    private IntBuffer myBuffer;
    private FloatBuffer listenerPosition;
    private FloatBuffer sourcePosition;
    private FloatBuffer sourceVelocity;

    public SourceLWJGLOpenAL(FloatBuffer paramFloatBuffer, IntBuffer paramIntBuffer, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, String paramString, FilenameURL paramFilenameURL, SoundBuffer paramSoundBuffer, float paramFloat1, float paramFloat2, float paramFloat3, int paramInt, float paramFloat4, boolean paramBoolean4) {
        super(paramBoolean1, paramBoolean2, paramBoolean3, paramString, paramFilenameURL, paramSoundBuffer, paramFloat1, paramFloat2, paramFloat3, paramInt, paramFloat4, paramBoolean4);
        if (this.codec != null) {
            this.codec.reverseByteOrder(true);
        }
        this.listenerPosition = paramFloatBuffer;
        this.myBuffer = paramIntBuffer;
        this.libraryType = LibraryLWJGLOpenAL.class;
        this.pitch = 1.0F;
        resetALInformation();
    }

    public SourceLWJGLOpenAL(FloatBuffer paramFloatBuffer, IntBuffer paramIntBuffer, Source paramSource, SoundBuffer paramSoundBuffer) {
        super(paramSource, paramSoundBuffer);
        if (this.codec != null) {
            this.codec.reverseByteOrder(true);
        }
        this.listenerPosition = paramFloatBuffer;
        this.myBuffer = paramIntBuffer;
        this.libraryType = LibraryLWJGLOpenAL.class;
        this.pitch = 1.0F;
        resetALInformation();
    }

    public SourceLWJGLOpenAL(FloatBuffer paramFloatBuffer, AudioFormat paramAudioFormat, boolean paramBoolean, String paramString, float paramFloat1, float paramFloat2, float paramFloat3, int paramInt, float paramFloat4) {
        super(paramAudioFormat, paramBoolean, paramString, paramFloat1, paramFloat2, paramFloat3, paramInt, paramFloat4);
        this.listenerPosition = paramFloatBuffer;
        this.libraryType = LibraryLWJGLOpenAL.class;
        this.pitch = 1.0F;
        resetALInformation();
    }

    public void cleanup() {
        super.cleanup();
    }

    public void changeSource(FloatBuffer paramFloatBuffer, IntBuffer paramIntBuffer, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, String paramString, FilenameURL paramFilenameURL, SoundBuffer paramSoundBuffer, float paramFloat1, float paramFloat2, float paramFloat3, int paramInt, float paramFloat4, boolean paramBoolean4) {
        super.changeSource(paramBoolean1, paramBoolean2, paramBoolean3, paramString, paramFilenameURL, paramSoundBuffer, paramFloat1, paramFloat2, paramFloat3, paramInt, paramFloat4, paramBoolean4);
        this.listenerPosition = paramFloatBuffer;
        this.myBuffer = paramIntBuffer;
        this.pitch = 1.0F;
        resetALInformation();
    }

    public boolean incrementSoundSequence() {
        if (!this.toStream) {
            errorMessage("Method 'incrementSoundSequence' may only be used for streaming sources.");
            return false;
        }
        synchronized (this.soundSequenceLock) {
            if ((this.soundSequenceQueue != null) && (this.soundSequenceQueue.size() > 0)) {
                this.filenameURL = ((FilenameURL) this.soundSequenceQueue.remove(0));
                if (this.codec != null) {
                    this.codec.cleanup();
                }
                this.codec = SoundSystemConfig.getCodec(this.filenameURL.getFilename());
                if (this.codec != null) {
                    this.codec.reverseByteOrder(true);
                    if (this.codec.getAudioFormat() == null) {
                        this.codec.initialize(this.filenameURL.getURL());
                    }
                    AudioFormat localAudioFormat = this.codec.getAudioFormat();
                    if (localAudioFormat == null) {
                        errorMessage("Audio Format null in method 'incrementSoundSequence'");
                        return false;
                    }
                    int i = 0;
                    if (localAudioFormat.getChannels() == 1) {
                        if (localAudioFormat.getSampleSizeInBits() == 8) {
                            i = 4352;
                        } else if (localAudioFormat.getSampleSizeInBits() == 16) {
                            i = 4353;
                        } else {
                            errorMessage("Illegal sample size in method 'incrementSoundSequence'");
                            return false;
                        }
                    } else if (localAudioFormat.getChannels() == 2) {
                        if (localAudioFormat.getSampleSizeInBits() == 8) {
                            i = 4354;
                        } else if (localAudioFormat.getSampleSizeInBits() == 16) {
                            i = 4355;
                        } else {
                            errorMessage("Illegal sample size in method 'incrementSoundSequence'");
                            return false;
                        }
                    } else {
                        errorMessage("Audio data neither mono nor stereo in method 'incrementSoundSequence'");
                        return false;
                    }
                    this.channelOpenAL.setFormat(i, (int) localAudioFormat.getSampleRate());
                    this.preLoad = true;
                }
                return true;
            }
        }
        return false;
    }

    public void listenerMoved() {
        positionChanged();
    }

    public void setPosition(float paramFloat1, float paramFloat2, float paramFloat3) {
        super.setPosition(paramFloat1, paramFloat2, paramFloat3);
        if (this.sourcePosition == null) {
            resetALInformation();
        } else {
            positionChanged();
        }
        this.sourcePosition.put(0, paramFloat1);
        this.sourcePosition.put(1, paramFloat2);
        this.sourcePosition.put(2, paramFloat3);
        if ((this.channel != null) && (this.channel.attachedSource == this) && (this.channelOpenAL != null) && (this.channelOpenAL.ALSource != null)) {
            AL10.alSource(this.channelOpenAL.ALSource.get(0), 4100, this.sourcePosition);
            checkALError();
        }
    }

    public void positionChanged() {
        calculateDistance();
        calculateGain();
        if ((this.channel != null) && (this.channel.attachedSource == this) && (this.channelOpenAL != null) && (this.channelOpenAL.ALSource != null)) {
            AL10.alSourcef(this.channelOpenAL.ALSource.get(0), 4106, this.gain * this.sourceVolume * Math.abs(this.fadeOutGain) * this.fadeInGain);
            checkALError();
        }
        checkPitch();
    }

    private void checkPitch() {
        if ((this.channel != null) && (this.channel.attachedSource == this) && (LibraryLWJGLOpenAL.alPitchSupported()) && (this.channelOpenAL != null) && (this.channelOpenAL.ALSource != null)) {
            AL10.alSourcef(this.channelOpenAL.ALSource.get(0), 4099, this.pitch);
            checkALError();
        }
    }

    public void setLooping(boolean paramBoolean) {
        super.setLooping(paramBoolean);
        if ((this.channel != null) && (this.channel.attachedSource == this) && (this.channelOpenAL != null) && (this.channelOpenAL.ALSource != null)) {
            if (paramBoolean) {
                AL10.alSourcei(this.channelOpenAL.ALSource.get(0), 4103, 1);
            } else {
                AL10.alSourcei(this.channelOpenAL.ALSource.get(0), 4103, 0);
            }
            checkALError();
        }
    }

    public void setAttenuation(int paramInt) {
        super.setAttenuation(paramInt);
        if ((this.channel != null) && (this.channel.attachedSource == this) && (this.channelOpenAL != null) && (this.channelOpenAL.ALSource != null)) {
            if (paramInt == 1) {
                AL10.alSourcef(this.channelOpenAL.ALSource.get(0), 4129, this.distOrRoll);
            } else {
                AL10.alSourcef(this.channelOpenAL.ALSource.get(0), 4129, 0.0F);
            }
            checkALError();
        }
    }

    public void setDistOrRoll(float paramFloat) {
        super.setDistOrRoll(paramFloat);
        if ((this.channel != null) && (this.channel.attachedSource == this) && (this.channelOpenAL != null) && (this.channelOpenAL.ALSource != null)) {
            if (this.attModel == 1) {
                AL10.alSourcef(this.channelOpenAL.ALSource.get(0), 4129, paramFloat);
            } else {
                AL10.alSourcef(this.channelOpenAL.ALSource.get(0), 4129, 0.0F);
            }
            checkALError();
        }
    }

    public void setVelocity(float paramFloat1, float paramFloat2, float paramFloat3) {
        super.setVelocity(paramFloat1, paramFloat2, paramFloat3);
        this.sourceVelocity = BufferUtils.createFloatBuffer(3).put(new float[]{paramFloat1, paramFloat2, paramFloat3});
        this.sourceVelocity.flip();
        if ((this.channel != null) && (this.channel.attachedSource == this) && (this.channelOpenAL != null) && (this.channelOpenAL.ALSource != null)) {
            AL10.alSource(this.channelOpenAL.ALSource.get(0), 4102, this.sourceVelocity);
            checkALError();
        }
    }

    public void setPitch(float paramFloat) {
        super.setPitch(paramFloat);
        checkPitch();
    }

    public void play(Channel paramChannel) {
        if (!active()) {
            if (this.toLoop) {
                this.toPlay = true;
            }
            return;
        }
        if (paramChannel == null) {
            errorMessage("Unable to play source, because channel was null");
            return;
        }
        int i = this.channel != paramChannel ? 1 : 0;
        if ((this.channel != null) && (this.channel.attachedSource != this)) {
            i = 1;
        }
        boolean bool = paused();
        super.play(paramChannel);
        this.channelOpenAL = ((ChannelLWJGLOpenAL) this.channel);
        if (i != 0) {
            setPosition(this.position.x, this.position.y, this.position.z);
            checkPitch();
            if ((this.channelOpenAL != null) && (this.channelOpenAL.ALSource != null)) {
                if (LibraryLWJGLOpenAL.alPitchSupported()) {
                    AL10.alSourcef(this.channelOpenAL.ALSource.get(0), 4099, this.pitch);
                    checkALError();
                }
                AL10.alSource(this.channelOpenAL.ALSource.get(0), 4100, this.sourcePosition);
                checkALError();
                AL10.alSource(this.channelOpenAL.ALSource.get(0), 4102, this.sourceVelocity);
                checkALError();
                if (this.attModel == 1) {
                    AL10.alSourcef(this.channelOpenAL.ALSource.get(0), 4129, this.distOrRoll);
                } else {
                    AL10.alSourcef(this.channelOpenAL.ALSource.get(0), 4129, 0.0F);
                }
                checkALError();
                if ((this.toLoop) && (!this.toStream)) {
                    AL10.alSourcei(this.channelOpenAL.ALSource.get(0), 4103, 1);
                } else {
                    AL10.alSourcei(this.channelOpenAL.ALSource.get(0), 4103, 0);
                }
                checkALError();
            }
            if (!this.toStream) {
                if (this.myBuffer == null) {
                    errorMessage("No sound buffer to play");
                    return;
                }
                this.channelOpenAL.attachBuffer(this.myBuffer);
            }
        }
        if (!playing()) {
            if ((this.toStream) && (!bool)) {
                if (this.codec == null) {
                    errorMessage("Decoder null in method 'play'");
                    return;
                }
                if (this.codec.getAudioFormat() == null) {
                    this.codec.initialize(this.filenameURL.getURL());
                }
                AudioFormat localAudioFormat = this.codec.getAudioFormat();
                if (localAudioFormat == null) {
                    errorMessage("Audio Format null in method 'play'");
                    return;
                }
                int j = 0;
                if (localAudioFormat.getChannels() == 1) {
                    if (localAudioFormat.getSampleSizeInBits() == 8) {
                        j = 4352;
                    } else if (localAudioFormat.getSampleSizeInBits() == 16) {
                        j = 4353;
                    } else {
                        errorMessage("Illegal sample size in method 'play'");
                    }
                } else if (localAudioFormat.getChannels() == 2) {
                    if (localAudioFormat.getSampleSizeInBits() == 8) {
                        j = 4354;
                    } else if (localAudioFormat.getSampleSizeInBits() == 16) {
                        j = 4355;
                    } else {
                        errorMessage("Illegal sample size in method 'play'");
                    }
                } else {
                    errorMessage("Audio data neither mono nor stereo in method 'play'");
                    return;
                }
                this.channelOpenAL.setFormat(j, (int) localAudioFormat.getSampleRate());
                this.preLoad = true;
            }
            this.channel.play();
            if (this.pitch != 1.0F) {
                checkPitch();
            }
        }
    }

    public boolean preLoad() {
        if (this.codec == null) {
            return false;
        }
        this.codec.initialize(this.filenameURL.getURL());
        LinkedList localLinkedList = new LinkedList();
        for (int i = 0; i < SoundSystemConfig.getNumberStreamingBuffers(); i++) {
            this.soundBuffer = this.codec.read();
            if ((this.soundBuffer == null) || (this.soundBuffer.audioData == null)) {
                break;
            }
            localLinkedList.add(this.soundBuffer.audioData);
        }
        positionChanged();
        this.channel.preLoadBuffers(localLinkedList);
        this.preLoad = false;
        return true;
    }

    private void resetALInformation() {
        this.sourcePosition = BufferUtils.createFloatBuffer(3).put(new float[]{this.position.x, this.position.y, this.position.z});
        this.sourceVelocity = BufferUtils.createFloatBuffer(3).put(new float[]{this.velocity.x, this.velocity.y, this.velocity.z});
        this.sourcePosition.flip();
        this.sourceVelocity.flip();
        positionChanged();
    }

    private void calculateDistance() {
        if (this.listenerPosition != null) {
            double d1 = this.position.x - this.listenerPosition.get(0);
            double d2 = this.position.y - this.listenerPosition.get(1);
            double d3 = this.position.z - this.listenerPosition.get(2);
            this.distanceFromListener = ((float) Math.sqrt(d1 * d1 + d2 * d2 + d3 * d3));
        }
    }

    private void calculateGain() {
        if (this.attModel == 2) {
            if (this.distanceFromListener <= 0.0F) {
                this.gain = 1.0F;
            } else if (this.distanceFromListener >= this.distOrRoll) {
                this.gain = 0.0F;
            } else {
                this.gain = (1.0F - this.distanceFromListener / this.distOrRoll);
            }
            if (this.gain > 1.0F) {
                this.gain = 1.0F;
            }
            if (this.gain < 0.0F) {
                this.gain = 0.0F;
            }
        } else {
            this.gain = 1.0F;
        }
    }

    private boolean checkALError() {
        switch () {
            case 0:
                return false;
            case 40961:
                errorMessage("Invalid name parameter.");
                return true;
            case 40962:
                errorMessage("Invalid parameter.");
                return true;
            case 40963:
                errorMessage("Invalid enumerated parameter value.");
                return true;
            case 40964:
                errorMessage("Illegal call.");
                return true;
            case 40965:
                errorMessage("Unable to allocate memory.");
                return true;
        }
        errorMessage("An unrecognized error occurred.");
        return true;
    }
}




