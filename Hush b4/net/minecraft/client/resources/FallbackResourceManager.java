// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.resources;

import java.io.OutputStream;
import java.io.PrintStream;
import java.io.ByteArrayOutputStream;
import java.util.Iterator;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileNotFoundException;
import net.minecraft.util.ResourceLocation;
import java.util.Set;
import com.google.common.collect.Lists;
import org.apache.logging.log4j.LogManager;
import net.minecraft.client.resources.data.IMetadataSerializer;
import java.util.List;
import org.apache.logging.log4j.Logger;

public class FallbackResourceManager implements IResourceManager
{
    private static final Logger logger;
    protected final List<IResourcePack> resourcePacks;
    private final IMetadataSerializer frmMetadataSerializer;
    
    static {
        logger = LogManager.getLogger();
    }
    
    public FallbackResourceManager(final IMetadataSerializer frmMetadataSerializerIn) {
        this.resourcePacks = (List<IResourcePack>)Lists.newArrayList();
        this.frmMetadataSerializer = frmMetadataSerializerIn;
    }
    
    public void addResourcePack(final IResourcePack resourcePack) {
        this.resourcePacks.add(resourcePack);
    }
    
    @Override
    public Set<String> getResourceDomains() {
        return null;
    }
    
    @Override
    public IResource getResource(final ResourceLocation location) throws IOException {
        IResourcePack iresourcepack = null;
        final ResourceLocation resourcelocation = getLocationMcmeta(location);
        for (int i = this.resourcePacks.size() - 1; i >= 0; --i) {
            final IResourcePack iresourcepack2 = this.resourcePacks.get(i);
            if (iresourcepack == null && iresourcepack2.resourceExists(resourcelocation)) {
                iresourcepack = iresourcepack2;
            }
            if (iresourcepack2.resourceExists(location)) {
                InputStream inputstream = null;
                if (iresourcepack != null) {
                    inputstream = this.getInputStream(resourcelocation, iresourcepack);
                }
                return new SimpleResource(iresourcepack2.getPackName(), location, this.getInputStream(location, iresourcepack2), inputstream, this.frmMetadataSerializer);
            }
        }
        throw new FileNotFoundException(location.toString());
    }
    
    protected InputStream getInputStream(final ResourceLocation location, final IResourcePack resourcePack) throws IOException {
        final InputStream inputstream = resourcePack.getInputStream(location);
        return FallbackResourceManager.logger.isDebugEnabled() ? new InputStreamLeakedResourceLogger(inputstream, location, resourcePack.getPackName()) : inputstream;
    }
    
    @Override
    public List<IResource> getAllResources(final ResourceLocation location) throws IOException {
        final List<IResource> list = (List<IResource>)Lists.newArrayList();
        final ResourceLocation resourcelocation = getLocationMcmeta(location);
        for (final IResourcePack iresourcepack : this.resourcePacks) {
            if (iresourcepack.resourceExists(location)) {
                final InputStream inputstream = iresourcepack.resourceExists(resourcelocation) ? this.getInputStream(resourcelocation, iresourcepack) : null;
                list.add(new SimpleResource(iresourcepack.getPackName(), location, this.getInputStream(location, iresourcepack), inputstream, this.frmMetadataSerializer));
            }
        }
        if (list.isEmpty()) {
            throw new FileNotFoundException(location.toString());
        }
        return list;
    }
    
    static ResourceLocation getLocationMcmeta(final ResourceLocation location) {
        return new ResourceLocation(location.getResourceDomain(), String.valueOf(location.getResourcePath()) + ".mcmeta");
    }
    
    static class InputStreamLeakedResourceLogger extends InputStream
    {
        private final InputStream field_177330_a;
        private final String field_177328_b;
        private boolean field_177329_c;
        
        public InputStreamLeakedResourceLogger(final InputStream p_i46093_1_, final ResourceLocation location, final String p_i46093_3_) {
            this.field_177329_c = false;
            this.field_177330_a = p_i46093_1_;
            final ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
            new Exception().printStackTrace(new PrintStream(bytearrayoutputstream));
            this.field_177328_b = "Leaked resource: '" + location + "' loaded from pack: '" + p_i46093_3_ + "'\n" + bytearrayoutputstream.toString();
        }
        
        @Override
        public void close() throws IOException {
            this.field_177330_a.close();
            this.field_177329_c = true;
        }
        
        @Override
        protected void finalize() throws Throwable {
            if (!this.field_177329_c) {
                FallbackResourceManager.logger.warn(this.field_177328_b);
            }
            super.finalize();
        }
        
        @Override
        public int read() throws IOException {
            return this.field_177330_a.read();
        }
    }
}
