package de.iotacb.client.gui.elements.textfields;

import java.awt.Color;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import de.iotacb.client.Client;
import de.iotacb.client.utilities.render.Render2D;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;

public class GuiTextBoxLogin extends GuiTextField {

	private final String title;
	private final boolean hide;
	
	private Color color;
	
	public GuiTextBoxLogin(int componentId, FontRenderer fontrendererObj, String title, int x, int y, int par5Width, int par6Height) {
		super(componentId, fontrendererObj, x, y, par5Width, par6Height);
		this.title = title;
		this.hide = false;
		this.color = new Color(0, 0, 0, 80);
	}
	
	public GuiTextBoxLogin(int componentId, FontRenderer fontrendererObj, String title, int x, int y, int par5Width, int par6Height, boolean hide) {
		super(componentId, fontrendererObj, x, y, par5Width, par6Height);
		this.title = title;
		this.hide = hide;
		this.color = new Color(0, 0, 0, 80);
	}
	
	public void drawTextBox(int mouseX, int mouseY) {
		Client.RENDER2D.rect(xPosition,  yPosition, width, height, color);
		
		final boolean widerThanWidth = Client.INSTANCE.getFontManager().getDefaultFont().getWidth((hide ? getText().replaceAll(".", "*") : getText())) > width - 10;
		
		final int offsetX = (int) (widerThanWidth ? (Client.INSTANCE.getFontManager().getDefaultFont().getWidth((hide ? getText().replaceAll(".", "*") : getText())) - width + 12) : 0);
		
		Client.RENDER2D.push();
		Client.RENDER2D.enable(GL11.GL_SCISSOR_TEST);
		Client.RENDER2D.scissor(xPosition + 5, yPosition, width - 5, height);
		
		if ((!isFocused() || isFocused()) && getTextUntrimmed().length() == 0) {
			Client.INSTANCE.getFontManager().getDefaultFont().drawStringWithShadow(title, xPosition + 5, yPosition + height / 2 - Client.INSTANCE.getFontManager().getDefaultFont().getHeight(title) / 2, Color.gray);
		}
		
		if (getText().length() > 0) {
			String text = (hide ? getTextUntrimmed().replaceAll(".", "*") : getTextUntrimmed());
			if (cursorPosition != getTextUntrimmed().length()) {
				text = text.substring(0, cursorPosition).concat(this.cursorCounter / 6 % 2 == 0 ? "_" : "  ").concat(text.substring(cursorPosition, text.length()));
			}
			Client.INSTANCE.getFontManager().getDefaultFont().drawStringWithShadow(text, xPosition + 5 - offsetX, yPosition + height / 2 - Client.INSTANCE.getFontManager().getDefaultFont().getHeight(text) / 2, Color.white);
		}
		if (this.cursorCounter / 6 % 2 == 0 && isFocused()) {
			if (cursorPosition == getTextUntrimmed().length()) {
				Client.INSTANCE.getFontManager().getDefaultFont().drawStringWithShadow("_", xPosition + 4 - offsetX + Client.INSTANCE.getFontManager().getDefaultFont().getWidth((hide ? getText().replaceAll(".", "*") : getText())), yPosition + height / 2 - Client.INSTANCE.getFontManager().getDefaultFont().getHeight("_") / 2, Color.white);
			}
		}
		Client.RENDER2D.disable(GL11.GL_SCISSOR_TEST);
		Client.RENDER2D.pop();
	}
	
	@Override
	public boolean textboxKeyTyped(char p_146201_1_, int p_146201_2_) {
		return super.textboxKeyTyped(p_146201_1_, p_146201_2_);
	}
	
	@Override
	public void mouseClicked(int p_146192_1_, int p_146192_2_, int p_146192_3_) {
		super.mouseClicked(p_146192_1_, p_146192_2_, p_146192_3_);
	}
	
	@Override
	public String getText() {
		return super.getText().trim();
	}
	
	public String getTextUntrimmed() {
		return super.getText();
	}
	
	public boolean isHovered(int mouseX, int mouseY) {
		return (mouseX > xPosition && mouseX < xPosition + width) && (mouseY > yPosition && mouseY < yPosition + height);
	}
	
	public void setColor(Color color) {
		this.color = color;
	}

}
