/*     */ package net.minecraft.block;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Queue;
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.properties.PropertyBool;
/*     */ import net.minecraft.block.state.BlockState;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.creativetab.CreativeTabs;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumFacing.Axis;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.util.Tuple;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class BlockSponge extends Block
/*     */ {
/*  26 */   public static final PropertyBool WET = PropertyBool.create("wet");
/*     */   
/*     */   protected BlockSponge()
/*     */   {
/*  30 */     super(Material.sponge);
/*  31 */     setDefaultState(this.blockState.getBaseState().withProperty(WET, Boolean.valueOf(false)));
/*  32 */     setCreativeTab(CreativeTabs.tabBlock);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getLocalizedName()
/*     */   {
/*  40 */     return net.minecraft.util.StatCollector.translateToLocal(getUnlocalizedName() + ".dry.name");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int damageDropped(IBlockState state)
/*     */   {
/*  49 */     return ((Boolean)state.getValue(WET)).booleanValue() ? 1 : 0;
/*     */   }
/*     */   
/*     */   public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
/*     */   {
/*  54 */     tryAbsorb(worldIn, pos, state);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
/*     */   {
/*  62 */     tryAbsorb(worldIn, pos, state);
/*  63 */     super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
/*     */   }
/*     */   
/*     */   protected void tryAbsorb(World worldIn, BlockPos pos, IBlockState state)
/*     */   {
/*  68 */     if ((!((Boolean)state.getValue(WET)).booleanValue()) && (absorb(worldIn, pos)))
/*     */     {
/*  70 */       worldIn.setBlockState(pos, state.withProperty(WET, Boolean.valueOf(true)), 2);
/*  71 */       worldIn.playAuxSFX(2001, pos, Block.getIdFromBlock(Blocks.water));
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean absorb(World worldIn, BlockPos pos)
/*     */   {
/*  77 */     Queue<Tuple<BlockPos, Integer>> queue = Lists.newLinkedList();
/*  78 */     ArrayList<BlockPos> arraylist = Lists.newArrayList();
/*  79 */     queue.add(new Tuple(pos, Integer.valueOf(0)));
/*  80 */     int i = 0;
/*     */     BlockPos blockpos;
/*  82 */     while (!queue.isEmpty())
/*     */     {
/*  84 */       Tuple<BlockPos, Integer> tuple = (Tuple)queue.poll();
/*  85 */       blockpos = (BlockPos)tuple.getFirst();
/*  86 */       int j = ((Integer)tuple.getSecond()).intValue();
/*     */       EnumFacing[] arrayOfEnumFacing;
/*  88 */       int j = (arrayOfEnumFacing = EnumFacing.values()).length; for (int i = 0; i < j; i++) { EnumFacing enumfacing = arrayOfEnumFacing[i];
/*     */         
/*  90 */         BlockPos blockpos1 = blockpos.offset(enumfacing);
/*     */         
/*  92 */         if (worldIn.getBlockState(blockpos1).getBlock().getMaterial() == Material.water)
/*     */         {
/*  94 */           worldIn.setBlockState(blockpos1, Blocks.air.getDefaultState(), 2);
/*  95 */           arraylist.add(blockpos1);
/*  96 */           i++;
/*     */           
/*  98 */           if (j < 6)
/*     */           {
/* 100 */             queue.add(new Tuple(blockpos1, Integer.valueOf(j + 1)));
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 105 */       if (i > 64) {
/*     */         break;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 111 */     for (BlockPos blockpos2 : arraylist)
/*     */     {
/* 113 */       worldIn.notifyNeighborsOfStateChange(blockpos2, Blocks.air);
/*     */     }
/*     */     
/* 116 */     return i > 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list)
/*     */   {
/* 124 */     list.add(new ItemStack(itemIn, 1, 0));
/* 125 */     list.add(new ItemStack(itemIn, 1, 1));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public IBlockState getStateFromMeta(int meta)
/*     */   {
/* 133 */     return getDefaultState().withProperty(WET, Boolean.valueOf((meta & 0x1) == 1));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMetaFromState(IBlockState state)
/*     */   {
/* 141 */     return ((Boolean)state.getValue(WET)).booleanValue() ? 1 : 0;
/*     */   }
/*     */   
/*     */   protected BlockState createBlockState()
/*     */   {
/* 146 */     return new BlockState(this, new IProperty[] { WET });
/*     */   }
/*     */   
/*     */   public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
/*     */   {
/* 151 */     if (((Boolean)state.getValue(WET)).booleanValue())
/*     */     {
/* 153 */       EnumFacing enumfacing = EnumFacing.random(rand);
/*     */       
/* 155 */       if ((enumfacing != EnumFacing.UP) && (!World.doesBlockHaveSolidTopSurface(worldIn, pos.offset(enumfacing))))
/*     */       {
/* 157 */         double d0 = pos.getX();
/* 158 */         double d1 = pos.getY();
/* 159 */         double d2 = pos.getZ();
/*     */         
/* 161 */         if (enumfacing == EnumFacing.DOWN)
/*     */         {
/* 163 */           d1 -= 0.05D;
/* 164 */           d0 += rand.nextDouble();
/* 165 */           d2 += rand.nextDouble();
/*     */         }
/*     */         else
/*     */         {
/* 169 */           d1 += rand.nextDouble() * 0.8D;
/*     */           
/* 171 */           if (enumfacing.getAxis() == EnumFacing.Axis.X)
/*     */           {
/* 173 */             d2 += rand.nextDouble();
/*     */             
/* 175 */             if (enumfacing == EnumFacing.EAST)
/*     */             {
/* 177 */               d0 += 1.0D;
/*     */             }
/*     */             else
/*     */             {
/* 181 */               d0 += 0.05D;
/*     */             }
/*     */           }
/*     */           else
/*     */           {
/* 186 */             d0 += rand.nextDouble();
/*     */             
/* 188 */             if (enumfacing == EnumFacing.SOUTH)
/*     */             {
/* 190 */               d2 += 1.0D;
/*     */             }
/*     */             else
/*     */             {
/* 194 */               d2 += 0.05D;
/*     */             }
/*     */           }
/*     */         }
/*     */         
/* 199 */         worldIn.spawnParticle(EnumParticleTypes.DRIP_WATER, d0, d1, d2, 0.0D, 0.0D, 0.0D, new int[0]);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\BlockSponge.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */