package net.minecraft.client.gui;

import com.google.common.collect.Lists;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;

public class GuiLabel extends Gui {
    protected int field_146167_a = 200;
    protected int field_146161_f = 20;
    public int field_146162_g;
    public int field_146174_h;
    private List field_146173_k;
    public int field_175204_i;
    private boolean centered;
    public boolean visible = true;
    private boolean labelBgEnabled;
    private int field_146168_n;
    private int field_146169_o;
    private int field_146166_p;
    private int field_146165_q;
    private FontRenderer fontRenderer;
    private int field_146163_s;
    private static final String __OBFID = "CL_00000671";

    public GuiLabel(FontRenderer p_i45540_1_, int p_i45540_2_, int p_i45540_3_, int p_i45540_4_, int p_i45540_5_, int p_i45540_6_, int p_i45540_7_) {
        this.fontRenderer = p_i45540_1_;
        this.field_175204_i = p_i45540_2_;
        this.field_146162_g = p_i45540_3_;
        this.field_146174_h = p_i45540_4_;
        this.field_146167_a = p_i45540_5_;
        this.field_146161_f = p_i45540_6_;
        this.field_146173_k = Lists.newArrayList();
        this.centered = false;
        this.labelBgEnabled = false;
        this.field_146168_n = p_i45540_7_;
        this.field_146169_o = -1;
        this.field_146166_p = -1;
        this.field_146165_q = -1;
        this.field_146163_s = 0;
    }

    public void func_175202_a(String p_175202_1_) {
        this.field_146173_k.add(I18n.format(p_175202_1_, new Object[0]));
    }

    public GuiLabel func_175203_a() {
        this.centered = true;
        return this;
    }

    public void drawLabel(Minecraft mc, int mouseX, int mouseY) {
        if (this.visible) {
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            this.drawLabelBackground(mc, mouseX, mouseY);
            int var4 = this.field_146174_h + this.field_146161_f / 2 + this.field_146163_s / 2;
            int var5 = var4 - this.field_146173_k.size() * 10 / 2;

            for (int var6 = 0; var6 < this.field_146173_k.size(); ++var6) {
                if (this.centered) {
                    this.drawCenteredString(this.fontRenderer, (String) this.field_146173_k.get(var6), this.field_146162_g + this.field_146167_a / 2, var5 + var6 * 10, this.field_146168_n);
                } else {
                    this.drawString(this.fontRenderer, (String) this.field_146173_k.get(var6), this.field_146162_g, var5 + var6 * 10, this.field_146168_n);
                }
            }
        }
    }

    protected void drawLabelBackground(Minecraft mcIn, int p_146160_2_, int p_146160_3_) {
        if (this.labelBgEnabled) {
            int var4 = this.field_146167_a + this.field_146163_s * 2;
            int var5 = this.field_146161_f + this.field_146163_s * 2;
            int var6 = this.field_146162_g - this.field_146163_s;
            int var7 = this.field_146174_h - this.field_146163_s;
            drawRect(var6, var7, var6 + var4, var7 + var5, this.field_146169_o);
            this.drawHorizontalLine(var6, var6 + var4, var7, this.field_146166_p);
            this.drawHorizontalLine(var6, var6 + var4, var7 + var5, this.field_146165_q);
            this.drawVerticalLine(var6, var7, var7 + var5, this.field_146166_p);
            this.drawVerticalLine(var6 + var4, var7, var7 + var5, this.field_146165_q);
        }
    }
}
