/*     */ package net.minecraft.item;
/*     */ 
/*     */ import net.minecraft.block.BlockDispenser;
/*     */ import net.minecraft.block.BlockRailBase;
/*     */ import net.minecraft.block.BlockRailBase.EnumRailDirection;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.creativetab.CreativeTabs;
/*     */ import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
/*     */ import net.minecraft.dispenser.IBlockSource;
/*     */ import net.minecraft.entity.item.EntityMinecart;
/*     */ import net.minecraft.entity.item.EntityMinecart.EnumMinecartType;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class ItemMinecart extends Item
/*     */ {
/*  19 */   private static final net.minecraft.dispenser.IBehaviorDispenseItem dispenserMinecartBehavior = new BehaviorDefaultDispenseItem()
/*     */   {
/*  21 */     private final BehaviorDefaultDispenseItem behaviourDefaultDispenseItem = new BehaviorDefaultDispenseItem();
/*     */     
/*     */     public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
/*  24 */       EnumFacing enumfacing = BlockDispenser.getFacing(source.getBlockMetadata());
/*  25 */       World world = source.getWorld();
/*  26 */       double d0 = source.getX() + enumfacing.getFrontOffsetX() * 1.125D;
/*  27 */       double d1 = Math.floor(source.getY()) + enumfacing.getFrontOffsetY();
/*  28 */       double d2 = source.getZ() + enumfacing.getFrontOffsetZ() * 1.125D;
/*  29 */       BlockPos blockpos = source.getBlockPos().offset(enumfacing);
/*  30 */       IBlockState iblockstate = world.getBlockState(blockpos);
/*  31 */       BlockRailBase.EnumRailDirection blockrailbase$enumraildirection = (iblockstate.getBlock() instanceof BlockRailBase) ? (BlockRailBase.EnumRailDirection)iblockstate.getValue(((BlockRailBase)iblockstate.getBlock()).getShapeProperty()) : BlockRailBase.EnumRailDirection.NORTH_SOUTH;
/*     */       double d3;
/*     */       double d3;
/*  34 */       if (BlockRailBase.isRailBlock(iblockstate)) {
/*     */         double d3;
/*  36 */         if (blockrailbase$enumraildirection.isAscending())
/*     */         {
/*  38 */           d3 = 0.6D;
/*     */         }
/*     */         else
/*     */         {
/*  42 */           d3 = 0.1D;
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/*  47 */         if ((iblockstate.getBlock().getMaterial() != net.minecraft.block.material.Material.air) || (!BlockRailBase.isRailBlock(world.getBlockState(blockpos.down()))))
/*     */         {
/*  49 */           return this.behaviourDefaultDispenseItem.dispense(source, stack);
/*     */         }
/*     */         
/*  52 */         IBlockState iblockstate1 = world.getBlockState(blockpos.down());
/*  53 */         BlockRailBase.EnumRailDirection blockrailbase$enumraildirection1 = (iblockstate1.getBlock() instanceof BlockRailBase) ? (BlockRailBase.EnumRailDirection)iblockstate1.getValue(((BlockRailBase)iblockstate1.getBlock()).getShapeProperty()) : BlockRailBase.EnumRailDirection.NORTH_SOUTH;
/*     */         double d3;
/*  55 */         if ((enumfacing != EnumFacing.DOWN) && (blockrailbase$enumraildirection1.isAscending()))
/*     */         {
/*  57 */           d3 = -0.4D;
/*     */         }
/*     */         else
/*     */         {
/*  61 */           d3 = -0.9D;
/*     */         }
/*     */       }
/*     */       
/*  65 */       EntityMinecart entityminecart = EntityMinecart.func_180458_a(world, d0, d1 + d3, d2, ((ItemMinecart)stack.getItem()).minecartType);
/*     */       
/*  67 */       if (stack.hasDisplayName())
/*     */       {
/*  69 */         entityminecart.setCustomNameTag(stack.getDisplayName());
/*     */       }
/*     */       
/*  72 */       world.spawnEntityInWorld(entityminecart);
/*  73 */       stack.splitStack(1);
/*  74 */       return stack;
/*     */     }
/*     */     
/*     */     protected void playDispenseSound(IBlockSource source) {
/*  78 */       source.getWorld().playAuxSFX(1000, source.getBlockPos(), 0);
/*     */     }
/*     */   };
/*     */   private final EntityMinecart.EnumMinecartType minecartType;
/*     */   
/*     */   public ItemMinecart(EntityMinecart.EnumMinecartType type)
/*     */   {
/*  85 */     this.maxStackSize = 1;
/*  86 */     this.minecartType = type;
/*  87 */     setCreativeTab(CreativeTabs.tabTransport);
/*  88 */     BlockDispenser.dispenseBehaviorRegistry.putObject(this, dispenserMinecartBehavior);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
/*     */   {
/*  96 */     IBlockState iblockstate = worldIn.getBlockState(pos);
/*     */     
/*  98 */     if (BlockRailBase.isRailBlock(iblockstate))
/*     */     {
/* 100 */       if (!worldIn.isRemote)
/*     */       {
/* 102 */         BlockRailBase.EnumRailDirection blockrailbase$enumraildirection = (iblockstate.getBlock() instanceof BlockRailBase) ? (BlockRailBase.EnumRailDirection)iblockstate.getValue(((BlockRailBase)iblockstate.getBlock()).getShapeProperty()) : BlockRailBase.EnumRailDirection.NORTH_SOUTH;
/* 103 */         double d0 = 0.0D;
/*     */         
/* 105 */         if (blockrailbase$enumraildirection.isAscending())
/*     */         {
/* 107 */           d0 = 0.5D;
/*     */         }
/*     */         
/* 110 */         EntityMinecart entityminecart = EntityMinecart.func_180458_a(worldIn, pos.getX() + 0.5D, pos.getY() + 0.0625D + d0, pos.getZ() + 0.5D, this.minecartType);
/*     */         
/* 112 */         if (stack.hasDisplayName())
/*     */         {
/* 114 */           entityminecart.setCustomNameTag(stack.getDisplayName());
/*     */         }
/*     */         
/* 117 */         worldIn.spawnEntityInWorld(entityminecart);
/*     */       }
/*     */       
/* 120 */       stack.stackSize -= 1;
/* 121 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 125 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\item\ItemMinecart.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */