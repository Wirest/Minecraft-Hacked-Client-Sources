/*    */ package net.minecraft.client.gui.spectator;
/*    */ 
/*    */ import com.mojang.authlib.GameProfile;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.entity.AbstractClientPlayer;
/*    */ import net.minecraft.client.gui.Gui;
/*    */ import net.minecraft.client.network.NetHandlerPlayClient;
/*    */ import net.minecraft.network.play.client.C18PacketSpectate;
/*    */ import net.minecraft.util.ChatComponentText;
/*    */ import net.minecraft.util.IChatComponent;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public class PlayerMenuObject implements ISpectatorMenuObject
/*    */ {
/*    */   private final GameProfile profile;
/*    */   private final ResourceLocation resourceLocation;
/*    */   
/*    */   public PlayerMenuObject(GameProfile profileIn)
/*    */   {
/* 20 */     this.profile = profileIn;
/* 21 */     this.resourceLocation = AbstractClientPlayer.getLocationSkin(profileIn.getName());
/* 22 */     AbstractClientPlayer.getDownloadImageSkin(this.resourceLocation, profileIn.getName());
/*    */   }
/*    */   
/*    */   public void func_178661_a(SpectatorMenu menu)
/*    */   {
/* 27 */     Minecraft.getMinecraft().getNetHandler().addToSendQueue(new C18PacketSpectate(this.profile.getId()));
/*    */   }
/*    */   
/*    */   public IChatComponent getSpectatorName()
/*    */   {
/* 32 */     return new ChatComponentText(this.profile.getName());
/*    */   }
/*    */   
/*    */   public void func_178663_a(float p_178663_1_, int alpha)
/*    */   {
/* 37 */     Minecraft.getMinecraft().getTextureManager().bindTexture(this.resourceLocation);
/* 38 */     net.minecraft.client.renderer.GlStateManager.color(1.0F, 1.0F, 1.0F, alpha / 255.0F);
/* 39 */     Gui.drawScaledCustomSizeModalRect(2, 2, 8.0F, 8.0F, 8, 8, 12, 12, 64.0F, 64.0F);
/* 40 */     Gui.drawScaledCustomSizeModalRect(2, 2, 40.0F, 8.0F, 8, 8, 12, 12, 64.0F, 64.0F);
/*    */   }
/*    */   
/*    */   public boolean func_178662_A_()
/*    */   {
/* 45 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\gui\spectator\PlayerMenuObject.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */