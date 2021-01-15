/*     */ package rip.jutting.polaris.module.render;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.nio.FloatBuffer;
/*     */ import java.nio.IntBuffer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.entity.EntityPlayerSP;
/*     */ import net.minecraft.client.gui.FontRenderer;
/*     */ import net.minecraft.client.gui.ScaledResolution;
/*     */ import net.minecraft.client.multiplayer.WorldClient;
/*     */ import net.minecraft.client.renderer.EntityRenderer;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.RenderHelper;
/*     */ import net.minecraft.client.renderer.entity.RenderItem;
/*     */ import net.minecraft.client.renderer.entity.RenderManager;
/*     */ import net.minecraft.client.settings.GameSettings;
/*     */ import net.minecraft.enchantment.Enchantment;
/*     */ import net.minecraft.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.item.EnumRarity;
/*     */ import net.minecraft.item.ItemArmor;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import net.minecraft.util.Timer;
/*     */ import org.lwjgl.BufferUtils;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import org.lwjgl.util.glu.GLU;
/*     */ import rip.jutting.polaris.Polaris;
/*     */ import rip.jutting.polaris.event.EventTarget;
/*     */ import rip.jutting.polaris.event.events.Event2D;
/*     */ import rip.jutting.polaris.event.events.Event3D;
/*     */ import rip.jutting.polaris.event.events.EventNametags;
/*     */ import rip.jutting.polaris.friend.FriendManager;
/*     */ import rip.jutting.polaris.module.Module;
/*     */ import rip.jutting.polaris.ui.click.settings.Setting;
/*     */ import rip.jutting.polaris.ui.click.settings.SettingsManager;
/*     */ import rip.jutting.polaris.utils.MathUtils;
/*     */ import rip.jutting.polaris.utils.RotationUtils;
/*     */ 
/*     */ public class Nametags extends Module
/*     */ {
/*     */   private boolean hideInvisibles;
/*     */   private double gradualFOVModifier;
/*  49 */   private Character formatChar = new Character('§');
/*  50 */   public static Map<net.minecraft.entity.EntityLivingBase, double[]> entityPositions = new java.util.HashMap();
/*     */   
/*  52 */   public double color = 70.0D;
/*     */   
/*     */   public Nametags() {
/*  55 */     super("Nametags", 0, rip.jutting.polaris.module.Category.RENDER);
/*     */   }
/*     */   
/*     */   public void setup()
/*     */   {
/*  60 */     Polaris.instance.settingsManager.rSetting(new Setting("Scale", this, 1.0D, 0.0D, 5.0D, false));
/*  61 */     Polaris.instance.settingsManager.rSetting(new Setting("Armor", this, true));
/*     */   }
/*     */   
/*     */   @EventTarget
/*     */   public void onEvent(EventNametags event) {
/*  66 */     event.setCancelled(true);
/*     */   }
/*     */   
/*     */   @EventTarget
/*     */   public void on3DRender(Event3D event) {
/*  71 */     updatePositions();
/*     */   }
/*     */   
/*     */   @EventTarget
/*     */   public void on2DRender(Event2D event) {
/*  76 */     GlStateManager.pushMatrix();
/*  77 */     ScaledResolution scaledRes = new ScaledResolution(mc, mc.displayWidth, 
/*  78 */       mc.displayHeight);
/*  79 */     double twoDscale = scaledRes.getScaleFactor() / Math.pow(scaledRes.getScaleFactor(), 2.0D);
/*  80 */     GlStateManager.scale(twoDscale, twoDscale, twoDscale);
/*  81 */     for (Entity ent : entityPositions.keySet()) {
/*  82 */       GlStateManager.pushMatrix();
/*  83 */       if (ent != mc.thePlayer)
/*     */       {
/*     */ 
/*  86 */         if (((ent instanceof EntityPlayer)) && (!rip.jutting.polaris.module.combat.AntiBot.bot.contains(ent))) {
/*  87 */           String str = ent.getDisplayName().getFormattedText();
/*  88 */           if (FriendManager.isFriend(ent.getName())) {
/*  89 */             str = str.replace(ent.getDisplayName().getFormattedText(), 
/*  90 */               "§a" + FriendManager.getAliasName(ent.getName()));
/*     */           }
/*  92 */           String colorString = this.formatChar.toString();
/*  93 */           double health = MathUtils.roundToPlace(((EntityPlayer)ent).getHealth() / 2.0F, 2);
/*  94 */           if (health >= 6.0D) {
/*  95 */             colorString = String.valueOf(colorString) + "2";
/*  96 */           } else if (health >= 3.5D) {
/*  97 */             colorString = String.valueOf(colorString) + "6";
/*     */           } else {
/*  99 */             colorString = String.valueOf(colorString) + "4";
/*     */           }
/* 101 */           double[] renderPositions = (double[])entityPositions.get(ent);
/* 102 */           str = String.valueOf(str) + " " + colorString + health;
/* 103 */           if ((renderPositions[3] < 0.0D) || (renderPositions[3] >= 1.0D)) {
/* 104 */             GlStateManager.popMatrix();
/* 105 */             continue;
/*     */           }
/* 107 */           GlStateManager.translate(renderPositions[0], renderPositions[1], 0.0D);
/* 108 */           scale(ent);
/* 109 */           GlStateManager.translate(0.0D, -2.5D, 0.0D);
/* 110 */           int strWidth = mc.fontRendererObj.getStringWidth(str);
/* 111 */           rip.jutting.polaris.utils.RenderUtils.R2DUtils.drawBorderedRect(-strWidth / 2 - 3, -12.0D, strWidth / 2 + 3, 1.0D, 1.0D, new Color(0, 0, 0, 50).getRGB(), new Color(0, 0, 0, 50).getRGB());
/* 112 */           GlStateManager.color(0.0F, 0.0F, 0.0F);
/* 113 */           mc.fontRendererObj.drawStringWithShadow(str, -strWidth / 2, -9.0D, -1);
/* 114 */           GlStateManager.color(1.0F, 1.0F, 1.0F);
/* 115 */           if (Polaris.instance.settingsManager.getSettingByName("Armor").getValBoolean()) {
/* 116 */             List<ItemStack> itemsToRender = new ArrayList();
/* 117 */             for (int i = 0; i < 5; i++) {
/* 118 */               ItemStack stack = ((EntityPlayer)ent).getEquipmentInSlot(i);
/* 119 */               if (stack != null) {
/* 120 */                 itemsToRender.add(stack);
/*     */               }
/*     */             }
/* 123 */             int x = -(itemsToRender.size() * 9);
/* 124 */             Iterator<ItemStack> iterator2 = itemsToRender.iterator();
/* 125 */             while (iterator2.hasNext()) {
/* 126 */               ItemStack stack = (ItemStack)iterator2.next();
/* 127 */               RenderHelper.enableGUIStandardItemLighting();
/* 128 */               mc.getRenderItem().renderItemIntoGUI(stack, x, -30);
/* 129 */               mc.getRenderItem().renderItemOverlays(mc.fontRendererObj, stack, x, 
/* 130 */                 -30);
/* 131 */               x += 2;
/* 132 */               RenderHelper.disableStandardItemLighting();
/* 133 */               String text = "";
/* 134 */               if (stack != null) {
/* 135 */                 int y = 21;
/* 136 */                 int sLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, 
/* 137 */                   stack);
/* 138 */                 int fLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, 
/* 139 */                   stack);
/* 140 */                 int kLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.knockback.effectId, 
/* 141 */                   stack);
/* 142 */                 if (sLevel > 0) {
/* 143 */                   drawEnchantTag("Sh" + sLevel, x, y);
/* 144 */                   y -= 9;
/*     */                 }
/* 146 */                 if (fLevel > 0) {
/* 147 */                   drawEnchantTag("Fir" + fLevel, x, y);
/* 148 */                   y -= 9;
/*     */                 }
/* 150 */                 if (kLevel > 0) {
/* 151 */                   drawEnchantTag("Kb" + kLevel, x, y);
/* 152 */                 } else if ((stack.getItem() instanceof ItemArmor)) {
/* 153 */                   int pLevel = 
/* 154 */                     EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack);
/* 155 */                   int tLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.thorns.effectId, 
/* 156 */                     stack);
/* 157 */                   int uLevel = 
/* 158 */                     EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack);
/* 159 */                   if (pLevel > 0) {
/* 160 */                     drawEnchantTag("P" + pLevel, x, y);
/* 161 */                     y -= 9;
/*     */                   }
/* 163 */                   if (tLevel > 0) {
/* 164 */                     drawEnchantTag("Th" + tLevel, x, y);
/* 165 */                     y -= 9;
/*     */                   }
/* 167 */                   if (uLevel > 0) {
/* 168 */                     drawEnchantTag("Unb" + uLevel, x, y);
/*     */                   }
/* 170 */                 } else if ((stack.getItem() instanceof net.minecraft.item.ItemBow)) {
/* 171 */                   int powLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, 
/* 172 */                     stack);
/* 173 */                   int punLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, 
/* 174 */                     stack);
/* 175 */                   int fireLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, 
/* 176 */                     stack);
/* 177 */                   if (powLevel > 0) {
/* 178 */                     drawEnchantTag("Pow" + powLevel, x, y);
/* 179 */                     y -= 9;
/*     */                   }
/* 181 */                   if (punLevel > 0) {
/* 182 */                     drawEnchantTag("Pun" + punLevel, x, y);
/* 183 */                     y -= 9;
/*     */                   }
/* 185 */                   if (fireLevel > 0) {
/* 186 */                     drawEnchantTag("Fir" + fireLevel, x, y);
/*     */                   }
/* 188 */                 } else if (stack.getRarity() == EnumRarity.EPIC) {
/* 189 */                   drawEnchantTag("§lGod", x, y);
/*     */                 }
/* 191 */                 x += 16;
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/* 196 */         GlStateManager.popMatrix();
/*     */       } }
/* 198 */     GlStateManager.popMatrix();
/*     */   }
/*     */   
/*     */   private static void drawEnchantTag(String text, int x, int y) {
/* 202 */     GlStateManager.pushMatrix();
/* 203 */     GlStateManager.disableDepth();
/* 204 */     x *= 1;
/* 205 */     y -= 4;
/* 206 */     GL11.glScalef(0.57F, 0.57F, 0.57F);
/* 207 */     mc.fontRendererObj.drawStringWithShadow(text, x, -36 - y, 64250);
/* 208 */     GlStateManager.enableDepth();
/* 209 */     GlStateManager.popMatrix();
/*     */   }
/*     */   
/*     */   private void scale(Entity ent) {
/* 213 */     float scale = (float)Polaris.instance.settingsManager.getSettingByName("Scale").getValDouble();
/* 214 */     float target = scale * (
/* 215 */       mc.gameSettings.fovSetting / (mc.gameSettings.fovSetting * mc.thePlayer.getFovModifier()));
/* 216 */     if ((this.gradualFOVModifier == 0.0D) || (Double.isNaN(this.gradualFOVModifier))) {
/* 217 */       this.gradualFOVModifier = target;
/*     */     }
/* 219 */     double gradualFOVModifier = this.gradualFOVModifier;
/* 220 */     double n = target - this.gradualFOVModifier;
/* 221 */     this.gradualFOVModifier = (gradualFOVModifier + n / (Minecraft.debugFPS * 0.7D));
/* 222 */     scale *= (float)this.gradualFOVModifier;
/* 223 */     scale *= ((mc.currentScreen == null) && (GameSettings.isKeyDown(mc.gameSettings.ofKeyBindZoom)) ? 3 : 1);
/* 224 */     GlStateManager.scale(scale, scale, scale);
/*     */   }
/*     */   
/*     */   private void updatePositions() {
/* 228 */     entityPositions.clear();
/* 229 */     float pTicks = mc.timer.renderPartialTicks;
/* 230 */     for (Object o : mc.theWorld.loadedEntityList) {
/* 231 */       Entity ent = (Entity)o;
/* 232 */       if ((ent != mc.thePlayer) && ((ent instanceof EntityPlayer)) && (
/* 233 */         (!ent.isInvisible()) || (!this.hideInvisibles)))
/*     */       {
/*     */ 
/* 236 */         double x = ent.lastTickPosX + (ent.posX - ent.lastTickPosX) * pTicks - 
/* 237 */           mc.getRenderManager().viewerPosX;
/* 238 */         double y = ent.lastTickPosY + (ent.posY - ent.lastTickPosY) * pTicks - mc.getRenderManager().viewerPosY;
/* 239 */         double z = ent.lastTickPosZ + (ent.posZ - ent.lastTickPosZ) * pTicks - 
/* 240 */           mc.getRenderManager().viewerPosZ;
/* 241 */         y += ent.height + 0.2D;
/* 242 */         if (convertTo2D(x, y, z)[2] >= 0.0D)
/*     */         {
/*     */ 
/* 245 */           if (convertTo2D(x, y, z)[2] < 1.0D)
/*     */           {
/*     */ 
/* 248 */             entityPositions.put((EntityPlayer)ent, 
/* 249 */               new double[] { convertTo2D(x, y, z)[0], convertTo2D(x, y, z)[1], 
/* 250 */               Math.abs(convertTo2D(x, y + 1.0D, z, ent)[1] - convertTo2D(x, y, z, ent)[1]), 
/* 251 */               convertTo2D(x, y, z)[2] }); } }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private double[] convertTo2D(double x, double y, double z, Entity ent) {
/* 257 */     float pTicks = mc.timer.renderPartialTicks;
/* 258 */     float prevYaw = mc.thePlayer.rotationYaw;
/* 259 */     float prevPrevYaw = mc.thePlayer.prevRotationYaw;
/* 260 */     float[] rotations = RotationUtils.getRotationFromPosition(
/* 261 */       ent.lastTickPosX + (ent.posX - ent.lastTickPosX) * pTicks, 
/* 262 */       ent.lastTickPosZ + (ent.posZ - ent.lastTickPosZ) * pTicks, 
/* 263 */       ent.lastTickPosY + (ent.posY - ent.lastTickPosY) * pTicks - 1.6D);
/* 264 */     Entity renderViewEntity = mc.getRenderViewEntity();
/* 265 */     Entity renderViewEntity2 = mc.getRenderViewEntity();
/* 266 */     float n = rotations[0];
/* 267 */     renderViewEntity2.prevRotationYaw = n;
/* 268 */     renderViewEntity.rotationYaw = n;
/* 269 */     mc.entityRenderer.setupCameraTransform(pTicks, 0);
/* 270 */     double[] convertedPoints = convertTo2D(x, y, z);
/* 271 */     mc.getRenderViewEntity().rotationYaw = prevYaw;
/* 272 */     mc.getRenderViewEntity().prevRotationYaw = prevPrevYaw;
/* 273 */     mc.entityRenderer.setupCameraTransform(pTicks, 0);
/* 274 */     return convertedPoints;
/*     */   }
/*     */   
/*     */   private double[] convertTo2D(double x, double y, double z) {
/* 278 */     FloatBuffer screenCoords = BufferUtils.createFloatBuffer(3);
/* 279 */     IntBuffer viewport = BufferUtils.createIntBuffer(16);
/* 280 */     FloatBuffer modelView = BufferUtils.createFloatBuffer(16);
/* 281 */     FloatBuffer projection = BufferUtils.createFloatBuffer(16);
/* 282 */     GL11.glGetFloat(2982, modelView);
/* 283 */     GL11.glGetFloat(2983, projection);
/* 284 */     GL11.glGetInteger(2978, viewport);
/* 285 */     boolean result = GLU.gluProject((float)x, (float)y, (float)z, modelView, projection, viewport, 
/* 286 */       screenCoords);
/* 287 */     if (result) {
/* 288 */       return new double[] { screenCoords.get(0), org.lwjgl.opengl.Display.getHeight() - screenCoords.get(1), screenCoords.get(2) };
/*     */     }
/* 290 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\module\render\Nametags.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */