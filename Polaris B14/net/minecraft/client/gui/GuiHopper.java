/*    */ package net.minecraft.client.gui;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.inventory.GuiContainer;
/*    */ import net.minecraft.entity.player.InventoryPlayer;
/*    */ import net.minecraft.inventory.IInventory;
/*    */ import net.minecraft.util.IChatComponent;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public class GuiHopper extends GuiContainer
/*    */ {
/* 12 */   private static final ResourceLocation HOPPER_GUI_TEXTURE = new ResourceLocation("textures/gui/container/hopper.png");
/*    */   private IInventory playerInventory;
/*    */   private IInventory hopperInventory;
/*    */   
/*    */   public GuiHopper(InventoryPlayer playerInv, IInventory hopperInv) {
/* 17 */     super(new net.minecraft.inventory.ContainerHopper(playerInv, hopperInv, Minecraft.getMinecraft().thePlayer));
/* 18 */     this.playerInventory = playerInv;
/* 19 */     this.hopperInventory = hopperInv;
/* 20 */     this.allowUserInput = false;
/* 21 */     this.ySize = 133;
/*    */   }
/*    */   
/*    */   protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
/* 25 */     this.fontRendererObj.drawString(this.hopperInventory.getDisplayName().getUnformattedText(), 8.0D, 6.0D, 4210752);
/* 26 */     this.fontRendererObj.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8.0D, this.ySize - 96 + 2, 4210752);
/*    */   }
/*    */   
/*    */   protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
/* 30 */     net.minecraft.client.renderer.GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 31 */     this.mc.getTextureManager().bindTexture(HOPPER_GUI_TEXTURE);
/* 32 */     int i = (width - this.xSize) / 2;
/* 33 */     int j = (height - this.ySize) / 2;
/* 34 */     drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\gui\GuiHopper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */