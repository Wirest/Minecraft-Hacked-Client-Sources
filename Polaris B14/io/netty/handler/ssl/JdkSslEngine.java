/*     */ package io.netty.handler.ssl;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import javax.net.ssl.SSLEngine;
/*     */ import javax.net.ssl.SSLEngineResult;
/*     */ import javax.net.ssl.SSLEngineResult.HandshakeStatus;
/*     */ import javax.net.ssl.SSLException;
/*     */ import javax.net.ssl.SSLParameters;
/*     */ import javax.net.ssl.SSLSession;
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
/*     */ class JdkSslEngine
/*     */   extends SSLEngine
/*     */ {
/*     */   private final SSLEngine engine;
/*     */   private final JdkSslSession session;
/*     */   
/*     */   JdkSslEngine(SSLEngine engine)
/*     */   {
/*  32 */     this.engine = engine;
/*  33 */     this.session = new JdkSslSession(engine);
/*     */   }
/*     */   
/*     */   public JdkSslSession getSession()
/*     */   {
/*  38 */     return this.session;
/*     */   }
/*     */   
/*     */   public SSLEngine getWrappedEngine() {
/*  42 */     return this.engine;
/*     */   }
/*     */   
/*     */   public void closeInbound() throws SSLException
/*     */   {
/*  47 */     this.engine.closeInbound();
/*     */   }
/*     */   
/*     */   public void closeOutbound()
/*     */   {
/*  52 */     this.engine.closeOutbound();
/*     */   }
/*     */   
/*     */   public String getPeerHost()
/*     */   {
/*  57 */     return this.engine.getPeerHost();
/*     */   }
/*     */   
/*     */   public int getPeerPort()
/*     */   {
/*  62 */     return this.engine.getPeerPort();
/*     */   }
/*     */   
/*     */   public SSLEngineResult wrap(ByteBuffer byteBuffer, ByteBuffer byteBuffer2) throws SSLException
/*     */   {
/*  67 */     return this.engine.wrap(byteBuffer, byteBuffer2);
/*     */   }
/*     */   
/*     */   public SSLEngineResult wrap(ByteBuffer[] byteBuffers, ByteBuffer byteBuffer) throws SSLException
/*     */   {
/*  72 */     return this.engine.wrap(byteBuffers, byteBuffer);
/*     */   }
/*     */   
/*     */   public SSLEngineResult wrap(ByteBuffer[] byteBuffers, int i, int i2, ByteBuffer byteBuffer) throws SSLException
/*     */   {
/*  77 */     return this.engine.wrap(byteBuffers, i, i2, byteBuffer);
/*     */   }
/*     */   
/*     */   public SSLEngineResult unwrap(ByteBuffer byteBuffer, ByteBuffer byteBuffer2) throws SSLException
/*     */   {
/*  82 */     return this.engine.unwrap(byteBuffer, byteBuffer2);
/*     */   }
/*     */   
/*     */   public SSLEngineResult unwrap(ByteBuffer byteBuffer, ByteBuffer[] byteBuffers) throws SSLException
/*     */   {
/*  87 */     return this.engine.unwrap(byteBuffer, byteBuffers);
/*     */   }
/*     */   
/*     */   public SSLEngineResult unwrap(ByteBuffer byteBuffer, ByteBuffer[] byteBuffers, int i, int i2) throws SSLException
/*     */   {
/*  92 */     return this.engine.unwrap(byteBuffer, byteBuffers, i, i2);
/*     */   }
/*     */   
/*     */   public Runnable getDelegatedTask()
/*     */   {
/*  97 */     return this.engine.getDelegatedTask();
/*     */   }
/*     */   
/*     */   public boolean isInboundDone()
/*     */   {
/* 102 */     return this.engine.isInboundDone();
/*     */   }
/*     */   
/*     */   public boolean isOutboundDone()
/*     */   {
/* 107 */     return this.engine.isOutboundDone();
/*     */   }
/*     */   
/*     */   public String[] getSupportedCipherSuites()
/*     */   {
/* 112 */     return this.engine.getSupportedCipherSuites();
/*     */   }
/*     */   
/*     */   public String[] getEnabledCipherSuites()
/*     */   {
/* 117 */     return this.engine.getEnabledCipherSuites();
/*     */   }
/*     */   
/*     */   public void setEnabledCipherSuites(String[] strings)
/*     */   {
/* 122 */     this.engine.setEnabledCipherSuites(strings);
/*     */   }
/*     */   
/*     */   public String[] getSupportedProtocols()
/*     */   {
/* 127 */     return this.engine.getSupportedProtocols();
/*     */   }
/*     */   
/*     */   public String[] getEnabledProtocols()
/*     */   {
/* 132 */     return this.engine.getEnabledProtocols();
/*     */   }
/*     */   
/*     */   public void setEnabledProtocols(String[] strings)
/*     */   {
/* 137 */     this.engine.setEnabledProtocols(strings);
/*     */   }
/*     */   
/*     */   public SSLSession getHandshakeSession()
/*     */   {
/* 142 */     return this.engine.getHandshakeSession();
/*     */   }
/*     */   
/*     */   public void beginHandshake() throws SSLException
/*     */   {
/* 147 */     this.engine.beginHandshake();
/*     */   }
/*     */   
/*     */   public SSLEngineResult.HandshakeStatus getHandshakeStatus()
/*     */   {
/* 152 */     return this.engine.getHandshakeStatus();
/*     */   }
/*     */   
/*     */   public void setUseClientMode(boolean b)
/*     */   {
/* 157 */     this.engine.setUseClientMode(b);
/*     */   }
/*     */   
/*     */   public boolean getUseClientMode()
/*     */   {
/* 162 */     return this.engine.getUseClientMode();
/*     */   }
/*     */   
/*     */   public void setNeedClientAuth(boolean b)
/*     */   {
/* 167 */     this.engine.setNeedClientAuth(b);
/*     */   }
/*     */   
/*     */   public boolean getNeedClientAuth()
/*     */   {
/* 172 */     return this.engine.getNeedClientAuth();
/*     */   }
/*     */   
/*     */   public void setWantClientAuth(boolean b)
/*     */   {
/* 177 */     this.engine.setWantClientAuth(b);
/*     */   }
/*     */   
/*     */   public boolean getWantClientAuth()
/*     */   {
/* 182 */     return this.engine.getWantClientAuth();
/*     */   }
/*     */   
/*     */   public void setEnableSessionCreation(boolean b)
/*     */   {
/* 187 */     this.engine.setEnableSessionCreation(b);
/*     */   }
/*     */   
/*     */   public boolean getEnableSessionCreation()
/*     */   {
/* 192 */     return this.engine.getEnableSessionCreation();
/*     */   }
/*     */   
/*     */   public SSLParameters getSSLParameters()
/*     */   {
/* 197 */     return this.engine.getSSLParameters();
/*     */   }
/*     */   
/*     */   public void setSSLParameters(SSLParameters sslParameters)
/*     */   {
/* 202 */     this.engine.setSSLParameters(sslParameters);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\ssl\JdkSslEngine.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */