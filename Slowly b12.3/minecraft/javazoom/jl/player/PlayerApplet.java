package javazoom.jl.player;

import java.applet.Applet;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URL;
import javazoom.jl.decoder.JavaLayerException;

public class PlayerApplet extends Applet implements Runnable {
   public static final String AUDIO_PARAMETER = "audioURL";
   private Player player = null;
   private Thread playerThread = null;
   private String fileName = null;

   protected AudioDevice getAudioDevice() throws JavaLayerException {
      return FactoryRegistry.systemRegistry().createAudioDevice();
   }

   protected InputStream getAudioStream() {
      InputStream in = null;

      try {
         URL url = this.getAudioURL();
         if (url != null) {
            in = url.openStream();
         }
      } catch (IOException var3) {
         System.err.println(var3);
      }

      return in;
   }

   protected String getAudioFileName() {
      String urlString = this.fileName;
      if (urlString == null) {
         urlString = this.getParameter("audioURL");
      }

      return urlString;
   }

   protected URL getAudioURL() {
      String urlString = this.getAudioFileName();
      URL url = null;
      if (urlString != null) {
         try {
            url = new URL(this.getDocumentBase(), urlString);
         } catch (Exception var4) {
            System.err.println(var4);
         }
      }

      return url;
   }

   public void setFileName(String name) {
      this.fileName = name;
   }

   public String getFileName() {
      return this.fileName;
   }

   protected void stopPlayer() throws JavaLayerException {
      if (this.player != null) {
         Player.close();
         this.player = null;
         this.playerThread = null;
      }

   }

   protected void play(InputStream in, AudioDevice dev) throws JavaLayerException {
      this.stopPlayer();
      if (in != null && dev != null) {
         this.player = new Player(in, dev);
         this.playerThread = this.createPlayerThread();
         this.playerThread.start();
      }

   }

   protected Thread createPlayerThread() {
      return new Thread(this, "Audio player thread");
   }

   public void init() {
   }

   public void start() {
      String name = this.getAudioFileName();

      try {
         InputStream in = this.getAudioStream();
         AudioDevice dev = this.getAudioDevice();
         this.play(in, dev);
      } catch (JavaLayerException var5) {
         JavaLayerException ex = var5;
         PrintStream var3 = System.err;
         synchronized(System.err) {
            System.err.println("Unable to play " + name);
            ex.printStackTrace(System.err);
         }
      }

   }

   public void stop() {
      try {
         this.stopPlayer();
      } catch (JavaLayerException var2) {
         System.err.println(var2);
      }

   }

   public void destroy() {
   }

   public void run() {
      if (this.player != null) {
         try {
            this.player.play();
         } catch (JavaLayerException var2) {
            System.err.println("Problem playing audio: " + var2);
         }
      }

   }
}
