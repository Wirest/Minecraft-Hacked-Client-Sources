// 
// Decompiled by Procyon v0.5.30
// 

package me.aristhena.client.module.modules.clans;

import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.network.play.server.S02PacketChat;
import me.aristhena.event.events.PacketReceiveEvent;
import me.aristhena.event.EventTarget;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import me.aristhena.utils.ClientUtils;
import me.aristhena.event.Event;
import me.aristhena.event.events.UpdateEvent;
import me.aristhena.utils.Timer;
import me.aristhena.client.option.Option;
import me.aristhena.client.module.Module;
import me.aristhena.client.module.Module.Mod;
@Mod
public class Soup extends Module
{
    @Option.Op(min = 0.5, max = 10.0, increment = 0.5)
    private double health;
    @Option.Op(min = 50.0, max = 1000.0, increment = 25.0)
    private double delay;
    @Option.Op(name = "Ignore Vanish")
    private boolean ignoreVanish;
    private Timer timer;
    private Timer invisTimer;
    private boolean invisible;
    
    public Soup() {
        this.health = 4.5;
        this.delay = 250.0;
        this.timer = new Timer();
        this.invisTimer = new Timer();
    }
    
    @EventTarget(3)
    private void onUpdate(final UpdateEvent event) {
        if (event.getState().equals(Event.State.PRE)) {
            if (this.invisible && this.invisTimer.delay(15000.0f)) {
                this.invisible = false;
            }
            final int soupSlot = this.getSoup();
            if ((this.ignoreVanish || !this.invisible) && soupSlot != -1 && ClientUtils.player().getHealth() <= this.health * 2.0 && this.timer.delay((float)this.delay) && ClientUtils.player().isCollidedVertically) {
                final int currentItem = ClientUtils.player().inventory.currentItem;
                ClientUtils.packet(new C09PacketHeldItemChange(soupSlot));
                ClientUtils.packet(new C08PacketPlayerBlockPlacement(ClientUtils.player().inventory.getCurrentItem()));
                ClientUtils.packet(new C09PacketHeldItemChange(currentItem));
                for (int i = 0; i < 80; ++i) {
                    ClientUtils.packet(new C03PacketPlayer(true));
                }
                this.timer.reset();
            }
        }
    }
    
    @EventTarget
    private void onPacketReceive(final PacketReceiveEvent event) {
        if (event.getPacket() instanceof S02PacketChat) {
            final S02PacketChat packet = (S02PacketChat)event.getPacket();
            final String message = packet.func_148915_c().getUnformattedText();
            if (message.equalsIgnoreCase("Condition> You are now invisible.")) {
                this.invisible = true;
                this.invisTimer.reset();
            }
            else if (message.equalsIgnoreCase("Condition> You are no longer invisible.")) {
                this.invisible = false;
                this.invisTimer.reset();
            }
        }
    }
    
    private int getSoup() {
        for (int i = 0; i <= 8; ++i) {
            final ItemStack stack = ClientUtils.player().inventory.getStackInSlot(i);
            if (stack != null) {
                if (Item.getIdFromItem(stack.getItem()) == Item.getIdFromItem(Items.mushroom_stew)) {
                    return i;
                }
            }
        }
        return -1;
    }
    
    @Override
    public void disable() {
        this.invisible = false;
        super.disable();
    }
}
