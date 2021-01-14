package cedo.modules.movement;

import cedo.events.Event;
import cedo.events.listeners.EventMotion;
import cedo.modules.Module;
import cedo.util.movement.MovementUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.BlockPos;
import org.lwjgl.input.Keyboard;

public class FastStairs extends Module {

    public FastStairs() {
        super("FastStairs", Keyboard.KEY_NONE, Category.MOVEMENT);
    }

    public void onEvent(Event e) {
        if (e instanceof EventMotion && e.isPre()) {
            EntityPlayerSP player = mc.thePlayer;
            Block below = mc.theWorld.getBlockState(new BlockPos(player.posX, player.boundingBox.minY, player.posZ)).getBlock();
            Block downBlock = mc.theWorld.getBlockState(new BlockPos(player.posX, player.boundingBox.minY, player.posZ).down()).getBlock();
            boolean down = player.posY < player.chasingPosY || player.fallDistance > 0;

            if (!player.onGround || !MovementUtil.isMoving())
                return;


            if (!down && below instanceof BlockStairs)
                mc.thePlayer.jump();

            if (down && downBlock instanceof BlockStairs)
                mc.thePlayer.motionY = -1;

        }
    }
}
