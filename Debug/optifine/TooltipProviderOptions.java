package optifine;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.GameSettings;

public class TooltipProviderOptions implements TooltipProvider
{
    public Rectangle getTooltipBounds(GuiScreen p_getTooltipBounds_1_, int p_getTooltipBounds_2_, int p_getTooltipBounds_3_)
    {
        int i = p_getTooltipBounds_1_.width / 2 - 150;
        int j = p_getTooltipBounds_1_.height / 6 - 7;

        if (p_getTooltipBounds_3_ <= j + 98)
        {
            j += 105;
        }

        int k = i + 150 + 150;
        int l = j + 84 + 10;
        return new Rectangle(i, j, k - i, l - j);
    }

    public boolean isRenderBorder()
    {
        return false;
    }

    public String[] getTooltipLines(GuiButton p_getTooltipLines_1_, int p_getTooltipLines_2_)
    {
        if (!(p_getTooltipLines_1_ instanceof IOptionControl))
        {
            return null;
        }
        else
        {
            IOptionControl ioptioncontrol = (IOptionControl)p_getTooltipLines_1_;
            GameSettings.Options gamesettings$options = ioptioncontrol.getOption();
            String[] astring = getTooltipLines(gamesettings$options.getEnumString());
            return astring;
        }
    }

    public static String[] getTooltipLines(String p_getTooltipLines_0_)
    {
        List<String> list = new ArrayList();

        for (int i = 0; i < 10; ++i)
        {
            String s = p_getTooltipLines_0_ + ".tooltip." + (i + 1);
            String s1 = Lang.get(s, (String)null);

            if (s1 == null)
            {
                break;
            }

            list.add(s1);
        }

        if (list.size() <= 0)
        {
            return null;
        }
        else
        {
            String[] astring = (String[])((String[])list.toArray(new String[list.size()]));
            return astring;
        }
    }
}
