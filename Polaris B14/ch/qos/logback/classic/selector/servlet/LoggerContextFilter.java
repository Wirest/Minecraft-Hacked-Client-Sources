/*    */ package ch.qos.logback.classic.selector.servlet;
/*    */ 
/*    */ import ch.qos.logback.classic.LoggerContext;
/*    */ import ch.qos.logback.classic.selector.ContextJNDISelector;
/*    */ import ch.qos.logback.classic.selector.ContextSelector;
/*    */ import ch.qos.logback.classic.util.ContextSelectorStaticBinder;
/*    */ import java.io.IOException;
/*    */ import javax.servlet.Filter;
/*    */ import javax.servlet.FilterChain;
/*    */ import javax.servlet.FilterConfig;
/*    */ import javax.servlet.ServletException;
/*    */ import javax.servlet.ServletRequest;
/*    */ import javax.servlet.ServletResponse;
/*    */ import org.slf4j.LoggerFactory;
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
/*    */ public class LoggerContextFilter
/*    */   implements Filter
/*    */ {
/*    */   public void destroy() {}
/*    */   
/*    */   public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
/*    */     throws IOException, ServletException
/*    */   {
/* 60 */     LoggerContext lc = (LoggerContext)LoggerFactory.getILoggerFactory();
/* 61 */     ContextSelector selector = ContextSelectorStaticBinder.getSingleton().getContextSelector();
/* 62 */     ContextJNDISelector sel = null;
/*    */     
/* 64 */     if ((selector instanceof ContextJNDISelector)) {
/* 65 */       sel = (ContextJNDISelector)selector;
/* 66 */       sel.setLocalContext(lc);
/*    */     }
/*    */     try
/*    */     {
/* 70 */       chain.doFilter(request, response);
/*    */     } finally {
/* 72 */       if (sel != null) {
/* 73 */         sel.removeLocalContext();
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   public void init(FilterConfig arg0)
/*    */     throws ServletException
/*    */   {}
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\selector\servlet\LoggerContextFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */