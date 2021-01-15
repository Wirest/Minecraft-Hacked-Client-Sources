/*    */ package ch.qos.logback.core.encoder;
/*    */ 
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.ObjectOutputStream;
/*    */ import java.io.OutputStream;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
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
/*    */ public class ObjectStreamEncoder<E>
/*    */   extends EncoderBase<E>
/*    */ {
/*    */   public static final int START_PEBBLE = 1853421169;
/*    */   public static final int STOP_PEBBLE = 640373619;
/* 37 */   private int MAX_BUFFER_SIZE = 100;
/*    */   
/* 39 */   List<E> bufferList = new ArrayList(this.MAX_BUFFER_SIZE);
/*    */   
/*    */   public void doEncode(E event) throws IOException {
/* 42 */     this.bufferList.add(event);
/* 43 */     if (this.bufferList.size() == this.MAX_BUFFER_SIZE) {
/* 44 */       writeBuffer();
/*    */     }
/*    */   }
/*    */   
/*    */   void writeHeader(ByteArrayOutputStream baos, int bufferSize) {
/* 49 */     ByteArrayUtil.writeInt(baos, 1853421169);
/* 50 */     ByteArrayUtil.writeInt(baos, bufferSize);
/* 51 */     ByteArrayUtil.writeInt(baos, 0);
/* 52 */     ByteArrayUtil.writeInt(baos, 0x6E78F671 ^ bufferSize);
/*    */   }
/*    */   
/*    */   void writeFooter(ByteArrayOutputStream baos, int bufferSize) {
/* 56 */     ByteArrayUtil.writeInt(baos, 640373619);
/* 57 */     ByteArrayUtil.writeInt(baos, 0x262B5373 ^ bufferSize);
/*    */   }
/*    */   
/* 60 */   void writeBuffer() throws IOException { ByteArrayOutputStream baos = new ByteArrayOutputStream(10000);
/*    */     
/* 62 */     int size = this.bufferList.size();
/* 63 */     writeHeader(baos, size);
/* 64 */     ObjectOutputStream oos = new ObjectOutputStream(baos);
/* 65 */     for (E e : this.bufferList) {
/* 66 */       oos.writeObject(e);
/*    */     }
/* 68 */     this.bufferList.clear();
/* 69 */     oos.flush();
/*    */     
/* 71 */     writeFooter(baos, size);
/*    */     
/* 73 */     byte[] byteArray = baos.toByteArray();
/* 74 */     oos.close();
/* 75 */     writeEndPosition(byteArray);
/* 76 */     this.outputStream.write(byteArray);
/*    */   }
/*    */   
/*    */   void writeEndPosition(byte[] byteArray)
/*    */   {
/* 81 */     int offset = 8;
/* 82 */     ByteArrayUtil.writeInt(byteArray, offset, byteArray.length - offset);
/*    */   }
/*    */   
/*    */   public void init(OutputStream os) throws IOException
/*    */   {
/* 87 */     super.init(os);
/* 88 */     this.bufferList.clear();
/*    */   }
/*    */   
/*    */   public void close() throws IOException {
/* 92 */     writeBuffer();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\encoder\ObjectStreamEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */