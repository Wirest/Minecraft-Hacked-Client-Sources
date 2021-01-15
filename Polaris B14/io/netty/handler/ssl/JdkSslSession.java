/*     */ package io.netty.handler.ssl;
/*     */ 
/*     */ import java.security.Principal;
/*     */ import java.security.cert.Certificate;
/*     */ import javax.net.ssl.SSLEngine;
/*     */ import javax.net.ssl.SSLPeerUnverifiedException;
/*     */ import javax.net.ssl.SSLSession;
/*     */ import javax.net.ssl.SSLSessionContext;
/*     */ import javax.security.cert.X509Certificate;
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
/*     */ final class JdkSslSession
/*     */   implements SSLSession
/*     */ {
/*     */   private final SSLEngine engine;
/*     */   private volatile String applicationProtocol;
/*     */   
/*     */   JdkSslSession(SSLEngine engine)
/*     */   {
/*  32 */     this.engine = engine;
/*     */   }
/*     */   
/*     */   void setApplicationProtocol(String applicationProtocol) {
/*  36 */     if (applicationProtocol != null) {
/*  37 */       applicationProtocol = applicationProtocol.replace(':', '_');
/*     */     }
/*  39 */     this.applicationProtocol = applicationProtocol;
/*     */   }
/*     */   
/*     */   public String getProtocol()
/*     */   {
/*  44 */     String protocol = unwrap().getProtocol();
/*  45 */     String applicationProtocol = this.applicationProtocol;
/*     */     
/*  47 */     if (applicationProtocol == null) {
/*  48 */       if (protocol != null) {
/*  49 */         return protocol.replace(':', '_');
/*     */       }
/*  51 */       return null;
/*     */     }
/*     */     
/*     */ 
/*  55 */     StringBuilder buf = new StringBuilder(32);
/*  56 */     if (protocol != null) {
/*  57 */       buf.append(protocol.replace(':', '_'));
/*  58 */       buf.append(':');
/*     */     } else {
/*  60 */       buf.append("null:");
/*     */     }
/*  62 */     buf.append(applicationProtocol);
/*  63 */     return buf.toString();
/*     */   }
/*     */   
/*     */   private SSLSession unwrap() {
/*  67 */     return this.engine.getSession();
/*     */   }
/*     */   
/*     */   public byte[] getId()
/*     */   {
/*  72 */     return unwrap().getId();
/*     */   }
/*     */   
/*     */   public SSLSessionContext getSessionContext()
/*     */   {
/*  77 */     return unwrap().getSessionContext();
/*     */   }
/*     */   
/*     */   public long getCreationTime()
/*     */   {
/*  82 */     return unwrap().getCreationTime();
/*     */   }
/*     */   
/*     */   public long getLastAccessedTime()
/*     */   {
/*  87 */     return unwrap().getLastAccessedTime();
/*     */   }
/*     */   
/*     */   public void invalidate()
/*     */   {
/*  92 */     unwrap().invalidate();
/*     */   }
/*     */   
/*     */   public boolean isValid()
/*     */   {
/*  97 */     return unwrap().isValid();
/*     */   }
/*     */   
/*     */   public void putValue(String s, Object o)
/*     */   {
/* 102 */     unwrap().putValue(s, o);
/*     */   }
/*     */   
/*     */   public Object getValue(String s)
/*     */   {
/* 107 */     return unwrap().getValue(s);
/*     */   }
/*     */   
/*     */   public void removeValue(String s)
/*     */   {
/* 112 */     unwrap().removeValue(s);
/*     */   }
/*     */   
/*     */   public String[] getValueNames()
/*     */   {
/* 117 */     return unwrap().getValueNames();
/*     */   }
/*     */   
/*     */   public Certificate[] getPeerCertificates() throws SSLPeerUnverifiedException
/*     */   {
/* 122 */     return unwrap().getPeerCertificates();
/*     */   }
/*     */   
/*     */   public Certificate[] getLocalCertificates()
/*     */   {
/* 127 */     return unwrap().getLocalCertificates();
/*     */   }
/*     */   
/*     */   public X509Certificate[] getPeerCertificateChain() throws SSLPeerUnverifiedException
/*     */   {
/* 132 */     return unwrap().getPeerCertificateChain();
/*     */   }
/*     */   
/*     */   public Principal getPeerPrincipal() throws SSLPeerUnverifiedException
/*     */   {
/* 137 */     return unwrap().getPeerPrincipal();
/*     */   }
/*     */   
/*     */   public Principal getLocalPrincipal()
/*     */   {
/* 142 */     return unwrap().getLocalPrincipal();
/*     */   }
/*     */   
/*     */   public String getCipherSuite()
/*     */   {
/* 147 */     return unwrap().getCipherSuite();
/*     */   }
/*     */   
/*     */   public String getPeerHost()
/*     */   {
/* 152 */     return unwrap().getPeerHost();
/*     */   }
/*     */   
/*     */   public int getPeerPort()
/*     */   {
/* 157 */     return unwrap().getPeerPort();
/*     */   }
/*     */   
/*     */   public int getPacketBufferSize()
/*     */   {
/* 162 */     return unwrap().getPacketBufferSize();
/*     */   }
/*     */   
/*     */   public int getApplicationBufferSize()
/*     */   {
/* 167 */     return unwrap().getApplicationBufferSize();
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\ssl\JdkSslSession.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */