/*    */ package ch.qos.logback.core.sift;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DefaultDiscriminator<E>
/*    */   extends AbstractDiscriminator<E>
/*    */ {
/*    */   public static final String DEFAULT = "default";
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getDiscriminatingValue(E e)
/*    */   {
/* 25 */     return "default";
/*    */   }
/*    */   
/*    */   public String getKey() {
/* 29 */     return "default";
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\sift\DefaultDiscriminator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */