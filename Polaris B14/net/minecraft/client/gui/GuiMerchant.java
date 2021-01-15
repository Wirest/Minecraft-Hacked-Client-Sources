/*     */ package net.minecraft.client.gui;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.inventory.GuiContainer;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.RenderHelper;
/*     */ import net.minecraft.client.renderer.entity.RenderItem;
/*     */ import net.minecraft.client.renderer.texture.TextureManager;
/*     */ import net.minecraft.client.resources.I18n;
/*     */ import net.minecraft.entity.IMerchant;
/*     */ import net.minecraft.entity.player.InventoryPlayer;
/*     */ import net.minecraft.inventory.ContainerMerchant;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.network.PacketBuffer;
/*     */ import net.minecraft.network.play.client.C17PacketCustomPayload;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraft.village.MerchantRecipe;
/*     */ import net.minecraft.village.MerchantRecipeList;
/*     */ import net.minecraft.world.World;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ 
/*     */ public class GuiMerchant extends GuiContainer
/*     */ {
/*  26 */   private static final org.apache.logging.log4j.Logger logger = ;
/*     */   
/*     */ 
/*  29 */   private static final ResourceLocation MERCHANT_GUI_TEXTURE = new ResourceLocation("textures/gui/container/villager.png");
/*     */   
/*     */ 
/*     */   private IMerchant merchant;
/*     */   
/*     */ 
/*     */   private MerchantButton nextButton;
/*     */   
/*     */ 
/*     */   private MerchantButton previousButton;
/*     */   
/*     */ 
/*     */   private int selectedMerchantRecipe;
/*     */   
/*     */ 
/*     */   private IChatComponent chatComponent;
/*     */   
/*     */ 
/*     */ 
/*     */   public GuiMerchant(InventoryPlayer p_i45500_1_, IMerchant p_i45500_2_, World worldIn)
/*     */   {
/*  50 */     super(new ContainerMerchant(p_i45500_1_, p_i45500_2_, worldIn));
/*  51 */     this.merchant = p_i45500_2_;
/*  52 */     this.chatComponent = p_i45500_2_.getDisplayName();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void initGui()
/*     */   {
/*  61 */     super.initGui();
/*  62 */     int i = (width - this.xSize) / 2;
/*  63 */     int j = (height - this.ySize) / 2;
/*  64 */     this.buttonList.add(this.nextButton = new MerchantButton(1, i + 120 + 27, j + 24 - 1, true));
/*  65 */     this.buttonList.add(this.previousButton = new MerchantButton(2, i + 36 - 19, j + 24 - 1, false));
/*  66 */     this.nextButton.enabled = false;
/*  67 */     this.previousButton.enabled = false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
/*     */   {
/*  75 */     String s = this.chatComponent.getUnformattedText();
/*  76 */     this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 6.0D, 4210752);
/*  77 */     this.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8.0D, this.ySize - 96 + 2, 4210752);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void updateScreen()
/*     */   {
/*  85 */     super.updateScreen();
/*  86 */     MerchantRecipeList merchantrecipelist = this.merchant.getRecipes(this.mc.thePlayer);
/*     */     
/*  88 */     if (merchantrecipelist != null)
/*     */     {
/*  90 */       this.nextButton.enabled = (this.selectedMerchantRecipe < merchantrecipelist.size() - 1);
/*  91 */       this.previousButton.enabled = (this.selectedMerchantRecipe > 0);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void actionPerformed(GuiButton button)
/*     */     throws java.io.IOException
/*     */   {
/* 100 */     boolean flag = false;
/*     */     
/* 102 */     if (button == this.nextButton)
/*     */     {
/* 104 */       this.selectedMerchantRecipe += 1;
/* 105 */       MerchantRecipeList merchantrecipelist = this.merchant.getRecipes(this.mc.thePlayer);
/*     */       
/* 107 */       if ((merchantrecipelist != null) && (this.selectedMerchantRecipe >= merchantrecipelist.size()))
/*     */       {
/* 109 */         this.selectedMerchantRecipe = (merchantrecipelist.size() - 1);
/*     */       }
/*     */       
/* 112 */       flag = true;
/*     */     }
/* 114 */     else if (button == this.previousButton)
/*     */     {
/* 116 */       this.selectedMerchantRecipe -= 1;
/*     */       
/* 118 */       if (this.selectedMerchantRecipe < 0)
/*     */       {
/* 120 */         this.selectedMerchantRecipe = 0;
/*     */       }
/*     */       
/* 123 */       flag = true;
/*     */     }
/*     */     
/* 126 */     if (flag)
/*     */     {
/* 128 */       ((ContainerMerchant)this.inventorySlots).setCurrentRecipeIndex(this.selectedMerchantRecipe);
/* 129 */       PacketBuffer packetbuffer = new PacketBuffer(io.netty.buffer.Unpooled.buffer());
/* 130 */       packetbuffer.writeInt(this.selectedMerchantRecipe);
/* 131 */       this.mc.getNetHandler().addToSendQueue(new C17PacketCustomPayload("MC|TrSel", packetbuffer));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
/*     */   {
/* 140 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 141 */     this.mc.getTextureManager().bindTexture(MERCHANT_GUI_TEXTURE);
/* 142 */     int i = (width - this.xSize) / 2;
/* 143 */     int j = (height - this.ySize) / 2;
/* 144 */     drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
/* 145 */     MerchantRecipeList merchantrecipelist = this.merchant.getRecipes(this.mc.thePlayer);
/*     */     
/* 147 */     if ((merchantrecipelist != null) && (!merchantrecipelist.isEmpty()))
/*     */     {
/* 149 */       int k = this.selectedMerchantRecipe;
/*     */       
/* 151 */       if ((k < 0) || (k >= merchantrecipelist.size()))
/*     */       {
/* 153 */         return;
/*     */       }
/*     */       
/* 156 */       MerchantRecipe merchantrecipe = (MerchantRecipe)merchantrecipelist.get(k);
/*     */       
/* 158 */       if (merchantrecipe.isRecipeDisabled())
/*     */       {
/* 160 */         this.mc.getTextureManager().bindTexture(MERCHANT_GUI_TEXTURE);
/* 161 */         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 162 */         GlStateManager.disableLighting();
/* 163 */         drawTexturedModalRect(this.guiLeft + 83, this.guiTop + 21, 212, 0, 28, 21);
/* 164 */         drawTexturedModalRect(this.guiLeft + 83, this.guiTop + 51, 212, 0, 28, 21);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void drawScreen(int mouseX, int mouseY, float partialTicks)
/*     */   {
/* 174 */     super.drawScreen(mouseX, mouseY, partialTicks);
/* 175 */     MerchantRecipeList merchantrecipelist = this.merchant.getRecipes(this.mc.thePlayer);
/*     */     
/* 177 */     if ((merchantrecipelist != null) && (!merchantrecipelist.isEmpty()))
/*     */     {
/* 179 */       int i = (width - this.xSize) / 2;
/* 180 */       int j = (height - this.ySize) / 2;
/* 181 */       int k = this.selectedMerchantRecipe;
/* 182 */       MerchantRecipe merchantrecipe = (MerchantRecipe)merchantrecipelist.get(k);
/* 183 */       ItemStack itemstack = merchantrecipe.getItemToBuy();
/* 184 */       ItemStack itemstack1 = merchantrecipe.getSecondItemToBuy();
/* 185 */       ItemStack itemstack2 = merchantrecipe.getItemToSell();
/* 186 */       GlStateManager.pushMatrix();
/* 187 */       RenderHelper.enableGUIStandardItemLighting();
/* 188 */       GlStateManager.disableLighting();
/* 189 */       GlStateManager.enableRescaleNormal();
/* 190 */       GlStateManager.enableColorMaterial();
/* 191 */       GlStateManager.enableLighting();
/* 192 */       this.itemRender.zLevel = 100.0F;
/* 193 */       this.itemRender.renderItemAndEffectIntoGUI(itemstack, i + 36, j + 24);
/* 194 */       this.itemRender.renderItemOverlays(this.fontRendererObj, itemstack, i + 36, j + 24);
/*     */       
/* 196 */       if (itemstack1 != null)
/*     */       {
/* 198 */         this.itemRender.renderItemAndEffectIntoGUI(itemstack1, i + 62, j + 24);
/* 199 */         this.itemRender.renderItemOverlays(this.fontRendererObj, itemstack1, i + 62, j + 24);
/*     */       }
/*     */       
/* 202 */       this.itemRender.renderItemAndEffectIntoGUI(itemstack2, i + 120, j + 24);
/* 203 */       this.itemRender.renderItemOverlays(this.fontRendererObj, itemstack2, i + 120, j + 24);
/* 204 */       this.itemRender.zLevel = 0.0F;
/* 205 */       GlStateManager.disableLighting();
/*     */       
/* 207 */       if ((isPointInRegion(36, 24, 16, 16, mouseX, mouseY)) && (itemstack != null))
/*     */       {
/* 209 */         renderToolTip(itemstack, mouseX, mouseY);
/*     */       }
/* 211 */       else if ((itemstack1 != null) && (isPointInRegion(62, 24, 16, 16, mouseX, mouseY)) && (itemstack1 != null))
/*     */       {
/* 213 */         renderToolTip(itemstack1, mouseX, mouseY);
/*     */       }
/* 215 */       else if ((itemstack2 != null) && (isPointInRegion(120, 24, 16, 16, mouseX, mouseY)) && (itemstack2 != null))
/*     */       {
/* 217 */         renderToolTip(itemstack2, mouseX, mouseY);
/*     */       }
/* 219 */       else if ((merchantrecipe.isRecipeDisabled()) && ((isPointInRegion(83, 21, 28, 21, mouseX, mouseY)) || (isPointInRegion(83, 51, 28, 21, mouseX, mouseY))))
/*     */       {
/* 221 */         drawCreativeTabHoveringText(I18n.format("merchant.deprecated", new Object[0]), mouseX, mouseY);
/*     */       }
/*     */       
/* 224 */       GlStateManager.popMatrix();
/* 225 */       GlStateManager.enableLighting();
/* 226 */       GlStateManager.enableDepth();
/* 227 */       RenderHelper.enableStandardItemLighting();
/*     */     }
/*     */   }
/*     */   
/*     */   public IMerchant getMerchant()
/*     */   {
/* 233 */     return this.merchant;
/*     */   }
/*     */   
/*     */   static class MerchantButton extends GuiButton
/*     */   {
/*     */     private final boolean field_146157_o;
/*     */     
/*     */     public MerchantButton(int buttonID, int x, int y, boolean p_i1095_4_)
/*     */     {
/* 242 */       super(x, y, 12, 19, "");
/* 243 */       this.field_146157_o = p_i1095_4_;
/*     */     }
/*     */     
/*     */     public void drawButton(Minecraft mc, int mouseX, int mouseY)
/*     */     {
/* 248 */       if (this.visible)
/*     */       {
/* 250 */         mc.getTextureManager().bindTexture(GuiMerchant.MERCHANT_GUI_TEXTURE);
/* 251 */         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 252 */         boolean flag = (mouseX >= this.xPosition) && (mouseY >= this.yPosition) && (mouseX < this.xPosition + this.width) && (mouseY < this.yPosition + this.height);
/* 253 */         int i = 0;
/* 254 */         int j = 176;
/*     */         
/* 256 */         if (!this.enabled)
/*     */         {
/* 258 */           j += this.width * 2;
/*     */         }
/* 260 */         else if (flag)
/*     */         {
/* 262 */           j += this.width;
/*     */         }
/*     */         
/* 265 */         if (!this.field_146157_o)
/*     */         {
/* 267 */           i += this.height;
/*     */         }
/*     */         
/* 270 */         drawTexturedModalRect(this.xPosition, this.yPosition, j, i, this.width, this.height);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\gui\GuiMerchant.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */