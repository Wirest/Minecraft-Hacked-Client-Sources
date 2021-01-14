package optifine.xdelta;

import java.io.IOException;
import java.io.InputStream;

public class SeekableSourceInputStream extends InputStream
{
    SeekableSource ss;

    public SeekableSourceInputStream(SeekableSource ss)
    {
        this.ss = ss;
    }

    public int read() throws IOException
    {
        byte[] abyte = new byte[1];
        this.ss.read(abyte, 0, 1);
        return abyte[0];
    }

    public int read(byte[] b, int off, int len) throws IOException
    {
        return this.ss.read(b, off, len);
    }

    public void close() throws IOException
    {
        this.ss.close();
    }
}
