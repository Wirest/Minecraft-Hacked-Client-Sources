package splash.client.managers.module;

import java.util.ArrayList;

import splash.api.manager.ClientManager;
import splash.api.module.Module;
import splash.api.module.category.ModuleCategory;
import splash.client.modules.NullModule;
import splash.client.modules.combat.AntiBot;
import splash.client.modules.combat.Aura;
import splash.client.modules.combat.AutoHeal;
import splash.client.modules.combat.Criticals;
import splash.client.modules.combat.TargetStrafe;
import splash.client.modules.combat.Velocity;
import splash.client.modules.misc.Autoplay;
import splash.client.modules.misc.CakeEater;
import splash.client.modules.misc.Disabler;
import splash.client.modules.misc.Spammer;
import splash.client.modules.movement.AntiVoid;
import splash.client.modules.movement.FlagDetector;
import splash.client.modules.movement.Flight;
import splash.client.modules.movement.InventoryMove;
import splash.client.modules.movement.NoSlow;
import splash.client.modules.movement.Phase;
import splash.client.modules.movement.Speed;
import splash.client.modules.movement.Sprint;
import splash.client.modules.movement.Step;
import splash.client.modules.player.ChestStealer;
import splash.client.modules.player.InventoryManager;
import splash.client.modules.player.Killsults;
import splash.client.modules.player.NoFall;
import splash.client.modules.player.NoRotate;
import splash.client.modules.player.Scaffold;
import splash.client.modules.visual.Ambiance;
import splash.client.modules.visual.Animations;
import splash.client.modules.visual.ESP;
import splash.client.modules.visual.Freecam;
import splash.client.modules.visual.Gamma;
import splash.client.modules.visual.HitEffects;
import splash.client.modules.visual.ItemPhysics;
import splash.client.modules.visual.Nametags;
import splash.client.modules.visual.UI;

/**
 * Author: Ice
 * Created: 00:25, 30-May-20
 * Project: Client
 */
public class ModuleManager extends ClientManager<Module> {

    public ModuleManager() {
	    addContent(new Flight());
	    addContent(new UI());
	    addContent(new Speed());
	    addContent(new Sprint());
	    addContent(new InventoryMove());
	    addContent(new Aura());
	    addContent(new NoSlow());
	    addContent(new NoRotate());
	    addContent(new NoFall());
	    addContent(new Gamma());
	    addContent(new Disabler());
	    addContent(new Velocity());
	    addContent(new Freecam());
	    addContent(new Ambiance());
	    addContent(new TargetStrafe());
	    addContent(new Scaffold());
	    addContent(new ChestStealer());
	    addContent(new Spammer());
	    addContent(new AntiVoid());
	    addContent(new Nametags());
	    addContent(new ESP());
	    addContent(new Animations());
	    addContent(new InventoryManager());
	    addContent(new AntiBot());
	    addContent(new Criticals());
	    addContent(new Killsults());
	    addContent(new CakeEater());
	    addContent(new Step());
	    addContent(new FlagDetector());
	    addContent(new Phase());
	    addContent(new Autoplay());
	    addContent(new AutoHeal());
	    addContent(new HitEffects());
	    addContent(new ItemPhysics());
    }

    @Override
    public String managerName() {
        return "ModuleManager";
    }

    public Module getModuleByDisplayName(String moduleDisplayName) {
        for(Module module : getContents()) {
            if(module.getModuleDisplayName().equalsIgnoreCase(moduleDisplayName)) {
                return module;
            }
        }
        return new NullModule();
    }

    public Module getModuleByClass(Class clazz) {
        for(Module module : getContents()) {
            if(module.getClass() == clazz) {
                return module;
            }
        }
        return new NullModule();
    }

    public ArrayList<Module> getModulesInCategory(ModuleCategory moduleCategory) {
        ArrayList<Module> modules = new ArrayList<>();
        for(Module module : getContents()) {
            if(module.getModuleCategory() == moduleCategory) {
                modules.add(module);
            }
        }
        return modules;
    }

    public ArrayList<Module> getModulesForRender() {
        ArrayList<Module> modulesForRender = new ArrayList<>();
        for(Module module : getContents()) {
            if(module.isModuleActive() && checkVisibility(module)) {
                modulesForRender.add(module);
            }
        }
        return modulesForRender;
    }

    public boolean checkVisibility(Module module) {
        return module != getModuleByClass(UI.class);
    }
}
