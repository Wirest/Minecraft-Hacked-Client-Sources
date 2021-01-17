// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui.spectator.categories;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiSpectator;
import net.minecraft.client.gui.spectator.SpectatorMenu;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import java.util.Iterator;
import net.minecraft.client.gui.spectator.PlayerMenuObject;
import net.minecraft.world.WorldSettings;
import com.google.common.collect.Lists;
import java.util.Collection;
import net.minecraft.client.Minecraft;
import com.google.common.collect.ComparisonChain;
import java.util.Comparator;
import java.util.List;
import net.minecraft.client.network.NetworkPlayerInfo;
import com.google.common.collect.Ordering;
import net.minecraft.client.gui.spectator.ISpectatorMenuObject;
import net.minecraft.client.gui.spectator.ISpectatorMenuView;

public class TeleportToPlayer implements ISpectatorMenuView, ISpectatorMenuObject
{
    private static final Ordering<NetworkPlayerInfo> field_178674_a;
    private final List<ISpectatorMenuObject> field_178673_b;
    
    static {
        field_178674_a = Ordering.from((Comparator<NetworkPlayerInfo>)new Comparator<NetworkPlayerInfo>() {
            @Override
            public int compare(final NetworkPlayerInfo p_compare_1_, final NetworkPlayerInfo p_compare_2_) {
                return ComparisonChain.start().compare(p_compare_1_.getGameProfile().getId(), p_compare_2_.getGameProfile().getId()).result();
            }
        });
    }
    
    public TeleportToPlayer() {
        this(TeleportToPlayer.field_178674_a.sortedCopy(Minecraft.getMinecraft().getNetHandler().getPlayerInfoMap()));
    }
    
    public TeleportToPlayer(final Collection<NetworkPlayerInfo> p_i45493_1_) {
        this.field_178673_b = (List<ISpectatorMenuObject>)Lists.newArrayList();
        for (final NetworkPlayerInfo networkplayerinfo : TeleportToPlayer.field_178674_a.sortedCopy(p_i45493_1_)) {
            if (networkplayerinfo.getGameType() != WorldSettings.GameType.SPECTATOR) {
                this.field_178673_b.add(new PlayerMenuObject(networkplayerinfo.getGameProfile()));
            }
        }
    }
    
    @Override
    public List<ISpectatorMenuObject> func_178669_a() {
        return this.field_178673_b;
    }
    
    @Override
    public IChatComponent func_178670_b() {
        return new ChatComponentText("Select a player to teleport to");
    }
    
    @Override
    public void func_178661_a(final SpectatorMenu menu) {
        menu.func_178647_a(this);
    }
    
    @Override
    public IChatComponent getSpectatorName() {
        return new ChatComponentText("Teleport to player");
    }
    
    @Override
    public void func_178663_a(final float p_178663_1_, final int alpha) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(GuiSpectator.field_175269_a);
        Gui.drawModalRectWithCustomSizedTexture(0, 0, 0.0f, 0.0f, 16, 16, 256.0f, 256.0f);
    }
    
    @Override
    public boolean func_178662_A_() {
        return !this.field_178673_b.isEmpty();
    }
}
