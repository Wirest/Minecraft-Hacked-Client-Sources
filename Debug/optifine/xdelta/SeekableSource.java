package optifine.xdelta;

import java.io.IOException;

public interface SeekableSource
{
    void seek(long var1) throws IOException;

    int read(byte[] var1, int var2, int var3) throws IOException;

    void close() throws IOException;

    long length() throws IOException;
}
