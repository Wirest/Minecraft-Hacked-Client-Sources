package com.ihl.client.gui;

import com.ihl.client.Helper;
import com.ihl.client.module.Category;
import com.ihl.client.module.Module;
import com.ihl.client.module.option.Option;
import com.ihl.client.util.ColorUtil;
import com.ihl.client.util.RenderUtil;
import com.ihl.client.util.RenderUtil2D;
import com.ihl.client.util.part.Settings;

import java.util.Arrays;
import java.util.List;

public class RingModule extends Ring {

    public RingModule(List<String> list) {
        super(list);
        for(int i = 0; i < list.size(); i++) {
            String key = list.get(i);
            Module module = Module.get(key);
            hasSettings[i] = module.options != null && !module.options.isEmpty();
        }
    }

    @Override
    public void tick() {
        if (list.isEmpty()) {
            Gui.changeRing(new RingCategory(Arrays.asList(Category.values())));
            return;
        }
        super.tick();
    }

    @Override
    public void mouseClicked(int button) {
        if (mouseOver && button == 0) {
            String key = (String) list.get(selected);
            Module module = Module.get(key);
            if (mouseOverSettings) {
                List<String> list = Arrays.asList(module.options.keySet().toArray(new String[0]));
                if (list != null && !list.isEmpty()) {
                    Gui.changeRing(new RingOption(list, module));
                }
            } else {
                module.toggle();
            }
        } else {
            Gui.changeRing(new RingCategory(Arrays.asList(Category.values())));
        }
    }

    @Override
    public void render() {
        super.render();
        double iconSize = 40;

        for (int i = 0; i < list.size(); i++) {
            String key = (String) list.get(i);
            Module module = Module.get(key);

            double ang = ((360f / (list.size() * 2)) * ((i + 0.5) * 2)) * Math.PI / 180;
            double inc = (sizeR - (width / 2));
            double iX = x + Math.cos(ang) * inc;
            double iY = y + Math.sin(ang) * inc;
            iY-= RenderUtil.fontTiny[0].getHeight()/2;

            if (module.active) {
                RenderUtil2D.donutSeg(x, y, sizeR + (settingSlider[i] * settingSliderWidth) + 20, sizeR + (settingSlider[i] * settingSliderWidth) + 10, i, list.size(), modulePadding, ColorUtil.transparency(module.color, alpha[1]));
            }

            Helper.mc().getTextureManager().bindTexture(module.icon);
            RenderUtil2D.texturedRect(iX - (iconSize / 2), iY - (iconSize / 2), iX + (iconSize / 2), iY + (iconSize / 2), ColorUtil.transparency(selected == i && mouseOver ? 0xFFFFFFFF : ColorUtil.intFromHex(Option.get(Settings.options, "guicolor").STRING()), alpha[1]));
            RenderUtil2D.string(RenderUtil.fontTiny[1], module.name, iX, iY + (iconSize / 2), ColorUtil.transparency(selected == i && mouseOver ? 0xFFFFFFFF : ColorUtil.intFromHex(Option.get(Settings.options, "guicolor").STRING()), alpha[1]), 0, 0, false);
        }

        if (selected != -1) {
            Module module = Module.get((String) list.get((int) Math.floor(selected)));
            RenderUtil2D.string(RenderUtil.fontLarge[1], module.name, x, y, ColorUtil.transparency(0xFFFFFFFF, alpha[0] * alpha[1]), 0, 0, false);
            RenderUtil2D.string(RenderUtil.fontTiny[1], module.desc, x, y + (RenderUtil.fontLarge[1].getHeight() / 2) + 2, ColorUtil.transparency(0xFFCCCCCC, alpha[0] * alpha[1]), 0, 1, false);
        }
    }

}
