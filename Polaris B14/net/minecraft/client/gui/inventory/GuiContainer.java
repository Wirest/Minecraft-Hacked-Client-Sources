/*     */ package net.minecraft.client.gui.inventory;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.entity.EntityPlayerSP;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.OpenGlHelper;
/*     */ import net.minecraft.client.renderer.RenderHelper;
/*     */ import net.minecraft.client.renderer.entity.RenderItem;
/*     */ import net.minecraft.client.renderer.texture.TextureMap;
/*     */ import net.minecraft.client.settings.GameSettings;
/*     */ import net.minecraft.client.settings.KeyBinding;
/*     */ import net.minecraft.entity.player.InventoryPlayer;
/*     */ import net.minecraft.inventory.Container;
/*     */ import net.minecraft.inventory.Slot;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import org.lwjgl.input.Keyboard;
/*     */ 
/*     */ public abstract class GuiContainer extends GuiScreen
/*     */ {
/*  25 */   protected static final ResourceLocation inventoryBackground = new ResourceLocation("textures/gui/container/inventory.png");
/*     */   
/*     */ 
/*  28 */   protected int xSize = 176;
/*     */   
/*     */ 
/*  31 */   protected int ySize = 166;
/*     */   
/*     */ 
/*     */   public Container inventorySlots;
/*     */   
/*     */ 
/*     */   protected int guiLeft;
/*     */   
/*     */ 
/*     */   protected int guiTop;
/*     */   
/*     */ 
/*     */   private Slot theSlot;
/*     */   
/*     */ 
/*     */   private Slot clickedSlot;
/*     */   
/*     */   private boolean isRightMouseClick;
/*     */   
/*     */   private ItemStack draggedStack;
/*     */   
/*     */   private int touchUpX;
/*     */   
/*     */   private int touchUpY;
/*     */   
/*     */   private Slot returningStackDestSlot;
/*     */   
/*     */   private long returningStackTime;
/*     */   
/*     */   private ItemStack returningStack;
/*     */   
/*     */   private Slot currentDragTargetSlot;
/*     */   
/*     */   private long dragItemDropDelay;
/*     */   
/*  66 */   protected final Set<Slot> dragSplittingSlots = com.google.common.collect.Sets.newHashSet();
/*     */   protected boolean dragSplitting;
/*     */   private int dragSplittingLimit;
/*     */   private int dragSplittingButton;
/*     */   private boolean ignoreMouseUp;
/*     */   private int dragSplittingRemnant;
/*     */   private long lastClickTime;
/*     */   private Slot lastClickSlot;
/*     */   private int lastClickButton;
/*     */   private boolean doubleClick;
/*     */   private ItemStack shiftClickedSlot;
/*     */   
/*     */   public GuiContainer(Container inventorySlotsIn)
/*     */   {
/*  80 */     this.inventorySlots = inventorySlotsIn;
/*  81 */     this.ignoreMouseUp = true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void initGui()
/*     */   {
/*  90 */     super.initGui();
/*  91 */     this.mc.thePlayer.openContainer = this.inventorySlots;
/*  92 */     this.guiLeft = ((width - this.xSize) / 2);
/*  93 */     this.guiTop = ((height - this.ySize) / 2);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void drawScreen(int mouseX, int mouseY, float partialTicks)
/*     */   {
/* 101 */     drawDefaultBackground();
/* 102 */     int i = this.guiLeft;
/* 103 */     int j = this.guiTop;
/* 104 */     drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
/* 105 */     GlStateManager.disableRescaleNormal();
/* 106 */     RenderHelper.disableStandardItemLighting();
/* 107 */     GlStateManager.disableLighting();
/* 108 */     GlStateManager.disableDepth();
/* 109 */     super.drawScreen(mouseX, mouseY, partialTicks);
/* 110 */     RenderHelper.enableGUIStandardItemLighting();
/* 111 */     GlStateManager.pushMatrix();
/* 112 */     GlStateManager.translate(i, j, 0.0F);
/* 113 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 114 */     GlStateManager.enableRescaleNormal();
/* 115 */     this.theSlot = null;
/* 116 */     int k = 240;
/* 117 */     int l = 240;
/* 118 */     OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, k / 1.0F, l / 1.0F);
/* 119 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/*     */     
/* 121 */     for (int i1 = 0; i1 < this.inventorySlots.inventorySlots.size(); i1++)
/*     */     {
/* 123 */       Slot slot = (Slot)this.inventorySlots.inventorySlots.get(i1);
/* 124 */       drawSlot(slot);
/*     */       
/* 126 */       if ((isMouseOverSlot(slot, mouseX, mouseY)) && (slot.canBeHovered()))
/*     */       {
/* 128 */         this.theSlot = slot;
/* 129 */         GlStateManager.disableLighting();
/* 130 */         GlStateManager.disableDepth();
/* 131 */         int j1 = slot.xDisplayPosition;
/* 132 */         int k1 = slot.yDisplayPosition;
/* 133 */         GlStateManager.colorMask(true, true, true, false);
/* 134 */         drawGradientRect(j1, k1, j1 + 16, k1 + 16, -2130706433, -2130706433);
/* 135 */         GlStateManager.colorMask(true, true, true, true);
/* 136 */         GlStateManager.enableLighting();
/* 137 */         GlStateManager.enableDepth();
/*     */       }
/*     */     }
/*     */     
/* 141 */     RenderHelper.disableStandardItemLighting();
/* 142 */     drawGuiContainerForegroundLayer(mouseX, mouseY);
/* 143 */     RenderHelper.enableGUIStandardItemLighting();
/* 144 */     InventoryPlayer inventoryplayer = this.mc.thePlayer.inventory;
/* 145 */     ItemStack itemstack = this.draggedStack == null ? inventoryplayer.getItemStack() : this.draggedStack;
/*     */     
/* 147 */     if (itemstack != null)
/*     */     {
/* 149 */       int j2 = 8;
/* 150 */       int k2 = this.draggedStack == null ? 8 : 16;
/* 151 */       String s = null;
/*     */       
/* 153 */       if ((this.draggedStack != null) && (this.isRightMouseClick))
/*     */       {
/* 155 */         itemstack = itemstack.copy();
/* 156 */         itemstack.stackSize = net.minecraft.util.MathHelper.ceiling_float_int(itemstack.stackSize / 2.0F);
/*     */       }
/* 158 */       else if ((this.dragSplitting) && (this.dragSplittingSlots.size() > 1))
/*     */       {
/* 160 */         itemstack = itemstack.copy();
/* 161 */         itemstack.stackSize = this.dragSplittingRemnant;
/*     */         
/* 163 */         if (itemstack.stackSize == 0)
/*     */         {
/* 165 */           s = EnumChatFormatting.YELLOW + "0";
/*     */         }
/*     */       }
/*     */       
/* 169 */       drawItemStack(itemstack, mouseX - i - j2, mouseY - j - k2, s);
/*     */     }
/*     */     
/* 172 */     if (this.returningStack != null)
/*     */     {
/* 174 */       float f = (float)(Minecraft.getSystemTime() - this.returningStackTime) / 100.0F;
/*     */       
/* 176 */       if (f >= 1.0F)
/*     */       {
/* 178 */         f = 1.0F;
/* 179 */         this.returningStack = null;
/*     */       }
/*     */       
/* 182 */       int l2 = this.returningStackDestSlot.xDisplayPosition - this.touchUpX;
/* 183 */       int i3 = this.returningStackDestSlot.yDisplayPosition - this.touchUpY;
/* 184 */       int l1 = this.touchUpX + (int)(l2 * f);
/* 185 */       int i2 = this.touchUpY + (int)(i3 * f);
/* 186 */       drawItemStack(this.returningStack, l1, i2, null);
/*     */     }
/*     */     
/* 189 */     GlStateManager.popMatrix();
/*     */     
/* 191 */     if ((inventoryplayer.getItemStack() == null) && (this.theSlot != null) && (this.theSlot.getHasStack()))
/*     */     {
/* 193 */       ItemStack itemstack1 = this.theSlot.getStack();
/* 194 */       renderToolTip(itemstack1, mouseX, mouseY);
/*     */     }
/*     */     
/* 197 */     GlStateManager.enableLighting();
/* 198 */     GlStateManager.enableDepth();
/* 199 */     RenderHelper.enableStandardItemLighting();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void drawItemStack(ItemStack stack, int x, int y, String altText)
/*     */   {
/* 207 */     GlStateManager.translate(0.0F, 0.0F, 32.0F);
/* 208 */     this.zLevel = 200.0F;
/* 209 */     this.itemRender.zLevel = 200.0F;
/* 210 */     this.itemRender.renderItemAndEffectIntoGUI(stack, x, y);
/* 211 */     this.itemRender.renderItemOverlayIntoGUI(this.fontRendererObj, stack, x, y - (this.draggedStack == null ? 0 : 8), altText);
/* 212 */     this.zLevel = 0.0F;
/* 213 */     this.itemRender.zLevel = 0.0F;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract void drawGuiContainerBackgroundLayer(float paramFloat, int paramInt1, int paramInt2);
/*     */   
/*     */ 
/*     */ 
/*     */   private void drawSlot(Slot slotIn)
/*     */   {
/* 230 */     int i = slotIn.xDisplayPosition;
/* 231 */     int j = slotIn.yDisplayPosition;
/* 232 */     ItemStack itemstack = slotIn.getStack();
/* 233 */     boolean flag = false;
/* 234 */     boolean flag1 = (slotIn == this.clickedSlot) && (this.draggedStack != null) && (!this.isRightMouseClick);
/* 235 */     ItemStack itemstack1 = this.mc.thePlayer.inventory.getItemStack();
/* 236 */     String s = null;
/*     */     
/* 238 */     if ((slotIn == this.clickedSlot) && (this.draggedStack != null) && (this.isRightMouseClick) && (itemstack != null))
/*     */     {
/* 240 */       itemstack = itemstack.copy();
/* 241 */       itemstack.stackSize /= 2;
/*     */     }
/* 243 */     else if ((this.dragSplitting) && (this.dragSplittingSlots.contains(slotIn)) && (itemstack1 != null))
/*     */     {
/* 245 */       if (this.dragSplittingSlots.size() == 1)
/*     */       {
/* 247 */         return;
/*     */       }
/*     */       
/* 250 */       if ((Container.canAddItemToSlot(slotIn, itemstack1, true)) && (this.inventorySlots.canDragIntoSlot(slotIn)))
/*     */       {
/* 252 */         itemstack = itemstack1.copy();
/* 253 */         flag = true;
/* 254 */         Container.computeStackSize(this.dragSplittingSlots, this.dragSplittingLimit, itemstack, slotIn.getStack() == null ? 0 : slotIn.getStack().stackSize);
/*     */         
/* 256 */         if (itemstack.stackSize > itemstack.getMaxStackSize())
/*     */         {
/* 258 */           s = EnumChatFormatting.YELLOW + itemstack.getMaxStackSize();
/* 259 */           itemstack.stackSize = itemstack.getMaxStackSize();
/*     */         }
/*     */         
/* 262 */         if (itemstack.stackSize > slotIn.getItemStackLimit(itemstack))
/*     */         {
/* 264 */           s = EnumChatFormatting.YELLOW + slotIn.getItemStackLimit(itemstack);
/* 265 */           itemstack.stackSize = slotIn.getItemStackLimit(itemstack);
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 270 */         this.dragSplittingSlots.remove(slotIn);
/* 271 */         updateDragSplitting();
/*     */       }
/*     */     }
/*     */     
/* 275 */     this.zLevel = 100.0F;
/* 276 */     this.itemRender.zLevel = 100.0F;
/*     */     
/* 278 */     if (itemstack == null)
/*     */     {
/* 280 */       String s1 = slotIn.getSlotTexture();
/*     */       
/* 282 */       if (s1 != null)
/*     */       {
/* 284 */         net.minecraft.client.renderer.texture.TextureAtlasSprite textureatlassprite = this.mc.getTextureMapBlocks().getAtlasSprite(s1);
/* 285 */         GlStateManager.disableLighting();
/* 286 */         this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
/* 287 */         drawTexturedModalRect(i, j, textureatlassprite, 16, 16);
/* 288 */         GlStateManager.enableLighting();
/* 289 */         flag1 = true;
/*     */       }
/*     */     }
/*     */     
/* 293 */     if (!flag1)
/*     */     {
/* 295 */       if (flag)
/*     */       {
/* 297 */         drawRect(i, j, i + 16, j + 16, -2130706433);
/*     */       }
/*     */       
/* 300 */       GlStateManager.enableDepth();
/* 301 */       this.itemRender.renderItemAndEffectIntoGUI(itemstack, i, j);
/* 302 */       this.itemRender.renderItemOverlayIntoGUI(this.fontRendererObj, itemstack, i, j, s);
/*     */     }
/*     */     
/* 305 */     this.itemRender.zLevel = 0.0F;
/* 306 */     this.zLevel = 0.0F;
/*     */   }
/*     */   
/*     */   private void updateDragSplitting()
/*     */   {
/* 311 */     ItemStack itemstack = this.mc.thePlayer.inventory.getItemStack();
/*     */     
/* 313 */     if ((itemstack != null) && (this.dragSplitting))
/*     */     {
/* 315 */       this.dragSplittingRemnant = itemstack.stackSize;
/*     */       
/* 317 */       for (Slot slot : this.dragSplittingSlots)
/*     */       {
/* 319 */         ItemStack itemstack1 = itemstack.copy();
/* 320 */         int i = slot.getStack() == null ? 0 : slot.getStack().stackSize;
/* 321 */         Container.computeStackSize(this.dragSplittingSlots, this.dragSplittingLimit, itemstack1, i);
/*     */         
/* 323 */         if (itemstack1.stackSize > itemstack1.getMaxStackSize())
/*     */         {
/* 325 */           itemstack1.stackSize = itemstack1.getMaxStackSize();
/*     */         }
/*     */         
/* 328 */         if (itemstack1.stackSize > slot.getItemStackLimit(itemstack1))
/*     */         {
/* 330 */           itemstack1.stackSize = slot.getItemStackLimit(itemstack1);
/*     */         }
/*     */         
/* 333 */         this.dragSplittingRemnant -= itemstack1.stackSize - i;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private Slot getSlotAtPosition(int x, int y)
/*     */   {
/* 343 */     for (int i = 0; i < this.inventorySlots.inventorySlots.size(); i++)
/*     */     {
/* 345 */       Slot slot = (Slot)this.inventorySlots.inventorySlots.get(i);
/*     */       
/* 347 */       if (isMouseOverSlot(slot, x, y))
/*     */       {
/* 349 */         return slot;
/*     */       }
/*     */     }
/*     */     
/* 353 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void mouseClicked(int mouseX, int mouseY, int mouseButton)
/*     */     throws java.io.IOException
/*     */   {
/* 361 */     super.mouseClicked(mouseX, mouseY, mouseButton);
/* 362 */     boolean flag = mouseButton == this.mc.gameSettings.keyBindPickBlock.getKeyCode() + 100;
/* 363 */     Slot slot = getSlotAtPosition(mouseX, mouseY);
/* 364 */     long i = Minecraft.getSystemTime();
/* 365 */     this.doubleClick = ((this.lastClickSlot == slot) && (i - this.lastClickTime < 250L) && (this.lastClickButton == mouseButton));
/* 366 */     this.ignoreMouseUp = false;
/*     */     
/* 368 */     if ((mouseButton == 0) || (mouseButton == 1) || (flag))
/*     */     {
/* 370 */       int j = this.guiLeft;
/* 371 */       int k = this.guiTop;
/* 372 */       boolean flag1 = (mouseX < j) || (mouseY < k) || (mouseX >= j + this.xSize) || (mouseY >= k + this.ySize);
/* 373 */       int l = -1;
/*     */       
/* 375 */       if (slot != null)
/*     */       {
/* 377 */         l = slot.slotNumber;
/*     */       }
/*     */       
/* 380 */       if (flag1)
/*     */       {
/* 382 */         l = 64537;
/*     */       }
/*     */       
/* 385 */       if ((this.mc.gameSettings.touchscreen) && (flag1) && (this.mc.thePlayer.inventory.getItemStack() == null))
/*     */       {
/* 387 */         this.mc.displayGuiScreen(null);
/* 388 */         return;
/*     */       }
/*     */       
/* 391 */       if (l != -1)
/*     */       {
/* 393 */         if (this.mc.gameSettings.touchscreen)
/*     */         {
/* 395 */           if ((slot != null) && (slot.getHasStack()))
/*     */           {
/* 397 */             this.clickedSlot = slot;
/* 398 */             this.draggedStack = null;
/* 399 */             this.isRightMouseClick = (mouseButton == 1);
/*     */           }
/*     */           else
/*     */           {
/* 403 */             this.clickedSlot = null;
/*     */           }
/*     */         }
/* 406 */         else if (!this.dragSplitting)
/*     */         {
/* 408 */           if (this.mc.thePlayer.inventory.getItemStack() == null)
/*     */           {
/* 410 */             if (mouseButton == this.mc.gameSettings.keyBindPickBlock.getKeyCode() + 100)
/*     */             {
/* 412 */               handleMouseClick(slot, l, mouseButton, 3);
/*     */             }
/*     */             else
/*     */             {
/* 416 */               boolean flag2 = (l != 64537) && ((Keyboard.isKeyDown(42)) || (Keyboard.isKeyDown(54)));
/* 417 */               int i1 = 0;
/*     */               
/* 419 */               if (flag2)
/*     */               {
/* 421 */                 this.shiftClickedSlot = ((slot != null) && (slot.getHasStack()) ? slot.getStack() : null);
/* 422 */                 i1 = 1;
/*     */               }
/* 424 */               else if (l == 64537)
/*     */               {
/* 426 */                 i1 = 4;
/*     */               }
/*     */               
/* 429 */               handleMouseClick(slot, l, mouseButton, i1);
/*     */             }
/*     */             
/* 432 */             this.ignoreMouseUp = true;
/*     */           }
/*     */           else
/*     */           {
/* 436 */             this.dragSplitting = true;
/* 437 */             this.dragSplittingButton = mouseButton;
/* 438 */             this.dragSplittingSlots.clear();
/*     */             
/* 440 */             if (mouseButton == 0)
/*     */             {
/* 442 */               this.dragSplittingLimit = 0;
/*     */             }
/* 444 */             else if (mouseButton == 1)
/*     */             {
/* 446 */               this.dragSplittingLimit = 1;
/*     */             }
/* 448 */             else if (mouseButton == this.mc.gameSettings.keyBindPickBlock.getKeyCode() + 100)
/*     */             {
/* 450 */               this.dragSplittingLimit = 2;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 457 */     this.lastClickSlot = slot;
/* 458 */     this.lastClickTime = i;
/* 459 */     this.lastClickButton = mouseButton;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick)
/*     */   {
/* 468 */     Slot slot = getSlotAtPosition(mouseX, mouseY);
/* 469 */     ItemStack itemstack = this.mc.thePlayer.inventory.getItemStack();
/*     */     
/* 471 */     if ((this.clickedSlot != null) && (this.mc.gameSettings.touchscreen))
/*     */     {
/* 473 */       if ((clickedMouseButton == 0) || (clickedMouseButton == 1))
/*     */       {
/* 475 */         if (this.draggedStack == null)
/*     */         {
/* 477 */           if ((slot != this.clickedSlot) && (this.clickedSlot.getStack() != null))
/*     */           {
/* 479 */             this.draggedStack = this.clickedSlot.getStack().copy();
/*     */           }
/*     */         }
/* 482 */         else if ((this.draggedStack.stackSize > 1) && (slot != null) && (Container.canAddItemToSlot(slot, this.draggedStack, false)))
/*     */         {
/* 484 */           long i = Minecraft.getSystemTime();
/*     */           
/* 486 */           if (this.currentDragTargetSlot == slot)
/*     */           {
/* 488 */             if (i - this.dragItemDropDelay > 500L)
/*     */             {
/* 490 */               handleMouseClick(this.clickedSlot, this.clickedSlot.slotNumber, 0, 0);
/* 491 */               handleMouseClick(slot, slot.slotNumber, 1, 0);
/* 492 */               handleMouseClick(this.clickedSlot, this.clickedSlot.slotNumber, 0, 0);
/* 493 */               this.dragItemDropDelay = (i + 750L);
/* 494 */               this.draggedStack.stackSize -= 1;
/*     */             }
/*     */           }
/*     */           else
/*     */           {
/* 499 */             this.currentDragTargetSlot = slot;
/* 500 */             this.dragItemDropDelay = i;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 505 */     else if ((this.dragSplitting) && (slot != null) && (itemstack != null) && (itemstack.stackSize > this.dragSplittingSlots.size()) && (Container.canAddItemToSlot(slot, itemstack, true)) && (slot.isItemValid(itemstack)) && (this.inventorySlots.canDragIntoSlot(slot)))
/*     */     {
/* 507 */       this.dragSplittingSlots.add(slot);
/* 508 */       updateDragSplitting();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void mouseReleased(int mouseX, int mouseY, int state)
/*     */   {
/* 517 */     Slot slot = getSlotAtPosition(mouseX, mouseY);
/* 518 */     int i = this.guiLeft;
/* 519 */     int j = this.guiTop;
/* 520 */     boolean flag = (mouseX < i) || (mouseY < j) || (mouseX >= i + this.xSize) || (mouseY >= j + this.ySize);
/* 521 */     int k = -1;
/*     */     
/* 523 */     if (slot != null)
/*     */     {
/* 525 */       k = slot.slotNumber;
/*     */     }
/*     */     
/* 528 */     if (flag)
/*     */     {
/* 530 */       k = 64537;
/*     */     }
/*     */     
/* 533 */     if ((this.doubleClick) && (slot != null) && (state == 0) && (this.inventorySlots.canMergeSlot(null, slot)))
/*     */     {
/* 535 */       if (isShiftKeyDown())
/*     */       {
/* 537 */         if ((slot != null) && (slot.inventory != null) && (this.shiftClickedSlot != null))
/*     */         {
/* 539 */           for (Slot slot2 : this.inventorySlots.inventorySlots)
/*     */           {
/* 541 */             if ((slot2 != null) && (slot2.canTakeStack(this.mc.thePlayer)) && (slot2.getHasStack()) && (slot2.inventory == slot.inventory) && (Container.canAddItemToSlot(slot2, this.shiftClickedSlot, true)))
/*     */             {
/* 543 */               handleMouseClick(slot2, slot2.slotNumber, state, 1);
/*     */             }
/*     */             
/*     */           }
/*     */         }
/*     */       }
/*     */       else {
/* 550 */         handleMouseClick(slot, k, state, 6);
/*     */       }
/*     */       
/* 553 */       this.doubleClick = false;
/* 554 */       this.lastClickTime = 0L;
/*     */     }
/*     */     else
/*     */     {
/* 558 */       if ((this.dragSplitting) && (this.dragSplittingButton != state))
/*     */       {
/* 560 */         this.dragSplitting = false;
/* 561 */         this.dragSplittingSlots.clear();
/* 562 */         this.ignoreMouseUp = true;
/* 563 */         return;
/*     */       }
/*     */       
/* 566 */       if (this.ignoreMouseUp)
/*     */       {
/* 568 */         this.ignoreMouseUp = false;
/* 569 */         return;
/*     */       }
/*     */       
/* 572 */       if ((this.clickedSlot != null) && (this.mc.gameSettings.touchscreen))
/*     */       {
/* 574 */         if ((state == 0) || (state == 1))
/*     */         {
/* 576 */           if ((this.draggedStack == null) && (slot != this.clickedSlot))
/*     */           {
/* 578 */             this.draggedStack = this.clickedSlot.getStack();
/*     */           }
/*     */           
/* 581 */           boolean flag2 = Container.canAddItemToSlot(slot, this.draggedStack, false);
/*     */           
/* 583 */           if ((k != -1) && (this.draggedStack != null) && (flag2))
/*     */           {
/* 585 */             handleMouseClick(this.clickedSlot, this.clickedSlot.slotNumber, state, 0);
/* 586 */             handleMouseClick(slot, k, 0, 0);
/*     */             
/* 588 */             if (this.mc.thePlayer.inventory.getItemStack() != null)
/*     */             {
/* 590 */               handleMouseClick(this.clickedSlot, this.clickedSlot.slotNumber, state, 0);
/* 591 */               this.touchUpX = (mouseX - i);
/* 592 */               this.touchUpY = (mouseY - j);
/* 593 */               this.returningStackDestSlot = this.clickedSlot;
/* 594 */               this.returningStack = this.draggedStack;
/* 595 */               this.returningStackTime = Minecraft.getSystemTime();
/*     */             }
/*     */             else
/*     */             {
/* 599 */               this.returningStack = null;
/*     */             }
/*     */           }
/* 602 */           else if (this.draggedStack != null)
/*     */           {
/* 604 */             this.touchUpX = (mouseX - i);
/* 605 */             this.touchUpY = (mouseY - j);
/* 606 */             this.returningStackDestSlot = this.clickedSlot;
/* 607 */             this.returningStack = this.draggedStack;
/* 608 */             this.returningStackTime = Minecraft.getSystemTime();
/*     */           }
/*     */           
/* 611 */           this.draggedStack = null;
/* 612 */           this.clickedSlot = null;
/*     */         }
/*     */       }
/* 615 */       else if ((this.dragSplitting) && (!this.dragSplittingSlots.isEmpty()))
/*     */       {
/* 617 */         handleMouseClick(null, 64537, Container.func_94534_d(0, this.dragSplittingLimit), 5);
/*     */         
/* 619 */         for (Slot slot1 : this.dragSplittingSlots)
/*     */         {
/* 621 */           handleMouseClick(slot1, slot1.slotNumber, Container.func_94534_d(1, this.dragSplittingLimit), 5);
/*     */         }
/*     */         
/* 624 */         handleMouseClick(null, 64537, Container.func_94534_d(2, this.dragSplittingLimit), 5);
/*     */       }
/* 626 */       else if (this.mc.thePlayer.inventory.getItemStack() != null)
/*     */       {
/* 628 */         if (state == this.mc.gameSettings.keyBindPickBlock.getKeyCode() + 100)
/*     */         {
/* 630 */           handleMouseClick(slot, k, state, 3);
/*     */         }
/*     */         else
/*     */         {
/* 634 */           boolean flag1 = (k != 64537) && ((Keyboard.isKeyDown(42)) || (Keyboard.isKeyDown(54)));
/*     */           
/* 636 */           if (flag1)
/*     */           {
/* 638 */             this.shiftClickedSlot = ((slot != null) && (slot.getHasStack()) ? slot.getStack() : null);
/*     */           }
/*     */           
/* 641 */           handleMouseClick(slot, k, state, flag1 ? 1 : 0);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 646 */     if (this.mc.thePlayer.inventory.getItemStack() == null)
/*     */     {
/* 648 */       this.lastClickTime = 0L;
/*     */     }
/*     */     
/* 651 */     this.dragSplitting = false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean isMouseOverSlot(Slot slotIn, int mouseX, int mouseY)
/*     */   {
/* 659 */     return isPointInRegion(slotIn.xDisplayPosition, slotIn.yDisplayPosition, 16, 16, mouseX, mouseY);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean isPointInRegion(int left, int top, int right, int bottom, int pointX, int pointY)
/*     */   {
/* 668 */     int i = this.guiLeft;
/* 669 */     int j = this.guiTop;
/* 670 */     pointX -= i;
/* 671 */     pointY -= j;
/* 672 */     return (pointX >= left - 1) && (pointX < left + right + 1) && (pointY >= top - 1) && (pointY < top + bottom + 1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void handleMouseClick(Slot slotIn, int slotId, int clickedButton, int clickType)
/*     */   {
/* 680 */     if (slotIn != null)
/*     */     {
/* 682 */       slotId = slotIn.slotNumber;
/*     */     }
/*     */     
/* 685 */     this.mc.playerController.windowClick(this.inventorySlots.windowId, slotId, clickedButton, clickType, this.mc.thePlayer);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void keyTyped(char typedChar, int keyCode)
/*     */     throws java.io.IOException
/*     */   {
/* 694 */     if ((keyCode == 1) || (keyCode == this.mc.gameSettings.keyBindInventory.getKeyCode()))
/*     */     {
/* 696 */       this.mc.thePlayer.closeScreen();
/*     */     }
/*     */     
/* 699 */     checkHotbarKeys(keyCode);
/*     */     
/* 701 */     if ((this.theSlot != null) && (this.theSlot.getHasStack()))
/*     */     {
/* 703 */       if (keyCode == this.mc.gameSettings.keyBindPickBlock.getKeyCode())
/*     */       {
/* 705 */         handleMouseClick(this.theSlot, this.theSlot.slotNumber, 0, 3);
/*     */       }
/* 707 */       else if (keyCode == this.mc.gameSettings.keyBindDrop.getKeyCode())
/*     */       {
/* 709 */         handleMouseClick(this.theSlot, this.theSlot.slotNumber, isCtrlKeyDown() ? 1 : 0, 4);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean checkHotbarKeys(int keyCode)
/*     */   {
/* 720 */     if ((this.mc.thePlayer.inventory.getItemStack() == null) && (this.theSlot != null))
/*     */     {
/* 722 */       for (int i = 0; i < 9; i++)
/*     */       {
/* 724 */         if (keyCode == this.mc.gameSettings.keyBindsHotbar[i].getKeyCode())
/*     */         {
/* 726 */           handleMouseClick(this.theSlot, this.theSlot.slotNumber, i, 2);
/* 727 */           return true;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 732 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onGuiClosed()
/*     */   {
/* 740 */     if (this.mc.thePlayer != null)
/*     */     {
/* 742 */       this.inventorySlots.onContainerClosed(this.mc.thePlayer);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean doesGuiPauseGame()
/*     */   {
/* 751 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void updateScreen()
/*     */   {
/* 759 */     super.updateScreen();
/*     */     
/* 761 */     if ((!this.mc.thePlayer.isEntityAlive()) || (this.mc.thePlayer.isDead))
/*     */     {
/* 763 */       this.mc.thePlayer.closeScreen();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\gui\inventory\GuiContainer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */