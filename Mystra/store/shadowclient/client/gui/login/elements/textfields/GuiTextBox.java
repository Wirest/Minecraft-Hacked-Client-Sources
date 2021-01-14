package store.shadowclient.client.gui.login.elements.textfields;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import store.shadowclient.client.Shadow;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;

public class GuiTextBox extends GuiTextField {

	private final String title;
	private final boolean hide;
	
	public GuiTextBox(int componentId, FontRenderer fontrendererObj, String title, int x, int y, int par5Width, int par6Height) {
		super(componentId, fontrendererObj, x, y, par5Width, par6Height);
		this.title = title;
		this.hide = false;
	}
	
	public GuiTextBox(int componentId, FontRenderer fontrendererObj, String title, int x, int y, int par5Width, int par6Height, boolean hide) {
		super(componentId, fontrendererObj, x, y, par5Width, par6Height);
		this.title = title;
		this.hide = hide;
	}
	
	public void drawTextBox(int mouseX, int mouseY) {
		Shadow.RENDER2D.rect(xPosition,  yPosition, width, height, isFocused() ? isHovered(mouseX, mouseY) ? new Color(255, 255, 255, 100) : new Color(255, 255, 255, 50) : isHovered(mouseX, mouseY) ? new Color(40, 40, 40) : new Color(30, 30, 30));
		
		final boolean widerThanWidth = Shadow.fontManager.getFont("SFL 10").getWidth((hide ? getText().replaceAll(".", "*") : getText())) > width - 10;
		
		final int offsetX = (int) (widerThanWidth ? (Shadow.fontManager.getFont("SFL 10").getWidth((hide ? getText().replaceAll(".", "*") : getText())) - width + 12) : 0);
		
		Shadow.RENDER2D.push();
		Shadow.RENDER2D.enable(GL11.GL_SCISSOR_TEST);
		Shadow.RENDER2D.scissor(xPosition + 5, yPosition, width - 5, height);
		
		if ((!isFocused() || isFocused()) && getTextUntrimmed().length() == 0) {
			Shadow.fontManager.getFont("SFL 10").drawStringWithShadow(title, xPosition + 5, yPosition + height / 2 - Shadow.fontManager.getFont("SFL 10").getHeight(title) / 2, 0x80808080);
		}
		
		if (getText().length() > 0) {
			String text = (hide ? getTextUntrimmed().replaceAll(".", "*") : getTextUntrimmed());
			if (cursorPosition != getTextUntrimmed().length()) {
				text = text.substring(0, cursorPosition).concat(this.cursorCounter / 6 % 2 == 0 ? "_" : "  ").concat(text.substring(cursorPosition, text.length()));
			}
			Shadow.fontManager.getFont("SFL 10").drawStringWithShadow(text, xPosition + 5 - offsetX, yPosition + height / 2 - Shadow.fontManager.getFont("SFL 10").getHeight(text) / 2, -1);
		}
		if (this.cursorCounter / 6 % 2 == 0 && isFocused()) {
			if (cursorPosition == getTextUntrimmed().length()) {
				Shadow.fontManager.getFont("SFL 10").drawStringWithShadow("_", xPosition + 4 - offsetX + Shadow.fontManager.getFont("SFL 10").getWidth((hide ? getTextUntrimmed().replaceAll(".", "*") : getText())), yPosition + height / 2 - Shadow.fontManager.getFont("SFL 10").getHeight("_") / 2, -1);
			}
		}
		Shadow.RENDER2D.disable(GL11.GL_SCISSOR_TEST);
		Shadow.RENDER2D.pop();
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

}
