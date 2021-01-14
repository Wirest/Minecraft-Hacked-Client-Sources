package net.minecraft.client.gui;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import com.ibm.icu.text.UFormat;

import de.iotacb.client.Client;
import de.iotacb.client.module.modules.render.HUD;
import de.iotacb.client.utilities.render.Render2D;
import de.iotacb.client.utilities.render.animations.AnimationUtil;
import de.iotacb.client.utilities.render.animations.easings.Quint;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class GuiButton extends Gui
{
    protected static final ResourceLocation buttonTextures = new ResourceLocation("textures/gui/widgets.png");

    /** Button width in pixels */
    protected double width;

    /** Button height in pixels */
    protected double height;

    /** The x position of this control. */
    public double xPosition;

    /** The y position of this control. */
    public double yPosition;

    /** The string displayed on this control. */
    public String displayString;
    public int id;

    /** True if this control is enabled, false to disable. */
    public boolean enabled;

    /** Hides the button completely if false. */
    public boolean visible;
    protected boolean hovered;
    
    AnimationUtil animUtil;
    
    private Color baseColor;
    
    public GuiButton(int buttonId, double x, double y, String buttonText)
    {
        this(buttonId, x, y, 200, 20, buttonText);
        this.animUtil = new AnimationUtil(Quint.class);
        this.animUtil.addProgression(0).setValue(0);
        this.animUtil.addProgression(1).setValue(1);
        this.baseColor = new Color(0, 0, 0, 200);
    }

    public GuiButton(int buttonId, double x, double y, double widthIn, double heightIn, String buttonText)
    {
    	this.animUtil = new AnimationUtil(Quint.class);
    	this.animUtil.addProgression(0).setValue(0);
    	this.animUtil.addProgression(1).setValue(1);
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
        this.baseColor = new Color(0, 0, 0, 200);
    }
    
    public GuiButton(int buttonId, double x, double y, String buttonText, boolean enabled)
    {
        this(buttonId, x, y, 200, 20, buttonText);
        this.animUtil = new AnimationUtil(Quint.class);
        this.animUtil.addProgression(0).setValue(0);
        this.animUtil.addProgression(1).setValue(1);
        this.enabled = enabled;
        this.baseColor = new Color(0, 0, 0, 200);
    }

    public GuiButton(int buttonId, double x, double y, double widthIn, double heightIn, String buttonText, boolean enabled)
    {
    	this.animUtil = new AnimationUtil(Quint.class);
    	this.animUtil.addProgression(0).setValue(0);
    	this.animUtil.addProgression(1).setValue(1);
        this.width = 200;
        this.height = 20;
        this.visible = true;
        this.id = buttonId;
        this.xPosition = x;
        this.yPosition = y;
        this.width = widthIn;
        this.height = heightIn;
        this.displayString = buttonText;
        this.enabled = enabled;
        this.baseColor = new Color(0, 0, 0, 200);
    }

    /**
     * Returns 0 if the button is disabled, 1 if the mouse is NOT hovering over this button and 2 if it IS hovering over
     * this button.
     */
    protected int getHoverState(boolean mouseOver)
    {
        int i = 1;

        if (!this.enabled)
        {
            i = 0;
        }
        else if (mouseOver)
        {
            i = 2;
        }

        return i;
    }
    
    double pennerX;

    /**
     * Draws this button to the screen.
     */
    public void drawButton(Minecraft mc, int mouseX, int mouseY)
    {
    	this.hovered = (mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height) && enabled;
    	if (hovered) {
    		animUtil.getProgression(1).setValue(0);
    		pennerX = animUtil.easeOut(0, 0, 1, .3);
    	} else {
    		animUtil.getProgression(0).setValue(0);
    		pennerX = 1 - animUtil.easeOut(1, 0, 1, .3);
    	}
    	pennerX = MathHelper.clamp_double(pennerX, 0, 1);
    	if (Client.INSTANCE.getModuleManager().getModuleByClass(HUD.class).getValueByName("HUDKöftespieß").getBooleanValue()) {
    		if (!enabled) GL11.glColor4f(1F, 1F, 1F, .5F);
    		Client.RENDER2D.imageCentered(new ResourceLocation("client/textures/koefte.png"), xPosition + width / 2, yPosition + height / 2, width - pennerX, height - (pennerX * .8));
    	} else {
    		Client.BLUR_UTIL.blur(xPosition, yPosition, width, height, 5);
    		Client.RENDER2D.rectCentered(xPosition + width / 2, yPosition + height / 2, width, height, hovered ? baseColor.brighter() : enabled ? baseColor : new Color(200, 50, 50));
    		Client.RENDER2D.rect(xPosition + (width / 2 * (1 - pennerX)), yPosition + height - 1, width * pennerX, 1, Client.INSTANCE.getClientColor());
    	}
    	Client.INSTANCE.getFontManager().getDefaultFont().drawStringWithShadow(displayString, xPosition + width / 2 - Client.INSTANCE.getFontManager().getDefaultFont().getWidth(displayString) / 2, yPosition + height / 2 - Client.INSTANCE.getFontManager().getDefaultFont().getHeight(displayString) / 2, Color.white);
    	this.mouseDragged(mc, mouseX, mouseY);
    }

    /**
     * Fired when the mouse button is dragged. Equivalent of MouseListener.mouseDragged(MouseEvent e).
     */
    protected void mouseDragged(Minecraft mc, int mouseX, int mouseY)
    {
    }

    /**
     * Fired when the mouse button is released. Equivalent of MouseListener.mouseReleased(MouseEvent e).
     */
    public void mouseReleased(int mouseX, int mouseY)
    {
    }

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

    public void drawButtonForegroundLayer(int mouseX, int mouseY)
    {
    }

    public void playPressSound(SoundHandler soundHandlerIn)
    {
        soundHandlerIn.playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
    }

    public double getButtonWidth()
    {
        return this.width;
    }

    public void setWidth(double width)
    {
        this.width = width;
    }
    
    public void setDisplayString(String displayString) {
		this.displayString = displayString;
	}
    
    public GuiButton setBaseColor(Color baseColor) {
		this.baseColor = baseColor;
		return this;
	}

}
