/*     */ package net.minecraft.block;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.material.MapColor;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.properties.PropertyBool;
/*     */ import net.minecraft.block.state.BlockState;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.command.server.CommandBlockLogic;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.tileentity.TileEntity;
/*     */ import net.minecraft.tileentity.TileEntityCommandBlock;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.world.GameRules;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class BlockCommandBlock extends BlockContainer
/*     */ {
/*  22 */   public static final PropertyBool TRIGGERED = PropertyBool.create("triggered");
/*     */   
/*     */   public BlockCommandBlock()
/*     */   {
/*  26 */     super(net.minecraft.block.material.Material.iron, MapColor.adobeColor);
/*  27 */     setDefaultState(this.blockState.getBaseState().withProperty(TRIGGERED, Boolean.valueOf(false)));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public TileEntity createNewTileEntity(World worldIn, int meta)
/*     */   {
/*  35 */     return new TileEntityCommandBlock();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
/*     */   {
/*  43 */     if (!worldIn.isRemote)
/*     */     {
/*  45 */       boolean flag = worldIn.isBlockPowered(pos);
/*  46 */       boolean flag1 = ((Boolean)state.getValue(TRIGGERED)).booleanValue();
/*     */       
/*  48 */       if ((flag) && (!flag1))
/*     */       {
/*  50 */         worldIn.setBlockState(pos, state.withProperty(TRIGGERED, Boolean.valueOf(true)), 4);
/*  51 */         worldIn.scheduleUpdate(pos, this, tickRate(worldIn));
/*     */       }
/*  53 */       else if ((!flag) && (flag1))
/*     */       {
/*  55 */         worldIn.setBlockState(pos, state.withProperty(TRIGGERED, Boolean.valueOf(false)), 4);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
/*     */   {
/*  62 */     TileEntity tileentity = worldIn.getTileEntity(pos);
/*     */     
/*  64 */     if ((tileentity instanceof TileEntityCommandBlock))
/*     */     {
/*  66 */       ((TileEntityCommandBlock)tileentity).getCommandBlockLogic().trigger(worldIn);
/*  67 */       worldIn.updateComparatorOutputLevel(pos, this);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int tickRate(World worldIn)
/*     */   {
/*  76 */     return 1;
/*     */   }
/*     */   
/*     */   public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
/*     */   {
/*  81 */     TileEntity tileentity = worldIn.getTileEntity(pos);
/*  82 */     return (tileentity instanceof TileEntityCommandBlock) ? ((TileEntityCommandBlock)tileentity).getCommandBlockLogic().tryOpenEditCommandBlock(playerIn) : false;
/*     */   }
/*     */   
/*     */   public boolean hasComparatorInputOverride()
/*     */   {
/*  87 */     return true;
/*     */   }
/*     */   
/*     */   public int getComparatorInputOverride(World worldIn, BlockPos pos)
/*     */   {
/*  92 */     TileEntity tileentity = worldIn.getTileEntity(pos);
/*  93 */     return (tileentity instanceof TileEntityCommandBlock) ? ((TileEntityCommandBlock)tileentity).getCommandBlockLogic().getSuccessCount() : 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
/*     */   {
/* 101 */     TileEntity tileentity = worldIn.getTileEntity(pos);
/*     */     
/* 103 */     if ((tileentity instanceof TileEntityCommandBlock))
/*     */     {
/* 105 */       CommandBlockLogic commandblocklogic = ((TileEntityCommandBlock)tileentity).getCommandBlockLogic();
/*     */       
/* 107 */       if (stack.hasDisplayName())
/*     */       {
/* 109 */         commandblocklogic.setName(stack.getDisplayName());
/*     */       }
/*     */       
/* 112 */       if (!worldIn.isRemote)
/*     */       {
/* 114 */         commandblocklogic.setTrackOutput(worldIn.getGameRules().getBoolean("sendCommandFeedback"));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int quantityDropped(Random random)
/*     */   {
/* 124 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getRenderType()
/*     */   {
/* 132 */     return 3;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public IBlockState getStateFromMeta(int meta)
/*     */   {
/* 140 */     return getDefaultState().withProperty(TRIGGERED, Boolean.valueOf((meta & 0x1) > 0));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMetaFromState(IBlockState state)
/*     */   {
/* 148 */     int i = 0;
/*     */     
/* 150 */     if (((Boolean)state.getValue(TRIGGERED)).booleanValue())
/*     */     {
/* 152 */       i |= 0x1;
/*     */     }
/*     */     
/* 155 */     return i;
/*     */   }
/*     */   
/*     */   protected BlockState createBlockState()
/*     */   {
/* 160 */     return new BlockState(this, new IProperty[] { TRIGGERED });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
/*     */   {
/* 169 */     return getDefaultState().withProperty(TRIGGERED, Boolean.valueOf(false));
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\BlockCommandBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */