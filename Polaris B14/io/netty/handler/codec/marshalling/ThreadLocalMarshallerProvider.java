/*    */ package io.netty.handler.codec.marshalling;
/*    */ 
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.util.concurrent.FastThreadLocal;
/*    */ import org.jboss.marshalling.Marshaller;
/*    */ import org.jboss.marshalling.MarshallerFactory;
/*    */ import org.jboss.marshalling.MarshallingConfiguration;
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
/*    */ public class ThreadLocalMarshallerProvider
/*    */   implements MarshallerProvider
/*    */ {
/* 31 */   private final FastThreadLocal<Marshaller> marshallers = new FastThreadLocal();
/*    */   
/*    */ 
/*    */   private final MarshallerFactory factory;
/*    */   
/*    */ 
/*    */   private final MarshallingConfiguration config;
/*    */   
/*    */ 
/*    */ 
/*    */   public ThreadLocalMarshallerProvider(MarshallerFactory factory, MarshallingConfiguration config)
/*    */   {
/* 43 */     this.factory = factory;
/* 44 */     this.config = config;
/*    */   }
/*    */   
/*    */   public Marshaller getMarshaller(ChannelHandlerContext ctx) throws Exception
/*    */   {
/* 49 */     Marshaller marshaller = (Marshaller)this.marshallers.get();
/* 50 */     if (marshaller == null) {
/* 51 */       marshaller = this.factory.createMarshaller(this.config);
/* 52 */       this.marshallers.set(marshaller);
/*    */     }
/* 54 */     return marshaller;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\marshalling\ThreadLocalMarshallerProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */