/*     */ package net.minecraft.entity.ai;
/*     */ 
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockBed;
/*     */ import net.minecraft.block.BlockBed.EnumPartType;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.passive.EntityOcelot;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.tileentity.TileEntityChest;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class EntityAIOcelotSit extends EntityAIMoveToBlock
/*     */ {
/*     */   private final EntityOcelot field_151493_a;
/*     */   
/*     */   public EntityAIOcelotSit(EntityOcelot p_i45315_1_, double p_i45315_2_)
/*     */   {
/*  19 */     super(p_i45315_1_, p_i45315_2_, 8);
/*  20 */     this.field_151493_a = p_i45315_1_;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean shouldExecute()
/*     */   {
/*  28 */     return (this.field_151493_a.isTamed()) && (!this.field_151493_a.isSitting()) && (super.shouldExecute());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean continueExecuting()
/*     */   {
/*  36 */     return super.continueExecuting();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void startExecuting()
/*     */   {
/*  44 */     super.startExecuting();
/*  45 */     this.field_151493_a.getAISit().setSitting(false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void resetTask()
/*     */   {
/*  53 */     super.resetTask();
/*  54 */     this.field_151493_a.setSitting(false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void updateTask()
/*     */   {
/*  62 */     super.updateTask();
/*  63 */     this.field_151493_a.getAISit().setSitting(false);
/*     */     
/*  65 */     if (!getIsAboveDestination())
/*     */     {
/*  67 */       this.field_151493_a.setSitting(false);
/*     */     }
/*  69 */     else if (!this.field_151493_a.isSitting())
/*     */     {
/*  71 */       this.field_151493_a.setSitting(true);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean shouldMoveTo(World worldIn, BlockPos pos)
/*     */   {
/*  80 */     if (!worldIn.isAirBlock(pos.up()))
/*     */     {
/*  82 */       return false;
/*     */     }
/*     */     
/*     */ 
/*  86 */     IBlockState iblockstate = worldIn.getBlockState(pos);
/*  87 */     Block block = iblockstate.getBlock();
/*     */     
/*  89 */     if (block == Blocks.chest)
/*     */     {
/*  91 */       net.minecraft.tileentity.TileEntity tileentity = worldIn.getTileEntity(pos);
/*     */       
/*  93 */       if (((tileentity instanceof TileEntityChest)) && (((TileEntityChest)tileentity).numPlayersUsing < 1))
/*     */       {
/*  95 */         return true;
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 100 */       if (block == Blocks.lit_furnace)
/*     */       {
/* 102 */         return true;
/*     */       }
/*     */       
/* 105 */       if ((block == Blocks.bed) && (iblockstate.getValue(BlockBed.PART) != BlockBed.EnumPartType.HEAD))
/*     */       {
/* 107 */         return true;
/*     */       }
/*     */     }
/*     */     
/* 111 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\ai\EntityAIOcelotSit.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */