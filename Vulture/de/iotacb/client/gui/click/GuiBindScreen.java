package de.iotacb.client.gui.click;

import java.awt.Color;
import java.io.IOException;

import org.lwjgl.input.Keyboard;

import de.iotacb.client.Client;
import de.iotacb.client.module.Module;
import de.iotacb.client.utilities.render.Render2D;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

public class GuiBindScreen extends GuiScreen {
	
	private final Module moduleToBind;
	
	public GuiBindScreen(Module moduleToBind) {
		this.moduleToBind = moduleToBind;
	}
	
	@Override
	public void initGui() {
		super.initGui();
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		final String text = "Press any key to bind ".concat(Client.INSTANCE.getClientColorCode().concat(moduleToBind.getName())).concat("§f to a key");
		Client.BLUR_UTIL.blur(5);
		Client.RENDER2D.rect(0, 0, width, height, new Color(0, 0, 0, 200));
		Client.RENDER2D.push();
		Client.RENDER2D.translate(width / 2 - mc.fontRendererObj.getStringWidth(text) / 2, height / 2);
		Client.RENDER2D.scale(2, 2);
		mc.fontRendererObj.drawStringWithShadow(text, - mc.fontRendererObj.getStringWidth(text) / 4, 0, Color.white.getRGB());
		Client.RENDER2D.pop();
		final String text2 = "Press ".concat(Client.INSTANCE.getClientColorCode()).concat("ESCAPE").concat("§f to unbind the module.");
		mc.fontRendererObj.drawStringWithShadow(text2, (width - mc.fontRendererObj.getStringWidth(text2)) / 2, height / 2 + 20, -1);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (keyCode == Keyboard.KEY_ESCAPE) {
			moduleToBind.setKey(Keyboard.getKeyIndex("NONE"));
			moduleToBind.setMultiBindKey(-1);
			moduleToBind.setMultiBinded(false);
			mc.displayGuiScreen(Client.INSTANCE.getClickGui());
		} else {
			moduleToBind.setKey(keyCode);
			moduleToBind.setMultiBindKey(-1);
			moduleToBind.setMultiBinded(false);
			mc.displayGuiScreen(Client.INSTANCE.getClickGui());
		}
	}
	
}
