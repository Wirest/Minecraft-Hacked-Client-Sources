/*    */ package rip.jutting.polaris.event.events;
/*    */ 
/*    */ import net.minecraft.network.Packet;
/*    */ 
/*    */ public class EventSendPacket extends rip.jutting.polaris.event.Event
/*    */ {
/*    */   private Packet packet;
/*    */   
/*    */   public EventSendPacket(Packet packet) {
/* 10 */     packet = null;
/* 11 */     setPacket(packet);
/*    */   }
/*    */   
/*    */   public Packet getPacket() {
/* 15 */     return this.packet;
/*    */   }
/*    */   
/* 18 */   public void setPacket(Packet packet) { this.packet = packet; }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\event\events\EventSendPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */