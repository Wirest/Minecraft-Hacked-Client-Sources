package net.minecraft.client.gui.advancements;

import com.google.common.collect.Maps;
import java.io.IOException;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.ClientAdvancementManager;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.network.play.client.CPacketSeenAdvancements;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

public class GuiScreenAdvancements extends GuiScreen implements ClientAdvancementManager.IListener
{
    private static final ResourceLocation field_191943_f = new ResourceLocation("textures/gui/advancements/window.png");
    private static final ResourceLocation field_191945_g = new ResourceLocation("textures/gui/advancements/tabs.png");
    private final ClientAdvancementManager field_191946_h;
    private final Map<Advancement, GuiAdvancementTab> field_191947_i = Maps.<Advancement, GuiAdvancementTab>newLinkedHashMap();
    private GuiAdvancementTab field_191940_s;
    private int field_191941_t;
    private int field_191942_u;
    private boolean field_191944_v;

    public GuiScreenAdvancements(ClientAdvancementManager p_i47383_1_)
    {
        this.field_191946_h = p_i47383_1_;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    public void initGui()
    {
        this.field_191947_i.clear();
        this.field_191940_s = null;
        this.field_191946_h.func_192798_a(this);

        if (this.field_191940_s == null && !this.field_191947_i.isEmpty())
        {
            this.field_191946_h.func_194230_a(((GuiAdvancementTab)this.field_191947_i.values().iterator().next()).func_193935_c(), true);
        }
        else
        {
            this.field_191946_h.func_194230_a(this.field_191940_s == null ? null : this.field_191940_s.func_193935_c(), true);
        }
    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    public void onGuiClosed()
    {
        this.field_191946_h.func_192798_a((ClientAdvancementManager.IListener)null);
        NetHandlerPlayClient nethandlerplayclient = this.mc.getConnection();

        if (nethandlerplayclient != null)
        {
            nethandlerplayclient.sendPacket(CPacketSeenAdvancements.func_194164_a());
        }
    }

    /**
     * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
     */
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        if (mouseButton == 0)
        {
            int i = (this.width - 252) / 2;
            int j = (this.height - 140) / 2;

            for (GuiAdvancementTab guiadvancementtab : this.field_191947_i.values())
            {
                if (guiadvancementtab.func_191793_c(i, j, mouseX, mouseY))
                {
                    this.field_191946_h.func_194230_a(guiadvancementtab.func_193935_c(), true);
                    break;
                }
            }
        }

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    /**
     * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     */
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        if (keyCode == this.mc.gameSettings.field_194146_ao.getKeyCode())
        {
            this.mc.displayGuiScreen((GuiScreen)null);
            this.mc.setIngameFocus();
        }
        else
        {
            super.keyTyped(typedChar, keyCode);
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        int i = (this.width - 252) / 2;
        int j = (this.height - 140) / 2;

        if (Mouse.isButtonDown(0))
        {
            if (!this.field_191944_v)
            {
                this.field_191944_v = true;
            }
            else if (this.field_191940_s != null)
            {
                this.field_191940_s.func_191797_b(mouseX - this.field_191941_t, mouseY - this.field_191942_u);
            }

            this.field_191941_t = mouseX;
            this.field_191942_u = mouseY;
        }
        else
        {
            this.field_191944_v = false;
        }

        this.drawDefaultBackground();
        this.func_191936_c(mouseX, mouseY, i, j);
        this.func_191934_b(i, j);
        this.func_191937_d(mouseX, mouseY, i, j);
    }

    private void func_191936_c(int p_191936_1_, int p_191936_2_, int p_191936_3_, int p_191936_4_)
    {
        GuiAdvancementTab guiadvancementtab = this.field_191940_s;

        if (guiadvancementtab == null)
        {
            drawRect(p_191936_3_ + 9, p_191936_4_ + 18, p_191936_3_ + 9 + 234, p_191936_4_ + 18 + 113, -16777216);
            String s = I18n.format("advancements.empty");
            int i = this.fontRendererObj.getStringWidth(s);
            this.fontRendererObj.drawString(s, p_191936_3_ + 9 + 117 - i / 2, p_191936_4_ + 18 + 56 - this.fontRendererObj.FONT_HEIGHT / 2, -1);
            this.fontRendererObj.drawString(":(", p_191936_3_ + 9 + 117 - this.fontRendererObj.getStringWidth(":(") / 2, p_191936_4_ + 18 + 113 - this.fontRendererObj.FONT_HEIGHT, -1);
        }
        else
        {
            GlStateManager.pushMatrix();
            GlStateManager.translate((float)(p_191936_3_ + 9), (float)(p_191936_4_ + 18), -400.0F);
            GlStateManager.enableDepth();
            guiadvancementtab.func_191799_a();
            GlStateManager.popMatrix();
            GlStateManager.depthFunc(515);
            GlStateManager.disableDepth();
        }
    }

    public void func_191934_b(int p_191934_1_, int p_191934_2_)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableBlend();
        RenderHelper.disableStandardItemLighting();
        this.mc.getTextureManager().bindTexture(field_191943_f);
        this.drawTexturedModalRect(p_191934_1_, p_191934_2_, 0, 0, 252, 140);

        if (this.field_191947_i.size() > 1)
        {
            this.mc.getTextureManager().bindTexture(field_191945_g);

            for (GuiAdvancementTab guiadvancementtab : this.field_191947_i.values())
            {
                guiadvancementtab.func_191798_a(p_191934_1_, p_191934_2_, guiadvancementtab == this.field_191940_s);
            }

            GlStateManager.enableRescaleNormal();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            RenderHelper.enableGUIStandardItemLighting();

            for (GuiAdvancementTab guiadvancementtab1 : this.field_191947_i.values())
            {
                guiadvancementtab1.func_191796_a(p_191934_1_, p_191934_2_, this.itemRender);
            }

            GlStateManager.disableBlend();
        }

        this.fontRendererObj.drawString(I18n.format("gui.advancements"), p_191934_1_ + 8, p_191934_2_ + 6, 4210752);
    }

    private void func_191937_d(int p_191937_1_, int p_191937_2_, int p_191937_3_, int p_191937_4_)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        if (this.field_191940_s != null)
        {
            GlStateManager.pushMatrix();
            GlStateManager.enableDepth();
            GlStateManager.translate((float)(p_191937_3_ + 9), (float)(p_191937_4_ + 18), 400.0F);
            this.field_191940_s.func_192991_a(p_191937_1_ - p_191937_3_ - 9, p_191937_2_ - p_191937_4_ - 18, p_191937_3_, p_191937_4_);
            GlStateManager.disableDepth();
            GlStateManager.popMatrix();
        }

        if (this.field_191947_i.size() > 1)
        {
            for (GuiAdvancementTab guiadvancementtab : this.field_191947_i.values())
            {
                if (guiadvancementtab.func_191793_c(p_191937_3_, p_191937_4_, p_191937_1_, p_191937_2_))
                {
                    this.drawCreativeTabHoveringText(guiadvancementtab.func_191795_d(), p_191937_1_, p_191937_2_);
                }
            }
        }
    }

    public void func_191931_a(Advancement p_191931_1_)
    {
        GuiAdvancementTab guiadvancementtab = GuiAdvancementTab.func_193936_a(this.mc, this, this.field_191947_i.size(), p_191931_1_);

        if (guiadvancementtab != null)
        {
            this.field_191947_i.put(p_191931_1_, guiadvancementtab);
        }
    }

    public void func_191928_b(Advancement p_191928_1_)
    {
    }

    public void func_191932_c(Advancement p_191932_1_)
    {
        GuiAdvancementTab guiadvancementtab = this.func_191935_f(p_191932_1_);

        if (guiadvancementtab != null)
        {
            guiadvancementtab.func_191800_a(p_191932_1_);
        }
    }

    public void func_191929_d(Advancement p_191929_1_)
    {
    }

    public void func_191933_a(Advancement p_191933_1_, AdvancementProgress p_191933_2_)
    {
        GuiAdvancement guiadvancement = this.func_191938_e(p_191933_1_);

        if (guiadvancement != null)
        {
            guiadvancement.func_191824_a(p_191933_2_);
        }
    }

    public void func_193982_e(@Nullable Advancement p_193982_1_)
    {
        this.field_191940_s = this.field_191947_i.get(p_193982_1_);
    }

    public void func_191930_a()
    {
        this.field_191947_i.clear();
        this.field_191940_s = null;
    }

    @Nullable
    public GuiAdvancement func_191938_e(Advancement p_191938_1_)
    {
        GuiAdvancementTab guiadvancementtab = this.func_191935_f(p_191938_1_);
        return guiadvancementtab == null ? null : guiadvancementtab.func_191794_b(p_191938_1_);
    }

    @Nullable
    private GuiAdvancementTab func_191935_f(Advancement p_191935_1_)
    {
        while (p_191935_1_.func_192070_b() != null)
        {
            p_191935_1_ = p_191935_1_.func_192070_b();
        }

        return this.field_191947_i.get(p_191935_1_);
    }
}
