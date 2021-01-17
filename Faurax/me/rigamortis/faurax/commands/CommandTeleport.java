package me.rigamortis.faurax.commands;

import me.cupboard.command.*;
import net.minecraft.client.*;
import net.minecraft.entity.player.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;
import me.cupboard.command.argument.*;

public class CommandTeleport extends Command
{
    public CommandTeleport() {
        super("teleport", new String[] { "tp" });
    }
    
    @Argument
    protected String teleport(final int posX, final int posY, final int posZ) {
        new Thread() {
            @Override
            public void run() {
                if (Minecraft.getMinecraft().thePlayer.ridingEntity == null && Minecraft.getMinecraft().thePlayer.getSleepTimer() < 0) {
                    return;
                }
                for (int i = 0; i < 20; ++i) {
                    Minecraft.getMinecraft().playerController.attackEntity(Minecraft.getMinecraft().thePlayer, Minecraft.getMinecraft().thePlayer.ridingEntity);
                }
                Minecraft.getMinecraft().thePlayer.setPosition(posX, posY, posZ);
                Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, posY, posZ, true));
            }
        }.start();
        return "Teleporting...";
    }
}
