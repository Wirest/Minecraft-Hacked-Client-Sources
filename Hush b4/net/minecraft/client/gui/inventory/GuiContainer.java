// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui.inventory;

import net.minecraft.entity.player.EntityPlayer;
import java.io.IOException;
import org.lwjgl.input.Keyboard;
import java.util.Iterator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.Minecraft;
import com.google.common.collect.Sets;
import java.util.Set;
import net.minecraft.item.ItemStack;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.gui.GuiScreen;

public abstract class GuiContainer extends GuiScreen
{
    protected static final ResourceLocation inventoryBackground;
    protected int xSize;
    protected int ySize;
    public Container inventorySlots;
    protected int guiLeft;
    protected int guiTop;
    private Slot theSlot;
    private Slot clickedSlot;
    private boolean isRightMouseClick;
    private ItemStack draggedStack;
    private int touchUpX;
    private int touchUpY;
    private Slot returningStackDestSlot;
    private long returningStackTime;
    private ItemStack returningStack;
    private Slot currentDragTargetSlot;
    private long dragItemDropDelay;
    protected final Set<Slot> dragSplittingSlots;
    protected boolean dragSplitting;
    private int dragSplittingLimit;
    private int dragSplittingButton;
    private boolean ignoreMouseUp;
    private int dragSplittingRemnant;
    private long lastClickTime;
    private Slot lastClickSlot;
    private int lastClickButton;
    private boolean doubleClick;
    private ItemStack shiftClickedSlot;
    
    static {
        inventoryBackground = new ResourceLocation("textures/gui/container/inventory.png");
    }
    
    public GuiContainer(final Container inventorySlotsIn) {
        this.xSize = 176;
        this.ySize = 166;
        this.dragSplittingSlots = (Set<Slot>)Sets.newHashSet();
        this.inventorySlots = inventorySlotsIn;
        this.ignoreMouseUp = true;
    }
    
    @Override
    public void initGui() {
        super.initGui();
        Minecraft.thePlayer.openContainer = this.inventorySlots;
        this.guiLeft = (this.width - this.xSize) / 2;
        this.guiTop = (this.height - this.ySize) / 2;
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        final int i = this.guiLeft;
        final int j = this.guiTop;
        this.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        super.drawScreen(mouseX, mouseY, partialTicks);
        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)i, (float)j, 0.0f);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.enableRescaleNormal();
        this.theSlot = null;
        final int k = 240;
        final int l = 240;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, k / 1.0f, l / 1.0f);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        for (int i2 = 0; i2 < this.inventorySlots.inventorySlots.size(); ++i2) {
            final Slot slot = this.inventorySlots.inventorySlots.get(i2);
            this.drawSlot(slot);
            if (this.isMouseOverSlot(slot, mouseX, mouseY) && slot.canBeHovered()) {
                this.theSlot = slot;
                GlStateManager.disableLighting();
                GlStateManager.disableDepth();
                final int j2 = slot.xDisplayPosition;
                final int k2 = slot.yDisplayPosition;
                GlStateManager.colorMask(true, true, true, false);
                this.drawGradientRect(j2, k2, j2 + 16, k2 + 16, -2130706433, -2130706433);
                GlStateManager.colorMask(true, true, true, true);
                GlStateManager.enableLighting();
                GlStateManager.enableDepth();
            }
        }
        RenderHelper.disableStandardItemLighting();
        this.drawGuiContainerForegroundLayer(mouseX, mouseY);
        RenderHelper.enableGUIStandardItemLighting();
        final InventoryPlayer inventoryplayer = Minecraft.thePlayer.inventory;
        ItemStack itemstack = (this.draggedStack == null) ? inventoryplayer.getItemStack() : this.draggedStack;
        if (itemstack != null) {
            final int j3 = 8;
            final int k3 = (this.draggedStack == null) ? 8 : 16;
            String s = null;
            if (this.draggedStack != null && this.isRightMouseClick) {
                itemstack = itemstack.copy();
                itemstack.stackSize = MathHelper.ceiling_float_int(itemstack.stackSize / 2.0f);
            }
            else if (this.dragSplitting && this.dragSplittingSlots.size() > 1) {
                itemstack = itemstack.copy();
                itemstack.stackSize = this.dragSplittingRemnant;
                if (itemstack.stackSize == 0) {
                    s = EnumChatFormatting.YELLOW + "0";
                }
            }
            this.drawItemStack(itemstack, mouseX - i - j3, mouseY - j - k3, s);
        }
        if (this.returningStack != null) {
            float f = (Minecraft.getSystemTime() - this.returningStackTime) / 100.0f;
            if (f >= 1.0f) {
                f = 1.0f;
                this.returningStack = null;
            }
            final int l2 = this.returningStackDestSlot.xDisplayPosition - this.touchUpX;
            final int i3 = this.returningStackDestSlot.yDisplayPosition - this.touchUpY;
            final int l3 = this.touchUpX + (int)(l2 * f);
            final int i4 = this.touchUpY + (int)(i3 * f);
            this.drawItemStack(this.returningStack, l3, i4, null);
        }
        GlStateManager.popMatrix();
        if (inventoryplayer.getItemStack() == null && this.theSlot != null && this.theSlot.getHasStack()) {
            final ItemStack itemstack2 = this.theSlot.getStack();
            this.renderToolTip(itemstack2, mouseX, mouseY);
        }
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        RenderHelper.enableStandardItemLighting();
    }
    
    private void drawItemStack(final ItemStack stack, final int x, final int y, final String altText) {
        GlStateManager.translate(0.0f, 0.0f, 32.0f);
        this.zLevel = 200.0f;
        this.itemRender.zLevel = 200.0f;
        this.itemRender.renderItemAndEffectIntoGUI(stack, x, y);
        this.itemRender.renderItemOverlayIntoGUI(this.fontRendererObj, stack, x, y - ((this.draggedStack == null) ? 0 : 8), altText);
        this.zLevel = 0.0f;
        this.itemRender.zLevel = 0.0f;
    }
    
    protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
    }
    
    protected abstract void drawGuiContainerBackgroundLayer(final float p0, final int p1, final int p2);
    
    private void drawSlot(final Slot slotIn) {
        final int i = slotIn.xDisplayPosition;
        final int j = slotIn.yDisplayPosition;
        ItemStack itemstack = slotIn.getStack();
        boolean flag = false;
        boolean flag2 = slotIn == this.clickedSlot && this.draggedStack != null && !this.isRightMouseClick;
        final ItemStack itemstack2 = Minecraft.thePlayer.inventory.getItemStack();
        String s = null;
        if (slotIn == this.clickedSlot && this.draggedStack != null && this.isRightMouseClick && itemstack != null) {
            final ItemStack copy;
            itemstack = (copy = itemstack.copy());
            copy.stackSize /= 2;
        }
        else if (this.dragSplitting && this.dragSplittingSlots.contains(slotIn) && itemstack2 != null) {
            if (this.dragSplittingSlots.size() == 1) {
                return;
            }
            if (Container.canAddItemToSlot(slotIn, itemstack2, true) && this.inventorySlots.canDragIntoSlot(slotIn)) {
                itemstack = itemstack2.copy();
                flag = true;
                Container.computeStackSize(this.dragSplittingSlots, this.dragSplittingLimit, itemstack, (slotIn.getStack() == null) ? 0 : slotIn.getStack().stackSize);
                if (itemstack.stackSize > itemstack.getMaxStackSize()) {
                    s = new StringBuilder().append(EnumChatFormatting.YELLOW).append(itemstack.getMaxStackSize()).toString();
                    itemstack.stackSize = itemstack.getMaxStackSize();
                }
                if (itemstack.stackSize > slotIn.getItemStackLimit(itemstack)) {
                    s = new StringBuilder().append(EnumChatFormatting.YELLOW).append(slotIn.getItemStackLimit(itemstack)).toString();
                    itemstack.stackSize = slotIn.getItemStackLimit(itemstack);
                }
            }
            else {
                this.dragSplittingSlots.remove(slotIn);
                this.updateDragSplitting();
            }
        }
        this.zLevel = 100.0f;
        this.itemRender.zLevel = 100.0f;
        if (itemstack == null) {
            final String s2 = slotIn.getSlotTexture();
            if (s2 != null) {
                final TextureAtlasSprite textureatlassprite = this.mc.getTextureMapBlocks().getAtlasSprite(s2);
                GlStateManager.disableLighting();
                this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
                this.drawTexturedModalRect(i, j, textureatlassprite, 16, 16);
                GlStateManager.enableLighting();
                flag2 = true;
            }
        }
        if (!flag2) {
            if (flag) {
                Gui.drawRect(i, j, i + 16, j + 16, -2130706433);
            }
            GlStateManager.enableDepth();
            this.itemRender.renderItemAndEffectIntoGUI(itemstack, i, j);
            this.itemRender.renderItemOverlayIntoGUI(this.fontRendererObj, itemstack, i, j, s);
        }
        this.itemRender.zLevel = 0.0f;
        this.zLevel = 0.0f;
    }
    
    private void updateDragSplitting() {
        final ItemStack itemstack = Minecraft.thePlayer.inventory.getItemStack();
        if (itemstack != null && this.dragSplitting) {
            this.dragSplittingRemnant = itemstack.stackSize;
            for (final Slot slot : this.dragSplittingSlots) {
                final ItemStack itemstack2 = itemstack.copy();
                final int i = (slot.getStack() == null) ? 0 : slot.getStack().stackSize;
                Container.computeStackSize(this.dragSplittingSlots, this.dragSplittingLimit, itemstack2, i);
                if (itemstack2.stackSize > itemstack2.getMaxStackSize()) {
                    itemstack2.stackSize = itemstack2.getMaxStackSize();
                }
                if (itemstack2.stackSize > slot.getItemStackLimit(itemstack2)) {
                    itemstack2.stackSize = slot.getItemStackLimit(itemstack2);
                }
                this.dragSplittingRemnant -= itemstack2.stackSize - i;
            }
        }
    }
    
    private Slot getSlotAtPosition(final int x, final int y) {
        for (int i = 0; i < this.inventorySlots.inventorySlots.size(); ++i) {
            final Slot slot = this.inventorySlots.inventorySlots.get(i);
            if (this.isMouseOverSlot(slot, x, y)) {
                return slot;
            }
        }
        return null;
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        final boolean flag = mouseButton == this.mc.gameSettings.keyBindPickBlock.getKeyCode() + 100;
        final Slot slot = this.getSlotAtPosition(mouseX, mouseY);
        final long i = Minecraft.getSystemTime();
        this.doubleClick = (this.lastClickSlot == slot && i - this.lastClickTime < 250L && this.lastClickButton == mouseButton);
        this.ignoreMouseUp = false;
        if (mouseButton == 0 || mouseButton == 1 || flag) {
            final int j = this.guiLeft;
            final int k = this.guiTop;
            final boolean flag2 = mouseX < j || mouseY < k || mouseX >= j + this.xSize || mouseY >= k + this.ySize;
            int l = -1;
            if (slot != null) {
                l = slot.slotNumber;
            }
            if (flag2) {
                l = -999;
            }
            if (this.mc.gameSettings.touchscreen && flag2 && Minecraft.thePlayer.inventory.getItemStack() == null) {
                this.mc.displayGuiScreen(null);
                return;
            }
            if (l != -1) {
                if (this.mc.gameSettings.touchscreen) {
                    if (slot != null && slot.getHasStack()) {
                        this.clickedSlot = slot;
                        this.draggedStack = null;
                        this.isRightMouseClick = (mouseButton == 1);
                    }
                    else {
                        this.clickedSlot = null;
                    }
                }
                else if (!this.dragSplitting) {
                    if (Minecraft.thePlayer.inventory.getItemStack() == null) {
                        if (mouseButton == this.mc.gameSettings.keyBindPickBlock.getKeyCode() + 100) {
                            this.handleMouseClick(slot, l, mouseButton, 3);
                        }
                        else {
                            final boolean flag3 = l != -999 && (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54));
                            int i2 = 0;
                            if (flag3) {
                                this.shiftClickedSlot = ((slot != null && slot.getHasStack()) ? slot.getStack() : null);
                                i2 = 1;
                            }
                            else if (l == -999) {
                                i2 = 4;
                            }
                            this.handleMouseClick(slot, l, mouseButton, i2);
                        }
                        this.ignoreMouseUp = true;
                    }
                    else {
                        this.dragSplitting = true;
                        this.dragSplittingButton = mouseButton;
                        this.dragSplittingSlots.clear();
                        if (mouseButton == 0) {
                            this.dragSplittingLimit = 0;
                        }
                        else if (mouseButton == 1) {
                            this.dragSplittingLimit = 1;
                        }
                        else if (mouseButton == this.mc.gameSettings.keyBindPickBlock.getKeyCode() + 100) {
                            this.dragSplittingLimit = 2;
                        }
                    }
                }
            }
        }
        this.lastClickSlot = slot;
        this.lastClickTime = i;
        this.lastClickButton = mouseButton;
    }
    
    @Override
    protected void mouseClickMove(final int mouseX, final int mouseY, final int clickedMouseButton, final long timeSinceLastClick) {
        final Slot slot = this.getSlotAtPosition(mouseX, mouseY);
        final ItemStack itemstack = Minecraft.thePlayer.inventory.getItemStack();
        if (this.clickedSlot != null && this.mc.gameSettings.touchscreen) {
            if (clickedMouseButton == 0 || clickedMouseButton == 1) {
                if (this.draggedStack == null) {
                    if (slot != this.clickedSlot && this.clickedSlot.getStack() != null) {
                        this.draggedStack = this.clickedSlot.getStack().copy();
                    }
                }
                else if (this.draggedStack.stackSize > 1 && slot != null && Container.canAddItemToSlot(slot, this.draggedStack, false)) {
                    final long i = Minecraft.getSystemTime();
                    if (this.currentDragTargetSlot == slot) {
                        if (i - this.dragItemDropDelay > 500L) {
                            this.handleMouseClick(this.clickedSlot, this.clickedSlot.slotNumber, 0, 0);
                            this.handleMouseClick(slot, slot.slotNumber, 1, 0);
                            this.handleMouseClick(this.clickedSlot, this.clickedSlot.slotNumber, 0, 0);
                            this.dragItemDropDelay = i + 750L;
                            final ItemStack draggedStack = this.draggedStack;
                            --draggedStack.stackSize;
                        }
                    }
                    else {
                        this.currentDragTargetSlot = slot;
                        this.dragItemDropDelay = i;
                    }
                }
            }
        }
        else if (this.dragSplitting && slot != null && itemstack != null && itemstack.stackSize > this.dragSplittingSlots.size() && Container.canAddItemToSlot(slot, itemstack, true) && slot.isItemValid(itemstack) && this.inventorySlots.canDragIntoSlot(slot)) {
            this.dragSplittingSlots.add(slot);
            this.updateDragSplitting();
        }
    }
    
    @Override
    protected void mouseReleased(final int mouseX, final int mouseY, final int state) {
        final Slot slot = this.getSlotAtPosition(mouseX, mouseY);
        final int i = this.guiLeft;
        final int j = this.guiTop;
        final boolean flag = mouseX < i || mouseY < j || mouseX >= i + this.xSize || mouseY >= j + this.ySize;
        int k = -1;
        if (slot != null) {
            k = slot.slotNumber;
        }
        if (flag) {
            k = -999;
        }
        if (this.doubleClick && slot != null && state == 0 && this.inventorySlots.canMergeSlot(null, slot)) {
            if (isShiftKeyDown()) {
                if (slot != null && slot.inventory != null && this.shiftClickedSlot != null) {
                    for (final Slot slot2 : this.inventorySlots.inventorySlots) {
                        if (slot2 != null && slot2.canTakeStack(Minecraft.thePlayer) && slot2.getHasStack() && slot2.inventory == slot.inventory && Container.canAddItemToSlot(slot2, this.shiftClickedSlot, true)) {
                            this.handleMouseClick(slot2, slot2.slotNumber, state, 1);
                        }
                    }
                }
            }
            else {
                this.handleMouseClick(slot, k, state, 6);
            }
            this.doubleClick = false;
            this.lastClickTime = 0L;
        }
        else {
            if (this.dragSplitting && this.dragSplittingButton != state) {
                this.dragSplitting = false;
                this.dragSplittingSlots.clear();
                this.ignoreMouseUp = true;
                return;
            }
            if (this.ignoreMouseUp) {
                this.ignoreMouseUp = false;
                return;
            }
            if (this.clickedSlot != null && this.mc.gameSettings.touchscreen) {
                if (state == 0 || state == 1) {
                    if (this.draggedStack == null && slot != this.clickedSlot) {
                        this.draggedStack = this.clickedSlot.getStack();
                    }
                    final boolean flag2 = Container.canAddItemToSlot(slot, this.draggedStack, false);
                    if (k != -1 && this.draggedStack != null && flag2) {
                        this.handleMouseClick(this.clickedSlot, this.clickedSlot.slotNumber, state, 0);
                        this.handleMouseClick(slot, k, 0, 0);
                        if (Minecraft.thePlayer.inventory.getItemStack() != null) {
                            this.handleMouseClick(this.clickedSlot, this.clickedSlot.slotNumber, state, 0);
                            this.touchUpX = mouseX - i;
                            this.touchUpY = mouseY - j;
                            this.returningStackDestSlot = this.clickedSlot;
                            this.returningStack = this.draggedStack;
                            this.returningStackTime = Minecraft.getSystemTime();
                        }
                        else {
                            this.returningStack = null;
                        }
                    }
                    else if (this.draggedStack != null) {
                        this.touchUpX = mouseX - i;
                        this.touchUpY = mouseY - j;
                        this.returningStackDestSlot = this.clickedSlot;
                        this.returningStack = this.draggedStack;
                        this.returningStackTime = Minecraft.getSystemTime();
                    }
                    this.draggedStack = null;
                    this.clickedSlot = null;
                }
            }
            else if (this.dragSplitting && !this.dragSplittingSlots.isEmpty()) {
                this.handleMouseClick(null, -999, Container.func_94534_d(0, this.dragSplittingLimit), 5);
                for (final Slot slot3 : this.dragSplittingSlots) {
                    this.handleMouseClick(slot3, slot3.slotNumber, Container.func_94534_d(1, this.dragSplittingLimit), 5);
                }
                this.handleMouseClick(null, -999, Container.func_94534_d(2, this.dragSplittingLimit), 5);
            }
            else if (Minecraft.thePlayer.inventory.getItemStack() != null) {
                if (state == this.mc.gameSettings.keyBindPickBlock.getKeyCode() + 100) {
                    this.handleMouseClick(slot, k, state, 3);
                }
                else {
                    final boolean flag3 = k != -999 && (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54));
                    if (flag3) {
                        this.shiftClickedSlot = ((slot != null && slot.getHasStack()) ? slot.getStack() : null);
                    }
                    this.handleMouseClick(slot, k, state, flag3 ? 1 : 0);
                }
            }
        }
        if (Minecraft.thePlayer.inventory.getItemStack() == null) {
            this.lastClickTime = 0L;
        }
        this.dragSplitting = false;
    }
    
    private boolean isMouseOverSlot(final Slot slotIn, final int mouseX, final int mouseY) {
        return this.isPointInRegion(slotIn.xDisplayPosition, slotIn.yDisplayPosition, 16, 16, mouseX, mouseY);
    }
    
    protected boolean isPointInRegion(final int left, final int top, final int right, final int bottom, int pointX, int pointY) {
        final int i = this.guiLeft;
        final int j = this.guiTop;
        pointX -= i;
        pointY -= j;
        return pointX >= left - 1 && pointX < left + right + 1 && pointY >= top - 1 && pointY < top + bottom + 1;
    }
    
    protected void handleMouseClick(final Slot slotIn, int slotId, final int clickedButton, final int clickType) {
        if (slotIn != null) {
            slotId = slotIn.slotNumber;
        }
        this.mc.playerController.windowClick(this.inventorySlots.windowId, slotId, clickedButton, clickType, Minecraft.thePlayer);
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (keyCode == 1 || keyCode == this.mc.gameSettings.keyBindInventory.getKeyCode()) {
            Minecraft.thePlayer.closeScreen();
        }
        this.checkHotbarKeys(keyCode);
        if (this.theSlot != null && this.theSlot.getHasStack()) {
            if (keyCode == this.mc.gameSettings.keyBindPickBlock.getKeyCode()) {
                this.handleMouseClick(this.theSlot, this.theSlot.slotNumber, 0, 3);
            }
            else if (keyCode == this.mc.gameSettings.keyBindDrop.getKeyCode()) {
                this.handleMouseClick(this.theSlot, this.theSlot.slotNumber, GuiScreen.isCtrlKeyDown() ? 1 : 0, 4);
            }
        }
    }
    
    protected boolean checkHotbarKeys(final int keyCode) {
        if (Minecraft.thePlayer.inventory.getItemStack() == null && this.theSlot != null) {
            for (int i = 0; i < 9; ++i) {
                if (keyCode == this.mc.gameSettings.keyBindsHotbar[i].getKeyCode()) {
                    this.handleMouseClick(this.theSlot, this.theSlot.slotNumber, i, 2);
                    return true;
                }
            }
        }
        return false;
    }
    
    @Override
    public void onGuiClosed() {
        if (Minecraft.thePlayer != null) {
            this.inventorySlots.onContainerClosed(Minecraft.thePlayer);
        }
    }
    
    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
    
    @Override
    public void updateScreen() {
        super.updateScreen();
        if (!Minecraft.thePlayer.isEntityAlive() || Minecraft.thePlayer.isDead) {
            Minecraft.thePlayer.closeScreen();
        }
    }
}
