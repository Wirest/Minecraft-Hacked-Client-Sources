package me.rigamortis.faurax.login.alts;

import net.minecraft.client.gui.*;
import net.minecraft.util.*;

public class AltPassField extends Gui
{
    private final FontRenderer fontRenderer;
    private final int xPos;
    private final int yPos;
    private final int width;
    private final int height;
    private String text;
    private int maxStringLength;
    private int cursorCounter;
    public boolean isFocused;
    public boolean isEnabled;
    private GuiScreen parentGuiScreen;
    
    public AltPassField(final GuiScreen var1, final FontRenderer var2, final int var3, final int var4, final int var5, final int var6, final String var7) {
        this.isFocused = false;
        this.isEnabled = true;
        this.parentGuiScreen = var1;
        this.fontRenderer = var2;
        this.xPos = var3;
        this.yPos = var4;
        this.width = var5;
        this.height = var6;
        this.setText(var7);
    }
    
    public void setText(final String var1) {
        this.text = var1;
    }
    
    public String getText() {
        return this.text;
    }
    
    public void updateCursorCounter() {
        ++this.cursorCounter;
    }
    
    public void textboxKeyTyped(final char var1, final int var2) {
        if (this.isEnabled && this.isFocused) {
            if (var1 == '\t') {
                this.parentGuiScreen.confirmClicked(this.isEnabled, var2);
            }
            if (var1 == '\u0016') {
                String var3 = GuiScreen.getClipboardString();
                if (var3 == null) {
                    var3 = "";
                }
                int var4 = 32 - this.text.length();
                if (var4 > var3.length()) {
                    var4 = var3.length();
                }
                if (var4 > 0) {
                    this.text = String.valueOf(this.text) + var3.substring(0, var4);
                }
            }
            if (var2 == 14 && this.text.length() > 0) {
                this.text = this.text.substring(0, this.text.length() - 1);
            }
            if ((ChatAllowedCharacters.allowedCharactersArray.toString().indexOf(var1) >= 0 || var1 > ' ') && this.text.length() < 35 && var1 != "`".charAt(0)) {
                this.text = String.valueOf(this.text) + var1;
            }
        }
    }
    
    public void mouseClicked(final int var1, final int var2, final int var3) {
        final boolean var4 = this.isEnabled && var1 >= this.xPos && var1 < this.xPos + this.width && var2 >= this.yPos && var2 < this.yPos + this.height;
        this.setFocused(var4);
    }
    
    public void setFocused(final boolean var1) {
        if (var1 && !this.isFocused) {
            this.cursorCounter = 0;
        }
        this.isFocused = var1;
    }
    
    public void drawTextBox() {
        Gui.drawRect(this.xPos - 1, this.yPos - 1, this.xPos + this.width + 1, this.yPos + this.height + 1, -6250336);
        Gui.drawRect(this.xPos, this.yPos, this.xPos + this.width, this.yPos + this.height, -16777216);
        final String s = this.text.replaceAll(".", "*");
        if (this.isEnabled) {
            final boolean var1 = this.isFocused && this.cursorCounter / 6 % 2 == 0;
            this.drawString(this.fontRenderer, String.valueOf(s) + (var1 ? "_" : ""), this.xPos + 4, this.yPos + (this.height - 8) / 2, 14737632);
        }
        else {
            this.drawString(this.fontRenderer, s, this.xPos + 4, this.yPos + (this.height - 8) / 2, 7368816);
        }
    }
    
    public void setMaxStringLength(final int var1) {
        this.maxStringLength = var1;
    }
}
