/*     */ package ch.qos.logback.core.status;
/*     */ 
/*     */ import ch.qos.logback.core.Context;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
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
/*     */ public class StatusUtil
/*     */ {
/*     */   StatusManager sm;
/*     */   
/*     */   public StatusUtil(StatusManager sm)
/*     */   {
/*  30 */     this.sm = sm;
/*     */   }
/*     */   
/*     */   public StatusUtil(Context context) {
/*  34 */     this.sm = context.getStatusManager();
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
/*     */   public static boolean contextHasStatusListener(Context context)
/*     */   {
/*  47 */     StatusManager sm = context.getStatusManager();
/*  48 */     if (sm == null)
/*  49 */       return false;
/*  50 */     List<StatusListener> listeners = sm.getCopyOfStatusListenerList();
/*  51 */     if ((listeners == null) || (listeners.size() == 0)) {
/*  52 */       return false;
/*     */     }
/*  54 */     return true;
/*     */   }
/*     */   
/*     */   public static List<Status> filterStatusListByTimeThreshold(List<Status> rawList, long threshold) {
/*  58 */     List<Status> filteredList = new ArrayList();
/*  59 */     for (Status s : rawList) {
/*  60 */       if (s.getDate().longValue() >= threshold)
/*  61 */         filteredList.add(s);
/*     */     }
/*  63 */     return filteredList;
/*     */   }
/*     */   
/*     */   public void addStatus(Status status) {
/*  67 */     if (this.sm != null) {
/*  68 */       this.sm.add(status);
/*     */     }
/*     */   }
/*     */   
/*     */   public void addInfo(Object caller, String msg) {
/*  73 */     addStatus(new InfoStatus(msg, caller));
/*     */   }
/*     */   
/*     */   public void addWarn(Object caller, String msg) {
/*  77 */     addStatus(new WarnStatus(msg, caller));
/*     */   }
/*     */   
/*     */   public void addError(Object caller, String msg, Throwable t)
/*     */   {
/*  82 */     addStatus(new ErrorStatus(msg, caller, t));
/*     */   }
/*     */   
/*     */   public boolean hasXMLParsingErrors(long threshold) {
/*  86 */     return containsMatch(threshold, 2, "XML_PARSING");
/*     */   }
/*     */   
/*     */   public boolean noXMLParsingErrorsOccurred(long threshold) {
/*  90 */     return !hasXMLParsingErrors(threshold);
/*     */   }
/*     */   
/*     */   public int getHighestLevel(long threshold) {
/*  94 */     List<Status> filteredList = filterStatusListByTimeThreshold(this.sm.getCopyOfStatusList(), threshold);
/*  95 */     int maxLevel = 0;
/*  96 */     for (Status s : filteredList) {
/*  97 */       if (s.getLevel() > maxLevel)
/*  98 */         maxLevel = s.getLevel();
/*     */     }
/* 100 */     return maxLevel;
/*     */   }
/*     */   
/*     */   public boolean isErrorFree(long threshold) {
/* 104 */     return 2 > getHighestLevel(threshold);
/*     */   }
/*     */   
/*     */   public boolean containsMatch(long threshold, int level, String regex) {
/* 108 */     List<Status> filteredList = filterStatusListByTimeThreshold(this.sm.getCopyOfStatusList(), threshold);
/* 109 */     Pattern p = Pattern.compile(regex);
/*     */     
/* 111 */     for (Status status : filteredList) {
/* 112 */       if (level == status.getLevel())
/*     */       {
/*     */ 
/* 115 */         String msg = status.getMessage();
/* 116 */         Matcher matcher = p.matcher(msg);
/* 117 */         if (matcher.lookingAt())
/* 118 */           return true;
/*     */       }
/*     */     }
/* 121 */     return false;
/*     */   }
/*     */   
/*     */   public boolean containsMatch(int level, String regex) {
/* 125 */     return containsMatch(0L, level, regex);
/*     */   }
/*     */   
/*     */   public boolean containsMatch(String regex) {
/* 129 */     Pattern p = Pattern.compile(regex);
/* 130 */     for (Status status : this.sm.getCopyOfStatusList()) {
/* 131 */       String msg = status.getMessage();
/* 132 */       Matcher matcher = p.matcher(msg);
/* 133 */       if (matcher.lookingAt()) {
/* 134 */         return true;
/*     */       }
/*     */     }
/* 137 */     return false;
/*     */   }
/*     */   
/*     */   public int matchCount(String regex) {
/* 141 */     int count = 0;
/* 142 */     Pattern p = Pattern.compile(regex);
/* 143 */     for (Status status : this.sm.getCopyOfStatusList()) {
/* 144 */       String msg = status.getMessage();
/* 145 */       Matcher matcher = p.matcher(msg);
/* 146 */       if (matcher.lookingAt()) {
/* 147 */         count++;
/*     */       }
/*     */     }
/* 150 */     return count;
/*     */   }
/*     */   
/*     */   public boolean containsException(Class<?> exceptionType) {
/* 154 */     Iterator<Status> stati = this.sm.getCopyOfStatusList().iterator();
/* 155 */     while (stati.hasNext()) {
/* 156 */       Status status = (Status)stati.next();
/* 157 */       Throwable t = status.getThrowable();
/* 158 */       if ((t != null) && (t.getClass().getName().equals(exceptionType.getName()))) {
/* 159 */         return true;
/*     */       }
/*     */     }
/* 162 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public long timeOfLastReset()
/*     */   {
/* 171 */     List<Status> statusList = this.sm.getCopyOfStatusList();
/* 172 */     if (statusList == null) {
/* 173 */       return -1L;
/*     */     }
/* 175 */     int len = statusList.size();
/* 176 */     for (int i = len - 1; i >= 0; i--) {
/* 177 */       Status s = (Status)statusList.get(i);
/* 178 */       if ("Will reset and reconfigure context ".equals(s.getMessage())) {
/* 179 */         return s.getDate().longValue();
/*     */       }
/*     */     }
/* 182 */     return -1L;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\status\StatusUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */