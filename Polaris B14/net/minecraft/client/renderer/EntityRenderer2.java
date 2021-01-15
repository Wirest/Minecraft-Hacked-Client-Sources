/*    */ package net.minecraft.client.renderer;
/*    */ 
/*    */ import java.util.concurrent.Callable;
/*    */ import net.minecraft.client.Minecraft;
/*    */ 
/*    */ class EntityRenderer2 implements Callable
/*    */ {
/*    */   final EntityRenderer field_90025_c;
/*    */   private static final String __OBFID = "CL_00000948";
/*    */   
/*    */   EntityRenderer2(EntityRenderer p_i46419_1_)
/*    */   {
/* 13 */     this.field_90025_c = p_i46419_1_;
/*    */   }
/*    */   
/*    */   public String call() throws Exception
/*    */   {
/* 18 */     return Minecraft.getMinecraft().currentScreen.getClass().getCanonicalName();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\EntityRenderer2.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */