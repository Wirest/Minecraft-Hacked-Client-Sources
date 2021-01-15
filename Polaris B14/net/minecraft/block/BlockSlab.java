/*     */ package net.minecraft.block;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.properties.PropertyEnum;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.IStringSerializable;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public abstract class BlockSlab extends Block
/*     */ {
/*  22 */   public static final PropertyEnum<EnumBlockHalf> HALF = PropertyEnum.create("half", EnumBlockHalf.class);
/*     */   
/*     */   public BlockSlab(Material materialIn)
/*     */   {
/*  26 */     super(materialIn);
/*     */     
/*  28 */     if (isDouble())
/*     */     {
/*  30 */       this.fullBlock = true;
/*     */     }
/*     */     else
/*     */     {
/*  34 */       setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
/*     */     }
/*     */     
/*  37 */     setLightOpacity(255);
/*     */   }
/*     */   
/*     */   protected boolean canSilkHarvest()
/*     */   {
/*  42 */     return false;
/*     */   }
/*     */   
/*     */   public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos)
/*     */   {
/*  47 */     if (isDouble())
/*     */     {
/*  49 */       setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
/*     */     }
/*     */     else
/*     */     {
/*  53 */       IBlockState iblockstate = worldIn.getBlockState(pos);
/*     */       
/*  55 */       if (iblockstate.getBlock() == this)
/*     */       {
/*  57 */         if (iblockstate.getValue(HALF) == EnumBlockHalf.TOP)
/*     */         {
/*  59 */           setBlockBounds(0.0F, 0.5F, 0.0F, 1.0F, 1.0F, 1.0F);
/*     */         }
/*     */         else
/*     */         {
/*  63 */           setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setBlockBoundsForItemRender()
/*     */   {
/*  74 */     if (isDouble())
/*     */     {
/*  76 */       setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
/*     */     }
/*     */     else
/*     */     {
/*  80 */       setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity)
/*     */   {
/*  89 */     setBlockBoundsBasedOnState(worldIn, pos);
/*  90 */     super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isOpaqueCube()
/*     */   {
/*  98 */     return isDouble();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
/*     */   {
/* 107 */     IBlockState iblockstate = super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(HALF, EnumBlockHalf.BOTTOM);
/* 108 */     return (facing != EnumFacing.DOWN) && ((facing == EnumFacing.UP) || (hitY <= 0.5D)) ? iblockstate : isDouble() ? iblockstate : iblockstate.withProperty(HALF, EnumBlockHalf.TOP);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int quantityDropped(Random random)
/*     */   {
/* 116 */     return isDouble() ? 2 : 1;
/*     */   }
/*     */   
/*     */   public boolean isFullCube()
/*     */   {
/* 121 */     return isDouble();
/*     */   }
/*     */   
/*     */   public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side)
/*     */   {
/* 126 */     if (isDouble())
/*     */     {
/* 128 */       return super.shouldSideBeRendered(worldIn, pos, side);
/*     */     }
/* 130 */     if ((side != EnumFacing.UP) && (side != EnumFacing.DOWN) && (!super.shouldSideBeRendered(worldIn, pos, side)))
/*     */     {
/* 132 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 136 */     BlockPos blockpos = pos.offset(side.getOpposite());
/* 137 */     IBlockState iblockstate = worldIn.getBlockState(pos);
/* 138 */     IBlockState iblockstate1 = worldIn.getBlockState(blockpos);
/* 139 */     boolean flag = (isSlab(iblockstate.getBlock())) && (iblockstate.getValue(HALF) == EnumBlockHalf.TOP);
/* 140 */     boolean flag1 = (isSlab(iblockstate1.getBlock())) && (iblockstate1.getValue(HALF) == EnumBlockHalf.TOP);
/* 141 */     return side == EnumFacing.DOWN;
/*     */   }
/*     */   
/*     */ 
/*     */   protected static boolean isSlab(Block blockIn)
/*     */   {
/* 147 */     return (blockIn == Blocks.stone_slab) || (blockIn == Blocks.wooden_slab) || (blockIn == Blocks.stone_slab2);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public abstract String getUnlocalizedName(int paramInt);
/*     */   
/*     */ 
/*     */   public int getDamageValue(World worldIn, BlockPos pos)
/*     */   {
/* 157 */     return super.getDamageValue(worldIn, pos) & 0x7;
/*     */   }
/*     */   
/*     */   public abstract boolean isDouble();
/*     */   
/*     */   public abstract IProperty<?> getVariantProperty();
/*     */   
/*     */   public abstract Object getVariant(ItemStack paramItemStack);
/*     */   
/*     */   public static enum EnumBlockHalf implements IStringSerializable
/*     */   {
/* 168 */     TOP("top"), 
/* 169 */     BOTTOM("bottom");
/*     */     
/*     */     private final String name;
/*     */     
/*     */     private EnumBlockHalf(String name)
/*     */     {
/* 175 */       this.name = name;
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 180 */       return this.name;
/*     */     }
/*     */     
/*     */     public String getName()
/*     */     {
/* 185 */       return this.name;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\BlockSlab.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */