/*    */ package rip.jutting.polaris.anticheat;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ 
/*    */ public class AnticheatManager
/*    */ {
/*    */   public Anticheat findAnticheat() {
/*  8 */     if (!Minecraft.getMinecraft().isSingleplayer()) { Anticheat[] arrayOfAnticheat;
/*  9 */       int j = (arrayOfAnticheat = Anticheat.values()).length; for (int i = 0; i < j; i++) { Anticheat ac = arrayOfAnticheat[i];
/* 10 */         String[] arrayOfString; int m = (arrayOfString = ac.getIps()).length; for (int k = 0; k < m; k++) { String ip = arrayOfString[k];
/* 11 */           if ((!ip.isEmpty()) && 
/* 12 */             (Minecraft.getMinecraft().getCurrentServerData().serverIP.toLowerCase().contains(ip))) {
/* 13 */             return ac;
/*    */           }
/*    */         }
/*    */       }
/*    */     }
/*    */     else {
/* 19 */       return Anticheat.SINGLEPLAYER;
/*    */     }
/* 21 */     return Anticheat.UNKNOWN;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\anticheat\AnticheatManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */