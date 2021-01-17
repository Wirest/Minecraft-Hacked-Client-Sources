// 
// Decompiled by Procyon v0.5.36
// 

package paulscode.sound.libraries;

import java.nio.ByteBuffer;
import org.lwjgl.BufferUtils;
import java.util.LinkedList;
import javax.sound.sampled.AudioFormat;
import org.lwjgl.openal.AL10;
import java.nio.IntBuffer;
import paulscode.sound.Channel;

public class ChannelLWJGLOpenAL extends Channel
{
    public IntBuffer ALSource;
    public int ALformat;
    public int sampleRate;
    public float millisPreviouslyPlayed;
    
    public ChannelLWJGLOpenAL(final int type, final IntBuffer src) {
        super(type);
        this.millisPreviouslyPlayed = 0.0f;
        this.libraryType = LibraryLWJGLOpenAL.class;
        this.ALSource = src;
    }
    
    @Override
    public void cleanup() {
        if (this.ALSource != null) {
            try {
                AL10.alSourceStop(this.ALSource);
                AL10.alGetError();
            }
            catch (Exception ex) {}
            try {
                AL10.alDeleteSources(this.ALSource);
                AL10.alGetError();
            }
            catch (Exception ex2) {}
            this.ALSource.clear();
        }
        this.ALSource = null;
        super.cleanup();
    }
    
    public boolean attachBuffer(final IntBuffer buf) {
        if (this.errorCheck(this.channelType != 0, "Sound buffers may only be attached to normal sources.")) {
            return false;
        }
        AL10.alSourcei(this.ALSource.get(0), 4105, buf.get(0));
        if (this.attachedSource != null && this.attachedSource.soundBuffer != null && this.attachedSource.soundBuffer.audioFormat != null) {
            this.setAudioFormat(this.attachedSource.soundBuffer.audioFormat);
        }
        return this.checkALError();
    }
    
    @Override
    public void setAudioFormat(final AudioFormat audioFormat) {
        int soundFormat = 0;
        if (audioFormat.getChannels() == 1) {
            if (audioFormat.getSampleSizeInBits() == 8) {
                soundFormat = 4352;
            }
            else {
                if (audioFormat.getSampleSizeInBits() != 16) {
                    this.errorMessage("Illegal sample size in method 'setAudioFormat'");
                    return;
                }
                soundFormat = 4353;
            }
        }
        else {
            if (audioFormat.getChannels() != 2) {
                this.errorMessage("Audio data neither mono nor stereo in method 'setAudioFormat'");
                return;
            }
            if (audioFormat.getSampleSizeInBits() == 8) {
                soundFormat = 4354;
            }
            else {
                if (audioFormat.getSampleSizeInBits() != 16) {
                    this.errorMessage("Illegal sample size in method 'setAudioFormat'");
                    return;
                }
                soundFormat = 4355;
            }
        }
        this.ALformat = soundFormat;
        this.sampleRate = (int)audioFormat.getSampleRate();
    }
    
    public void setFormat(final int format, final int rate) {
        this.ALformat = format;
        this.sampleRate = rate;
    }
    
    @Override
    public boolean preLoadBuffers(final LinkedList<byte[]> bufferList) {
        if (this.errorCheck(this.channelType != 1, "Buffers may only be queued for streaming sources.")) {
            return false;
        }
        if (this.errorCheck(bufferList == null, "Buffer List null in method 'preLoadBuffers'")) {
            return false;
        }
        final boolean playing = this.playing();
        if (playing) {
            AL10.alSourceStop(this.ALSource.get(0));
            this.checkALError();
        }
        final int processed = AL10.alGetSourcei(this.ALSource.get(0), 4118);
        if (processed > 0) {
            final IntBuffer streamBuffers = BufferUtils.createIntBuffer(processed);
            AL10.alGenBuffers(streamBuffers);
            if (this.errorCheck(this.checkALError(), "Error clearing stream buffers in method 'preLoadBuffers'")) {
                return false;
            }
            AL10.alSourceUnqueueBuffers(this.ALSource.get(0), streamBuffers);
            if (this.errorCheck(this.checkALError(), "Error unqueuing stream buffers in method 'preLoadBuffers'")) {
                return false;
            }
        }
        if (playing) {
            AL10.alSourcePlay(this.ALSource.get(0));
            this.checkALError();
        }
        final IntBuffer streamBuffers = BufferUtils.createIntBuffer(bufferList.size());
        AL10.alGenBuffers(streamBuffers);
        if (this.errorCheck(this.checkALError(), "Error generating stream buffers in method 'preLoadBuffers'")) {
            return false;
        }
        ByteBuffer byteBuffer = null;
        for (int i = 0; i < bufferList.size(); ++i) {
            byteBuffer = (ByteBuffer)BufferUtils.createByteBuffer(bufferList.get(i).length).put(bufferList.get(i)).flip();
            try {
                AL10.alBufferData(streamBuffers.get(i), this.ALformat, byteBuffer, this.sampleRate);
            }
            catch (Exception e) {
                this.errorMessage("Error creating buffers in method 'preLoadBuffers'");
                this.printStackTrace(e);
                return false;
            }
            if (this.errorCheck(this.checkALError(), "Error creating buffers in method 'preLoadBuffers'")) {
                return false;
            }
        }
        try {
            AL10.alSourceQueueBuffers(this.ALSource.get(0), streamBuffers);
        }
        catch (Exception e2) {
            this.errorMessage("Error queuing buffers in method 'preLoadBuffers'");
            this.printStackTrace(e2);
            return false;
        }
        if (this.errorCheck(this.checkALError(), "Error queuing buffers in method 'preLoadBuffers'")) {
            return false;
        }
        AL10.alSourcePlay(this.ALSource.get(0));
        return !this.errorCheck(this.checkALError(), "Error playing source in method 'preLoadBuffers'");
    }
    
    @Override
    public boolean queueBuffer(final byte[] buffer) {
        if (this.errorCheck(this.channelType != 1, "Buffers may only be queued for streaming sources.")) {
            return false;
        }
        final ByteBuffer byteBuffer = (ByteBuffer)BufferUtils.createByteBuffer(buffer.length).put(buffer).flip();
        final IntBuffer intBuffer = BufferUtils.createIntBuffer(1);
        AL10.alSourceUnqueueBuffers(this.ALSource.get(0), intBuffer);
        if (this.checkALError()) {
            return false;
        }
        if (AL10.alIsBuffer(intBuffer.get(0))) {
            this.millisPreviouslyPlayed += this.millisInBuffer(intBuffer.get(0));
        }
        this.checkALError();
        AL10.alBufferData(intBuffer.get(0), this.ALformat, byteBuffer, this.sampleRate);
        if (this.checkALError()) {
            return false;
        }
        AL10.alSourceQueueBuffers(this.ALSource.get(0), intBuffer);
        return !this.checkALError();
    }
    
    @Override
    public int feedRawAudioData(final byte[] buffer) {
        if (this.errorCheck(this.channelType != 1, "Raw audio data can only be fed to streaming sources.")) {
            return -1;
        }
        final ByteBuffer byteBuffer = (ByteBuffer)BufferUtils.createByteBuffer(buffer.length).put(buffer).flip();
        final int processed = AL10.alGetSourcei(this.ALSource.get(0), 4118);
        IntBuffer intBuffer;
        if (processed > 0) {
            intBuffer = BufferUtils.createIntBuffer(processed);
            AL10.alGenBuffers(intBuffer);
            if (this.errorCheck(this.checkALError(), "Error clearing stream buffers in method 'feedRawAudioData'")) {
                return -1;
            }
            AL10.alSourceUnqueueBuffers(this.ALSource.get(0), intBuffer);
            if (this.errorCheck(this.checkALError(), "Error unqueuing stream buffers in method 'feedRawAudioData'")) {
                return -1;
            }
            if (AL10.alIsBuffer(intBuffer.get(0))) {
                this.millisPreviouslyPlayed += this.millisInBuffer(intBuffer.get(0));
            }
            this.checkALError();
        }
        else {
            intBuffer = BufferUtils.createIntBuffer(1);
            AL10.alGenBuffers(intBuffer);
            if (this.errorCheck(this.checkALError(), "Error generating stream buffers in method 'preLoadBuffers'")) {
                return -1;
            }
        }
        AL10.alBufferData(intBuffer.get(0), this.ALformat, byteBuffer, this.sampleRate);
        if (this.checkALError()) {
            return -1;
        }
        AL10.alSourceQueueBuffers(this.ALSource.get(0), intBuffer);
        if (this.checkALError()) {
            return -1;
        }
        if (this.attachedSource != null && this.attachedSource.channel == this && this.attachedSource.active() && !this.playing()) {
            AL10.alSourcePlay(this.ALSource.get(0));
            this.checkALError();
        }
        return processed;
    }
    
    public float millisInBuffer(final int alBufferi) {
        return AL10.alGetBufferi(alBufferi, 8196) / (float)AL10.alGetBufferi(alBufferi, 8195) / (AL10.alGetBufferi(alBufferi, 8194) / 8.0f) / this.sampleRate * 1000.0f;
    }
    
    @Override
    public float millisecondsPlayed() {
        float offset = (float)AL10.alGetSourcei(this.ALSource.get(0), 4134);
        float bytesPerFrame = 1.0f;
        switch (this.ALformat) {
            case 4352: {
                bytesPerFrame = 1.0f;
                break;
            }
            case 4353: {
                bytesPerFrame = 2.0f;
                break;
            }
            case 4354: {
                bytesPerFrame = 2.0f;
                break;
            }
            case 4355: {
                bytesPerFrame = 4.0f;
                break;
            }
        }
        offset = offset / bytesPerFrame / this.sampleRate * 1000.0f;
        if (this.channelType == 1) {
            offset += this.millisPreviouslyPlayed;
        }
        return offset;
    }
    
    @Override
    public int buffersProcessed() {
        if (this.channelType != 1) {
            return 0;
        }
        final int processed = AL10.alGetSourcei(this.ALSource.get(0), 4118);
        if (this.checkALError()) {
            return 0;
        }
        return processed;
    }
    
    @Override
    public void flush() {
        if (this.channelType != 1) {
            return;
        }
        int queued = AL10.alGetSourcei(this.ALSource.get(0), 4117);
        if (this.checkALError()) {
            return;
        }
        final IntBuffer intBuffer = BufferUtils.createIntBuffer(1);
        while (queued > 0) {
            try {
                AL10.alSourceUnqueueBuffers(this.ALSource.get(0), intBuffer);
            }
            catch (Exception e) {
                return;
            }
            if (this.checkALError()) {
                return;
            }
            --queued;
        }
        this.millisPreviouslyPlayed = 0.0f;
    }
    
    @Override
    public void close() {
        try {
            AL10.alSourceStop(this.ALSource.get(0));
            AL10.alGetError();
        }
        catch (Exception ex) {}
        if (this.channelType == 1) {
            this.flush();
        }
    }
    
    @Override
    public void play() {
        AL10.alSourcePlay(this.ALSource.get(0));
        this.checkALError();
    }
    
    @Override
    public void pause() {
        AL10.alSourcePause(this.ALSource.get(0));
        this.checkALError();
    }
    
    @Override
    public void stop() {
        AL10.alSourceStop(this.ALSource.get(0));
        if (!this.checkALError()) {
            this.millisPreviouslyPlayed = 0.0f;
        }
    }
    
    @Override
    public void rewind() {
        if (this.channelType == 1) {
            return;
        }
        AL10.alSourceRewind(this.ALSource.get(0));
        if (!this.checkALError()) {
            this.millisPreviouslyPlayed = 0.0f;
        }
    }
    
    @Override
    public boolean playing() {
        final int state = AL10.alGetSourcei(this.ALSource.get(0), 4112);
        return !this.checkALError() && state == 4114;
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
