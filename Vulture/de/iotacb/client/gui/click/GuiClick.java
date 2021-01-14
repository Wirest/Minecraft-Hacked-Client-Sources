package de.iotacb.client.gui.click;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import de.iotacb.client.Client;
import de.iotacb.client.file.files.ClickGuiFile;
import de.iotacb.client.file.files.ClientConfigFile;
import de.iotacb.client.file.files.ConfigFile;
import de.iotacb.client.file.files.ModuleFile;
import de.iotacb.client.gui.click.elements.ElementPanel;
import de.iotacb.client.gui.config.GuiConfig;
import de.iotacb.client.gui.elements.buttons.GuiTexturedButton;
import de.iotacb.client.gui.particle.ParticleManager;
import de.iotacb.client.module.Category;
import de.iotacb.client.module.Module;
import de.iotacb.client.module.modules.render.ClickGui;
import de.iotacb.client.module.modules.render.HUD;
import de.iotacb.client.utilities.render.animations.AnimationUtil;
import de.iotacb.client.utilities.render.animations.easings.Expo;
import de.iotacb.client.utilities.render.animations.easings.utilities.Progression;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;

public class GuiClick extends GuiScreen {
	
	private List<ElementPanel> panels;
	
	private double scaleFactor, maxScaleFactor, blurStrength;
	
	private ParticleManager pManager;
	
	private AnimationUtil animUtil;
	private Progression prog;
	
	private boolean opening;
	
	@Override
	public void initGui() {
		if (mc.thePlayer == null || mc.theWorld == null) return;
		
		maxScaleFactor = Client.INSTANCE.getModuleManager().getModuleByClass(ClickGui.class).getValueByName("ClickGuiScale factor").getNumberValue();
		
		this.panels = new ArrayList<ElementPanel>();
		
		this.animUtil = new AnimationUtil(Expo.class);
		this.prog = new Progression();
		this.pManager = new ParticleManager();
		
		opening = true;
		prog.setValue(0);
		
		this.buttonList.add(new GuiTexturedButton(0, 10, height - 70, 40, 40, "client/designs/default/sub_buttons/wheel.png"));
		this.buttonList.add(new GuiButton(1, width - 50, height - 70, 40, 40, "Reset"));
		this.buttonList.add(new GuiButton(2, width - 160, 10, 150, 30, "Hide modules in ArrayList"));
		
		int panelSpacing = (int) ClickConfig.PANEL_WIDTH;
		for (Category category : Category.values()) {
			final String panelTitle = String.valueOf(category.name().charAt(0)) + category.name().substring(1).toLowerCase();
			final ElementPanel panel = new ElementPanel(panelSpacing, 100, ClickConfig.PANEL_WIDTH, Client.INSTANCE.getFontManager().getDefaultFont().getHeight(panelTitle) + 5, null, panelTitle);
			panels.add(panel);
			panelSpacing += ClickConfig.PANEL_WIDTH + 20;
		}
		
		((ClickGuiFile) Client.INSTANCE.getFileManager().getFileByClass(ClickGuiFile.class)).readClick();
		
		super.initGui();
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 0) {
			mc.displayGuiScreen(new GuiConfig(this));
		}
		if (button.id == 1) {
			panels.clear();
			int panelSpacing = (int) ClickConfig.PANEL_WIDTH;
			for (Category category : Category.values()) {
				final String panelTitle = String.valueOf(category.name().charAt(0)) + category.name().substring(1).toLowerCase();
				final ElementPanel panel = new ElementPanel(panelSpacing, 100, ClickConfig.PANEL_WIDTH, Client.INSTANCE.getFontManager().getDefaultFont().getHeight(panelTitle) + 5, null, panelTitle);
				panels.add(panel);
				panelSpacing += ClickConfig.PANEL_WIDTH + 20;
			}
		}
		if (button.id == 2) {
			for (Module module : Client.INSTANCE.getModuleManager().getModules()) {
				module.getValueByName(module.getName() + "Show in List").setBooleanValue(false);
			}
		}
		super.actionPerformed(button);
	}
	
	double blurring;
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		mouseX /= scaleFactor;
		mouseY /= scaleFactor;
		buttonList.get(0).yPosition = height - (Client.INSTANCE.getModuleManager().getModuleByClass(HUD.class).getValueByName("HUDInfo").getBooleanValue() ? 70 : 50);
		Client.INSTANCE.setClientFont(Client.INSTANCE.getModuleManager().getModuleByClass(HUD.class).getValueByName("HUDFonts").getComboValue());
		panels.sort(Comparator.comparingInt(panel -> ((ElementPanel)panel).getClickIndex()));
		updateScaleAnimation(opening);
		this.pManager.draw(mouseX, mouseY);
		Client.BLUR_UTIL.blur((int)blurring);
		Client.RENDER2D.gradient(0, 0, this.width, this.height, Client.INSTANCE.getClientColor().setAlpha((int) (200 * scaleFactor)), new Color(255, 255, 255, (int) (50 * scaleFactor)));
		Client.RENDER2D.push();
		Client.RENDER2D.translate(width / 2 * (maxScaleFactor - scaleFactor), -height * (maxScaleFactor - scaleFactor));
		Client.RENDER2D.scale(scaleFactor, scaleFactor);
		for (final ElementPanel panel : panels) {
			panel.updateElement(mouseX, mouseY);
			panel.drawElement(mouseX, mouseY);
		}
		super.drawScreen(mouseX, mouseY, partialTicks);
		Client.RENDER2D.pop();
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		mouseX /= scaleFactor;
		mouseY /= scaleFactor;
		for (final ElementPanel panel : panels) {
			panel.clickElement(mouseX, mouseY, mouseButton);
		}
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (!Client.INSTANCE.getModuleManager().getModuleByClass(ClickGui.class).getValueByName("ClickGuiAnimations").getBooleanValue()) {
			super.keyTyped(typedChar, keyCode);
		}
		if (keyCode == Keyboard.KEY_ESCAPE) {
			if (opening) {
				prog.setValue(0);
				animUtil.getProgression(1).reset();
				animUtil.getProgression(484).reset();
				opening = false;
				pManager.setFadeIn(false);
			}
		}
		
		
		if (keyCode == Client.INSTANCE.getModuleManager().getModuleByClass(ClickGui.class).getKey()) {
			if (opening) {
				prog.setValue(0);
				animUtil.getProgression(1).reset();
				animUtil.getProgression(484).reset();
				opening = false;
			}
		}
	}
	
	@Override
	public void onGuiClosed() {
		scaleFactor = 0;
		
		((ClickGuiFile) Client.INSTANCE.getFileManager().getFileByClass(ClickGuiFile.class)).saveClick();
		((ConfigFile) Client.INSTANCE.getFileManager().getFileByClass(ConfigFile.class)).saveConfig();
		((ModuleFile) Client.INSTANCE.getFileManager().getFileByClass(ModuleFile.class)).saveModules();
		((ClientConfigFile) Client.INSTANCE.getFileManager().getFileByClass(ClientConfigFile.class)).saveConfig();
		super.onGuiClosed();
	}
	
	public final void updateScaleAnimation(boolean opening) {
		blurring = MathHelper.clamp_double(blurring, 0, 5);
		scaleFactor = MathHelper.clamp_double(scaleFactor, 0, maxScaleFactor);
		if (opening) {
			scaleFactor = animUtil.easeOut(prog, 0, maxScaleFactor, .5);
			blurring = animUtil.easeOut(484, 0, 2, 1);
		} else {
			scaleFactor = maxScaleFactor - animUtil.easeIn(prog, 0, maxScaleFactor, .3);
			blurring = 2 - animUtil.easeOut(484, 0, 2, 1);
			if (scaleFactor < .1) {
		        this.mc.displayGuiScreen((GuiScreen)null);

	            if (this.mc.currentScreen == null)
	            {
	                this.mc.setIngameFocus();
	            }
			}
		}
		if (!Client.INSTANCE.getModuleManager().getModuleByClass(ClickGui.class).getValueByName("ClickGuiAnimations").getBooleanValue()) {
			scaleFactor = maxScaleFactor;
		}
	}
	
	public final List<ElementPanel> getPanels() {
		return panels;
	}
	
	public final double getScaleFactor() {
		return scaleFactor;
	}
	
	public final ElementPanel getPanelByTitle(String panelTitle) {
		for (final ElementPanel panel : getPanels()) {
			if (panel.getPanelTitle().equalsIgnoreCase(panelTitle)) {
				return panel;
			}
		}
		return null;
	}

}
