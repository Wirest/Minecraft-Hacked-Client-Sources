/*     */ package net.minecraft.world.gen.structure;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Random;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.nbt.NBTTagList;
/*     */ import net.minecraft.world.ChunkCoordIntPair;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public abstract class StructureStart
/*     */ {
/*  13 */   protected LinkedList<StructureComponent> components = new LinkedList();
/*     */   
/*     */   protected StructureBoundingBox boundingBox;
/*     */   
/*     */   private int chunkPosX;
/*     */   private int chunkPosZ;
/*     */   
/*     */   public StructureStart() {}
/*     */   
/*     */   public StructureStart(int chunkX, int chunkZ)
/*     */   {
/*  24 */     this.chunkPosX = chunkX;
/*  25 */     this.chunkPosZ = chunkZ;
/*     */   }
/*     */   
/*     */   public StructureBoundingBox getBoundingBox()
/*     */   {
/*  30 */     return this.boundingBox;
/*     */   }
/*     */   
/*     */   public LinkedList<StructureComponent> getComponents()
/*     */   {
/*  35 */     return this.components;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void generateStructure(World worldIn, Random rand, StructureBoundingBox structurebb)
/*     */   {
/*  43 */     Iterator<StructureComponent> iterator = this.components.iterator();
/*     */     
/*  45 */     while (iterator.hasNext())
/*     */     {
/*  47 */       StructureComponent structurecomponent = (StructureComponent)iterator.next();
/*     */       
/*  49 */       if ((structurecomponent.getBoundingBox().intersectsWith(structurebb)) && (!structurecomponent.addComponentParts(worldIn, rand, structurebb)))
/*     */       {
/*  51 */         iterator.remove();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void updateBoundingBox()
/*     */   {
/*  61 */     this.boundingBox = StructureBoundingBox.getNewBoundingBox();
/*     */     
/*  63 */     for (StructureComponent structurecomponent : this.components)
/*     */     {
/*  65 */       this.boundingBox.expandTo(structurecomponent.getBoundingBox());
/*     */     }
/*     */   }
/*     */   
/*     */   public NBTTagCompound writeStructureComponentsToNBT(int chunkX, int chunkZ)
/*     */   {
/*  71 */     NBTTagCompound nbttagcompound = new NBTTagCompound();
/*  72 */     nbttagcompound.setString("id", MapGenStructureIO.getStructureStartName(this));
/*  73 */     nbttagcompound.setInteger("ChunkX", chunkX);
/*  74 */     nbttagcompound.setInteger("ChunkZ", chunkZ);
/*  75 */     nbttagcompound.setTag("BB", this.boundingBox.toNBTTagIntArray());
/*  76 */     NBTTagList nbttaglist = new NBTTagList();
/*     */     
/*  78 */     for (StructureComponent structurecomponent : this.components)
/*     */     {
/*  80 */       nbttaglist.appendTag(structurecomponent.createStructureBaseNBT());
/*     */     }
/*     */     
/*  83 */     nbttagcompound.setTag("Children", nbttaglist);
/*  84 */     writeToNBT(nbttagcompound);
/*  85 */     return nbttagcompound;
/*     */   }
/*     */   
/*     */ 
/*     */   public void writeToNBT(NBTTagCompound tagCompound) {}
/*     */   
/*     */ 
/*     */   public void readStructureComponentsFromNBT(World worldIn, NBTTagCompound tagCompound)
/*     */   {
/*  94 */     this.chunkPosX = tagCompound.getInteger("ChunkX");
/*  95 */     this.chunkPosZ = tagCompound.getInteger("ChunkZ");
/*     */     
/*  97 */     if (tagCompound.hasKey("BB"))
/*     */     {
/*  99 */       this.boundingBox = new StructureBoundingBox(tagCompound.getIntArray("BB"));
/*     */     }
/*     */     
/* 102 */     NBTTagList nbttaglist = tagCompound.getTagList("Children", 10);
/*     */     
/* 104 */     for (int i = 0; i < nbttaglist.tagCount(); i++)
/*     */     {
/* 106 */       this.components.add(MapGenStructureIO.getStructureComponent(nbttaglist.getCompoundTagAt(i), worldIn));
/*     */     }
/*     */     
/* 109 */     readFromNBT(tagCompound);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void readFromNBT(NBTTagCompound tagCompound) {}
/*     */   
/*     */ 
/*     */ 
/*     */   protected void markAvailableHeight(World worldIn, Random rand, int p_75067_3_)
/*     */   {
/* 121 */     int i = worldIn.func_181545_F() - p_75067_3_;
/* 122 */     int j = this.boundingBox.getYSize() + 1;
/*     */     
/* 124 */     if (j < i)
/*     */     {
/* 126 */       j += rand.nextInt(i - j);
/*     */     }
/*     */     
/* 129 */     int k = j - this.boundingBox.maxY;
/* 130 */     this.boundingBox.offset(0, k, 0);
/*     */     
/* 132 */     for (StructureComponent structurecomponent : this.components)
/*     */     {
/* 134 */       structurecomponent.func_181138_a(0, k, 0);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void setRandomHeight(World worldIn, Random rand, int p_75070_3_, int p_75070_4_)
/*     */   {
/* 140 */     int i = p_75070_4_ - p_75070_3_ + 1 - this.boundingBox.getYSize();
/* 141 */     int j = 1;
/*     */     
/* 143 */     if (i > 1)
/*     */     {
/* 145 */       j = p_75070_3_ + rand.nextInt(i);
/*     */     }
/*     */     else
/*     */     {
/* 149 */       j = p_75070_3_;
/*     */     }
/*     */     
/* 152 */     int k = j - this.boundingBox.minY;
/* 153 */     this.boundingBox.offset(0, k, 0);
/*     */     
/* 155 */     for (StructureComponent structurecomponent : this.components)
/*     */     {
/* 157 */       structurecomponent.func_181138_a(0, k, 0);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isSizeableStructure()
/*     */   {
/* 166 */     return true;
/*     */   }
/*     */   
/*     */   public boolean func_175788_a(ChunkCoordIntPair pair)
/*     */   {
/* 171 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   public void func_175787_b(ChunkCoordIntPair pair) {}
/*     */   
/*     */ 
/*     */   public int getChunkPosX()
/*     */   {
/* 180 */     return this.chunkPosX;
/*     */   }
/*     */   
/*     */   public int getChunkPosZ()
/*     */   {
/* 185 */     return this.chunkPosZ;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\gen\structure\StructureStart.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */