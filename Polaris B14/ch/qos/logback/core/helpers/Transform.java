/*     */ package ch.qos.logback.core.helpers;
/*     */ 
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Transform
/*     */ {
/*     */   private static final String CDATA_START = "<![CDATA[";
/*     */   private static final String CDATA_END = "]]>";
/*     */   private static final String CDATA_PSEUDO_END = "]]&gt;";
/*     */   private static final String CDATA_EMBEDED_END = "]]>]]&gt;<![CDATA[";
/*  30 */   private static final int CDATA_END_LEN = "]]>".length();
/*  31 */   private static final Pattern UNSAFE_XML_CHARS = Pattern.compile("[\000-\b\013\f\016-\037<>&'\"]");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String escapeTags(String input)
/*     */   {
/*  43 */     if ((input == null) || (input.length() == 0) || (!UNSAFE_XML_CHARS.matcher(input).find())) {
/*  44 */       return input;
/*     */     }
/*  46 */     StringBuffer buf = new StringBuffer(input);
/*  47 */     return escapeTags(buf);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String escapeTags(StringBuffer buf)
/*     */   {
/*  59 */     for (int i = 0; i < buf.length(); i++) {
/*  60 */       char ch = buf.charAt(i);
/*  61 */       switch (ch)
/*     */       {
/*     */       case '\t': 
/*     */       case '\n': 
/*     */       case '\r': 
/*     */         break;
/*     */       case '&': 
/*  68 */         buf.replace(i, i + 1, "&amp;");
/*  69 */         break;
/*     */       case '<': 
/*  71 */         buf.replace(i, i + 1, "&lt;");
/*  72 */         break;
/*     */       case '>': 
/*  74 */         buf.replace(i, i + 1, "&gt;");
/*  75 */         break;
/*     */       case '"': 
/*  77 */         buf.replace(i, i + 1, "&quot;");
/*  78 */         break;
/*     */       case '\'': 
/*  80 */         buf.replace(i, i + 1, "&#39;");
/*  81 */         break;
/*     */       default: 
/*  83 */         if (ch < ' ')
/*     */         {
/*     */ 
/*  86 */           buf.replace(i, i + 1, "�");
/*     */         }
/*     */         break;
/*     */       }
/*     */     }
/*  91 */     return buf.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void appendEscapingCDATA(StringBuilder output, String str)
/*     */   {
/* 107 */     if (str == null) {
/* 108 */       return;
/*     */     }
/*     */     
/* 111 */     int end = str.indexOf("]]>");
/*     */     
/* 113 */     if (end < 0) {
/* 114 */       output.append(str);
/*     */       
/* 116 */       return;
/*     */     }
/*     */     
/* 119 */     int start = 0;
/*     */     
/* 121 */     while (end > -1) {
/* 122 */       output.append(str.substring(start, end));
/* 123 */       output.append("]]>]]&gt;<![CDATA[");
/* 124 */       start = end + CDATA_END_LEN;
/*     */       
/* 126 */       if (start < str.length()) {
/* 127 */         end = str.indexOf("]]>", start);
/*     */       } else {
/* 129 */         return;
/*     */       }
/*     */     }
/*     */     
/* 133 */     output.append(str.substring(start));
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\helpers\Transform.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */