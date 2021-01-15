/*     */ package net.minecraft.entity.ai;
/*     */ 
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockCrops;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.passive.EntityVillager;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.inventory.InventoryBasic;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.world.GameRules;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class EntityAIHarvestFarmland extends EntityAIMoveToBlock
/*     */ {
/*     */   private final EntityVillager theVillager;
/*     */   private boolean hasFarmItem;
/*     */   private boolean field_179503_e;
/*     */   private int field_179501_f;
/*     */   
/*     */   public EntityAIHarvestFarmland(EntityVillager theVillagerIn, double speedIn)
/*     */   {
/*  24 */     super(theVillagerIn, speedIn, 16);
/*  25 */     this.theVillager = theVillagerIn;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean shouldExecute()
/*     */   {
/*  33 */     if (this.runDelay <= 0)
/*     */     {
/*  35 */       if (!this.theVillager.worldObj.getGameRules().getBoolean("mobGriefing"))
/*     */       {
/*  37 */         return false;
/*     */       }
/*     */       
/*  40 */       this.field_179501_f = -1;
/*  41 */       this.hasFarmItem = this.theVillager.isFarmItemInInventory();
/*  42 */       this.field_179503_e = this.theVillager.func_175557_cr();
/*     */     }
/*     */     
/*  45 */     return super.shouldExecute();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean continueExecuting()
/*     */   {
/*  53 */     return (this.field_179501_f >= 0) && (super.continueExecuting());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void startExecuting()
/*     */   {
/*  61 */     super.startExecuting();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void resetTask()
/*     */   {
/*  69 */     super.resetTask();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void updateTask()
/*     */   {
/*  77 */     super.updateTask();
/*  78 */     this.theVillager.getLookHelper().setLookPosition(this.destinationBlock.getX() + 0.5D, this.destinationBlock.getY() + 1, this.destinationBlock.getZ() + 0.5D, 10.0F, this.theVillager.getVerticalFaceSpeed());
/*     */     
/*  80 */     if (getIsAboveDestination())
/*     */     {
/*  82 */       World world = this.theVillager.worldObj;
/*  83 */       BlockPos blockpos = this.destinationBlock.up();
/*  84 */       IBlockState iblockstate = world.getBlockState(blockpos);
/*  85 */       Block block = iblockstate.getBlock();
/*     */       
/*  87 */       if ((this.field_179501_f == 0) && ((block instanceof BlockCrops)) && (((Integer)iblockstate.getValue(BlockCrops.AGE)).intValue() == 7))
/*     */       {
/*  89 */         world.destroyBlock(blockpos, true);
/*     */       }
/*  91 */       else if ((this.field_179501_f == 1) && (block == Blocks.air))
/*     */       {
/*  93 */         InventoryBasic inventorybasic = this.theVillager.getVillagerInventory();
/*     */         
/*  95 */         for (int i = 0; i < inventorybasic.getSizeInventory(); i++)
/*     */         {
/*  97 */           ItemStack itemstack = inventorybasic.getStackInSlot(i);
/*  98 */           boolean flag = false;
/*     */           
/* 100 */           if (itemstack != null)
/*     */           {
/* 102 */             if (itemstack.getItem() == Items.wheat_seeds)
/*     */             {
/* 104 */               world.setBlockState(blockpos, Blocks.wheat.getDefaultState(), 3);
/* 105 */               flag = true;
/*     */             }
/* 107 */             else if (itemstack.getItem() == Items.potato)
/*     */             {
/* 109 */               world.setBlockState(blockpos, Blocks.potatoes.getDefaultState(), 3);
/* 110 */               flag = true;
/*     */             }
/* 112 */             else if (itemstack.getItem() == Items.carrot)
/*     */             {
/* 114 */               world.setBlockState(blockpos, Blocks.carrots.getDefaultState(), 3);
/* 115 */               flag = true;
/*     */             }
/*     */           }
/*     */           
/* 119 */           if (flag)
/*     */           {
/* 121 */             itemstack.stackSize -= 1;
/*     */             
/* 123 */             if (itemstack.stackSize > 0)
/*     */               break;
/* 125 */             inventorybasic.setInventorySlotContents(i, null);
/*     */             
/*     */ 
/* 128 */             break;
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 133 */       this.field_179501_f = -1;
/* 134 */       this.runDelay = 10;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean shouldMoveTo(World worldIn, BlockPos pos)
/*     */   {
/* 143 */     Block block = worldIn.getBlockState(pos).getBlock();
/*     */     
/* 145 */     if (block == Blocks.farmland)
/*     */     {
/* 147 */       pos = pos.up();
/* 148 */       IBlockState iblockstate = worldIn.getBlockState(pos);
/* 149 */       block = iblockstate.getBlock();
/*     */       
/* 151 */       if (((block instanceof BlockCrops)) && (((Integer)iblockstate.getValue(BlockCrops.AGE)).intValue() == 7) && (this.field_179503_e) && ((this.field_179501_f == 0) || (this.field_179501_f < 0)))
/*     */       {
/* 153 */         this.field_179501_f = 0;
/* 154 */         return true;
/*     */       }
/*     */       
/* 157 */       if ((block == Blocks.air) && (this.hasFarmItem) && ((this.field_179501_f == 1) || (this.field_179501_f < 0)))
/*     */       {
/* 159 */         this.field_179501_f = 1;
/* 160 */         return true;
/*     */       }
/*     */     }
/*     */     
/* 164 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\ai\EntityAIHarvestFarmland.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */