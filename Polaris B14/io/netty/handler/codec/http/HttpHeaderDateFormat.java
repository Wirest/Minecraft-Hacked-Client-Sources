/*    */ package io.netty.handler.codec.http;
/*    */ 
/*    */ import io.netty.util.concurrent.FastThreadLocal;
/*    */ import java.text.ParsePosition;
/*    */ import java.text.SimpleDateFormat;
/*    */ import java.util.Date;
/*    */ import java.util.Locale;
/*    */ import java.util.TimeZone;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class HttpHeaderDateFormat
/*    */   extends SimpleDateFormat
/*    */ {
/*    */   private static final long serialVersionUID = -925286159755905325L;
/* 39 */   private final SimpleDateFormat format1 = new HttpHeaderDateFormatObsolete1();
/* 40 */   private final SimpleDateFormat format2 = new HttpHeaderDateFormatObsolete2();
/*    */   
/* 42 */   private static final FastThreadLocal<HttpHeaderDateFormat> dateFormatThreadLocal = new FastThreadLocal()
/*    */   {
/*    */     protected HttpHeaderDateFormat initialValue()
/*    */     {
/* 46 */       return new HttpHeaderDateFormat(null);
/*    */     }
/*    */   };
/*    */   
/*    */   static HttpHeaderDateFormat get() {
/* 51 */     return (HttpHeaderDateFormat)dateFormatThreadLocal.get();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   private HttpHeaderDateFormat()
/*    */   {
/* 59 */     super("E, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
/* 60 */     setTimeZone(TimeZone.getTimeZone("GMT"));
/*    */   }
/*    */   
/*    */   public Date parse(String text, ParsePosition pos)
/*    */   {
/* 65 */     Date date = super.parse(text, pos);
/* 66 */     if (date == null) {
/* 67 */       date = this.format1.parse(text, pos);
/*    */     }
/* 69 */     if (date == null) {
/* 70 */       date = this.format2.parse(text, pos);
/*    */     }
/* 72 */     return date;
/*    */   }
/*    */   
/*    */ 
/*    */   private static final class HttpHeaderDateFormatObsolete1
/*    */     extends SimpleDateFormat
/*    */   {
/*    */     private static final long serialVersionUID = -3178072504225114298L;
/*    */     
/*    */     HttpHeaderDateFormatObsolete1()
/*    */     {
/* 83 */       super(Locale.ENGLISH);
/* 84 */       setTimeZone(TimeZone.getTimeZone("GMT"));
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */   private static final class HttpHeaderDateFormatObsolete2
/*    */     extends SimpleDateFormat
/*    */   {
/*    */     private static final long serialVersionUID = 3010674519968303714L;
/*    */     
/*    */ 
/*    */     HttpHeaderDateFormatObsolete2()
/*    */     {
/* 97 */       super(Locale.ENGLISH);
/* 98 */       setTimeZone(TimeZone.getTimeZone("GMT"));
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\HttpHeaderDateFormat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */