/*     */ package io.netty.util;
/*     */ 
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import java.io.InputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.net.URL;
/*     */ import java.text.ParseException;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Date;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.Set;
/*     */ import java.util.TreeMap;
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
/*     */ public final class Version
/*     */ {
/*     */   private static final String PROP_VERSION = ".version";
/*     */   private static final String PROP_BUILD_DATE = ".buildDate";
/*     */   private static final String PROP_COMMIT_DATE = ".commitDate";
/*     */   private static final String PROP_SHORT_COMMIT_HASH = ".shortCommitHash";
/*     */   private static final String PROP_LONG_COMMIT_HASH = ".longCommitHash";
/*     */   private static final String PROP_REPO_STATUS = ".repoStatus";
/*     */   private final String artifactId;
/*     */   private final String artifactVersion;
/*     */   private final long buildTimeMillis;
/*     */   private final long commitTimeMillis;
/*     */   private final String shortCommitHash;
/*     */   private final String longCommitHash;
/*     */   private final String repositoryStatus;
/*     */   
/*     */   public static Map<String, Version> identify()
/*     */   {
/*  56 */     return identify(null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Map<String, Version> identify(ClassLoader classLoader)
/*     */   {
/*  65 */     if (classLoader == null) {
/*  66 */       classLoader = PlatformDependent.getContextClassLoader();
/*     */     }
/*     */     
/*     */ 
/*  70 */     Properties props = new Properties();
/*     */     try {
/*  72 */       Enumeration<URL> resources = classLoader.getResources("META-INF/io.netty.versions.properties");
/*  73 */       for (;;) { if (resources.hasMoreElements()) {
/*  74 */           URL url = (URL)resources.nextElement();
/*  75 */           InputStream in = url.openStream();
/*     */           try {
/*  77 */             props.load(in);
/*     */             try
/*     */             {
/*  80 */               in.close(); } catch (Exception ignore) {} } finally { try { in.close();
/*     */             }
/*     */             catch (Exception ignore) {}
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (Exception ignore) {}
/*     */     
/*     */ 
/*     */ 
/*  91 */     Set<String> artifactIds = new HashSet();
/*  92 */     for (Object o : props.keySet()) {
/*  93 */       String k = (String)o;
/*     */       
/*  95 */       int dotIndex = k.indexOf('.');
/*  96 */       if (dotIndex > 0)
/*     */       {
/*     */ 
/*     */ 
/* 100 */         String artifactId = k.substring(0, dotIndex);
/*     */         
/*     */ 
/* 103 */         if ((props.containsKey(artifactId + ".version")) && (props.containsKey(artifactId + ".buildDate")) && (props.containsKey(artifactId + ".commitDate")) && (props.containsKey(artifactId + ".shortCommitHash")) && (props.containsKey(artifactId + ".longCommitHash")) && (props.containsKey(artifactId + ".repoStatus")))
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 112 */           artifactIds.add(artifactId); }
/*     */       }
/*     */     }
/* 115 */     Map<String, Version> versions = new TreeMap();
/* 116 */     for (String artifactId : artifactIds) {
/* 117 */       versions.put(artifactId, new Version(artifactId, props.getProperty(artifactId + ".version"), parseIso8601(props.getProperty(artifactId + ".buildDate")), parseIso8601(props.getProperty(artifactId + ".commitDate")), props.getProperty(artifactId + ".shortCommitHash"), props.getProperty(artifactId + ".longCommitHash"), props.getProperty(artifactId + ".repoStatus")));
/*     */     }
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
/* 129 */     return versions;
/*     */   }
/*     */   
/*     */   private static long parseIso8601(String value) {
/*     */     try {
/* 134 */       return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z").parse(value).getTime();
/*     */     } catch (ParseException ignored) {}
/* 136 */     return 0L;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void main(String[] args)
/*     */   {
/* 144 */     for (Version v : identify().values()) {
/* 145 */       System.err.println(v);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   private Version(String artifactId, String artifactVersion, long buildTimeMillis, long commitTimeMillis, String shortCommitHash, String longCommitHash, String repositoryStatus)
/*     */   {
/* 161 */     this.artifactId = artifactId;
/* 162 */     this.artifactVersion = artifactVersion;
/* 163 */     this.buildTimeMillis = buildTimeMillis;
/* 164 */     this.commitTimeMillis = commitTimeMillis;
/* 165 */     this.shortCommitHash = shortCommitHash;
/* 166 */     this.longCommitHash = longCommitHash;
/* 167 */     this.repositoryStatus = repositoryStatus;
/*     */   }
/*     */   
/*     */   public String artifactId() {
/* 171 */     return this.artifactId;
/*     */   }
/*     */   
/*     */   public String artifactVersion() {
/* 175 */     return this.artifactVersion;
/*     */   }
/*     */   
/*     */   public long buildTimeMillis() {
/* 179 */     return this.buildTimeMillis;
/*     */   }
/*     */   
/*     */   public long commitTimeMillis() {
/* 183 */     return this.commitTimeMillis;
/*     */   }
/*     */   
/*     */   public String shortCommitHash() {
/* 187 */     return this.shortCommitHash;
/*     */   }
/*     */   
/*     */   public String longCommitHash() {
/* 191 */     return this.longCommitHash;
/*     */   }
/*     */   
/*     */   public String repositoryStatus() {
/* 195 */     return this.repositoryStatus;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 200 */     return this.artifactId + '-' + this.artifactVersion + '.' + this.shortCommitHash + ("clean".equals(this.repositoryStatus) ? "" : new StringBuilder().append(" (repository: ").append(this.repositoryStatus).append(')').toString());
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\util\Version.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */