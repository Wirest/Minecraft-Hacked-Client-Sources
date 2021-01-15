/*     */ package rip.jutting.polaris.utils;
/*     */ 
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockAir;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.entity.EntityPlayerSP;
/*     */ import net.minecraft.client.multiplayer.WorldClient;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.projectile.EntityEgg;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.Vec3;
/*     */ import net.minecraft.util.Vec3i;
/*     */ 
/*     */ public final class BlockUtils
/*     */ {
/*  20 */   private static final Minecraft mc = ;
/*     */   
/*     */   public static Block getBlock(int x, int y, int z) {
/*  23 */     return Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
/*     */   }
/*     */   
/*     */   public static Block getBlock(BlockPos pos) {
/*  27 */     return Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock();
/*     */   }
/*     */   
/*     */   public static float[] getDirectionToBlock(int var0, int var1, int var2, EnumFacing var3) {
/*  31 */     EntityEgg var4 = new EntityEgg(mc.theWorld);
/*  32 */     var4.posX = (var0 + 0.5D);
/*  33 */     var4.posY = (var1 + 0.5D);
/*  34 */     var4.posZ = (var2 + 0.5D);
/*  35 */     var4.posX += var3.getDirectionVec().getX() * 0.25D;
/*  36 */     var4.posY += var3.getDirectionVec().getY() * 0.25D;
/*  37 */     var4.posZ += var3.getDirectionVec().getZ() * 0.25D;
/*  38 */     return getDirectionToEntity(var4);
/*     */   }
/*     */   
/*     */   private static float[] getDirectionToEntity(Entity var0) {
/*  42 */     return new float[] { getYaw(var0) + mc.thePlayer.rotationYaw, getPitch(var0) + mc.thePlayer.rotationPitch };
/*     */   }
/*     */   
/*     */   public static float getPitch(Entity var0) {
/*  46 */     double var1 = var0.posX - mc.thePlayer.posX;
/*  47 */     double var3 = var0.posZ - mc.thePlayer.posZ;
/*  48 */     double var5 = var0.posY - 1.6D + var0.getEyeHeight() - mc.thePlayer.posY;
/*  49 */     double var7 = MathHelper.sqrt_double(var1 * var1 + var3 * var3);
/*  50 */     double var9 = -Math.toDegrees(Math.atan(var5 / var7));
/*  51 */     return -MathHelper.wrapAngleTo180_float(mc.thePlayer.rotationPitch - (float)var9);
/*     */   }
/*     */   
/*     */   public static float getYaw(Entity var0) {
/*  55 */     double var1 = var0.posX - mc.thePlayer.posX;
/*  56 */     double var3 = var0.posZ - mc.thePlayer.posZ;
/*     */     double var5;
/*     */     double var5;
/*  59 */     if ((var3 < 0.0D) && (var1 < 0.0D)) {
/*  60 */       var5 = 90.0D + Math.toDegrees(Math.atan(var3 / var1)); } else { double var5;
/*  61 */       if ((var3 < 0.0D) && (var1 > 0.0D)) {
/*  62 */         var5 = -90.0D + Math.toDegrees(Math.atan(var3 / var1));
/*     */       } else {
/*  64 */         var5 = Math.toDegrees(-Math.atan(var1 / var3));
/*     */       }
/*     */     }
/*  67 */     return MathHelper.wrapAngleTo180_float(-(mc.thePlayer.rotationYaw - (float)var5));
/*     */   }
/*     */   
/*     */   public static EnumFacing getFacingDirection(BlockPos pos) {
/*  71 */     EnumFacing direction = null;
/*  72 */     if (!mc.theWorld.getBlockState(pos.add(0, 1, 0)).getBlock().isFullCube()) {
/*  73 */       direction = EnumFacing.UP;
/*  74 */     } else if (!mc.theWorld.getBlockState(pos.add(0, -1, 0)).getBlock().isFullCube()) {
/*  75 */       direction = EnumFacing.DOWN;
/*  76 */     } else if (!mc.theWorld.getBlockState(pos.add(1, 0, 0)).getBlock().isFullCube()) {
/*  77 */       direction = EnumFacing.EAST;
/*  78 */     } else if (!mc.theWorld.getBlockState(pos.add(-1, 0, 0)).getBlock().isFullCube()) {
/*  79 */       direction = EnumFacing.WEST;
/*  80 */     } else if (!mc.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock().isFullCube()) {
/*  81 */       direction = EnumFacing.SOUTH;
/*  82 */     } else if (!mc.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock().isFullCube()) {
/*  83 */       direction = EnumFacing.NORTH;
/*     */     }
/*     */     
/*  86 */     return direction;
/*     */   }
/*     */   
/*     */   public static float[] aimAtBlock(BlockPos pos) {
/*  90 */     EnumFacing[] arrenumFacing = EnumFacing.values();
/*  91 */     int n = arrenumFacing.length;
/*  92 */     int n2 = 0;
/*  93 */     float yaw = 1.0F;
/*  94 */     float pitch = 1.0F;
/*  95 */     if (n2 <= n) {
/*  96 */       EnumFacing side = arrenumFacing[n2];
/*  97 */       BlockPos neighbor = pos.offset(side);
/*  98 */       EnumFacing side2 = side.getOpposite();
/*  99 */       Vec3 hitVec = new Vec3(neighbor).addVector(0.5D, 0.5D, 0.5D).add(new Vec3(side2.getDirectionVec()).scale(0.5D).normalize());
/*     */       
/* 101 */       yaw = RotationUtils2.getNeededRotations(hitVec)[0];
/* 102 */       pitch = RotationUtils2.getNeededRotations(hitVec)[1];
/* 103 */       if (canBeClicked(neighbor)) {
/* 104 */         return new float[] { yaw, pitch };
/*     */       }
/* 106 */       hitVec = new Vec3(pos).addVector(0.5D, 0.5D, 0.5D).add(new Vec3(side.getDirectionVec()).scale(0.5D).normalize());
/* 107 */       yaw = RotationUtils2.getNeededRotations(hitVec)[0];
/* 108 */       pitch = RotationUtils2.getNeededRotations(hitVec)[1];
/* 109 */       return new float[] { yaw, pitch };
/*     */     }
/*     */     
/*     */ 
/* 113 */     return new float[] { 1.0F, 1.0F };
/*     */   }
/*     */   
/*     */   public static boolean isOnSoulSand()
/*     */   {
/* 118 */     boolean onIce = false;
/* 119 */     int y = (int)Minecraft.getMinecraft().thePlayer.getEntityBoundingBox().offset(0.0D, -0.1D, 0.0D).minY;
/*     */     
/* 121 */     for (int x = MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.getEntityBoundingBox().minX); x < MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.getEntityBoundingBox().maxX) + 1; x++)
/*     */     {
/* 123 */       for (int z = MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.getEntityBoundingBox().minZ); z < MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.getEntityBoundingBox().maxZ) + 1; z++)
/*     */       {
/* 125 */         Block block = getBlock(x, y, z);
/*     */         
/* 127 */         if ((block != null) && (!(block instanceof BlockAir)) && ((block instanceof net.minecraft.block.BlockSoulSand)))
/*     */         {
/* 129 */           onIce = true;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 134 */     return onIce;
/*     */   }
/*     */   
/*     */   public static boolean isOnIce()
/*     */   {
/* 139 */     boolean onIce = false;
/* 140 */     int y = (int)Minecraft.getMinecraft().thePlayer.getEntityBoundingBox().offset(0.0D, -0.1D, 0.0D).minY;
/*     */     
/* 142 */     for (int x = MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.getEntityBoundingBox().minX); x < MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.getEntityBoundingBox().maxX) + 1; x++)
/*     */     {
/* 144 */       for (int z = MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.getEntityBoundingBox().minZ); z < MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.getEntityBoundingBox().maxZ) + 1; z++)
/*     */       {
/* 146 */         Block block = getBlock(x, y, z);
/*     */         
/* 148 */         if ((block != null) && (!(block instanceof BlockAir)) && (((block instanceof net.minecraft.block.BlockPackedIce)) || ((block instanceof net.minecraft.block.BlockIce))))
/*     */         {
/* 150 */           onIce = true;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 155 */     return onIce;
/*     */   }
/*     */   
/*     */   public static boolean isOnCactus()
/*     */   {
/* 160 */     boolean onIce = false;
/* 161 */     int y = (int)Minecraft.getMinecraft().thePlayer.getEntityBoundingBox().offset(0.0D, -0.1D, 0.0D).minY;
/*     */     
/* 163 */     for (int x = MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.getEntityBoundingBox().minX); x < MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.getEntityBoundingBox().maxX) + 1; x++)
/*     */     {
/* 165 */       for (int z = MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.getEntityBoundingBox().minZ); z < MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.getEntityBoundingBox().maxZ) + 1; z++)
/*     */       {
/* 167 */         Block block = getBlock(x, y, z);
/*     */         
/* 169 */         if ((block != null) && (!(block instanceof BlockAir)) && ((block instanceof net.minecraft.block.BlockCactus)))
/*     */         {
/* 171 */           onIce = true;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 176 */     return onIce;
/*     */   }
/*     */   
/*     */   public static boolean isOnAir() {
/* 180 */     for (int x = MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.minX); x < MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.maxX) + 1; x++) {
/* 181 */       int z = MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.minZ);
/* 182 */       if (z < MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.maxZ) + 1) {
/* 183 */         BlockPos pos = new BlockPos(x, (int)Minecraft.getMinecraft().thePlayer.posY - 1, z);
/* 184 */         Block block = Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock();
/* 185 */         return block instanceof BlockAir;
/*     */       }
/*     */     }
/* 188 */     return false;
/*     */   }
/*     */   
/*     */   public static IBlockState getState(BlockPos pos) {
/* 192 */     return mc.theWorld.getBlockState(pos);
/*     */   }
/*     */   
/* 195 */   public static int getId(BlockPos pos) { return Block.getIdFromBlock(getBlock(pos)); }
/*     */   
/*     */   public static boolean canBeClicked(BlockPos pos)
/*     */   {
/* 199 */     return getBlock(pos).canCollideCheck(getState(pos), false);
/*     */   }
/*     */   
/*     */   public static float getHardness(BlockPos pos) {
/* 203 */     return getBlock(pos).getPlayerRelativeBlockHardness(mc.thePlayer, mc.theWorld, pos);
/*     */   }
/*     */   
/*     */   public static float[] getBlockRotations(double x, double y, double z) {
/* 207 */     double var4 = x - mc.thePlayer.posX + 0.5D;
/* 208 */     double var6 = z - mc.thePlayer.posZ + 0.5D;
/* 209 */     double var8 = y - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight() - 1.0D);
/* 210 */     double var14 = MathHelper.sqrt_double(var4 * var4 + var6 * var6);
/* 211 */     float var12 = (float)(Math.atan2(var6, var4) * 180.0D / 3.141592653589793D) - 90.0F;
/* 212 */     return new float[] { var12, (float)-(Math.atan2(var8, var14) * 180.0D / 3.141592653589793D) };
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\utils\BlockUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */