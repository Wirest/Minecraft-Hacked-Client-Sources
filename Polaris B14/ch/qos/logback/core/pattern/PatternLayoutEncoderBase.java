/*    */ package ch.qos.logback.core.pattern;
/*    */ 
/*    */ import ch.qos.logback.core.Layout;
/*    */ import ch.qos.logback.core.encoder.LayoutWrappingEncoder;
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
/*    */ 
/*    */ public class PatternLayoutEncoderBase<E>
/*    */   extends LayoutWrappingEncoder<E>
/*    */ {
/*    */   String pattern;
/* 24 */   protected boolean outputPatternAsHeader = false;
/*    */   
/*    */   public String getPattern() {
/* 27 */     return this.pattern;
/*    */   }
/*    */   
/*    */   public void setPattern(String pattern) {
/* 31 */     this.pattern = pattern;
/*    */   }
/*    */   
/*    */   public boolean isOutputPatternAsHeader() {
/* 35 */     return this.outputPatternAsHeader;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setOutputPatternAsHeader(boolean outputPatternAsHeader)
/*    */   {
/* 46 */     this.outputPatternAsHeader = outputPatternAsHeader;
/*    */   }
/*    */   
/*    */   public boolean isOutputPatternAsPresentationHeader()
/*    */   {
/* 51 */     return this.outputPatternAsHeader;
/*    */   }
/*    */   
/*    */   /**
/*    */    * @deprecated
/*    */    */
/*    */   public void setOutputPatternAsPresentationHeader(boolean outputPatternAsHeader) {
/* 58 */     addWarn("[outputPatternAsPresentationHeader] property is deprecated. Please use [outputPatternAsHeader] option instead.");
/* 59 */     this.outputPatternAsHeader = outputPatternAsHeader;
/*    */   }
/*    */   
/*    */   public void setLayout(Layout<E> layout)
/*    */   {
/* 64 */     throw new UnsupportedOperationException("one cannot set the layout of " + getClass().getName());
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\pattern\PatternLayoutEncoderBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */