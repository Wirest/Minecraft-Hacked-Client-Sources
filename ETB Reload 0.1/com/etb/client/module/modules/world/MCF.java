package com.etb.client.module.modules.world;

import java.awt.Color;

import org.greenrobot.eventbus.Subscribe;

import com.etb.client.Client;
import com.etb.client.event.events.input.MouseEvent;
import com.etb.client.module.Module;
import com.etb.client.utils.Printer;

import net.minecraft.entity.player.EntityPlayer;

public class MCF extends Module {

    public MCF() {
        super("MCF", Category.PLAYER, new Color(255, 195, 215, 255).getRGB());
        setDescription("Middle click friends");
        setRenderlabel("Middle Click Friends");
    }

    @Subscribe
    public void onMouseClick(MouseEvent event) {
        if (event.getButton() == 2 && mc.objectMouseOver != null && mc.objectMouseOver.entityHit != null && mc.objectMouseOver.entityHit instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) mc.objectMouseOver.entityHit;
            String name = player.getName();
            if (Client.INSTANCE.getFriendManager().isFriend(name)) {
                Client.INSTANCE.getFriendManager().removeFriend(name);
                Printer.print("Removed " + name + " as a friend!");
            } else {
                Client.INSTANCE.getFriendManager().addFriend(name);
                Printer.print("Added " + name + " as a friend!");
            }
        }
    }
}
