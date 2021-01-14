package cedo.modules.movement;

import cedo.events.Event;
import cedo.events.listeners.EventMotion;
import cedo.events.listeners.EventStep;
import cedo.modules.Module;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.util.BlockPos;
import org.lwjgl.input.Keyboard;

public class Step extends Module {
    private double preY;


    public Step() {
        super("Step", Keyboard.KEY_U, Category.MOVEMENT);
        addSettings();
    }

    public void onEnable() {
        super.onEnable();
    }

    public void onEvent(Event e) {
        if (e instanceof EventStep) {
            EventStep event = (EventStep) e;

            if (event.getEntity() != mc.thePlayer) {
                return;
            }

            Block below = mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.boundingBox.minY, mc.thePlayer.posZ)).getBlock();
            Block downBlock = mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.boundingBox.minY, mc.thePlayer.posZ).down()).getBlock();

            boolean canStep = !(below instanceof BlockStairs) && !(downBlock instanceof BlockStairs) && !mc.gameSettings.keyBindJump.isKeyDown() && mc.thePlayer.isCollidedVertically && mc.thePlayer.onGround && mc.thePlayer.motionY < 0.0 && !mc.thePlayer.movementInput.jump;

            if (event.isPre()) {
                preY = mc.thePlayer.posY;
                if (canStep) {
                    event.setStepHeight(1);

                } else {
                    event.setStepHeight(0.5f);
                }
            } else {
                if (event.getStepHeight() != 1) {
                    return;
                }

                double offset = mc.thePlayer.boundingBox.minY - preY;
                if (offset > 0.6) {
                    double[] offsets = {0.41999998688698, 0.7531999805212};

                    if (canStep)
                        for (double tp : offsets) {
                            mc.thePlayer.sendQueue.sendPacketNoEvent(new C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + tp, mc.thePlayer.posZ, false));
                        }
                }
            }


        }
        if (e instanceof EventMotion && e.isPre()) {
            mc.thePlayer.stepHeight = 1;

        }
    }

    public void onDisable() {
        mc.thePlayer.stepHeight = 0.5f;
        super.onDisable();
    }
}
