package cheatware.module;

import java.util.ArrayList;

import cheatware.module.combat.AntiBot;
import cheatware.module.combat.AutoArmor;
import cheatware.module.combat.Criticals;
import cheatware.module.combat.KillAura;
import cheatware.module.combat.NoSlow;
import cheatware.module.movement.Fly;
import cheatware.module.movement.GuiMove;
import cheatware.module.movement.Phase;
import cheatware.module.movement.Speed;
import cheatware.module.movement.Sprint;
import cheatware.module.movement.Step;
import cheatware.module.player.AntiVoid;
import cheatware.module.player.ChestSteal;
import cheatware.module.player.NoFall;
import cheatware.module.player.Reload;
import cheatware.module.player.Scaffold;
import cheatware.module.player.SelfDestruct;
import cheatware.module.player.ServerCrasher;
import cheatware.module.render.Animations;
import cheatware.module.render.BoxESP;
import cheatware.module.render.ClickGUI;
import cheatware.module.render.Fullbright;
import cheatware.module.render.Hud;
import cheatware.module.render.NoBob;

public class ModuleManager {
    private ArrayList<Module> modules = new ArrayList<Module>();

    public ModuleManager() {
        modules.add(new KillAura());
        modules.add(new AntiBot());
        modules.add(new AutoArmor());
        modules.add(new Criticals());
        modules.add(new Sprint());
        modules.add(new Fly());
        modules.add(new Step());
        modules.add(new Speed());
        modules.add(new Phase());
        modules.add(new Fullbright());
        modules.add(new ClickGUI());
        modules.add(new NoFall());
        modules.add(new Reload());
        modules.add(new ChestSteal());
        modules.add(new NoSlow());
        modules.add(new SelfDestruct());
        modules.add(new Hud());
        modules.add(new Scaffold());
        modules.add(new AntiVoid());
        modules.add(new ServerCrasher());
        modules.add(new BoxESP());
        modules.add(new NoBob());
        modules.add(new GuiMove());
        modules.add(new Animations());
    }

    public ArrayList<Module> getModules() {
        return modules;
    }
    public Module getModuleByName(String name) {
        return modules.stream().filter(module -> module.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }
}
