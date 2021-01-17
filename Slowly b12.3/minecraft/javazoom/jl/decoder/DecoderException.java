package javazoom.jl.decoder;

public class DecoderException extends JavaLayerException implements DecoderErrors {
   private int errorcode;

   public DecoderException(String msg, Throwable t) {
      super(msg, t);
      this.errorcode = 512;
   }

   public DecoderException(int errorcode, Throwable t) {
      this(getErrorString(errorcode), t);
      this.errorcode = errorcode;
   }

   public int getErrorCode() {
      return this.errorcode;
   }

   public static String getErrorString(int errorcode) {
      return "Decoder errorcode " + Integer.toHexString(errorcode);
   }
}
