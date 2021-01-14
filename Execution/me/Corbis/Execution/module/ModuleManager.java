package me.Corbis.Execution.module;

import me.Corbis.Execution.module.implementations.*;
import me.Corbis.Execution.module.implementations.targets.*;
import me.Corbis.Execution.utils.NameProtect;

import java.util.ArrayList;

public class ModuleManager {
    ArrayList<Module> modules = new ArrayList<>();
    public ModuleManager(){
        modules.add(new Sprint());
        modules.add(new Step());
        modules.add(new ClickGui());
        modules.add(new BlockAnimation());
        modules.add(new Minimap());
        modules.add(new HUD());
        modules.add(new Flight());
        modules.add(new Speed());
        modules.add(new Villagers());
        modules.add(new Invisibles());
        modules.add(new Teams());
        modules.add(new Mobs());
        modules.add(new Players());
        modules.add(new Aura());
        modules.add(new OnScreenRadar());
        modules.add(new InventoryManager());
        modules.add(new NoHurtCam());
        modules.add(new Stealer());
        modules.add(new Velocity());
        modules.add(new NoSlow());
        modules.add(new Scaffold());
        modules.add(new AntiBot());
        modules.add(new NoFall());
        modules.add(new GuiMove());
        modules.add(new AutoArmor());
        modules.add(new Phase());
        modules.add(new HypixelVertFly());
        modules.add(new AntiVoid());
        modules.add(new Criticals());
        modules.add(new Unstuck());
        modules.add(new PingSpoof());
        modules.add(new AACAura());
        modules.add(new Ambiance());
        modules.add(new ESP());
        modules.add(new Longjump());
        modules.add(new TabGui());
        modules.add(new SoundVisualizer());
        modules.add(new ScoreboardMover());
        modules.add(new Disabler());
        modules.add(new FakeHacker());
        modules.add(new AntiWeb());
        modules.add(new Jesus());
        modules.add(new NoRotate());
        modules.add(new InfiniteAura());
        modules.add(new HighJump());
        modules.add(new Parkour());
        modules.add(new NameTags());
        modules.add(new Chams());
        modules.add(new TargetStrafe());
        modules.add(new Teleport());
        modules.add(new FakeLag());
        modules.add(new Killsults());
        modules.add(new StaffDetection());
        modules.add(new Tracker()       );
        modules.add(new TargetHUD());
        modules.add(new Dead());
        modules.add(new CakeEater());
        modules.add(new Derp());
        modules.add(new AIPvP());
        modules.add(new Tracers());
        modules.add(new AntiFlag());
        modules.add(new ServerCrasher());
        modules.add(new AutoPot());
        modules.add(new NameProtect());
        modules.add(new ItemPhysics());
        modules.add(new ChatBypass());
        modules.add(new Spammer());
        modules.add(new HackerDetect());
        modules.add(new FPSBoost());
        modules.add(new Skeletons());
        modules.add(new TestModule());
        modules.add(new PPESP());
        modules.add(new ChatCleaner());
        modules.add(new FullBright());
        modules.add(new HoverDisabler());
        modules.add(new ChestAura());
        modules.add(new Keystrokes());
        modules.add(new FastLadder());









    }
    public <T extends Module> T getModule(Class<T> clazz) {
        return (T) modules.stream().filter(mod -> mod.getClass() == clazz).findFirst().orElse(null);
    }
    public ArrayList<Module> getModules() {
        return modules;
    }
    public ArrayList<Module> getEnabledModules(){
        ArrayList<Module> enabled = new ArrayList<>();
        for(Module m : modules){
            if(m.isEnabled){
                enabled.add(m);
            }else {
                enabled.remove(m);
            }
        }
        return enabled;
    }
    public Module getModuleByName(String name){
        for(Module m : modules){
            if (m.getName().equalsIgnoreCase(name)){
                return m;
            }
        }
        return null;
    }

    public ArrayList<Module> getModulesInCategory(Category category){
        ArrayList<Module> categoryModules = new ArrayList<>();
        for(Module m : modules){
            if (m.getCategory() == category){
                categoryModules.add(m);
            }
        }
        return categoryModules;
    }
}
