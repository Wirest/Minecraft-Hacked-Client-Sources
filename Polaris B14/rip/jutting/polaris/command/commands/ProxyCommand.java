/*    */ package rip.jutting.polaris.command.commands;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ import java.net.InetSocketAddress;
/*    */ import java.net.Proxy;
/*    */ import java.net.Proxy.Type;
/*    */ import java.net.Socket;
/*    */ import java.net.SocketAddress;
/*    */ import java.util.Properties;
/*    */ import rip.jutting.polaris.Polaris;
/*    */ import rip.jutting.polaris.command.Command;
/*    */ import rip.jutting.polaris.utils.MiscUtils;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ProxyCommand
/*    */   implements Command
/*    */ {
/*    */   public boolean run(String[] args)
/*    */   {
/* 21 */     if (args.length == 2) {
/* 22 */       if (args[1].contains(":"))
/*    */       {
/* 24 */         String ip = args[1].split(":")[0];
/* 25 */         String portSring = args[1].split(":")[1];
/* 26 */         if (!MiscUtils.isInteger(portSring)) {
/* 27 */           Polaris.sendMessage("Invalid port: " + portSring);
/*    */         }
/*    */         try {
/* 30 */           System.getProperties().put("proxySet", "true");
/* 31 */           System.setProperty("socksProxyHost", ip);
/* 32 */           System.setProperty("socksProxyPort", portSring);
/* 33 */           int parts2 = Integer.parseInt(portSring);
/* 34 */           SocketAddress proxyAddr = new InetSocketAddress(ip, parts2);
/* 35 */           Proxy proxy = new Proxy(Proxy.Type.SOCKS, proxyAddr);
/* 36 */           Socket socket = new Socket(proxy);
/* 37 */           InetSocketAddress dest = new InetSocketAddress(ip, parts2);
/* 38 */           socket.connect(dest);
/* 39 */           System.out.println(dest.getAddress() + ":" + dest.getPort());
/* 40 */           System.out.println(ip);
/* 41 */           System.out.println(portSring);
/* 42 */           socket.close();
/*    */         }
/*    */         catch (Exception e) {
/* 45 */           Polaris.sendMessage(e.getMessage());
/*    */         }
/* 47 */       } else if (args[0].equalsIgnoreCase("none"))
/*    */       {
/* 49 */         System.setProperty("socksProxyHost", "");
/* 50 */         System.setProperty("socksProxyPort", "");
/*    */       } else {
/* 52 */         Polaris.sendMessage("Not a proxy: " + args[1]); }
/* 53 */       Polaris.sendMessage("Proxy set to " + args[1]);
/*    */     }
/* 55 */     return true;
/*    */   }
/*    */   
/*    */   public String usage()
/*    */   {
/* 60 */     return "-proxy [ip:port]";
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\command\commands\ProxyCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */