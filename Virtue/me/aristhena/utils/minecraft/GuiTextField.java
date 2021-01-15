// 
// Decompiled by Procyon v0.5.30
// 

package me.aristhena.utils.minecraft;

import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ChatAllowedCharacters;
import com.google.common.base.Predicates;
import com.google.common.base.Predicate;
import net.minecraft.client.gui.GuiPageButtonList;
import net.minecraft.client.gui.Gui;

public class GuiTextField extends Gui
{
    private final int field_175208_g;
    private final FontRenderer fontRendererInstance;
    public int xPosition;
    public int yPosition;
    public int width;
    public int height;
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
    private Predicate field_175209_y;
    private static final String __OBFID = "CL_00000670";
    
    public GuiTextField(final int p_i45542_1_, final FontRenderer p_i45542_2_, final int p_i45542_3_, final int p_i45542_4_, final int p_i45542_5_, final int p_i45542_6_) {
        this.text = "";
        this.maxStringLength = Integer.MAX_VALUE;
        this.enableBackgroundDrawing = true;
        this.canLoseFocus = true;
        this.isEnabled = true;
        this.enabledColor = 14737632;
        this.disabledColor = 7368816;
        this.visible = true;
        this.field_175209_y = Predicates.alwaysTrue();
        this.field_175208_g = p_i45542_1_;
        this.fontRendererInstance = p_i45542_2_;
        this.xPosition = p_i45542_3_;
        this.yPosition = p_i45542_4_;
        this.width = p_i45542_5_;
        this.height = p_i45542_6_;
    }
    
    public void func_175207_a(final GuiPageButtonList.GuiResponder p_175207_1_) {
        this.field_175210_x = p_175207_1_;
    }
    
    public void updateCursorCounter() {
        ++this.cursorCounter;
    }
    
    public void setText(final String p_146180_1_) {
        if (this.field_175209_y.apply((Object)p_146180_1_)) {
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
        final int var1 = (this.cursorPosition < this.selectionEnd) ? this.cursorPosition : this.selectionEnd;
        final int var2 = (this.cursorPosition < this.selectionEnd) ? this.selectionEnd : this.cursorPosition;
        return this.text.substring(var1, var2);
    }
    
    public void func_175205_a(final Predicate p_175205_1_) {
        this.field_175209_y = p_175205_1_;
    }
    
    public void writeText(final String p_146191_1_) {
        String var2 = "";
        final String var3 = ChatAllowedCharacters.filterAllowedCharacters(p_146191_1_);
        final int var4 = (this.cursorPosition < this.selectionEnd) ? this.cursorPosition : this.selectionEnd;
        final int var5 = (this.cursorPosition < this.selectionEnd) ? this.selectionEnd : this.cursorPosition;
        final int var6 = this.maxStringLength - this.text.length() - (var4 - var5);
        final boolean var7 = false;
        if (this.text.length() > 0) {
            var2 = String.valueOf(var2) + this.text.substring(0, var4);
        }
        int var8;
        if (var6 < var3.length()) {
            var2 = String.valueOf(var2) + var3.substring(0, var6);
            var8 = var6;
        }
        else {
            var2 = String.valueOf(var2) + var3;
            var8 = var3.length();
        }
        if (this.text.length() > 0 && var5 < this.text.length()) {
            var2 = String.valueOf(var2) + this.text.substring(var5);
        }
        if (this.field_175209_y.apply((Object)var2)) {
            this.text = var2;
            this.moveCursorBy(var4 - this.selectionEnd + var8);
            if (this.field_175210_x != null) {
                this.field_175210_x.func_175319_a(this.field_175208_g, this.text);
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
                final boolean var2 = p_146175_1_ < 0;
                final int var3 = var2 ? (this.cursorPosition + p_146175_1_) : this.cursorPosition;
                final int var4 = var2 ? this.cursorPosition : (this.cursorPosition + p_146175_1_);
                String var5 = "";
                if (var3 >= 0) {
                    var5 = this.text.substring(0, var3);
                }
                if (var4 < this.text.length()) {
                    var5 = String.valueOf(var5) + this.text.substring(var4);
                }
                this.text = var5;
                if (var2) {
                    this.moveCursorBy(p_146175_1_);
                }
                if (this.field_175210_x != null) {
                    this.field_175210_x.func_175319_a(this.field_175208_g, this.text);
                }
            }
        }
    }
    
    public int func_175206_d() {
        return this.field_175208_g;
    }
    
    public int getNthWordFromCursor(final int p_146187_1_) {
        return this.getNthWordFromPos(p_146187_1_, this.getCursorPosition());
    }
    
    public int getNthWordFromPos(final int p_146183_1_, final int p_146183_2_) {
        return this.func_146197_a(p_146183_1_, p_146183_2_, true);
    }
    
    public int func_146197_a(final int p_146197_1_, final int p_146197_2_, final boolean p_146197_3_) {
        int var4 = p_146197_2_;
        final boolean var5 = p_146197_1_ < 0;
        for (int var6 = Math.abs(p_146197_1_), var7 = 0; var7 < var6; ++var7) {
            if (var5) {
                while (p_146197_3_ && var4 > 0) {
                    if (this.text.charAt(var4 - 1) != ' ') {
                        break;
                    }
                    --var4;
                }
                while (var4 > 0) {
                    if (this.text.charAt(var4 - 1) == ' ') {
                        break;
                    }
                    --var4;
                }
            }
            else {
                final int var8 = this.text.length();
                var4 = this.text.indexOf(32, var4);
                if (var4 == -1) {
                    var4 = var8;
                }
                else {
                    while (p_146197_3_ && var4 < var8 && this.text.charAt(var4) == ' ') {
                        ++var4;
                    }
                }
            }
        }
        return var4;
    }
    
    public void moveCursorBy(final int p_146182_1_) {
        this.setCursorPosition(this.selectionEnd + p_146182_1_);
    }
    
    public void setCursorPosition(final int p_146190_1_) {
        this.cursorPosition = p_146190_1_;
        final int var2 = this.text.length();
        this.setSelectionPos(this.cursorPosition = MathHelper.clamp_int(this.cursorPosition, 0, var2));
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
        if (GuiScreen.func_175278_g(p_146201_2_)) {
            this.setCursorPositionEnd();
            this.setSelectionPos(0);
            return true;
        }
        if (GuiScreen.func_175280_f(p_146201_2_)) {
            GuiScreen.setClipboardString(this.getSelectedText());
            return true;
        }
        if (GuiScreen.func_175279_e(p_146201_2_)) {
            if (this.isEnabled) {
                this.writeText(GuiScreen.getClipboardString());
            }
            return true;
        }
        if (GuiScreen.func_175277_d(p_146201_2_)) {
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
        final boolean var4 = p_146192_1_ >= this.xPosition && p_146192_1_ < this.xPosition + this.width && p_146192_2_ >= this.yPosition && p_146192_2_ < this.yPosition + this.height;
        if (this.canLoseFocus) {
            this.setFocused(var4);
        }
        if (this.isFocused && var4 && p_146192_3_ == 0) {
            int var5 = p_146192_1_ - this.xPosition;
            if (this.enableBackgroundDrawing) {
                var5 -= 4;
            }
            final String var6 = this.fontRendererInstance.trimStringToWidth(this.text.substring(this.lineScrollOffset), this.getWidth());
            this.setCursorPosition(this.fontRendererInstance.trimStringToWidth(var6, var5).length() + this.lineScrollOffset);
        }
    }
    
    public void drawTextBox() {
        if (this.getVisible()) {
            if (this.getEnableBackgroundDrawing()) {
                Gui.drawRect(this.xPosition - 1, this.yPosition - 1, this.xPosition + this.width + 1, this.yPosition + this.height + 1, -6250336);
                Gui.drawRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, -16777216);
            }
            final int var1 = this.isEnabled ? this.enabledColor : this.disabledColor;
            final int var2 = this.cursorPosition - this.lineScrollOffset;
            int var3 = this.selectionEnd - this.lineScrollOffset;
            final String var4 = this.fontRendererInstance.trimStringToWidth(this.text.substring(this.lineScrollOffset), this.getWidth());
            final boolean var5 = var2 >= 0 && var2 <= var4.length();
            final boolean var6 = this.isFocused && this.cursorCounter / 6 % 2 == 0 && var5;
            final int var7 = this.enableBackgroundDrawing ? (this.xPosition + 4) : this.xPosition;
            final int var8 = this.enableBackgroundDrawing ? (this.yPosition + (this.height - 8) / 2) : this.yPosition;
            int var9 = var7;
            if (var3 > var4.length()) {
                var3 = var4.length();
            }
            if (var4.length() > 0) {
                final String var10 = var5 ? var4.substring(0, var2) : var4;
                var9 = this.fontRendererInstance.drawString(var10, var7, var8, var1);
            }
            final boolean var11 = this.cursorPosition < this.text.length() || this.text.length() >= this.getMaxStringLength();
            int var12 = var9;
            if (!var5) {
                var12 = ((var2 > 0) ? (var7 + this.width) : var7);
            }
            else if (var11) {
                var12 = var9 - 1;
                --var9;
            }
            if (var4.length() > 0 && var5 && var2 < var4.length()) {
                var9 = this.fontRendererInstance.drawString(var4.substring(var2), var9, var8, var1);
            }
            if (var6) {
                if (var11) {
                    Gui.drawRect(var12, var8 - 1, var12 + 1, var8 + 1 + this.fontRendererInstance.FONT_HEIGHT, -3092272);
                }
                else {
                    this.fontRendererInstance.drawString("_", var12, var8, var1);
                }
            }
            if (var3 != var2) {
                final int var13 = var7 + this.fontRendererInstance.getStringWidth(var4.substring(0, var3));
                this.drawCursorVertical(var12, var8 - 1, var13 - 1, var8 + 1 + this.fontRendererInstance.FONT_HEIGHT);
            }
        }
    }
    
    private void drawCursorVertical(int p_146188_1_, int p_146188_2_, int p_146188_3_, int p_146188_4_) {
        if (p_146188_1_ < p_146188_3_) {
            final int var5 = p_146188_1_;
            p_146188_1_ = p_146188_3_;
            p_146188_3_ = var5;
        }
        if (p_146188_2_ < p_146188_4_) {
            final int var5 = p_146188_2_;
            p_146188_2_ = p_146188_4_;
            p_146188_4_ = var5;
        }
        if (p_146188_3_ > this.xPosition + this.width) {
            p_146188_3_ = this.xPosition + this.width;
        }
        if (p_146188_1_ > this.xPosition + this.width) {
            p_146188_1_ = this.xPosition + this.width;
        }
        final Tessellator var6 = Tessellator.getInstance();
        final WorldRenderer var7 = var6.getWorldRenderer();
        GlStateManager.color(0.0f, 0.0f, 255.0f, 255.0f);
        GlStateManager.disableTexture2D();
        GlStateManager.enableColorLogic();
        GlStateManager.colorLogicOp(5387);
        var7.startDrawingQuads();
        var7.addVertex(p_146188_1_, p_146188_4_, 0.0);
        var7.addVertex(p_146188_3_, p_146188_4_, 0.0);
        var7.addVertex(p_146188_3_, p_146188_2_, 0.0);
        var7.addVertex(p_146188_1_, p_146188_2_, 0.0);
        var6.draw();
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
        final int var2 = this.text.length();
        if (p_146199_1_ > var2) {
            p_146199_1_ = var2;
        }
        if (p_146199_1_ < 0) {
            p_146199_1_ = 0;
        }
        this.selectionEnd = p_146199_1_;
        if (this.fontRendererInstance != null) {
            if (this.lineScrollOffset > var2) {
                this.lineScrollOffset = var2;
            }
            final int var3 = this.getWidth();
            final String var4 = this.fontRendererInstance.trimStringToWidth(this.text.substring(this.lineScrollOffset), var3);
            final int var5 = var4.length() + this.lineScrollOffset;
            if (p_146199_1_ == this.lineScrollOffset) {
                this.lineScrollOffset -= this.fontRendererInstance.trimStringToWidth(this.text, var3, true).length();
            }
            if (p_146199_1_ > var5) {
                this.lineScrollOffset += p_146199_1_ - var5;
            }
            else if (p_146199_1_ <= this.lineScrollOffset) {
                this.lineScrollOffset -= this.lineScrollOffset - p_146199_1_;
            }
            this.lineScrollOffset = MathHelper.clamp_int(this.lineScrollOffset, 0, var2);
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
