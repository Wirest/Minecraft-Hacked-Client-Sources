package jaco.mp3.resources;

public final class Frame {
   public static final int[][] frequencies = new int[][]{{22050, 24000, 16000, 1}, {44100, 48000, 32000, 1}, {11025, 12000, 8000, 1}};
   public static final int MPEG2_LSF = 0;
   public static final int MPEG25_LSF = 2;
   public static final int MPEG1 = 1;
   public static final int STEREO = 0;
   public static final int JOINT_STEREO = 1;
   public static final int DUAL_CHANNEL = 2;
   public static final int SINGLE_CHANNEL = 3;
   public static final int FOURTYFOUR_POINT_ONE = 0;
   public static final int FOURTYEIGHT = 1;
   public static final int THIRTYTWO = 2;
   private int h_layer;
   private int h_protection_bit;
   private int h_bitrate_index;
   private int h_padding_bit;
   private int h_mode_extension;
   private int h_version;
   private int h_mode;
   private int h_sample_frequency;
   private int h_number_of_subbands;
   private int h_intensity_stereo_bound;
   private boolean h_copyright;
   private boolean h_original;
   private double[] h_vbr_time_per_frame = new double[]{-1.0D, 384.0D, 1152.0D, 1152.0D};
   private boolean h_vbr;
   private int h_vbr_frames;
   private int h_vbr_scale;
   private int h_vbr_bytes;
   private byte[] h_vbr_toc;
   private byte syncmode;
   private Crc16 crc;
   public short checksum;
   public int framesize;
   public int nSlots;
   private int _headerstring;
   public static final int[][][] bitrates = new int[][][]{{{0, 32000, 48000, 56000, 64000, 80000, 96000, 112000, 128000, 144000, 160000, 176000, 192000, 224000, 256000, 0}, {0, 8000, 16000, 24000, 32000, 40000, 48000, 56000, 64000, 80000, 96000, 112000, 128000, 144000, 160000, 0}, {0, 8000, 16000, 24000, 32000, 40000, 48000, 56000, 64000, 80000, 96000, 112000, 128000, 144000, 160000, 0}}, {{0, 32000, 64000, 96000, 128000, 160000, 192000, 224000, 256000, 288000, 320000, 352000, 384000, 416000, 448000, 0}, {0, 32000, 48000, 56000, 64000, 80000, 96000, 112000, 128000, 160000, 192000, 224000, 256000, 320000, 384000, 0}, {0, 32000, 40000, 48000, 56000, 64000, 80000, 96000, 112000, 128000, 160000, 192000, 224000, 256000, 320000, 0}}, {{0, 32000, 48000, 56000, 64000, 80000, 96000, 112000, 128000, 144000, 160000, 176000, 192000, 224000, 256000, 0}, {0, 8000, 16000, 24000, 32000, 40000, 48000, 56000, 64000, 80000, 96000, 112000, 128000, 144000, 160000, 0}, {0, 8000, 16000, 24000, 32000, 40000, 48000, 56000, 64000, 80000, 96000, 112000, 128000, 144000, 160000, 0}}};
   public static final String[][][] bitrate_str = new String[][][]{{{"free format", "32 kbit/s", "48 kbit/s", "56 kbit/s", "64 kbit/s", "80 kbit/s", "96 kbit/s", "112 kbit/s", "128 kbit/s", "144 kbit/s", "160 kbit/s", "176 kbit/s", "192 kbit/s", "224 kbit/s", "256 kbit/s", "forbidden"}, {"free format", "8 kbit/s", "16 kbit/s", "24 kbit/s", "32 kbit/s", "40 kbit/s", "48 kbit/s", "56 kbit/s", "64 kbit/s", "80 kbit/s", "96 kbit/s", "112 kbit/s", "128 kbit/s", "144 kbit/s", "160 kbit/s", "forbidden"}, {"free format", "8 kbit/s", "16 kbit/s", "24 kbit/s", "32 kbit/s", "40 kbit/s", "48 kbit/s", "56 kbit/s", "64 kbit/s", "80 kbit/s", "96 kbit/s", "112 kbit/s", "128 kbit/s", "144 kbit/s", "160 kbit/s", "forbidden"}}, {{"free format", "32 kbit/s", "64 kbit/s", "96 kbit/s", "128 kbit/s", "160 kbit/s", "192 kbit/s", "224 kbit/s", "256 kbit/s", "288 kbit/s", "320 kbit/s", "352 kbit/s", "384 kbit/s", "416 kbit/s", "448 kbit/s", "forbidden"}, {"free format", "32 kbit/s", "48 kbit/s", "56 kbit/s", "64 kbit/s", "80 kbit/s", "96 kbit/s", "112 kbit/s", "128 kbit/s", "160 kbit/s", "192 kbit/s", "224 kbit/s", "256 kbit/s", "320 kbit/s", "384 kbit/s", "forbidden"}, {"free format", "32 kbit/s", "40 kbit/s", "48 kbit/s", "56 kbit/s", "64 kbit/s", "80 kbit/s", "96 kbit/s", "112 kbit/s", "128 kbit/s", "160 kbit/s", "192 kbit/s", "224 kbit/s", "256 kbit/s", "320 kbit/s", "forbidden"}}, {{"free format", "32 kbit/s", "48 kbit/s", "56 kbit/s", "64 kbit/s", "80 kbit/s", "96 kbit/s", "112 kbit/s", "128 kbit/s", "144 kbit/s", "160 kbit/s", "176 kbit/s", "192 kbit/s", "224 kbit/s", "256 kbit/s", "forbidden"}, {"free format", "8 kbit/s", "16 kbit/s", "24 kbit/s", "32 kbit/s", "40 kbit/s", "48 kbit/s", "56 kbit/s", "64 kbit/s", "80 kbit/s", "96 kbit/s", "112 kbit/s", "128 kbit/s", "144 kbit/s", "160 kbit/s", "forbidden"}, {"free format", "8 kbit/s", "16 kbit/s", "24 kbit/s", "32 kbit/s", "40 kbit/s", "48 kbit/s", "56 kbit/s", "64 kbit/s", "80 kbit/s", "96 kbit/s", "112 kbit/s", "128 kbit/s", "144 kbit/s", "160 kbit/s", "forbidden"}}};

   Frame() {
      this.syncmode = SoundStream.INITIAL_SYNC;
      this._headerstring = -1;
   }

   public String toString() {
      StringBuffer buffer = new StringBuffer(200);
      buffer.append("Layer ");
      buffer.append(this.layer_string());
      buffer.append(" frame ");
      buffer.append(this.mode_string());
      buffer.append(' ');
      buffer.append(this.version_string());
      if (!this.checksums()) {
         buffer.append(" no");
      }

      buffer.append(" checksums");
      buffer.append(' ');
      buffer.append(this.sample_frequency_string());
      buffer.append(',');
      buffer.append(' ');
      buffer.append(this.bitrate_string());
      String s = buffer.toString();
      return s;
   }

   void read_header(SoundStream stream, Crc16[] crcp) throws BitstreamException {
      boolean sync = false;

      int headerstring;
      do {
         headerstring = stream.syncHeader(this.syncmode);
         this._headerstring = headerstring;
         if (this.syncmode == SoundStream.INITIAL_SYNC) {
            this.h_version = headerstring >>> 19 & 1;
            if ((headerstring >>> 20 & 1) == 0) {
               if (this.h_version != 0) {
                  throw stream.newBitstreamException(256);
               }

               this.h_version = 2;
            }

            if ((this.h_sample_frequency = headerstring >>> 10 & 3) == 3) {
               throw stream.newBitstreamException(256);
            }
         }

         this.h_layer = 4 - (headerstring >>> 17) & 3;
         this.h_protection_bit = headerstring >>> 16 & 1;
         this.h_bitrate_index = headerstring >>> 12 & 15;
         this.h_padding_bit = headerstring >>> 9 & 1;
         this.h_mode = headerstring >>> 6 & 3;
         this.h_mode_extension = headerstring >>> 4 & 3;
         if (this.h_mode == 1) {
            this.h_intensity_stereo_bound = (this.h_mode_extension << 2) + 4;
         } else {
            this.h_intensity_stereo_bound = 0;
         }

         if ((headerstring >>> 3 & 1) == 1) {
            this.h_copyright = true;
         }

         if ((headerstring >>> 2 & 1) == 1) {
            this.h_original = true;
         }

         if (this.h_layer == 1) {
            this.h_number_of_subbands = 32;
         } else {
            int channel_bitrate = this.h_bitrate_index;
            if (this.h_mode != 3) {
               if (channel_bitrate == 4) {
                  channel_bitrate = 1;
               } else {
                  channel_bitrate -= 4;
               }
            }

            if (channel_bitrate != 1 && channel_bitrate != 2) {
               if (this.h_sample_frequency != 1 && (channel_bitrate < 3 || channel_bitrate > 5)) {
                  this.h_number_of_subbands = 30;
               } else {
                  this.h_number_of_subbands = 27;
               }
            } else if (this.h_sample_frequency == 2) {
               this.h_number_of_subbands = 12;
            } else {
               this.h_number_of_subbands = 8;
            }
         }

         if (this.h_intensity_stereo_bound > this.h_number_of_subbands) {
            this.h_intensity_stereo_bound = this.h_number_of_subbands;
         }

         this.calculate_framesize();
         int framesizeloaded = stream.read_frame_data(this.framesize);
         if (this.framesize >= 0 && framesizeloaded != this.framesize) {
            throw stream.newBitstreamException(261);
         }

         if (stream.isSyncCurrentPosition(this.syncmode)) {
            if (this.syncmode == SoundStream.INITIAL_SYNC) {
               this.syncmode = SoundStream.STRICT_SYNC;
               stream.set_syncword(headerstring & -521024);
            }

            sync = true;
         } else {
            stream.unreadFrame();
         }
      } while(!sync);

      stream.parse_frame();
      if (this.h_protection_bit == 0) {
         this.checksum = (short)stream.get_bits(16);
         if (this.crc == null) {
            this.crc = new Crc16();
         }

         this.crc.add_bits(headerstring, 16);
         crcp[0] = this.crc;
      } else {
         crcp[0] = null;
      }

   }

