// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui.spectator.categories;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;
import net.minecraft.client.gui.FontRenderer;
import java.util.Collection;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.entity.AbstractClientPlayer;
import java.util.Random;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiSpectator;
import net.minecraft.client.gui.spectator.SpectatorMenu;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import java.util.Iterator;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.client.Minecraft;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.client.gui.spectator.ISpectatorMenuObject;
import net.minecraft.client.gui.spectator.ISpectatorMenuView;

public class TeleportToTeam implements ISpectatorMenuView, ISpectatorMenuObject
{
    private final List<ISpectatorMenuObject> field_178672_a;
    
    public TeleportToTeam() {
        this.field_178672_a = (List<ISpectatorMenuObject>)Lists.newArrayList();
        final Minecraft minecraft = Minecraft.getMinecraft();
        for (final ScorePlayerTeam scoreplayerteam : minecraft.theWorld.getScoreboard().getTeams()) {
            this.field_178672_a.add(new TeamSelectionObject(scoreplayerteam));
        }
    }
    
    @Override
    public List<ISpectatorMenuObject> func_178669_a() {
        return this.field_178672_a;
    }
    
    @Override
    public IChatComponent func_178670_b() {
        return new ChatComponentText("Select a team to teleport to");
    }
    
    @Override
    public void func_178661_a(final SpectatorMenu menu) {
        menu.func_178647_a(this);
    }
    
    @Override
    public IChatComponent getSpectatorName() {
        return new ChatComponentText("Teleport to team member");
    }
    
    @Override
    public void func_178663_a(final float p_178663_1_, final int alpha) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(GuiSpectator.field_175269_a);
        Gui.drawModalRectWithCustomSizedTexture(0, 0, 16.0f, 0.0f, 16, 16, 256.0f, 256.0f);
    }
    
    @Override
    public boolean func_178662_A_() {
        for (final ISpectatorMenuObject ispectatormenuobject : this.field_178672_a) {
            if (ispectatormenuobject.func_178662_A_()) {
                return true;
            }
        }
        return false;
    }
    
    class TeamSelectionObject implements ISpectatorMenuObject
    {
        private final ScorePlayerTeam field_178676_b;
        private final ResourceLocation field_178677_c;
        private final List<NetworkPlayerInfo> field_178675_d;
        
        public TeamSelectionObject(final ScorePlayerTeam p_i45492_2_) {
            this.field_178676_b = p_i45492_2_;
            this.field_178675_d = (List<NetworkPlayerInfo>)Lists.newArrayList();
            for (final String s : p_i45492_2_.getMembershipCollection()) {
                final NetworkPlayerInfo networkplayerinfo = Minecraft.getMinecraft().getNetHandler().getPlayerInfo(s);
                if (networkplayerinfo != null) {
                    this.field_178675_d.add(networkplayerinfo);
                }
            }
            if (!this.field_178675_d.isEmpty()) {
                final String s2 = this.field_178675_d.get(new Random().nextInt(this.field_178675_d.size())).getGameProfile().getName();
                AbstractClientPlayer.getDownloadImageSkin(this.field_178677_c = AbstractClientPlayer.getLocationSkin(s2), s2);
            }
            else {
                this.field_178677_c = DefaultPlayerSkin.getDefaultSkinLegacy();
            }
        }
        
        @Override
        public void func_178661_a(final SpectatorMenu menu) {
            menu.func_178647_a(new TeleportToPlayer(this.field_178675_d));
        }
        
        @Override
        public IChatComponent getSpectatorName() {
            return new ChatComponentText(this.field_178676_b.getTeamName());
        }
        
        @Override
        public void func_178663_a(final float p_178663_1_, final int alpha) {
            int i = -1;
            final String s = FontRenderer.getFormatFromString(this.field_178676_b.getColorPrefix());
            if (s.length() >= 2) {
                i = Minecraft.getMinecraft().fontRendererObj.getColorCode(s.charAt(1));
            }
            if (i >= 0) {
                final float f = (i >> 16 & 0xFF) / 255.0f;
                final float f2 = (i >> 8 & 0xFF) / 255.0f;
                final float f3 = (i & 0xFF) / 255.0f;
                Gui.drawRect(1.0, 1.0, 15.0, 15.0, MathHelper.func_180183_b(f * p_178663_1_, f2 * p_178663_1_, f3 * p_178663_1_) | alpha << 24);
            }
            Minecraft.getMinecraft().getTextureManager().bindTexture(this.field_178677_c);
            GlStateManager.color(p_178663_1_, p_178663_1_, p_178663_1_, alpha / 255.0f);
            Gui.drawScaledCustomSizeModalRect(2, 2, 8.0f, 8.0f, 8, 8, 12, 12, 64.0f, 64.0f);
            Gui.drawScaledCustomSizeModalRect(2, 2, 40.0f, 8.0f, 8, 8, 12, 12, 64.0f, 64.0f);
        }
        
        @Override
        public boolean func_178662_A_() {
            return !this.field_178675_d.isEmpty();
        }
    }
}
