/*    */ package net.minecraft.client.gui.inventory;
/*    */ 
/*    */ import net.minecraft.client.gui.FontRenderer;
/*    */ import net.minecraft.client.resources.I18n;
/*    */ import net.minecraft.entity.player.InventoryPlayer;
/*    */ import net.minecraft.inventory.ContainerWorkbench;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class GuiCrafting extends GuiContainer
/*    */ {
/* 13 */   private static final ResourceLocation craftingTableGuiTextures = new ResourceLocation("textures/gui/container/crafting_table.png");
/*    */   
/*    */   public GuiCrafting(InventoryPlayer playerInv, World worldIn)
/*    */   {
/* 17 */     this(playerInv, worldIn, BlockPos.ORIGIN);
/*    */   }
/*    */   
/*    */   public GuiCrafting(InventoryPlayer playerInv, World worldIn, BlockPos blockPosition)
/*    */   {
/* 22 */     super(new ContainerWorkbench(playerInv, worldIn, blockPosition));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
/*    */   {
/* 30 */     this.fontRendererObj.drawString(I18n.format("container.crafting", new Object[0]), 28.0D, 6.0D, 4210752);
/* 31 */     this.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8.0D, this.ySize - 96 + 2, 4210752);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
/*    */   {
/* 39 */     net.minecraft.client.renderer.GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 40 */     this.mc.getTextureManager().bindTexture(craftingTableGuiTextures);
/* 41 */     int i = (width - this.xSize) / 2;
/* 42 */     int j = (height - this.ySize) / 2;
/* 43 */     drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\gui\inventory\GuiCrafting.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */