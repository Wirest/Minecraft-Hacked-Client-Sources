/*     */ package net.minecraft.item;
/*     */ 
/*     */ import com.google.common.collect.Multiset;
/*     */ import java.util.List;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockDirt;
/*     */ import net.minecraft.block.BlockStone;
/*     */ import net.minecraft.block.material.MapColor;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.util.BlockPos.MutableBlockPos;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.WorldProvider;
/*     */ import net.minecraft.world.chunk.Chunk;
/*     */ import net.minecraft.world.storage.MapData;
/*     */ import net.minecraft.world.storage.MapData.MapInfo;
/*     */ import net.minecraft.world.storage.WorldInfo;
/*     */ 
/*     */ public class ItemMap extends ItemMapBase
/*     */ {
/*     */   protected ItemMap()
/*     */   {
/*  28 */     setHasSubtypes(true);
/*     */   }
/*     */   
/*     */   public static MapData loadMapData(int mapId, World worldIn)
/*     */   {
/*  33 */     String s = "map_" + mapId;
/*  34 */     MapData mapdata = (MapData)worldIn.loadItemData(MapData.class, s);
/*     */     
/*  36 */     if (mapdata == null)
/*     */     {
/*  38 */       mapdata = new MapData(s);
/*  39 */       worldIn.setItemData(s, mapdata);
/*     */     }
/*     */     
/*  42 */     return mapdata;
/*     */   }
/*     */   
/*     */   public MapData getMapData(ItemStack stack, World worldIn)
/*     */   {
/*  47 */     String s = "map_" + stack.getMetadata();
/*  48 */     MapData mapdata = (MapData)worldIn.loadItemData(MapData.class, s);
/*     */     
/*  50 */     if ((mapdata == null) && (!worldIn.isRemote))
/*     */     {
/*  52 */       stack.setItemDamage(worldIn.getUniqueDataId("map"));
/*  53 */       s = "map_" + stack.getMetadata();
/*  54 */       mapdata = new MapData(s);
/*  55 */       mapdata.scale = 3;
/*  56 */       mapdata.calculateMapCenter(worldIn.getWorldInfo().getSpawnX(), worldIn.getWorldInfo().getSpawnZ(), mapdata.scale);
/*  57 */       mapdata.dimension = ((byte)worldIn.provider.getDimensionId());
/*  58 */       mapdata.markDirty();
/*  59 */       worldIn.setItemData(s, mapdata);
/*     */     }
/*     */     
/*  62 */     return mapdata;
/*     */   }
/*     */   
/*     */   public void updateMapData(World worldIn, Entity viewer, MapData data)
/*     */   {
/*  67 */     if ((worldIn.provider.getDimensionId() == data.dimension) && ((viewer instanceof EntityPlayer)))
/*     */     {
/*  69 */       int i = 1 << data.scale;
/*  70 */       int j = data.xCenter;
/*  71 */       int k = data.zCenter;
/*  72 */       int l = MathHelper.floor_double(viewer.posX - j) / i + 64;
/*  73 */       int i1 = MathHelper.floor_double(viewer.posZ - k) / i + 64;
/*  74 */       int j1 = 128 / i;
/*     */       
/*  76 */       if (worldIn.provider.getHasNoSky())
/*     */       {
/*  78 */         j1 /= 2;
/*     */       }
/*     */       
/*  81 */       MapData.MapInfo mapdata$mapinfo = data.getMapInfo((EntityPlayer)viewer);
/*  82 */       mapdata$mapinfo.field_82569_d += 1;
/*  83 */       boolean flag = false;
/*     */       
/*  85 */       for (int k1 = l - j1 + 1; k1 < l + j1; k1++)
/*     */       {
/*  87 */         if (((k1 & 0xF) == (mapdata$mapinfo.field_82569_d & 0xF)) || (flag))
/*     */         {
/*  89 */           flag = false;
/*  90 */           double d0 = 0.0D;
/*     */           
/*  92 */           for (int l1 = i1 - j1 - 1; l1 < i1 + j1; l1++)
/*     */           {
/*  94 */             if ((k1 >= 0) && (l1 >= -1) && (k1 < 128) && (l1 < 128))
/*     */             {
/*  96 */               int i2 = k1 - l;
/*  97 */               int j2 = l1 - i1;
/*  98 */               boolean flag1 = i2 * i2 + j2 * j2 > (j1 - 2) * (j1 - 2);
/*  99 */               int k2 = (j / i + k1 - 64) * i;
/* 100 */               int l2 = (k / i + l1 - 64) * i;
/* 101 */               Multiset<MapColor> multiset = com.google.common.collect.HashMultiset.create();
/* 102 */               Chunk chunk = worldIn.getChunkFromBlockCoords(new net.minecraft.util.BlockPos(k2, 0, l2));
/*     */               
/* 104 */               if (!chunk.isEmpty())
/*     */               {
/* 106 */                 int i3 = k2 & 0xF;
/* 107 */                 int j3 = l2 & 0xF;
/* 108 */                 int k3 = 0;
/* 109 */                 double d1 = 0.0D;
/*     */                 
/* 111 */                 if (worldIn.provider.getHasNoSky())
/*     */                 {
/* 113 */                   int l3 = k2 + l2 * 231871;
/* 114 */                   l3 = l3 * l3 * 31287121 + l3 * 11;
/*     */                   
/* 116 */                   if ((l3 >> 20 & 0x1) == 0)
/*     */                   {
/* 118 */                     multiset.add(Blocks.dirt.getMapColor(Blocks.dirt.getDefaultState().withProperty(BlockDirt.VARIANT, net.minecraft.block.BlockDirt.DirtType.DIRT)), 10);
/*     */                   }
/*     */                   else
/*     */                   {
/* 122 */                     multiset.add(Blocks.stone.getMapColor(Blocks.stone.getDefaultState().withProperty(BlockStone.VARIANT, net.minecraft.block.BlockStone.EnumType.STONE)), 100);
/*     */                   }
/*     */                   
/* 125 */                   d1 = 100.0D;
/*     */                 }
/*     */                 else
/*     */                 {
/* 129 */                   BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
/*     */                   
/* 131 */                   for (int i4 = 0; i4 < i; i4++)
/*     */                   {
/* 133 */                     for (int j4 = 0; j4 < i; j4++)
/*     */                     {
/* 135 */                       int k4 = chunk.getHeightValue(i4 + i3, j4 + j3) + 1;
/* 136 */                       IBlockState iblockstate = Blocks.air.getDefaultState();
/*     */                       
/* 138 */                       if (k4 > 1)
/*     */                       {
/*     */ 
/*     */                         do
/*     */                         {
/*     */ 
/* 144 */                           k4--;
/* 145 */                           iblockstate = chunk.getBlockState(blockpos$mutableblockpos.func_181079_c(i4 + i3, k4, j4 + j3));
/*     */                         }
/* 147 */                         while ((iblockstate.getBlock().getMapColor(iblockstate) == MapColor.airColor) && (k4 > 0));
/*     */                         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 153 */                         if ((k4 > 0) && (iblockstate.getBlock().getMaterial().isLiquid()))
/*     */                         {
/* 155 */                           int l4 = k4 - 1;
/*     */                           Block block;
/*     */                           do
/*     */                           {
/* 159 */                             block = chunk.getBlock(i4 + i3, l4--, j4 + j3);
/* 160 */                             k3++;
/*     */                           }
/* 162 */                           while ((l4 > 0) && (block.getMaterial().isLiquid()));
/*     */                         }
/*     */                       }
/*     */                       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 171 */                       d1 = d1 + k4 / (i * i);
/* 172 */                       multiset.add(iblockstate.getBlock().getMapColor(iblockstate));
/*     */                     }
/*     */                   }
/*     */                 }
/*     */                 
/* 177 */                 k3 /= i * i;
/* 178 */                 double d2 = (d1 - d0) * 4.0D / (i + 4) + ((k1 + l1 & 0x1) - 0.5D) * 0.4D;
/* 179 */                 int i5 = 1;
/*     */                 
/* 181 */                 if (d2 > 0.6D)
/*     */                 {
/* 183 */                   i5 = 2;
/*     */                 }
/*     */                 
/* 186 */                 if (d2 < -0.6D)
/*     */                 {
/* 188 */                   i5 = 0;
/*     */                 }
/*     */                 
/* 191 */                 MapColor mapcolor = (MapColor)com.google.common.collect.Iterables.getFirst(com.google.common.collect.Multisets.copyHighestCountFirst(multiset), MapColor.airColor);
/*     */                 
/* 193 */                 if (mapcolor == MapColor.waterColor)
/*     */                 {
/* 195 */                   d2 = k3 * 0.1D + (k1 + l1 & 0x1) * 0.2D;
/* 196 */                   i5 = 1;
/*     */                   
/* 198 */                   if (d2 < 0.5D)
/*     */                   {
/* 200 */                     i5 = 2;
/*     */                   }
/*     */                   
/* 203 */                   if (d2 > 0.9D)
/*     */                   {
/* 205 */                     i5 = 0;
/*     */                   }
/*     */                 }
/*     */                 
/* 209 */                 d0 = d1;
/*     */                 
/* 211 */                 if ((l1 >= 0) && (i2 * i2 + j2 * j2 < j1 * j1) && ((!flag1) || ((k1 + l1 & 0x1) != 0)))
/*     */                 {
/* 213 */                   byte b0 = data.colors[(k1 + l1 * 128)];
/* 214 */                   byte b1 = (byte)(mapcolor.colorIndex * 4 + i5);
/*     */                   
/* 216 */                   if (b0 != b1)
/*     */                   {
/* 218 */                     data.colors[(k1 + l1 * 128)] = b1;
/* 219 */                     data.updateMapData(k1, l1);
/* 220 */                     flag = true;
/*     */                   }
/*     */                 }
/*     */               }
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
/*     */ 
/*     */   public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
/*     */   {
/* 237 */     if (!worldIn.isRemote)
/*     */     {
/* 239 */       MapData mapdata = getMapData(stack, worldIn);
/*     */       
/* 241 */       if ((entityIn instanceof EntityPlayer))
/*     */       {
/* 243 */         EntityPlayer entityplayer = (EntityPlayer)entityIn;
/* 244 */         mapdata.updateVisiblePlayers(entityplayer, stack);
/*     */       }
/*     */       
/* 247 */       if (isSelected)
/*     */       {
/* 249 */         updateMapData(worldIn, entityIn, mapdata);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public net.minecraft.network.Packet createMapDataPacket(ItemStack stack, World worldIn, EntityPlayer player)
/*     */   {
/* 256 */     return getMapData(stack, worldIn).getMapPacket(stack, worldIn, player);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn)
/*     */   {
/* 264 */     if ((stack.hasTagCompound()) && (stack.getTagCompound().getBoolean("map_is_scaling")))
/*     */     {
/* 266 */       MapData mapdata = Items.filled_map.getMapData(stack, worldIn);
/* 267 */       stack.setItemDamage(worldIn.getUniqueDataId("map"));
/* 268 */       MapData mapdata1 = new MapData("map_" + stack.getMetadata());
/* 269 */       mapdata1.scale = ((byte)(mapdata.scale + 1));
/*     */       
/* 271 */       if (mapdata1.scale > 4)
/*     */       {
/* 273 */         mapdata1.scale = 4;
/*     */       }
/*     */       
/* 276 */       mapdata1.calculateMapCenter(mapdata.xCenter, mapdata.zCenter, mapdata1.scale);
/* 277 */       mapdata1.dimension = mapdata.dimension;
/* 278 */       mapdata1.markDirty();
/* 279 */       worldIn.setItemData("map_" + stack.getMetadata(), mapdata1);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
/*     */   {
/* 288 */     MapData mapdata = getMapData(stack, playerIn.worldObj);
/*     */     
/* 290 */     if (advanced)
/*     */     {
/* 292 */       if (mapdata == null)
/*     */       {
/* 294 */         tooltip.add("Unknown map");
/*     */       }
/*     */       else
/*     */       {
/* 298 */         tooltip.add("Scaling at 1:" + (1 << mapdata.scale));
/* 299 */         tooltip.add("(Level " + mapdata.scale + "/" + 4 + ")");
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\item\ItemMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */