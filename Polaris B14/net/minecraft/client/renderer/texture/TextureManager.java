/*     */ package net.minecraft.client.renderer.texture;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.io.IOException;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.Callable;
/*     */ import net.minecraft.client.resources.IResourceManager;
/*     */ import net.minecraft.client.resources.IResourceManagerReloadListener;
/*     */ import net.minecraft.crash.CrashReport;
/*     */ import net.minecraft.crash.CrashReportCategory;
/*     */ import net.minecraft.util.ReportedException;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import optfine.Config;
/*     */ import optfine.RandomMobs;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class TextureManager implements ITickable, IResourceManagerReloadListener
/*     */ {
/*  25 */   private static final Logger logger = ;
/*  26 */   private final Map mapTextureObjects = Maps.newHashMap();
/*  27 */   private final List listTickables = Lists.newArrayList();
/*  28 */   private final Map mapTextureCounters = Maps.newHashMap();
/*     */   private IResourceManager theResourceManager;
/*     */   private static final String __OBFID = "CL_00001064";
/*     */   
/*     */   public TextureManager(IResourceManager resourceManager)
/*     */   {
/*  34 */     this.theResourceManager = resourceManager;
/*     */   }
/*     */   
/*     */   public void bindTexture(ResourceLocation resource)
/*     */   {
/*  39 */     if (Config.isRandomMobs())
/*     */     {
/*  41 */       resource = RandomMobs.getTextureLocation(resource);
/*     */     }
/*     */     
/*  44 */     Object object = (ITextureObject)this.mapTextureObjects.get(resource);
/*     */     
/*  46 */     if (object == null)
/*     */     {
/*  48 */       object = new SimpleTexture(resource);
/*  49 */       loadTexture(resource, (ITextureObject)object);
/*     */     }
/*     */     
/*  52 */     TextureUtil.bindTexture(((ITextureObject)object).getGlTextureId());
/*     */   }
/*     */   
/*     */   public boolean loadTickableTexture(ResourceLocation textureLocation, ITickableTextureObject textureObj)
/*     */   {
/*  57 */     if (loadTexture(textureLocation, textureObj))
/*     */     {
/*  59 */       this.listTickables.add(textureObj);
/*  60 */       return true;
/*     */     }
/*     */     
/*     */ 
/*  64 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean loadTexture(ResourceLocation textureLocation, final ITextureObject textureObj)
/*     */   {
/*  70 */     boolean flag = true;
/*  71 */     ITextureObject itextureobject = textureObj;
/*     */     
/*     */     try
/*     */     {
/*  75 */       textureObj.loadTexture(this.theResourceManager);
/*     */     }
/*     */     catch (IOException ioexception)
/*     */     {
/*  79 */       logger.warn("Failed to load texture: " + textureLocation, ioexception);
/*  80 */       itextureobject = TextureUtil.missingTexture;
/*  81 */       this.mapTextureObjects.put(textureLocation, itextureobject);
/*  82 */       flag = false;
/*     */     }
/*     */     catch (Throwable throwable)
/*     */     {
/*  86 */       CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Registering texture");
/*  87 */       CrashReportCategory crashreportcategory = crashreport.makeCategory("Resource location being registered");
/*  88 */       crashreportcategory.addCrashSection("Resource location", textureLocation);
/*  89 */       crashreportcategory.addCrashSectionCallable("Texture object class", new Callable()
/*     */       {
/*     */         private static final String __OBFID = "CL_00001065";
/*     */         
/*     */         public String call() throws Exception {
/*  94 */           return textureObj.getClass().getName();
/*     */         }
/*  96 */       });
/*  97 */       throw new ReportedException(crashreport);
/*     */     }
/*     */     
/* 100 */     this.mapTextureObjects.put(textureLocation, itextureobject);
/* 101 */     return flag;
/*     */   }
/*     */   
/*     */   public ITextureObject getTexture(ResourceLocation textureLocation)
/*     */   {
/* 106 */     return (ITextureObject)this.mapTextureObjects.get(textureLocation);
/*     */   }
/*     */   
/*     */   public ResourceLocation getDynamicTextureLocation(String name, DynamicTexture texture)
/*     */   {
/* 111 */     Integer integer = (Integer)this.mapTextureCounters.get(name);
/*     */     
/* 113 */     if (integer == null)
/*     */     {
/* 115 */       integer = Integer.valueOf(1);
/*     */     }
/*     */     else
/*     */     {
/* 119 */       integer = Integer.valueOf(integer.intValue() + 1);
/*     */     }
/*     */     
/* 122 */     this.mapTextureCounters.put(name, integer);
/* 123 */     ResourceLocation resourcelocation = new ResourceLocation(String.format("dynamic/%s_%d", new Object[] { name, integer }));
/* 124 */     loadTexture(resourcelocation, texture);
/* 125 */     return resourcelocation;
/*     */   }
/*     */   
/*     */   public void tick()
/*     */   {
/* 130 */     for (Object itickable : this.listTickables)
/*     */     {
/* 132 */       ((ITickable)itickable).tick();
/*     */     }
/*     */   }
/*     */   
/*     */   public void deleteTexture(ResourceLocation textureLocation)
/*     */   {
/* 138 */     ITextureObject itextureobject = getTexture(textureLocation);
/*     */     
/* 140 */     if (itextureobject != null)
/*     */     {
/* 142 */       TextureUtil.deleteTexture(itextureobject.getGlTextureId());
/*     */     }
/*     */   }
/*     */   
/*     */   public void onResourceManagerReload(IResourceManager resourceManager)
/*     */   {
/* 148 */     Config.dbg("*** Reloading textures ***");
/* 149 */     Config.log("Resource packs: " + Config.getResourcePackNames());
/* 150 */     Iterator iterator = this.mapTextureObjects.keySet().iterator();
/*     */     ITextureObject itextureobject;
/* 152 */     while (iterator.hasNext())
/*     */     {
/* 154 */       ResourceLocation resourcelocation = (ResourceLocation)iterator.next();
/*     */       
/* 156 */       if (resourcelocation.getResourcePath().startsWith("mcpatcher/"))
/*     */       {
/* 158 */         itextureobject = (ITextureObject)this.mapTextureObjects.get(resourcelocation);
/*     */         
/* 160 */         if ((itextureobject instanceof AbstractTexture))
/*     */         {
/* 162 */           AbstractTexture abstracttexture = (AbstractTexture)itextureobject;
/* 163 */           abstracttexture.deleteGlTexture();
/*     */         }
/*     */         
/* 166 */         iterator.remove();
/*     */       }
/*     */     }
/*     */     
/* 170 */     for (Object entry : this.mapTextureObjects.entrySet())
/*     */     {
/* 172 */       loadTexture((ResourceLocation)((Map.Entry)entry).getKey(), (ITextureObject)((Map.Entry)entry).getValue());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\texture\TextureManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */