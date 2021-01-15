/*    */ package rip.jutting.polaris.event;
/*    */ 
/*    */ import java.lang.reflect.InvocationTargetException;
/*    */ import rip.jutting.polaris.Polaris;
/*    */ 
/*    */ public abstract class Event
/*    */ {
/*    */   private boolean cancelled;
/*    */   
/*    */   public static enum State
/*    */   {
/* 12 */     PRE("PRE", 0),  POST("POST", 1);
/*    */     
/*    */     private State(String string, int number) {}
/*    */   }
/*    */   
/*    */   public Event call() {
/* 18 */     this.cancelled = false;
/* 19 */     call(this);
/* 20 */     return this;
/*    */   }
/*    */   
/*    */   public boolean isCancelled() {
/* 24 */     return this.cancelled;
/*    */   }
/*    */   
/*    */   public void setCancelled(boolean cancelled)
/*    */   {
/* 29 */     this.cancelled = cancelled;
/*    */   }
/*    */   
/*    */   private static void call(Event event) {
/* 33 */     ArrayHelper<Data> dataList = Polaris.instance.eventManager.get(event.getClass());
/* 34 */     if (dataList != null) {
/* 35 */       for (Data data : dataList) {
/*    */         try {
/* 37 */           data.target.invoke(data.source, new Object[] { event });
/*    */         } catch (IllegalAccessException e) {
/* 39 */           e.printStackTrace();
/*    */         } catch (InvocationTargetException e) {
/* 41 */           e.printStackTrace();
/*    */         }
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\event\Event.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */