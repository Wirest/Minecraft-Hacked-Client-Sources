package store.shadowclient.client.module;

import java.util.ArrayList;

import store.shadowclient.client.module.combat.*;
import store.shadowclient.client.module.misc.*;
import store.shadowclient.client.module.movement.*;
import store.shadowclient.client.module.player.*;
import store.shadowclient.client.module.render.*;

public class ModuleManager {
    private ArrayList<Module> modules = new ArrayList<Module>();

    public ModuleManager() {
    	
        // COMBAT
    	modules.add(new TargetStrafe());
    	modules.add(new AutoClicker());
    	modules.add(new AutoGapple());
    	modules.add(new AutoArmor());
        modules.add(new AutoSword());
        modules.add(new Criticals());
        modules.add(new AimAssist());
        modules.add(new AutoSoup());
        modules.add(new FastBow());
        modules.add(new AntiBot());
        modules.add(new Aimbot());
        modules.add(new Reach());
        modules.add(new Regen());
        modules.add(new Aura());
        modules.add(new WTap());
        
        // MOVEMENT
        modules.add(new ToggleSneak());
        modules.add(new WaterSpeed());
        modules.add(new NoSlowdown());
        modules.add(new FastLadder());
        modules.add(new LongJump());
        modules.add(new HighJump());
        modules.add(new Velocity());
        modules.add(new Scaffold());
        modules.add(new AutoWalk());
        modules.add(new FastFall());
        modules.add(new AirMove());
        modules.add(new Dolphin());
        modules.add(new InvMove());
        modules.add(new BoatFly());
        modules.add(new Sprint());
        modules.add(new Strafe());
        modules.add(new NoClip());
        modules.add(new Speed());
        modules.add(new Phase());
        modules.add(new Jesus());
        modules.add(new Eagle());
        modules.add(new NoWeb());
        modules.add(new Glide());
        modules.add(new Blink());
        modules.add(new Step());
        modules.add(new Fly());
        
        // RENDER
        modules.add(new Trajectories());
        modules.add(new NoScoreboard());
        modules.add(new NameProtect());
        modules.add(new ItemPhysics());
        modules.add(new Fullbright());
        modules.add(new DamageHits());
        modules.add(new MotionBlur());
        modules.add(new Skeletons());
        modules.add(new NoHurtCam());
        modules.add(new ESP2DMod());
        modules.add(new Ambiance());
        modules.add(new ChestESP());
        modules.add(new NameTags());
        modules.add(new ClickGUI());
        modules.add(new FreeCam());
        modules.add(new AntiFire());
        modules.add(new WallHack());
        modules.add(new NoRender());
        modules.add(new Tracers());
        modules.add(new ItemESP());
        modules.add(new NoFov());
        modules.add(new NoBob());
        modules.add(new Chams());
        modules.add(new Xray());
        modules.add(new ESP());

        // PLAYER
        modules.add(new InventoryManager());
        modules.add(new InventoryHelper());
        modules.add(new ChestStealer());
        modules.add(new AutoRespawn());
        modules.add(new FastPlace());
        modules.add(new PingSpoof());
        modules.add(new FastBreak());
        modules.add(new AutoPlay());
        modules.add(new AutoTool());
        modules.add(new AutoEat());
        modules.add(new FastEat());
        modules.add(new FastWeb());
        modules.add(new Parkour());
        modules.add(new NoVoid());
        modules.add(new NoFall());
        modules.add(new Timer());
        modules.add(new Nuker());

        // MISC
        //modules.add(new PluginFinder());
        modules.add(new SwordAnimation());
        modules.add(new ServerCrasher());
        modules.add(new CrashSkull());
        modules.add(new AntiVanish());
        modules.add(new Cosmetics());
        modules.add(new KillSults());
        modules.add(new Detector());
        modules.add(new Disabler());
        modules.add(new Spammer());
        modules.add(new Configs());
        modules.add(new Derp());
        modules.add(new HUD());

        // NONE
    }

    public ArrayList<Module> getModules() {
        return modules;
    }
    public Module getModuleByName(String name) {
        return modules.stream().filter(module -> module.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }
}
