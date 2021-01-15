/*     */ package net.minecraft.client.multiplayer;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.io.File;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.nbt.CompressedStreamTools;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.nbt.NBTTagList;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class ServerList
/*     */ {
/*  15 */   private static final Logger logger = ;
/*     */   
/*     */   private final Minecraft mc;
/*     */   
/*  19 */   private final List<ServerData> servers = Lists.newArrayList();
/*     */   
/*     */   public ServerList(Minecraft mcIn)
/*     */   {
/*  23 */     this.mc = mcIn;
/*  24 */     loadServerList();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void loadServerList()
/*     */   {
/*     */     try
/*     */     {
/*  35 */       this.servers.clear();
/*  36 */       NBTTagCompound nbttagcompound = CompressedStreamTools.read(new File(this.mc.mcDataDir, "servers.dat"));
/*     */       
/*  38 */       if (nbttagcompound == null)
/*     */       {
/*  40 */         return;
/*     */       }
/*     */       
/*  43 */       NBTTagList nbttaglist = nbttagcompound.getTagList("servers", 10);
/*     */       
/*  45 */       for (int i = 0; i < nbttaglist.tagCount(); i++)
/*     */       {
/*  47 */         this.servers.add(ServerData.getServerDataFromNBTCompound(nbttaglist.getCompoundTagAt(i)));
/*     */       }
/*     */     }
/*     */     catch (Exception exception)
/*     */     {
/*  52 */       logger.error("Couldn't load server list", exception);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void saveServerList()
/*     */   {
/*     */     try
/*     */     {
/*  64 */       NBTTagList nbttaglist = new NBTTagList();
/*     */       
/*  66 */       for (ServerData serverdata : this.servers)
/*     */       {
/*  68 */         nbttaglist.appendTag(serverdata.getNBTCompound());
/*     */       }
/*     */       
/*  71 */       NBTTagCompound nbttagcompound = new NBTTagCompound();
/*  72 */       nbttagcompound.setTag("servers", nbttaglist);
/*  73 */       CompressedStreamTools.safeWrite(nbttagcompound, new File(this.mc.mcDataDir, "servers.dat"));
/*     */     }
/*     */     catch (Exception exception)
/*     */     {
/*  77 */       logger.error("Couldn't save server list", exception);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ServerData getServerData(int p_78850_1_)
/*     */   {
/*  86 */     return (ServerData)this.servers.get(p_78850_1_);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void removeServerData(int p_78851_1_)
/*     */   {
/*  94 */     this.servers.remove(p_78851_1_);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addServerData(ServerData p_78849_1_)
/*     */   {
/* 102 */     this.servers.add(p_78849_1_);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int countServers()
/*     */   {
/* 110 */     return this.servers.size();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void swapServers(int p_78857_1_, int p_78857_2_)
/*     */   {
/* 118 */     ServerData serverdata = getServerData(p_78857_1_);
/* 119 */     this.servers.set(p_78857_1_, getServerData(p_78857_2_));
/* 120 */     this.servers.set(p_78857_2_, serverdata);
/* 121 */     saveServerList();
/*     */   }
/*     */   
/*     */   public void func_147413_a(int p_147413_1_, ServerData p_147413_2_)
/*     */   {
/* 126 */     this.servers.set(p_147413_1_, p_147413_2_);
/*     */   }
/*     */   
/*     */   public static void func_147414_b(ServerData p_147414_0_)
/*     */   {
/* 131 */     ServerList serverlist = new ServerList(Minecraft.getMinecraft());
/* 132 */     serverlist.loadServerList();
/*     */     
/* 134 */     for (int i = 0; i < serverlist.countServers(); i++)
/*     */     {
/* 136 */       ServerData serverdata = serverlist.getServerData(i);
/*     */       
/* 138 */       if ((serverdata.serverName.equals(p_147414_0_.serverName)) && (serverdata.serverIP.equals(p_147414_0_.serverIP)))
/*     */       {
/* 140 */         serverlist.func_147413_a(i, p_147414_0_);
/* 141 */         break;
/*     */       }
/*     */     }
/*     */     
/* 145 */     serverlist.saveServerList();
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\multiplayer\ServerList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */