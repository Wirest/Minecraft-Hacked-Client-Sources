package net.minecraft.client.resources;

import java.util.List;

public interface IReloadableResourceManager extends IResourceManager
{
    void reloadResources(List p_110541_1_);

    void registerReloadListener(IResourceManagerReloadListener p_110542_1_);
}
