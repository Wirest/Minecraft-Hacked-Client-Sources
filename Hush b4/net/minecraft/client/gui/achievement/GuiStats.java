// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui.achievement;

import net.minecraft.entity.EntityList;
import java.util.Iterator;
import net.minecraft.stats.StatList;
import com.google.common.collect.Lists;
import java.util.Collections;
import net.minecraft.stats.StatBase;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import java.util.Comparator;
import net.minecraft.stats.StatCrafting;
import java.util.List;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.Item;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.stats.StatFileWriter;
import net.minecraft.client.gui.IProgressMeter;
import net.minecraft.client.gui.GuiScreen;

public class GuiStats extends GuiScreen implements IProgressMeter
{
    protected GuiScreen parentScreen;
    protected String screenTitle;
    private StatsGeneral generalStats;
    private StatsItem itemStats;
    private StatsBlock blockStats;
    private StatsMobsList mobStats;
    private StatFileWriter field_146546_t;
    private GuiSlot displaySlot;
    private boolean doesGuiPauseGame;
    
    public GuiStats(final GuiScreen p_i1071_1_, final StatFileWriter p_i1071_2_) {
        this.screenTitle = "Select world";
        this.doesGuiPauseGame = true;
        this.parentScreen = p_i1071_1_;
        this.field_146546_t = p_i1071_2_;
    }
    
    @Override
    public void initGui() {
        this.screenTitle = I18n.format("gui.stats", new Object[0]);
        this.doesGuiPauseGame = true;
        this.mc.getNetHandler().addToSendQueue(new C16PacketClientStatus(C16PacketClientStatus.EnumState.REQUEST_STATS));
    }
    
    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        if (this.displaySlot != null) {
            this.displaySlot.handleMouseInput();
        }
    }
    
    public void func_175366_f() {
        (this.generalStats = new StatsGeneral(this.mc)).registerScrollButtons(1, 1);
        (this.itemStats = new StatsItem(this.mc)).registerScrollButtons(1, 1);
        (this.blockStats = new StatsBlock(this.mc)).registerScrollButtons(1, 1);
        (this.mobStats = new StatsMobsList(this.mc)).registerScrollButtons(1, 1);
    }
    
    public void createButtons() {
        this.buttonList.add(new GuiButton(0, this.width / 2 + 4, this.height - 28, 150, 20, I18n.format("gui.done", new Object[0])));
        this.buttonList.add(new GuiButton(1, this.width / 2 - 160, this.height - 52, 80, 20, I18n.format("stat.generalButton", new Object[0])));
        final GuiButton guibutton;
        this.buttonList.add(guibutton = new GuiButton(2, this.width / 2 - 80, this.height - 52, 80, 20, I18n.format("stat.blocksButton", new Object[0])));
        final GuiButton guibutton2;
        this.buttonList.add(guibutton2 = new GuiButton(3, this.width / 2, this.height - 52, 80, 20, I18n.format("stat.itemsButton", new Object[0])));
        final GuiButton guibutton3;
        this.buttonList.add(guibutton3 = new GuiButton(4, this.width / 2 + 80, this.height - 52, 80, 20, I18n.format("stat.mobsButton", new Object[0])));
        if (this.blockStats.getSize() == 0) {
            guibutton.enabled = false;
        }
        if (this.itemStats.getSize() == 0) {
            guibutton2.enabled = false;
        }
        if (this.mobStats.getSize() == 0) {
            guibutton3.enabled = false;
        }
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.enabled) {
            if (button.id == 0) {
                this.mc.displayGuiScreen(this.parentScreen);
            }
            else if (button.id == 1) {
                this.displaySlot = this.generalStats;
            }
            else if (button.id == 3) {
                this.displaySlot = this.itemStats;
            }
            else if (button.id == 2) {
                this.displaySlot = this.blockStats;
            }
            else if (button.id == 4) {
                this.displaySlot = this.mobStats;
            }
            else {
                this.displaySlot.actionPerformed(button);
            }
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        if (this.doesGuiPauseGame) {
            this.drawDefaultBackground();
            this.drawCenteredString(this.fontRendererObj, I18n.format("multiplayer.downloadingStats", new Object[0]), this.width / 2, this.height / 2, 16777215);
            this.drawCenteredString(this.fontRendererObj, GuiStats.lanSearchStates[(int)(Minecraft.getSystemTime() / 150L % GuiStats.lanSearchStates.length)], this.width / 2, this.height / 2 + this.fontRendererObj.FONT_HEIGHT * 2, 16777215);
        }
        else {
            this.displaySlot.drawScreen(mouseX, mouseY, partialTicks);
            this.drawCenteredString(this.fontRendererObj, this.screenTitle, this.width / 2, 20, 16777215);
            super.drawScreen(mouseX, mouseY, partialTicks);
        }
    }
    
    @Override
    public void doneLoading() {
        if (this.doesGuiPauseGame) {
            this.func_175366_f();
            this.createButtons();
            this.displaySlot = this.generalStats;
            this.doesGuiPauseGame = false;
        }
    }
    
    @Override
    public boolean doesGuiPauseGame() {
        return !this.doesGuiPauseGame;
    }
    
    private void drawStatsScreen(final int p_146521_1_, final int p_146521_2_, final Item p_146521_3_) {
        this.drawButtonBackground(p_146521_1_ + 1, p_146521_2_ + 1);
        GlStateManager.enableRescaleNormal();
        RenderHelper.enableGUIStandardItemLighting();
        this.itemRender.renderItemIntoGUI(new ItemStack(p_146521_3_, 1, 0), p_146521_1_ + 2, p_146521_2_ + 2);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
    }
    
    private void drawButtonBackground(final int p_146531_1_, final int p_146531_2_) {
        this.drawSprite(p_146531_1_, p_146531_2_, 0, 0);
    }
    
    private void drawSprite(final int p_146527_1_, final int p_146527_2_, final int p_146527_3_, final int p_146527_4_) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(GuiStats.statIcons);
        final float f = 0.0078125f;
        final float f2 = 0.0078125f;
        final int i = 18;
        final int j = 18;
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(p_146527_1_ + 0, p_146527_2_ + 18, this.zLevel).tex((p_146527_3_ + 0) * 0.0078125f, (p_146527_4_ + 18) * 0.0078125f).endVertex();
        worldrenderer.pos(p_146527_1_ + 18, p_146527_2_ + 18, this.zLevel).tex((p_146527_3_ + 18) * 0.0078125f, (p_146527_4_ + 18) * 0.0078125f).endVertex();
        worldrenderer.pos(p_146527_1_ + 18, p_146527_2_ + 0, this.zLevel).tex((p_146527_3_ + 18) * 0.0078125f, (p_146527_4_ + 0) * 0.0078125f).endVertex();
        worldrenderer.pos(p_146527_1_ + 0, p_146527_2_ + 0, this.zLevel).tex((p_146527_3_ + 0) * 0.0078125f, (p_146527_4_ + 0) * 0.0078125f).endVertex();
        tessellator.draw();
    }
    
    abstract class Stats extends GuiSlot
    {
        protected int field_148218_l;
        protected List<StatCrafting> statsHolder;
        protected Comparator<StatCrafting> statSorter;
        protected int field_148217_o;
        protected int field_148215_p;
        
        protected Stats(final Minecraft mcIn) {
            super(mcIn, GuiStats.this.width, GuiStats.this.height, 32, GuiStats.this.height - 64, 20);
            this.field_148218_l = -1;
            this.field_148217_o = -1;
            this.setShowSelectionBox(false);
            this.setHasListHeader(true, 20);
        }
        
        @Override
        protected void elementClicked(final int slotIndex, final boolean isDoubleClick, final int mouseX, final int mouseY) {
        }
        
        @Override
        protected boolean isSelected(final int slotIndex) {
            return false;
        }
        
        @Override
        protected void drawBackground() {
            GuiStats.this.drawDefaultBackground();
        }
        
        @Override
        protected void drawListHeader(final int p_148129_1_, final int p_148129_2_, final Tessellator p_148129_3_) {
            if (!Mouse.isButtonDown(0)) {
                this.field_148218_l = -1;
            }
            if (this.field_148218_l == 0) {
                GuiStats.this.drawSprite(p_148129_1_ + 115 - 18, p_148129_2_ + 1, 0, 0);
            }
            else {
                GuiStats.this.drawSprite(p_148129_1_ + 115 - 18, p_148129_2_ + 1, 0, 18);
            }
            if (this.field_148218_l == 1) {
                GuiStats.this.drawSprite(p_148129_1_ + 165 - 18, p_148129_2_ + 1, 0, 0);
            }
            else {
                GuiStats.this.drawSprite(p_148129_1_ + 165 - 18, p_148129_2_ + 1, 0, 18);
            }
            if (this.field_148218_l == 2) {
                GuiStats.this.drawSprite(p_148129_1_ + 215 - 18, p_148129_2_ + 1, 0, 0);
            }
            else {
                GuiStats.this.drawSprite(p_148129_1_ + 215 - 18, p_148129_2_ + 1, 0, 18);
            }
            if (this.field_148217_o != -1) {
                int i = 79;
                int j = 18;
                if (this.field_148217_o == 1) {
                    i = 129;
                }
                else if (this.field_148217_o == 2) {
                    i = 179;
                }
                if (this.field_148215_p == 1) {
                    j = 36;
                }
                GuiStats.this.drawSprite(p_148129_1_ + i, p_148129_2_ + 1, j, 0);
            }
        }
        
        @Override
        protected void func_148132_a(final int p_148132_1_, final int p_148132_2_) {
            this.field_148218_l = -1;
            if (p_148132_1_ >= 79 && p_148132_1_ < 115) {
                this.field_148218_l = 0;
            }
            else if (p_148132_1_ >= 129 && p_148132_1_ < 165) {
                this.field_148218_l = 1;
            }
            else if (p_148132_1_ >= 179 && p_148132_1_ < 215) {
                this.field_148218_l = 2;
            }
            if (this.field_148218_l >= 0) {
                this.func_148212_h(this.field_148218_l);
                this.mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0f));
            }
        }
        
        @Override
        protected final int getSize() {
            return this.statsHolder.size();
        }
        
        protected final StatCrafting func_148211_c(final int p_148211_1_) {
            return this.statsHolder.get(p_148211_1_);
        }
        
        protected abstract String func_148210_b(final int p0);
        
        protected void func_148209_a(final StatBase p_148209_1_, final int p_148209_2_, final int p_148209_3_, final boolean p_148209_4_) {
            if (p_148209_1_ != null) {
                final String s = p_148209_1_.format(GuiStats.this.field_146546_t.readStat(p_148209_1_));
                GuiStats.this.drawString(GuiStats.this.fontRendererObj, s, p_148209_2_ - GuiStats.this.fontRendererObj.getStringWidth(s), p_148209_3_ + 5, p_148209_4_ ? 16777215 : 9474192);
            }
            else {
                final String s2 = "-";
                GuiStats.this.drawString(GuiStats.this.fontRendererObj, s2, p_148209_2_ - GuiStats.this.fontRendererObj.getStringWidth(s2), p_148209_3_ + 5, p_148209_4_ ? 16777215 : 9474192);
            }
        }
        
        @Override
        protected void func_148142_b(final int p_148142_1_, final int p_148142_2_) {
            if (p_148142_2_ >= this.top && p_148142_2_ <= this.bottom) {
                final int i = this.getSlotIndexFromScreenCoords(p_148142_1_, p_148142_2_);
                final int j = this.width / 2 - 92 - 16;
                if (i >= 0) {
                    if (p_148142_1_ < j + 40 || p_148142_1_ > j + 40 + 20) {
                        return;
                    }
                    final StatCrafting statcrafting = this.func_148211_c(i);
                    this.func_148213_a(statcrafting, p_148142_1_, p_148142_2_);
                }
                else {
                    String s = "";
                    if (p_148142_1_ >= j + 115 - 18 && p_148142_1_ <= j + 115) {
                        s = this.func_148210_b(0);
                    }
                    else if (p_148142_1_ >= j + 165 - 18 && p_148142_1_ <= j + 165) {
                        s = this.func_148210_b(1);
                    }
                    else {
                        if (p_148142_1_ < j + 215 - 18 || p_148142_1_ > j + 215) {
                            return;
                        }
                        s = this.func_148210_b(2);
                    }
                    s = new StringBuilder().append(I18n.format(s, new Object[0])).toString().trim();
                    if (s.length() > 0) {
                        final int k = p_148142_1_ + 12;
                        final int l = p_148142_2_ - 12;
                        final int i2 = GuiStats.this.fontRendererObj.getStringWidth(s);
                        Gui.this.drawGradientRect(k - 3, l - 3, k + i2 + 3, l + 8 + 3, -1073741824, -1073741824);
                        GuiStats.this.fontRendererObj.drawStringWithShadow(s, (float)k, (float)l, -1);
                    }
                }
            }
        }
        
        protected void func_148213_a(final StatCrafting p_148213_1_, final int p_148213_2_, final int p_148213_3_) {
            if (p_148213_1_ != null) {
                final Item item = p_148213_1_.func_150959_a();
                final ItemStack itemstack = new ItemStack(item);
                final String s = itemstack.getUnlocalizedName();
                final String s2 = new StringBuilder().append(I18n.format(String.valueOf(s) + ".name", new Object[0])).toString().trim();
                if (s2.length() > 0) {
                    final int i = p_148213_2_ + 12;
                    final int j = p_148213_3_ - 12;
                    final int k = GuiStats.this.fontRendererObj.getStringWidth(s2);
                    Gui.this.drawGradientRect(i - 3, j - 3, i + k + 3, j + 8 + 3, -1073741824, -1073741824);
                    GuiStats.this.fontRendererObj.drawStringWithShadow(s2, (float)i, (float)j, -1);
                }
            }
        }
        
        protected void func_148212_h(final int p_148212_1_) {
            if (p_148212_1_ != this.field_148217_o) {
                this.field_148217_o = p_148212_1_;
                this.field_148215_p = -1;
            }
            else if (this.field_148215_p == -1) {
                this.field_148215_p = 1;
            }
            else {
                this.field_148217_o = -1;
                this.field_148215_p = 0;
            }
            Collections.sort(this.statsHolder, this.statSorter);
        }
    }
    
    class StatsBlock extends Stats
    {
        public StatsBlock(final Minecraft mcIn) {
            super(mcIn);
            this.statsHolder = (List<StatCrafting>)Lists.newArrayList();
            for (final StatCrafting statcrafting : StatList.objectMineStats) {
                boolean flag = false;
                final int i = Item.getIdFromItem(statcrafting.func_150959_a());
                if (GuiStats.this.field_146546_t.readStat(statcrafting) > 0) {
                    flag = true;
                }
                else if (StatList.objectUseStats[i] != null && GuiStats.this.field_146546_t.readStat(StatList.objectUseStats[i]) > 0) {
                    flag = true;
                }
                else if (StatList.objectCraftStats[i] != null && GuiStats.this.field_146546_t.readStat(StatList.objectCraftStats[i]) > 0) {
                    flag = true;
                }
                if (flag) {
                    this.statsHolder.add(statcrafting);
                }
            }
            this.statSorter = new Comparator<StatCrafting>() {
                @Override
                public int compare(final StatCrafting p_compare_1_, final StatCrafting p_compare_2_) {
                    final int j = Item.getIdFromItem(p_compare_1_.func_150959_a());
                    final int k = Item.getIdFromItem(p_compare_2_.func_150959_a());
                    StatBase statbase = null;
                    StatBase statbase2 = null;
                    if (StatsBlock.this.field_148217_o == 2) {
                        statbase = StatList.mineBlockStatArray[j];
                        statbase2 = StatList.mineBlockStatArray[k];
                    }
                    else if (StatsBlock.this.field_148217_o == 0) {
                        statbase = StatList.objectCraftStats[j];
                        statbase2 = StatList.objectCraftStats[k];
                    }
                    else if (StatsBlock.this.field_148217_o == 1) {
                        statbase = StatList.objectUseStats[j];
                        statbase2 = StatList.objectUseStats[k];
                    }
                    if (statbase != null || statbase2 != null) {
                        if (statbase == null) {
                            return 1;
                        }
                        if (statbase2 == null) {
                            return -1;
                        }
                        final int l = GuiStats.this.field_146546_t.readStat(statbase);
                        final int i1 = GuiStats.this.field_146546_t.readStat(statbase2);
                        if (l != i1) {
                            return (l - i1) * StatsBlock.this.field_148215_p;
                        }
                    }
                    return j - k;
                }
            };
        }
        
        @Override
        protected void drawListHeader(final int p_148129_1_, final int p_148129_2_, final Tessellator p_148129_3_) {
            super.drawListHeader(p_148129_1_, p_148129_2_, p_148129_3_);
            if (this.field_148218_l == 0) {
                GuiStats.this.drawSprite(p_148129_1_ + 115 - 18 + 1, p_148129_2_ + 1 + 1, 18, 18);
            }
            else {
                GuiStats.this.drawSprite(p_148129_1_ + 115 - 18, p_148129_2_ + 1, 18, 18);
            }
            if (this.field_148218_l == 1) {
                GuiStats.this.drawSprite(p_148129_1_ + 165 - 18 + 1, p_148129_2_ + 1 + 1, 36, 18);
            }
            else {
                GuiStats.this.drawSprite(p_148129_1_ + 165 - 18, p_148129_2_ + 1, 36, 18);
            }
            if (this.field_148218_l == 2) {
                GuiStats.this.drawSprite(p_148129_1_ + 215 - 18 + 1, p_148129_2_ + 1 + 1, 54, 18);
            }
            else {
                GuiStats.this.drawSprite(p_148129_1_ + 215 - 18, p_148129_2_ + 1, 54, 18);
            }
        }
        
        @Override
        protected void drawSlot(final int entryID, final int p_180791_2_, final int p_180791_3_, final int p_180791_4_, final int mouseXIn, final int mouseYIn) {
            final StatCrafting statcrafting = this.func_148211_c(entryID);
            final Item item = statcrafting.func_150959_a();
            GuiStats.this.drawStatsScreen(p_180791_2_ + 40, p_180791_3_, item);
            final int i = Item.getIdFromItem(item);
            this.func_148209_a(StatList.objectCraftStats[i], p_180791_2_ + 115, p_180791_3_, entryID % 2 == 0);
            this.func_148209_a(StatList.objectUseStats[i], p_180791_2_ + 165, p_180791_3_, entryID % 2 == 0);
            this.func_148209_a(statcrafting, p_180791_2_ + 215, p_180791_3_, entryID % 2 == 0);
        }
        
        @Override
        protected String func_148210_b(final int p_148210_1_) {
            return (p_148210_1_ == 0) ? "stat.crafted" : ((p_148210_1_ == 1) ? "stat.used" : "stat.mined");
        }
    }
    
    class StatsGeneral extends GuiSlot
    {
        public StatsGeneral(final Minecraft mcIn) {
            super(mcIn, GuiStats.this.width, GuiStats.this.height, 32, GuiStats.this.height - 64, 10);
            this.setShowSelectionBox(false);
        }
        
        @Override
        protected int getSize() {
            return StatList.generalStats.size();
        }
        
        @Override
        protected void elementClicked(final int slotIndex, final boolean isDoubleClick, final int mouseX, final int mouseY) {
        }
        
        @Override
        protected boolean isSelected(final int slotIndex) {
            return false;
        }
        
        @Override
        protected int getContentHeight() {
            return this.getSize() * 10;
        }
        
        @Override
        protected void drawBackground() {
            GuiStats.this.drawDefaultBackground();
        }
        
        @Override
        protected void drawSlot(final int entryID, final int p_180791_2_, final int p_180791_3_, final int p_180791_4_, final int mouseXIn, final int mouseYIn) {
            final StatBase statbase = StatList.generalStats.get(entryID);
            GuiStats.this.drawString(GuiStats.this.fontRendererObj, statbase.getStatName().getUnformattedText(), p_180791_2_ + 2, p_180791_3_ + 1, (entryID % 2 == 0) ? 16777215 : 9474192);
            final String s = statbase.format(GuiStats.this.field_146546_t.readStat(statbase));
            GuiStats.this.drawString(GuiStats.this.fontRendererObj, s, p_180791_2_ + 2 + 213 - GuiStats.this.fontRendererObj.getStringWidth(s), p_180791_3_ + 1, (entryID % 2 == 0) ? 16777215 : 9474192);
        }
    }
    
    class StatsItem extends Stats
    {
        public StatsItem(final Minecraft mcIn) {
            super(mcIn);
            this.statsHolder = (List<StatCrafting>)Lists.newArrayList();
            for (final StatCrafting statcrafting : StatList.itemStats) {
                boolean flag = false;
                final int i = Item.getIdFromItem(statcrafting.func_150959_a());
                if (GuiStats.this.field_146546_t.readStat(statcrafting) > 0) {
                    flag = true;
                }
                else if (StatList.objectBreakStats[i] != null && GuiStats.this.field_146546_t.readStat(StatList.objectBreakStats[i]) > 0) {
                    flag = true;
                }
                else if (StatList.objectCraftStats[i] != null && GuiStats.this.field_146546_t.readStat(StatList.objectCraftStats[i]) > 0) {
                    flag = true;
                }
                if (flag) {
                    this.statsHolder.add(statcrafting);
                }
            }
            this.statSorter = new Comparator<StatCrafting>() {
                @Override
                public int compare(final StatCrafting p_compare_1_, final StatCrafting p_compare_2_) {
                    final int j = Item.getIdFromItem(p_compare_1_.func_150959_a());
                    final int k = Item.getIdFromItem(p_compare_2_.func_150959_a());
                    StatBase statbase = null;
                    StatBase statbase2 = null;
                    if (StatsItem.this.field_148217_o == 0) {
                        statbase = StatList.objectBreakStats[j];
                        statbase2 = StatList.objectBreakStats[k];
                    }
                    else if (StatsItem.this.field_148217_o == 1) {
                        statbase = StatList.objectCraftStats[j];
                        statbase2 = StatList.objectCraftStats[k];
                    }
                    else if (StatsItem.this.field_148217_o == 2) {
                        statbase = StatList.objectUseStats[j];
                        statbase2 = StatList.objectUseStats[k];
                    }
                    if (statbase != null || statbase2 != null) {
                        if (statbase == null) {
                            return 1;
                        }
                        if (statbase2 == null) {
                            return -1;
                        }
                        final int l = GuiStats.this.field_146546_t.readStat(statbase);
                        final int i1 = GuiStats.this.field_146546_t.readStat(statbase2);
                        if (l != i1) {
                            return (l - i1) * StatsItem.this.field_148215_p;
                        }
                    }
                    return j - k;
                }
            };
        }
        
        @Override
        protected void drawListHeader(final int p_148129_1_, final int p_148129_2_, final Tessellator p_148129_3_) {
            super.drawListHeader(p_148129_1_, p_148129_2_, p_148129_3_);
            if (this.field_148218_l == 0) {
                GuiStats.this.drawSprite(p_148129_1_ + 115 - 18 + 1, p_148129_2_ + 1 + 1, 72, 18);
            }
            else {
                GuiStats.this.drawSprite(p_148129_1_ + 115 - 18, p_148129_2_ + 1, 72, 18);
            }
            if (this.field_148218_l == 1) {
                GuiStats.this.drawSprite(p_148129_1_ + 165 - 18 + 1, p_148129_2_ + 1 + 1, 18, 18);
            }
            else {
                GuiStats.this.drawSprite(p_148129_1_ + 165 - 18, p_148129_2_ + 1, 18, 18);
            }
            if (this.field_148218_l == 2) {
                GuiStats.this.drawSprite(p_148129_1_ + 215 - 18 + 1, p_148129_2_ + 1 + 1, 36, 18);
            }
            else {
                GuiStats.this.drawSprite(p_148129_1_ + 215 - 18, p_148129_2_ + 1, 36, 18);
            }
        }
        
        @Override
        protected void drawSlot(final int entryID, final int p_180791_2_, final int p_180791_3_, final int p_180791_4_, final int mouseXIn, final int mouseYIn) {
            final StatCrafting statcrafting = this.func_148211_c(entryID);
            final Item item = statcrafting.func_150959_a();
            GuiStats.this.drawStatsScreen(p_180791_2_ + 40, p_180791_3_, item);
            final int i = Item.getIdFromItem(item);
            this.func_148209_a(StatList.objectBreakStats[i], p_180791_2_ + 115, p_180791_3_, entryID % 2 == 0);
            this.func_148209_a(StatList.objectCraftStats[i], p_180791_2_ + 165, p_180791_3_, entryID % 2 == 0);
            this.func_148209_a(statcrafting, p_180791_2_ + 215, p_180791_3_, entryID % 2 == 0);
        }
        
        @Override
        protected String func_148210_b(final int p_148210_1_) {
            return (p_148210_1_ == 1) ? "stat.crafted" : ((p_148210_1_ == 2) ? "stat.used" : "stat.depleted");
        }
    }
    
    class StatsMobsList extends GuiSlot
    {
        private final List<EntityList.EntityEggInfo> field_148222_l;
        
        public StatsMobsList(final Minecraft mcIn) {
            super(mcIn, GuiStats.this.width, GuiStats.this.height, 32, GuiStats.this.height - 64, GuiStats.this.fontRendererObj.FONT_HEIGHT * 4);
            this.field_148222_l = (List<EntityList.EntityEggInfo>)Lists.newArrayList();
            this.setShowSelectionBox(false);
            for (final EntityList.EntityEggInfo entitylist$entityegginfo : EntityList.entityEggs.values()) {
                if (GuiStats.this.field_146546_t.readStat(entitylist$entityegginfo.field_151512_d) > 0 || GuiStats.this.field_146546_t.readStat(entitylist$entityegginfo.field_151513_e) > 0) {
                    this.field_148222_l.add(entitylist$entityegginfo);
                }
            }
        }
        
        @Override
        protected int getSize() {
            return this.field_148222_l.size();
        }
        
        @Override
        protected void elementClicked(final int slotIndex, final boolean isDoubleClick, final int mouseX, final int mouseY) {
        }
        
        @Override
        protected boolean isSelected(final int slotIndex) {
            return false;
        }
        
        @Override
        protected int getContentHeight() {
            return this.getSize() * GuiStats.this.fontRendererObj.FONT_HEIGHT * 4;
        }
        
        @Override
        protected void drawBackground() {
            GuiStats.this.drawDefaultBackground();
        }
        
        @Override
        protected void drawSlot(final int entryID, final int p_180791_2_, final int p_180791_3_, final int p_180791_4_, final int mouseXIn, final int mouseYIn) {
            final EntityList.EntityEggInfo entitylist$entityegginfo = this.field_148222_l.get(entryID);
            final String s = I18n.format("entity." + EntityList.getStringFromID(entitylist$entityegginfo.spawnedID) + ".name", new Object[0]);
            final int i = GuiStats.this.field_146546_t.readStat(entitylist$entityegginfo.field_151512_d);
            final int j = GuiStats.this.field_146546_t.readStat(entitylist$entityegginfo.field_151513_e);
            String s2 = I18n.format("stat.entityKills", i, s);
            String s3 = I18n.format("stat.entityKilledBy", s, j);
            if (i == 0) {
                s2 = I18n.format("stat.entityKills.none", s);
            }
            if (j == 0) {
                s3 = I18n.format("stat.entityKilledBy.none", s);
            }
            GuiStats.this.drawString(GuiStats.this.fontRendererObj, s, p_180791_2_ + 2 - 10, p_180791_3_ + 1, 16777215);
            GuiStats.this.drawString(GuiStats.this.fontRendererObj, s2, p_180791_2_ + 2, p_180791_3_ + 1 + GuiStats.this.fontRendererObj.FONT_HEIGHT, (i == 0) ? 6316128 : 9474192);
            GuiStats.this.drawString(GuiStats.this.fontRendererObj, s3, p_180791_2_ + 2, p_180791_3_ + 1 + GuiStats.this.fontRendererObj.FONT_HEIGHT * 2, (j == 0) ? 6316128 : 9474192);
        }
    }
}
