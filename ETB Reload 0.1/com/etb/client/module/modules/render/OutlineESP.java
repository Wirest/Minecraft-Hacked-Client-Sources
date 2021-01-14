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

/**
 * made by oHare for oHareWare
 *
 * @since 6/24/2019
 **/
public class OutlineESP extends Module {
    public NumberValue<Float> red = new NumberValue("Red", 1.0F, 0.001F, 1.0F, 0.001F);
    public NumberValue<Float> green = new NumberValue("Green", 1.0F, 0.001F, 1.0F, 0.001F);
    public NumberValue<Float> blue = new NumberValue("Blue", 1.0F, 0.001F, 1.0F, 0.001F);
    public NumberValue<Float> alpha = new NumberValue("Alpha", 1.0F, 0.001F, 1.0F, 0.001F);
    public NumberValue<Float> width = new NumberValue("Width",3.0F,1.0F,10.0F,0.01F);
    private BooleanValue players = new BooleanValue("Players", true);
    private BooleanValue animals = new BooleanValue("Animals", true);
    private BooleanValue mobs = new BooleanValue("Mobs", false);
    public BooleanValue invisibles = new BooleanValue("Invisibles", false);
    private BooleanValue passives = new BooleanValue("Passives", true);
    public BooleanValue rainbow = new BooleanValue("Raindow", false);
    public OutlineESP() {
        super("OutlineESP", Category.RENDER, new Color(0xA54DFF).getRGB());
        addValues(red, green, blue, alpha,width, players, animals, mobs, invisibles, passives, rainbow);
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
