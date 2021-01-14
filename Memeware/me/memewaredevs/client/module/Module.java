/*
 * Decompiled with CFR 0.145.
 *
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 */
package me.memewaredevs.client.module;

import me.memewaredevs.client.Memeware;
import me.memewaredevs.client.event.EventAPI;
import me.memewaredevs.client.module.hud.GameHud;
import me.memewaredevs.client.option.*;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

@SuppressWarnings("all")
public abstract class Module {
    protected static final Minecraft mc = Minecraft.getMinecraft();
    private boolean state = false;
    private final String name;
    private int key;
    private final Category category;

    public Module(final String name, final int key, final Category category) {
        this.name = name;
        this.key = key;
        this.category = category;
    }

    public final static Module getInstance(final Class<? extends Module> clazz) {
        return Memeware.INSTANCE.getModuleManager().get(clazz);
    }

    public final void addModes(final String... modes) {
        Memeware.INSTANCE.getSettingsManager().addSetting(new StringOption(this, "Mode", modes));
    }

    public final void addDouble(final String name, final double dval, final double min, final double max) {
        Memeware.INSTANCE.getSettingsManager().addSetting(new NumberOption(this, name, dval, min, max, false));
    }

    public final void addDouble(final String name, String mode, final double dval, final double min, final double max) {
        Memeware.INSTANCE.getSettingsManager().addSetting(new NumberOption(this, name, mode, dval, min, max, false));
    }

    public final void addInt(final String name, String mode, final double dval, final double min, final double max) {
        Memeware.INSTANCE.getSettingsManager().addSetting(new NumberOption(this, name, mode, dval, min, max, true));
    }

    public final void addBoolean(final String name, final boolean value) {
        Memeware.INSTANCE.getSettingsManager().addSetting(new BooleanOption(this, name, value));
    }

    public final void addInt(final String name, final double dval, final double min, final double max) {
        Memeware.INSTANCE.getSettingsManager().addSetting(new NumberOption(this, name, dval, min, max, true));
    }

    public final Double getDouble(final String string) {
        return (Double) Memeware.INSTANCE.getSettingsManager().getSettingByMod(this.getClass(), string).getValue();
    }

    public final Double getDouble(final String mode, final String string) {
        return (Double) Memeware.INSTANCE.getSettingsManager().getSettingByMod(this.getClass(), mode, string)
                .getValue();
    }

    public final boolean getBool(final String string) {
        return (Boolean) Memeware.INSTANCE.getSettingsManager().getSettingByMod(this.getClass(), string).getValue();
    }

    public final String getMode(boolean format) {
        try {
            Option<?> option = Memeware.INSTANCE.getSettingsManager().getSettingByMod(this.getClass(), "Mode");
            if (option != null) {
                String str = option.getValue().toString();
                if (format) {
                    str = "\2477 - " + str;
                }
                return str;
            }
        } catch (Exception e) {

        }
        return "";
    }

    public final boolean isMode(final String mode) {
        return getMode(false).equalsIgnoreCase(mode);
    }

    public final void toggle() {
        this.state = !this.state;
        if (this.state) {
            if (this.mc.thePlayer != null) {
                this.onEnable();
            }
            EventAPI.put(this);
        } else {
            EventAPI.remove(this);
            if (this.mc.thePlayer != null) {
                this.onDisable();
            }
        }
        GameHud.sortingUpdate = true;
    }

    public final boolean isToggled() {
        return this.state;
    }

    public void onEnable() {

    }

    public void onDisable() {

    }

    public Category getCategory() {
        return this.category;
    }

    public String getName() {
        return this.name;
    }

    public String getModuleDisplayname() {
        return this.name;
    }

    public void setKeyBind(final int keyCode) {
        this.key = keyCode;
    }

    public int getKey() {
        return this.key;
    }

    public void addBlockSelection(String name, ArrayList<Block> list) {
        Memeware.INSTANCE.getSettingsManager().addSetting(new BlockSelectionSetting(this, name, list));
    }

    public enum Category {
        COMBAT, MOVEMENT, PLAYER, MISC, RENDER, EXPLOIT;

        public String getName() {
            return StringUtils.capitalize(this.name().toLowerCase());
        }

    }

}
