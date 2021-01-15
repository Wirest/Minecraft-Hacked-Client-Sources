package jaco.mp3.resources;

import java.io.IOException;
import java.io.InputStream;

public class InputStreamSource implements Source {
   private final InputStream in;

   public InputStreamSource(InputStream in) {
      if (in == null) {
         throw new NullPointerException("in");
      } else {
         this.in = in;
      }
   }

   public int read(byte[] b, int offs, int len) throws IOException {
      int read = this.in.read(b, offs, len);
      return read;
   }

   public boolean willReadBlock() {
      return true;
   }

   public boolean isSeekable() {
      return false;
   }

   public long tell() {
      return -1L;
   }

   public long seek(long to) {
      return -1L;
   }

   public long length() {
      return -1L;
   }
}
