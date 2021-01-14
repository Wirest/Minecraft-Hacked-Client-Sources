package de.iotacb.client.gui.config;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import de.iotacb.client.Client;
import de.iotacb.client.file.files.AltManagerFile;
import de.iotacb.client.file.files.ConfigFile;
import de.iotacb.client.gui.config.sub.GuiAddConfig;
import de.iotacb.client.gui.config.sub.GuiUploadConfig;
import de.iotacb.client.gui.particle.ParticleManager;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class GuiConfig extends GuiScreen {
	
	public final List<ConfigSlot> configSlots = new ArrayList<ConfigSlot>();

	public int selectedSlot = -1;
	
	private boolean slotsHovered;
	
	private boolean loadModules = false, loadRenders = false;
	
	private final GuiScreen prevScreen;
	
	private ParticleManager pManager;
	
	private GuiButton loadButton, modulesButton, rendersButton;
	
	private final ResourceLocation SHADOW = new ResourceLocation("client/textures/shadow_minimap.png");
	
	public GuiConfig(GuiScreen prevScreen) {
		this.prevScreen = prevScreen;
	}
	
	@Override
	public void initGui() {
		final double heightOffset = 30;
		
		this.pManager = new ParticleManager();

		buttonList.add(new GuiButton(0, width / 2 - 250, height / 2 - 100, 125, 30, "Add"));
		buttonList.add(new GuiButton(1, width / 2 - 250, height / 2 - 69, 125, 30, "Remove"));
		buttonList.add(loadButton = new GuiButton(2, width / 2 - 250, height / 2 - 38, 125, 30, "Load"));
		buttonList.add(new GuiButton(3, width / 2 - 250, height / 2 - 7, 125, 30, "Upload"));
		
		buttonList.add(modulesButton = new GuiButton(4, width / 2 - 250, height / 2 + 45, 125, 30, loadModules ? "Modules will be loaded" : "Modules wont be loaded"));
		buttonList.add(rendersButton = new GuiButton(5, width / 2 - 250, height / 2 + 76, 125, 30, loadRenders ? "R-settings will be loaded" : "R-settings wont be loaded"));
		
		buttonList.add(new GuiButton(6, width / 2 - 250, height / 2 + 120, 125, 30, "Cancel"));
//		buttonList.add(new GuiButton(1, width / 2 - (width / 8) / 2, height - heightOffset - 25, width / 8, 20, "Import"));
//		buttonList.add(new GuiButton(2, width / 2 + ((width / 8) / 2) + 5, height - heightOffset - 25, width / 8, 20, "Remove"));
//
//		buttonList.add(new GuiButton(3, width / 2 - (((width / 8) / 2) * 3) - 5, height - heightOffset, width / 8, 20, "Login"));
//		buttonList.add(new GuiButton(4, width / 2 - (width / 8) / 2, height - heightOffset, width / 8, 20, "Direct login"));
//		buttonList.add(new GuiButton(5, width / 2 + ((width / 8) / 2) + 5, height - heightOffset, width / 8, 20, "Cancel"));
		
		init();
		
		super.initGui();
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 0) {
			mc.displayGuiScreen(new GuiAddConfig(this));
		}
		if (button.id == 1) {
			if (selectedSlot != -1) {
				new File(getSlotById(selectedSlot).getPath()).delete();
				configSlots.remove(selectedSlot);
				updateIds();
				selectedSlot = -1;
				Client.INSTANCE.getNotificationManager().addNotification("Config Gui", "Removed config.");
			}
		}
		if (button.id == 2) {
			if (selectedSlot != -1) {
				final ConfigFile configFile = new ConfigFile(getSlotById(selectedSlot).getPath());
				if (configFile.readConfig(loadRenders, loadModules)) {
					Client.INSTANCE.getNotificationManager().addNotification("Config Gui", "Loaded config!");
				}
			}
		}
		if (button.id == 3) {
			mc.displayGuiScreen(new GuiUploadConfig(this));
		}
		if (button.id == 4) {
			loadModules = !loadModules;
			modulesButton.setDisplayString(loadModules ? "Modules will be loaded" : "Modules wont be loaded");
		}
		if (button.id == 5) {
			loadRenders = !loadRenders;
			rendersButton.setDisplayString(loadRenders ? "R-settings will be loaded" : "R-settings wont be loaded");
		}
		if (button.id == 6) {
			mc.displayGuiScreen(prevScreen);
		}
		super.actionPerformed(button);
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (keyCode == Keyboard.KEY_ESCAPE) {
			mc.displayGuiScreen(prevScreen);
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		slotsHovered = (mouseX > width / 2 - 125 && mouseX < width / 2 + 250) && (mouseY > height / 2 - 150 && mouseY < height / 2 + 150);
		
		if (slotsHovered && (40 * configSlots.size()) + 104 > height / 2 + 150) {
			updateScrolling(new ArrayList<ConfigSlot>(configSlots));
		}
		
		final double w = 500;
		final double h = 300;
		pManager.draw(mouseX, mouseY);
		Client.BLUR_UTIL.blur(2);
//		Client.BLUR_UTIL.blur(width / 2 - w / 2, height / 2 - h / 2, w, h, 5);
		Client.RENDER2D.image(SHADOW, width / 2 - w / 2 - 25, height / 2 - h / 2 - 15, w + 50, h + 30);
		Client.RENDER2D.rect(width / 2 - w / 2, height / 2 - h / 2, w, h, new Color(30, 30, 30));
		Client.RENDER2D.rect(width / 2 - w / 2, height / 2 - h / 2, w / 4, h, new Color(20, 20, 20));
		Client.RENDER2D.gradientSideways(w / 2 + w / 4 - 20, height / 2 - h / 2, 10, h, new Color(0, 0, 0, 100), new Color(0, 0, 0, 0));
		
		GL11.glEnable(GL11.GL_SCISSOR_TEST);
		Client.RENDER2D.scissor(0, height / 2 - 150, width, 300);
		
		drawSlots(mouseX, mouseY, new ArrayList<ConfigSlot>(configSlots));
		
		if (configSlots.size() == 0) {
			Client.INSTANCE.getFontManager().getBigFont().drawCenteredStringWithShadow("No local configs found!", width / 2 + 60, height / 2 - 15, Color.gray);
		}
		
		GL11.glDisable(GL11.GL_SCISSOR_TEST);
		
		Client.INSTANCE.getNotificationManager().draw(0);
		
		Client.INSTANCE.getFontManager().getBigFont().drawCenteredStringWithShadow("Configs", width / 2 - 187, height / 2 - 140, Color.white);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		double y = scroll + 104;
		for (ConfigSlot slot : new ArrayList<ConfigSlot>(configSlots)) {
			slot.setY(y);
			final boolean hovered = (mouseX > slot.getX() && mouseX < slot.getX() + slot.getWidth()) && (mouseY > slot.getY() && mouseY < slot.getY() + slot.getHeight());
			if (mouseButton == 0 && hovered) {
				selectedSlot = slot.getSlotId();
			}
			y += slot.getHeight();
		}
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
	public void updateScreen() {
		if (selectedSlot == -1) {
			loadButton.enabled = false;
		} else {
			loadButton.enabled = true;
		}
		super.updateScreen();
	}
	
	private void drawSlots(int mouseX, int mouseY, List<ConfigSlot> slots) {
		double y = scroll + 104;
		for (ConfigSlot slot : slots) {
			slot.setY(y);
			slot.draw(mouseX, mouseY);
			y += slot.getHeight();
		}
	}
	
	private double scroll;
	
	private void updateScrolling(List<ConfigSlot> slots) {
		scroll += Mouse.getDWheel() * .1;
		for (ConfigSlot slot : slots) {
			scroll = MathHelper.clamp_double(scroll, -(slot.getHeight() * configSlots.size()) + height / 2 + 46, 0);
			slot.setY(slot.getY() + scroll + 100);
		}
	}
	
	private void updateIds() {
		int id = 0;
		for (ConfigSlot slot : configSlots) {
			slot.setSlotId(id);
			id++;
		}
	}
	
	private ConfigSlot getSlotById(int id) {
		for (ConfigSlot slot : configSlots) {
			if (slot.getSlotId() == id) {
				return slot;
			}
		}
		return null;
	}
	
	private void init() {
		configSlots.clear();
		
//		((C) Client.INSTANCE.getFileManager().getFileByClass(AltManagerFile.class)).readAlts();
//		
//		for (int i = 0; i < Client.INSTANCE.getAltManager().getAlts().size(); i++) {
//			final Alt alt = Client.INSTANCE.getAltManager().getAlts().get(i);
//			altSlots.add(new AltSlot(width / 2 - (((width / 8) / 2) * 3) - 10, 0, (((width / 8) + 5) * 3) + 5, 40, i, alt.getEmail(), alt.getPassword(), this));
//		}
		int y = 0;
		if(!new File(Client.INSTANCE.getFileManager().getVultureFolder().concat("/files/configs")).exists()) {
			new File(Client.INSTANCE.getFileManager().getVultureFolder().concat("/files/configs")).mkdirs();
		} else {
			final String[] configs = new File(Client.INSTANCE.getFileManager().getVultureFolder().concat("/files/configs/")).list();
			for (int i = 0; i < configs.length; i++) {
				final ConfigSlot slot = new ConfigSlot(width / 2 - 125, 0, 375, 40, i, configs[i], Client.INSTANCE.getFileManager().getVultureFolder().concat("/files/configs/").concat(configs[i]), this);
				configSlots.add(slot);
			}
			
		}
		
		updateIds();
	}

}
