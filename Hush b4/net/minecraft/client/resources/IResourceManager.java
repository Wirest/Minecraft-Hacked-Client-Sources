// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.resources;

import java.util.List;
import java.io.IOException;
import net.minecraft.util.ResourceLocation;
import java.util.Set;

public interface IResourceManager
{
    Set<String> getResourceDomains();
    
    IResource getResource(final ResourceLocation p0) throws IOException;
    
    List<IResource> getAllResources(final ResourceLocation p0) throws IOException;
}
