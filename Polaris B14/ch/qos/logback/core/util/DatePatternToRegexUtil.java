/*    */ package ch.qos.logback.core.util;
/*    */ 
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DatePatternToRegexUtil
/*    */ {
/*    */   final String datePattern;
/*    */   final int datePatternLength;
/* 30 */   final CharSequenceToRegexMapper regexMapper = new CharSequenceToRegexMapper();
/*    */   
/*    */   public DatePatternToRegexUtil(String datePattern) {
/* 33 */     this.datePattern = datePattern;
/* 34 */     this.datePatternLength = datePattern.length();
/*    */   }
/*    */   
/*    */   public String toRegex() {
/* 38 */     List<CharSequenceState> charSequenceList = tokenize();
/* 39 */     StringBuilder sb = new StringBuilder();
/* 40 */     for (CharSequenceState seq : charSequenceList) {
/* 41 */       sb.append(this.regexMapper.toRegex(seq));
/*    */     }
/* 43 */     return sb.toString();
/*    */   }
/*    */   
/*    */   private List<CharSequenceState> tokenize() {
/* 47 */     List<CharSequenceState> sequenceList = new ArrayList();
/*    */     
/* 49 */     CharSequenceState lastCharSequenceState = null;
/*    */     
/* 51 */     for (int i = 0; i < this.datePatternLength; i++) {
/* 52 */       char t = this.datePattern.charAt(i);
/* 53 */       if ((lastCharSequenceState == null) || (lastCharSequenceState.c != t)) {
/* 54 */         lastCharSequenceState = new CharSequenceState(t);
/* 55 */         sequenceList.add(lastCharSequenceState);
/*    */       } else {
/* 57 */         lastCharSequenceState.incrementOccurrences();
/*    */       }
/*    */     }
/* 60 */     return sequenceList;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\util\DatePatternToRegexUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */