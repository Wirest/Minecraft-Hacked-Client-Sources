package net.minecraft.client.gui;

import java.awt.Color;

import org.m0jang.crystal.Font.Fonts;
import org.m0jang.crystal.GUI.click.RenderUtils;
import org.m0jang.crystal.Utils.AnimationTimer;
import org.m0jang.crystal.Utils.TimeHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiButton extends Gui{
	public AnimationTimer anim;
    protected static final ResourceLocation buttonTextures = new ResourceLocation("textures/gui/widgets.png");

    /** Button width in pixels */
    protected int width;

    /** Button height in pixels */
    protected int height;

    /** The x position of this control. */
    public int xPosition;

    /** The y position of this control. */
    public int yPosition;

    /** The string displayed on this control. */
    public String displayString;
    public int id;

    /** True if this control is enabled, false to disable. */
    public boolean enabled;

    /** Hides the button completely if false. */
    public boolean visible;
    protected boolean hovered;
    private static final String __OBFID = "CL_00000668";
    
    TimeHelper timer;
    
    public GuiButton(int buttonId, int x, int y, String buttonText)
    {
        this(buttonId, x, y, 200, 20, buttonText);
    }

    public GuiButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText)
    {
    	this.timer = new TimeHelper();
        this.width = 200;
        this.height = 20;
        this.enabled = true;
        this.visible = true;
        this.id = buttonId;
        this.xPosition = x;
        this.yPosition = y;
        this.width = widthIn;
        this.height = heightIn;
        this.displayString = buttonText;
        
        this.anim = new AnimationTimer(5);
    }

    /**
     * Returns 0 if the button is disabled, 1 if the mouse is NOT hovering over this button and 2 if it IS hovering over
     * this button.
     */
    protected int getHoverState(boolean mouseOver)
    {
        byte var2 = 1;

        if (!this.enabled)
        {
            var2 = 0;
        }
        else if (mouseOver)
        {
            var2 = 2;
        }

        return var2;
    }

    /**
     * Draws this button to the screen.
     */
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (this.visible) {
           int borderColor = (new Color(255, 255, 255)).getRGB();
           int borderColorDIsabled = (new Color(177, 177, 177)).getRGB();
           FontRenderer var4 = mc.fontRendererObj;
           mc.getTextureManager().bindTexture(buttonTextures);
           GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
           this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
           if (this.timer.hasPassed(20.0D)) {
              this.anim.update(this.hovered);
              this.timer.reset();
           }

           GlStateManager.enableBlend();
           GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
           GlStateManager.blendFunc(770, 771);
           this.mouseDragged(mc, mouseX, mouseY);
           int var6 = 14737632;
           int alpha = (int)(this.anim.getValue() * 100.0D);
           if (alpha < 1 || !this.enabled) {
              alpha = 1;
           }

           RenderUtils.drawBorderedRect((float)this.xPosition, (float)this.yPosition, (float)this.width, (float)this.height, 1.0F, (new Color(255, 255, 255, alpha)).getRGB(), this.enabled ? borderColor : borderColorDIsabled);
           Fonts.segoe18.drawCenteredString(this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, var6);
        }

     }

    /**
     * Fired when the mouse button is dragged. Equivalent of MouseListener.mouseDragged(MouseEvent e).
     */
    protected void mouseDragged(Minecraft mc, int mouseX, int mouseY) {}

    /**
     * Fired when the mouse button is released. Equivalent of MouseListener.mouseReleased(MouseEvent e).
     */
    public void mouseReleased(int mouseX, int mouseY) {}

    /**
     * Returns true if the mouse has been pressed on this control. Equivalent of MouseListener.mousePressed(MouseEvent
     * e).
     */
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
    {
        return this.enabled && this.visible && mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
    }

    /**
     * Whether the mouse cursor is currently over the button.
     */
    public boolean isMouseOver()
    {
        return this.hovered;
    }

    public void drawButtonForegroundLayer(int mouseX, int mouseY) {}

    public void playPressSound(SoundHandler soundHandlerIn)
    {
        soundHandlerIn.playSound(PositionedSoundRecord.createPositionedSoundRecord(new ResourceLocation("gui.button.press"), 1.0F));
    }

    public int getButtonWidth()
    {
        return this.width;
    }

    public void func_175211_a(int p_175211_1_)
    {
        this.width = p_175211_1_;
    }
}
