/*     */ package net.minecraft.block;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumFacing.Axis;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class BlockRedstoneTorch extends BlockTorch
/*     */ {
/*  20 */   private static Map<World, List<Toggle>> toggles = ;
/*     */   private final boolean isOn;
/*     */   
/*     */   private boolean isBurnedOut(World worldIn, BlockPos pos, boolean turnOff)
/*     */   {
/*  25 */     if (!toggles.containsKey(worldIn))
/*     */     {
/*  27 */       toggles.put(worldIn, Lists.newArrayList());
/*     */     }
/*     */     
/*  30 */     List<Toggle> list = (List)toggles.get(worldIn);
/*     */     
/*  32 */     if (turnOff)
/*     */     {
/*  34 */       list.add(new Toggle(pos, worldIn.getTotalWorldTime()));
/*     */     }
/*     */     
/*  37 */     int i = 0;
/*     */     
/*  39 */     for (int j = 0; j < list.size(); j++)
/*     */     {
/*  41 */       Toggle blockredstonetorch$toggle = (Toggle)list.get(j);
/*     */       
/*  43 */       if (blockredstonetorch$toggle.pos.equals(pos))
/*     */       {
/*  45 */         i++;
/*     */         
/*  47 */         if (i >= 8)
/*     */         {
/*  49 */           return true;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*  54 */     return false;
/*     */   }
/*     */   
/*     */   protected BlockRedstoneTorch(boolean isOn)
/*     */   {
/*  59 */     this.isOn = isOn;
/*  60 */     setTickRandomly(true);
/*  61 */     setCreativeTab(null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int tickRate(World worldIn)
/*     */   {
/*  69 */     return 2;
/*     */   }
/*     */   
/*     */   public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
/*     */   {
/*  74 */     if (this.isOn) {
/*     */       EnumFacing[] arrayOfEnumFacing;
/*  76 */       int j = (arrayOfEnumFacing = EnumFacing.values()).length; for (int i = 0; i < j; i++) { EnumFacing enumfacing = arrayOfEnumFacing[i];
/*     */         
/*  78 */         worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), this);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
/*     */   {
/*  85 */     if (this.isOn) {
/*     */       EnumFacing[] arrayOfEnumFacing;
/*  87 */       int j = (arrayOfEnumFacing = EnumFacing.values()).length; for (int i = 0; i < j; i++) { EnumFacing enumfacing = arrayOfEnumFacing[i];
/*     */         
/*  89 */         worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), this);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public int getWeakPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side)
/*     */   {
/*  96 */     return (this.isOn) && (state.getValue(FACING) != side) ? 15 : 0;
/*     */   }
/*     */   
/*     */   private boolean shouldBeOff(World worldIn, BlockPos pos, IBlockState state)
/*     */   {
/* 101 */     EnumFacing enumfacing = ((EnumFacing)state.getValue(FACING)).getOpposite();
/* 102 */     return worldIn.isSidePowered(pos.offset(enumfacing), enumfacing);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random) {}
/*     */   
/*     */ 
/*     */ 
/*     */   public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
/*     */   {
/* 114 */     boolean flag = shouldBeOff(worldIn, pos, state);
/* 115 */     List<Toggle> list = (List)toggles.get(worldIn);
/*     */     
/* 117 */     while ((list != null) && (!list.isEmpty()) && (worldIn.getTotalWorldTime() - ((Toggle)list.get(0)).time > 60L))
/*     */     {
/* 119 */       list.remove(0);
/*     */     }
/*     */     
/* 122 */     if (this.isOn)
/*     */     {
/* 124 */       if (flag)
/*     */       {
/* 126 */         worldIn.setBlockState(pos, Blocks.unlit_redstone_torch.getDefaultState().withProperty(FACING, (EnumFacing)state.getValue(FACING)), 3);
/*     */         
/* 128 */         if (isBurnedOut(worldIn, pos, true))
/*     */         {
/* 130 */           worldIn.playSoundEffect(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, "random.fizz", 0.5F, 2.6F + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.8F);
/*     */           
/* 132 */           for (int i = 0; i < 5; i++)
/*     */           {
/* 134 */             double d0 = pos.getX() + rand.nextDouble() * 0.6D + 0.2D;
/* 135 */             double d1 = pos.getY() + rand.nextDouble() * 0.6D + 0.2D;
/* 136 */             double d2 = pos.getZ() + rand.nextDouble() * 0.6D + 0.2D;
/* 137 */             worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, 0.0D, 0.0D, 0.0D, new int[0]);
/*     */           }
/*     */           
/* 140 */           worldIn.scheduleUpdate(pos, worldIn.getBlockState(pos).getBlock(), 160);
/*     */         }
/*     */       }
/*     */     }
/* 144 */     else if ((!flag) && (!isBurnedOut(worldIn, pos, false)))
/*     */     {
/* 146 */       worldIn.setBlockState(pos, Blocks.redstone_torch.getDefaultState().withProperty(FACING, (EnumFacing)state.getValue(FACING)), 3);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
/*     */   {
/* 155 */     if (!onNeighborChangeInternal(worldIn, pos, state))
/*     */     {
/* 157 */       if (this.isOn == shouldBeOff(worldIn, pos, state))
/*     */       {
/* 159 */         worldIn.scheduleUpdate(pos, this, tickRate(worldIn));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public int getStrongPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side)
/*     */   {
/* 166 */     return side == EnumFacing.DOWN ? getWeakPower(worldIn, pos, state, side) : 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Item getItemDropped(IBlockState state, Random rand, int fortune)
/*     */   {
/* 174 */     return Item.getItemFromBlock(Blocks.redstone_torch);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean canProvidePower()
/*     */   {
/* 182 */     return true;
/*     */   }
/*     */   
/*     */   public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
/*     */   {
/* 187 */     if (this.isOn)
/*     */     {
/* 189 */       double d0 = pos.getX() + 0.5D + (rand.nextDouble() - 0.5D) * 0.2D;
/* 190 */       double d1 = pos.getY() + 0.7D + (rand.nextDouble() - 0.5D) * 0.2D;
/* 191 */       double d2 = pos.getZ() + 0.5D + (rand.nextDouble() - 0.5D) * 0.2D;
/* 192 */       EnumFacing enumfacing = (EnumFacing)state.getValue(FACING);
/*     */       
/* 194 */       if (enumfacing.getAxis().isHorizontal())
/*     */       {
/* 196 */         EnumFacing enumfacing1 = enumfacing.getOpposite();
/* 197 */         double d3 = 0.27D;
/* 198 */         d0 += 0.27D * enumfacing1.getFrontOffsetX();
/* 199 */         d1 += 0.22D;
/* 200 */         d2 += 0.27D * enumfacing1.getFrontOffsetZ();
/*     */       }
/*     */       
/* 203 */       worldIn.spawnParticle(EnumParticleTypes.REDSTONE, d0, d1, d2, 0.0D, 0.0D, 0.0D, new int[0]);
/*     */     }
/*     */   }
/*     */   
/*     */   public Item getItem(World worldIn, BlockPos pos)
/*     */   {
/* 209 */     return Item.getItemFromBlock(Blocks.redstone_torch);
/*     */   }
/*     */   
/*     */   public boolean isAssociatedBlock(Block other)
/*     */   {
/* 214 */     return (other == Blocks.unlit_redstone_torch) || (other == Blocks.redstone_torch);
/*     */   }
/*     */   
/*     */   static class Toggle
/*     */   {
/*     */     BlockPos pos;
/*     */     long time;
/*     */     
/*     */     public Toggle(BlockPos pos, long time)
/*     */     {
/* 224 */       this.pos = pos;
/* 225 */       this.time = time;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\BlockRedstoneTorch.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */