/*     */ package ch.qos.logback.core.net.ssl;
/*     */ 
/*     */ import ch.qos.logback.core.spi.ContextAwareBase;
/*     */ import ch.qos.logback.core.util.OptionHelper;
/*     */ import ch.qos.logback.core.util.StringCollectionUtil;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SSLParametersConfiguration
/*     */   extends ContextAwareBase
/*     */ {
/*     */   private String includedProtocols;
/*     */   private String excludedProtocols;
/*     */   private String includedCipherSuites;
/*     */   private String excludedCipherSuites;
/*     */   private Boolean needClientAuth;
/*     */   private Boolean wantClientAuth;
/*     */   private String[] enabledProtocols;
/*     */   private String[] enabledCipherSuites;
/*     */   
/*     */   public void configure(SSLConfigurable socket)
/*     */   {
/*  50 */     socket.setEnabledProtocols(enabledProtocols(socket.getSupportedProtocols(), socket.getDefaultProtocols()));
/*     */     
/*  52 */     socket.setEnabledCipherSuites(enabledCipherSuites(socket.getSupportedCipherSuites(), socket.getDefaultCipherSuites()));
/*     */     
/*  54 */     if (isNeedClientAuth() != null) {
/*  55 */       socket.setNeedClientAuth(isNeedClientAuth().booleanValue());
/*     */     }
/*  57 */     if (isWantClientAuth() != null) {
/*  58 */       socket.setWantClientAuth(isWantClientAuth().booleanValue());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private String[] enabledProtocols(String[] supportedProtocols, String[] defaultProtocols)
/*     */   {
/*  70 */     if (this.enabledProtocols == null)
/*     */     {
/*     */ 
/*  73 */       if ((OptionHelper.isEmpty(getIncludedProtocols())) && (OptionHelper.isEmpty(getExcludedProtocols())))
/*     */       {
/*  75 */         this.enabledProtocols = ((String[])Arrays.copyOf(defaultProtocols, defaultProtocols.length));
/*     */       }
/*     */       else
/*     */       {
/*  79 */         this.enabledProtocols = includedStrings(supportedProtocols, getIncludedProtocols(), getExcludedProtocols());
/*     */       }
/*     */       
/*  82 */       for (String protocol : this.enabledProtocols) {
/*  83 */         addInfo("enabled protocol: " + protocol);
/*     */       }
/*     */     }
/*  86 */     return this.enabledProtocols;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private String[] enabledCipherSuites(String[] supportedCipherSuites, String[] defaultCipherSuites)
/*     */   {
/*  97 */     if (this.enabledCipherSuites == null)
/*     */     {
/*     */ 
/* 100 */       if ((OptionHelper.isEmpty(getIncludedCipherSuites())) && (OptionHelper.isEmpty(getExcludedCipherSuites())))
/*     */       {
/* 102 */         this.enabledCipherSuites = ((String[])Arrays.copyOf(defaultCipherSuites, defaultCipherSuites.length));
/*     */       }
/*     */       else
/*     */       {
/* 106 */         this.enabledCipherSuites = includedStrings(supportedCipherSuites, getIncludedCipherSuites(), getExcludedCipherSuites());
/*     */       }
/*     */       
/* 109 */       for (String cipherSuite : this.enabledCipherSuites) {
/* 110 */         addInfo("enabled cipher suite: " + cipherSuite);
/*     */       }
/*     */     }
/* 113 */     return this.enabledCipherSuites;
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
/*     */   private String[] includedStrings(String[] defaults, String included, String excluded)
/*     */   {
/* 128 */     List<String> values = new ArrayList(defaults.length);
/* 129 */     values.addAll(Arrays.asList(defaults));
/* 130 */     if (included != null) {
/* 131 */       StringCollectionUtil.retainMatching(values, stringToArray(included));
/*     */     }
/* 133 */     if (excluded != null) {
/* 134 */       StringCollectionUtil.removeMatching(values, stringToArray(excluded));
/*     */     }
/* 136 */     return (String[])values.toArray(new String[values.size()]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private String[] stringToArray(String s)
/*     */   {
/* 145 */     return s.split("\\s*,\\s*");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getIncludedProtocols()
/*     */   {
/* 154 */     return this.includedProtocols;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setIncludedProtocols(String protocols)
/*     */   {
/* 164 */     this.includedProtocols = protocols;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getExcludedProtocols()
/*     */   {
/* 173 */     return this.excludedProtocols;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setExcludedProtocols(String protocols)
/*     */   {
/* 183 */     this.excludedProtocols = protocols;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getIncludedCipherSuites()
/*     */   {
/* 192 */     return this.includedCipherSuites;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setIncludedCipherSuites(String cipherSuites)
/*     */   {
/* 202 */     this.includedCipherSuites = cipherSuites;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getExcludedCipherSuites()
/*     */   {
/* 211 */     return this.excludedCipherSuites;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setExcludedCipherSuites(String cipherSuites)
/*     */   {
/* 221 */     this.excludedCipherSuites = cipherSuites;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Boolean isNeedClientAuth()
/*     */   {
/* 229 */     return this.needClientAuth;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setNeedClientAuth(Boolean needClientAuth)
/*     */   {
/* 237 */     this.needClientAuth = needClientAuth;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Boolean isWantClientAuth()
/*     */   {
/* 245 */     return this.wantClientAuth;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setWantClientAuth(Boolean wantClientAuth)
/*     */   {
/* 253 */     this.wantClientAuth = wantClientAuth;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\net\ssl\SSLParametersConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */