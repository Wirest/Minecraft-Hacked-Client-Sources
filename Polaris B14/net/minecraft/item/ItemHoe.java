/*    */ package net.minecraft.item;
/*    */ 
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.block.Block.SoundType;
/*    */ import net.minecraft.block.BlockDirt.DirtType;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.creativetab.CreativeTabs;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.init.Blocks;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.EnumFacing;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class ItemHoe extends Item
/*    */ {
/*    */   protected Item.ToolMaterial theToolMaterial;
/*    */   
/*    */   public ItemHoe(Item.ToolMaterial material)
/*    */   {
/* 20 */     this.theToolMaterial = material;
/* 21 */     this.maxStackSize = 1;
/* 22 */     setMaxDamage(material.getMaxUses());
/* 23 */     setCreativeTab(CreativeTabs.tabTools);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
/*    */   {
/* 33 */     if (!playerIn.canPlayerEdit(pos.offset(side), side, stack))
/*    */     {
/* 35 */       return false;
/*    */     }
/*    */     
/*    */ 
/* 39 */     IBlockState iblockstate = worldIn.getBlockState(pos);
/* 40 */     Block block = iblockstate.getBlock();
/*    */     
/* 42 */     if ((side != EnumFacing.DOWN) && (worldIn.getBlockState(pos.up()).getBlock().getMaterial() == net.minecraft.block.material.Material.air))
/*    */     {
/* 44 */       if (block == Blocks.grass)
/*    */       {
/* 46 */         return useHoe(stack, playerIn, worldIn, pos, Blocks.farmland.getDefaultState());
/*    */       }
/*    */       
/* 49 */       if (block == Blocks.dirt)
/*    */       {
/* 51 */         switch ((BlockDirt.DirtType)iblockstate.getValue(net.minecraft.block.BlockDirt.VARIANT))
/*    */         {
/*    */         case COARSE_DIRT: 
/* 54 */           return useHoe(stack, playerIn, worldIn, pos, Blocks.farmland.getDefaultState());
/*    */         
/*    */         case DIRT: 
/* 57 */           return useHoe(stack, playerIn, worldIn, pos, Blocks.dirt.getDefaultState().withProperty(net.minecraft.block.BlockDirt.VARIANT, BlockDirt.DirtType.DIRT));
/*    */         }
/*    */         
/*    */       }
/*    */     }
/* 62 */     return false;
/*    */   }
/*    */   
/*    */ 
/*    */   protected boolean useHoe(ItemStack stack, EntityPlayer player, World worldIn, BlockPos target, IBlockState newState)
/*    */   {
/* 68 */     worldIn.playSoundEffect(target.getX() + 0.5F, target.getY() + 0.5F, target.getZ() + 0.5F, newState.getBlock().stepSound.getStepSound(), (newState.getBlock().stepSound.getVolume() + 1.0F) / 2.0F, newState.getBlock().stepSound.getFrequency() * 0.8F);
/*    */     
/* 70 */     if (worldIn.isRemote)
/*    */     {
/* 72 */       return true;
/*    */     }
/*    */     
/*    */ 
/* 76 */     worldIn.setBlockState(target, newState);
/* 77 */     stack.damageItem(1, player);
/* 78 */     return true;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean isFull3D()
/*    */   {
/* 87 */     return true;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getMaterialName()
/*    */   {
/* 96 */     return this.theToolMaterial.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\item\ItemHoe.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */