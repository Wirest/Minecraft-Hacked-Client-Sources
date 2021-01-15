/*    */ package rip.jutting.polaris.event.events;
/*    */ 
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.util.AxisAlignedBB;
/*    */ 
/*    */ public class EventCollide extends rip.jutting.polaris.event.Event
/*    */ {
/*    */   private Entity entity;
/*    */   private double posX;
/*    */   private double posY;
/*    */   private double posZ;
/*    */   private AxisAlignedBB boundingBox;
/*    */   private net.minecraft.block.Block block;
/*    */   
/*    */   public EventCollide(Entity entity, double posX, double posY, double posZ, AxisAlignedBB boundingBox, net.minecraft.block.Block block)
/*    */   {
/* 17 */     this.entity = entity;
/* 18 */     this.posX = posX;
/* 19 */     this.posY = posY;
/* 20 */     this.posZ = posZ;
/* 21 */     this.boundingBox = boundingBox;
/* 22 */     this.block = block;
/*    */   }
/*    */   
/*    */   public AxisAlignedBB getBoundingBox() {
/* 26 */     return this.boundingBox;
/*    */   }
/*    */   
/*    */   public void setBoundingBox(AxisAlignedBB boundingBox) {
/* 30 */     this.boundingBox = boundingBox;
/*    */   }
/*    */   
/*    */   public Entity getEntity() {
/* 34 */     return this.entity;
/*    */   }
/*    */   
/*    */   public double getPosX() {
/* 38 */     return this.posX;
/*    */   }
/*    */   
/*    */   public double getPosY() {
/* 42 */     return this.posY;
/*    */   }
/*    */   
/*    */   public double getPosZ() {
/* 46 */     return this.posZ;
/*    */   }
/*    */   
/*    */   public net.minecraft.block.Block getBlock() {
/* 50 */     return this.block;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\event\events\EventCollide.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */