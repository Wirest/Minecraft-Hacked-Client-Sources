package optifine;

import java.awt.Rectangle;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class TooltipManager
{
    private GuiScreen guiScreen;
    private TooltipProvider tooltipProvider;
    private int lastMouseX = 0;
    private int lastMouseY = 0;
    private long mouseStillTime = 0L;

    public TooltipManager(GuiScreen p_i112_1_, TooltipProvider p_i112_2_)
    {
        this.guiScreen = p_i112_1_;
        this.tooltipProvider = p_i112_2_;
    }

    public void drawTooltips(int p_drawTooltips_1_, int p_drawTooltips_2_, List p_drawTooltips_3_)
    {
        if (Math.abs(p_drawTooltips_1_ - this.lastMouseX) <= 5 && Math.abs(p_drawTooltips_2_ - this.lastMouseY) <= 5)
        {
            int i = 700;

            if (System.currentTimeMillis() >= this.mouseStillTime + (long)i)
            {
                GuiButton guibutton = GuiScreenOF.getSelectedButton(p_drawTooltips_1_, p_drawTooltips_2_, p_drawTooltips_3_);

                if (guibutton != null)
                {
                    Rectangle rectangle = this.tooltipProvider.getTooltipBounds(this.guiScreen, p_drawTooltips_1_, p_drawTooltips_2_);
                    String[] astring = this.tooltipProvider.getTooltipLines(guibutton, rectangle.width);

                    if (astring != null)
                    {
                        if (this.tooltipProvider.isRenderBorder())
                        {
                            int j = -528449408;
                            this.drawRectBorder(rectangle.x, rectangle.y, rectangle.x + rectangle.width, rectangle.y + rectangle.height, j);
                        }

                        Gui.drawRect(rectangle.x, rectangle.y, rectangle.x + rectangle.width, rectangle.y + rectangle.height, -536870912);

                        for (int l = 0; l < astring.length; ++l)
                        {
                            String s = astring[l];
                            int k = 14540253;

                            if (s.endsWith("!"))
                            {
                                k = 16719904;
                            }

                            FontRenderer fontrenderer = Minecraft.getMinecraft().fontRendererObj;
                            fontrenderer.drawStringWithShadow(s, (float)(rectangle.x + 5), (float)(rectangle.y + 5 + l * 11), k);
                        }
                    }
                }
            }
        }
        else
        {
            this.lastMouseX = p_drawTooltips_1_;
            this.lastMouseY = p_drawTooltips_2_;
            this.mouseStillTime = System.currentTimeMillis();
        }
    }

    private void drawRectBorder(int p_drawRectBorder_1_, int p_drawRectBorder_2_, int p_drawRectBorder_3_, int p_drawRectBorder_4_, int p_drawRectBorder_5_)
    {
        Gui.drawRect(p_drawRectBorder_1_, p_drawRectBorder_2_ - 1, p_drawRectBorder_3_, p_drawRectBorder_2_, p_drawRectBorder_5_);
        Gui.drawRect(p_drawRectBorder_1_, p_drawRectBorder_4_, p_drawRectBorder_3_, p_drawRectBorder_4_ + 1, p_drawRectBorder_5_);
        Gui.drawRect(p_drawRectBorder_1_ - 1, p_drawRectBorder_2_, p_drawRectBorder_1_, p_drawRectBorder_4_, p_drawRectBorder_5_);
        Gui.drawRect(p_drawRectBorder_3_, p_drawRectBorder_2_, p_drawRectBorder_3_ + 1, p_drawRectBorder_4_, p_drawRectBorder_5_);
    }
}
