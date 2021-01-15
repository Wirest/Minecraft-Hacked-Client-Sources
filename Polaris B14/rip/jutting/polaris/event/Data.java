/*    */ package rip.jutting.polaris.event;
/*    */ 
/*    */ import java.lang.reflect.Method;
/*    */ 
/*    */ public class Data
/*    */ {
/*    */   public final Object source;
/*    */   public final Method target;
/*    */   public final byte priority;
/*    */   
/*    */   public Data(Object source, Method target, byte priority) {
/* 12 */     this.source = source;
/* 13 */     this.target = target;
/* 14 */     this.priority = priority;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\event\Data.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */