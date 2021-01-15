/*    */ package rip.jutting.polaris.module.other;
/*    */ 
/*    */ import java.io.InputStreamReader;
/*    */ import java.net.URL;
/*    */ import java.net.URLConnection;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import java.util.Scanner;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.multiplayer.WorldClient;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import rip.jutting.polaris.Polaris;
/*    */ import rip.jutting.polaris.event.EventTarget;
/*    */ import rip.jutting.polaris.event.events.EventUpdate;
/*    */ import rip.jutting.polaris.module.Category;
/*    */ import rip.jutting.polaris.module.Module;
/*    */ 
/*    */ 
/*    */ public class StaffAlerts
/*    */   extends Module
/*    */ {
/* 23 */   private static List<String> staff = new ArrayList();
/*    */   
/*    */   private static Scanner scanner;
/* 26 */   public boolean isStaff = false;
/*    */   
/*    */   public StaffAlerts() {
/* 29 */     super("StaffAlerts", 0, Category.OTHER);
/*    */   }
/*    */   
/*    */   public void onEnable()
/*    */   {
/* 34 */     checkStaff();
/* 35 */     this.isStaff = false;
/* 36 */     super.onEnable();
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onUpdate(EventUpdate event) {
/* 41 */     if (mc.theWorld.playerEntities != null) { Iterator localIterator2;
/* 42 */       for (Iterator localIterator1 = mc.theWorld.playerEntities.iterator(); localIterator1.hasNext(); 
/*    */           
/* 44 */           localIterator2.hasNext())
/*    */       {
/* 42 */         Object object = localIterator1.next();
/* 43 */         EntityPlayer entityPlayer = (EntityPlayer)object;
/* 44 */         localIterator2 = staff.iterator(); continue;String staffxd = (String)localIterator2.next();
/* 45 */         if ((entityPlayer != null) && (entityPlayer.getName().equalsIgnoreCase(staffxd)) && (!this.isStaff)) {
/* 46 */           Polaris.sendMessage(entityPlayer.getName() + " is staff!");
/* 47 */           this.isStaff = true;
/* 48 */           staff.clear();
/*    */         }
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   public void checkStaff()
/*    */   {
/*    */     try {
/* 57 */       URLConnection openConnection = new URL("http://54.36.24.226/names.txt").openConnection();
/* 58 */       openConnection.addRequestProperty("User-Agent", 
/* 59 */         "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
/*    */       
/* 61 */       scanner = new Scanner(new InputStreamReader(openConnection.getInputStream()));
/*    */       
/* 63 */       while (scanner.hasNextLine()) {
/* 64 */         String meme = scanner.nextLine();
/* 65 */         if ((!meme.contains(":")) && (!meme.contains("(")) && (meme.length() > 1)) {
/* 66 */           staff.add(meme);
/*    */         }
/*    */       }
/*    */       
/* 70 */       scanner.close();
/*    */     } catch (Exception e) {
/* 72 */       e.printStackTrace();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\module\other\StaffAlerts.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */