/*     */ package net.minecraft.item;
/*     */ 
/*     */ import net.minecraft.block.Block.SoundType;
/*     */ import net.minecraft.block.BlockSlab;
/*     */ import net.minecraft.block.BlockSlab.EnumBlockHalf;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class ItemSlab extends ItemBlock
/*     */ {
/*     */   private final BlockSlab singleSlab;
/*     */   private final BlockSlab doubleSlab;
/*     */   
/*     */   public ItemSlab(net.minecraft.block.Block block, BlockSlab singleSlab, BlockSlab doubleSlab)
/*     */   {
/*  19 */     super(block);
/*  20 */     this.singleSlab = singleSlab;
/*  21 */     this.doubleSlab = doubleSlab;
/*  22 */     setMaxDamage(0);
/*  23 */     setHasSubtypes(true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMetadata(int damage)
/*     */   {
/*  32 */     return damage;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getUnlocalizedName(ItemStack stack)
/*     */   {
/*  41 */     return this.singleSlab.getUnlocalizedName(stack.getMetadata());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
/*     */   {
/*  49 */     if (stack.stackSize == 0)
/*     */     {
/*  51 */       return false;
/*     */     }
/*  53 */     if (!playerIn.canPlayerEdit(pos.offset(side), side, stack))
/*     */     {
/*  55 */       return false;
/*     */     }
/*     */     
/*     */ 
/*  59 */     Object object = this.singleSlab.getVariant(stack);
/*  60 */     IBlockState iblockstate = worldIn.getBlockState(pos);
/*     */     
/*  62 */     if (iblockstate.getBlock() == this.singleSlab)
/*     */     {
/*  64 */       net.minecraft.block.properties.IProperty iproperty = this.singleSlab.getVariantProperty();
/*  65 */       Comparable comparable = iblockstate.getValue(iproperty);
/*  66 */       BlockSlab.EnumBlockHalf blockslab$enumblockhalf = (BlockSlab.EnumBlockHalf)iblockstate.getValue(BlockSlab.HALF);
/*     */       
/*  68 */       if (((side == EnumFacing.UP) && (blockslab$enumblockhalf == BlockSlab.EnumBlockHalf.BOTTOM)) || ((side == EnumFacing.DOWN) && (blockslab$enumblockhalf == BlockSlab.EnumBlockHalf.TOP) && (comparable == object)))
/*     */       {
/*  70 */         IBlockState iblockstate1 = this.doubleSlab.getDefaultState().withProperty(iproperty, comparable);
/*     */         
/*  72 */         if ((worldIn.checkNoEntityCollision(this.doubleSlab.getCollisionBoundingBox(worldIn, pos, iblockstate1))) && (worldIn.setBlockState(pos, iblockstate1, 3)))
/*     */         {
/*  74 */           worldIn.playSoundEffect(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, this.doubleSlab.stepSound.getPlaceSound(), (this.doubleSlab.stepSound.getVolume() + 1.0F) / 2.0F, this.doubleSlab.stepSound.getFrequency() * 0.8F);
/*  75 */           stack.stackSize -= 1;
/*     */         }
/*     */         
/*  78 */         return true;
/*     */       }
/*     */     }
/*     */     
/*  82 */     return tryPlace(stack, worldIn, pos.offset(side), object) ? true : super.onItemUse(stack, playerIn, worldIn, pos, side, hitX, hitY, hitZ);
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side, EntityPlayer player, ItemStack stack)
/*     */   {
/*  88 */     BlockPos blockpos = pos;
/*  89 */     net.minecraft.block.properties.IProperty iproperty = this.singleSlab.getVariantProperty();
/*  90 */     Object object = this.singleSlab.getVariant(stack);
/*  91 */     IBlockState iblockstate = worldIn.getBlockState(pos);
/*     */     
/*  93 */     if (iblockstate.getBlock() == this.singleSlab)
/*     */     {
/*  95 */       boolean flag = iblockstate.getValue(BlockSlab.HALF) == BlockSlab.EnumBlockHalf.TOP;
/*     */       
/*  97 */       if (((side == EnumFacing.UP) && (!flag)) || ((side == EnumFacing.DOWN) && (flag) && (object == iblockstate.getValue(iproperty))))
/*     */       {
/*  99 */         return true;
/*     */       }
/*     */     }
/*     */     
/* 103 */     pos = pos.offset(side);
/* 104 */     IBlockState iblockstate1 = worldIn.getBlockState(pos);
/* 105 */     return (iblockstate1.getBlock() == this.singleSlab) && (object == iblockstate1.getValue(iproperty)) ? true : super.canPlaceBlockOnSide(worldIn, blockpos, side, player, stack);
/*     */   }
/*     */   
/*     */   private boolean tryPlace(ItemStack stack, World worldIn, BlockPos pos, Object variantInStack)
/*     */   {
/* 110 */     IBlockState iblockstate = worldIn.getBlockState(pos);
/*     */     
/* 112 */     if (iblockstate.getBlock() == this.singleSlab)
/*     */     {
/* 114 */       Comparable comparable = iblockstate.getValue(this.singleSlab.getVariantProperty());
/*     */       
/* 116 */       if (comparable == variantInStack)
/*     */       {
/* 118 */         IBlockState iblockstate1 = this.doubleSlab.getDefaultState().withProperty(this.singleSlab.getVariantProperty(), comparable);
/*     */         
/* 120 */         if ((worldIn.checkNoEntityCollision(this.doubleSlab.getCollisionBoundingBox(worldIn, pos, iblockstate1))) && (worldIn.setBlockState(pos, iblockstate1, 3)))
/*     */         {
/* 122 */           worldIn.playSoundEffect(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, this.doubleSlab.stepSound.getPlaceSound(), (this.doubleSlab.stepSound.getVolume() + 1.0F) / 2.0F, this.doubleSlab.stepSound.getFrequency() * 0.8F);
/* 123 */           stack.stackSize -= 1;
/*     */         }
/*     */         
/* 126 */         return true;
/*     */       }
/*     */     }
/*     */     
/* 130 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\item\ItemSlab.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */