/*     */ package org.slf4j;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.URL;
/*     */ import java.util.Arrays;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.slf4j.helpers.NOPLoggerFactory;
/*     */ import org.slf4j.helpers.SubstituteLogger;
/*     */ import org.slf4j.helpers.SubstituteLoggerFactory;
/*     */ import org.slf4j.helpers.Util;
/*     */ import org.slf4j.impl.StaticLoggerBinder;
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
/*     */ public final class LoggerFactory
/*     */ {
/*     */   static final String CODES_PREFIX = "http://www.slf4j.org/codes.html";
/*     */   static final String NO_STATICLOGGERBINDER_URL = "http://www.slf4j.org/codes.html#StaticLoggerBinder";
/*     */   static final String MULTIPLE_BINDINGS_URL = "http://www.slf4j.org/codes.html#multiple_bindings";
/*     */   static final String NULL_LF_URL = "http://www.slf4j.org/codes.html#null_LF";
/*     */   static final String VERSION_MISMATCH = "http://www.slf4j.org/codes.html#version_mismatch";
/*     */   static final String SUBSTITUTE_LOGGER_URL = "http://www.slf4j.org/codes.html#substituteLogger";
/*     */   static final String UNSUCCESSFUL_INIT_URL = "http://www.slf4j.org/codes.html#unsuccessfulInit";
/*     */   static final String UNSUCCESSFUL_INIT_MSG = "org.slf4j.LoggerFactory could not be successfully initialized. See also http://www.slf4j.org/codes.html#unsuccessfulInit";
/*     */   static final int UNINITIALIZED = 0;
/*     */   static final int ONGOING_INITIALIZATION = 1;
/*     */   static final int FAILED_INITIALIZATION = 2;
/*     */   static final int SUCCESSFUL_INITIALIZATION = 3;
/*     */   static final int NOP_FALLBACK_INITIALIZATION = 4;
/*  74 */   static int INITIALIZATION_STATE = 0;
/*  75 */   static SubstituteLoggerFactory TEMP_FACTORY = new SubstituteLoggerFactory();
/*  76 */   static NOPLoggerFactory NOP_FALLBACK_FACTORY = new NOPLoggerFactory();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  85 */   private static final String[] API_COMPATIBILITY_LIST = { "1.6", "1.7" };
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
/*     */   static void reset()
/*     */   {
/* 103 */     INITIALIZATION_STATE = 0;
/* 104 */     TEMP_FACTORY = new SubstituteLoggerFactory();
/*     */   }
/*     */   
/*     */   private static final void performInitialization() {
/*     */     
/* 109 */     if (INITIALIZATION_STATE == 3) {
/* 110 */       versionSanityCheck();
/*     */     }
/*     */   }
/*     */   
/*     */   private static boolean messageContainsOrgSlf4jImplStaticLoggerBinder(String msg) {
/* 115 */     if (msg == null)
/* 116 */       return false;
/* 117 */     if (msg.indexOf("org/slf4j/impl/StaticLoggerBinder") != -1)
/* 118 */       return true;
/* 119 */     if (msg.indexOf("org.slf4j.impl.StaticLoggerBinder") != -1)
/* 120 */       return true;
/* 121 */     return false;
/*     */   }
/*     */   
/*     */   private static final void bind() {
/*     */     try {
/* 126 */       Set<URL> staticLoggerBinderPathSet = findPossibleStaticLoggerBinderPathSet();
/* 127 */       reportMultipleBindingAmbiguity(staticLoggerBinderPathSet);
/*     */       
/* 129 */       StaticLoggerBinder.getSingleton();
/* 130 */       INITIALIZATION_STATE = 3;
/* 131 */       reportActualBinding(staticLoggerBinderPathSet);
/* 132 */       fixSubstitutedLoggers();
/*     */     } catch (NoClassDefFoundError ncde) {
/* 134 */       String msg = ncde.getMessage();
/* 135 */       if (messageContainsOrgSlf4jImplStaticLoggerBinder(msg)) {
/* 136 */         INITIALIZATION_STATE = 4;
/* 137 */         Util.report("Failed to load class \"org.slf4j.impl.StaticLoggerBinder\".");
/* 138 */         Util.report("Defaulting to no-operation (NOP) logger implementation");
/* 139 */         Util.report("See http://www.slf4j.org/codes.html#StaticLoggerBinder for further details.");
/*     */       }
/*     */       else {
/* 142 */         failedBinding(ncde);
/* 143 */         throw ncde;
/*     */       }
/*     */     } catch (NoSuchMethodError nsme) {
/* 146 */       String msg = nsme.getMessage();
/* 147 */       if ((msg != null) && (msg.indexOf("org.slf4j.impl.StaticLoggerBinder.getSingleton()") != -1)) {
/* 148 */         INITIALIZATION_STATE = 2;
/* 149 */         Util.report("slf4j-api 1.6.x (or later) is incompatible with this binding.");
/* 150 */         Util.report("Your binding is version 1.5.5 or earlier.");
/* 151 */         Util.report("Upgrade your binding to version 1.6.x.");
/*     */       }
/* 153 */       throw nsme;
/*     */     } catch (Exception e) {
/* 155 */       failedBinding(e);
/* 156 */       throw new IllegalStateException("Unexpected initialization failure", e);
/*     */     }
/*     */   }
/*     */   
/*     */   static void failedBinding(Throwable t) {
/* 161 */     INITIALIZATION_STATE = 2;
/* 162 */     Util.report("Failed to instantiate SLF4J LoggerFactory", t);
/*     */   }
/*     */   
/*     */   private static final void fixSubstitutedLoggers() {
/* 166 */     List<SubstituteLogger> loggers = TEMP_FACTORY.getLoggers();
/*     */     
/* 168 */     if (loggers.isEmpty()) {
/* 169 */       return;
/*     */     }
/*     */     
/* 172 */     Util.report("The following set of substitute loggers may have been accessed");
/* 173 */     Util.report("during the initialization phase. Logging calls during this");
/* 174 */     Util.report("phase were not honored. However, subsequent logging calls to these");
/* 175 */     Util.report("loggers will work as normally expected.");
/* 176 */     Util.report("See also http://www.slf4j.org/codes.html#substituteLogger");
/* 177 */     for (SubstituteLogger subLogger : loggers) {
/* 178 */       subLogger.setDelegate(getLogger(subLogger.getName()));
/* 179 */       Util.report(subLogger.getName());
/*     */     }
/*     */     
/* 182 */     TEMP_FACTORY.clear();
/*     */   }
/*     */   
/*     */   private static final void versionSanityCheck() {
/*     */     try {
/* 187 */       String requested = StaticLoggerBinder.REQUESTED_API_VERSION;
/*     */       
/* 189 */       boolean match = false;
/* 190 */       for (int i = 0; i < API_COMPATIBILITY_LIST.length; i++) {
/* 191 */         if (requested.startsWith(API_COMPATIBILITY_LIST[i])) {
/* 192 */           match = true;
/*     */         }
/*     */       }
/* 195 */       if (!match) {
/* 196 */         Util.report("The requested version " + requested + " by your slf4j binding is not compatible with " + Arrays.asList(API_COMPATIBILITY_LIST).toString());
/*     */         
/*     */ 
/* 199 */         Util.report("See http://www.slf4j.org/codes.html#version_mismatch for further details.");
/*     */ 
/*     */       }
/*     */       
/*     */ 
/*     */     }
/*     */     catch (NoSuchFieldError nsfe) {}catch (Throwable e)
/*     */     {
/*     */ 
/* 208 */       Util.report("Unexpected problem occured during version sanity check", e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/* 214 */   private static String STATIC_LOGGER_BINDER_PATH = "org/slf4j/impl/StaticLoggerBinder.class";
/*     */   
/*     */ 
/*     */   private static Set<URL> findPossibleStaticLoggerBinderPathSet()
/*     */   {
/* 219 */     Set<URL> staticLoggerBinderPathSet = new LinkedHashSet();
/*     */     try {
/* 221 */       ClassLoader loggerFactoryClassLoader = LoggerFactory.class.getClassLoader();
/*     */       Enumeration<URL> paths;
/*     */       Enumeration<URL> paths;
/* 224 */       if (loggerFactoryClassLoader == null) {
/* 225 */         paths = ClassLoader.getSystemResources(STATIC_LOGGER_BINDER_PATH);
/*     */       } else {
/* 227 */         paths = loggerFactoryClassLoader.getResources(STATIC_LOGGER_BINDER_PATH);
/*     */       }
/*     */       
/* 230 */       while (paths.hasMoreElements()) {
/* 231 */         URL path = (URL)paths.nextElement();
/* 232 */         staticLoggerBinderPathSet.add(path);
/*     */       }
/*     */     } catch (IOException ioe) {
/* 235 */       Util.report("Error getting resources from path", ioe);
/*     */     }
/* 237 */     return staticLoggerBinderPathSet;
/*     */   }
/*     */   
/*     */   private static boolean isAmbiguousStaticLoggerBinderPathSet(Set<URL> staticLoggerBinderPathSet) {
/* 241 */     return staticLoggerBinderPathSet.size() > 1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void reportMultipleBindingAmbiguity(Set<URL> staticLoggerBinderPathSet)
/*     */   {
/* 250 */     if (isAmbiguousStaticLoggerBinderPathSet(staticLoggerBinderPathSet)) {
/* 251 */       Util.report("Class path contains multiple SLF4J bindings.");
/* 252 */       Iterator<URL> iterator = staticLoggerBinderPathSet.iterator();
/* 253 */       while (iterator.hasNext()) {
/* 254 */         URL path = (URL)iterator.next();
/* 255 */         Util.report("Found binding in [" + path + "]");
/*     */       }
/* 257 */       Util.report("See http://www.slf4j.org/codes.html#multiple_bindings for an explanation.");
/*     */     }
/*     */   }
/*     */   
/*     */   private static void reportActualBinding(Set<URL> staticLoggerBinderPathSet) {
/* 262 */     if (isAmbiguousStaticLoggerBinderPathSet(staticLoggerBinderPathSet)) {
/* 263 */       Util.report("Actual binding is of type [" + StaticLoggerBinder.getSingleton().getLoggerFactoryClassStr() + "]");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Logger getLogger(String name)
/*     */   {
/* 276 */     ILoggerFactory iLoggerFactory = getILoggerFactory();
/* 277 */     return iLoggerFactory.getLogger(name);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Logger getLogger(Class clazz)
/*     */   {
/* 288 */     return getLogger(clazz.getName());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ILoggerFactory getILoggerFactory()
/*     */   {
/* 300 */     if (INITIALIZATION_STATE == 0) {
/* 301 */       INITIALIZATION_STATE = 1;
/* 302 */       performInitialization();
/*     */     }
/* 304 */     switch (INITIALIZATION_STATE) {
/*     */     case 3: 
/* 306 */       return StaticLoggerBinder.getSingleton().getLoggerFactory();
/*     */     case 4: 
/* 308 */       return NOP_FALLBACK_FACTORY;
/*     */     case 2: 
/* 310 */       throw new IllegalStateException("org.slf4j.LoggerFactory could not be successfully initialized. See also http://www.slf4j.org/codes.html#unsuccessfulInit");
/*     */     
/*     */ 
/*     */     case 1: 
/* 314 */       return TEMP_FACTORY;
/*     */     }
/* 316 */     throw new IllegalStateException("Unreachable code");
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\org\slf4j\LoggerFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */