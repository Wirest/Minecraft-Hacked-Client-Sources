/*     */ package net.minecraft.client.resources;
/*     */ 
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Joiner;
/*     */ import com.google.common.collect.Iterables;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Sets;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import net.minecraft.client.resources.data.IMetadataSerializer;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class SimpleReloadableResourceManager implements IReloadableResourceManager
/*     */ {
/*  21 */   private static final Logger logger = ;
/*  22 */   private static final Joiner joinerResourcePacks = Joiner.on(", ");
/*  23 */   private final Map<String, FallbackResourceManager> domainResourceManagers = Maps.newHashMap();
/*  24 */   private final List<IResourceManagerReloadListener> reloadListeners = Lists.newArrayList();
/*  25 */   private final Set<String> setResourceDomains = Sets.newLinkedHashSet();
/*     */   private final IMetadataSerializer rmMetadataSerializer;
/*     */   
/*     */   public SimpleReloadableResourceManager(IMetadataSerializer rmMetadataSerializerIn)
/*     */   {
/*  30 */     this.rmMetadataSerializer = rmMetadataSerializerIn;
/*     */   }
/*     */   
/*     */   public void reloadResourcePack(IResourcePack resourcePack)
/*     */   {
/*  35 */     for (String s : resourcePack.getResourceDomains())
/*     */     {
/*  37 */       this.setResourceDomains.add(s);
/*  38 */       FallbackResourceManager fallbackresourcemanager = (FallbackResourceManager)this.domainResourceManagers.get(s);
/*     */       
/*  40 */       if (fallbackresourcemanager == null)
/*     */       {
/*  42 */         fallbackresourcemanager = new FallbackResourceManager(this.rmMetadataSerializer);
/*  43 */         this.domainResourceManagers.put(s, fallbackresourcemanager);
/*     */       }
/*     */       
/*  46 */       fallbackresourcemanager.addResourcePack(resourcePack);
/*     */     }
/*     */   }
/*     */   
/*     */   public Set<String> getResourceDomains()
/*     */   {
/*  52 */     return this.setResourceDomains;
/*     */   }
/*     */   
/*     */   public IResource getResource(ResourceLocation location) throws IOException
/*     */   {
/*  57 */     IResourceManager iresourcemanager = (IResourceManager)this.domainResourceManagers.get(location.getResourceDomain());
/*     */     
/*  59 */     if (iresourcemanager != null)
/*     */     {
/*  61 */       return iresourcemanager.getResource(location);
/*     */     }
/*     */     
/*     */ 
/*  65 */     throw new FileNotFoundException(location.toString());
/*     */   }
/*     */   
/*     */   public List<IResource> getAllResources(ResourceLocation location)
/*     */     throws IOException
/*     */   {
/*  71 */     IResourceManager iresourcemanager = (IResourceManager)this.domainResourceManagers.get(location.getResourceDomain());
/*     */     
/*  73 */     if (iresourcemanager != null)
/*     */     {
/*  75 */       return iresourcemanager.getAllResources(location);
/*     */     }
/*     */     
/*     */ 
/*  79 */     throw new FileNotFoundException(location.toString());
/*     */   }
/*     */   
/*     */ 
/*     */   private void clearResources()
/*     */   {
/*  85 */     this.domainResourceManagers.clear();
/*  86 */     this.setResourceDomains.clear();
/*     */   }
/*     */   
/*     */   public void reloadResources(List<IResourcePack> p_110541_1_)
/*     */   {
/*  91 */     clearResources();
/*  92 */     logger.info("Reloading ResourceManager: " + joinerResourcePacks.join(Iterables.transform(p_110541_1_, new Function()
/*     */     {
/*     */       public String apply(IResourcePack p_apply_1_)
/*     */       {
/*  96 */         return p_apply_1_.getPackName();
/*     */       }
/*     */     })));
/*     */     
/* 100 */     for (IResourcePack iresourcepack : p_110541_1_)
/*     */     {
/* 102 */       reloadResourcePack(iresourcepack);
/*     */     }
/*     */     
/* 105 */     notifyReloadListeners();
/*     */   }
/*     */   
/*     */   public void registerReloadListener(IResourceManagerReloadListener reloadListener)
/*     */   {
/* 110 */     this.reloadListeners.add(reloadListener);
/* 111 */     reloadListener.onResourceManagerReload(this);
/*     */   }
/*     */   
/*     */   private void notifyReloadListeners()
/*     */   {
/* 116 */     for (IResourceManagerReloadListener iresourcemanagerreloadlistener : this.reloadListeners)
/*     */     {
/* 118 */       iresourcemanagerreloadlistener.onResourceManagerReload(this);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\resources\SimpleReloadableResourceManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */