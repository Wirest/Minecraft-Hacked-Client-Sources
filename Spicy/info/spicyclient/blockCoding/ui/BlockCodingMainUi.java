package info.spicyclient.blockCoding.ui;

import java.io.IOException;

import org.lwjgl.input.Keyboard;

import info.spicyclient.SpicyClient;
import info.spicyclient.blockCoding.CustomModule;
import info.spicyclient.chatCommands.Command;
import info.spicyclient.modules.Module;
import info.spicyclient.modules.player.BlockCoding;
import info.spicyclient.settings.KeybindSetting;
import info.spicyclient.settings.Setting;
import info.spicyclient.ui.customOpenGLWidgets.Button;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

public class BlockCodingMainUi extends GuiScreen {
	
	public BlockCodingMainUi(String name) {
		
		module = new CustomModule(name);
		
	}
	
	public CustomModule module;
	
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
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		
		ScaledResolution sr = new ScaledResolution(mc);
		
		Gui.drawRect(0, 0, this.width, this.height, 0x9f000000);
		
		int factor = 10, textscale = 3;
		
		Gui.drawRect(sr.getScaledWidth() / factor, sr.getScaledHeight() / factor, sr.getScaledWidth() - (sr.getScaledWidth() / factor), sr.getScaledHeight() - (sr.getScaledHeight() / factor), 0xff36393f);
		
		GlStateManager.pushMatrix();
		GlStateManager.scale(textscale, textscale, 1);
		mc.fontRendererObj.drawString(module.name, ((sr.getScaledWidth() / 2) - ((mc.fontRendererObj.getStringWidth(module.name) * textscale) / 2)) / textscale, ((sr.getScaledHeight() / factor) + 10) / textscale, 0xffffffff);
		GlStateManager.popMatrix();
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		
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
