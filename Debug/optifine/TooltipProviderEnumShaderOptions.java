package optifine;

import java.awt.Rectangle;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import shadersmod.client.EnumShaderOption;
import shadersmod.client.GuiButtonEnumShaderOption;

public class TooltipProviderEnumShaderOptions implements TooltipProvider
{
    public Rectangle getTooltipBounds(GuiScreen p_getTooltipBounds_1_, int p_getTooltipBounds_2_, int p_getTooltipBounds_3_)
    {
        int i = p_getTooltipBounds_1_.width - 450;
        int j = 35;

        if (i < 10)
        {
            i = 10;
        }

        if (p_getTooltipBounds_3_ <= j + 94)
        {
            j += 100;
        }

        int k = i + 150 + 150;
        int l = j + 84 + 10;
        return new Rectangle(i, j, k - i, l - j);
    }

    public boolean isRenderBorder()
    {
        return true;
    }

    public String[] getTooltipLines(GuiButton p_getTooltipLines_1_, int p_getTooltipLines_2_)
    {
        if (!(p_getTooltipLines_1_ instanceof GuiButtonEnumShaderOption))
        {
            return null;
        }
        else
        {
            GuiButtonEnumShaderOption guibuttonenumshaderoption = (GuiButtonEnumShaderOption)p_getTooltipLines_1_;
            EnumShaderOption enumshaderoption = guibuttonenumshaderoption.getEnumShaderOption();
            String[] astring = this.getTooltipLines(enumshaderoption);
            return astring;
        }
    }

    private String[] getTooltipLines(EnumShaderOption p_getTooltipLines_1_)
    {
        return TooltipProviderOptions.getTooltipLines(p_getTooltipLines_1_.getResourceKey());
    }
}
