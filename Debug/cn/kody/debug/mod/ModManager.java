package cn.kody.debug.mod;

import cn.kody.debug.mod.Mod;
import cn.kody.debug.mod.mods.COMBAT.*;
import cn.kody.debug.mod.mods.MOVEMENT.*;
import cn.kody.debug.mod.mods.PLAYER.*;
import cn.kody.debug.mod.mods.RENDER.*;
import cn.kody.debug.mod.mods.WORLD.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import net.minecraft.client.Minecraft;

public class ModManager {
    public static ArrayList<Mod> modList = new ArrayList();

    public ModManager() {
        modList.add(new NoCommand());
        modList.add(new ClickGui());
        modList.add(new HUD());
        modList.add(new Sprint());
        modList.add(new NoSlowDown());
        modList.add(new Fullbright());
        modList.add(new DamageParticle());
        modList.add(new ItemESP());
        modList.add(new ESP());
        modList.add(new Velocity());
        modList.add(new BlockESP());
        modList.add(new ChestESP());
        modList.add(new AutoHeal());
        modList.add(new SpeedMine());
        modList.add(new AutoArmor());
        modList.add(new InvManager());
        modList.add(new AutoSword());
        modList.add(new RealBobbing());
        modList.add(new Scaffold());
        modList.add(new Speed());
        modList.add(new KillAura());
        modList.add(new AntiBot());
        modList.add(new Fly());
        modList.add(new Xray());
        modList.add(new NoFall());
        modList.add(new InvMove());
        modList.add(new AntiFall());
        modList.add(new Criticals());
        modList.add(new BlockAnimation());
        modList.add(new Disabler());
        modList.add(new AutoTool());
        modList.add(new AimAssist());
        modList.add(new AutoClicker());
        modList.add(new Hitbox());
        modList.add(new Reach());
        modList.add(new WTap());
        //MoveMent
        modList.add(new FakeLag());
        modList.add(new Jesus());
        modList.add(new Step());
        
        modList.add(new ItemPhysic());
        //player
        modList.add(new AntiAim());

        modList.add(new NameTag());
        modList.add(new Trajectory());
        modList.add(new ViewClip());
        
        modList.add(new AntiObsidian());
        modList.add(new AutoGGBan());
        modList.add(new FastPlace());
        modList.add(new MurderFinder());

    }

    public static ArrayList getEnabledModListHUD() {
        ArrayList<Mod> enabledModList = new ArrayList<Mod>();
        for (Mod m : modList) {
            if (!m.isEnabled()) continue;
            enabledModList.add(m);
        }
        return enabledModList;
    }

    public static Mod getModByName(String mod) {
        for (Mod m : modList) {
            if (!m.getName().equalsIgnoreCase(mod)) continue;
            return m;
        }
        return null;
    }

    public static ArrayList<Mod> getModList() {
        return modList;
    }

    public static Mod getMod(final Class<? extends Mod> clazz) {
        for (final Mod mod : ModManager.modList) {
            if (mod.getClass() == clazz) {
                return mod;
            }
        }
        return null;
    }


    public static ArrayList getEnabled() {
        final ArrayList<Mod> list = new ArrayList<Mod>();
        for (final Mod mod : ModManager.modList) {
            if (mod.isEnabled() && mod.getCategory() != Category.RENDER) {
                list.add(mod);
            }
        }
        return list;
    }

}

