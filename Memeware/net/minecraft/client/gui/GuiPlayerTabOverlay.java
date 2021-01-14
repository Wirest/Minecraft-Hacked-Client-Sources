package net.minecraft.client.gui;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

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

public class GuiPlayerTabOverlay extends Gui {
    private static final Ordering field_175252_a = Ordering.from(new GuiPlayerTabOverlay.PlayerComparator(null));
    private final Minecraft field_175250_f;
    private final GuiIngame field_175251_g;
    private IChatComponent footer;
    private IChatComponent header;
    private long field_175253_j;
    private boolean field_175254_k;
    private static final String __OBFID = "CL_00001943";

    public GuiPlayerTabOverlay(Minecraft mcIn, GuiIngame p_i45529_2_) {
        this.field_175250_f = mcIn;
        this.field_175251_g = p_i45529_2_;
    }

    public String func_175243_a(NetworkPlayerInfo p_175243_1_) {
        return p_175243_1_.func_178854_k() != null ? p_175243_1_.func_178854_k().getFormattedText() : ScorePlayerTeam.formatPlayerName(p_175243_1_.func_178850_i(), p_175243_1_.func_178845_a().getName());
    }

    public void func_175246_a(boolean p_175246_1_) {
        if (p_175246_1_ && !this.field_175254_k) {
            this.field_175253_j = Minecraft.getSystemTime();
        }

        this.field_175254_k = p_175246_1_;
    }

    public void func_175249_a(int p_175249_1_, Scoreboard p_175249_2_, ScoreObjective p_175249_3_) {
        NetHandlerPlayClient var4 = this.field_175250_f.thePlayer.sendQueue;
        List var5 = field_175252_a.sortedCopy(var4.func_175106_d());
        int var6 = 0;
        int var7 = 0;
        Iterator var8 = var5.iterator();
        int var10;

        while (var8.hasNext()) {
            NetworkPlayerInfo var9 = (NetworkPlayerInfo) var8.next();
            var10 = this.field_175250_f.fontRendererObj.getStringWidth(this.func_175243_a(var9));
            var6 = Math.max(var6, var10);

            if (p_175249_3_ != null && p_175249_3_.func_178766_e() != IScoreObjectiveCriteria.EnumRenderType.HEARTS) {
                var10 = this.field_175250_f.fontRendererObj.getStringWidth(" " + p_175249_2_.getValueFromObjective(var9.func_178845_a().getName(), p_175249_3_).getScorePoints());
                var7 = Math.max(var7, var10);
            }
        }

        var5 = var5.subList(0, Math.min(var5.size(), 80));
        int var28 = var5.size();
        int var29 = var28;

        for (var10 = 1; var29 > 20; var29 = (var28 + var10 - 1) / var10) {
            ++var10;
        }

        boolean var11 = this.field_175250_f.isIntegratedServerRunning() || this.field_175250_f.getNetHandler().getNetworkManager().func_179292_f();
        int var12;

        if (p_175249_3_ != null) {
            if (p_175249_3_.func_178766_e() == IScoreObjectiveCriteria.EnumRenderType.HEARTS) {
                var12 = 90;
            } else {
                var12 = var7;
            }
        } else {
            var12 = 0;
        }

        int var13 = Math.min(var10 * ((var11 ? 9 : 0) + var6 + var12 + 13), p_175249_1_ - 50) / var10;
        int var14 = p_175249_1_ / 2 - (var13 * var10 + (var10 - 1) * 5) / 2;
        int var15 = 10;
        int var16 = var13 * var10 + (var10 - 1) * 5;
        List var17 = null;
        List var18 = null;
        Iterator var19;
        String var20;

        if (this.header != null) {
            var17 = this.field_175250_f.fontRendererObj.listFormattedStringToWidth(this.header.getFormattedText(), p_175249_1_ - 50);

            for (var19 = var17.iterator(); var19.hasNext(); var16 = Math.max(var16, this.field_175250_f.fontRendererObj.getStringWidth(var20))) {
                var20 = (String) var19.next();
            }
        }

        if (this.footer != null) {
            var18 = this.field_175250_f.fontRendererObj.listFormattedStringToWidth(this.footer.getFormattedText(), p_175249_1_ - 50);

            for (var19 = var18.iterator(); var19.hasNext(); var16 = Math.max(var16, this.field_175250_f.fontRendererObj.getStringWidth(var20))) {
                var20 = (String) var19.next();
            }
        }

        int var21;

        if (var17 != null) {
            drawRect(p_175249_1_ / 2 - var16 / 2 - 1, var15 - 1, p_175249_1_ / 2 + var16 / 2 + 1, var15 + var17.size() * this.field_175250_f.fontRendererObj.FONT_HEIGHT, Integer.MIN_VALUE);

            for (var19 = var17.iterator(); var19.hasNext(); var15 += this.field_175250_f.fontRendererObj.FONT_HEIGHT) {
                var20 = (String) var19.next();
                var21 = this.field_175250_f.fontRendererObj.getStringWidth(var20);
                this.field_175250_f.fontRendererObj.func_175063_a(var20, (float) (p_175249_1_ / 2 - var21 / 2), (float) var15, -1);
            }

            ++var15;
        }

        drawRect(p_175249_1_ / 2 - var16 / 2 - 1, var15 - 1, p_175249_1_ / 2 + var16 / 2 + 1, var15 + var29 * 9, Integer.MIN_VALUE);

        for (int var30 = 0; var30 < var28; ++var30) {
            int var31 = var30 / var29;
            var21 = var30 % var29;
            int var22 = var14 + var31 * var13 + var31 * 5;
            int var23 = var15 + var21 * 9;
            drawRect(var22, var23, var22 + var13, var23 + 8, 553648127);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.enableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);

            if (var30 < var5.size()) {
                NetworkPlayerInfo var24 = (NetworkPlayerInfo) var5.get(var30);
                String var25 = this.func_175243_a(var24);

                if (var11) {
                    this.field_175250_f.getTextureManager().bindTexture(var24.func_178837_g());
                    Gui.drawScaledCustomSizeModalRect(var22, var23, 8.0F, 8.0F, 8, 8, 8, 8, 64.0F, 64.0F);
                    EntityPlayer var26 = this.field_175250_f.theWorld.getPlayerEntityByUUID(var24.func_178845_a().getId());

                    if (var26 != null && var26.func_175148_a(EnumPlayerModelParts.HAT)) {
                        Gui.drawScaledCustomSizeModalRect(var22, var23, 40.0F, 8.0F, 8, 8, 8, 8, 64.0F, 64.0F);
                    }

                    var22 += 9;
                }

                if (var24.getGameType() == WorldSettings.GameType.SPECTATOR) {
                    var25 = EnumChatFormatting.ITALIC + var25;
                    this.field_175250_f.fontRendererObj.func_175063_a(var25, (float) var22, (float) var23, -1862270977);
                } else {
                    this.field_175250_f.fontRendererObj.func_175063_a(var25, (float) var22, (float) var23, -1);
                }

                if (p_175249_3_ != null && var24.getGameType() != WorldSettings.GameType.SPECTATOR) {
                    int var32 = var22 + var6 + 1;
                    int var27 = var32 + var12;

                    if (var27 - var32 > 5) {
                        this.func_175247_a(p_175249_3_, var23, var24.func_178845_a().getName(), var32, var27, var24);
                    }
                }

                this.func_175245_a(var13, var22 - (var11 ? 9 : 0), var23, var24);
            }
        }

        if (var18 != null) {
            var15 += var29 * 9 + 1;
            drawRect(p_175249_1_ / 2 - var16 / 2 - 1, var15 - 1, p_175249_1_ / 2 + var16 / 2 + 1, var15 + var18.size() * this.field_175250_f.fontRendererObj.FONT_HEIGHT, Integer.MIN_VALUE);

            for (var19 = var18.iterator(); var19.hasNext(); var15 += this.field_175250_f.fontRendererObj.FONT_HEIGHT) {
                var20 = (String) var19.next();
                var21 = this.field_175250_f.fontRendererObj.getStringWidth(var20);
                this.field_175250_f.fontRendererObj.func_175063_a(var20, (float) (p_175249_1_ / 2 - var21 / 2), (float) var15, -1);
            }
        }
    }

    protected void func_175245_a(int p_175245_1_, int p_175245_2_, int p_175245_3_, NetworkPlayerInfo p_175245_4_) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.field_175250_f.getTextureManager().bindTexture(icons);
        byte var5 = 0;
        boolean var6 = false;
        byte var7;

        if (p_175245_4_.getResponseTime() < 0) {
            var7 = 5;
        } else if (p_175245_4_.getResponseTime() < 150) {
            var7 = 0;
        } else if (p_175245_4_.getResponseTime() < 300) {
            var7 = 1;
        } else if (p_175245_4_.getResponseTime() < 600) {
            var7 = 2;
        } else if (p_175245_4_.getResponseTime() < 1000) {
            var7 = 3;
        } else {
            var7 = 4;
        }

        this.zLevel += 100.0F;
        this.drawTexturedModalRect(p_175245_2_ + p_175245_1_ - 11, p_175245_3_, 0 + var5 * 10, 176 + var7 * 8, 10, 8);
        this.zLevel -= 100.0F;
    }

    private void func_175247_a(ScoreObjective p_175247_1_, int p_175247_2_, String p_175247_3_, int p_175247_4_, int p_175247_5_, NetworkPlayerInfo p_175247_6_) {
        int var7 = p_175247_1_.getScoreboard().getValueFromObjective(p_175247_3_, p_175247_1_).getScorePoints();

        if (p_175247_1_.func_178766_e() == IScoreObjectiveCriteria.EnumRenderType.HEARTS) {
            this.field_175250_f.getTextureManager().bindTexture(icons);

            if (this.field_175253_j == p_175247_6_.func_178855_p()) {
                if (var7 < p_175247_6_.func_178835_l()) {
                    p_175247_6_.func_178846_a(Minecraft.getSystemTime());
                    p_175247_6_.func_178844_b((long) (this.field_175251_g.getUpdateCounter() + 20));
                } else if (var7 > p_175247_6_.func_178835_l()) {
                    p_175247_6_.func_178846_a(Minecraft.getSystemTime());
                    p_175247_6_.func_178844_b((long) (this.field_175251_g.getUpdateCounter() + 10));
                }
            }

            if (Minecraft.getSystemTime() - p_175247_6_.func_178847_n() > 1000L || this.field_175253_j != p_175247_6_.func_178855_p()) {
                p_175247_6_.func_178836_b(var7);
                p_175247_6_.func_178857_c(var7);
                p_175247_6_.func_178846_a(Minecraft.getSystemTime());
            }

            p_175247_6_.func_178843_c(this.field_175253_j);
            p_175247_6_.func_178836_b(var7);
            int var8 = MathHelper.ceiling_float_int((float) Math.max(var7, p_175247_6_.func_178860_m()) / 2.0F);
            int var9 = Math.max(MathHelper.ceiling_float_int((float) (var7 / 2)), Math.max(MathHelper.ceiling_float_int((float) (p_175247_6_.func_178860_m() / 2)), 10));
            boolean var10 = p_175247_6_.func_178858_o() > (long) this.field_175251_g.getUpdateCounter() && (p_175247_6_.func_178858_o() - (long) this.field_175251_g.getUpdateCounter()) / 3L % 2L == 1L;

            if (var8 > 0) {
                float var11 = Math.min((float) (p_175247_5_ - p_175247_4_ - 4) / (float) var9, 9.0F);

                if (var11 > 3.0F) {
                    int var12;

                    for (var12 = var8; var12 < var9; ++var12) {
                        this.func_175174_a((float) p_175247_4_ + (float) var12 * var11, (float) p_175247_2_, var10 ? 25 : 16, 0, 9, 9);
                    }

                    for (var12 = 0; var12 < var8; ++var12) {
                        this.func_175174_a((float) p_175247_4_ + (float) var12 * var11, (float) p_175247_2_, var10 ? 25 : 16, 0, 9, 9);

                        if (var10) {
                            if (var12 * 2 + 1 < p_175247_6_.func_178860_m()) {
                                this.func_175174_a((float) p_175247_4_ + (float) var12 * var11, (float) p_175247_2_, 70, 0, 9, 9);
                            }

                            if (var12 * 2 + 1 == p_175247_6_.func_178860_m()) {
                                this.func_175174_a((float) p_175247_4_ + (float) var12 * var11, (float) p_175247_2_, 79, 0, 9, 9);
                            }
                        }

                        if (var12 * 2 + 1 < var7) {
                            this.func_175174_a((float) p_175247_4_ + (float) var12 * var11, (float) p_175247_2_, var12 >= 10 ? 160 : 52, 0, 9, 9);
                        }

                        if (var12 * 2 + 1 == var7) {
                            this.func_175174_a((float) p_175247_4_ + (float) var12 * var11, (float) p_175247_2_, var12 >= 10 ? 169 : 61, 0, 9, 9);
                        }
                    }
                } else {
                    float var16 = MathHelper.clamp_float((float) var7 / 20.0F, 0.0F, 1.0F);
                    int var13 = (int) ((1.0F - var16) * 255.0F) << 16 | (int) (var16 * 255.0F) << 8;
                    String var14 = "" + (float) var7 / 2.0F;

                    if (p_175247_5_ - this.field_175250_f.fontRendererObj.getStringWidth(var14 + "hp") >= p_175247_4_) {
                        var14 = var14 + "hp";
                    }

                    this.field_175250_f.fontRendererObj.func_175063_a(var14, (float) ((p_175247_5_ + p_175247_4_) / 2 - this.field_175250_f.fontRendererObj.getStringWidth(var14) / 2), (float) p_175247_2_, var13);
                }
            }
        } else {
            String var15 = EnumChatFormatting.YELLOW + "" + var7;
            this.field_175250_f.fontRendererObj.func_175063_a(var15, (float) (p_175247_5_ - this.field_175250_f.fontRendererObj.getStringWidth(var15)), (float) p_175247_2_, 16777215);
        }
    }

    public void setFooter(IChatComponent p_175248_1_) {
        this.footer = p_175248_1_;
    }

    public void setHeader(IChatComponent p_175244_1_) {
        this.header = p_175244_1_;
    }

    static class PlayerComparator implements Comparator {
        private static final String __OBFID = "CL_00001941";

        private PlayerComparator() {
        }

        public int func_178952_a(NetworkPlayerInfo p_178952_1_, NetworkPlayerInfo p_178952_2_) {
            ScorePlayerTeam var3 = p_178952_1_.func_178850_i();
            ScorePlayerTeam var4 = p_178952_2_.func_178850_i();
            return ComparisonChain.start().compareTrueFirst(p_178952_1_.getGameType() != WorldSettings.GameType.SPECTATOR, p_178952_2_.getGameType() != WorldSettings.GameType.SPECTATOR).compare(var3 != null ? var3.getRegisteredName() : "", var4 != null ? var4.getRegisteredName() : "").compare(p_178952_1_.func_178845_a().getName(), p_178952_2_.func_178845_a().getName()).result();
        }

        public int compare(Object p_compare_1_, Object p_compare_2_) {
            return this.func_178952_a((NetworkPlayerInfo) p_compare_1_, (NetworkPlayerInfo) p_compare_2_);
        }

        PlayerComparator(Object p_i45528_1_) {
            this();
        }
    }
}
