/*    */ package rip.jutting.polaris.event.events;
/*    */ 
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.util.AxisAlignedBB;
/*    */ import rip.jutting.polaris.utils.Location;
/*    */ 
/*    */ public class EventEntityCollision extends rip.jutting.polaris.event.Event
/*    */ {
/*    */   private Entity entity;
/*    */   private Block block;
/*    */   private Location location;
/*    */   private AxisAlignedBB boundingBox;
/*    */   
/*    */   public EventEntityCollision(Entity entity, Location location, AxisAlignedBB boundingBox, Block block)
/*    */   {
/* 17 */     this.entity = entity;
/* 18 */     this.location = location;
/* 19 */     this.boundingBox = boundingBox;
/* 20 */     this.block = block;
/*    */   }
/*    */   
/*    */   public AxisAlignedBB getBoundingBox() {
/* 24 */     return this.boundingBox;
/*    */   }
/*    */   
/*    */   public void setBoundingBox(AxisAlignedBB boundingBox) {
/* 28 */     this.boundingBox = boundingBox;
/*    */   }
/*    */   
/*    */   public Entity getEntity() {
/* 32 */     return this.entity;
/*    */   }
/*    */   
/*    */   public Location getLocation() {
/* 36 */     return this.location;
/*    */   }
/*    */   
/*    */   public Block getBlock() {
/* 40 */     return this.block;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\event\events\EventEntityCollision.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */