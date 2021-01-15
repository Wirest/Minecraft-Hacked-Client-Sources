/*    */ package rip.jutting.polaris.event.events;
/*    */ 
/*    */ import rip.jutting.polaris.event.Event;
/*    */ 
/*    */ public class Event3D extends Event
/*    */ {
/*    */   public float particlTicks;
/*    */   
/*    */   public Event3D(float particlTicks)
/*    */   {
/* 11 */     this.particlTicks = particlTicks;
/*    */   }
/*    */   
/*    */   public float getParticalTicks()
/*    */   {
/* 16 */     return this.particlTicks;
/*    */   }
/*    */   
/*    */   public void setParticalTicks(float particlTicks)
/*    */   {
/* 21 */     this.particlTicks = particlTicks;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\event\events\Event3D.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */