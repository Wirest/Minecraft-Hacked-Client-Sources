package me.ihaq.iClient.gui.clickgui;

import java.awt.Font;
import java.io.IOException;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import me.ihaq.iClient.Envy;
import me.ihaq.iClient.modules.Module;
import me.ihaq.iClient.ttf.MinecraftFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;

public class KeyBinder extends Component {

	public Module mod;
	public boolean binding;

	public final static MinecraftFontRenderer fr = new MinecraftFontRenderer(new Font("Audiowide", Font.PLAIN, 16), true, true);

	public KeyBinder(Module mod, Component parent) {
		this.parent = parent;
		this.mod = mod;
		this.width = parent.width - 4;
		this.height = 14;
		this.renderWidth = width;
		this.renderHeight = height;
	}

	@Override
	public void draw(int mouseX, int mouseY, float partialTicks, int parX, int parY, GuiScreen screen) {
		GL11.glPushMatrix();
		GL11.glTranslatef(this.parent.x, this.parent.y, 0);
		this.absx = parX + this.x;
		this.absy = parY + this.y;
		screen.drawRect(x - 1, y, x + width + 1, y + height + 1, 0xFF000000);
		screen.drawRect(x, y, x + width, y + height, 0xFF606B68);
		if (mouseX >= this.absx && mouseX <= this.absx + this.width) {
			if (mouseY >= this.absy && mouseY <= this.absy + this.height) {
				screen.drawRect(x, y, x + width, y + height, 0x60000000);
			}
		}
		String kb = binding ? "..." : Keyboard.getKeyName(mod.getKeyCode());
		fr.drawString("Key", x + 2, y + 4, 0xFFFFFFFF);
		fr.drawString(kb, x + width - 2 - fr.getStringWidth(kb), y + 4, 0xFFFFFF);
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
					this.binding = true;

				}
			}

		}
	}

	@Override
	public void mouseReleased(int mouseX, int mouseY, int mouseButton) {

	}

	@Override
	public void keyTyped(char typedChar, int keyCode) throws IOException {
		if (this.binding) {
			this.binding = false;
			if (keyCode == Keyboard.KEY_DELETE) {
				mod.setKeyCode(0);
			} else {
				mod.setKeyCode(keyCode);
			}
		}
	}

}