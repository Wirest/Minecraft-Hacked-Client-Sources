package me.rigamortis.faurax.commands;

import me.cupboard.command.*;
import net.minecraft.client.*;
import me.cupboard.command.argument.*;

public class CommandForward extends Command
{
    private Minecraft mc;
    
    public CommandForward() {
        super("forward", new String[0]);
        this.mc = Minecraft.getMinecraft();
    }
    
    @Argument
    protected String forkward() {
        final double[] forward = { 0.8, 1.2, 1.8, 2.6, 3.2, 4.1, 4.9, 5.6, 6.2, 7.0 };
        for (int i = 0; i < forward.length; ++i) {
            float dir = this.mc.thePlayer.rotationYaw;
            if (this.mc.thePlayer.moveForward < 0.0f) {
                dir += 180.0f;
            }
            if (this.mc.thePlayer.moveStrafing > 0.0f) {
                dir -= 90.0f * ((this.mc.thePlayer.moveForward < 0.0f) ? -0.5f : ((this.mc.thePlayer.moveForward > 0.0f) ? 0.5f : 1.0f));
            }
            if (this.mc.thePlayer.moveStrafing < 0.0f) {
                dir += 90.0f * ((this.mc.thePlayer.moveForward < 0.0f) ? -0.5f : ((this.mc.thePlayer.moveForward > 0.0f) ? 0.5f : 1.0f));
            }
            final double hOff = forward[i];
            final float xD = (float)(Math.cos(dir + 1.5707963267948966) * hOff);
            final float zD = (float)(Math.sin(dir + 1.5707963267948966) * hOff);
            this.mc.thePlayer.setPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY - 0.02, this.mc.thePlayer.posZ);
            this.mc.thePlayer.setPosition(this.mc.thePlayer.posX + xD * 2.2f, this.mc.thePlayer.posY, this.mc.thePlayer.posZ + zD * 2.2f);
        }
        return "Done.";
    }
}
