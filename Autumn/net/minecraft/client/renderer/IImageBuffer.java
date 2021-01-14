package net.minecraft.client.renderer;

import java.awt.image.BufferedImage;

public interface IImageBuffer {
   BufferedImage parseUserSkin(BufferedImage var1);

   void skinAvailable();
}
