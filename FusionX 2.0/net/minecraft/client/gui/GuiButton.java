package net.minecraft.client.gui;

import java.awt.Color;

import me.CheerioFX.FusionX.module.modules.GhostClient;
import me.CheerioFX.FusionX.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiButton extends Gui {
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
	private double hoverStage;

	public GuiButton(int buttonId, int x, int y, String buttonText) {
		this(buttonId, x, y, 200, 20, buttonText);
	}

	public GuiButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
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
	}

	/**
	 * Returns 0 if the button is disabled, 1 if the mouse is NOT hovering over
	 * this button and 2 if it IS hovering over this button.
	 */
	protected int getHoverState(boolean mouseOver) {
		byte var2 = 1;

		if (!this.enabled) {
			var2 = 0;
		} else if (mouseOver) {
			var2 = 2;
		}

		return var2;
	}

	/**
	 * Draws this button to the screen.
	 */
	public void drawButton(final Minecraft mc, final int mouseX, final int mouseY) {
		if (this.visible) {
			if (GhostClient.enabled) {
				final FontRenderer var4 = mc.fontRendererObj;
				mc.getTextureManager().bindTexture(GuiButton.buttonTextures);
				GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
				this.hovered = (mouseX >= this.xPosition && mouseY >= this.yPosition
						&& mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height);
				final int var5 = this.getHoverState(this.hovered);
				GlStateManager.enableBlend();
				GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
				GlStateManager.blendFunc(770, 771);
				this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, 46 + var5 * 20, this.width / 2,
						this.height);
				this.drawTexturedModalRect(this.xPosition + this.width / 2, this.yPosition, 200 - this.width / 2,
						46 + var5 * 20, this.width / 2, this.height);
				this.mouseDragged(mc, mouseX, mouseY);
				int var6 = 14737632;
				if (!this.enabled) {
					var6 = 10526880;
				} else if (this.hovered) {
					var6 = 16777120;
				}
				this.drawCenteredString(var4, this.displayString, this.xPosition + this.width / 2,
						this.yPosition + (this.height - 8) / 2, var6);
			} else {
				final FontRenderer var4 = mc.fontRendererObj;
				GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
				this.hovered = (mouseX >= this.xPosition && mouseY >= this.yPosition
						&& mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height);
				final int var5 = this.getHoverState(this.hovered);
				GlStateManager.enableBlend();
				GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
				GlStateManager.blendFunc(770, 771);
				final int theColor = new Color(255, 220, 30, 0).hashCode();
				RenderUtils.drawRect((int) (this.xPosition - this.hoverStage),
						(int) (this.yPosition - this.hoverStage) + 1,
						(int) (this.xPosition + this.width + this.hoverStage),
						(int) (this.yPosition - this.hoverStage) + 3, theColor);
				RenderUtils.drawRect((int) (this.xPosition - this.hoverStage),
						(int) (this.yPosition + this.height + this.hoverStage) - 1,
						(int) (this.xPosition + this.width + this.hoverStage),
						(int) (this.yPosition + this.height + this.hoverStage) - 3, theColor);
				RenderUtils.drawRect((int) (this.xPosition - this.hoverStage),
						(int) (this.yPosition - this.hoverStage) + 1, (int) (this.xPosition - this.hoverStage) - 1,
						(int) (this.yPosition + this.height + this.hoverStage) - 1, theColor);
				RenderUtils.drawRect((int) (this.xPosition + this.width + this.hoverStage) + 1,
						(int) (this.yPosition - this.hoverStage) + 1,
						(int) (this.xPosition + this.width + this.hoverStage),
						(int) (this.yPosition + this.height + this.hoverStage) - 1, theColor);
				RenderUtils.drawBorderRect(this.xPosition - this.hoverStage, this.yPosition - this.hoverStage + 2.0,
						this.xPosition + this.width + this.hoverStage,
						this.yPosition + this.height + this.hoverStage - 2.0, -1, Integer.MIN_VALUE, 0);
				if (this.isMouseOver() && this.enabled) {
					if (this.hoverStage < 1.0) {
						this.hoverStage += 0.75;
					}
				} else if (this.hoverStage > 0.0) {
					this.hoverStage -= 0.75;
				}
				this.mouseDragged(mc, mouseX, mouseY);
				int var7 = 14737632;
				if (!this.enabled) {
					var7 = 10526880;
				} else if (this.hovered) {
					var7 = 16777120;
				}
				this.drawCenteredString(var4, this.displayString, this.xPosition + this.width / 2,
						this.yPosition + (this.height - 8) / 2, var7);
			}
		}
	}

	/**
	 * Fired when the mouse button is dragged. Equivalent of
	 * MouseListener.mouseDragged(MouseEvent e).
	 */
	protected void mouseDragged(Minecraft mc, int mouseX, int mouseY) {
	}

	/**
	 * Fired when the mouse button is released. Equivalent of
	 * MouseListener.mouseReleased(MouseEvent e).
	 */
	public void mouseReleased(int mouseX, int mouseY) {
	}

	/**
	 * Returns true if the mouse has been pressed on this control. Equivalent of
	 * MouseListener.mousePressed(MouseEvent e).
	 */
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
		return this.enabled && this.visible && mouseX >= this.xPosition && mouseY >= this.yPosition
				&& mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
	}

	/**
	 * Whether the mouse cursor is currently over the button.
	 */
	public boolean isMouseOver() {
		return this.hovered;
	}

	public void drawButtonForegroundLayer(int mouseX, int mouseY) {
	}

	public void playPressSound(SoundHandler soundHandlerIn) {
		soundHandlerIn.playSound(
				PositionedSoundRecord.createPositionedSoundRecord(new ResourceLocation("gui.button.press"), 1.0F));
	}

	public int getButtonWidth() {
		return this.width;
	}

	public void func_175211_a(int p_175211_1_) {
		this.width = p_175211_1_;
	}
}
