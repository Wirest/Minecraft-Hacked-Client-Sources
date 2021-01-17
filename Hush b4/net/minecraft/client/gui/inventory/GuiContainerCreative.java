// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui.inventory;

import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.client.gui.achievement.GuiAchievements;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.RenderHelper;
import java.util.Map;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Mouse;
import net.minecraft.inventory.IInventory;
import com.google.common.collect.Lists;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.renderer.GlStateManager;
import java.util.Iterator;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.init.Items;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import java.io.IOException;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.inventory.ICrafting;
import org.lwjgl.input.Keyboard;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.Container;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.inventory.Slot;
import java.util.List;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.renderer.InventoryEffectRenderer;

public class GuiContainerCreative extends InventoryEffectRenderer
{
    private static final ResourceLocation creativeInventoryTabs;
    private static InventoryBasic field_147060_v;
    private static int selectedTabIndex;
    private float currentScroll;
    private boolean isScrolling;
    private boolean wasClicking;
    private GuiTextField searchField;
    private List<Slot> field_147063_B;
    private Slot field_147064_C;
    private boolean field_147057_D;
    private CreativeCrafting field_147059_E;
    
    static {
        creativeInventoryTabs = new ResourceLocation("textures/gui/container/creative_inventory/tabs.png");
        GuiContainerCreative.field_147060_v = new InventoryBasic("tmp", true, 45);
        GuiContainerCreative.selectedTabIndex = CreativeTabs.tabBlock.getTabIndex();
    }
    
    public GuiContainerCreative(final EntityPlayer p_i1088_1_) {
        super(new ContainerCreative(p_i1088_1_));
        p_i1088_1_.openContainer = this.inventorySlots;
        this.allowUserInput = true;
        this.ySize = 136;
        this.xSize = 195;
    }
    
    @Override
    public void updateScreen() {
        if (!this.mc.playerController.isInCreativeMode()) {
            this.mc.displayGuiScreen(new GuiInventory(Minecraft.thePlayer));
        }
        this.updateActivePotionEffects();
    }
    
