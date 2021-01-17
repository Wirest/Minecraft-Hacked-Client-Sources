package me.rigamortis.faurax.commands;

import me.cupboard.command.*;
import net.minecraft.client.*;
import me.rigamortis.faurax.*;
import net.minecraft.entity.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;
import me.cupboard.command.argument.*;

public class CommandHclip extends Command
{
    private Minecraft mc;
    
    public CommandHclip() {
        super("HClip", new String[] { "hc" });
        this.mc = Minecraft.getMinecraft();
        Client.getCMDS().register(this);
    }
    
    @Argument
    protected String hclip(final double dist) {
        if (Client.getClientHelper().direction(this.mc.thePlayer).equalsIgnoreCase("south")) {
            this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ + 0.0010000000474974513, true));
            this.mc.thePlayer.setPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ + dist);
        }
        if (Client.getClientHelper().direction(this.mc.thePlayer).equalsIgnoreCase("west")) {
            this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX - 0.0010000000474974513, this.mc.thePlayer.posY, this.mc.thePlayer.posZ, true));
            this.mc.thePlayer.setPosition(this.mc.thePlayer.posX - dist, this.mc.thePlayer.posY, this.mc.thePlayer.posZ);
        }
        if (Client.getClientHelper().direction(this.mc.thePlayer).equalsIgnoreCase("north")) {
            this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ - 0.0010000000474974513, true));
            this.mc.thePlayer.setPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ - dist);
        }
        if (Client.getClientHelper().direction(this.mc.thePlayer).equalsIgnoreCase("east")) {
            this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX + 0.0010000000474974513, this.mc.thePlayer.posY, this.mc.thePlayer.posZ, true));
            this.mc.thePlayer.setPosition(this.mc.thePlayer.posX + dist, this.mc.thePlayer.posY, this.mc.thePlayer.posZ);
        }
        return "HClipped §b" + (float)dist + "§f";
    }
}
