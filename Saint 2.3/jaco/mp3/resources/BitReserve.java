package jaco.mp3.resources;

final class BitReserve {
   private static final int BUFSIZE = 32768;
   private static final int BUFSIZE_MASK = 32767;
   private int offset = 0;
   private int totbit = 0;
   private int buf_byte_idx = 0;
   private final int[] buf = new int['耀'];
   private int buf_bit_idx;

   public int hsstell() {
      return this.totbit;
   }

   public int hgetbits(int N) {
      this.totbit += N;
      int val = 0;
      int pos = this.buf_byte_idx;
      if (pos + N < 32768) {
         while(N-- > 0) {
            val <<= 1;
            val |= this.buf[pos++] != 0 ? 1 : 0;
         }
      } else {
         while(N-- > 0) {
            val <<= 1;
            val |= this.buf[pos] != 0 ? 1 : 0;
            pos = pos + 1 & 32767;
         }
      }

      this.buf_byte_idx = pos;
      return val;
   }

   public int hget1bit() {
      ++this.totbit;
      int val = this.buf[this.buf_byte_idx];
      this.buf_byte_idx = this.buf_byte_idx + 1 & 32767;
      return val;
   }

   public void hputbuf(int val) {
      int ofs = this.offset;
      this.buf[ofs++] = val & 128;
      this.buf[ofs++] = val & 64;
      this.buf[ofs++] = val & 32;
      this.buf[ofs++] = val & 16;
      this.buf[ofs++] = val & 8;
      this.buf[ofs++] = val & 4;
      this.buf[ofs++] = val & 2;
      this.buf[ofs++] = val & 1;
      if (ofs == 32768) {
         this.offset = 0;
      } else {
         this.offset = ofs;
      }

   }

   public void rewindNbits(int N) {
      this.totbit -= N;
      this.buf_byte_idx -= N;
      if (this.buf_byte_idx < 0) {
         this.buf_byte_idx += 32768;
      }

   }

   public void rewindNbytes(int N) {
      int bits = N << 3;
      this.totbit -= bits;
      this.buf_byte_idx -= bits;
      if (this.buf_byte_idx < 0) {
         this.buf_byte_idx += 32768;
      }

   }
}
