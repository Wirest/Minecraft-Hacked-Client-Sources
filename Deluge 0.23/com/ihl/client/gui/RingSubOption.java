package com.ihl.client.gui;

import com.ihl.client.Helper;
import com.ihl.client.module.Module;
import com.ihl.client.module.option.Option;
import com.ihl.client.module.option.ValueChoice;
import com.ihl.client.module.option.ValueDouble;
import com.ihl.client.module.option.ValueList;
import com.ihl.client.util.ChatUtil;
import com.ihl.client.util.ColorUtil;
import com.ihl.client.util.RenderUtil;
import com.ihl.client.util.RenderUtil2D;
import com.ihl.client.util.part.Settings;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

public class RingSubOption extends Ring {

    private Module module;
    private Option option;

    public RingSubOption(List<String> list, Module module, Option option) {
        super(list);
        this.module = module;
        this.option = option;

        for(int i = 0; i < list.size(); i++) {
            String key = list.get(i);
            Option subOption = Option.get(option.options, key);
            hasSettings[i] = subOption.options != null && !subOption.options.isEmpty();
        }
    }

    @Override
    public void tick() {
        if (list.isEmpty()) {
            List<String> list = Arrays.asList(module.options.keySet().toArray(new String[0]));
            if (list != null && !list.isEmpty()) {
                Gui.changeRing(new RingOption(list, module));
                return;
            }
        }
        super.tick();
    }

    @Override
    public void mouseClicked(int button) {
        if (mouseOver && button == 0) {
            String key = (String) list.get(selected);
            Option subOption = Option.get(option.options, key);
            if (mouseOverSettings && hasSettings[selected]) {
                if (subOption.options != null && !subOption.options.isEmpty()) {
                    List<String> list = Arrays.asList(subOption.options.keySet().toArray(new String[0]));
                    if (list != null && !list.isEmpty()) {
                        Gui.changeRing(new RingSubOption(list, module, subOption));
                    }
                }
            } else {
                switch(subOption.type) {
                    case BOOLEAN:
                        subOption.value.value = !((boolean) subOption.value.value);
                        break;
                    case CHOICE:
                        Gui.changeRing(new RingChoice(module, subOption, Arrays.asList(((ValueChoice) subOption.value).list)));
                        break;
                    case KEYBIND:
                        Gui.changeRing(new RingKeybind(module, subOption, Arrays.asList(subOption.name)));
                        break;
                    case LIST:
                        Gui.changeRing(new RingList(module, subOption, (List<String>)((ValueList) subOption.value).value));
                        break;
                    case NUMBER:
                        Gui.changeRing(new RingSlider(module, subOption, Arrays.asList(subOption.name)));
                        break;
                    case OTHER:
                        ChatUtil.send("Dunno why you're editting the src :(");
                        break;
                    case STRING:
                        Gui.changeRing(new RingString(module, subOption, Arrays.asList(option.name)));
                        break;
                }
            }
        } else {
            List<String> list = Arrays.asList(module.options.keySet().toArray(new String[0]));
            if (list != null && !list.isEmpty()) {
                Gui.changeRing(new RingOption(list, module));
            }
        }
    }

    @Override
    public void render() {
        super.render();
        double iconSize = 40;

        for (int i = 0; i < list.size(); i++) {
            String key = (String) list.get(i);
            Option subOption = Option.get(option.options, key);

            double iX = x + Math.cos(((360f / (list.size() * 2)) * ((i + 0.5) * 2)) * Math.PI / 180) * (sizeR - (width / 2));
            double iY = y + Math.sin(((360f / (list.size() * 2)) * ((i + 0.5) * 2)) * Math.PI / 180) * (sizeR - (width / 2));
            iY-= RenderUtil.fontTiny[0].getHeight()/2;

            switch(subOption.type) {
                case BOOLEAN:
                    if (subOption.BOOLEAN()) {
                        RenderUtil2D.donutSeg(x, y, sizeR + (settingSlider[i] * settingSliderWidth) + 20, sizeR + (settingSlider[i] * settingSliderWidth) + 10, i, list.size(), optionPadding, ColorUtil.transparency(subOption.color, alpha[1]));
                    }
                    break;
                case CHOICE:
                    break;
                case KEYBIND:
                    break;
                case LIST:
                    break;
                case NUMBER:
                    ValueDouble val = (ValueDouble)subOption.value;
                    double frac = (1/val.limit[1])*((double)val.value-val.limit[0]);
                    RenderUtil2D.donutSegFrac(x, y, sizeR + (settingSlider[i] * settingSliderWidth) + 20, sizeR + (settingSlider[i] * settingSliderWidth) + 10, i, list.size(), frac, optionPadding, ColorUtil.transparency(subOption.color, alpha[1]));
                    break;
                case OTHER:
                    break;
                case STRING:
                    break;
            }

            Helper.mc().getTextureManager().bindTexture(subOption.icon);
            RenderUtil2D.texturedRect(iX - (iconSize / 2), iY - (iconSize / 2), iX + (iconSize / 2), iY + (iconSize / 2), ColorUtil.transparency(selected == i && mouseOver ? 0xFFFFFFFF : ColorUtil.intFromHex(Option.get(Settings.options, "guicolor").STRING()), alpha[1]));
            RenderUtil2D.string(RenderUtil.fontTiny[1], subOption.name, iX, iY + (iconSize / 2), ColorUtil.transparency(selected == i && mouseOver ? 0xFFFFFFFF : ColorUtil.intFromHex(Option.get(Settings.options, "guicolor").STRING()), alpha[1]), 0, 0, false);
        }

        if (selected != -1) {
            Option subOption = Option.get(option.options, (String) list.get((int) Math.floor(selected)));
            RenderUtil2D.string(RenderUtil.fontLarge[1], subOption.name, x, y, ColorUtil.transparency(0xFFFFFFFF, alpha[0] * alpha[1]), 0, 0, false);
            RenderUtil2D.string(RenderUtil.fontTiny[1], subOption.desc, x, y + (RenderUtil.fontLarge[1].getHeight() / 2) + 2, ColorUtil.transparency(0xFFCCCCCC, alpha[0] * alpha[1]), 0, 1, false);
            RenderUtil2D.string(RenderUtil.fontSmall[1], StringUtils.capitalize(subOption.STRING()), x, y + (RenderUtil.fontLarge[1].getHeight() / 2) + RenderUtil.fontTiny[1].getHeight() + 2, ColorUtil.transparency(0xFFFFFFFF, alpha[0] * alpha[1]), 0, 1, false);
        }
    }
}
