/*    */ package optfine;
/*    */ 
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonParser;
/*    */ 
/*    */ public class PlayerConfigurationReceiver implements IFileDownloadListener
/*    */ {
/*  8 */   private String player = null;
/*    */   
/*    */   public PlayerConfigurationReceiver(String p_i48_1_)
/*    */   {
/* 12 */     this.player = p_i48_1_;
/*    */   }
/*    */   
/*    */   public void fileDownloadFinished(String p_fileDownloadFinished_1_, byte[] p_fileDownloadFinished_2_, Throwable p_fileDownloadFinished_3_)
/*    */   {
/* 17 */     if (p_fileDownloadFinished_2_ != null)
/*    */     {
/*    */       try
/*    */       {
/* 21 */         String s = new String(p_fileDownloadFinished_2_, "ASCII");
/* 22 */         JsonParser jsonparser = new JsonParser();
/* 23 */         JsonElement jsonelement = jsonparser.parse(s);
/* 24 */         PlayerConfigurationParser playerconfigurationparser = new PlayerConfigurationParser(this.player);
/* 25 */         PlayerConfiguration playerconfiguration = playerconfigurationparser.parsePlayerConfiguration(jsonelement);
/*    */         
/* 27 */         if (playerconfiguration != null)
/*    */         {
/* 29 */           playerconfiguration.setInitialized(true);
/* 30 */           PlayerConfigurations.setPlayerConfiguration(this.player, playerconfiguration);
/*    */         }
/*    */       }
/*    */       catch (Exception exception)
/*    */       {
/* 35 */         exception.printStackTrace();
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\optfine\PlayerConfigurationReceiver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */