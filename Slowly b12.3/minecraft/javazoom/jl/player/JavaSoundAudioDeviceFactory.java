package javazoom.jl.player;

import javazoom.jl.decoder.JavaLayerException;

public class JavaSoundAudioDeviceFactory extends AudioDeviceFactory {
   private boolean tested = false;
   private static final String DEVICE_CLASS_NAME = "javazoom.jl.player.JavaSoundAudioDevice";

   public synchronized AudioDevice createAudioDevice() throws JavaLayerException {
      if (!this.tested) {
         this.testAudioDevice();
         this.tested = true;
      }

      try {
         return this.createAudioDeviceImpl();
      } catch (Exception var2) {
         throw new JavaLayerException("unable to create JavaSound device: " + var2);
      } catch (LinkageError var3) {
         throw new JavaLayerException("unable to create JavaSound device: " + var3);
      }
   }

   protected JavaSoundAudioDevice createAudioDeviceImpl() throws JavaLayerException {
      ClassLoader loader = this.getClass().getClassLoader();

      try {
         JavaSoundAudioDevice dev = (JavaSoundAudioDevice)this.instantiate(loader, "javazoom.jl.player.JavaSoundAudioDevice");
         return dev;
      } catch (Exception var3) {
         throw new JavaLayerException("Cannot create JavaSound device", var3);
      } catch (LinkageError var4) {
         throw new JavaLayerException("Cannot create JavaSound device", var4);
      }
   }

   public void testAudioDevice() throws JavaLayerException {
      JavaSoundAudioDevice dev = this.createAudioDeviceImpl();
      dev.test();
   }
}
