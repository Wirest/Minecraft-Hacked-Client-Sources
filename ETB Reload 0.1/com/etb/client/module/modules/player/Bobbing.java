package com.etb.client.module.modules.player;

import com.etb.client.event.events.player.UpdateEvent;
import com.etb.client.module.Module;
import com.etb.client.utils.value.impl.EnumValue;
import com.etb.client.utils.value.impl.NumberValue;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.util.BlockPos;
import org.greenrobot.eventbus.Subscribe;

import java.awt.*;

public class Bobbing extends Module {
    private NumberValue<Double> bob = new NumberValue("Amount", 1.0D, 0.1D, 5.0D, 0.1D);

    public Bobbing() {
        super("Bobbing", Category.PLAYER, new Color(255, 144, 177).getRGB());
        setDescription("Bob");
        addValues(bob);
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        if (mc.thePlayer.onGround) {
            mc.thePlayer.cameraYaw = ((float) (0.09090908616781235D * (bob.getValue())));
        }
    }
}