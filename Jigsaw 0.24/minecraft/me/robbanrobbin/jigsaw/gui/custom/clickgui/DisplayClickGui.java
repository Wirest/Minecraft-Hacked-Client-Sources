package me.robbanrobbin.jigsaw.gui.custom.clickgui;

import java.io.IOException;
import java.util.Collections;

import com.google.common.collect.Lists;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.gui.custom.SearchBar;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.utils.GuiUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;

public class DisplayClickGui extends GuiScreen {
	
	private int lastX;
	private int lastY;
	
	private boolean dragged = false;
	
	private boolean mouseDown = false;
	
	private boolean inMenu = false;
	
	public final SearchBar searchBar;
	
	public DisplayClickGui(boolean b) {
		this.inMenu = b;
		this.searchBar = new SearchBar();
	}

	@Override
	public void initGui() {
		super.initGui();
		if (!inMenu) {
			searchBar.init();
		}
		else {
			this.buttonList.add(new GuiButton(3299, width - 100, height - 20, 100, 20, "Back"));
		}
		Jigsaw.getClickGuiManager().globalAlphaMinus = 1f;
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		super.keyTyped(typedChar, keyCode);
		if (!inMenu) {
			searchBar.keyTyped(typedChar, keyCode);
		}
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		super.actionPerformed(button);
		if(button.id == 3299) {
			mc.displayGuiScreen(new GuiMainMenu());
		}
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		mouseX *= mc.gameSettings.guiScale / 2d;
		mouseY *= mc.gameSettings.guiScale / 2d;
		super.drawScreen(mouseX, mouseY, partialTicks);
		if(inMenu) {
			drawDefaultBackground();
		}
		for(ModuleWindow wind : Lists.reverse(Jigsaw.getClickGuiManager().windows)) {
			if(mouseX > wind.getX() && mouseX < wind.getX() + wind.getWidth()
					&& mouseY > wind.getY() && mouseY < wind.getY() + wind.getHeight()) {
				wind.onHover(mouseX - wind.getX(), mouseY - wind.getY());
				break;
			}
		}
		GuiUtils.partialTicks = partialTicks;
		ClickGuiManager manager = Jigsaw.getClickGuiManager();
		manager.mouseX = mouseX;
		manager.mouseY = mouseY;
		manager.draw();
		if (!inMenu) {
			searchBar.render(width, height, mouseX, mouseY);
		}
	}
	
	@Override
	public void updateScreen() {
		if(!inMenu) {
			searchBar.update();
		}
		if(Jigsaw.getClickGuiManager().globalAlphaMinus > 0.0f) {
			Jigsaw.getClickGuiManager().globalAlphaMinus -= Jigsaw.getClickGuiManager().getAlphaDec();
		}
		if(Jigsaw.getClickGuiManager().globalAlphaMinus <= 0.0f) {
			Jigsaw.getClickGuiManager().globalAlphaMinus = 0.0f;
		}
		ClickGuiManager manager = Jigsaw.getClickGuiManager();
		manager.update();
		super.updateScreen();
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		mouseX *= mc.gameSettings.guiScale / 2d;
		mouseY *= mc.gameSettings.guiScale / 2d;
		mouseDown = true;
		lastX = mouseX;
		lastY = mouseY;
		for(ModuleWindow wind : Lists.reverse(Jigsaw.getClickGuiManager().windows)) {
			if(mouseX > wind.getX() && mouseX < wind.getX() + wind.getWidth()
					&& mouseY > wind.getY() && mouseY < wind.getY() + wind.getHeight()) {
				wind.onClicked(mouseX - wind.getX(), mouseY - wind.getY(), mouseButton);
				break;
			}
		}
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		mouseX *= mc.gameSettings.guiScale / 2d;
		mouseY *= mc.gameSettings.guiScale / 2d;
		mouseDown = false;
		dragged = false;
		for(ModuleWindow wind : Jigsaw.getClickGuiManager().windows) {
			wind.onReleased(state);
		}
		for(ModuleWindow wind : Lists.reverse(Jigsaw.getClickGuiManager().windows)) {
			if(mouseX > wind.getX() && mouseX < wind.getX() + wind.getWidth()
					&& mouseY > wind.getY() && mouseY < wind.getY() + wind.getHeight()) {
				wind.onReleased(mouseX - wind.getX(), mouseY - wind.getY(), state);
				break;
			}
		}
		super.mouseReleased(mouseX, mouseY, state);
	}
	
	@Override
	protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
		mouseX *= mc.gameSettings.guiScale / 2d;
		mouseY *= mc.gameSettings.guiScale / 2d;
		dragged = true;
		for(ModuleWindow wind : Lists.reverse(Jigsaw.getClickGuiManager().windows)) {
			if(mouseX >= wind.getX() && mouseX < wind.getX() + wind.getWidth()
					&& mouseY >= wind.getY() && mouseY < wind.getY() + wind.getHeight()) {
				wind.onDragged(mouseX - lastX, mouseY - lastY, mouseX - wind.getX(), mouseY - wind.getY(), clickedMouseButton);
				break;
			}
		}
		lastX = mouseX;
		lastY = mouseY;
		super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
	
}
