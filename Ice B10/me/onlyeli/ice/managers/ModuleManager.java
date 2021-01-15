package me.onlyeli.ice.managers;

import me.onlyeli.ice.Module;
import me.onlyeli.ice.main.*;
import java.util.*;

public class ModuleManager
{
    public static boolean IsModuleEnabled(final String moduleName) {
        for (final Module module : Ice.getModules()) {
            if (module.getModName().equalsIgnoreCase(moduleName)) {
                return module.isToggled();
            }
        }
        return false;
    }
    
    public static void ToggleModule(final String moduleName) {
        for (final Module module : Ice.getModules()) {
            if (module.getModName().equalsIgnoreCase(moduleName)) {
                module.setToggled(!module.isToggled());
            }
        }
    }
    
    public static void EnableModule(final String moduleName) {
        for (final Module module : Ice.getModules()) {
            if (module.getModName().equalsIgnoreCase(moduleName)) {
                module.setToggled(true);
            }
        }
    }
    
    public static void DisableModule(final String moduleName) {
        for (final Module module : Ice.getModules()) {
            if (module.getModName().equalsIgnoreCase(moduleName)) {
                module.setToggled(false);
            }
        }
    }
    
    public static Module getModule(final String moduleName) {
        for (final Module module : Ice.getModules()) {
            if (module.getModName().equalsIgnoreCase(moduleName)) {
                return module;
            }
        }
        return null;
    }
}
