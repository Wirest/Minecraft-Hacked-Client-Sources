package skyline.specc.render.modules.tabgui;

import skyline.specc.SkyLine;
import skyline.specc.render.main.Renderer;

public class TabGuiOverlay extends Renderer {
	
	public TabGuiOverlay(){
		super("TabGui");
	}
	
	@Override
	public void render() {
		SkyLine.getVital().getTabGui().onRender();
	}
	
}
