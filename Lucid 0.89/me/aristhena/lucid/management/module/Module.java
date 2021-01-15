/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  org.lwjgl.input.Keyboard
 */
package me.aristhena.lucid.management.module;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import me.aristhena.lucid.eventapi.EventManager;
import me.aristhena.lucid.management.module.Category;
import me.aristhena.lucid.management.module.ModuleManager;
import me.aristhena.lucid.management.option.OptionManager;
import me.aristhena.lucid.management.value.Value;
import me.aristhena.lucid.management.value.ValueManager;
import me.aristhena.lucid.ui.clickgui.Option;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

public class Module {
    protected Minecraft mc = Minecraft.getMinecraft();
    private static final int MIN_HEX = -865847;
    private static final int MAX_HEX = -3542326;
    public String realName;
    public String name;
    public boolean enabled;
    public int keyBind;
    public boolean shown;
    public int color;
    public Category category;
    public String suffix;

    public void toggle() {
        if (this.enabled) {
            this.onDisable();
        } else {
            this.onEnable();
        }
        ModuleManager.save();
    }

    public void onEnable() {
        Random randomService = new Random();
        StringBuilder sb = new StringBuilder();
        sb.append("0x");
        while (sb.length() < 10) {
            sb.append(Integer.toHexString(randomService.nextInt()));
        }
        sb.setLength(8);
        this.color = Integer.decode(sb.toString());
        this.enabled = true;
        EventManager.register(this);
    }

    public void onDisable() {
        this.enabled = false;
        EventManager.unregister(this);
    }

    public void preInit() {
    }

    public List<me.aristhena.lucid.management.option.Option> getOptions() {
        ArrayList<me.aristhena.lucid.management.option.Option> list = new ArrayList<me.aristhena.lucid.management.option.Option>();
        for (me.aristhena.lucid.management.option.Option option : OptionManager.optionList) {
            if (option.mod != this) continue;
            list.add(option);
        }
        return list;
    }

    private int getFadeHex(int hex1, int hex2, double ratio) {
        int r = hex1 >> 16;
        int g = hex1 >> 8 & 255;
        int b = hex1 & 255;
        r = (int)((double)r + (double)((hex2 >> 16) - r) * ratio);
        g = (int)((double)g + (double)((hex2 >> 8 & 255) - g) * ratio);
        b = (int)((double)b + (double)((hex2 & 255) - b) * ratio);
        return r << 16 | g << 8 | b;
    }

    public List<Option> getConvertedOptions() {
        ArrayList<Option> optionList = new ArrayList<Option>();
        for (me.aristhena.lucid.management.option.Option option : OptionManager.optionList) {
            if (!option.mod.equals(this)) continue;
            Option boolOption = new Option(this, Option.Type.bool, option.name, option.value, new double[]{0.0, 0.0}, 0.0);
            optionList.add(boolOption);
        }
        for (Value value : ValueManager.valueList) {
            if (!value.mod.equals(this)) continue;
            Option valOption = new Option(this, Option.Type.floa, value.name, Float.valueOf((float)value.value), new double[]{value.min, value.max}, value.increment);
            optionList.add(valOption);
        }
        String keyName = "None";
        if (this.keyBind > 0) {
            keyName = Keyboard.getKeyName((int)this.keyBind);
        }
        Option bindOption = new Option(this, Option.Type.keyb, "Bind", keyName, new double[]{0.0, 0.0}, 0.0);
        optionList.add(bindOption);
        return optionList;
    }
}

