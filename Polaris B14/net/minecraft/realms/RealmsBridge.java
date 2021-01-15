/*    */ package net.minecraft.realms;
/*    */ 
/*    */ import java.lang.reflect.Constructor;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.GuiScreen;
/*    */ import org.apache.logging.log4j.LogManager;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ 
/*    */ public class RealmsBridge extends RealmsScreen
/*    */ {
/* 11 */   private static final Logger LOGGER = ;
/*    */   private GuiScreen previousScreen;
/*    */   
/*    */   public void switchToRealms(GuiScreen p_switchToRealms_1_)
/*    */   {
/* 16 */     this.previousScreen = p_switchToRealms_1_;
/*    */     
/*    */     try
/*    */     {
/* 20 */       Class<?> oclass = Class.forName("com.mojang.realmsclient.RealmsMainScreen");
/* 21 */       Constructor<?> constructor = oclass.getDeclaredConstructor(new Class[] { RealmsScreen.class });
/* 22 */       constructor.setAccessible(true);
/* 23 */       Object object = constructor.newInstance(new Object[] { this });
/* 24 */       Minecraft.getMinecraft().displayGuiScreen(((RealmsScreen)object).getProxy());
/*    */     }
/*    */     catch (Exception exception)
/*    */     {
/* 28 */       LOGGER.error("Realms module missing", exception);
/*    */     }
/*    */   }
/*    */   
/*    */   public void init()
/*    */   {
/* 34 */     Minecraft.getMinecraft().displayGuiScreen(this.previousScreen);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\realms\RealmsBridge.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */