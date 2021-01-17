package javazoom.jl.decoder;

import java.io.IOException;

public interface Source {
   long LENGTH_UNKNOWN = -1L;

   int read(byte[] var1, int var2, int var3) throws IOException;

   boolean willReadBlock();

   boolean isSeekable();

   long length();

   long tell();

   long seek(long var1);
}
