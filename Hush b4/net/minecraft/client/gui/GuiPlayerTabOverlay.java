// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import com.google.common.collect.ComparisonChain;
import net.minecraft.util.MathHelper;
import net.minecraft.entity.player.EntityPlayer;
import com.mojang.authlib.GameProfile;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.WorldSettings;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.scoreboard.IScoreObjectiveCriteria;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.scoreboard.ScorePlayerTeam;
import java.util.Comparator;
import net.minecraft.util.IChatComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import com.google.common.collect.Ordering;

public class GuiPlayerTabOverlay extends Gui
{
    private static final Ordering<NetworkPlayerInfo> field_175252_a;
    private final Minecraft mc;
    private final GuiIngame guiIngame;
    private IChatComponent footer;
    private IChatComponent header;
    private long lastTimeOpened;
    private boolean isBeingRendered;
    
    static {
        field_175252_a = Ordering.from((Comparator<NetworkPlayerInfo>)new PlayerComparator(null));
    }
    
    public GuiPlayerTabOverlay(final Minecraft mcIn, final GuiIngame guiIngameIn) {
        this.mc = mcIn;
        this.guiIngame = guiIngameIn;
    }
    
    public String getPlayerName(final NetworkPlayerInfo networkPlayerInfoIn) {
        return (networkPlayerInfoIn.getDisplayName() != null) ? networkPlayerInfoIn.getDisplayName().getFormattedText() : ScorePlayerTeam.formatPlayerName(networkPlayerInfoIn.getPlayerTeam(), networkPlayerInfoIn.getGameProfile().getName());
    }
    
    public void updatePlayerList(final boolean willBeRendered) {
        if (willBeRendered && !this.isBeingRendered) {
            this.lastTimeOpened = Minecraft.getSystemTime();
        }
        this.isBeingRendered = willBeRendered;
    }
    
    public void renderPlayerlist(final int width, final Scoreboard scoreboardIn, final ScoreObjective scoreObjectiveIn) {
        final NetHandlerPlayClient nethandlerplayclient = Minecraft.thePlayer.sendQueue;
        List<NetworkPlayerInfo> list = GuiPlayerTabOverlay.field_175252_a.sortedCopy(nethandlerplayclient.getPlayerInfoMap());
        int i = 0;
        int j = 0;
        for (final NetworkPlayerInfo networkplayerinfo : list) {
            int k = this.mc.fontRendererObj.getStringWidth(this.getPlayerName(networkplayerinfo));
            i = Math.max(i, k);
            if (scoreObjectiveIn != null && scoreObjectiveIn.getRenderType() != IScoreObjectiveCriteria.EnumRenderType.HEARTS) {
                k = this.mc.fontRendererObj.getStringWidth(" " + scoreboardIn.getValueFromObjective(networkplayerinfo.getGameProfile().getName(), scoreObjectiveIn).getScorePoints());
                j = Math.max(j, k);
            }
        }
        list = list.subList(0, Math.min(list.size(), 80));
        int i2;
        int l3;
        int j2;
        for (l3 = (i2 = list.size()), j2 = 1; i2 > 20; i2 = (l3 + j2 - 1) / j2) {
            ++j2;
        }
        final boolean flag = this.mc.isIntegratedServerRunning() || this.mc.getNetHandler().getNetworkManager().getIsencrypted();
        int m;
        if (scoreObjectiveIn != null) {
            if (scoreObjectiveIn.getRenderType() == IScoreObjectiveCriteria.EnumRenderType.HEARTS) {
                m = 90;
            }
            else {
                m = j;
            }
        }
        else {
            m = 0;
        }
        final int i3 = Math.min(j2 * ((flag ? 9 : 0) + i + m + 13), width - 50) / j2;
        final int j3 = width / 2 - (i3 * j2 + (j2 - 1) * 5) / 2;
        int k2 = 10;
        int l4 = i3 * j2 + (j2 - 1) * 5;
        List<String> list2 = null;
        List<String> list3 = null;
        if (this.header != null) {
            list2 = (List<String>)this.mc.fontRendererObj.listFormattedStringToWidth(this.header.getFormattedText(), width - 50);
            for (final String s : list2) {
                l4 = Math.max(l4, this.mc.fontRendererObj.getStringWidth(s));
            }
        }
        if (this.footer != null) {
            list3 = (List<String>)this.mc.fontRendererObj.listFormattedStringToWidth(this.footer.getFormattedText(), width - 50);
            for (final String s2 : list3) {
                l4 = Math.max(l4, this.mc.fontRendererObj.getStringWidth(s2));
            }
        }
        if (list2 != null) {
            Gui.drawRect(width / 2 - l4 / 2 - 1, k2 - 1, width / 2 + l4 / 2 + 1, k2 + list2.size() * this.mc.fontRendererObj.FONT_HEIGHT, Integer.MIN_VALUE);
            for (final String s3 : list2) {
                final int i4 = this.mc.fontRendererObj.getStringWidth(s3);
                this.mc.fontRendererObj.drawStringWithShadow(s3, (float)(width / 2 - i4 / 2), (float)k2, -1);
                k2 += this.mc.fontRendererObj.FONT_HEIGHT;
            }
            ++k2;
        }
        Gui.drawRect(width / 2 - l4 / 2 - 1, k2 - 1, width / 2 + l4 / 2 + 1, k2 + i2 * 9, Integer.MIN_VALUE);
        for (int k3 = 0; k3 < l3; ++k3) {
            final int l5 = k3 / i2;
            final int i5 = k3 % i2;
            int j4 = j3 + l5 * i3 + l5 * 5;
            final int k4 = k2 + i5 * 9;
            Gui.drawRect(j4, k4, j4 + i3, k4 + 8, 553648127);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.enableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            if (k3 < list.size()) {
                final NetworkPlayerInfo networkplayerinfo2 = list.get(k3);
                String s4 = this.getPlayerName(networkplayerinfo2);
                final GameProfile gameprofile = networkplayerinfo2.getGameProfile();
                if (flag) {
                    final EntityPlayer entityplayer = this.mc.theWorld.getPlayerEntityByUUID(gameprofile.getId());
                    final boolean flag2 = entityplayer != null && entityplayer.isWearing(EnumPlayerModelParts.CAPE) && (gameprofile.getName().equals("Dinnerbone") || gameprofile.getName().equals("Grumm"));
                    this.mc.getTextureManager().bindTexture(networkplayerinfo2.getLocationSkin());
                    final int l6 = 8 + (flag2 ? 8 : 0);
                    final int i6 = 8 * (flag2 ? -1 : 1);
                    Gui.drawScaledCustomSizeModalRect(j4, k4, 8.0f, (float)l6, 8, i6, 8, 8, 64.0f, 64.0f);
                    if (entityplayer != null && entityplayer.isWearing(EnumPlayerModelParts.HAT)) {
                        final int j5 = 8 + (flag2 ? 8 : 0);
                        final int k5 = 8 * (flag2 ? -1 : 1);
                        Gui.drawScaledCustomSizeModalRect(j4, k4, 40.0f, (float)j5, 8, k5, 8, 8, 64.0f, 64.0f);
                    }
                    j4 += 9;
                }
                if (networkplayerinfo2.getGameType() == WorldSettings.GameType.SPECTATOR) {
                    s4 = EnumChatFormatting.ITALIC + s4;
                    this.mc.fontRendererObj.drawStringWithShadow(s4, (float)j4, (float)k4, -1862270977);
                }
                else {
                    this.mc.fontRendererObj.drawStringWithShadow(s4, (float)j4, (float)k4, -1);
                }
                if (scoreObjectiveIn != null && networkplayerinfo2.getGameType() != WorldSettings.GameType.SPECTATOR) {
                    final int k6 = j4 + i + 1;
                    final int l7 = k6 + m;
                    if (l7 - k6 > 5) {
                        this.drawScoreboardValues(scoreObjectiveIn, k4, gameprofile.getName(), k6, l7, networkplayerinfo2);
                    }
                }
                this.drawPing(i3, j4 - (flag ? 9 : 0), k4, networkplayerinfo2);
            }
        }
        if (list3 != null) {
            k2 = k2 + i2 * 9 + 1;
            Gui.drawRect(width / 2 - l4 / 2 - 1, k2 - 1, width / 2 + l4 / 2 + 1, k2 + list3.size() * this.mc.fontRendererObj.FONT_HEIGHT, Integer.MIN_VALUE);
            for (final String s5 : list3) {
                final int j6 = this.mc.fontRendererObj.getStringWidth(s5);
                this.mc.fontRendererObj.drawStringWithShadow(s5, (float)(width / 2 - j6 / 2), (float)k2, -1);
                k2 += this.mc.fontRendererObj.FONT_HEIGHT;
            }
        }
    }
    
    protected void drawPing(final int p_175245_1_, final int p_175245_2_, final int p_175245_3_, final NetworkPlayerInfo networkPlayerInfoIn) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(GuiPlayerTabOverlay.icons);
        final int i = 0;
        int j = 0;
        if (networkPlayerInfoIn.getResponseTime() < 0) {
            j = 5;
        }
        else if (networkPlayerInfoIn.getResponseTime() < 150) {
            j = 0;
        }
        else if (networkPlayerInfoIn.getResponseTime() < 300) {
            j = 1;
        }
        else if (networkPlayerInfoIn.getResponseTime() < 600) {
            j = 2;
        }
        else if (networkPlayerInfoIn.getResponseTime() < 1000) {
            j = 3;
        }
        else {
            j = 4;
        }
        this.zLevel += 100.0f;
        this.drawTexturedModalRect(p_175245_2_ + p_175245_1_ - 11, p_175245_3_, 0 + i * 10, 176 + j * 8, 10, 8);
        this.zLevel -= 100.0f;
    }
    
    private void drawScoreboardValues(final ScoreObjective p_175247_1_, final int p_175247_2_, final String p_175247_3_, final int p_175247_4_, final int p_175247_5_, final NetworkPlayerInfo p_175247_6_) {
        final int i = p_175247_1_.getScoreboard().getValueFromObjective(p_175247_3_, p_175247_1_).getScorePoints();
        if (p_175247_1_.getRenderType() == IScoreObjectiveCriteria.EnumRenderType.HEARTS) {
            this.mc.getTextureManager().bindTexture(GuiPlayerTabOverlay.icons);
            if (this.lastTimeOpened == p_175247_6_.func_178855_p()) {
                if (i < p_175247_6_.func_178835_l()) {
                    p_175247_6_.func_178846_a(Minecraft.getSystemTime());
                    p_175247_6_.func_178844_b(this.guiIngame.getUpdateCounter() + 20);
                }
                else if (i > p_175247_6_.func_178835_l()) {
                    p_175247_6_.func_178846_a(Minecraft.getSystemTime());
                    p_175247_6_.func_178844_b(this.guiIngame.getUpdateCounter() + 10);
                }
            }
            if (Minecraft.getSystemTime() - p_175247_6_.func_178847_n() > 1000L || this.lastTimeOpened != p_175247_6_.func_178855_p()) {
                p_175247_6_.func_178836_b(i);
                p_175247_6_.func_178857_c(i);
                p_175247_6_.func_178846_a(Minecraft.getSystemTime());
            }
            p_175247_6_.func_178843_c(this.lastTimeOpened);
            p_175247_6_.func_178836_b(i);
            final int j = MathHelper.ceiling_float_int(Math.max(i, p_175247_6_.func_178860_m()) / 2.0f);
            final int k = Math.max(MathHelper.ceiling_float_int((float)(i / 2)), Math.max(MathHelper.ceiling_float_int((float)(p_175247_6_.func_178860_m() / 2)), 10));
            final boolean flag = p_175247_6_.func_178858_o() > this.guiIngame.getUpdateCounter() && (p_175247_6_.func_178858_o() - this.guiIngame.getUpdateCounter()) / 3L % 2L == 1L;
            if (j > 0) {
                final float f = Math.min((p_175247_5_ - p_175247_4_ - 4) / (float)k, 9.0f);
                if (f > 3.0f) {
                    for (int l = j; l < k; ++l) {
                        this.drawTexturedModalRect(p_175247_4_ + l * f, (float)p_175247_2_, flag ? 25 : 16, 0, 9, 9);
                    }
                    for (int j2 = 0; j2 < j; ++j2) {
                        this.drawTexturedModalRect(p_175247_4_ + j2 * f, (float)p_175247_2_, flag ? 25 : 16, 0, 9, 9);
                        if (flag) {
                            if (j2 * 2 + 1 < p_175247_6_.func_178860_m()) {
                                this.drawTexturedModalRect(p_175247_4_ + j2 * f, (float)p_175247_2_, 70, 0, 9, 9);
                            }
                            if (j2 * 2 + 1 == p_175247_6_.func_178860_m()) {
                                this.drawTexturedModalRect(p_175247_4_ + j2 * f, (float)p_175247_2_, 79, 0, 9, 9);
                            }
                        }
                        if (j2 * 2 + 1 < i) {
                            this.drawTexturedModalRect(p_175247_4_ + j2 * f, (float)p_175247_2_, (j2 >= 10) ? 160 : 52, 0, 9, 9);
                        }
                        if (j2 * 2 + 1 == i) {
                            this.drawTexturedModalRect(p_175247_4_ + j2 * f, (float)p_175247_2_, (j2 >= 10) ? 169 : 61, 0, 9, 9);
                        }
                    }
                }
                else {
                    final float f2 = MathHelper.clamp_float(i / 20.0f, 0.0f, 1.0f);
                    final int i2 = (int)((1.0f - f2) * 255.0f) << 16 | (int)(f2 * 255.0f) << 8;
                    String s = new StringBuilder().append(i / 2.0f).toString();
                    if (p_175247_5_ - this.mc.fontRendererObj.getStringWidth(String.valueOf(s) + "hp") >= p_175247_4_) {
                        s = String.valueOf(s) + "hp";
                    }
                    this.mc.fontRendererObj.drawStringWithShadow(s, (float)((p_175247_5_ + p_175247_4_) / 2 - this.mc.fontRendererObj.getStringWidth(s) / 2), (float)p_175247_2_, i2);
                }
            }
        }
        else {
            final String s2 = new StringBuilder().append(EnumChatFormatting.YELLOW).append(i).toString();
            this.mc.fontRendererObj.drawStringWithShadow(s2, (float)(p_175247_5_ - this.mc.fontRendererObj.getStringWidth(s2)), (float)p_175247_2_, 16777215);
        }
    }
    
    public void setFooter(final IChatComponent footerIn) {
        this.footer = footerIn;
    }
    
    public void setHeader(final IChatComponent headerIn) {
        this.header = headerIn;
    }
    
    public void func_181030_a() {
        this.header = null;
        this.footer = null;
    }
    
    static class PlayerComparator implements Comparator<NetworkPlayerInfo>
    {
        private PlayerComparator() {
        }
        
        @Override
        public int compare(final NetworkPlayerInfo p_compare_1_, final NetworkPlayerInfo p_compare_2_) {
            final ScorePlayerTeam scoreplayerteam = p_compare_1_.getPlayerTeam();
            final ScorePlayerTeam scoreplayerteam2 = p_compare_2_.getPlayerTeam();
            return ComparisonChain.start().compareTrueFirst(p_compare_1_.getGameType() != WorldSettings.GameType.SPECTATOR, p_compare_2_.getGameType() != WorldSettings.GameType.SPECTATOR).compare((scoreplayerteam != null) ? scoreplayerteam.getRegisteredName() : "", (scoreplayerteam2 != null) ? scoreplayerteam2.getRegisteredName() : "").compare(p_compare_1_.getGameProfile().getName(), p_compare_2_.getGameProfile().getName()).result();
        }
    }
}
