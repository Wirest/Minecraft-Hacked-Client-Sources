/*    */ package rip.jutting.polaris.event.events;
/*    */ 
/*    */ import net.minecraft.network.Packet;
/*    */ 
/*    */ public class EventReceivePacket extends rip.jutting.polaris.event.Event
/*    */ {
/*    */   private Packet packet;
/*    */   
/*    */   public EventReceivePacket(Packet packet) {
/* 10 */     this.packet = packet;
/*    */   }
/*    */   
/*    */   public Packet getPacket() {
/* 14 */     return this.packet;
/*    */   }
/*    */   
/* 17 */   public void setPacket(Packet packet) { this.packet = packet; }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\event\events\EventReceivePacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */