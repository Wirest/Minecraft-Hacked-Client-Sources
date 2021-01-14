package info.sigmaclient.gui.screen.component;

public class InvManagerItem {
	boolean selected;
	int id;
	InvManagerSlot slot;
	float y;
	public InvManagerItem(InvManagerSlot slot, int id, boolean selected){
		this.slot = slot;
		this.id = id;
		this.selected = selected;
	}
	
	public InvManagerSlot getSlot(){
		return this.slot;
	}
	
	public int getId(){
		return this.id;
	}
	
	public float getY(){
		return this.y;
	}
	
	public boolean isSelected(){
		return this.selected;
	}
	
	public void setSlot(InvManagerSlot  slot){
		this.slot = slot;
	}
	
	public void setY(float y){
		this.y = y;
	}
	
	public void setId(int id){
		this.id = id;
	}
	
	public void setSelected(boolean selected){
		this.selected = selected;
	}
}
