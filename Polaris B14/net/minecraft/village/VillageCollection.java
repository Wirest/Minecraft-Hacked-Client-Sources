/*     */ package net.minecraft.village;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockDoor;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.nbt.NBTTagList;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.WorldProvider;
/*     */ import net.minecraft.world.WorldSavedData;
/*     */ 
/*     */ public class VillageCollection extends WorldSavedData
/*     */ {
/*     */   private World worldObj;
/*  20 */   private final List<BlockPos> villagerPositionsList = Lists.newArrayList();
/*  21 */   private final List<VillageDoorInfo> newDoors = Lists.newArrayList();
/*  22 */   private final List<Village> villageList = Lists.newArrayList();
/*     */   private int tickCounter;
/*     */   
/*     */   public VillageCollection(String name)
/*     */   {
/*  27 */     super(name);
/*     */   }
/*     */   
/*     */   public VillageCollection(World worldIn)
/*     */   {
/*  32 */     super(fileNameForProvider(worldIn.provider));
/*  33 */     this.worldObj = worldIn;
/*  34 */     markDirty();
/*     */   }
/*     */   
/*     */   public void setWorldsForAll(World worldIn)
/*     */   {
/*  39 */     this.worldObj = worldIn;
/*     */     
/*  41 */     for (Village village : this.villageList)
/*     */     {
/*  43 */       village.setWorld(worldIn);
/*     */     }
/*     */   }
/*     */   
/*     */   public void addToVillagerPositionList(BlockPos pos)
/*     */   {
/*  49 */     if (this.villagerPositionsList.size() <= 64)
/*     */     {
/*  51 */       if (!positionInList(pos))
/*     */       {
/*  53 */         this.villagerPositionsList.add(pos);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void tick()
/*     */   {
/*  63 */     this.tickCounter += 1;
/*     */     
/*  65 */     for (Village village : this.villageList)
/*     */     {
/*  67 */       village.tick(this.tickCounter);
/*     */     }
/*     */     
/*  70 */     removeAnnihilatedVillages();
/*  71 */     dropOldestVillagerPosition();
/*  72 */     addNewDoorsToVillageOrCreateVillage();
/*     */     
/*  74 */     if (this.tickCounter % 400 == 0)
/*     */     {
/*  76 */       markDirty();
/*     */     }
/*     */   }
/*     */   
/*     */   private void removeAnnihilatedVillages()
/*     */   {
/*  82 */     Iterator<Village> iterator = this.villageList.iterator();
/*     */     
/*  84 */     while (iterator.hasNext())
/*     */     {
/*  86 */       Village village = (Village)iterator.next();
/*     */       
/*  88 */       if (village.isAnnihilated())
/*     */       {
/*  90 */         iterator.remove();
/*  91 */         markDirty();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public List<Village> getVillageList()
/*     */   {
/*  98 */     return this.villageList;
/*     */   }
/*     */   
/*     */   public Village getNearestVillage(BlockPos doorBlock, int radius)
/*     */   {
/* 103 */     Village village = null;
/* 104 */     double d0 = 3.4028234663852886E38D;
/*     */     
/* 106 */     for (Village village1 : this.villageList)
/*     */     {
/* 108 */       double d1 = village1.getCenter().distanceSq(doorBlock);
/*     */       
/* 110 */       if (d1 < d0)
/*     */       {
/* 112 */         float f = radius + village1.getVillageRadius();
/*     */         
/* 114 */         if (d1 <= f * f)
/*     */         {
/* 116 */           village = village1;
/* 117 */           d0 = d1;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 122 */     return village;
/*     */   }
/*     */   
/*     */   private void dropOldestVillagerPosition()
/*     */   {
/* 127 */     if (!this.villagerPositionsList.isEmpty())
/*     */     {
/* 129 */       addDoorsAround((BlockPos)this.villagerPositionsList.remove(0));
/*     */     }
/*     */   }
/*     */   
/*     */   private void addNewDoorsToVillageOrCreateVillage()
/*     */   {
/* 135 */     for (int i = 0; i < this.newDoors.size(); i++)
/*     */     {
/* 137 */       VillageDoorInfo villagedoorinfo = (VillageDoorInfo)this.newDoors.get(i);
/* 138 */       Village village = getNearestVillage(villagedoorinfo.getDoorBlockPos(), 32);
/*     */       
/* 140 */       if (village == null)
/*     */       {
/* 142 */         village = new Village(this.worldObj);
/* 143 */         this.villageList.add(village);
/* 144 */         markDirty();
/*     */       }
/*     */       
/* 147 */       village.addVillageDoorInfo(villagedoorinfo);
/*     */     }
/*     */     
/* 150 */     this.newDoors.clear();
/*     */   }
/*     */   
/*     */   private void addDoorsAround(BlockPos central)
/*     */   {
/* 155 */     int i = 16;
/* 156 */     int j = 4;
/* 157 */     int k = 16;
/*     */     
/* 159 */     for (int l = -i; l < i; l++)
/*     */     {
/* 161 */       for (int i1 = -j; i1 < j; i1++)
/*     */       {
/* 163 */         for (int j1 = -k; j1 < k; j1++)
/*     */         {
/* 165 */           BlockPos blockpos = central.add(l, i1, j1);
/*     */           
/* 167 */           if (isWoodDoor(blockpos))
/*     */           {
/* 169 */             VillageDoorInfo villagedoorinfo = checkDoorExistence(blockpos);
/*     */             
/* 171 */             if (villagedoorinfo == null)
/*     */             {
/* 173 */               addToNewDoorsList(blockpos);
/*     */             }
/*     */             else
/*     */             {
/* 177 */               villagedoorinfo.func_179849_a(this.tickCounter);
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
/*     */   private VillageDoorInfo checkDoorExistence(BlockPos doorBlock)
/*     */   {
/* 190 */     for (VillageDoorInfo villagedoorinfo : this.newDoors)
/*     */     {
/* 192 */       if ((villagedoorinfo.getDoorBlockPos().getX() == doorBlock.getX()) && (villagedoorinfo.getDoorBlockPos().getZ() == doorBlock.getZ()) && (Math.abs(villagedoorinfo.getDoorBlockPos().getY() - doorBlock.getY()) <= 1))
/*     */       {
/* 194 */         return villagedoorinfo;
/*     */       }
/*     */     }
/*     */     
/* 198 */     for (Village village : this.villageList)
/*     */     {
/* 200 */       VillageDoorInfo villagedoorinfo1 = village.getExistedDoor(doorBlock);
/*     */       
/* 202 */       if (villagedoorinfo1 != null)
/*     */       {
/* 204 */         return villagedoorinfo1;
/*     */       }
/*     */     }
/*     */     
/* 208 */     return null;
/*     */   }
/*     */   
/*     */   private void addToNewDoorsList(BlockPos doorBlock)
/*     */   {
/* 213 */     EnumFacing enumfacing = BlockDoor.getFacing(this.worldObj, doorBlock);
/* 214 */     EnumFacing enumfacing1 = enumfacing.getOpposite();
/* 215 */     int i = countBlocksCanSeeSky(doorBlock, enumfacing, 5);
/* 216 */     int j = countBlocksCanSeeSky(doorBlock, enumfacing1, i + 1);
/*     */     
/* 218 */     if (i != j)
/*     */     {
/* 220 */       this.newDoors.add(new VillageDoorInfo(doorBlock, i < j ? enumfacing : enumfacing1, this.tickCounter));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private int countBlocksCanSeeSky(BlockPos centerPos, EnumFacing direction, int limitation)
/*     */   {
/* 229 */     int i = 0;
/*     */     
/* 231 */     for (int j = 1; j <= 5; j++)
/*     */     {
/* 233 */       if (this.worldObj.canSeeSky(centerPos.offset(direction, j)))
/*     */       {
/* 235 */         i++;
/*     */         
/* 237 */         if (i >= limitation)
/*     */         {
/* 239 */           return i;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 244 */     return i;
/*     */   }
/*     */   
/*     */   private boolean positionInList(BlockPos pos)
/*     */   {
/* 249 */     for (BlockPos blockpos : this.villagerPositionsList)
/*     */     {
/* 251 */       if (blockpos.equals(pos))
/*     */       {
/* 253 */         return true;
/*     */       }
/*     */     }
/*     */     
/* 257 */     return false;
/*     */   }
/*     */   
/*     */   private boolean isWoodDoor(BlockPos doorPos)
/*     */   {
/* 262 */     Block block = this.worldObj.getBlockState(doorPos).getBlock();
/* 263 */     return block.getMaterial() == net.minecraft.block.material.Material.wood;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void readFromNBT(NBTTagCompound nbt)
/*     */   {
/* 271 */     this.tickCounter = nbt.getInteger("Tick");
/* 272 */     NBTTagList nbttaglist = nbt.getTagList("Villages", 10);
/*     */     
/* 274 */     for (int i = 0; i < nbttaglist.tagCount(); i++)
/*     */     {
/* 276 */       NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
/* 277 */       Village village = new Village();
/* 278 */       village.readVillageDataFromNBT(nbttagcompound);
/* 279 */       this.villageList.add(village);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeToNBT(NBTTagCompound nbt)
/*     */   {
/* 288 */     nbt.setInteger("Tick", this.tickCounter);
/* 289 */     NBTTagList nbttaglist = new NBTTagList();
/*     */     
/* 291 */     for (Village village : this.villageList)
/*     */     {
/* 293 */       NBTTagCompound nbttagcompound = new NBTTagCompound();
/* 294 */       village.writeVillageDataToNBT(nbttagcompound);
/* 295 */       nbttaglist.appendTag(nbttagcompound);
/*     */     }
/*     */     
/* 298 */     nbt.setTag("Villages", nbttaglist);
/*     */   }
/*     */   
/*     */   public static String fileNameForProvider(WorldProvider provider)
/*     */   {
/* 303 */     return "villages" + provider.getInternalNameSuffix();
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\village\VillageCollection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */