package nivia.commands.commands;

import nivia.commands.Command;
import nivia.utils.Logger;

public class hClip extends Command {
    public hClip(){
        super("hClip", "Teleports sideways some blocks.", Logger.LogExecutionFail("Value!", new String[]{"Blocks"}), false ,"hc");
    }

    @Override
    public void execute(String commandName, String[] arguments) {
        float multiplier = Float.parseFloat(arguments[1]);
        double mx = Math.cos(Math.toRadians(mc.thePlayer.rotationYaw + 90.0F));
        double mz = Math.sin(Math.toRadians(mc.thePlayer.rotationYaw + 90.0F));
        double x = (1 * multiplier * mx + 0 * multiplier * mz);
        double z = (1 * multiplier * mz - 0 * multiplier * mx);

        this.mc.thePlayer.setPosition(this.mc.thePlayer.posX + (x) , this.mc.thePlayer.posY, this.mc.thePlayer.posZ + (z));
    }
}