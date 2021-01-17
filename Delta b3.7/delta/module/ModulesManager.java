/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  me.xtrm.atlaspluginloader.core.event.EventTarget
 *  me.xtrm.delta.api.event.events.player.EventKeyboard
 *  me.xtrm.delta.api.module.IModule
 *  me.xtrm.delta.api.module.IModuleManager
 */
package delta.module;

import delta.Class55;
import delta.Class93;
import delta.client.DeltaClient;
import delta.module.modules.Aimbot;
import delta.module.modules.AirJump;
import delta.module.modules.AntiAFK;
import delta.module.modules.AntiKnockback;
import delta.module.modules.AutoBreak;
import delta.module.modules.AutoRespawn;
import delta.module.modules.AutoSpawn;
import delta.module.modules.AutoWalk;
import delta.module.modules.Blink;
import delta.module.modules.ChestESP;
import delta.module.modules.ClickGUIModule;
import delta.module.modules.Criticals;
import delta.module.modules.DeathCoords;
import delta.module.modules.Derp;
import delta.module.modules.ESP;
import delta.module.modules.FastBreak;
import delta.module.modules.FastEat;
import delta.module.modules.FastPlace;
import delta.module.modules.FastSpawn;
import delta.module.modules.Fly;
import delta.module.modules.Freecam;
import delta.module.modules.Fullbright;
import delta.module.modules.Glide;
import delta.module.modules.HUD;
import delta.module.modules.HighJump;
import delta.module.modules.InventoryMove;
import delta.module.modules.Jesus;
import delta.module.modules.KillAura;
import delta.module.modules.MiddleClickFriend;
import delta.module.modules.Nametags;
import delta.module.modules.NoClip;
import delta.module.modules.NoFall;
import delta.module.modules.Nuker;
import delta.module.modules.Regen;
import delta.module.modules.SelfDestruct;
import delta.module.modules.Speed;
import delta.module.modules.Spider;
import delta.module.modules.Spin;
import delta.module.modules.Sprint;
import delta.module.modules.Step;
import delta.module.modules.TPAura;
import delta.module.modules.Timer;
import delta.module.modules.Tracers;
import delta.module.modules.Twerk;
import delta.module.modules.UnclaimFinder;
import delta.module.modules.Xray;
import java.util.ArrayList;
import java.util.List;
import me.xtrm.atlaspluginloader.core.event.EventTarget;
import me.xtrm.delta.api.event.events.player.EventKeyboard;
import me.xtrm.delta.api.module.IModule;
import me.xtrm.delta.api.module.IModuleManager;

public class ModulesManager
implements IModuleManager {
    private List<IModule> color$ = new ArrayList<IModule>();

    public List<IModule> getModules() {
        return this.color$;
    }

    private static boolean ioIo(EventKeyboard eventKeyboard, IModule iModule) {
        return iModule.getKey() == eventKeyboard.getKey();
    }

    @EventTarget
    public void onKeyPressed(EventKeyboard eventKeyboard) {
        if (eventKeyboard.getKey() == 0 || eventKeyboard.getKey() == -1) {
            return;
        }
        this.getModules().stream().filter(arg_0 -> ModulesManager.ioIo(eventKeyboard, arg_0)).forEach(IModule::toggle);
    }

    public void registerModules() {
        this.color$.add((IModule)new Aimbot());
        this.color$.add((IModule)new AirJump());
        this.color$.add((IModule)new AntiAFK());
        if (DeltaClient.instance._resort()._commonly()) {
            this.color$.add((IModule)new AntiKnockback());
        }
        this.color$.add((IModule)new AutoBreak());
        this.color$.add((IModule)new AutoRespawn());
        if (Class55._option()) {
            this.color$.add((IModule)new AutoSpawn());
        }
        this.color$.add((IModule)new AutoWalk());
        if (DeltaClient.instance._resort()._commonly()) {
            this.color$.add((IModule)new Blink());
        }
        this.color$.add((IModule)new ChestESP());
        this.color$.add((IModule)new ClickGUIModule());
        this.color$.add((IModule)new Criticals());
        this.color$.add((IModule)new DeathCoords());
        this.color$.add((IModule)new Derp());
        this.color$.add((IModule)new ESP());
        this.color$.add((IModule)new FastBreak());
        this.color$.add((IModule)new FastEat());
        this.color$.add((IModule)new FastPlace());
        if (Class55._option()) {
            this.color$.add((IModule)new FastSpawn());
        }
        this.color$.add((IModule)new Fly());
        if (DeltaClient.instance._resort()._commonly()) {
            this.color$.add((IModule)new Freecam());
        }
        this.color$.add((IModule)new Fullbright());
        this.color$.add((IModule)new Glide());
        this.color$.add((IModule)new HighJump());
        this.color$.add((IModule)new HUD());
        this.color$.add((IModule)new InventoryMove());
        this.color$.add((IModule)new Jesus());
        this.color$.add((IModule)new KillAura());
        this.color$.add((IModule)new MiddleClickFriend());
        this.color$.add((IModule)new Nametags());
        if (DeltaClient.instance._resort()._commonly()) {
            this.color$.add((IModule)new NoClip());
        }
        this.color$.add((IModule)new NoFall());
        this.color$.add((IModule)new Nuker());
        this.color$.add((IModule)new Regen());
        if (DeltaClient.instance._resort()._emily()) {
            this.color$.add((IModule)new SelfDestruct());
        }
        this.color$.add((IModule)new Class93());
        this.color$.add((IModule)new Speed());
        this.color$.add((IModule)new Spider());
        this.color$.add((IModule)new Spin());
        this.color$.add((IModule)new Sprint());
        this.color$.add((IModule)new Step());
        this.color$.add((IModule)new Timer());
        this.color$.add((IModule)new TPAura());
        this.color$.add((IModule)new Tracers());
        this.color$.add((IModule)new Twerk());
        this.color$.add((IModule)new UnclaimFinder());
        if (DeltaClient.instance._resort()._commonly()) {
            this.color$.add((IModule)new Xray());
        }
        DeltaClient.instance.managers.eventManager.register((Object)this);
    }
}

