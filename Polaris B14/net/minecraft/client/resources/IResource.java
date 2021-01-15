package net.minecraft.client.resources;

import java.io.InputStream;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.util.ResourceLocation;

public abstract interface IResource
{
  public abstract ResourceLocation getResourceLocation();
  
  public abstract InputStream getInputStream();
  
  public abstract boolean hasMetadata();
  
  public abstract <T extends IMetadataSection> T getMetadata(String paramString);
  
  public abstract String getResourcePackName();
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\resources\IResource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */