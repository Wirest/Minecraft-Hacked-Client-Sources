/*     */ package net.minecraft.tileentity;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockPistonMoving;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.ITickable;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class TileEntityPiston
/*     */   extends TileEntity
/*     */   implements ITickable
/*     */ {
/*     */   private IBlockState pistonState;
/*     */   private EnumFacing pistonFacing;
/*     */   private boolean extending;
/*     */   private boolean shouldHeadBeRendered;
/*     */   private float progress;
/*     */   private float lastProgress;
/*  26 */   private List<Entity> field_174933_k = Lists.newArrayList();
/*     */   
/*     */ 
/*     */   public TileEntityPiston() {}
/*     */   
/*     */ 
/*     */   public TileEntityPiston(IBlockState pistonStateIn, EnumFacing pistonFacingIn, boolean extendingIn, boolean shouldHeadBeRenderedIn)
/*     */   {
/*  34 */     this.pistonState = pistonStateIn;
/*  35 */     this.pistonFacing = pistonFacingIn;
/*  36 */     this.extending = extendingIn;
/*  37 */     this.shouldHeadBeRendered = shouldHeadBeRenderedIn;
/*     */   }
/*     */   
/*     */   public IBlockState getPistonState()
/*     */   {
/*  42 */     return this.pistonState;
/*     */   }
/*     */   
/*     */   public int getBlockMetadata()
/*     */   {
/*  47 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isExtending()
/*     */   {
/*  55 */     return this.extending;
/*     */   }
/*     */   
/*     */   public EnumFacing getFacing()
/*     */   {
/*  60 */     return this.pistonFacing;
/*     */   }
/*     */   
/*     */   public boolean shouldPistonHeadBeRendered()
/*     */   {
/*  65 */     return this.shouldHeadBeRendered;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public float getProgress(float ticks)
/*     */   {
/*  74 */     if (ticks > 1.0F)
/*     */     {
/*  76 */       ticks = 1.0F;
/*     */     }
/*     */     
/*  79 */     return this.lastProgress + (this.progress - this.lastProgress) * ticks;
/*     */   }
/*     */   
/*     */   public float getOffsetX(float ticks)
/*     */   {
/*  84 */     return this.extending ? (getProgress(ticks) - 1.0F) * this.pistonFacing.getFrontOffsetX() : (1.0F - getProgress(ticks)) * this.pistonFacing.getFrontOffsetX();
/*     */   }
/*     */   
/*     */   public float getOffsetY(float ticks)
/*     */   {
/*  89 */     return this.extending ? (getProgress(ticks) - 1.0F) * this.pistonFacing.getFrontOffsetY() : (1.0F - getProgress(ticks)) * this.pistonFacing.getFrontOffsetY();
/*     */   }
/*     */   
/*     */   public float getOffsetZ(float ticks)
/*     */   {
/*  94 */     return this.extending ? (getProgress(ticks) - 1.0F) * this.pistonFacing.getFrontOffsetZ() : (1.0F - getProgress(ticks)) * this.pistonFacing.getFrontOffsetZ();
/*     */   }
/*     */   
/*     */   private void launchWithSlimeBlock(float p_145863_1_, float p_145863_2_)
/*     */   {
/*  99 */     if (this.extending)
/*     */     {
/* 101 */       p_145863_1_ = 1.0F - p_145863_1_;
/*     */     }
/*     */     else
/*     */     {
/* 105 */       p_145863_1_ -= 1.0F;
/*     */     }
/*     */     
/* 108 */     AxisAlignedBB axisalignedbb = Blocks.piston_extension.getBoundingBox(this.worldObj, this.pos, this.pistonState, p_145863_1_, this.pistonFacing);
/*     */     
/* 110 */     if (axisalignedbb != null)
/*     */     {
/* 112 */       List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(null, axisalignedbb);
/*     */       
/* 114 */       if (!list.isEmpty())
/*     */       {
/* 116 */         this.field_174933_k.addAll(list);
/*     */         
/* 118 */         for (Entity entity : this.field_174933_k)
/*     */         {
/* 120 */           if ((this.pistonState.getBlock() == Blocks.slime_block) && (this.extending)) {}
/*     */           
/* 122 */           switch (this.pistonFacing.getAxis())
/*     */           {
/*     */           case X: 
/* 125 */             entity.motionX = this.pistonFacing.getFrontOffsetX();
/* 126 */             break;
/*     */           
/*     */           case Y: 
/* 129 */             entity.motionY = this.pistonFacing.getFrontOffsetY();
/* 130 */             break;
/*     */           
/*     */           case Z: 
/* 133 */             entity.motionZ = this.pistonFacing.getFrontOffsetZ();
/*     */           
/*     */ 
/*     */ 
/*     */           default: 
/* 138 */             continue;entity.moveEntity(p_145863_2_ * this.pistonFacing.getFrontOffsetX(), p_145863_2_ * this.pistonFacing.getFrontOffsetY(), p_145863_2_ * this.pistonFacing.getFrontOffsetZ());
/*     */           }
/*     */           
/*     */         }
/* 142 */         this.field_174933_k.clear();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void clearPistonTileEntity()
/*     */   {
/* 152 */     if ((this.lastProgress < 1.0F) && (this.worldObj != null))
/*     */     {
/* 154 */       this.lastProgress = (this.progress = 1.0F);
/* 155 */       this.worldObj.removeTileEntity(this.pos);
/* 156 */       invalidate();
/*     */       
/* 158 */       if (this.worldObj.getBlockState(this.pos).getBlock() == Blocks.piston_extension)
/*     */       {
/* 160 */         this.worldObj.setBlockState(this.pos, this.pistonState, 3);
/* 161 */         this.worldObj.notifyBlockOfStateChange(this.pos, this.pistonState.getBlock());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void update()
/*     */   {
/* 171 */     this.lastProgress = this.progress;
/*     */     
/* 173 */     if (this.lastProgress >= 1.0F)
/*     */     {
/* 175 */       launchWithSlimeBlock(1.0F, 0.25F);
/* 176 */       this.worldObj.removeTileEntity(this.pos);
/* 177 */       invalidate();
/*     */       
/* 179 */       if (this.worldObj.getBlockState(this.pos).getBlock() == Blocks.piston_extension)
/*     */       {
/* 181 */         this.worldObj.setBlockState(this.pos, this.pistonState, 3);
/* 182 */         this.worldObj.notifyBlockOfStateChange(this.pos, this.pistonState.getBlock());
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 187 */       this.progress += 0.5F;
/*     */       
/* 189 */       if (this.progress >= 1.0F)
/*     */       {
/* 191 */         this.progress = 1.0F;
/*     */       }
/*     */       
/* 194 */       if (this.extending)
/*     */       {
/* 196 */         launchWithSlimeBlock(this.progress, this.progress - this.lastProgress + 0.0625F);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void readFromNBT(NBTTagCompound compound)
/*     */   {
/* 203 */     super.readFromNBT(compound);
/* 204 */     this.pistonState = Block.getBlockById(compound.getInteger("blockId")).getStateFromMeta(compound.getInteger("blockData"));
/* 205 */     this.pistonFacing = EnumFacing.getFront(compound.getInteger("facing"));
/* 206 */     this.lastProgress = (this.progress = compound.getFloat("progress"));
/* 207 */     this.extending = compound.getBoolean("extending");
/*     */   }
/*     */   
/*     */   public void writeToNBT(NBTTagCompound compound)
/*     */   {
/* 212 */     super.writeToNBT(compound);
/* 213 */     compound.setInteger("blockId", Block.getIdFromBlock(this.pistonState.getBlock()));
/* 214 */     compound.setInteger("blockData", this.pistonState.getBlock().getMetaFromState(this.pistonState));
/* 215 */     compound.setInteger("facing", this.pistonFacing.getIndex());
/* 216 */     compound.setFloat("progress", this.lastProgress);
/* 217 */     compound.setBoolean("extending", this.extending);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\tileentity\TileEntityPiston.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */