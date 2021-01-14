package optifine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.settings.GameSettings;
import shadersmod.client.GuiButtonShaderOption;
import shadersmod.client.ShaderOption;
import shadersmod.client.ShaderOptionProfile;

public class TooltipProviderShaderOptions extends TooltipProviderOptions
{
    public String[] getTooltipLines(GuiButton p_getTooltipLines_1_, int p_getTooltipLines_2_)
    {
        if (!(p_getTooltipLines_1_ instanceof GuiButtonShaderOption))
        {
            return null;
        }
        else
        {
            GuiButtonShaderOption guibuttonshaderoption = (GuiButtonShaderOption)p_getTooltipLines_1_;
            ShaderOption shaderoption = guibuttonshaderoption.getShaderOption();
            String[] astring = this.makeTooltipLines(shaderoption, p_getTooltipLines_2_);
            return astring;
        }
    }

    private String[] makeTooltipLines(ShaderOption p_makeTooltipLines_1_, int p_makeTooltipLines_2_)
    {
        if (p_makeTooltipLines_1_ instanceof ShaderOptionProfile)
        {
            return null;
        }
        else
        {
            String s = p_makeTooltipLines_1_.getNameText();
            String s1 = Config.normalize(p_makeTooltipLines_1_.getDescriptionText()).trim();
            String[] astring = this.splitDescription(s1);
            GameSettings gamesettings = Config.getGameSettings();
            String s2 = null;

            if (!s.equals(p_makeTooltipLines_1_.getName()) && gamesettings.advancedItemTooltips)
            {
                s2 = "\u00a78" + Lang.get("of.general.id") + ": " + p_makeTooltipLines_1_.getName();
            }

            String s3 = null;

            if (p_makeTooltipLines_1_.getPaths() != null && gamesettings.advancedItemTooltips)
            {
                s3 = "\u00a78" + Lang.get("of.general.from") + ": " + Config.arrayToString((Object[])p_makeTooltipLines_1_.getPaths());
            }

            String s4 = null;

            if (p_makeTooltipLines_1_.getValueDefault() != null && gamesettings.advancedItemTooltips)
            {
                String s5 = p_makeTooltipLines_1_.isEnabled() ? p_makeTooltipLines_1_.getValueText(p_makeTooltipLines_1_.getValueDefault()) : Lang.get("of.general.ambiguous");
                s4 = "\u00a78" + Lang.getDefault() + ": " + s5;
            }

            List<String> list = new ArrayList();
            list.add(s);
            list.addAll(Arrays.<String>asList(astring));

            if (s2 != null)
            {
                list.add(s2);
            }

            if (s3 != null)
            {
                list.add(s3);
            }

            if (s4 != null)
            {
                list.add(s4);
            }

            String[] astring1 = this.makeTooltipLines(p_makeTooltipLines_2_, list);
            return astring1;
        }
    }

    private String[] splitDescription(String p_splitDescription_1_)
    {
        if (p_splitDescription_1_.length() <= 0)
        {
            return new String[0];
        }
        else
        {
            p_splitDescription_1_ = StrUtils.removePrefix(p_splitDescription_1_, "//");
            String[] astring = p_splitDescription_1_.split("\\. ");

            for (int i = 0; i < astring.length; ++i)
            {
                astring[i] = "- " + astring[i].trim();
                astring[i] = StrUtils.removeSuffix(astring[i], ".");
            }

            return astring;
        }
    }

    private String[] makeTooltipLines(int p_makeTooltipLines_1_, List<String> p_makeTooltipLines_2_)
    {
        FontRenderer fontrenderer = Config.getMinecraft().fontRendererObj;
        List<String> list = new ArrayList();

        for (int i = 0; i < p_makeTooltipLines_2_.size(); ++i)
        {
            String s = (String)p_makeTooltipLines_2_.get(i);

            if (s != null && s.length() > 0)
            {
                for (String s1 : fontrenderer.listFormattedStringToWidth(s, p_makeTooltipLines_1_))
                {
                    list.add(s1);
                }
            }
        }

        String[] astring = (String[])((String[])list.toArray(new String[list.size()]));
        return astring;
    }
}
