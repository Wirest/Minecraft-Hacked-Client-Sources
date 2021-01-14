package store.shadowclient.client.module.render;

import store.shadowclient.client.module.Category;
import store.shadowclient.client.module.Module;

public class NameProtect extends Module {

	public static boolean isEnabled = false;
	public static String fakename = "§4Shrunkie";
	
	public NameProtect() {
		super("NameProtect", 0, Category.RENDER);
	}
	
	@Override
	public void onDisable() {
		super.onDisable();
	}
	
	@Override
	public void onEnable() {
		super.onEnable();
	}

}
