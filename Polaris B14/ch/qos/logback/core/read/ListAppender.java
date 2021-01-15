/*    */ package ch.qos.logback.core.read;
/*    */ 
/*    */ import ch.qos.logback.core.AppenderBase;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ListAppender<E>
/*    */   extends AppenderBase<E>
/*    */ {
/* 23 */   public List<E> list = new ArrayList();
/*    */   
/*    */   protected void append(E e) {
/* 26 */     this.list.add(e);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\read\ListAppender.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */