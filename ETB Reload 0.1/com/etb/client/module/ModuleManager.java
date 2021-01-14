package com.etb.client.module;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.etb.client.gui.clickui.ClickGUI;
import com.etb.client.module.modules.combat.*;
import com.etb.client.module.modules.movement.*;
import com.etb.client.module.modules.player.*;
import com.etb.client.module.modules.world.*;
import com.etb.client.module.modules.render.*;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.etb.client.Client;
import com.etb.client.event.events.input.KeyPressEvent;

import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

/**
 * made by etb for Client
 *
 * @since 5/25/2019
 **/
public class ModuleManager {
    public Map<String, Module> map = new HashMap<>();
    private File directory;

    public Map<String, Module> getModuleMap() {
        return map;
    }

    private ClickGUI gui;

    public void initialize() {
        register(Criticals.class);
        register(HUD.class);
        register(Trails.class);
        register(Velocity.class);
        register(AutoGG.class);
        register(Speed.class);
        register(AntiDeath.class);
        register(Nametags.class);
        register(OutlineESP.class);
        register(Xray.class);
        register(ESP.class);
        register(NoStrike.class);
        register(Sprint.class);
        register(MCF.class);
        register(AutoArmor.class);
        register(Jesus.class);
        register(NoRotate.class);
        register(HypixelDisabler.class);
        register(FastBow.class);
        register(LongJump.class);
        register(Step.class);
        register(AutoSword.class);
        register(Flight.class);
        register(AntiBot.class);
        register(NoSlowdown.class);
        register(AutoHeal.class);
        register(Phase.class);
        register(NoFall.class);
        register(ChestStealer.class);
        register(Scaffold.class);
        register(InvCleaner.class);
        register(Teleport.class);
        register(InventoryPlus.class);
        register(FullBright.class);
        register(AutoAccept.class);
        register(BanWave.class);
        register(SkinFlash.class);
        register(AutoApple.class);
        register(Sneak.class);
        register(Zoot.class);
        register(Bobbing.class);
        register(Blink.class);
        register(Emote.class);
        register(Disabler.class);
        register(AntiVoid.class);
        register(NoRender.class);
        register(Tracers.class);
        register(Chams.class);
        register(Freecam.class);
        register(AntiTabComplete.class);
        register(Crosshair.class);
        register(KillAura.class);
        EventBus.getDefault().register(this);
    }

    public void setDirectory(File directory) {
        this.directory = directory;
    }

    private void register(Class<? extends Module> moduleClass) {
        try {
            Module createdModule = moduleClass.newInstance();
            map.put(createdModule.getLabel().toLowerCase(), createdModule);
        } catch (Exception e) {
        }
    }

    @Subscribe
    public void onKeyPress(KeyPressEvent e) {
        getModuleMap().values().forEach(m -> {
            if (m.getKeybind() == e.getKey()) {
                m.toggle();
            }
        });
        if (e.getKey() == Keyboard.KEY_RSHIFT) {
            if (gui == null) {
                gui = new ClickGUI();
            }
            Minecraft.getMinecraft().displayGuiScreen(gui);
        }
    }

    public ArrayList<Module> getCategoryCheats(Module.Category cat) {
        final ArrayList<Module> modsInCategory = new ArrayList<Module>();
        for (Module mod : Client.INSTANCE.getModuleManager().getModuleMap().values()) {
            if (mod.getCategory() == cat) {
                modsInCategory.add(mod);
            }
        }
        return modsInCategory;
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

    public Module getModule(String name) {
        return getModuleMap().get(name);
    }

    public void saveModules() {
        if (getModuleMap().values().isEmpty()) {
            directory.delete();
        }
        File[] files = directory.listFiles();
        if (!directory.exists()) {
            directory.mkdir();
        } else if (files != null) {
            for (File file : files) {
                file.delete();
            }
        }
        getModuleMap().values().forEach(module -> {
            File file = new File(directory, module.getLabel() + ".json");
            JsonObject node = new JsonObject();
            module.save(node);
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
        files = directory.listFiles();
        if (files == null || files.length == 0) {
            directory.delete();
        }
    }

    public void loadModules() {
        getModuleMap().values().forEach(module -> {
            final File file = new File(directory, module.getLabel() + ".json");
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
