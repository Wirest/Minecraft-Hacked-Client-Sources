/*     */ package net.minecraft.block;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.properties.PropertyDirection;
/*     */ import net.minecraft.block.properties.PropertyInteger;
/*     */ import net.minecraft.block.state.BlockState;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.tileentity.TileEntity;
/*     */ import net.minecraft.tileentity.TileEntityBanner;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumFacing.Plane;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class BlockBanner extends BlockContainer
/*     */ {
/*  26 */   public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
/*  27 */   public static final PropertyInteger ROTATION = PropertyInteger.create("rotation", 0, 15);
/*     */   
/*     */   protected BlockBanner()
/*     */   {
/*  31 */     super(Material.wood);
/*  32 */     float f = 0.25F;
/*  33 */     float f1 = 1.0F;
/*  34 */     setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f1, 0.5F + f);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getLocalizedName()
/*     */   {
/*  42 */     return net.minecraft.util.StatCollector.translateToLocal("item.banner.white.name");
/*     */   }
/*     */   
/*     */   public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
/*     */   {
/*  47 */     return null;
/*     */   }
/*     */   
/*     */   public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos)
/*     */   {
/*  52 */     setBlockBoundsBasedOnState(worldIn, pos);
/*  53 */     return super.getSelectedBoundingBox(worldIn, pos);
/*     */   }
/*     */   
/*     */   public boolean isFullCube()
/*     */   {
/*  58 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isPassable(IBlockAccess worldIn, BlockPos pos)
/*     */   {
/*  63 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isOpaqueCube()
/*     */   {
/*  71 */     return false;
/*     */   }
/*     */   
/*     */   public boolean func_181623_g()
/*     */   {
/*  76 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public TileEntity createNewTileEntity(World worldIn, int meta)
/*     */   {
/*  84 */     return new TileEntityBanner();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Item getItemDropped(IBlockState state, Random rand, int fortune)
/*     */   {
/*  92 */     return Items.banner;
/*     */   }
/*     */   
/*     */   public Item getItem(World worldIn, BlockPos pos)
/*     */   {
/*  97 */     return Items.banner;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune)
/*     */   {
/* 105 */     TileEntity tileentity = worldIn.getTileEntity(pos);
/*     */     
/* 107 */     if ((tileentity instanceof TileEntityBanner))
/*     */     {
/* 109 */       ItemStack itemstack = new ItemStack(Items.banner, 1, ((TileEntityBanner)tileentity).getBaseColor());
/* 110 */       NBTTagCompound nbttagcompound = new NBTTagCompound();
/* 111 */       tileentity.writeToNBT(nbttagcompound);
/* 112 */       nbttagcompound.removeTag("x");
/* 113 */       nbttagcompound.removeTag("y");
/* 114 */       nbttagcompound.removeTag("z");
/* 115 */       nbttagcompound.removeTag("id");
/* 116 */       itemstack.setTagInfo("BlockEntityTag", nbttagcompound);
/* 117 */       spawnAsEntity(worldIn, pos, itemstack);
/*     */     }
/*     */     else
/*     */     {
/* 121 */       super.dropBlockAsItemWithChance(worldIn, pos, state, chance, fortune);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
/*     */   {
/* 127 */     return (!func_181087_e(worldIn, pos)) && (super.canPlaceBlockAt(worldIn, pos));
/*     */   }
/*     */   
/*     */   public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te)
/*     */   {
/* 132 */     if ((te instanceof TileEntityBanner))
/*     */     {
/* 134 */       TileEntityBanner tileentitybanner = (TileEntityBanner)te;
/* 135 */       ItemStack itemstack = new ItemStack(Items.banner, 1, ((TileEntityBanner)te).getBaseColor());
/* 136 */       NBTTagCompound nbttagcompound = new NBTTagCompound();
/* 137 */       TileEntityBanner.func_181020_a(nbttagcompound, tileentitybanner.getBaseColor(), tileentitybanner.func_181021_d());
/* 138 */       itemstack.setTagInfo("BlockEntityTag", nbttagcompound);
/* 139 */       spawnAsEntity(worldIn, pos, itemstack);
/*     */     }
/*     */     else
/*     */     {
/* 143 */       super.harvestBlock(worldIn, player, pos, state, null);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class BlockBannerHanging extends BlockBanner
/*     */   {
/*     */     public BlockBannerHanging()
/*     */     {
/* 151 */       setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
/*     */     }
/*     */     
/*     */     public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos)
/*     */     {
/* 156 */       EnumFacing enumfacing = (EnumFacing)worldIn.getBlockState(pos).getValue(FACING);
/* 157 */       float f = 0.0F;
/* 158 */       float f1 = 0.78125F;
/* 159 */       float f2 = 0.0F;
/* 160 */       float f3 = 1.0F;
/* 161 */       float f4 = 0.125F;
/* 162 */       setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
/*     */       
/* 164 */       switch (enumfacing)
/*     */       {
/*     */       case NORTH: 
/*     */       default: 
/* 168 */         setBlockBounds(f2, f, 1.0F - f4, f3, f1, 1.0F);
/* 169 */         break;
/*     */       
/*     */       case SOUTH: 
/* 172 */         setBlockBounds(f2, f, 0.0F, f3, f1, f4);
/* 173 */         break;
/*     */       
/*     */       case UP: 
/* 176 */         setBlockBounds(1.0F - f4, f, f2, 1.0F, f1, f3);
/* 177 */         break;
/*     */       
/*     */       case WEST: 
/* 180 */         setBlockBounds(0.0F, f, f2, f4, f1, f3);
/*     */       }
/*     */     }
/*     */     
/*     */     public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
/*     */     {
/* 186 */       EnumFacing enumfacing = (EnumFacing)state.getValue(FACING);
/*     */       
/* 188 */       if (!worldIn.getBlockState(pos.offset(enumfacing.getOpposite())).getBlock().getMaterial().isSolid())
/*     */       {
/* 190 */         dropBlockAsItem(worldIn, pos, state, 0);
/* 191 */         worldIn.setBlockToAir(pos);
/*     */       }
/*     */       
/* 194 */       super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
/*     */     }
/*     */     
/*     */     public IBlockState getStateFromMeta(int meta)
/*     */     {
/* 199 */       EnumFacing enumfacing = EnumFacing.getFront(meta);
/*     */       
/* 201 */       if (enumfacing.getAxis() == net.minecraft.util.EnumFacing.Axis.Y)
/*     */       {
/* 203 */         enumfacing = EnumFacing.NORTH;
/*     */       }
/*     */       
/* 206 */       return getDefaultState().withProperty(FACING, enumfacing);
/*     */     }
/*     */     
/*     */     public int getMetaFromState(IBlockState state)
/*     */     {
/* 211 */       return ((EnumFacing)state.getValue(FACING)).getIndex();
/*     */     }
/*     */     
/*     */     protected BlockState createBlockState()
/*     */     {
/* 216 */       return new BlockState(this, new IProperty[] { FACING });
/*     */     }
/*     */   }
/*     */   
/*     */   public static class BlockBannerStanding extends BlockBanner
/*     */   {
/*     */     public BlockBannerStanding()
/*     */     {
/* 224 */       setDefaultState(this.blockState.getBaseState().withProperty(ROTATION, Integer.valueOf(0)));
/*     */     }
/*     */     
/*     */     public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
/*     */     {
/* 229 */       if (!worldIn.getBlockState(pos.down()).getBlock().getMaterial().isSolid())
/*     */       {
/* 231 */         dropBlockAsItem(worldIn, pos, state, 0);
/* 232 */         worldIn.setBlockToAir(pos);
/*     */       }
/*     */       
/* 235 */       super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
/*     */     }
/*     */     
/*     */     public IBlockState getStateFromMeta(int meta)
/*     */     {
/* 240 */       return getDefaultState().withProperty(ROTATION, Integer.valueOf(meta));
/*     */     }
/*     */     
/*     */     public int getMetaFromState(IBlockState state)
/*     */     {
/* 245 */       return ((Integer)state.getValue(ROTATION)).intValue();
/*     */     }
/*     */     
/*     */     protected BlockState createBlockState()
/*     */     {
/* 250 */       return new BlockState(this, new IProperty[] { ROTATION });
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\BlockBanner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */