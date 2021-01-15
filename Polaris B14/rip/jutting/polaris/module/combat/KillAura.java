/*     */ package rip.jutting.polaris.module.combat;
/*     */ 
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.entity.EntityPlayerSP;
/*     */ import net.minecraft.client.multiplayer.PlayerControllerMP;
/*     */ import net.minecraft.client.multiplayer.ServerData;
/*     */ import net.minecraft.client.multiplayer.WorldClient;
/*     */ import net.minecraft.client.network.NetHandlerPlayClient;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.passive.EntityVillager;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.item.ItemSword;
/*     */ import net.minecraft.network.play.client.C02PacketUseEntity;
/*     */ import net.minecraft.network.play.client.C02PacketUseEntity.Action;
/*     */ import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
/*     */ import net.minecraft.network.play.client.C07PacketPlayerDigging;
/*     */ import net.minecraft.network.play.client.C07PacketPlayerDigging.Action;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import rip.jutting.polaris.Polaris;
/*     */ import rip.jutting.polaris.anticheat.Anticheat;
/*     */ import rip.jutting.polaris.anticheat.AnticheatManager;
/*     */ import rip.jutting.polaris.event.events.EventUpdate;
/*     */ import rip.jutting.polaris.module.Module;
/*     */ import rip.jutting.polaris.ui.click.settings.Setting;
/*     */ import rip.jutting.polaris.ui.click.settings.SettingsManager;
/*     */ import rip.jutting.polaris.utils.RotationUtils;
/*     */ import rip.jutting.polaris.utils.Timer;
/*     */ 
/*     */ public class KillAura extends Module
/*     */ {
/*  35 */   private int ticks = 0;
/*     */   public static float yaw;
/*     */   public static float pitch;
/*     */   public static Entity entity;
/*     */   public static EntityLivingBase target;
/*  40 */   private final Timer timer = new Timer();
/*     */   
/*     */   private int xd;
/*     */   private int tpdelay;
/*     */   
/*     */   public KillAura()
/*     */   {
/*  47 */     super("KillAura", 0, rip.jutting.polaris.module.Category.COMBAT);
/*  48 */     this.xd = 0;
/*     */   }
/*     */   
/*     */   public void setup()
/*     */   {
/*  53 */     Polaris.instance.settingsManager.rSetting(new Setting("KA Speed", this, 2.0D, 2.0D, 10.0D, true));
/*  54 */     Polaris.instance.settingsManager.rSetting(new Setting("Reach", this, 4.0D, 1.0D, 6.0D, false));
/*  55 */     Polaris.instance.settingsManager.rSetting(new Setting("Invisibles", this, false));
/*  56 */     Polaris.instance.settingsManager.rSetting(new Setting("Players", this, true));
/*  57 */     Polaris.instance.settingsManager.rSetting(new Setting("Mobs", this, false));
/*  58 */     Polaris.instance.settingsManager.rSetting(new Setting("Teams", this, false));
/*  59 */     Polaris.instance.settingsManager.rSetting(new Setting("AutoBlock", this, false));
/*  60 */     Polaris.instance.settingsManager.rSetting(new Setting("Velt AutoBlock", this, false));
/*  61 */     Polaris.instance.settingsManager.rSetting(new Setting("New AutoBlock", this, false));
/*  62 */     Polaris.instance.settingsManager.rSetting(new Setting("Auto Disable", this, true));
/*  63 */     Polaris.instance.settingsManager.rSetting(new Setting("MMC", this, false));
/*  64 */     Polaris.instance.settingsManager.rSetting(new Setting("God Mode", this, false));
/*  65 */     Polaris.instance.settingsManager.rSetting(new Setting("Dev Logs", this, false));
/*     */   }
/*     */   
/*     */   @rip.jutting.polaris.event.EventTarget
/*     */   public void onUpdate(EventUpdate event) {
/*  70 */     if ((Polaris.instance.settingsManager.getSettingByName("Auto Disable").getValBoolean()) && (
/*  71 */       (mc.thePlayer == null) || (mc.theWorld == null) || ((mc.currentScreen instanceof net.minecraft.client.gui.GuiDownloadTerrain)) || 
/*  72 */       ((mc.currentScreen instanceof net.minecraft.client.gui.GuiGameOver)) || ((mc.currentScreen instanceof net.minecraft.client.gui.GuiWinGame)) || 
/*  73 */       (mc.thePlayer.isDead) || (mc.thePlayer.getHealth() <= 0.0F))) {
/*  74 */       toggle();
/*     */     }
/*     */     
/*  77 */     if (Polaris.instance.settingsManager.getSettingByName("MMC").getValBoolean()) {
/*  78 */       setDisplayName("KillAura §7- AGC");
/*  79 */       this.xd += 1;
/*  80 */       this.tpdelay += 1;
/*  81 */       float reach = (float)Polaris.instance.settingsManager.getSettingByName("Reach").getValDouble();
/*  82 */       if (this.xd >= 20.0D - Polaris.instance.settingsManager.getSettingByName("KA Speed").getValDouble() * 3.0D) {
/*  83 */         this.xd = 0;
/*  84 */         for (Object object : mc.theWorld.loadedEntityList) {
/*  85 */           if ((object instanceof EntityLivingBase)) {
/*  86 */             EntityLivingBase entity = (EntityLivingBase)object;
/*  87 */             if (!(entity instanceof EntityPlayerSP))
/*     */             {
/*     */ 
/*  90 */               if (mc.thePlayer.getDistanceToEntity(entity) <= reach)
/*     */               {
/*     */ 
/*  93 */                 if (entity.isEntityAlive()) {
/*  94 */                   if ((this.tpdelay >= 4) && 
/*  95 */                     (!Minecraft.getMinecraft().isSingleplayer()))
/*     */                   {
/*  97 */                     if (!Minecraft.getMinecraft().getCurrentServerData().serverIP.contains("faithful")) {
/*  98 */                       mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(
/*  99 */                         entity.posX, entity.posY, entity.posZ, false));
/*     */                     }
/*     */                   }
/* 102 */                   if (mc.thePlayer.getDistanceToEntity(entity) < reach)
/* 103 */                     mmcAttack(entity);
/*     */                 } }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     } else {
/* 110 */       setDisplayName("KillAura §7- Single");
/* 111 */       this.ticks += 1;
/* 112 */       entity = closeEntity();
/* 113 */       float reach = (float)Polaris.instance.settingsManager.getSettingByName("Reach").getValDouble();
/* 114 */       if ((!Minecraft.getMinecraft().isSingleplayer()) && 
/* 115 */         ((Minecraft.getMinecraft().getCurrentServerData().serverIP.contains("velt")) || 
/* 116 */         (Minecraft.getMinecraft().getCurrentServerData().serverIP.contains("arcane"))) && 
/* 117 */         (!mc.thePlayer.onGround)) {
/* 118 */         reach = 2.9F;
/*     */       }
/* 120 */       if (AutoPot.doPot) {
/* 121 */         return;
/*     */       }
/* 123 */       if (entity != null)
/*     */       {
/* 125 */         if (mc.thePlayer.getDistanceToEntity(entity) <= (float)Polaris.instance.settingsManager.getSettingByName("Reach").getValDouble())
/*     */         {
/* 127 */           if ((Polaris.instance.settingsManager.getSettingByName("Invisibles").getValBoolean()) && 
/* 128 */             (entity.isInvisible())) {
/* 129 */             if (Polaris.instance.anticheatManager.findAnticheat() != Anticheat.AGC) {
/* 130 */               LookAtEntity(entity);
/* 131 */               event.setYaw(yaw);
/* 132 */               event.setPitch(pitch);
/*     */             }
/* 134 */             if ((mc.thePlayer.getCurrentEquippedItem() != null) && 
/* 135 */               (Polaris.instance.settingsManager.getSettingByName("AutoBlock").getValBoolean()) && 
/* 136 */               ((mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword))) {
/* 137 */               mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, 
/* 138 */                 mc.thePlayer.getCurrentEquippedItem());
/* 139 */               if (Polaris.instance.settingsManager.getSettingByName("Dev Logs").getValBoolean()) {
/* 140 */                 Polaris.sendMessage("Blocked sword");
/*     */               }
/*     */             }
/* 143 */             if ((mc.thePlayer.getCurrentEquippedItem() != null) && 
/* 144 */               (Polaris.instance.settingsManager.getSettingByName("Velt AutoBlock").getValBoolean()) && 
/* 145 */               ((mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword))) {
/* 146 */               mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, 
/* 147 */                 mc.thePlayer.getCurrentEquippedItem());
/* 148 */               if (Polaris.instance.settingsManager.getSettingByName("Dev Logs").getValBoolean()) {
/* 149 */                 Polaris.sendMessage("Blocked sword");
/*     */               }
/*     */             }
/* 152 */             if ((mc.thePlayer.getCurrentEquippedItem() != null) && 
/* 153 */               (Polaris.instance.settingsManager.getSettingByName("New AutoBlock").getValBoolean()) && 
/* 154 */               ((mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword))) {
/* 155 */               mc.thePlayer.setItemInUse(mc.thePlayer.getCurrentEquippedItem(), 100000);
/* 156 */               if (Polaris.instance.settingsManager.getSettingByName("Dev Logs").getValBoolean()) {
/* 157 */                 Polaris.sendMessage("Blocked sword");
/*     */               }
/*     */             }
/* 160 */             if ((mc.thePlayer.isBlocking()) && (mc.thePlayer.isMoving()) && 
/* 161 */               (Polaris.instance.settingsManager.getSettingByName("AutoBlock").getValBoolean())) {
/* 162 */               mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(
/* 163 */                 C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
/* 164 */               if (Polaris.instance.settingsManager.getSettingByName("Dev Logs").getValBoolean()) {
/* 165 */                 Polaris.sendMessage("Released Blocking");
/*     */               }
/*     */             }
/* 168 */             target = (EntityLivingBase)closeEntity();
/* 169 */             float[] r = RotationUtils.getRotations(target);
/* 170 */             if (Polaris.instance.settingsManager.getSettingByName("God Mode").getValBoolean()) {
/* 171 */               event.y += 4.5D;
/* 172 */               event.setGround(true);
/*     */             }
/*     */             
/* 175 */             if (this.ticks >= 20.0D / Polaris.instance.settingsManager.getSettingByName("KA Speed").getValDouble()) {
/* 176 */               attack(entity);
/* 177 */               if (Polaris.instance.settingsManager.getSettingByName("God Mode").getValBoolean()) {
/* 178 */                 event.y += 9.8D;
/*     */               }
/* 180 */               if (Polaris.instance.settingsManager.getSettingByName("Dev Logs").getValBoolean()) {
/* 181 */                 Polaris.sendMessage("Hit Target");
/*     */               }
/*     */               
/* 184 */               this.ticks = 0;
/*     */             }
/*     */           }
/* 187 */           if (entity.isEntityAlive()) {
/* 188 */             if ((Polaris.instance.anticheatManager.findAnticheat() != Anticheat.AGC) || 
/* 189 */               (Polaris.instance.anticheatManager.findAnticheat() != Anticheat.AREA51)) {
/* 190 */               LookAtEntity(entity);
/* 191 */               event.setYaw(yaw);
/* 192 */               event.setPitch(pitch);
/*     */             }
/* 194 */             if ((mc.thePlayer.getCurrentEquippedItem() != null) && 
/* 195 */               (Polaris.instance.settingsManager.getSettingByName("AutoBlock").getValBoolean()) && 
/* 196 */               ((mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword))) {
/* 197 */               mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, 
/* 198 */                 mc.thePlayer.getCurrentEquippedItem());
/* 199 */               if (Polaris.instance.settingsManager.getSettingByName("Dev Logs").getValBoolean()) {
/* 200 */                 Polaris.sendMessage("Blocked sword");
/*     */               }
/*     */             }
/* 203 */             if ((mc.thePlayer.getCurrentEquippedItem() != null) && 
/* 204 */               (Polaris.instance.settingsManager.getSettingByName("Velt AutoBlock").getValBoolean()) && 
/* 205 */               ((mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword))) {
/* 206 */               mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, 
/* 207 */                 mc.thePlayer.getCurrentEquippedItem());
/* 208 */               if (Polaris.instance.settingsManager.getSettingByName("Dev Logs").getValBoolean()) {
/* 209 */                 Polaris.sendMessage("Blocked sword");
/*     */               }
/*     */             }
/* 212 */             if ((mc.thePlayer.getCurrentEquippedItem() != null) && 
/* 213 */               (Polaris.instance.settingsManager.getSettingByName("New AutoBlock").getValBoolean()) && 
/* 214 */               ((mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword))) {
/* 215 */               mc.thePlayer.setItemInUse(mc.thePlayer.getCurrentEquippedItem(), 100000);
/* 216 */               if (Polaris.instance.settingsManager.getSettingByName("Dev Logs").getValBoolean()) {
/* 217 */                 Polaris.sendMessage("Blocked sword");
/*     */               }
/*     */             }
/* 220 */             if ((mc.thePlayer.isBlocking()) && (mc.thePlayer.isMoving()) && 
/* 221 */               (Polaris.instance.settingsManager.getSettingByName("AutoBlock").getValBoolean())) {
/* 222 */               mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(
/* 223 */                 C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
/* 224 */               if (Polaris.instance.settingsManager.getSettingByName("Dev Logs").getValBoolean()) {
/* 225 */                 Polaris.sendMessage("Released Blocking");
/*     */               }
/*     */             }
/* 228 */             target = (EntityLivingBase)closeEntity();
/* 229 */             float[] r = RotationUtils.getRotations(target);
/* 230 */             if (Polaris.instance.settingsManager.getSettingByName("God Mode").getValBoolean()) {
/* 231 */               event.y += 4.5D;
/* 232 */               event.setGround(true);
/*     */             }
/*     */             
/* 235 */             if (this.ticks >= 20.0D / Polaris.instance.settingsManager.getSettingByName("KA Speed").getValDouble()) {
/* 236 */               attack(entity);
/* 237 */               if (Polaris.instance.settingsManager.getSettingByName("God Mode").getValBoolean()) {
/* 238 */                 event.y += 9.8D;
/*     */               }
/* 240 */               if (Polaris.instance.settingsManager.getSettingByName("Dev Logs").getValBoolean()) {
/* 241 */                 Polaris.sendMessage("Hit Target");
/*     */               }
/*     */               
/* 244 */               this.ticks = 0;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static boolean validEntity(Entity entity) {
/* 253 */     if (!(entity instanceof EntityLivingBase))
/* 254 */       return false;
/* 255 */     if (!entity.isEntityAlive())
/* 256 */       return false;
/* 257 */     if (entity == mc.thePlayer)
/* 258 */       return false;
/* 259 */     if ((entity instanceof net.minecraft.entity.item.EntityArmorStand))
/* 260 */       return false;
/* 261 */     if (rip.jutting.polaris.friend.FriendManager.isFriend(entity.getName()))
/* 262 */       return false;
/* 263 */     float reach = (float)Polaris.instance.settingsManager.getSettingByName("Reach").getValDouble();
/* 264 */     if (mc.thePlayer.getDistanceToEntity(entity) > reach)
/* 265 */       return false;
/* 266 */     if (((entity instanceof EntityPlayer)) && (((EntityPlayer)entity).isPlayerSleeping()))
/* 267 */       return false;
/* 268 */     if (((entity instanceof EntityPlayer)) && (!Polaris.instance.settingsManager.getSettingByName("Players").getValBoolean())) {
/* 269 */       return false;
/*     */     }
/*     */     
/* 272 */     if ((((entity instanceof net.minecraft.entity.passive.EntityAnimal)) || ((entity instanceof EntityVillager)) || ((entity instanceof net.minecraft.entity.monster.EntityMob))) && 
/* 273 */       (!Polaris.instance.settingsManager.getSettingByName("Mobs").getValBoolean()))
/* 274 */       return false;
/* 275 */     if ((entity.isInvisible()) && (!Polaris.instance.settingsManager.getSettingByName("Invisibles").getValBoolean()))
/* 276 */       return false;
/* 277 */     if (isOnSameTeam(entity))
/* 278 */       return false;
/* 279 */     if ((Polaris.instance.moduleManager.getModuleByName("AntiBot").isToggled()) && (AntiBot.bot.contains(entity)))
/* 280 */       return false;
/* 281 */     return true;
/*     */   }
/*     */   
/*     */   public static boolean isOnSameTeam(Entity entity) {
/* 285 */     boolean team = false;
/*     */     
/* 287 */     if ((Polaris.instance.settingsManager.getSettingByName("Teams").getValBoolean()) && ((entity instanceof EntityPlayer))) {
/* 288 */       String n = entity.getDisplayName().getFormattedText();
/* 289 */       if ((n.startsWith("§f")) && (!n.equalsIgnoreCase(entity.getName()))) {
/* 290 */         team = 
/* 291 */           n.substring(0, 6).equalsIgnoreCase(mc.thePlayer.getDisplayName().getFormattedText().substring(0, 6));
/*     */       } else {
/* 293 */         team = 
/* 294 */           n.substring(0, 2).equalsIgnoreCase(mc.thePlayer.getDisplayName().getFormattedText().substring(0, 2));
/*     */       }
/*     */     }
/* 297 */     return team;
/*     */   }
/*     */   
/*     */   public Entity closeEntity() {
/* 301 */     Entity close = null;
/* 302 */     for (Object o : mc.theWorld.loadedEntityList) {
/* 303 */       Entity e = (Entity)o;
/* 304 */       if (((e instanceof Entity)) && (validEntity(e)) && (
/* 305 */         (close == null) || (mc.thePlayer.getDistanceToEntity(e) < mc.thePlayer.getDistanceToEntity(close)))) {
/* 306 */         close = e;
/*     */       }
/*     */     }
/*     */     
/* 310 */     return close;
/*     */   }
/*     */   
/*     */   public void attack(Entity ent) {
/* 314 */     mc.thePlayer.swingItem();
/* 315 */     Entity hit = mc.objectMouseOver.entityHit;
/* 316 */     mc.getNetHandler().getNetworkManager()
/* 317 */       .sendPacket(new C02PacketUseEntity(ent, C02PacketUseEntity.Action.ATTACK));
/*     */     try
/*     */     {
/* 320 */       if (mc.thePlayer.getCurrentEquippedItem().isItemEnchanted()) {
/* 321 */         mc.thePlayer.onEnchantmentCritical(ent);
/*     */       }
/*     */     }
/*     */     catch (Exception localException) {}
/*     */   }
/*     */   
/*     */   public void mmcAttack(EntityLivingBase entity)
/*     */   {
/* 329 */     mmcAttack(entity, false);
/*     */   }
/*     */   
/*     */   public void mmcAttack(EntityLivingBase entity, boolean crit) {
/* 333 */     mc.thePlayer.swingItem();
/* 334 */     float sharpLevel = net.minecraft.enchantment.EnchantmentHelper.func_152377_a(mc.thePlayer.getHeldItem(), 
/* 335 */       entity.getCreatureAttribute());
/* 336 */     boolean vanillaCrit = (mc.thePlayer.fallDistance > 0.0F) && (!mc.thePlayer.onGround) && 
/* 337 */       (!mc.thePlayer.isOnLadder()) && (!mc.thePlayer.isInWater()) && 
/* 338 */       (!mc.thePlayer.isPotionActive(net.minecraft.potion.Potion.blindness)) && (mc.thePlayer.ridingEntity == null);
/* 339 */     mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(entity, C02PacketUseEntity.Action.ATTACK));
/* 340 */     if ((crit) || (vanillaCrit)) {
/* 341 */       mc.thePlayer.onCriticalHit(entity);
/*     */     }
/* 343 */     if (sharpLevel > 0.0F) {
/* 344 */       mc.thePlayer.onEnchantmentCritical(entity);
/*     */     }
/*     */   }
/*     */   
/*     */   public static float[] getRotations(Entity ent) {
/* 349 */     double x = ent.posX;
/* 350 */     double z = ent.posZ;
/* 351 */     double y = ent.boundingBox.maxY - 4.0D;
/* 352 */     return getRotationFromPosition(x, z, y);
/*     */   }
/*     */   
/*     */   public static float[] getRotationFromPosition(double x, double z, double y) {
/* 356 */     double xDiff = x - Minecraft.getMinecraft().thePlayer.posX;
/* 357 */     double zDiff = z - Minecraft.getMinecraft().thePlayer.posZ;
/* 358 */     double yDiff = y - Minecraft.getMinecraft().thePlayer.posY + 
/* 359 */       Minecraft.getMinecraft().thePlayer.getEyeHeight();
/* 360 */     double dist = net.minecraft.util.MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
/* 361 */     float yaw = (float)(Math.atan2(zDiff, xDiff) * 180.0D / 3.141592653589793D) - 90.0F;
/* 362 */     float pitch = (float)-(Math.atan2(yDiff, dist) * 180.0D / 3.141592653589793D);
/* 363 */     return new float[] { yaw, pitch };
/*     */   }
/*     */   
/*     */   public static void LookAtEntity(Entity entity) {
/* 367 */     float[] rotations = getRotations(entity);
/* 368 */     yaw = rotations[0];
/* 369 */     pitch = rotations[1];
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\module\combat\KillAura.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */