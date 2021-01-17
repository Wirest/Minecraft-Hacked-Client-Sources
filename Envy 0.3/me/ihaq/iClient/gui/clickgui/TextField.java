package me.ihaq.iClient.gui.clickgui;

import java.io.IOException;

import org.lwjgl.opengl.GL11;

import me.ihaq.iClient.Envy;
import me.ihaq.iClient.ttf.MinecraftFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;

public class TextField extends Component {
	
	public boolean typing;
	
	public String text;
	
	public static MinecraftFontRenderer fr = Envy.FONT_MANAGER.hud;
	
	public TextField(Component parent) {
		this.parent = parent;
		this.width = parent.width - 4;
		this.height = 15;
		this.renderWidth = width;
		this.renderHeight = height;
	}
	
	
	@Override
	public void draw(int mouseX, int mouseY, float partialTicks, int parX, int parY, GuiScreen screen) {
		GL11.glPushMatrix();
		GL11.glTranslatef(this.parent.x, this.parent.y, 0);
		this.absx = parX + this.x;
		this.absy = parY + this.y;
		screen.drawRect(x, y, x + width, y + height, 0x90000000);
		if (mouseX >= this.absx && mouseX <= this.absx + this.width) {
			if (mouseY >= this.absy && mouseY <= this.absy + this.height) {
				screen.drawRect(x, y, x + width, y + height, 0x60000000);
			}
		}
		screen.drawString(fr, "Key", x + 2, y + 2, 0xFFFFFFFF);
		screen.drawString(fr, text, x + width - 2 - fr.getStringWidth(text), y + 2, 0xFFFFFF);
		GL11.glPopMatrix();
	}

	

	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		
		if (!this.isVisible) {
			return;
		}

		if (mouseX >= this.absx && mouseX <= this.absx + this.width) {
			if (mouseY >= this.absy && mouseY <= this.absy + this.height) {
				if (mouseButton == 0) {
					this.typing = !typing;
				}
			}

		}
	}

	@Override
	public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
		
	}
	
	@Override
	public void keyTyped(char typedChar, int keyCode) throws IOException {
		if(this.typing) {
			text += String.valueOf(typedChar);
		}
	}
	
}