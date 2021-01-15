/*     */ package net.minecraft.tileentity;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockStainedGlass;
/*     */ import net.minecraft.block.BlockStainedGlassPane;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.passive.EntitySheep;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.InventoryPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.inventory.Container;
/*     */ import net.minecraft.inventory.ContainerBeacon;
/*     */ import net.minecraft.inventory.IInventory;
/*     */ import net.minecraft.item.EnumDyeColor;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
/*     */ import net.minecraft.potion.Potion;
/*     */ import net.minecraft.potion.PotionEffect;
/*     */ import net.minecraft.stats.AchievementList;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.BlockPos.MutableBlockPos;
/*     */ import net.minecraft.util.ITickable;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class TileEntityBeacon extends TileEntityLockable implements ITickable, IInventory
/*     */ {
/*  33 */   public static final Potion[][] effectsList = { { Potion.moveSpeed, Potion.digSpeed }, { Potion.resistance, Potion.jump }, { Potion.damageBoost }, { Potion.regeneration } };
/*  34 */   private final List<BeamSegment> beamSegments = Lists.newArrayList();
/*     */   
/*     */   private long beamRenderCounter;
/*     */   
/*     */   private float field_146014_j;
/*     */   private boolean isComplete;
/*  40 */   private int levels = -1;
/*     */   
/*     */ 
/*     */   private int primaryEffect;
/*     */   
/*     */ 
/*     */   private int secondaryEffect;
/*     */   
/*     */ 
/*     */   private ItemStack payment;
/*     */   
/*     */ 
/*     */   private String customName;
/*     */   
/*     */ 
/*     */   public void update()
/*     */   {
/*  57 */     if (this.worldObj.getTotalWorldTime() % 80L == 0L)
/*     */     {
/*  59 */       updateBeacon();
/*     */     }
/*     */   }
/*     */   
/*     */   public void updateBeacon()
/*     */   {
/*  65 */     updateSegmentColors();
/*  66 */     addEffectsToPlayers();
/*     */   }
/*     */   
/*     */   private void addEffectsToPlayers()
/*     */   {
/*  71 */     if ((this.isComplete) && (this.levels > 0) && (!this.worldObj.isRemote) && (this.primaryEffect > 0))
/*     */     {
/*  73 */       double d0 = this.levels * 10 + 10;
/*  74 */       int i = 0;
/*     */       
/*  76 */       if ((this.levels >= 4) && (this.primaryEffect == this.secondaryEffect))
/*     */       {
/*  78 */         i = 1;
/*     */       }
/*     */       
/*  81 */       int j = this.pos.getX();
/*  82 */       int k = this.pos.getY();
/*  83 */       int l = this.pos.getZ();
/*  84 */       AxisAlignedBB axisalignedbb = new AxisAlignedBB(j, k, l, j + 1, k + 1, l + 1).expand(d0, d0, d0).addCoord(0.0D, this.worldObj.getHeight(), 0.0D);
/*  85 */       List<EntityPlayer> list = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, axisalignedbb);
/*     */       
/*  87 */       for (EntityPlayer entityplayer : list)
/*     */       {
/*  89 */         entityplayer.addPotionEffect(new PotionEffect(this.primaryEffect, 180, i, true, true));
/*     */       }
/*     */       
/*  92 */       if ((this.levels >= 4) && (this.primaryEffect != this.secondaryEffect) && (this.secondaryEffect > 0))
/*     */       {
/*  94 */         for (EntityPlayer entityplayer1 : list)
/*     */         {
/*  96 */           entityplayer1.addPotionEffect(new PotionEffect(this.secondaryEffect, 180, 0, true, true));
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void updateSegmentColors()
/*     */   {
/* 104 */     int i = this.levels;
/* 105 */     int j = this.pos.getX();
/* 106 */     int k = this.pos.getY();
/* 107 */     int l = this.pos.getZ();
/* 108 */     this.levels = 0;
/* 109 */     this.beamSegments.clear();
/* 110 */     this.isComplete = true;
/* 111 */     BeamSegment tileentitybeacon$beamsegment = new BeamSegment(EntitySheep.func_175513_a(EnumDyeColor.WHITE));
/* 112 */     this.beamSegments.add(tileentitybeacon$beamsegment);
/* 113 */     boolean flag = true;
/* 114 */     BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
/*     */     
/* 116 */     for (int i1 = k + 1; i1 < 256; i1++)
/*     */     {
/* 118 */       IBlockState iblockstate = this.worldObj.getBlockState(blockpos$mutableblockpos.func_181079_c(j, i1, l));
/*     */       float[] afloat;
/*     */       float[] afloat;
/* 121 */       if (iblockstate.getBlock() == Blocks.stained_glass)
/*     */       {
/* 123 */         afloat = EntitySheep.func_175513_a((EnumDyeColor)iblockstate.getValue(BlockStainedGlass.COLOR));
/*     */       }
/*     */       else
/*     */       {
/* 127 */         if (iblockstate.getBlock() != Blocks.stained_glass_pane)
/*     */         {
/* 129 */           if ((iblockstate.getBlock().getLightOpacity() >= 15) && (iblockstate.getBlock() != Blocks.bedrock))
/*     */           {
/* 131 */             this.isComplete = false;
/* 132 */             this.beamSegments.clear();
/* 133 */             break;
/*     */           }
/*     */           
/* 136 */           tileentitybeacon$beamsegment.incrementHeight();
/* 137 */           continue;
/*     */         }
/*     */         
/* 140 */         afloat = EntitySheep.func_175513_a((EnumDyeColor)iblockstate.getValue(BlockStainedGlassPane.COLOR));
/*     */       }
/*     */       
/* 143 */       if (!flag)
/*     */       {
/* 145 */         afloat = new float[] { (tileentitybeacon$beamsegment.getColors()[0] + afloat[0]) / 2.0F, (tileentitybeacon$beamsegment.getColors()[1] + afloat[1]) / 2.0F, (tileentitybeacon$beamsegment.getColors()[2] + afloat[2]) / 2.0F };
/*     */       }
/*     */       
/* 148 */       if (Arrays.equals(afloat, tileentitybeacon$beamsegment.getColors()))
/*     */       {
/* 150 */         tileentitybeacon$beamsegment.incrementHeight();
/*     */       }
/*     */       else
/*     */       {
/* 154 */         tileentitybeacon$beamsegment = new BeamSegment(afloat);
/* 155 */         this.beamSegments.add(tileentitybeacon$beamsegment);
/*     */       }
/*     */       
/* 158 */       flag = false;
/*     */     }
/*     */     int i2;
/* 161 */     if (this.isComplete)
/*     */     {
/* 163 */       for (int l1 = 1; l1 <= 4; this.levels = (l1++))
/*     */       {
/* 165 */         i2 = k - l1;
/*     */         
/* 167 */         if (i2 < 0) {
/*     */           break;
/*     */         }
/*     */         
/*     */ 
/* 172 */         boolean flag1 = true;
/*     */         
/* 174 */         for (int j1 = j - l1; (j1 <= j + l1) && (flag1); j1++)
/*     */         {
/* 176 */           for (int k1 = l - l1; k1 <= l + l1; k1++)
/*     */           {
/* 178 */             Block block = this.worldObj.getBlockState(new BlockPos(j1, i2, k1)).getBlock();
/*     */             
/* 180 */             if ((block != Blocks.emerald_block) && (block != Blocks.gold_block) && (block != Blocks.diamond_block) && (block != Blocks.iron_block))
/*     */             {
/* 182 */               flag1 = false;
/* 183 */               break;
/*     */             }
/*     */           }
/*     */         }
/*     */         
/* 188 */         if (!flag1) {
/*     */           break;
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 194 */       if (this.levels == 0)
/*     */       {
/* 196 */         this.isComplete = false;
/*     */       }
/*     */     }
/*     */     
/* 200 */     if ((!this.worldObj.isRemote) && (this.levels == 4) && (i < this.levels))
/*     */     {
/* 202 */       for (EntityPlayer entityplayer : this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(j, k, l, j, k - 4, l).expand(10.0D, 5.0D, 10.0D)))
/*     */       {
/* 204 */         entityplayer.triggerAchievement(AchievementList.fullBeacon);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public List<BeamSegment> getBeamSegments()
/*     */   {
/* 211 */     return this.beamSegments;
/*     */   }
/*     */   
/*     */   public float shouldBeamRender()
/*     */   {
/* 216 */     if (!this.isComplete)
/*     */     {
/* 218 */       return 0.0F;
/*     */     }
/*     */     
/*     */ 
/* 222 */     int i = (int)(this.worldObj.getTotalWorldTime() - this.beamRenderCounter);
/* 223 */     this.beamRenderCounter = this.worldObj.getTotalWorldTime();
/*     */     
/* 225 */     if (i > 1)
/*     */     {
/* 227 */       this.field_146014_j -= i / 40.0F;
/*     */       
/* 229 */       if (this.field_146014_j < 0.0F)
/*     */       {
/* 231 */         this.field_146014_j = 0.0F;
/*     */       }
/*     */     }
/*     */     
/* 235 */     this.field_146014_j += 0.025F;
/*     */     
/* 237 */     if (this.field_146014_j > 1.0F)
/*     */     {
/* 239 */       this.field_146014_j = 1.0F;
/*     */     }
/*     */     
/* 242 */     return this.field_146014_j;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public net.minecraft.network.Packet getDescriptionPacket()
/*     */   {
/* 252 */     NBTTagCompound nbttagcompound = new NBTTagCompound();
/* 253 */     writeToNBT(nbttagcompound);
/* 254 */     return new S35PacketUpdateTileEntity(this.pos, 3, nbttagcompound);
/*     */   }
/*     */   
/*     */   public double getMaxRenderDistanceSquared()
/*     */   {
/* 259 */     return 65536.0D;
/*     */   }
/*     */   
/*     */   private int func_183001_h(int p_183001_1_)
/*     */   {
/* 264 */     if ((p_183001_1_ >= 0) && (p_183001_1_ < Potion.potionTypes.length) && (Potion.potionTypes[p_183001_1_] != null))
/*     */     {
/* 266 */       Potion potion = Potion.potionTypes[p_183001_1_];
/* 267 */       return (potion != Potion.moveSpeed) && (potion != Potion.digSpeed) && (potion != Potion.resistance) && (potion != Potion.jump) && (potion != Potion.damageBoost) && (potion != Potion.regeneration) ? 0 : p_183001_1_;
/*     */     }
/*     */     
/*     */ 
/* 271 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */   public void readFromNBT(NBTTagCompound compound)
/*     */   {
/* 277 */     super.readFromNBT(compound);
/* 278 */     this.primaryEffect = func_183001_h(compound.getInteger("Primary"));
/* 279 */     this.secondaryEffect = func_183001_h(compound.getInteger("Secondary"));
/* 280 */     this.levels = compound.getInteger("Levels");
/*     */   }
/*     */   
/*     */   public void writeToNBT(NBTTagCompound compound)
/*     */   {
/* 285 */     super.writeToNBT(compound);
/* 286 */     compound.setInteger("Primary", this.primaryEffect);
/* 287 */     compound.setInteger("Secondary", this.secondaryEffect);
/* 288 */     compound.setInteger("Levels", this.levels);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getSizeInventory()
/*     */   {
/* 296 */     return 1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ItemStack getStackInSlot(int index)
/*     */   {
/* 304 */     return index == 0 ? this.payment : null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ItemStack decrStackSize(int index, int count)
/*     */   {
/* 312 */     if ((index == 0) && (this.payment != null))
/*     */     {
/* 314 */       if (count >= this.payment.stackSize)
/*     */       {
/* 316 */         ItemStack itemstack = this.payment;
/* 317 */         this.payment = null;
/* 318 */         return itemstack;
/*     */       }
/*     */       
/*     */ 
/* 322 */       this.payment.stackSize -= count;
/* 323 */       return new ItemStack(this.payment.getItem(), count, this.payment.getMetadata());
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 328 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ItemStack removeStackFromSlot(int index)
/*     */   {
/* 337 */     if ((index == 0) && (this.payment != null))
/*     */     {
/* 339 */       ItemStack itemstack = this.payment;
/* 340 */       this.payment = null;
/* 341 */       return itemstack;
/*     */     }
/*     */     
/*     */ 
/* 345 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setInventorySlotContents(int index, ItemStack stack)
/*     */   {
/* 354 */     if (index == 0)
/*     */     {
/* 356 */       this.payment = stack;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getName()
/*     */   {
/* 365 */     return hasCustomName() ? this.customName : "container.beacon";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean hasCustomName()
/*     */   {
/* 373 */     return (this.customName != null) && (this.customName.length() > 0);
/*     */   }
/*     */   
/*     */   public void setName(String name)
/*     */   {
/* 378 */     this.customName = name;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getInventoryStackLimit()
/*     */   {
/* 386 */     return 1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isUseableByPlayer(EntityPlayer player)
/*     */   {
/* 394 */     return this.worldObj.getTileEntity(this.pos) == this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void openInventory(EntityPlayer player) {}
/*     */   
/*     */ 
/*     */ 
/*     */   public void closeInventory(EntityPlayer player) {}
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isItemValidForSlot(int index, ItemStack stack)
/*     */   {
/* 410 */     return (stack.getItem() == Items.emerald) || (stack.getItem() == Items.diamond) || (stack.getItem() == Items.gold_ingot) || (stack.getItem() == Items.iron_ingot);
/*     */   }
/*     */   
/*     */   public String getGuiID()
/*     */   {
/* 415 */     return "minecraft:beacon";
/*     */   }
/*     */   
/*     */   public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
/*     */   {
/* 420 */     return new ContainerBeacon(playerInventory, this);
/*     */   }
/*     */   
/*     */   public int getField(int id)
/*     */   {
/* 425 */     switch (id)
/*     */     {
/*     */     case 0: 
/* 428 */       return this.levels;
/*     */     
/*     */     case 1: 
/* 431 */       return this.primaryEffect;
/*     */     
/*     */     case 2: 
/* 434 */       return this.secondaryEffect;
/*     */     }
/*     */     
/* 437 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */   public void setField(int id, int value)
/*     */   {
/* 443 */     switch (id)
/*     */     {
/*     */     case 0: 
/* 446 */       this.levels = value;
/* 447 */       break;
/*     */     
/*     */     case 1: 
/* 450 */       this.primaryEffect = func_183001_h(value);
/* 451 */       break;
/*     */     
/*     */     case 2: 
/* 454 */       this.secondaryEffect = func_183001_h(value);
/*     */     }
/*     */   }
/*     */   
/*     */   public int getFieldCount()
/*     */   {
/* 460 */     return 3;
/*     */   }
/*     */   
/*     */   public void clear()
/*     */   {
/* 465 */     this.payment = null;
/*     */   }
/*     */   
/*     */   public boolean receiveClientEvent(int id, int type)
/*     */   {
/* 470 */     if (id == 1)
/*     */     {
/* 472 */       updateBeacon();
/* 473 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 477 */     return super.receiveClientEvent(id, type);
/*     */   }
/*     */   
/*     */ 
/*     */   public static class BeamSegment
/*     */   {
/*     */     private final float[] colors;
/*     */     private int height;
/*     */     
/*     */     public BeamSegment(float[] p_i45669_1_)
/*     */     {
/* 488 */       this.colors = p_i45669_1_;
/* 489 */       this.height = 1;
/*     */     }
/*     */     
/*     */     protected void incrementHeight()
/*     */     {
/* 494 */       this.height += 1;
/*     */     }
/*     */     
/*     */     public float[] getColors()
/*     */     {
/* 499 */       return this.colors;
/*     */     }
/*     */     
/*     */     public int getHeight()
/*     */     {
/* 504 */       return this.height;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\tileentity\TileEntityBeacon.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */