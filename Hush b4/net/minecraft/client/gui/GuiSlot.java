// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import org.lwjgl.input.Mouse;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.Minecraft;

public abstract class GuiSlot
{
    protected final Minecraft mc;
    protected int width;
    protected int height;
    protected int top;
    protected int bottom;
    protected int right;
    protected int left;
    protected final int slotHeight;
    private int scrollUpButtonID;
    private int scrollDownButtonID;
    protected int mouseX;
    protected int mouseY;
    protected boolean field_148163_i;
    protected int initialClickY;
    protected float scrollMultiplier;
    protected float amountScrolled;
    protected int selectedElement;
    protected long lastClicked;
    protected boolean field_178041_q;
    protected boolean showSelectionBox;
    protected boolean hasListHeader;
    protected int headerPadding;
    private boolean enabled;
    
    public GuiSlot(final Minecraft mcIn, final int width, final int height, final int topIn, final int bottomIn, final int slotHeightIn) {
        this.field_148163_i = true;
        this.initialClickY = -2;
        this.selectedElement = -1;
        this.field_178041_q = true;
        this.showSelectionBox = true;
        this.enabled = true;
        this.mc = mcIn;
        this.width = width;
        this.height = height;
        this.top = topIn;
        this.bottom = bottomIn;
        this.slotHeight = slotHeightIn;
        this.left = 0;
        this.right = width;
    }
    
    public void setDimensions(final int widthIn, final int heightIn, final int topIn, final int bottomIn) {
        this.width = widthIn;
        this.height = heightIn;
        this.top = topIn;
        this.bottom = bottomIn;
        this.left = 0;
        this.right = widthIn;
    }
    
    public void setShowSelectionBox(final boolean showSelectionBoxIn) {
        this.showSelectionBox = showSelectionBoxIn;
    }
    
    protected void setHasListHeader(final boolean hasListHeaderIn, final int headerPaddingIn) {
        this.hasListHeader = hasListHeaderIn;
        this.headerPadding = headerPaddingIn;
        if (!hasListHeaderIn) {
            this.headerPadding = 0;
        }
    }
    
    protected abstract int getSize();
    
    protected abstract void elementClicked(final int p0, final boolean p1, final int p2, final int p3);
    
    protected abstract boolean isSelected(final int p0);
    
    protected int getContentHeight() {
        return this.getSize() * this.slotHeight + this.headerPadding;
    }
    
    protected abstract void drawBackground();
    
    protected void func_178040_a(final int p_178040_1_, final int p_178040_2_, final int p_178040_3_) {
    }
    
    protected abstract void drawSlot(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5);
    
    protected void drawListHeader(final int p_148129_1_, final int p_148129_2_, final Tessellator p_148129_3_) {
    }
    
    protected void func_148132_a(final int p_148132_1_, final int p_148132_2_) {
    }
    
    protected void func_148142_b(final int p_148142_1_, final int p_148142_2_) {
    }
    
    public int getSlotIndexFromScreenCoords(final int p_148124_1_, final int p_148124_2_) {
        final int i = this.left + this.width / 2 - this.getListWidth() / 2;
        final int j = this.left + this.width / 2 + this.getListWidth() / 2;
        final int k = p_148124_2_ - this.top - this.headerPadding + (int)this.amountScrolled - 4;
        final int l = k / this.slotHeight;
        return (p_148124_1_ < this.getScrollBarX() && p_148124_1_ >= i && p_148124_1_ <= j && l >= 0 && k >= 0 && l < this.getSize()) ? l : -1;
    }
    
    public void registerScrollButtons(final int scrollUpButtonIDIn, final int scrollDownButtonIDIn) {
        this.scrollUpButtonID = scrollUpButtonIDIn;
        this.scrollDownButtonID = scrollDownButtonIDIn;
    }
    
    protected void bindAmountScrolled() {
        this.amountScrolled = MathHelper.clamp_float(this.amountScrolled, 0.0f, (float)this.func_148135_f());
    }
    
    public int func_148135_f() {
        return Math.max(0, this.getContentHeight() - (this.bottom - this.top - 4));
    }
    
    public int getAmountScrolled() {
        return (int)this.amountScrolled;
    }
    
    public boolean isMouseYWithinSlotBounds(final int p_148141_1_) {
        return p_148141_1_ >= this.top && p_148141_1_ <= this.bottom && this.mouseX >= this.left && this.mouseX <= this.right;
    }
    
    public void scrollBy(final int amount) {
        this.amountScrolled += amount;
        this.bindAmountScrolled();
        this.initialClickY = -2;
    }
    
    public void actionPerformed(final GuiButton button) {
        if (button.enabled) {
            if (button.id == this.scrollUpButtonID) {
                this.amountScrolled -= this.slotHeight * 2 / 3;
                this.initialClickY = -2;
                this.bindAmountScrolled();
            }
            else if (button.id == this.scrollDownButtonID) {
                this.amountScrolled += this.slotHeight * 2 / 3;
                this.initialClickY = -2;
                this.bindAmountScrolled();
            }
        }
    }
    
    public void drawScreen(final int mouseXIn, final int mouseYIn, final float p_148128_3_) {
        if (this.field_178041_q) {
            this.mouseX = mouseXIn;
            this.mouseY = mouseYIn;
            this.drawBackground();
            final int i = this.getScrollBarX();
            final int j = i + 6;
            this.bindAmountScrolled();
            GlStateManager.disableLighting();
            GlStateManager.disableFog();
            final Tessellator tessellator = Tessellator.getInstance();
            final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            this.mc.getTextureManager().bindTexture(Gui.optionsBackground);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            final float f = 32.0f;
            worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            worldrenderer.pos(this.left, this.bottom, 0.0).tex(this.left / f, (this.bottom + (int)this.amountScrolled) / f).color(32, 32, 32, 255).endVertex();
            worldrenderer.pos(this.right, this.bottom, 0.0).tex(this.right / f, (this.bottom + (int)this.amountScrolled) / f).color(32, 32, 32, 255).endVertex();
            worldrenderer.pos(this.right, this.top, 0.0).tex(this.right / f, (this.top + (int)this.amountScrolled) / f).color(32, 32, 32, 255).endVertex();
            worldrenderer.pos(this.left, this.top, 0.0).tex(this.left / f, (this.top + (int)this.amountScrolled) / f).color(32, 32, 32, 255).endVertex();
            tessellator.draw();
            final int k = this.left + this.width / 2 - this.getListWidth() / 2 + 2;
            final int l = this.top + 4 - (int)this.amountScrolled;
            if (this.hasListHeader) {
                this.drawListHeader(k, l, tessellator);
            }
            this.drawSelectionBox(k, l, mouseXIn, mouseYIn);
            GlStateManager.disableDepth();
            final int i2 = 4;
            this.overlayBackground(0, this.top, 255, 255);
            this.overlayBackground(this.bottom, this.height, 255, 255);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
            GlStateManager.disableAlpha();
            GlStateManager.shadeModel(7425);
            GlStateManager.disableTexture2D();
            worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            worldrenderer.pos(this.left, this.top + i2, 0.0).tex(0.0, 1.0).color(0, 0, 0, 0).endVertex();
            worldrenderer.pos(this.right, this.top + i2, 0.0).tex(1.0, 1.0).color(0, 0, 0, 0).endVertex();
            worldrenderer.pos(this.right, this.top, 0.0).tex(1.0, 0.0).color(0, 0, 0, 255).endVertex();
            worldrenderer.pos(this.left, this.top, 0.0).tex(0.0, 0.0).color(0, 0, 0, 255).endVertex();
            tessellator.draw();
            worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            worldrenderer.pos(this.left, this.bottom, 0.0).tex(0.0, 1.0).color(0, 0, 0, 255).endVertex();
            worldrenderer.pos(this.right, this.bottom, 0.0).tex(1.0, 1.0).color(0, 0, 0, 255).endVertex();
            worldrenderer.pos(this.right, this.bottom - i2, 0.0).tex(1.0, 0.0).color(0, 0, 0, 0).endVertex();
            worldrenderer.pos(this.left, this.bottom - i2, 0.0).tex(0.0, 0.0).color(0, 0, 0, 0).endVertex();
            tessellator.draw();
            final int j2 = this.func_148135_f();
            if (j2 > 0) {
                int k2 = (this.bottom - this.top) * (this.bottom - this.top) / this.getContentHeight();
                k2 = MathHelper.clamp_int(k2, 32, this.bottom - this.top - 8);
                int l2 = (int)this.amountScrolled * (this.bottom - this.top - k2) / j2 + this.top;
                if (l2 < this.top) {
                    l2 = this.top;
                }
                worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                worldrenderer.pos(i, this.bottom, 0.0).tex(0.0, 1.0).color(0, 0, 0, 255).endVertex();
                worldrenderer.pos(j, this.bottom, 0.0).tex(1.0, 1.0).color(0, 0, 0, 255).endVertex();
                worldrenderer.pos(j, this.top, 0.0).tex(1.0, 0.0).color(0, 0, 0, 255).endVertex();
                worldrenderer.pos(i, this.top, 0.0).tex(0.0, 0.0).color(0, 0, 0, 255).endVertex();
                tessellator.draw();
                worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                worldrenderer.pos(i, l2 + k2, 0.0).tex(0.0, 1.0).color(128, 128, 128, 255).endVertex();
                worldrenderer.pos(j, l2 + k2, 0.0).tex(1.0, 1.0).color(128, 128, 128, 255).endVertex();
                worldrenderer.pos(j, l2, 0.0).tex(1.0, 0.0).color(128, 128, 128, 255).endVertex();
                worldrenderer.pos(i, l2, 0.0).tex(0.0, 0.0).color(128, 128, 128, 255).endVertex();
                tessellator.draw();
                worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                worldrenderer.pos(i, l2 + k2 - 1, 0.0).tex(0.0, 1.0).color(192, 192, 192, 255).endVertex();
                worldrenderer.pos(j - 1, l2 + k2 - 1, 0.0).tex(1.0, 1.0).color(192, 192, 192, 255).endVertex();
                worldrenderer.pos(j - 1, l2, 0.0).tex(1.0, 0.0).color(192, 192, 192, 255).endVertex();
                worldrenderer.pos(i, l2, 0.0).tex(0.0, 0.0).color(192, 192, 192, 255).endVertex();
                tessellator.draw();
            }
            this.func_148142_b(mouseXIn, mouseYIn);
            GlStateManager.enableTexture2D();
            GlStateManager.shadeModel(7424);
            GlStateManager.enableAlpha();
            GlStateManager.disableBlend();
        }
    }
    
    public void handleMouseInput() {
        if (this.isMouseYWithinSlotBounds(this.mouseY)) {
            if (Mouse.getEventButton() == 0 && Mouse.getEventButtonState() && this.mouseY >= this.top && this.mouseY <= this.bottom) {
                final int i = (this.width - this.getListWidth()) / 2;
                final int j = (this.width + this.getListWidth()) / 2;
                final int k = this.mouseY - this.top - this.headerPadding + (int)this.amountScrolled - 4;
                final int l = k / this.slotHeight;
                if (l < this.getSize() && this.mouseX >= i && this.mouseX <= j && l >= 0 && k >= 0) {
                    this.elementClicked(l, false, this.mouseX, this.mouseY);
                    this.selectedElement = l;
                }
                else if (this.mouseX >= i && this.mouseX <= j && k < 0) {
                    this.func_148132_a(this.mouseX - i, this.mouseY - this.top + (int)this.amountScrolled - 4);
                }
            }
            if (Mouse.isButtonDown(0) && this.getEnabled()) {
                if (this.initialClickY == -1) {
                    boolean flag1 = true;
                    if (this.mouseY >= this.top && this.mouseY <= this.bottom) {
                        final int j2 = (this.width - this.getListWidth()) / 2;
                        final int k2 = (this.width + this.getListWidth()) / 2;
                        final int l2 = this.mouseY - this.top - this.headerPadding + (int)this.amountScrolled - 4;
                        final int i2 = l2 / this.slotHeight;
                        if (i2 < this.getSize() && this.mouseX >= j2 && this.mouseX <= k2 && i2 >= 0 && l2 >= 0) {
                            final boolean flag2 = i2 == this.selectedElement && Minecraft.getSystemTime() - this.lastClicked < 250L;
                            this.elementClicked(i2, flag2, this.mouseX, this.mouseY);
                            this.selectedElement = i2;
                            this.lastClicked = Minecraft.getSystemTime();
                        }
                        else if (this.mouseX >= j2 && this.mouseX <= k2 && l2 < 0) {
                            this.func_148132_a(this.mouseX - j2, this.mouseY - this.top + (int)this.amountScrolled - 4);
                            flag1 = false;
                        }
                        final int i3 = this.getScrollBarX();
                        final int j3 = i3 + 6;
                        if (this.mouseX >= i3 && this.mouseX <= j3) {
                            this.scrollMultiplier = -1.0f;
                            int k3 = this.func_148135_f();
                            if (k3 < 1) {
                                k3 = 1;
                            }
                            int l3 = (int)((this.bottom - this.top) * (this.bottom - this.top) / (float)this.getContentHeight());
                            l3 = MathHelper.clamp_int(l3, 32, this.bottom - this.top - 8);
                            this.scrollMultiplier /= (this.bottom - this.top - l3) / (float)k3;
                        }
                        else {
                            this.scrollMultiplier = 1.0f;
                        }
                        if (flag1) {
                            this.initialClickY = this.mouseY;
                        }
                        else {
                            this.initialClickY = -2;
                        }
                    }
                    else {
                        this.initialClickY = -2;
                    }
                }
                else if (this.initialClickY >= 0) {
                    this.amountScrolled -= (this.mouseY - this.initialClickY) * this.scrollMultiplier;
                    this.initialClickY = this.mouseY;
                }
            }
            else {
                this.initialClickY = -1;
            }
            int i4 = Mouse.getEventDWheel();
            if (i4 != 0) {
                if (i4 > 0) {
                    i4 = -1;
                }
                else if (i4 < 0) {
                    i4 = 1;
                }
                this.amountScrolled += i4 * this.slotHeight / 2;
            }
        }
    }
    
    public void setEnabled(final boolean enabledIn) {
        this.enabled = enabledIn;
    }
    
    public boolean getEnabled() {
        return this.enabled;
    }
    
    public int getListWidth() {
        return 220;
    }
    
    protected void drawSelectionBox(final int p_148120_1_, final int p_148120_2_, final int mouseXIn, final int mouseYIn) {
        final int i = this.getSize();
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        for (int j = 0; j < i; ++j) {
            final int k = p_148120_2_ + j * this.slotHeight + this.headerPadding;
            final int l = this.slotHeight - 4;
            if (k > this.bottom || k + l < this.top) {
                this.func_178040_a(j, p_148120_1_, k);
            }
            if (this.showSelectionBox && this.isSelected(j)) {
                final int i2 = this.left + (this.width / 2 - this.getListWidth() / 2);
                final int j2 = this.left + this.width / 2 + this.getListWidth() / 2;
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                GlStateManager.disableTexture2D();
                worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                worldrenderer.pos(i2, k + l + 2, 0.0).tex(0.0, 1.0).color(128, 128, 128, 255).endVertex();
                worldrenderer.pos(j2, k + l + 2, 0.0).tex(1.0, 1.0).color(128, 128, 128, 255).endVertex();
                worldrenderer.pos(j2, k - 2, 0.0).tex(1.0, 0.0).color(128, 128, 128, 255).endVertex();
                worldrenderer.pos(i2, k - 2, 0.0).tex(0.0, 0.0).color(128, 128, 128, 255).endVertex();
                worldrenderer.pos(i2 + 1, k + l + 1, 0.0).tex(0.0, 1.0).color(0, 0, 0, 255).endVertex();
                worldrenderer.pos(j2 - 1, k + l + 1, 0.0).tex(1.0, 1.0).color(0, 0, 0, 255).endVertex();
                worldrenderer.pos(j2 - 1, k - 1, 0.0).tex(1.0, 0.0).color(0, 0, 0, 255).endVertex();
                worldrenderer.pos(i2 + 1, k - 1, 0.0).tex(0.0, 0.0).color(0, 0, 0, 255).endVertex();
                tessellator.draw();
                GlStateManager.enableTexture2D();
            }
            this.drawSlot(j, p_148120_1_, k, l, mouseXIn, mouseYIn);
        }
    }
    
    protected int getScrollBarX() {
        return this.width / 2 + 124;
    }
    
    protected void overlayBackground(final int startY, final int endY, final int startAlpha, final int endAlpha) {
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        this.mc.getTextureManager().bindTexture(Gui.optionsBackground);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        final float f = 32.0f;
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        worldrenderer.pos(this.left, endY, 0.0).tex(0.0, endY / 32.0f).color(64, 64, 64, endAlpha).endVertex();
        worldrenderer.pos(this.left + this.width, endY, 0.0).tex(this.width / 32.0f, endY / 32.0f).color(64, 64, 64, endAlpha).endVertex();
        worldrenderer.pos(this.left + this.width, startY, 0.0).tex(this.width / 32.0f, startY / 32.0f).color(64, 64, 64, startAlpha).endVertex();
        worldrenderer.pos(this.left, startY, 0.0).tex(0.0, startY / 32.0f).color(64, 64, 64, startAlpha).endVertex();
        tessellator.draw();
    }
    
    public void setSlotXBoundsFromLeft(final int leftIn) {
        this.left = leftIn;
        this.right = leftIn + this.width;
    }
    
    public int getSlotHeight() {
        return this.slotHeight;
    }
}
