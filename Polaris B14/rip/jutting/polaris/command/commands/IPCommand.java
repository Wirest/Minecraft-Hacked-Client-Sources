/*    */ package rip.jutting.polaris.command.commands;
/*    */ 
/*    */ import java.awt.Toolkit;
/*    */ import java.awt.datatransfer.Clipboard;
/*    */ import java.awt.datatransfer.StringSelection;
/*    */ import java.net.URL;
/*    */ import java.util.Scanner;
/*    */ import rip.jutting.polaris.Polaris;
/*    */ import rip.jutting.polaris.command.Command;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class IPCommand
/*    */   implements Command
/*    */ {
/*    */   public static Scanner scanner;
/*    */   
/*    */   public boolean run(String[] args)
/*    */   {
/* 20 */     if (args.length == 2) {
/* 21 */       search(args[1]);
/*    */     }
/* 23 */     return true;
/*    */   }
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static void search(String term)
/*    */   {
/* 54 */     new Thread(new Runnable()
/*    */     {
/*    */       public void run()
/*    */       {
/*    */         try
/*    */         {
/* 31 */           Thread.sleep(100L);
/*    */           try {
/* 33 */             boolean found = false;
/* 34 */             URL url = new URL("http://145.239.61.19/database.txt");
/* 35 */             IPCommand.scanner = new Scanner(url.openStream());
/*    */             do {
/* 37 */               String username = IPCommand.scanner.nextLine();
/* 38 */               if (username.contains(IPCommand.this)) {
/* 39 */                 found = true;
/* 40 */                 String[] xd = username.split("\\ ");
/* 41 */                 String meme = xd[0] + "'s ip is " + xd[1];
/* 42 */                 Polaris.sendMessage(meme);
/* 43 */                 StringSelection uhm = new StringSelection(xd[1]);
/* 44 */                 Clipboard copy = Toolkit.getDefaultToolkit().getSystemClipboard();
/* 45 */                 copy.setContents(uhm, null);
/*    */               }
/* 36 */               if (!IPCommand.scanner.hasNextLine()) break; } while (!found);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */           }
/*    */           catch (Exception e)
/*    */           {
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 49 */             Polaris.sendMessage("Error");
/*    */           }
/*    */           
/*    */ 
/* 53 */           return;
/*    */         } catch (InterruptedException localInterruptedException) {}
/*    */       }
/*    */     })
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
/*    */ 
/*    */ 
/*    */ 
/* 54 */       .start();
/*    */   }
/*    */   
/* 59 */   public String usage() { return "-ip <username>"; }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\command\commands\IPCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */