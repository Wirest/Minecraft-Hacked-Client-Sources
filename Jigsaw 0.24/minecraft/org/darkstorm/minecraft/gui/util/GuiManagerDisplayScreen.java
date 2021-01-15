package org.darkstorm.minecraft.gui.util;

import java.awt.Rectangle;
import java.io.IOException;

import org.darkstorm.minecraft.gui.GuiManager;
import org.darkstorm.minecraft.gui.component.Component;
import org.darkstorm.minecraft.gui.component.Frame;
import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.gui.custom.SearchBar;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;

public class GuiManagerDisplayScreen extends GuiScreen {
	private final GuiManager guiManager;
	public final SearchBar searchBar;
	public boolean bg = false;
	public float displayTime = 0;

	public GuiManagerDisplayScreen(GuiManager guiManager) {
		this.guiManager = guiManager;
		this.searchBar = new SearchBar();
		bg = false;
	}

	public GuiManagerDisplayScreen(GuiManager guiManager, boolean b) {
		this.guiManager = guiManager;
		this.searchBar = new SearchBar();
		bg = b;
	}

	@Override
	public void initGui() {
		displayTime = 0;
		Keyboard.enableRepeatEvents(true);
		if (!bg) {
			searchBar.init();
		}
		else {
			this.buttonList.add(new GuiButton(3299, width - 100, height - 20, 100, 20, "Back"));
		}
		super.initGui();
	}

	@Override
	protected void mouseClicked(int x, int y, int button) throws IOException {
		x *= mc.gameSettings.guiScale / 2d;
		y *= mc.gameSettings.guiScale / 2d;
		super.mouseClicked(x, y, button); // This line throws IOException which
											// is why this method has to have
											// the 'throws' declaration
		for (Frame frame : guiManager.getFrames()) {
			if (!frame.isVisible())
				continue;
			if (!frame.isMinimized() && !frame.getArea().contains(x, y)) {
				for (Component component : frame.getChildren()) {
					for (Rectangle area : component.getTheme().getUIForComponent(component)
							.getInteractableRegions(component)) {
						if (area.contains(x - frame.getX() - component.getX(), y - frame.getY() - component.getY())) {
							frame.onMousePress(x - frame.getX(), y - frame.getY(), button);
							guiManager.bringForward(frame);
							return;
						}
					}
				}
			}
		}
		for (Frame frame : guiManager.getFrames()) {
			if (!frame.isVisible())
				continue;
			if (!frame.isMinimized() && frame.getArea().contains(x, y)) {
				frame.onMousePress(x - frame.getX(), y - frame.getY(), button);
				guiManager.bringForward(frame);
				break;
			} else if (frame.isMinimized()) {
				for (Rectangle area : frame.getTheme().getUIForComponent(frame).getInteractableRegions(frame)) {
					if (area.contains(x - frame.getX(), y - frame.getY())) {
						frame.onMousePress(x - frame.getX(), y - frame.getY(), button);
						guiManager.bringForward(frame);
						return;
					}
				}
			}
		}
		searchBar.mouseClicked(x, y, button);
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		super.actionPerformed(button);
		if(button.id == 3299) {
			mc.displayGuiScreen(new GuiMainMenu());
		}
	}
	
	@Override
	public void mouseReleased(int x, int y, int button) {
		x *= mc.gameSettings.guiScale / 2d;
		y *= mc.gameSettings.guiScale / 2d;
		super.mouseReleased(x, y, button);
		for (Frame frame : guiManager.getFrames()) {
			if (!frame.isVisible())
				continue;
			if (!frame.isMinimized() && !frame.getArea().contains(x, y)) {
				for (Component component : frame.getChildren()) {
					for (Rectangle area : component.getTheme().getUIForComponent(component)
							.getInteractableRegions(component)) {
						if (area.contains(x - frame.getX() - component.getX(), y - frame.getY() - component.getY())) {
							frame.onMouseRelease(x - frame.getX(), y - frame.getY(), button);
							guiManager.bringForward(frame);
							return;
						}
					}
				}
			}
		}
		for (Frame frame : guiManager.getFrames()) {
			if (!frame.isVisible())
				continue;
			if (!frame.isMinimized() && frame.getArea().contains(x, y)) {
				frame.onMouseRelease(x - frame.getX(), y - frame.getY(), button);
				guiManager.bringForward(frame);
				break;
			} else if (frame.isMinimized()) {
				for (Rectangle area : frame.getTheme().getUIForComponent(frame).getInteractableRegions(frame)) {
					if (area.contains(x - frame.getX(), y - frame.getY())) {
						frame.onMouseRelease(x - frame.getX(), y - frame.getY(), button);
						guiManager.bringForward(frame);
						return;
					}
				}
			}
		}
		searchBar.mouseReleased(x, y, button);
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		super.keyTyped(typedChar, keyCode);
		if (!bg) {
			searchBar.keyTyped(typedChar, keyCode);
		}

	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		//drawRect(0, 0, width, height, 0x6a000000);
		//drawDefaultBackground();
		
		drawString(fontRendererObj, "§7Right-click a hack to show its options", 2,
				height - (fontRendererObj.FONT_HEIGHT + 1) * 2, 0xffffff);
		drawString(fontRendererObj, "§7Left-click a hack to toggle it", 2,
				height - (fontRendererObj.FONT_HEIGHT + 1) * 3, 0xffffff);
		drawString(fontRendererObj, "§7Do .fontsize <size> to change the GUI size", 2,
				height - (fontRendererObj.FONT_HEIGHT + 1), 0xffffff);
		displayTime += partialTicks;
		float scale = displayTime / 100;
		if (bg) {
			drawGradientRect(0, 0, width, height, 0xff000000, 0xff000000);
			GlStateManager.disableTexture2D();
		}
		if (!bg) {
			searchBar.render(width, height, mouseX, mouseY);
		}
		GlStateManager.scale(2d / mc.gameSettings.guiScale, 2d / mc.gameSettings.guiScale, 1);
		guiManager.render();
		

		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	public void updateScreen() {
		if (bg) {
			guiManager.update();
		} else {
			searchBar.update();
		}
		super.updateScreen();
	}

	@Override
	public void onGuiClosed() {
		displayTime = 0;
		Keyboard.enableRepeatEvents(false);
		super.onGuiClosed();
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

}