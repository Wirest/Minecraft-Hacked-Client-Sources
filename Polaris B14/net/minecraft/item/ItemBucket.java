/*     */ package net.minecraft.item;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockLiquid;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.creativetab.CreativeTabs;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.PlayerCapabilities;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.util.MovingObjectPosition;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class ItemBucket extends Item
/*     */ {
/*     */   private Block isFull;
/*     */   
/*     */   public ItemBucket(Block containedBlock)
/*     */   {
/*  24 */     this.maxStackSize = 1;
/*  25 */     this.isFull = containedBlock;
/*  26 */     setCreativeTab(CreativeTabs.tabMisc);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn)
/*     */   {
/*  34 */     boolean flag = this.isFull == Blocks.air;
/*  35 */     MovingObjectPosition movingobjectposition = getMovingObjectPositionFromPlayer(worldIn, playerIn, flag);
/*     */     
/*  37 */     if (movingobjectposition == null)
/*     */     {
/*  39 */       return itemStackIn;
/*     */     }
/*     */     
/*     */ 
/*  43 */     if (movingobjectposition.typeOfHit == net.minecraft.util.MovingObjectPosition.MovingObjectType.BLOCK)
/*     */     {
/*  45 */       BlockPos blockpos = movingobjectposition.getBlockPos();
/*     */       
/*  47 */       if (!worldIn.isBlockModifiable(playerIn, blockpos))
/*     */       {
/*  49 */         return itemStackIn;
/*     */       }
/*     */       
/*  52 */       if (flag)
/*     */       {
/*  54 */         if (!playerIn.canPlayerEdit(blockpos.offset(movingobjectposition.sideHit), movingobjectposition.sideHit, itemStackIn))
/*     */         {
/*  56 */           return itemStackIn;
/*     */         }
/*     */         
/*  59 */         IBlockState iblockstate = worldIn.getBlockState(blockpos);
/*  60 */         Material material = iblockstate.getBlock().getMaterial();
/*     */         
/*  62 */         if ((material == Material.water) && (((Integer)iblockstate.getValue(BlockLiquid.LEVEL)).intValue() == 0))
/*     */         {
/*  64 */           worldIn.setBlockToAir(blockpos);
/*  65 */           playerIn.triggerAchievement(net.minecraft.stats.StatList.objectUseStats[Item.getIdFromItem(this)]);
/*  66 */           return fillBucket(itemStackIn, playerIn, Items.water_bucket);
/*     */         }
/*     */         
/*  69 */         if ((material == Material.lava) && (((Integer)iblockstate.getValue(BlockLiquid.LEVEL)).intValue() == 0))
/*     */         {
/*  71 */           worldIn.setBlockToAir(blockpos);
/*  72 */           playerIn.triggerAchievement(net.minecraft.stats.StatList.objectUseStats[Item.getIdFromItem(this)]);
/*  73 */           return fillBucket(itemStackIn, playerIn, Items.lava_bucket);
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/*  78 */         if (this.isFull == Blocks.air)
/*     */         {
/*  80 */           return new ItemStack(Items.bucket);
/*     */         }
/*     */         
/*  83 */         BlockPos blockpos1 = blockpos.offset(movingobjectposition.sideHit);
/*     */         
/*  85 */         if (!playerIn.canPlayerEdit(blockpos1, movingobjectposition.sideHit, itemStackIn))
/*     */         {
/*  87 */           return itemStackIn;
/*     */         }
/*     */         
/*  90 */         if ((tryPlaceContainedLiquid(worldIn, blockpos1)) && (!playerIn.capabilities.isCreativeMode))
/*     */         {
/*  92 */           playerIn.triggerAchievement(net.minecraft.stats.StatList.objectUseStats[Item.getIdFromItem(this)]);
/*  93 */           return new ItemStack(Items.bucket);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*  98 */     return itemStackIn;
/*     */   }
/*     */   
/*     */ 
/*     */   private ItemStack fillBucket(ItemStack emptyBuckets, EntityPlayer player, Item fullBucket)
/*     */   {
/* 104 */     if (player.capabilities.isCreativeMode)
/*     */     {
/* 106 */       return emptyBuckets;
/*     */     }
/* 108 */     if (--emptyBuckets.stackSize <= 0)
/*     */     {
/* 110 */       return new ItemStack(fullBucket);
/*     */     }
/*     */     
/*     */ 
/* 114 */     if (!player.inventory.addItemStackToInventory(new ItemStack(fullBucket)))
/*     */     {
/* 116 */       player.dropPlayerItemWithRandomChoice(new ItemStack(fullBucket, 1, 0), false);
/*     */     }
/*     */     
/* 119 */     return emptyBuckets;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean tryPlaceContainedLiquid(World worldIn, BlockPos pos)
/*     */   {
/* 125 */     if (this.isFull == Blocks.air)
/*     */     {
/* 127 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 131 */     Material material = worldIn.getBlockState(pos).getBlock().getMaterial();
/* 132 */     boolean flag = !material.isSolid();
/*     */     
/* 134 */     if ((!worldIn.isAirBlock(pos)) && (!flag))
/*     */     {
/* 136 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 140 */     if ((worldIn.provider.doesWaterVaporize()) && (this.isFull == Blocks.flowing_water))
/*     */     {
/* 142 */       int i = pos.getX();
/* 143 */       int j = pos.getY();
/* 144 */       int k = pos.getZ();
/* 145 */       worldIn.playSoundEffect(i + 0.5F, j + 0.5F, k + 0.5F, "random.fizz", 0.5F, 2.6F + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.8F);
/*     */       
/* 147 */       for (int l = 0; l < 8; l++)
/*     */       {
/* 149 */         worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, i + Math.random(), j + Math.random(), k + Math.random(), 0.0D, 0.0D, 0.0D, new int[0]);
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 154 */       if ((!worldIn.isRemote) && (flag) && (!material.isLiquid()))
/*     */       {
/* 156 */         worldIn.destroyBlock(pos, true);
/*     */       }
/*     */       
/* 159 */       worldIn.setBlockState(pos, this.isFull.getDefaultState(), 3);
/*     */     }
/*     */     
/* 162 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\item\ItemBucket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */