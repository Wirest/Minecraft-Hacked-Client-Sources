package me.Corbis.Execution.module.implementations;

import me.Corbis.Execution.event.EventTarget;
import me.Corbis.Execution.event.events.EventMotionUpdate;
import me.Corbis.Execution.module.Category;
import me.Corbis.Execution.module.Module;
import net.minecraft.block.BlockAir;
import net.minecraft.util.BlockPos;
import org.lwjgl.input.Keyboard;

public class Parkour extends Module {
    public Parkour(){
        super("Parkour", Keyboard.KEY_NONE, Category.MOVEMENT);
    }

    @EventTarget
    public void onPre(EventMotionUpdate event){
        BlockPos pos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 0.5001, mc.thePlayer.posZ);
        if(mc.theWorld.getBlock(pos.getX(), pos.getY(), pos.getZ()) instanceof BlockAir){
            if(mc.thePlayer.onGround)
                mc.thePlayer.motionY = 0.42d;
        }
    }
}
