// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui.spectator;

import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.client.gui.spectator.categories.TeleportToTeam;
import net.minecraft.client.gui.spectator.categories.TeleportToPlayer;
import com.google.common.collect.Lists;
import java.util.List;

public class BaseSpectatorGroup implements ISpectatorMenuView
{
    private final List<ISpectatorMenuObject> field_178671_a;
    
    public BaseSpectatorGroup() {
        (this.field_178671_a = (List<ISpectatorMenuObject>)Lists.newArrayList()).add(new TeleportToPlayer());
        this.field_178671_a.add(new TeleportToTeam());
    }
    
    @Override
    public List<ISpectatorMenuObject> func_178669_a() {
        return this.field_178671_a;
    }
    
    @Override
    public IChatComponent func_178670_b() {
        return new ChatComponentText("Press a key to select a command, and again to use it.");
    }
}
