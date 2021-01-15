package VenusClient.online.Module;

import VenusClient.online.Module.impl.Combat.Antibot;
import VenusClient.online.Module.impl.Combat.Criticals;
import VenusClient.online.Module.impl.Combat.Killaura;
import VenusClient.online.Module.impl.Combat.Velocity;
import VenusClient.online.Module.impl.Ghost.AutoClicker;
import VenusClient.online.Module.impl.Ghost.Eagle;
import VenusClient.online.Module.impl.Ghost.GhostMode;
import VenusClient.online.Module.impl.Ghost.GhostVelocity;
import VenusClient.online.Module.impl.Movement.AntiVoid;
import VenusClient.online.Module.impl.Movement.Fly;
import VenusClient.online.Module.impl.Movement.OldFly;
import VenusClient.online.Module.impl.Movement.Phase;
import VenusClient.online.Module.impl.Movement.Speed;
import VenusClient.online.Module.impl.Movement.Sprint;
import VenusClient.online.Module.impl.Movement.TargetStrafe;
import VenusClient.online.Module.impl.Render.ClickGui;
import VenusClient.online.Module.impl.Render.ESP;
import VenusClient.online.Module.impl.Render.HUD;
import VenusClient.online.Module.impl.World.ChestStealer;
import VenusClient.online.Module.impl.World.Disabler;
import VenusClient.online.Module.impl.World.Scaffold;
import VenusClient.online.Module.impl.Player.AutoBypass;
import VenusClient.online.Module.impl.Player.InvManager;
import VenusClient.online.Module.impl.Player.InvMove;
import VenusClient.online.Module.impl.Player.Nofall;
import VenusClient.online.Module.impl.World.ScaffoldSkid;

import java.util.ArrayList;

public class ModuleManager {
    public static ArrayList<Module> modules = new ArrayList<Module>();

    public ModuleManager() {
        //Register Module Here

        //Combat
        modules.add(new Killaura());
        modules.add(new Velocity());
        modules.add(new Criticals());
        modules.add(new Antibot());

        //Movement
        modules.add(new Sprint());
        //modules.add(new OldFly());
        modules.add(new Fly());
        modules.add(new Speed());
        modules.add(new AntiVoid());
        modules.add(new TargetStrafe());
        modules.add(new Phase());
        
        //Render
        modules.add(new ClickGui());
        modules.add(new HUD());
        modules.add(new ESP());

        //Player
        modules.add(new Nofall());
        modules.add(new InvManager());
        modules.add(new InvMove());
        modules.add(new AutoBypass());

        //World
        modules.add(new Disabler());
        modules.add(new ChestStealer());
        modules.add(new Scaffold());

        //Ghost
        modules.add(new AutoClicker());
        modules.add(new GhostMode());
        modules.add(new GhostVelocity());
        modules.add(new Eagle());
    }

    public static ArrayList<Module> getModules() {
        return modules;
    }
    public Module getModuleByName(String name) {
        return modules.stream().filter(module -> module.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }


}
