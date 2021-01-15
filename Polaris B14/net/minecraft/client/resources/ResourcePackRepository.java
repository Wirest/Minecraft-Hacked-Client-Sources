/*     */ package net.minecraft.client.resources;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.hash.HashFunction;
/*     */ import com.google.common.hash.Hashing;
/*     */ import com.google.common.io.Files;
/*     */ import com.google.common.util.concurrent.FutureCallback;
/*     */ import com.google.common.util.concurrent.Futures;
/*     */ import com.google.common.util.concurrent.ListenableFuture;
/*     */ import com.google.common.util.concurrent.SettableFuture;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.io.Closeable;
/*     */ import java.io.File;
/*     */ import java.io.FileFilter;
/*     */ import java.io.IOException;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiScreenWorking;
/*     */ import net.minecraft.client.renderer.texture.TextureManager;
/*     */ import net.minecraft.client.resources.data.IMetadataSerializer;
/*     */ import net.minecraft.client.resources.data.PackMetadataSection;
/*     */ import net.minecraft.client.settings.GameSettings;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ import net.minecraft.util.HttpUtil;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import org.apache.commons.io.FileUtils;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ import org.apache.commons.io.comparator.LastModifiedFileComparator;
/*     */ import org.apache.commons.io.filefilter.TrueFileFilter;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class ResourcePackRepository
/*     */ {
/*  42 */   private static final Logger logger = ;
/*  43 */   private static final FileFilter resourcePackFilter = new FileFilter()
/*     */   {
/*     */     public boolean accept(File p_accept_1_)
/*     */     {
/*  47 */       boolean flag = (p_accept_1_.isFile()) && (p_accept_1_.getName().endsWith(".zip"));
/*  48 */       boolean flag1 = (p_accept_1_.isDirectory()) && (new File(p_accept_1_, "pack.mcmeta").isFile());
/*  49 */       return (flag) || (flag1);
/*     */     }
/*     */   };
/*     */   private final File dirResourcepacks;
/*     */   public final IResourcePack rprDefaultResourcePack;
/*     */   private final File dirServerResourcepacks;
/*     */   public final IMetadataSerializer rprMetadataSerializer;
/*     */   private IResourcePack resourcePackInstance;
/*  57 */   private final ReentrantLock lock = new ReentrantLock();
/*     */   private ListenableFuture<Object> field_177322_i;
/*  59 */   private List<Entry> repositoryEntriesAll = Lists.newArrayList();
/*  60 */   private List<Entry> repositoryEntries = Lists.newArrayList();
/*     */   
/*     */   public ResourcePackRepository(File dirResourcepacksIn, File dirServerResourcepacksIn, IResourcePack rprDefaultResourcePackIn, IMetadataSerializer rprMetadataSerializerIn, GameSettings settings)
/*     */   {
/*  64 */     this.dirResourcepacks = dirResourcepacksIn;
/*  65 */     this.dirServerResourcepacks = dirServerResourcepacksIn;
/*  66 */     this.rprDefaultResourcePack = rprDefaultResourcePackIn;
/*  67 */     this.rprMetadataSerializer = rprMetadataSerializerIn;
/*  68 */     fixDirResourcepacks();
/*  69 */     updateRepositoryEntriesAll();
/*  70 */     Iterator<String> iterator = settings.resourcePacks.iterator();
/*     */     
/*  72 */     while (iterator.hasNext())
/*     */     {
/*  74 */       String s = (String)iterator.next();
/*     */       
/*  76 */       for (Entry resourcepackrepository$entry : this.repositoryEntriesAll)
/*     */       {
/*  78 */         if (resourcepackrepository$entry.getResourcePackName().equals(s))
/*     */         {
/*  80 */           if ((resourcepackrepository$entry.func_183027_f() == 1) || (settings.field_183018_l.contains(resourcepackrepository$entry.getResourcePackName())))
/*     */           {
/*  82 */             this.repositoryEntries.add(resourcepackrepository$entry);
/*  83 */             break;
/*     */           }
/*     */           
/*  86 */           iterator.remove();
/*  87 */           logger.warn("Removed selected resource pack {} because it's no longer compatible", new Object[] { resourcepackrepository$entry.getResourcePackName() });
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void fixDirResourcepacks()
/*     */   {
/*  95 */     if (this.dirResourcepacks.exists())
/*     */     {
/*  97 */       if ((!this.dirResourcepacks.isDirectory()) && ((!this.dirResourcepacks.delete()) || (!this.dirResourcepacks.mkdirs())))
/*     */       {
/*  99 */         logger.warn("Unable to recreate resourcepack folder, it exists but is not a directory: " + this.dirResourcepacks);
/*     */       }
/*     */     }
/* 102 */     else if (!this.dirResourcepacks.mkdirs())
/*     */     {
/* 104 */       logger.warn("Unable to create resourcepack folder: " + this.dirResourcepacks);
/*     */     }
/*     */   }
/*     */   
/*     */   private List<File> getResourcePackFiles()
/*     */   {
/* 110 */     return this.dirResourcepacks.isDirectory() ? Arrays.asList(this.dirResourcepacks.listFiles(resourcePackFilter)) : Collections.emptyList();
/*     */   }
/*     */   
/*     */   public void updateRepositoryEntriesAll()
/*     */   {
/* 115 */     List<Entry> list = Lists.newArrayList();
/*     */     
/* 117 */     for (File file1 : getResourcePackFiles())
/*     */     {
/* 119 */       Entry resourcepackrepository$entry = new Entry(file1, null);
/*     */       
/* 121 */       if (!this.repositoryEntriesAll.contains(resourcepackrepository$entry))
/*     */       {
/*     */         try
/*     */         {
/* 125 */           resourcepackrepository$entry.updateResourcePack();
/* 126 */           list.add(resourcepackrepository$entry);
/*     */         }
/*     */         catch (Exception var6)
/*     */         {
/* 130 */           list.remove(resourcepackrepository$entry);
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 135 */         int i = this.repositoryEntriesAll.indexOf(resourcepackrepository$entry);
/*     */         
/* 137 */         if ((i > -1) && (i < this.repositoryEntriesAll.size()))
/*     */         {
/* 139 */           list.add((Entry)this.repositoryEntriesAll.get(i));
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 144 */     this.repositoryEntriesAll.removeAll(list);
/*     */     
/* 146 */     for (Entry resourcepackrepository$entry1 : this.repositoryEntriesAll)
/*     */     {
/* 148 */       resourcepackrepository$entry1.closeResourcePack();
/*     */     }
/*     */     
/* 151 */     this.repositoryEntriesAll = list;
/*     */   }
/*     */   
/*     */   public List<Entry> getRepositoryEntriesAll()
/*     */   {
/* 156 */     return ImmutableList.copyOf(this.repositoryEntriesAll);
/*     */   }
/*     */   
/*     */   public List<Entry> getRepositoryEntries()
/*     */   {
/* 161 */     return ImmutableList.copyOf(this.repositoryEntries);
/*     */   }
/*     */   
/*     */   public void setRepositories(List<Entry> p_148527_1_)
/*     */   {
/* 166 */     this.repositoryEntries.clear();
/* 167 */     this.repositoryEntries.addAll(p_148527_1_);
/*     */   }
/*     */   
/*     */   public File getDirResourcepacks()
/*     */   {
/* 172 */     return this.dirResourcepacks;
/*     */   }
/*     */   
/*     */   public ListenableFuture<Object> downloadResourcePack(String url, String hash)
/*     */   {
/*     */     String s;
/*     */     String s;
/* 179 */     if (hash.matches("^[a-f0-9]{40}$"))
/*     */     {
/* 181 */       s = hash;
/*     */     }
/*     */     else
/*     */     {
/* 185 */       s = "legacy";
/*     */     }
/*     */     
/* 188 */     final File file1 = new File(this.dirServerResourcepacks, s);
/* 189 */     this.lock.lock();
/*     */     
/*     */     try
/*     */     {
/* 193 */       func_148529_f();
/*     */       ListenableFuture localListenableFuture1;
/* 195 */       if ((file1.exists()) && (hash.length() == 40))
/*     */       {
/*     */         try
/*     */         {
/* 199 */           String s1 = Hashing.sha1().hashBytes(Files.toByteArray(file1)).toString();
/*     */           
/* 201 */           if (s1.equals(hash))
/*     */           {
/* 203 */             ListenableFuture listenablefuture1 = setResourcePackInstance(file1);
/* 204 */             return listenablefuture1;
/*     */           }
/*     */           
/* 207 */           logger.warn("File " + file1 + " had wrong hash (expected " + hash + ", found " + s1 + "). Deleting it.");
/* 208 */           FileUtils.deleteQuietly(file1);
/*     */         }
/*     */         catch (IOException ioexception)
/*     */         {
/* 212 */           logger.warn("File " + file1 + " couldn't be hashed. Deleting it.", ioexception);
/* 213 */           FileUtils.deleteQuietly(file1);
/*     */         }
/*     */       }
/*     */       
/* 217 */       func_183028_i();
/* 218 */       final GuiScreenWorking guiscreenworking = new GuiScreenWorking();
/* 219 */       Map<String, String> map = Minecraft.getSessionInfo();
/* 220 */       final Minecraft minecraft = Minecraft.getMinecraft();
/* 221 */       Futures.getUnchecked(minecraft.addScheduledTask(new Runnable()
/*     */       {
/*     */         public void run()
/*     */         {
/* 225 */           minecraft.displayGuiScreen(guiscreenworking);
/*     */         }
/* 227 */       }));
/* 228 */       final SettableFuture<Object> settablefuture = SettableFuture.create();
/* 229 */       this.field_177322_i = HttpUtil.downloadResourcePack(file1, url, map, 52428800, guiscreenworking, minecraft.getProxy());
/* 230 */       Futures.addCallback(this.field_177322_i, new FutureCallback()
/*     */       {
/*     */         public void onSuccess(Object p_onSuccess_1_)
/*     */         {
/* 234 */           ResourcePackRepository.this.setResourcePackInstance(file1);
/* 235 */           settablefuture.set(null);
/*     */         }
/*     */         
/*     */         public void onFailure(Throwable p_onFailure_1_) {
/* 239 */           settablefuture.setException(p_onFailure_1_);
/*     */         }
/* 241 */       });
/* 242 */       ListenableFuture listenablefuture = this.field_177322_i;
/* 243 */       return listenablefuture;
/*     */     }
/*     */     finally
/*     */     {
/* 247 */       this.lock.unlock();
/*     */     }
/*     */   }
/*     */   
/*     */   private void func_183028_i()
/*     */   {
/* 253 */     List<File> list = Lists.newArrayList(FileUtils.listFiles(this.dirServerResourcepacks, TrueFileFilter.TRUE, null));
/* 254 */     Collections.sort(list, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
/* 255 */     int i = 0;
/*     */     
/* 257 */     for (File file1 : list)
/*     */     {
/* 259 */       if (i++ >= 10)
/*     */       {
/* 261 */         logger.info("Deleting old server resource pack " + file1.getName());
/* 262 */         FileUtils.deleteQuietly(file1);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public ListenableFuture<Object> setResourcePackInstance(File p_177319_1_)
/*     */   {
/* 269 */     this.resourcePackInstance = new FileResourcePack(p_177319_1_);
/* 270 */     return Minecraft.getMinecraft().scheduleResourcesRefresh();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public IResourcePack getResourcePackInstance()
/*     */   {
/* 278 */     return this.resourcePackInstance;
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public void func_148529_f()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 57	net/minecraft/client/resources/ResourcePackRepository:lock	Ljava/util/concurrent/locks/ReentrantLock;
/*     */     //   4: invokevirtual 276	java/util/concurrent/locks/ReentrantLock:lock	()V
/*     */     //   7: aload_0
/*     */     //   8: getfield 379	net/minecraft/client/resources/ResourcePackRepository:field_177322_i	Lcom/google/common/util/concurrent/ListenableFuture;
/*     */     //   11: ifnull +14 -> 25
/*     */     //   14: aload_0
/*     */     //   15: getfield 379	net/minecraft/client/resources/ResourcePackRepository:field_177322_i	Lcom/google/common/util/concurrent/ListenableFuture;
/*     */     //   18: iconst_1
/*     */     //   19: invokeinterface 456 2 0
/*     */     //   24: pop
/*     */     //   25: aload_0
/*     */     //   26: aconst_null
/*     */     //   27: putfield 379	net/minecraft/client/resources/ResourcePackRepository:field_177322_i	Lcom/google/common/util/concurrent/ListenableFuture;
/*     */     //   30: aload_0
/*     */     //   31: getfield 443	net/minecraft/client/resources/ResourcePackRepository:resourcePackInstance	Lnet/minecraft/client/resources/IResourcePack;
/*     */     //   34: ifnull +28 -> 62
/*     */     //   37: aload_0
/*     */     //   38: aconst_null
/*     */     //   39: putfield 443	net/minecraft/client/resources/ResourcePackRepository:resourcePackInstance	Lnet/minecraft/client/resources/IResourcePack;
/*     */     //   42: invokestatic 348	net/minecraft/client/Minecraft:getMinecraft	()Lnet/minecraft/client/Minecraft;
/*     */     //   45: invokevirtual 447	net/minecraft/client/Minecraft:scheduleResourcesRefresh	()Lcom/google/common/util/concurrent/ListenableFuture;
/*     */     //   48: pop
/*     */     //   49: goto +13 -> 62
/*     */     //   52: astore_1
/*     */     //   53: aload_0
/*     */     //   54: getfield 57	net/minecraft/client/resources/ResourcePackRepository:lock	Ljava/util/concurrent/locks/ReentrantLock;
/*     */     //   57: invokevirtual 310	java/util/concurrent/locks/ReentrantLock:unlock	()V
/*     */     //   60: aload_1
/*     */     //   61: athrow
/*     */     //   62: aload_0
/*     */     //   63: getfield 57	net/minecraft/client/resources/ResourcePackRepository:lock	Ljava/util/concurrent/locks/ReentrantLock;
/*     */     //   66: invokevirtual 310	java/util/concurrent/locks/ReentrantLock:unlock	()V
/*     */     //   69: return
/*     */     // Line number table:
/*     */     //   Java source line #283	-> byte code offset #0
/*     */     //   Java source line #287	-> byte code offset #7
/*     */     //   Java source line #289	-> byte code offset #14
/*     */     //   Java source line #292	-> byte code offset #25
/*     */     //   Java source line #294	-> byte code offset #30
/*     */     //   Java source line #296	-> byte code offset #37
/*     */     //   Java source line #297	-> byte code offset #42
/*     */     //   Java source line #299	-> byte code offset #49
/*     */     //   Java source line #301	-> byte code offset #52
/*     */     //   Java source line #302	-> byte code offset #53
/*     */     //   Java source line #303	-> byte code offset #60
/*     */     //   Java source line #302	-> byte code offset #62
/*     */     //   Java source line #304	-> byte code offset #69
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	70	0	this	ResourcePackRepository
/*     */     //   52	9	1	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   7	52	52	finally
/*     */   }
/*     */   
/*     */   public class Entry
/*     */   {
/*     */     private final File resourcePackFile;
/*     */     private IResourcePack reResourcePack;
/*     */     private PackMetadataSection rePackMetadataSection;
/*     */     private BufferedImage texturePackIcon;
/*     */     private ResourceLocation locationTexturePackIcon;
/*     */     
/*     */     private Entry(File resourcePackFileIn)
/*     */     {
/* 316 */       this.resourcePackFile = resourcePackFileIn;
/*     */     }
/*     */     
/*     */     public void updateResourcePack() throws IOException
/*     */     {
/* 321 */       this.reResourcePack = (this.resourcePackFile.isDirectory() ? new FolderResourcePack(this.resourcePackFile) : new FileResourcePack(this.resourcePackFile));
/* 322 */       this.rePackMetadataSection = ((PackMetadataSection)this.reResourcePack.getPackMetadata(ResourcePackRepository.this.rprMetadataSerializer, "pack"));
/*     */       
/*     */       try
/*     */       {
/* 326 */         this.texturePackIcon = this.reResourcePack.getPackImage();
/*     */       }
/*     */       catch (IOException localIOException) {}
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 333 */       if (this.texturePackIcon == null)
/*     */       {
/* 335 */         this.texturePackIcon = ResourcePackRepository.this.rprDefaultResourcePack.getPackImage();
/*     */       }
/*     */       
/* 338 */       closeResourcePack();
/*     */     }
/*     */     
/*     */     public void bindTexturePackIcon(TextureManager textureManagerIn)
/*     */     {
/* 343 */       if (this.locationTexturePackIcon == null)
/*     */       {
/* 345 */         this.locationTexturePackIcon = textureManagerIn.getDynamicTextureLocation("texturepackicon", new net.minecraft.client.renderer.texture.DynamicTexture(this.texturePackIcon));
/*     */       }
/*     */       
/* 348 */       textureManagerIn.bindTexture(this.locationTexturePackIcon);
/*     */     }
/*     */     
/*     */     public void closeResourcePack()
/*     */     {
/* 353 */       if ((this.reResourcePack instanceof Closeable))
/*     */       {
/* 355 */         IOUtils.closeQuietly((Closeable)this.reResourcePack);
/*     */       }
/*     */     }
/*     */     
/*     */     public IResourcePack getResourcePack()
/*     */     {
/* 361 */       return this.reResourcePack;
/*     */     }
/*     */     
/*     */     public String getResourcePackName()
/*     */     {
/* 366 */       return this.reResourcePack.getPackName();
/*     */     }
/*     */     
/*     */     public String getTexturePackDescription()
/*     */     {
/* 371 */       return this.rePackMetadataSection == null ? EnumChatFormatting.RED + "Invalid pack.mcmeta (or missing 'pack' section)" : this.rePackMetadataSection.getPackDescription().getFormattedText();
/*     */     }
/*     */     
/*     */     public int func_183027_f()
/*     */     {
/* 376 */       return this.rePackMetadataSection.getPackFormat();
/*     */     }
/*     */     
/*     */     public boolean equals(Object p_equals_1_)
/*     */     {
/* 381 */       return this == p_equals_1_;
/*     */     }
/*     */     
/*     */     public int hashCode()
/*     */     {
/* 386 */       return toString().hashCode();
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 391 */       return String.format("%s:%s:%d", new Object[] { this.resourcePackFile.getName(), this.resourcePackFile.isDirectory() ? "folder" : "zip", Long.valueOf(this.resourcePackFile.lastModified()) });
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\resources\ResourcePackRepository.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */