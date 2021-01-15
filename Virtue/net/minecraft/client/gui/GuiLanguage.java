package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.Language;
import net.minecraft.client.resources.LanguageManager;
import net.minecraft.client.settings.GameSettings;

public class GuiLanguage extends GuiScreen
{
    protected GuiScreen field_146453_a;
    private GuiLanguage.List field_146450_f;

    /** Reference to the GameSettings object. */
    private final GameSettings game_settings_3;
    private final LanguageManager field_146454_h;
    private GuiOptionButton field_146455_i;
    private GuiOptionButton field_146452_r;
    private static final String __OBFID = "CL_00000698";

    public GuiLanguage(GuiScreen p_i1043_1_, GameSettings p_i1043_2_, LanguageManager p_i1043_3_)
    {
        this.field_146453_a = p_i1043_1_;
        this.game_settings_3 = p_i1043_2_;
        this.field_146454_h = p_i1043_3_;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        this.buttonList.add(this.field_146455_i = new GuiOptionButton(100, this.width / 2 - 155, this.height - 38, GameSettings.Options.FORCE_UNICODE_FONT, this.game_settings_3.getKeyBinding(GameSettings.Options.FORCE_UNICODE_FONT)));
        this.buttonList.add(this.field_146452_r = new GuiOptionButton(6, this.width / 2 - 155 + 160, this.height - 38, I18n.format("gui.done", new Object[0])));
        this.field_146450_f = new GuiLanguage.List(this.mc);
        this.field_146450_f.registerScrollButtons(7, 8);
    }

    /**
     * Handles mouse input.
     */
    public void handleMouseInput() throws IOException
    {
        super.handleMouseInput();
        this.field_146450_f.func_178039_p();
    }

    protected void actionPerformed(GuiButton button) throws IOException
    {
        if (button.enabled)
        {
            switch (button.id)
            {
                case 5:
                    break;

                case 6:
                    this.mc.displayGuiScreen(this.field_146453_a);
                    break;

                case 100:
                    if (button instanceof GuiOptionButton)
                    {
                        this.game_settings_3.setOptionValue(((GuiOptionButton)button).returnEnumOptions(), 1);
                        button.displayString = this.game_settings_3.getKeyBinding(GameSettings.Options.FORCE_UNICODE_FONT);
                        ScaledResolution var2 = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
                        int var3 = var2.getScaledWidth();
                        int var4 = var2.getScaledHeight();
                        this.setWorldAndResolution(this.mc, var3, var4);
                    }

                    break;

                default:
                    this.field_146450_f.actionPerformed(button);
            }
        }
    }

    /**
     * Draws the screen and all the components in it. Args : mouseX, mouseY, renderPartialTicks
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.field_146450_f.drawScreen(mouseX, mouseY, partialTicks);
        this.drawCenteredString(this.fontRendererObj, I18n.format("options.language", new Object[0]), this.width / 2, 16, 16777215);
        this.drawCenteredString(this.fontRendererObj, "(" + I18n.format("options.languageWarning", new Object[0]) + ")", this.width / 2, this.height - 56, 8421504);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    class List extends GuiSlot
    {
        private final java.util.List field_148176_l = Lists.newArrayList();
        private final Map field_148177_m = Maps.newHashMap();
        private static final String __OBFID = "CL_00000699";

        public List(Minecraft mcIn)
        {
            super(mcIn, GuiLanguage.this.width, GuiLanguage.this.height, 32, GuiLanguage.this.height - 65 + 4, 18);
            Iterator var3 = GuiLanguage.this.field_146454_h.getLanguages().iterator();

            while (var3.hasNext())
            {
                Language var4 = (Language)var3.next();
                this.field_148177_m.put(var4.getLanguageCode(), var4);
                this.field_148176_l.add(var4.getLanguageCode());
            }
        }

        protected int getSize()
        {
            return this.field_148176_l.size();
        }

        protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY)
        {
            Language var5 = (Language)this.field_148177_m.get(this.field_148176_l.get(slotIndex));
            GuiLanguage.this.field_146454_h.setCurrentLanguage(var5);
            GuiLanguage.this.game_settings_3.language = var5.getLanguageCode();
            this.mc.refreshResources();
            GuiLanguage.this.fontRendererObj.setUnicodeFlag(GuiLanguage.this.field_146454_h.isCurrentLocaleUnicode() || GuiLanguage.this.game_settings_3.forceUnicodeFont);
            GuiLanguage.this.fontRendererObj.setBidiFlag(GuiLanguage.this.field_146454_h.isCurrentLanguageBidirectional());
            GuiLanguage.this.field_146452_r.displayString = I18n.format("gui.done", new Object[0]);
            GuiLanguage.this.field_146455_i.displayString = GuiLanguage.this.game_settings_3.getKeyBinding(GameSettings.Options.FORCE_UNICODE_FONT);
            GuiLanguage.this.game_settings_3.saveOptions();
        }

        protected boolean isSelected(int slotIndex)
        {
            return ((String)this.field_148176_l.get(slotIndex)).equals(GuiLanguage.this.field_146454_h.getCurrentLanguage().getLanguageCode());
        }

        protected int getContentHeight()
        {
            return this.getSize() * 18;
        }

        protected void drawBackground()
        {
            GuiLanguage.this.drawDefaultBackground();
        }

        protected void drawSlot(int p_180791_1_, int p_180791_2_, int p_180791_3_, int p_180791_4_, int p_180791_5_, int p_180791_6_)
        {
            GuiLanguage.this.fontRendererObj.setBidiFlag(true);
            GuiLanguage.this.drawCenteredString(GuiLanguage.this.fontRendererObj, ((Language)this.field_148177_m.get(this.field_148176_l.get(p_180791_1_))).toString(), this.width / 2, p_180791_3_ + 1, 16777215);
            GuiLanguage.this.fontRendererObj.setBidiFlag(GuiLanguage.this.field_146454_h.getCurrentLanguage().isBidirectional());
        }
    }
}
