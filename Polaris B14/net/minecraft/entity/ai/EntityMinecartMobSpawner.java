/*    */ package net.minecraft.entity.ai;
/*    */ 
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.entity.item.EntityMinecart;
/*    */ import net.minecraft.entity.item.EntityMinecart.EnumMinecartType;
/*    */ import net.minecraft.nbt.NBTTagCompound;
/*    */ import net.minecraft.tileentity.MobSpawnerBaseLogic;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class EntityMinecartMobSpawner extends EntityMinecart
/*    */ {
/* 14 */   private final MobSpawnerBaseLogic mobSpawnerLogic = new MobSpawnerBaseLogic()
/*    */   {
/*    */     public void func_98267_a(int id)
/*    */     {
/* 18 */       EntityMinecartMobSpawner.this.worldObj.setEntityState(EntityMinecartMobSpawner.this, (byte)id);
/*    */     }
/*    */     
/*    */     public World getSpawnerWorld() {
/* 22 */       return EntityMinecartMobSpawner.this.worldObj;
/*    */     }
/*    */     
/*    */     public BlockPos getSpawnerPosition() {
/* 26 */       return new BlockPos(EntityMinecartMobSpawner.this);
/*    */     }
/*    */   };
/*    */   
/*    */   public EntityMinecartMobSpawner(World worldIn)
/*    */   {
/* 32 */     super(worldIn);
/*    */   }
/*    */   
/*    */   public EntityMinecartMobSpawner(World worldIn, double p_i1726_2_, double p_i1726_4_, double p_i1726_6_)
/*    */   {
/* 37 */     super(worldIn, p_i1726_2_, p_i1726_4_, p_i1726_6_);
/*    */   }
/*    */   
/*    */   public EntityMinecart.EnumMinecartType getMinecartType()
/*    */   {
/* 42 */     return EntityMinecart.EnumMinecartType.SPAWNER;
/*    */   }
/*    */   
/*    */   public IBlockState getDefaultDisplayTile()
/*    */   {
/* 47 */     return net.minecraft.init.Blocks.mob_spawner.getDefaultState();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected void readEntityFromNBT(NBTTagCompound tagCompund)
/*    */   {
/* 55 */     super.readEntityFromNBT(tagCompund);
/* 56 */     this.mobSpawnerLogic.readFromNBT(tagCompund);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected void writeEntityToNBT(NBTTagCompound tagCompound)
/*    */   {
/* 64 */     super.writeEntityToNBT(tagCompound);
/* 65 */     this.mobSpawnerLogic.writeToNBT(tagCompound);
/*    */   }
/*    */   
/*    */   public void handleStatusUpdate(byte id)
/*    */   {
/* 70 */     this.mobSpawnerLogic.setDelayToMin(id);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void onUpdate()
/*    */   {
/* 78 */     super.onUpdate();
/* 79 */     this.mobSpawnerLogic.updateSpawner();
/*    */   }
/*    */   
/*    */   public MobSpawnerBaseLogic func_98039_d()
/*    */   {
/* 84 */     return this.mobSpawnerLogic;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\ai\EntityMinecartMobSpawner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */