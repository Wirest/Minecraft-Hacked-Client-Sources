package org.m0jang.crystal.Mod;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import org.m0jang.crystal.Mod.Collection.Combat.AACAura;
import org.m0jang.crystal.Mod.Collection.Combat.AntiBots;
import org.m0jang.crystal.Mod.Collection.Combat.Aura;
import org.m0jang.crystal.Mod.Collection.Combat.AutoArmor;
import org.m0jang.crystal.Mod.Collection.Combat.AutoClicker;
import org.m0jang.crystal.Mod.Collection.Combat.AutoPot;
import org.m0jang.crystal.Mod.Collection.Combat.AutoSoup;
import org.m0jang.crystal.Mod.Collection.Combat.BowAimbot;
import org.m0jang.crystal.Mod.Collection.Combat.Criticals;
import org.m0jang.crystal.Mod.Collection.Combat.Firion;
import org.m0jang.crystal.Mod.Collection.Combat.LegitAura;
import org.m0jang.crystal.Mod.Collection.Combat.Reach;
import org.m0jang.crystal.Mod.Collection.Combat.Regen;
import org.m0jang.crystal.Mod.Collection.Combat.TPAura;
import org.m0jang.crystal.Mod.Collection.Combat.Velocity;
import org.m0jang.crystal.Mod.Collection.Misc.Bowfly;
import org.m0jang.crystal.Mod.Collection.Misc.Commands;
import org.m0jang.crystal.Mod.Collection.Misc.CrashSkull;
import org.m0jang.crystal.Mod.Collection.Misc.Crashvil;
import org.m0jang.crystal.Mod.Collection.Misc.Derp;
import org.m0jang.crystal.Mod.Collection.Misc.ExtraEnchant;
import org.m0jang.crystal.Mod.Collection.Misc.FastUse;
import org.m0jang.crystal.Mod.Collection.Misc.FlashSkin;
import org.m0jang.crystal.Mod.Collection.Misc.FreeRotate;
import org.m0jang.crystal.Mod.Collection.Misc.Ghost;
import org.m0jang.crystal.Mod.Collection.Misc.InventoryCleaner;
import org.m0jang.crystal.Mod.Collection.Misc.NameProtect;
import org.m0jang.crystal.Mod.Collection.Misc.Palalyze;
import org.m0jang.crystal.Mod.Collection.Misc.Panic;
import org.m0jang.crystal.Mod.Collection.Misc.Suicide;
import org.m0jang.crystal.Mod.Collection.Movement.AirJump;
import org.m0jang.crystal.Mod.Collection.Movement.Blink;
import org.m0jang.crystal.Mod.Collection.Movement.Chams;
import org.m0jang.crystal.Mod.Collection.Movement.FastLadder;
import org.m0jang.crystal.Mod.Collection.Movement.FastSwim;
import org.m0jang.crystal.Mod.Collection.Movement.Fly;
import org.m0jang.crystal.Mod.Collection.Movement.InvMove;
import org.m0jang.crystal.Mod.Collection.Movement.Jesus;
import org.m0jang.crystal.Mod.Collection.Movement.LongJump;
import org.m0jang.crystal.Mod.Collection.Movement.NoFall;
import org.m0jang.crystal.Mod.Collection.Movement.NoSlow;
import org.m0jang.crystal.Mod.Collection.Movement.NoVoid;
import org.m0jang.crystal.Mod.Collection.Movement.SelfStuck;
import org.m0jang.crystal.Mod.Collection.Movement.SmartMoving;
import org.m0jang.crystal.Mod.Collection.Movement.Sneak;
import org.m0jang.crystal.Mod.Collection.Movement.Speed;
import org.m0jang.crystal.Mod.Collection.Movement.Sprint;
import org.m0jang.crystal.Mod.Collection.Movement.Step;
import org.m0jang.crystal.Mod.Collection.Render.Ambience;
import org.m0jang.crystal.Mod.Collection.Render.ClickGUI;
import org.m0jang.crystal.Mod.Collection.Render.ESP;
import org.m0jang.crystal.Mod.Collection.Render.FPSHurtCam;
import org.m0jang.crystal.Mod.Collection.Render.Freecam;
import org.m0jang.crystal.Mod.Collection.Render.FullBright;
import org.m0jang.crystal.Mod.Collection.Render.Hats;
import org.m0jang.crystal.Mod.Collection.Render.Hud;
import org.m0jang.crystal.Mod.Collection.Render.NameTags;
import org.m0jang.crystal.Mod.Collection.Render.NoFov;
import org.m0jang.crystal.Mod.Collection.Render.NoHurtCam;
import org.m0jang.crystal.Mod.Collection.Render.NoRenderDrops;
import org.m0jang.crystal.Mod.Collection.Render.NoWeather;
import org.m0jang.crystal.Mod.Collection.Render.RealBobbing;
import org.m0jang.crystal.Mod.Collection.Render.Search;
import org.m0jang.crystal.Mod.Collection.Render.StorageESP;
import org.m0jang.crystal.Mod.Collection.Render.Tracers;
import org.m0jang.crystal.Mod.Collection.Render.ViewClip;
import org.m0jang.crystal.Mod.Collection.Render.Wallhack;
import org.m0jang.crystal.Mod.Collection.Render.Wings;
import org.m0jang.crystal.Mod.Collection.World.AntiCactus;
import org.m0jang.crystal.Mod.Collection.World.ArisPhase;
import org.m0jang.crystal.Mod.Collection.World.ChestAura;
import org.m0jang.crystal.Mod.Collection.World.ChestSteal;
import org.m0jang.crystal.Mod.Collection.World.CreativeDrop;
import org.m0jang.crystal.Mod.Collection.World.FastBreak;
import org.m0jang.crystal.Mod.Collection.World.FastPlace;
import org.m0jang.crystal.Mod.Collection.World.KillerPot;
import org.m0jang.crystal.Mod.Collection.World.NexusBroker;
import org.m0jang.crystal.Mod.Collection.World.Nuker;
import org.m0jang.crystal.Mod.Collection.World.Phase;
import org.m0jang.crystal.Mod.Collection.World.Scaffold;
import org.m0jang.crystal.Mod.Collection.World.SkipClip;
import org.m0jang.crystal.Mod.Collection.World.TestClip;
import org.m0jang.crystal.Mod.Collection.World.TrollPot;

public class Modules {
   private ArrayList modList = new ArrayList();

   public Modules() {
      this.add(new AntiCactus());
      this.add(new Aura());
      this.add(new NoRenderDrops());
      this.add(new FastUse());
      this.add(new Criticals());
      this.add(new LegitAura());
      this.add(new Wallhack());
      this.add(new AutoArmor());
      this.add(new Regen());
      this.add(new AutoPot());
      this.add(new Hud());
      this.add(new Blink());
      this.add(new NameTags());
      this.add(new Hats());
      this.add(new LongJump());
      this.add(new AutoSoup());
      this.add(new AutoClicker());
      this.add(new Commands());
      this.add(new CreativeDrop());
      this.add(new Suicide());
      this.add(new RealBobbing());
      this.add(new FastPlace());
      this.add(new Fly());
      this.add(new Freecam());
      this.add(new FullBright());
      this.add(new InvMove());
      this.add(new Jesus());
      this.add(new FreeRotate());
      this.add(new NoWeather());
      this.add(new NoSlow());
      this.add(new ESP());
      this.add(new Phase());
      this.add(new Speed());
      this.add(new Sprint());
      this.add(new Step());
      this.add(new StorageESP());
      this.add(new SkipClip());
      this.add(new Tracers());
      this.add(new Velocity());
      this.add(new Derp());
      this.add(new ChestSteal());
      this.add(new AntiBots());
      this.add(new Scaffold());
      this.add(new InventoryCleaner());
      this.add(new FastBreak());
      this.add(new AirJump());
      this.add(new NoFov());
      this.add(new NoFall());
      this.add(new ChestAura());
      this.add(new Firion());
      this.add(new FastLadder());
      this.add(new ViewClip());
      this.add(new NoHurtCam());
      this.add(new SmartMoving());
      this.add(new Palalyze());
      this.add(new FlashSkin());
      this.add(new Nuker());
      this.add(new SelfStuck());
      this.add(new NoVoid());
      this.add(new Sneak());
      this.add(new FastSwim());
      this.add(new NameProtect());
      this.add(new KillerPot());
      this.add(new TrollPot());
      this.add(new Crashvil());
      this.add(new Ghost());
      this.add(new Reach());
      this.add(new Panic());
      this.add(new AACAura());
      this.add(new TPAura());
      this.add(new ArisPhase());
      this.add(new Bowfly());
      this.add(new Search());
      this.add(new NexusBroker());
      this.add(new BowAimbot());
      this.add(new ClickGUI());
      this.add(new FPSHurtCam());
      this.add(new CrashSkull());
      this.add(new ExtraEnchant());
      this.add(new Chams());
      this.add(new TestClip());
      this.add(new Ambience());
      this.add(new Wings());
      
      this.modList.sort(new Comparator<Module>() {
         public int compare(Module o1, Module o2) {
            return o1.getName().length() - o2.getName().length();
         }
      });
      this.modList.trimToSize();
   }

   private void add(Module mod) {
      this.modList.add(mod);
   }

   public Module get(Class theMod) {
      if (this.getModList() == null) {
         return null;
      } else {
         Iterator var3 = this.getModList().iterator();

         while(var3.hasNext()) {
            Module mod = (Module)var3.next();
            if (mod.getClass() == theMod) {
               return mod;
            }
         }

         return null;
      }
   }

   public Module get(String theMod) {
      Iterator var3 = this.getModList().iterator();

      while(var3.hasNext()) {
         Module mod = (Module)var3.next();
         if (mod.getName().equalsIgnoreCase(theMod)) {
            return mod;
         }
      }

      return null;
   }

   public ArrayList getModList() {
      return this.modList;
   }
}
