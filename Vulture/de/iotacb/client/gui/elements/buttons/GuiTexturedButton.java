package de.iotacb.client.gui.elements.buttons;

import java.awt.Color;
import java.util.Random;

import org.lwjgl.opengl.GL11;

import com.sun.jna.platform.win32.WinDef.HWND;

//import de.iotacb.client.utilities.render.EasingUtils;
import de.iotacb.client.utilities.render.Render2D;
import de.iotacb.client.Client;
import de.iotacb.client.utilities.render.DeltaUtil;
import de.iotacb.client.utilities.render.animations.AnimationUtil;
import de.iotacb.client.utilities.render.animations.easings.Elastic;
import de.iotacb.client.utilities.render.animations.easings.Expo;
import de.iotacb.client.utilities.render.animations.easings.Quint;
import de.iotacb.client.utilities.render.animations.easings.utilities.Progression;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class GuiTexturedButton extends GuiButton {
	
	private ResourceLocation resourceLocation;
	
	private double scaleFactor;
	
	private AnimationUtil animUtil;
	private Progression progIn, progOut;
	
	private double startPosX, startPosY;
	
	private boolean scissor;

	public GuiTexturedButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText, String imageLocation) {
		super(buttonId, x, y, widthIn, heightIn, buttonText);
		init(imageLocation, x, y, false);
	}
	
	public GuiTexturedButton(int buttonId, int x, int y, int widthIn, int heightIn, String imageLocation) {
		super(buttonId, x, y, widthIn, heightIn, "");
		init(imageLocation, x, y, false);
	}
	
	public GuiTexturedButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText, String imageLocation, boolean scissor) {
		super(buttonId, x, y, widthIn, heightIn, buttonText);
		init(imageLocation, x, y, scissor);
	}
	
	public GuiTexturedButton(int buttonId, int x, int y, int widthIn, int heightIn, String imageLocation, boolean scissor) {
		super(buttonId, x, y, widthIn, heightIn, "");
		init(imageLocation, x, y, scissor);
	}
	
	private void init(String imageLocation, double x, double y, boolean scissor) {
		this.resourceLocation = new ResourceLocation(imageLocation);
		this.animUtil = new AnimationUtil(Quint.class);
		this.progIn = new Progression();
		this.progOut = new Progression();
		this.startPosX = x;
		this.startPosY = y;
		this.scissor = scissor;
	}
	
	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY) {
		Client.RENDER2D.color(Color.white);
		updateButton(mouseX, mouseY);
		
		Client.RENDER2D.push();
		if (scissor) {
			GL11.glEnable(GL11.GL_SCISSOR_TEST);
			Client.RENDER2D.scissor(startPosX, startPosY + 1, width, height - 1);
		}
		Client.RENDER2D.translate(this.xPosition + this.width / 2, this.yPosition + this.height / 2);
		Client.RENDER2D.rect(-width / 2, -height / 2, width, height, new Color(0, 0, 0, 200));
		Client.RENDER2D.scale(1 + scaleFactor, 1 + scaleFactor);
		Client.RENDER2D.image(resourceLocation, -this.width / 2 + 10, -this.height / 2 + 10, this.width - 20, this.height - 20);
		if (scissor) GL11.glDisable(GL11.GL_SCISSOR_TEST);
		Client.RENDER2D.pop();
		if (!displayString.isEmpty())
			Client.INSTANCE.getFontManager().getBigFont().drawStringWithShadow(displayString, xPosition + width / 2 - Client.INSTANCE.getFontManager().getBigFont().getWidth(displayString) /  2, yPosition + height / 2 - Client.INSTANCE.getFontManager().getBigFont().getHeight(displayString) / 2, Color.white);
		if (scissor) Client.RENDER2D.outlineInlinedGradientRect(startPosX, startPosY, width, height, 5, Color.black, new Color(0, 0, 0, 0));
	}
	
	public final void updateButton(int mouseX, int mouseY) {
		this.hovered = (mouseX > xPosition && mouseX < xPosition + width) && (mouseY > yPosition && mouseY < yPosition + height);
		if (this.hovered) {
			progOut.setValue(0);
			scaleFactor = animUtil.easeOut(progIn, 0, .2F, .5);
		} else {
			progIn.setValue(0);
			scaleFactor = .2F - animUtil.easeOut(progOut, 0, .2F, .5);
		}
		this.scaleFactor = MathHelper.clamp_double(scaleFactor, 0, .2F);
	}
	
	public final double getStartPosX() {
		return startPosX;
	}
	
	public final double getStartPosY() {
		return startPosY;
	}

}
