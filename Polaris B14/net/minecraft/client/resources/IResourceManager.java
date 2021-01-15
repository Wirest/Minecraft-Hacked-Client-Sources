package net.minecraft.client.resources;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import net.minecraft.util.ResourceLocation;

public abstract interface IResourceManager
{
  public abstract Set<String> getResourceDomains();
  
  public abstract IResource getResource(ResourceLocation paramResourceLocation)
    throws IOException;
  
  public abstract List<IResource> getAllResources(ResourceLocation paramResourceLocation)
    throws IOException;
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\resources\IResourceManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */