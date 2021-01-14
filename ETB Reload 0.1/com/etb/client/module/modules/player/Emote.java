package com.etb.client.module.modules.player;

import java.awt.Color;

import com.etb.client.module.Module;
import com.etb.client.utils.value.impl.EnumValue;


public class Emote extends Module {
    public int heilY;
    public EnumValue<Mode> mode = new EnumValue("Mode",Mode.DAB);
    public Emote() {
        super("Emote", Category.PLAYER, new Color(255, 0, 0).getRGB());
        setDescription("Cool emotes");
        addValues(mode);
    }

    @Override
    public void onEnable() {
        heilY = 0;
    }

    @Override
    public boolean hasSubscribers() {
        return false;
    }

    public enum Mode {
        DAB,NAZISALUTE
    }
}
