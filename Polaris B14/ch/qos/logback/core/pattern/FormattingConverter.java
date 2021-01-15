/*    */ package ch.qos.logback.core.pattern;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class FormattingConverter<E>
/*    */   extends Converter<E>
/*    */ {
/*    */   static final int INITIAL_BUF_SIZE = 256;
/*    */   
/*    */ 
/*    */ 
/*    */   static final int MAX_CAPACITY = 1024;
/*    */   
/*    */ 
/*    */ 
/*    */   FormatInfo formattingInfo;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public final FormatInfo getFormattingInfo()
/*    */   {
/* 25 */     return this.formattingInfo;
/*    */   }
/*    */   
/*    */   public final void setFormattingInfo(FormatInfo formattingInfo) {
/* 29 */     if (this.formattingInfo != null) {
/* 30 */       throw new IllegalStateException("FormattingInfo has been already set");
/*    */     }
/* 32 */     this.formattingInfo = formattingInfo;
/*    */   }
/*    */   
/*    */   public final void write(StringBuilder buf, E event)
/*    */   {
/* 37 */     String s = convert(event);
/*    */     
/* 39 */     if (this.formattingInfo == null) {
/* 40 */       buf.append(s);
/* 41 */       return;
/*    */     }
/*    */     
/* 44 */     int min = this.formattingInfo.getMin();
/* 45 */     int max = this.formattingInfo.getMax();
/*    */     
/*    */ 
/* 48 */     if (s == null) {
/* 49 */       if (0 < min)
/* 50 */         SpacePadder.spacePad(buf, min);
/* 51 */       return;
/*    */     }
/*    */     
/* 54 */     int len = s.length();
/*    */     
/* 56 */     if (len > max) {
/* 57 */       if (this.formattingInfo.isLeftTruncate()) {
/* 58 */         buf.append(s.substring(len - max));
/*    */       } else {
/* 60 */         buf.append(s.substring(0, max));
/*    */       }
/* 62 */     } else if (len < min) {
/* 63 */       if (this.formattingInfo.isLeftPad()) {
/* 64 */         SpacePadder.leftPad(buf, s, min);
/*    */       } else {
/* 66 */         SpacePadder.rightPad(buf, s, min);
/*    */       }
/*    */     } else {
/* 69 */       buf.append(s);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\pattern\FormattingConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */