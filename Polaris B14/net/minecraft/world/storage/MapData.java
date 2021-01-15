/*     */ package net.minecraft.world.storage;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import net.minecraft.entity.item.EntityItemFrame;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.InventoryPlayer;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.nbt.NBTTagList;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.play.server.S34PacketMaps;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.Vec4b;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.WorldSavedData;
/*     */ 
/*     */ public class MapData extends WorldSavedData
/*     */ {
/*     */   public int xCenter;
/*     */   public int zCenter;
/*     */   public byte dimension;
/*     */   public byte scale;
/*  28 */   public byte[] colors = new byte['䀀'];
/*  29 */   public List<MapInfo> playersArrayList = Lists.newArrayList();
/*  30 */   private Map<EntityPlayer, MapInfo> playersHashMap = Maps.newHashMap();
/*  31 */   public Map<String, Vec4b> mapDecorations = Maps.newLinkedHashMap();
/*     */   
/*     */   public MapData(String mapname)
/*     */   {
/*  35 */     super(mapname);
/*     */   }
/*     */   
/*     */   public void calculateMapCenter(double x, double z, int mapScale)
/*     */   {
/*  40 */     int i = 128 * (1 << mapScale);
/*  41 */     int j = MathHelper.floor_double((x + 64.0D) / i);
/*  42 */     int k = MathHelper.floor_double((z + 64.0D) / i);
/*  43 */     this.xCenter = (j * i + i / 2 - 64);
/*  44 */     this.zCenter = (k * i + i / 2 - 64);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void readFromNBT(NBTTagCompound nbt)
/*     */   {
/*  52 */     this.dimension = nbt.getByte("dimension");
/*  53 */     this.xCenter = nbt.getInteger("xCenter");
/*  54 */     this.zCenter = nbt.getInteger("zCenter");
/*  55 */     this.scale = nbt.getByte("scale");
/*  56 */     this.scale = ((byte)MathHelper.clamp_int(this.scale, 0, 4));
/*  57 */     int i = nbt.getShort("width");
/*  58 */     int j = nbt.getShort("height");
/*     */     
/*  60 */     if ((i == 128) && (j == 128))
/*     */     {
/*  62 */       this.colors = nbt.getByteArray("colors");
/*     */     }
/*     */     else
/*     */     {
/*  66 */       byte[] abyte = nbt.getByteArray("colors");
/*  67 */       this.colors = new byte['䀀'];
/*  68 */       int k = (128 - i) / 2;
/*  69 */       int l = (128 - j) / 2;
/*     */       
/*  71 */       for (int i1 = 0; i1 < j; i1++)
/*     */       {
/*  73 */         int j1 = i1 + l;
/*     */         
/*  75 */         if ((j1 >= 0) || (j1 < 128))
/*     */         {
/*  77 */           for (int k1 = 0; k1 < i; k1++)
/*     */           {
/*  79 */             int l1 = k1 + k;
/*     */             
/*  81 */             if ((l1 >= 0) || (l1 < 128))
/*     */             {
/*  83 */               this.colors[(l1 + j1 * 128)] = abyte[(k1 + i1 * i)];
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeToNBT(NBTTagCompound nbt)
/*     */   {
/*  96 */     nbt.setByte("dimension", this.dimension);
/*  97 */     nbt.setInteger("xCenter", this.xCenter);
/*  98 */     nbt.setInteger("zCenter", this.zCenter);
/*  99 */     nbt.setByte("scale", this.scale);
/* 100 */     nbt.setShort("width", (short)128);
/* 101 */     nbt.setShort("height", (short)128);
/* 102 */     nbt.setByteArray("colors", this.colors);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void updateVisiblePlayers(EntityPlayer player, ItemStack mapStack)
/*     */   {
/* 110 */     if (!this.playersHashMap.containsKey(player))
/*     */     {
/* 112 */       MapInfo mapdata$mapinfo = new MapInfo(player);
/* 113 */       this.playersHashMap.put(player, mapdata$mapinfo);
/* 114 */       this.playersArrayList.add(mapdata$mapinfo);
/*     */     }
/*     */     
/* 117 */     if (!player.inventory.hasItemStack(mapStack))
/*     */     {
/* 119 */       this.mapDecorations.remove(player.getName());
/*     */     }
/*     */     
/* 122 */     for (int i = 0; i < this.playersArrayList.size(); i++)
/*     */     {
/* 124 */       MapInfo mapdata$mapinfo1 = (MapInfo)this.playersArrayList.get(i);
/*     */       
/* 126 */       if ((!mapdata$mapinfo1.entityplayerObj.isDead) && ((mapdata$mapinfo1.entityplayerObj.inventory.hasItemStack(mapStack)) || (mapStack.isOnItemFrame())))
/*     */       {
/* 128 */         if ((!mapStack.isOnItemFrame()) && (mapdata$mapinfo1.entityplayerObj.dimension == this.dimension))
/*     */         {
/* 130 */           updateDecorations(0, mapdata$mapinfo1.entityplayerObj.worldObj, mapdata$mapinfo1.entityplayerObj.getName(), mapdata$mapinfo1.entityplayerObj.posX, mapdata$mapinfo1.entityplayerObj.posZ, mapdata$mapinfo1.entityplayerObj.rotationYaw);
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 135 */         this.playersHashMap.remove(mapdata$mapinfo1.entityplayerObj);
/* 136 */         this.playersArrayList.remove(mapdata$mapinfo1);
/*     */       }
/*     */     }
/*     */     
/* 140 */     if (mapStack.isOnItemFrame())
/*     */     {
/* 142 */       EntityItemFrame entityitemframe = mapStack.getItemFrame();
/* 143 */       BlockPos blockpos = entityitemframe.getHangingPosition();
/* 144 */       updateDecorations(1, player.worldObj, "frame-" + entityitemframe.getEntityId(), blockpos.getX(), blockpos.getZ(), entityitemframe.facingDirection.getHorizontalIndex() * 90);
/*     */     }
/*     */     
/* 147 */     if ((mapStack.hasTagCompound()) && (mapStack.getTagCompound().hasKey("Decorations", 9)))
/*     */     {
/* 149 */       NBTTagList nbttaglist = mapStack.getTagCompound().getTagList("Decorations", 10);
/*     */       
/* 151 */       for (int j = 0; j < nbttaglist.tagCount(); j++)
/*     */       {
/* 153 */         NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(j);
/*     */         
/* 155 */         if (!this.mapDecorations.containsKey(nbttagcompound.getString("id")))
/*     */         {
/* 157 */           updateDecorations(nbttagcompound.getByte("type"), player.worldObj, nbttagcompound.getString("id"), nbttagcompound.getDouble("x"), nbttagcompound.getDouble("z"), nbttagcompound.getDouble("rot"));
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void updateDecorations(int type, World worldIn, String entityIdentifier, double worldX, double worldZ, double rotation)
/*     */   {
/* 165 */     int i = 1 << this.scale;
/* 166 */     float f = (float)(worldX - this.xCenter) / i;
/* 167 */     float f1 = (float)(worldZ - this.zCenter) / i;
/* 168 */     byte b0 = (byte)(int)(f * 2.0F + 0.5D);
/* 169 */     byte b1 = (byte)(int)(f1 * 2.0F + 0.5D);
/* 170 */     int j = 63;
/*     */     
/*     */     byte b2;
/* 173 */     if ((f >= -j) && (f1 >= -j) && (f <= j) && (f1 <= j))
/*     */     {
/* 175 */       rotation += (rotation < 0.0D ? -8.0D : 8.0D);
/* 176 */       byte b2 = (byte)(int)(rotation * 16.0D / 360.0D);
/*     */       
/* 178 */       if (this.dimension < 0)
/*     */       {
/* 180 */         int k = (int)(worldIn.getWorldInfo().getWorldTime() / 10L);
/* 181 */         b2 = (byte)(k * k * 34187121 + k * 121 >> 15 & 0xF);
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 186 */       if ((Math.abs(f) >= 320.0F) || (Math.abs(f1) >= 320.0F))
/*     */       {
/* 188 */         this.mapDecorations.remove(entityIdentifier);
/* 189 */         return;
/*     */       }
/*     */       
/* 192 */       type = 6;
/* 193 */       b2 = 0;
/*     */       
/* 195 */       if (f <= -j)
/*     */       {
/* 197 */         b0 = (byte)(int)(j * 2 + 2.5D);
/*     */       }
/*     */       
/* 200 */       if (f1 <= -j)
/*     */       {
/* 202 */         b1 = (byte)(int)(j * 2 + 2.5D);
/*     */       }
/*     */       
/* 205 */       if (f >= j)
/*     */       {
/* 207 */         b0 = (byte)(j * 2 + 1);
/*     */       }
/*     */       
/* 210 */       if (f1 >= j)
/*     */       {
/* 212 */         b1 = (byte)(j * 2 + 1);
/*     */       }
/*     */     }
/*     */     
/* 216 */     this.mapDecorations.put(entityIdentifier, new Vec4b((byte)type, b0, b1, b2));
/*     */   }
/*     */   
/*     */   public Packet getMapPacket(ItemStack mapStack, World worldIn, EntityPlayer player)
/*     */   {
/* 221 */     MapInfo mapdata$mapinfo = (MapInfo)this.playersHashMap.get(player);
/* 222 */     return mapdata$mapinfo == null ? null : mapdata$mapinfo.getPacket(mapStack);
/*     */   }
/*     */   
/*     */   public void updateMapData(int x, int y)
/*     */   {
/* 227 */     super.markDirty();
/*     */     
/* 229 */     for (MapInfo mapdata$mapinfo : this.playersArrayList)
/*     */     {
/* 231 */       mapdata$mapinfo.update(x, y);
/*     */     }
/*     */   }
/*     */   
/*     */   public MapInfo getMapInfo(EntityPlayer player)
/*     */   {
/* 237 */     MapInfo mapdata$mapinfo = (MapInfo)this.playersHashMap.get(player);
/*     */     
/* 239 */     if (mapdata$mapinfo == null)
/*     */     {
/* 241 */       mapdata$mapinfo = new MapInfo(player);
/* 242 */       this.playersHashMap.put(player, mapdata$mapinfo);
/* 243 */       this.playersArrayList.add(mapdata$mapinfo);
/*     */     }
/*     */     
/* 246 */     return mapdata$mapinfo;
/*     */   }
/*     */   
/*     */   public class MapInfo
/*     */   {
/*     */     public final EntityPlayer entityplayerObj;
/* 252 */     private boolean field_176105_d = true;
/* 253 */     private int minX = 0;
/* 254 */     private int minY = 0;
/* 255 */     private int maxX = 127;
/* 256 */     private int maxY = 127;
/*     */     private int field_176109_i;
/*     */     public int field_82569_d;
/*     */     
/*     */     public MapInfo(EntityPlayer player)
/*     */     {
/* 262 */       this.entityplayerObj = player;
/*     */     }
/*     */     
/*     */     public Packet getPacket(ItemStack stack)
/*     */     {
/* 267 */       if (this.field_176105_d)
/*     */       {
/* 269 */         this.field_176105_d = false;
/* 270 */         return new S34PacketMaps(stack.getMetadata(), MapData.this.scale, MapData.this.mapDecorations.values(), MapData.this.colors, this.minX, this.minY, this.maxX + 1 - this.minX, this.maxY + 1 - this.minY);
/*     */       }
/*     */       
/*     */ 
/* 274 */       return this.field_176109_i++ % 5 == 0 ? new S34PacketMaps(stack.getMetadata(), MapData.this.scale, MapData.this.mapDecorations.values(), MapData.this.colors, 0, 0, 0, 0) : null;
/*     */     }
/*     */     
/*     */ 
/*     */     public void update(int x, int y)
/*     */     {
/* 280 */       if (this.field_176105_d)
/*     */       {
/* 282 */         this.minX = Math.min(this.minX, x);
/* 283 */         this.minY = Math.min(this.minY, y);
/* 284 */         this.maxX = Math.max(this.maxX, x);
/* 285 */         this.maxY = Math.max(this.maxY, y);
/*     */       }
/*     */       else
/*     */       {
/* 289 */         this.field_176105_d = true;
/* 290 */         this.minX = x;
/* 291 */         this.minY = y;
/* 292 */         this.maxX = x;
/* 293 */         this.maxY = y;
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\storage\MapData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */