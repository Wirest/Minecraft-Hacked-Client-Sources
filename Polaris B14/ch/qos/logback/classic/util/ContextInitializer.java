/*     */ package ch.qos.logback.classic.util;
/*     */ 
/*     */ import ch.qos.logback.classic.BasicConfigurator;
/*     */ import ch.qos.logback.classic.LoggerContext;
/*     */ import ch.qos.logback.classic.gaffer.GafferUtil;
/*     */ import ch.qos.logback.classic.joran.JoranConfigurator;
/*     */ import ch.qos.logback.classic.spi.Configurator;
/*     */ import ch.qos.logback.core.LogbackException;
/*     */ import ch.qos.logback.core.joran.spi.JoranException;
/*     */ import ch.qos.logback.core.status.ErrorStatus;
/*     */ import ch.qos.logback.core.status.InfoStatus;
/*     */ import ch.qos.logback.core.status.StatusManager;
/*     */ import ch.qos.logback.core.status.WarnStatus;
/*     */ import ch.qos.logback.core.util.Loader;
/*     */ import ch.qos.logback.core.util.OptionHelper;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.util.Set;
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
/*     */ public class ContextInitializer
/*     */ {
/*     */   public static final String GROOVY_AUTOCONFIG_FILE = "logback.groovy";
/*     */   public static final String AUTOCONFIG_FILE = "logback.xml";
/*     */   public static final String TEST_AUTOCONFIG_FILE = "logback-test.xml";
/*     */   public static final String CONFIG_FILE_PROPERTY = "logback.configurationFile";
/*     */   public static final String STATUS_LISTENER_CLASS = "logback.statusListenerClass";
/*     */   public static final String SYSOUT = "SYSOUT";
/*     */   final LoggerContext loggerContext;
/*     */   
/*     */   public ContextInitializer(LoggerContext loggerContext)
/*     */   {
/*  56 */     this.loggerContext = loggerContext;
/*     */   }
/*     */   
/*     */   public void configureByResource(URL url) throws JoranException {
/*  60 */     if (url == null) {
/*  61 */       throw new IllegalArgumentException("URL argument cannot be null");
/*     */     }
/*  63 */     String urlString = url.toString();
/*  64 */     if (urlString.endsWith("groovy")) {
/*  65 */       if (EnvUtil.isGroovyAvailable())
/*     */       {
/*     */ 
/*  68 */         GafferUtil.runGafferConfiguratorOn(this.loggerContext, this, url);
/*     */       } else {
/*  70 */         StatusManager sm = this.loggerContext.getStatusManager();
/*  71 */         sm.add(new ErrorStatus("Groovy classes are not available on the class path. ABORTING INITIALIZATION.", this.loggerContext));
/*     */       }
/*     */     }
/*  74 */     else if (urlString.endsWith("xml")) {
/*  75 */       JoranConfigurator configurator = new JoranConfigurator();
/*  76 */       configurator.setContext(this.loggerContext);
/*  77 */       configurator.doConfigure(url);
/*     */     } else {
/*  79 */       throw new LogbackException("Unexpected filename extension of file [" + url.toString() + "]. Should be either .groovy or .xml");
/*     */     }
/*     */   }
/*     */   
/*     */   void joranConfigureByResource(URL url) throws JoranException {
/*  84 */     JoranConfigurator configurator = new JoranConfigurator();
/*  85 */     configurator.setContext(this.loggerContext);
/*  86 */     configurator.doConfigure(url);
/*     */   }
/*     */   
/*     */   private URL findConfigFileURLFromSystemProperties(ClassLoader classLoader, boolean updateStatus) {
/*  90 */     String logbackConfigFile = OptionHelper.getSystemProperty("logback.configurationFile");
/*  91 */     if (logbackConfigFile != null) {
/*  92 */       URL result = null;
/*     */       try {
/*  94 */         result = new URL(logbackConfigFile);
/*  95 */         return result;
/*     */       }
/*     */       catch (MalformedURLException e)
/*     */       {
/*  99 */         result = Loader.getResource(logbackConfigFile, classLoader);
/* 100 */         if (result != null) {
/* 101 */           return result;
/*     */         }
/* 103 */         File f = new File(logbackConfigFile);
/* 104 */         if ((f.exists()) && (f.isFile())) {
/*     */           try {
/* 106 */             result = f.toURI().toURL();
/* 107 */             return result;
/*     */           }
/*     */           catch (MalformedURLException e1) {}
/*     */         }
/*     */       } finally {
/* 112 */         if (updateStatus) {
/* 113 */           statusOnResourceSearch(logbackConfigFile, classLoader, result);
/*     */         }
/*     */       }
/*     */     }
/* 117 */     return null;
/*     */   }
/*     */   
/*     */   public URL findURLOfDefaultConfigurationFile(boolean updateStatus) {
/* 121 */     ClassLoader myClassLoader = Loader.getClassLoaderOfObject(this);
/* 122 */     URL url = findConfigFileURLFromSystemProperties(myClassLoader, updateStatus);
/* 123 */     if (url != null) {
/* 124 */       return url;
/*     */     }
/*     */     
/* 127 */     url = getResource("logback.groovy", myClassLoader, updateStatus);
/* 128 */     if (url != null) {
/* 129 */       return url;
/*     */     }
/*     */     
/* 132 */     url = getResource("logback-test.xml", myClassLoader, updateStatus);
/* 133 */     if (url != null) {
/* 134 */       return url;
/*     */     }
/*     */     
/* 137 */     return getResource("logback.xml", myClassLoader, updateStatus);
/*     */   }
/*     */   
/*     */   private URL getResource(String filename, ClassLoader myClassLoader, boolean updateStatus) {
/* 141 */     URL url = Loader.getResource(filename, myClassLoader);
/* 142 */     if (updateStatus) {
/* 143 */       statusOnResourceSearch(filename, myClassLoader, url);
/*     */     }
/* 145 */     return url;
/*     */   }
/*     */   
/*     */   public void autoConfig() throws JoranException {
/* 149 */     StatusListenerConfigHelper.installIfAsked(this.loggerContext);
/* 150 */     URL url = findURLOfDefaultConfigurationFile(true);
/* 151 */     if (url != null) {
/* 152 */       configureByResource(url);
/*     */     } else {
/* 154 */       Configurator c = (Configurator)EnvUtil.loadFromServiceLoader(Configurator.class);
/* 155 */       if (c != null) {
/*     */         try {
/* 157 */           c.setContext(this.loggerContext);
/* 158 */           c.configure(this.loggerContext);
/*     */         } catch (Exception e) {
/* 160 */           throw new LogbackException(String.format("Failed to initialize Configurator: %s using ServiceLoader", new Object[] { c != null ? c.getClass().getCanonicalName() : "null" }), e);
/*     */         }
/*     */         
/*     */       } else {
/* 164 */         BasicConfigurator.configure(this.loggerContext);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void multiplicityWarning(String resourceName, ClassLoader classLoader) {
/* 170 */     Set<URL> urlSet = null;
/* 171 */     StatusManager sm = this.loggerContext.getStatusManager();
/*     */     try {
/* 173 */       urlSet = Loader.getResourceOccurrenceCount(resourceName, classLoader);
/*     */     } catch (IOException e) {
/* 175 */       sm.add(new ErrorStatus("Failed to get url list for resource [" + resourceName + "]", this.loggerContext, e));
/*     */     }
/*     */     
/* 178 */     if ((urlSet != null) && (urlSet.size() > 1)) {
/* 179 */       sm.add(new WarnStatus("Resource [" + resourceName + "] occurs multiple times on the classpath.", this.loggerContext));
/*     */       
/* 181 */       for (URL url : urlSet) {
/* 182 */         sm.add(new WarnStatus("Resource [" + resourceName + "] occurs at [" + url.toString() + "]", this.loggerContext));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void statusOnResourceSearch(String resourceName, ClassLoader classLoader, URL url)
/*     */   {
/* 189 */     StatusManager sm = this.loggerContext.getStatusManager();
/* 190 */     if (url == null) {
/* 191 */       sm.add(new InfoStatus("Could NOT find resource [" + resourceName + "]", this.loggerContext));
/*     */     }
/*     */     else {
/* 194 */       sm.add(new InfoStatus("Found resource [" + resourceName + "] at [" + url.toString() + "]", this.loggerContext));
/*     */       
/* 196 */       multiplicityWarning(resourceName, classLoader);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\util\ContextInitializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */