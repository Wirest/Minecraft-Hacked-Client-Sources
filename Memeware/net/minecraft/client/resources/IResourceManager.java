package net.minecraft.client.resources;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import net.minecraft.util.ResourceLocation;

public interface IResourceManager {
    Set getResourceDomains();

    IResource getResource(ResourceLocation var1) throws IOException;

    List getAllResources(ResourceLocation var1) throws IOException;
}
