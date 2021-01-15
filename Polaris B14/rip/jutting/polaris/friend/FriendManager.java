/*    */ package rip.jutting.polaris.friend;
/*    */ 
/*    */ import com.mojang.authlib.GameProfile;
/*    */ import java.io.File;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.entity.EntityPlayerSP;
/*    */ import net.minecraft.util.StringUtils;
/*    */ import rip.jutting.polaris.utils.FileUtils;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FriendManager
/*    */ {
/* 17 */   private static File FRIEND_DIR = FileUtils.getConfigFile("Friends");
/* 18 */   public static ArrayList<Friend> friendsList = new ArrayList();
/*    */   
/*    */   public static void start()
/*    */   {
/* 22 */     load();
/* 23 */     save();
/*    */   }
/*    */   
/*    */   public static void addFriend(String name, String alias) {
/* 27 */     friendsList.add(new Friend(name, alias));
/* 28 */     save();
/*    */   }
/*    */   
/*    */   public static String getAliasName(String name) {
/* 32 */     String alias = "";
/* 33 */     for (Friend friend : friendsList) {
/* 34 */       if (friend.name.equalsIgnoreCase(StringUtils.stripControlCodes(name))) {
/* 35 */         alias = friend.alias;
/* 36 */         break;
/*    */       }
/*    */     }
/* 39 */     if ((Minecraft.getMinecraft().thePlayer != null) && (Minecraft.getMinecraft().thePlayer.getGameProfile().getName() == name)) {
/* 40 */       return name;
/*    */     }
/* 42 */     return alias;
/*    */   }
/*    */   
/*    */   public static void removeFriend(String name) {
/* 46 */     for (Friend friend : friendsList) {
/* 47 */       if (friend.name.equalsIgnoreCase(name)) {
/* 48 */         friendsList.remove(friend);
/* 49 */         break;
/*    */       }
/*    */     }
/* 52 */     save();
/*    */   }
/*    */   
/*    */   public static String replaceText(String text) {
/* 56 */     for (Friend friend : friendsList) {
/* 57 */       if (text.contains(friend.name)) {
/* 58 */         text = friend.alias;
/*    */       }
/*    */     }
/* 61 */     return text;
/*    */   }
/*    */   
/*    */   public static boolean isFriend(String name) {
/* 65 */     boolean isFriend = false;
/* 66 */     for (Friend friend : friendsList) {
/* 67 */       if (friend.name.equalsIgnoreCase(StringUtils.stripControlCodes(name))) {
/* 68 */         isFriend = true;
/* 69 */         break;
/*    */       }
/*    */     }
/* 72 */     if (Minecraft.getMinecraft().thePlayer.getGameProfile().getName() == name) {
/* 73 */       isFriend = true;
/*    */     }
/* 75 */     return isFriend;
/*    */   }
/*    */   
/*    */   public static void load() {
/* 79 */     friendsList.clear();
/* 80 */     List<String> fileContent = FileUtils.read(FRIEND_DIR);
/* 81 */     for (String line : fileContent) {
/*    */       try {
/* 83 */         String[] split = line.split(":");
/* 84 */         String name = split[0];
/* 85 */         String alias = split[1];
/* 86 */         addFriend(name, alias);
/*    */       }
/*    */       catch (Exception localException) {}
/*    */     }
/*    */   }
/*    */   
/*    */   public static void save() {
/* 93 */     List<String> fileContent = new ArrayList();
/* 94 */     for (Friend friend : friendsList) {
/* 95 */       String alias = getAliasName(friend.name);
/* 96 */       fileContent.add(String.format("%s:%s", new Object[] { friend.name, alias }));
/*    */     }
/* 98 */     FileUtils.write(FRIEND_DIR, fileContent, true);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\friend\FriendManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */