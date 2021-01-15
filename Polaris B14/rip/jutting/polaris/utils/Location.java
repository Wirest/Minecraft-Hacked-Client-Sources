/*     */ package rip.jutting.polaris.utils;
/*     */ 
/*     */ import net.minecraft.util.BlockPos;
/*     */ 
/*     */ public class Location {
/*     */   private double x;
/*     */   private double y;
/*     */   private double z;
/*     */   private float yaw;
/*     */   private float pitch;
/*     */   
/*     */   public Location(double x, double y, double z, float yaw, float pitch) {
/*  13 */     this.x = x;
/*  14 */     this.y = y;
/*  15 */     this.z = z;
/*  16 */     this.yaw = yaw;
/*  17 */     this.pitch = pitch;
/*     */   }
/*     */   
/*     */   public Location(double x, double y, double z) {
/*  21 */     this.x = x;
/*  22 */     this.y = y;
/*  23 */     this.z = z;
/*  24 */     this.yaw = 0.0F;
/*  25 */     this.pitch = 0.0F;
/*     */   }
/*     */   
/*     */   public Location(int x, int y, int z) {
/*  29 */     this.x = x;
/*  30 */     this.y = y;
/*  31 */     this.z = z;
/*  32 */     this.yaw = 0.0F;
/*  33 */     this.pitch = 0.0F;
/*     */   }
/*     */   
/*     */   public Location add(int x, int y, int z) {
/*  37 */     this.x += x;
/*  38 */     this.y += y;
/*  39 */     this.z += z;
/*     */     
/*  41 */     return this;
/*     */   }
/*     */   
/*     */   public Location add(double x, double y, double z) {
/*  45 */     this.x += x;
/*  46 */     this.y += y;
/*  47 */     this.z += z;
/*     */     
/*  49 */     return this;
/*     */   }
/*     */   
/*     */   public Location subtract(int x, int y, int z) {
/*  53 */     this.x -= x;
/*  54 */     this.y -= y;
/*  55 */     this.z -= z;
/*     */     
/*  57 */     return this;
/*     */   }
/*     */   
/*     */   public Location subtract(double x, double y, double z) {
/*  61 */     this.x -= x;
/*  62 */     this.y -= y;
/*  63 */     this.z -= z;
/*     */     
/*  65 */     return this;
/*     */   }
/*     */   
/*     */   public net.minecraft.block.Block getBlock() {
/*  69 */     return net.minecraft.client.Minecraft.getMinecraft().theWorld.getBlockState(toBlockPos()).getBlock();
/*     */   }
/*     */   
/*     */   public double getX() {
/*  73 */     return this.x;
/*     */   }
/*     */   
/*     */   public Location setX(double x) {
/*  77 */     this.x = x;
/*  78 */     return this;
/*     */   }
/*     */   
/*     */   public double getY() {
/*  82 */     return this.y;
/*     */   }
/*     */   
/*     */   public Location setY(double y) {
/*  86 */     this.y = y;
/*  87 */     return this;
/*     */   }
/*     */   
/*     */   public double getZ() {
/*  91 */     return this.z;
/*     */   }
/*     */   
/*     */   public Location setZ(double z) {
/*  95 */     this.z = z;
/*  96 */     return this;
/*     */   }
/*     */   
/*     */   public float getYaw() {
/* 100 */     return this.yaw;
/*     */   }
/*     */   
/*     */   public Location setYaw(float yaw) {
/* 104 */     this.yaw = yaw;
/* 105 */     return this;
/*     */   }
/*     */   
/*     */   public float getPitch() {
/* 109 */     return this.pitch;
/*     */   }
/*     */   
/*     */   public Location setPitch(float pitch) {
/* 113 */     this.pitch = pitch;
/* 114 */     return this;
/*     */   }
/*     */   
/*     */   public static Location fromBlockPos(BlockPos blockPos) {
/* 118 */     return new Location(blockPos.getX(), blockPos.getY(), blockPos.getZ());
/*     */   }
/*     */   
/*     */   public BlockPos toBlockPos() {
/* 122 */     return new BlockPos(getX(), getY(), getZ());
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\utils\Location.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */