/*    */ package io.netty.channel;
/*    */ 
/*    */ import java.net.SocketAddress;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class AbstractServerChannel
/*    */   extends AbstractChannel
/*    */   implements ServerChannel
/*    */ {
/* 33 */   private static final ChannelMetadata METADATA = new ChannelMetadata(false);
/*    */   
/*    */ 
/*    */ 
/*    */   protected AbstractServerChannel()
/*    */   {
/* 39 */     super(null);
/*    */   }
/*    */   
/*    */   public ChannelMetadata metadata()
/*    */   {
/* 44 */     return METADATA;
/*    */   }
/*    */   
/*    */   public SocketAddress remoteAddress()
/*    */   {
/* 49 */     return null;
/*    */   }
/*    */   
/*    */   protected SocketAddress remoteAddress0()
/*    */   {
/* 54 */     return null;
/*    */   }
/*    */   
/*    */   protected void doDisconnect() throws Exception
/*    */   {
/* 59 */     throw new UnsupportedOperationException();
/*    */   }
/*    */   
/*    */   protected AbstractChannel.AbstractUnsafe newUnsafe()
/*    */   {
/* 64 */     return new DefaultServerUnsafe(null);
/*    */   }
/*    */   
/*    */   protected void doWrite(ChannelOutboundBuffer in) throws Exception
/*    */   {
/* 69 */     throw new UnsupportedOperationException();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/* 74 */   protected final Object filterOutboundMessage(Object msg) { throw new UnsupportedOperationException(); }
/*    */   
/*    */   private final class DefaultServerUnsafe extends AbstractChannel.AbstractUnsafe {
/* 77 */     private DefaultServerUnsafe() { super(); }
/*    */     
/*    */     public void connect(SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) {
/* 80 */       safeSetFailure(promise, new UnsupportedOperationException());
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\AbstractServerChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */