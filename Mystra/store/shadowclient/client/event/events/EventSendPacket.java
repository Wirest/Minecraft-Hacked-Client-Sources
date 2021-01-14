package store.shadowclient.client.event.events;

import store.shadowclient.client.event.Event;
import net.minecraft.network.Packet;

public class EventSendPacket extends Event {
    public Packet packet;
    private float yaw, pitch;
    
    public EventSendPacket(Packet packet) {
        this.packet = packet;
        //setPacket(packet);
    }
    public Packet getPacket() {
        return packet;
    }
    public void setPacket(Packet packet) {
        this.packet = packet;
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