package net.minecraft.client.resources;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Lists;

import net.minecraft.client.resources.data.IMetadataSerializer;
import net.minecraft.util.ResourceLocation;

public class FallbackResourceManager implements IResourceManager
{
    private static final Logger logger = LogManager.getLogger();
    protected final List resourcePacks = Lists.newArrayList();
    private final IMetadataSerializer frmMetadataSerializer;

    public FallbackResourceManager(IMetadataSerializer frmMetadataSerializerIn)
    {
        this.frmMetadataSerializer = frmMetadataSerializerIn;
    }

    public void addResourcePack(IResourcePack resourcePack)
    {
        this.resourcePacks.add(resourcePack);
    }

    @Override
	public Set getResourceDomains()
    {
        return null;
    }

    @Override
	public IResource getResource(ResourceLocation location) throws IOException
    {
        IResourcePack var2 = null;
        ResourceLocation var3 = getLocationMcmeta(location);

        for (int var4 = this.resourcePacks.size() - 1; var4 >= 0; --var4)
        {
            IResourcePack var5 = (IResourcePack)this.resourcePacks.get(var4);

            if (var2 == null && var5.resourceExists(var3))
            {
                var2 = var5;
            }

            if (var5.resourceExists(location))
            {
                InputStream var6 = null;

                if (var2 != null)
                {
                    var6 = this.getInputStream(var3, var2);
                }

                return new SimpleResource(var5.getPackName(), location, this.getInputStream(location, var5), var6, this.frmMetadataSerializer);
            }
        }

        throw new FileNotFoundException(location.toString());
    }

    @SuppressWarnings("resource")
	protected InputStream getInputStream(ResourceLocation location, IResourcePack resourcePack) throws IOException
    {
        InputStream var3 = resourcePack.getInputStream(location);
        return logger.isDebugEnabled() ? new FallbackResourceManager.ImputStreamLeakedResourceLogger(var3, location, resourcePack.getPackName()) : var3;
    }

    @Override
	public List getAllResources(ResourceLocation location) throws IOException
    {
        ArrayList var2 = Lists.newArrayList();
        ResourceLocation var3 = getLocationMcmeta(location);
        Iterator var4 = this.resourcePacks.iterator();

        while (var4.hasNext())
        {
            IResourcePack var5 = (IResourcePack)var4.next();

            if (var5.resourceExists(location))
            {
                InputStream var6 = var5.resourceExists(var3) ? this.getInputStream(var3, var5) : null;
                var2.add(new SimpleResource(var5.getPackName(), location, this.getInputStream(location, var5), var6, this.frmMetadataSerializer));
            }
        }

        if (var2.isEmpty())
        {
            throw new FileNotFoundException(location.toString());
        }
        else
        {
            return var2;
        }
    }

    static ResourceLocation getLocationMcmeta(ResourceLocation location)
    {
        return new ResourceLocation(location.getResourceDomain(), location.getResourcePath() + ".mcmeta");
    }

    static class ImputStreamLeakedResourceLogger extends InputStream
    {
        private final InputStream field_177330_a;
        private final String field_177328_b;
        private boolean field_177329_c = false;

        public ImputStreamLeakedResourceLogger(InputStream p_i46093_1_, ResourceLocation location, String p_i46093_3_)
        {
            this.field_177330_a = p_i46093_1_;
            ByteArrayOutputStream var4 = new ByteArrayOutputStream();
            (new Exception()).printStackTrace(new PrintStream(var4));
            this.field_177328_b = "Leaked resource: \'" + location + "\' loaded from pack: \'" + p_i46093_3_ + "\'\n" + var4.toString();
        }

        @Override
		public void close() throws IOException
        {
            this.field_177330_a.close();
            this.field_177329_c = true;
        }

        @Override
		protected void finalize() throws Throwable
        {
            if (!this.field_177329_c)
            {
                FallbackResourceManager.logger.warn(this.field_177328_b);
            }

            super.finalize();
        }

        @Override
		public int read() throws IOException
        {
            return this.field_177330_a.read();
        }
    }
}
