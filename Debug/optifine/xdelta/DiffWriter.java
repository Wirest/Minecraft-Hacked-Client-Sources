package optifine.xdelta;

import java.io.IOException;

public interface DiffWriter
{
    void addCopy(int var1, int var2) throws IOException;

    void addData(byte var1) throws IOException;

    void flush() throws IOException;

    void close() throws IOException;
}
