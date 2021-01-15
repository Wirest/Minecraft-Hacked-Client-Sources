/*     */ package net.minecraft.entity.item;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.InventoryPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.inventory.Container;
/*     */ import net.minecraft.inventory.ContainerHopper;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.tileentity.IHopper;
/*     */ import net.minecraft.tileentity.TileEntityHopper;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.util.EntitySelectors;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class EntityMinecartHopper extends EntityMinecartContainer implements IHopper
/*     */ {
/*  22 */   private boolean isBlocked = true;
/*  23 */   private int transferTicker = -1;
/*  24 */   private BlockPos field_174900_c = BlockPos.ORIGIN;
/*     */   
/*     */   public EntityMinecartHopper(World worldIn)
/*     */   {
/*  28 */     super(worldIn);
/*     */   }
/*     */   
/*     */   public EntityMinecartHopper(World worldIn, double p_i1721_2_, double p_i1721_4_, double p_i1721_6_)
/*     */   {
/*  33 */     super(worldIn, p_i1721_2_, p_i1721_4_, p_i1721_6_);
/*     */   }
/*     */   
/*     */   public EntityMinecart.EnumMinecartType getMinecartType()
/*     */   {
/*  38 */     return EntityMinecart.EnumMinecartType.HOPPER;
/*     */   }
/*     */   
/*     */   public IBlockState getDefaultDisplayTile()
/*     */   {
/*  43 */     return Blocks.hopper.getDefaultState();
/*     */   }
/*     */   
/*     */   public int getDefaultDisplayTileOffset()
/*     */   {
/*  48 */     return 1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getSizeInventory()
/*     */   {
/*  56 */     return 5;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean interactFirst(EntityPlayer playerIn)
/*     */   {
/*  64 */     if (!this.worldObj.isRemote)
/*     */     {
/*  66 */       playerIn.displayGUIChest(this);
/*     */     }
/*     */     
/*  69 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onActivatorRailPass(int x, int y, int z, boolean receivingPower)
/*     */   {
/*  77 */     boolean flag = !receivingPower;
/*     */     
/*  79 */     if (flag != getBlocked())
/*     */     {
/*  81 */       setBlocked(flag);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean getBlocked()
/*     */   {
/*  90 */     return this.isBlocked;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setBlocked(boolean p_96110_1_)
/*     */   {
/*  98 */     this.isBlocked = p_96110_1_;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public World getWorld()
/*     */   {
/* 106 */     return this.worldObj;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public double getXPos()
/*     */   {
/* 114 */     return this.posX;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public double getYPos()
/*     */   {
/* 122 */     return this.posY + 0.5D;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public double getZPos()
/*     */   {
/* 130 */     return this.posZ;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onUpdate()
/*     */   {
/* 138 */     super.onUpdate();
/*     */     
/* 140 */     if ((!this.worldObj.isRemote) && (isEntityAlive()) && (getBlocked()))
/*     */     {
/* 142 */       BlockPos blockpos = new BlockPos(this);
/*     */       
/* 144 */       if (blockpos.equals(this.field_174900_c))
/*     */       {
/* 146 */         this.transferTicker -= 1;
/*     */       }
/*     */       else
/*     */       {
/* 150 */         setTransferTicker(0);
/*     */       }
/*     */       
/* 153 */       if (!canTransfer())
/*     */       {
/* 155 */         setTransferTicker(0);
/*     */         
/* 157 */         if (func_96112_aD())
/*     */         {
/* 159 */           setTransferTicker(4);
/* 160 */           markDirty();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean func_96112_aD()
/*     */   {
/* 168 */     if (TileEntityHopper.captureDroppedItems(this))
/*     */     {
/* 170 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 174 */     List<EntityItem> list = this.worldObj.getEntitiesWithinAABB(EntityItem.class, getEntityBoundingBox().expand(0.25D, 0.0D, 0.25D), EntitySelectors.selectAnything);
/*     */     
/* 176 */     if (list.size() > 0)
/*     */     {
/* 178 */       TileEntityHopper.putDropInInventoryAllSlots(this, (EntityItem)list.get(0));
/*     */     }
/*     */     
/* 181 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public void killMinecart(DamageSource p_94095_1_)
/*     */   {
/* 187 */     super.killMinecart(p_94095_1_);
/*     */     
/* 189 */     if (this.worldObj.getGameRules().getBoolean("doEntityDrops"))
/*     */     {
/* 191 */       dropItemWithOffset(Item.getItemFromBlock(Blocks.hopper), 1, 0.0F);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void writeEntityToNBT(NBTTagCompound tagCompound)
/*     */   {
/* 200 */     super.writeEntityToNBT(tagCompound);
/* 201 */     tagCompound.setInteger("TransferCooldown", this.transferTicker);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void readEntityFromNBT(NBTTagCompound tagCompund)
/*     */   {
/* 209 */     super.readEntityFromNBT(tagCompund);
/* 210 */     this.transferTicker = tagCompund.getInteger("TransferCooldown");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setTransferTicker(int p_98042_1_)
/*     */   {
/* 218 */     this.transferTicker = p_98042_1_;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean canTransfer()
/*     */   {
/* 226 */     return this.transferTicker > 0;
/*     */   }
/*     */   
/*     */   public String getGuiID()
/*     */   {
/* 231 */     return "minecraft:hopper";
/*     */   }
/*     */   
/*     */   public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
/*     */   {
/* 236 */     return new ContainerHopper(playerInventory, this, playerIn);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\item\EntityMinecartHopper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */