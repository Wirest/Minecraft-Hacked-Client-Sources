package paulscode.sound.codecs;

import com.jcraft.jogg.Packet;
import com.jcraft.jogg.Page;
import com.jcraft.jogg.StreamState;
import com.jcraft.jogg.SyncState;
import com.jcraft.jorbis.Block;
import com.jcraft.jorbis.Comment;
import com.jcraft.jorbis.DspState;
import com.jcraft.jorbis.Info;
import paulscode.sound.ICodec;
import paulscode.sound.SoundBuffer;
import paulscode.sound.SoundSystemConfig;
import paulscode.sound.SoundSystemLogger;

import javax.sound.sampled.AudioFormat;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownServiceException;

public class CodecJOrbis
        implements ICodec {
    private static final boolean GET = false;
    private static final boolean SET = true;
    private static final boolean XXX = false;
    private URL url;
    private URLConnection urlConnection = null;
    private InputStream inputStream;
    private AudioFormat audioFormat;
    private boolean endOfStream = false;
    private boolean initialized = false;
    private byte[] buffer = null;
    private int bufferSize;
    private int count = 0;
    private int index = 0;
    private int convertedBufferSize;
    private byte[] convertedBuffer = null;
    private float[][][] pcmInfo;
    private int[] pcmIndex;
    private Packet joggPacket = new Packet();
    private Page joggPage = new Page();
    private StreamState joggStreamState = new StreamState();
    private SyncState joggSyncState = new SyncState();
    private DspState jorbisDspState = new DspState();
    private Block jorbisBlock = new Block(this.jorbisDspState);
    private Comment jorbisComment = new Comment();
    private Info jorbisInfo = new Info();
    private SoundSystemLogger logger = SoundSystemConfig.getLogger();

    private static byte[] trimArray(byte[] paramArrayOfByte, int paramInt) {
        byte[] arrayOfByte = null;
        if ((paramArrayOfByte != null) && (paramArrayOfByte.length > paramInt)) {
            arrayOfByte = new byte[paramInt];
            System.arraycopy(paramArrayOfByte, 0, arrayOfByte, 0, paramInt);
        }
        return arrayOfByte;
    }

    private static byte[] appendByteArrays(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int paramInt) {
        int i = paramInt;
        if ((paramArrayOfByte2 == null) || (paramArrayOfByte2.length == 0)) {
            i = 0;
        } else if (paramArrayOfByte2.length < paramInt) {
            i = paramArrayOfByte2.length;
        }
        if ((paramArrayOfByte1 == null) && ((paramArrayOfByte2 == null) || (i <= 0))) {
            return null;
        }
        byte[] arrayOfByte;
        if (paramArrayOfByte1 == null) {
            arrayOfByte = new byte[i];
            System.arraycopy(paramArrayOfByte2, 0, arrayOfByte, 0, i);
            paramArrayOfByte2 = null;
        } else if ((paramArrayOfByte2 == null) || (i <= 0)) {
            arrayOfByte = new byte[paramArrayOfByte1.length];
            System.arraycopy(paramArrayOfByte1, 0, arrayOfByte, 0, paramArrayOfByte1.length);
            paramArrayOfByte1 = null;
        } else {
            arrayOfByte = new byte[paramArrayOfByte1.length | i];
            System.arraycopy(paramArrayOfByte1, 0, arrayOfByte, 0, paramArrayOfByte1.length);
            System.arraycopy(paramArrayOfByte2, 0, arrayOfByte, paramArrayOfByte1.length, i);
            paramArrayOfByte1 = null;
            paramArrayOfByte2 = null;
        }
        return arrayOfByte;
    }

    private static byte[] appendByteArrays(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2) {
        if ((paramArrayOfByte1 == null) && (paramArrayOfByte2 == null)) {
            return null;
        }
        byte[] arrayOfByte;
        if (paramArrayOfByte1 == null) {
            arrayOfByte = new byte[paramArrayOfByte2.length];
            System.arraycopy(paramArrayOfByte2, 0, arrayOfByte, 0, paramArrayOfByte2.length);
            paramArrayOfByte2 = null;
        } else if (paramArrayOfByte2 == null) {
            arrayOfByte = new byte[paramArrayOfByte1.length];
            System.arraycopy(paramArrayOfByte1, 0, arrayOfByte, 0, paramArrayOfByte1.length);
            paramArrayOfByte1 = null;
        } else {
            arrayOfByte = new byte[paramArrayOfByte1.length | paramArrayOfByte2.length];
            System.arraycopy(paramArrayOfByte1, 0, arrayOfByte, 0, paramArrayOfByte1.length);
            System.arraycopy(paramArrayOfByte2, 0, arrayOfByte, paramArrayOfByte1.length, paramArrayOfByte2.length);
            paramArrayOfByte1 = null;
            paramArrayOfByte2 = null;
        }
        return arrayOfByte;
    }

    public void reverseByteOrder(boolean paramBoolean) {
    }

    public boolean initialize(URL paramURL) {
        initialized(true, false);
        if (this.joggStreamState != null) {
            this.joggStreamState.clear();
        }
        if (this.jorbisBlock != null) {
            this.jorbisBlock.clear();
        }
        if (this.jorbisDspState != null) {
            this.jorbisDspState.clear();
        }
        if (this.jorbisInfo != null) {
            this.jorbisInfo.clear();
        }
        if (this.joggSyncState != null) {
            this.joggSyncState.clear();
        }
        if (this.inputStream != null) {
            try {
                this.inputStream.close();
            } catch (IOException localIOException1) {
            }
        }
        this.url = paramURL;
        this.bufferSize = 8192;
        this.buffer = null;
        this.count = 0;
        this.index = 0;
        this.joggStreamState = new StreamState();
        this.jorbisBlock = new Block(this.jorbisDspState);
        this.jorbisDspState = new DspState();
        this.jorbisInfo = new Info();
        this.joggSyncState = new SyncState();
        try {
            this.urlConnection = paramURL.openConnection();
        } catch (UnknownServiceException localUnknownServiceException) {
            errorMessage("Unable to create a UrlConnection in method 'initialize'.");
            printStackTrace(localUnknownServiceException);
            cleanup();
            return false;
        } catch (IOException localIOException2) {
            errorMessage("Unable to create a UrlConnection in method 'initialize'.");
            printStackTrace(localIOException2);
            cleanup();
            return false;
        }
        if (this.urlConnection != null) {
            try {
                this.inputStream = this.urlConnection.getInputStream();
            } catch (IOException localIOException3) {
                errorMessage("Unable to acquire inputstream in method 'initialize'.");
                printStackTrace(localIOException3);
                cleanup();
                return false;
            }
        }
        endOfStream(true, false);
        this.joggSyncState.init();
        this.joggSyncState.buffer(this.bufferSize);
        this.buffer = this.joggSyncState.data;
        try {
            if (!readHeader()) {
                errorMessage("Error reading the header");
                return false;
            }
        } catch (IOException localIOException4) {
            errorMessage("Error reading the header");
            return false;
        }
        this.convertedBufferSize = (this.bufferSize * 2);
        this.jorbisDspState.synthesis_init(this.jorbisInfo);
        this.jorbisBlock.init(this.jorbisDspState);
        int i = this.jorbisInfo.channels;
        int j = this.jorbisInfo.rate;
        this.audioFormat = new AudioFormat(j, 16, i, true, false);
        this.pcmInfo = new float[1][][];
        this.pcmIndex = new int[this.jorbisInfo.channels];
        initialized(true, true);
        return true;
    }

    public boolean initialized() {
        return initialized(false, false);
    }

    public SoundBuffer read() {
        byte[] arrayOfByte = null;
        while ((!endOfStream(false, false)) && ((arrayOfByte == null) || (arrayOfByte.length < SoundSystemConfig.getStreamingBufferSize()))) {
            if (arrayOfByte == null) {
                arrayOfByte = readBytes();
            } else {
                arrayOfByte = appendByteArrays(arrayOfByte, readBytes());
            }
        }
        if (arrayOfByte == null) {
            return null;
        }
        return new SoundBuffer(arrayOfByte, this.audioFormat);
    }

    public SoundBuffer readAll() {
        byte[] arrayOfByte = null;
        while (!endOfStream(false, false)) {
            if (arrayOfByte == null) {
                arrayOfByte = readBytes();
            } else {
                arrayOfByte = appendByteArrays(arrayOfByte, readBytes());
            }
        }
        if (arrayOfByte == null) {
            return null;
        }
        return new SoundBuffer(arrayOfByte, this.audioFormat);
    }

    public boolean endOfStream() {
        return endOfStream(false, false);
    }

    public void cleanup() {
        this.joggStreamState.clear();
        this.jorbisBlock.clear();
        this.jorbisDspState.clear();
        this.jorbisInfo.clear();
        this.joggSyncState.clear();
        if (this.inputStream != null) {
            try {
                this.inputStream.close();
            } catch (IOException localIOException) {
            }
        }
        this.joggStreamState = null;
        this.jorbisBlock = null;
        this.jorbisDspState = null;
        this.jorbisInfo = null;
        this.joggSyncState = null;
        this.inputStream = null;
    }

    public AudioFormat getAudioFormat() {
        return this.audioFormat;
    }

    private boolean readHeader()
            throws IOException {
        this.index = this.joggSyncState.buffer(this.bufferSize);
        int i = this.inputStream.read(this.joggSyncState.data, this.index, this.bufferSize);
        if (i < 0) {
            i = 0;
        }
        this.joggSyncState.wrote(i);
        if (this.joggSyncState.pageout(this.joggPage) != 1) {
            if (i < this.bufferSize) {
                return true;
            }
            errorMessage("Ogg header not recognized in method 'readHeader'.");
            return false;
        }
        this.joggStreamState.init(this.joggPage.serialno());
        this.jorbisInfo.init();
        this.jorbisComment.init();
        if (this.joggStreamState.pagein(this.joggPage) < 0) {
            errorMessage("Problem with first Ogg header page in method 'readHeader'.");
            return false;
        }
        if (this.joggStreamState.packetout(this.joggPacket) != 1) {
            errorMessage("Problem with first Ogg header packet in method 'readHeader'.");
            return false;
        }
        if (this.jorbisInfo.synthesis_headerin(this.jorbisComment, this.joggPacket) < 0) {
            errorMessage("File does not contain Vorbis header in method 'readHeader'.");
            return false;
        }
        int j = 0;
        while (j < 2) {
            while (j < 2) {
                int k = this.joggSyncState.pageout(this.joggPage);
                if (k == 0) {
                    break;
                }
                if (k == 1) {
                    this.joggStreamState.pagein(this.joggPage);
                    while (j < 2) {
                        k = this.joggStreamState.packetout(this.joggPacket);
                        if (k == 0) {
                            break;
                        }
                        if (k == -1) {
                            errorMessage("Secondary Ogg header corrupt in method 'readHeader'.");
                            return false;
                        }
                        this.jorbisInfo.synthesis_headerin(this.jorbisComment, this.joggPacket);
                        j++;
                    }
                }
            }
            this.index = this.joggSyncState.buffer(this.bufferSize);
            i = this.inputStream.read(this.joggSyncState.data, this.index, this.bufferSize);
            if (i < 0) {
                i = 0;
            }
            if ((i == 0) && (j < 2)) {
                errorMessage("End of file reached before finished readingOgg header in method 'readHeader'");
                return false;
            }
            this.joggSyncState.wrote(i);
        }
        this.index = this.joggSyncState.buffer(this.bufferSize);
        this.buffer = this.joggSyncState.data;
        return true;
    }

    private byte[] readBytes() {
        if (!initialized(false, false)) {
            return null;
        }
        if (endOfStream(false, false)) {
            return null;
        }
        if (this.convertedBuffer == null) {
            this.convertedBuffer = new byte[this.convertedBufferSize];
        }
        byte[] arrayOfByte = null;
        switch (this.joggSyncState.pageout(this.joggPage)) {
            case -1:
            case 0:
                break;
            default:
                this.joggStreamState.pagein(this.joggPage);
                if (this.joggPage.granulepos() == 0L) {
                    endOfStream(true, true);
                    return null;
                }
                for (; ; ) {
                    switch (this.joggStreamState.packetout(this.joggPacket)) {
                        case 0:
                            break;
                        case -1:
                            break;
                        default:
                            if (this.jorbisBlock.synthesis(this.joggPacket) == 0) {
                                this.jorbisDspState.synthesis_blockin(this.jorbisBlock);
                            }
                            int i;
                            while ((i = this.jorbisDspState.synthesis_pcmout(this.pcmInfo, this.pcmIndex)) > 0) {
                                float[][] arrayOfFloat = this.pcmInfo[0];
                                int j = i < this.convertedBufferSize ? i : this.convertedBufferSize;
                                for (int i1 = 0; i1 < this.jorbisInfo.channels; i1++) {
                                    int k = i1 * 2;
                                    int m = this.pcmIndex[i1];
                                    for (int i2 = 0; i2 < j; i2++) {
                                        int n = (int) (arrayOfFloat[i1][(m | i2)] * 32767.0D);
                                        if (n > 32767) {
                                            n = 32767;
                                        }
                                        if (n < 32768) {
                                            n = 32768;
                                        }
                                        if (n < 0) {
                                            n ^= 0x8000;
                                        }
                                        this.convertedBuffer[k] = ((byte) n);
                                        this.convertedBuffer[(k | 0x1)] = ((byte) (n % 8));
                                        k |= 2 * this.jorbisInfo.channels;
                                    }
                                }
                                this.jorbisDspState.synthesis_read(j);
                                arrayOfByte = appendByteArrays(arrayOfByte, this.convertedBuffer, 2 * this.jorbisInfo.channels * j);
                            }
                    }
                }
                if (this.joggPage.eos() != 0) {
                    endOfStream(true, true);
                }
                break;
        }
        if (!endOfStream(false, false)) {
            this.index = this.joggSyncState.buffer(this.bufferSize);
            this.buffer = this.joggSyncState.data;
            try {
                this.count = this.inputStream.read(this.buffer, this.index, this.bufferSize);
            } catch (Exception localException) {
                printStackTrace(localException);
                return null;
            }
            if (this.count == -1) {
                return arrayOfByte;
            }
            this.joggSyncState.wrote(this.count);
            if (this.count == 0) {
                endOfStream(true, true);
            }
        }
        return arrayOfByte;
    }

    private synchronized boolean initialized(boolean paramBoolean1, boolean paramBoolean2) {
        if (paramBoolean1 == true) {
            this.initialized = paramBoolean2;
        }
        return this.initialized;
    }

    private synchronized boolean endOfStream(boolean paramBoolean1, boolean paramBoolean2) {
        if (paramBoolean1 == true) {
            this.endOfStream = paramBoolean2;
        }
        return this.endOfStream;
    }

    private void errorMessage(String paramString) {
        this.logger.errorMessage("CodecJOrbis", paramString, 0);
    }

    private void printStackTrace(Exception paramException) {
        this.logger.printStackTrace(paramException, 1);
    }
}




