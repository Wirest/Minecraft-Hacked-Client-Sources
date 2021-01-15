package net.minecraft.client.resources;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.IMetadataSerializer;
import net.minecraft.util.ResourceLocation;

public abstract interface IResourcePack
{
  public abstract InputStream getInputStream(ResourceLocation paramResourceLocation)
    throws IOException;
  
  public abstract boolean resourceExists(ResourceLocation paramResourceLocation);
  
  public abstract Set<String> getResourceDomains();
  
  public abstract <T extends IMetadataSection> T getPackMetadata(IMetadataSerializer paramIMetadataSerializer, String paramString)
    throws IOException;
  
  public abstract BufferedImage getPackImage()
    throws IOException;
  
  public abstract String getPackName();
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\resources\IResourcePack.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */