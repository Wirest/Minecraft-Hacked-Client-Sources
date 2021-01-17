// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import com.google.common.collect.Lists;
import java.util.List;

public class GuiLabel extends Gui
{
    protected int field_146167_a;
    protected int field_146161_f;
    public int field_146162_g;
    public int field_146174_h;
    private List<String> field_146173_k;
    public int field_175204_i;
    private boolean centered;
    public boolean visible;
    private boolean labelBgEnabled;
    private int field_146168_n;
    private int field_146169_o;
    private int field_146166_p;
    private int field_146165_q;
    private FontRenderer fontRenderer;
    private int field_146163_s;
    
    public GuiLabel(final FontRenderer fontRendererObj, final int p_i45540_2_, final int p_i45540_3_, final int p_i45540_4_, final int p_i45540_5_, final int p_i45540_6_, final int p_i45540_7_) {
        this.field_146167_a = 200;
        this.field_146161_f = 20;
        this.visible = true;
        this.fontRenderer = fontRendererObj;
        this.field_175204_i = p_i45540_2_;
        this.field_146162_g = p_i45540_3_;
        this.field_146174_h = p_i45540_4_;
        this.field_146167_a = p_i45540_5_;
        this.field_146161_f = p_i45540_6_;
        this.field_146173_k = (List<String>)Lists.newArrayList();
        this.centered = false;
        this.labelBgEnabled = false;
        this.field_146168_n = p_i45540_7_;
        this.field_146169_o = -1;
        this.field_146166_p = -1;
        this.field_146165_q = -1;
        this.field_146163_s = 0;
    }
    
    public void func_175202_a(final String p_175202_1_) {
        this.field_146173_k.add(I18n.format(p_175202_1_, new Object[0]));
    }
    
    public GuiLabel setCentered() {
        this.centered = true;
        return this;
    }
    
    public void drawLabel(final Minecraft mc, final int mouseX, final int mouseY) {
        if (this.visible) {
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            this.drawLabelBackground(mc, mouseX, mouseY);
            final int i = this.field_146174_h + this.field_146161_f / 2 + this.field_146163_s / 2;
            final int j = i - this.field_146173_k.size() * 10 / 2;
            for (int k = 0; k < this.field_146173_k.size(); ++k) {
                if (this.centered) {
                    this.drawCenteredString(this.fontRenderer, this.field_146173_k.get(k), this.field_146162_g + this.field_146167_a / 2, j + k * 10, this.field_146168_n);
                }
                else {
                    this.drawString(this.fontRenderer, this.field_146173_k.get(k), this.field_146162_g, j + k * 10, this.field_146168_n);
                }
            }
        }
    }
    
    protected void drawLabelBackground(final Minecraft mcIn, final int p_146160_2_, final int p_146160_3_) {
        if (this.labelBgEnabled) {
            final int i = this.field_146167_a + this.field_146163_s * 2;
            final int j = this.field_146161_f + this.field_146163_s * 2;
            final int k = this.field_146162_g - this.field_146163_s;
            final int l = this.field_146174_h - this.field_146163_s;
            Gui.drawRect(k, l, k + i, l + j, this.field_146169_o);
            this.drawHorizontalLine(k, k + i, l, this.field_146166_p);
            this.drawHorizontalLine(k, k + i, l + j, this.field_146165_q);
            this.drawVerticalLine(k, l, l + j, this.field_146166_p);
            this.drawVerticalLine(k + i, l, l + j, this.field_146165_q);
        }
    }
}
