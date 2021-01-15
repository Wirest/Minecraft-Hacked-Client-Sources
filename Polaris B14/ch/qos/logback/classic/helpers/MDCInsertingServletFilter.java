/*    */ package ch.qos.logback.classic.helpers;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import javax.servlet.Filter;
/*    */ import javax.servlet.FilterChain;
/*    */ import javax.servlet.FilterConfig;
/*    */ import javax.servlet.ServletException;
/*    */ import javax.servlet.ServletRequest;
/*    */ import javax.servlet.ServletResponse;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import org.slf4j.MDC;
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
/*    */ public class MDCInsertingServletFilter
/*    */   implements Filter
/*    */ {
/*    */   public void destroy() {}
/*    */   
/*    */   public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
/*    */     throws IOException, ServletException
/*    */   {
/* 49 */     insertIntoMDC(request);
/*    */     try {
/* 51 */       chain.doFilter(request, response);
/*    */     } finally {
/* 53 */       clearMDC();
/*    */     }
/*    */   }
/*    */   
/*    */   void insertIntoMDC(ServletRequest request)
/*    */   {
/* 59 */     MDC.put("req.remoteHost", request.getRemoteHost());
/*    */     
/*    */ 
/* 62 */     if ((request instanceof HttpServletRequest)) {
/* 63 */       HttpServletRequest httpServletRequest = (HttpServletRequest)request;
/* 64 */       MDC.put("req.requestURI", httpServletRequest.getRequestURI());
/*    */       
/* 66 */       StringBuffer requestURL = httpServletRequest.getRequestURL();
/* 67 */       if (requestURL != null) {
/* 68 */         MDC.put("req.requestURL", requestURL.toString());
/*    */       }
/* 70 */       MDC.put("req.method", httpServletRequest.getMethod());
/* 71 */       MDC.put("req.queryString", httpServletRequest.getQueryString());
/* 72 */       MDC.put("req.userAgent", httpServletRequest.getHeader("User-Agent"));
/*    */       
/* 74 */       MDC.put("req.xForwardedFor", httpServletRequest.getHeader("X-Forwarded-For"));
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */   void clearMDC()
/*    */   {
/* 81 */     MDC.remove("req.remoteHost");
/* 82 */     MDC.remove("req.requestURI");
/* 83 */     MDC.remove("req.queryString");
/*    */     
/* 85 */     MDC.remove("req.requestURL");
/* 86 */     MDC.remove("req.method");
/* 87 */     MDC.remove("req.userAgent");
/* 88 */     MDC.remove("req.xForwardedFor");
/*    */   }
/*    */   
/*    */   public void init(FilterConfig arg0)
/*    */     throws ServletException
/*    */   {}
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\helpers\MDCInsertingServletFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */