package jaco.mp3.resources;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;

public final class SoundStream implements BitstreamErrors {
   static byte INITIAL_SYNC = 0;
   static byte STRICT_SYNC = 1;
   private static final int BUFFER_INT_SIZE = 433;
   private final int[] framebuffer = new int[433];
   private int framesize;
   private byte[] frame_bytes = new byte[1732];
   private int wordpointer;
   private int bitindex;
   private int syncword;
   private int header_pos = 0;
   private boolean single_ch_mode;
   private final int[] bitmask = new int[]{0, 1, 3, 7, 15, 31, 63, 127, 255, 511, 1023, 2047, 4095, 8191, 16383, 32767, 65535, 131071};
   private final PushbackInputStream source;
   private final Frame header = new Frame();
   private final byte[] syncbuf = new byte[4];
   private Crc16[] crc = new Crc16[1];
   private byte[] rawid3v2 = null;
   private boolean firstframe = true;

   public SoundStream(InputStream in) {
      if (in == null) {
         throw new NullPointerException("in");
      } else {
         InputStream in = new BufferedInputStream(in);
         this.loadID3v2(in);
         this.firstframe = true;
         this.source = new PushbackInputStream(in, 1732);
         this.closeFrame();
      }
   }

   public int header_pos() {
      return this.header_pos;
   }

   private void loadID3v2(InputStream in) {
      int size = -1;

      try {
         in.mark(10);
         size = this.readID3v2Header(in);
         this.header_pos = size;
      } catch (IOException var14) {
      } finally {
         try {
            in.reset();
         } catch (IOException var12) {
         }

      }

      try {
         if (size > 0) {
            this.rawid3v2 = new byte[size];
            in.read(this.rawid3v2, 0, this.rawid3v2.length);
         }
      } catch (IOException var13) {
      }

   }

   private int readID3v2Header(InputStream in) throws IOException {
      byte[] id3header = new byte[4];
      int size = -10;
      in.read(id3header, 0, 3);
      if (id3header[0] == 73 && id3header[1] == 68 && id3header[2] == 51) {
         in.read(id3header, 0, 3);
         int majorVersion = id3header[0];
         int revision = id3header[1];
         in.read(id3header, 0, 4);
         size = (id3header[0] << 21) + (id3header[1] << 14) + (id3header[2] << 7) + id3header[3];
      }

      return size + 10;
   }

   public InputStream getRawID3v2() {
      if (this.rawid3v2 == null) {
         return null;
      } else {
         ByteArrayInputStream bain = new ByteArrayInputStream(this.rawid3v2);
         return bain;
      }
   }

   public void close() throws BitstreamException {
      try {
         this.source.close();
      } catch (IOException var2) {
         throw this.newBitstreamException(258, var2);
      }
   }

   public Frame readFrame() throws BitstreamException {
      Frame result = null;

      try {
         result = this.readNextFrame();
         if (this.firstframe) {
            result.parseVBR(this.frame_bytes);
            this.firstframe = false;
         }
      } catch (BitstreamException var5) {
         if (var5.getErrorCode() == 261) {
            try {
               this.closeFrame();
               result = this.readNextFrame();
            } catch (BitstreamException var4) {
               if (var4.getErrorCode() != 260) {
                  throw this.newBitstreamException(var4.getErrorCode(), var4);
               }
            }
         } else if (var5.getErrorCode() != 260) {
            throw this.newBitstreamException(var5.getErrorCode(), var5);
         }
      }

      return result;
   }

   private Frame readNextFrame() throws BitstreamException {
      if (this.framesize == -1) {
         this.nextFrame();
      }

      return this.header;
   }

   private void nextFrame() throws BitstreamException {
      this.header.read_header(this, this.crc);
   }

   public void unreadFrame() throws BitstreamException {
      if (this.wordpointer == -1 && this.bitindex == -1 && this.framesize > 0) {
         try {
            this.source.unread(this.frame_bytes, 0, this.framesize);
         } catch (IOException var2) {
            throw this.newBitstreamException(258);
         }
      }

   }

   public void closeFrame() {
      this.framesize = -1;
      this.wordpointer = -1;
      this.bitindex = -1;
   }

   public boolean isSyncCurrentPosition(int syncmode) throws BitstreamException {
      int read = this.readBytes(this.syncbuf, 0, 4);
      int headerstring = this.syncbuf[0] << 24 & -16777216 | this.syncbuf[1] << 16 & 16711680 | this.syncbuf[2] << 8 & '\uff00' | this.syncbuf[3] << 0 & 255;

      try {
         this.source.unread(this.syncbuf, 0, read);
      } catch (IOException var5) {
      }

      boolean sync = false;
      switch(read) {
      case 0:
         sync = true;
      case 1:
      case 2:
      case 3:
      default:
         break;
      case 4:
         sync = this.isSyncMark(headerstring, syncmode, this.syncword);
      }

      return sync;
   }

   public int readBits(int n) {
      return this.get_bits(n);
   }

   public int readCheckedBits(int n) {
      return this.get_bits(n);
   }

   protected BitstreamException newBitstreamException(int errorcode) {
      return new BitstreamException(errorcode, (Throwable)null);
   }

   protected BitstreamException newBitstreamException(int errorcode, Throwable throwable) {
      return new BitstreamException(errorcode, throwable);
   }

   int syncHeader(byte syncmode) throws BitstreamException {
      int bytesRead = this.readBytes(this.syncbuf, 0, 3);
      if (bytesRead != 3) {
         throw this.newBitstreamException(260, (Throwable)null);
      } else {
         int headerstring = this.syncbuf[0] << 16 & 16711680 | this.syncbuf[1] << 8 & '\uff00' | this.syncbuf[2] << 0 & 255;

         boolean sync;
         do {
            headerstring <<= 8;
            if (this.readBytes(this.syncbuf, 3, 1) != 1) {
               throw this.newBitstreamException(260, (Throwable)null);
            }

            headerstring |= this.syncbuf[3] & 255;
            sync = this.isSyncMark(headerstring, syncmode, this.syncword);
         } while(!sync);

         return headerstring;
      }
   }

   public boolean isSyncMark(int headerstring, int syncmode, int word) {
      boolean sync = false;
      if (syncmode == INITIAL_SYNC) {
         sync = (headerstring & -2097152) == -2097152;
      } else {
         sync = (headerstring & -521216) == word && (headerstring & 192) == 192 == this.single_ch_mode;
      }

      if (sync) {
         sync = (headerstring >>> 10 & 3) != 3;
      }

      if (sync) {
         sync = (headerstring >>> 17 & 3) != 0;
      }

      if (sync) {
         sync = (headerstring >>> 19 & 3) != 1;
      }

      return sync;
   }

   int read_frame_data(int bytesize) throws BitstreamException {
      int numread = false;
      int numread = this.readFully(this.frame_bytes, 0, bytesize);
      this.framesize = bytesize;
      this.wordpointer = -1;
      this.bitindex = -1;
      return numread;
   }

   void parse_frame() throws BitstreamException {
      int b = 0;
      byte[] byteread = this.frame_bytes;
      int bytesize = this.framesize;

      for(int k = 0; k < bytesize; k += 4) {
         int convert = false;
         byte b0 = false;
         byte b1 = 0;
         byte b2 = 0;
         byte b3 = 0;
         byte b0 = byteread[k];
         if (k + 1 < bytesize) {
            b1 = byteread[k + 1];
         }

         if (k + 2 < bytesize) {
            b2 = byteread[k + 2];
         }

         if (k + 3 < bytesize) {
            b3 = byteread[k + 3];
         }

         this.framebuffer[b++] = b0 << 24 & -16777216 | b1 << 16 & 16711680 | b2 << 8 & '\uff00' | b3 & 255;
      }

      this.wordpointer = 0;
      this.bitindex = 0;
   }

   public int get_bits(int number_of_bits) {
      int returnvalue = false;
      int sum = this.bitindex + number_of_bits;
      if (this.wordpointer < 0) {
         this.wordpointer = 0;
      }

      int returnvalue;
      if (sum <= 32) {
         returnvalue = this.framebuffer[this.wordpointer] >>> 32 - sum & this.bitmask[number_of_bits];
         if ((this.bitindex += number_of_bits) == 32) {
            this.bitindex = 0;
            ++this.wordpointer;
         }

         return returnvalue;
      } else {
         int Right = this.framebuffer[this.wordpointer] & '\uffff';
         ++this.wordpointer;
         int Left = this.framebuffer[this.wordpointer] & -65536;
         returnvalue = Right << 16 & -65536 | Left >>> 16 & '\uffff';
         returnvalue >>>= 48 - sum;
         returnvalue &= this.bitmask[number_of_bits];
         this.bitindex = sum - 32;
         return returnvalue;
      }
   }

   void set_syncword(int syncword0) {
      this.syncword = syncword0 & -193;
      this.single_ch_mode = (syncword0 & 192) == 192;
   }

   private int readFully(byte[] b, int offs, int len) throws BitstreamException {
      int nRead = 0;

      try {
         while(len > 0) {
            int bytesread = this.source.read(b, offs, len);
            if (bytesread == -1) {
               while(len-- > 0) {
                  b[offs++] = 0;
               }

               return nRead;
            }

            nRead += bytesread;
            offs += bytesread;
            len -= bytesread;
         }

         return nRead;
      } catch (IOException var6) {
         throw this.newBitstreamException(258, var6);
      }
   }

   private int readBytes(byte[] b, int offs, int len) throws BitstreamException {
      int totalBytesRead = 0;

      try {
         while(len > 0) {
            int bytesread = this.source.read(b, offs, len);
            if (bytesread == -1) {
               break;
            }

            totalBytesRead += bytesread;
            offs += bytesread;
            len -= bytesread;
         }

         return totalBytesRead;
      } catch (IOException var6) {
         throw this.newBitstreamException(258, var6);
      }
   }
}
