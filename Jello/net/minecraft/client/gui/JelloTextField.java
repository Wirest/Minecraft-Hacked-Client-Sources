package net.minecraft.client.gui;

import java.awt.Color;

import org.lwjgl.input.Keyboard;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.mentalfrostbyte.jello.jelloclickgui.JelloGui;
import com.mentalfrostbyte.jello.main.Jello;
import com.mentalfrostbyte.jello.util.FontUtil;
import com.mentalfrostbyte.jello.util.Stencil;
import com.mentalfrostbyte.jello.util.TimerUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.MathHelper;

public class JelloTextField extends GuiTextField
{
    private final int field_175208_g;
    private final FontRenderer fontRendererInstance;
    public float xPosition;
    public float yPosition;

    /** The width of this text field. */
    private final float width;
    private final float height;

    /** Has the current text being edited on the textbox. */
    private String text = "";
    private int maxStringLength = 32;
    private int cursorCounter;
    private boolean enableBackgroundDrawing = true;

    /**
     * if true the textbox can lose focus by clicking elsewhere on the screen
     */
    private boolean canLoseFocus = true;

    /**
     * If this value is true along with isEnabled, keyTyped will process the keys.
     */
    private boolean isFocused;

    /**
     * If this value is true along with isFocused, keyTyped will process the keys.
     */
    private boolean isEnabled = true;

    /**
     * The current character index that should be used as start of the rendered text.
     */
    private int lineScrollOffset;
    
    private float offset;
    private float lastOffset;
    
    private int cursorPosition;

    /** other selection position, maybe the same as the cursor */
    private int selectionEnd;
    private int enabledColor = 14737632;
    private int disabledColor = 7368816;

    /** True if this textbox is visible */
    private boolean visible = true;
    private GuiPageButtonList.GuiResponder field_175210_x;
    private Predicate field_175209_y = Predicates.alwaysTrue();
    
    public boolean dragging;
    public int startChar;
    public int endChar;
    
    public TimerUtil backspaceTime = new TimerUtil();
    
    public String placeholder = "";
    public boolean isPassword;
    

    public JelloTextField(int p_i45542_1_, FontRenderer p_i45542_2_, float f, float g, int p_i45542_5_, int p_i45542_6_)
    {
    	super(p_i45542_1_, p_i45542_2_, (int)f, (int)g, (int)p_i45542_5_, (int)p_i45542_6_);
        this.field_175208_g = p_i45542_1_;
        this.fontRendererInstance = p_i45542_2_;
        this.xPosition = f;
        this.yPosition = g;
        this.width = p_i45542_5_;
        this.height = p_i45542_6_;
    }

    public void setPlaceholder(String s) {
    	placeholder = s;
    }
    
    public void func_175207_a(GuiPageButtonList.GuiResponder p_175207_1_)
    {
        this.field_175210_x = p_175207_1_;
    }

    /**
     * Increments the cursor counter
     */
    public void updateCursorCounter()
    {
        ++this.cursorCounter;
    }

    /**
     * Sets the text of the textbox
     */
    public void setText(String p_146180_1_)
    {
        if (this.field_175209_y.apply(p_146180_1_))
        {
            if (p_146180_1_.length() > this.maxStringLength)
            {
                this.text = p_146180_1_.substring(0, this.maxStringLength);
            }
            else
            {
                this.text = p_146180_1_;
            }

            this.setCursorPositionEnd();
        }
    }

    /**
     * Returns the contents of the textbox
     */
    public String getText()
    {
        return this.text;
    }

    /**
     * returns the text between the cursor and selectionEnd
     */
    public String getSelectedText()
    {
        int var1 = this.cursorPosition < this.selectionEnd ? this.cursorPosition : this.selectionEnd;
        int var2 = this.cursorPosition < this.selectionEnd ? this.selectionEnd : this.cursorPosition;
        return this.text.substring(var1, var2);
    }

    public void func_175205_a(Predicate p_175205_1_)
    {
        this.field_175209_y = p_175205_1_;
    }

    /**
     * replaces selected text, or inserts text at the position on the cursor
     */
    public void writeText(String p_146191_1_)
    {
        String var2 = "";
        String var3 = ChatAllowedCharacters.filterAllowedCharacters(p_146191_1_);
        int var4 = this.cursorPosition < this.selectionEnd ? this.cursorPosition : this.selectionEnd;
        int var5 = this.cursorPosition < this.selectionEnd ? this.selectionEnd : this.cursorPosition;
        int var6 = this.maxStringLength - this.text.length() - (var4 - var5);
        boolean var7 = false;

        if (this.text.length() > 0)
        {
            var2 = var2 + this.text.substring(0, var4);
        }

        int var8;

        if (var6 < var3.length())
        {
            var2 = var2 + var3.substring(0, var6);
            var8 = var6;
        }
        else
        {
            var2 = var2 + var3;
            var8 = var3.length();
        }

        if (this.text.length() > 0 && var5 < this.text.length())
        {
            var2 = var2 + this.text.substring(var5);
        }

        if (this.field_175209_y.apply(var2))
        {
            this.text = var2;
            													//TODO DELETE?
            this.moveCursorBy(var4 - this.selectionEnd + var8/* - 1*/);

            if (this.field_175210_x != null)
            {
                this.field_175210_x.func_175319_a(this.field_175208_g, this.text);
            }
        }
    }

    /**
     * Deletes the specified number of words starting at the cursor position. Negative numbers will delete words left of
     * the cursor.
     */
    public void deleteWords(int p_146177_1_)
    {
        if (this.text.length() != 0)
        {
            if (this.selectionEnd != this.cursorPosition)
            {
                this.writeText("");
            }
            else
            {
                this.deleteFromCursor(this.getNthWordFromCursor(p_146177_1_) - this.cursorPosition);
            }
        }
    }

    /**
     * delete the selected text, otherwsie deletes characters from either side of the cursor. params: delete num
     */
    public void deleteFromCursor(int p_146175_1_)
    {
        if (this.text.length() != 0)
        {
            if (this.selectionEnd != this.cursorPosition)
            {
                this.writeText("");
            }
            else
            {
                boolean var2 = p_146175_1_ < 0;
                int var3 = var2 ? this.cursorPosition + p_146175_1_ : this.cursorPosition;
                int var4 = var2 ? this.cursorPosition : this.cursorPosition + p_146175_1_;
                String var5 = "";

                if (var3 >= 0)
                {
                    var5 = this.text.substring(0, var3);
                }

                if (var4 < this.text.length())
                {
                    var5 = var5 + this.text.substring(var4);
                }

                this.text = var5;

                if (var2)
                {
                    this.moveCursorBy(p_146175_1_);
                }

                if (this.field_175210_x != null)
                {
                    this.field_175210_x.func_175319_a(this.field_175208_g, this.text);
                }
            }
        }
    }

    public int func_175206_d()
    {
        return this.field_175208_g;
    }

    /**
     * see @getNthNextWordFromPos() params: N, position
     */
    public int getNthWordFromCursor(int p_146187_1_)
    {
        return this.getNthWordFromPos(p_146187_1_, this.getCursorPosition());
    }

    /**
     * gets the position of the nth word. N may be negative, then it looks backwards. params: N, position
     */
    public int getNthWordFromPos(int p_146183_1_, int p_146183_2_)
    {
        return this.func_146197_a(p_146183_1_, p_146183_2_, true);
    }

    public int func_146197_a(int p_146197_1_, int p_146197_2_, boolean p_146197_3_)
    {
        int var4 = p_146197_2_;
        boolean var5 = p_146197_1_ < 0;
        int var6 = Math.abs(p_146197_1_);

        for (int var7 = 0; var7 < var6; ++var7)
        {
            if (var5)
            {
                while (p_146197_3_ && var4 > 0 && this.text.charAt(var4 - 1) == 32)
                {
                    --var4;
                }

                while (var4 > 0 && this.text.charAt(var4 - 1) != 32)
                {
                    --var4;
                }
            }
            else
            {
                int var8 = this.text.length();
                var4 = this.text.indexOf(32, var4);

                if (var4 == -1)
                {
                    var4 = var8;
                }
                else
                {
                    while (p_146197_3_ && var4 < var8 && this.text.charAt(var4) == 32)
                    {
                        ++var4;
                    }
                }
            }
        }

        return var4;
    }

    /**
     * Moves the text cursor by a specified number of characters and clears the selection
     */
    public void moveCursorBy(int p_146182_1_)
    {
        this.setCursorPosition(this.selectionEnd + p_146182_1_);
    }

    /**
     * sets the position of the cursor to the provided index
     */
    public void setCursorPosition(int p_146190_1_)
    {
        this.cursorPosition = p_146190_1_;
        startChar = p_146190_1_;
        endChar = p_146190_1_;
        int var2 = this.text.length();
        this.cursorPosition = MathHelper.clamp_int(this.cursorPosition, 0, var2);
        this.setSelectionPos(this.cursorPosition);
    }

    /**
     * sets the cursors position to the beginning
     */
    public void setCursorPositionZero()
    {
        this.setCursorPosition(0);
    }

    /**
     * sets the cursors position to after the text
     */
    public void setCursorPositionEnd()
    {
        this.setCursorPosition(this.text.length());
        startChar = 0;
        endChar = text.length();
    }

    /**
     * Call this method from your GuiScreen to process the keys into the textbox
     */
    public boolean textboxKeyTyped(char p_146201_1_, int p_146201_2_)
    {
        if (!this.isFocused)
        {
            return false;
        }
        else if (GuiScreen.isControlA(p_146201_2_))
        {
            this.setCursorPositionEnd();
            this.setSelectionPos(0);
            return true;
        }
        else if (GuiScreen.isControlC(p_146201_2_))
        {
            GuiScreen.setClipboardString(this.getSelectedText());
            return true;
        }
        else if (GuiScreen.isControlV(p_146201_2_))
        {
            if (this.isEnabled)
            {
                this.writeText(GuiScreen.getClipboardString());
            }

            return true;
        }
        else if (GuiScreen.isControlX(p_146201_2_))
        {
            GuiScreen.setClipboardString(this.getSelectedText());

            if (this.isEnabled)
            {
                this.writeText("");
            }

            return true;
        }
        else
        {
            switch (p_146201_2_)
            {
                case 14:
                    if (GuiScreen.isCtrlKeyDown())
                    {
                        if (this.isEnabled)
                        {
                            this.deleteWords(-1);
                        }
                    }
                    else if (this.isEnabled)
                    {
                        this.deleteFromCursor(-1);
                    }

                    return true;

                case 199:
                    if (GuiScreen.isShiftKeyDown())
                    {
                        this.setSelectionPos(0);
                    }
                    else
                    {
                        this.setCursorPositionZero();
                    }

                    return true;

                case 203:
                    if (GuiScreen.isShiftKeyDown())
                    {
                        if (GuiScreen.isCtrlKeyDown())
                        {
                            this.setSelectionPos(this.getNthWordFromPos(-1, this.getSelectionEnd()));
                        }
                        else
                        {
                            this.setSelectionPos(this.getSelectionEnd() - 1);
                        }
                    }
                    else if (GuiScreen.isCtrlKeyDown())
                    {
                        this.setCursorPosition(this.getNthWordFromCursor(-1));
                    }
                    else
                    {
                        this.moveCursorBy(-1);
                    }

                    return true;

                case 205:
                    if (GuiScreen.isShiftKeyDown())
                    {
                        if (GuiScreen.isCtrlKeyDown())
                        {
                            this.setSelectionPos(this.getNthWordFromPos(1, this.getSelectionEnd()));
                        }
                        else
                        {
                            this.setSelectionPos(this.getSelectionEnd() + 1);
                        }
                    }
                    else if (GuiScreen.isCtrlKeyDown())
                    {
                        this.setCursorPosition(this.getNthWordFromCursor(1));
                    }
                    else
                    {
                        this.moveCursorBy(1);
                    }

                    return true;

                case 207:
                    if (GuiScreen.isShiftKeyDown())
                    {
                        this.setSelectionPos(this.text.length());
                    }
                    else
                    {
                        this.setCursorPositionEnd();
                    }

                    return true;

                case 211:
                    if (GuiScreen.isCtrlKeyDown())
                    {
                        if (this.isEnabled)
                        {
                            this.deleteWords(1);
                        }
                    }
                    else if (this.isEnabled)
                    {
                        this.deleteFromCursor(1);
                    }

                    return true;

                default:
                    if (ChatAllowedCharacters.isAllowedCharacter(p_146201_1_))
                    {
                        if (this.isEnabled)
                        {
                            this.writeText(Character.toString(p_146201_1_));
                        }

                        return true;
                    }
                    else
                    {
                        return false;
                    }
            }
        }
    }
    
    public void onTick() {
    	lastOffset = offset;
    	String missing = this.text.substring(0, (int)(this.lineScrollOffset));
        offset += (((isPassword ? FontUtil.getTextFieldFont(isPassword).getPasswordWidth(missing) : FontUtil.getTextFieldFont(isPassword).getStringWidth(missing)/*-(FontUtil.getTextFieldFont(isPassword).getStringWidth(missing) > this.getWidth()-30 ? 18 : 0)*/)-offset)/(2))+0.01;
        
        if(Keyboard.isKeyDown(14)) {
        	if(this.getText().length() > 0 && backspaceTime.hasTimeElapsed(500, false))
        	this.setText(this.getText().substring(0, this.getText().length()-1));
        }else {
        	backspaceTime.reset();
        }
    }
    
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
    	 dragging = false;
    }
    
    /**
     * Args: x, y, buttonClicked
     */
    public void mouseClicked(int mouseX, int mouseY, int mouseButton)
    {
        boolean var4 = mouseX >= this.xPosition && mouseX < this.xPosition + this.width && mouseY >= this.yPosition && mouseY < this.yPosition + this.height;

        if (this.canLoseFocus)
        {
        	if(!var4){
        		float var5 = mouseX - this.xPosition;

            if (this.enableBackgroundDrawing)
            {
                var5 += isPassword ? 1 : 4 ;
            }
            if(isPassword){
            String var6 = FontUtil.getTextFieldFont(isPassword).trimStringToWidthPassword(this.text.substring(this.lineScrollOffset), this.getWidth(), false);
            this.setCursorPosition(FontUtil.getTextFieldFont(isPassword).trimStringToWidthPassword(var6, (int) var5, false).length() + this.lineScrollOffset);
            startChar = FontUtil.getTextFieldFont(isPassword).trimStringToWidthPassword(var6, (int) var5, false).length() + this.lineScrollOffset;
            }else{
            	 String var6 = FontUtil.getTextFieldFont(isPassword).trimStringToWidth(this.text.substring(this.lineScrollOffset), this.getWidth(), false);
                 this.setCursorPosition(FontUtil.getTextFieldFont(isPassword).trimStringToWidth(var6, (int) var5, false).length() + this.lineScrollOffset);
                 startChar = FontUtil.getTextFieldFont(isPassword).trimStringToWidth(var6, (int) var5, false).length() + this.lineScrollOffset;
                }
            dragging = false;
            lineScrollOffset = 0;
        	}
            this.setFocused(var4);
        }

        if (this.isFocused && var4 && mouseButton == 0)
        {
        	float var5 = mouseX - this.xPosition;

            if (this.enableBackgroundDrawing)
            {
                var5 += isPassword ? 1 : 4 ;
            }

            if(isPassword){
            String var6 = FontUtil.getTextFieldFont(isPassword).trimStringToWidthPassword(this.text.substring(this.lineScrollOffset), this.getWidth(), false);
            this.setCursorPosition(FontUtil.getTextFieldFont(isPassword).trimStringToWidthPassword(var6, (int) var5, false).length() + this.lineScrollOffset);
            startChar = FontUtil.getTextFieldFont(isPassword).trimStringToWidthPassword(var6, (int) var5, false).length() + this.lineScrollOffset;
            }else{
            	String var6 = FontUtil.getTextFieldFont(isPassword).trimStringToWidth(this.text.substring(this.lineScrollOffset), this.getWidth(), false);
                this.setCursorPosition(FontUtil.getTextFieldFont(isPassword).trimStringToWidth(var6, (int) var5, false).length() + this.lineScrollOffset);
                startChar = FontUtil.getTextFieldFont(isPassword).trimStringToWidth(var6, (int) var5, false).length() + this.lineScrollOffset;
            }
            dragging = true;
        }
    }
    
    public int applyAlpha(int color, float alpha){
    	float f = (color >> 24 & 0xFF) / 255.0f;
    	Color c = new Color(color);
    	Color c2 = new Color(c.getRed()/255f, c.getGreen()/255f, c.getBlue()/255f, (f)*alpha);
    	return c2.getRGB();
    }
    
    public float smoothTrans(double current, double last){
		return (float) (current * Minecraft.getMinecraft().timer.renderPartialTicks + (last * (1.0f - Minecraft.getMinecraft().timer.renderPartialTicks)));
	}
    /**
     * Draws the textbox
     */
    public void drawTextBox(int mouseX, int mouseY)
    {
        if (this.getVisible())
        {
        	float percent = smoothTrans(Jello.jgui.percent, Jello.jgui.lastPercent);
    		float percent2 = smoothTrans(Jello.jgui.percent2, Jello.jgui.lastPercent2);
    		float outro = smoothTrans(Jello.jgui.outro, Jello.jgui.lastOutro);
    		
    		float opacity = (float) (Minecraft.getMinecraft().currentScreen == null ? Math.min(1, Math.max(((1-(outro-1)-.4)*1.66667), 0)) : (float)Math.max(Math.min(-(((percent-1.23)*4)), 1), 0));
    		
        	boolean isInClickGUI = Minecraft.getMinecraft().currentScreen instanceof JelloGui || Minecraft.getMinecraft().currentScreen == null;
        	float offse = (float) (offset * Minecraft.getMinecraft().timer.elapsedPartialTicks + (lastOffset * (1.0f - Minecraft.getMinecraft().timer.elapsedPartialTicks)));
			
        	float realOpacity = isInClickGUI ? opacity : 1;
        	
        	if (this.getEnableBackgroundDrawing())
            {
                GlStateManager.disableBlend();
            	 
             	
            	GlStateManager.enableBlend();
            	drawFloatRect(this.xPosition - 1, this.yPosition + this.height + (isInClickGUI ? 7f : 0) , this.xPosition + this.width + 1, this.yPosition + this.height + 1 + (isInClickGUI ? 7f : 0) , applyAlpha(isFocused ? 0xffcac9ca : isInClickGUI ? 0x90ffffff : 0xffe5e4e5, realOpacity));
            }
            
        	if(dragging && mouseX >= xPosition+width) {
        		//if(this.getSelectionEnd() < 14)
        		 this.setSelectionPos(this.getSelectionEnd() + 1);
        	}

            if (this.isFocused && dragging)
            {
                int var5 =(int)(mouseX - this.xPosition);

                if (this.enableBackgroundDrawing)
                {
                    var5 += 4;
                }

                String var6 = FontUtil.getTextFieldFont(isPassword).trimStringToWidth(this.text.substring(this.lineScrollOffset), this.getWidth());//this.fontRendererInstance.trimStringToWidth(this.text.substring(this.lineScrollOffset), this.getWidth(), false, true);
                endChar = FontUtil.getTextFieldFont(isPassword).trimStringToWidth(var6, var5).length() + this.lineScrollOffset;//this.fontRendererInstance.trimStringToWidth(var6, var5, false, true).length() + this.lineScrollOffset;
                this.setSelectionPos(endChar);
            }
            
            int var1 = applyAlpha(this.isEnabled ? Minecraft.getMinecraft().currentScreen instanceof GuiChat ? 0xffffffff: isFocused ? isInClickGUI ? -1:0xff000000 : isInClickGUI ? 0x90ffffff : 0xff828182 : Minecraft.getMinecraft().currentScreen instanceof GuiChat ? 0xfffffffe:0xff000001, realOpacity);
            int var2 = this.cursorPosition - this.lineScrollOffset;
            int var3 = this.selectionEnd - this.lineScrollOffset;
            

            String var4 = text;//this.fontRendererInstance.trimStringToWidth(this.text.substring(this.lineScrollOffset), this.getWidth(), false, true);
            String missing = this.text.substring(0, (int)(this.lineScrollOffset));
            boolean var5 = var2 >= 0 && var2 <= var4.length();
            boolean var6 = this.isFocused && this.cursorCounter / 6 % 2 == 0 && var5;
            float var7 = this.enableBackgroundDrawing ? this.xPosition + 4 : this.xPosition;
            float var8 = this.enableBackgroundDrawing ? this.yPosition + (this.height - 8) / 2 : this.yPosition;
            float var9 = var7;

            if (var3 > var4.length())
            {
                var3 = var4.length();
            }

            if(!isFocused && isInClickGUI){
            }else{
            Stencil.write(false);
            Gui.drawFloatRect(xPosition-1, yPosition, xPosition+this.width+1, yPosition+this.height, -1);
            Stencil.erase(true);
            }
            
            if(this.getText().isEmpty() && !placeholder.isEmpty() && !this.isFocused) {
            	FontUtil.getTextFieldFont(isPassword).drawNoBSString(placeholder, var7 - 3.5f, var8 - 1.5f, applyAlpha(isInClickGUI ? 0x90ffffff : 0xff8d8b8d, realOpacity));
            }
            
            if (var4.length() > 0)
            {
                String var10 = var5 ? var4.substring(0, var2) : var4;
                GlStateManager.color(1, 1, 1,1);
                var9 = 0;//FontUtil.getTextFieldFont(isPassword).drawString(var10, (float)var7 - 3.5f, (float)var8, -1);
                GlStateManager.color(1, 1, 1,1);
            }

            boolean var13 = this.cursorPosition < this.text.length() || this.text.length() >= this.getMaxStringLength();
            float var11 = var9;

            if (!var5)
            {
                var11 = var2 > 0 ? var7 + this.width : var7;
            }
            else if (var13)
            {
                var11 = var9 - 1;
                --var9;
            }
            boolean highlighting = false;
            if (var3 != var2)
            {
            	 GlStateManager.color(1, 1, 1,1);
                double var12 = var7 + FontUtil.getTextFieldFont(isPassword).getStringWidth(var4.substring(0, var3)) - 3.5f;
                
                int lowestChar = startChar > endChar ? endChar : startChar;
                int highestChar = startChar < endChar ? endChar : startChar;
                
                if((4 + xPosition + FontUtil.getTextFieldFont(isPassword).getStringWidth(var4.substring(0, lowestChar)) - offse - 3.5f) - (4 + xPosition + FontUtil.getTextFieldFont(isPassword).getStringWidth(var4.substring(0, lowestChar)) - offse + FontUtil.getTextFieldFont(isPassword).getStringWidth(var4.substring(lowestChar, highestChar)) - 3.5f) != 0){
                	highlighting = true;
                }
                
                Gui.drawFloatRect(4 + xPosition + FontUtil.getTextFieldFont(isPassword).getStringWidth(var4.substring(0, lowestChar)) - offse - 3.5f, yPosition + 2 - 5 + 4, 4 + xPosition + FontUtil.getTextFieldFont(isPassword).getStringWidth(var4.substring(0, lowestChar)) - offse + FontUtil.getTextFieldFont(isPassword).getStringWidth(var4.substring(lowestChar, highestChar)) - 3.5f, yPosition + height-1.5f - 2, applyAlpha(0xffadcffe, realOpacity));
                GlStateManager.color(1, 1, 1,1);
            }
            
            
            //DRAW OVERLAY STRING
            if (var4.length() > 0)
            {
                String var10 = var5 ? var4.substring(0, var2) : var4;
                GlStateManager.color(1, 1, 1,1);
                var9 = FontUtil.getTextFieldFont(isPassword).drawNoBSString(var4, (float)var7 - offse - 3.5f, (float)var8 +( isInClickGUI ? 0:- 1.5f), applyAlpha(var1, realOpacity));
                GlStateManager.color(1, 1, 1,1);
            }
            if (var6)
            {
                if (var13)
                {
                	
                	String sub = "";
                	int alpha = (int) Math.min(255, ((System.currentTimeMillis()/3 % 255) > 255/2 ? (Math.abs(Math.abs(System.currentTimeMillis()/3) % 255 - 255)) : System.currentTimeMillis()/3 % 255)*2);
                	
                	if(startChar > 0)
                		sub = var4.substring(0, (startChar));
                	if(!highlighting){
                		
                	if(startChar > endChar)
                		Gui.drawFloatRect(xPosition+FontUtil.getTextFieldFont(isPassword).getStringWidth(sub)+3.5f-offse - 3.5f, var8 - 5, xPosition+FontUtil.getTextFieldFont(isPassword).getStringWidth(sub) + 0.5f+3.5f-offse - 3.5f, var8 + 1 + FontUtil.getTextFieldFont(isPassword).getHeight() - 2, alpha > 255/2f ? 0xffcdcbcd : isInClickGUI ? -1 : 0xff000000);
                	else
                		Gui.drawFloatRect(xPosition+FontUtil.getTextFieldFont(isPassword).getStringWidth(sub)+4.0f-offse - 3.5f, var8 - 5, xPosition+FontUtil.getTextFieldFont(isPassword).getStringWidth(sub) + +4.5f-offse - 3.5f, var8 + 1 + FontUtil.getTextFieldFont(isPassword).getHeight() - 2, alpha > 255/2f ? 0xffcdcbcd : isInClickGUI ? -1 : 0xff000000);
                		
                	}
                }
                else
                {
                	GlStateManager.color(1, 1, 1,1);
                	int alpha = (int) Math.min(255, ((System.currentTimeMillis()/3 % 255) > 255/2 ? (255) : 255/2f));
                	if(!highlighting){
                	Gui.drawFloatRect(xPosition+FontUtil.getTextFieldFont(isPassword).getStringWidth(var4)+4.5f-offse - 4f, var8 - 5, xPosition+FontUtil.getTextFieldFont(isPassword).getStringWidth(var4) + 0.5f+4.5f-offse - 4f, var8 + 1 + FontUtil.getTextFieldFont(isPassword).getHeight() - 2, alpha > 255/2f ? isInClickGUI ? 0xff909090 : 0xffcdcbcd : isInClickGUI ? -1 : 0xff000000);
                	}
                	 GlStateManager.color(1, 1, 1,1);
                }
            }
            if(!isFocused && isInClickGUI){
            
            }else{
            Stencil.dispose();
            }
            
            
        }
    }
    
    
    public void drawPasswordBox(int mouseX, int mouseY)
    {
        if (this.getVisible())
        {
        	float offse = (float) (offset * Minecraft.getMinecraft().timer.elapsedPartialTicks + (lastOffset * (1.0f - Minecraft.getMinecraft().timer.elapsedPartialTicks)));
        	String var4 = text;//text.replaceAll(".", ".");
        	
        	if (this.getEnableBackgroundDrawing())
            {
                GlStateManager.disableBlend();
            	 
             	
            	GlStateManager.enableBlend();
            	drawFloatRect(this.xPosition - 1, this.yPosition + this.height , this.xPosition + this.width + 1, this.yPosition + this.height + 1, isFocused ? 0xffcac9ca : 0xffe5e4e5);
                // drawRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, -16777216);
            }
            
        	if(dragging && mouseX >= xPosition+width) {
        		//if(this.getSelectionEnd() < 14)
        		 this.setSelectionPos(this.getSelectionEnd() + 1);
        	}

            if (this.isFocused && dragging)
            {
                int var5 =(int)(mouseX - this.xPosition);

                if (this.enableBackgroundDrawing)
                {
                    var5 += 1;
                }

                String var6 = FontUtil.getTextFieldFont(true).trimStringToWidthPassword(var4.substring(this.lineScrollOffset), this.getWidth(), false);//this.fontRendererInstance.trimStringToWidth(this.text.substring(this.lineScrollOffset), this.getWidth(), false, true);
                //this.setCursorPosition(this.fontRendererInstance.trimStringToWidth(var6, var5).length() + this.lineScrollOffset);
                endChar = FontUtil.getTextFieldFont(true).trimStringToWidthPassword(var6, var5, false).length() + this.lineScrollOffset;//this.fontRendererInstance.trimStringToWidth(var6, var5, false, true).length() + this.lineScrollOffset;
                this.setSelectionPos(endChar);
            }
            
            int var1 = this.isEnabled ? Minecraft.getMinecraft().currentScreen instanceof GuiChat ? 0xffffffff: isFocused ? 0xff000000 : 0xff828182 : Minecraft.getMinecraft().currentScreen instanceof GuiChat ? 0xfffffffe:0xff000001;
            int var2 = this.cursorPosition - this.lineScrollOffset;
            int var3 = this.selectionEnd - this.lineScrollOffset;
            

            //this.fontRendererInstance.trimStringToWidth(this.text.substring(this.lineScrollOffset), this.getWidth(), false, true);
            String missing = var4.substring(0, (int)(this.lineScrollOffset));
            boolean var5 = var2 >= 0 && var2 <= var4.length();
            boolean var6 = this.isFocused && this.cursorCounter / 6 % 2 == 0 && var5;
            float var7 = this.enableBackgroundDrawing ? this.xPosition + 4 : this.xPosition;
            float var8 = this.enableBackgroundDrawing ? this.yPosition + (this.height - 8) / 2 : this.yPosition;
            float var9 = var7;

            if (var3 > var4.length())
            {
                var3 = var4.length();
            }

            
            Stencil.write(false);
            Gui.drawFloatRect(xPosition-1, yPosition, xPosition+this.width+1, yPosition+this.height, -1);
            Stencil.erase(true);
            
            if(this.getText().isEmpty() && !placeholder.isEmpty() && !this.isFocused) {
            	FontUtil.jelloFontAddAlt.drawString(placeholder, var7 - 3.5f, var8 - 1.5f, 0xff8d8b8d);
            }
            
            if (var4.length() > 0)
            {
                String var10 = var5 ? var4.substring(0, var2) : var4;
                GlStateManager.color(1, 1, 1,1);
                var9 = FontUtil.getTextFieldFont(true).drawPassword(var10, (float)var7 - 3.5f, (float)var8, -1);
                GlStateManager.color(1, 1, 1,1);
            }

            boolean var13 = this.cursorPosition < var4.length() || var4.length() >= this.getMaxStringLength();
            float var11 = var9;

            if (!var5)
            {
                var11 = var2 > 0 ? var7 + this.width : var7;
            }
            else if (var13)
            {
                var11 = var9 - 1;
                --var9;
            }
            boolean highlighting = false;
            if (var3 != var2)
            {
            	 GlStateManager.color(1, 1, 1,1);
                double var12 = var7 + FontUtil.getTextFieldFont(true).getPasswordWidth(var4.substring(0, var3)) - 3.5f;
                //this.drawCursorVertical( startChar > endChar ? var11+ offse:var11+2 - offse, var8 - 1, startChar > endChar ? var12+ offse:var12-1 - offse, var8 + 1 + this.fontRendererInstance.FONT_HEIGHT);
                
                int lowestChar = startChar > endChar ? endChar : startChar;
                int highestChar = startChar < endChar ? endChar : startChar;
                
                if((4 + xPosition + FontUtil.getTextFieldFont(true).getPasswordWidth(var4.substring(0, lowestChar)) - offse - 3.5f) - (4 + xPosition + FontUtil.getTextFieldFont(true).getPasswordWidth(var4.substring(0, lowestChar)) - offse + FontUtil.getTextFieldFont(true).getPasswordWidth(var4.substring(lowestChar, highestChar)) - 3.5f) != 0){
                	highlighting = true;
                }
                
               Gui.drawFloatRect(4 + xPosition + FontUtil.getTextFieldFont(true).getPasswordWidth(var4.substring(0, lowestChar)) - offse - 3.5f, yPosition + 2 - 5 + 4, 4 + xPosition + FontUtil.getTextFieldFont(true).getPasswordWidth(var4.substring(0, lowestChar)) - offse + FontUtil.getTextFieldFont(true).getPasswordWidth(var4.substring(lowestChar, highestChar)) - 3.5f, yPosition + height-1.5f - 2, 0xffadcffe);
                GlStateManager.color(1, 1, 1,1);
            }
            
            
            //DRAW OVERLAY STRING
            if (var4.length() > 0)
            {
                String var10 = var5 ? var4.substring(0, var2) : var4;
                GlStateManager.color(1, 1, 1,1);
                var9 = FontUtil.getTextFieldFont(true).drawPassword(var4, (float)var7 - offse - 3.5f, (float)var8 - 1.5f + 6 - 19/2f, var1);
                GlStateManager.color(1, 1, 1,1);
            }
            if (var6)
            {
                if (var13)
                {
                	String sub = "";
                	int alpha = (int) Math.min(255, ((System.currentTimeMillis()/3 % 255) > 255/2 ? (Math.abs(Math.abs(System.currentTimeMillis()/3) % 255 - 255)) : System.currentTimeMillis()/3 % 255)*2);
                	
                	if(startChar > 0)
                		sub = var4.substring(0, (startChar));
                	if(!highlighting){
                		
                	if(startChar > endChar)
                		Gui.drawFloatRect(xPosition+FontUtil.getTextFieldFont(true).getPasswordWidth(sub)+3.5f-offse - 3.5f, var8 - 5, xPosition+FontUtil.getTextFieldFont(true).getPasswordWidth(sub) + 0.5f+3.5f-offse - 3.5f, var8 + 1 + FontUtil.getTextFieldFont(true).getHeight() - 2, alpha > 255/2f ? 0xffcdcbcd : 0xff000000);
                	else
                		Gui.drawFloatRect(xPosition+FontUtil.getTextFieldFont(true).getPasswordWidth(sub)+4.0f-offse - 3.5f, var8 - 5, xPosition+FontUtil.getTextFieldFont(true).getPasswordWidth(sub) + +4.5f-offse - 3.5f, var8 + 1 + FontUtil.getTextFieldFont(true).getHeight() - 2, alpha > 255/2f ? 0xffcdcbcd : 0xff000000);
                		
                	}
                }
                else
                {
                	GlStateManager.color(1, 1, 1,1);
                	//if(!dragging)
                	int alpha = (int) Math.min(255, ((System.currentTimeMillis()/3 % 255) > 255/2 ? (255) : 255/2f));
                	if(!highlighting){
                	Gui.drawFloatRect(xPosition+FontUtil.getTextFieldFont(true).getPasswordWidth(var4)+4.5f-offse - 4f, var8 - 5, xPosition+FontUtil.getTextFieldFont(true).getPasswordWidth(var4) + 0.5f+4.5f-offse - 4f, var8 + 1 + FontUtil.getTextFieldFont(true).getHeight() - 2, alpha > 255/2f ? 0xffcdcbcd : 0xff000000);
                	}
                	 GlStateManager.color(1, 1, 1,1);
                }
            }
            Stencil.dispose();
            
        }
    }

    /**
     * draws the vertical line cursor in the textbox
     */
    private void drawCursorVertical(float f, float p_146188_2_, float g, float p_146188_4_)
    {
    	float var5;

        if (f < g)
        {
            var5 = f;
            f = g;
            g = var5;
        }

        if (p_146188_2_ < p_146188_4_)
        {
            var5 = p_146188_2_;
            p_146188_2_ = p_146188_4_;
            p_146188_4_ = var5;
        }

        if (g > this.xPosition + this.width)
        {
            g = this.xPosition + this.width;
        }

        if (f > this.xPosition + this.width)
        {
            f = this.xPosition + this.width;
        }

        Gui.drawFloatRect(f + 1.5f, p_146188_2_ + 0.5f, g + 0.5f, p_146188_4_ - 1, 0xffadcffe);
        
       /* Tessellator var7 = Tessellator.getInstance();
        WorldRenderer var6 = var7.getWorldRenderer();
        GlStateManager.color(0.0F, 0.0F, 255.0F, 255.0F);
        GlStateManager.disableTexture2D();
        GlStateManager.enableColorLogic();
        GlStateManager.colorLogicOp(5387);
        var6.startDrawingQuads();
        var6.addVertex((double)p_146188_1_, (double)p_146188_4_, 0.0D);
        var6.addVertex((double)p_146188_3_, (double)p_146188_4_, 0.0D);
        var6.addVertex((double)p_146188_3_, (double)p_146188_2_, 0.0D);
        var6.addVertex((double)p_146188_1_, (double)p_146188_2_, 0.0D);
        var7.draw();
        GlStateManager.disableColorLogic();
        GlStateManager.enableTexture2D();*/
    }

    public void setMaxStringLength(int p_146203_1_)
    {
        this.maxStringLength = p_146203_1_;

        if (this.text.length() > p_146203_1_)
        {
            this.text = this.text.substring(0, p_146203_1_);
        }
    }

    /**
     * returns the maximum number of character that can be contained in this textbox
     */
    public int getMaxStringLength()
    {
        return this.maxStringLength;
    }

    /**
     * returns the current position of the cursor
     */
    public int getCursorPosition()
    {
        return this.cursorPosition;
    }

    /**
     * get enable drawing background and outline
     */
    public boolean getEnableBackgroundDrawing()
    {
        return this.enableBackgroundDrawing;
    }

    /**
     * enable drawing background and outline
     */
    public void setEnableBackgroundDrawing(boolean p_146185_1_)
    {
        this.enableBackgroundDrawing = p_146185_1_;
    }

    /**
     * Sets the text colour for this textbox (disabled text will not use this colour)
     */
    public void setTextColor(int p_146193_1_)
    {
        this.enabledColor = p_146193_1_;
    }

    public void setDisabledTextColour(int p_146204_1_)
    {
        this.disabledColor = p_146204_1_;
    }

    /**
     * Sets focus to this gui element
     */
    public void setFocused(boolean p_146195_1_)
    {
        if (p_146195_1_ && !this.isFocused)
        {
            this.cursorCounter = 0;
        }

        this.isFocused = p_146195_1_;
    }

    /**
     * Getter for the focused field
     */
    public boolean isFocused()
    {
        return this.isFocused;
    }

    public void setEnabled(boolean p_146184_1_)
    {
        this.isEnabled = p_146184_1_;
    }

    /**
     * the side of the selection that is not the cursor, may be the same as the cursor
     */
    public int getSelectionEnd()
    {
        return this.selectionEnd;
    }

    /**
     * returns the width of the textbox depending on if background drawing is enabled
     */
    public int getWidth()
    {
        return (int) (this.getEnableBackgroundDrawing() ? this.width - 8 : this.width);
    }

    /**
     * Sets the position of the selection anchor (i.e. position the selection was started at)
     */
    public void setSelectionPos(int p_146199_1_)
    {
        int var2 = this.text.length();

        if (p_146199_1_ > var2)
        {
            p_146199_1_ = var2;
        }

        if (p_146199_1_ < 0)
        {
            p_146199_1_ = 0;
        }

        this.selectionEnd = p_146199_1_;

        if (this.fontRendererInstance != null)
        {
            if (this.lineScrollOffset > var2)
            {
                this.lineScrollOffset = var2;
            }

            float var3 = this.getWidth();
            String var4 = isPassword  ? FontUtil.getTextFieldFont(isPassword).trimStringToWidthPassword(this.text.substring(this.lineScrollOffset), (int) var3, false) : FontUtil.getTextFieldFont(isPassword).trimStringToWidth(this.text.substring(this.lineScrollOffset), (int) var3, false);

            float var5 = var4.length() + this.lineScrollOffset;

            if (p_146199_1_ == this.lineScrollOffset)
            {
                this.lineScrollOffset -= isPassword  ? FontUtil.getTextFieldFont(isPassword).trimStringToWidthPassword(this.text, (int) var3, true).length() : FontUtil.getTextFieldFont(isPassword).trimStringToWidth(this.text, (int) var3, true).length();
            }

            if (p_146199_1_ > var5)
            {
                this.lineScrollOffset += p_146199_1_ - var5;
            }
            else if (p_146199_1_ <= this.lineScrollOffset)
            {
                this.lineScrollOffset -= this.lineScrollOffset - p_146199_1_;
            }

            this.lineScrollOffset = MathHelper.clamp_int(this.lineScrollOffset, 0, var2);
        }
    }

    /**
     * if true the textbox can lose focus by clicking elsewhere on the screen
     */
    public void setCanLoseFocus(boolean p_146205_1_)
    {
        this.canLoseFocus = p_146205_1_;
    }

    /**
     * returns true if this textbox is visible
     */
    public boolean getVisible()
    {
        return this.visible;
    }

    /**
     * Sets whether or not this textbox is visible
     */
    public void setVisible(boolean p_146189_1_)
    {
        this.visible = p_146189_1_;
    }
}
