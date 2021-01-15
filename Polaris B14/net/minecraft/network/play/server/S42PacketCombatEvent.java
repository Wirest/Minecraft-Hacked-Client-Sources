/*    */ package net.minecraft.network.play.server;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.play.INetHandlerPlayClient;
/*    */ import net.minecraft.util.CombatTracker;
/*    */ import net.minecraft.util.IChatComponent;
/*    */ 
/*    */ 
/*    */ public class S42PacketCombatEvent
/*    */   implements Packet<INetHandlerPlayClient>
/*    */ {
/*    */   public Event eventType;
/*    */   public int field_179774_b;
/*    */   public int field_179775_c;
/*    */   public int field_179772_d;
/*    */   public String deathMessage;
/*    */   
/*    */   public S42PacketCombatEvent() {}
/*    */   
/*    */   public S42PacketCombatEvent(CombatTracker combatTrackerIn, Event combatEventType)
/*    */   {
/* 25 */     this.eventType = combatEventType;
/* 26 */     EntityLivingBase entitylivingbase = combatTrackerIn.func_94550_c();
/*    */     
/* 28 */     switch (combatEventType)
/*    */     {
/*    */     case ENTER_COMBAT: 
/* 31 */       this.field_179772_d = combatTrackerIn.func_180134_f();
/* 32 */       this.field_179775_c = (entitylivingbase == null ? -1 : entitylivingbase.getEntityId());
/* 33 */       break;
/*    */     
/*    */     case ENTITY_DIED: 
/* 36 */       this.field_179774_b = combatTrackerIn.getFighter().getEntityId();
/* 37 */       this.field_179775_c = (entitylivingbase == null ? -1 : entitylivingbase.getEntityId());
/* 38 */       this.deathMessage = combatTrackerIn.getDeathMessage().getUnformattedText();
/*    */     }
/*    */     
/*    */   }
/*    */   
/*    */ 
/*    */   public void readPacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 47 */     this.eventType = ((Event)buf.readEnumValue(Event.class));
/*    */     
/* 49 */     if (this.eventType == Event.END_COMBAT)
/*    */     {
/* 51 */       this.field_179772_d = buf.readVarIntFromBuffer();
/* 52 */       this.field_179775_c = buf.readInt();
/*    */     }
/* 54 */     else if (this.eventType == Event.ENTITY_DIED)
/*    */     {
/* 56 */       this.field_179774_b = buf.readVarIntFromBuffer();
/* 57 */       this.field_179775_c = buf.readInt();
/* 58 */       this.deathMessage = buf.readStringFromBuffer(32767);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void writePacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 67 */     buf.writeEnumValue(this.eventType);
/*    */     
/* 69 */     if (this.eventType == Event.END_COMBAT)
/*    */     {
/* 71 */       buf.writeVarIntToBuffer(this.field_179772_d);
/* 72 */       buf.writeInt(this.field_179775_c);
/*    */     }
/* 74 */     else if (this.eventType == Event.ENTITY_DIED)
/*    */     {
/* 76 */       buf.writeVarIntToBuffer(this.field_179774_b);
/* 77 */       buf.writeInt(this.field_179775_c);
/* 78 */       buf.writeString(this.deathMessage);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void processPacket(INetHandlerPlayClient handler)
/*    */   {
/* 87 */     handler.handleCombatEvent(this);
/*    */   }
/*    */   
/*    */   public static enum Event
/*    */   {
/* 92 */     ENTER_COMBAT, 
/* 93 */     END_COMBAT, 
/* 94 */     ENTITY_DIED;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\network\play\server\S42PacketCombatEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */