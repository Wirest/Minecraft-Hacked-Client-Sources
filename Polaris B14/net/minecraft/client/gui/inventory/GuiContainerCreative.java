/*      */ package net.minecraft.client.gui.inventory;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import net.minecraft.client.Minecraft;
/*      */ import net.minecraft.client.entity.EntityPlayerSP;
/*      */ import net.minecraft.client.gui.FontRenderer;
/*      */ import net.minecraft.client.gui.GuiButton;
/*      */ import net.minecraft.client.gui.GuiTextField;
/*      */ import net.minecraft.client.multiplayer.PlayerControllerMP;
/*      */ import net.minecraft.client.renderer.GlStateManager;
/*      */ import net.minecraft.client.renderer.InventoryEffectRenderer;
/*      */ import net.minecraft.client.renderer.entity.RenderItem;
/*      */ import net.minecraft.client.renderer.texture.TextureManager;
/*      */ import net.minecraft.client.resources.I18n;
/*      */ import net.minecraft.client.settings.GameSettings;
/*      */ import net.minecraft.creativetab.CreativeTabs;
/*      */ import net.minecraft.enchantment.Enchantment;
/*      */ import net.minecraft.entity.player.EntityPlayer;
/*      */ import net.minecraft.entity.player.InventoryPlayer;
/*      */ import net.minecraft.init.Items;
/*      */ import net.minecraft.inventory.Container;
/*      */ import net.minecraft.inventory.IInventory;
/*      */ import net.minecraft.inventory.InventoryBasic;
/*      */ import net.minecraft.inventory.Slot;
/*      */ import net.minecraft.item.Item;
/*      */ import net.minecraft.item.ItemStack;
/*      */ import net.minecraft.util.EnumChatFormatting;
/*      */ import net.minecraft.util.MathHelper;
/*      */ import net.minecraft.util.ResourceLocation;
/*      */ import org.lwjgl.input.Keyboard;
/*      */ 
/*      */ public class GuiContainerCreative extends InventoryEffectRenderer
/*      */ {
/*   38 */   private static final ResourceLocation creativeInventoryTabs = new ResourceLocation("textures/gui/container/creative_inventory/tabs.png");
/*   39 */   private static InventoryBasic field_147060_v = new InventoryBasic("tmp", true, 45);
/*      */   
/*      */ 
/*   42 */   private static int selectedTabIndex = CreativeTabs.tabBlock.getTabIndex();
/*      */   
/*      */   private float currentScroll;
/*      */   
/*      */   private boolean isScrolling;
/*      */   
/*      */   private boolean wasClicking;
/*      */   
/*      */   private GuiTextField searchField;
/*      */   
/*      */   private List<Slot> field_147063_B;
/*      */   
/*      */   private Slot field_147064_C;
/*      */   
/*      */   private boolean field_147057_D;
/*      */   
/*      */   private CreativeCrafting field_147059_E;
/*      */   
/*      */   public GuiContainerCreative(EntityPlayer p_i1088_1_)
/*      */   {
/*   62 */     super(new ContainerCreative(p_i1088_1_));
/*   63 */     p_i1088_1_.openContainer = this.inventorySlots;
/*   64 */     this.allowUserInput = true;
/*   65 */     this.ySize = 136;
/*   66 */     this.xSize = 195;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void updateScreen()
/*      */   {
/*   74 */     if (!this.mc.playerController.isInCreativeMode())
/*      */     {
/*   76 */       this.mc.displayGuiScreen(new GuiInventory(this.mc.thePlayer));
/*      */     }
/*      */     
/*   79 */     updateActivePotionEffects();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void handleMouseClick(Slot slotIn, int slotId, int clickedButton, int clickType)
/*      */   {
/*   87 */     this.field_147057_D = true;
/*   88 */     boolean flag = clickType == 1;
/*   89 */     clickType = (slotId == 64537) && (clickType == 0) ? 4 : clickType;
/*      */     
/*   91 */     if ((slotIn == null) && (selectedTabIndex != CreativeTabs.tabInventory.getTabIndex()) && (clickType != 5))
/*      */     {
/*   93 */       InventoryPlayer inventoryplayer1 = this.mc.thePlayer.inventory;
/*      */       
/*   95 */       if (inventoryplayer1.getItemStack() != null)
/*      */       {
/*   97 */         if (clickedButton == 0)
/*      */         {
/*   99 */           this.mc.thePlayer.dropPlayerItemWithRandomChoice(inventoryplayer1.getItemStack(), true);
/*  100 */           this.mc.playerController.sendPacketDropItem(inventoryplayer1.getItemStack());
/*  101 */           inventoryplayer1.setItemStack(null);
/*      */         }
/*      */         
/*  104 */         if (clickedButton == 1)
/*      */         {
/*  106 */           ItemStack itemstack5 = inventoryplayer1.getItemStack().splitStack(1);
/*  107 */           this.mc.thePlayer.dropPlayerItemWithRandomChoice(itemstack5, true);
/*  108 */           this.mc.playerController.sendPacketDropItem(itemstack5);
/*      */           
/*  110 */           if (inventoryplayer1.getItemStack().stackSize == 0)
/*      */           {
/*  112 */             inventoryplayer1.setItemStack(null);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  117 */     else if ((slotIn == this.field_147064_C) && (flag))
/*      */     {
/*  119 */       for (int j = 0; j < this.mc.thePlayer.inventoryContainer.getInventory().size(); j++)
/*      */       {
/*  121 */         this.mc.playerController.sendSlotPacket(null, j);
/*      */       }
/*      */     }
/*  124 */     else if (selectedTabIndex == CreativeTabs.tabInventory.getTabIndex())
/*      */     {
/*  126 */       if (slotIn == this.field_147064_C)
/*      */       {
/*  128 */         this.mc.thePlayer.inventory.setItemStack(null);
/*      */       }
/*  130 */       else if ((clickType == 4) && (slotIn != null) && (slotIn.getHasStack()))
/*      */       {
/*  132 */         ItemStack itemstack = slotIn.decrStackSize(clickedButton == 0 ? 1 : slotIn.getStack().getMaxStackSize());
/*  133 */         this.mc.thePlayer.dropPlayerItemWithRandomChoice(itemstack, true);
/*  134 */         this.mc.playerController.sendPacketDropItem(itemstack);
/*      */       }
/*  136 */       else if ((clickType == 4) && (this.mc.thePlayer.inventory.getItemStack() != null))
/*      */       {
/*  138 */         this.mc.thePlayer.dropPlayerItemWithRandomChoice(this.mc.thePlayer.inventory.getItemStack(), true);
/*  139 */         this.mc.playerController.sendPacketDropItem(this.mc.thePlayer.inventory.getItemStack());
/*  140 */         this.mc.thePlayer.inventory.setItemStack(null);
/*      */       }
/*      */       else
/*      */       {
/*  144 */         this.mc.thePlayer.inventoryContainer.slotClick(slotIn == null ? slotId : ((CreativeSlot)slotIn).slot.slotNumber, clickedButton, clickType, this.mc.thePlayer);
/*  145 */         this.mc.thePlayer.inventoryContainer.detectAndSendChanges();
/*      */       }
/*      */     }
/*  148 */     else if ((clickType != 5) && (slotIn.inventory == field_147060_v))
/*      */     {
/*  150 */       InventoryPlayer inventoryplayer = this.mc.thePlayer.inventory;
/*  151 */       ItemStack itemstack1 = inventoryplayer.getItemStack();
/*  152 */       ItemStack itemstack2 = slotIn.getStack();
/*      */       
/*  154 */       if (clickType == 2)
/*      */       {
/*  156 */         if ((itemstack2 != null) && (clickedButton >= 0) && (clickedButton < 9))
/*      */         {
/*  158 */           ItemStack itemstack7 = itemstack2.copy();
/*  159 */           itemstack7.stackSize = itemstack7.getMaxStackSize();
/*  160 */           this.mc.thePlayer.inventory.setInventorySlotContents(clickedButton, itemstack7);
/*  161 */           this.mc.thePlayer.inventoryContainer.detectAndSendChanges();
/*      */         }
/*      */         
/*  164 */         return;
/*      */       }
/*      */       
/*  167 */       if (clickType == 3)
/*      */       {
/*  169 */         if ((inventoryplayer.getItemStack() == null) && (slotIn.getHasStack()))
/*      */         {
/*  171 */           ItemStack itemstack6 = slotIn.getStack().copy();
/*  172 */           itemstack6.stackSize = itemstack6.getMaxStackSize();
/*  173 */           inventoryplayer.setItemStack(itemstack6);
/*      */         }
/*      */         
/*  176 */         return;
/*      */       }
/*      */       
/*  179 */       if (clickType == 4)
/*      */       {
/*  181 */         if (itemstack2 != null)
/*      */         {
/*  183 */           ItemStack itemstack3 = itemstack2.copy();
/*  184 */           itemstack3.stackSize = (clickedButton == 0 ? 1 : itemstack3.getMaxStackSize());
/*  185 */           this.mc.thePlayer.dropPlayerItemWithRandomChoice(itemstack3, true);
/*  186 */           this.mc.playerController.sendPacketDropItem(itemstack3);
/*      */         }
/*      */         
/*  189 */         return;
/*      */       }
/*      */       
/*  192 */       if ((itemstack1 != null) && (itemstack2 != null) && (itemstack1.isItemEqual(itemstack2)))
/*      */       {
/*  194 */         if (clickedButton == 0)
/*      */         {
/*  196 */           if (flag)
/*      */           {
/*  198 */             itemstack1.stackSize = itemstack1.getMaxStackSize();
/*      */           }
/*  200 */           else if (itemstack1.stackSize < itemstack1.getMaxStackSize())
/*      */           {
/*  202 */             itemstack1.stackSize += 1;
/*      */           }
/*      */         }
/*  205 */         else if (itemstack1.stackSize <= 1)
/*      */         {
/*  207 */           inventoryplayer.setItemStack(null);
/*      */         }
/*      */         else
/*      */         {
/*  211 */           itemstack1.stackSize -= 1;
/*      */         }
/*      */       }
/*  214 */       else if ((itemstack2 != null) && (itemstack1 == null))
/*      */       {
/*  216 */         inventoryplayer.setItemStack(ItemStack.copyItemStack(itemstack2));
/*  217 */         itemstack1 = inventoryplayer.getItemStack();
/*      */         
/*  219 */         if (flag)
/*      */         {
/*  221 */           itemstack1.stackSize = itemstack1.getMaxStackSize();
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/*  226 */         inventoryplayer.setItemStack(null);
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/*  231 */       this.inventorySlots.slotClick(slotIn == null ? slotId : slotIn.slotNumber, clickedButton, clickType, this.mc.thePlayer);
/*      */       
/*  233 */       if (Container.getDragEvent(clickedButton) == 2)
/*      */       {
/*  235 */         for (int i = 0; i < 9; i++)
/*      */         {
/*  237 */           this.mc.playerController.sendSlotPacket(this.inventorySlots.getSlot(45 + i).getStack(), 36 + i);
/*      */         }
/*      */       }
/*  240 */       else if (slotIn != null)
/*      */       {
/*  242 */         ItemStack itemstack4 = this.inventorySlots.getSlot(slotIn.slotNumber).getStack();
/*  243 */         this.mc.playerController.sendSlotPacket(itemstack4, slotIn.slotNumber - this.inventorySlots.inventorySlots.size() + 9 + 36);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   protected void updateActivePotionEffects()
/*      */   {
/*  250 */     int i = this.guiLeft;
/*  251 */     super.updateActivePotionEffects();
/*      */     
/*  253 */     if ((this.searchField != null) && (this.guiLeft != i))
/*      */     {
/*  255 */       this.searchField.xPosition = (this.guiLeft + 82);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void initGui()
/*      */   {
/*  265 */     if (this.mc.playerController.isInCreativeMode())
/*      */     {
/*  267 */       super.initGui();
/*  268 */       this.buttonList.clear();
/*  269 */       Keyboard.enableRepeatEvents(true);
/*  270 */       this.searchField = new GuiTextField(0, this.fontRendererObj, this.guiLeft + 82, this.guiTop + 6, 89, this.fontRendererObj.FONT_HEIGHT);
/*  271 */       this.searchField.setMaxStringLength(15);
/*  272 */       this.searchField.setEnableBackgroundDrawing(false);
/*  273 */       this.searchField.setVisible(false);
/*  274 */       this.searchField.setTextColor(16777215);
/*  275 */       int i = selectedTabIndex;
/*  276 */       selectedTabIndex = -1;
/*  277 */       setCurrentCreativeTab(CreativeTabs.creativeTabArray[i]);
/*  278 */       this.field_147059_E = new CreativeCrafting(this.mc);
/*  279 */       this.mc.thePlayer.inventoryContainer.onCraftGuiOpened(this.field_147059_E);
/*      */     }
/*      */     else
/*      */     {
/*  283 */       this.mc.displayGuiScreen(new GuiInventory(this.mc.thePlayer));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void onGuiClosed()
/*      */   {
/*  292 */     super.onGuiClosed();
/*      */     
/*  294 */     if ((this.mc.thePlayer != null) && (this.mc.thePlayer.inventory != null))
/*      */     {
/*  296 */       this.mc.thePlayer.inventoryContainer.removeCraftingFromCrafters(this.field_147059_E);
/*      */     }
/*      */     
/*  299 */     Keyboard.enableRepeatEvents(false);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void keyTyped(char typedChar, int keyCode)
/*      */     throws IOException
/*      */   {
/*  308 */     if (selectedTabIndex != CreativeTabs.tabAllSearch.getTabIndex())
/*      */     {
/*  310 */       if (GameSettings.isKeyDown(this.mc.gameSettings.keyBindChat))
/*      */       {
/*  312 */         setCurrentCreativeTab(CreativeTabs.tabAllSearch);
/*      */       }
/*      */       else
/*      */       {
/*  316 */         super.keyTyped(typedChar, keyCode);
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/*  321 */       if (this.field_147057_D)
/*      */       {
/*  323 */         this.field_147057_D = false;
/*  324 */         this.searchField.setText("");
/*      */       }
/*      */       
/*  327 */       if (!checkHotbarKeys(keyCode))
/*      */       {
/*  329 */         if (this.searchField.textboxKeyTyped(typedChar, keyCode))
/*      */         {
/*  331 */           updateCreativeSearch();
/*      */         }
/*      */         else
/*      */         {
/*  335 */           super.keyTyped(typedChar, keyCode);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private void updateCreativeSearch()
/*      */   {
/*  343 */     ContainerCreative guicontainercreative$containercreative = (ContainerCreative)this.inventorySlots;
/*  344 */     guicontainercreative$containercreative.itemList.clear();
/*      */     
/*  346 */     for (Item item : Item.itemRegistry)
/*      */     {
/*  348 */       if ((item != null) && (item.getCreativeTab() != null))
/*      */       {
/*  350 */         item.getSubItems(item, null, guicontainercreative$containercreative.itemList);
/*      */       }
/*      */     }
/*      */     Enchantment[] arrayOfEnchantment;
/*  354 */     int j = (arrayOfEnchantment = Enchantment.enchantmentsBookList).length; for (int i = 0; i < j; i++) { Enchantment enchantment = arrayOfEnchantment[i];
/*      */       
/*  356 */       if ((enchantment != null) && (enchantment.type != null))
/*      */       {
/*  358 */         Items.enchanted_book.getAll(enchantment, guicontainercreative$containercreative.itemList);
/*      */       }
/*      */     }
/*      */     
/*  362 */     Iterator<ItemStack> iterator = guicontainercreative$containercreative.itemList.iterator();
/*  363 */     String s1 = this.searchField.getText().toLowerCase();
/*      */     
/*  365 */     while (iterator.hasNext())
/*      */     {
/*  367 */       ItemStack itemstack = (ItemStack)iterator.next();
/*  368 */       boolean flag = false;
/*      */       
/*  370 */       for (String s : itemstack.getTooltip(this.mc.thePlayer, this.mc.gameSettings.advancedItemTooltips))
/*      */       {
/*  372 */         if (EnumChatFormatting.getTextWithoutFormattingCodes(s).toLowerCase().contains(s1))
/*      */         {
/*  374 */           flag = true;
/*  375 */           break;
/*      */         }
/*      */       }
/*      */       
/*  379 */       if (!flag)
/*      */       {
/*  381 */         iterator.remove();
/*      */       }
/*      */     }
/*      */     
/*  385 */     this.currentScroll = 0.0F;
/*  386 */     guicontainercreative$containercreative.scrollTo(0.0F);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
/*      */   {
/*  394 */     CreativeTabs creativetabs = CreativeTabs.creativeTabArray[selectedTabIndex];
/*      */     
/*  396 */     if (creativetabs.drawInForegroundOfTab())
/*      */     {
/*  398 */       GlStateManager.disableBlend();
/*  399 */       this.fontRendererObj.drawString(I18n.format(creativetabs.getTranslatedTabLabel(), new Object[0]), 8.0D, 6.0D, 4210752);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected void mouseClicked(int mouseX, int mouseY, int mouseButton)
/*      */     throws IOException
/*      */   {
/*  408 */     if (mouseButton == 0)
/*      */     {
/*  410 */       int i = mouseX - this.guiLeft;
/*  411 */       int j = mouseY - this.guiTop;
/*      */       CreativeTabs[] arrayOfCreativeTabs;
/*  413 */       int j = (arrayOfCreativeTabs = CreativeTabs.creativeTabArray).length; for (int i = 0; i < j; i++) { CreativeTabs creativetabs = arrayOfCreativeTabs[i];
/*      */         
/*  415 */         if (func_147049_a(creativetabs, i, j))
/*      */         {
/*  417 */           return;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  422 */     super.mouseClicked(mouseX, mouseY, mouseButton);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void mouseReleased(int mouseX, int mouseY, int state)
/*      */   {
/*  430 */     if (state == 0)
/*      */     {
/*  432 */       int i = mouseX - this.guiLeft;
/*  433 */       int j = mouseY - this.guiTop;
/*      */       CreativeTabs[] arrayOfCreativeTabs;
/*  435 */       int j = (arrayOfCreativeTabs = CreativeTabs.creativeTabArray).length; for (int i = 0; i < j; i++) { CreativeTabs creativetabs = arrayOfCreativeTabs[i];
/*      */         
/*  437 */         if (func_147049_a(creativetabs, i, j))
/*      */         {
/*  439 */           setCurrentCreativeTab(creativetabs);
/*  440 */           return;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  445 */     super.mouseReleased(mouseX, mouseY, state);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private boolean needsScrollBars()
/*      */   {
/*  453 */     return (selectedTabIndex != CreativeTabs.tabInventory.getTabIndex()) && (CreativeTabs.creativeTabArray[selectedTabIndex].shouldHidePlayerInventory()) && (((ContainerCreative)this.inventorySlots).func_148328_e());
/*      */   }
/*      */   
/*      */   private void setCurrentCreativeTab(CreativeTabs p_147050_1_)
/*      */   {
/*  458 */     int i = selectedTabIndex;
/*  459 */     selectedTabIndex = p_147050_1_.getTabIndex();
/*  460 */     ContainerCreative guicontainercreative$containercreative = (ContainerCreative)this.inventorySlots;
/*  461 */     this.dragSplittingSlots.clear();
/*  462 */     guicontainercreative$containercreative.itemList.clear();
/*  463 */     p_147050_1_.displayAllReleventItems(guicontainercreative$containercreative.itemList);
/*      */     
/*  465 */     if (p_147050_1_ == CreativeTabs.tabInventory)
/*      */     {
/*  467 */       Container container = this.mc.thePlayer.inventoryContainer;
/*      */       
/*  469 */       if (this.field_147063_B == null)
/*      */       {
/*  471 */         this.field_147063_B = guicontainercreative$containercreative.inventorySlots;
/*      */       }
/*      */       
/*  474 */       guicontainercreative$containercreative.inventorySlots = com.google.common.collect.Lists.newArrayList();
/*      */       
/*  476 */       for (int j = 0; j < container.inventorySlots.size(); j++)
/*      */       {
/*  478 */         Slot slot = new CreativeSlot((Slot)container.inventorySlots.get(j), j);
/*  479 */         guicontainercreative$containercreative.inventorySlots.add(slot);
/*      */         
/*  481 */         if ((j >= 5) && (j < 9))
/*      */         {
/*  483 */           int j1 = j - 5;
/*  484 */           int k1 = j1 / 2;
/*  485 */           int l1 = j1 % 2;
/*  486 */           slot.xDisplayPosition = (9 + k1 * 54);
/*  487 */           slot.yDisplayPosition = (6 + l1 * 27);
/*      */         }
/*  489 */         else if ((j >= 0) && (j < 5))
/*      */         {
/*  491 */           slot.yDisplayPosition = 63536;
/*  492 */           slot.xDisplayPosition = 63536;
/*      */         }
/*  494 */         else if (j < container.inventorySlots.size())
/*      */         {
/*  496 */           int k = j - 9;
/*  497 */           int l = k % 9;
/*  498 */           int i1 = k / 9;
/*  499 */           slot.xDisplayPosition = (9 + l * 18);
/*      */           
/*  501 */           if (j >= 36)
/*      */           {
/*  503 */             slot.yDisplayPosition = 112;
/*      */           }
/*      */           else
/*      */           {
/*  507 */             slot.yDisplayPosition = (54 + i1 * 18);
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*  512 */       this.field_147064_C = new Slot(field_147060_v, 0, 173, 112);
/*  513 */       guicontainercreative$containercreative.inventorySlots.add(this.field_147064_C);
/*      */     }
/*  515 */     else if (i == CreativeTabs.tabInventory.getTabIndex())
/*      */     {
/*  517 */       guicontainercreative$containercreative.inventorySlots = this.field_147063_B;
/*  518 */       this.field_147063_B = null;
/*      */     }
/*      */     
/*  521 */     if (this.searchField != null)
/*      */     {
/*  523 */       if (p_147050_1_ == CreativeTabs.tabAllSearch)
/*      */       {
/*  525 */         this.searchField.setVisible(true);
/*  526 */         this.searchField.setCanLoseFocus(false);
/*  527 */         this.searchField.setFocused(true);
/*  528 */         this.searchField.setText("");
/*  529 */         updateCreativeSearch();
/*      */       }
/*      */       else
/*      */       {
/*  533 */         this.searchField.setVisible(false);
/*  534 */         this.searchField.setCanLoseFocus(true);
/*  535 */         this.searchField.setFocused(false);
/*      */       }
/*      */     }
/*      */     
/*  539 */     this.currentScroll = 0.0F;
/*  540 */     guicontainercreative$containercreative.scrollTo(0.0F);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void handleMouseInput()
/*      */     throws IOException
/*      */   {
/*  548 */     super.handleMouseInput();
/*  549 */     int i = org.lwjgl.input.Mouse.getEventDWheel();
/*      */     
/*  551 */     if ((i != 0) && (needsScrollBars()))
/*      */     {
/*  553 */       int j = ((ContainerCreative)this.inventorySlots).itemList.size() / 9 - 5;
/*      */       
/*  555 */       if (i > 0)
/*      */       {
/*  557 */         i = 1;
/*      */       }
/*      */       
/*  560 */       if (i < 0)
/*      */       {
/*  562 */         i = -1;
/*      */       }
/*      */       
/*  565 */       this.currentScroll = ((float)(this.currentScroll - i / j));
/*  566 */       this.currentScroll = MathHelper.clamp_float(this.currentScroll, 0.0F, 1.0F);
/*  567 */       ((ContainerCreative)this.inventorySlots).scrollTo(this.currentScroll);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void drawScreen(int mouseX, int mouseY, float partialTicks)
/*      */   {
/*  576 */     boolean flag = org.lwjgl.input.Mouse.isButtonDown(0);
/*  577 */     int i = this.guiLeft;
/*  578 */     int j = this.guiTop;
/*  579 */     int k = i + 175;
/*  580 */     int l = j + 18;
/*  581 */     int i1 = k + 14;
/*  582 */     int j1 = l + 112;
/*      */     
/*  584 */     if ((!this.wasClicking) && (flag) && (mouseX >= k) && (mouseY >= l) && (mouseX < i1) && (mouseY < j1))
/*      */     {
/*  586 */       this.isScrolling = needsScrollBars();
/*      */     }
/*      */     
/*  589 */     if (!flag)
/*      */     {
/*  591 */       this.isScrolling = false;
/*      */     }
/*      */     
/*  594 */     this.wasClicking = flag;
/*      */     
/*  596 */     if (this.isScrolling)
/*      */     {
/*  598 */       this.currentScroll = ((mouseY - l - 7.5F) / (j1 - l - 15.0F));
/*  599 */       this.currentScroll = MathHelper.clamp_float(this.currentScroll, 0.0F, 1.0F);
/*  600 */       ((ContainerCreative)this.inventorySlots).scrollTo(this.currentScroll);
/*      */     }
/*      */     
/*  603 */     super.drawScreen(mouseX, mouseY, partialTicks);
/*      */     CreativeTabs[] arrayOfCreativeTabs;
/*  605 */     int j = (arrayOfCreativeTabs = CreativeTabs.creativeTabArray).length; for (int i = 0; i < j; i++) { CreativeTabs creativetabs = arrayOfCreativeTabs[i];
/*      */       
/*  607 */       if (renderCreativeInventoryHoveringText(creativetabs, mouseX, mouseY)) {
/*      */         break;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  613 */     if ((this.field_147064_C != null) && (selectedTabIndex == CreativeTabs.tabInventory.getTabIndex()) && (isPointInRegion(this.field_147064_C.xDisplayPosition, this.field_147064_C.yDisplayPosition, 16, 16, mouseX, mouseY)))
/*      */     {
/*  615 */       drawCreativeTabHoveringText(I18n.format("inventory.binSlot", new Object[0]), mouseX, mouseY);
/*      */     }
/*      */     
/*  618 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/*  619 */     GlStateManager.disableLighting();
/*      */   }
/*      */   
/*      */   protected void renderToolTip(ItemStack stack, int x, int y)
/*      */   {
/*  624 */     if (selectedTabIndex == CreativeTabs.tabAllSearch.getTabIndex())
/*      */     {
/*  626 */       List<String> list = stack.getTooltip(this.mc.thePlayer, this.mc.gameSettings.advancedItemTooltips);
/*  627 */       CreativeTabs creativetabs = stack.getItem().getCreativeTab();
/*      */       
/*  629 */       if ((creativetabs == null) && (stack.getItem() == Items.enchanted_book))
/*      */       {
/*  631 */         Map<Integer, Integer> map = net.minecraft.enchantment.EnchantmentHelper.getEnchantments(stack);
/*      */         
/*  633 */         if (map.size() == 1)
/*      */         {
/*  635 */           Enchantment enchantment = Enchantment.getEnchantmentById(((Integer)map.keySet().iterator().next()).intValue());
/*      */           CreativeTabs[] arrayOfCreativeTabs;
/*  637 */           int j = (arrayOfCreativeTabs = CreativeTabs.creativeTabArray).length; for (int i = 0; i < j; i++) { CreativeTabs creativetabs1 = arrayOfCreativeTabs[i];
/*      */             
/*  639 */             if (creativetabs1.hasRelevantEnchantmentType(enchantment.type))
/*      */             {
/*  641 */               creativetabs = creativetabs1;
/*  642 */               break;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*  648 */       if (creativetabs != null)
/*      */       {
/*  650 */         list.add(1, EnumChatFormatting.BOLD + EnumChatFormatting.BLUE + I18n.format(creativetabs.getTranslatedTabLabel(), new Object[0]));
/*      */       }
/*      */       
/*  653 */       for (int i = 0; i < list.size(); i++)
/*      */       {
/*  655 */         if (i == 0)
/*      */         {
/*  657 */           list.set(i, stack.getRarity().rarityColor + (String)list.get(i));
/*      */         }
/*      */         else
/*      */         {
/*  661 */           list.set(i, EnumChatFormatting.GRAY + (String)list.get(i));
/*      */         }
/*      */       }
/*      */       
/*  665 */       drawHoveringText(list, x, y);
/*      */     }
/*      */     else
/*      */     {
/*  669 */       super.renderToolTip(stack, x, y);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
/*      */   {
/*  678 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/*  679 */     net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();
/*  680 */     CreativeTabs creativetabs = CreativeTabs.creativeTabArray[selectedTabIndex];
/*      */     CreativeTabs[] arrayOfCreativeTabs;
/*  682 */     int j = (arrayOfCreativeTabs = CreativeTabs.creativeTabArray).length; for (int i = 0; i < j; i++) { CreativeTabs creativetabs1 = arrayOfCreativeTabs[i];
/*      */       
/*  684 */       this.mc.getTextureManager().bindTexture(creativeInventoryTabs);
/*      */       
/*  686 */       if (creativetabs1.getTabIndex() != selectedTabIndex)
/*      */       {
/*  688 */         func_147051_a(creativetabs1);
/*      */       }
/*      */     }
/*      */     
/*  692 */     this.mc.getTextureManager().bindTexture(new ResourceLocation("textures/gui/container/creative_inventory/tab_" + creativetabs.getBackgroundImageName()));
/*  693 */     drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
/*  694 */     this.searchField.drawTextBox();
/*  695 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/*  696 */     int i = this.guiLeft + 175;
/*  697 */     int j = this.guiTop + 18;
/*  698 */     int k = j + 112;
/*  699 */     this.mc.getTextureManager().bindTexture(creativeInventoryTabs);
/*      */     
/*  701 */     if (creativetabs.shouldHidePlayerInventory())
/*      */     {
/*  703 */       drawTexturedModalRect(i, j + (int)((k - j - 17) * this.currentScroll), 'è' + (needsScrollBars() ? 0 : 12), 0, 12, 15);
/*      */     }
/*      */     
/*  706 */     func_147051_a(creativetabs);
/*      */     
/*  708 */     if (creativetabs == CreativeTabs.tabInventory)
/*      */     {
/*  710 */       GuiInventory.drawEntityOnScreen(this.guiLeft + 43, this.guiTop + 45, 20, this.guiLeft + 43 - mouseX, this.guiTop + 45 - 30 - mouseY, this.mc.thePlayer);
/*      */     }
/*      */   }
/*      */   
/*      */   protected boolean func_147049_a(CreativeTabs p_147049_1_, int p_147049_2_, int p_147049_3_)
/*      */   {
/*  716 */     int i = p_147049_1_.getTabColumn();
/*  717 */     int j = 28 * i;
/*  718 */     int k = 0;
/*      */     
/*  720 */     if (i == 5)
/*      */     {
/*  722 */       j = this.xSize - 28 + 2;
/*      */     }
/*  724 */     else if (i > 0)
/*      */     {
/*  726 */       j += i;
/*      */     }
/*      */     
/*  729 */     if (p_147049_1_.isTabInFirstRow())
/*      */     {
/*  731 */       k -= 32;
/*      */     }
/*      */     else
/*      */     {
/*  735 */       k += this.ySize;
/*      */     }
/*      */     
/*  738 */     return (p_147049_2_ >= j) && (p_147049_2_ <= j + 28) && (p_147049_3_ >= k) && (p_147049_3_ <= k + 32);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean renderCreativeInventoryHoveringText(CreativeTabs p_147052_1_, int p_147052_2_, int p_147052_3_)
/*      */   {
/*  747 */     int i = p_147052_1_.getTabColumn();
/*  748 */     int j = 28 * i;
/*  749 */     int k = 0;
/*      */     
/*  751 */     if (i == 5)
/*      */     {
/*  753 */       j = this.xSize - 28 + 2;
/*      */     }
/*  755 */     else if (i > 0)
/*      */     {
/*  757 */       j += i;
/*      */     }
/*      */     
/*  760 */     if (p_147052_1_.isTabInFirstRow())
/*      */     {
/*  762 */       k -= 32;
/*      */     }
/*      */     else
/*      */     {
/*  766 */       k += this.ySize;
/*      */     }
/*      */     
/*  769 */     if (isPointInRegion(j + 3, k + 3, 23, 27, p_147052_2_, p_147052_3_))
/*      */     {
/*  771 */       drawCreativeTabHoveringText(I18n.format(p_147052_1_.getTranslatedTabLabel(), new Object[0]), p_147052_2_, p_147052_3_);
/*  772 */       return true;
/*      */     }
/*      */     
/*      */ 
/*  776 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */   protected void func_147051_a(CreativeTabs p_147051_1_)
/*      */   {
/*  782 */     boolean flag = p_147051_1_.getTabIndex() == selectedTabIndex;
/*  783 */     boolean flag1 = p_147051_1_.isTabInFirstRow();
/*  784 */     int i = p_147051_1_.getTabColumn();
/*  785 */     int j = i * 28;
/*  786 */     int k = 0;
/*  787 */     int l = this.guiLeft + 28 * i;
/*  788 */     int i1 = this.guiTop;
/*  789 */     int j1 = 32;
/*      */     
/*  791 */     if (flag)
/*      */     {
/*  793 */       k += 32;
/*      */     }
/*      */     
/*  796 */     if (i == 5)
/*      */     {
/*  798 */       l = this.guiLeft + this.xSize - 28;
/*      */     }
/*  800 */     else if (i > 0)
/*      */     {
/*  802 */       l += i;
/*      */     }
/*      */     
/*  805 */     if (flag1)
/*      */     {
/*  807 */       i1 -= 28;
/*      */     }
/*      */     else
/*      */     {
/*  811 */       k += 64;
/*  812 */       i1 += this.ySize - 4;
/*      */     }
/*      */     
/*  815 */     GlStateManager.disableLighting();
/*  816 */     drawTexturedModalRect(l, i1, j, k, 28, j1);
/*  817 */     this.zLevel = 100.0F;
/*  818 */     this.itemRender.zLevel = 100.0F;
/*  819 */     l += 6;
/*  820 */     i1 = i1 + 8 + (flag1 ? 1 : -1);
/*  821 */     GlStateManager.enableLighting();
/*  822 */     GlStateManager.enableRescaleNormal();
/*  823 */     ItemStack itemstack = p_147051_1_.getIconItemStack();
/*  824 */     this.itemRender.renderItemAndEffectIntoGUI(itemstack, l, i1);
/*  825 */     this.itemRender.renderItemOverlays(this.fontRendererObj, itemstack, l, i1);
/*  826 */     GlStateManager.disableLighting();
/*  827 */     this.itemRender.zLevel = 0.0F;
/*  828 */     this.zLevel = 0.0F;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected void actionPerformed(GuiButton button)
/*      */     throws IOException
/*      */   {
/*  836 */     if (button.id == 0)
/*      */     {
/*  838 */       this.mc.displayGuiScreen(new net.minecraft.client.gui.achievement.GuiAchievements(this, this.mc.thePlayer.getStatFileWriter()));
/*      */     }
/*      */     
/*  841 */     if (button.id == 1)
/*      */     {
/*  843 */       this.mc.displayGuiScreen(new net.minecraft.client.gui.achievement.GuiStats(this, this.mc.thePlayer.getStatFileWriter()));
/*      */     }
/*      */   }
/*      */   
/*      */   public int getSelectedTabIndex()
/*      */   {
/*  849 */     return selectedTabIndex;
/*      */   }
/*      */   
/*      */   static class ContainerCreative extends Container
/*      */   {
/*  854 */     public List<ItemStack> itemList = com.google.common.collect.Lists.newArrayList();
/*      */     
/*      */     public ContainerCreative(EntityPlayer p_i1086_1_)
/*      */     {
/*  858 */       InventoryPlayer inventoryplayer = p_i1086_1_.inventory;
/*      */       
/*  860 */       for (int i = 0; i < 5; i++)
/*      */       {
/*  862 */         for (int j = 0; j < 9; j++)
/*      */         {
/*  864 */           addSlotToContainer(new Slot(GuiContainerCreative.field_147060_v, i * 9 + j, 9 + j * 18, 18 + i * 18));
/*      */         }
/*      */       }
/*      */       
/*  868 */       for (int k = 0; k < 9; k++)
/*      */       {
/*  870 */         addSlotToContainer(new Slot(inventoryplayer, k, 9 + k * 18, 112));
/*      */       }
/*      */       
/*  873 */       scrollTo(0.0F);
/*      */     }
/*      */     
/*      */     public boolean canInteractWith(EntityPlayer playerIn)
/*      */     {
/*  878 */       return true;
/*      */     }
/*      */     
/*      */     public void scrollTo(float p_148329_1_)
/*      */     {
/*  883 */       int i = (this.itemList.size() + 9 - 1) / 9 - 5;
/*  884 */       int j = (int)(p_148329_1_ * i + 0.5D);
/*      */       
/*  886 */       if (j < 0)
/*      */       {
/*  888 */         j = 0;
/*      */       }
/*      */       
/*  891 */       for (int k = 0; k < 5; k++)
/*      */       {
/*  893 */         for (int l = 0; l < 9; l++)
/*      */         {
/*  895 */           int i1 = l + (k + j) * 9;
/*      */           
/*  897 */           if ((i1 >= 0) && (i1 < this.itemList.size()))
/*      */           {
/*  899 */             GuiContainerCreative.field_147060_v.setInventorySlotContents(l + k * 9, (ItemStack)this.itemList.get(i1));
/*      */           }
/*      */           else
/*      */           {
/*  903 */             GuiContainerCreative.field_147060_v.setInventorySlotContents(l + k * 9, null);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */     public boolean func_148328_e()
/*      */     {
/*  911 */       return this.itemList.size() > 45;
/*      */     }
/*      */     
/*      */ 
/*      */     protected void retrySlotClick(int slotId, int clickedButton, boolean mode, EntityPlayer playerIn) {}
/*      */     
/*      */ 
/*      */     public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
/*      */     {
/*  920 */       if ((index >= this.inventorySlots.size() - 9) && (index < this.inventorySlots.size()))
/*      */       {
/*  922 */         Slot slot = (Slot)this.inventorySlots.get(index);
/*      */         
/*  924 */         if ((slot != null) && (slot.getHasStack()))
/*      */         {
/*  926 */           slot.putStack(null);
/*      */         }
/*      */       }
/*      */       
/*  930 */       return null;
/*      */     }
/*      */     
/*      */     public boolean canMergeSlot(ItemStack stack, Slot p_94530_2_)
/*      */     {
/*  935 */       return p_94530_2_.yDisplayPosition > 90;
/*      */     }
/*      */     
/*      */     public boolean canDragIntoSlot(Slot p_94531_1_)
/*      */     {
/*  940 */       return ((p_94531_1_.inventory instanceof InventoryPlayer)) || ((p_94531_1_.yDisplayPosition > 90) && (p_94531_1_.xDisplayPosition <= 162));
/*      */     }
/*      */   }
/*      */   
/*      */   class CreativeSlot extends Slot
/*      */   {
/*      */     private final Slot slot;
/*      */     
/*      */     public CreativeSlot(Slot p_i46313_2_, int p_i46313_3_)
/*      */     {
/*  950 */       super(p_i46313_3_, 0, 0);
/*  951 */       this.slot = p_i46313_2_;
/*      */     }
/*      */     
/*      */     public void onPickupFromSlot(EntityPlayer playerIn, ItemStack stack)
/*      */     {
/*  956 */       this.slot.onPickupFromSlot(playerIn, stack);
/*      */     }
/*      */     
/*      */     public boolean isItemValid(ItemStack stack)
/*      */     {
/*  961 */       return this.slot.isItemValid(stack);
/*      */     }
/*      */     
/*      */     public ItemStack getStack()
/*      */     {
/*  966 */       return this.slot.getStack();
/*      */     }
/*      */     
/*      */     public boolean getHasStack()
/*      */     {
/*  971 */       return this.slot.getHasStack();
/*      */     }
/*      */     
/*      */     public void putStack(ItemStack stack)
/*      */     {
/*  976 */       this.slot.putStack(stack);
/*      */     }
/*      */     
/*      */     public void onSlotChanged()
/*      */     {
/*  981 */       this.slot.onSlotChanged();
/*      */     }
/*      */     
/*      */     public int getSlotStackLimit()
/*      */     {
/*  986 */       return this.slot.getSlotStackLimit();
/*      */     }
/*      */     
/*      */     public int getItemStackLimit(ItemStack stack)
/*      */     {
/*  991 */       return this.slot.getItemStackLimit(stack);
/*      */     }
/*      */     
/*      */     public String getSlotTexture()
/*      */     {
/*  996 */       return this.slot.getSlotTexture();
/*      */     }
/*      */     
/*      */     public ItemStack decrStackSize(int amount)
/*      */     {
/* 1001 */       return this.slot.decrStackSize(amount);
/*      */     }
/*      */     
/*      */     public boolean isHere(IInventory inv, int slotIn)
/*      */     {
/* 1006 */       return this.slot.isHere(inv, slotIn);
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\gui\inventory\GuiContainerCreative.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */