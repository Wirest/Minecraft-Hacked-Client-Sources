package nivia.commands.commands;

import net.minecraft.item.ItemEnderPearl;
import nivia.commands.Command;
import nivia.utils.Logger;
import nivia.utils.utils.Timer;

public class PearlClip extends Command {
    private final Timer timer = new Timer();
    public PearlClip(){
        super("PearlClip", "Teleports up/down some blocks, and pearls.", Logger.LogExecutionFail("Value!", new String[]{"Blocks"}), false ,"pc", "pearlClip", "pclip");
    }

    @Override
    public void execute(String commandName, String[] arguments) {
        float distance = Float.parseFloat(arguments[1]);
        this.mc.thePlayer.setPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + distance, this.mc.thePlayer.posZ);
        if (this.timer.hasTimeElapsed(3, true)){
            if(mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemEnderPearl)
            mc.playerController.sendUseItem(mc.thePlayer,mc.theWorld, mc.thePlayer.getCurrentEquippedItem());
            else Logger.logChat("Equip a pearl nigger.");
        }
    }
}