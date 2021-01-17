package me.ihaq.iClient.gui.clickgui;

import java.awt.Font;
import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import me.ihaq.iClient.Envy;
import me.ihaq.iClient.ttf.MinecraftFontRenderer;
import me.ihaq.iClient.utils.Colors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;

public class SpecialButton extends Component {

	GuiMainMenu gmu;
	
	public ArrayList<Component> elements = new ArrayList<Component>();
	
	public String title;
	public String desc;
	public boolean held;
	public boolean enabled;
	public long hoverTime = 0;
	public long lastTime = 0;
	public final static MinecraftFontRenderer fr = new MinecraftFontRenderer(new Font("Audiowide", Font.PLAIN, 18), true, true);
	float p_73968_1_;
	
	public static int size = 100;
	
	public boolean open;
	
	public SpecialButton(String title, String desc, Component parent) {
		this.title = title;
		this.desc = desc;
		this.parent = parent;
		this.width = parent.width - 4;
		this.height = 15;
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
		
		screen.drawRect(x, y, x + width, y + rHeight, 0x9973716D);
		screen.drawRect(x - 1, y, x + width + 1, y + height + 1, 0xFF000000);
		
		if(enabled) {
			screen.drawRect(x, y, x + width, y + height, Colors.getColor());
			fr.drawString(title, x + width / 2 - 55, y + 4, 0xFFFFFFFF);
			//fr.drawStringWithShadow("\u2713", (float)(this.x + this.width / 2 + 50), (float)(this.y + 2), -1);
		}
		if(!enabled) {
			screen.drawRect(x, y, x + width, y + height, 0x9973716D);
			fr.drawString(title, x + width / 2 - 55, y + 4, 0xFFFFFFFF);
		}
		
		if (mouseX >= this.absx && mouseX <= this.absx + this.width) {
			if (mouseY >= this.absy && mouseY <= this.absy + this.height) {
				screen.drawRect(x, y, x + width, y + height, 0x30000000);
			}
		}
		
		int compY = this.height;
		int compX = 0;
		int spacer = 2;
		for (Component c : this.elements) {
			c.y = compY + spacer;
			c.x = spacer;
			c.width = this.width - spacer * 2;
			compY += c.renderHeight + spacer - 1;
		}
		  if(this.open){
		   this.renderHeight = compY + spacer + 10;
		  }
		if (this.elements.size() > 0) {
			if (this.open) {
				for (Component c : this.elements) {
					c.draw(mouseX, mouseY, partialTicks, absx, absy, screen);
					c.isVisible = true;
				}
				this.renderHeight -= 11;
			} else {
				this.renderHeight = this.height;
				for (Component c : this.elements) {
					c.isVisible = false;
				}
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
				}else{
					if(this.elements.size() > 0) {
						this.open = !this.open;
					}
				}
			}

		}
		if(open) {
			for(Component c : elements) {
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
		if(open) {
			for(Component c : elements) {
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
		for(Component c : elements) {
			c.keyTyped(typedChar, keyCode);
		}
		
	}
}

