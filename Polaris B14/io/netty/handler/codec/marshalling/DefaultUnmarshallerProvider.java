/*    */ package io.netty.handler.codec.marshalling;
/*    */ 
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import org.jboss.marshalling.MarshallerFactory;
/*    */ import org.jboss.marshalling.MarshallingConfiguration;
/*    */ import org.jboss.marshalling.Unmarshaller;
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
/*    */ 
/*    */ public class DefaultUnmarshallerProvider
/*    */   implements UnmarshallerProvider
/*    */ {
/*    */   private final MarshallerFactory factory;
/*    */   private final MarshallingConfiguration config;
/*    */   
/*    */   public DefaultUnmarshallerProvider(MarshallerFactory factory, MarshallingConfiguration config)
/*    */   {
/* 41 */     this.factory = factory;
/* 42 */     this.config = config;
/*    */   }
/*    */   
/*    */   public Unmarshaller getUnmarshaller(ChannelHandlerContext ctx) throws Exception
/*    */   {
/* 47 */     return this.factory.createUnmarshaller(this.config);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\marshalling\DefaultUnmarshallerProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */