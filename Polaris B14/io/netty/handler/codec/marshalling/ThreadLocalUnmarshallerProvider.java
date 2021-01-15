/*    */ package io.netty.handler.codec.marshalling;
/*    */ 
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.util.concurrent.FastThreadLocal;
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
/*    */ public class ThreadLocalUnmarshallerProvider
/*    */   implements UnmarshallerProvider
/*    */ {
/* 31 */   private final FastThreadLocal<Unmarshaller> unmarshallers = new FastThreadLocal();
/*    */   
/*    */ 
/*    */   private final MarshallerFactory factory;
/*    */   
/*    */ 
/*    */   private final MarshallingConfiguration config;
/*    */   
/*    */ 
/*    */ 
/*    */   public ThreadLocalUnmarshallerProvider(MarshallerFactory factory, MarshallingConfiguration config)
/*    */   {
/* 43 */     this.factory = factory;
/* 44 */     this.config = config;
/*    */   }
/*    */   
/*    */   public Unmarshaller getUnmarshaller(ChannelHandlerContext ctx) throws Exception
/*    */   {
/* 49 */     Unmarshaller unmarshaller = (Unmarshaller)this.unmarshallers.get();
/* 50 */     if (unmarshaller == null) {
/* 51 */       unmarshaller = this.factory.createUnmarshaller(this.config);
/* 52 */       this.unmarshallers.set(unmarshaller);
/*    */     }
/* 54 */     return unmarshaller;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\marshalling\ThreadLocalUnmarshallerProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */