/*    */ package io.netty.handler.codec.compression;
/*    */ 
/*    */ import com.jcraft.jzlib.Deflater;
/*    */ import com.jcraft.jzlib.Inflater;
/*    */ import com.jcraft.jzlib.JZlib;
/*    */ import com.jcraft.jzlib.JZlib.WrapperType;
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
/*    */ final class ZlibUtil
/*    */ {
/*    */   static void fail(Inflater z, String message, int resultCode)
/*    */   {
/* 28 */     throw inflaterException(z, message, resultCode);
/*    */   }
/*    */   
/*    */   static void fail(Deflater z, String message, int resultCode) {
/* 32 */     throw deflaterException(z, message, resultCode);
/*    */   }
/*    */   
/*    */   static DecompressionException inflaterException(Inflater z, String message, int resultCode) {
/* 36 */     return new DecompressionException(message + " (" + resultCode + ')' + (z.msg != null ? ": " + z.msg : ""));
/*    */   }
/*    */   
/*    */   static CompressionException deflaterException(Deflater z, String message, int resultCode) {
/* 40 */     return new CompressionException(message + " (" + resultCode + ')' + (z.msg != null ? ": " + z.msg : ""));
/*    */   }
/*    */   
/*    */   static JZlib.WrapperType convertWrapperType(ZlibWrapper wrapper) {
/*    */     JZlib.WrapperType convertedWrapperType;
/* 45 */     switch (wrapper) {
/*    */     case NONE: 
/* 47 */       convertedWrapperType = JZlib.W_NONE;
/* 48 */       break;
/*    */     case ZLIB: 
/* 50 */       convertedWrapperType = JZlib.W_ZLIB;
/* 51 */       break;
/*    */     case GZIP: 
/* 53 */       convertedWrapperType = JZlib.W_GZIP;
/* 54 */       break;
/*    */     case ZLIB_OR_NONE: 
/* 56 */       convertedWrapperType = JZlib.W_ANY;
/* 57 */       break;
/*    */     default: 
/* 59 */       throw new Error();
/*    */     }
/* 61 */     return convertedWrapperType;
/*    */   }
/*    */   
/*    */   static int wrapperOverhead(ZlibWrapper wrapper) {
/*    */     int overhead;
/* 66 */     switch (wrapper) {
/*    */     case NONE: 
/* 68 */       overhead = 0;
/* 69 */       break;
/*    */     case ZLIB: 
/*    */     case ZLIB_OR_NONE: 
/* 72 */       overhead = 2;
/* 73 */       break;
/*    */     case GZIP: 
/* 75 */       overhead = 10;
/* 76 */       break;
/*    */     default: 
/* 78 */       throw new Error();
/*    */     }
/* 80 */     return overhead;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\compression\ZlibUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */