package optifine.xdelta;

import java.io.IOException;

public class ByteArraySeekableSource implements SeekableSource
{
    byte[] source;
    long lastPos = 0L;

    public ByteArraySeekableSource(byte[] source)
    {
        this.source = source;
    }

    public void seek(long pos) throws IOException
    {
        this.lastPos = pos;
    }

    public int read(byte[] b, int off, int len) throws IOException
    {
        int i = this.source.length - (int)this.lastPos;

        if (i <= 0)
        {
            return -1;
        }
        else
        {
            if (i < len)
            {
                len = i;
            }

            System.arraycopy(this.source, (int)this.lastPos, b, off, len);
            this.lastPos += (long)len;
            return len;
        }
    }

    public long length() throws IOException
    {
        return (long)this.source.length;
    }

    public void close() throws IOException
    {
        this.source = null;
    }
}
