package cedo.modules;

import cedo.Fan;
import cedo.modules.combat.*;
import cedo.modules.customize.*;
import cedo.modules.exploit.Antibot;
import cedo.modules.exploit.FastPlace;
import cedo.modules.exploit.SigmaDelete;
import cedo.modules.movement.*;
import cedo.modules.player.*;
import cedo.modules.render.*;

import java.util.concurrent.CopyOnWriteArrayList;

public class ModuleManager {
    public static CopyOnWriteArrayList<Module> modules = Fan.modules;

    public static void init() {
        modules.add(Fan.fly = new Fly());
        modules.add(new AutoPot());
        modules.add(new LongJump());
        modules.add(new Sprint());
        modules.add(new FullBright());
        modules.add(new NoFall());
        modules.add(Fan.tabgui = new TabGUI());
        modules.add(Fan.killaura = new Killaura());
        modules.add(Fan.clickGui = new ClickGUI());
        modules.add(Fan.speed = new Speed());
        modules.add(new FastPlace());
        modules.add(new Nametags());
        modules.add(Fan.esp = new ESP());
        modules.add(Fan.inventoryManager = new InventoryManager());
        modules.add(Fan.chams = new Chams());
        modules.add(new NoBob());
        modules.add(Fan.hudMod = new HudModule());
        modules.add(new TimerMod());
        modules.add(new NoSlow());
        modules.add(Fan.scaffold = new Scaffold());
        modules.add(new AimAssist());
        modules.add(new AutoClicker());
        modules.add(new AntiKB());
        //modules.add(new PingSpoof());
        modules.add(new AutoHypixel());
        modules.add(new Antibot());
        //modules.add(new FastLadder());
        modules.add(new FastStairs());
        modules.add(new ChestAssist());
        modules.add(new AntiServer());
        modules.add(new ChestStealer());
        modules.add(new AutoArmor());
        modules.add(new AntiVoid());
        modules.add(Fan.effects = new Animations());
        modules.add(new InventoryMove());
        modules.add(new Step());
        modules.add(new Jesus());
        //modules.add(new LongJump());
        modules.add(new Criticals());
        // modules.add(new Tracers());
        // modules.add(new BadScaffold());
        //modules.add(new DevTest());
        modules.add(Fan.arraylist = new Arraylist());
        modules.add(Fan.notificationsMod = new NotificationsMod());
        // modules.add(new AutoJump());
        modules.add(Fan.targetHud = new TargetHUD());
        modules.add(new SigmaDelete());
        modules.add(new Phase());
        modules.add(Fan.chestesp = new ChestESP());
        modules.add(new esp2d());
        //modules.add(new Trajectories());
        modules.add(new WorldTime());
        modules.add(new Skeletons());
        modules.add(new Keystrokes2());
        modules.add(Fan.targetStrafe = new TargetStrafe());
        modules.add(new ModuleIndicator());
        modules.add(Fan.statistics = new Statistics());
        modules.add(Fan.reach = new Reach());
        modules.add(Fan.itemCustomization = new ItemCustomization());
    }
}
