// 
// Decompiled by Procyon v0.5.36
// 

package me.nico.hush.modules.combat;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import me.nico.hush.events.EventUpdate;
import me.nico.hush.modules.Category;
import net.minecraft.entity.player.EntityPlayer;
import java.util.ArrayList;
import me.nico.hush.modules.Module;

public class Teams extends Module
{
    public static ArrayList<EntityPlayer> teams;
    
    static {
        Teams.teams = new ArrayList<EntityPlayer>();
    }
    
    public Teams() {
        super("Teams", "Teams", 16777215, 0, Category.COMBAT);
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
        for (final Entity e : Teams.mc.theWorld.loadedEntityList) {
            if (e instanceof EntityPlayer) {
                final EntityPlayer ep = (EntityPlayer)e;
                final String formattedText = ep.getDisplayName().getFormattedText();
                final StringBuilder sb = new StringBuilder("§");
                final Minecraft mc = Teams.mc;
                if (formattedText.startsWith(sb.append(Minecraft.thePlayer.getDisplayName().getFormattedText().charAt(1)).toString())) {
                    Teams.teams.add(ep);
                }
                else {
                    if (!Teams.teams.contains(ep)) {
                        continue;
                    }
                    Teams.teams.remove(ep);
                }
            }
        }
    }
    
    @Override
    public void onEnable() {
        EventManager.register(this);
        Teams.teams.clear();
    }
    
    @Override
    public void onDisable() {
        EventManager.unregister(this);
    }
}
