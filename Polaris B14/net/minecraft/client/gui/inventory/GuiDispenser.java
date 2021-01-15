/*    */ package net.minecraft.client.gui.inventory;
/*    */ 
/*    */ import net.minecraft.client.gui.FontRenderer;
/*    */ import net.minecraft.entity.player.InventoryPlayer;
/*    */ import net.minecraft.inventory.IInventory;
/*    */ import net.minecraft.util.IChatComponent;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public class GuiDispenser extends GuiContainer
/*    */ {
/* 11 */   private static final ResourceLocation dispenserGuiTextures = new ResourceLocation("textures/gui/container/dispenser.png");
/*    */   
/*    */ 
/*    */   private final InventoryPlayer playerInventory;
/*    */   
/*    */   public IInventory dispenserInventory;
/*    */   
/*    */ 
/*    */   public GuiDispenser(InventoryPlayer playerInv, IInventory dispenserInv)
/*    */   {
/* 21 */     super(new net.minecraft.inventory.ContainerDispenser(playerInv, dispenserInv));
/* 22 */     this.playerInventory = playerInv;
/* 23 */     this.dispenserInventory = dispenserInv;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
/*    */   {
/* 31 */     String s = this.dispenserInventory.getDisplayName().getUnformattedText();
/* 32 */     this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 6.0D, 4210752);
/* 33 */     this.fontRendererObj.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8.0D, this.ySize - 96 + 2, 4210752);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
/*    */   {
/* 41 */     net.minecraft.client.renderer.GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 42 */     this.mc.getTextureManager().bindTexture(dispenserGuiTextures);
/* 43 */     int i = (width - this.xSize) / 2;
/* 44 */     int j = (height - this.ySize) / 2;
/* 45 */     drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\gui\inventory\GuiDispenser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */