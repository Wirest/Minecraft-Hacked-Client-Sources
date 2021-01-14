package info.sigmaclient.gui.screen.component;

import net.minecraft.item.ItemStack;

public class InvManagerSlot {
	int id,x,y;
	InvManagerItem item;
	public InvManagerSlot(int id, int x, int y, InvManagerItem item){
		this.id = id;
		this.x = x;
		this.y = y;
		this.item = item;
	}
	public int getID(){
		return this.id;
	}
	public int getX(){
		return this.x;
	}
	public int getY(){
		return this.y;
	}
	public InvManagerItem getItem(){
		return this.item;
	}
	public void setId(int id){
		this.id = id;
	}
	public void setX(int x){
		this.x = x;
	}
	public void setY(int y){
		this.y = y;
	}
	public void setItem(InvManagerItem item){
		this.item = item;
	}
}
