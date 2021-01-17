package javazoom.jl.player;

import javazoom.jl.decoder.JavaLayerException;

public abstract class AudioDeviceFactory {
   public abstract AudioDevice createAudioDevice() throws JavaLayerException;

   protected AudioDevice instantiate(ClassLoader loader, String name) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
      AudioDevice dev = null;
      Class cls = null;
      if (loader == null) {
         cls = Class.forName(name);
      } else {
         cls = loader.loadClass(name);
      }

      Object o = cls.newInstance();
      dev = (AudioDevice)o;
      return dev;
   }
}
