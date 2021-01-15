/*     */ package ch.qos.logback.core.net;
/*     */ 
/*     */ import ch.qos.logback.core.AppenderBase;
/*     */ import java.util.Properties;
/*     */ import javax.naming.Context;
/*     */ import javax.naming.InitialContext;
/*     */ import javax.naming.NameNotFoundException;
/*     */ import javax.naming.NamingException;
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
/*     */ public abstract class JMSAppenderBase<E>
/*     */   extends AppenderBase<E>
/*     */ {
/*     */   protected String securityPrincipalName;
/*     */   protected String securityCredentials;
/*     */   protected String initialContextFactoryName;
/*     */   protected String urlPkgPrefixes;
/*     */   protected String providerURL;
/*     */   protected String userName;
/*     */   protected String password;
/*     */   
/*     */   protected Object lookup(Context ctx, String name)
/*     */     throws NamingException
/*     */   {
/*     */     try
/*     */     {
/*  49 */       return ctx.lookup(name);
/*     */     } catch (NameNotFoundException e) {
/*  51 */       addError("Could not find name [" + name + "].");
/*  52 */       throw e;
/*     */     }
/*     */   }
/*     */   
/*     */   public Context buildJNDIContext() throws NamingException {
/*  57 */     Context jndi = null;
/*     */     
/*     */ 
/*  60 */     if (this.initialContextFactoryName != null) {
/*  61 */       Properties env = buildEnvProperties();
/*  62 */       jndi = new InitialContext(env);
/*     */     } else {
/*  64 */       jndi = new InitialContext();
/*     */     }
/*  66 */     return jndi;
/*     */   }
/*     */   
/*     */   public Properties buildEnvProperties() {
/*  70 */     Properties env = new Properties();
/*  71 */     env.put("java.naming.factory.initial", this.initialContextFactoryName);
/*  72 */     if (this.providerURL != null) {
/*  73 */       env.put("java.naming.provider.url", this.providerURL);
/*     */     } else {
/*  75 */       addWarn("You have set InitialContextFactoryName option but not the ProviderURL. This is likely to cause problems.");
/*     */     }
/*     */     
/*  78 */     if (this.urlPkgPrefixes != null) {
/*  79 */       env.put("java.naming.factory.url.pkgs", this.urlPkgPrefixes);
/*     */     }
/*     */     
/*  82 */     if (this.securityPrincipalName != null) {
/*  83 */       env.put("java.naming.security.principal", this.securityPrincipalName);
/*  84 */       if (this.securityCredentials != null) {
/*  85 */         env.put("java.naming.security.credentials", this.securityCredentials);
/*     */       } else {
/*  87 */         addWarn("You have set SecurityPrincipalName option but not the SecurityCredentials. This is likely to cause problems.");
/*     */       }
/*     */     }
/*     */     
/*  91 */     return env;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getInitialContextFactoryName()
/*     */   {
/* 102 */     return this.initialContextFactoryName;
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
/*     */   public void setInitialContextFactoryName(String initialContextFactoryName)
/*     */   {
/* 116 */     this.initialContextFactoryName = initialContextFactoryName;
/*     */   }
/*     */   
/*     */   public String getProviderURL() {
/* 120 */     return this.providerURL;
/*     */   }
/*     */   
/*     */   public void setProviderURL(String providerURL) {
/* 124 */     this.providerURL = providerURL;
/*     */   }
/*     */   
/*     */   public String getURLPkgPrefixes() {
/* 128 */     return this.urlPkgPrefixes;
/*     */   }
/*     */   
/*     */   public void setURLPkgPrefixes(String urlPkgPrefixes) {
/* 132 */     this.urlPkgPrefixes = urlPkgPrefixes;
/*     */   }
/*     */   
/*     */   public String getSecurityCredentials() {
/* 136 */     return this.securityCredentials;
/*     */   }
/*     */   
/*     */   public void setSecurityCredentials(String securityCredentials) {
/* 140 */     this.securityCredentials = securityCredentials;
/*     */   }
/*     */   
/*     */   public String getSecurityPrincipalName() {
/* 144 */     return this.securityPrincipalName;
/*     */   }
/*     */   
/*     */   public void setSecurityPrincipalName(String securityPrincipalName) {
/* 148 */     this.securityPrincipalName = securityPrincipalName;
/*     */   }
/*     */   
/*     */   public String getUserName() {
/* 152 */     return this.userName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setUserName(String userName)
/*     */   {
/* 162 */     this.userName = userName;
/*     */   }
/*     */   
/*     */   public String getPassword() {
/* 166 */     return this.password;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setPassword(String password)
/*     */   {
/* 173 */     this.password = password;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\net\JMSAppenderBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */