/*    */ package net.minecraft.client.gui.inventory;
/*    */ 
/*    */ import net.minecraft.client.gui.FontRenderer;
/*    */ import net.minecraft.entity.player.InventoryPlayer;
/*    */ import net.minecraft.inventory.IInventory;
/*    */ import net.minecraft.tileentity.TileEntityFurnace;
/*    */ import net.minecraft.util.IChatComponent;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public class GuiFurnace extends GuiContainer
/*    */ {
/* 12 */   private static final ResourceLocation furnaceGuiTextures = new ResourceLocation("textures/gui/container/furnace.png");
/*    */   
/*    */   private final InventoryPlayer playerInventory;
/*    */   
/*    */   private IInventory tileFurnace;
/*    */   
/*    */   public GuiFurnace(InventoryPlayer playerInv, IInventory furnaceInv)
/*    */   {
/* 20 */     super(new net.minecraft.inventory.ContainerFurnace(playerInv, furnaceInv));
/* 21 */     this.playerInventory = playerInv;
/* 22 */     this.tileFurnace = furnaceInv;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
/*    */   {
/* 30 */     String s = this.tileFurnace.getDisplayName().getUnformattedText();
/* 31 */     this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 6.0D, 4210752);
/* 32 */     this.fontRendererObj.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8.0D, this.ySize - 96 + 2, 4210752);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
/*    */   {
/* 40 */     net.minecraft.client.renderer.GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 41 */     this.mc.getTextureManager().bindTexture(furnaceGuiTextures);
/* 42 */     int i = (width - this.xSize) / 2;
/* 43 */     int j = (height - this.ySize) / 2;
/* 44 */     drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
/*    */     
/* 46 */     if (TileEntityFurnace.isBurning(this.tileFurnace))
/*    */     {
/* 48 */       int k = getBurnLeftScaled(13);
/* 49 */       drawTexturedModalRect(i + 56, j + 36 + 12 - k, 176, 12 - k, 14, k + 1);
/*    */     }
/*    */     
/* 52 */     int l = getCookProgressScaled(24);
/* 53 */     drawTexturedModalRect(i + 79, j + 34, 176, 14, l + 1, 16);
/*    */   }
/*    */   
/*    */   private int getCookProgressScaled(int pixels)
/*    */   {
/* 58 */     int i = this.tileFurnace.getField(2);
/* 59 */     int j = this.tileFurnace.getField(3);
/* 60 */     return (j != 0) && (i != 0) ? i * pixels / j : 0;
/*    */   }
/*    */   
/*    */   private int getBurnLeftScaled(int pixels)
/*    */   {
/* 65 */     int i = this.tileFurnace.getField(1);
/*    */     
/* 67 */     if (i == 0)
/*    */     {
/* 69 */       i = 200;
/*    */     }
/*    */     
/* 72 */     return this.tileFurnace.getField(0) * pixels / i;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\gui\inventory\GuiFurnace.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */