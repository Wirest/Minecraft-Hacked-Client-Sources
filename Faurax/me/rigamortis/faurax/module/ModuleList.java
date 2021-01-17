package me.rigamortis.faurax.module;

import me.rigamortis.faurax.module.modules.ui.*;
import me.rigamortis.faurax.module.modules.movement.*;
import me.rigamortis.faurax.module.modules.render.*;
import me.rigamortis.faurax.module.modules.misc.*;
import me.rigamortis.faurax.module.modules.world.*;
import me.rigamortis.faurax.module.modules.player.*;
import me.rigamortis.faurax.module.modules.combat.*;
import java.util.*;

public class ModuleList
{
    public ArrayList<Module> mods;
    
    public ModuleList() {
        (this.mods = new ArrayList<Module>()).add(new ESP());
        this.mods.add(new Sprint());
        this.mods.add(new Sneak());
        this.mods.add(new AntiKnockback());
        this.mods.add(new Criticals());
        this.mods.add(new Ghost());
        this.mods.add(new NoItems());
        this.mods.add(new Speed());
        this.mods.add(new KillAura());
        this.mods.add(new NoSwing());
        this.mods.add(new ClickPearl());
        this.mods.add(new SafeWalk());
        this.mods.add(new Brightness());
        this.mods.add(new MoreInv());
        this.mods.add(new Trajectories());
        this.mods.add(new FastPlace());
        this.mods.add(new KeepSprint());
        this.mods.add(new NoRemoveEntity());
        this.mods.add(new LongJump());
        this.mods.add(new KeepSneak());
        this.mods.add(new QuickAttack());
        this.mods.add(new WayPoints());
        this.mods.add(new Control());
        this.mods.add(new NoSlowdown());
        this.mods.add(new PlaceAnywhere());
        this.mods.add(new LEETSpeak());
        this.mods.add(new Godmode());
        this.mods.add(new IdiotSpeek());
        this.mods.add(new NoGlitchBlocks());
        this.mods.add(new CivBreak());
        this.mods.add(new InfDura());
        this.mods.add(new InvMove());
        this.mods.add(new QuakeCraft());
        this.mods.add(new Interact());
        this.mods.add(new FastUse());
        this.mods.add(new AntiVanish());
        this.mods.add(new CommandBot());
        this.mods.add(new AutoFarm());
        this.mods.add(new NoClip());
        this.mods.add(new Reach());
        this.mods.add(new Xray());
        this.mods.add(new ItemSpoof());
        this.mods.add(new Revive());
        this.mods.add(new Tracers());
        this.mods.add(new Search());
        this.mods.add(new Step());
        this.mods.add(new NoBreakAnimation());
        this.mods.add(new Jesus());
        this.mods.add(new GUI());
        this.mods.add(new NameTags());
        this.mods.add(new StorageESP());
        this.mods.add(new Regen());
        this.mods.add(new NoFriendDmg());
        this.mods.add(new SpeedMine());
        this.mods.add(new AutoTool());
        this.mods.add(new InstantSell());
        this.mods.add(new Flight());
        this.mods.add(new NoFall());
        this.mods.add(new Zoot());
        this.mods.add(new Blink());
        this.mods.add(new LessHungerLoss());
        this.mods.add(new AutoTpAccept());
        this.mods.add(new Chams());
        this.mods.add(new CopsAndCrims());
        this.mods.add(new FastBow());
        this.mods.add(new AntiAim());
        this.mods.add(new ChestStealer());
        this.mods.add(new Nuker());
        this.mods.add(new Triggerbot());
        this.mods.add(new FreeCam());
        this.mods.add(new BowAimbot());
    }
    
    public boolean isModToggled(final String name) {
        boolean state = false;
        for (final Module mod : this.mods) {
            if (mod.isToggled() && mod.getName().equalsIgnoreCase(name)) {
                state = true;
            }
        }
        return state;
    }
}
