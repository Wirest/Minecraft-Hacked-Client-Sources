/*     */ package rip.jutting.polaris.event.events;
/*     */ 
/*     */ import rip.jutting.polaris.event.Event.State;
/*     */ 
/*     */ public class EventUpdate extends rip.jutting.polaris.event.Event
/*     */ {
/*     */   public Event.State state;
/*     */   public float yaw;
/*     */   public float pitch;
/*     */   public double y;
/*     */   public double x;
/*     */   public double z;
/*     */   private rip.jutting.polaris.utils.Location location;
/*     */   private boolean alwaysSend;
/*     */   public boolean pre;
/*     */   public boolean ground;
/*     */   public boolean fullUpdate;
/*     */   
/*     */   public EventUpdate()
/*     */   {
/*  21 */     this.state = Event.State.POST;
/*     */   }
/*     */   
/*     */   public EventUpdate(double x, double y, double z, float yaw, float pitch, boolean ground) {
/*  25 */     this.state = Event.State.PRE;
/*  26 */     this.x = x;
/*  27 */     this.y = y;
/*  28 */     this.z = z;
/*  29 */     this.yaw = yaw;
/*  30 */     this.pitch = pitch;
/*  31 */     this.ground = ground;
/*  32 */     this.fullUpdate = false;
/*     */   }
/*     */   
/*     */   public Event.State getState() {
/*  36 */     return this.state;
/*     */   }
/*     */   
/*     */   public double getX() {
/*  40 */     return this.x;
/*     */   }
/*     */   
/*     */   public double getY() {
/*  44 */     return this.y;
/*     */   }
/*     */   
/*     */   public double getZ() {
/*  48 */     return this.z;
/*     */   }
/*     */   
/*     */   public void setX(double x) {
/*  52 */     this.x = x;
/*     */   }
/*     */   
/*     */   public void setY(double y) {
/*  56 */     this.y = y;
/*     */   }
/*     */   
/*     */   public void setZ(double z) {
/*  60 */     this.z = z;
/*     */   }
/*     */   
/*     */   public rip.jutting.polaris.utils.Location getLocation() {
/*  64 */     return this.location;
/*     */   }
/*     */   
/*     */   public void setLocation(rip.jutting.polaris.utils.Location location) {
/*  68 */     this.location = location;
/*     */   }
/*     */   
/*     */   public void ground(boolean newGround) {
/*  72 */     this.ground = newGround;
/*     */   }
/*     */   
/*     */   public boolean onGround() {
/*  76 */     return this.ground;
/*     */   }
/*     */   
/*     */   public boolean shouldAlwaysSend() {
/*  80 */     return this.alwaysSend;
/*     */   }
/*     */   
/*     */   public void setYaw(float yaw) {
/*  84 */     this.yaw = yaw;
/*     */   }
/*     */   
/*     */   public void setPitch(float pitch) {
/*  88 */     this.pitch = pitch;
/*     */   }
/*     */   
/*     */   public float getYaw() {
/*  92 */     return this.yaw;
/*     */   }
/*     */   
/*     */   public float getPitch() {
/*  96 */     return this.pitch;
/*     */   }
/*     */   
/*     */   public void setGround(boolean ground) {
/* 100 */     this.ground = ground;
/*     */   }
/*     */   
/*     */   public void setAlwaysSend(boolean alwaysSend) {
/* 104 */     this.alwaysSend = alwaysSend;
/*     */   }
/*     */   
/* 107 */   public boolean isPre() { return this.pre; }
/*     */   
/*     */   public boolean isFullUpdate()
/*     */   {
/* 111 */     return this.fullUpdate;
/*     */   }
/*     */   
/*     */   public void setRotations(float yaw, float pitch)
/*     */   {
/* 116 */     this.yaw = yaw;
/* 117 */     this.pitch = pitch;
/*     */   }
/*     */   
/*     */   public void forceFullUpdate(boolean fullUpdate) {
/* 121 */     this.fullUpdate = fullUpdate;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\event\events\EventUpdate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */