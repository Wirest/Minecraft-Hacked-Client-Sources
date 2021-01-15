// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.module;

import java.util.Iterator;
import me.CheerioFX.FusionX.FusionX;
import java.util.List;
import me.CheerioFX.FusionX.module.modules.AutoChestSteal;
import me.CheerioFX.FusionX.module.modules.AutoAreaClick;
import me.CheerioFX.FusionX.GUI.clickgui.Targets;
import me.CheerioFX.FusionX.GUI.clickgui.Restrictions;
import me.CheerioFX.FusionX.module.modules.FastBow;
import me.CheerioFX.FusionX.GUI.clickgui.UI;
import me.CheerioFX.FusionX.GUI.GUI;
import me.CheerioFX.FusionX.module.modules.ClearSaves;
import me.CheerioFX.FusionX.module.modules.SuperMagnet;
import me.CheerioFX.FusionX.module.modules.Scaffold;
import me.CheerioFX.FusionX.module.modules.Safewalk;
import me.CheerioFX.FusionX.module.modules.SkinDerp;
import me.CheerioFX.FusionX.module.modules.HideKeyBinds;
import me.CheerioFX.FusionX.module.modules.NoSlowDown;
import me.CheerioFX.FusionX.module.modules.DankBobbing;
import me.CheerioFX.FusionX.module.modules.NoItems;
import me.CheerioFX.FusionX.module.modules.LiquidWalk;
import me.CheerioFX.FusionX.module.modules.AutoPot;
import me.CheerioFX.FusionX.module.modules.TPLocation;
import me.CheerioFX.FusionX.module.modules.InfiniteEggAura;
import me.CheerioFX.FusionX.module.modules.Search;
import me.CheerioFX.FusionX.module.modules.StorageESP;
import me.CheerioFX.FusionX.module.modules.InstantRegen;
import me.CheerioFX.FusionX.module.modules.Nametags;
import me.CheerioFX.FusionX.module.modules.ChestStealer;
import me.CheerioFX.FusionX.module.modules.InstantDrop;
import me.CheerioFX.FusionX.module.modules.InventoryMove;
import me.CheerioFX.FusionX.module.modules.Extended_Reach;
import me.CheerioFX.FusionX.module.modules.Teleport;
import me.CheerioFX.FusionX.module.modules.AntiFire;
import me.CheerioFX.FusionX.module.modules.AutoArmor;
import me.CheerioFX.FusionX.module.modules.AntiKB;
import me.CheerioFX.FusionX.module.modules.NoCameraShake;
import me.CheerioFX.FusionX.module.modules.Regen;
import me.CheerioFX.FusionX.module.modules.SafeFall;
import me.CheerioFX.FusionX.module.modules.Blink;
import me.CheerioFX.FusionX.module.modules.GhostAura;
import me.CheerioFX.FusionX.module.modules.PotionSaver;
import me.CheerioFX.FusionX.module.modules.Sneak;
import me.CheerioFX.FusionX.module.modules.AutoRespawn;
import me.CheerioFX.FusionX.module.modules.InstantStop;
import me.CheerioFX.FusionX.module.modules.FreeCam;
import me.CheerioFX.FusionX.module.modules.NoNegitiveEffects;
import me.CheerioFX.FusionX.module.modules.NoRain;
import me.CheerioFX.FusionX.module.modules.NoFall;
import me.CheerioFX.FusionX.module.modules.SkyWalker;
import me.CheerioFX.FusionX.module.modules.Longjump;
import me.CheerioFX.FusionX.module.modules.Critical;
import me.CheerioFX.FusionX.module.modules.Speed;
import me.CheerioFX.FusionX.module.modules.AirJump;
import me.CheerioFX.FusionX.module.modules.Step;
import me.CheerioFX.FusionX.module.modules.Timer;
import me.CheerioFX.FusionX.module.modules.GhostClient;
import me.CheerioFX.FusionX.module.modules.Spambot;
import me.CheerioFX.FusionX.module.modules.Tracers;
import me.CheerioFX.FusionX.module.modules.ESP;
import me.CheerioFX.FusionX.module.modules.NoRightClickDelay;
import me.CheerioFX.FusionX.module.modules.FullBright;
import me.CheerioFX.FusionX.module.modules.Suicide;
import me.CheerioFX.FusionX.module.modules.Flight;
import me.CheerioFX.FusionX.module.modules.Sprint;
import me.CheerioFX.FusionX.module.modules.InfiniteAura;
import me.CheerioFX.FusionX.module.modules.KillAura;
import java.util.ArrayList;

public class ModuleManager
{
    public static ArrayList<Module> activeModules;
    
    static {
        ModuleManager.activeModules = new ArrayList<Module>();
    }
    
    public ModuleManager() {
        ModuleManager.activeModules.add(new KillAura());
        ModuleManager.activeModules.add(new InfiniteAura());
        ModuleManager.activeModules.add(new Sprint());
        ModuleManager.activeModules.add(new Flight());
        ModuleManager.activeModules.add(new Suicide());
        ModuleManager.activeModules.add(new FullBright());
        ModuleManager.activeModules.add(new NoRightClickDelay());
        ModuleManager.activeModules.add(new ESP());
        ModuleManager.activeModules.add(new Tracers());
        ModuleManager.activeModules.add(new Spambot());
        ModuleManager.activeModules.add(new GhostClient());
        ModuleManager.activeModules.add(new Timer());
        ModuleManager.activeModules.add(new Step());
        ModuleManager.activeModules.add(new AirJump());
        ModuleManager.activeModules.add(new Speed());
        ModuleManager.activeModules.add(new Critical());
        ModuleManager.activeModules.add(new Longjump());
        ModuleManager.activeModules.add(new SkyWalker());
        ModuleManager.activeModules.add(new NoFall());
        ModuleManager.activeModules.add(new NoRain());
        ModuleManager.activeModules.add(new NoNegitiveEffects());
        ModuleManager.activeModules.add(new FreeCam());
        ModuleManager.activeModules.add(new InstantStop());
        ModuleManager.activeModules.add(new AutoRespawn());
        ModuleManager.activeModules.add(new Sneak());
        ModuleManager.activeModules.add(new PotionSaver());
        ModuleManager.activeModules.add(new GhostAura());
        ModuleManager.activeModules.add(new Blink());
        ModuleManager.activeModules.add(new SafeFall());
        ModuleManager.activeModules.add(new Regen());
        ModuleManager.activeModules.add(new NoCameraShake());
        ModuleManager.activeModules.add(new AntiKB());
        ModuleManager.activeModules.add(new AutoArmor());
        ModuleManager.activeModules.add(new AntiFire());
        ModuleManager.activeModules.add(new Teleport());
        ModuleManager.activeModules.add(new Extended_Reach());
        ModuleManager.activeModules.add(new InventoryMove());
        ModuleManager.activeModules.add(new InstantDrop());
        ModuleManager.activeModules.add(new ChestStealer());
        ModuleManager.activeModules.add(new Nametags());
        ModuleManager.activeModules.add(new InstantRegen());
        ModuleManager.activeModules.add(new StorageESP());
        ModuleManager.activeModules.add(new Search());
        ModuleManager.activeModules.add(new InfiniteEggAura());
        ModuleManager.activeModules.add(new TPLocation());
        ModuleManager.activeModules.add(new AutoPot());
        ModuleManager.activeModules.add(new LiquidWalk());
        ModuleManager.activeModules.add(new NoItems());
        ModuleManager.activeModules.add(new DankBobbing());
        ModuleManager.activeModules.add(new NoSlowDown());
        ModuleManager.activeModules.add(new HideKeyBinds());
        ModuleManager.activeModules.add(new SkinDerp());
        ModuleManager.activeModules.add(new Safewalk());
        ModuleManager.activeModules.add(new Scaffold());
        ModuleManager.activeModules.add(new SuperMagnet());
        ModuleManager.activeModules.add(new ClearSaves());
        ModuleManager.activeModules.add(new GUI());
        ModuleManager.activeModules.add(new UI());
        ModuleManager.activeModules.add(new FastBow());
        ModuleManager.activeModules.add(new Restrictions());
        ModuleManager.activeModules.add(new Targets());
        ModuleManager.activeModules.add(new AutoAreaClick());
        ModuleManager.activeModules.add(new AutoChestSteal());
    }
    
    public static ArrayList<Module> getModules() {
        return ModuleManager.activeModules;
    }
    
    private static List<Module> getModulesInCategory(final Category category) {
        final List<Module> modules = new ArrayList<Module>();
        final ModuleManager moduleManager = FusionX.theClient.moduleManager;
        for (final Module m : getModules()) {
            if (m.getCategory() == category) {
                modules.add(m);
            }
        }
        return modules;
    }
    
    public Module getModuleByName(final String name) {
        for (final Module mod : getModules()) {
            if (mod.getName().equalsIgnoreCase(name)) {
                return mod;
            }
        }
        return null;
    }
    
    public Module getModule(final Class<? extends Module> theClass) {
        for (final Module mod : getModules()) {
            if (mod.getClass() == theClass) {
                return mod;
            }
        }
        return null;
    }
}
