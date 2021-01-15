package me.robbanrobbin.jigsaw.gui.custom;

import java.awt.Font;

import org.darkstorm.minecraft.gui.font.UnicodeFontRenderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;

public class SearchBar extends Gui {

	public boolean hovered;
	public String typed = "";
	public FontRenderer fontRenderer = new UnicodeFontRenderer(new Font("Consolas", Font.PLAIN, 19));
	private int width;
	private int height;
	public boolean typing;

	public void render(int w, int h, int mouseX, int mouseY) {
		this.width = w;
		this.height = h;
		Minecraft mc = Minecraft.getMinecraft();
		int x = width - 200;
		int y = height - 20;
		if (mouseX > x && mouseY > y) {
			hovered = true;
		} else {
			hovered = false;
		}
		drawHorizontalLine(x, width, height - 20, 0xff000000);
		drawVerticalLine(x, height, height - 20, 0xff000000);

		if (hovered) {
			drawRect(x, y, width, height, 0xab20201a);
		} else {
			drawRect(x, y, width, height, 0x8020201a);
		}
		if (typing) {
			fontRenderer.drawString(typed + "_", x + 5, y + 5, 0xffcfcfcf);
		} else {
			fontRenderer.drawString("Search...", x + 5, y + 5, 0xffcccccc);
		}
	}

	public void update() {
		if (typed == null) {
			typed = "";
		}
	}

	public void keyTyped(char typedChar, int keyCode) {
		typing = true;
		// Jigsaw.chatMessage(Character.toString(typedChar));
		// Jigsaw.chatMessage(keyCode);
		if (keyCode == 1) { // Escape
			typing = false;
			return;
		}
		if (keyCode == 14) { // Backspace
			if (!typed.isEmpty()) {
				typed = typed.substring(0, typed.length() - 1);
			}
			return;
		}
		if (keyCode == 28 || keyCode == 42 || keyCode == 29) {
			return;
		}
		if ((keyCode > 1 && keyCode < 12) || (keyCode > 15 && keyCode < 26) || (keyCode > 29 && keyCode < 39)
				|| (keyCode > 43 && keyCode < 51)) {
			typed += Character.toString(typedChar);
		}
	}

	public void mouseClicked(int x, int y, int button) {

	}

	public void mouseReleased(int x, int y, int button) {

	}

	public void init() {
		typing = false;
		typed = "";
	}

}
