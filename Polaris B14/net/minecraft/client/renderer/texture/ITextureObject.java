package net.minecraft.client.renderer.texture;

import java.io.IOException;
import net.minecraft.client.resources.IResourceManager;

public abstract interface ITextureObject
{
  public abstract void setBlurMipmap(boolean paramBoolean1, boolean paramBoolean2);
  
  public abstract void restoreLastBlurMipmap();
  
  public abstract void loadTexture(IResourceManager paramIResourceManager)
    throws IOException;
  
  public abstract int getGlTextureId();
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\texture\ITextureObject.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */