package nivia.commands.commands;

import net.minecraft.network.play.client.C03PacketPlayer;
import nivia.commands.Command;
import nivia.utils.Helper;
import nivia.utils.Logger;



public class Damage extends Command {
    public Damage() {
        super("Damage", "Damages you a bit", Logger.LogExecutionFail("Value", new String[]{"Hearts"}), false, "dmg");
    }
    @Override
    public void execute(String commandName, String[] arguments){

        double damage = Double.parseDouble(arguments[1]);
        for (int i = 0; i < 80 + (40 * (damage - 0.5)); i++){
           Helper.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.06, mc.thePlayer.posZ, false));
           Helper.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
        }
       Helper.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY , mc.thePlayer.posZ, false));
       Helper.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.02, mc.thePlayer.posZ, false));
    }
}
