// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.openal;

import org.lwjgl.openal.OpenALException;
import org.newdawn.slick.util.Log;
import java.io.IOException;
import org.newdawn.slick.util.ResourceLoader;
import org.lwjgl.openal.AL10;
import org.lwjgl.BufferUtils;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class OpenALStreamPlayer
{
    public static final int BUFFER_COUNT = 3;
    private static final int sectionSize = 81920;
    private byte[] buffer;
    private IntBuffer bufferNames;
    private ByteBuffer bufferData;
    private IntBuffer unqueued;
    private int source;
    private int remainingBufferCount;
    private boolean loop;
    private boolean done;
    private AudioInputStream audio;
    private String ref;
    private URL url;
    private float pitch;
    private float positionOffset;
    
    public OpenALStreamPlayer(final int source, final String ref) {
        this.buffer = new byte[81920];
        this.bufferData = BufferUtils.createByteBuffer(81920);
        this.unqueued = BufferUtils.createIntBuffer(1);
        this.done = true;
        this.source = source;
        this.ref = ref;
        AL10.alGenBuffers(this.bufferNames = BufferUtils.createIntBuffer(3));
    }
    
    public OpenALStreamPlayer(final int source, final URL url) {
        this.buffer = new byte[81920];
        this.bufferData = BufferUtils.createByteBuffer(81920);
        this.unqueued = BufferUtils.createIntBuffer(1);
        this.done = true;
        this.source = source;
        this.url = url;
        AL10.alGenBuffers(this.bufferNames = BufferUtils.createIntBuffer(3));
    }
    
    private void initStreams() throws IOException {
        if (this.audio != null) {
            this.audio.close();
        }
        OggInputStream audio;
        if (this.url != null) {
            audio = new OggInputStream(this.url.openStream());
        }
        else {
            audio = new OggInputStream(ResourceLoader.getResourceAsStream(this.ref));
        }
        this.audio = audio;
        this.positionOffset = 0.0f;
    }
    
    public String getSource() {
        return (this.url == null) ? this.ref : this.url.toString();
    }
    
    private void removeBuffers() {
        final IntBuffer buffer = BufferUtils.createIntBuffer(1);
        for (int queued = AL10.alGetSourcei(this.source, 4117); queued > 0; --queued) {
            AL10.alSourceUnqueueBuffers(this.source, buffer);
        }
    }
    
    public void play(final boolean loop) throws IOException {
        this.loop = loop;
        this.initStreams();
        this.done = false;
        AL10.alSourceStop(this.source);
        this.removeBuffers();
        this.startPlayback();
    }
    
    public void setup(final float pitch) {
        this.pitch = pitch;
    }
    
    public boolean done() {
        return this.done;
    }
    
    public void update() {
        if (this.done) {
            return;
        }
        final float sampleRate = (float)this.audio.getRate();
        float sampleSize;
        if (this.audio.getChannels() > 1) {
            sampleSize = 4.0f;
        }
        else {
            sampleSize = 2.0f;
        }
        for (int processed = AL10.alGetSourcei(this.source, 4118); processed > 0; --processed) {
            this.unqueued.clear();
            AL10.alSourceUnqueueBuffers(this.source, this.unqueued);
            final int bufferIndex = this.unqueued.get(0);
            final float bufferLength = AL10.alGetBufferi(bufferIndex, 8196) / sampleSize / sampleRate;
            this.positionOffset += bufferLength;
            if (this.stream(bufferIndex)) {
                AL10.alSourceQueueBuffers(this.source, this.unqueued);
            }
            else {
                --this.remainingBufferCount;
                if (this.remainingBufferCount == 0) {
                    this.done = true;
                }
            }
        }
        final int state = AL10.alGetSourcei(this.source, 4112);
        if (state != 4114) {
            AL10.alSourcePlay(this.source);
        }
    }
    
    public boolean stream(final int bufferId) {
        try {
            final int count = this.audio.read(this.buffer);
            if (count != -1) {
                this.bufferData.clear();
                this.bufferData.put(this.buffer, 0, count);
                this.bufferData.flip();
                final int format = (this.audio.getChannels() > 1) ? 4355 : 4353;
                try {
                    AL10.alBufferData(bufferId, format, this.bufferData, this.audio.getRate());
                    return true;
                }
                catch (OpenALException e) {
                    Log.error("Failed to loop buffer: " + bufferId + " " + format + " " + count + " " + this.audio.getRate(), e);
                    return false;
                }
            }
            if (!this.loop) {
                this.done = true;
                return false;
            }
            this.initStreams();
            this.stream(bufferId);
            return true;
        }
        catch (IOException e2) {
            Log.error(e2);
            return false;
        }
    }
    
    public boolean setPosition(final float position) {
        try {
            if (this.getPosition() > position) {
                this.initStreams();
            }
            final float sampleRate = (float)this.audio.getRate();
            float sampleSize;
            if (this.audio.getChannels() > 1) {
                sampleSize = 4.0f;
            }
            else {
                sampleSize = 2.0f;
            }
            while (this.positionOffset < position) {
                final int count = this.audio.read(this.buffer);
                if (count == -1) {
                    if (this.loop) {
                        this.initStreams();
                    }
                    else {
                        this.done = true;
                    }
                    return false;
                }
                final float bufferLength = count / sampleSize / sampleRate;
                this.positionOffset += bufferLength;
            }
            this.startPlayback();
            return true;
        }
        catch (IOException e) {
            Log.error(e);
            return false;
        }
    }
    
    private void startPlayback() {
        AL10.alSourcei(this.source, 4103, 0);
        AL10.alSourcef(this.source, 4099, this.pitch);
        this.remainingBufferCount = 3;
        for (int i = 0; i < 3; ++i) {
            this.stream(this.bufferNames.get(i));
        }
        AL10.alSourceQueueBuffers(this.source, this.bufferNames);
        AL10.alSourcePlay(this.source);
    }
    
    public float getPosition() {
        return this.positionOffset + AL10.alGetSourcef(this.source, 4132);
    }
}
