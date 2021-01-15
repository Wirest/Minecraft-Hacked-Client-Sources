/*    */ package net.minecraft.client.audio;
/*    */ 
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public abstract class MovingSound extends PositionedSound implements ITickableSound
/*    */ {
/*  7 */   protected boolean donePlaying = false;
/*    */   
/*    */   protected MovingSound(ResourceLocation location)
/*    */   {
/* 11 */     super(location);
/*    */   }
/*    */   
/*    */   public boolean isDonePlaying()
/*    */   {
/* 16 */     return this.donePlaying;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\audio\MovingSound.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */