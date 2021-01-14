package net.minecraft.client.gui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.ChatAllowedCharacters;

import org.lwjgl.opengl.GL11;

public class PasswordField extends Gui {
    /**
     * Have the font renderer from GuiScreen to render the textbox text into the
     * screen.
     */
    private final FontRenderer fontRenderer;
    private final int xPos;
    private final int yPos;
    /**
     * The width of this text field.
     */
    private final int width;
    private final int height;
    /**
     * Have the current text beign edited on the textbox.
     */
    private String text = "";
    private int maxStringLength = 32;
    private int cursorCounter;
    private boolean enableBackgroundDrawing = true;
    /**
     * if true the textbox can lose focus by clicking elsewhere on the screen
     */
    private boolean canLoseFocus = true;
    /**
     * If this value is true along isEnabled, keyTyped will process the keys.
     */
    public boolean isFocused = false;
    /**
     * If this value is true along isFocused, keyTyped will process the keys.
     */
    private final boolean isEnabled = true;
    private int field_73816_n = 0;
    private int cursorPosition = 0;
    /**
     * other selection position, maybe the same as the cursor
     */
    private int selectionEnd = 0;
    private int enabledColor = 14737632;
    private final int disabledColor = 7368816;
    private boolean visible = true;

    public PasswordField(FontRenderer par1FontRenderer, int par2, int par3, int par4, int par5) {
        this.fontRenderer = par1FontRenderer;
        this.xPos = par2;
        this.yPos = par3;
        this.width = par4;
        this.height = par5;
    }

    /**
     * Increments the cursor counter
     */
    public void updateCursorCounter() {
        ++this.cursorCounter;
    }

    /**
     * Sets the text of the textbox.
     */
    public void setText(String par1Str) {
        if (par1Str.length() > this.maxStringLength) {
            this.text = par1Str.substring(0, this.maxStringLength);
        } else {
            this.text = par1Str;
        }
        this.setCursorPositionEnd();
    }

    /**
     * Returns the text beign edited on the textbox.
     */
    public String getText() {
        return this.text;
    }

    /**
     * @return returns the text between the cursor and selectionEnd
     */
    public String getSelectedtext() {
        final int var1 = this.cursorPosition < this.selectionEnd ? this.cursorPosition : this.selectionEnd;
        final int var2 = this.cursorPosition < this.selectionEnd ? this.selectionEnd : this.cursorPosition;
        return this.text.substring(var1, var2);
    }

    /**
     * replaces selected text, or inserts text at the position on the cursor
     */
    public void writeText(String par1Str) {
        String var2 = "";
        final String var3 = ChatAllowedCharacters.filterAllowedCharacters(par1Str);
        final int var4 = this.cursorPosition < this.selectionEnd ? this.cursorPosition : this.selectionEnd;
        final int var5 = this.cursorPosition < this.selectionEnd ? this.selectionEnd : this.cursorPosition;
        final int var6 = this.maxStringLength - this.text.length() - (var4 - this.selectionEnd);
        final boolean var7 = false;
        if (this.text.length() > 0) {
            var2 = var2 + this.text.substring(0, var4);
        }
        int var8;
        if (var6 < var3.length()) {
            var2 = var2 + var3.substring(0, var6);
            var8 = var6;
        } else {
            var2 = var2 + var3;
            var8 = var3.length();
        }
        if (this.text.length() > 0 && var5 < this.text.length()) {
            var2 = var2 + this.text.substring(var5);
        }
        this.text = var2;
        this.func_73784_d(var4 - this.selectionEnd + var8);
    }

    public void func_73779_a(int par1) {
        if (this.text.length() != 0) {
            if (this.selectionEnd != this.cursorPosition) {
                this.writeText("");
            } else {
                this.deleteFromCursor(this.getNthWordFromCursor(par1) - this.cursorPosition);
            }
        }
    }

    /**
     * delete the selected text, otherwsie deletes characters from either side
     * of the cursor. params: delete num
     */
    public void deleteFromCursor(int par1) {
        if (this.text.length() != 0) {
            if (this.selectionEnd != this.cursorPosition) {
                this.writeText("");
            } else {
                final boolean var2 = par1 < 0;
                final int var3 = var2 ? this.cursorPosition + par1 : this.cursorPosition;
                final int var4 = var2 ? this.cursorPosition : this.cursorPosition + par1;
                String var5 = "";
                if (var3 >= 0) {
                    var5 = this.text.substring(0, var3);
                }
                if (var4 < this.text.length()) {
                    var5 = var5 + this.text.substring(var4);
                }
                this.text = var5;
                if (var2) {
                    this.func_73784_d(par1);
                }
            }
        }
    }

    /**
     * see @getNthNextWordFromPos() params: N, position
     */
    public int getNthWordFromCursor(int par1) {
        return this.getNthWordFromPos(par1, this.getCursorPosition());
    }

    /**
     * gets the position of the nth word. N may be negative, then it looks
     * backwards. params: N, position
     */
    public int getNthWordFromPos(int par1, int par2) {
        return this.func_73798_a(par1, this.getCursorPosition(), true);
    }

    public int func_73798_a(int par1, int par2, boolean par3) {
        int var4 = par2;
        final boolean var5 = par1 < 0;
        final int var6 = Math.abs(par1);
        for (int var7 = 0; var7 < var6; ++var7) {
            if (var5) {
                while (par3 && var4 > 0 && this.text.charAt(var4 - 1) == 32) {
                    --var4;
                }
                while (var4 > 0 && this.text.charAt(var4 - 1) != 32) {
                    --var4;
                }
            } else {
                final int var8 = this.text.length();
                var4 = this.text.indexOf(32, var4);
                if (var4 == -1) {
                    var4 = var8;
                } else {
                    while (par3 && var4 < var8 && this.text.charAt(var4) == 32) {
                        ++var4;
                    }
                }
            }
        }
        return var4;
    }

    public void func_73784_d(int par1) {
        this.setCursorPosition(this.selectionEnd + par1);
    }

    /**
     * sets the position of the cursor to the provided index
     */
    public void setCursorPosition(int par1) {
        this.cursorPosition = par1;
        final int var2 = this.text.length();
        if (this.cursorPosition < 0) {
            this.cursorPosition = 0;
        }
        if (this.cursorPosition > var2) {
            this.cursorPosition = var2;
        }
        this.func_73800_i(this.cursorPosition);
    }

    /**
     * sets the cursors position to the beginning
     */
    public void setCursorPositionZero() {
        this.setCursorPosition(0);
    }

    /**
     * sets the cursors position to after the text
     */
    public void setCursorPositionEnd() {
        this.setCursorPosition(this.text.length());
    }

    /**
     * Call this method from you GuiScreen to process the keys into textbox.
     */
    public boolean textboxKeyTyped(char par1, int par2) {
        if (this.isEnabled && this.isFocused) {
            switch (par1) {
                case 1:
                    this.setCursorPositionEnd();
                    this.func_73800_i(0);
                    return true;
                case 3:
                    GuiScreen.setClipboardString(this.getSelectedtext());
                    return true;
                case 22:
                    this.writeText(GuiScreen.getClipboardString());
                    return true;
                case 24:
                    GuiScreen.setClipboardString(this.getSelectedtext());
                    this.writeText("");
                    return true;
                default:
                    switch (par2) {
                        case 14:
                            if (GuiScreen.isCtrlKeyDown()) {
                                this.func_73779_a(-1);
                            } else {
                                this.deleteFromCursor(-1);
                            }
                            return true;
                        case 199:
                            if (GuiScreen.isShiftKeyDown()) {
                                this.func_73800_i(0);
                            } else {
                                this.setCursorPositionZero();
                            }
                            return true;
                        case 203:
                            if (GuiScreen.isShiftKeyDown()) {
                                if (GuiScreen.isCtrlKeyDown()) {
                                    this.func_73800_i(this.getNthWordFromPos(-1, this.getSelectionEnd()));
                                } else {
                                    this.func_73800_i(this.getSelectionEnd() - 1);
                                }
                            } else if (GuiScreen.isCtrlKeyDown()) {
                                this.setCursorPosition(this.getNthWordFromCursor(-1));
                            } else {
                                this.func_73784_d(-1);
                            }
                            return true;
                        case 205:
                            if (GuiScreen.isShiftKeyDown()) {
                                if (GuiScreen.isCtrlKeyDown()) {
                                    this.func_73800_i(this.getNthWordFromPos(1, this.getSelectionEnd()));
                                } else {
                                    this.func_73800_i(this.getSelectionEnd() + 1);
                                }
                            } else if (GuiScreen.isCtrlKeyDown()) {
                                this.setCursorPosition(this.getNthWordFromCursor(1));
                            } else {
                                this.func_73784_d(1);
                            }
                            return true;
                        case 207:
                            if (GuiScreen.isShiftKeyDown()) {
                                this.func_73800_i(this.text.length());
                            } else {
                                this.setCursorPositionEnd();
                            }
                            return true;
                        case 211:
                            if (GuiScreen.isCtrlKeyDown()) {
                                this.func_73779_a(1);
                            } else {
                                this.deleteFromCursor(1);
                            }
                            return true;
                        default:
                            if (ChatAllowedCharacters.isAllowedCharacter(par1)) {
                                this.writeText(Character.toString(par1));
                                return true;
                            } else {
                                return false;
                            }
                    }
            }
        } else {
            return false;
        }
    }

