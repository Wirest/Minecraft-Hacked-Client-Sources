/*    */ package net.minecraft.entity.monster;
/*    */ 
/*    */ import net.minecraft.entity.SharedMonsterAttributes;
/*    */ import net.minecraft.entity.ai.attributes.IAttributeInstance;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class EntityGiantZombie extends EntityMob
/*    */ {
/*    */   public EntityGiantZombie(World worldIn)
/*    */   {
/* 11 */     super(worldIn);
/* 12 */     setSize(this.width * 6.0F, this.height * 6.0F);
/*    */   }
/*    */   
/*    */   public float getEyeHeight()
/*    */   {
/* 17 */     return 10.440001F;
/*    */   }
/*    */   
/*    */   protected void applyEntityAttributes()
/*    */   {
/* 22 */     super.applyEntityAttributes();
/* 23 */     getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(100.0D);
/* 24 */     getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.5D);
/* 25 */     getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(50.0D);
/*    */   }
/*    */   
/*    */   public float getBlockPathWeight(net.minecraft.util.BlockPos pos)
/*    */   {
/* 30 */     return this.worldObj.getLightBrightness(pos) - 0.5F;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\monster\EntityGiantZombie.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */