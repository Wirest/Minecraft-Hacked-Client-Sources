// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ChatAllowedCharacters;
import com.google.common.base.Predicates;
import com.google.common.base.Predicate;

public class GuiTextField extends Gui
{
    private final int id;
    private final FontRenderer fontRendererInstance;
    public int xPosition;
    public int yPosition;
    private final int width;
    private final int height;
    private String text;
    private int maxStringLength;
    private int cursorCounter;
    private boolean enableBackgroundDrawing;
    private boolean canLoseFocus;
    private boolean isFocused;
    private boolean isEnabled;
    private int lineScrollOffset;
    private int cursorPosition;
    private int selectionEnd;
    private int enabledColor;
    private int disabledColor;
    private boolean visible;
    private GuiPageButtonList.GuiResponder field_175210_x;
    private Predicate<String> field_175209_y;
    
    public GuiTextField(final int componentId, final FontRenderer fontrendererObj, final int x, final int y, final int par5Width, final int par6Height) {
        this.text = "";
        this.maxStringLength = 52;
        this.enableBackgroundDrawing = true;
        this.canLoseFocus = true;
        this.isEnabled = true;
        this.enabledColor = 14737632;
        this.disabledColor = 7368816;
        this.visible = true;
        this.field_175209_y = Predicates.alwaysTrue();
        this.id = componentId;
        this.fontRendererInstance = fontrendererObj;
        this.xPosition = x;
        this.yPosition = y;
        this.width = par5Width;
        this.height = par6Height;
    }
    
    public void func_175207_a(final GuiPageButtonList.GuiResponder p_175207_1_) {
        this.field_175210_x = p_175207_1_;
    }
    
    public void updateCursorCounter() {
        ++this.cursorCounter;
    }
    
    public void setText(final String p_146180_1_) {
        if (this.field_175209_y.apply(p_146180_1_)) {
            if (p_146180_1_.length() > this.maxStringLength) {
                this.text = p_146180_1_.substring(0, this.maxStringLength);
            }
            else {
                this.text = p_146180_1_;
            }
            this.setCursorPositionEnd();
        }
    }
    
    public String getText() {
        return this.text;
    }
    
    public String getSelectedText() {
        final int i = (this.cursorPosition < this.selectionEnd) ? this.cursorPosition : this.selectionEnd;
        final int j = (this.cursorPosition < this.selectionEnd) ? this.selectionEnd : this.cursorPosition;
        return this.text.substring(i, j);
    }
    
    public void func_175205_a(final Predicate<String> p_175205_1_) {
        this.field_175209_y = p_175205_1_;
    }
    
    public void writeText(final String p_146191_1_) {
        String s = "";
        final String s2 = ChatAllowedCharacters.filterAllowedCharacters(p_146191_1_);
        final int i = (this.cursorPosition < this.selectionEnd) ? this.cursorPosition : this.selectionEnd;
        final int j = (this.cursorPosition < this.selectionEnd) ? this.selectionEnd : this.cursorPosition;
        final int k = this.maxStringLength - this.text.length() - (i - j);
        int l = 0;
        if (this.text.length() > 0) {
            s = String.valueOf(s) + this.text.substring(0, i);
        }
        if (k < s2.length()) {
            s = String.valueOf(s) + s2.substring(0, k);
            l = k;
        }
        else {
            s = String.valueOf(s) + s2;
            l = s2.length();
        }
        if (this.text.length() > 0 && j < this.text.length()) {
            s = String.valueOf(s) + this.text.substring(j);
        }
        if (this.field_175209_y.apply(s)) {
            this.text = s;
            this.moveCursorBy(i - this.selectionEnd + l);
            if (this.field_175210_x != null) {
                this.field_175210_x.func_175319_a(this.id, this.text);
            }
        }
    }
    
    public void deleteWords(final int p_146177_1_) {
        if (this.text.length() != 0) {
            if (this.selectionEnd != this.cursorPosition) {
                this.writeText("");
            }
            else {
                this.deleteFromCursor(this.getNthWordFromCursor(p_146177_1_) - this.cursorPosition);
            }
        }
    }
    
    public void deleteFromCursor(final int p_146175_1_) {
        if (this.text.length() != 0) {
            if (this.selectionEnd != this.cursorPosition) {
                this.writeText("");
            }
            else {
                final boolean flag = p_146175_1_ < 0;
                final int i = flag ? (this.cursorPosition + p_146175_1_) : this.cursorPosition;
                final int j = flag ? this.cursorPosition : (this.cursorPosition + p_146175_1_);
                String s = "";
                if (i >= 0) {
                    s = this.text.substring(0, i);
                }
                if (j < this.text.length()) {
                    s = String.valueOf(s) + this.text.substring(j);
                }
                if (this.field_175209_y.apply(s)) {
                    this.text = s;
                    if (flag) {
                        this.moveCursorBy(p_146175_1_);
                    }
                    if (this.field_175210_x != null) {
                        this.field_175210_x.func_175319_a(this.id, this.text);
                    }
                }
            }
        }
    }
    
    public int getId() {
        return this.id;
    }
    
    public int getNthWordFromCursor(final int p_146187_1_) {
        return this.getNthWordFromPos(p_146187_1_, this.getCursorPosition());
    }
    
    public int getNthWordFromPos(final int p_146183_1_, final int p_146183_2_) {
        return this.func_146197_a(p_146183_1_, p_146183_2_, true);
    }
    
    public int func_146197_a(final int p_146197_1_, final int p_146197_2_, final boolean p_146197_3_) {
        int i = p_146197_2_;
        final boolean flag = p_146197_1_ < 0;
        for (int j = Math.abs(p_146197_1_), k = 0; k < j; ++k) {
            if (!flag) {
                final int l = this.text.length();
                i = this.text.indexOf(32, i);
                if (i == -1) {
                    i = l;
                }
                else {
                    while (p_146197_3_ && i < l) {
                        if (this.text.charAt(i) != ' ') {
                            break;
                        }
                        ++i;
                    }
                }
            }
            else {
                while (p_146197_3_ && i > 0) {
                    if (this.text.charAt(i - 1) != ' ') {
                        break;
                    }
                    --i;
                }
                while (i > 0 && this.text.charAt(i - 1) != ' ') {
                    --i;
                }
            }
        }
        return i;
    }
    
    public void moveCursorBy(final int p_146182_1_) {
        this.setCursorPosition(this.selectionEnd + p_146182_1_);
    }
    
    public void setCursorPosition(final int p_146190_1_) {
        this.cursorPosition = p_146190_1_;
        final int i = this.text.length();
        this.setSelectionPos(this.cursorPosition = MathHelper.clamp_int(this.cursorPosition, 0, i));
    }
    
    public void setCursorPositionZero() {
        this.setCursorPosition(0);
    }
    
    public void setCursorPositionEnd() {
        this.setCursorPosition(this.text.length());
    }
    
    public boolean textboxKeyTyped(final char p_146201_1_, final int p_146201_2_) {
        if (!this.isFocused) {
            return false;
        }
        if (GuiScreen.isKeyComboCtrlA(p_146201_2_)) {
            this.setCursorPositionEnd();
            this.setSelectionPos(0);
            return true;
        }
        if (GuiScreen.isKeyComboCtrlC(p_146201_2_)) {
            GuiScreen.setClipboardString(this.getSelectedText());
            return true;
        }
        if (GuiScreen.isKeyComboCtrlV(p_146201_2_)) {
            if (this.isEnabled) {
                this.writeText(GuiScreen.getClipboardString());
            }
            return true;
        }
        if (GuiScreen.isKeyComboCtrlX(p_146201_2_)) {
            GuiScreen.setClipboardString(this.getSelectedText());
            if (this.isEnabled) {
                this.writeText("");
            }
            return true;
        }
        switch (p_146201_2_) {
            case 14: {
                if (GuiScreen.isCtrlKeyDown()) {
                    if (this.isEnabled) {
                        this.deleteWords(-1);
                    }
                }
                else if (this.isEnabled) {
                    this.deleteFromCursor(-1);
                }
                return true;
            }
            case 199: {
                if (GuiScreen.isShiftKeyDown()) {
                    this.setSelectionPos(0);
                }
                else {
                    this.setCursorPositionZero();
                }
                return true;
            }
            case 203: {
                if (GuiScreen.isShiftKeyDown()) {
                    if (GuiScreen.isCtrlKeyDown()) {
                        this.setSelectionPos(this.getNthWordFromPos(-1, this.getSelectionEnd()));
                    }
                    else {
                        this.setSelectionPos(this.getSelectionEnd() - 1);
                    }
                }
                else if (GuiScreen.isCtrlKeyDown()) {
                    this.setCursorPosition(this.getNthWordFromCursor(-1));
                }
                else {
                    this.moveCursorBy(-1);
                }
                return true;
            }
            case 205: {
                if (GuiScreen.isShiftKeyDown()) {
                    if (GuiScreen.isCtrlKeyDown()) {
                        this.setSelectionPos(this.getNthWordFromPos(1, this.getSelectionEnd()));
                    }
                    else {
                        this.setSelectionPos(this.getSelectionEnd() + 1);
                    }
                }
                else if (GuiScreen.isCtrlKeyDown()) {
                    this.setCursorPosition(this.getNthWordFromCursor(1));
                }
                else {
                    this.moveCursorBy(1);
                }
                return true;
            }
            case 207: {
                if (GuiScreen.isShiftKeyDown()) {
                    this.setSelectionPos(this.text.length());
                }
                else {
                    this.setCursorPositionEnd();
                }
                return true;
            }
            case 211: {
                if (GuiScreen.isCtrlKeyDown()) {
                    if (this.isEnabled) {
                        this.deleteWords(1);
                    }
                }
                else if (this.isEnabled) {
                    this.deleteFromCursor(1);
                }
                return true;
            }
            default: {
                if (ChatAllowedCharacters.isAllowedCharacter(p_146201_1_)) {
                    if (this.isEnabled) {
                        this.writeText(Character.toString(p_146201_1_));
                    }
                    return true;
                }
                return false;
            }
        }
    }
    
    public void mouseClicked(final int p_146192_1_, final int p_146192_2_, final int p_146192_3_) {
        final boolean flag = p_146192_1_ >= this.xPosition && p_146192_1_ < this.xPosition + this.width && p_146192_2_ >= this.yPosition && p_146192_2_ < this.yPosition + this.height;
        if (this.canLoseFocus) {
            this.setFocused(flag);
        }
        if (this.isFocused && flag && p_146192_3_ == 0) {
            int i = p_146192_1_ - this.xPosition;
            if (this.enableBackgroundDrawing) {
                i -= 4;
            }
            final String s = this.fontRendererInstance.trimStringToWidth(this.text.substring(this.lineScrollOffset), this.getWidth());
            this.setCursorPosition(this.fontRendererInstance.trimStringToWidth(s, i).length() + this.lineScrollOffset);
        }
    }
    
    public void drawTextBox() {
        if (this.getVisible()) {
            if (this.getEnableBackgroundDrawing()) {
                Gui.drawRect(this.xPosition - 1, this.yPosition - 1, this.xPosition + this.width + 1, this.yPosition + this.height + 1, -6250336);
                Gui.drawRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, -16777216);
            }
            final int i = this.isEnabled ? this.enabledColor : this.disabledColor;
            final int j = this.cursorPosition - this.lineScrollOffset;
            int k = this.selectionEnd - this.lineScrollOffset;
            final String s = this.fontRendererInstance.trimStringToWidth(this.text.substring(this.lineScrollOffset), this.getWidth());
            final boolean flag = j >= 0 && j <= s.length();
            final boolean flag2 = this.isFocused && this.cursorCounter / 6 % 2 == 0 && flag;
            final int l = this.enableBackgroundDrawing ? (this.xPosition + 4) : this.xPosition;
            final int i2 = this.enableBackgroundDrawing ? (this.yPosition + (this.height - 8) / 2) : this.yPosition;
            int j2 = l;
            if (k > s.length()) {
                k = s.length();
            }
            if (s.length() > 0) {
                final String s2 = flag ? s.substring(0, j) : s;
                j2 = this.fontRendererInstance.drawStringWithShadow(s2, (float)l, (float)i2, i);
            }
            final boolean flag3 = this.cursorPosition < this.text.length() || this.text.length() >= this.getMaxStringLength();
            int k2 = j2;
            if (!flag) {
                k2 = ((j > 0) ? (l + this.width) : l);
            }
            else if (flag3) {
                k2 = j2 - 1;
                --j2;
            }
            if (s.length() > 0 && flag && j < s.length()) {
                j2 = this.fontRendererInstance.drawStringWithShadow(s.substring(j), (float)j2, (float)i2, i);
            }
            if (flag2) {
                if (flag3) {
                    Gui.drawRect(k2, i2 - 1, k2 + 1, i2 + 1 + this.fontRendererInstance.FONT_HEIGHT, -3092272);
                }
                else {
                    this.fontRendererInstance.drawStringWithShadow("_", (float)k2, (float)i2, i);
                }
            }
            if (k != j) {
                final int l2 = l + this.fontRendererInstance.getStringWidth(s.substring(0, k));
                this.drawCursorVertical(k2, i2 - 1, l2 - 1, i2 + 1 + this.fontRendererInstance.FONT_HEIGHT);
            }
        }
    }
    
    private void drawCursorVertical(int p_146188_1_, int p_146188_2_, int p_146188_3_, int p_146188_4_) {
        if (p_146188_1_ < p_146188_3_) {
            final int i = p_146188_1_;
            p_146188_1_ = p_146188_3_;
            p_146188_3_ = i;
        }
        if (p_146188_2_ < p_146188_4_) {
            final int j = p_146188_2_;
            p_146188_2_ = p_146188_4_;
            p_146188_4_ = j;
        }
        if (p_146188_3_ > this.xPosition + this.width) {
            p_146188_3_ = this.xPosition + this.width;
        }
        if (p_146188_1_ > this.xPosition + this.width) {
            p_146188_1_ = this.xPosition + this.width;
        }
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.color(0.0f, 0.0f, 255.0f, 255.0f);
        GlStateManager.disableTexture2D();
        GlStateManager.enableColorLogic();
        GlStateManager.colorLogicOp(5387);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos(p_146188_1_, p_146188_4_, 0.0).endVertex();
        worldrenderer.pos(p_146188_3_, p_146188_4_, 0.0).endVertex();
        worldrenderer.pos(p_146188_3_, p_146188_2_, 0.0).endVertex();
        worldrenderer.pos(p_146188_1_, p_146188_2_, 0.0).endVertex();
        tessellator.draw();
        GlStateManager.disableColorLogic();
        GlStateManager.enableTexture2D();
    }
    
    public void setMaxStringLength(final int p_146203_1_) {
        this.maxStringLength = p_146203_1_;
        if (this.text.length() > p_146203_1_) {
            this.text = this.text.substring(0, p_146203_1_);
        }
    }
    
    public int getMaxStringLength() {
        return this.maxStringLength;
    }
    
    public int getCursorPosition() {
        return this.cursorPosition;
    }
    
    public boolean getEnableBackgroundDrawing() {
        return this.enableBackgroundDrawing;
    }
    
    public void setEnableBackgroundDrawing(final boolean p_146185_1_) {
        this.enableBackgroundDrawing = p_146185_1_;
    }
    
    public void setTextColor(final int p_146193_1_) {
        this.enabledColor = p_146193_1_;
    }
    
    public void setDisabledTextColour(final int p_146204_1_) {
        this.disabledColor = p_146204_1_;
    }
    
    public void setFocused(final boolean p_146195_1_) {
        if (p_146195_1_ && !this.isFocused) {
            this.cursorCounter = 0;
        }
        this.isFocused = p_146195_1_;
    }
    
    public boolean isFocused() {
        return this.isFocused;
    }
    
    public void setEnabled(final boolean p_146184_1_) {
        this.isEnabled = p_146184_1_;
    }
    
    public int getSelectionEnd() {
        return this.selectionEnd;
    }
    
    public int getWidth() {
        return this.getEnableBackgroundDrawing() ? (this.width - 8) : this.width;
    }
    
    public void setSelectionPos(int p_146199_1_) {
        final int i = this.text.length();
        if (p_146199_1_ > i) {
            p_146199_1_ = i;
        }
        if (p_146199_1_ < 0) {
            p_146199_1_ = 0;
        }
        this.selectionEnd = p_146199_1_;
        if (this.fontRendererInstance != null) {
            if (this.lineScrollOffset > i) {
                this.lineScrollOffset = i;
            }
            final int j = this.getWidth();
            final String s = this.fontRendererInstance.trimStringToWidth(this.text.substring(this.lineScrollOffset), j);
            final int k = s.length() + this.lineScrollOffset;
            if (p_146199_1_ == this.lineScrollOffset) {
                this.lineScrollOffset -= this.fontRendererInstance.trimStringToWidth(this.text, j, true).length();
            }
            if (p_146199_1_ > k) {
                this.lineScrollOffset += p_146199_1_ - k;
            }
            else if (p_146199_1_ <= this.lineScrollOffset) {
                this.lineScrollOffset -= this.lineScrollOffset - p_146199_1_;
            }
            this.lineScrollOffset = MathHelper.clamp_int(this.lineScrollOffset, 0, i);
        }
    }
    
    public void setCanLoseFocus(final boolean p_146205_1_) {
        this.canLoseFocus = p_146205_1_;
    }
    
    public boolean getVisible() {
        return this.visible;
    }
    
    public void setVisible(final boolean p_146189_1_) {
        this.visible = p_146189_1_;
    }
}
