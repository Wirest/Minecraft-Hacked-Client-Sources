package com.ihl.client.gui;

import com.ihl.client.Helper;
import com.ihl.client.input.InputUtil;
import com.ihl.client.module.option.Option;
import com.ihl.client.util.ColorUtil;
import com.ihl.client.util.MathUtil;
import com.ihl.client.util.RenderUtil2D;
import com.ihl.client.util.part.Settings;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.Display;

import java.util.List;

public class Ring extends Component {

    public static double size = 256;
    public static double width = 96;

    protected final double segPadding = 0;
    protected final double modulePadding = 0;
    protected final double optionPadding = 0;
    protected final double settingsPadding = 0.5;

    protected double x, y;
    protected int selected = -1;
    protected double selectedR = selected;
    protected boolean mouseOver, mouseOverSettings;

    protected final double settingSliderWidth = 32;
    protected boolean[] hasSettings;
    protected double[] settingSlider;
    protected ResourceLocation settingsIcon = new ResourceLocation("client/icons/settings.png");

    protected double[] alpha = new double[]{0, 0};
    protected List<Object> list;

    protected double sizeR;

    public Ring(List list) {
        this.list = list;
        x = Display.getWidth() / 2;
        y = Display.getHeight() / 2;
        sizeR = size + width;
        settingSlider = new double[list.size()];
        hasSettings = new boolean[list.size()];
    }

    @Override
    public void tick() {
        size = Math.min(Display.getHeight()/3, 256);
        width = size/(256/96d);
        double dist = MathUtil.distTo(x, y, InputUtil.mouse[0], InputUtil.mouse[1]);
        if (dist > size - width) {
            double mang = MathUtil.dirTo(x, y, InputUtil.mouse[0], InputUtil.mouse[1]) + 180;
            selected = (int) Math.floor((list.size() / 360D) * mang) % list.size();
            mouseOver = true;
            if (alpha[0] == 0) {
                selectedR = selected;
            }
            mouseOverSettings = dist > size;
        } else {
            mouseOver = false;
            mouseOverSettings = false;
        }

        double a = 2;
        double b = 2;
        double c = 2;
        double d = 0.4;

        for(int i = 0; i < settingSlider.length; i++) {
            settingSlider[i]+= ((mouseOverSettings && selected == i && hasSettings[i] ? 1 : 0)-settingSlider[i])/a;
        }

        selectedR += (((((selected - selectedR) % list.size()) + (list.size() * 1.5D)) % list.size()) - (list.size() * 0.5D)) / b;
        sizeR += (size - sizeR) / c;
        alpha[0] += (mouseOver ? d : -d);
        alpha[1] += ((Helper.mc().currentScreen instanceof GuiHandle ? 1 : 0) - alpha[1]) / b;

        for (int i = 0; i < alpha.length; i++) {
            alpha[i] = Math.min(Math.max(0, alpha[i]), 1);
        }
    }

    @Override
    public void keyPress(int k, char c) {
    }

    @Override
    public void keyRelease(int k, char c) {
    }

    @Override
    public void mouseClicked(int button) {
    }

    @Override
    public void mouseReleased(int button) {
    }

    @Override
    public void render() {
        RenderUtil2D.circleOutline(x, y, sizeR+1, ColorUtil.transparency(0xFF000000, alpha[1]), 2f);
        RenderUtil2D.circleOutline(x, y, sizeR - width - 1, ColorUtil.transparency(0xFF000000, alpha[1]), 2f);

        for(int i = 0; i < list.size(); i++) {
            RenderUtil2D.donutSeg(x, y, sizeR + (settingSlider[i] * settingSliderWidth)+2, sizeR, i, list.size(), settingsPadding - ((1 / (360D / list.size())) * 5.5), ColorUtil.transparency(0xFF000000, alpha[1]));
        }

        RenderUtil2D.donut(x, y, sizeR, sizeR - width, ColorUtil.transparency(0x80FFFFFF, alpha[1] / 2));
        RenderUtil2D.donutSeg(x, y, sizeR, sizeR - width, selectedR, list.size(), segPadding, ColorUtil.transparency(ColorUtil.intFromHex(Option.get(Settings.options, "guicolor").STRING()), alpha[0] * alpha[1]));
        RenderUtil2D.circle(x, y, sizeR - width, ColorUtil.transparency(0xFF000000, alpha[1] / 2));

        double iconSize = 20;
        for(int i = 0; i < list.size(); i++) {
            RenderUtil2D.donutSeg(x, y, sizeR + (settingSlider[i] * settingSliderWidth), sizeR, i, list.size(), settingsPadding-((1/(360D/list.size()))*5), ColorUtil.transparency(ColorUtil.intFromHex(Option.get(Settings.options, "guicolor").STRING()), alpha[1]));

            double ang = ((360D/list.size())*(i+0.5));
            double rad = (sizeR + (settingSlider[i] * (settingSliderWidth/2)));
            double iX = x + Math.cos(ang * Math.PI / 180D) * rad;
            double iY = y + Math.sin(ang * Math.PI / 180D) * rad;

            Helper.mc().getTextureManager().bindTexture(settingsIcon);
            RenderUtil2D.texturedRect(iX - (iconSize / 2), iY - (iconSize / 2), iX + (iconSize / 2), iY + (iconSize / 2), ColorUtil.transparency(0xFFFFFFFF, settingSlider[i] * alpha[1]));
        }
    }
}
