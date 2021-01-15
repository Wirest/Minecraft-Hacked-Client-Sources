/*    */ package net.minecraft.client.gui.inventory;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.FontRenderer;
/*    */ import net.minecraft.entity.passive.EntityHorse;
/*    */ import net.minecraft.inventory.IInventory;
/*    */ import net.minecraft.util.IChatComponent;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public class GuiScreenHorseInventory extends GuiContainer
/*    */ {
/* 12 */   private static final ResourceLocation horseGuiTextures = new ResourceLocation("textures/gui/container/horse.png");
/*    */   
/*    */ 
/*    */   private IInventory playerInventory;
/*    */   
/*    */ 
/*    */   private IInventory horseInventory;
/*    */   
/*    */ 
/*    */   private EntityHorse horseEntity;
/*    */   
/*    */ 
/*    */   private float mousePosx;
/*    */   
/*    */   private float mousePosY;
/*    */   
/*    */ 
/*    */   public GuiScreenHorseInventory(IInventory playerInv, IInventory horseInv, EntityHorse horse)
/*    */   {
/* 31 */     super(new net.minecraft.inventory.ContainerHorseInventory(playerInv, horseInv, horse, Minecraft.getMinecraft().thePlayer));
/* 32 */     this.playerInventory = playerInv;
/* 33 */     this.horseInventory = horseInv;
/* 34 */     this.horseEntity = horse;
/* 35 */     this.allowUserInput = false;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
/*    */   {
/* 43 */     this.fontRendererObj.drawString(this.horseInventory.getDisplayName().getUnformattedText(), 8.0D, 6.0D, 4210752);
/* 44 */     this.fontRendererObj.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8.0D, this.ySize - 96 + 2, 4210752);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
/*    */   {
/* 52 */     net.minecraft.client.renderer.GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 53 */     this.mc.getTextureManager().bindTexture(horseGuiTextures);
/* 54 */     int i = (width - this.xSize) / 2;
/* 55 */     int j = (height - this.ySize) / 2;
/* 56 */     drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
/*    */     
/* 58 */     if (this.horseEntity.isChested())
/*    */     {
/* 60 */       drawTexturedModalRect(i + 79, j + 17, 0, this.ySize, 90, 54);
/*    */     }
/*    */     
/* 63 */     if (this.horseEntity.canWearArmor())
/*    */     {
/* 65 */       drawTexturedModalRect(i + 7, j + 35, 0, this.ySize + 54, 18, 18);
/*    */     }
/*    */     
/* 68 */     GuiInventory.drawEntityOnScreen(i + 51, j + 60, 17, i + 51 - this.mousePosx, j + 75 - 50 - this.mousePosY, this.horseEntity);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void drawScreen(int mouseX, int mouseY, float partialTicks)
/*    */   {
/* 76 */     this.mousePosx = mouseX;
/* 77 */     this.mousePosY = mouseY;
/* 78 */     super.drawScreen(mouseX, mouseY, partialTicks);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\gui\inventory\GuiScreenHorseInventory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */