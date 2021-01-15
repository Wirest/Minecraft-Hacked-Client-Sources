/*    */ package rip.jutting.polaris.utils;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.entity.EntityPlayerSP;
/*    */ import net.minecraft.client.gui.ScaledResolution;
/*    */ import net.minecraft.client.multiplayer.WorldClient;
/*    */ import net.minecraft.client.network.NetHandlerPlayClient;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraft.network.Packet;
/*    */ import org.apache.http.util.EntityUtils;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Helper
/*    */ {
/*    */   private static EntityUtils entityUtils;
/* 25 */   private static RenderUtils.R2DUtils r2DUtils = new RenderUtils.R2DUtils();
/* 26 */   private static RenderUtils.R3DUtils r3DUtils = new RenderUtils.R3DUtils();
/* 27 */   private static RenderUtils.ColorUtils colorUtils = new RenderUtils.ColorUtils();
/*    */   private static MathUtils mathUtils;
/*    */   
/*    */   protected static List<Entity> getLoadedEntities() {
/* 31 */     return Minecraft.getMinecraft().theWorld.loadedEntityList;
/*    */   }
/*    */   
/*    */   public static boolean hasArmor(EntityPlayer player) {
/* 35 */     if (player.inventory == null) {
/* 36 */       return false;
/*    */     }
/* 38 */     ItemStack boots = player.inventory.armorInventory[0];
/* 39 */     ItemStack pants = player.inventory.armorInventory[1];
/* 40 */     ItemStack chest = player.inventory.armorInventory[2];
/* 41 */     ItemStack head = player.inventory.armorInventory[3];
/* 42 */     return (boots != null) || (pants != null) || (chest != null) || (head != null);
/*    */   }
/*    */   
/*    */   public static Minecraft mc() {
/* 46 */     return Minecraft.getMinecraft();
/*    */   }
/*    */   
/*    */   public static EntityPlayerSP player() {
/* 50 */     return mc().thePlayer;
/*    */   }
/*    */   
/*    */   public static WorldClient world() {
/* 54 */     return mc().theWorld;
/*    */   }
/*    */   
/*    */   public static EntityUtils entityUtils() {
/* 58 */     return entityUtils;
/*    */   }
/*    */   
/*    */   public static ScaledResolution scaled() {
/* 62 */     return new ScaledResolution(mc(), mc().displayWidth, mc().displayHeight);
/*    */   }
/*    */   
/*    */   public static RenderUtils.R2DUtils get2DUtils() {
/* 66 */     return r2DUtils;
/*    */   }
/*    */   
/*    */   public static RenderUtils.R3DUtils get3DUtils() {
/* 70 */     return r3DUtils;
/*    */   }
/*    */   
/*    */   public static RenderUtils.ColorUtils colorUtils() {
/* 74 */     return colorUtils;
/*    */   }
/*    */   
/*    */   public static MathUtils mathUtils() {
/* 78 */     return mathUtils;
/*    */   }
/*    */   
/*    */   public static void sendPacket(Packet p) {
/* 82 */     mc().getNetHandler().addToSendQueue(p);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\utils\Helper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */