// 
// Decompiled by Procyon v0.5.36
// 

package paulscode.sound.codecs;

import java.nio.ShortBuffer;
import java.nio.ByteOrder;
import java.nio.ByteBuffer;
import javax.sound.sampled.AudioFormat;
import paulscode.sound.SoundBuffer;
import java.io.IOException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.InputStream;
import javax.sound.sampled.AudioSystem;
import java.io.BufferedInputStream;
import java.net.URL;
import paulscode.sound.SoundSystemConfig;
import paulscode.sound.SoundSystemLogger;
import javax.sound.sampled.AudioInputStream;
import paulscode.sound.ICodec;

public class CodecWav implements ICodec
{
    private static final boolean GET = false;
    private static final boolean SET = true;
    private static final boolean XXX = false;
    private boolean endOfStream;
    private boolean initialized;
    private AudioInputStream myAudioInputStream;
    private SoundSystemLogger logger;
    
    public void reverseByteOrder(final boolean b) {
    }
    
    public CodecWav() {
        this.endOfStream = false;
        this.initialized = false;
        this.myAudioInputStream = null;
        this.logger = SoundSystemConfig.getLogger();
    }
    
    public boolean initialize(final URL url) {
        this.initialized(true, false);
        this.cleanup();
        if (url == null) {
            this.errorMessage("url null in method 'initialize'");
            this.cleanup();
            return false;
        }
        try {
            this.myAudioInputStream = AudioSystem.getAudioInputStream(new BufferedInputStream(url.openStream()));
        }
        catch (UnsupportedAudioFileException uafe) {
            this.errorMessage("Unsupported audio format in method 'initialize'");
            this.printStackTrace(uafe);
            return false;
        }
        catch (IOException ioe) {
            this.errorMessage("Error setting up audio input stream in method 'initialize'");
            this.printStackTrace(ioe);
            return false;
        }
        this.endOfStream(true, false);
        this.initialized(true, true);
        return true;
    }
    
    public boolean initialized() {
        return this.initialized(false, false);
    }
    
    public SoundBuffer read() {
        if (this.myAudioInputStream == null) {
            return null;
        }
        final AudioFormat audioFormat = this.myAudioInputStream.getFormat();
        if (audioFormat == null) {
            this.errorMessage("Audio Format null in method 'read'");
            return null;
        }
        int bytesRead = 0;
        int cnt = 0;
        byte[] streamBuffer = new byte[SoundSystemConfig.getStreamingBufferSize()];
        try {
            while (!this.endOfStream(false, false) && bytesRead < streamBuffer.length) {
                if ((cnt = this.myAudioInputStream.read(streamBuffer, bytesRead, streamBuffer.length - bytesRead)) <= 0) {
                    this.endOfStream(true, true);
                    break;
                }
                bytesRead += cnt;
            }
        }
        catch (IOException ioe) {
            this.endOfStream(true, true);
            return null;
        }
        if (bytesRead <= 0) {
            return null;
        }
        if (bytesRead < streamBuffer.length) {
            streamBuffer = trimArray(streamBuffer, bytesRead);
        }
        final byte[] data = convertAudioBytes(streamBuffer, audioFormat.getSampleSizeInBits() == 16);
        final SoundBuffer buffer = new SoundBuffer(data, audioFormat);
        return buffer;
    }
    
    public SoundBuffer readAll() {
        if (this.myAudioInputStream == null) {
            this.errorMessage("Audio input stream null in method 'readAll'");
            return null;
        }
        final AudioFormat myAudioFormat = this.myAudioInputStream.getFormat();
        if (myAudioFormat == null) {
            this.errorMessage("Audio Format null in method 'readAll'");
            return null;
        }
        byte[] fullBuffer = null;
        final int fileSize = myAudioFormat.getChannels() * (int)this.myAudioInputStream.getFrameLength() * myAudioFormat.getSampleSizeInBits() / 8;
        if (fileSize > 0) {
            fullBuffer = new byte[myAudioFormat.getChannels() * (int)this.myAudioInputStream.getFrameLength() * myAudioFormat.getSampleSizeInBits() / 8];
            int read = 0;
            int total = 0;
            try {
                while ((read = this.myAudioInputStream.read(fullBuffer, total, fullBuffer.length - total)) != -1 && total < fullBuffer.length) {
                    total += read;
                }
            }
            catch (IOException e) {
                this.errorMessage("Exception thrown while reading from the AudioInputStream (location #1).");
                this.printStackTrace(e);
                return null;
            }
        }
        else {
            int totalBytes = 0;
            int bytesRead = 0;
            int cnt = 0;
            byte[] smallBuffer;
            for (smallBuffer = null, smallBuffer = new byte[SoundSystemConfig.getFileChunkSize()]; !this.endOfStream(false, false) && totalBytes < SoundSystemConfig.getMaxFileSize(); totalBytes += bytesRead, fullBuffer = appendByteArrays(fullBuffer, smallBuffer, bytesRead)) {
                bytesRead = 0;
                cnt = 0;
                try {
                    while (bytesRead < smallBuffer.length) {
                        if ((cnt = this.myAudioInputStream.read(smallBuffer, bytesRead, smallBuffer.length - bytesRead)) <= 0) {
                            this.endOfStream(true, true);
                            break;
                        }
                        bytesRead += cnt;
                    }
                }
                catch (IOException e2) {
                    this.errorMessage("Exception thrown while reading from the AudioInputStream (location #2).");
                    this.printStackTrace(e2);
                    return null;
                }
            }
        }
        final byte[] data = convertAudioBytes(fullBuffer, myAudioFormat.getSampleSizeInBits() == 16);
        final SoundBuffer soundBuffer = new SoundBuffer(data, myAudioFormat);
        try {
            this.myAudioInputStream.close();
        }
        catch (IOException ex) {}
        return soundBuffer;
    }
    
    public boolean endOfStream() {
        return this.endOfStream(false, false);
    }
    
    public void cleanup() {
        if (this.myAudioInputStream != null) {
            try {
                this.myAudioInputStream.close();
            }
            catch (Exception ex) {}
        }
        this.myAudioInputStream = null;
    }
    
    public AudioFormat getAudioFormat() {
        if (this.myAudioInputStream == null) {
            return null;
        }
        return this.myAudioInputStream.getFormat();
    }
    
    private synchronized boolean initialized(final boolean action, final boolean value) {
        if (action) {
            this.initialized = value;
        }
        return this.initialized;
    }
    
    private synchronized boolean endOfStream(final boolean action, final boolean value) {
        if (action) {
            this.endOfStream = value;
        }
        return this.endOfStream;
    }
    
    private static byte[] trimArray(final byte[] array, final int maxLength) {
        byte[] trimmedArray = null;
        if (array != null && array.length > maxLength) {
            trimmedArray = new byte[maxLength];
            System.arraycopy(array, 0, trimmedArray, 0, maxLength);
        }
        return trimmedArray;
    }
    
    private static byte[] convertAudioBytes(final byte[] audio_bytes, final boolean two_bytes_data) {
        final ByteBuffer dest = ByteBuffer.allocateDirect(audio_bytes.length);
        dest.order(ByteOrder.nativeOrder());
        final ByteBuffer src = ByteBuffer.wrap(audio_bytes);
        src.order(ByteOrder.LITTLE_ENDIAN);
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
        if (!dest.hasArray()) {
            final byte[] arrayBackedBuffer = new byte[dest.capacity()];
            dest.get(arrayBackedBuffer);
            dest.clear();
            return arrayBackedBuffer;
        }
        return dest.array();
    }
    
    private static byte[] appendByteArrays(byte[] arrayOne, byte[] arrayTwo, final int length) {
        if (arrayOne == null && arrayTwo == null) {
            return null;
        }
        byte[] newArray;
        if (arrayOne == null) {
            newArray = new byte[length];
            System.arraycopy(arrayTwo, 0, newArray, 0, length);
            arrayTwo = null;
        }
        else if (arrayTwo == null) {
            newArray = new byte[arrayOne.length];
            System.arraycopy(arrayOne, 0, newArray, 0, arrayOne.length);
            arrayOne = null;
        }
        else {
            newArray = new byte[arrayOne.length + length];
            System.arraycopy(arrayOne, 0, newArray, 0, arrayOne.length);
            System.arraycopy(arrayTwo, 0, newArray, arrayOne.length, length);
            arrayOne = null;
            arrayTwo = null;
        }
        return newArray;
    }
    
    private void errorMessage(final String message) {
        this.logger.errorMessage("CodecWav", message, 0);
    }
    
    private void printStackTrace(final Exception e) {
        this.logger.printStackTrace(e, 1);
    }
}
