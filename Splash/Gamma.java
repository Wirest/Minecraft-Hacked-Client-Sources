package splash.client.modules.visual;

import splash.api.module.Module;
import splash.api.module.category.ModuleCategory;

/**
 * Author: Ice
 * Created: 22:22, 11-Jun-20
 * Project: Client
 */
public class Gamma extends Module {

    public Gamma() {
        super("Gamma", "Lightens up your day nigger.", ModuleCategory.VISUALS);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        mc.gameSettings.gammaSetting = 1000F;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        mc.gameSettings.gammaSetting = 1F;
    }
}
