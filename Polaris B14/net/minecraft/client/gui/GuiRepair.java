/*     */ package net.minecraft.client.gui;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.entity.EntityPlayerSP;
/*     */ import net.minecraft.client.gui.inventory.GuiContainer;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.resources.I18n;
/*     */ import net.minecraft.entity.player.InventoryPlayer;
/*     */ import net.minecraft.entity.player.PlayerCapabilities;
/*     */ import net.minecraft.inventory.Container;
/*     */ import net.minecraft.inventory.ContainerRepair;
/*     */ import net.minecraft.inventory.IInventory;
/*     */ import net.minecraft.inventory.Slot;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.network.PacketBuffer;
/*     */ import net.minecraft.network.play.client.C17PacketCustomPayload;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraft.world.World;
/*     */ import org.lwjgl.input.Keyboard;
/*     */ 
/*     */ public class GuiRepair extends GuiContainer implements net.minecraft.inventory.ICrafting
/*     */ {
/*  25 */   private static final ResourceLocation anvilResource = new ResourceLocation("textures/gui/container/anvil.png");
/*     */   private ContainerRepair anvil;
/*     */   private GuiTextField nameField;
/*     */   private InventoryPlayer playerInventory;
/*     */   
/*     */   public GuiRepair(InventoryPlayer inventoryIn, World worldIn)
/*     */   {
/*  32 */     super(new ContainerRepair(inventoryIn, worldIn, Minecraft.getMinecraft().thePlayer));
/*  33 */     this.playerInventory = inventoryIn;
/*  34 */     this.anvil = ((ContainerRepair)this.inventorySlots);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void initGui()
/*     */   {
/*  43 */     super.initGui();
/*  44 */     Keyboard.enableRepeatEvents(true);
/*  45 */     int i = (width - this.xSize) / 2;
/*  46 */     int j = (height - this.ySize) / 2;
/*  47 */     this.nameField = new GuiTextField(0, this.fontRendererObj, i + 62, j + 24, 103, 12);
/*  48 */     this.nameField.setTextColor(-1);
/*  49 */     this.nameField.setDisabledTextColour(-1);
/*  50 */     this.nameField.setEnableBackgroundDrawing(false);
/*  51 */     this.nameField.setMaxStringLength(30);
/*  52 */     this.inventorySlots.removeCraftingFromCrafters(this);
/*  53 */     this.inventorySlots.onCraftGuiOpened(this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onGuiClosed()
/*     */   {
/*  61 */     super.onGuiClosed();
/*  62 */     Keyboard.enableRepeatEvents(false);
/*  63 */     this.inventorySlots.removeCraftingFromCrafters(this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
/*     */   {
/*  71 */     GlStateManager.disableLighting();
/*  72 */     GlStateManager.disableBlend();
/*  73 */     this.fontRendererObj.drawString(I18n.format("container.repair", new Object[0]), 60.0D, 6.0D, 4210752);
/*     */     
/*  75 */     if (this.anvil.maximumCost > 0)
/*     */     {
/*  77 */       int i = 8453920;
/*  78 */       boolean flag = true;
/*  79 */       String s = I18n.format("container.repair.cost", new Object[] { Integer.valueOf(this.anvil.maximumCost) });
/*     */       
/*  81 */       if ((this.anvil.maximumCost >= 40) && (!this.mc.thePlayer.capabilities.isCreativeMode))
/*     */       {
/*  83 */         s = I18n.format("container.repair.expensive", new Object[0]);
/*  84 */         i = 16736352;
/*     */       }
/*  86 */       else if (!this.anvil.getSlot(2).getHasStack())
/*     */       {
/*  88 */         flag = false;
/*     */       }
/*  90 */       else if (!this.anvil.getSlot(2).canTakeStack(this.playerInventory.player))
/*     */       {
/*  92 */         i = 16736352;
/*     */       }
/*     */       
/*  95 */       if (flag)
/*     */       {
/*  97 */         int j = 0xFF000000 | (i & 0xFCFCFC) >> 2 | i & 0xFF000000;
/*  98 */         int k = this.xSize - 8 - this.fontRendererObj.getStringWidth(s);
/*  99 */         int l = 67;
/*     */         
/* 101 */         if (this.fontRendererObj.getUnicodeFlag())
/*     */         {
/* 103 */           drawRect(k - 3, l - 2, this.xSize - 7, l + 10, -16777216);
/* 104 */           drawRect(k - 2, l - 1, this.xSize - 8, l + 9, -12895429);
/*     */         }
/*     */         else
/*     */         {
/* 108 */           this.fontRendererObj.drawString(s, k, l + 1, j);
/* 109 */           this.fontRendererObj.drawString(s, k + 1, l, j);
/* 110 */           this.fontRendererObj.drawString(s, k + 1, l + 1, j);
/*     */         }
/*     */         
/* 113 */         this.fontRendererObj.drawString(s, k, l, i);
/*     */       }
/*     */     }
/*     */     
/* 117 */     GlStateManager.enableLighting();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void keyTyped(char typedChar, int keyCode)
/*     */     throws IOException
/*     */   {
/* 126 */     if (this.nameField.textboxKeyTyped(typedChar, keyCode))
/*     */     {
/* 128 */       renameItem();
/*     */     }
/*     */     else
/*     */     {
/* 132 */       super.keyTyped(typedChar, keyCode);
/*     */     }
/*     */   }
/*     */   
/*     */   private void renameItem()
/*     */   {
/* 138 */     String s = this.nameField.getText();
/* 139 */     Slot slot = this.anvil.getSlot(0);
/*     */     
/* 141 */     if ((slot != null) && (slot.getHasStack()) && (!slot.getStack().hasDisplayName()) && (s.equals(slot.getStack().getDisplayName())))
/*     */     {
/* 143 */       s = "";
/*     */     }
/*     */     
/* 146 */     this.anvil.updateItemName(s);
/* 147 */     this.mc.thePlayer.sendQueue.addToSendQueue(new C17PacketCustomPayload("MC|ItemName", new PacketBuffer(io.netty.buffer.Unpooled.buffer()).writeString(s)));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void mouseClicked(int mouseX, int mouseY, int mouseButton)
/*     */     throws IOException
/*     */   {
/* 155 */     super.mouseClicked(mouseX, mouseY, mouseButton);
/* 156 */     this.nameField.mouseClicked(mouseX, mouseY, mouseButton);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void drawScreen(int mouseX, int mouseY, float partialTicks)
/*     */   {
/* 164 */     super.drawScreen(mouseX, mouseY, partialTicks);
/* 165 */     GlStateManager.disableLighting();
/* 166 */     GlStateManager.disableBlend();
/* 167 */     this.nameField.drawTextBox();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
/*     */   {
/* 175 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 176 */     this.mc.getTextureManager().bindTexture(anvilResource);
/* 177 */     int i = (width - this.xSize) / 2;
/* 178 */     int j = (height - this.ySize) / 2;
/* 179 */     drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
/* 180 */     drawTexturedModalRect(i + 59, j + 20, 0, this.ySize + (this.anvil.getSlot(0).getHasStack() ? 0 : 16), 110, 16);
/*     */     
/* 182 */     if (((this.anvil.getSlot(0).getHasStack()) || (this.anvil.getSlot(1).getHasStack())) && (!this.anvil.getSlot(2).getHasStack()))
/*     */     {
/* 184 */       drawTexturedModalRect(i + 99, j + 45, this.xSize, 0, 28, 21);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void updateCraftingInventory(Container containerToSend, List<ItemStack> itemsList)
/*     */   {
/* 193 */     sendSlotContents(containerToSend, 0, containerToSend.getSlot(0).getStack());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void sendSlotContents(Container containerToSend, int slotInd, ItemStack stack)
/*     */   {
/* 202 */     if (slotInd == 0)
/*     */     {
/* 204 */       this.nameField.setText(stack == null ? "" : stack.getDisplayName());
/* 205 */       this.nameField.setEnabled(stack != null);
/*     */       
/* 207 */       if (stack != null)
/*     */       {
/* 209 */         renameItem();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void sendProgressBarUpdate(Container containerIn, int varToUpdate, int newValue) {}
/*     */   
/*     */   public void func_175173_a(Container p_175173_1_, IInventory p_175173_2_) {}
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\gui\GuiRepair.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */