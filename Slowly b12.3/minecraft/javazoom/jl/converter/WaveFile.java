package javazoom.jl.converter;

public class WaveFile extends RiffFile {
   public static final int MAX_WAVE_CHANNELS = 2;
   private WaveFile.WaveFormat_Chunk wave_format = new WaveFile.WaveFormat_Chunk();
   private RiffFile.RiffChunkHeader pcm_data = new RiffFile.RiffChunkHeader();
   private long pcm_data_offset = 0L;
   private int num_samples = 0;

   public WaveFile() {
      this.pcm_data.ckID = FourCC("data");
      this.pcm_data.ckSize = 0;
      this.num_samples = 0;
   }

   public int OpenForWrite(String Filename, int SamplingRate, short BitsPerSample, short NumChannels) {
      if (Filename != null && (BitsPerSample == 8 || BitsPerSample == 16) && NumChannels >= 1 && NumChannels <= 2) {
         this.wave_format.data.Config(SamplingRate, BitsPerSample, NumChannels);
         int retcode = this.Open(Filename, 1);
         if (retcode == 0) {
            byte[] theWave = new byte[]{87, 65, 86, 69};
            retcode = this.Write(theWave, 4);
            if (retcode == 0) {
               retcode = this.Write(this.wave_format.header, 8);
               retcode = this.Write(this.wave_format.data.wFormatTag, 2);
               retcode = this.Write(this.wave_format.data.nChannels, 2);
               retcode = this.Write(this.wave_format.data.nSamplesPerSec, 4);
               retcode = this.Write(this.wave_format.data.nAvgBytesPerSec, 4);
               retcode = this.Write(this.wave_format.data.nBlockAlign, 2);
               retcode = this.Write(this.wave_format.data.nBitsPerSample, 2);
               if (retcode == 0) {
                  this.pcm_data_offset = this.CurrentFilePosition();
                  retcode = this.Write(this.pcm_data, 8);
               }
            }
         }

         return retcode;
      } else {
         return 4;
      }
   }

   public int WriteData(short[] data, int numData) {
      int extraBytes = numData * 2;
      this.pcm_data.ckSize += extraBytes;
      return super.Write(data, extraBytes);
   }

   public int Close() {
      int rc = 0;
      if (this.fmode == 1) {
         rc = this.Backpatch(this.pcm_data_offset, this.pcm_data, 8);
      }

      if (rc == 0) {
         rc = super.Close();
      }

      return rc;
   }

   public int SamplingRate() {
      return this.wave_format.data.nSamplesPerSec;
   }

   public short BitsPerSample() {
      return this.wave_format.data.nBitsPerSample;
   }

   public short NumChannels() {
      return this.wave_format.data.nChannels;
   }

   public int NumSamples() {
      return this.num_samples;
   }

   public int OpenForWrite(String Filename, WaveFile OtherWave) {
      return this.OpenForWrite(Filename, OtherWave.SamplingRate(), OtherWave.BitsPerSample(), OtherWave.NumChannels());
   }

   public long CurrentFilePosition() {
      return super.CurrentFilePosition();
   }

   public class WaveFileSample {
      public short[] chan = new short[2];
   }

   class WaveFormat_Chunk {
      public RiffFile.RiffChunkHeader header = WaveFile.this.new RiffChunkHeader();
      public WaveFile.WaveFormat_ChunkData data = WaveFile.this.new WaveFormat_ChunkData();

      public WaveFormat_Chunk() {
         this.header.ckID = WaveFile.FourCC("fmt ");
         this.header.ckSize = 16;
      }

      public int VerifyValidity() {
         boolean ret = this.header.ckID == WaveFile.FourCC("fmt ") && (this.data.nChannels == 1 || this.data.nChannels == 2) && this.data.nAvgBytesPerSec == this.data.nChannels * this.data.nSamplesPerSec * this.data.nBitsPerSample / 8 && this.data.nBlockAlign == this.data.nChannels * this.data.nBitsPerSample / 8;
         return ret ? 1 : 0;
      }
   }

   class WaveFormat_ChunkData {
      public short wFormatTag = 0;
      public short nChannels = 0;
      public int nSamplesPerSec = 0;
      public int nAvgBytesPerSec = 0;
      public short nBlockAlign = 0;
      public short nBitsPerSample = 0;

      public WaveFormat_ChunkData() {
         this.wFormatTag = 1;
         this.Config(44100, (short)16, (short)1);
      }

      public void Config(int NewSamplingRate, short NewBitsPerSample, short NewNumChannels) {
         this.nSamplesPerSec = NewSamplingRate;
         this.nChannels = NewNumChannels;
         this.nBitsPerSample = NewBitsPerSample;
         this.nAvgBytesPerSec = this.nChannels * this.nSamplesPerSec * this.nBitsPerSample / 8;
         this.nBlockAlign = (short)(this.nChannels * this.nBitsPerSample / 8);
      }
   }
}
