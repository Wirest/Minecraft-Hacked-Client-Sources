package com.etb.client.module.modules.world;

import java.awt.Color;

import org.greenrobot.eventbus.Subscribe;

import com.etb.client.event.events.player.UpdateEvent;
import com.etb.client.module.Module;
import com.etb.client.utils.value.impl.EnumValue;

import net.minecraft.util.EnumParticleTypes;

public class Trails extends Module {
    public EnumValue<Mode> mode = new EnumValue<>("Type", Mode.HEART);

    public Trails() {
        super("Trails", Category.WORLD, new Color(255, 195, 215, 255).getRGB());
        setDescription("Walk behind you");
        addValues(mode);
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        if (mc.thePlayer.isMoving()) switch (mode.getValue()) {
            case HEART:
                mc.effectRenderer.emitParticleAtEntity(mc.thePlayer, EnumParticleTypes.HEART);
                break;
            case LAVA:
                mc.effectRenderer.emitParticleAtEntity(mc.thePlayer, EnumParticleTypes.LAVA);
                break;
            case SMOKE:
                mc.effectRenderer.emitParticleAtEntity(mc.thePlayer, EnumParticleTypes.REDSTONE);
                break;
            case CLOUD:
                mc.effectRenderer.emitParticleAtEntity(mc.thePlayer, EnumParticleTypes.CLOUD);
                break;
            case FLAME:
                mc.effectRenderer.emitParticleAtEntity(mc.thePlayer, EnumParticleTypes.FLAME);
                break;
            case SLIME:
                mc.effectRenderer.emitParticleAtEntity(mc.thePlayer, EnumParticleTypes.SLIME);
                break;
            case WATER:
                mc.effectRenderer.emitParticleAtEntity(mc.thePlayer, EnumParticleTypes.WATER_SPLASH);
                break;
            case FIREWORK:
                mc.effectRenderer.emitParticleAtEntity(mc.thePlayer, EnumParticleTypes.FIREWORKS_SPARK);
                break;
        }
    }

    public enum Mode {
        SMOKE, HEART, FIREWORK, FLAME, CLOUD, WATER, LAVA, SLIME
    }
}
