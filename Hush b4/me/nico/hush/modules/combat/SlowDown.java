// 
// Decompiled by Procyon v0.5.36
// 

package me.nico.hush.modules.combat;

import de.Hero.settings.Setting;
import me.nico.hush.Client;
import java.util.ArrayList;
import me.nico.hush.modules.Category;
import me.nico.hush.modules.Module;

public class SlowDown extends Module
{
    public SlowDown() {
        super("SlowDown", "SlowDown", 16777215, 0, Category.COMBAT);
        final ArrayList<String> mode = new ArrayList<String>();
        mode.add("NCP");
        mode.add("AAC");
        mode.add("Custom");
        Client.instance.settingManager.rSetting(new Setting("Mode", "SlowDownMode", this, "Custom", mode));
        Client.instance.settingManager.rSetting(new Setting("SlowDown §l\u2192", "SlowDownValue", this, 1.2, 0.5, 3.5, false));
    }
}
