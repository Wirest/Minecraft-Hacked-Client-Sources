/*     */ package net.minecraft.client.multiplayer;
/*     */ 
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.util.ChatComponentTranslation;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ServerData
/*     */ {
/*     */   public String serverName;
/*     */   public String serverIP;
/*     */   public String populationInfo;
/*     */   public String serverMOTD;
/*     */   public long pingToServer;
/*  26 */   public int version = 47;
/*     */   
/*     */ 
/*  29 */   public String gameVersion = "1.8.8";
/*     */   public boolean field_78841_f;
/*     */   public String playerList;
/*  32 */   private ServerResourceMode resourceMode = ServerResourceMode.PROMPT;
/*     */   private String serverIcon;
/*     */   private boolean field_181042_l;
/*     */   
/*     */   public ServerData(String p_i46420_1_, String p_i46420_2_, boolean p_i46420_3_)
/*     */   {
/*  38 */     this.serverName = p_i46420_1_;
/*  39 */     this.serverIP = p_i46420_2_;
/*  40 */     this.field_181042_l = p_i46420_3_;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public NBTTagCompound getNBTCompound()
/*     */   {
/*  48 */     NBTTagCompound nbttagcompound = new NBTTagCompound();
/*  49 */     nbttagcompound.setString("name", this.serverName);
/*  50 */     nbttagcompound.setString("ip", this.serverIP);
/*     */     
/*  52 */     if (this.serverIcon != null)
/*     */     {
/*  54 */       nbttagcompound.setString("icon", this.serverIcon);
/*     */     }
/*     */     
/*  57 */     if (this.resourceMode == ServerResourceMode.ENABLED)
/*     */     {
/*  59 */       nbttagcompound.setBoolean("acceptTextures", true);
/*     */     }
/*  61 */     else if (this.resourceMode == ServerResourceMode.DISABLED)
/*     */     {
/*  63 */       nbttagcompound.setBoolean("acceptTextures", false);
/*     */     }
/*     */     
/*  66 */     return nbttagcompound;
/*     */   }
/*     */   
/*     */   public ServerResourceMode getResourceMode()
/*     */   {
/*  71 */     return this.resourceMode;
/*     */   }
/*     */   
/*     */   public void setResourceMode(ServerResourceMode mode)
/*     */   {
/*  76 */     this.resourceMode = mode;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ServerData getServerDataFromNBTCompound(NBTTagCompound nbtCompound)
/*     */   {
/*  84 */     ServerData serverdata = new ServerData(nbtCompound.getString("name"), nbtCompound.getString("ip"), false);
/*     */     
/*  86 */     if (nbtCompound.hasKey("icon", 8))
/*     */     {
/*  88 */       serverdata.setBase64EncodedIconData(nbtCompound.getString("icon"));
/*     */     }
/*     */     
/*  91 */     if (nbtCompound.hasKey("acceptTextures", 1))
/*     */     {
/*  93 */       if (nbtCompound.getBoolean("acceptTextures"))
/*     */       {
/*  95 */         serverdata.setResourceMode(ServerResourceMode.ENABLED);
/*     */       }
/*     */       else
/*     */       {
/*  99 */         serverdata.setResourceMode(ServerResourceMode.DISABLED);
/*     */       }
/*     */       
/*     */     }
/*     */     else {
/* 104 */       serverdata.setResourceMode(ServerResourceMode.PROMPT);
/*     */     }
/*     */     
/* 107 */     return serverdata;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getBase64EncodedIconData()
/*     */   {
/* 115 */     return this.serverIcon;
/*     */   }
/*     */   
/*     */   public void setBase64EncodedIconData(String icon)
/*     */   {
/* 120 */     this.serverIcon = icon;
/*     */   }
/*     */   
/*     */   public boolean func_181041_d()
/*     */   {
/* 125 */     return this.field_181042_l;
/*     */   }
/*     */   
/*     */   public void copyFrom(ServerData serverDataIn)
/*     */   {
/* 130 */     this.serverIP = serverDataIn.serverIP;
/* 131 */     this.serverName = serverDataIn.serverName;
/* 132 */     setResourceMode(serverDataIn.getResourceMode());
/* 133 */     this.serverIcon = serverDataIn.serverIcon;
/* 134 */     this.field_181042_l = serverDataIn.field_181042_l;
/*     */   }
/*     */   
/*     */   public static enum ServerResourceMode
/*     */   {
/* 139 */     ENABLED("enabled"), 
/* 140 */     DISABLED("disabled"), 
/* 141 */     PROMPT("prompt");
/*     */     
/*     */     private final IChatComponent motd;
/*     */     
/*     */     private ServerResourceMode(String p_i1053_3_)
/*     */     {
/* 147 */       this.motd = new ChatComponentTranslation("addServer.resourcePack." + p_i1053_3_, new Object[0]);
/*     */     }
/*     */     
/*     */     public IChatComponent getMotd()
/*     */     {
/* 152 */       return this.motd;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\multiplayer\ServerData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */