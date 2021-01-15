// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.utils.Property;

import me.CheerioFX.FusionX.module.Module;
import java.util.ArrayList;

public class PropertyManager
{
    public static ArrayList<Property> properties;
    
    static {
        PropertyManager.properties = new ArrayList<Property>();
    }
    
    public static ArrayList<Property> getProperties() {
        return PropertyManager.properties;
    }
    
    public static Property getPropertybyName(final String name) {
        return null;
    }
    
    public ArrayList<Property> getPropertiesFromModule(final Module module) {
        final ArrayList<Property> array = new ArrayList<Property>();
        return array;
    }
}
