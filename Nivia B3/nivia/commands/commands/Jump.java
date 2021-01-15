package nivia.commands.commands;


import net.minecraft.block.BlockIce;
import net.minecraft.block.BlockPackedIce;
import nivia.commands.Command;
import nivia.events.EventTarget;
import nivia.events.events.EventTick;
import nivia.managers.EventManager;
import nivia.utils.Logger;
import nivia.utils.utils.Timer;

public class Jump extends Command {

    private Timer timer = new Timer();
    public Jump() {
        super("Jump", "Jumps to where you are looking.", null, false);
    }

    @Override
    public void execute(String commandName, String[] arguments) {
        float 	direction = mc.thePlayer.rotationYaw +
                (mc.thePlayer.moveForward < 0 ? 180 : 0) +
                (mc.thePlayer.moveStrafing > 0 ? (-90 * (mc.thePlayer.moveForward < 0 ? -0.5F : (mc.thePlayer.moveForward > 0 ? 0.5F : 1))) : 0) -
                (mc.thePlayer.moveStrafing < 0 ? (-90 * (mc.thePlayer.moveForward < 0 ? -0.5F : (mc.thePlayer.moveForward > 0 ? 0.5F : 1))) : 0),
                xDir = (float) Math.cos((direction + 90) * Math.PI / 180),
                zDir = (float) Math.sin((direction + 90) * Math.PI / 180);

        mc.thePlayer.setSprinting(true);
        mc.thePlayer.onGround = true;
        if(mc.thePlayer.isCollidedVertically) {
            mc.thePlayer.motionY = 0.425;
            if(nivia.utils.utils.BlockUtils.getBlockUnderPlayer(mc.thePlayer, mc.thePlayer.posY) instanceof BlockPackedIce || nivia.utils.utils.BlockUtils.getBlockUnderPlayer(mc.thePlayer, mc.thePlayer.posY) instanceof BlockIce) {
                mc.thePlayer.motionX = xDir * 1.7F;
                mc.thePlayer.motionZ = zDir * 1.7F;
            }
            //mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ - 0.2);
        } else {
            mc.thePlayer.motionX = xDir * .36;
            mc.thePlayer.motionZ = zDir * .36;
        }
        EventManager.register(this, EventTick.class);
        Logger.logChat("You should now jump!");
    }
    @EventTarget
    public void onTick(EventTick event) {
        float 	direction = mc.thePlayer.rotationYaw +
                (mc.thePlayer.moveForward < 0 ? 180 : 0) +
                (mc.thePlayer.moveStrafing > 0 ? (-90 * (mc.thePlayer.moveForward < 0 ? -0.5F : (mc.thePlayer.moveForward > 0 ? 0.5F : 1))) : 0) -
                (mc.thePlayer.moveStrafing < 0 ? (-90 * (mc.thePlayer.moveForward < 0 ? -0.5F : (mc.thePlayer.moveForward > 0 ? 0.5F : 1))) : 0),
                xDir = (float) Math.cos((direction + 90) * Math.PI / 180),
                zDir = (float) Math.sin((direction + 90) * Math.PI / 180);

        mc.thePlayer.setSprinting(true);
        mc.thePlayer.onGround = true;
        if(mc.thePlayer.isCollidedVertically) {
            //mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ - 0.2);
        } else {
            mc.thePlayer.motionX = xDir * 0.0;
            mc.thePlayer.motionZ = zDir * 0.0;
        }
        if(mc.thePlayer.isCollidedVertically) {
            mc.thePlayer.setVelocity(0, 0, 0);
            EventManager.unregister(this, EventTick.class);
        }
    }
}