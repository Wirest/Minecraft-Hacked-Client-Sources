package store.shadowclient.client.gui.login.elements.buttons;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import store.shadowclient.client.Shadow;
import store.shadowclient.client.utils.gui.AnimationUtil;
import store.shadowclient.client.utils.gui.easings.Quint;
import store.shadowclient.client.utils.gui.easings.utilities.Progression;
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
		//this.animUtil = new AnimationUtil(Quint.class);
		this.animUtil = new AnimationUtil(Quint.class);
		this.progIn = new Progression();
		this.progOut = new Progression();
		this.startPosX = x;
		this.startPosY = y;
		this.scissor = scissor;
	}
	
	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY) {
		Shadow.RENDER2D.color(Color.white);
		updateButton(mouseX, mouseY);
		
		Shadow.RENDER2D.push();
		if (scissor) {
			GL11.glEnable(GL11.GL_SCISSOR_TEST);
			Shadow.RENDER2D.scissor(startPosX, startPosY + 1, width, height - 1);
		}
		Shadow.RENDER2D.translate(this.xPosition + this.width / 2, this.yPosition + this.height / 2);
		Shadow.RENDER2D.rect(-width / 2, -height / 2, width, height, new Color(0, 0, 0, 200));
		Shadow.RENDER2D.scale(1 + scaleFactor, 1 + scaleFactor);
		Shadow.RENDER2D.image(resourceLocation, -this.width / 2 + 10, -this.height / 2 + 10, this.width - 20, this.height - 20);
		if (scissor) GL11.glDisable(GL11.GL_SCISSOR_TEST);
		Shadow.RENDER2D.pop();
		if (!displayString.isEmpty())
			Shadow.fontManager.getFont("SFL 10").drawStringWithShadow(displayString, xPosition + width / 2 - Shadow.fontManager.getFont("SFL 10").getWidth(displayString) /  2, yPosition + height / 2 - Shadow.fontManager.getFont("SFL 10").getHeight(displayString) / 2, -1);
		if (scissor) Shadow.RENDER2D.outlineInlinedGradientRect(startPosX, startPosY, width, height, 5, Color.black, new Color(0, 0, 0, 0));
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
