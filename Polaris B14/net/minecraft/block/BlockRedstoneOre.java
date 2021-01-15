/*     */ package net.minecraft.block;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class BlockRedstoneOre extends Block
/*     */ {
/*     */   private final boolean isOn;
/*     */   
/*     */   public BlockRedstoneOre(boolean isOn)
/*     */   {
/*  23 */     super(Material.rock);
/*     */     
/*  25 */     if (isOn)
/*     */     {
/*  27 */       setTickRandomly(true);
/*     */     }
/*     */     
/*  30 */     this.isOn = isOn;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int tickRate(World worldIn)
/*     */   {
/*  38 */     return 30;
/*     */   }
/*     */   
/*     */   public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn)
/*     */   {
/*  43 */     activate(worldIn, pos);
/*  44 */     super.onBlockClicked(worldIn, pos, playerIn);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, Entity entityIn)
/*     */   {
/*  52 */     activate(worldIn, pos);
/*  53 */     super.onEntityCollidedWithBlock(worldIn, pos, entityIn);
/*     */   }
/*     */   
/*     */   public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
/*     */   {
/*  58 */     activate(worldIn, pos);
/*  59 */     return super.onBlockActivated(worldIn, pos, state, playerIn, side, hitX, hitY, hitZ);
/*     */   }
/*     */   
/*     */   private void activate(World worldIn, BlockPos pos)
/*     */   {
/*  64 */     spawnParticles(worldIn, pos);
/*     */     
/*  66 */     if (this == Blocks.redstone_ore)
/*     */     {
/*  68 */       worldIn.setBlockState(pos, Blocks.lit_redstone_ore.getDefaultState());
/*     */     }
/*     */   }
/*     */   
/*     */   public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
/*     */   {
/*  74 */     if (this == Blocks.lit_redstone_ore)
/*     */     {
/*  76 */       worldIn.setBlockState(pos, Blocks.redstone_ore.getDefaultState());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Item getItemDropped(IBlockState state, Random rand, int fortune)
/*     */   {
/*  85 */     return Items.redstone;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int quantityDroppedWithBonus(int fortune, Random random)
/*     */   {
/*  93 */     return quantityDropped(random) + random.nextInt(fortune + 1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int quantityDropped(Random random)
/*     */   {
/* 101 */     return 4 + random.nextInt(2);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune)
/*     */   {
/* 109 */     super.dropBlockAsItemWithChance(worldIn, pos, state, chance, fortune);
/*     */     
/* 111 */     if (getItemDropped(state, worldIn.rand, fortune) != Item.getItemFromBlock(this))
/*     */     {
/* 113 */       int i = 1 + worldIn.rand.nextInt(5);
/* 114 */       dropXpOnBlockBreak(worldIn, pos, i);
/*     */     }
/*     */   }
/*     */   
/*     */   public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
/*     */   {
/* 120 */     if (this.isOn)
/*     */     {
/* 122 */       spawnParticles(worldIn, pos);
/*     */     }
/*     */   }
/*     */   
/*     */   private void spawnParticles(World worldIn, BlockPos pos)
/*     */   {
/* 128 */     Random random = worldIn.rand;
/* 129 */     double d0 = 0.0625D;
/*     */     
/* 131 */     for (int i = 0; i < 6; i++)
/*     */     {
/* 133 */       double d1 = pos.getX() + random.nextFloat();
/* 134 */       double d2 = pos.getY() + random.nextFloat();
/* 135 */       double d3 = pos.getZ() + random.nextFloat();
/*     */       
/* 137 */       if ((i == 0) && (!worldIn.getBlockState(pos.up()).getBlock().isOpaqueCube()))
/*     */       {
/* 139 */         d2 = pos.getY() + d0 + 1.0D;
/*     */       }
/*     */       
/* 142 */       if ((i == 1) && (!worldIn.getBlockState(pos.down()).getBlock().isOpaqueCube()))
/*     */       {
/* 144 */         d2 = pos.getY() - d0;
/*     */       }
/*     */       
/* 147 */       if ((i == 2) && (!worldIn.getBlockState(pos.south()).getBlock().isOpaqueCube()))
/*     */       {
/* 149 */         d3 = pos.getZ() + d0 + 1.0D;
/*     */       }
/*     */       
/* 152 */       if ((i == 3) && (!worldIn.getBlockState(pos.north()).getBlock().isOpaqueCube()))
/*     */       {
/* 154 */         d3 = pos.getZ() - d0;
/*     */       }
/*     */       
/* 157 */       if ((i == 4) && (!worldIn.getBlockState(pos.east()).getBlock().isOpaqueCube()))
/*     */       {
/* 159 */         d1 = pos.getX() + d0 + 1.0D;
/*     */       }
/*     */       
/* 162 */       if ((i == 5) && (!worldIn.getBlockState(pos.west()).getBlock().isOpaqueCube()))
/*     */       {
/* 164 */         d1 = pos.getX() - d0;
/*     */       }
/*     */       
/* 167 */       if ((d1 < pos.getX()) || (d1 > pos.getX() + 1) || (d2 < 0.0D) || (d2 > pos.getY() + 1) || (d3 < pos.getZ()) || (d3 > pos.getZ() + 1))
/*     */       {
/* 169 */         worldIn.spawnParticle(EnumParticleTypes.REDSTONE, d1, d2, d3, 0.0D, 0.0D, 0.0D, new int[0]);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected ItemStack createStackedBlock(IBlockState state)
/*     */   {
/* 176 */     return new ItemStack(Blocks.redstone_ore);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\BlockRedstoneOre.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */