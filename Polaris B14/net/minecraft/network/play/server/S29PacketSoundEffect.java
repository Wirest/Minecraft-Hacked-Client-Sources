/*    */ package net.minecraft.network.play.server;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.play.INetHandlerPlayClient;
/*    */ import net.minecraft.util.MathHelper;
/*    */ import org.apache.commons.lang3.Validate;
/*    */ 
/*    */ public class S29PacketSoundEffect implements Packet<INetHandlerPlayClient>
/*    */ {
/*    */   private String soundName;
/*    */   private int posX;
/* 14 */   private int posY = Integer.MAX_VALUE;
/*    */   
/*    */   private int posZ;
/*    */   
/*    */   private float soundVolume;
/*    */   private int soundPitch;
/*    */   
/*    */   public S29PacketSoundEffect() {}
/*    */   
/*    */   public S29PacketSoundEffect(String soundNameIn, double soundX, double soundY, double soundZ, float volume, float pitch)
/*    */   {
/* 25 */     Validate.notNull(soundNameIn, "name", new Object[0]);
/* 26 */     this.soundName = soundNameIn;
/* 27 */     this.posX = ((int)(soundX * 8.0D));
/* 28 */     this.posY = ((int)(soundY * 8.0D));
/* 29 */     this.posZ = ((int)(soundZ * 8.0D));
/* 30 */     this.soundVolume = volume;
/* 31 */     this.soundPitch = ((int)(pitch * 63.0F));
/* 32 */     pitch = MathHelper.clamp_float(pitch, 0.0F, 255.0F);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void readPacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 40 */     this.soundName = buf.readStringFromBuffer(256);
/* 41 */     this.posX = buf.readInt();
/* 42 */     this.posY = buf.readInt();
/* 43 */     this.posZ = buf.readInt();
/* 44 */     this.soundVolume = buf.readFloat();
/* 45 */     this.soundPitch = buf.readUnsignedByte();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void writePacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 53 */     buf.writeString(this.soundName);
/* 54 */     buf.writeInt(this.posX);
/* 55 */     buf.writeInt(this.posY);
/* 56 */     buf.writeInt(this.posZ);
/* 57 */     buf.writeFloat(this.soundVolume);
/* 58 */     buf.writeByte(this.soundPitch);
/*    */   }
/*    */   
/*    */   public String getSoundName()
/*    */   {
/* 63 */     return this.soundName;
/*    */   }
/*    */   
/*    */   public double getX()
/*    */   {
/* 68 */     return this.posX / 8.0F;
/*    */   }
/*    */   
/*    */   public double getY()
/*    */   {
/* 73 */     return this.posY / 8.0F;
/*    */   }
/*    */   
/*    */   public double getZ()
/*    */   {
/* 78 */     return this.posZ / 8.0F;
/*    */   }
/*    */   
/*    */   public float getVolume()
/*    */   {
/* 83 */     return this.soundVolume;
/*    */   }
/*    */   
/*    */   public float getPitch()
/*    */   {
/* 88 */     return this.soundPitch / 63.0F;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void processPacket(INetHandlerPlayClient handler)
/*    */   {
/* 96 */     handler.handleSoundEffect(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\network\play\server\S29PacketSoundEffect.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */