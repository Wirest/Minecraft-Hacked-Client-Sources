package optifine.xdelta;

import java.io.IOException;
import java.io.RandomAccessFile;

public class RandomAccessFileSeekableSource implements SeekableSource
{
    RandomAccessFile raf;

    public RandomAccessFileSeekableSource(RandomAccessFile raf)
    {
        this.raf = raf;
    }

    public void seek(long pos) throws IOException
    {
        this.raf.seek(pos);
    }

    public int read(byte[] b, int off, int len) throws IOException
    {
        return this.raf.read(b, off, len);
    }

    public long length() throws IOException
    {
        return this.raf.length();
    }

    public void close() throws IOException
    {
        this.raf.close();
    }
}
