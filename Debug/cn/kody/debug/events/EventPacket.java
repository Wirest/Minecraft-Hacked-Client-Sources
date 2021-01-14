package cn.kody.debug.events;

import com.darkmagician6.eventapi.events.Event;
import com.darkmagician6.eventapi.types.EventType;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.Packet;

public class EventPacket
implements Event {
    private EventPacketType type;
    public static boolean sendcancel;
    public EntityLivingBase target;
    private boolean cancelled;
    public static boolean recievecancel;
    public Packet packet;

    public EventPacket(EventPacketType type, Packet packet) {
        this.type = type;
        this.packet = packet;
    }

    public EventPacketType getType() {
        return this.type;
    }

    public void setType(EventPacketType type) {
        this.type = type;
    }

    public static boolean isSendcancel() {
        return sendcancel;
    }

    public static void setSendcancel(boolean sendcancel) {
        EventPacket.sendcancel = sendcancel;
    }

    public EntityLivingBase getTarget() {
        return this.target;
    }

    public void setTarget(EntityLivingBase target) {
        this.target = target;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public static boolean isRecievecancel() {
        return recievecancel;
    }

    public static void setRecievecancel(boolean recievecancel) {
        EventPacket.recievecancel = recievecancel;
    }

    public Packet getPacket() {
        return this.packet;
    }

    public void setPacket(Packet packet) {
        this.packet = packet;
    }

    public static enum EventPacketType {
        SEND,
        RECEIVE;
        

        private EventPacketType() {
        }
    }

    public boolean isOutgoing() {
        boolean b;
        if (this.type == EventPacketType.SEND) {
            b = true;
        }
        else {
            b = false;
        }
        return b;
    }

}

