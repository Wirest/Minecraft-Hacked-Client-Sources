/*     */ package net.minecraft.item;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.creativetab.CreativeTabs;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.item.EntityBoat;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.PlayerCapabilities;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.MovingObjectPosition;
/*     */ import net.minecraft.util.Vec3;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class ItemBoat extends Item
/*     */ {
/*     */   public ItemBoat()
/*     */   {
/*  21 */     this.maxStackSize = 1;
/*  22 */     setCreativeTab(CreativeTabs.tabTransport);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn)
/*     */   {
/*  30 */     float f = 1.0F;
/*  31 */     float f1 = playerIn.prevRotationPitch + (playerIn.rotationPitch - playerIn.prevRotationPitch) * f;
/*  32 */     float f2 = playerIn.prevRotationYaw + (playerIn.rotationYaw - playerIn.prevRotationYaw) * f;
/*  33 */     double d0 = playerIn.prevPosX + (playerIn.posX - playerIn.prevPosX) * f;
/*  34 */     double d1 = playerIn.prevPosY + (playerIn.posY - playerIn.prevPosY) * f + playerIn.getEyeHeight();
/*  35 */     double d2 = playerIn.prevPosZ + (playerIn.posZ - playerIn.prevPosZ) * f;
/*  36 */     Vec3 vec3 = new Vec3(d0, d1, d2);
/*  37 */     float f3 = MathHelper.cos(-f2 * 0.017453292F - 3.1415927F);
/*  38 */     float f4 = MathHelper.sin(-f2 * 0.017453292F - 3.1415927F);
/*  39 */     float f5 = -MathHelper.cos(-f1 * 0.017453292F);
/*  40 */     float f6 = MathHelper.sin(-f1 * 0.017453292F);
/*  41 */     float f7 = f4 * f5;
/*  42 */     float f8 = f3 * f5;
/*  43 */     double d3 = 5.0D;
/*  44 */     Vec3 vec31 = vec3.addVector(f7 * d3, f6 * d3, f8 * d3);
/*  45 */     MovingObjectPosition movingobjectposition = worldIn.rayTraceBlocks(vec3, vec31, true);
/*     */     
/*  47 */     if (movingobjectposition == null)
/*     */     {
/*  49 */       return itemStackIn;
/*     */     }
/*     */     
/*     */ 
/*  53 */     Vec3 vec32 = playerIn.getLook(f);
/*  54 */     boolean flag = false;
/*  55 */     float f9 = 1.0F;
/*  56 */     List<Entity> list = worldIn.getEntitiesWithinAABBExcludingEntity(playerIn, playerIn.getEntityBoundingBox().addCoord(vec32.xCoord * d3, vec32.yCoord * d3, vec32.zCoord * d3).expand(f9, f9, f9));
/*     */     
/*  58 */     for (int i = 0; i < list.size(); i++)
/*     */     {
/*  60 */       Entity entity = (Entity)list.get(i);
/*     */       
/*  62 */       if (entity.canBeCollidedWith())
/*     */       {
/*  64 */         float f10 = entity.getCollisionBorderSize();
/*  65 */         AxisAlignedBB axisalignedbb = entity.getEntityBoundingBox().expand(f10, f10, f10);
/*     */         
/*  67 */         if (axisalignedbb.isVecInside(vec3))
/*     */         {
/*  69 */           flag = true;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*  74 */     if (flag)
/*     */     {
/*  76 */       return itemStackIn;
/*     */     }
/*     */     
/*     */ 
/*  80 */     if (movingobjectposition.typeOfHit == net.minecraft.util.MovingObjectPosition.MovingObjectType.BLOCK)
/*     */     {
/*  82 */       BlockPos blockpos = movingobjectposition.getBlockPos();
/*     */       
/*  84 */       if (worldIn.getBlockState(blockpos).getBlock() == net.minecraft.init.Blocks.snow_layer)
/*     */       {
/*  86 */         blockpos = blockpos.down();
/*     */       }
/*     */       
/*  89 */       EntityBoat entityboat = new EntityBoat(worldIn, blockpos.getX() + 0.5F, blockpos.getY() + 1.0F, blockpos.getZ() + 0.5F);
/*  90 */       entityboat.rotationYaw = (((MathHelper.floor_double(playerIn.rotationYaw * 4.0F / 360.0F + 0.5D) & 0x3) - 1) * 90);
/*     */       
/*  92 */       if (!worldIn.getCollidingBoundingBoxes(entityboat, entityboat.getEntityBoundingBox().expand(-0.1D, -0.1D, -0.1D)).isEmpty())
/*     */       {
/*  94 */         return itemStackIn;
/*     */       }
/*     */       
/*  97 */       if (!worldIn.isRemote)
/*     */       {
/*  99 */         worldIn.spawnEntityInWorld(entityboat);
/*     */       }
/*     */       
/* 102 */       if (!playerIn.capabilities.isCreativeMode)
/*     */       {
/* 104 */         itemStackIn.stackSize -= 1;
/*     */       }
/*     */       
/* 107 */       playerIn.triggerAchievement(net.minecraft.stats.StatList.objectUseStats[Item.getIdFromItem(this)]);
/*     */     }
/*     */     
/* 110 */     return itemStackIn;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\item\ItemBoat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */