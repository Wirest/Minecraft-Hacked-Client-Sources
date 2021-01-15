/*    */ package io.netty.handler.codec.marshalling;
/*    */ 
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.util.Attribute;
/*    */ import io.netty.util.AttributeKey;
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
/*    */ public class ContextBoundUnmarshallerProvider
/*    */   extends DefaultUnmarshallerProvider
/*    */ {
/* 37 */   private static final AttributeKey<Unmarshaller> UNMARSHALLER = AttributeKey.valueOf(ContextBoundUnmarshallerProvider.class, "UNMARSHALLER");
/*    */   
/*    */   public ContextBoundUnmarshallerProvider(MarshallerFactory factory, MarshallingConfiguration config)
/*    */   {
/* 41 */     super(factory, config);
/*    */   }
/*    */   
/*    */   public Unmarshaller getUnmarshaller(ChannelHandlerContext ctx) throws Exception
/*    */   {
/* 46 */     Attribute<Unmarshaller> attr = ctx.attr(UNMARSHALLER);
/* 47 */     Unmarshaller unmarshaller = (Unmarshaller)attr.get();
/* 48 */     if (unmarshaller == null) {
/* 49 */       unmarshaller = super.getUnmarshaller(ctx);
/* 50 */       attr.set(unmarshaller);
/*    */     }
/* 52 */     return unmarshaller;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\marshalling\ContextBoundUnmarshallerProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */