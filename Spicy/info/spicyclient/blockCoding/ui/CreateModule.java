package info.spicyclient.blockCoding.ui;

import java.io.IOException;

import org.lwjgl.input.Keyboard;

import info.spicyclient.SpicyClient;
import info.spicyclient.modules.player.BlockCoding;
import info.spicyclient.ui.customOpenGLWidgets.Button;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

public class CreateModule extends GuiScreen {
	
	@Override
	public void initGui() {
		
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		
		BlockCoding blockCoding = SpicyClient.config.blockCoding;
		
		if (keyCode == Keyboard.KEY_ESCAPE || keyCode == blockCoding.keycode.code) {
			mc.displayGuiScreen(null);
		}
		
	}
	
	public Button createNewModule = new Button((this.width / 2) + 30, 100, (this.width / 2) + 130, 130, 0xff202225, 0xff7289da, -1, 2, this);
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		
		ScaledResolution sr = new ScaledResolution(mc);
		
		Gui.drawRect(0, 0, this.width, this.height, 0x9f000000);
		
		int textscale = 2;
		
		Gui.drawRect(sr.getScaledWidth(), sr.getScaledHeight(), sr.getScaledWidth() - (sr.getScaledWidth()), sr.getScaledHeight() - (sr.getScaledHeight()), 0xff36393f);
		
		GlStateManager.pushMatrix();
		GlStateManager.scale(textscale, textscale, 1);
		drawCenteredString(mc.fontRendererObj, "Create or edit a module", (this.width / 2) / textscale, 20 / textscale, -1);
		GlStateManager.popMatrix();
		
		createNewModule = new Button((this.width / 2) + 30 + 40, 130, (this.width / 2) + 180 + 40, 90, 0xff202225, 0xff7289da, -1, 2, this);
		createNewModule.setText("Create new module");
		createNewModule.textScale = 1.4f;
		
		int buttonHoverExpand = 2;
		
		// For the create new module button
		if (mouseX > (this.width / 2) + 30 + 40 && mouseX < (this.width / 2) + 180 + 40 && mouseY < 130 && mouseY > 90) {
			createNewModule.insideColor = 0xff4d5c91;
			createNewModule.setLeft((float) (createNewModule.getLeft() - buttonHoverExpand));
			createNewModule.setUp((float) (createNewModule.getUp() - buttonHoverExpand));
			createNewModule.setRight((float) (createNewModule.getRight() + buttonHoverExpand));
			createNewModule.setBottom((float) (createNewModule.getBottom() + buttonHoverExpand));
		}
		
		drawRect((this.width / 2) + 30 + 30, (this.height / 2) - 130, this.width, this.height, 0xff202225);
		
		createNewModule.draw();
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		
		if (mouseX > (this.width / 2) + 30 + 40 && mouseX < (this.width / 2) + 180 + 40 && mouseY < 130 && mouseY > 90 & mouseButton == 0) {
			
			mc.displayGuiScreen(new BlockCodingMainUi("New module"));
			
		}
		
	}
	
	@Override
	protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
		
	}
	
	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
	
}
