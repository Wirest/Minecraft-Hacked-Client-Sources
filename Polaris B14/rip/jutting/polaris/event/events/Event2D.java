/*    */ package rip.jutting.polaris.event.events;
/*    */ 
/*    */ import rip.jutting.polaris.event.Event;
/*    */ 
/*    */ public class Event2D extends Event {
/*    */   private float width;
/*    */   private float height;
/*    */   
/*  9 */   public Event2D(float width, float height) { this.width = width;
/* 10 */     this.height = height;
/*    */   }
/*    */   
/*    */   public float getWidth() {
/* 14 */     return this.width;
/*    */   }
/*    */   
/*    */   public float getHeight() {
/* 18 */     return this.height;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\event\events\Event2D.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */