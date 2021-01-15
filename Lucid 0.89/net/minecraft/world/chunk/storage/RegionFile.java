package net.minecraft.world.chunk.storage;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

import com.google.common.collect.Lists;

import net.minecraft.server.MinecraftServer;

public class RegionFile
{
    private static final byte[] emptySector = new byte[4096];
    private RandomAccessFile dataFile;
    private final int[] offsets = new int[1024];
    private final int[] chunkTimestamps = new int[1024];
    private List sectorFree;

    private long lastModified;

    public RegionFile(File fileNameIn)
    {
        try
        {
            if (fileNameIn.exists())
            {
                this.lastModified = fileNameIn.lastModified();
            }

            this.dataFile = new RandomAccessFile(fileNameIn, "rw");
            int var2;

            if (this.dataFile.length() < 4096L)
            {
                for (var2 = 0; var2 < 1024; ++var2)
                {
                    this.dataFile.writeInt(0);
                }

                for (var2 = 0; var2 < 1024; ++var2)
                {
                    this.dataFile.writeInt(0);
                }
            }

            if ((this.dataFile.length() & 4095L) != 0L)
            {
                for (var2 = 0; var2 < (this.dataFile.length() & 4095L); ++var2)
                {
                    this.dataFile.write(0);
                }
            }

            var2 = (int)this.dataFile.length() / 4096;
            this.sectorFree = Lists.newArrayListWithCapacity(var2);
            int var3;

            for (var3 = 0; var3 < var2; ++var3)
            {
                this.sectorFree.add(Boolean.valueOf(true));
            }

            this.sectorFree.set(0, Boolean.valueOf(false));
            this.sectorFree.set(1, Boolean.valueOf(false));
            this.dataFile.seek(0L);
            int var4;

            for (var3 = 0; var3 < 1024; ++var3)
            {
                var4 = this.dataFile.readInt();
                this.offsets[var3] = var4;

                if (var4 != 0 && (var4 >> 8) + (var4 & 255) <= this.sectorFree.size())
                {
                    for (int var5 = 0; var5 < (var4 & 255); ++var5)
                    {
                        this.sectorFree.set((var4 >> 8) + var5, Boolean.valueOf(false));
                    }
                }
            }

            for (var3 = 0; var3 < 1024; ++var3)
            {
                var4 = this.dataFile.readInt();
                this.chunkTimestamps[var3] = var4;
            }
        }
        catch (IOException var6)
        {
            var6.printStackTrace();
        }
    }

    /**
     * Returns an uncompressed chunk stream from the region file.
     *  
     * @param x Chunk X coordinate
     * @param z Chunk Z coordinate
     */
    public synchronized DataInputStream getChunkDataInputStream(int x, int z)
    {
        if (this.outOfBounds(x, z))
        {
            return null;
        }
        else
        {
            try
            {
                int var3 = this.getOffset(x, z);

                if (var3 == 0)
                {
                    return null;
                }
                else
                {
                    int var4 = var3 >> 8;
                    int var5 = var3 & 255;

                    if (var4 + var5 > this.sectorFree.size())
                    {
                        return null;
                    }
                    else
                    {
                        this.dataFile.seek(var4 * 4096);
                        int var6 = this.dataFile.readInt();

                        if (var6 > 4096 * var5)
                        {
                            return null;
                        }
                        else if (var6 <= 0)
                        {
                            return null;
                        }
                        else
                        {
                            byte var7 = this.dataFile.readByte();
                            byte[] var8;

                            if (var7 == 1)
                            {
                                var8 = new byte[var6 - 1];
                                this.dataFile.read(var8);
                                return new DataInputStream(new BufferedInputStream(new GZIPInputStream(new ByteArrayInputStream(var8))));
                            }
                            else if (var7 == 2)
                            {
                                var8 = new byte[var6 - 1];
                                this.dataFile.read(var8);
                                return new DataInputStream(new BufferedInputStream(new InflaterInputStream(new ByteArrayInputStream(var8))));
                            }
                            else
                            {
                                return null;
                            }
                        }
                    }
                }
            }
            catch (IOException var9)
            {
                return null;
            }
        }
    }

    /**
     * Returns an output stream used to write chunk data. Data is on disk when the returned stream is closed.
     *  
     * @param x Chunk X coordinate
     * @param z Chunk Z coordinate
     */
    @SuppressWarnings("resource")
	public DataOutputStream getChunkDataOutputStream(int x, int z)
    {
        return this.outOfBounds(x, z) ? null : new DataOutputStream(new DeflaterOutputStream(new RegionFile.ChunkBuffer(x, z)));
    }

    /**
     * args: x, z, data, length - write chunk data at (x, z) to disk
     *  
     * @param x Chunk X coordinate
     * @param z Chunk Z coordinate
     * @param data The chunk data to write
     * @param length The length of the data
     */
    protected synchronized void write(int x, int z, byte[] data, int length)
    {
        try
        {
            int var5 = this.getOffset(x, z);
            int var6 = var5 >> 8;
            int var7 = var5 & 255;
            int var8 = (length + 5) / 4096 + 1;

            if (var8 >= 256)
            {
                return;
            }

            if (var6 != 0 && var7 == var8)
            {
                this.write(var6, data, length);
            }
            else
            {
                int var9;

                for (var9 = 0; var9 < var7; ++var9)
                {
                    this.sectorFree.set(var6 + var9, Boolean.valueOf(true));
                }

                var9 = this.sectorFree.indexOf(Boolean.valueOf(true));
                int var10 = 0;
                int var11;

                if (var9 != -1)
                {
                    for (var11 = var9; var11 < this.sectorFree.size(); ++var11)
                    {
                        if (var10 != 0)
                        {
                            if (((Boolean)this.sectorFree.get(var11)).booleanValue())
                            {
                                ++var10;
                            }
                            else
                            {
                                var10 = 0;
                            }
                        }
                        else if (((Boolean)this.sectorFree.get(var11)).booleanValue())
                        {
                            var9 = var11;
                            var10 = 1;
                        }

                        if (var10 >= var8)
                        {
                            break;
                        }
                    }
                }

                if (var10 >= var8)
                {
                    var6 = var9;
                    this.setOffset(x, z, var9 << 8 | var8);

                    for (var11 = 0; var11 < var8; ++var11)
                    {
                        this.sectorFree.set(var6 + var11, Boolean.valueOf(false));
                    }

                    this.write(var6, data, length);
                }
                else
                {
                    this.dataFile.seek(this.dataFile.length());
                    var6 = this.sectorFree.size();

                    for (var11 = 0; var11 < var8; ++var11)
                    {
                        this.dataFile.write(emptySector);
                        this.sectorFree.add(Boolean.valueOf(false));
                    }

                    this.write(var6, data, length);
                    this.setOffset(x, z, var6 << 8 | var8);
                }
            }

            this.setChunkTimestamp(x, z, (int)(MinecraftServer.getCurrentTimeMillis() / 1000L));
        }
        catch (IOException var12)
        {
            var12.printStackTrace();
        }
    }

    /**
     * args: sectorNumber, data, length - write the chunk data to this RegionFile
     */
    private void write(int sectorNumber, byte[] data, int length) throws IOException
    {
        this.dataFile.seek(sectorNumber * 4096);
        this.dataFile.writeInt(length + 1);
        this.dataFile.writeByte(2);
        this.dataFile.write(data, 0, length);
    }

    /**
     * args: x, z - check region bounds
     *  
     * @param x Chunk X coordinate
     * @param z Chunk Z coordinate
     */
    private boolean outOfBounds(int x, int z)
    {
        return x < 0 || x >= 32 || z < 0 || z >= 32;
    }

    /**
     * args: x, z - get chunk's offset in region file
     *  
     * @param x Chunk X coordinate
     * @param z Chunk Z coordinate
     */
    private int getOffset(int x, int z)
    {
        return this.offsets[x + z * 32];
    }

    /**
     * args: x, z, - true if chunk has been saved / converted
     *  
     * @param x Chunk X coordinate
     * @param z Chunk Z coordinate
     */
    public boolean isChunkSaved(int x, int z)
    {
        return this.getOffset(x, z) != 0;
    }

    /**
     * args: x, z, offset - sets the chunk's offset in the region file
     *  
     * @param x Chunk X coordinate
     * @param z Chunk Z coordinate
     * @param offset The chunk offset
     */
    private void setOffset(int x, int z, int offset) throws IOException
    {
        this.offsets[x + z * 32] = offset;
        this.dataFile.seek((x + z * 32) * 4);
        this.dataFile.writeInt(offset);
    }

    /**
     * args: x, z, timestamp - sets the chunk's write timestamp
     *  
     * @param x Chunk X coordinate
     * @param z Chunk Z coordinate
     * @param timestamp The chunk's write timestamp
     */
    private void setChunkTimestamp(int x, int z, int timestamp) throws IOException
    {
        this.chunkTimestamps[x + z * 32] = timestamp;
        this.dataFile.seek(4096 + (x + z * 32) * 4);
        this.dataFile.writeInt(timestamp);
    }

    /**
     * close this RegionFile and prevent further writes
     */
    public void close() throws IOException
    {
        if (this.dataFile != null)
        {
            this.dataFile.close();
        }
    }

    class ChunkBuffer extends ByteArrayOutputStream
    {
        private int chunkX;
        private int chunkZ;

        public ChunkBuffer(int x, int z)
        {
            super(8096);
            this.chunkX = x;
            this.chunkZ = z;
        }

        @Override
		public void close() throws IOException
        {
            RegionFile.this.write(this.chunkX, this.chunkZ, this.buf, this.count);
        }
    }
}
