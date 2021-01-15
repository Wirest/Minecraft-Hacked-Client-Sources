/*    */ package net.minecraft.network.play.client;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.entity.player.EntityPlayer.EnumChatVisibility;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.play.INetHandlerPlayServer;
/*    */ 
/*    */ 
/*    */ public class C15PacketClientSettings
/*    */   implements Packet<INetHandlerPlayServer>
/*    */ {
/*    */   private String lang;
/*    */   private int view;
/*    */   private EntityPlayer.EnumChatVisibility chatVisibility;
/*    */   private boolean enableColors;
/*    */   private int modelPartFlags;
/*    */   
/*    */   public C15PacketClientSettings() {}
/*    */   
/*    */   public C15PacketClientSettings(String langIn, int viewIn, EntityPlayer.EnumChatVisibility chatVisibilityIn, boolean enableColorsIn, int modelPartFlagsIn)
/*    */   {
/* 23 */     this.lang = langIn;
/* 24 */     this.view = viewIn;
/* 25 */     this.chatVisibility = chatVisibilityIn;
/* 26 */     this.enableColors = enableColorsIn;
/* 27 */     this.modelPartFlags = modelPartFlagsIn;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void readPacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 35 */     this.lang = buf.readStringFromBuffer(7);
/* 36 */     this.view = buf.readByte();
/* 37 */     this.chatVisibility = EntityPlayer.EnumChatVisibility.getEnumChatVisibility(buf.readByte());
/* 38 */     this.enableColors = buf.readBoolean();
/* 39 */     this.modelPartFlags = buf.readUnsignedByte();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void writePacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 47 */     buf.writeString(this.lang);
/* 48 */     buf.writeByte(this.view);
/* 49 */     buf.writeByte(this.chatVisibility.getChatVisibility());
/* 50 */     buf.writeBoolean(this.enableColors);
/* 51 */     buf.writeByte(this.modelPartFlags);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void processPacket(INetHandlerPlayServer handler)
/*    */   {
/* 59 */     handler.processClientSettings(this);
/*    */   }
/*    */   
/*    */   public String getLang()
/*    */   {
/* 64 */     return this.lang;
/*    */   }
/*    */   
/*    */   public EntityPlayer.EnumChatVisibility getChatVisibility()
/*    */   {
/* 69 */     return this.chatVisibility;
/*    */   }
/*    */   
/*    */   public boolean isColorsEnabled()
/*    */   {
/* 74 */     return this.enableColors;
/*    */   }
/*    */   
/*    */   public int getModelPartFlags()
/*    */   {
/* 79 */     return this.modelPartFlags;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\network\play\client\C15PacketClientSettings.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */