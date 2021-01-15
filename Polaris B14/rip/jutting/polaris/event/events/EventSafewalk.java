/*    */ package rip.jutting.polaris.event.events;
/*    */ 
/*    */ import rip.jutting.polaris.event.Event;
/*    */ 
/*    */ public class EventSafewalk extends Event
/*    */ {
/*    */   private boolean shouldWalkSafely;
/*    */   
/*    */   public EventSafewalk(boolean shouldWalkSafely) {
/* 10 */     this.shouldWalkSafely = shouldWalkSafely;
/*    */   }
/*    */   
/*    */   public boolean getShouldWalkSafely()
/*    */   {
/* 15 */     return this.shouldWalkSafely;
/*    */   }
/*    */   
/*    */ 
/*    */   public void setShouldWalkSafely(boolean shouldWalkSafely)
/*    */   {
/* 21 */     this.shouldWalkSafely = shouldWalkSafely;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\event\events\EventSafewalk.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */