package net.minecraft.client.resources;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import net.minecraft.util.ResourceLocation;

public interface IResourceManager
{
    Set getResourceDomains();

    IResource getResource(ResourceLocation p_110536_1_) throws IOException;

    List getAllResources(ResourceLocation p_135056_1_) throws IOException;
}
