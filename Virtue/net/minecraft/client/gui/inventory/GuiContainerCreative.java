package net.minecraft.client.gui.inventory;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.achievement.GuiAchievements;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class GuiContainerCreative extends InventoryEffectRenderer
{
    /** The location of the creative inventory tabs texture */
    private static final ResourceLocation creativeInventoryTabs = new ResourceLocation("textures/gui/container/creative_inventory/tabs.png");
    private static InventoryBasic field_147060_v = new InventoryBasic("tmp", true, 45);

    /** Currently selected creative inventory tab index. */
    private static int selectedTabIndex = CreativeTabs.tabBlock.getTabIndex();

    /** Amount scrolled in Creative mode inventory (0 = top, 1 = bottom) */
    private float currentScroll;

    /** True if the scrollbar is being dragged */
    private boolean isScrolling;

    /**
     * True if the left mouse button was held down last time drawScreen was called.
     */
    private boolean wasClicking;
    private GuiTextField searchField;
    private List field_147063_B;
    private Slot field_147064_C;
    private boolean field_147057_D;
    private CreativeCrafting field_147059_E;
    private static final String __OBFID = "CL_00000752";

    public GuiContainerCreative(EntityPlayer p_i1088_1_)
    {
        super(new GuiContainerCreative.ContainerCreative(p_i1088_1_));
        p_i1088_1_.openContainer = this.inventorySlots;
        this.allowUserInput = true;
        this.ySize = 136;
        this.xSize = 195;
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        if (!this.mc.playerController.isInCreativeMode())
        {
            this.mc.displayGuiScreen(new GuiInventory(this.mc.thePlayer));
        }

        this.func_175378_g();
    }

    /**
     * Called when the mouse is clicked over a slot or outside the gui.
     */
    protected void handleMouseClick(Slot slotIn, int slotId, int clickedButton, int clickType)
    {
        this.field_147057_D = true;
        boolean var5 = clickType == 1;
        clickType = slotId == -999 && clickType == 0 ? 4 : clickType;
        ItemStack var7;
        InventoryPlayer var11;

        if (slotIn == null && selectedTabIndex != CreativeTabs.tabInventory.getTabIndex() && clickType != 5)
        {
            var11 = this.mc.thePlayer.inventory;

            if (var11.getItemStack() != null)
            {
                if (clickedButton == 0)
                {
                    this.mc.thePlayer.dropPlayerItemWithRandomChoice(var11.getItemStack(), true);
                    this.mc.playerController.sendPacketDropItem(var11.getItemStack());
                    var11.setItemStack((ItemStack)null);
                }

                if (clickedButton == 1)
                {
                    var7 = var11.getItemStack().splitStack(1);
                    this.mc.thePlayer.dropPlayerItemWithRandomChoice(var7, true);
                    this.mc.playerController.sendPacketDropItem(var7);

                    if (var11.getItemStack().stackSize == 0)
                    {
                        var11.setItemStack((ItemStack)null);
                    }
                }
            }
        }
        else
        {
            int var10;

            if (slotIn == this.field_147064_C && var5)
            {
                for (var10 = 0; var10 < this.mc.thePlayer.inventoryContainer.getInventory().size(); ++var10)
                {
                    this.mc.playerController.sendSlotPacket((ItemStack)null, var10);
                }
            }
            else
            {
                ItemStack var6;

                if (selectedTabIndex == CreativeTabs.tabInventory.getTabIndex())
                {
                    if (slotIn == this.field_147064_C)
                    {
                        this.mc.thePlayer.inventory.setItemStack((ItemStack)null);
                    }
                    else if (clickType == 4 && slotIn != null && slotIn.getHasStack())
                    {
                        var6 = slotIn.decrStackSize(clickedButton == 0 ? 1 : slotIn.getStack().getMaxStackSize());
                        this.mc.thePlayer.dropPlayerItemWithRandomChoice(var6, true);
                        this.mc.playerController.sendPacketDropItem(var6);
                    }
                    else if (clickType == 4 && this.mc.thePlayer.inventory.getItemStack() != null)
                    {
                        this.mc.thePlayer.dropPlayerItemWithRandomChoice(this.mc.thePlayer.inventory.getItemStack(), true);
                        this.mc.playerController.sendPacketDropItem(this.mc.thePlayer.inventory.getItemStack());
                        this.mc.thePlayer.inventory.setItemStack((ItemStack)null);
                    }
                    else
                    {
                        this.mc.thePlayer.inventoryContainer.slotClick(slotIn == null ? slotId : ((GuiContainerCreative.CreativeSlot)slotIn).field_148332_b.slotNumber, clickedButton, clickType, this.mc.thePlayer);
                        this.mc.thePlayer.inventoryContainer.detectAndSendChanges();
                    }
                }
                else if (clickType != 5 && slotIn.inventory == field_147060_v)
                {
                    var11 = this.mc.thePlayer.inventory;
                    var7 = var11.getItemStack();
                    ItemStack var8 = slotIn.getStack();
                    ItemStack var9;

                    if (clickType == 2)
                    {
                        if (var8 != null && clickedButton >= 0 && clickedButton < 9)
                        {
                            var9 = var8.copy();
                            var9.stackSize = var9.getMaxStackSize();
                            this.mc.thePlayer.inventory.setInventorySlotContents(clickedButton, var9);
                            this.mc.thePlayer.inventoryContainer.detectAndSendChanges();
                        }

                        return;
                    }

                    if (clickType == 3)
                    {
                        if (var11.getItemStack() == null && slotIn.getHasStack())
                        {
                            var9 = slotIn.getStack().copy();
                            var9.stackSize = var9.getMaxStackSize();
                            var11.setItemStack(var9);
                        }

                        return;
                    }

                    if (clickType == 4)
                    {
                        if (var8 != null)
                        {
                            var9 = var8.copy();
                            var9.stackSize = clickedButton == 0 ? 1 : var9.getMaxStackSize();
                            this.mc.thePlayer.dropPlayerItemWithRandomChoice(var9, true);
                            this.mc.playerController.sendPacketDropItem(var9);
                        }

                        return;
                    }

                    if (var7 != null && var8 != null && var7.isItemEqual(var8))
                    {
                        if (clickedButton == 0)
                        {
                            if (var5)
                            {
                                var7.stackSize = var7.getMaxStackSize();
                            }
                            else if (var7.stackSize < var7.getMaxStackSize())
                            {
                                ++var7.stackSize;
                            }
                        }
                        else if (var7.stackSize <= 1)
                        {
                            var11.setItemStack((ItemStack)null);
                        }
                        else
                        {
                            --var7.stackSize;
                        }
                    }
                    else if (var8 != null && var7 == null)
                    {
                        var11.setItemStack(ItemStack.copyItemStack(var8));
                        var7 = var11.getItemStack();

                        if (var5)
                        {
                            var7.stackSize = var7.getMaxStackSize();
                        }
                    }
                    else
                    {
                        var11.setItemStack((ItemStack)null);
                    }
                }
                else
                {
                    this.inventorySlots.slotClick(slotIn == null ? slotId : slotIn.slotNumber, clickedButton, clickType, this.mc.thePlayer);

                    if (Container.getDragEvent(clickedButton) == 2)
                    {
                        for (var10 = 0; var10 < 9; ++var10)
                        {
                            this.mc.playerController.sendSlotPacket(this.inventorySlots.getSlot(45 + var10).getStack(), 36 + var10);
                        }
                    }
                    else if (slotIn != null)
                    {
                        var6 = this.inventorySlots.getSlot(slotIn.slotNumber).getStack();
                        this.mc.playerController.sendSlotPacket(var6, slotIn.slotNumber - this.inventorySlots.inventorySlots.size() + 9 + 36);
                    }
                }
            }
        }
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        if (this.mc.playerController.isInCreativeMode())
        {
            super.initGui();
            this.buttonList.clear();
            Keyboard.enableRepeatEvents(true);
            this.searchField = new GuiTextField(0, this.fontRendererObj, this.guiLeft + 82, this.guiTop + 6, 89, this.fontRendererObj.FONT_HEIGHT);
            this.searchField.setMaxStringLength(15);
            this.searchField.setEnableBackgroundDrawing(false);
            this.searchField.setVisible(false);
            this.searchField.setTextColor(16777215);
            int var1 = selectedTabIndex;
            selectedTabIndex = -1;
            this.setCurrentCreativeTab(CreativeTabs.creativeTabArray[var1]);
            this.field_147059_E = new CreativeCrafting(this.mc);
            this.mc.thePlayer.inventoryContainer.onCraftGuiOpened(this.field_147059_E);
        }
        else
        {
            this.mc.displayGuiScreen(new GuiInventory(this.mc.thePlayer));
        }
    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    public void onGuiClosed()
    {
        super.onGuiClosed();

        if (this.mc.thePlayer != null && this.mc.thePlayer.inventory != null)
        {
            this.mc.thePlayer.inventoryContainer.removeCraftingFromCrafters(this.field_147059_E);
        }

        Keyboard.enableRepeatEvents(false);
    }

    /**
     * Fired when a key is typed (except F11 who toggle full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     */
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        if (selectedTabIndex != CreativeTabs.tabAllSearch.getTabIndex())
        {
            if (GameSettings.isKeyDown(this.mc.gameSettings.keyBindChat))
            {
                this.setCurrentCreativeTab(CreativeTabs.tabAllSearch);
            }
            else
            {
                super.keyTyped(typedChar, keyCode);
            }
        }
        else
        {
            if (this.field_147057_D)
            {
                this.field_147057_D = false;
                this.searchField.setText("");
            }

            if (!this.checkHotbarKeys(keyCode))
            {
                if (this.searchField.textboxKeyTyped(typedChar, keyCode))
                {
                    this.updateCreativeSearch();
                }
                else
                {
                    super.keyTyped(typedChar, keyCode);
                }
            }
        }
    }

    private void updateCreativeSearch()
    {
        GuiContainerCreative.ContainerCreative var1 = (GuiContainerCreative.ContainerCreative)this.inventorySlots;
        var1.itemList.clear();
        Iterator var2 = Item.itemRegistry.iterator();

        while (var2.hasNext())
        {
            Item var3 = (Item)var2.next();

            if (var3 != null && var3.getCreativeTab() != null)
            {
                var3.getSubItems(var3, (CreativeTabs)null, var1.itemList);
            }
        }

        Enchantment[] var8 = Enchantment.enchantmentsList;
        int var9 = var8.length;

        for (int var4 = 0; var4 < var9; ++var4)
        {
            Enchantment var5 = var8[var4];

            if (var5 != null && var5.type != null)
            {
                Items.enchanted_book.func_92113_a(var5, var1.itemList);
            }
        }

        var2 = var1.itemList.iterator();
        String var10 = this.searchField.getText().toLowerCase();

        while (var2.hasNext())
        {
            ItemStack var11 = (ItemStack)var2.next();
            boolean var12 = false;
            Iterator var6 = var11.getTooltip(this.mc.thePlayer, this.mc.gameSettings.advancedItemTooltips).iterator();

            while (true)
            {
                if (var6.hasNext())
                {
                    String var7 = (String)var6.next();

                    if (!EnumChatFormatting.getTextWithoutFormattingCodes(var7).toLowerCase().contains(var10))
                    {
                        continue;
                    }

                    var12 = true;
                }

                if (!var12)
                {
                    var2.remove();
                }

                break;
            }
        }

        this.currentScroll = 0.0F;
        var1.scrollTo(0.0F);
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items). Args : mouseX, mouseY
     */
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        CreativeTabs var3 = CreativeTabs.creativeTabArray[selectedTabIndex];

        if (var3.drawInForegroundOfTab())
        {
            GlStateManager.disableBlend();
            this.fontRendererObj.drawString(I18n.format(var3.getTranslatedTabLabel(), new Object[0]), 8, 6, 4210752);
        }
    }

    /**
     * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
     */
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        if (mouseButton == 0)
        {
            int var4 = mouseX - this.guiLeft;
            int var5 = mouseY - this.guiTop;
            CreativeTabs[] var6 = CreativeTabs.creativeTabArray;
            int var7 = var6.length;

            for (int var8 = 0; var8 < var7; ++var8)
            {
                CreativeTabs var9 = var6[var8];

                if (this.func_147049_a(var9, var4, var5))
                {
                    return;
                }
            }
        }

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    /**
     * Called when a mouse button is released.  Args : mouseX, mouseY, releaseButton
     */
    protected void mouseReleased(int mouseX, int mouseY, int state)
    {
        if (state == 0)
        {
            int var4 = mouseX - this.guiLeft;
            int var5 = mouseY - this.guiTop;
            CreativeTabs[] var6 = CreativeTabs.creativeTabArray;
            int var7 = var6.length;

            for (int var8 = 0; var8 < var7; ++var8)
            {
                CreativeTabs var9 = var6[var8];

                if (this.func_147049_a(var9, var4, var5))
                {
                    this.setCurrentCreativeTab(var9);
                    return;
                }
            }
        }

        super.mouseReleased(mouseX, mouseY, state);
    }

    /**
     * returns (if you are not on the inventoryTab) and (the flag isn't set) and (you have more than 1 page of items)
     */
    private boolean needsScrollBars()
    {
        return selectedTabIndex != CreativeTabs.tabInventory.getTabIndex() && CreativeTabs.creativeTabArray[selectedTabIndex].shouldHidePlayerInventory() && ((GuiContainerCreative.ContainerCreative)this.inventorySlots).func_148328_e();
    }

    private void setCurrentCreativeTab(CreativeTabs p_147050_1_)
    {
        int var2 = selectedTabIndex;
        selectedTabIndex = p_147050_1_.getTabIndex();
        GuiContainerCreative.ContainerCreative var3 = (GuiContainerCreative.ContainerCreative)this.inventorySlots;
        this.dragSplittingSlots.clear();
        var3.itemList.clear();
        p_147050_1_.displayAllReleventItems(var3.itemList);

        if (p_147050_1_ == CreativeTabs.tabInventory)
        {
            Container var4 = this.mc.thePlayer.inventoryContainer;

            if (this.field_147063_B == null)
            {
                this.field_147063_B = var3.inventorySlots;
            }

            var3.inventorySlots = Lists.newArrayList();

            for (int var5 = 0; var5 < var4.inventorySlots.size(); ++var5)
            {
                GuiContainerCreative.CreativeSlot var6 = new GuiContainerCreative.CreativeSlot((Slot)var4.inventorySlots.get(var5), var5);
                var3.inventorySlots.add(var6);
                int var7;
                int var8;
                int var9;

                if (var5 >= 5 && var5 < 9)
                {
                    var7 = var5 - 5;
                    var8 = var7 / 2;
                    var9 = var7 % 2;
                    var6.xDisplayPosition = 9 + var8 * 54;
                    var6.yDisplayPosition = 6 + var9 * 27;
                }
                else if (var5 >= 0 && var5 < 5)
                {
                    var6.yDisplayPosition = -2000;
                    var6.xDisplayPosition = -2000;
                }
                else if (var5 < var4.inventorySlots.size())
                {
                    var7 = var5 - 9;
                    var8 = var7 % 9;
                    var9 = var7 / 9;
                    var6.xDisplayPosition = 9 + var8 * 18;

                    if (var5 >= 36)
                    {
                        var6.yDisplayPosition = 112;
                    }
                    else
                    {
                        var6.yDisplayPosition = 54 + var9 * 18;
                    }
                }
            }

            this.field_147064_C = new Slot(field_147060_v, 0, 173, 112);
            var3.inventorySlots.add(this.field_147064_C);
        }
        else if (var2 == CreativeTabs.tabInventory.getTabIndex())
        {
            var3.inventorySlots = this.field_147063_B;
            this.field_147063_B = null;
        }

        if (this.searchField != null)
        {
            if (p_147050_1_ == CreativeTabs.tabAllSearch)
            {
                this.searchField.setVisible(true);
                this.searchField.setCanLoseFocus(false);
                this.searchField.setFocused(true);
                this.searchField.setText("");
                this.updateCreativeSearch();
            }
            else
            {
                this.searchField.setVisible(false);
                this.searchField.setCanLoseFocus(true);
                this.searchField.setFocused(false);
            }
        }

        this.currentScroll = 0.0F;
        var3.scrollTo(0.0F);
    }

    /**
     * Handles mouse input.
     */
    public void handleMouseInput() throws IOException
    {
        super.handleMouseInput();
        int var1 = Mouse.getEventDWheel();

        if (var1 != 0 && this.needsScrollBars())
        {
            int var2 = ((GuiContainerCreative.ContainerCreative)this.inventorySlots).itemList.size() / 9 - 5 + 1;

            if (var1 > 0)
            {
                var1 = 1;
            }

            if (var1 < 0)
            {
                var1 = -1;
            }

            this.currentScroll = (float)((double)this.currentScroll - (double)var1 / (double)var2);
            this.currentScroll = MathHelper.clamp_float(this.currentScroll, 0.0F, 1.0F);
            ((GuiContainerCreative.ContainerCreative)this.inventorySlots).scrollTo(this.currentScroll);
        }
    }

    /**
     * Draws the screen and all the components in it. Args : mouseX, mouseY, renderPartialTicks
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        boolean var4 = Mouse.isButtonDown(0);
        int var5 = this.guiLeft;
        int var6 = this.guiTop;
        int var7 = var5 + 175;
        int var8 = var6 + 18;
        int var9 = var7 + 14;
        int var10 = var8 + 112;

        if (!this.wasClicking && var4 && mouseX >= var7 && mouseY >= var8 && mouseX < var9 && mouseY < var10)
        {
            this.isScrolling = this.needsScrollBars();
        }

        if (!var4)
        {
            this.isScrolling = false;
        }

        this.wasClicking = var4;

        if (this.isScrolling)
        {
            this.currentScroll = ((float)(mouseY - var8) - 7.5F) / ((float)(var10 - var8) - 15.0F);
            this.currentScroll = MathHelper.clamp_float(this.currentScroll, 0.0F, 1.0F);
            ((GuiContainerCreative.ContainerCreative)this.inventorySlots).scrollTo(this.currentScroll);
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
        CreativeTabs[] var11 = CreativeTabs.creativeTabArray;
        int var12 = var11.length;

        for (int var13 = 0; var13 < var12; ++var13)
        {
            CreativeTabs var14 = var11[var13];

            if (this.renderCreativeInventoryHoveringText(var14, mouseX, mouseY))
            {
                break;
            }
        }

        if (this.field_147064_C != null && selectedTabIndex == CreativeTabs.tabInventory.getTabIndex() && this.isPointInRegion(this.field_147064_C.xDisplayPosition, this.field_147064_C.yDisplayPosition, 16, 16, mouseX, mouseY))
        {
            this.drawCreativeTabHoveringText(I18n.format("inventory.binSlot", new Object[0]), mouseX, mouseY);
        }

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableLighting();
    }

    protected void renderToolTip(ItemStack itemIn, int x, int y)
    {
        if (selectedTabIndex == CreativeTabs.tabAllSearch.getTabIndex())
        {
            List var4 = itemIn.getTooltip(this.mc.thePlayer, this.mc.gameSettings.advancedItemTooltips);
            CreativeTabs var5 = itemIn.getItem().getCreativeTab();

            if (var5 == null && itemIn.getItem() == Items.enchanted_book)
            {
                Map var6 = EnchantmentHelper.getEnchantments(itemIn);

                if (var6.size() == 1)
                {
                    Enchantment var7 = Enchantment.func_180306_c(((Integer)var6.keySet().iterator().next()).intValue());
                    CreativeTabs[] var8 = CreativeTabs.creativeTabArray;
                    int var9 = var8.length;

                    for (int var10 = 0; var10 < var9; ++var10)
                    {
                        CreativeTabs var11 = var8[var10];

                        if (var11.hasRelevantEnchantmentType(var7.type))
                        {
                            var5 = var11;
                            break;
                        }
                    }
                }
            }

            if (var5 != null)
            {
                var4.add(1, "" + EnumChatFormatting.BOLD + EnumChatFormatting.BLUE + I18n.format(var5.getTranslatedTabLabel(), new Object[0]));
            }

            for (int var12 = 0; var12 < var4.size(); ++var12)
            {
                if (var12 == 0)
                {
                    var4.set(var12, itemIn.getRarity().rarityColor + (String)var4.get(var12));
                }
                else
                {
                    var4.set(var12, EnumChatFormatting.GRAY + (String)var4.get(var12));
                }
            }

            this.drawHoveringText(var4, x, y);
        }
        else
        {
            super.renderToolTip(itemIn, x, y);
        }
    }

    /**
     * Args : renderPartialTicks, mouseX, mouseY
     */
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        RenderHelper.enableGUIStandardItemLighting();
        CreativeTabs var4 = CreativeTabs.creativeTabArray[selectedTabIndex];
        CreativeTabs[] var5 = CreativeTabs.creativeTabArray;
        int var6 = var5.length;
        int var7;

        for (var7 = 0; var7 < var6; ++var7)
        {
            CreativeTabs var8 = var5[var7];
            this.mc.getTextureManager().bindTexture(creativeInventoryTabs);

            if (var8.getTabIndex() != selectedTabIndex)
            {
                this.func_147051_a(var8);
            }
        }

        this.mc.getTextureManager().bindTexture(new ResourceLocation("textures/gui/container/creative_inventory/tab_" + var4.getBackgroundImageName()));
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        this.searchField.drawTextBox();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        int var9 = this.guiLeft + 175;
        var6 = this.guiTop + 18;
        var7 = var6 + 112;
        this.mc.getTextureManager().bindTexture(creativeInventoryTabs);

        if (var4.shouldHidePlayerInventory())
        {
            this.drawTexturedModalRect(var9, var6 + (int)((float)(var7 - var6 - 17) * this.currentScroll), 232 + (this.needsScrollBars() ? 0 : 12), 0, 12, 15);
        }

        this.func_147051_a(var4);

        if (var4 == CreativeTabs.tabInventory)
        {
            GuiInventory.drawEntityOnScreen(this.guiLeft + 43, this.guiTop + 45, 20, (float)(this.guiLeft + 43 - mouseX), (float)(this.guiTop + 45 - 30 - mouseY), this.mc.thePlayer);
        }
    }

    protected boolean func_147049_a(CreativeTabs p_147049_1_, int p_147049_2_, int p_147049_3_)
    {
        int var4 = p_147049_1_.getTabColumn();
        int var5 = 28 * var4;
        byte var6 = 0;

        if (var4 == 5)
        {
            var5 = this.xSize - 28 + 2;
        }
        else if (var4 > 0)
        {
            var5 += var4;
        }

        int var7;

        if (p_147049_1_.isTabInFirstRow())
        {
            var7 = var6 - 32;
        }
        else
        {
            var7 = var6 + this.ySize;
        }

        return p_147049_2_ >= var5 && p_147049_2_ <= var5 + 28 && p_147049_3_ >= var7 && p_147049_3_ <= var7 + 32;
    }

    /**
     * Renders the creative inventory hovering text if mouse is over it. Returns true if did render or false otherwise.
     * Params: current creative tab to be checked, current mouse x position, current mouse y position.
     */
    protected boolean renderCreativeInventoryHoveringText(CreativeTabs p_147052_1_, int p_147052_2_, int p_147052_3_)
    {
        int var4 = p_147052_1_.getTabColumn();
        int var5 = 28 * var4;
        byte var6 = 0;

        if (var4 == 5)
        {
            var5 = this.xSize - 28 + 2;
        }
        else if (var4 > 0)
        {
            var5 += var4;
        }

        int var7;

        if (p_147052_1_.isTabInFirstRow())
        {
            var7 = var6 - 32;
        }
        else
        {
            var7 = var6 + this.ySize;
        }

        if (this.isPointInRegion(var5 + 3, var7 + 3, 23, 27, p_147052_2_, p_147052_3_))
        {
            this.drawCreativeTabHoveringText(I18n.format(p_147052_1_.getTranslatedTabLabel(), new Object[0]), p_147052_2_, p_147052_3_);
            return true;
        }
        else
        {
            return false;
        }
    }

    protected void func_147051_a(CreativeTabs p_147051_1_)
    {
        boolean var2 = p_147051_1_.getTabIndex() == selectedTabIndex;
        boolean var3 = p_147051_1_.isTabInFirstRow();
        int var4 = p_147051_1_.getTabColumn();
        int var5 = var4 * 28;
        int var6 = 0;
        int var7 = this.guiLeft + 28 * var4;
        int var8 = this.guiTop;
        byte var9 = 32;

        if (var2)
        {
            var6 += 32;
        }

        if (var4 == 5)
        {
            var7 = this.guiLeft + this.xSize - 28;
        }
        else if (var4 > 0)
        {
            var7 += var4;
        }

        if (var3)
        {
            var8 -= 28;
        }
        else
        {
            var6 += 64;
            var8 += this.ySize - 4;
        }

        GlStateManager.disableLighting();
        this.drawTexturedModalRect(var7, var8, var5, var6, 28, var9);
        this.zLevel = 100.0F;
        this.itemRender.zLevel = 100.0F;
        var7 += 6;
        var8 += 8 + (var3 ? 1 : -1);
        GlStateManager.enableLighting();
        GlStateManager.enableRescaleNormal();
        ItemStack var10 = p_147051_1_.getIconItemStack();
        this.itemRender.func_180450_b(var10, var7, var8);
        this.itemRender.func_175030_a(this.fontRendererObj, var10, var7, var8);
        GlStateManager.disableLighting();
        this.itemRender.zLevel = 0.0F;
        this.zLevel = 0.0F;
    }

    protected void actionPerformed(GuiButton button) throws IOException
    {
        if (button.id == 0)
        {
            this.mc.displayGuiScreen(new GuiAchievements(this, this.mc.thePlayer.getStatFileWriter()));
        }

        if (button.id == 1)
        {
            this.mc.displayGuiScreen(new GuiStats(this, this.mc.thePlayer.getStatFileWriter()));
        }
    }

    public int func_147056_g()
    {
        return selectedTabIndex;
    }

    static class ContainerCreative extends Container
    {
        public List itemList = Lists.newArrayList();
        private static final String __OBFID = "CL_00000753";

        public ContainerCreative(EntityPlayer p_i1086_1_)
        {
            InventoryPlayer var2 = p_i1086_1_.inventory;
            int var3;

            for (var3 = 0; var3 < 5; ++var3)
            {
                for (int var4 = 0; var4 < 9; ++var4)
                {
                    this.addSlotToContainer(new Slot(GuiContainerCreative.field_147060_v, var3 * 9 + var4, 9 + var4 * 18, 18 + var3 * 18));
                }
            }

            for (var3 = 0; var3 < 9; ++var3)
            {
                this.addSlotToContainer(new Slot(var2, var3, 9 + var3 * 18, 112));
            }

            this.scrollTo(0.0F);
        }

        public boolean canInteractWith(EntityPlayer playerIn)
        {
            return true;
        }

        public void scrollTo(float p_148329_1_)
        {
            int var2 = (this.itemList.size() + 8) / 9 - 5;
            int var3 = (int)((double)(p_148329_1_ * (float)var2) + 0.5D);

            if (var3 < 0)
            {
                var3 = 0;
            }

            for (int var4 = 0; var4 < 5; ++var4)
            {
                for (int var5 = 0; var5 < 9; ++var5)
                {
                    int var6 = var5 + (var4 + var3) * 9;

                    if (var6 >= 0 && var6 < this.itemList.size())
                    {
                        GuiContainerCreative.field_147060_v.setInventorySlotContents(var5 + var4 * 9, (ItemStack)this.itemList.get(var6));
                    }
                    else
                    {
                        GuiContainerCreative.field_147060_v.setInventorySlotContents(var5 + var4 * 9, (ItemStack)null);
                    }
                }
            }
        }

        public boolean func_148328_e()
        {
            return this.itemList.size() > 45;
        }

        protected void retrySlotClick(int p_75133_1_, int p_75133_2_, boolean p_75133_3_, EntityPlayer p_75133_4_) {}

        public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
        {
            if (index >= this.inventorySlots.size() - 9 && index < this.inventorySlots.size())
            {
                Slot var3 = (Slot)this.inventorySlots.get(index);

                if (var3 != null && var3.getHasStack())
                {
                    var3.putStack((ItemStack)null);
                }
            }

            return null;
        }

        public boolean func_94530_a(ItemStack p_94530_1_, Slot p_94530_2_)
        {
            return p_94530_2_.yDisplayPosition > 90;
        }

        public boolean canDragIntoSlot(Slot p_94531_1_)
        {
            return p_94531_1_.inventory instanceof InventoryPlayer || p_94531_1_.yDisplayPosition > 90 && p_94531_1_.xDisplayPosition <= 162;
        }
    }

    class CreativeSlot extends Slot
    {
        private final Slot field_148332_b;
        private static final String __OBFID = "CL_00000754";

        public CreativeSlot(Slot p_i46313_2_, int p_i46313_3_)
        {
            super(p_i46313_2_.inventory, p_i46313_3_, 0, 0);
            this.field_148332_b = p_i46313_2_;
        }

        public void onPickupFromSlot(EntityPlayer playerIn, ItemStack stack)
        {
            this.field_148332_b.onPickupFromSlot(playerIn, stack);
        }

        public boolean isItemValid(ItemStack stack)
        {
            return this.field_148332_b.isItemValid(stack);
        }

        public ItemStack getStack()
        {
            return this.field_148332_b.getStack();
        }

        public boolean getHasStack()
        {
            return this.field_148332_b.getHasStack();
        }

        public void putStack(ItemStack p_75215_1_)
        {
            this.field_148332_b.putStack(p_75215_1_);
        }

        public void onSlotChanged()
        {
            this.field_148332_b.onSlotChanged();
        }

        public int getSlotStackLimit()
        {
            return this.field_148332_b.getSlotStackLimit();
        }

        public int func_178170_b(ItemStack p_178170_1_)
        {
            return this.field_148332_b.func_178170_b(p_178170_1_);
        }

        public String func_178171_c()
        {
            return this.field_148332_b.func_178171_c();
        }

        public ItemStack decrStackSize(int p_75209_1_)
        {
            return this.field_148332_b.decrStackSize(p_75209_1_);
        }

        public boolean isHere(IInventory p_75217_1_, int p_75217_2_)
        {
            return this.field_148332_b.isHere(p_75217_1_, p_75217_2_);
        }
    }
}
