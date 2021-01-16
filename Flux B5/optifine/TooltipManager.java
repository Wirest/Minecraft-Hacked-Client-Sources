package optifine;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiVideoSettings;
import net.minecraft.client.settings.GameSettings;

public class TooltipManager
{
    private GuiScreen guiScreen = null;
    private int lastMouseX = 0;
    private int lastMouseY = 0;
    private long mouseStillTime = 0L;

    public TooltipManager(GuiScreen guiScreen)
    {
        this.guiScreen = guiScreen;
    }

    public void drawTooltips(int x, int y, List buttonList)
    {
        if (Math.abs(x - this.lastMouseX) <= 5 && Math.abs(y - this.lastMouseY) <= 5)
        {
            short activateDelay = 700;

            if (System.currentTimeMillis() >= this.mouseStillTime + (long)activateDelay)
            {
                int x1 = this.guiScreen.width / 2 - 150;
                int y1 = this.guiScreen.height / 6 - 7;

                if (y <= y1 + 98)
                {
                    y1 += 105;
                }

                int x2 = x1 + 150 + 150;
                int y2 = y1 + 84 + 10;
                GuiButton btn = this.getSelectedButton(x, y, buttonList);

                if (btn instanceof IOptionControl)
                {
                    IOptionControl ctl = (IOptionControl)btn;
                    GameSettings.Options option = ctl.getOption();
                    String[] lines = getTooltipLines(option);

                    if (lines == null)
                    {
                        return;
                    }

                    GuiVideoSettings.drawGradientRect(this.guiScreen, x1, y1, x2, y2, -536870912, -536870912);

                    for (int i = 0; i < lines.length; ++i)
                    {
                        String line = lines[i];
                        int col = 14540253;

                        if (line.endsWith("!"))
                        {
                            col = 16719904;
                        }

                        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;
                        fontRenderer.func_175063_a(line, (float)(x1 + 5), (float)(y1 + 5 + i * 11), col);
                    }
                }
            }
        }
        else
        {
            this.lastMouseX = x;
            this.lastMouseY = y;
            this.mouseStillTime = System.currentTimeMillis();
        }
    }

    private GuiButton getSelectedButton(int x, int y, List buttonList)
    {
        for (int k = 0; k < buttonList.size(); ++k)
        {
            GuiButton btn = (GuiButton)buttonList.get(k);
            int btnWidth = GuiVideoSettings.getButtonWidth(btn);
            int btnHeight = GuiVideoSettings.getButtonHeight(btn);
            boolean flag = x >= btn.xPosition && y >= btn.yPosition && x < btn.xPosition + btnWidth && y < btn.yPosition + btnHeight;

            if (flag)
            {
                return btn;
            }
        }

        return null;
    }

    private static String[] getTooltipLines(GameSettings.Options option)
    {
        return getTooltipLines(option.getEnumString());
    }

    private static String[] getTooltipLines(String key)
    {
        ArrayList list = new ArrayList();

        for (int lines = 0; lines < 10; ++lines)
        {
            String lineKey = key + ".tooltip." + (lines + 1);
            String line = Lang.get(lineKey, (String)null);

            if (line == null)
            {
                break;
            }

            list.add(line);
        }

        if (list.size() <= 0)
        {
            return null;
        }
        else
        {
            String[] var5 = (String[])((String[])list.toArray(new String[list.size()]));
            return var5;
        }
    }
}
