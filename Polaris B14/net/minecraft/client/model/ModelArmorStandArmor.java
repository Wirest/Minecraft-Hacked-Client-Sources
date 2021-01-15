/*    */ package net.minecraft.client.model;
/*    */ 
/*    */ import net.minecraft.entity.item.EntityArmorStand;
/*    */ import net.minecraft.util.Rotations;
/*    */ 
/*    */ public class ModelArmorStandArmor extends ModelBiped
/*    */ {
/*    */   public ModelArmorStandArmor()
/*    */   {
/* 10 */     this(0.0F);
/*    */   }
/*    */   
/*    */   public ModelArmorStandArmor(float modelSize)
/*    */   {
/* 15 */     this(modelSize, 64, 32);
/*    */   }
/*    */   
/*    */   protected ModelArmorStandArmor(float modelSize, int textureWidthIn, int textureHeightIn)
/*    */   {
/* 20 */     super(modelSize, 0.0F, textureWidthIn, textureHeightIn);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, net.minecraft.entity.Entity entityIn)
/*    */   {
/* 30 */     if ((entityIn instanceof EntityArmorStand))
/*    */     {
/* 32 */       EntityArmorStand entityarmorstand = (EntityArmorStand)entityIn;
/* 33 */       this.bipedHead.rotateAngleX = (0.017453292F * entityarmorstand.getHeadRotation().getX());
/* 34 */       this.bipedHead.rotateAngleY = (0.017453292F * entityarmorstand.getHeadRotation().getY());
/* 35 */       this.bipedHead.rotateAngleZ = (0.017453292F * entityarmorstand.getHeadRotation().getZ());
/* 36 */       this.bipedHead.setRotationPoint(0.0F, 1.0F, 0.0F);
/* 37 */       this.bipedBody.rotateAngleX = (0.017453292F * entityarmorstand.getBodyRotation().getX());
/* 38 */       this.bipedBody.rotateAngleY = (0.017453292F * entityarmorstand.getBodyRotation().getY());
/* 39 */       this.bipedBody.rotateAngleZ = (0.017453292F * entityarmorstand.getBodyRotation().getZ());
/* 40 */       this.bipedLeftArm.rotateAngleX = (0.017453292F * entityarmorstand.getLeftArmRotation().getX());
/* 41 */       this.bipedLeftArm.rotateAngleY = (0.017453292F * entityarmorstand.getLeftArmRotation().getY());
/* 42 */       this.bipedLeftArm.rotateAngleZ = (0.017453292F * entityarmorstand.getLeftArmRotation().getZ());
/* 43 */       this.bipedRightArm.rotateAngleX = (0.017453292F * entityarmorstand.getRightArmRotation().getX());
/* 44 */       this.bipedRightArm.rotateAngleY = (0.017453292F * entityarmorstand.getRightArmRotation().getY());
/* 45 */       this.bipedRightArm.rotateAngleZ = (0.017453292F * entityarmorstand.getRightArmRotation().getZ());
/* 46 */       this.bipedLeftLeg.rotateAngleX = (0.017453292F * entityarmorstand.getLeftLegRotation().getX());
/* 47 */       this.bipedLeftLeg.rotateAngleY = (0.017453292F * entityarmorstand.getLeftLegRotation().getY());
/* 48 */       this.bipedLeftLeg.rotateAngleZ = (0.017453292F * entityarmorstand.getLeftLegRotation().getZ());
/* 49 */       this.bipedLeftLeg.setRotationPoint(1.9F, 11.0F, 0.0F);
/* 50 */       this.bipedRightLeg.rotateAngleX = (0.017453292F * entityarmorstand.getRightLegRotation().getX());
/* 51 */       this.bipedRightLeg.rotateAngleY = (0.017453292F * entityarmorstand.getRightLegRotation().getY());
/* 52 */       this.bipedRightLeg.rotateAngleZ = (0.017453292F * entityarmorstand.getRightLegRotation().getZ());
/* 53 */       this.bipedRightLeg.setRotationPoint(-1.9F, 11.0F, 0.0F);
/* 54 */       copyModelAngles(this.bipedHead, this.bipedHeadwear);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\model\ModelArmorStandArmor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */