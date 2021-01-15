package nivia.commands.commands;

import net.minecraft.network.play.client.C03PacketPlayer;
import nivia.commands.Command;
import nivia.utils.Logger;

public class vClip extends Command {
    public vClip(){
        super("vClip", "Teleports up some blocks.", Logger.LogExecutionFail("Value!", new String[]{"Blocks"}), false ,"vc", "verticalClip");
    }

    @Override
    public void execute(String commandName, String[] arguments) {
        float distance = Float.parseFloat(arguments[1]);
        this.mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + (distance), this.mc.thePlayer.posZ, true));
        this.mc.thePlayer.setPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + distance, this.mc.thePlayer.posZ);
      
    }
}