/*    */ package net.minecraft.client.audio;
/*    */ 
/*    */ import net.minecraft.entity.item.EntityMinecart;
/*    */ import net.minecraft.util.MathHelper;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public class MovingSoundMinecart extends MovingSound
/*    */ {
/*    */   private final EntityMinecart minecart;
/* 10 */   private float distance = 0.0F;
/*    */   
/*    */   public MovingSoundMinecart(EntityMinecart minecartIn)
/*    */   {
/* 14 */     super(new ResourceLocation("minecraft:minecart.base"));
/* 15 */     this.minecart = minecartIn;
/* 16 */     this.repeat = true;
/* 17 */     this.repeatDelay = 0;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void update()
/*    */   {
/* 25 */     if (this.minecart.isDead)
/*    */     {
/* 27 */       this.donePlaying = true;
/*    */     }
/*    */     else
/*    */     {
/* 31 */       this.xPosF = ((float)this.minecart.posX);
/* 32 */       this.yPosF = ((float)this.minecart.posY);
/* 33 */       this.zPosF = ((float)this.minecart.posZ);
/* 34 */       float f = MathHelper.sqrt_double(this.minecart.motionX * this.minecart.motionX + this.minecart.motionZ * this.minecart.motionZ);
/*    */       
/* 36 */       if (f >= 0.01D)
/*    */       {
/* 38 */         this.distance = MathHelper.clamp_float(this.distance + 0.0025F, 0.0F, 1.0F);
/* 39 */         this.volume = (0.0F + MathHelper.clamp_float(f, 0.0F, 0.5F) * 0.7F);
/*    */       }
/*    */       else
/*    */       {
/* 43 */         this.distance = 0.0F;
/* 44 */         this.volume = 0.0F;
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\audio\MovingSoundMinecart.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */