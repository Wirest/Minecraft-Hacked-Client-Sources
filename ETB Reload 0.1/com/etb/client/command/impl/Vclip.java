package com.etb.client.command.impl;

import com.etb.client.command.Command;
import com.etb.client.Client;
import com.etb.client.gui.notification.Notification;
import com.etb.client.utils.Printer;

import net.minecraft.client.Minecraft;

public class Vclip extends Command {

    public Vclip() {
        super("Vclip", new String[]{"v", "vclip"}, "Vertically teleport yourself");
    }

    @Override
    public void onRun(final String[] args) {
        final double distance = Double.parseDouble(args[1]);
        Minecraft.getMinecraft().thePlayer.getEntityBoundingBox().offsetAndUpdate(0, distance, 0);
        Client.INSTANCE.getNotificationManager().sendClientMessage("Vcliped " + args[1] + "!", Notification.Type.SUCCESS);
        Printer.print("Vcliped " + args[1] + "!");
    }
}
