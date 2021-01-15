/*     */ package io.netty.handler.ssl;
/*     */ 
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import java.util.Collections;
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
/*     */ public final class ApplicationProtocolConfig
/*     */ {
/*  33 */   public static final ApplicationProtocolConfig DISABLED = new ApplicationProtocolConfig();
/*     */   
/*     */ 
/*     */   private final List<String> supportedProtocols;
/*     */   
/*     */ 
/*     */   private final Protocol protocol;
/*     */   
/*     */ 
/*     */   private final SelectorFailureBehavior selectorBehavior;
/*     */   
/*     */   private final SelectedListenerFailureBehavior selectedBehavior;
/*     */   
/*     */ 
/*     */   public ApplicationProtocolConfig(Protocol protocol, SelectorFailureBehavior selectorBehavior, SelectedListenerFailureBehavior selectedBehavior, Iterable<String> supportedProtocols)
/*     */   {
/*  49 */     this(protocol, selectorBehavior, selectedBehavior, ApplicationProtocolUtil.toList(supportedProtocols));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ApplicationProtocolConfig(Protocol protocol, SelectorFailureBehavior selectorBehavior, SelectedListenerFailureBehavior selectedBehavior, String... supportedProtocols)
/*     */   {
/*  61 */     this(protocol, selectorBehavior, selectedBehavior, ApplicationProtocolUtil.toList(supportedProtocols));
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
/*     */   private ApplicationProtocolConfig(Protocol protocol, SelectorFailureBehavior selectorBehavior, SelectedListenerFailureBehavior selectedBehavior, List<String> supportedProtocols)
/*     */   {
/*  74 */     this.supportedProtocols = Collections.unmodifiableList((List)ObjectUtil.checkNotNull(supportedProtocols, "supportedProtocols"));
/*  75 */     this.protocol = ((Protocol)ObjectUtil.checkNotNull(protocol, "protocol"));
/*  76 */     this.selectorBehavior = ((SelectorFailureBehavior)ObjectUtil.checkNotNull(selectorBehavior, "selectorBehavior"));
/*  77 */     this.selectedBehavior = ((SelectedListenerFailureBehavior)ObjectUtil.checkNotNull(selectedBehavior, "selectedBehavior"));
/*     */     
/*  79 */     if (protocol == Protocol.NONE) {
/*  80 */       throw new IllegalArgumentException("protocol (" + Protocol.NONE + ") must not be " + Protocol.NONE + '.');
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private ApplicationProtocolConfig()
/*     */   {
/*  88 */     this.supportedProtocols = Collections.emptyList();
/*  89 */     this.protocol = Protocol.NONE;
/*  90 */     this.selectorBehavior = SelectorFailureBehavior.CHOOSE_MY_LAST_PROTOCOL;
/*  91 */     this.selectedBehavior = SelectedListenerFailureBehavior.ACCEPT;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static enum Protocol
/*     */   {
/*  98 */     NONE,  NPN,  ALPN,  NPN_AND_ALPN;
/*     */     
/*     */     private Protocol() {}
/*     */   }
/*     */   
/*     */   public static enum SelectorFailureBehavior
/*     */   {
/* 105 */     FATAL_ALERT,  NO_ADVERTISE,  CHOOSE_MY_LAST_PROTOCOL;
/*     */     
/*     */     private SelectorFailureBehavior() {}
/*     */   }
/*     */   
/*     */   public static enum SelectedListenerFailureBehavior
/*     */   {
/* 112 */     ACCEPT,  FATAL_ALERT,  CHOOSE_MY_LAST_PROTOCOL;
/*     */     
/*     */     private SelectedListenerFailureBehavior() {}
/*     */   }
/*     */   
/*     */   public List<String> supportedProtocols()
/*     */   {
/* 119 */     return this.supportedProtocols;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Protocol protocol()
/*     */   {
/* 126 */     return this.protocol;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public SelectorFailureBehavior selectorFailureBehavior()
/*     */   {
/* 133 */     return this.selectorBehavior;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public SelectedListenerFailureBehavior selectedListenerFailureBehavior()
/*     */   {
/* 140 */     return this.selectedBehavior;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\ssl\ApplicationProtocolConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */