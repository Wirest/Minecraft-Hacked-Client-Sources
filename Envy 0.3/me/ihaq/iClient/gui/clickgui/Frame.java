package me.ihaq.iClient.gui.clickgui;

import java.awt.FontMetrics;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import me.ihaq.iClient.Envy;
import me.ihaq.iClient.utils.Colors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;

public class Frame extends Component {
	GuiMainMenu gmu;
	public String title;
	private boolean isOpen;
	private boolean isDrag;
	private int xOffset;
	private int yOffset;
	public ArrayList<Component> Items = new ArrayList();
	public SoundHandler soundHandlerIn;
	public int max;
	public int min;
	private int size = 0;

	public Frame(String title) {
		this.title = title;
		this.x = 0;
		this.y = 0;
		this.width = 120;
		this.height = 15;
	}

	public void addItem(Component item) {
		item.parent = this;
		this.Items.add(item);
	}

	public void draw(int mouseX, int mouseY, float partialTicks, int parX, int parY, GuiScreen screen) {
		if (this.isDrag) {
			this.x = (mouseX - this.xOffset);
			this.y = (mouseY - this.yOffset);
		}
		this.absx = (parX + this.x);
		this.absy = (parY + this.y);
		Component item;
		if (this.isOpen) {
			int rHeight = this.height;
			for (Iterator localIterator = this.Items.iterator(); localIterator.hasNext();) {
				item = (Component) localIterator.next();
				rHeight += item.renderHeight + 2;
			}
			rHeight -= this.Items.size() + 1;
		}
		GuiScreen.drawRect(this.x - 5, this.y - 5, this.x + this.width + 5, this.y + this.height, Colors.getColor());
		Envy.FONT_MANAGER.hud.drawString(this.title, (this.x + (this.width /2)) - (Envy.FONT_MANAGER.hud.getStringWidth(this.title)/2) , this.y +1, -1);
		if (this.isOpen) {
			for (Component item1 : this.Items) {
				item1.draw(mouseX, mouseY, partialTicks, this.absx, this.absy, screen);
				item1.isVisible = true;
			}
		} else {
			for (Component item1 : this.Items) {
				item1.isVisible = false;
			}
		}
		resizeComponenets();
	}

	private void resizeComponenets() {
		int compY = this.height;
		for (Component comp : this.Items) {
			comp.x = 0;
			comp.y = compY;
			comp.width = this.width;
			compY += comp.renderHeight + 1;
		}
	}

	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		if ((mouseX >= this.x) && (mouseX <= this.x + this.width) && (mouseY >= this.y)
				&& (mouseY <= this.y + this.height)) {
			if (mouseButton == 0) {
				this.isDrag = true;
				this.xOffset = (mouseX - this.x);
				this.yOffset = (mouseY - this.y);
			} else {
				this.isOpen = (!this.isOpen);
			}
		}
		if (this.isOpen) {
			for (Component child : this.Items) {
				child.mouseClicked(mouseX, mouseY, mouseButton);
			}
		}
	}

	public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
		this.isDrag = false;
		for (Component child : this.Items) {
			child.mouseReleased(mouseX, mouseY, mouseButton);
		}
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public void keyTyped(char typedChar, int keyCode) throws IOException {
		for (Component child : this.Items) {
			child.keyTyped(typedChar, keyCode);
		}
	}

	public void keyTypedNum(int typedChar, int keyCode) throws IOException {
	}

	public boolean isExpanded() {
		return isOpen;
	}

	public String getTitle() {
		return title;
	}

	public void setExpanded(boolean open) {
		isOpen = open;

	}
}
