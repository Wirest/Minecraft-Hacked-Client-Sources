// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.util;

import java.nio.ShortBuffer;
import javax.sound.sampled.AudioFormat;
import java.io.IOException;
import java.nio.ByteOrder;
import javax.sound.sampled.AudioInputStream;
import java.io.ByteArrayInputStream;
import javax.sound.sampled.AudioSystem;
import org.lwjgl.LWJGLUtil;
import java.io.InputStream;
import java.io.BufferedInputStream;
import com.sun.media.sound.WaveFileReader;
import java.net.URL;
import java.nio.ByteBuffer;

public class WaveData
{
    public final ByteBuffer data;
    public final int format;
    public final int samplerate;
    
    private WaveData(final ByteBuffer data, final int format, final int samplerate) {
        this.data = data;
        this.format = format;
        this.samplerate = samplerate;
    }
    
    public void dispose() {
        this.data.clear();
    }
    
    public static WaveData create(final URL path) {
        try {
            final WaveFileReader wfr = new WaveFileReader();
            return create(wfr.getAudioInputStream(new BufferedInputStream(path.openStream())));
        }
        catch (Exception e) {
            LWJGLUtil.log("Unable to create from: " + path + ", " + e.getMessage());
            return null;
        }
    }
    
    public static WaveData create(final String path) {
        return create(Thread.currentThread().getContextClassLoader().getResource(path));
    }
    
    public static WaveData create(final InputStream is) {
        try {
            return create(AudioSystem.getAudioInputStream(is));
        }
        catch (Exception e) {
            LWJGLUtil.log("Unable to create from inputstream, " + e.getMessage());
            return null;
        }
    }
    
    public static WaveData create(final byte[] buffer) {
        try {
            return create(AudioSystem.getAudioInputStream(new BufferedInputStream(new ByteArrayInputStream(buffer))));
        }
        catch (Exception e) {
            LWJGLUtil.log("Unable to create from byte array, " + e.getMessage());
            return null;
        }
    }
    
    public static WaveData create(final ByteBuffer buffer) {
        try {
            byte[] bytes = null;
            if (buffer.hasArray()) {
                bytes = buffer.array();
            }
            else {
                bytes = new byte[buffer.capacity()];
                buffer.get(bytes);
            }
            return create(bytes);
        }
        catch (Exception e) {
            LWJGLUtil.log("Unable to create from ByteBuffer, " + e.getMessage());
            return null;
        }
    }
    
    public static WaveData create(final AudioInputStream ais) {
        final AudioFormat audioformat = ais.getFormat();
        int channels = 0;
        if (audioformat.getChannels() == 1) {
            if (audioformat.getSampleSizeInBits() == 8) {
                channels = 4352;
            }
            else if (audioformat.getSampleSizeInBits() == 16) {
                channels = 4353;
            }
            else {
                assert false : "Illegal sample size";
            }
        }
        else if (audioformat.getChannels() == 2) {
            if (audioformat.getSampleSizeInBits() == 8) {
                channels = 4354;
            }
            else if (audioformat.getSampleSizeInBits() == 16) {
                channels = 4355;
            }
            else {
                assert false : "Illegal sample size";
            }
        }
        else {
            assert false : "Only mono or stereo is supported";
        }
        ByteBuffer buffer = null;
        try {
            int available = ais.available();
            if (available <= 0) {
                available = ais.getFormat().getChannels() * (int)ais.getFrameLength() * ais.getFormat().getSampleSizeInBits() / 8;
            }
            final byte[] buf = new byte[ais.available()];
            for (int read = 0, total = 0; (read = ais.read(buf, total, buf.length - total)) != -1 && total < buf.length; total += read) {}
            buffer = convertAudioBytes(buf, audioformat.getSampleSizeInBits() == 16, audioformat.isBigEndian() ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN);
        }
        catch (IOException ioe) {
            return null;
        }
        final WaveData wavedata = new WaveData(buffer, channels, (int)audioformat.getSampleRate());
        try {
            ais.close();
        }
        catch (IOException ex) {}
        return wavedata;
    }
    
    private static ByteBuffer convertAudioBytes(final byte[] audio_bytes, final boolean two_bytes_data, final ByteOrder order) {
        final ByteBuffer dest = ByteBuffer.allocateDirect(audio_bytes.length);
        dest.order(ByteOrder.nativeOrder());
        final ByteBuffer src = ByteBuffer.wrap(audio_bytes);
        src.order(order);
        if (two_bytes_data) {
            final ShortBuffer dest_short = dest.asShortBuffer();
            final ShortBuffer src_short = src.asShortBuffer();
            while (src_short.hasRemaining()) {
                dest_short.put(src_short.get());
            }
        }
        else {
            while (src.hasRemaining()) {
                dest.put(src.get());
            }
        }
        dest.rewind();
        return dest;
    }
}
