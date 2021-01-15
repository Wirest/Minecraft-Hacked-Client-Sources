/*     */ package net.minecraft.client.network;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.io.IOException;
/*     */ import java.net.DatagramPacket;
/*     */ import java.net.InetAddress;
/*     */ import java.net.MulticastSocket;
/*     */ import java.net.SocketTimeoutException;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.multiplayer.ThreadLanServerPing;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class LanServerDetector
/*     */ {
/*  19 */   private static final AtomicInteger field_148551_a = new AtomicInteger(0);
/*  20 */   private static final Logger logger = LogManager.getLogger();
/*     */   
/*     */   public static class LanServer
/*     */   {
/*     */     private String lanServerMotd;
/*     */     private String lanServerIpPort;
/*     */     private long timeLastSeen;
/*     */     
/*     */     public LanServer(String motd, String address)
/*     */     {
/*  30 */       this.lanServerMotd = motd;
/*  31 */       this.lanServerIpPort = address;
/*  32 */       this.timeLastSeen = Minecraft.getSystemTime();
/*     */     }
/*     */     
/*     */     public String getServerMotd()
/*     */     {
/*  37 */       return this.lanServerMotd;
/*     */     }
/*     */     
/*     */     public String getServerIpPort()
/*     */     {
/*  42 */       return this.lanServerIpPort;
/*     */     }
/*     */     
/*     */     public void updateLastSeen()
/*     */     {
/*  47 */       this.timeLastSeen = Minecraft.getSystemTime();
/*     */     }
/*     */   }
/*     */   
/*     */   public static class LanServerList
/*     */   {
/*  53 */     private List<LanServerDetector.LanServer> listOfLanServers = Lists.newArrayList();
/*     */     boolean wasUpdated;
/*     */     
/*     */     public synchronized boolean getWasUpdated()
/*     */     {
/*  58 */       return this.wasUpdated;
/*     */     }
/*     */     
/*     */     public synchronized void setWasNotUpdated()
/*     */     {
/*  63 */       this.wasUpdated = false;
/*     */     }
/*     */     
/*     */     public synchronized List<LanServerDetector.LanServer> getLanServers()
/*     */     {
/*  68 */       return Collections.unmodifiableList(this.listOfLanServers);
/*     */     }
/*     */     
/*     */     public synchronized void func_77551_a(String p_77551_1_, InetAddress p_77551_2_)
/*     */     {
/*  73 */       String s = ThreadLanServerPing.getMotdFromPingResponse(p_77551_1_);
/*  74 */       String s1 = ThreadLanServerPing.getAdFromPingResponse(p_77551_1_);
/*     */       
/*  76 */       if (s1 != null)
/*     */       {
/*  78 */         s1 = p_77551_2_.getHostAddress() + ":" + s1;
/*  79 */         boolean flag = false;
/*     */         
/*  81 */         for (LanServerDetector.LanServer lanserverdetector$lanserver : this.listOfLanServers)
/*     */         {
/*  83 */           if (lanserverdetector$lanserver.getServerIpPort().equals(s1))
/*     */           {
/*  85 */             lanserverdetector$lanserver.updateLastSeen();
/*  86 */             flag = true;
/*  87 */             break;
/*     */           }
/*     */         }
/*     */         
/*  91 */         if (!flag)
/*     */         {
/*  93 */           this.listOfLanServers.add(new LanServerDetector.LanServer(s, s1));
/*  94 */           this.wasUpdated = true;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static class ThreadLanServerFind extends Thread
/*     */   {
/*     */     private final LanServerDetector.LanServerList localServerList;
/*     */     private final InetAddress broadcastAddress;
/*     */     private final MulticastSocket socket;
/*     */     
/*     */     public ThreadLanServerFind(LanServerDetector.LanServerList p_i1320_1_) throws IOException
/*     */     {
/* 108 */       super();
/* 109 */       this.localServerList = p_i1320_1_;
/* 110 */       setDaemon(true);
/* 111 */       this.socket = new MulticastSocket(4445);
/* 112 */       this.broadcastAddress = InetAddress.getByName("224.0.2.60");
/* 113 */       this.socket.setSoTimeout(5000);
/* 114 */       this.socket.joinGroup(this.broadcastAddress);
/*     */     }
/*     */     
/*     */     public void run()
/*     */     {
/* 119 */       byte[] abyte = new byte['Ѐ'];
/*     */       
/* 121 */       while (!isInterrupted())
/*     */       {
/* 123 */         DatagramPacket datagrampacket = new DatagramPacket(abyte, abyte.length);
/*     */         
/*     */         try
/*     */         {
/* 127 */           this.socket.receive(datagrampacket);
/*     */         }
/*     */         catch (SocketTimeoutException var5)
/*     */         {
/*     */           continue;
/*     */         }
/*     */         catch (IOException ioexception)
/*     */         {
/* 135 */           LanServerDetector.logger.error("Couldn't ping server", ioexception);
/* 136 */           break;
/*     */         }
/*     */         
/* 139 */         String s = new String(datagrampacket.getData(), datagrampacket.getOffset(), datagrampacket.getLength());
/* 140 */         LanServerDetector.logger.debug(datagrampacket.getAddress() + ": " + s);
/* 141 */         this.localServerList.func_77551_a(s, datagrampacket.getAddress());
/*     */       }
/*     */       
/*     */       try
/*     */       {
/* 146 */         this.socket.leaveGroup(this.broadcastAddress);
/*     */       }
/*     */       catch (IOException localIOException1) {}
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 153 */       this.socket.close();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\network\LanServerDetector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */