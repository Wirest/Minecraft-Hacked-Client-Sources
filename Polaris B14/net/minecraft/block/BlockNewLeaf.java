/*     */ package net.minecraft.block;
/*     */ 
/*     */ import com.google.common.base.Predicate;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.properties.PropertyEnum;
/*     */ import net.minecraft.block.state.BlockState;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.creativetab.CreativeTabs;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.tileentity.TileEntity;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class BlockNewLeaf extends BlockLeaves
/*     */ {
/*  21 */   public static final PropertyEnum<BlockPlanks.EnumType> VARIANT = PropertyEnum.create("variant", BlockPlanks.EnumType.class, new Predicate()
/*     */   {
/*     */     public boolean apply(BlockPlanks.EnumType p_apply_1_)
/*     */     {
/*  25 */       return p_apply_1_.getMetadata() >= 4;
/*     */     }
/*  21 */   });
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public BlockNewLeaf()
/*     */   {
/*  31 */     setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(CHECK_DECAY, Boolean.valueOf(true)).withProperty(DECAYABLE, Boolean.valueOf(true)));
/*     */   }
/*     */   
/*     */   protected void dropApple(World worldIn, BlockPos pos, IBlockState state, int chance)
/*     */   {
/*  36 */     if ((state.getValue(VARIANT) == BlockPlanks.EnumType.DARK_OAK) && (worldIn.rand.nextInt(chance) == 0))
/*     */     {
/*  38 */       spawnAsEntity(worldIn, pos, new ItemStack(Items.apple, 1, 0));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int damageDropped(IBlockState state)
/*     */   {
/*  48 */     return ((BlockPlanks.EnumType)state.getValue(VARIANT)).getMetadata();
/*     */   }
/*     */   
/*     */   public int getDamageValue(World worldIn, BlockPos pos)
/*     */   {
/*  53 */     IBlockState iblockstate = worldIn.getBlockState(pos);
/*  54 */     return iblockstate.getBlock().getMetaFromState(iblockstate) & 0x3;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list)
/*     */   {
/*  62 */     list.add(new ItemStack(itemIn, 1, 0));
/*  63 */     list.add(new ItemStack(itemIn, 1, 1));
/*     */   }
/*     */   
/*     */   protected ItemStack createStackedBlock(IBlockState state)
/*     */   {
/*  68 */     return new ItemStack(Item.getItemFromBlock(this), 1, ((BlockPlanks.EnumType)state.getValue(VARIANT)).getMetadata() - 4);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public IBlockState getStateFromMeta(int meta)
/*     */   {
/*  76 */     return getDefaultState().withProperty(VARIANT, getWoodType(meta)).withProperty(DECAYABLE, Boolean.valueOf((meta & 0x4) == 0)).withProperty(CHECK_DECAY, Boolean.valueOf((meta & 0x8) > 0));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMetaFromState(IBlockState state)
/*     */   {
/*  84 */     int i = 0;
/*  85 */     i |= ((BlockPlanks.EnumType)state.getValue(VARIANT)).getMetadata() - 4;
/*     */     
/*  87 */     if (!((Boolean)state.getValue(DECAYABLE)).booleanValue())
/*     */     {
/*  89 */       i |= 0x4;
/*     */     }
/*     */     
/*  92 */     if (((Boolean)state.getValue(CHECK_DECAY)).booleanValue())
/*     */     {
/*  94 */       i |= 0x8;
/*     */     }
/*     */     
/*  97 */     return i;
/*     */   }
/*     */   
/*     */   public BlockPlanks.EnumType getWoodType(int meta)
/*     */   {
/* 102 */     return BlockPlanks.EnumType.byMetadata((meta & 0x3) + 4);
/*     */   }
/*     */   
/*     */   protected BlockState createBlockState()
/*     */   {
/* 107 */     return new BlockState(this, new IProperty[] { VARIANT, CHECK_DECAY, DECAYABLE });
/*     */   }
/*     */   
/*     */   public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te)
/*     */   {
/* 112 */     if ((!worldIn.isRemote) && (player.getCurrentEquippedItem() != null) && (player.getCurrentEquippedItem().getItem() == Items.shears))
/*     */     {
/* 114 */       player.triggerAchievement(net.minecraft.stats.StatList.mineBlockStatArray[Block.getIdFromBlock(this)]);
/* 115 */       spawnAsEntity(worldIn, pos, new ItemStack(Item.getItemFromBlock(this), 1, ((BlockPlanks.EnumType)state.getValue(VARIANT)).getMetadata() - 4));
/*     */     }
/*     */     else
/*     */     {
/* 119 */       super.harvestBlock(worldIn, player, pos, state, te);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\BlockNewLeaf.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */