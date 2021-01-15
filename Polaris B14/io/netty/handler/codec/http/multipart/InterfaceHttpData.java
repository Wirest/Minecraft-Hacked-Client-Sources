/*    */ package io.netty.handler.codec.http.multipart;
/*    */ 
/*    */ import io.netty.util.ReferenceCounted;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract interface InterfaceHttpData
/*    */   extends Comparable<InterfaceHttpData>, ReferenceCounted
/*    */ {
/*    */   public abstract String getName();
/*    */   
/*    */   public abstract HttpDataType getHttpDataType();
/*    */   
/*    */   public abstract InterfaceHttpData retain();
/*    */   
/*    */   public abstract InterfaceHttpData retain(int paramInt);
/*    */   
/*    */   public abstract InterfaceHttpData touch();
/*    */   
/*    */   public abstract InterfaceHttpData touch(Object paramObject);
/*    */   
/*    */   public static enum HttpDataType
/*    */   {
/* 25 */     Attribute,  FileUpload,  InternalAttribute;
/*    */     
/*    */     private HttpDataType() {}
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\multipart\InterfaceHttpData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */