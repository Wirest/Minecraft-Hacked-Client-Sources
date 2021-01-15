package info.spicyclient.blockCoding;

import info.spicyclient.ClickGUI.Tab;

public class Block extends Tab {
	
	public Block(Color color) {
		this.color = color;
	}
	
	public Block connected;
	public Color color;
	public int x = 0, y = 0, offsetX = 0, offsetY = 0, status = 0;
	
	public Block getConnected() {
		return connected;
	}

	public void setConnected(Block connected) {
		this.connected = connected;
	}
	
}
