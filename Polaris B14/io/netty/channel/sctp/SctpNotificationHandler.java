/*    */ package io.netty.channel.sctp;
/*    */ 
/*    */ import com.sun.nio.sctp.AbstractNotificationHandler;
/*    */ import com.sun.nio.sctp.AssociationChangeNotification;
/*    */ import com.sun.nio.sctp.HandlerResult;
/*    */ import com.sun.nio.sctp.Notification;
/*    */ import com.sun.nio.sctp.PeerAddressChangeNotification;
/*    */ import com.sun.nio.sctp.SendFailedNotification;
/*    */ import com.sun.nio.sctp.ShutdownNotification;
/*    */ import io.netty.channel.ChannelPipeline;
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
/*    */ public final class SctpNotificationHandler
/*    */   extends AbstractNotificationHandler<Object>
/*    */ {
/*    */   private final SctpChannel sctpChannel;
/*    */   
/*    */   public SctpNotificationHandler(SctpChannel sctpChannel)
/*    */   {
/* 38 */     if (sctpChannel == null) {
/* 39 */       throw new NullPointerException("sctpChannel");
/*    */     }
/* 41 */     this.sctpChannel = sctpChannel;
/*    */   }
/*    */   
/*    */   public HandlerResult handleNotification(AssociationChangeNotification notification, Object o)
/*    */   {
/* 46 */     fireEvent(notification);
/* 47 */     return HandlerResult.CONTINUE;
/*    */   }
/*    */   
/*    */   public HandlerResult handleNotification(PeerAddressChangeNotification notification, Object o)
/*    */   {
/* 52 */     fireEvent(notification);
/* 53 */     return HandlerResult.CONTINUE;
/*    */   }
/*    */   
/*    */   public HandlerResult handleNotification(SendFailedNotification notification, Object o)
/*    */   {
/* 58 */     fireEvent(notification);
/* 59 */     return HandlerResult.CONTINUE;
/*    */   }
/*    */   
/*    */   public HandlerResult handleNotification(ShutdownNotification notification, Object o)
/*    */   {
/* 64 */     fireEvent(notification);
/* 65 */     this.sctpChannel.close();
/* 66 */     return HandlerResult.RETURN;
/*    */   }
/*    */   
/*    */   private void fireEvent(Notification notification) {
/* 70 */     this.sctpChannel.pipeline().fireUserEventTriggered(notification);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\sctp\SctpNotificationHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */