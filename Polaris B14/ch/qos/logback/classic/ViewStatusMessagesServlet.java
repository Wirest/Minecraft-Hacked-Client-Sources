/*    */ package ch.qos.logback.classic;
/*    */ 
/*    */ import ch.qos.logback.core.status.StatusManager;
/*    */ import ch.qos.logback.core.status.ViewStatusMessagesServletBase;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
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
/*    */ public class ViewStatusMessagesServlet
/*    */   extends ViewStatusMessagesServletBase
/*    */ {
/*    */   private static final long serialVersionUID = 443878494348593337L;
/*    */   
/*    */   protected StatusManager getStatusManager(HttpServletRequest req, HttpServletResponse resp)
/*    */   {
/* 31 */     LoggerContext lc = (LoggerContext)LoggerFactory.getILoggerFactory();
/* 32 */     return lc.getStatusManager();
/*    */   }
/*    */   
/*    */   protected String getPageTitle(HttpServletRequest req, HttpServletResponse resp)
/*    */   {
/* 37 */     LoggerContext lc = (LoggerContext)LoggerFactory.getILoggerFactory();
/* 38 */     return "<h2>Status messages for LoggerContext named [" + lc.getName() + "]</h2>\r\n";
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\ViewStatusMessagesServlet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */