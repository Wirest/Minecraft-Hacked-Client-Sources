package skyline.specc.render.modules.tabgui.main;

import skyline.specc.extras.tabgui.TabPanel;

public class TabObject {

	private TabPanel parent;
	
	public TabObject(TabPanel parent) {
		this.parent = parent;
	}

	public void onKeyPress(int key){}

	public TabPanel getParent(){
		return this.parent;
	}
}
