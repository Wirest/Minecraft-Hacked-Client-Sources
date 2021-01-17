package skyline.specc.extras.tabgui;

import org.lwjgl.input.Keyboard;

import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.ModType;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.Module;
import skyline.specc.SkyLine;
import skyline.specc.render.modules.tabgui.main.TabPart;

public class TabTypePart extends TabPart {

	private ModType type;

	public TabTypePart(ModType type, TabPanel parent){
		super(Character.toString(type.toString().toLowerCase().charAt(0)).toUpperCase() + type.toString().toLowerCase().substring(1), parent);
		this.type = type;
		
		
	}
	
	@Override
	public void onKeyPress(int key) {
		if(key == Keyboard.KEY_RIGHT){
			TabPanel panel = new TabPanel(this.getParent().getTabGui());
			panel.setVisible(true);

			for(Module module : SkyLine.getManagers().getModuleManager().getContents()){
				if(module.getType() == type){
					TabModulePart mButton = new TabModulePart(module, panel);

					panel.addElement(mButton);
				}
			}

			if(panel.getElements().isEmpty()) return;

			this.getParent().getTabGui().addPanel(panel);
		}
	}
	
	public ModType getType() {
		return type;
	}

}
