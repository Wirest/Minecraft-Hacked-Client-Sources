/*
 * Decompiled with CFR 0_122.
 */
package me.razerboy420.weepcraft.module;

import java.lang.reflect.Field;

import me.razerboy420.weepcraft.Weepcraft;
import me.razerboy420.weepcraft.module.modules.auto.AutoAccept;
import me.razerboy420.weepcraft.module.modules.auto.AutoFish;
import me.razerboy420.weepcraft.module.modules.auto.AutoMine;
import me.razerboy420.weepcraft.module.modules.auto.AutoRespawn;
import me.razerboy420.weepcraft.module.modules.auto.AutoSwitch;
import me.razerboy420.weepcraft.module.modules.auto.AutoTool;
import me.razerboy420.weepcraft.module.modules.auto.AutoWalk;
import me.razerboy420.weepcraft.module.modules.combat.AutoClicker;
import me.razerboy420.weepcraft.module.modules.combat.AutoSoup;
import me.razerboy420.weepcraft.module.modules.combat.NewPot;
import me.razerboy420.weepcraft.module.modules.combat.Velocity;
import me.razerboy420.weepcraft.module.modules.combat.aura.Aura;
import me.razerboy420.weepcraft.module.modules.graphics.AntiBlind;
import me.razerboy420.weepcraft.module.modules.graphics.AntiInvis;
import me.razerboy420.weepcraft.module.modules.graphics.ChestESP;
import me.razerboy420.weepcraft.module.modules.graphics.Crosshair;
import me.razerboy420.weepcraft.module.modules.graphics.ESP;
import me.razerboy420.weepcraft.module.modules.graphics.Fullbright;
import me.razerboy420.weepcraft.module.modules.graphics.GUI;
import me.razerboy420.weepcraft.module.modules.graphics.HUD;
import me.razerboy420.weepcraft.module.modules.graphics.Tracers;
import me.razerboy420.weepcraft.module.modules.misc.AntiAAC;
import me.razerboy420.weepcraft.module.modules.misc.DeathDerp;
import me.razerboy420.weepcraft.module.modules.misc.EggWars;
import me.razerboy420.weepcraft.module.modules.misc.FakeLag;
import me.razerboy420.weepcraft.module.modules.misc.FlashBack;
import me.razerboy420.weepcraft.module.modules.misc.IRC;
import me.razerboy420.weepcraft.module.modules.misc.MiddleClickFriends;
import me.razerboy420.weepcraft.module.modules.movement.Climb;
import me.razerboy420.weepcraft.module.modules.movement.DepthStrider;
import me.razerboy420.weepcraft.module.modules.movement.Fly;
import me.razerboy420.weepcraft.module.modules.movement.InvMove;
import me.razerboy420.weepcraft.module.modules.movement.JetPack;
import me.razerboy420.weepcraft.module.modules.movement.NoClip;
import me.razerboy420.weepcraft.module.modules.movement.SkipLadder;
import me.razerboy420.weepcraft.module.modules.movement.Sneak;
import me.razerboy420.weepcraft.module.modules.movement.Speed;
import me.razerboy420.weepcraft.module.modules.movement.Sprint;
import me.razerboy420.weepcraft.module.modules.movement.Step;
import me.razerboy420.weepcraft.module.modules.player.AntiPush;
import me.razerboy420.weepcraft.module.modules.player.AutoEat;
import me.razerboy420.weepcraft.module.modules.player.FastBreak;
import me.razerboy420.weepcraft.module.modules.player.FastEat;
import me.razerboy420.weepcraft.module.modules.player.FastPlace;
import me.razerboy420.weepcraft.module.modules.player.Ghost;
import me.razerboy420.weepcraft.module.modules.player.GhostHand;
import me.razerboy420.weepcraft.module.modules.player.NoForceTurn;
import me.razerboy420.weepcraft.module.modules.player.Nofall;
import me.razerboy420.weepcraft.module.modules.player.Safewalk;
import me.razerboy420.weepcraft.module.modules.player.Terrain;
import me.razerboy420.weepcraft.module.modules.player.Zoot;
import me.razerboy420.weepcraft.module.modules.world.Build;
import me.razerboy420.weepcraft.module.modules.world.ChestStealer;

public class ModuleManager {
    public static Sprint sprint = new Sprint();
    public static IRC irc = new IRC();
    public static ChestESP chestesp = new ChestESP();
    public static Fullbright fullbright = new Fullbright();
    public static Velocity velocity = new Velocity();
    public static AntiAAC antiaac = new AntiAAC();
    public static FakeLag fakelag = new FakeLag();
    public static Build build = new Build();
    public static AntiPush antipush = new AntiPush();
    public static Nofall nofall = new Nofall();
    public static Speed speed = new Speed();
    public static Terrain terrain = new Terrain();
    public static NoForceTurn noforceturn = new NoForceTurn();
    public static AutoRespawn autorespawn = new AutoRespawn();
    public static EggWars eggwars = new EggWars();
    public static NoClip noclip = new NoClip();
    public static AutoSoup autosoup = new AutoSoup();
    public static FastEat fasteat = new FastEat();
    public static NewPot autopot = new NewPot();
    public static AntiInvis antiinvis = new AntiInvis();
    public static AntiBlind antiblind = new AntiBlind();
    public static FastBreak fastbreak = new FastBreak();
    public static AutoFish autofish = new AutoFish();
    public static GhostHand ghosthand = new GhostHand();
    public static DepthStrider depth = new DepthStrider();
    public static ESP esp = new ESP();
    public static Tracers tracers = new Tracers();
    public static AutoEat autoeat = new AutoEat();
    public static Zoot zoot = new Zoot();
    public static ChestStealer cheststealer = new ChestStealer();
    public static DeathDerp deathderp = new DeathDerp();
    public static FlashBack flashback = new FlashBack();
    public static MiddleClickFriends mcf = new MiddleClickFriends();
    public static Step step = new Step();
    public static AutoTool autotool = new AutoTool();
    public static Ghost ghost = new Ghost();
    public static FastPlace fastplace = new FastPlace();
    public static SkipLadder fastladder = new SkipLadder();
    public static AutoAccept autoaccept = new AutoAccept();
    public static AutoSwitch autoswitch = new AutoSwitch();
    public static Climb climb = new Climb();
    public static Fly fly = new Fly();
    public static AutoWalk autowalk = new AutoWalk();
    public static Safewalk safewalk = new Safewalk();
    public static AutoMine automine = new AutoMine();
    public static Crosshair crosshair = new Crosshair();
    public static InvMove invmove = new InvMove();
    public static AutoClicker autoclicker = new AutoClicker();
    public static JetPack jetpack = new JetPack();
    public static Sneak sneak = new Sneak();
    public static Aura aura = new Aura();
    public static GUI gui = new GUI();
    public static HUD hud = new HUD();

    public ModuleManager() {
        this.registerModules();
    }

    public void registerModules() {
        Field[] arrfield = this.getClass().getFields();
        int n = arrfield.length;
        int n2 = 0;
        while (n2 < n) {
            Field field = arrfield[n2];
            try {
                Module mod = (Module)field.get(this);
                Weepcraft.getMods().add(mod);
                System.out.println(String.valueOf(String.valueOf(mod.getName())) + " found");
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            ++n2;
        }
    }
}