    @Override
    protected void handleMouseClick(final Slot slotIn, final int slotId, final int clickedButton, int clickType) {
        this.field_147057_D = true;
        final boolean flag = clickType == 1;
        clickType = ((slotId == -999 && clickType == 0) ? 4 : clickType);
        if (slotIn == null && GuiContainerCreative.selectedTabIndex != CreativeTabs.tabInventory.getTabIndex() && clickType != 5) {
            final InventoryPlayer inventoryplayer1 = Minecraft.thePlayer.inventory;
            if (inventoryplayer1.getItemStack() != null) {
                if (clickedButton == 0) {
                    Minecraft.thePlayer.dropPlayerItemWithRandomChoice(inventoryplayer1.getItemStack(), true);
                    this.mc.playerController.sendPacketDropItem(inventoryplayer1.getItemStack());
                    inventoryplayer1.setItemStack(null);
                }
                if (clickedButton == 1) {
                    final ItemStack itemstack5 = inventoryplayer1.getItemStack().splitStack(1);
                    Minecraft.thePlayer.dropPlayerItemWithRandomChoice(itemstack5, true);
                    this.mc.playerController.sendPacketDropItem(itemstack5);
                    if (inventoryplayer1.getItemStack().stackSize == 0) {
                        inventoryplayer1.setItemStack(null);
                    }
                }
            }
        }
        else if (slotIn == this.field_147064_C && flag) {
            for (int j = 0; j < Minecraft.thePlayer.inventoryContainer.getInventory().size(); ++j) {
                this.mc.playerController.sendSlotPacket(null, j);
            }
        }
        else if (GuiContainerCreative.selectedTabIndex == CreativeTabs.tabInventory.getTabIndex()) {
            if (slotIn == this.field_147064_C) {
                Minecraft.thePlayer.inventory.setItemStack(null);
            }
            else if (clickType == 4 && slotIn != null && slotIn.getHasStack()) {
                final ItemStack itemstack6 = slotIn.decrStackSize((clickedButton == 0) ? 1 : slotIn.getStack().getMaxStackSize());
                Minecraft.thePlayer.dropPlayerItemWithRandomChoice(itemstack6, true);
                this.mc.playerController.sendPacketDropItem(itemstack6);
            }
            else if (clickType == 4 && Minecraft.thePlayer.inventory.getItemStack() != null) {
                Minecraft.thePlayer.dropPlayerItemWithRandomChoice(Minecraft.thePlayer.inventory.getItemStack(), true);
                this.mc.playerController.sendPacketDropItem(Minecraft.thePlayer.inventory.getItemStack());
                Minecraft.thePlayer.inventory.setItemStack(null);
            }
            else {
                Minecraft.thePlayer.inventoryContainer.slotClick((slotIn == null) ? slotId : ((CreativeSlot)slotIn).slot.slotNumber, clickedButton, clickType, Minecraft.thePlayer);
                Minecraft.thePlayer.inventoryContainer.detectAndSendChanges();
            }
        }
        else if (clickType != 5 && slotIn.inventory == GuiContainerCreative.field_147060_v) {
            final InventoryPlayer inventoryplayer2 = Minecraft.thePlayer.inventory;
            ItemStack itemstack7 = inventoryplayer2.getItemStack();
            final ItemStack itemstack8 = slotIn.getStack();
            if (clickType == 2) {
                if (itemstack8 != null && clickedButton >= 0 && clickedButton < 9) {
                    final ItemStack itemstack9 = itemstack8.copy();
                    itemstack9.stackSize = itemstack9.getMaxStackSize();
                    Minecraft.thePlayer.inventory.setInventorySlotContents(clickedButton, itemstack9);
                    Minecraft.thePlayer.inventoryContainer.detectAndSendChanges();
                }
                return;
            }
            if (clickType == 3) {
                if (inventoryplayer2.getItemStack() == null && slotIn.getHasStack()) {
                    final ItemStack itemstack10 = slotIn.getStack().copy();
                    itemstack10.stackSize = itemstack10.getMaxStackSize();
                    inventoryplayer2.setItemStack(itemstack10);
                }
                return;
            }
            if (clickType == 4) {
                if (itemstack8 != null) {
                    final ItemStack itemstack11 = itemstack8.copy();
                    itemstack11.stackSize = ((clickedButton == 0) ? 1 : itemstack11.getMaxStackSize());
                    Minecraft.thePlayer.dropPlayerItemWithRandomChoice(itemstack11, true);
                    this.mc.playerController.sendPacketDropItem(itemstack11);
                }
                return;
            }
            if (itemstack7 != null && itemstack8 != null && itemstack7.isItemEqual(itemstack8)) {
                if (clickedButton == 0) {
                    if (flag) {
                        itemstack7.stackSize = itemstack7.getMaxStackSize();
                    }
                    else if (itemstack7.stackSize < itemstack7.getMaxStackSize()) {
                        final ItemStack itemStack = itemstack7;
                        ++itemStack.stackSize;
                    }
                }
                else if (itemstack7.stackSize <= 1) {
                    inventoryplayer2.setItemStack(null);
                }
                else {
                    final ItemStack itemStack2 = itemstack7;
                    --itemStack2.stackSize;
                }
            }
            else if (itemstack8 != null && itemstack7 == null) {
                inventoryplayer2.setItemStack(ItemStack.copyItemStack(itemstack8));
                itemstack7 = inventoryplayer2.getItemStack();
                if (flag) {
                    itemstack7.stackSize = itemstack7.getMaxStackSize();
                }
            }
            else {
                inventoryplayer2.setItemStack(null);
            }
        }
        else {
            this.inventorySlots.slotClick((slotIn == null) ? slotId : slotIn.slotNumber, clickedButton, clickType, Minecraft.thePlayer);
            if (Container.getDragEvent(clickedButton) == 2) {
                for (int i = 0; i < 9; ++i) {
                    this.mc.playerController.sendSlotPacket(this.inventorySlots.getSlot(45 + i).getStack(), 36 + i);
                }
            }
            else if (slotIn != null) {
                final ItemStack itemstack12 = this.inventorySlots.getSlot(slotIn.slotNumber).getStack();
                this.mc.playerController.sendSlotPacket(itemstack12, slotIn.slotNumber - this.inventorySlots.inventorySlots.size() + 9 + 36);
            }
        }
    }
    
    @Override
    protected void updateActivePotionEffects() {
        final int i = this.guiLeft;
        super.updateActivePotionEffects();
        if (this.searchField != null && this.guiLeft != i) {
            this.searchField.xPosition = this.guiLeft + 82;
        }
    }
    
    @Override
    public void initGui() {
        if (this.mc.playerController.isInCreativeMode()) {
            super.initGui();
            this.buttonList.clear();
            Keyboard.enableRepeatEvents(true);
            (this.searchField = new GuiTextField(0, this.fontRendererObj, this.guiLeft + 82, this.guiTop + 6, 89, this.fontRendererObj.FONT_HEIGHT)).setMaxStringLength(15);
            this.searchField.setEnableBackgroundDrawing(false);
            this.searchField.setVisible(false);
            this.searchField.setTextColor(16777215);
            final int i = GuiContainerCreative.selectedTabIndex;
            GuiContainerCreative.selectedTabIndex = -1;
            this.setCurrentCreativeTab(CreativeTabs.creativeTabArray[i]);
            this.field_147059_E = new CreativeCrafting(this.mc);
            Minecraft.thePlayer.inventoryContainer.onCraftGuiOpened(this.field_147059_E);
        }
        else {
            this.mc.displayGuiScreen(new GuiInventory(Minecraft.thePlayer));
        }
    }
    
    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        if (Minecraft.thePlayer != null && Minecraft.thePlayer.inventory != null) {
            Minecraft.thePlayer.inventoryContainer.removeCraftingFromCrafters(this.field_147059_E);
        }
        Keyboard.enableRepeatEvents(false);
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (GuiContainerCreative.selectedTabIndex != CreativeTabs.tabAllSearch.getTabIndex()) {
            if (GameSettings.isKeyDown(this.mc.gameSettings.keyBindChat)) {
                this.setCurrentCreativeTab(CreativeTabs.tabAllSearch);
            }
            else {
                super.keyTyped(typedChar, keyCode);
            }
        }
        else {
            if (this.field_147057_D) {
                this.field_147057_D = false;
                this.searchField.setText("");
            }
            if (!this.checkHotbarKeys(keyCode)) {
                if (this.searchField.textboxKeyTyped(typedChar, keyCode)) {
                    this.updateCreativeSearch();
                }
                else {
                    super.keyTyped(typedChar, keyCode);
                }
            }
        }
    }
    
    private void updateCreativeSearch() {
        final ContainerCreative guicontainercreative$containercreative = (ContainerCreative)this.inventorySlots;
        guicontainercreative$containercreative.itemList.clear();
        for (final Item item : Item.itemRegistry) {
            if (item != null && item.getCreativeTab() != null) {
                item.getSubItems(item, null, guicontainercreative$containercreative.itemList);
            }
        }
        Enchantment[] enchantmentsBookList;
        for (int length = (enchantmentsBookList = Enchantment.enchantmentsBookList).length, i = 0; i < length; ++i) {
            final Enchantment enchantment = enchantmentsBookList[i];
            if (enchantment != null && enchantment.type != null) {
                Items.enchanted_book.getAll(enchantment, guicontainercreative$containercreative.itemList);
            }
        }
        final Iterator<ItemStack> iterator = guicontainercreative$containercreative.itemList.iterator();
        final String s1 = this.searchField.getText().toLowerCase();
        while (iterator.hasNext()) {
            final ItemStack itemstack = iterator.next();
            boolean flag = false;
            for (final String s2 : itemstack.getTooltip(Minecraft.thePlayer, this.mc.gameSettings.advancedItemTooltips)) {
                if (EnumChatFormatting.getTextWithoutFormattingCodes(s2).toLowerCase().contains(s1)) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                iterator.remove();
            }
        }
        guicontainercreative$containercreative.scrollTo(this.currentScroll = 0.0f);
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
        final CreativeTabs creativetabs = CreativeTabs.creativeTabArray[GuiContainerCreative.selectedTabIndex];
        if (creativetabs.drawInForegroundOfTab()) {
            GlStateManager.disableBlend();
            this.fontRendererObj.drawString(I18n.format(creativetabs.getTranslatedTabLabel(), new Object[0]), 8.0, 6.0, 4210752);
        }
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        if (mouseButton == 0) {
            final int i = mouseX - this.guiLeft;
            final int j = mouseY - this.guiTop;
            CreativeTabs[] creativeTabArray;
            for (int length = (creativeTabArray = CreativeTabs.creativeTabArray).length, k = 0; k < length; ++k) {
                final CreativeTabs creativetabs = creativeTabArray[k];
                if (this.func_147049_a(creativetabs, i, j)) {
                    return;
                }
            }
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    @Override
    protected void mouseReleased(final int mouseX, final int mouseY, final int state) {
        if (state == 0) {
            final int i = mouseX - this.guiLeft;
            final int j = mouseY - this.guiTop;
            CreativeTabs[] creativeTabArray;
            for (int length = (creativeTabArray = CreativeTabs.creativeTabArray).length, k = 0; k < length; ++k) {
                final CreativeTabs creativetabs = creativeTabArray[k];
                if (this.func_147049_a(creativetabs, i, j)) {
                    this.setCurrentCreativeTab(creativetabs);
                    return;
                }
            }
        }
        super.mouseReleased(mouseX, mouseY, state);
    }
    
    private boolean needsScrollBars() {
        return GuiContainerCreative.selectedTabIndex != CreativeTabs.tabInventory.getTabIndex() && CreativeTabs.creativeTabArray[GuiContainerCreative.selectedTabIndex].shouldHidePlayerInventory() && ((ContainerCreative)this.inventorySlots).func_148328_e();
    }
    
    private void setCurrentCreativeTab(final CreativeTabs p_147050_1_) {
        final int i = GuiContainerCreative.selectedTabIndex;
        GuiContainerCreative.selectedTabIndex = p_147050_1_.getTabIndex();
        final ContainerCreative guicontainercreative$containercreative = (ContainerCreative)this.inventorySlots;
        this.dragSplittingSlots.clear();
        guicontainercreative$containercreative.itemList.clear();
        p_147050_1_.displayAllReleventItems(guicontainercreative$containercreative.itemList);
        if (p_147050_1_ == CreativeTabs.tabInventory) {
            final Container container = Minecraft.thePlayer.inventoryContainer;
            if (this.field_147063_B == null) {
                this.field_147063_B = guicontainercreative$containercreative.inventorySlots;
            }
            guicontainercreative$containercreative.inventorySlots = (List<Slot>)Lists.newArrayList();
            for (int j = 0; j < container.inventorySlots.size(); ++j) {
                final Slot slot = new CreativeSlot(container.inventorySlots.get(j), j);
                guicontainercreative$containercreative.inventorySlots.add(slot);
                if (j >= 5 && j < 9) {
                    final int j2 = j - 5;
                    final int k1 = j2 / 2;
                    final int l1 = j2 % 2;
                    slot.xDisplayPosition = 9 + k1 * 54;
                    slot.yDisplayPosition = 6 + l1 * 27;
                }
                else if (j >= 0 && j < 5) {
                    slot.yDisplayPosition = -2000;
                    slot.xDisplayPosition = -2000;
                }
                else if (j < container.inventorySlots.size()) {
                    final int m = j - 9;
                    final int l2 = m % 9;
                    final int i2 = m / 9;
                    slot.xDisplayPosition = 9 + l2 * 18;
                    if (j >= 36) {
                        slot.yDisplayPosition = 112;
                    }
                    else {
                        slot.yDisplayPosition = 54 + i2 * 18;
                    }
                }
            }
            this.field_147064_C = new Slot(GuiContainerCreative.field_147060_v, 0, 173, 112);
            guicontainercreative$containercreative.inventorySlots.add(this.field_147064_C);
        }
        else if (i == CreativeTabs.tabInventory.getTabIndex()) {
            guicontainercreative$containercreative.inventorySlots = this.field_147063_B;
            this.field_147063_B = null;
        }
        if (this.searchField != null) {
            if (p_147050_1_ == CreativeTabs.tabAllSearch) {
                this.searchField.setVisible(true);
                this.searchField.setCanLoseFocus(false);
                this.searchField.setFocused(true);
                this.searchField.setText("");
                this.updateCreativeSearch();
            }
            else {
                this.searchField.setVisible(false);
                this.searchField.setCanLoseFocus(true);
                this.searchField.setFocused(false);
            }
        }
        guicontainercreative$containercreative.scrollTo(this.currentScroll = 0.0f);
    }
    
    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int i = Mouse.getEventDWheel();
        if (i != 0 && this.needsScrollBars()) {
            final int j = ((ContainerCreative)this.inventorySlots).itemList.size() / 9 - 5;
            if (i > 0) {
                i = 1;
            }
            if (i < 0) {
                i = -1;
            }
            this.currentScroll -= (float)(i / (double)j);
            this.currentScroll = MathHelper.clamp_float(this.currentScroll, 0.0f, 1.0f);
            ((ContainerCreative)this.inventorySlots).scrollTo(this.currentScroll);
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        final boolean flag = Mouse.isButtonDown(0);
        final int i = this.guiLeft;
        final int j = this.guiTop;
        final int k = i + 175;
        final int l = j + 18;
        final int i2 = k + 14;
        final int j2 = l + 112;
        if (!this.wasClicking && flag && mouseX >= k && mouseY >= l && mouseX < i2 && mouseY < j2) {
            this.isScrolling = this.needsScrollBars();
        }
        if (!flag) {
            this.isScrolling = false;
        }
        this.wasClicking = flag;
        if (this.isScrolling) {
            this.currentScroll = (mouseY - l - 7.5f) / (j2 - l - 15.0f);
            this.currentScroll = MathHelper.clamp_float(this.currentScroll, 0.0f, 1.0f);
            ((ContainerCreative)this.inventorySlots).scrollTo(this.currentScroll);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
        CreativeTabs[] creativeTabArray;
        for (int length = (creativeTabArray = CreativeTabs.creativeTabArray).length, n = 0; n < length; ++n) {
            final CreativeTabs creativetabs = creativeTabArray[n];
            if (this.renderCreativeInventoryHoveringText(creativetabs, mouseX, mouseY)) {
                break;
            }
        }
        if (this.field_147064_C != null && GuiContainerCreative.selectedTabIndex == CreativeTabs.tabInventory.getTabIndex() && this.isPointInRegion(this.field_147064_C.xDisplayPosition, this.field_147064_C.yDisplayPosition, 16, 16, mouseX, mouseY)) {
            this.drawCreativeTabHoveringText(I18n.format("inventory.binSlot", new Object[0]), mouseX, mouseY);
        }
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.disableLighting();
    }
    
    @Override
    protected void renderToolTip(final ItemStack stack, final int x, final int y) {
        if (GuiContainerCreative.selectedTabIndex == CreativeTabs.tabAllSearch.getTabIndex()) {
            final List<String> list = stack.getTooltip(Minecraft.thePlayer, this.mc.gameSettings.advancedItemTooltips);
            CreativeTabs creativetabs = stack.getItem().getCreativeTab();
            if (creativetabs == null && stack.getItem() == Items.enchanted_book) {
                final Map<Integer, Integer> map = EnchantmentHelper.getEnchantments(stack);
                if (map.size() == 1) {
                    final Enchantment enchantment = Enchantment.getEnchantmentById(map.keySet().iterator().next());
                    CreativeTabs[] creativeTabArray;
                    for (int length = (creativeTabArray = CreativeTabs.creativeTabArray).length, j = 0; j < length; ++j) {
                        final CreativeTabs creativetabs2 = creativeTabArray[j];
                        if (creativetabs2.hasRelevantEnchantmentType(enchantment.type)) {
                            creativetabs = creativetabs2;
                            break;
                        }
                    }
                }
            }
            if (creativetabs != null) {
                list.add(1, new StringBuilder().append(EnumChatFormatting.BOLD).append(EnumChatFormatting.BLUE).append(I18n.format(creativetabs.getTranslatedTabLabel(), new Object[0])).toString());
            }
            for (int i = 0; i < list.size(); ++i) {
                if (i == 0) {
                    list.set(i, stack.getRarity().rarityColor + list.get(i));
                }
                else {
                    list.set(i, EnumChatFormatting.GRAY + list.get(i));
                }
            }
            this.drawHoveringText(list, x, y);
        }
        else {
            super.renderToolTip(stack, x, y);
        }
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        RenderHelper.enableGUIStandardItemLighting();
        final CreativeTabs creativetabs = CreativeTabs.creativeTabArray[GuiContainerCreative.selectedTabIndex];
        CreativeTabs[] creativeTabArray;
        for (int length = (creativeTabArray = CreativeTabs.creativeTabArray).length, l = 0; l < length; ++l) {
            final CreativeTabs creativetabs2 = creativeTabArray[l];
            this.mc.getTextureManager().bindTexture(GuiContainerCreative.creativeInventoryTabs);
            if (creativetabs2.getTabIndex() != GuiContainerCreative.selectedTabIndex) {
                this.func_147051_a(creativetabs2);
            }
        }
        this.mc.getTextureManager().bindTexture(new ResourceLocation("textures/gui/container/creative_inventory/tab_" + creativetabs.getBackgroundImageName()));
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        this.searchField.drawTextBox();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        final int i = this.guiLeft + 175;
        final int j = this.guiTop + 18;
        final int k = j + 112;
        this.mc.getTextureManager().bindTexture(GuiContainerCreative.creativeInventoryTabs);
        if (creativetabs.shouldHidePlayerInventory()) {
            this.drawTexturedModalRect(i, j + (int)((k - j - 17) * this.currentScroll), 232 + (this.needsScrollBars() ? 0 : 12), 0, 12, 15);
        }
        this.func_147051_a(creativetabs);
        if (creativetabs == CreativeTabs.tabInventory) {
            GuiInventory.drawEntityOnScreen(this.guiLeft + 43, this.guiTop + 45, 20, (float)(this.guiLeft + 43 - mouseX), (float)(this.guiTop + 45 - 30 - mouseY), Minecraft.thePlayer);
        }
    }
    
    protected boolean func_147049_a(final CreativeTabs p_147049_1_, final int p_147049_2_, final int p_147049_3_) {
        final int i = p_147049_1_.getTabColumn();
        int j = 28 * i;
        int k = 0;
        if (i == 5) {
            j = this.xSize - 28 + 2;
        }
        else if (i > 0) {
            j += i;
        }
        if (p_147049_1_.isTabInFirstRow()) {
            k -= 32;
        }
        else {
            k += this.ySize;
        }
        return p_147049_2_ >= j && p_147049_2_ <= j + 28 && p_147049_3_ >= k && p_147049_3_ <= k + 32;
    }
    
    protected boolean renderCreativeInventoryHoveringText(final CreativeTabs p_147052_1_, final int p_147052_2_, final int p_147052_3_) {
        final int i = p_147052_1_.getTabColumn();
        int j = 28 * i;
        int k = 0;
        if (i == 5) {
            j = this.xSize - 28 + 2;
        }
        else if (i > 0) {
            j += i;
        }
        if (p_147052_1_.isTabInFirstRow()) {
            k -= 32;
        }
        else {
            k += this.ySize;
        }
        if (this.isPointInRegion(j + 3, k + 3, 23, 27, p_147052_2_, p_147052_3_)) {
            this.drawCreativeTabHoveringText(I18n.format(p_147052_1_.getTranslatedTabLabel(), new Object[0]), p_147052_2_, p_147052_3_);
            return true;
        }
        return false;
    }
    
    protected void func_147051_a(final CreativeTabs p_147051_1_) {
        final boolean flag = p_147051_1_.getTabIndex() == GuiContainerCreative.selectedTabIndex;
        final boolean flag2 = p_147051_1_.isTabInFirstRow();
        final int i = p_147051_1_.getTabColumn();
        final int j = i * 28;
        int k = 0;
        int l = this.guiLeft + 28 * i;
        int i2 = this.guiTop;
        final int j2 = 32;
        if (flag) {
            k += 32;
        }
        if (i == 5) {
            l = this.guiLeft + this.xSize - 28;
        }
        else if (i > 0) {
            l += i;
        }
        if (flag2) {
            i2 -= 28;
        }
        else {
            k += 64;
            i2 += this.ySize - 4;
        }
        GlStateManager.disableLighting();
        this.drawTexturedModalRect(l, i2, j, k, 28, j2);
        this.zLevel = 100.0f;
        this.itemRender.zLevel = 100.0f;
        l += 6;
        i2 = i2 + 8 + (flag2 ? 1 : -1);
        GlStateManager.enableLighting();
        GlStateManager.enableRescaleNormal();
        final ItemStack itemstack = p_147051_1_.getIconItemStack();
        this.itemRender.renderItemAndEffectIntoGUI(itemstack, l, i2);
        this.itemRender.renderItemOverlays(this.fontRendererObj, itemstack, l, i2);
        GlStateManager.disableLighting();
        this.itemRender.zLevel = 0.0f;
        this.zLevel = 0.0f;
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.id == 0) {
            this.mc.displayGuiScreen(new GuiAchievements(this, Minecraft.thePlayer.getStatFileWriter()));
        }
        if (button.id == 1) {
            this.mc.displayGuiScreen(new GuiStats(this, Minecraft.thePlayer.getStatFileWriter()));
        }
    }
    
    public int getSelectedTabIndex() {
        return GuiContainerCreative.selectedTabIndex;
    }
    
    static class ContainerCreative extends Container
    {
        public List<ItemStack> itemList;
        
        public ContainerCreative(final EntityPlayer p_i1086_1_) {
            this.itemList = (List<ItemStack>)Lists.newArrayList();
            final InventoryPlayer inventoryplayer = p_i1086_1_.inventory;
            for (int i = 0; i < 5; ++i) {
                for (int j = 0; j < 9; ++j) {
                    this.addSlotToContainer(new Slot(GuiContainerCreative.field_147060_v, i * 9 + j, 9 + j * 18, 18 + i * 18));
                }
            }
            for (int k = 0; k < 9; ++k) {
                this.addSlotToContainer(new Slot(inventoryplayer, k, 9 + k * 18, 112));
            }
            this.scrollTo(0.0f);
        }
        
        @Override
        public boolean canInteractWith(final EntityPlayer playerIn) {
            return true;
        }
        
        public void scrollTo(final float p_148329_1_) {
            final int i = (this.itemList.size() + 9 - 1) / 9 - 5;
            int j = (int)(p_148329_1_ * i + 0.5);
            if (j < 0) {
                j = 0;
            }
            for (int k = 0; k < 5; ++k) {
                for (int l = 0; l < 9; ++l) {
                    final int i2 = l + (k + j) * 9;
                    if (i2 >= 0 && i2 < this.itemList.size()) {
                        GuiContainerCreative.field_147060_v.setInventorySlotContents(l + k * 9, this.itemList.get(i2));
                    }
                    else {
                        GuiContainerCreative.field_147060_v.setInventorySlotContents(l + k * 9, null);
                    }
                }
            }
        }
        
        public boolean func_148328_e() {
            return this.itemList.size() > 45;
        }
        
        @Override
        protected void retrySlotClick(final int slotId, final int clickedButton, final boolean mode, final EntityPlayer playerIn) {
        }
        
        @Override
        public ItemStack transferStackInSlot(final EntityPlayer playerIn, final int index) {
            if (index >= this.inventorySlots.size() - 9 && index < this.inventorySlots.size()) {
                final Slot slot = this.inventorySlots.get(index);
                if (slot != null && slot.getHasStack()) {
                    slot.putStack(null);
                }
            }
            return null;
        }
        
        @Override
        public boolean canMergeSlot(final ItemStack stack, final Slot p_94530_2_) {
            return p_94530_2_.yDisplayPosition > 90;
        }
        
        @Override
        public boolean canDragIntoSlot(final Slot p_94531_1_) {
            return p_94531_1_.inventory instanceof InventoryPlayer || (p_94531_1_.yDisplayPosition > 90 && p_94531_1_.xDisplayPosition <= 162);
        }
    }
    
    class CreativeSlot extends Slot
    {
        private final Slot slot;
        
        public CreativeSlot(final Slot p_i46313_2_, final int p_i46313_3_) {
            super(p_i46313_2_.inventory, p_i46313_3_, 0, 0);
            this.slot = p_i46313_2_;
        }
        
        @Override
        public void onPickupFromSlot(final EntityPlayer playerIn, final ItemStack stack) {
            this.slot.onPickupFromSlot(playerIn, stack);
        }
        
        @Override
        public boolean isItemValid(final ItemStack stack) {
            return this.slot.isItemValid(stack);
        }
        
        @Override
        public ItemStack getStack() {
            return this.slot.getStack();
        }
        
        @Override
        public boolean getHasStack() {
            return this.slot.getHasStack();
        }
        
        @Override
        public void putStack(final ItemStack stack) {
            this.slot.putStack(stack);
        }
        
        @Override
        public void onSlotChanged() {
            this.slot.onSlotChanged();
        }
        
        @Override
        public int getSlotStackLimit() {
            return this.slot.getSlotStackLimit();
        }
        
        @Override
        public int getItemStackLimit(final ItemStack stack) {
            return this.slot.getItemStackLimit(stack);
        }
        
        @Override
        public String getSlotTexture() {
            return this.slot.getSlotTexture();
        }
        
        @Override
        public ItemStack decrStackSize(final int amount) {
            return this.slot.decrStackSize(amount);
        }
        
        @Override
        public boolean isHere(final IInventory inv, final int slotIn) {
            return this.slot.isHere(inv, slotIn);
        }
    }
}
