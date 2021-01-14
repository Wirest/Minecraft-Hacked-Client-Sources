package paulscode.sound.libraries;

import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;
import paulscode.sound.Channel;

import javax.sound.sampled.AudioFormat;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.LinkedList;

public class ChannelLWJGLOpenAL
        extends Channel {
    public IntBuffer ALSource;
    public int ALformat;
    public int sampleRate;
    public float millisPreviouslyPlayed = 0.0F;

    public ChannelLWJGLOpenAL(int paramInt, IntBuffer paramIntBuffer) {
        super(paramInt);
        this.libraryType = LibraryLWJGLOpenAL.class;
        this.ALSource = paramIntBuffer;
    }

    public void cleanup() {
        if (this.ALSource != null) {
            try {
                AL10.alSourceStop(this.ALSource);
                AL10.alGetError();
            } catch (Exception localException1) {
            }
            try {
                AL10.alDeleteSources(this.ALSource);
                AL10.alGetError();
            } catch (Exception localException2) {
            }
            this.ALSource.clear();
        }
        this.ALSource = null;
        super.cleanup();
    }

    public boolean attachBuffer(IntBuffer paramIntBuffer) {
        if (errorCheck(this.channelType != 0, "Sound buffers may only be attached to normal sources.")) {
            return false;
        }
        AL10.alSourcei(this.ALSource.get(0), 4105, paramIntBuffer.get(0));
        if ((this.attachedSource != null) && (this.attachedSource.soundBuffer != null) && (this.attachedSource.soundBuffer.audioFormat != null)) {
            setAudioFormat(this.attachedSource.soundBuffer.audioFormat);
        }
        return checkALError();
    }

    public void setAudioFormat(AudioFormat paramAudioFormat) {
        int i = 0;
        if (paramAudioFormat.getChannels() == 1) {
            if (paramAudioFormat.getSampleSizeInBits() == 8) {
                i = 4352;
            } else if (paramAudioFormat.getSampleSizeInBits() == 16) {
                i = 4353;
            } else {
                errorMessage("Illegal sample size in method 'setAudioFormat'");
            }
        } else if (paramAudioFormat.getChannels() == 2) {
            if (paramAudioFormat.getSampleSizeInBits() == 8) {
                i = 4354;
            } else if (paramAudioFormat.getSampleSizeInBits() == 16) {
                i = 4355;
            } else {
                errorMessage("Illegal sample size in method 'setAudioFormat'");
            }
        } else {
            errorMessage("Audio data neither mono nor stereo in method 'setAudioFormat'");
            return;
        }
        this.ALformat = i;
        this.sampleRate = ((int) paramAudioFormat.getSampleRate());
    }

    public void setFormat(int paramInt1, int paramInt2) {
        this.ALformat = paramInt1;
        this.sampleRate = paramInt2;
    }

    public boolean preLoadBuffers(LinkedList<byte[]> paramLinkedList) {
        if (errorCheck(this.channelType != 1, "Buffers may only be queued for streaming sources.")) {
            return false;
        }
        if (errorCheck(paramLinkedList == null, "Buffer List null in method 'preLoadBuffers'")) {
            return false;
        }
        boolean bool = playing();
        if (bool) {
            AL10.alSourceStop(this.ALSource.get(0));
            checkALError();
        }
        int i = AL10.alGetSourcei(this.ALSource.get(0), 4118);
        if (i > 0) {
            localIntBuffer = BufferUtils.createIntBuffer(i);
            AL10.alGenBuffers(localIntBuffer);
            if (errorCheck(checkALError(), "Error clearing stream buffers in method 'preLoadBuffers'")) {
                return false;
            }
            AL10.alSourceUnqueueBuffers(this.ALSource.get(0), localIntBuffer);
            if (errorCheck(checkALError(), "Error unqueuing stream buffers in method 'preLoadBuffers'")) {
                return false;
            }
        }
        if (bool) {
            AL10.alSourcePlay(this.ALSource.get(0));
            checkALError();
        }
        IntBuffer localIntBuffer = BufferUtils.createIntBuffer(paramLinkedList.size());
        AL10.alGenBuffers(localIntBuffer);
        if (errorCheck(checkALError(), "Error generating stream buffers in method 'preLoadBuffers'")) {
            return false;
        }
        ByteBuffer localByteBuffer = null;
        for (int j = 0; j < paramLinkedList.size(); j++) {
            localByteBuffer = (ByteBuffer) BufferUtils.createByteBuffer(((byte[]) paramLinkedList.get(j)).length).put((byte[]) paramLinkedList.get(j)).flip();
            try {
                AL10.alBufferData(localIntBuffer.get(j), this.ALformat, localByteBuffer, this.sampleRate);
            } catch (Exception localException2) {
                errorMessage("Error creating buffers in method 'preLoadBuffers'");
                printStackTrace(localException2);
                return false;
            }
            if (errorCheck(checkALError(), "Error creating buffers in method 'preLoadBuffers'")) {
                return false;
            }
        }
        try {
            AL10.alSourceQueueBuffers(this.ALSource.get(0), localIntBuffer);
        } catch (Exception localException1) {
            errorMessage("Error queuing buffers in method 'preLoadBuffers'");
            printStackTrace(localException1);
            return false;
        }
        if (errorCheck(checkALError(), "Error queuing buffers in method 'preLoadBuffers'")) {
            return false;
        }
        AL10.alSourcePlay(this.ALSource.get(0));
        return !errorCheck(checkALError(), "Error playing source in method 'preLoadBuffers'");
    }

    public boolean queueBuffer(byte[] paramArrayOfByte) {
        if (errorCheck(this.channelType != 1, "Buffers may only be queued for streaming sources.")) {
            return false;
        }
        ByteBuffer localByteBuffer = (ByteBuffer) BufferUtils.createByteBuffer(paramArrayOfByte.length).put(paramArrayOfByte).flip();
        IntBuffer localIntBuffer = BufferUtils.createIntBuffer(1);
        AL10.alSourceUnqueueBuffers(this.ALSource.get(0), localIntBuffer);
        if (checkALError()) {
            return false;
        }
        if (AL10.alIsBuffer(localIntBuffer.get(0))) {
            this.millisPreviouslyPlayed += millisInBuffer(localIntBuffer.get(0));
        }
        checkALError();
        AL10.alBufferData(localIntBuffer.get(0), this.ALformat, localByteBuffer, this.sampleRate);
        if (checkALError()) {
            return false;
        }
        AL10.alSourceQueueBuffers(this.ALSource.get(0), localIntBuffer);
        return !checkALError();
    }

    public int feedRawAudioData(byte[] paramArrayOfByte) {
        if (errorCheck(this.channelType != 1, "Raw audio data can only be fed to streaming sources.")) {
            return -1;
        }
        ByteBuffer localByteBuffer = (ByteBuffer) BufferUtils.createByteBuffer(paramArrayOfByte.length).put(paramArrayOfByte).flip();
        int i = AL10.alGetSourcei(this.ALSource.get(0), 4118);
        IntBuffer localIntBuffer;
        if (i > 0) {
            localIntBuffer = BufferUtils.createIntBuffer(i);
            AL10.alGenBuffers(localIntBuffer);
            if (errorCheck(checkALError(), "Error clearing stream buffers in method 'feedRawAudioData'")) {
                return -1;
            }
            AL10.alSourceUnqueueBuffers(this.ALSource.get(0), localIntBuffer);
            if (errorCheck(checkALError(), "Error unqueuing stream buffers in method 'feedRawAudioData'")) {
                return -1;
            }
            if (AL10.alIsBuffer(localIntBuffer.get(0))) {
                this.millisPreviouslyPlayed += millisInBuffer(localIntBuffer.get(0));
            }
            checkALError();
        } else {
            localIntBuffer = BufferUtils.createIntBuffer(1);
            AL10.alGenBuffers(localIntBuffer);
            if (errorCheck(checkALError(), "Error generating stream buffers in method 'preLoadBuffers'")) {
                return -1;
            }
        }
        AL10.alBufferData(localIntBuffer.get(0), this.ALformat, localByteBuffer, this.sampleRate);
        if (checkALError()) {
            return -1;
        }
        AL10.alSourceQueueBuffers(this.ALSource.get(0), localIntBuffer);
        if (checkALError()) {
            return -1;
        }
        if ((this.attachedSource != null) && (this.attachedSource.channel == this) && (this.attachedSource.active()) && (!playing())) {
            AL10.alSourcePlay(this.ALSource.get(0));
            checkALError();
        }
        return i;
    }

    public float millisInBuffer(int paramInt) {
        return AL10.alGetBufferi(paramInt, 8196) / AL10.alGetBufferi(paramInt, 8195) / (AL10.alGetBufferi(paramInt, 8194) / 8.0F) / this.sampleRate * 1000.0F;
    }

    public float millisecondsPlayed() {
        float f1 = AL10.alGetSourcei(this.ALSource.get(0), 4134);
        float f2 = 1.0F;
        switch (this.ALformat) {
            case 4352:
                f2 = 1.0F;
                break;
            case 4353:
                f2 = 2.0F;
                break;
            case 4354:
                f2 = 2.0F;
                break;
            case 4355:
                f2 = 4.0F;
                break;
        }
        f1 = f1 / f2 / this.sampleRate * 1000.0F;
        if (this.channelType == 1) {
            f1 += this.millisPreviouslyPlayed;
        }
        return f1;
    }

    public int buffersProcessed() {
        if (this.channelType != 1) {
            return 0;
        }
        int i = AL10.alGetSourcei(this.ALSource.get(0), 4118);
        if (checkALError()) {
            return 0;
        }
        return i;
    }

    public void flush() {
        if (this.channelType != 1) {
            return;
        }
        int i = AL10.alGetSourcei(this.ALSource.get(0), 4117);
        if (checkALError()) {
            return;
        }
        IntBuffer localIntBuffer = BufferUtils.createIntBuffer(1);
        while (i > 0) {
            try {
                AL10.alSourceUnqueueBuffers(this.ALSource.get(0), localIntBuffer);
            } catch (Exception localException) {
                return;
            }
            if (checkALError()) {
                return;
            }
            i--;
        }
        this.millisPreviouslyPlayed = 0.0F;
    }

    public void close() {
        try {
            AL10.alSourceStop(this.ALSource.get(0));
            AL10.alGetError();
        } catch (Exception localException) {
        }
        if (this.channelType == 1) {
            flush();
        }
    }

    public void play() {
        AL10.alSourcePlay(this.ALSource.get(0));
        checkALError();
    }

    public void pause() {
        AL10.alSourcePause(this.ALSource.get(0));
        checkALError();
    }

    public void stop() {
        AL10.alSourceStop(this.ALSource.get(0));
        if (!checkALError()) {
            this.millisPreviouslyPlayed = 0.0F;
        }
    }

    public void rewind() {
        if (this.channelType == 1) {
            return;
        }
        AL10.alSourceRewind(this.ALSource.get(0));
        if (!checkALError()) {
            this.millisPreviouslyPlayed = 0.0F;
        }
    }

    public boolean playing() {
        int i = AL10.alGetSourcei(this.ALSource.get(0), 4112);
        if (checkALError()) {
            return false;
        }
        return i == 4114;
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




