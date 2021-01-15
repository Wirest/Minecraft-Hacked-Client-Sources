/*     */ package net.minecraft.client.multiplayer;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.DatagramPacket;
/*     */ import java.net.DatagramSocket;
/*     */ import java.net.InetAddress;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class ThreadLanServerPing extends Thread
/*     */ {
/*  13 */   private static final AtomicInteger field_148658_a = new AtomicInteger(0);
/*  14 */   private static final Logger logger = LogManager.getLogger();
/*     */   
/*     */   private final String motd;
/*     */   
/*     */   private final DatagramSocket socket;
/*  19 */   private boolean isStopping = true;
/*     */   private final String address;
/*     */   
/*     */   public ThreadLanServerPing(String p_i1321_1_, String p_i1321_2_) throws IOException
/*     */   {
/*  24 */     super("LanServerPinger #" + field_148658_a.incrementAndGet());
/*  25 */     this.motd = p_i1321_1_;
/*  26 */     this.address = p_i1321_2_;
/*  27 */     setDaemon(true);
/*  28 */     this.socket = new DatagramSocket();
/*     */   }
/*     */   
/*     */   public void run()
/*     */   {
/*  33 */     String s = getPingResponse(this.motd, this.address);
/*  34 */     byte[] abyte = s.getBytes();
/*     */     
/*  36 */     while ((!isInterrupted()) && (this.isStopping))
/*     */     {
/*     */       try
/*     */       {
/*  40 */         InetAddress inetaddress = InetAddress.getByName("224.0.2.60");
/*  41 */         DatagramPacket datagrampacket = new DatagramPacket(abyte, abyte.length, inetaddress, 4445);
/*  42 */         this.socket.send(datagrampacket);
/*     */       }
/*     */       catch (IOException ioexception)
/*     */       {
/*  46 */         logger.warn("LanServerPinger: " + ioexception.getMessage());
/*  47 */         break;
/*     */       }
/*     */       
/*     */       try
/*     */       {
/*  52 */         sleep(1500L);
/*     */       }
/*     */       catch (InterruptedException localInterruptedException) {}
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void interrupt()
/*     */   {
/*  63 */     super.interrupt();
/*  64 */     this.isStopping = false;
/*     */   }
/*     */   
/*     */   public static String getPingResponse(String p_77525_0_, String p_77525_1_)
/*     */   {
/*  69 */     return "[MOTD]" + p_77525_0_ + "[/MOTD][AD]" + p_77525_1_ + "[/AD]";
/*     */   }
/*     */   
/*     */   public static String getMotdFromPingResponse(String p_77524_0_)
/*     */   {
/*  74 */     int i = p_77524_0_.indexOf("[MOTD]");
/*     */     
/*  76 */     if (i < 0)
/*     */     {
/*  78 */       return "missing no";
/*     */     }
/*     */     
/*     */ 
/*  82 */     int j = p_77524_0_.indexOf("[/MOTD]", i + "[MOTD]".length());
/*  83 */     return j < i ? "missing no" : p_77524_0_.substring(i + "[MOTD]".length(), j);
/*     */   }
/*     */   
/*     */ 
/*     */   public static String getAdFromPingResponse(String p_77523_0_)
/*     */   {
/*  89 */     int i = p_77523_0_.indexOf("[/MOTD]");
/*     */     
/*  91 */     if (i < 0)
/*     */     {
/*  93 */       return null;
/*     */     }
/*     */     
/*     */ 
/*  97 */     int j = p_77523_0_.indexOf("[/MOTD]", i + "[/MOTD]".length());
/*     */     
/*  99 */     if (j >= 0)
/*     */     {
/* 101 */       return null;
/*     */     }
/*     */     
/*     */ 
/* 105 */     int k = p_77523_0_.indexOf("[AD]", i + "[/MOTD]".length());
/*     */     
/* 107 */     if (k < 0)
/*     */     {
/* 109 */       return null;
/*     */     }
/*     */     
/*     */ 
/* 113 */     int l = p_77523_0_.indexOf("[/AD]", k + "[AD]".length());
/* 114 */     return l < k ? null : p_77523_0_.substring(k + "[AD]".length(), l);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\multiplayer\ThreadLanServerPing.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */