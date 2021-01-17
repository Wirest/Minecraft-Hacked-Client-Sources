package me.rigamortis.faurax.commands;

import me.cupboard.command.*;
import net.minecraft.client.*;
import me.rigamortis.faurax.*;
import me.cupboard.command.argument.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;

public class CommandSuicide extends Command
{
    private Minecraft mc;
    
    public CommandSuicide() {
        super("Suicide", new String[0]);
        this.mc = Minecraft.getMinecraft();
        Client.getCMDS().register(this);
    }
    
    @Argument
    protected String suicide() {
        this.damage();
        return "";
    }
    
    public void damage() {
        final double[] d = { 0.2, 0.26 };
        for (int a = 0; a < 500; ++a) {
            for (int i = 0; i < d.length; ++i) {
                this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + d[i], this.mc.thePlayer.posZ, false));
            }
        }
    }
}
