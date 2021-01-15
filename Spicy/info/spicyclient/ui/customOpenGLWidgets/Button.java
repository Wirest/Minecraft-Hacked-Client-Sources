package info.spicyclient.ui.customOpenGLWidgets;

import java.io.IOException;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ChatAllowedCharacters;

public class Button {
	
	public Button(float left, float bottom, float right, float up, int outlineColor, int insideColor, int textColor, float outlineThickness, GuiScreen screen) {
		this.left = left;
		this.bottom = bottom;
		this.right = right;
		this.up = up;
		this.outlineColor = outlineColor;
		this.insideColor = insideColor;
		this.textColor = textColor;
		this.outlineThickness = outlineThickness;
		this.screen = screen;
	}
	
	public float left = 0, bottom = 0, right = 0, up = 0 , outlineThickness = 2;
	public int outlineColor = -1, insideColor = 0xff000000, textColor = -1;
	public GuiScreen screen = null;
	public String text = null;
	public float textScale = 1;
	
	private long blinker = System.currentTimeMillis();
	
	public void draw() {
		
		GlStateManager.pushMatrix();
		screen.drawRect(left, bottom, right, up, outlineColor);
		screen.drawRect(left + outlineThickness, bottom - outlineThickness, right - outlineThickness, up + outlineThickness, insideColor);
		GlStateManager.popMatrix();
		
		GlStateManager.pushMatrix();
		GlStateManager.scale(textScale, textScale, 1);
		
		screen.drawCenteredString(Minecraft.getMinecraft().fontRendererObj, text, (((right - left) / 2) + left) / textScale, (((bottom - up) / 2) + up - 6) / textScale, textColor);
		
		GlStateManager.popMatrix();
		
	}
	
	public void addChar(String string) {
		if (text == null) {
			text = string;
		}else {
			text = text + string;
		}
	}
	
	public void clearText() {
		text = null;
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}

	public double getLeft() {
		return left;
	}

	public void setLeft(float left) {
		this.left = left;
	}

	public double getBottom() {
		return bottom;
	}

	public void setBottom(float bottom) {
		this.bottom = bottom;
	}

	public double getRight() {
		return right;
	}

	public void setRight(float right) {
		this.right = right;
	}

	public double getUp() {
		return up;
	}

	public void setUp(float up) {
		this.up = up;
	}

	public double getOutlineThiccness() {
		return outlineThickness;
	}

	public void setOutlineThiccness(float outlineThiccness) {
		this.outlineThickness = outlineThiccness;
	}

	public int getOutlineColor() {
		return outlineColor;
	}

	public void setOutlineColor(int outlineColor) {
		this.outlineColor = outlineColor;
	}

	public int getInsideColor() {
		return insideColor;
	}

	public void setInsideColor(int insideColor) {
		this.insideColor = insideColor;
	}

	public int getTextColor() {
		return textColor;
	}

	public void setTextColor(int textColor) {
		this.textColor = textColor;
	}
	
	public GuiScreen getScreen() {
		return screen;
	}

	public void setScreen(GuiScreen screen) {
		this.screen = screen;
	}

	public float getTextScale() {
		return textScale;
	}

	public void setTextScale(float textScale) {
		this.textScale = textScale;
	}
	
}
