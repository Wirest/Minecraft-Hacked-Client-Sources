package com.etb.client.module.modules.player;

import com.etb.client.Client;
import com.etb.client.event.events.game.ChatEvent;
import com.etb.client.event.events.player.UpdateEvent;
import com.etb.client.gui.notification.Notification;
import com.etb.client.module.Module;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import org.greenrobot.eventbus.Subscribe;

import java.awt.*;

public class AutoAccept extends Module {
    public AutoAccept() {
        super("AutoAccept", Category.PLAYER, new Color(255, 194, 97).getRGB());
        setDescription("Automatically accept invites");
    }

    @Subscribe
    public void onPacket(ChatEvent e) {
            String message = e.getMsg();
            if (this.gotTpaRequest(message)) {
                this.handleRequest(message, "/tpaccept", "Accepted teleport");
            }
            if (this.gotPartyRequest(message)) {
                this.handleRequest(message, "/party accept", "Accepted party invite");
            }
            if (this.gotFactionRequest(message)) {
                this.handleRequest(message, "/f join", "Accepted faction invitation");
            }
    }

    private void handleRequest(String message, String messageToSend, String notificationMessage) {
        final Object o;
        Client.INSTANCE.getFriendManager().getFriends().forEach(friends -> {
            if (message.contains(friends.getName())) {
                mc.thePlayer.sendChatMessage(messageToSend + " " + friends.getName());
               Client.INSTANCE.getNotificationManager().sendClientMessage(notificationMessage,Notification.Type.INFO);
            }
        });
    }

    private boolean gotTpaRequest(String message) {
        message = message.toLowerCase();
        return message.contains("has requested to teleport") || message.contains("to teleport to you") || (message.contains("has requested that you teleport to them"));
    }
    private boolean gotFactionRequest(String message) {
        message = message.toLowerCase();
        return message.contains("has invited you to join") && !message.contains("party");
    }

    private boolean gotPartyRequest(String message) {
        message = message.toLowerCase();
        return message.contains("has invited you to join their party\n");
    }
}