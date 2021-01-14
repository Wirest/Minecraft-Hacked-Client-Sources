package info.sigmaclient.management.users;

import info.sigmaclient.Client;
import info.sigmaclient.gui.click.ClickGui;
import info.sigmaclient.module.Module;

public class PremiumLoader {

    public boolean load() {
        String[] premiumModulesNames = {"HypixelScaffoldAddon", "PremiumFlyAddon", "AutoClutch", "LongjumpMineplexAddon", "PremiumBhopAddon", "PremiumAntiBotAddon", "PremiumTeleportAddon", "PremiumInfiniteAuraAddon"};
        try {
            for (String name : premiumModulesNames) {
                Class c = getClass().getClassLoader().loadClass("info.sigmaclient.module.impl.premium." + name);
                Module module = (Module) c.newInstance();
                if (module.shouldRegister()) {
                    Client.getModuleManager().add(module);
                }
            }
            Client.clickGui = new ClickGui();
            Module.loadSettings();
            return true;
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
        return false;
    }
}
