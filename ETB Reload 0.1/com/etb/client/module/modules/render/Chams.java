package com.etb.client.module.modules.render;

import com.etb.client.module.Module;
import com.etb.client.utils.value.impl.BooleanValue;
import com.etb.client.utils.value.impl.NumberValue;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;


import java.awt.*;

public class Chams extends Module {
    public NumberValue<Float> visiblered = new NumberValue("VisibleRed", 1.0F, 0.001F, 1.0F, 0.001F);
    public NumberValue<Float> visiblegreen = new NumberValue("VisibleGreen", 0.0F, 0.001F, 1.0F, 0.001F);
    public NumberValue<Float> visibleblue = new NumberValue("VisibleBlue", 0.0F, 0.001F, 1.0F, 0.001F);
    public NumberValue<Float> hiddenred = new NumberValue("HiddenRed", 1.0F, 0.001F, 1.0F, 0.001F);
    public NumberValue<Float> hiddengreen = new NumberValue("HiddenGreen", 0.0F, 0.001F, 1.0F, 0.001F);
    public NumberValue<Float> hiddenblue = new NumberValue("HiddenBlue", 1.0F, 0.001F, 1.0F, 0.001F);
    public NumberValue<Float> alpha = new NumberValue("Alpha", 1.0F, 0.001F, 1.0F, 0.001F);
    private BooleanValue players = new BooleanValue("Players", true);
    private BooleanValue animals = new BooleanValue("Animals", true);
    private BooleanValue mobs = new BooleanValue("Mobs", false);
    public BooleanValue invisibles = new BooleanValue("Invisibles", false);
    private BooleanValue passives = new BooleanValue("Passives", true);
    public BooleanValue colored = new BooleanValue("Colored", false);
    public BooleanValue hands = new BooleanValue("Hands", false);
    public BooleanValue rainbow = new BooleanValue("Raindow", false);

    public Chams() {
        super("Chams", Category.RENDER, new Color(0xA54DFF).getRGB());
        addValues(visiblered, visiblegreen, visibleblue, hiddenred, hiddengreen, hiddenblue, alpha, players, animals, mobs, invisibles, passives, hands, colored, rainbow);
        setDescription("Colored wallhacks");
    }

    public boolean isValid(EntityLivingBase entity) {
        return isValidType(entity) && entity.isEntityAlive() && (!entity.isInvisible() || invisibles.isEnabled());
    }

    private boolean isValidType(EntityLivingBase entity) {
        return (players.isEnabled() && entity instanceof EntityPlayer) || (mobs.isEnabled() && (entity instanceof EntityMob || entity instanceof EntitySlime) || (passives.isEnabled() && (entity instanceof EntityVillager || entity instanceof EntityGolem)) || (animals.isEnabled() && entity instanceof EntityAnimal));
    }

    @Override
    public boolean hasSubscribers() {
        return false;
    }
}
