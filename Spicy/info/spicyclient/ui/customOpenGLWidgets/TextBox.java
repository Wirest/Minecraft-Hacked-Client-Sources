package info.spicyclient.ui.customOpenGLWidgets;

import org.lwjgl.input.Keyboard;

import info.spicyclient.SpicyClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ChatAllowedCharacters;

public class TextBox {
	
	public TextBox(double left, double bottom, double right, double up, int outlineColor, int insideColor, int textColor, int ghostTextColor, float outlineThickness, Boolean selected, GuiScreen screen) {
		this.left = left;
		this.bottom = bottom;
		this.right = right;
		this.up = up;
		this.outlineColor = outlineColor;
		this.insideColor = insideColor;
		this.textColor = textColor;
		this.ghostTextColor = ghostTextColor;
		this.outlineThickness = outlineThickness;
		this.selected = selected;
		this.screen = screen;
	}
	
	public double left = 0, bottom = 0, right = 0, up = 0 , outlineThickness = 2;
	public int outlineColor = -1, insideColor = 0xff000000, textColor = -1, ghostTextColor = -1;
	public GuiScreen screen = null;
	public String text = null, ghostText = null;
	public boolean selected = false;
	public float textScale = 1;
	
	private long blinker = System.currentTimeMillis();
	
	public void draw() {
		
		GlStateManager.pushMatrix();
		screen.drawRect(left, bottom, right, up, outlineColor);
		screen.drawRect(left + outlineThickness, bottom - outlineThickness, right - outlineThickness, up + outlineThickness, insideColor);
		GlStateManager.popMatrix();
		
		GlStateManager.pushMatrix();
		GlStateManager.scale(textScale, textScale, 1);
		if (text == null && ghostText != null && !selected) {
			screen.drawString(Minecraft.getMinecraft().fontRendererObj, ghostText, (left + outlineThickness) / textScale, ((((bottom - up) / 2) / 2) + up) / textScale, ghostTextColor);
		}
		else if (text == null) {
			
			if (blinker > System.currentTimeMillis() && selected) {
				if (text == null) {
					screen.drawString(Minecraft.getMinecraft().fontRendererObj, " ", (left + outlineThickness) / textScale, ((((bottom - up) / 2) / 2) + up) / textScale, textColor);
				}
				else if (text.length() == 0) {
					screen.drawString(Minecraft.getMinecraft().fontRendererObj, " ", (left + outlineThickness) / textScale, ((((bottom - up) / 2) / 2) + up) / textScale, textColor);
				}
				else {
					screen.drawString(Minecraft.getMinecraft().fontRendererObj, text, (left + outlineThickness) / textScale, ((((bottom - up) / 2) / 2) + up) / textScale, textColor);
				}
			}
			else if (selected) {
				if (blinker < System.currentTimeMillis() - 500) {
					blinker = System.currentTimeMillis() + 500;
				}
				if (text == null){
					screen.drawString(Minecraft.getMinecraft().fontRendererObj, "_", (left + outlineThickness) / textScale, ((((bottom - up) / 2) / 2) + up) / textScale, textColor);
				}
				else if (text.length() == 0) {
					screen.drawString(Minecraft.getMinecraft().fontRendererObj, "_", (left + outlineThickness) / textScale, ((((bottom - up) / 2) / 2) + up) / textScale, textColor);
				}
				else {
					screen.drawString(Minecraft.getMinecraft().fontRendererObj, text + "_", (left + outlineThickness) / textScale, ((((bottom - up) / 2) / 2) + up) / textScale, textColor);
				}
				
			}
			
		}
		else if (text.length() == 0 && ghostText != null && !selected) {
			screen.drawString(Minecraft.getMinecraft().fontRendererObj, ghostText, (left + outlineThickness) / textScale, ((((bottom - up) / 2) / 2) + up) / textScale, ghostTextColor);
		}
		else {
			if (blinker > System.currentTimeMillis() && selected) {
				if (text == null) {
					screen.drawString(Minecraft.getMinecraft().fontRendererObj, " ", (left + outlineThickness) / textScale, ((((bottom - up) / 2) / 2) + up) / textScale, textColor);
				}
				else if (text.length() == 0) {
					screen.drawString(Minecraft.getMinecraft().fontRendererObj, " ", (left + outlineThickness) / textScale, ((((bottom - up) / 2) / 2) + up) / textScale, textColor);
				}
				else {
					screen.drawString(Minecraft.getMinecraft().fontRendererObj, text, (left + outlineThickness) / textScale, ((((bottom - up) / 2) / 2) + up) / textScale, textColor);
				}
			}
			else if (selected) {
				if (blinker < System.currentTimeMillis() - 500) {
					blinker = System.currentTimeMillis() + 500;
				}
				if (text == null){
					screen.drawString(Minecraft.getMinecraft().fontRendererObj, "_", (left + outlineThickness) / textScale, ((((bottom - up) / 2) / 2) + up) / textScale, textColor);
				}
				else if (text.length() == 0) {
					screen.drawString(Minecraft.getMinecraft().fontRendererObj, "_", (left + outlineThickness) / textScale, ((((bottom - up) / 2) / 2) + up) / textScale, textColor);
				}
				else {
					screen.drawString(Minecraft.getMinecraft().fontRendererObj, text + "_", (left + outlineThickness) / textScale, ((((bottom - up) / 2) / 2) + up) / textScale, textColor);
				}
				
			}
			else if (!selected) {
				screen.drawString(Minecraft.getMinecraft().fontRendererObj, text, (left + outlineThickness) / textScale, ((((bottom - up) / 2) / 2) + up) / textScale, textColor);
			}
		}
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
	
	public void setGhostText(String text) {
		this.ghostText = text;
	}
	
	public void typeKey(char character, int keycode) {
		
		if (keycode == Keyboard.KEY_BACK) {
			if (text == null) {
				
			}
			else if (text.length() > 0) {
				text = text.substring(0, text.length() - 1);
			}
		}
		else if (ChatAllowedCharacters.isAllowedCharacter(character)) {
			
            if (selected) {
                this.addChar(Character.toString(character));
            }
            
        }
		
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
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

	public void setLeft(double left) {
		this.left = left;
	}

	public double getBottom() {
		return bottom;
	}

	public void setBottom(double bottom) {
		this.bottom = bottom;
	}

	public double getRight() {
		return right;
	}

	public void setRight(double right) {
		this.right = right;
	}

	public double getUp() {
		return up;
	}

	public void setUp(double up) {
		this.up = up;
	}

	public double getOutlineThiccness() {
		return outlineThickness;
	}

	public void setOutlineThiccness(double outlineThiccness) {
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

	public int getGhostTextColor() {
		return ghostTextColor;
	}

	public void setGhostTextColor(int ghostTextColor) {
		this.ghostTextColor = ghostTextColor;
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

	public String getGhostText() {
		return ghostText;
	}
	
}
