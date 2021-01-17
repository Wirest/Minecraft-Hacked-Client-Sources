// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui.achievement;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.stats.Achievement;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import java.util.Random;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Mouse;
import net.minecraft.client.Minecraft;
import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptionButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatFileWriter;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.gui.IProgressMeter;
import net.minecraft.client.gui.GuiScreen;

public class GuiAchievements extends GuiScreen implements IProgressMeter
{
    private static final int field_146572_y;
    private static final int field_146571_z;
    private static final int field_146559_A;
    private static final int field_146560_B;
    private static final ResourceLocation ACHIEVEMENT_BACKGROUND;
    protected GuiScreen parentScreen;
    protected int field_146555_f;
    protected int field_146557_g;
    protected int field_146563_h;
    protected int field_146564_i;
    protected float field_146570_r;
    protected double field_146569_s;
    protected double field_146568_t;
    protected double field_146567_u;
    protected double field_146566_v;
    protected double field_146565_w;
    protected double field_146573_x;
    private int field_146554_D;
    private StatFileWriter statFileWriter;
    private boolean loadingAchievements;
    
    static {
        field_146572_y = AchievementList.minDisplayColumn * 24 - 112;
        field_146571_z = AchievementList.minDisplayRow * 24 - 112;
        field_146559_A = AchievementList.maxDisplayColumn * 24 - 77;
        field_146560_B = AchievementList.maxDisplayRow * 24 - 77;
        ACHIEVEMENT_BACKGROUND = new ResourceLocation("textures/gui/achievement/achievement_background.png");
    }
    
    public GuiAchievements(final GuiScreen parentScreenIn, final StatFileWriter statFileWriterIn) {
        this.field_146555_f = 256;
        this.field_146557_g = 202;
        this.field_146570_r = 1.0f;
        this.loadingAchievements = true;
        this.parentScreen = parentScreenIn;
        this.statFileWriter = statFileWriterIn;
        final int i = 141;
        final int j = 141;
        final double field_146569_s = AchievementList.openInventory.displayColumn * 24 - i / 2 - 12;
        this.field_146565_w = field_146569_s;
        this.field_146567_u = field_146569_s;
        this.field_146569_s = field_146569_s;
        final double field_146568_t = AchievementList.openInventory.displayRow * 24 - j / 2;
        this.field_146573_x = field_146568_t;
        this.field_146566_v = field_146568_t;
        this.field_146568_t = field_146568_t;
    }
    
    @Override
    public void initGui() {
        this.mc.getNetHandler().addToSendQueue(new C16PacketClientStatus(C16PacketClientStatus.EnumState.REQUEST_STATS));
        this.buttonList.clear();
        this.buttonList.add(new GuiOptionButton(1, this.width / 2 + 24, this.height / 2 + 74, 80, 20, I18n.format("gui.done", new Object[0])));
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (!this.loadingAchievements && button.id == 1) {
            this.mc.displayGuiScreen(this.parentScreen);
        }
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (keyCode == this.mc.gameSettings.keyBindInventory.getKeyCode()) {
            this.mc.displayGuiScreen(null);
            this.mc.setIngameFocus();
        }
        else {
            super.keyTyped(typedChar, keyCode);
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        if (this.loadingAchievements) {
            this.drawDefaultBackground();
            this.drawCenteredString(this.fontRendererObj, I18n.format("multiplayer.downloadingStats", new Object[0]), this.width / 2, this.height / 2, 16777215);
            this.drawCenteredString(this.fontRendererObj, GuiAchievements.lanSearchStates[(int)(Minecraft.getSystemTime() / 150L % GuiAchievements.lanSearchStates.length)], this.width / 2, this.height / 2 + this.fontRendererObj.FONT_HEIGHT * 2, 16777215);
        }
        else {
            if (Mouse.isButtonDown(0)) {
                final int i = (this.width - this.field_146555_f) / 2;
                final int j = (this.height - this.field_146557_g) / 2;
                final int k = i + 8;
                final int l = j + 17;
                if ((this.field_146554_D == 0 || this.field_146554_D == 1) && mouseX >= k && mouseX < k + 224 && mouseY >= l && mouseY < l + 155) {
                    if (this.field_146554_D == 0) {
                        this.field_146554_D = 1;
                    }
                    else {
                        this.field_146567_u -= (mouseX - this.field_146563_h) * this.field_146570_r;
                        this.field_146566_v -= (mouseY - this.field_146564_i) * this.field_146570_r;
                        final double field_146567_u = this.field_146567_u;
                        this.field_146569_s = field_146567_u;
                        this.field_146565_w = field_146567_u;
                        final double field_146566_v = this.field_146566_v;
                        this.field_146568_t = field_146566_v;
                        this.field_146573_x = field_146566_v;
                    }
                    this.field_146563_h = mouseX;
                    this.field_146564_i = mouseY;
                }
            }
            else {
                this.field_146554_D = 0;
            }
            final int i2 = Mouse.getDWheel();
            final float f3 = this.field_146570_r;
            if (i2 < 0) {
                this.field_146570_r += 0.25f;
            }
            else if (i2 > 0) {
                this.field_146570_r -= 0.25f;
            }
            this.field_146570_r = MathHelper.clamp_float(this.field_146570_r, 1.0f, 2.0f);
            if (this.field_146570_r != f3) {
                final float f4 = f3 - this.field_146570_r;
                final float f5 = f3 * this.field_146555_f;
                final float f6 = f3 * this.field_146557_g;
                final float f7 = this.field_146570_r * this.field_146555_f;
                final float f8 = this.field_146570_r * this.field_146557_g;
                this.field_146567_u -= (f7 - f5) * 0.5f;
                this.field_146566_v -= (f8 - f6) * 0.5f;
                final double field_146567_u2 = this.field_146567_u;
                this.field_146569_s = field_146567_u2;
                this.field_146565_w = field_146567_u2;
                final double field_146566_v2 = this.field_146566_v;
                this.field_146568_t = field_146566_v2;
                this.field_146573_x = field_146566_v2;
            }
            if (this.field_146565_w < GuiAchievements.field_146572_y) {
                this.field_146565_w = GuiAchievements.field_146572_y;
            }
            if (this.field_146573_x < GuiAchievements.field_146571_z) {
                this.field_146573_x = GuiAchievements.field_146571_z;
            }
            if (this.field_146565_w >= GuiAchievements.field_146559_A) {
                this.field_146565_w = GuiAchievements.field_146559_A - 1;
            }
            if (this.field_146573_x >= GuiAchievements.field_146560_B) {
                this.field_146573_x = GuiAchievements.field_146560_B - 1;
            }
            this.drawDefaultBackground();
            this.drawAchievementScreen(mouseX, mouseY, partialTicks);
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            this.drawTitle();
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
        }
    }
    
    @Override
    public void doneLoading() {
        if (this.loadingAchievements) {
            this.loadingAchievements = false;
        }
    }
    
    @Override
    public void updateScreen() {
        if (!this.loadingAchievements) {
            this.field_146569_s = this.field_146567_u;
            this.field_146568_t = this.field_146566_v;
            final double d0 = this.field_146565_w - this.field_146567_u;
            final double d2 = this.field_146573_x - this.field_146566_v;
            if (d0 * d0 + d2 * d2 < 4.0) {
                this.field_146567_u += d0;
                this.field_146566_v += d2;
            }
            else {
                this.field_146567_u += d0 * 0.85;
                this.field_146566_v += d2 * 0.85;
            }
        }
    }
    
    protected void drawTitle() {
        final int i = (this.width - this.field_146555_f) / 2;
        final int j = (this.height - this.field_146557_g) / 2;
        this.fontRendererObj.drawString(I18n.format("gui.achievements", new Object[0]), i + 15, j + 5, 4210752);
    }
    
    protected void drawAchievementScreen(final int p_146552_1_, final int p_146552_2_, final float p_146552_3_) {
        int i = MathHelper.floor_double(this.field_146569_s + (this.field_146567_u - this.field_146569_s) * p_146552_3_);
        int j = MathHelper.floor_double(this.field_146568_t + (this.field_146566_v - this.field_146568_t) * p_146552_3_);
        if (i < GuiAchievements.field_146572_y) {
            i = GuiAchievements.field_146572_y;
        }
        if (j < GuiAchievements.field_146571_z) {
            j = GuiAchievements.field_146571_z;
        }
        if (i >= GuiAchievements.field_146559_A) {
            i = GuiAchievements.field_146559_A - 1;
        }
        if (j >= GuiAchievements.field_146560_B) {
            j = GuiAchievements.field_146560_B - 1;
        }
        final int k = (this.width - this.field_146555_f) / 2;
        final int l = (this.height - this.field_146557_g) / 2;
        final int i2 = k + 16;
        final int j2 = l + 17;
        this.zLevel = 0.0f;
        GlStateManager.depthFunc(518);
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)i2, (float)j2, -200.0f);
        GlStateManager.scale(1.0f / this.field_146570_r, 1.0f / this.field_146570_r, 0.0f);
        GlStateManager.enableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableColorMaterial();
        final int k2 = i + 288 >> 4;
        final int l2 = j + 288 >> 4;
        final int i3 = (i + 288) % 16;
        final int j3 = (j + 288) % 16;
        final int k3 = 4;
        final int l3 = 8;
        final int i4 = 10;
        final int j4 = 22;
        final int k4 = 37;
        final Random random = new Random();
        final float f = 16.0f / this.field_146570_r;
        final float f2 = 16.0f / this.field_146570_r;
        for (int l4 = 0; l4 * f - j3 < 155.0f; ++l4) {
            final float f3 = 0.6f - (l2 + l4) / 25.0f * 0.3f;
            GlStateManager.color(f3, f3, f3, 1.0f);
            for (int i5 = 0; i5 * f2 - i3 < 224.0f; ++i5) {
                random.setSeed(this.mc.getSession().getPlayerID().hashCode() + k2 + i5 + (l2 + l4) * 16);
                final int j5 = random.nextInt(1 + l2 + l4) + (l2 + l4) / 2;
                TextureAtlasSprite textureatlassprite = this.func_175371_a(Blocks.sand);
                if (j5 <= 37 && l2 + l4 != 35) {
                    if (j5 == 22) {
                        if (random.nextInt(2) == 0) {
                            textureatlassprite = this.func_175371_a(Blocks.diamond_ore);
                        }
                        else {
                            textureatlassprite = this.func_175371_a(Blocks.redstone_ore);
                        }
                    }
                    else if (j5 == 10) {
                        textureatlassprite = this.func_175371_a(Blocks.iron_ore);
                    }
                    else if (j5 == 8) {
                        textureatlassprite = this.func_175371_a(Blocks.coal_ore);
                    }
                    else if (j5 > 4) {
                        textureatlassprite = this.func_175371_a(Blocks.stone);
                    }
                    else if (j5 > 0) {
                        textureatlassprite = this.func_175371_a(Blocks.dirt);
                    }
                }
                else {
                    final Block block = Blocks.bedrock;
                    textureatlassprite = this.func_175371_a(block);
                }
                this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
                this.drawTexturedModalRect(i5 * 16 - i3, l4 * 16 - j3, textureatlassprite, 16, 16);
            }
        }
        GlStateManager.enableDepth();
        GlStateManager.depthFunc(515);
        this.mc.getTextureManager().bindTexture(GuiAchievements.ACHIEVEMENT_BACKGROUND);
        for (int j6 = 0; j6 < AchievementList.achievementList.size(); ++j6) {
            final Achievement achievement1 = AchievementList.achievementList.get(j6);
            if (achievement1.parentAchievement != null) {
                final int k5 = achievement1.displayColumn * 24 - i + 11;
                final int l5 = achievement1.displayRow * 24 - j + 11;
                final int j7 = achievement1.parentAchievement.displayColumn * 24 - i + 11;
                final int k6 = achievement1.parentAchievement.displayRow * 24 - j + 11;
                final boolean flag = this.statFileWriter.hasAchievementUnlocked(achievement1);
                final boolean flag2 = this.statFileWriter.canUnlockAchievement(achievement1);
                final int k7 = this.statFileWriter.func_150874_c(achievement1);
                if (k7 <= 4) {
                    int l6 = -16777216;
                    if (flag) {
                        l6 = -6250336;
                    }
                    else if (flag2) {
                        l6 = -16711936;
                    }
                    this.drawHorizontalLine(k5, j7, l5, l6);
                    this.drawVerticalLine(j7, l5, k6, l6);
                    if (k5 > j7) {
                        this.drawTexturedModalRect(k5 - 11 - 7, l5 - 5, 114, 234, 7, 11);
                    }
                    else if (k5 < j7) {
                        this.drawTexturedModalRect(k5 + 11, l5 - 5, 107, 234, 7, 11);
                    }
                    else if (l5 > k6) {
                        this.drawTexturedModalRect(k5 - 5, l5 - 11 - 7, 96, 234, 11, 7);
                    }
                    else if (l5 < k6) {
                        this.drawTexturedModalRect(k5 - 5, l5 + 11, 96, 241, 11, 7);
                    }
                }
            }
        }
        Achievement achievement2 = null;
        final float f4 = (p_146552_1_ - i2) * this.field_146570_r;
        final float f5 = (p_146552_2_ - j2) * this.field_146570_r;
        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.disableLighting();
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableColorMaterial();
        for (int i6 = 0; i6 < AchievementList.achievementList.size(); ++i6) {
            final Achievement achievement3 = AchievementList.achievementList.get(i6);
            final int l7 = achievement3.displayColumn * 24 - i;
            final int j8 = achievement3.displayRow * 24 - j;
            if (l7 >= -24 && j8 >= -24 && l7 <= 224.0f * this.field_146570_r && j8 <= 155.0f * this.field_146570_r) {
                final int l8 = this.statFileWriter.func_150874_c(achievement3);
                if (this.statFileWriter.hasAchievementUnlocked(achievement3)) {
                    final float f6 = 0.75f;
                    GlStateManager.color(f6, f6, f6, 1.0f);
                }
                else if (this.statFileWriter.canUnlockAchievement(achievement3)) {
                    final float f7 = 1.0f;
                    GlStateManager.color(f7, f7, f7, 1.0f);
                }
                else if (l8 < 3) {
                    final float f8 = 0.3f;
                    GlStateManager.color(f8, f8, f8, 1.0f);
                }
                else if (l8 == 3) {
                    final float f9 = 0.2f;
                    GlStateManager.color(f9, f9, f9, 1.0f);
                }
                else {
                    if (l8 != 4) {
                        continue;
                    }
                    final float f10 = 0.1f;
                    GlStateManager.color(f10, f10, f10, 1.0f);
                }
                this.mc.getTextureManager().bindTexture(GuiAchievements.ACHIEVEMENT_BACKGROUND);
                if (achievement3.getSpecial()) {
                    this.drawTexturedModalRect(l7 - 2, j8 - 2, 26, 202, 26, 26);
                }
                else {
                    this.drawTexturedModalRect(l7 - 2, j8 - 2, 0, 202, 26, 26);
                }
                if (!this.statFileWriter.canUnlockAchievement(achievement3)) {
                    final float f11 = 0.1f;
                    GlStateManager.color(f11, f11, f11, 1.0f);
                    this.itemRender.func_175039_a(false);
                }
                GlStateManager.enableLighting();
                GlStateManager.enableCull();
                this.itemRender.renderItemAndEffectIntoGUI(achievement3.theItemStack, l7 + 3, j8 + 3);
                GlStateManager.blendFunc(770, 771);
                GlStateManager.disableLighting();
                if (!this.statFileWriter.canUnlockAchievement(achievement3)) {
                    this.itemRender.func_175039_a(true);
                }
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                if (f4 >= l7 && f4 <= l7 + 22 && f5 >= j8 && f5 <= j8 + 22) {
                    achievement2 = achievement3;
                }
            }
        }
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GlStateManager.popMatrix();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(GuiAchievements.ACHIEVEMENT_BACKGROUND);
        this.drawTexturedModalRect(k, l, 0, 0, this.field_146555_f, this.field_146557_g);
        this.zLevel = 0.0f;
        GlStateManager.depthFunc(515);
        GlStateManager.disableDepth();
        GlStateManager.enableTexture2D();
        super.drawScreen(p_146552_1_, p_146552_2_, p_146552_3_);
        if (achievement2 != null) {
            String s = achievement2.getStatName().getUnformattedText();
            final String s2 = achievement2.getDescription();
            final int i7 = p_146552_1_ + 12;
            final int k8 = p_146552_2_ - 4;
            final int i8 = this.statFileWriter.func_150874_c(achievement2);
            if (this.statFileWriter.canUnlockAchievement(achievement2)) {
                final int j9 = Math.max(this.fontRendererObj.getStringWidth(s), 120);
                int i9 = this.fontRendererObj.splitStringWidth(s2, j9);
                if (this.statFileWriter.hasAchievementUnlocked(achievement2)) {
                    i9 += 12;
                }
                this.drawGradientRect(i7 - 3, k8 - 3, i7 + j9 + 3, k8 + i9 + 3 + 12, -1073741824, -1073741824);
                this.fontRendererObj.drawSplitString(s2, i7, k8 + 12, j9, -6250336);
                if (this.statFileWriter.hasAchievementUnlocked(achievement2)) {
                    this.fontRendererObj.drawStringWithShadow(I18n.format("achievement.taken", new Object[0]), (float)i7, (float)(k8 + i9 + 4), -7302913);
                }
            }
            else if (i8 == 3) {
                s = I18n.format("achievement.unknown", new Object[0]);
                final int k9 = Math.max(this.fontRendererObj.getStringWidth(s), 120);
                final String s3 = new ChatComponentTranslation("achievement.requires", new Object[] { achievement2.parentAchievement.getStatName() }).getUnformattedText();
                final int i10 = this.fontRendererObj.splitStringWidth(s3, k9);
                this.drawGradientRect(i7 - 3, k8 - 3, i7 + k9 + 3, k8 + i10 + 12 + 3, -1073741824, -1073741824);
                this.fontRendererObj.drawSplitString(s3, i7, k8 + 12, k9, -9416624);
            }
            else if (i8 < 3) {
                final int l9 = Math.max(this.fontRendererObj.getStringWidth(s), 120);
                final String s4 = new ChatComponentTranslation("achievement.requires", new Object[] { achievement2.parentAchievement.getStatName() }).getUnformattedText();
                final int j10 = this.fontRendererObj.splitStringWidth(s4, l9);
                this.drawGradientRect(i7 - 3, k8 - 3, i7 + l9 + 3, k8 + j10 + 12 + 3, -1073741824, -1073741824);
                this.fontRendererObj.drawSplitString(s4, i7, k8 + 12, l9, -9416624);
            }
            else {
                s = null;
            }
            if (s != null) {
                this.fontRendererObj.drawStringWithShadow(s, (float)i7, (float)k8, this.statFileWriter.canUnlockAchievement(achievement2) ? (achievement2.getSpecial() ? -128 : -1) : (achievement2.getSpecial() ? -8355776 : -8355712));
            }
        }
        GlStateManager.enableDepth();
        GlStateManager.enableLighting();
        RenderHelper.disableStandardItemLighting();
    }
    
    private TextureAtlasSprite func_175371_a(final Block p_175371_1_) {
        return Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getTexture(p_175371_1_.getDefaultState());
    }
    
    @Override
    public boolean doesGuiPauseGame() {
        return !this.loadingAchievements;
    }
}
