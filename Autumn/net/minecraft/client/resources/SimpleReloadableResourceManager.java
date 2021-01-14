package net.minecraft.client.resources;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.client.resources.data.IMetadataSerializer;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SimpleReloadableResourceManager implements IReloadableResourceManager {
   private static final Logger logger = LogManager.getLogger();
   private static final Joiner joinerResourcePacks = Joiner.on(", ");
   private final Map domainResourceManagers = Maps.newHashMap();
   private final List reloadListeners = Lists.newArrayList();
   private final Set setResourceDomains = Sets.newLinkedHashSet();
   private final IMetadataSerializer rmMetadataSerializer;

   public SimpleReloadableResourceManager(IMetadataSerializer rmMetadataSerializerIn) {
      this.rmMetadataSerializer = rmMetadataSerializerIn;
   }

   public void reloadResourcePack(IResourcePack resourcePack) {
      FallbackResourceManager fallbackresourcemanager;
      for(Iterator var2 = resourcePack.getResourceDomains().iterator(); var2.hasNext(); fallbackresourcemanager.addResourcePack(resourcePack)) {
         String s = (String)var2.next();
         this.setResourceDomains.add(s);
         fallbackresourcemanager = (FallbackResourceManager)this.domainResourceManagers.get(s);
         if (fallbackresourcemanager == null) {
            fallbackresourcemanager = new FallbackResourceManager(this.rmMetadataSerializer);
            this.domainResourceManagers.put(s, fallbackresourcemanager);
         }
      }

   }

   public Set getResourceDomains() {
      return this.setResourceDomains;
   }

   public IResource getResource(ResourceLocation location) throws IOException {
      IResourceManager iresourcemanager = (IResourceManager)this.domainResourceManagers.get(location.getResourceDomain());
      if (iresourcemanager != null) {
         return iresourcemanager.getResource(location);
      } else {
         throw new FileNotFoundException(location.toString());
      }
   }

   public List getAllResources(ResourceLocation location) throws IOException {
      IResourceManager iresourcemanager = (IResourceManager)this.domainResourceManagers.get(location.getResourceDomain());
      if (iresourcemanager != null) {
         return iresourcemanager.getAllResources(location);
      } else {
         throw new FileNotFoundException(location.toString());
      }
   }

   private void clearResources() {
      this.domainResourceManagers.clear();
      this.setResourceDomains.clear();
   }

   public void reloadResources(List p_110541_1_) {
      this.clearResources();
      logger.info("Reloading ResourceManager: " + joinerResourcePacks.join(Iterables.transform(p_110541_1_, new Function() {
         public String apply(IResourcePack p_apply_1_) {
            return p_apply_1_.getPackName();
         }
      })));
      Iterator var2 = p_110541_1_.iterator();

      while(var2.hasNext()) {
         IResourcePack iresourcepack = (IResourcePack)var2.next();
         this.reloadResourcePack(iresourcepack);
      }

      this.notifyReloadListeners();
   }

   public void registerReloadListener(IResourceManagerReloadListener reloadListener) {
      this.reloadListeners.add(reloadListener);
      reloadListener.onResourceManagerReload(this);
   }

   private void notifyReloadListeners() {
      Iterator var1 = this.reloadListeners.iterator();

      while(var1.hasNext()) {
         IResourceManagerReloadListener iresourcemanagerreloadlistener = (IResourceManagerReloadListener)var1.next();
         iresourcemanagerreloadlistener.onResourceManagerReload(this);
      }

   }
}
