package VenusClient.online.Module.impl.Render;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import VenusClient.online.Client;
import VenusClient.online.Module.Category;
import VenusClient.online.Module.Module;
import de.Hero.settings.Setting;

public class ESP extends Module{

	public ESP() {
		super("ESP", "ESP", Category.RENDER, Keyboard.KEY_NONE);
	}
	
	@Override
	public void setup() {
        ArrayList<String> options = new ArrayList<>();
        options.add("Shader");
        Client.instance.setmgr.rSetting(new Setting("ESP Mode", this, "Shader", options));
	}
	
}
