/*     */ package ch.qos.logback.core.status;
/*     */ 
/*     */ import ch.qos.logback.core.CoreConstants;
/*     */ import ch.qos.logback.core.helpers.Transform;
/*     */ import ch.qos.logback.core.util.CachingDateFormatter;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.StringWriter;
/*     */ import java.util.List;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.http.HttpServlet;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
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
/*     */ public abstract class ViewStatusMessagesServletBase
/*     */   extends HttpServlet
/*     */ {
/*     */   private static final long serialVersionUID = -3551928133801157219L;
/*  35 */   private static CachingDateFormatter SDF = new CachingDateFormatter("yyyy-MM-dd HH:mm:ss");
/*     */   
/*     */ 
/*  38 */   static String SUBMIT = "submit";
/*  39 */   static String CLEAR = "Clear";
/*     */   
/*     */   int count;
/*     */   
/*     */   protected abstract StatusManager getStatusManager(HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse);
/*     */   
/*     */   protected abstract String getPageTitle(HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse);
/*     */   
/*     */   protected void service(HttpServletRequest req, HttpServletResponse resp)
/*     */     throws ServletException, IOException
/*     */   {
/*  50 */     this.count = 0;
/*  51 */     StatusManager sm = getStatusManager(req, resp);
/*     */     
/*  53 */     resp.setContentType("text/html");
/*  54 */     PrintWriter output = resp.getWriter();
/*     */     
/*  56 */     output.append("<html>\r\n");
/*  57 */     output.append("<head>\r\n");
/*  58 */     printCSS(req.getContextPath(), output);
/*  59 */     output.append("</head>\r\n");
/*  60 */     output.append("<body>\r\n");
/*  61 */     output.append(getPageTitle(req, resp));
/*     */     
/*     */ 
/*  64 */     output.append("<form method=\"POST\">\r\n");
/*  65 */     output.append("<input type=\"submit\" name=\"" + SUBMIT + "\" value=\"" + CLEAR + "\">");
/*  66 */     output.append("</form>\r\n");
/*     */     
/*     */ 
/*  69 */     if (CLEAR.equalsIgnoreCase(req.getParameter(SUBMIT))) {
/*  70 */       sm.clear();
/*  71 */       sm.add(new InfoStatus("Cleared all status messages", this));
/*     */     }
/*     */     
/*  74 */     output.append("<table>");
/*  75 */     StringBuilder buf = new StringBuilder();
/*  76 */     if (sm != null) {
/*  77 */       printList(buf, sm);
/*     */     } else {
/*  79 */       output.append("Could not find status manager");
/*     */     }
/*  81 */     output.append(buf);
/*  82 */     output.append("</table>");
/*  83 */     output.append("</body>\r\n");
/*  84 */     output.append("</html>\r\n");
/*  85 */     output.flush();
/*  86 */     output.close();
/*     */   }
/*     */   
/*     */   public void printCSS(String localRef, PrintWriter output) {
/*  90 */     output.append("  <STYLE TYPE=\"text/css\">\r\n");
/*  91 */     output.append("    .warn  { font-weight: bold; color: #FF6600;} \r\n");
/*  92 */     output.append("    .error { font-weight: bold; color: #CC0000;} \r\n");
/*  93 */     output.append("    table { margin-left: 2em; margin-right: 2em; border-left: 2px solid #AAA; }\r\n");
/*     */     
/*  95 */     output.append("    tr.even { background: #FFFFFF; }\r\n");
/*  96 */     output.append("    tr.odd  { background: #EAEAEA; }\r\n");
/*  97 */     output.append("    td { padding-right: 1ex; padding-left: 1ex; border-right: 2px solid #AAA; }\r\n");
/*     */     
/*  99 */     output.append("    td.date { text-align: right; font-family: courier, monospace; font-size: smaller; }");
/*     */     
/* 101 */     output.append(CoreConstants.LINE_SEPARATOR);
/*     */     
/* 103 */     output.append("  td.level { text-align: right; }");
/* 104 */     output.append(CoreConstants.LINE_SEPARATOR);
/* 105 */     output.append("    tr.header { background: #596ED5; color: #FFF; font-weight: bold; font-size: larger; }");
/*     */     
/* 107 */     output.append(CoreConstants.LINE_SEPARATOR);
/*     */     
/* 109 */     output.append("  td.exception { background: #A2AEE8; white-space: pre; font-family: courier, monospace;}");
/*     */     
/* 111 */     output.append(CoreConstants.LINE_SEPARATOR);
/*     */     
/* 113 */     output.append("  </STYLE>\r\n");
/*     */   }
/*     */   
/*     */   public void printList(StringBuilder buf, StatusManager sm)
/*     */   {
/* 118 */     buf.append("<table>\r\n");
/* 119 */     printHeader(buf);
/* 120 */     List<Status> statusList = sm.getCopyOfStatusList();
/* 121 */     for (Status s : statusList) {
/* 122 */       this.count += 1;
/* 123 */       printStatus(buf, s);
/*     */     }
/* 125 */     buf.append("</table>\r\n");
/*     */   }
/*     */   
/*     */   public void printHeader(StringBuilder buf) {
/* 129 */     buf.append("  <tr class=\"header\">\r\n");
/* 130 */     buf.append("    <th>Date </th>\r\n");
/* 131 */     buf.append("    <th>Level</th>\r\n");
/* 132 */     buf.append("    <th>Origin</th>\r\n");
/* 133 */     buf.append("    <th>Message</th>\r\n");
/* 134 */     buf.append("  </tr>\r\n");
/*     */   }
/*     */   
/*     */   String statusLevelAsString(Status s)
/*     */   {
/* 139 */     switch (s.getEffectiveLevel()) {
/*     */     case 0: 
/* 141 */       return "INFO";
/*     */     case 1: 
/* 143 */       return "<span class=\"warn\">WARN</span>";
/*     */     case 2: 
/* 145 */       return "<span class=\"error\">ERROR</span>";
/*     */     }
/* 147 */     return null;
/*     */   }
/*     */   
/*     */   String abbreviatedOrigin(Status s) {
/* 151 */     Object o = s.getOrigin();
/* 152 */     if (o == null) {
/* 153 */       return null;
/*     */     }
/* 155 */     String fqClassName = o.getClass().getName();
/* 156 */     int lastIndex = fqClassName.lastIndexOf('.');
/* 157 */     if (lastIndex != -1) {
/* 158 */       return fqClassName.substring(lastIndex + 1, fqClassName.length());
/*     */     }
/* 160 */     return fqClassName;
/*     */   }
/*     */   
/*     */   private void printStatus(StringBuilder buf, Status s) {
/*     */     String trClass;
/*     */     String trClass;
/* 166 */     if (this.count % 2 == 0) {
/* 167 */       trClass = "even";
/*     */     } else {
/* 169 */       trClass = "odd";
/*     */     }
/* 171 */     buf.append("  <tr class=\"").append(trClass).append("\">\r\n");
/* 172 */     String dateStr = SDF.format(s.getDate().longValue());
/* 173 */     buf.append("    <td class=\"date\">").append(dateStr).append("</td>\r\n");
/* 174 */     buf.append("    <td class=\"level\">").append(statusLevelAsString(s)).append("</td>\r\n");
/*     */     
/* 176 */     buf.append("    <td>").append(abbreviatedOrigin(s)).append("</td>\r\n");
/* 177 */     buf.append("    <td>").append(s.getMessage()).append("</td>\r\n");
/* 178 */     buf.append("  </tr>\r\n");
/* 179 */     if (s.getThrowable() != null) {
/* 180 */       printThrowable(buf, s.getThrowable());
/*     */     }
/*     */   }
/*     */   
/*     */   private void printThrowable(StringBuilder buf, Throwable t) {
/* 185 */     buf.append("  <tr>\r\n");
/* 186 */     buf.append("    <td colspan=\"4\" class=\"exception\"><pre>");
/* 187 */     StringWriter sw = new StringWriter();
/* 188 */     PrintWriter pw = new PrintWriter(sw);
/* 189 */     t.printStackTrace(pw);
/* 190 */     buf.append(Transform.escapeTags(sw.getBuffer()));
/* 191 */     buf.append("    </pre></td>\r\n");
/* 192 */     buf.append("  </tr>\r\n");
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\status\ViewStatusMessagesServletBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */