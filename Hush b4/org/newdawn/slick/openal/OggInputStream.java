// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.openal;

import org.newdawn.slick.util.Log;
import java.io.IOException;
import org.lwjgl.BufferUtils;
import java.nio.ByteOrder;
import java.nio.ByteBuffer;
import com.jcraft.jorbis.Block;
import com.jcraft.jorbis.DspState;
import com.jcraft.jorbis.Comment;
import com.jcraft.jogg.Packet;
import com.jcraft.jogg.Page;
import com.jcraft.jogg.StreamState;
import com.jcraft.jogg.SyncState;
import com.jcraft.jorbis.Info;
import java.io.InputStream;

public class OggInputStream extends InputStream implements AudioInputStream
{
    private int convsize;
    private byte[] convbuffer;
    private InputStream input;
    private Info oggInfo;
    private boolean endOfStream;
    private SyncState syncState;
    private StreamState streamState;
    private Page page;
    private Packet packet;
    private Comment comment;
    private DspState dspState;
    private Block vorbisBlock;
    byte[] buffer;
    int bytes;
    boolean bigEndian;
    boolean endOfBitStream;
    boolean inited;
    private int readIndex;
    private ByteBuffer pcmBuffer;
    private int total;
    
    public OggInputStream(final InputStream input) throws IOException {
        this.convsize = 16384;
        this.convbuffer = new byte[this.convsize];
        this.oggInfo = new Info();
        this.syncState = new SyncState();
        this.streamState = new StreamState();
        this.page = new Page();
        this.packet = new Packet();
        this.comment = new Comment();
        this.dspState = new DspState();
        this.vorbisBlock = new Block(this.dspState);
        this.bytes = 0;
        this.bigEndian = ByteOrder.nativeOrder().equals(ByteOrder.BIG_ENDIAN);
        this.endOfBitStream = true;
        this.inited = false;
        this.pcmBuffer = BufferUtils.createByteBuffer(2048000);
        this.input = input;
        this.total = input.available();
        this.init();
    }
    
    public int getLength() {
        return this.total;
    }
    
    @Override
    public int getChannels() {
        return this.oggInfo.channels;
    }
    
    @Override
    public int getRate() {
        return this.oggInfo.rate;
    }
    
    private void init() throws IOException {
        this.initVorbis();
        this.readPCM();
    }
    
    @Override
    public int available() {
        return this.endOfStream ? 0 : 1;
    }
    
    private void initVorbis() {
        this.syncState.init();
    }
    
    private boolean getPageAndPacket() {
        int index = this.syncState.buffer(4096);
        this.buffer = this.syncState.data;
        if (this.buffer == null) {
            this.endOfStream = true;
            return false;
        }
        try {
            this.bytes = this.input.read(this.buffer, index, 4096);
        }
        catch (Exception e) {
            Log.error("Failure reading in vorbis");
            Log.error(e);
            this.endOfStream = true;
            return false;
        }
        this.syncState.wrote(this.bytes);
        if (this.syncState.pageout(this.page) != 1) {
            if (this.bytes < 4096) {
                return false;
            }
            Log.error("Input does not appear to be an Ogg bitstream.");
            this.endOfStream = true;
            return false;
        }
        else {
            this.streamState.init(this.page.serialno());
            this.oggInfo.init();
            this.comment.init();
            if (this.streamState.pagein(this.page) < 0) {
                Log.error("Error reading first page of Ogg bitstream data.");
                this.endOfStream = true;
                return false;
            }
            if (this.streamState.packetout(this.packet) != 1) {
                Log.error("Error reading initial header packet.");
                this.endOfStream = true;
                return false;
            }
            if (this.oggInfo.synthesis_headerin(this.comment, this.packet) < 0) {
                Log.error("This Ogg bitstream does not contain Vorbis audio data.");
                this.endOfStream = true;
                return false;
            }
            int i = 0;
            while (i < 2) {
                while (i < 2) {
                    int result = this.syncState.pageout(this.page);
                    if (result == 0) {
                        break;
                    }
                    if (result != 1) {
                        continue;
                    }
                    this.streamState.pagein(this.page);
                    while (i < 2) {
                        result = this.streamState.packetout(this.packet);
                        if (result == 0) {
                            break;
                        }
                        if (result == -1) {
                            Log.error("Corrupt secondary header.  Exiting.");
                            this.endOfStream = true;
                            return false;
                        }
                        this.oggInfo.synthesis_headerin(this.comment, this.packet);
                        ++i;
                    }
                }
                index = this.syncState.buffer(4096);
                this.buffer = this.syncState.data;
                try {
                    this.bytes = this.input.read(this.buffer, index, 4096);
                }
                catch (Exception e2) {
                    Log.error("Failed to read Vorbis: ");
                    Log.error(e2);
                    this.endOfStream = true;
                    return false;
                }
                if (this.bytes == 0 && i < 2) {
                    Log.error("End of file before finding all Vorbis headers!");
                    this.endOfStream = true;
                    return false;
                }
                this.syncState.wrote(this.bytes);
            }
            this.convsize = 4096 / this.oggInfo.channels;
            this.dspState.synthesis_init(this.oggInfo);
            this.vorbisBlock.init(this.dspState);
            return true;
        }
    }
    
    private void readPCM() throws IOException {
        boolean wrote = false;
        while (true) {
            if (this.endOfBitStream) {
                if (!this.getPageAndPacket()) {
                    this.syncState.clear();
                    this.endOfStream = true;
                    return;
                }
                this.endOfBitStream = false;
            }
            if (!this.inited) {
                this.inited = true;
                return;
            }
            final float[][][] _pcm = { null };
            final int[] _index = new int[this.oggInfo.channels];
            while (!this.endOfBitStream) {
                while (!this.endOfBitStream) {
                    int result = this.syncState.pageout(this.page);
                    if (result == 0) {
                        break;
                    }
                    if (result == -1) {
                        Log.error("Corrupt or missing data in bitstream; continuing...");
                    }
                    else {
                        this.streamState.pagein(this.page);
                        while (true) {
                            result = this.streamState.packetout(this.packet);
                            if (result == 0) {
                                break;
                            }
                            if (result == -1) {
                                continue;
                            }
                            if (this.vorbisBlock.synthesis(this.packet) == 0) {
                                this.dspState.synthesis_blockin(this.vorbisBlock);
                            }
                            int samples;
                            while ((samples = this.dspState.synthesis_pcmout(_pcm, _index)) > 0) {
                                final float[][] pcm = _pcm[0];
                                final int bout = (samples < this.convsize) ? samples : this.convsize;
                                for (int i = 0; i < this.oggInfo.channels; ++i) {
                                    int ptr = i * 2;
                                    final int mono = _index[i];
                                    for (int j = 0; j < bout; ++j) {
                                        int val = (int)(pcm[i][mono + j] * 32767.0);
                                        if (val > 32767) {
                                            val = 32767;
                                        }
                                        if (val < -32768) {
                                            val = -32768;
                                        }
                                        if (val < 0) {
                                            val |= 0x8000;
                                        }
                                        if (this.bigEndian) {
                                            this.convbuffer[ptr] = (byte)(val >>> 8);
                                            this.convbuffer[ptr + 1] = (byte)val;
                                        }
                                        else {
                                            this.convbuffer[ptr] = (byte)val;
                                            this.convbuffer[ptr + 1] = (byte)(val >>> 8);
                                        }
                                        ptr += 2 * this.oggInfo.channels;
                                    }
                                }
                                final int bytesToWrite = 2 * this.oggInfo.channels * bout;
                                if (bytesToWrite >= this.pcmBuffer.remaining()) {
                                    Log.warn("Read block from OGG that was too big to be buffered: " + bytesToWrite);
                                }
                                else {
                                    this.pcmBuffer.put(this.convbuffer, 0, bytesToWrite);
                                }
                                wrote = true;
                                this.dspState.synthesis_read(bout);
                            }
                        }
                        if (this.page.eos() != 0) {
                            this.endOfBitStream = true;
                        }
                        if (!this.endOfBitStream && wrote) {
                            return;
                        }
                        continue;
                    }
                }
                if (!this.endOfBitStream) {
                    this.bytes = 0;
                    final int index = this.syncState.buffer(4096);
                    Label_0579: {
                        if (index >= 0) {
                            this.buffer = this.syncState.data;
                            try {
                                this.bytes = this.input.read(this.buffer, index, 4096);
                                break Label_0579;
                            }
                            catch (Exception e) {
                                Log.error("Failure during vorbis decoding");
                                Log.error(e);
                                this.endOfStream = true;
                                return;
                            }
                        }
                        this.bytes = 0;
                    }
                    this.syncState.wrote(this.bytes);
                    if (this.bytes != 0) {
                        continue;
                    }
                    this.endOfBitStream = true;
                }
            }
            this.streamState.clear();
            this.vorbisBlock.clear();
            this.dspState.clear();
            this.oggInfo.clear();
        }
    }
    
    @Override
    public int read() throws IOException {
        if (this.readIndex >= this.pcmBuffer.position()) {
            this.pcmBuffer.clear();
            this.readPCM();
            this.readIndex = 0;
        }
        if (this.readIndex >= this.pcmBuffer.position()) {
            return -1;
        }
        int value = this.pcmBuffer.get(this.readIndex);
        if (value < 0) {
            value += 256;
        }
        ++this.readIndex;
        return value;
    }
    
    @Override
    public boolean atEnd() {
        return this.endOfStream && this.readIndex >= this.pcmBuffer.position();
    }
    
    @Override
    public int read(final byte[] b, final int off, final int len) throws IOException {
        for (int i = 0; i < len; ++i) {
            try {
                final int value = this.read();
                if (value >= 0) {
                    b[i] = (byte)value;
                }
                else {
                    if (i == 0) {
                        return -1;
                    }
                    return i;
                }
            }
            catch (IOException e) {
                Log.error(e);
                return i;
            }
        }
        return len;
    }
    
    @Override
    public int read(final byte[] b) throws IOException {
        return this.read(b, 0, b.length);
    }
    
    @Override
    public void close() throws IOException {
    }
}
