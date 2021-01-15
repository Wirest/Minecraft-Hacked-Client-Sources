/*    */ import java.io.File;
/*    */ 
/*    */ public class InjectionAPI
/*    */ {
/*    */   public static void inject() throws Exception
/*    */   {
/*  7 */     String userHome = System.getProperty("user.home", ".");
/*    */     File workingDirectory;
/*  9 */     File workingDirectory; File workingDirectory; File workingDirectory; switch (getPlatform()) {
/*    */     case LINUX: 
/* 11 */       workingDirectory = new File(userHome, ".minecraft/");
/* 12 */       break;
/*    */     case SOLARIS: 
/* 14 */       String applicationData = System.getenv("APPDATA");
/* 15 */       String folder = applicationData != null ? applicationData : userHome;
/* 16 */       workingDirectory = new File(folder, ".minecraft/");
/* 17 */       break;
/*    */     case UNKNOWN: 
/* 19 */       workingDirectory = new File(userHome, "Library/Application Support/minecraft");
/* 20 */       break;
/*    */     case MACOS: default: 
/* 22 */       workingDirectory = new File(userHome, "minecraft/");
/*    */     }
/*    */     
/* 25 */     net.minecraft.client.main.Main.main(new String[] { "--version", "Polaris", 
/* 26 */       "--accessToken", "0", 
/* 27 */       "--assetIndex", "1.8", 
/* 28 */       "--userProperties", "{}", 
/* 29 */       "--gameDir", new File(workingDirectory, ".").getAbsolutePath(), 
/* 30 */       "--assetsDir", new File(workingDirectory, "assets/").getAbsolutePath() });
/*    */   }
/*    */   
/*    */   public static InjectionAPI.OS getPlatform()
/*    */   {
/* 35 */     String s = System.getProperty("os.name").toLowerCase();
/* 36 */     return s.contains("unix") ? InjectionAPI.OS.LINUX : s.contains("linux") ? InjectionAPI.OS.LINUX : s.contains("sunos") ? InjectionAPI.OS.SOLARIS : s.contains("solaris") ? InjectionAPI.OS.SOLARIS : s.contains("mac") ? InjectionAPI.OS.MACOS : s.contains("win") ? InjectionAPI.OS.WINDOWS : InjectionAPI.OS.UNKNOWN;
/*    */   }
/*    */   
/*    */   public static enum OS {
/* 40 */     LINUX, 
/* 41 */     SOLARIS, 
/* 42 */     WINDOWS, 
/* 43 */     MACOS, 
/* 44 */     UNKNOWN;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\InjectionAPI.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */