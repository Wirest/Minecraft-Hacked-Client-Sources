package de.iotacb.client.gui.config.sub;

import java.awt.Color;
import java.io.IOException;

import org.lwjgl.input.Keyboard;

import de.iotacb.client.Client;
import de.iotacb.client.file.files.ConfigFile;
import de.iotacb.client.gui.elements.textfields.GuiTextBoxLogin;
import de.iotacb.cu.core.page.PageUtil;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiAddConfig extends GuiScreen {

	private GuiTextBoxLogin configName;
	private GuiButton addButton;
	
	private final GuiScreen prevScreen;
	
	public GuiAddConfig(GuiScreen prevScreen) {
		this.prevScreen = prevScreen;
	}
	
	@Override
	public void initGui() {
		Keyboard.enableRepeatEvents(true);
		
		this.configName = new GuiTextBoxLogin(0, fontRendererObj, "Config name or URL", width / 2 - 150, height / 2 - 15, 300, 30);
		this.buttonList.add(addButton = new GuiButton(0, width / 2 - 150, height / 2 + 16, 300, 30, "Add"));
		this.buttonList.add(new GuiButton(1, width / 2 - 150, height / 2 + 16 + 31, 300, 30, "Cancel"));
		super.initGui();
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 0) {
			saveConfig();
		}
		if (button.id == 1) {
			mc.displayGuiScreen(prevScreen);
		}
		super.actionPerformed(button);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		Client.RENDER2D.rect(width / 2 - 155, height / 2 - 30, 310, 120, new Color(20, 20, 20, 200));
		
		addButton.enabled = !configName.getText().isEmpty();
		configName.drawTextBox(mouseX, mouseY);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		configName.mouseClicked(mouseX, mouseY, mouseButton);
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
	public void updateScreen() {
		configName.updateCursorCounter();
		super.updateScreen();
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (keyCode == Keyboard.KEY_ESCAPE) {
			mc.displayGuiScreen(prevScreen);
		}
		if (keyCode == Keyboard.KEY_RETURN) {
			saveConfig();
		}
		configName.textboxKeyTyped(typedChar, keyCode);
		super.keyTyped(typedChar, keyCode);
	}
	
	private void saveConfig() {
		final String name = configName.getText();
		if (name.isEmpty()) return;
		if (name.contains("http")) {
			final String config = PageUtil.getContentOfPage(name);
			final String configName = name.split("/")[name.split("/").length - 1];
			final ConfigFile configFile = new ConfigFile(Client.INSTANCE.getFileManager().getVultureFolder().concat("/files/configs/") + configName + ".txt");
			if (configFile.saveConfig(config)) {
				mc.displayGuiScreen(prevScreen);
			}
		} else {
			final ConfigFile configFile = new ConfigFile(Client.INSTANCE.getFileManager().getVultureFolder().concat("/files/configs/") + name + ".txt");
			if (configFile.saveConfig()) {
				mc.displayGuiScreen(prevScreen);
			}
		}
	}
	
	
}
