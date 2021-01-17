package javazoom.jl.player.advanced;

import java.io.InputStream;
import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.BitstreamException;
import javazoom.jl.decoder.Decoder;
import javazoom.jl.decoder.Header;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.decoder.SampleBuffer;
import javazoom.jl.player.AudioDevice;
import javazoom.jl.player.FactoryRegistry;

public class AdvancedPlayer {
   private static Bitstream bitstream;
   private Decoder decoder;
   private static AudioDevice audio;
   public static boolean closed = false;
   private boolean complete;
   private static int lastPosition = 0;
   private PlaybackListener listener;

   public AdvancedPlayer(InputStream stream) throws JavaLayerException {
      this(stream, (AudioDevice)null);
   }

   public AdvancedPlayer(InputStream stream, AudioDevice device) throws JavaLayerException {
      this.complete = false;
      bitstream = new Bitstream(stream);
      if (device != null) {
         audio = device;
      } else {
         audio = FactoryRegistry.systemRegistry().createAudioDevice();
      }

      audio.open(this.decoder = new Decoder());
   }

   public void play() throws JavaLayerException {
      this.play(Integer.MAX_VALUE);
   }

   public boolean play(int frames) throws JavaLayerException {
      boolean ret = true;
      if (this.listener != null) {
         this.listener.playbackStarted(this.createEvent(PlaybackEvent.STARTED));
      }

      while(frames-- > 0 && ret) {
         ret = this.decodeFrame();
      }

      AudioDevice out = audio;
      if (out != null) {
         out.flush();
         synchronized(this) {
            this.complete = !closed;
            close();
         }

         if (this.listener != null) {
            this.listener.playbackFinished(this.createEvent(out, PlaybackEvent.STOPPED));
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

   protected boolean skipFrame() throws JavaLayerException {
      Header h = bitstream.readFrame();
      if (h == null) {
         return false;
      } else {
         bitstream.closeFrame();
         return true;
      }
   }

   public boolean play(int start, int end) throws JavaLayerException {
      boolean ret = true;

      for(int var4 = start; var4-- > 0 && ret; ret = this.skipFrame()) {
         ;
      }

      return this.play(end - start);
   }

   private PlaybackEvent createEvent(int id) {
      return this.createEvent(audio, id);
   }

   private PlaybackEvent createEvent(AudioDevice dev, int id) {
      return new PlaybackEvent(this, id, dev.getPosition());
   }

   public void setPlayBackListener(PlaybackListener listener) {
      this.listener = listener;
   }

   public PlaybackListener getPlayBackListener() {
      return this.listener;
   }

   public void stop() {
      this.listener.playbackFinished(this.createEvent(PlaybackEvent.STOPPED));
      close();
   }
}
