/*     */ package net.minecraft.item;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.creativetab.CreativeTabs;
/*     */ import net.minecraft.entity.item.EntityArmorStand;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.Rotations;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class ItemArmorStand extends Item
/*     */ {
/*     */   public ItemArmorStand()
/*     */   {
/*  21 */     setCreativeTab(CreativeTabs.tabDecorations);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
/*     */   {
/*  29 */     if (side == EnumFacing.DOWN)
/*     */     {
/*  31 */       return false;
/*     */     }
/*     */     
/*     */ 
/*  35 */     boolean flag = worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos);
/*  36 */     BlockPos blockpos = flag ? pos : pos.offset(side);
/*     */     
/*  38 */     if (!playerIn.canPlayerEdit(blockpos, side, stack))
/*     */     {
/*  40 */       return false;
/*     */     }
/*     */     
/*     */ 
/*  44 */     BlockPos blockpos1 = blockpos.up();
/*  45 */     boolean flag1 = (!worldIn.isAirBlock(blockpos)) && (!worldIn.getBlockState(blockpos).getBlock().isReplaceable(worldIn, blockpos));
/*  46 */     flag1 |= ((!worldIn.isAirBlock(blockpos1)) && (!worldIn.getBlockState(blockpos1).getBlock().isReplaceable(worldIn, blockpos1)));
/*     */     
/*  48 */     if (flag1)
/*     */     {
/*  50 */       return false;
/*     */     }
/*     */     
/*     */ 
/*  54 */     double d0 = blockpos.getX();
/*  55 */     double d1 = blockpos.getY();
/*  56 */     double d2 = blockpos.getZ();
/*  57 */     List<net.minecraft.entity.Entity> list = worldIn.getEntitiesWithinAABBExcludingEntity(null, net.minecraft.util.AxisAlignedBB.fromBounds(d0, d1, d2, d0 + 1.0D, d1 + 2.0D, d2 + 1.0D));
/*     */     
/*  59 */     if (list.size() > 0)
/*     */     {
/*  61 */       return false;
/*     */     }
/*     */     
/*     */ 
/*  65 */     if (!worldIn.isRemote)
/*     */     {
/*  67 */       worldIn.setBlockToAir(blockpos);
/*  68 */       worldIn.setBlockToAir(blockpos1);
/*  69 */       EntityArmorStand entityarmorstand = new EntityArmorStand(worldIn, d0 + 0.5D, d1, d2 + 0.5D);
/*  70 */       float f = MathHelper.floor_float((MathHelper.wrapAngleTo180_float(playerIn.rotationYaw - 180.0F) + 22.5F) / 45.0F) * 45.0F;
/*  71 */       entityarmorstand.setLocationAndAngles(d0 + 0.5D, d1, d2 + 0.5D, f, 0.0F);
/*  72 */       applyRandomRotations(entityarmorstand, worldIn.rand);
/*  73 */       NBTTagCompound nbttagcompound = stack.getTagCompound();
/*     */       
/*  75 */       if ((nbttagcompound != null) && (nbttagcompound.hasKey("EntityTag", 10)))
/*     */       {
/*  77 */         NBTTagCompound nbttagcompound1 = new NBTTagCompound();
/*  78 */         entityarmorstand.writeToNBTOptional(nbttagcompound1);
/*  79 */         nbttagcompound1.merge(nbttagcompound.getCompoundTag("EntityTag"));
/*  80 */         entityarmorstand.readFromNBT(nbttagcompound1);
/*     */       }
/*     */       
/*  83 */       worldIn.spawnEntityInWorld(entityarmorstand);
/*     */     }
/*     */     
/*  86 */     stack.stackSize -= 1;
/*  87 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void applyRandomRotations(EntityArmorStand armorStand, Random rand)
/*     */   {
/*  96 */     Rotations rotations = armorStand.getHeadRotation();
/*  97 */     float f = rand.nextFloat() * 5.0F;
/*  98 */     float f1 = rand.nextFloat() * 20.0F - 10.0F;
/*  99 */     Rotations rotations1 = new Rotations(rotations.getX() + f, rotations.getY() + f1, rotations.getZ());
/* 100 */     armorStand.setHeadRotation(rotations1);
/* 101 */     rotations = armorStand.getBodyRotation();
/* 102 */     f = rand.nextFloat() * 10.0F - 5.0F;
/* 103 */     rotations1 = new Rotations(rotations.getX(), rotations.getY() + f, rotations.getZ());
/* 104 */     armorStand.setBodyRotation(rotations1);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\item\ItemArmorStand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */