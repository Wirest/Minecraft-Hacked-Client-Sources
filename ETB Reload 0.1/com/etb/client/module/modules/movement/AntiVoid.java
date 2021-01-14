package com.etb.client.module.modules.movement;

import java.awt.Color;

import org.greenrobot.eventbus.Subscribe;

import com.etb.client.event.events.player.UpdateEvent;
import com.etb.client.module.Module;

import net.minecraft.block.BlockAir;
import net.minecraft.util.BlockPos;

/**
 * made by Xen for ETB Reloaded
 *
 * @since 6/10/2019
 **/
public class AntiVoid extends Module {

    public AntiVoid() {
        super("AntiVoid", Category.WORLD, new Color(223,233,233).getRGB());
        setDescription("Flags you back when falling into the void");
        setRenderlabel("Anti Void");
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        boolean blockUnderneath = false;
        for (int i = 0; i < mc.thePlayer.posY + 2; i++) {
            BlockPos pos = new BlockPos(mc.thePlayer.posX, i, mc.thePlayer.posZ);
            if (mc.theWorld.getBlockState(pos).getBlock() instanceof BlockAir)
                continue;
            blockUnderneath = true;
        }

        if (blockUnderneath)
            return;

        if (mc.thePlayer.fallDistance < 2)
            return;

        if (!mc.thePlayer.onGround && !mc.thePlayer.isCollidedVertically) {
            mc.thePlayer.motionY += 0.07;
        }

    }
}
