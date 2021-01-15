/*    */ package rip.jutting.polaris.module.other;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.entity.EntityPlayerSP;
/*    */ import rip.jutting.polaris.Polaris;
/*    */ import rip.jutting.polaris.module.Module;
/*    */ 
/*    */ public class AutoPlay extends Module
/*    */ {
/*    */   public AutoPlay()
/*    */   {
/* 13 */     super("AutoPlay", 0, rip.jutting.polaris.module.Category.OTHER);
/*    */   }
/*    */   
/*    */   public void setup()
/*    */   {
/* 18 */     ArrayList<String> options = new ArrayList();
/* 19 */     options.add("Solo Normal");
/* 20 */     options.add("Solo Insane");
/* 21 */     options.add("Teams Normal");
/* 22 */     options.add("Teams Insane");
/* 23 */     Polaris.instance.settingsManager.rSetting(new rip.jutting.polaris.ui.click.settings.Setting("Game Mode", this, "Solo Insane", options));
/*    */   }
/*    */   
/*    */   public void onEnable()
/*    */   {
/* 28 */     String mode = Polaris.instance.settingsManager.getSettingByName("Game Mode").getValString();
/* 29 */     if (mode.equalsIgnoreCase("Solo Normal")) {
/* 30 */       mc.thePlayer.sendChatMessage("/play solo_normal");
/*    */     }
/* 32 */     if (mode.equalsIgnoreCase("Solo Insane")) {
/* 33 */       mc.thePlayer.sendChatMessage("/play solo_insane");
/*    */     }
/* 35 */     if (mode.equalsIgnoreCase("Teams Normal")) {
/* 36 */       mc.thePlayer.sendChatMessage("/play teams_normal");
/*    */     }
/* 38 */     if (mode.equalsIgnoreCase("Teams Insane")) {
/* 39 */       mc.thePlayer.sendChatMessage("/play teams_insane");
/*    */     }
/* 41 */     toggle();
/* 42 */     super.onEnable();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\module\other\AutoPlay.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */