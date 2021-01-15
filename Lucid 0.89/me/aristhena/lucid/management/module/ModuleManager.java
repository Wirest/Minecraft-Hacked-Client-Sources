/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  org.reflections.Reflections
 *  org.reflections.scanners.Scanner
 */
package me.aristhena.lucid.management.module;

import java.io.File;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import me.aristhena.lucid.eventapi.EventManager;
import me.aristhena.lucid.eventapi.EventTarget;
import me.aristhena.lucid.eventapi.events.KeyboardEvent;
import me.aristhena.lucid.management.module.Category;
import me.aristhena.lucid.management.module.Mod;
import me.aristhena.lucid.management.module.Module;
import me.aristhena.lucid.util.FileUtils;
import org.reflections.Reflections;
import org.reflections.scanners.Scanner;

public class ModuleManager
{
    public static final Module MODULE_EMPTY;
    public static List<Module> moduleList;
    private static final File MODULE_DIR;
    
    static {
        MODULE_EMPTY = new Module();
        ModuleManager.moduleList = new ArrayList<Module>();
        MODULE_DIR = FileUtils.getConfigFile("Mods");
    }
    
    public static void init() throws InstantiationException, IllegalAccessException {
        ModuleManager.MODULE_EMPTY.name = "null";
        final Reflections reflections = new Reflections("me.aristhena.lucid.modules", new Scanner[0]);
        final Set<Class<? extends Module>> classes = (Set<Class<? extends Module>>)reflections.getSubTypesOf((Class)Module.class);
        for (final Class<? extends Module> clazz : classes) {
            try {
                final Module loadedModule = (Module)clazz.newInstance();
                if (!clazz.isAnnotationPresent(Mod.class)) {
                    continue;
                }
                final Mod modAnnotation = clazz.getAnnotation(Mod.class);
                loadedModule.realName = clazz.getSimpleName();
                loadedModule.name = (modAnnotation.name().equalsIgnoreCase("null") ? clazz.getSimpleName() : modAnnotation.name());
                loadedModule.keyBind = modAnnotation.keybind();
                loadedModule.shown = modAnnotation.shown();
                loadedModule.suffix = modAnnotation.suffix();
                final String categoryString = clazz.getPackage().getName().split("modules.")[1];
                loadedModule.category = Category.valueOf(categoryString.toUpperCase());
                if (modAnnotation.enabled()) {
                    loadedModule.onEnable();
                }
                loadedModule.preInit();
                ModuleManager.moduleList.add(loadedModule);
            }
            catch (Exception ex) {}
        }
        load();
        save();
        ModuleManager.moduleList.sort(new Comparator<Module>() {
            @Override
            public int compare(final Module m1, final Module m2) {
                final String s1 = m1.name;
                final String s2 = m2.name;
                return s1.compareTo(s2);
            }
        });
        EventManager.register(new ModuleManager());
    }
    
    @EventTarget
    private void onKeyBoard(final KeyboardEvent event) {
        for (final Module mod : ModuleManager.moduleList) {
            if (event.key == mod.keyBind) {
                mod.toggle();
                save();
            }
        }
    }
    
    public static void load() {
        try {
            final List<String> fileContent = FileUtils.read(ModuleManager.MODULE_DIR);
            for (final String line : fileContent) {
                final String[] split = line.split(":");
                final String moduleRealName = split[0];
                final String moduleName = split[1];
                final String visibleString = split[2];
                final String moduleKey = split[3];
                final String enabledString = split[4];
                final Module module = getModule(moduleRealName);
                if (module.name.equalsIgnoreCase("null")) {
                    continue;
                }
                final int moduleKeybind = moduleKey.equalsIgnoreCase("null") ? 0 : Integer.parseInt(moduleKey);
                final boolean enabled = Boolean.parseBoolean(enabledString);
                final boolean visible = Boolean.parseBoolean(visibleString);
                if (enabled != module.enabled) {
                    module.toggle();
                }
                module.shown = visible;
                module.name = moduleName;
                module.keyBind = moduleKeybind;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void save() {
        final List<String> fileContent = new ArrayList<String>();
        for (final Module module : ModuleManager.moduleList) {
            final String moduleRealName = module.realName;
            final String moduleName = module.name;
            final String visible = Boolean.toString(module.shown);
            final String moduleKey = (module.keyBind <= 0) ? "null" : Integer.toString(module.keyBind);
            final String enabled = Boolean.toString(module.enabled);
            fileContent.add(String.format("%s:%s:%s:%s:%s", moduleRealName, moduleName, visible, moduleKey, enabled));
        }
        FileUtils.write(ModuleManager.MODULE_DIR, fileContent, true);
    }
    
    public static Module getModule(final Class<? extends Module> modClass) {
        for (final Module module : ModuleManager.moduleList) {
            if (module.getClass().equals(modClass)) {
                return module;
            }
        }
        return ModuleManager.MODULE_EMPTY;
    }
    
    public static Module getModule(final String modName) {
        for (final Module module : ModuleManager.moduleList) {
            if (module.realName.equalsIgnoreCase(modName) || module.name.equalsIgnoreCase(modName)) {
                return module;
            }
        }
        return ModuleManager.MODULE_EMPTY;
    }
}
