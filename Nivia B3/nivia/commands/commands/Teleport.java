package nivia.commands.commands;

import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import nivia.commands.Command;
import nivia.utils.Helper;
import nivia.utils.Logger;

import java.util.Objects;

public class Teleport extends Command{
    public Teleport(){
        super("Teleport", "Teleports to the specified coords", "Invalid Coords.", false , "tp", "tel");
    }

    @Override
    public void execute(String commandName, String[] arguments) {
        String Sx = arguments[1];
        String Sy = arguments[2];
        String Sz = arguments[3];
        double x = Double.parseDouble(Sx);
        double y = Double.parseDouble(Sy);
        double z = Double.parseDouble(Sz);
        if(Objects.nonNull(mc.thePlayer.ridingEntity)){
            for(int i = 0; i < 100; i++){
                Helper.sendPacket(new C02PacketUseEntity(mc.thePlayer.ridingEntity, C02PacketUseEntity.Action.ATTACK));
            }
        }
        Helper.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, 0 , mc.thePlayer.posZ, false));
        Helper.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 2 , z, false));
        mc.thePlayer.setPositionAndUpdate(x, y, z);
        for(int i = 0; i < 10; i++)
            Helper.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition((x * i), y - (0.1 * i) , (z * i),  true));
        Helper.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z ,false));
        mc.thePlayer.setPositionAndUpdate(x, y, z);
        Logger.logChat("Teleported to: \247b" + x + "\2477, \247b" + y + "\2477, \247b" + z + "\2477." );
    }
}
