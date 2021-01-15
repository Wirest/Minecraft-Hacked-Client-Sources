package nivia.managers;

import nivia.modules.Hooked;
import nivia.modules.Module;
import nivia.modules.Module.Category;
import nivia.modules.combat.*;
import nivia.modules.exploits.*;
import nivia.modules.ghost.*;
import nivia.modules.miscellanous.*;
import nivia.modules.movement.*;
import nivia.modules.player.*;
import nivia.modules.render.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class ModuleManager {
    public static List<Module> mods = new ArrayList<>();
    private Hooked hooked;
    public ModuleManager() {

        mods.add(new AutoArmor());
        //mods.add(new AutoEat());
        mods.add(new AutoPot());
        mods.add(new AntiBot());
        mods.add(new AutoSoup());
        mods.add(new Criticals());
        mods.add(new KillAura());
        mods.add(new NoVelocity());
        mods.add(new Regen());
        mods.add(new BowAimbot());
        mods.add(new AutoBypass());

        mods.add(new ChestStealer());
        mods.add(new AutoTool());
        mods.add(new Scaffold());
        mods.add(new InventoryMove());
        mods.add(new AutoAccept());
        mods.add(new Paralyze());
        mods.add(new AntiHunger());

        mods.add(new AntiAim());
        mods.add(new InventoryCleaner());

        mods.add(new Jesus());
        mods.add(new Dolphin());
        mods.add(new NoSlow());
        mods.add(new Fly());
        mods.add(new LongHop());
        mods.add(new NoRotate());
        mods.add(new Speed());
        mods.add(new Step());
        mods.add(new Glide());
        mods.add(new Sprint());

        mods.add(new FastUse());
        mods.add(new Teleport());
        mods.add(new Firion());
        mods.add(new InventoryPlus());
        mods.add(new NoFall());
        mods.add(new Phase());
        mods.add(new ThrowPot());
        mods.add(new Refill());
        mods.add(new CivBreak());

        mods.add(new Blink());
        mods.add(new Nuker());
        mods.add(new Fastplace());
        mods.add(new Freecam());
        mods.add(new SpeedMine());
        mods.add(new Sneak());
        mods.add(new Timer());

        mods.add(new SmoothAim());
        mods.add(new TriggerBot());
        mods.add(new AutoClicker());

        //mods.add(new Notifications());
        mods.add(new ESP());
        mods.add(new Chams());
        mods.add(new Projectiles());
        mods.add(new Search());
        mods.add(new StorageESP());
        mods.add(new FullBright());
        mods.add(new Nametags());
        mods.add(new MurderMysterys());
        mods.add(new NoRender());
        //mods.add(new TestMod());
        mods.add(new Tracers());
        mods.add(new WayPoints());
        mods.add(new ClickGUI());
        mods.add(new GUI());

        hooked = new Hooked();
    }
    private List<Module> getModules() {
        return Collections.unmodifiableList(mods);
    }
    public ArrayList<Module> getModulesInCategory(Category cat) {
        ArrayList<Module> mods = new ArrayList();
        getModules().stream().filter(m -> m.getCategory() == cat).forEach(mods::add);
        return mods;
    }
    public Module getModule(Class<? extends Module> clazz) {
        return getModules().stream().filter(m -> m.getClass() == clazz).findFirst().orElse(null);
    }
    public Module getModbyName(String name){
        return getModules().stream().filter(m -> m.getName().equalsIgnoreCase(name) || m.getTag().equalsIgnoreCase(name)).findFirst().orElse(null);
    }
    public Module getModByAlias(String alias) {
        for (Module mod : getModules()) {
            for (String aliases : mod.getAlias()) {
                if (aliases.equalsIgnoreCase(alias)){
                    return mod;
                }
            }
        }
        return null;
    }
    public boolean getModState(String name){
        AtomicReference<Boolean> state = new AtomicReference<>();
        getModules().stream().filter(m -> m.getName() == name).forEach(m -> state.set(m.State));
        return state.get();
    }
    public ArrayList<Module> enabledModules(){
        ArrayList<Module> emods = new ArrayList<>();
        mods.stream().filter(Module::getState).forEach(emods::add);
        mods.stream().filter(emods::contains).filter(m -> !m.getState()).forEach(emods::remove);
        return emods;
    }


}