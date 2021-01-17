package skyline.specc.render.modules.tabgui.main;

import skyline.specc.extras.tabgui.TabPanel;

public class TabPart extends TabObject {
	
	private String text;
	private boolean selected;
	private int y;
	private int x;

	public TabPart(String text, TabPanel parent){
		super(parent);
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	
}
