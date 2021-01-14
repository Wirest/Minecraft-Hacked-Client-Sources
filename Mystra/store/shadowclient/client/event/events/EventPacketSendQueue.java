package store.shadowclient.client.event.events;

import store.shadowclient.client.event.Event;
import net.minecraft.network.Packet;

public class EventPacketSendQueue extends Event {
	private float yaw, pitch;
    private boolean cancel;

      public static Packet packet;

      public EventPacketSendQueue(Packet packet) {
          EventPacketSendQueue.packet = packet;
      }
      public Packet getPacket() {
        return packet;
      }

      public static Packet getPacket2() {
        return packet;
      }

      public boolean isCancelled() {
        return this.cancel;
      }

      public void setCancelled(boolean cancel) {
        this.cancel = cancel;
      }

      public void setPacket(Packet packet) {
          EventPacketSendQueue.packet = packet;
      }

      public static Packet getPacket1() {
        return packet;
      }

      public static void setPacket1(Packet packet) {
          EventPacketSendQueue.packet = packet;
      }
      
      public void setRotation(float yaw, float pitch) {
  		if (Float.isNaN(yaw) || Float.isNaN(pitch) || pitch > 90 || pitch < -90) return;
  		
  		this.yaw = yaw;
  		this.pitch = pitch;
  	  }
      public void setRotations(float[] rotations) {
		setRotation(rotations[0], rotations[1]);
      }
}
