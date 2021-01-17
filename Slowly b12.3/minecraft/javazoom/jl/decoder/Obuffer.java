package javazoom.jl.decoder;

public abstract class Obuffer {
   public static final int OBUFFERSIZE = 2304;
   public static final int MAXCHANNELS = 2;

   public abstract void append(int var1, short var2);

   public void appendSamples(int channel, float[] f) {
      int i = 0;

      while(i < 32) {
         short s = this.clip(f[i++]);
         this.append(channel, s);
      }

   }

   private final short clip(float sample) {
      return sample > 32767.0F ? 32767 : (sample < -32768.0F ? -32768 : (short)((int)sample));
   }

   public abstract void write_buffer(int var1);

   public abstract void close();

   public abstract void clear_buffer();

   public abstract void set_stop_flag();
}
