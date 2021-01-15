/*    */ package net.minecraft.server.integrated;
/*    */ 
/*    */ import com.mojang.authlib.GameProfile;
/*    */ import java.net.SocketAddress;
/*    */ import net.minecraft.entity.player.EntityPlayerMP;
/*    */ import net.minecraft.nbt.NBTTagCompound;
/*    */ import net.minecraft.server.management.ServerConfigurationManager;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class IntegratedPlayerList
/*    */   extends ServerConfigurationManager
/*    */ {
/*    */   private NBTTagCompound hostPlayerData;
/*    */   
/*    */   public IntegratedPlayerList(IntegratedServer p_i1314_1_)
/*    */   {
/* 18 */     super(p_i1314_1_);
/* 19 */     setViewDistance(10);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected void writePlayerData(EntityPlayerMP playerIn)
/*    */   {
/* 27 */     if (playerIn.getName().equals(getServerInstance().getServerOwner()))
/*    */     {
/* 29 */       this.hostPlayerData = new NBTTagCompound();
/* 30 */       playerIn.writeToNBT(this.hostPlayerData);
/*    */     }
/*    */     
/* 33 */     super.writePlayerData(playerIn);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public String allowUserToConnect(SocketAddress address, GameProfile profile)
/*    */   {
/* 41 */     return (profile.getName().equalsIgnoreCase(getServerInstance().getServerOwner())) && (getPlayerByUsername(profile.getName()) != null) ? "That name is already taken." : super.allowUserToConnect(address, profile);
/*    */   }
/*    */   
/*    */   public IntegratedServer getServerInstance()
/*    */   {
/* 46 */     return (IntegratedServer)super.getServerInstance();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public NBTTagCompound getHostPlayerData()
/*    */   {
/* 54 */     return this.hostPlayerData;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\server\integrated\IntegratedPlayerList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */