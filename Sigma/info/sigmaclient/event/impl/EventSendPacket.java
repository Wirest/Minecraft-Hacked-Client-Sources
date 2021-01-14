/**
 * Time: 8:13:13 PM
 * Date: Dec 27, 2016
 * Creator: cool1
 */
package info.sigmaclient.event.impl;

import info.sigmaclient.event.Event;
import net.minecraft.network.Packet;

/**
 * @author cool1
 */
public class EventSendPacket extends Event {
    private Packet packet;
    private boolean pre;

    public void fire(boolean state, Packet packet) {
        this.pre = state;
        this.packet = packet;
        super.fire();
    }

    public Packet getPacket() {
        return this.packet;
    }

    public boolean isPre() {
        return pre;
    }

    public void setPacket(Packet packet) {
        this.packet = packet;
    }

    public void setState(boolean state) {
        pre = state;
    }
}
