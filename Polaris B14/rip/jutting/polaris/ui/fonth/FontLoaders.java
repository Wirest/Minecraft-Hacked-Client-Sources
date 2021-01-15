/*    */ package rip.jutting.polaris.ui.fonth;
/*    */ 
/*    */ import java.awt.Font;
/*    */ import java.io.InputStream;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.resources.IResource;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public abstract class FontLoaders
/*    */ {
/* 11 */   public static CFontRenderer vardana12 = new CFontRenderer(getSansation(17), true, true);
/* 12 */   public static CFontRenderer verdana12 = new CFontRenderer(new Font("Verdana", 0, 17), true, true);
/* 13 */   public static CFontRenderer chat1 = new CFontRenderer(getSansation(16), true, true);
/* 14 */   public static CFontRenderer chat2 = new CFontRenderer(getSansation(16), true, true);
/* 15 */   public static CFontRenderer chat = new CFontRenderer(getSansation(16), true, true);
/* 16 */   public static CFontRenderer hud = new CFontRenderer(getPacifico(25), true, true);
/* 17 */   public static CFontRenderer mainmenu = new CFontRenderer(getPacifico(38), true, true);
/*    */   
/* 19 */   public static CFontRenderer raleway = new CFontRenderer(getRaleway(17), true, true);
/*    */   
/*    */   private static Font getRaleway(int size) {
/*    */     Font font;
/*    */     try {
/* 24 */       InputStream is = Minecraft.getMinecraft().getResourceManager()
/* 25 */         .getResource(new ResourceLocation("polaris/Raleway.ttf")).getInputStream();
/* 26 */       Font font = Font.createFont(0, is);
/* 27 */       font = font.deriveFont(0, size);
/*    */     } catch (Exception ex) {
/* 29 */       ex.printStackTrace();
/* 30 */       System.out.println("Error loading font");
/* 31 */       font = new Font("default", 0, size);
/*    */     }
/* 33 */     return font;
/*    */   }
/*    */   
/*    */   private static Font getPacifico(int size) {
/*    */     Font font;
/*    */     try {
/* 39 */       InputStream is = Minecraft.getMinecraft().getResourceManager()
/* 40 */         .getResource(new ResourceLocation("polaris/Pacifico.ttf")).getInputStream();
/* 41 */       Font font = Font.createFont(0, is);
/* 42 */       font = font.deriveFont(0, size);
/*    */     } catch (Exception ex) {
/* 44 */       ex.printStackTrace();
/* 45 */       System.out.println("Error loading font");
/* 46 */       font = new Font("default", 0, size);
/*    */     }
/* 48 */     return font;
/*    */   }
/*    */   
/*    */   private static Font getSansation(int size) {
/*    */     Font font;
/*    */     try {
/* 54 */       InputStream is = Minecraft.getMinecraft().getResourceManager()
/* 55 */         .getResource(new ResourceLocation("polaris/Sansation.ttf")).getInputStream();
/* 56 */       Font font = Font.createFont(0, is);
/* 57 */       font = font.deriveFont(0, size);
/*    */     } catch (Exception ex) {
/* 59 */       ex.printStackTrace();
/* 60 */       System.out.println("Error loading font");
/* 61 */       font = new Font("default", 0, size);
/*    */     }
/* 63 */     return font;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\ui\fonth\FontLoaders.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */