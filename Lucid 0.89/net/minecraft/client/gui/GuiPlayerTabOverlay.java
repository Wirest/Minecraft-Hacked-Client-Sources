package net.minecraft.client.gui;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.scoreboard.IScoreObjectiveCriteria;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.world.WorldSettings;

public class GuiPlayerTabOverlay extends Gui
{
    private static final Ordering field_175252_a = Ordering.from(new GuiPlayerTabOverlay.PlayerComparator(null));
    private final Minecraft mc;
    private final GuiIngame guiIngame;
    private IChatComponent footer;
    private IChatComponent header;

    /**
     * The last time the playerlist was opened (went from not being renderd, to being rendered)
     */
    private long lastTimeOpened;

    /** Weither or not the playerlist is currently being rendered */
    private boolean isBeingRendered;

    public GuiPlayerTabOverlay(Minecraft mcIn, GuiIngame guiIngameIn)
    {
        this.mc = mcIn;
        this.guiIngame = guiIngameIn;
    }

    /**
     * Returns the name that should be renderd for the player supplied
     */
    public String getPlayerName(NetworkPlayerInfo networkPlayerInfoIn)
    {
        return networkPlayerInfoIn.getDisplayName() != null ? networkPlayerInfoIn.getDisplayName().getFormattedText() : ScorePlayerTeam.formatPlayerName(networkPlayerInfoIn.getPlayerTeam(), networkPlayerInfoIn.getGameProfile().getName());
    }

    /**
     * Called by GuiIngame to update the information stored in the playerlist, does not actually render the list,
     * however.
     *  
     * @param willBeRendered True if the playerlist is intended to be renderd subsequently.
     */
    public void updatePlayerList(boolean willBeRendered)
    {
        if (willBeRendered && !this.isBeingRendered)
        {
            this.lastTimeOpened = Minecraft.getSystemTime();
        }

        this.isBeingRendered = willBeRendered;
    }

    /**
     * Renders the playerlist, its background, headers and footers.
     */
    public void renderPlayerlist(int width, Scoreboard scoreboardIn, ScoreObjective scoreObjectiveIn)
    {
        NetHandlerPlayClient var4 = this.mc.thePlayer.sendQueue;
        List var5 = field_175252_a.sortedCopy(var4.func_175106_d());
        int var6 = 0;
        int var7 = 0;
        Iterator var8 = var5.iterator();
        int var10;

        while (var8.hasNext())
        {
            NetworkPlayerInfo var9 = (NetworkPlayerInfo)var8.next();
            var10 = this.mc.fontRendererObj.getStringWidth(this.getPlayerName(var9));
            var6 = Math.max(var6, var10);

            if (scoreObjectiveIn != null && scoreObjectiveIn.getRenderType() != IScoreObjectiveCriteria.EnumRenderType.HEARTS)
            {
                var10 = this.mc.fontRendererObj.getStringWidth(" " + scoreboardIn.getValueFromObjective(var9.getGameProfile().getName(), scoreObjectiveIn).getScorePoints());
                var7 = Math.max(var7, var10);
            }
        }

        var5 = var5.subList(0, Math.min(var5.size(), 80));
        int var28 = var5.size();
        int var29 = var28;

        for (var10 = 1; var29 > 20; var29 = (var28 + var10 - 1) / var10)
        {
            ++var10;
        }

        boolean var11 = this.mc.isIntegratedServerRunning() || this.mc.getNetHandler().getNetworkManager().getIsencrypted();
        int var12;

        if (scoreObjectiveIn != null)
        {
            if (scoreObjectiveIn.getRenderType() == IScoreObjectiveCriteria.EnumRenderType.HEARTS)
            {
                var12 = 90;
            }
            else
            {
                var12 = var7;
            }
        }
        else
        {
            var12 = 0;
        }

        int var13 = Math.min(var10 * ((var11 ? 9 : 0) + var6 + var12 + 13), width - 50) / var10;
        int var14 = width / 2 - (var13 * var10 + (var10 - 1) * 5) / 2;
        int var15 = 10;
        int var16 = var13 * var10 + (var10 - 1) * 5;
        List var17 = null;
        List var18 = null;
        Iterator var19;
        String var20;

        if (this.header != null)
        {
            var17 = this.mc.fontRendererObj.listFormattedStringToWidth(this.header.getFormattedText(), width - 50);

            for (var19 = var17.iterator(); var19.hasNext(); var16 = Math.max(var16, this.mc.fontRendererObj.getStringWidth(var20)))
            {
                var20 = (String)var19.next();
            }
        }

        if (this.footer != null)
        {
            var18 = this.mc.fontRendererObj.listFormattedStringToWidth(this.footer.getFormattedText(), width - 50);

            for (var19 = var18.iterator(); var19.hasNext(); var16 = Math.max(var16, this.mc.fontRendererObj.getStringWidth(var20)))
            {
                var20 = (String)var19.next();
            }
        }

        int var21;

        if (var17 != null)
        {
            drawRect(width / 2 - var16 / 2 - 1, var15 - 1, width / 2 + var16 / 2 + 1, var15 + var17.size() * this.mc.fontRendererObj.FONT_HEIGHT, Integer.MIN_VALUE);

            for (var19 = var17.iterator(); var19.hasNext(); var15 += this.mc.fontRendererObj.FONT_HEIGHT)
            {
                var20 = (String)var19.next();
                var21 = this.mc.fontRendererObj.getStringWidth(var20);
                this.mc.fontRendererObj.drawStringWithShadow(var20, width / 2 - var21 / 2, var15, -1);
            }

            ++var15;
        }

        drawRect(width / 2 - var16 / 2 - 1, var15 - 1, width / 2 + var16 / 2 + 1, var15 + var29 * 9, Integer.MIN_VALUE);

        for (int var30 = 0; var30 < var28; ++var30)
        {
            int var31 = var30 / var29;
            var21 = var30 % var29;
            int var22 = var14 + var31 * var13 + var31 * 5;
            int var23 = var15 + var21 * 9;
            drawRect(var22, var23, var22 + var13, var23 + 8, 553648127);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.enableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);

            if (var30 < var5.size())
            {
                NetworkPlayerInfo var24 = (NetworkPlayerInfo)var5.get(var30);
                String var25 = this.getPlayerName(var24);

                if (var11)
                {
                    this.mc.getTextureManager().bindTexture(var24.getLocationSkin());
                    Gui.drawScaledCustomSizeModalRect(var22, var23, 8.0F, 8.0F, 8, 8, 8, 8, 64.0F, 64.0F);
                    EntityPlayer var26 = this.mc.theWorld.getPlayerEntityByUUID(var24.getGameProfile().getId());

                    if (var26 != null && var26.isWearing(EnumPlayerModelParts.HAT))
                    {
                        Gui.drawScaledCustomSizeModalRect(var22, var23, 40.0F, 8.0F, 8, 8, 8, 8, 64.0F, 64.0F);
                    }

                    var22 += 9;
                }

                if (var24.getGameType() == WorldSettings.GameType.SPECTATOR)
                {
                    var25 = EnumChatFormatting.ITALIC + var25;
                    this.mc.fontRendererObj.drawStringWithShadow(var25, var22, var23, -1862270977);
                }
                else
                {
                    this.mc.fontRendererObj.drawStringWithShadow(var25, var22, var23, -1);
                }

                if (scoreObjectiveIn != null && var24.getGameType() != WorldSettings.GameType.SPECTATOR)
                {
                    int var32 = var22 + var6 + 1;
                    int var27 = var32 + var12;

                    if (var27 - var32 > 5)
                    {
                        this.drawScoreboardValues(scoreObjectiveIn, var23, var24.getGameProfile().getName(), var32, var27, var24);
                    }
                }

                this.drawPing(var13, var22 - (var11 ? 9 : 0), var23, var24);
            }
        }

        if (var18 != null)
        {
            var15 += var29 * 9 + 1;
            drawRect(width / 2 - var16 / 2 - 1, var15 - 1, width / 2 + var16 / 2 + 1, var15 + var18.size() * this.mc.fontRendererObj.FONT_HEIGHT, Integer.MIN_VALUE);

            for (var19 = var18.iterator(); var19.hasNext(); var15 += this.mc.fontRendererObj.FONT_HEIGHT)
            {
                var20 = (String)var19.next();
                var21 = this.mc.fontRendererObj.getStringWidth(var20);
                this.mc.fontRendererObj.drawStringWithShadow(var20, width / 2 - var21 / 2, var15, -1);
            }
        }
    }

    protected void drawPing(int p_175245_1_, int p_175245_2_, int p_175245_3_, NetworkPlayerInfo networkPlayerInfoIn)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(icons);
        byte var5 = 0;
        byte var7;

        if (networkPlayerInfoIn.getResponseTime() < 0)
        {
            var7 = 5;
        }
        else if (networkPlayerInfoIn.getResponseTime() < 150)
        {
            var7 = 0;
        }
        else if (networkPlayerInfoIn.getResponseTime() < 300)
        {
            var7 = 1;
        }
        else if (networkPlayerInfoIn.getResponseTime() < 600)
        {
            var7 = 2;
        }
        else if (networkPlayerInfoIn.getResponseTime() < 1000)
        {
            var7 = 3;
        }
        else
        {
            var7 = 4;
        }

        this.zLevel += 100.0F;
        this.drawTexturedModalRect(p_175245_2_ + p_175245_1_ - 11, p_175245_3_, 0 + var5 * 10, 176 + var7 * 8, 10, 8);
        this.zLevel -= 100.0F;
    }

    private void drawScoreboardValues(ScoreObjective p_175247_1_, int p_175247_2_, String p_175247_3_, int p_175247_4_, int p_175247_5_, NetworkPlayerInfo p_175247_6_)
    {
        int var7 = p_175247_1_.getScoreboard().getValueFromObjective(p_175247_3_, p_175247_1_).getScorePoints();

        if (p_175247_1_.getRenderType() == IScoreObjectiveCriteria.EnumRenderType.HEARTS)
        {
            this.mc.getTextureManager().bindTexture(icons);

            if (this.lastTimeOpened == p_175247_6_.func_178855_p())
            {
                if (var7 < p_175247_6_.func_178835_l())
                {
                    p_175247_6_.func_178846_a(Minecraft.getSystemTime());
                    p_175247_6_.func_178844_b(this.guiIngame.getUpdateCounter() + 20);
                }
                else if (var7 > p_175247_6_.func_178835_l())
                {
                    p_175247_6_.func_178846_a(Minecraft.getSystemTime());
                    p_175247_6_.func_178844_b(this.guiIngame.getUpdateCounter() + 10);
                }
            }

            if (Minecraft.getSystemTime() - p_175247_6_.func_178847_n() > 1000L || this.lastTimeOpened != p_175247_6_.func_178855_p())
            {
                p_175247_6_.func_178836_b(var7);
                p_175247_6_.func_178857_c(var7);
                p_175247_6_.func_178846_a(Minecraft.getSystemTime());
            }

            p_175247_6_.func_178843_c(this.lastTimeOpened);
            p_175247_6_.func_178836_b(var7);
            int var8 = MathHelper.ceiling_float_int(Math.max(var7, p_175247_6_.func_178860_m()) / 2.0F);
            int var9 = Math.max(MathHelper.ceiling_float_int(var7 / 2), Math.max(MathHelper.ceiling_float_int(p_175247_6_.func_178860_m() / 2), 10));
            boolean var10 = p_175247_6_.func_178858_o() > this.guiIngame.getUpdateCounter() && (p_175247_6_.func_178858_o() - this.guiIngame.getUpdateCounter()) / 3L % 2L == 1L;

            if (var8 > 0)
            {
                float var11 = Math.min((float)(p_175247_5_ - p_175247_4_ - 4) / (float)var9, 9.0F);

                if (var11 > 3.0F)
                {
                    int var12;

                    for (var12 = var8; var12 < var9; ++var12)
                    {
                        this.drawTexturedModalRect(p_175247_4_ + var12 * var11, p_175247_2_, var10 ? 25 : 16, 0, 9, 9);
                    }

                    for (var12 = 0; var12 < var8; ++var12)
                    {
                        this.drawTexturedModalRect(p_175247_4_ + var12 * var11, p_175247_2_, var10 ? 25 : 16, 0, 9, 9);

                        if (var10)
                        {
                            if (var12 * 2 + 1 < p_175247_6_.func_178860_m())
                            {
                                this.drawTexturedModalRect(p_175247_4_ + var12 * var11, p_175247_2_, 70, 0, 9, 9);
                            }

                            if (var12 * 2 + 1 == p_175247_6_.func_178860_m())
                            {
                                this.drawTexturedModalRect(p_175247_4_ + var12 * var11, p_175247_2_, 79, 0, 9, 9);
                            }
                        }

                        if (var12 * 2 + 1 < var7)
                        {
                            this.drawTexturedModalRect(p_175247_4_ + var12 * var11, p_175247_2_, var12 >= 10 ? 160 : 52, 0, 9, 9);
                        }

                        if (var12 * 2 + 1 == var7)
                        {
                            this.drawTexturedModalRect(p_175247_4_ + var12 * var11, p_175247_2_, var12 >= 10 ? 169 : 61, 0, 9, 9);
                        }
                    }
                }
                else
                {
                    float var16 = MathHelper.clamp_float(var7 / 20.0F, 0.0F, 1.0F);
                    int var13 = (int)((1.0F - var16) * 255.0F) << 16 | (int)(var16 * 255.0F) << 8;
                    String var14 = "" + var7 / 2.0F;

                    if (p_175247_5_ - this.mc.fontRendererObj.getStringWidth(var14 + "hp") >= p_175247_4_)
                    {
                        var14 = var14 + "hp";
                    }

                    this.mc.fontRendererObj.drawStringWithShadow(var14, (p_175247_5_ + p_175247_4_) / 2 - this.mc.fontRendererObj.getStringWidth(var14) / 2, p_175247_2_, var13);
                }
            }
        }
        else
        {
            String var15 = EnumChatFormatting.YELLOW + "" + var7;
            this.mc.fontRendererObj.drawStringWithShadow(var15, p_175247_5_ - this.mc.fontRendererObj.getStringWidth(var15), p_175247_2_, 16777215);
        }
    }

    public void setFooter(IChatComponent footerIn)
    {
        this.footer = footerIn;
    }

    public void setHeader(IChatComponent headerIn)
    {
        this.header = headerIn;
    }

    static class PlayerComparator implements Comparator
    {

        private PlayerComparator() {}

        public int compare(NetworkPlayerInfo p_178952_1_, NetworkPlayerInfo p_178952_2_)
        {
            ScorePlayerTeam var3 = p_178952_1_.getPlayerTeam();
            ScorePlayerTeam var4 = p_178952_2_.getPlayerTeam();
            return ComparisonChain.start().compareTrueFirst(p_178952_1_.getGameType() != WorldSettings.GameType.SPECTATOR, p_178952_2_.getGameType() != WorldSettings.GameType.SPECTATOR).compare(var3 != null ? var3.getRegisteredName() : "", var4 != null ? var4.getRegisteredName() : "").compare(p_178952_1_.getGameProfile().getName(), p_178952_2_.getGameProfile().getName()).result();
        }

        @Override
		public int compare(Object p_compare_1_, Object p_compare_2_)
        {
            return this.compare((NetworkPlayerInfo)p_compare_1_, (NetworkPlayerInfo)p_compare_2_);
        }

        PlayerComparator(Object p_i45528_1_)
        {
            this();
        }
    }
}
