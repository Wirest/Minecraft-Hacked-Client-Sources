// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.openal;

import java.nio.ShortBuffer;
import java.nio.ByteOrder;
import javax.sound.sampled.AudioFormat;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import java.io.ByteArrayInputStream;
import org.lwjgl.LWJGLUtil;
import java.io.InputStream;
import javax.sound.sampled.AudioSystem;
import java.io.BufferedInputStream;
import java.net.URL;
import java.nio.ByteBuffer;

public class AiffData
{
    public final ByteBuffer data;
    public final int format;
    public final int samplerate;
    
    private AiffData(final ByteBuffer data, final int format, final int samplerate) {
        this.data = data;
        this.format = format;
        this.samplerate = samplerate;
    }
    
    public void dispose() {
        this.data.clear();
    }
    
    public static AiffData create(final URL path) {
        try {
            return create(AudioSystem.getAudioInputStream(new BufferedInputStream(path.openStream())));
        }
        catch (Exception e) {
            LWJGLUtil.log("Unable to create from: " + path);
            e.printStackTrace();
            return null;
        }
    }
    
    public static AiffData create(final String path) {
        return create(AiffData.class.getClassLoader().getResource(path));
    }
    
    public static AiffData create(final InputStream is) {
        try {
            return create(AudioSystem.getAudioInputStream(is));
        }
        catch (Exception e) {
            LWJGLUtil.log("Unable to create from inputstream");
            e.printStackTrace();
            return null;
        }
    }
    
    public static AiffData create(final byte[] buffer) {
        try {
            return create(AudioSystem.getAudioInputStream(new BufferedInputStream(new ByteArrayInputStream(buffer))));
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static AiffData create(final ByteBuffer buffer) {
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
            e.printStackTrace();
            return null;
        }
    }
    
    public static AiffData create(final AudioInputStream ais) {
        final AudioFormat audioformat = ais.getFormat();
        int channels = 0;
        if (audioformat.getChannels() == 1) {
            if (audioformat.getSampleSizeInBits() == 8) {
                channels = 4352;
            }
            else {
                if (audioformat.getSampleSizeInBits() != 16) {
                    throw new RuntimeException("Illegal sample size");
                }
                channels = 4353;
            }
        }
        else {
            if (audioformat.getChannels() != 2) {
                throw new RuntimeException("Only mono or stereo is supported");
            }
            if (audioformat.getSampleSizeInBits() == 8) {
                channels = 4354;
            }
            else {
                if (audioformat.getSampleSizeInBits() != 16) {
                    throw new RuntimeException("Illegal sample size");
                }
                channels = 4355;
            }
        }
        final byte[] buf = new byte[audioformat.getChannels() * (int)ais.getFrameLength() * audioformat.getSampleSizeInBits() / 8];
        int read = 0;
        int total = 0;
        try {
            while ((read = ais.read(buf, total, buf.length - total)) != -1) {
                if (total >= buf.length) {
                    break;
                }
                total += read;
            }
        }
        catch (IOException ioe) {
            return null;
        }
        final ByteBuffer buffer = convertAudioBytes(audioformat, buf, audioformat.getSampleSizeInBits() == 16);
        final AiffData Aiffdata = new AiffData(buffer, channels, (int)audioformat.getSampleRate());
        try {
            ais.close();
        }
        catch (IOException ex) {}
        return Aiffdata;
    }
    
    private static ByteBuffer convertAudioBytes(final AudioFormat format, final byte[] audio_bytes, final boolean two_bytes_data) {
        final ByteBuffer dest = ByteBuffer.allocateDirect(audio_bytes.length);
        dest.order(ByteOrder.nativeOrder());
        final ByteBuffer src = ByteBuffer.wrap(audio_bytes);
        src.order(ByteOrder.BIG_ENDIAN);
        if (two_bytes_data) {
            final ShortBuffer dest_short = dest.asShortBuffer();
            final ShortBuffer src_short = src.asShortBuffer();
            while (src_short.hasRemaining()) {
                dest_short.put(src_short.get());
            }
        }
        else {
            while (src.hasRemaining()) {
                byte b = src.get();
                if (format.getEncoding() == AudioFormat.Encoding.PCM_SIGNED) {
                    b += 127;
                }
                dest.put(b);
            }
        }
        dest.rewind();
        return dest;
    }
}
