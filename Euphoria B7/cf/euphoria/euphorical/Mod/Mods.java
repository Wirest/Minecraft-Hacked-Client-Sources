// 
// Decompiled by Procyon v0.5.30
// 

package cf.euphoria.euphorical.Mod;

import java.util.Iterator;
import java.util.Comparator;
import cf.euphoria.euphorical.Mod.Collection.Player.Zoot;
import cf.euphoria.euphorical.Mod.Collection.Player.Velocity;
import cf.euphoria.euphorical.Mod.Collection.Misc.TrollPot;
import cf.euphoria.euphorical.Mod.Collection.Render.Tracers;
import cf.euphoria.euphorical.Mod.Collection.Render.StorageESP;
import cf.euphoria.euphorical.Mod.Collection.Movement.Step;
import cf.euphoria.euphorical.Mod.Collection.Movement.Sprint;
import cf.euphoria.euphorical.Mod.Collection.Movement.Speed;
import cf.euphoria.euphorical.Mod.Collection.Player.PingSpoof;
import cf.euphoria.euphorical.Mod.Collection.World.Phase;
import cf.euphoria.euphorical.Mod.Collection.Movement.NoSlow;
import cf.euphoria.euphorical.Mod.Collection.Player.NoRotate;
import cf.euphoria.euphorical.Mod.Collection.Misc.Lagger;
import cf.euphoria.euphorical.Mod.Collection.Misc.KillerPot;
import cf.euphoria.euphorical.Mod.Collection.Misc.Keybinds;
import cf.euphoria.euphorical.Mod.Collection.Combat.KeepSprint;
import cf.euphoria.euphorical.Mod.Collection.World.Jesus;
import cf.euphoria.euphorical.Mod.Collection.Player.InvMove;
import cf.euphoria.euphorical.Mod.Collection.Render.Gui;
import cf.euphoria.euphorical.Mod.Collection.Render.Gamma;
import cf.euphoria.euphorical.Mod.Collection.Render.Freecam;
import cf.euphoria.euphorical.Mod.Collection.Movement.Flight;
import cf.euphoria.euphorical.Mod.Collection.Player.FastPlace;
import cf.euphoria.euphorical.Mod.Collection.Render.EntityESP;
import cf.euphoria.euphorical.Mod.Collection.Render.DankBobbing;
import cf.euphoria.euphorical.Mod.Collection.Misc.Damage;
import cf.euphoria.euphorical.Mod.Collection.Misc.CreativeDrop;
import cf.euphoria.euphorical.Mod.Collection.Misc.Crashvil;
import cf.euphoria.euphorical.Mod.Collection.Misc.Commands;
import cf.euphoria.euphorical.Mod.Collection.Player.Blink;
import cf.euphoria.euphorical.Mod.Collection.Combat.AutoPot;
import cf.euphoria.euphorical.Mod.Collection.Combat.AutoArmor;
import cf.euphoria.euphorical.Mod.Collection.Combat.Aura;
import cf.euphoria.euphorical.Mod.Collection.World.AntiPrick;
import cf.euphoria.euphorical.Mod.Collection.Combat.Aimbot;
import java.util.ArrayList;

public class Mods
{
    private ArrayList<Mod> mods;
    
    public Mods() {
        this.mods = new ArrayList<Mod>();
        this.add(new Aimbot());
        this.add(new AntiPrick());
        this.add(new Aura());
        this.add(new AutoArmor());
        this.add(new AutoPot());
        this.add(new Blink());
        this.add(new Commands());
        this.add(new Crashvil());
        this.add(new CreativeDrop());
        this.add(new Damage());
        this.add(new DankBobbing());
        this.add(new EntityESP());
        this.add(new FastPlace());
        this.add(new Flight());
        this.add(new Freecam());
        this.add(new Gamma());
        this.add(new Gui());
        this.add(new InvMove());
        this.add(new Jesus());
        this.add(new KeepSprint());
        this.add(new Keybinds());
        this.add(new KillerPot());
        this.add(new Lagger());
        this.add(new NoRotate());
        this.add(new NoSlow());
        this.add(new Phase());
        this.add(new PingSpoof());
        this.add(new Speed());
        this.add(new Sprint());
        this.add(new Step());
        this.add(new StorageESP());
        this.add(new Tracers());
        this.add(new TrollPot());
        this.add(new Velocity());
        this.add(new Zoot());
        this.mods.sort(new Comparator<Mod>() {
            @Override
            public int compare(final Mod o1, final Mod o2) {
                return o1.getModName().length() - o2.getModName().length();
            }
        });
        this.mods.trimToSize();
    }
    
    private void add(final Mod mod) {
        this.mods.add(mod);
    }
    
    public ArrayList<Mod> getMods() {
        this.mods.sort(new Comparator<Mod>() {
            @Override
            public int compare(final Mod o1, final Mod o2) {
                return o1.getModName().length() - o2.getModName().length();
            }
        });
        this.mods.trimToSize();
        return this.mods;
    }
    
    public Mod getMod(final Class<? extends Mod> theMod) {
        for (final Mod mod : this.getMods()) {
            if (mod.getClass() == theMod) {
                return mod;
            }
        }
        return null;
    }
    
    public Mod getMod(final String theMod) {
        for (final Mod mod : this.getMods()) {
            if (mod.getName().equalsIgnoreCase(theMod)) {
                return mod;
            }
        }
        return null;
    }
}
