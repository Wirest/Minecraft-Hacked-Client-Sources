/*    */ package net.minecraft.entity.boss;
/*    */ 
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.IEntityMultiPart;
/*    */ import net.minecraft.nbt.NBTTagCompound;
/*    */ import net.minecraft.util.DamageSource;
/*    */ 
/*    */ public class EntityDragonPart
/*    */   extends Entity
/*    */ {
/*    */   public final IEntityMultiPart entityDragonObj;
/*    */   public final String partName;
/*    */   
/*    */   public EntityDragonPart(IEntityMultiPart parent, String partName, float base, float sizeHeight)
/*    */   {
/* 16 */     super(parent.getWorld());
/* 17 */     setSize(base, sizeHeight);
/* 18 */     this.entityDragonObj = parent;
/* 19 */     this.partName = partName;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected void entityInit() {}
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected void readEntityFromNBT(NBTTagCompound tagCompund) {}
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected void writeEntityToNBT(NBTTagCompound tagCompound) {}
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean canBeCollidedWith()
/*    */   {
/* 45 */     return true;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean attackEntityFrom(DamageSource source, float amount)
/*    */   {
/* 53 */     return isEntityInvulnerable(source) ? false : this.entityDragonObj.attackEntityFromPart(this, source, amount);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean isEntityEqual(Entity entityIn)
/*    */   {
/* 61 */     return (this == entityIn) || (this.entityDragonObj == entityIn);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\boss\EntityDragonPart.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */