/*    */ package net.minecraft.entity.monster;
/*    */ 
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.entity.IEntityLivingData;
/*    */ import net.minecraft.entity.SharedMonsterAttributes;
/*    */ import net.minecraft.potion.Potion;
/*    */ import net.minecraft.potion.PotionEffect;
/*    */ import net.minecraft.world.DifficultyInstance;
/*    */ import net.minecraft.world.EnumDifficulty;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class EntityCaveSpider extends EntitySpider
/*    */ {
/*    */   public EntityCaveSpider(World worldIn)
/*    */   {
/* 17 */     super(worldIn);
/* 18 */     setSize(0.7F, 0.5F);
/*    */   }
/*    */   
/*    */   protected void applyEntityAttributes()
/*    */   {
/* 23 */     super.applyEntityAttributes();
/* 24 */     getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(12.0D);
/*    */   }
/*    */   
/*    */   public boolean attackEntityAsMob(Entity entityIn)
/*    */   {
/* 29 */     if (super.attackEntityAsMob(entityIn))
/*    */     {
/* 31 */       if ((entityIn instanceof EntityLivingBase))
/*    */       {
/* 33 */         int i = 0;
/*    */         
/* 35 */         if (this.worldObj.getDifficulty() == EnumDifficulty.NORMAL)
/*    */         {
/* 37 */           i = 7;
/*    */         }
/* 39 */         else if (this.worldObj.getDifficulty() == EnumDifficulty.HARD)
/*    */         {
/* 41 */           i = 15;
/*    */         }
/*    */         
/* 44 */         if (i > 0)
/*    */         {
/* 46 */           ((EntityLivingBase)entityIn).addPotionEffect(new PotionEffect(Potion.poison.id, i * 20, 0));
/*    */         }
/*    */       }
/*    */       
/* 50 */       return true;
/*    */     }
/*    */     
/*    */ 
/* 54 */     return false;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata)
/*    */   {
/* 64 */     return livingdata;
/*    */   }
/*    */   
/*    */   public float getEyeHeight()
/*    */   {
/* 69 */     return 0.45F;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\monster\EntityCaveSpider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */