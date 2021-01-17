package skyline.specc.managers;

import skyline.specc.managers.File.Manager;
import skyline.specc.render.main.Renderer;

public class OverlayManager extends Manager<Renderer>{

	public void removeOverlay(String name){
		for(int i = 0; i < getContents().size(); i++){
			Renderer overlay = getContents().get(i);
			if(overlay.getName() == name){
				this.removeContent(overlay);
				break;
			}
		}
	}
	
}
