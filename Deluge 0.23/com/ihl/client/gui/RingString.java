package com.ihl.client.gui;

import com.ihl.client.module.Module;
import com.ihl.client.module.option.Option;
import com.ihl.client.module.option.ValueString;
import com.ihl.client.util.ColorUtil;
import com.ihl.client.util.RenderUtil;
import com.ihl.client.util.RenderUtil2D;
import com.ihl.client.util.part.Settings;
import net.minecraft.util.ChatAllowedCharacters;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Keyboard;

import java.util.Arrays;
import java.util.List;

public class RingString extends Ring {

    private Module module;
    private Option option;

    public RingString(Module module, Option option, List<String> list) {
        super(list);
        this.module = module;
        this.option = option;
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
    public void keyPress(int k, char c) {
        ValueString valueString = (ValueString) option.value;
        String text = option.STRING();

        if (k == Keyboard.KEY_BACK) {
            if (text.length() > 0) {
                text = text.substring(0, text.length() - 1);
            }
        } else if (ChatAllowedCharacters.isAllowedCharacter(c)){
            text+= c;
        }

        valueString.value = text;
    }

    @Override
    public void mouseClicked(int button) {
        if (!mouseOver || button != 0) {
            List<String> list = Arrays.asList(module.options.keySet().toArray(new String[0]));
            if (list != null && !list.isEmpty()) {
                Gui.changeRing(new RingOption(list, module));
            }
        }
    }

    @Override
    public void render() {
        super.render();

        RenderUtil2D.string(RenderUtil.fontLarge[1], option.STRING()+((System.currentTimeMillis()%1000) < 500 ? "_" : "  "), x, y, ColorUtil.transparency(0xFFFFFFFF, alpha[1]), 0, 0, false);

        for (int i = 0; i < list.size(); i++) {
            double iX = x + Math.cos(((360f / (list.size() * 2)) * ((i + 0.5) * 2)) * Math.PI / 180) * (sizeR - (width / 2));
            double iY = y + Math.sin(((360f / (list.size() * 2)) * ((i + 0.5) * 2)) * Math.PI / 180) * (sizeR - (width / 2));
            iY-= RenderUtil.fontTiny[0].getHeight()/2;

            RenderUtil2D.string(RenderUtil.fontSmall[1], StringUtils.capitalize(""+list.get(i)), iX, iY, ColorUtil.transparency(selected == i && mouseOver ? 0xFFFFFFFF : ColorUtil.intFromHex(Option.get(Settings.options, "guicolor").STRING()), alpha[1]), 0, 0, false);
        }
    }

}
