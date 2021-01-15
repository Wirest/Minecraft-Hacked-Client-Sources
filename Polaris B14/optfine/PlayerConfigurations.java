/*    */ package optfine;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import net.minecraft.client.entity.AbstractClientPlayer;
/*    */ import net.minecraft.client.model.ModelBiped;
/*    */ import net.minecraft.util.StringUtils;
/*    */ 
/*    */ public class PlayerConfigurations
/*    */ {
/* 11 */   private static Map mapConfigurations = null;
/*    */   
/*    */   public static void renderPlayerItems(ModelBiped p_renderPlayerItems_0_, AbstractClientPlayer p_renderPlayerItems_1_, float p_renderPlayerItems_2_, float p_renderPlayerItems_3_)
/*    */   {
/* 15 */     PlayerConfiguration playerconfiguration = getPlayerConfiguration(p_renderPlayerItems_1_);
/*    */     
/* 17 */     if (playerconfiguration != null)
/*    */     {
/* 19 */       playerconfiguration.renderPlayerItems(p_renderPlayerItems_0_, p_renderPlayerItems_1_, p_renderPlayerItems_2_, p_renderPlayerItems_3_);
/*    */     }
/*    */   }
/*    */   
/*    */   public static synchronized PlayerConfiguration getPlayerConfiguration(AbstractClientPlayer p_getPlayerConfiguration_0_)
/*    */   {
/* 25 */     String s = getPlayerName(p_getPlayerConfiguration_0_);
/* 26 */     PlayerConfiguration playerconfiguration = (PlayerConfiguration)getMapConfigurations().get(s);
/*    */     
/* 28 */     if (playerconfiguration == null)
/*    */     {
/* 30 */       playerconfiguration = new PlayerConfiguration();
/* 31 */       getMapConfigurations().put(s, playerconfiguration);
/* 32 */       PlayerConfigurationReceiver playerconfigurationreceiver = new PlayerConfigurationReceiver(s);
/* 33 */       String s1 = "http://s.optifine.net/users/" + s + ".cfg";
/* 34 */       FileDownloadThread filedownloadthread = new FileDownloadThread(s1, playerconfigurationreceiver);
/* 35 */       filedownloadthread.start();
/*    */     }
/*    */     
/* 38 */     return playerconfiguration;
/*    */   }
/*    */   
/*    */   public static synchronized void setPlayerConfiguration(String p_setPlayerConfiguration_0_, PlayerConfiguration p_setPlayerConfiguration_1_)
/*    */   {
/* 43 */     getMapConfigurations().put(p_setPlayerConfiguration_0_, p_setPlayerConfiguration_1_);
/*    */   }
/*    */   
/*    */   private static Map getMapConfigurations()
/*    */   {
/* 48 */     if (mapConfigurations == null)
/*    */     {
/* 50 */       mapConfigurations = new HashMap();
/*    */     }
/*    */     
/* 53 */     return mapConfigurations;
/*    */   }
/*    */   
/*    */   public static String getPlayerName(AbstractClientPlayer p_getPlayerName_0_)
/*    */   {
/* 58 */     String s = p_getPlayerName_0_.getName();
/*    */     
/* 60 */     if ((s != null) && (!s.isEmpty()))
/*    */     {
/* 62 */       s = StringUtils.stripControlCodes(s);
/*    */     }
/*    */     
/* 65 */     return s;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\optfine\PlayerConfigurations.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */