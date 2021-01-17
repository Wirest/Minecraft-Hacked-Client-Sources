package me.ihaq.iClient.gui.clickgui;

import java.awt.Font;
import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import me.ihaq.iClient.Envy;
import me.ihaq.iClient.modules.Module;
import me.ihaq.iClient.ttf.MinecraftFontRenderer;
import me.ihaq.iClient.utils.Colors;
import me.ihaq.iClient.utils.SpecialCircle;
import me.ihaq.iClient.utils.values.Value;
import me.ihaq.iClient.utils.values.ValueManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;

public class Button extends Component {

	GuiMainMenu gmu;

	public ArrayList<Component> elements = new ArrayList<Component>();

	public String title;
	public String desc;
	public boolean held;
	public boolean enabled;
	public long hoverTime = 0;
	public long lastTime = 0;
	public static MinecraftFontRenderer fr = new MinecraftFontRenderer(new Font("Audiowide", Font.PLAIN, 20), true, true);
	float p_73968_1_;

	public static int size = 100;

	public boolean open;

	public Button(String title, String desc, Component parent) {
		this.title = title;
		this.desc = desc;
		this.parent = parent;
		this.width = parent.width - 4;
		this.height = 16;
		this.renderWidth = width;
		this.renderHeight = height;
	}

	@Override
	public void draw(int mouseX, int mouseY, float partialTicks, int parX, int parY, GuiScreen screen) {
		this.onUpdate();
		GL11.glPushMatrix();
		GL11.glTranslatef(this.parent.x, this.parent.y, 0);
		this.absx = parX + this.x;
		this.absy = parY + this.y;

		int rHeight = this.height;

		screen.drawRect(x - 1, y, x + width + 1, y + height, 0xFF000000);
		screen.drawRect(x - 1, y, x + width + 1, y + height + 1, 0xFF000000);

		screen.drawRect(x, y, x + width, y + rHeight, 0x9973716D);
		
		if (enabled) {
			screen.drawRect(x, y, x + width, y + height, Colors.getColor());
			fr.drawString(title, x + width / 2 - 55, y + 4, 0xFFFFFFFF);
		} else if (!enabled) {
			screen.drawRect(x, y, x + width, y + height, 0x90000000);
			fr.drawString(title, x + width / 2 - 55, y + 4, 0xFFFFFFFF);
		}

		
		for (Module mod : Envy.MODULE_MANAGER.getModules()) {
			for (Value val : ValueManager.INSTANCE.getValues(mod)) {
				if((Boolean) val.getValue()) {
				}
			}
		}
		

		int compY = this.height;
		int compX = 0;
		int spacer = 2;
		for (Component c : this.elements) {
			c.y = compY + spacer - 2;
			c.x = spacer;
			c.width = this.width - spacer * 2;
			compY += c.renderHeight + spacer - 2;
		}
		if (this.open) {
			this.renderHeight = compY + spacer + 10;
			//GL11.glRotated(5, 0, Integer.MAX_VALUE, 0);
		}
		if (this.elements.size() > 0) {
			if (this.open) {
				for (Component c : this.elements) {
					// screen.drawRect(x, y + 12, x + width, y + height,
					// 0xFF000000);
				}
				for (Component c : this.elements) {
					c.draw(mouseX, mouseY, partialTicks, absx, absy, screen);
					c.isVisible = true;
				}
				this.renderHeight -= 13;
				SpecialCircle.circleOutline(x + width / 2 + 54, y + 7, 5, 0xFFFFFFFF);
				fr.drawString("-", x + width / 2 + (float)51.1, y + (float)3.1, 0xFFFFFFFF);
			} else {
				this.renderHeight = this.height;
				for (Component c : this.elements) {
					c.isVisible = false;
				}
				SpecialCircle.circleOutline(x + width / 2 + 54, y + 7, 5, 0xFFFFFFFF);
				fr.drawString("+", x + width / 2 + (float)51.1, y + (float)3.1, 0xFFFFFFFF);
			}
		} else {
			this.renderHeight = this.height;
		}
		GL11.glPopMatrix();
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		if (!this.isVisible) {
			return;
		}

		if (mouseX >= this.absx && mouseX <= this.absx + this.width) {
			if (mouseY >= this.absy && mouseY <= this.absy + this.height) {

			}
		}
		if (mouseX >= this.absx && mouseX <= this.absx + this.width) {
			if (mouseY >= this.absy && mouseY <= this.absy + this.height) {
				if (mouseButton == 0) {
					this.held = true;
				} else {
					if (this.elements.size() > 0) {
						this.open = !this.open;
					}
				}
			}

		}
		if (open) {
			for (Component c : elements) {
				c.mouseClicked(mouseX, mouseY, mouseButton);
			}
		}
	}

	@Override
	public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
		if (mouseX >= this.absx && mouseX <= this.absx + this.width) {
			if (mouseY >= this.absy && mouseY <= this.absy + this.height) {
				if (this.held) {
					this.held = false;
					this.onPressed();
				}
			}
		}
		if (open) {
			for (Component c : elements) {
				c.mouseReleased(mouseX, mouseY, mouseButton);
			}
		}
	}

	public void onPressed() {
	}

	public void onUpdate() {
	}

	@Override
	public void keyTyped(char typedChar, int keyCode) throws IOException {
		for (Component c : elements) {
			c.keyTyped(typedChar, keyCode);
		}

	}
}
