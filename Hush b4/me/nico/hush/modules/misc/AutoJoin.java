// 
// Decompiled by Procyon v0.5.36
// 

package me.nico.hush.modules.misc;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.client.Minecraft;
import me.nico.hush.events.EventUpdate;
import me.nico.hush.modules.Category;
import me.nico.hush.utils.TimeHelper;
import me.nico.hush.modules.Module;

public class AutoJoin extends Module
{
    boolean cansend;
    TimeHelper delay;
    
    public AutoJoin() {
        super("AutoJoin", "AutoJoin", 433212, 0, Category.MISC);
        this.cansend = false;
        this.delay = new TimeHelper();
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
        if (AutoJoin.mc.getCurrentServerData() == null) {
            return;
        }
        final Minecraft mc = AutoJoin.mc;
        Label_0044: {
            if (!Minecraft.thePlayer.isSpectator()) {
                final Minecraft mc2 = AutoJoin.mc;
                if (!Minecraft.thePlayer.capabilities.allowFlying) {
                    break Label_0044;
                }
            }
            this.cansend = true;
        }
        if (TimeHelper.hasReached(1000L) && AutoJoin.mc.getCurrentServerData().serverIP.equalsIgnoreCase("mc-central.net") && this.cansend) {
            int i = 7;
            if (AutoJoin.mc.getCurrentServerData().serverIP.equalsIgnoreCase("mc-central.net")) {
                i = 4;
            }
            final C09PacketHeldItemChange localC09PacketHeldItemChange1 = new C09PacketHeldItemChange(i);
            final Minecraft mc3 = AutoJoin.mc;
            Minecraft.thePlayer.sendQueue.getNetworkManager().sendPacket(localC09PacketHeldItemChange1);
            try {
                final Minecraft mc4 = AutoJoin.mc;
                final NetHandlerPlayClient sendQueue = Minecraft.thePlayer.sendQueue;
                final Minecraft mc5 = AutoJoin.mc;
                sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(Minecraft.thePlayer.inventoryContainer.getSlot(i).getStack()));
            }
            catch (Exception ex) {}
            final Minecraft mc6 = AutoJoin.mc;
            final C09PacketHeldItemChange localC09PacketHeldItemChange2 = new C09PacketHeldItemChange(Minecraft.thePlayer.inventory.currentItem);
            final Minecraft mc7 = AutoJoin.mc;
            Minecraft.thePlayer.sendQueue.getNetworkManager().sendPacket(localC09PacketHeldItemChange2);
            this.cansend = false;
        }
    }
    
    @Override
    public void onEnable() {
        EventManager.register(this);
    }
    
    @Override
    public void onDisable() {
        EventManager.unregister(this);
    }
}
