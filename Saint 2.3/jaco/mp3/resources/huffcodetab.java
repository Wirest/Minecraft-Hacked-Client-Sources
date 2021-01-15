package jaco.mp3.resources;

final class huffcodetab {
   private static final int MXOFF = 250;
   private static final int HTN = 34;
   private char tablename0 = ' ';
   private char tablename1 = ' ';
   private char tablename2 = ' ';
   private int xlen;
   private int ylen;
   private int linbits;
   private int linmax;
   private int ref;
   private int[] table = null;
   private int[] hlen = null;
   private int[][] val = null;
   private int treelen;
   private static int[][] ValTab0;
   private static int[][] ValTab1;
   private static int[][] ValTab2;
   private static int[][] ValTab3;
   private static int[][] ValTab4;
   private static int[][] ValTab5;
   private static int[][] ValTab6;
   private static int[][] ValTab7;
   private static int[][] ValTab8;
   private static int[][] ValTab9;
   private static int[][] ValTab10;
   private static int[][] ValTab11;
   private static int[][] ValTab12;
   private static int[][] ValTab13;
   private static int[][] ValTab14;
   private static int[][] ValTab15;
   private static int[][] ValTab16;
   private static int[][] ValTab24;
   private static int[][] ValTab32;
   private static int[][] ValTab33;
   public static huffcodetab[] ht;
   private static int[] bitbuf;

   static {
      // $FF: Couldn't be decompiled
   }

   private huffcodetab(String S, int XLEN, int YLEN, int LINBITS, int LINMAX, int REF, int[] TABLE, int[] HLEN, int[][] VAL, int TREELEN) {
      this.tablename0 = S.charAt(0);
      this.tablename1 = S.charAt(1);
      this.tablename2 = S.charAt(2);
      this.xlen = XLEN;
      this.ylen = YLEN;
      this.linbits = LINBITS;
      this.linmax = LINMAX;
      this.ref = REF;
      this.table = TABLE;
      this.hlen = HLEN;
      this.val = VAL;
      this.treelen = TREELEN;
   }

   public static int huffman_decoder(huffcodetab h, int[] x, int[] y, int[] v, int[] w, BitReserve br) {
      int dmask = Integer.MIN_VALUE;
      int hs = true;
      int point = 0;
      int error = 1;
      int level = dmask;
      if (h.val == null) {
         return 2;
      } else if (h.treelen == 0) {
         x[0] = y[0] = 0;
         return 0;
      } else {
         do {
            if (h.val[point][0] == 0) {
               x[0] = h.val[point][1] >>> 4;
               y[0] = h.val[point][1] & 15;
               error = 0;
               break;
            }

            if (br.hget1bit() != 0) {
               while(h.val[point][1] >= 250) {
                  point += h.val[point][1];
               }

               point += h.val[point][1];
            } else {
               while(h.val[point][0] >= 250) {
                  point += h.val[point][0];
               }

               point += h.val[point][0];
            }

            level >>>= 1;
         } while(level != 0 || point < 0);

         if (h.tablename0 != '3' || h.tablename1 != '2' && h.tablename1 != '3') {
            if (h.linbits != 0 && h.xlen - 1 == x[0]) {
               x[0] += br.hgetbits(h.linbits);
            }

            if (x[0] != 0 && br.hget1bit() != 0) {
               x[0] = -x[0];
            }

            if (h.linbits != 0 && h.ylen - 1 == y[0]) {
               y[0] += br.hgetbits(h.linbits);
            }

            if (y[0] != 0 && br.hget1bit() != 0) {
               y[0] = -y[0];
            }
         } else {
            v[0] = y[0] >> 3 & 1;
            w[0] = y[0] >> 2 & 1;
            x[0] = y[0] >> 1 & 1;
            y[0] &= 1;
            if (v[0] != 0 && br.hget1bit() != 0) {
               v[0] = -v[0];
            }

            if (w[0] != 0 && br.hget1bit() != 0) {
               w[0] = -w[0];
            }

            if (x[0] != 0 && br.hget1bit() != 0) {
               x[0] = -x[0];
            }

            if (y[0] != 0 && br.hget1bit() != 0) {
               y[0] = -y[0];
            }
         }

         return error;
      }
   }

   public static void inithuff() {
      if (ht == null) {
         ht = new huffcodetab[34];
         ht[0] = new huffcodetab("0  ", 0, 0, 0, 0, -1, (int[])null, (int[])null, ValTab0, 0);
         ht[1] = new huffcodetab("1  ", 2, 2, 0, 0, -1, (int[])null, (int[])null, ValTab1, 7);
         ht[2] = new huffcodetab("2  ", 3, 3, 0, 0, -1, (int[])null, (int[])null, ValTab2, 17);
         ht[3] = new huffcodetab("3  ", 3, 3, 0, 0, -1, (int[])null, (int[])null, ValTab3, 17);
         ht[4] = new huffcodetab("4  ", 0, 0, 0, 0, -1, (int[])null, (int[])null, ValTab4, 0);
         ht[5] = new huffcodetab("5  ", 4, 4, 0, 0, -1, (int[])null, (int[])null, ValTab5, 31);
         ht[6] = new huffcodetab("6  ", 4, 4, 0, 0, -1, (int[])null, (int[])null, ValTab6, 31);
         ht[7] = new huffcodetab("7  ", 6, 6, 0, 0, -1, (int[])null, (int[])null, ValTab7, 71);
         ht[8] = new huffcodetab("8  ", 6, 6, 0, 0, -1, (int[])null, (int[])null, ValTab8, 71);
         ht[9] = new huffcodetab("9  ", 6, 6, 0, 0, -1, (int[])null, (int[])null, ValTab9, 71);
         ht[10] = new huffcodetab("10 ", 8, 8, 0, 0, -1, (int[])null, (int[])null, ValTab10, 127);
         ht[11] = new huffcodetab("11 ", 8, 8, 0, 0, -1, (int[])null, (int[])null, ValTab11, 127);
         ht[12] = new huffcodetab("12 ", 8, 8, 0, 0, -1, (int[])null, (int[])null, ValTab12, 127);
         ht[13] = new huffcodetab("13 ", 16, 16, 0, 0, -1, (int[])null, (int[])null, ValTab13, 511);
         ht[14] = new huffcodetab("14 ", 0, 0, 0, 0, -1, (int[])null, (int[])null, ValTab14, 0);
         ht[15] = new huffcodetab("15 ", 16, 16, 0, 0, -1, (int[])null, (int[])null, ValTab15, 511);
         ht[16] = new huffcodetab("16 ", 16, 16, 1, 1, -1, (int[])null, (int[])null, ValTab16, 511);
         ht[17] = new huffcodetab("17 ", 16, 16, 2, 3, 16, (int[])null, (int[])null, ValTab16, 511);
         ht[18] = new huffcodetab("18 ", 16, 16, 3, 7, 16, (int[])null, (int[])null, ValTab16, 511);
         ht[19] = new huffcodetab("19 ", 16, 16, 4, 15, 16, (int[])null, (int[])null, ValTab16, 511);
         ht[20] = new huffcodetab("20 ", 16, 16, 6, 63, 16, (int[])null, (int[])null, ValTab16, 511);
         ht[21] = new huffcodetab("21 ", 16, 16, 8, 255, 16, (int[])null, (int[])null, ValTab16, 511);
         ht[22] = new huffcodetab("22 ", 16, 16, 10, 1023, 16, (int[])null, (int[])null, ValTab16, 511);
         ht[23] = new huffcodetab("23 ", 16, 16, 13, 8191, 16, (int[])null, (int[])null, ValTab16, 511);
         ht[24] = new huffcodetab("24 ", 16, 16, 4, 15, -1, (int[])null, (int[])null, ValTab24, 512);
         ht[25] = new huffcodetab("25 ", 16, 16, 5, 31, 24, (int[])null, (int[])null, ValTab24, 512);
         ht[26] = new huffcodetab("26 ", 16, 16, 6, 63, 24, (int[])null, (int[])null, ValTab24, 512);
         ht[27] = new huffcodetab("27 ", 16, 16, 7, 127, 24, (int[])null, (int[])null, ValTab24, 512);
         ht[28] = new huffcodetab("28 ", 16, 16, 8, 255, 24, (int[])null, (int[])null, ValTab24, 512);
         ht[29] = new huffcodetab("29 ", 16, 16, 9, 511, 24, (int[])null, (int[])null, ValTab24, 512);
         ht[30] = new huffcodetab("30 ", 16, 16, 11, 2047, 24, (int[])null, (int[])null, ValTab24, 512);
         ht[31] = new huffcodetab("31 ", 16, 16, 13, 8191, 24, (int[])null, (int[])null, ValTab24, 512);
         ht[32] = new huffcodetab("32 ", 1, 16, 0, 0, -1, (int[])null, (int[])null, ValTab32, 31);
         ht[33] = new huffcodetab("33 ", 1, 16, 0, 0, -1, (int[])null, (int[])null, ValTab33, 31);
      }
   }
}
