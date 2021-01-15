/*     */ package net.minecraft.block;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.properties.PropertyBool;
/*     */ import net.minecraft.block.state.BlockState;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumWorldBlockLayer;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class BlockTripWire extends Block
/*     */ {
/*  24 */   public static final PropertyBool POWERED = PropertyBool.create("powered");
/*  25 */   public static final PropertyBool SUSPENDED = PropertyBool.create("suspended");
/*  26 */   public static final PropertyBool ATTACHED = PropertyBool.create("attached");
/*  27 */   public static final PropertyBool DISARMED = PropertyBool.create("disarmed");
/*  28 */   public static final PropertyBool NORTH = PropertyBool.create("north");
/*  29 */   public static final PropertyBool EAST = PropertyBool.create("east");
/*  30 */   public static final PropertyBool SOUTH = PropertyBool.create("south");
/*  31 */   public static final PropertyBool WEST = PropertyBool.create("west");
/*     */   
/*     */   public BlockTripWire()
/*     */   {
/*  35 */     super(net.minecraft.block.material.Material.circuits);
/*  36 */     setDefaultState(this.blockState.getBaseState().withProperty(POWERED, Boolean.valueOf(false)).withProperty(SUSPENDED, Boolean.valueOf(false)).withProperty(ATTACHED, Boolean.valueOf(false)).withProperty(DISARMED, Boolean.valueOf(false)).withProperty(NORTH, Boolean.valueOf(false)).withProperty(EAST, Boolean.valueOf(false)).withProperty(SOUTH, Boolean.valueOf(false)).withProperty(WEST, Boolean.valueOf(false)));
/*  37 */     setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.15625F, 1.0F);
/*  38 */     setTickRandomly(true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
/*     */   {
/*  47 */     return state.withProperty(NORTH, Boolean.valueOf(isConnectedTo(worldIn, pos, state, EnumFacing.NORTH))).withProperty(EAST, Boolean.valueOf(isConnectedTo(worldIn, pos, state, EnumFacing.EAST))).withProperty(SOUTH, Boolean.valueOf(isConnectedTo(worldIn, pos, state, EnumFacing.SOUTH))).withProperty(WEST, Boolean.valueOf(isConnectedTo(worldIn, pos, state, EnumFacing.WEST)));
/*     */   }
/*     */   
/*     */   public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
/*     */   {
/*  52 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isOpaqueCube()
/*     */   {
/*  60 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isFullCube()
/*     */   {
/*  65 */     return false;
/*     */   }
/*     */   
/*     */   public EnumWorldBlockLayer getBlockLayer()
/*     */   {
/*  70 */     return EnumWorldBlockLayer.TRANSLUCENT;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Item getItemDropped(IBlockState state, Random rand, int fortune)
/*     */   {
/*  78 */     return Items.string;
/*     */   }
/*     */   
/*     */   public Item getItem(World worldIn, BlockPos pos)
/*     */   {
/*  83 */     return Items.string;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
/*     */   {
/*  91 */     boolean flag = ((Boolean)state.getValue(SUSPENDED)).booleanValue();
/*  92 */     boolean flag1 = !World.doesBlockHaveSolidTopSurface(worldIn, pos.down());
/*     */     
/*  94 */     if (flag != flag1)
/*     */     {
/*  96 */       dropBlockAsItem(worldIn, pos, state, 0);
/*  97 */       worldIn.setBlockToAir(pos);
/*     */     }
/*     */   }
/*     */   
/*     */   public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos)
/*     */   {
/* 103 */     IBlockState iblockstate = worldIn.getBlockState(pos);
/* 104 */     boolean flag = ((Boolean)iblockstate.getValue(ATTACHED)).booleanValue();
/* 105 */     boolean flag1 = ((Boolean)iblockstate.getValue(SUSPENDED)).booleanValue();
/*     */     
/* 107 */     if (!flag1)
/*     */     {
/* 109 */       setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.09375F, 1.0F);
/*     */     }
/* 111 */     else if (!flag)
/*     */     {
/* 113 */       setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
/*     */     }
/*     */     else
/*     */     {
/* 117 */       setBlockBounds(0.0F, 0.0625F, 0.0F, 1.0F, 0.15625F, 1.0F);
/*     */     }
/*     */   }
/*     */   
/*     */   public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
/*     */   {
/* 123 */     state = state.withProperty(SUSPENDED, Boolean.valueOf(!World.doesBlockHaveSolidTopSurface(worldIn, pos.down())));
/* 124 */     worldIn.setBlockState(pos, state, 3);
/* 125 */     notifyHook(worldIn, pos, state);
/*     */   }
/*     */   
/*     */   public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
/*     */   {
/* 130 */     notifyHook(worldIn, pos, state.withProperty(POWERED, Boolean.valueOf(true)));
/*     */   }
/*     */   
/*     */   public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player)
/*     */   {
/* 135 */     if (!worldIn.isRemote)
/*     */     {
/* 137 */       if ((player.getCurrentEquippedItem() != null) && (player.getCurrentEquippedItem().getItem() == Items.shears))
/*     */       {
/* 139 */         worldIn.setBlockState(pos, state.withProperty(DISARMED, Boolean.valueOf(true)), 4);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void notifyHook(World worldIn, BlockPos pos, IBlockState state) {
/*     */     EnumFacing[] arrayOfEnumFacing;
/* 146 */     int j = (arrayOfEnumFacing = new EnumFacing[] { EnumFacing.SOUTH, EnumFacing.WEST }).length; for (int i = 0; i < j; i++) { EnumFacing enumfacing = arrayOfEnumFacing[i];
/*     */       
/* 148 */       for (int i = 1; i < 42; i++)
/*     */       {
/* 150 */         BlockPos blockpos = pos.offset(enumfacing, i);
/* 151 */         IBlockState iblockstate = worldIn.getBlockState(blockpos);
/*     */         
/* 153 */         if (iblockstate.getBlock() == Blocks.tripwire_hook)
/*     */         {
/* 155 */           if (iblockstate.getValue(BlockTripWireHook.FACING) == enumfacing.getOpposite())
/*     */           {
/* 157 */             Blocks.tripwire_hook.func_176260_a(worldIn, blockpos, iblockstate, false, true, i, state);
/*     */           }
/*     */           
/*     */ 
/*     */         }
/*     */         else {
/* 163 */           if (iblockstate.getBlock() != Blocks.tripwire) {
/*     */             break;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
/*     */   {
/* 176 */     if (!worldIn.isRemote)
/*     */     {
/* 178 */       if (!((Boolean)state.getValue(POWERED)).booleanValue())
/*     */       {
/* 180 */         updateState(worldIn, pos);
/*     */       }
/*     */     }
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
/* 194 */     if (!worldIn.isRemote)
/*     */     {
/* 196 */       if (((Boolean)worldIn.getBlockState(pos).getValue(POWERED)).booleanValue())
/*     */       {
/* 198 */         updateState(worldIn, pos);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void updateState(World worldIn, BlockPos pos)
/*     */   {
/* 205 */     IBlockState iblockstate = worldIn.getBlockState(pos);
/* 206 */     boolean flag = ((Boolean)iblockstate.getValue(POWERED)).booleanValue();
/* 207 */     boolean flag1 = false;
/* 208 */     List<? extends Entity> list = worldIn.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(pos.getX() + this.minX, pos.getY() + this.minY, pos.getZ() + this.minZ, pos.getX() + this.maxX, pos.getY() + this.maxY, pos.getZ() + this.maxZ));
/*     */     
/* 210 */     if (!list.isEmpty())
/*     */     {
/* 212 */       for (Entity entity : list)
/*     */       {
/* 214 */         if (!entity.doesEntityNotTriggerPressurePlate())
/*     */         {
/* 216 */           flag1 = true;
/* 217 */           break;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 222 */     if (flag1 != flag)
/*     */     {
/* 224 */       iblockstate = iblockstate.withProperty(POWERED, Boolean.valueOf(flag1));
/* 225 */       worldIn.setBlockState(pos, iblockstate, 3);
/* 226 */       notifyHook(worldIn, pos, iblockstate);
/*     */     }
/*     */     
/* 229 */     if (flag1)
/*     */     {
/* 231 */       worldIn.scheduleUpdate(pos, this, tickRate(worldIn));
/*     */     }
/*     */   }
/*     */   
/*     */   public static boolean isConnectedTo(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing direction)
/*     */   {
/* 237 */     BlockPos blockpos = pos.offset(direction);
/* 238 */     IBlockState iblockstate = worldIn.getBlockState(blockpos);
/* 239 */     Block block = iblockstate.getBlock();
/*     */     
/* 241 */     if (block == Blocks.tripwire_hook)
/*     */     {
/* 243 */       EnumFacing enumfacing = direction.getOpposite();
/* 244 */       return iblockstate.getValue(BlockTripWireHook.FACING) == enumfacing;
/*     */     }
/* 246 */     if (block == Blocks.tripwire)
/*     */     {
/* 248 */       boolean flag = ((Boolean)state.getValue(SUSPENDED)).booleanValue();
/* 249 */       boolean flag1 = ((Boolean)iblockstate.getValue(SUSPENDED)).booleanValue();
/* 250 */       return flag == flag1;
/*     */     }
/*     */     
/*     */ 
/* 254 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public IBlockState getStateFromMeta(int meta)
/*     */   {
/* 263 */     return getDefaultState().withProperty(POWERED, Boolean.valueOf((meta & 0x1) > 0)).withProperty(SUSPENDED, Boolean.valueOf((meta & 0x2) > 0)).withProperty(ATTACHED, Boolean.valueOf((meta & 0x4) > 0)).withProperty(DISARMED, Boolean.valueOf((meta & 0x8) > 0));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMetaFromState(IBlockState state)
/*     */   {
/* 271 */     int i = 0;
/*     */     
/* 273 */     if (((Boolean)state.getValue(POWERED)).booleanValue())
/*     */     {
/* 275 */       i |= 0x1;
/*     */     }
/*     */     
/* 278 */     if (((Boolean)state.getValue(SUSPENDED)).booleanValue())
/*     */     {
/* 280 */       i |= 0x2;
/*     */     }
/*     */     
/* 283 */     if (((Boolean)state.getValue(ATTACHED)).booleanValue())
/*     */     {
/* 285 */       i |= 0x4;
/*     */     }
/*     */     
/* 288 */     if (((Boolean)state.getValue(DISARMED)).booleanValue())
/*     */     {
/* 290 */       i |= 0x8;
/*     */     }
/*     */     
/* 293 */     return i;
/*     */   }
/*     */   
/*     */   protected BlockState createBlockState()
/*     */   {
/* 298 */     return new BlockState(this, new IProperty[] { POWERED, SUSPENDED, ATTACHED, DISARMED, NORTH, EAST, WEST, SOUTH });
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\BlockTripWire.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */