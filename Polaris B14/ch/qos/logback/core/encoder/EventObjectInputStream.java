/*     */ package ch.qos.logback.core.encoder;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EventObjectInputStream<E>
/*     */   extends InputStream
/*     */ {
/*     */   NonClosableInputStream ncis;
/*  36 */   List<E> buffer = new ArrayList();
/*     */   
/*  38 */   int index = 0;
/*     */   
/*     */   EventObjectInputStream(InputStream is) throws IOException {
/*  41 */     this.ncis = new NonClosableInputStream(is);
/*     */   }
/*     */   
/*     */   public int read() throws IOException
/*     */   {
/*  46 */     throw new UnsupportedOperationException("Only the readEvent method is supported.");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int available()
/*     */     throws IOException
/*     */   {
/*  54 */     return this.ncis.available();
/*     */   }
/*     */   
/*     */   public E readEvent() throws IOException
/*     */   {
/*  59 */     E event = getFromBuffer();
/*  60 */     if (event != null) {
/*  61 */       return event;
/*     */     }
/*     */     
/*  64 */     internalReset();
/*  65 */     int count = readHeader();
/*  66 */     if (count == -1) {
/*  67 */       return null;
/*     */     }
/*  69 */     readPayload(count);
/*  70 */     readFooter(count);
/*  71 */     return (E)getFromBuffer();
/*     */   }
/*     */   
/*     */   private void internalReset() {
/*  75 */     this.index = 0;
/*  76 */     this.buffer.clear();
/*     */   }
/*     */   
/*     */   E getFromBuffer() {
/*  80 */     if (this.index >= this.buffer.size()) {
/*  81 */       return null;
/*     */     }
/*  83 */     return (E)this.buffer.get(this.index++);
/*     */   }
/*     */   
/*     */   int readHeader() throws IOException {
/*  87 */     byte[] headerBA = new byte[16];
/*     */     
/*  89 */     int bytesRead = this.ncis.read(headerBA);
/*  90 */     if (bytesRead == -1) {
/*  91 */       return -1;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  97 */     int offset = 0;
/*  98 */     int startPebble = ByteArrayUtil.readInt(headerBA, offset);
/*  99 */     if (startPebble != 1853421169) {
/* 100 */       throw new IllegalStateException("Does not look like data created by ObjectStreamEncoder");
/*     */     }
/*     */     
/* 103 */     offset += 4;
/* 104 */     int count = ByteArrayUtil.readInt(headerBA, offset);
/* 105 */     offset += 4;
/* 106 */     int endPointer = ByteArrayUtil.readInt(headerBA, offset);
/* 107 */     offset += 4;
/* 108 */     int checksum = ByteArrayUtil.readInt(headerBA, offset);
/* 109 */     if (checksum != (0x6E78F671 ^ count)) {
/* 110 */       throw new IllegalStateException("Invalid checksum");
/*     */     }
/* 112 */     return count;
/*     */   }
/*     */   
/*     */   E readEvents(ObjectInputStream ois) throws IOException
/*     */   {
/* 117 */     E e = null;
/*     */     try {
/* 119 */       e = ois.readObject();
/* 120 */       this.buffer.add(e);
/*     */     }
/*     */     catch (ClassNotFoundException e1) {
/* 123 */       e1.printStackTrace();
/*     */     }
/* 125 */     return e;
/*     */   }
/*     */   
/*     */   void readFooter(int count) throws IOException {
/* 129 */     byte[] headerBA = new byte[8];
/* 130 */     this.ncis.read(headerBA);
/*     */     
/* 132 */     int offset = 0;
/* 133 */     int stopPebble = ByteArrayUtil.readInt(headerBA, offset);
/* 134 */     if (stopPebble != 640373619) {
/* 135 */       throw new IllegalStateException("Looks like a corrupt stream");
/*     */     }
/*     */     
/* 138 */     offset += 4;
/* 139 */     int checksum = ByteArrayUtil.readInt(headerBA, offset);
/* 140 */     if (checksum != (0x262B5373 ^ count)) {
/* 141 */       throw new IllegalStateException("Invalid checksum");
/*     */     }
/*     */   }
/*     */   
/*     */   void readPayload(int count) throws IOException {
/* 146 */     List<E> eventList = new ArrayList(count);
/* 147 */     ObjectInputStream ois = new ObjectInputStream(this.ncis);
/* 148 */     for (int i = 0; i < count; i++) {
/* 149 */       E e = readEvents(ois);
/* 150 */       eventList.add(e);
/*     */     }
/* 152 */     ois.close();
/*     */   }
/*     */   
/*     */   public void close() throws IOException {
/* 156 */     this.ncis.realClose();
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\encoder\EventObjectInputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */