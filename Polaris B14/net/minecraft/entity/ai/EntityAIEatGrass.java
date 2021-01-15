/*     */ package net.minecraft.entity.ai;
/*     */ 
/*     */ import com.google.common.base.Predicate;
/*     */ import com.google.common.base.Predicates;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.block.state.pattern.BlockStateHelper;
/*     */ import net.minecraft.entity.EntityLiving;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.world.GameRules;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class EntityAIEatGrass extends EntityAIBase
/*     */ {
/*  16 */   private static final Predicate<IBlockState> field_179505_b = BlockStateHelper.forBlock(Blocks.tallgrass).where(net.minecraft.block.BlockTallGrass.TYPE, Predicates.equalTo(net.minecraft.block.BlockTallGrass.EnumType.GRASS));
/*     */   
/*     */ 
/*     */   private EntityLiving grassEaterEntity;
/*     */   
/*     */ 
/*     */   private World entityWorld;
/*     */   
/*     */   int eatingGrassTimer;
/*     */   
/*     */ 
/*     */   public EntityAIEatGrass(EntityLiving grassEaterEntityIn)
/*     */   {
/*  29 */     this.grassEaterEntity = grassEaterEntityIn;
/*  30 */     this.entityWorld = grassEaterEntityIn.worldObj;
/*  31 */     setMutexBits(7);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean shouldExecute()
/*     */   {
/*  39 */     if (this.grassEaterEntity.getRNG().nextInt(this.grassEaterEntity.isChild() ? 50 : 1000) != 0)
/*     */     {
/*  41 */       return false;
/*     */     }
/*     */     
/*     */ 
/*  45 */     BlockPos blockpos = new BlockPos(this.grassEaterEntity.posX, this.grassEaterEntity.posY, this.grassEaterEntity.posZ);
/*  46 */     return field_179505_b.apply(this.entityWorld.getBlockState(blockpos));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void startExecuting()
/*     */   {
/*  55 */     this.eatingGrassTimer = 40;
/*  56 */     this.entityWorld.setEntityState(this.grassEaterEntity, (byte)10);
/*  57 */     this.grassEaterEntity.getNavigator().clearPathEntity();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void resetTask()
/*     */   {
/*  65 */     this.eatingGrassTimer = 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean continueExecuting()
/*     */   {
/*  73 */     return this.eatingGrassTimer > 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getEatingGrassTimer()
/*     */   {
/*  81 */     return this.eatingGrassTimer;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void updateTask()
/*     */   {
/*  89 */     this.eatingGrassTimer = Math.max(0, this.eatingGrassTimer - 1);
/*     */     
/*  91 */     if (this.eatingGrassTimer == 4)
/*     */     {
/*  93 */       BlockPos blockpos = new BlockPos(this.grassEaterEntity.posX, this.grassEaterEntity.posY, this.grassEaterEntity.posZ);
/*     */       
/*  95 */       if (field_179505_b.apply(this.entityWorld.getBlockState(blockpos)))
/*     */       {
/*  97 */         if (this.entityWorld.getGameRules().getBoolean("mobGriefing"))
/*     */         {
/*  99 */           this.entityWorld.destroyBlock(blockpos, false);
/*     */         }
/*     */         
/* 102 */         this.grassEaterEntity.eatGrassBonus();
/*     */       }
/*     */       else
/*     */       {
/* 106 */         BlockPos blockpos1 = blockpos.down();
/*     */         
/* 108 */         if (this.entityWorld.getBlockState(blockpos1).getBlock() == Blocks.grass)
/*     */         {
/* 110 */           if (this.entityWorld.getGameRules().getBoolean("mobGriefing"))
/*     */           {
/* 112 */             this.entityWorld.playAuxSFX(2001, blockpos1, Block.getIdFromBlock(Blocks.grass));
/* 113 */             this.entityWorld.setBlockState(blockpos1, Blocks.dirt.getDefaultState(), 2);
/*     */           }
/*     */           
/* 116 */           this.grassEaterEntity.eatGrassBonus();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\ai\EntityAIEatGrass.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */