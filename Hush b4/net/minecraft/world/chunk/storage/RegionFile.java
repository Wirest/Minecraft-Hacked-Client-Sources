// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.chunk.storage;

import java.io.ByteArrayOutputStream;
import net.minecraft.server.MinecraftServer;
import java.util.zip.DeflaterOutputStream;
import java.io.OutputStream;
import java.io.DataOutputStream;
import java.util.zip.InflaterInputStream;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import com.google.common.collect.Lists;
import java.util.List;
import java.io.RandomAccessFile;
import java.io.File;

public class RegionFile
{
    private static final byte[] emptySector;
    private final File fileName;
    private RandomAccessFile dataFile;
    private final int[] offsets;
    private final int[] chunkTimestamps;
    private List<Boolean> sectorFree;
    private int sizeDelta;
    private long lastModified;
    
    static {
        emptySector = new byte[4096];
    }
    
    public RegionFile(final File fileNameIn) {
        this.offsets = new int[1024];
        this.chunkTimestamps = new int[1024];
        this.fileName = fileNameIn;
        this.sizeDelta = 0;
        try {
            if (fileNameIn.exists()) {
                this.lastModified = fileNameIn.lastModified();
            }
            this.dataFile = new RandomAccessFile(fileNameIn, "rw");
            if (this.dataFile.length() < 4096L) {
                for (int i = 0; i < 1024; ++i) {
                    this.dataFile.writeInt(0);
                }
                for (int i2 = 0; i2 < 1024; ++i2) {
                    this.dataFile.writeInt(0);
                }
                this.sizeDelta += 8192;
            }
            if ((this.dataFile.length() & 0xFFFL) != 0x0L) {
                for (int j1 = 0; j1 < (this.dataFile.length() & 0xFFFL); ++j1) {
                    this.dataFile.write(0);
                }
            }
            final int k1 = (int)this.dataFile.length() / 4096;
            this.sectorFree = (List<Boolean>)Lists.newArrayListWithCapacity(k1);
            for (int l = 0; l < k1; ++l) {
                this.sectorFree.add(true);
            }
            this.sectorFree.set(0, false);
            this.sectorFree.set(1, false);
            this.dataFile.seek(0L);
            for (int l2 = 0; l2 < 1024; ++l2) {
                final int m = this.dataFile.readInt();
                this.offsets[l2] = m;
                if (m != 0 && (m >> 8) + (m & 0xFF) <= this.sectorFree.size()) {
                    for (int l3 = 0; l3 < (m & 0xFF); ++l3) {
                        this.sectorFree.set((m >> 8) + l3, false);
                    }
                }
            }
            for (int i3 = 0; i3 < 1024; ++i3) {
                final int j2 = this.dataFile.readInt();
                this.chunkTimestamps[i3] = j2;
            }
        }
        catch (IOException ioexception) {
            ioexception.printStackTrace();
        }
    }
    
    public synchronized DataInputStream getChunkDataInputStream(final int x, final int z) {
        if (this.outOfBounds(x, z)) {
            return null;
        }
        try {
            final int i = this.getOffset(x, z);
            if (i == 0) {
                return null;
            }
            final int j = i >> 8;
            final int k = i & 0xFF;
            if (j + k > this.sectorFree.size()) {
                return null;
            }
            this.dataFile.seek(j * 4096);
            final int l = this.dataFile.readInt();
            if (l > 4096 * k) {
                return null;
            }
            if (l <= 0) {
                return null;
            }
            final byte b0 = this.dataFile.readByte();
            if (b0 == 1) {
                final byte[] abyte1 = new byte[l - 1];
                this.dataFile.read(abyte1);
                return new DataInputStream(new BufferedInputStream(new GZIPInputStream(new ByteArrayInputStream(abyte1))));
            }
            if (b0 == 2) {
                final byte[] abyte2 = new byte[l - 1];
                this.dataFile.read(abyte2);
                return new DataInputStream(new BufferedInputStream(new InflaterInputStream(new ByteArrayInputStream(abyte2))));
            }
            return null;
        }
        catch (IOException var9) {
            return null;
        }
    }
    
    public DataOutputStream getChunkDataOutputStream(final int x, final int z) {
        DataOutputStream dataOutputStream;
        if (this.outOfBounds(x, z)) {
            dataOutputStream = null;
        }
        else {
            final DeflaterOutputStream out;
            dataOutputStream = new DataOutputStream(out);
            out = new DeflaterOutputStream(new ChunkBuffer(x, z));
        }
        return dataOutputStream;
    }
    
    protected synchronized void write(final int x, final int z, final byte[] data, final int length) {
        try {
            final int i = this.getOffset(x, z);
            int j = i >> 8;
            final int k = i & 0xFF;
            final int l = (length + 5) / 4096 + 1;
            if (l >= 256) {
                return;
            }
            if (j != 0 && k == l) {
                this.write(j, data, length);
            }
            else {
                for (int i2 = 0; i2 < k; ++i2) {
                    this.sectorFree.set(j + i2, true);
                }
                int l2 = this.sectorFree.indexOf(true);
                int j2 = 0;
                if (l2 != -1) {
                    for (int k2 = l2; k2 < this.sectorFree.size(); ++k2) {
                        if (j2 != 0) {
                            if (this.sectorFree.get(k2)) {
                                ++j2;
                            }
                            else {
                                j2 = 0;
                            }
                        }
                        else if (this.sectorFree.get(k2)) {
                            l2 = k2;
                            j2 = 1;
                        }
                        if (j2 >= l) {
                            break;
                        }
                    }
                }
                if (j2 >= l) {
                    j = l2;
                    this.setOffset(x, z, l2 << 8 | l);
                    for (int j3 = 0; j3 < l; ++j3) {
                        this.sectorFree.set(j + j3, false);
                    }
                    this.write(j, data, length);
                }
                else {
                    this.dataFile.seek(this.dataFile.length());
                    j = this.sectorFree.size();
                    for (int i3 = 0; i3 < l; ++i3) {
                        this.dataFile.write(RegionFile.emptySector);
                        this.sectorFree.add(false);
                    }
                    this.sizeDelta += 4096 * l;
                    this.write(j, data, length);
                    this.setOffset(x, z, j << 8 | l);
                }
            }
            this.setChunkTimestamp(x, z, (int)(MinecraftServer.getCurrentTimeMillis() / 1000L));
        }
        catch (IOException ioexception) {
            ioexception.printStackTrace();
        }
    }
    
    private void write(final int sectorNumber, final byte[] data, final int length) throws IOException {
        this.dataFile.seek(sectorNumber * 4096);
        this.dataFile.writeInt(length + 1);
        this.dataFile.writeByte(2);
        this.dataFile.write(data, 0, length);
    }
    
    private boolean outOfBounds(final int x, final int z) {
        return x < 0 || x >= 32 || z < 0 || z >= 32;
    }
    
    private int getOffset(final int x, final int z) {
        return this.offsets[x + z * 32];
    }
    
    public boolean isChunkSaved(final int x, final int z) {
        return this.getOffset(x, z) != 0;
    }
    
    private void setOffset(final int x, final int z, final int offset) throws IOException {
        this.offsets[x + z * 32] = offset;
        this.dataFile.seek((x + z * 32) * 4);
        this.dataFile.writeInt(offset);
    }
    
    private void setChunkTimestamp(final int x, final int z, final int timestamp) throws IOException {
        this.chunkTimestamps[x + z * 32] = timestamp;
        this.dataFile.seek(4096 + (x + z * 32) * 4);
        this.dataFile.writeInt(timestamp);
    }
    
    public void close() throws IOException {
        if (this.dataFile != null) {
            this.dataFile.close();
        }
    }
    
    class ChunkBuffer extends ByteArrayOutputStream
    {
        private int chunkX;
        private int chunkZ;
        
        public ChunkBuffer(final int x, final int z) {
            super(8096);
            this.chunkX = x;
            this.chunkZ = z;
        }
        
        @Override
        public void close() throws IOException {
            RegionFile.this.write(this.chunkX, this.chunkZ, this.buf, this.count);
        }
    }
}