    /**
     * Args: x, y, buttonClicked
     */
    public void mouseClicked(int par1, int par2, int par3) {
        final boolean var4 = par1 >= this.xPos && par1 < this.xPos + this.width && par2 >= this.yPos && par2 < this.yPos + this.height;
        if (this.canLoseFocus) {
            this.setFocused(this.isEnabled && var4);
        }
        if (this.isFocused && par3 == 0) {
            int var5 = par1 - this.xPos;
            if (this.enableBackgroundDrawing) {
                var5 -= 4;
            }
            final String var6 = this.fontRenderer.trimStringToWidth(this.text.substring(this.field_73816_n), this.getWidth());
            this.setCursorPosition(this.fontRenderer.trimStringToWidth(var6, var5).length() + this.field_73816_n);
        }
    }

    /**
     * Draws the textbox
     */
    public void drawTextBox() {
        if (this.func_73778_q()) {
            if (this.getEnableBackgroundDrawing()) {
                drawRect(this.xPos - 1, this.yPos - 1, this.xPos + this.width + 1, this.yPos + this.height + 1, -6250336);
                drawRect(this.xPos, this.yPos, this.xPos + this.width, this.yPos + this.height, -16777216);
            }
            final int var1 = this.isEnabled ? this.enabledColor : this.disabledColor;
            final int var2 = this.cursorPosition - this.field_73816_n;
            int var3 = this.selectionEnd - this.field_73816_n;
            final String var4 = this.fontRenderer.trimStringToWidth(this.text.substring(this.field_73816_n), this.getWidth());
            final boolean var5 = var2 >= 0 && var2 <= var4.length();
            final boolean var6 = this.isFocused && this.cursorCounter / 6 % 2 == 0 && var5;
            final int var7 = this.enableBackgroundDrawing ? this.xPos + 4 : this.xPos;
            final int var8 = this.enableBackgroundDrawing ? this.yPos + (this.height - 8) / 2 : this.yPos;
            int var9 = var7;
            if (var3 > var4.length()) {
                var3 = var4.length();
            }
            if (var4.length() > 0) {
                final String var10 = var5 ? var4.substring(0, var2) : var4;
                var9 = fontRenderer.drawStringWithShadow(var10.replaceAll("(?s).", "*"), var7, var8, var1);
            }
            final boolean var13 = this.cursorPosition < this.text.length() || this.text.length() >= this.getMaxStringLength();
            int var11 = var9;
            if (!var5) {
                var11 = var2 > 0 ? var7 + this.width : var7;
            } else if (var13) {
                var11 = var9 - 1;
                --var9;
            }
            if (var4.length() > 0 && var5 && var2 < var4.length()) {
                this.fontRenderer.drawStringWithShadow(var4.substring(var2).replaceAll("(?s).", "*"), var9, var8, var1);
            }
            if (var6) {
                if (var13) {
                    Gui.drawRect(var11, var8 - 1, var11 + 1, var8 + 1 + this.fontRenderer.FONT_HEIGHT, -3092272);
                } else {
                    this.fontRenderer.drawStringWithShadow("_", var11, var8, var1);
                }
            }
            if (var3 != var2) {
                final int var12 = var7 + this.fontRenderer.getStringWidth(var4.substring(0, var3));
                this.drawCursorVertical(var11, var8 - 1, var12 - 1, var8 + 1 + this.fontRenderer.FONT_HEIGHT);
            }
        }
    }

    /**
     * draws the vertical line cursor in the textbox
     */
    private void drawCursorVertical(int par1, int par2, int par3, int par4) {
        int var5;
        if (par1 < par3) {
            var5 = par1;
            par1 = par3;
            par3 = var5;
        }
        if (par2 < par4) {
            var5 = par2;
            par2 = par4;
            par4 = var5;
        }
        final Tessellator var6 = Tessellator.getInstance();
        GL11.glColor4f(0.0F, 0.0F, 255.0F, 255.0F);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_COLOR_LOGIC_OP);
        GL11.glLogicOp(GL11.GL_OR_REVERSE);
        WorldRenderer var9 = var6.getWorldRenderer();
        var9.startDrawingQuads();
        var9.addVertex(par1, par4, 0.0D);
        var9.addVertex(par3, par4, 0.0D);
        var9.addVertex(par3, par2, 0.0D);
        var9.addVertex(par1, par2, 0.0D);
        var9.draw();
        GL11.glDisable(GL11.GL_COLOR_LOGIC_OP);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    public void setMaxStringLength(int par1) {
        this.maxStringLength = par1;
        if (this.text.length() > par1) {
            this.text = this.text.substring(0, par1);
        }
    }

    /**
     * returns the maximum number of character that can be contained in this
     * textbox
     */
    public int getMaxStringLength() {
        return this.maxStringLength;
    }

    /**
     * returns the current position of the cursor
     */
    public int getCursorPosition() {
        return this.cursorPosition;
    }

    /**
     * get enable drawing background and outline
     */
    public boolean getEnableBackgroundDrawing() {
        return this.enableBackgroundDrawing;
    }

    /**
     * enable drawing background and outline
     */
    public void setEnableBackgroundDrawing(boolean par1) {
        this.enableBackgroundDrawing = par1;
    }

    public void func_73794_g(int par1) {
        this.enabledColor = par1;
    }

    /**
     * setter for the focused field
     */
    public void setFocused(boolean par1) {
        if (par1 && !this.isFocused) {
            this.cursorCounter = 0;
        }
        this.isFocused = par1;
    }

    /**
     * getter for the focused field
     */
    public boolean isFocused() {
        return this.isFocused;
    }

    /**
     * the side of the selection that is not the cursor, maye be the same as the
     * cursor
     */
    public int getSelectionEnd() {
        return this.selectionEnd;
    }

    /**
     * returns the width of the textbox depending on if the the box is enabled
     */
    public int getWidth() {
        return this.getEnableBackgroundDrawing() ? this.width - 8 : this.width;
    }

    public void func_73800_i(int par1) {
        final int var2 = this.text.length();
        if (par1 > var2) {
            par1 = var2;
        }
        if (par1 < 0) {
            par1 = 0;
        }
        this.selectionEnd = par1;
        if (this.fontRenderer != null) {
            if (this.field_73816_n > var2) {
                this.field_73816_n = var2;
            }
            final int var3 = this.getWidth();
            final String var4 = this.fontRenderer.trimStringToWidth(this.text.substring(this.field_73816_n), var3);
            final int var5 = var4.length() + this.field_73816_n;
            if (par1 == this.field_73816_n) {
                this.field_73816_n -= this.fontRenderer.trimStringToWidth(this.text, var3, true).length();
            }
            if (par1 > var5) {
                this.field_73816_n += par1 - var5;
            } else if (par1 <= this.field_73816_n) {
                this.field_73816_n -= this.field_73816_n - par1;
            }
            if (this.field_73816_n < 0) {
                this.field_73816_n = 0;
            }
            if (this.field_73816_n > var2) {
                this.field_73816_n = var2;
            }
        }
    }

    /**
     * if true the textbox can lose focus by clicking elsewhere on the screen
     */
    public void setCanLoseFocus(boolean par1) {
        this.canLoseFocus = par1;
    }

    public boolean func_73778_q() {
        return this.visible;
    }

    public void setVisible(boolean par1) {
        this.visible = par1;
    }
}