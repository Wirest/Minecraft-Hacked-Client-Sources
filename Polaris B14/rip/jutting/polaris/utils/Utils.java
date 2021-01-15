/*     */ package rip.jutting.polaris.utils;
/*     */ 
/*     */ import java.security.MessageDigest;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockSlab;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.entity.EntityPlayerSP;
/*     */ import net.minecraft.client.settings.GameSettings;
/*     */ import net.minecraft.client.settings.KeyBinding;
/*     */ import net.minecraft.inventory.Container;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.item.ItemSword;
/*     */ import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
/*     */ 
/*     */ public class Utils
/*     */ {
/*  19 */   public static boolean fuck = true;
/*  20 */   private static Minecraft mc = Minecraft.getMinecraft();
/*     */   
/*     */   public static boolean isContainerEmpty(Container container) {
/*  23 */     int i = 0; for (int slotAmount = container.inventorySlots.size() == 90 ? 54 : 27; i < slotAmount; i++) {
/*  24 */       if (container.getSlot(i).getHasStack()) {
/*  25 */         return false;
/*     */       }
/*     */     }
/*  28 */     return true;
/*     */   }
/*     */   
/*     */   public static Minecraft getMinecraft() {
/*  32 */     return mc;
/*     */   }
/*     */   
/*     */   public static boolean canBlock() {
/*  36 */     if (mc == null) {
/*  37 */       mc = Minecraft.getMinecraft();
/*     */     }
/*  39 */     if (mc.thePlayer.getHeldItem() == null)
/*  40 */       return false;
/*  41 */     if ((mc.thePlayer.isBlocking()) || ((mc.thePlayer.isUsingItem()) && ((mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword))))
/*  42 */       return true;
/*  43 */     if (((mc.thePlayer.getHeldItem().getItem() instanceof ItemSword)) && (Minecraft.getMinecraft().gameSettings.keyBindUseItem.isPressed())) {
/*  44 */       return true;
/*     */     }
/*  46 */     return false;
/*     */   }
/*     */   
/*     */   public static String getMD5(String input) {
/*  50 */     StringBuilder res = new StringBuilder();
/*     */     try {
/*  52 */       MessageDigest algorithm = MessageDigest.getInstance("MD5");
/*  53 */       algorithm.reset();
/*  54 */       algorithm.update(input.getBytes());
/*  55 */       byte[] md5 = algorithm.digest();
/*     */       byte[] arrayOfByte1;
/*  57 */       int j = (arrayOfByte1 = md5).length; for (int i = 0; i < j; i++) { byte aMd5 = arrayOfByte1[i];
/*  58 */         String tmp = Integer.toHexString(0xFF & aMd5);
/*  59 */         if (tmp.length() == 1) {
/*  60 */           res.append("0").append(tmp);
/*     */         } else {
/*  62 */           res.append(tmp);
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {}
/*  67 */     return res.toString();
/*     */   }
/*     */   
/*     */   public static void breakAnticheats() {
/*  71 */     mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX + mc.thePlayer.motionX, mc.thePlayer.posY - 110.0D, mc.thePlayer.posZ + mc.thePlayer.motionZ, true));
/*     */   }
/*     */   
/*     */   public static int add(int number, int add, int max) {
/*  75 */     return number + add > max ? max : number + add;
/*     */   }
/*     */   
/*     */   public static int remove(int number, int remove, int min) {
/*  79 */     return number - remove < min ? min : number - remove;
/*     */   }
/*     */   
/*     */   public static int check(int number) {
/*  83 */     return number > 255 ? 255 : number <= 0 ? 1 : number;
/*     */   }
/*     */   
/*     */   public static double getDist() {
/*  87 */     double distance = 0.0D;
/*  88 */     for (double i = mc.thePlayer.posY; i > 0.0D; i -= 0.1D) {
/*  89 */       if (i < 0.0D) {
/*     */         break;
/*     */       }
/*  92 */       Block block = mc.theWorld.getBlockState(new net.minecraft.util.BlockPos(mc.thePlayer.posX, i, mc.thePlayer.posZ)).getBlock();
/*  93 */       if ((block.getMaterial() != net.minecraft.block.material.Material.air) && (block.isCollidable()) && ((block.isFullBlock()) || ((block instanceof BlockSlab)) || ((block instanceof net.minecraft.block.BlockBarrier)) || ((block instanceof net.minecraft.block.BlockStairs)) || 
/*  94 */         ((block instanceof net.minecraft.block.BlockGlass)) || ((block instanceof net.minecraft.block.BlockStainedGlass))))
/*     */       {
/*  96 */         if ((block instanceof BlockSlab)) {
/*  97 */           i -= 0.5D;
/*     */         }
/*  99 */         distance = i;
/* 100 */         break;
/*     */       }
/*     */     }
/* 103 */     return mc.thePlayer.posY - distance;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\utils\Utils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */