package javazoom.jl.decoder;

public class OutputChannels {
   public static final int BOTH_CHANNELS = 0;
   public static final int LEFT_CHANNEL = 1;
   public static final int RIGHT_CHANNEL = 2;
   public static final int DOWNMIX_CHANNELS = 3;
   public static final OutputChannels LEFT = new OutputChannels(1);
   public static final OutputChannels RIGHT = new OutputChannels(2);
   public static final OutputChannels BOTH = new OutputChannels(0);
   public static final OutputChannels DOWNMIX = new OutputChannels(3);
   private int outputChannels;

   public static OutputChannels fromInt(int code) {
      switch(code) {
      case 0:
         return BOTH;
      case 1:
         return LEFT;
      case 2:
         return RIGHT;
      case 3:
         return DOWNMIX;
      default:
         throw new IllegalArgumentException("Invalid channel code: " + code);
      }
   }

   private OutputChannels(int channels) {
      this.outputChannels = channels;
      if (channels < 0 || channels > 3) {
         throw new IllegalArgumentException("channels");
      }
   }

   public int getChannelsOutputCode() {
      return this.outputChannels;
   }

   public int getChannelCount() {
      int count = this.outputChannels == 0 ? 2 : 1;
      return count;
   }

   public boolean equals(Object o) {
      boolean equals = false;
      if (o instanceof OutputChannels) {
         OutputChannels oc = (OutputChannels)o;
         equals = oc.outputChannels == this.outputChannels;
      }

      return equals;
   }

   public int hashCode() {
      return this.outputChannels;
   }
}
