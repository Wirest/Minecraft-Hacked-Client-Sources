/*    */ package ch.qos.logback.core.net;
/*    */ 
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ import java.net.DatagramPacket;
/*    */ import java.net.DatagramSocket;
/*    */ import java.net.InetAddress;
/*    */ import java.net.SocketException;
/*    */ import java.net.UnknownHostException;
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
/*    */ public class SyslogOutputStream
/*    */   extends OutputStream
/*    */ {
/*    */   private static final int MAX_LEN = 1024;
/*    */   private InetAddress address;
/*    */   private DatagramSocket ds;
/* 39 */   private ByteArrayOutputStream baos = new ByteArrayOutputStream();
/*    */   private final int port;
/*    */   
/*    */   public SyslogOutputStream(String syslogHost, int port) throws UnknownHostException, SocketException
/*    */   {
/* 44 */     this.address = InetAddress.getByName(syslogHost);
/* 45 */     this.port = port;
/* 46 */     this.ds = new DatagramSocket();
/*    */   }
/*    */   
/*    */   public void write(byte[] byteArray, int offset, int len) throws IOException {
/* 50 */     this.baos.write(byteArray, offset, len);
/*    */   }
/*    */   
/*    */   public void flush() throws IOException {
/* 54 */     byte[] bytes = this.baos.toByteArray();
/* 55 */     DatagramPacket packet = new DatagramPacket(bytes, bytes.length, this.address, this.port);
/*    */     
/*    */ 
/*    */ 
/* 59 */     if (this.baos.size() > 1024) {
/* 60 */       this.baos = new ByteArrayOutputStream();
/*    */     } else {
/* 62 */       this.baos.reset();
/*    */     }
/*    */     
/*    */ 
/*    */ 
/* 67 */     if (bytes.length == 0) {
/* 68 */       return;
/*    */     }
/* 70 */     if (this.ds != null) {
/* 71 */       this.ds.send(packet);
/*    */     }
/*    */   }
/*    */   
/*    */   public void close()
/*    */   {
/* 77 */     this.address = null;
/* 78 */     this.ds = null;
/*    */   }
/*    */   
/*    */   public int getPort() {
/* 82 */     return this.port;
/*    */   }
/*    */   
/*    */   public void write(int b) throws IOException
/*    */   {
/* 87 */     this.baos.write(b);
/*    */   }
/*    */   
/*    */   int getSendBufferSize() throws SocketException {
/* 91 */     return this.ds.getSendBufferSize();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\net\SyslogOutputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */