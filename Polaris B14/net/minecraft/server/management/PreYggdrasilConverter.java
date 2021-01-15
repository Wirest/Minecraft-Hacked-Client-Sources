/*    */ package net.minecraft.server.management;
/*    */ 
/*    */ import com.google.common.base.Predicate;
/*    */ import com.google.common.collect.Iterators;
/*    */ import com.google.common.collect.Lists;
/*    */ import com.mojang.authlib.GameProfile;
/*    */ import com.mojang.authlib.GameProfileRepository;
/*    */ import com.mojang.authlib.ProfileLookupCallback;
/*    */ import java.io.File;
/*    */ import java.util.Collection;
/*    */ import java.util.List;
/*    */ import java.util.UUID;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.util.StringUtils;
/*    */ import org.apache.logging.log4j.LogManager;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ 
/*    */ public class PreYggdrasilConverter
/*    */ {
/* 21 */   private static final Logger LOGGER = ;
/* 22 */   public static final File OLD_IPBAN_FILE = new File("banned-ips.txt");
/* 23 */   public static final File OLD_PLAYERBAN_FILE = new File("banned-players.txt");
/* 24 */   public static final File OLD_OPS_FILE = new File("ops.txt");
/* 25 */   public static final File OLD_WHITELIST_FILE = new File("white-list.txt");
/*    */   
/*    */   private static void lookupNames(MinecraftServer server, Collection<String> names, ProfileLookupCallback callback)
/*    */   {
/* 29 */     String[] astring = (String[])Iterators.toArray(Iterators.filter(names.iterator(), new Predicate()
/*    */     {
/*    */       public boolean apply(String p_apply_1_)
/*    */       {
/* 33 */         return !StringUtils.isNullOrEmpty(p_apply_1_);
/*    */       }
/* 35 */     }), String.class);
/*    */     
/* 37 */     if (server.isServerInOnlineMode())
/*    */     {
/* 39 */       server.getGameProfileRepository().findProfilesByNames(astring, com.mojang.authlib.Agent.MINECRAFT, callback);
/*    */     }
/*    */     else {
/*    */       String[] arrayOfString1;
/* 43 */       int j = (arrayOfString1 = astring).length; for (int i = 0; i < j; i++) { String s = arrayOfString1[i];
/*    */         
/* 45 */         UUID uuid = EntityPlayer.getUUID(new GameProfile(null, s));
/* 46 */         GameProfile gameprofile = new GameProfile(uuid, s);
/* 47 */         callback.onProfileLookupSucceeded(gameprofile);
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   public static String getStringUUIDFromName(String p_152719_0_)
/*    */   {
/* 54 */     if ((!StringUtils.isNullOrEmpty(p_152719_0_)) && (p_152719_0_.length() <= 16))
/*    */     {
/* 56 */       MinecraftServer minecraftserver = MinecraftServer.getServer();
/* 57 */       GameProfile gameprofile = minecraftserver.getPlayerProfileCache().getGameProfileForUsername(p_152719_0_);
/*    */       
/* 59 */       if ((gameprofile != null) && (gameprofile.getId() != null))
/*    */       {
/* 61 */         return gameprofile.getId().toString();
/*    */       }
/* 63 */       if ((!minecraftserver.isSinglePlayer()) && (minecraftserver.isServerInOnlineMode()))
/*    */       {
/* 65 */         final List<GameProfile> list = Lists.newArrayList();
/* 66 */         ProfileLookupCallback profilelookupcallback = new ProfileLookupCallback()
/*    */         {
/*    */           public void onProfileLookupSucceeded(GameProfile p_onProfileLookupSucceeded_1_)
/*    */           {
/* 70 */             PreYggdrasilConverter.this.getPlayerProfileCache().addEntry(p_onProfileLookupSucceeded_1_);
/* 71 */             list.add(p_onProfileLookupSucceeded_1_);
/*    */           }
/*    */           
/*    */           public void onProfileLookupFailed(GameProfile p_onProfileLookupFailed_1_, Exception p_onProfileLookupFailed_2_) {
/* 75 */             PreYggdrasilConverter.LOGGER.warn("Could not lookup user whitelist entry for " + p_onProfileLookupFailed_1_.getName(), p_onProfileLookupFailed_2_);
/*    */           }
/* 77 */         };
/* 78 */         lookupNames(minecraftserver, Lists.newArrayList(new String[] { p_152719_0_ }), profilelookupcallback);
/* 79 */         return (list.size() > 0) && (((GameProfile)list.get(0)).getId() != null) ? ((GameProfile)list.get(0)).getId().toString() : "";
/*    */       }
/*    */       
/*    */ 
/* 83 */       return EntityPlayer.getUUID(new GameProfile(null, p_152719_0_)).toString();
/*    */     }
/*    */     
/*    */ 
/*    */ 
/* 88 */     return p_152719_0_;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\server\management\PreYggdrasilConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */