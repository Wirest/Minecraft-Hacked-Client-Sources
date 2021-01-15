package net.minecraft.client.resources;

import java.util.List;

public abstract interface IReloadableResourceManager
  extends IResourceManager
{
  public abstract void reloadResources(List<IResourcePack> paramList);
  
  public abstract void registerReloadListener(IResourceManagerReloadListener paramIResourceManagerReloadListener);
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\resources\IReloadableResourceManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */