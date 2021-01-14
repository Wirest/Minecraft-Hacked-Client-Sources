package splash.client.modules;

import splash.api.module.Module;
import splash.api.module.category.ModuleCategory;

/**
 * Author: Ice
 * Created: 00:27, 30-May-20
 * Project: Client
 */
public class NullModule extends Module {

    public NullModule() {
        super("ERROR", "ERROR", ModuleCategory.COMBAT);
    }
}
