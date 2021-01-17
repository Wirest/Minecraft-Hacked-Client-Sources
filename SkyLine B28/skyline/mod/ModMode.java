package skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod;

import net.minecraft.client.Mineman;
import skyline.specc.helper.Player;

public class ModMode<T> {

	protected T parent;
	
	private String name;
	
	public static Player p;
	public static Mineman mc;

	public ModMode(T parent, String name) {
		this.parent = parent;
		this.name = name;
	}
	
	public T getParent() {
		return parent;
	}

	public String getName() {
		return name;
	}

	public void onEnable(){}
	
	public void onDisable(){}
	
}
