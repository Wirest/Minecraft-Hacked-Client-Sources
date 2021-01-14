package moonx.ohare.client.module;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import moonx.ohare.client.Moonx;
import moonx.ohare.client.event.bus.Handler;
import moonx.ohare.client.event.impl.input.KeyInputEvent;
import moonx.ohare.client.module.impl.combat.*;
import moonx.ohare.client.module.impl.combat.CopsNCrims;
import moonx.ohare.client.module.impl.exploit.*;
import moonx.ohare.client.module.impl.ghost.*;
import moonx.ohare.client.module.impl.movement.*;
import moonx.ohare.client.module.impl.other.*;
import moonx.ohare.client.module.impl.player.*;
import moonx.ohare.client.module.impl.visuals.*;
import moonx.ohare.client.utils.value.Value;


import java.io.*;
import java.lang.reflect.Field;
import java.util.*;

/**
 * made by oHare for MoonX
 *
 * @since 8/27/2019
 **/
public class ModuleManager {
    private Map<String, Module> map = new HashMap<>();
    private File dir;

    public void initializeModules() {
        registerMod(HUD.class);
        registerMod(AimAssist.class);
        registerMod(NoHurtCam.class);
        registerMod(Gay.class);
        registerMod(Unstuck.class);
        registerMod(CopsNCrims.class);
        registerMod(FastLadder.class);
        registerMod(Speedmine.class);
        registerMod(Fastbow.class);
        registerMod(FastUse.class);
        registerMod(Autoclicker.class);
        registerMod(Reach.class);
        registerMod(AntiBot.class);
        registerMod(Spammer.class);
        registerMod(Blink.class);
        registerMod(BlinkLag.class);
        registerMod(AutoMatchJoin.class);
        registerMod(LongJump.class);
        registerMod(OffscreenESP.class);
        registerMod(Phase.class);
        registerMod(SkeletonESP.class);
        registerMod(Chams.class);
        registerMod(Disabler.class);
        registerMod(AutoArmor.class);
        registerMod(AutoGG.class);
        registerMod(Cape.class);
        registerMod(Criticals.class);
        registerMod(FullBright.class);
        registerMod(AntiFreeze.class);
        registerMod(ChestESP.class);
        registerMod(PenisESP.class);
        registerMod(Detector.class);
        registerMod(TimeChanger.class);
        registerMod(AntiVoid.class);
        registerMod(ChatBypass.class);
        registerMod(VanishDetector.class);
        registerMod(Scaffold.class);
        registerMod(BowAimbot.class);
        registerMod(AutoSword.class);
        registerMod(Sneak.class);
        registerMod(Animation.class);
        registerMod(Step.class);
        registerMod(Jesus.class);
        registerMod(Waypoints.class);
        registerMod(Sprint.class);
        registerMod(Regen.class);
        registerMod(AutoHeal.class);
        registerMod(Crosshair.class);
        registerMod(Tracers.class);
        registerMod(NoRotate.class);
        registerMod(InvCleaner.class);
        registerMod(NoSlowdown.class);
        registerMod(TargetStrafe.class);
        registerMod(MurderMystery.class);
        registerMod(Freecam.class);
        registerMod(AutoTool.class);
        registerMod(InvWalk.class);
        registerMod(Nametags.class);
        registerMod(AutoApple.class);
        registerMod(MCF.class);
        registerMod(Speed.class);
        registerMod(Flight.class);
        registerMod(KillAura.class);
        registerMod(ChestStealer.class);
        registerMod(NoFall.class);
        registerMod(Velocity.class);
        registerMod(ESP.class);
        registerMod(ClickGui.class);
        registerMod(AntiAim.class);
        registerMod(Trajectories.class);
        Moonx.INSTANCE.getEventBus().bind(this);
    }

    @Handler
    public void onKeyInput(KeyInputEvent event) {
        for (Module module : map.values()) {
            if (module.getKeybind() == event.getKey()) module.setEnabled(!module.isEnabled());
        }
    }

    public void setDir(File dir) {
        this.dir = dir;
    }

    public Map<String, Module> getMap() {
        return map;
    }

    public File getDir() {
        return dir;
    }

    public Map<String, Module> getModuleMap() {
        return map;
    }

    public boolean isModule(final String modulename) {
        for (Module mod : getModuleMap().values()) {
            if (mod.getLabel().equalsIgnoreCase(modulename)) {
                return true;
            }
        }
        return false;
    }

    public Module getModuleClass(final Class<?> clazz) {
        for (Module mod : getModuleMap().values()) {
            if (mod.getClass().equals(clazz)) {
                return mod;
            }
        }
        return null;
    }

    public ArrayList<Module> getModulesInCategory(Module.Category category) {
        final ArrayList<Module> mods = new ArrayList<>();
        for (Module module : map.values()) {
            if (module.getCategory() == category) {
                mods.add(module);
            }
        }
        return mods;
    }

    public float getLongestModInCategory(Module.Category category) {
        final HUD hud = (HUD) Moonx.INSTANCE.getModuleManager().getModule("hud");
        float width = hud.fontValue.getValue().getStringWidth(getModulesInCategory(category).get(0).getLabel());
        for (Module module : getModulesInCategory(category)) {
            if (hud.fontValue.getValue().getStringWidth(module.getLabel()) > width) {
                width = hud.fontValue.getValue().getStringWidth(module.getLabel());
            }
        }
        return width;
    }

    public Module getModule(String name) {
        return getModuleMap().get(name.toLowerCase());
    }


    private void registerMod(Class<? extends Module> moduleClass) {
        try {
            final Module createdModule = moduleClass.newInstance();
            for (final Field field : createdModule.getClass().getDeclaredFields()) {
                try {
                    field.setAccessible(true);
                    final Object obj = field.get(createdModule);

                    if (obj instanceof Value)
                        createdModule.getValues().add((Value) obj);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            map.put(createdModule.getLabel().toLowerCase(), createdModule);
        } catch (Exception ignored) {
        }
    }

    public void saveModules() {
        File[] files = dir.listFiles();
        if (!dir.exists()) {
            dir.mkdir();
        } else if (files != null) {
            for (File file : files) {
                file.delete();
            }
        }
        getModuleMap().values().forEach(module -> {
            File file = new File(dir, module.getLabel() + ".json");
            JsonObject node = new JsonObject();
            module.save(node, true);
            if (node.entrySet().isEmpty()) {
                return;
            }
            try {
                file.createNewFile();
            } catch (IOException e) {
                return;
            }
            try (Writer writer = new FileWriter(file)) {
                writer.write(new GsonBuilder().setPrettyPrinting().create().toJson(node));
            } catch (IOException e) {
                file.delete();
            }
        });
        files = dir.listFiles();
        if (files == null || files.length == 0) {
            dir.delete();
        }
    }

    public void loadModules() {
        getModuleMap().values().forEach(module -> {
            final File file = new File(dir, module.getLabel() + ".json");
            if (!file.exists()) {
                return;
            }
            try (Reader reader = new FileReader(file)) {
                JsonElement node = new JsonParser().parse(reader);
                if (!node.isJsonObject()) {
                    return;
                }
                module.load(node.getAsJsonObject());
            } catch (IOException e) {
            }
        });
    }
}
