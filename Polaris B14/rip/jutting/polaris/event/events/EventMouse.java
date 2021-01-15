/*    */ package rip.jutting.polaris.event.events;
/*    */ 
/*    */ import rip.jutting.polaris.event.Event;
/*    */ 
/*    */ public class EventMouse extends Event
/*    */ {
/*    */   private int key;
/*    */   
/*    */   public EventMouse(int key) {
/* 10 */     this.key = key;
/*    */   }
/*    */   
/*    */   public int getKey() {
/* 14 */     return this.key;
/*    */   }
/*    */   
/*    */   public void setKey(int key) {
/* 18 */     this.key = key;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\event\events\EventMouse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */