// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.resources;

import java.awt.image.BufferedImage;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.IMetadataSerializer;
import java.util.Set;
import java.io.IOException;
import java.io.InputStream;
import net.minecraft.util.ResourceLocation;

public interface IResourcePack
{
    InputStream getInputStream(final ResourceLocation p0) throws IOException;
    
    boolean resourceExists(final ResourceLocation p0);
    
    Set<String> getResourceDomains();
    
     <T extends IMetadataSection> T getPackMetadata(final IMetadataSerializer p0, final String p1) throws IOException;
    
    BufferedImage getPackImage() throws IOException;
    
    String getPackName();
}
