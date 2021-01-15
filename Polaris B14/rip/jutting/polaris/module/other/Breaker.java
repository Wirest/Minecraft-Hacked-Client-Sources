/*     */ package rip.jutting.polaris.module.other;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.entity.EntityPlayerSP;
/*     */ import net.minecraft.client.multiplayer.WorldClient;
/*     */ import net.minecraft.client.network.NetHandlerPlayClient;
/*     */ import net.minecraft.network.play.client.C03PacketPlayer.C05PacketPlayerLook;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import rip.jutting.polaris.Polaris;
/*     */ import rip.jutting.polaris.module.Module;
/*     */ import rip.jutting.polaris.utils.Timer;
/*     */ 
/*     */ public class Breaker extends Module
/*     */ {
/*     */   private Block nukeBlock;
/*     */   private BlockPos blockBreaking;
/*     */   
/*     */   public Breaker()
/*     */   {
/*  24 */     super("Breaker", 0, rip.jutting.polaris.module.Category.OTHER);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*  29 */   private final Timer timer = new Timer();
/*     */   
/*     */   private int xOffset;
/*     */   private int zOffset;
/*     */   private int yOffset;
/*     */   
/*     */   public void setup()
/*     */   {
/*  37 */     ArrayList<String> options = new ArrayList();
/*  38 */     options.add("Bed");
/*  39 */     options.add("Cake");
/*  40 */     options.add("Egg");
/*  41 */     Polaris.instance.settingsManager.rSetting(new rip.jutting.polaris.ui.click.settings.Setting("Breaker Mode", this, "Bed", options));
/*     */   }
/*     */   
/*     */   @rip.jutting.polaris.event.EventTarget
/*     */   public void onUpdate(rip.jutting.polaris.event.events.EventUpdate event) {
/*  46 */     String mode = Polaris.instance.settingsManager.getSettingByName("Breaker Mode").getValString();
/*  47 */     if (mode.equalsIgnoreCase("Bed")) {
/*  48 */       setDisplayName("Breaker §7- Bed");
/*     */     }
/*  50 */     if (mode.equalsIgnoreCase("Cake")) {
/*  51 */       setDisplayName("Breaker §7- Cake");
/*     */     }
/*  53 */     if (mode.equalsIgnoreCase("Egg")) {
/*  54 */       setDisplayName("Breaker §7- Egg");
/*     */     }
/*  56 */     if (mode.equalsIgnoreCase("Bed")) {
/*  57 */       if ((mc.thePlayer == null) || (mc.theWorld == null)) {
/*  58 */         return;
/*     */       }
/*  60 */       for (int xOffset = -5; xOffset < 6; xOffset++) {
/*  61 */         for (int zOffset = -5; zOffset < 6; zOffset++) {
/*  62 */           for (int yOffset = 5; yOffset > -5; yOffset--) {
/*  63 */             double x = mc.thePlayer.posX + xOffset;
/*  64 */             double y = mc.thePlayer.posY + yOffset;
/*  65 */             double z = mc.thePlayer.posZ + zOffset;
/*  66 */             BlockPos pos = new BlockPos(x, y, z);
/*  67 */             int id = Block.getIdFromBlock(mc.theWorld.getBlockState(pos).getBlock());
/*  68 */             if ((id == 26) && (Timer.hasReached(700L))) {
/*  69 */               mc.thePlayer.setSprinting(false);
/*  70 */               smashBlock(new BlockPos(x, y, z));
/*  71 */               mc.thePlayer.setSprinting(false);
/*  72 */               this.timer.getTime();
/*  73 */               mc.thePlayer.setSprinting(false);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*  79 */     if (mode.equalsIgnoreCase("Cake")) {
/*  80 */       this.xOffset = -5;
/*  81 */       while (this.xOffset < 6) {
/*  82 */         this.zOffset = -5;
/*  83 */         while (this.zOffset < 6) {
/*  84 */           this.yOffset = 5;
/*  85 */           while (this.yOffset > -5) {
/*  86 */             double x = mc.thePlayer.posX + this.xOffset;
/*  87 */             double y = mc.thePlayer.posY + this.yOffset;
/*  88 */             double z = mc.thePlayer.posZ + this.zOffset;
/*  89 */             int id = 
/*  90 */               Block.getIdFromBlock(mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock());
/*  91 */             if (id == 92) {
/*  92 */               smashBlock(new BlockPos(x, y, z));
/*     */               
/*  94 */               break;
/*     */             }
/*  96 */             this.yOffset -= 1;
/*     */           }
/*  98 */           this.zOffset += 1;
/*     */         }
/* 100 */         this.xOffset += 1;
/*     */       }
/*     */     }
/* 103 */     if (mode.equalsIgnoreCase("Egg")) {
/* 104 */       this.xOffset = -5;
/* 105 */       while (this.xOffset < 6) {
/* 106 */         this.zOffset = -5;
/* 107 */         while (this.zOffset < 6) {
/* 108 */           this.yOffset = 5;
/* 109 */           while (this.yOffset > -5) {
/* 110 */             double x = mc.thePlayer.posX + this.xOffset;
/* 111 */             double y = mc.thePlayer.posY + this.yOffset;
/* 112 */             double z = mc.thePlayer.posZ + this.zOffset;
/* 113 */             int id = 
/* 114 */               Block.getIdFromBlock(mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock());
/* 115 */             if (id == 122) {
/* 116 */               smashBlock(new BlockPos(x, y, z));
/*     */               
/* 118 */               break;
/*     */             }
/* 120 */             this.yOffset -= 1;
/*     */           }
/* 122 */           this.zOffset += 1;
/*     */         }
/* 124 */         this.xOffset += 1;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void smashBlock(BlockPos pos) {
/* 130 */     mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(getRotations(pos)[0], getRotations(pos)[1], mc.thePlayer.onGround));
/* 131 */     mc.thePlayer.swingItem();
/* 132 */     mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(getRotations(pos)[0], getRotations(pos)[1], mc.thePlayer.onGround));
/* 133 */     mc.thePlayer.sendQueue.addToSendQueue(new net.minecraft.network.play.client.C07PacketPlayerDigging(net.minecraft.network.play.client.C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, pos, net.minecraft.util.EnumFacing.UP));
/* 134 */     mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(getRotations(pos)[0], getRotations(pos)[1], mc.thePlayer.onGround));
/* 135 */     mc.thePlayer.sendQueue.addToSendQueue(new net.minecraft.network.play.client.C07PacketPlayerDigging(net.minecraft.network.play.client.C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, pos, net.minecraft.util.EnumFacing.UP));
/* 136 */     mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(getRotations(pos)[0], getRotations(pos)[1], mc.thePlayer.onGround));
/*     */   }
/*     */   
/*     */   public static float[] getRotations(BlockPos pos)
/*     */   {
/* 141 */     if (pos == null) {
/* 142 */       return null;
/*     */     }
/* 144 */     double diffX = pos.getX() - mc.thePlayer.posX;
/* 145 */     double diffZ = pos.getZ() - mc.thePlayer.posZ;
/* 146 */     double diffY = pos.getY() - mc.thePlayer.getEyeHeight();
/* 147 */     double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
/* 148 */     float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0D / 3.141592653589793D) - 90.0F;
/* 149 */     float pitch = (float)(Math.atan2(diffY, dist) * 180.0D / 3.141592653589793D) / (float)dist;
/* 150 */     return new float[] {
/* 151 */       mc.thePlayer.rotationYaw + 
/* 152 */       MathHelper.wrapAngleTo180_float(yaw - Minecraft.getMinecraft().thePlayer.rotationYaw), 
/* 153 */       mc.thePlayer.rotationPitch + 
/* 154 */       MathHelper.wrapAngleTo180_float(pitch - Minecraft.getMinecraft().thePlayer.rotationPitch) };
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\module\other\Breaker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */