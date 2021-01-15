package net.minecraft.client.renderer;

import java.awt.image.BufferedImage;

public abstract interface IImageBuffer
{
  public abstract BufferedImage parseUserSkin(BufferedImage paramBufferedImage);
  
  public abstract void skinAvailable();
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\IImageBuffer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */