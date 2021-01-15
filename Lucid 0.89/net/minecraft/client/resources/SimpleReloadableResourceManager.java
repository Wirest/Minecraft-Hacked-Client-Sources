package net.minecraft.client.resources;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import net.minecraft.client.resources.data.IMetadataSerializer;
import net.minecraft.util.ResourceLocation;

public class SimpleReloadableResourceManager implements IReloadableResourceManager
{
    private static final Logger logger = LogManager.getLogger();
    private static final Joiner joinerResourcePacks = Joiner.on(", ");
    private final Map domainResourceManagers = Maps.newHashMap();
    private final List reloadListeners = Lists.newArrayList();
    private final Set setResourceDomains = Sets.newLinkedHashSet();
    private final IMetadataSerializer rmMetadataSerializer;

    public SimpleReloadableResourceManager(IMetadataSerializer rmMetadataSerializerIn)
    {
        this.rmMetadataSerializer = rmMetadataSerializerIn;
    }

    public void reloadResourcePack(IResourcePack resourcePack)
    {
        FallbackResourceManager var4;

        for (Iterator var2 = resourcePack.getResourceDomains().iterator(); var2.hasNext(); var4.addResourcePack(resourcePack))
        {
            String var3 = (String)var2.next();
            this.setResourceDomains.add(var3);
            var4 = (FallbackResourceManager)this.domainResourceManagers.get(var3);

            if (var4 == null)
            {
                var4 = new FallbackResourceManager(this.rmMetadataSerializer);
                this.domainResourceManagers.put(var3, var4);
            }
        }
    }

    @Override
	public Set getResourceDomains()
    {
        return this.setResourceDomains;
    }

    @Override
	public IResource getResource(ResourceLocation location) throws IOException
    {
        IResourceManager var2 = (IResourceManager)this.domainResourceManagers.get(location.getResourceDomain());

        if (var2 != null)
        {
            return var2.getResource(location);
        }
        else
        {
            throw new FileNotFoundException(location.toString());
        }
    }

    @Override
	public List getAllResources(ResourceLocation location) throws IOException
    {
        IResourceManager var2 = (IResourceManager)this.domainResourceManagers.get(location.getResourceDomain());

        if (var2 != null)
        {
            return var2.getAllResources(location);
        }
        else
        {
            throw new FileNotFoundException(location.toString());
        }
    }

    private void clearResources()
    {
        this.domainResourceManagers.clear();
        this.setResourceDomains.clear();
    }

    @Override
	public void reloadResources(List p_110541_1_)
    {
        this.clearResources();
        logger.info("Reloading ResourceManager: " + joinerResourcePacks.join(Iterables.transform(p_110541_1_, new Function()
        {
            public String apply(IResourcePack p_apply_1_)
            {
                return p_apply_1_.getPackName();
            }
            @Override
			public Object apply(Object p_apply_1_)
            {
                return this.apply((IResourcePack)p_apply_1_);
            }
        })));
        Iterator var2 = p_110541_1_.iterator();

        while (var2.hasNext())
        {
            IResourcePack var3 = (IResourcePack)var2.next();
            this.reloadResourcePack(var3);
        }

        this.notifyReloadListeners();
    }

    @Override
	public void registerReloadListener(IResourceManagerReloadListener reloadListener)
    {
        this.reloadListeners.add(reloadListener);
        reloadListener.onResourceManagerReload(this);
    }

    private void notifyReloadListeners()
    {
        Iterator var1 = this.reloadListeners.iterator();

        while (var1.hasNext())
        {
            IResourceManagerReloadListener var2 = (IResourceManagerReloadListener)var1.next();
            var2.onResourceManagerReload(this);
        }
    }
}
