/*     */ package net.minecraft.block;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.material.MapColor;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.item.EntityFallingBlock;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class BlockDragonEgg extends Block
/*     */ {
/*     */   public BlockDragonEgg()
/*     */   {
/*  20 */     super(Material.dragonEgg, MapColor.blackColor);
/*  21 */     setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 1.0F, 0.9375F);
/*     */   }
/*     */   
/*     */   public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
/*     */   {
/*  26 */     worldIn.scheduleUpdate(pos, this, tickRate(worldIn));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
/*     */   {
/*  34 */     worldIn.scheduleUpdate(pos, this, tickRate(worldIn));
/*     */   }
/*     */   
/*     */   public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
/*     */   {
/*  39 */     checkFall(worldIn, pos);
/*     */   }
/*     */   
/*     */   private void checkFall(World worldIn, BlockPos pos)
/*     */   {
/*  44 */     if ((BlockFalling.canFallInto(worldIn, pos.down())) && (pos.getY() >= 0))
/*     */     {
/*  46 */       int i = 32;
/*     */       
/*  48 */       if ((!BlockFalling.fallInstantly) && (worldIn.isAreaLoaded(pos.add(-i, -i, -i), pos.add(i, i, i))))
/*     */       {
/*  50 */         worldIn.spawnEntityInWorld(new EntityFallingBlock(worldIn, pos.getX() + 0.5F, pos.getY(), pos.getZ() + 0.5F, getDefaultState()));
/*     */       }
/*     */       else
/*     */       {
/*  54 */         worldIn.setBlockToAir(pos);
/*     */         
/*     */ 
/*  57 */         for (BlockPos blockpos = pos; (BlockFalling.canFallInto(worldIn, blockpos)) && (blockpos.getY() > 0); blockpos = blockpos.down()) {}
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*  62 */         if (blockpos.getY() > 0)
/*     */         {
/*  64 */           worldIn.setBlockState(blockpos, getDefaultState(), 2);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
/*     */   {
/*  72 */     teleport(worldIn, pos);
/*  73 */     return true;
/*     */   }
/*     */   
/*     */   public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn)
/*     */   {
/*  78 */     teleport(worldIn, pos);
/*     */   }
/*     */   
/*     */   private void teleport(World worldIn, BlockPos pos)
/*     */   {
/*  83 */     IBlockState iblockstate = worldIn.getBlockState(pos);
/*     */     
/*  85 */     if (iblockstate.getBlock() == this)
/*     */     {
/*  87 */       for (int i = 0; i < 1000; i++)
/*     */       {
/*  89 */         BlockPos blockpos = pos.add(worldIn.rand.nextInt(16) - worldIn.rand.nextInt(16), worldIn.rand.nextInt(8) - worldIn.rand.nextInt(8), worldIn.rand.nextInt(16) - worldIn.rand.nextInt(16));
/*     */         
/*  91 */         if (worldIn.getBlockState(blockpos).getBlock().blockMaterial == Material.air)
/*     */         {
/*  93 */           if (worldIn.isRemote)
/*     */           {
/*  95 */             for (int j = 0; j < 128; j++)
/*     */             {
/*  97 */               double d0 = worldIn.rand.nextDouble();
/*  98 */               float f = (worldIn.rand.nextFloat() - 0.5F) * 0.2F;
/*  99 */               float f1 = (worldIn.rand.nextFloat() - 0.5F) * 0.2F;
/* 100 */               float f2 = (worldIn.rand.nextFloat() - 0.5F) * 0.2F;
/* 101 */               double d1 = blockpos.getX() + (pos.getX() - blockpos.getX()) * d0 + (worldIn.rand.nextDouble() - 0.5D) * 1.0D + 0.5D;
/* 102 */               double d2 = blockpos.getY() + (pos.getY() - blockpos.getY()) * d0 + worldIn.rand.nextDouble() * 1.0D - 0.5D;
/* 103 */               double d3 = blockpos.getZ() + (pos.getZ() - blockpos.getZ()) * d0 + (worldIn.rand.nextDouble() - 0.5D) * 1.0D + 0.5D;
/* 104 */               worldIn.spawnParticle(EnumParticleTypes.PORTAL, d1, d2, d3, f, f1, f2, new int[0]);
/*     */             }
/*     */           }
/*     */           else
/*     */           {
/* 109 */             worldIn.setBlockState(blockpos, iblockstate, 2);
/* 110 */             worldIn.setBlockToAir(pos);
/*     */           }
/*     */           
/* 113 */           return;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int tickRate(World worldIn)
/*     */   {
/* 124 */     return 5;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isOpaqueCube()
/*     */   {
/* 132 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isFullCube()
/*     */   {
/* 137 */     return false;
/*     */   }
/*     */   
/*     */   public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side)
/*     */   {
/* 142 */     return true;
/*     */   }
/*     */   
/*     */   public Item getItem(World worldIn, BlockPos pos)
/*     */   {
/* 147 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\BlockDragonEgg.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */