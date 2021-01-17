/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  cpw.mods.fml.common.Loader
 *  cpw.mods.fml.common.ModContainer
 */
package delta;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;

public class Class55 {
    private static String incurred$;

    public static String _party() {
        if (Class55._guilty()) {
            incurred$ = "V";
            System.out.println("env is V... wot?");
        } else if (Class55._supplier()) {
            incurred$ = "F";
            if (Class55._option()) {
                incurred$ = incurred$ + "-P";
            } else if (Class55._remove()) {
                incurred$ = incurred$ + "-D";
            }
        } else {
            incurred$ = "C-MCP";
            System.out.println("env is C-MCP... WHAT THE FUCK");
        }
        return incurred$;
    }

    public static boolean _supplier() {
        try {
            Class.forName("cpw.mods.fml.common.launcher.FMLTweaker");
            return 217 - 251 + 68 + -33;
        }
        catch (Exception exception) {
            return 260 - 302 + 84 - 49 + 7;
        }
    }

    public static boolean _guilty() {
        return (!Class55._remove() && !Class55._option() && !Class55._supplier() ? 83 - 114 + 108 + -76 : 273 - 438 + 422 + -257) != 0;
    }

    public static boolean _remove() {
        for (ModContainer modContainer : Loader.instance().getModList()) {
            if (!modContainer.getName().toLowerCase().contains("decimation")) continue;
            return 226 - 245 + 23 + -3;
        }
        return 152 - 280 + 230 - 136 + 34;
    }

    public static boolean _option() {
        try {
            Class.forName("cpw.mods.fml.common.discovery.PalaDiscoverer");
            return 169 - 261 + 5 - 2 + 90;
        }
        catch (Exception exception) {
            return 121 - 133 + 111 - 26 + -73;
        }
    }
}

