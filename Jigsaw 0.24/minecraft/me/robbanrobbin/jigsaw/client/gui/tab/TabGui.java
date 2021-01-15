package me.robbanrobbin.jigsaw.client.gui.tab;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.main.ReturnType;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.client.settings.ClientSettings;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.client.renderer.GlStateManager;

public class TabGui {

	public TabGuiContainer rootContainer;
	
	public static boolean novitex = false;
	
	public int selectedIndex = 0;
	
	boolean maximise = false;

	public TabGui() {
		rootContainer = new TabGuiContainer();
		rootContainer.y = 29;
		rootContainer.x = 3;

		for (Category cat : Category.values()) {
			if (cat == Category.SETTINGS || cat == Category.TARGET || cat == Category.HIDDEN || cat == Category.BYPASSES) {
				continue;
			}
			String name = cat.toString().toLowerCase();
			name = Character.toUpperCase(name.charAt(0)) + name.substring(1);
			TabGuiTitle item = new TabGuiTitle();
			item.title = name;
			TabGuiContainer nextContainer = new TabGuiContainer();
			for (Object o : Jigsaw.filterModulesByCategory(Jigsaw.getModules(), new Category[] { cat },
					ReturnType.NAME)) {
				Module mod = Jigsaw.getModuleByName((String) o);
				TabGuiTitle title = new TabGuiTitle();
				title.title = mod.getName();
				nextContainer.addItem(title);
			}
			item.nextContainer = nextContainer;
			rootContainer.addItem(item);
		}
		rootContainer.selectedIndex = 0;
		rootContainer.layoutItems();
		rootContainer.setValues();
	}

	public void update() {
		if(!ClientSettings.tabGui) {
			return;
		}
		for(TabGuiItem item : rootContainer.getItems()) {
			item.selected = false;
		}
		rootContainer.getItems().get(selectedIndex).selected = true;
		rootContainer.update();
	}

	public void render() {
		rootContainer.y = ClientSettings.bigWaterMark ? 29: 16;
		if(!ClientSettings.tabGui) {
			return;
		}
		rootContainer.render();
	}

	public void onKeyPressed(int keyCode) {
		if(!ClientSettings.tabGui) {
			return;
		}
//		if (keyCode == Keyboard.KEY_UP) {
//			rootContainer.getItems().get(selectedIndex).maximised = false;
//			selectedIndex--;
//		}
//		if (keyCode == Keyboard.KEY_DOWN) {
//			rootContainer.getItems().get(selectedIndex).maximised = false;
//			selectedIndex++;
//		}
//		if (keyCode == Keyboard.KEY_RIGHT) {
//			maximise = true;
//			rootContainer.getItems().get(selectedIndex).maximised = maximise;
//		}
//		if (keyCode == Keyboard.KEY_LEFT) {
//			maximise = false;
//			rootContainer.getItems().get(selectedIndex).maximised = maximise;
//		}
//		if(selectedIndex < 0) {
//			selectedIndex = 0;
//		}
//		if(selectedIndex > rootContainer.getItems().size() - 1) {
//			selectedIndex = rootContainer.getItems().size() - 1;
//		}
		rootContainer.getItems().get(0).keyPressed(keyCode);
	}

}
