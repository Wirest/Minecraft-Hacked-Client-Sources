// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.module.modules;

import java.util.Iterator;
import me.CheerioFX.FusionX.FusionX;
import net.minecraft.entity.Entity;
import me.CheerioFX.FusionX.module.Category;
import net.minecraft.entity.item.EntityItem;
import java.util.ArrayList;
import me.CheerioFX.FusionX.module.Module;

public class NoItems extends Module
{
    public static boolean listUnloadedItems;
    public static ArrayList<EntityItem> unloadedEntities;
    
    static {
        NoItems.listUnloadedItems = true;
        NoItems.unloadedEntities = new ArrayList<EntityItem>();
    }
    
    public NoItems() {
        super("NoItems", 0, Category.RENDER);
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
    }
    
    @Override
    public void onRender() {
        if (this.getState()) {
            for (final Object entity : NoItems.mc.theWorld.loadedEntityList) {
                if (entity instanceof EntityItem && !NoItems.unloadedEntities.contains(entity)) {
                    if (NoItems.listUnloadedItems) {
                        NoItems.unloadedEntities.add((EntityItem)entity);
                    }
                    NoItems.mc.theWorld.removeEntity((Entity)entity);
                    FusionX.addChatMessage("Removed Entity: " + ((EntityItem)entity).getName());
                }
            }
        }
    }
    
    @Override
    public void onDisable() {
        if (NoItems.listUnloadedItems) {
            String message = "Unloaded Items: ";
            for (int o = 0; o < NoItems.unloadedEntities.size(); ++o) {
                message = String.valueOf(message) + NoItems.unloadedEntities.get(o).getName() + ", ";
            }
            FusionX.addChatMessage(message);
            FusionX.addChatMessage("Total Number Of Unloaded Items: " + NoItems.unloadedEntities.size());
            NoItems.unloadedEntities.clear();
        }
        super.onDisable();
    }
}
