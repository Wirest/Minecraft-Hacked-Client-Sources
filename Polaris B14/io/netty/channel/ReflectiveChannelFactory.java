/*    */ package io.netty.channel;
/*    */ 
/*    */ import io.netty.util.internal.StringUtil;
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
/*    */ public class ReflectiveChannelFactory<T extends Channel>
/*    */   implements ChannelFactory<T>
/*    */ {
/*    */   private final Class<? extends T> clazz;
/*    */   
/*    */   public ReflectiveChannelFactory(Class<? extends T> clazz)
/*    */   {
/* 29 */     if (clazz == null) {
/* 30 */       throw new NullPointerException("clazz");
/*    */     }
/* 32 */     this.clazz = clazz;
/*    */   }
/*    */   
/*    */   public T newChannel()
/*    */   {
/*    */     try {
/* 38 */       return (Channel)this.clazz.newInstance();
/*    */     } catch (Throwable t) {
/* 40 */       throw new ChannelException("Unable to create Channel from class " + this.clazz, t);
/*    */     }
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 46 */     return StringUtil.simpleClassName(this.clazz) + ".class";
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\ReflectiveChannelFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */