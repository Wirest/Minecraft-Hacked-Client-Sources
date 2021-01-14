package me.Corbis.Execution.module.implementations;

import me.Corbis.Execution.Execution;
import me.Corbis.Execution.event.EventTarget;
import me.Corbis.Execution.event.events.EventReceivePacket;
import me.Corbis.Execution.event.events.EventSendPacket;
import me.Corbis.Execution.module.Category;
import me.Corbis.Execution.module.Module;
import net.minecraft.entity.Entity;
import net.minecraft.network.login.server.S00PacketDisconnect;
import net.minecraft.network.login.server.S01PacketEncryptionRequest;
import net.minecraft.network.login.server.S02PacketLoginSuccess;
import net.minecraft.network.login.server.S03PacketEnableCompression;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
import net.minecraft.network.play.server.*;
import org.lwjgl.input.Keyboard;

import java.util.Random;

public class Disabler extends Module {
    public Disabler(){
        super("Disabler", Keyboard.KEY_U, Category.MOVEMENT);
    }

    @EventTarget
    public void onReceivePacket(EventReceivePacket event){


       // Execution.instance.addChatMessage(event.getPacket().toString());


            if(event.getPacket() instanceof S32PacketConfirmTransaction){
            S32PacketConfirmTransaction packet = (S32PacketConfirmTransaction) event.getPacket();
            if(packet.getActionNumber() < 0){
             //   Execution.instance.addChatMessage("nib");

                event.setCancelled(true);
            }
        }

    }

    @EventTarget
    public void onSendPacket(EventSendPacket e){


        if (e.getPacket() instanceof C00PacketKeepAlive) {
            mc.getNetHandler().addToSendQueueSilent(new C00PacketKeepAlive(Integer.MIN_VALUE + new Random().nextInt(100)));
            e.setCancelled(true);
        }
        if (e.getPacket() instanceof C0FPacketConfirmTransaction) {
            final C0FPacketConfirmTransaction packetConfirmTransaction = (C0FPacketConfirmTransaction)e.getPacket();
            mc.getNetHandler().addToSendQueueSilent(new C0FPacketConfirmTransaction(Integer.MAX_VALUE, packetConfirmTransaction.getUid(), false));
            e.setCancelled(true);
        }
    }
}
