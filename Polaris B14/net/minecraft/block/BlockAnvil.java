/*     */ package net.minecraft.block;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.properties.PropertyDirection;
/*     */ import net.minecraft.block.properties.PropertyInteger;
/*     */ import net.minecraft.block.state.BlockState;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.creativetab.CreativeTabs;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.item.EntityFallingBlock;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.InventoryPlayer;
/*     */ import net.minecraft.inventory.Container;
/*     */ import net.minecraft.inventory.ContainerRepair;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumFacing.Axis;
/*     */ import net.minecraft.util.EnumFacing.Plane;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.IInteractionObject;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class BlockAnvil extends BlockFalling
/*     */ {
/*  30 */   public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
/*  31 */   public static final PropertyInteger DAMAGE = PropertyInteger.create("damage", 0, 2);
/*     */   
/*     */   protected BlockAnvil()
/*     */   {
/*  35 */     super(Material.anvil);
/*  36 */     setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(DAMAGE, Integer.valueOf(0)));
/*  37 */     setLightOpacity(0);
/*  38 */     setCreativeTab(CreativeTabs.tabDecorations);
/*     */   }
/*     */   
/*     */   public boolean isFullCube()
/*     */   {
/*  43 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isOpaqueCube()
/*     */   {
/*  51 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
/*     */   {
/*  60 */     EnumFacing enumfacing = placer.getHorizontalFacing().rotateY();
/*  61 */     return super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(FACING, enumfacing).withProperty(DAMAGE, Integer.valueOf(meta >> 2));
/*     */   }
/*     */   
/*     */   public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
/*     */   {
/*  66 */     if (!worldIn.isRemote)
/*     */     {
/*  68 */       playerIn.displayGui(new Anvil(worldIn, pos));
/*     */     }
/*     */     
/*  71 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int damageDropped(IBlockState state)
/*     */   {
/*  80 */     return ((Integer)state.getValue(DAMAGE)).intValue();
/*     */   }
/*     */   
/*     */   public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos)
/*     */   {
/*  85 */     EnumFacing enumfacing = (EnumFacing)worldIn.getBlockState(pos).getValue(FACING);
/*     */     
/*  87 */     if (enumfacing.getAxis() == EnumFacing.Axis.X)
/*     */     {
/*  89 */       setBlockBounds(0.0F, 0.0F, 0.125F, 1.0F, 1.0F, 0.875F);
/*     */     }
/*     */     else
/*     */     {
/*  93 */       setBlockBounds(0.125F, 0.0F, 0.0F, 0.875F, 1.0F, 1.0F);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list)
/*     */   {
/* 102 */     list.add(new ItemStack(itemIn, 1, 0));
/* 103 */     list.add(new ItemStack(itemIn, 1, 1));
/* 104 */     list.add(new ItemStack(itemIn, 1, 2));
/*     */   }
/*     */   
/*     */   protected void onStartFalling(EntityFallingBlock fallingEntity)
/*     */   {
/* 109 */     fallingEntity.setHurtEntities(true);
/*     */   }
/*     */   
/*     */   public void onEndFalling(World worldIn, BlockPos pos)
/*     */   {
/* 114 */     worldIn.playAuxSFX(1022, pos, 0);
/*     */   }
/*     */   
/*     */   public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side)
/*     */   {
/* 119 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public IBlockState getStateForEntityRender(IBlockState state)
/*     */   {
/* 127 */     return getDefaultState().withProperty(FACING, EnumFacing.SOUTH);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public IBlockState getStateFromMeta(int meta)
/*     */   {
/* 135 */     return getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta & 0x3)).withProperty(DAMAGE, Integer.valueOf((meta & 0xF) >> 2));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMetaFromState(IBlockState state)
/*     */   {
/* 143 */     int i = 0;
/* 144 */     i |= ((EnumFacing)state.getValue(FACING)).getHorizontalIndex();
/* 145 */     i |= ((Integer)state.getValue(DAMAGE)).intValue() << 2;
/* 146 */     return i;
/*     */   }
/*     */   
/*     */   protected BlockState createBlockState()
/*     */   {
/* 151 */     return new BlockState(this, new IProperty[] { FACING, DAMAGE });
/*     */   }
/*     */   
/*     */   public static class Anvil implements IInteractionObject
/*     */   {
/*     */     private final World world;
/*     */     private final BlockPos position;
/*     */     
/*     */     public Anvil(World worldIn, BlockPos pos)
/*     */     {
/* 161 */       this.world = worldIn;
/* 162 */       this.position = pos;
/*     */     }
/*     */     
/*     */     public String getName()
/*     */     {
/* 167 */       return "anvil";
/*     */     }
/*     */     
/*     */     public boolean hasCustomName()
/*     */     {
/* 172 */       return false;
/*     */     }
/*     */     
/*     */     public IChatComponent getDisplayName()
/*     */     {
/* 177 */       return new net.minecraft.util.ChatComponentTranslation(net.minecraft.init.Blocks.anvil.getUnlocalizedName() + ".name", new Object[0]);
/*     */     }
/*     */     
/*     */     public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
/*     */     {
/* 182 */       return new ContainerRepair(playerInventory, this.world, this.position, playerIn);
/*     */     }
/*     */     
/*     */     public String getGuiID()
/*     */     {
/* 187 */       return "minecraft:anvil";
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\BlockAnvil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */