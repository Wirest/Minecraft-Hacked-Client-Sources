package VenusClient.online.Module.impl.Render;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import VenusClient.online.Client;
import VenusClient.online.Module.Category;
import VenusClient.online.Module.Module;
import de.Hero.settings.Setting;


/**
 *  Made by HeroCode
 *  it's free to use
 *  but you have to credit me
 *
 *  @author HeroCode
 */
public class ClickGui extends Module{

    public ClickGui()
    {
        super("ClickGUI", "ClickGUI", Keyboard.KEY_RSHIFT, Category.RENDER);
    }

    //Setup is called in the Module con
    @Override
    public void setup(){
    	ArrayList<String> options = new ArrayList<>();
    	options.add("JellyLike");
    	options.add("New");
    	Client.instance.setmgr.rSetting(new Setting("Design", this, "New", options));
    	Client.instance.setmgr.rSetting(new Setting("Sound", this, false));
    	Client.instance.setmgr.rSetting(new Setting("GuiRed", this, 255, 0, 255, true));
    	Client.instance.setmgr.rSetting(new Setting("GuiGreen", this, 26, 0, 255, true));
    	Client.instance.setmgr.rSetting(new Setting("GuiBlue", this, 42, 0, 255, true));
    }
    
    @Override
    public void onEnable()
    {
    	/**
    	 * Einfach in der StartMethode
    	 * clickgui = new ClickGUI(); ;)
    	 */
    	mc.displayGuiScreen(Client.instance.clickgui);
    	toggle();
    	super.onEnable();
    }
}
