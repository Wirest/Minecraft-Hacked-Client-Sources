// 
// Decompiled by Procyon v0.5.36
// 

package me.nico.hush.modules;

import java.util.Iterator;
import me.nico.hush.Client;
import de.Hero.example.GUI;
import me.nico.hush.modules.render.HUD;
import me.nico.hush.modules.render.ESP;
import me.nico.hush.modules.render.WallHack;
import me.nico.hush.modules.misc.NameProtect;
import me.nico.hush.modules.movement.Fly;
import me.nico.hush.modules.movement.Bhop;
import me.nico.hush.modules.movement.Step;
import me.nico.hush.modules.render.Items;
import me.nico.hush.modules.render.NoBob;
import me.nico.hush.modules.world.Tower;
import me.nico.hush.modules.movement.Speed;
import me.nico.hush.modules.movement.Glide;
import me.nico.hush.modules.movement.BugUp;
import me.nico.hush.modules.combat.Teams;
import me.nico.hush.modules.player.NoFall;
import me.nico.hush.modules.movement.Sprint;
import me.nico.hush.modules.player.Parkour;
import me.nico.hush.modules.player.FastUse;
import me.nico.hush.modules.misc.Spammer;
import me.nico.hush.modules.world.SafeWalk;
import me.nico.hush.modules.movement.IceSpeed;
import me.nico.hush.modules.movement.HighJump;
import me.nico.hush.modules.movement.LongJump;
import me.nico.hush.modules.player.TNTBlock;
import me.nico.hush.modules.combat.SlowDown;
import me.nico.hush.modules.combat.Velocity;
import me.nico.hush.modules.combat.KillAura;
import me.nico.hush.modules.misc.AutoJoin;
import me.nico.hush.modules.world.FastPlace;
import me.nico.hush.modules.world.Destroyer;
import me.nico.hush.modules.player.AutoArmor;
import me.nico.hush.modules.combat.Criticals;
import me.nico.hush.modules.render.FullBright;
import me.nico.hush.modules.movement.FastLadder;
import me.nico.hush.modules.misc.AutoConfig;
import me.nico.hush.modules.player.AutoRespawn;
import me.nico.hush.modules.player.ChestStealer;
import me.nico.hush.modules.world.ScaffoldWalk;
import me.nico.hush.modules.combat.AntiKnockBack;
import me.nico.hush.modules.player.InventoryCleaner;
import java.util.ArrayList;
import java.util.List;

public class ModuleManager
{
    public List<Module> modules;
    
    public ModuleManager() {
        this.modules = new ArrayList<Module>();
        this.addModule(new InventoryCleaner());
        this.addModule(new AntiKnockBack());
        this.addModule(new ScaffoldWalk());
        this.addModule(new ChestStealer());
        this.addModule(new AutoRespawn());
        this.addModule(new AutoConfig());
        this.addModule(new FastLadder());
        this.addModule(new FullBright());
        this.addModule(new Criticals());
        this.addModule(new AutoArmor());
        this.addModule(new Destroyer());
        this.addModule(new FastPlace());
        this.addModule(new AutoJoin());
        this.addModule(new KillAura());
        this.addModule(new Velocity());
        this.addModule(new SlowDown());
        this.addModule(new TNTBlock());
        this.addModule(new LongJump());
        this.addModule(new HighJump());
        this.addModule(new IceSpeed());
        this.addModule(new SafeWalk());
        this.addModule(new Spammer());
        this.addModule(new FastUse());
        this.addModule(new Parkour());
        this.addModule(new Sprint());
        this.addModule(new NoFall());
        this.addModule(new Teams());
        this.addModule(new BugUp());
        this.addModule(new Glide());
        this.addModule(new Speed());
        this.addModule(new Tower());
        this.addModule(new NoBob());
        this.addModule(new Items());
        this.addModule(new Step());
        this.addModule(new Bhop());
        this.addModule(new Fly());
        this.addModule(new NameProtect());
        this.addModule(new WallHack());
        this.addModule(new ESP());
        this.addModule(new HUD());
        this.addModule(new GUI());
        Client.instance.logger.Info("Loaded Modules: " + this.modules.size());
    }
    
    public void addModule(final Module module) {
        this.modules.add(module);
    }
    
    public List<Module> getModules() {
        return this.modules;
    }
    
    public Module getModuleName(final String moduleName) {
        for (final Module mod : this.modules) {
            if (mod.getName().trim().equalsIgnoreCase(moduleName) || mod.toString().trim().equalsIgnoreCase(moduleName.trim())) {
                return mod;
            }
        }
        return null;
    }
    
    public Module getModule(final Class<? extends Module> clazz) {
        for (final Module m : this.modules) {
            if (m.getClass() == clazz) {
                return m;
            }
        }
        return null;
    }
}
