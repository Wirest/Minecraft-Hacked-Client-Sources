package skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.util.file;

import org.lwjgl.input.Keyboard;

public class KeyBind {

	private String name;
	private int key;

	public KeyBind(String name, int key){
		this.name = name;
		this.key = key;
	}

	public boolean isPressed(){
		return Keyboard.isKeyDown(key);
	}

	public String getName(){
		return name;
	}

	public void setName(String name){
		this.name = name;
	}

	public int getKey(){
		return key;
	}

	public void setKey(int key){
		this.key = key;
	}

}