   void parseVBR(byte[] firstframe) throws BitstreamException {
      String xing = "Xing";
      byte[] tmp = new byte[4];
      int offset = false;
      byte offset;
      if (this.h_version == 1) {
         if (this.h_mode == 3) {
            offset = 17;
         } else {
            offset = 32;
         }
      } else if (this.h_mode == 3) {
         offset = 9;
      } else {
         offset = 17;
      }

      try {
         System.arraycopy(firstframe, offset, tmp, 0, 4);
         if (xing.equals(new String(tmp))) {
            this.h_vbr = true;
            this.h_vbr_frames = -1;
            this.h_vbr_bytes = -1;
            this.h_vbr_scale = -1;
            this.h_vbr_toc = new byte[100];
            int length = 4;
            byte[] flags = new byte[4];
            System.arraycopy(firstframe, offset + length, flags, 0, flags.length);
            int length = length + flags.length;
            if ((flags[3] & 1) != 0) {
               System.arraycopy(firstframe, offset + length, tmp, 0, tmp.length);
               this.h_vbr_frames = tmp[0] << 24 & -16777216 | tmp[1] << 16 & 16711680 | tmp[2] << 8 & '\uff00' | tmp[3] & 255;
               length += 4;
            }

            if ((flags[3] & 2) != 0) {
               System.arraycopy(firstframe, offset + length, tmp, 0, tmp.length);
               this.h_vbr_bytes = tmp[0] << 24 & -16777216 | tmp[1] << 16 & 16711680 | tmp[2] << 8 & '\uff00' | tmp[3] & 255;
               length += 4;
            }

            if ((flags[3] & 4) != 0) {
               System.arraycopy(firstframe, offset + length, this.h_vbr_toc, 0, this.h_vbr_toc.length);
               length += this.h_vbr_toc.length;
            }

            if ((flags[3] & 8) != 0) {
               System.arraycopy(firstframe, offset + length, tmp, 0, tmp.length);
               this.h_vbr_scale = tmp[0] << 24 & -16777216 | tmp[1] << 16 & 16711680 | tmp[2] << 8 & '\uff00' | tmp[3] & 255;
               length += 4;
            }
         }
      } catch (ArrayIndexOutOfBoundsException var8) {
         throw new BitstreamException("XingVBRHeader Corrupted", var8);
      }

      String vbri = "VBRI";
      offset = 32;

      try {
         System.arraycopy(firstframe, offset, tmp, 0, 4);
         if (vbri.equals(new String(tmp))) {
            this.h_vbr = true;
            this.h_vbr_frames = -1;
            this.h_vbr_bytes = -1;
            this.h_vbr_scale = -1;
            this.h_vbr_toc = new byte[100];
            int length = 10;
            System.arraycopy(firstframe, offset + length, tmp, 0, tmp.length);
            this.h_vbr_bytes = tmp[0] << 24 & -16777216 | tmp[1] << 16 & 16711680 | tmp[2] << 8 & '\uff00' | tmp[3] & 255;
            int length = length + 4;
            System.arraycopy(firstframe, offset + length, tmp, 0, tmp.length);
            this.h_vbr_frames = tmp[0] << 24 & -16777216 | tmp[1] << 16 & 16711680 | tmp[2] << 8 & '\uff00' | tmp[3] & 255;
            length += 4;
         }

      } catch (ArrayIndexOutOfBoundsException var7) {
         throw new BitstreamException("VBRIVBRHeader Corrupted", var7);
      }
   }

   public int version() {
      return this.h_version;
   }

   public int layer() {
      return this.h_layer;
   }

   public int bitrate_index() {
      return this.h_bitrate_index;
   }

   public int sample_frequency() {
      return this.h_sample_frequency;
   }

   public int frequency() {
      return frequencies[this.h_version][this.h_sample_frequency];
   }

   public int mode() {
      return this.h_mode;
   }

   public boolean checksums() {
      return this.h_protection_bit == 0;
   }

   public boolean copyright() {
      return this.h_copyright;
   }

   public boolean original() {
      return this.h_original;
   }

   public boolean vbr() {
      return this.h_vbr;
   }

   public int vbr_scale() {
      return this.h_vbr_scale;
   }

   public byte[] vbr_toc() {
      return this.h_vbr_toc;
   }

   public boolean checksum_ok() {
      return this.checksum == this.crc.checksum();
   }

   public boolean padding() {
      return this.h_padding_bit != 0;
   }

   public int slots() {
      return this.nSlots;
   }

   public int mode_extension() {
      return this.h_mode_extension;
   }

   public int calculate_framesize() {
      if (this.h_layer == 1) {
         this.framesize = 12 * bitrates[this.h_version][0][this.h_bitrate_index] / frequencies[this.h_version][this.h_sample_frequency];
         if (this.h_padding_bit != 0) {
            ++this.framesize;
         }

         this.framesize <<= 2;
         this.nSlots = 0;
      } else {
         this.framesize = 144 * bitrates[this.h_version][this.h_layer - 1][this.h_bitrate_index] / frequencies[this.h_version][this.h_sample_frequency];
         if (this.h_version == 0 || this.h_version == 2) {
            this.framesize >>= 1;
         }

         if (this.h_padding_bit != 0) {
            ++this.framesize;
         }

         if (this.h_layer == 3) {
            if (this.h_version == 1) {
               this.nSlots = this.framesize - (this.h_mode == 3 ? 17 : 32) - (this.h_protection_bit != 0 ? 0 : 2) - 4;
            } else {
               this.nSlots = this.framesize - (this.h_mode == 3 ? 9 : 17) - (this.h_protection_bit != 0 ? 0 : 2) - 4;
            }
         } else {
            this.nSlots = 0;
         }
      }

      this.framesize -= 4;
      return this.framesize;
   }

   public int max_number_of_frames(int streamsize) {
      if (this.h_vbr) {
         return this.h_vbr_frames;
      } else {
         return this.framesize + 4 - this.h_padding_bit == 0 ? 0 : streamsize / (this.framesize + 4 - this.h_padding_bit);
      }
   }

   public int min_number_of_frames(int streamsize) {
      if (this.h_vbr) {
         return this.h_vbr_frames;
      } else {
         return this.framesize + 5 - this.h_padding_bit == 0 ? 0 : streamsize / (this.framesize + 5 - this.h_padding_bit);
      }
   }

   public float ms_per_frame() {
      if (!this.h_vbr) {
         float[][] ms_per_frame_array = new float[][]{{8.707483F, 8.0F, 12.0F}, {26.12245F, 24.0F, 36.0F}, {26.12245F, 24.0F, 36.0F}};
         return ms_per_frame_array[this.h_layer - 1][this.h_sample_frequency];
      } else {
         double tpf = this.h_vbr_time_per_frame[this.layer()] / (double)this.frequency();
         if (this.h_version == 0 || this.h_version == 2) {
            tpf /= 2.0D;
         }

         return (float)(tpf * 1000.0D);
      }
   }

   public float total_ms(int streamsize) {
      return (float)this.max_number_of_frames(streamsize) * this.ms_per_frame();
   }

   public int getSyncHeader() {
      return this._headerstring;
   }

   public String layer_string() {
      switch(this.h_layer) {
      case 1:
         return "I";
      case 2:
         return "II";
      case 3:
         return "III";
      default:
         return null;
      }
   }

   public String bitrate_string() {
      return this.h_vbr ? Integer.toString(this.bitrate() / 1000) + " kb/s" : bitrate_str[this.h_version][this.h_layer - 1][this.h_bitrate_index];
   }

   public int bitrate() {
      return this.h_vbr ? (int)((float)(this.h_vbr_bytes * 8) / (this.ms_per_frame() * (float)this.h_vbr_frames)) * 1000 : bitrates[this.h_version][this.h_layer - 1][this.h_bitrate_index];
   }

   public int bitrate_instant() {
      return bitrates[this.h_version][this.h_layer - 1][this.h_bitrate_index];
   }

   public String sample_frequency_string() {
      switch(this.h_sample_frequency) {
      case 0:
         if (this.h_version == 1) {
            return "44.1 kHz";
         } else {
            if (this.h_version == 0) {
               return "22.05 kHz";
            }

            return "11.025 kHz";
         }
      case 1:
         if (this.h_version == 1) {
            return "48 kHz";
         } else {
            if (this.h_version == 0) {
               return "24 kHz";
            }

            return "12 kHz";
         }
      case 2:
         if (this.h_version == 1) {
            return "32 kHz";
         } else {
            if (this.h_version == 0) {
               return "16 kHz";
            }

            return "8 kHz";
         }
      default:
         return null;
      }
   }

   public String mode_string() {
      switch(this.h_mode) {
      case 0:
         return "Stereo";
      case 1:
         return "Joint stereo";
      case 2:
         return "Dual channel";
      case 3:
         return "Single channel";
      default:
         return null;
      }
   }

   public String version_string() {
      switch(this.h_version) {
      case 0:
         return "MPEG-2 LSF";
      case 1:
         return "MPEG-1";
      case 2:
         return "MPEG-2.5 LSF";
      default:
         return null;
      }
   }

   public int number_of_subbands() {
      return this.h_number_of_subbands;
   }

   public int intensity_stereo_bound() {
      return this.h_intensity_stereo_bound;
   }
}
