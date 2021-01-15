/*    */ package io.netty.handler.codec.serialization;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.ObjectOutputStream;
/*    */ import java.io.ObjectStreamClass;
/*    */ import java.io.OutputStream;
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
/*    */ class CompactObjectOutputStream
/*    */   extends ObjectOutputStream
/*    */ {
/*    */   static final int TYPE_FAT_DESCRIPTOR = 0;
/*    */   static final int TYPE_THIN_DESCRIPTOR = 1;
/*    */   
/*    */   CompactObjectOutputStream(OutputStream out)
/*    */     throws IOException
/*    */   {
/* 29 */     super(out);
/*    */   }
/*    */   
/*    */   protected void writeStreamHeader() throws IOException
/*    */   {
/* 34 */     writeByte(5);
/*    */   }
/*    */   
/*    */   protected void writeClassDescriptor(ObjectStreamClass desc) throws IOException
/*    */   {
/* 39 */     Class<?> clazz = desc.forClass();
/* 40 */     if ((clazz.isPrimitive()) || (clazz.isArray()) || (clazz.isInterface()) || (desc.getSerialVersionUID() == 0L))
/*    */     {
/* 42 */       write(0);
/* 43 */       super.writeClassDescriptor(desc);
/*    */     } else {
/* 45 */       write(1);
/* 46 */       writeUTF(desc.getName());
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\serialization\CompactObjectOutputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */