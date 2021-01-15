/*    */ package io.netty.channel;
/*    */ 
/*    */ import java.net.ConnectException;
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
/*    */ public class ConnectTimeoutException
/*    */   extends ConnectException
/*    */ {
/*    */   private static final long serialVersionUID = 2317065249988317463L;
/*    */   
/*    */   public ConnectTimeoutException(String msg)
/*    */   {
/* 28 */     super(msg);
/*    */   }
/*    */   
/*    */   public ConnectTimeoutException() {}
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\ConnectTimeoutException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */