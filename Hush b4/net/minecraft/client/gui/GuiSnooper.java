// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import net.minecraft.client.resources.I18n;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.client.settings.GameSettings;

public class GuiSnooper extends GuiScreen
{
    private final GuiScreen field_146608_a;
    private final GameSettings game_settings_2;
    private final java.util.List<String> field_146604_g;
    private final java.util.List<String> field_146609_h;
    private String field_146610_i;
    private String[] field_146607_r;
    private List field_146606_s;
    private GuiButton field_146605_t;
    
    public GuiSnooper(final GuiScreen p_i1061_1_, final GameSettings p_i1061_2_) {
        this.field_146604_g = (java.util.List<String>)Lists.newArrayList();
        this.field_146609_h = (java.util.List<String>)Lists.newArrayList();
        this.field_146608_a = p_i1061_1_;
        this.game_settings_2 = p_i1061_2_;
    }
    
    @Override
    public void initGui() {
        this.field_146610_i = I18n.format("options.snooper.title", new Object[0]);
        final String s = I18n.format("options.snooper.desc", new Object[0]);
        final java.util.List<String> list = (java.util.List<String>)Lists.newArrayList();
        for (final Object s2 : this.fontRendererObj.listFormattedStringToWidth(s, this.width - 30)) {
            list.add((String)s2);
        }
        this.field_146607_r = list.toArray(new String[list.size()]);
        this.field_146604_g.clear();
        this.field_146609_h.clear();
        this.buttonList.add(this.field_146605_t = new GuiButton(1, this.width / 2 - 152, this.height - 30, 150, 20, this.game_settings_2.getKeyBinding(GameSettings.Options.SNOOPER_ENABLED)));
        this.buttonList.add(new GuiButton(2, this.width / 2 + 2, this.height - 30, 150, 20, I18n.format("gui.done", new Object[0])));
        final boolean flag = this.mc.getIntegratedServer() != null && this.mc.getIntegratedServer().getPlayerUsageSnooper() != null;
        for (final Map.Entry<String, String> entry : new TreeMap<String, String>(this.mc.getPlayerUsageSnooper().getCurrentStats()).entrySet()) {
            this.field_146604_g.add(String.valueOf(flag ? "C " : "") + entry.getKey());
            this.field_146609_h.add(this.fontRendererObj.trimStringToWidth(entry.getValue(), this.width - 220));
        }
        if (flag) {
            for (final Map.Entry<String, String> entry2 : new TreeMap<String, String>(this.mc.getIntegratedServer().getPlayerUsageSnooper().getCurrentStats()).entrySet()) {
                this.field_146604_g.add("S " + entry2.getKey());
                this.field_146609_h.add(this.fontRendererObj.trimStringToWidth(entry2.getValue(), this.width - 220));
            }
        }
        this.field_146606_s = new List();
    }
    
    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.field_146606_s.handleMouseInput();
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.enabled) {
            if (button.id == 2) {
                this.game_settings_2.saveOptions();
                this.game_settings_2.saveOptions();
                this.mc.displayGuiScreen(this.field_146608_a);
            }
            if (button.id == 1) {
                this.game_settings_2.setOptionValue(GameSettings.Options.SNOOPER_ENABLED, 1);
                this.field_146605_t.displayString = this.game_settings_2.getKeyBinding(GameSettings.Options.SNOOPER_ENABLED);
            }
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        this.field_146606_s.drawScreen(mouseX, mouseY, partialTicks);
        this.drawCenteredString(this.fontRendererObj, this.field_146610_i, this.width / 2, 8, 16777215);
        int i = 22;
        String[] field_146607_r;
        for (int length = (field_146607_r = this.field_146607_r).length, j = 0; j < length; ++j) {
            final String s = field_146607_r[j];
            this.drawCenteredString(this.fontRendererObj, s, this.width / 2, i, 8421504);
            i += this.fontRendererObj.FONT_HEIGHT;
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    class List extends GuiSlot
    {
        public List() {
            super(GuiSnooper.this.mc, GuiSnooper.this.width, GuiSnooper.this.height, 80, GuiSnooper.this.height - 40, GuiSnooper.this.fontRendererObj.FONT_HEIGHT + 1);
        }
        
        @Override
        protected int getSize() {
            return GuiSnooper.this.field_146604_g.size();
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
        }
        
        @Override
        protected void drawSlot(final int entryID, final int p_180791_2_, final int p_180791_3_, final int p_180791_4_, final int mouseXIn, final int mouseYIn) {
            GuiSnooper.this.fontRendererObj.drawString(GuiSnooper.this.field_146604_g.get(entryID), 10.0, p_180791_3_, 16777215);
            GuiSnooper.this.fontRendererObj.drawString(GuiSnooper.this.field_146609_h.get(entryID), 230.0, p_180791_3_, 16777215);
        }
        
        @Override
        protected int getScrollBarX() {
            return this.width - 10;
        }
    }
}
