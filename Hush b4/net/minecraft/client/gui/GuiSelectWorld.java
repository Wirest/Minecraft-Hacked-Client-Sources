// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import net.minecraft.util.EnumChatFormatting;
import java.util.Date;
import net.minecraft.client.Minecraft;
import net.minecraft.world.storage.WorldInfo;
import net.minecraft.world.storage.ISaveHandler;
import org.apache.commons.lang3.StringUtils;
import net.minecraft.world.storage.ISaveFormat;
import java.util.Collections;
import java.io.IOException;
import net.minecraft.world.WorldSettings;
import net.minecraft.client.AnvilConverterException;
import net.minecraft.client.resources.I18n;
import java.text.SimpleDateFormat;
import org.apache.logging.log4j.LogManager;
import net.minecraft.world.storage.SaveFormatComparator;
import java.util.List;
import java.text.DateFormat;
import org.apache.logging.log4j.Logger;

public class GuiSelectWorld extends GuiScreen implements GuiYesNoCallback
{
    private static final Logger logger;
    private final DateFormat field_146633_h;
    protected GuiScreen parentScreen;
    protected String field_146628_f;
    private boolean field_146634_i;
    private int field_146640_r;
    private java.util.List<SaveFormatComparator> field_146639_s;
    private List field_146638_t;
    private String field_146637_u;
    private String field_146636_v;
    private String[] field_146635_w;
    private boolean field_146643_x;
    private GuiButton deleteButton;
    private GuiButton selectButton;
    private GuiButton renameButton;
    private GuiButton recreateButton;
    
    static {
        logger = LogManager.getLogger();
    }
    
    public GuiSelectWorld(final GuiScreen parentScreenIn) {
        this.field_146633_h = new SimpleDateFormat();
        this.field_146628_f = "Select world";
        this.field_146635_w = new String[4];
        this.parentScreen = parentScreenIn;
    }
    
    @Override
    public void initGui() {
        this.field_146628_f = I18n.format("selectWorld.title", new Object[0]);
        try {
            this.func_146627_h();
        }
        catch (AnvilConverterException anvilconverterexception) {
            GuiSelectWorld.logger.error("Couldn't load level list", anvilconverterexception);
            this.mc.displayGuiScreen(new GuiErrorScreen("Unable to load worlds", anvilconverterexception.getMessage()));
            return;
        }
        this.field_146637_u = I18n.format("selectWorld.world", new Object[0]);
        this.field_146636_v = I18n.format("selectWorld.conversion", new Object[0]);
        this.field_146635_w[WorldSettings.GameType.SURVIVAL.getID()] = I18n.format("gameMode.survival", new Object[0]);
        this.field_146635_w[WorldSettings.GameType.CREATIVE.getID()] = I18n.format("gameMode.creative", new Object[0]);
        this.field_146635_w[WorldSettings.GameType.ADVENTURE.getID()] = I18n.format("gameMode.adventure", new Object[0]);
        this.field_146635_w[WorldSettings.GameType.SPECTATOR.getID()] = I18n.format("gameMode.spectator", new Object[0]);
        (this.field_146638_t = new List(this.mc)).registerScrollButtons(4, 5);
        this.func_146618_g();
    }
    
    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.field_146638_t.handleMouseInput();
    }
    
    private void func_146627_h() throws AnvilConverterException {
        final ISaveFormat isaveformat = this.mc.getSaveLoader();
        Collections.sort(this.field_146639_s = isaveformat.getSaveList());
        this.field_146640_r = -1;
    }
    
    protected String func_146621_a(final int p_146621_1_) {
        return this.field_146639_s.get(p_146621_1_).getFileName();
    }
    
    protected String func_146614_d(final int p_146614_1_) {
        String s = this.field_146639_s.get(p_146614_1_).getDisplayName();
        if (StringUtils.isEmpty(s)) {
            s = String.valueOf(I18n.format("selectWorld.world", new Object[0])) + " " + (p_146614_1_ + 1);
        }
        return s;
    }
    
    public void func_146618_g() {
        this.buttonList.add(this.selectButton = new GuiButton(1, this.width / 2 - 154, this.height - 52, 150, 20, I18n.format("selectWorld.select", new Object[0])));
        this.buttonList.add(new GuiButton(3, this.width / 2 + 4, this.height - 52, 150, 20, I18n.format("selectWorld.create", new Object[0])));
        this.buttonList.add(this.renameButton = new GuiButton(6, this.width / 2 - 154, this.height - 28, 72, 20, I18n.format("selectWorld.rename", new Object[0])));
        this.buttonList.add(this.deleteButton = new GuiButton(2, this.width / 2 - 76, this.height - 28, 72, 20, I18n.format("selectWorld.delete", new Object[0])));
        this.buttonList.add(this.recreateButton = new GuiButton(7, this.width / 2 + 4, this.height - 28, 72, 20, I18n.format("selectWorld.recreate", new Object[0])));
        this.buttonList.add(new GuiButton(0, this.width / 2 + 82, this.height - 28, 72, 20, I18n.format("gui.cancel", new Object[0])));
        this.selectButton.enabled = false;
        this.deleteButton.enabled = false;
        this.renameButton.enabled = false;
        this.recreateButton.enabled = false;
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.enabled) {
            if (button.id == 2) {
                final String s = this.func_146614_d(this.field_146640_r);
                if (s != null) {
                    this.field_146643_x = true;
                    final GuiYesNo guiyesno = func_152129_a(this, s, this.field_146640_r);
                    this.mc.displayGuiScreen(guiyesno);
                }
            }
            else if (button.id == 1) {
                this.func_146615_e(this.field_146640_r);
            }
            else if (button.id == 3) {
                this.mc.displayGuiScreen(new GuiCreateWorld(this));
            }
            else if (button.id == 6) {
                this.mc.displayGuiScreen(new GuiRenameWorld(this, this.func_146621_a(this.field_146640_r)));
            }
            else if (button.id == 0) {
                this.mc.displayGuiScreen(this.parentScreen);
            }
            else if (button.id == 7) {
                final GuiCreateWorld guicreateworld = new GuiCreateWorld(this);
                final ISaveHandler isavehandler = this.mc.getSaveLoader().getSaveLoader(this.func_146621_a(this.field_146640_r), false);
                final WorldInfo worldinfo = isavehandler.loadWorldInfo();
                isavehandler.flush();
                guicreateworld.func_146318_a(worldinfo);
                this.mc.displayGuiScreen(guicreateworld);
            }
            else {
                this.field_146638_t.actionPerformed(button);
            }
        }
    }
    
    public void func_146615_e(final int p_146615_1_) {
        this.mc.displayGuiScreen(null);
        if (!this.field_146634_i) {
            this.field_146634_i = true;
            String s = this.func_146621_a(p_146615_1_);
            if (s == null) {
                s = "World" + p_146615_1_;
            }
            String s2 = this.func_146614_d(p_146615_1_);
            if (s2 == null) {
                s2 = "World" + p_146615_1_;
            }
            if (this.mc.getSaveLoader().canLoadWorld(s)) {
                this.mc.launchIntegratedServer(s, s2, null);
            }
        }
    }
    
    @Override
    public void confirmClicked(final boolean result, final int id) {
        if (this.field_146643_x) {
            this.field_146643_x = false;
            if (result) {
                final ISaveFormat isaveformat = this.mc.getSaveLoader();
                isaveformat.flushCache();
                isaveformat.deleteWorldDirectory(this.func_146621_a(id));
                try {
                    this.func_146627_h();
                }
                catch (AnvilConverterException anvilconverterexception) {
                    GuiSelectWorld.logger.error("Couldn't load level list", anvilconverterexception);
                }
            }
            this.mc.displayGuiScreen(this);
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.field_146638_t.drawScreen(mouseX, mouseY, partialTicks);
        this.drawCenteredString(this.fontRendererObj, this.field_146628_f, this.width / 2, 20, 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    public static GuiYesNo func_152129_a(final GuiYesNoCallback p_152129_0_, final String p_152129_1_, final int p_152129_2_) {
        final String s = I18n.format("selectWorld.deleteQuestion", new Object[0]);
        final String s2 = "'" + p_152129_1_ + "' " + I18n.format("selectWorld.deleteWarning", new Object[0]);
        final String s3 = I18n.format("selectWorld.deleteButton", new Object[0]);
        final String s4 = I18n.format("gui.cancel", new Object[0]);
        final GuiYesNo guiyesno = new GuiYesNo(p_152129_0_, s, s2, s3, s4, p_152129_2_);
        return guiyesno;
    }
    
    static /* synthetic */ void access$1(final GuiSelectWorld guiSelectWorld, final int field_146640_r) {
        guiSelectWorld.field_146640_r = field_146640_r;
    }
    
    class List extends GuiSlot
    {
        public List(final Minecraft mcIn) {
            super(mcIn, GuiSelectWorld.this.width, GuiSelectWorld.this.height, 32, GuiSelectWorld.this.height - 64, 36);
        }
        
        @Override
        protected int getSize() {
            return GuiSelectWorld.this.field_146639_s.size();
        }
        
        @Override
        protected void elementClicked(final int slotIndex, final boolean isDoubleClick, final int mouseX, final int mouseY) {
            GuiSelectWorld.access$1(GuiSelectWorld.this, slotIndex);
            final boolean flag = GuiSelectWorld.this.field_146640_r >= 0 && GuiSelectWorld.this.field_146640_r < this.getSize();
            GuiSelectWorld.this.selectButton.enabled = flag;
            GuiSelectWorld.this.deleteButton.enabled = flag;
            GuiSelectWorld.this.renameButton.enabled = flag;
            GuiSelectWorld.this.recreateButton.enabled = flag;
            if (isDoubleClick && flag) {
                GuiSelectWorld.this.func_146615_e(slotIndex);
            }
        }
        
        @Override
        protected boolean isSelected(final int slotIndex) {
            return slotIndex == GuiSelectWorld.this.field_146640_r;
        }
        
        @Override
        protected int getContentHeight() {
            return GuiSelectWorld.this.field_146639_s.size() * 36;
        }
        
        @Override
        protected void drawBackground() {
            GuiSelectWorld.this.drawDefaultBackground();
        }
        
        @Override
        protected void drawSlot(final int entryID, final int p_180791_2_, final int p_180791_3_, final int p_180791_4_, final int mouseXIn, final int mouseYIn) {
            final SaveFormatComparator saveformatcomparator = GuiSelectWorld.this.field_146639_s.get(entryID);
            String s = saveformatcomparator.getDisplayName();
            if (StringUtils.isEmpty(s)) {
                s = String.valueOf(GuiSelectWorld.this.field_146637_u) + " " + (entryID + 1);
            }
            String s2 = saveformatcomparator.getFileName();
            s2 = String.valueOf(s2) + " (" + GuiSelectWorld.this.field_146633_h.format(new Date(saveformatcomparator.getLastTimePlayed()));
            s2 = String.valueOf(s2) + ")";
            String s3 = "";
            if (saveformatcomparator.requiresConversion()) {
                s3 = String.valueOf(GuiSelectWorld.this.field_146636_v) + " " + s3;
            }
            else {
                s3 = GuiSelectWorld.this.field_146635_w[saveformatcomparator.getEnumGameType().getID()];
                if (saveformatcomparator.isHardcoreModeEnabled()) {
                    s3 = EnumChatFormatting.DARK_RED + I18n.format("gameMode.hardcore", new Object[0]) + EnumChatFormatting.RESET;
                }
                if (saveformatcomparator.getCheatsEnabled()) {
                    s3 = String.valueOf(s3) + ", " + I18n.format("selectWorld.cheats", new Object[0]);
                }
            }
            GuiSelectWorld.this.drawString(GuiSelectWorld.this.fontRendererObj, s, p_180791_2_ + 2, p_180791_3_ + 1, 16777215);
            GuiSelectWorld.this.drawString(GuiSelectWorld.this.fontRendererObj, s2, p_180791_2_ + 2, p_180791_3_ + 12, 8421504);
            GuiSelectWorld.this.drawString(GuiSelectWorld.this.fontRendererObj, s3, p_180791_2_ + 2, p_180791_3_ + 12 + 10, 8421504);
        }
    }
}
