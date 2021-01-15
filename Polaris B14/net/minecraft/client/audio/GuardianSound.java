/*    */ package net.minecraft.client.audio;
/*    */ 
/*    */ import net.minecraft.entity.monster.EntityGuardian;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public class GuardianSound extends MovingSound
/*    */ {
/*    */   private final EntityGuardian guardian;
/*    */   
/*    */   public GuardianSound(EntityGuardian guardian)
/*    */   {
/* 12 */     super(new ResourceLocation("minecraft:mob.guardian.attack"));
/* 13 */     this.guardian = guardian;
/* 14 */     this.attenuationType = ISound.AttenuationType.NONE;
/* 15 */     this.repeat = true;
/* 16 */     this.repeatDelay = 0;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void update()
/*    */   {
/* 24 */     if ((!this.guardian.isDead) && (this.guardian.hasTargetedEntity()))
/*    */     {
/* 26 */       this.xPosF = ((float)this.guardian.posX);
/* 27 */       this.yPosF = ((float)this.guardian.posY);
/* 28 */       this.zPosF = ((float)this.guardian.posZ);
/* 29 */       float f = this.guardian.func_175477_p(0.0F);
/* 30 */       this.volume = (0.0F + 1.0F * f * f);
/* 31 */       this.pitch = (0.7F + 0.5F * f);
/*    */     }
/*    */     else
/*    */     {
/* 35 */       this.donePlaying = true;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\audio\GuardianSound.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */