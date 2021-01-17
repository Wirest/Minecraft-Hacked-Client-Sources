package javazoom.jl.player;

import java.io.InputStream;
import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.BitstreamException;
import javazoom.jl.decoder.Decoder;
import javazoom.jl.decoder.Header;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.decoder.SampleBuffer;

public class Player {
   private int frame;
   private static Bitstream bitstream;
   private Decoder decoder;
   private static AudioDevice audio;
   public static boolean closed = false;
   private boolean complete;
   private static int lastPosition = 0;

   public Player(InputStream stream) throws JavaLayerException {
      this(stream, (AudioDevice)null);
   }

   public Player(InputStream stream, AudioDevice device) throws JavaLayerException {
      this.frame = 0;
      this.complete = false;
      bitstream = new Bitstream(stream);
      this.decoder = new Decoder();
      if (device != null) {
         audio = device;
      } else {
         FactoryRegistry r = FactoryRegistry.systemRegistry();
         audio = r.createAudioDevice();
      }

      audio.open(this.decoder);
   }

   public void play() throws JavaLayerException {
      this.play(Integer.MAX_VALUE);
   }

   public boolean play(int frames) throws JavaLayerException {
      boolean ret;
      for(ret = true; frames-- > 0 && ret; ret = this.decodeFrame()) {
         ;
      }

      if (!ret) {
         AudioDevice out = audio;
         if (out != null) {
            out.flush();
            synchronized(this) {
               this.complete = !closed;
               close();
            }
         }
      }

      return ret;
   }

   public static void close() {
      AudioDevice out = audio;
      if (out != null) {
         closed = true;
         audio = null;
         out.close();
         lastPosition = out.getPosition();

         try {
            bitstream.close();
         } catch (BitstreamException var2) {
            ;
         }
      }

   }

   public synchronized boolean isComplete() {
      return this.complete;
   }

   public int getPosition() {
      int position = lastPosition;
      AudioDevice out = audio;
      if (out != null) {
         position = out.getPosition();
      }

      return position;
   }

   protected boolean decodeFrame() throws JavaLayerException {
      try {
         AudioDevice out = audio;
         if (out == null) {
            return false;
         } else {
            Header h = bitstream.readFrame();
            if (h == null) {
               return false;
            } else {
               SampleBuffer output = (SampleBuffer)this.decoder.decodeFrame(h, bitstream);
               synchronized(this) {
                  out = audio;
                  if (out != null) {
                     out.write(output.getBuffer(), 0, output.getBufferLength());
                  }
               }

               bitstream.closeFrame();
               return true;
            }
         }
      } catch (RuntimeException var6) {
         throw new JavaLayerException("Exception decoding audio frame", var6);
      }
   }
}
