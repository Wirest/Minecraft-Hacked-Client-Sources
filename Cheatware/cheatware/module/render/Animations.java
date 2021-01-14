package cheatware.module.render;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import cheatware.Cheatware;
import cheatware.module.Category;
import cheatware.module.Module;
import de.Hero.settings.Setting;

public class Animations extends Module {

    public Animations() {
        super("Animations", Keyboard.KEY_NONE, Category.RENDER);
    }	
    
     @Override
     public void setup() {
    	  ArrayList<String> options = new ArrayList<>();
    	  options.add("Exhibition");
    	  options.add("1.7");
    	  options.add("Spin");
    	  options.add("Ambitious");
    	  options.add("Sigma");
    	  options.add("Tap");
    	  options.add("Slide");
    	  options.add("Remix");
    	  options.add("Cheatware");
          Cheatware.instance.settingsManager.rSetting(new Setting("Animation Mode", this, "Exhibition", options));
     }
    
}
