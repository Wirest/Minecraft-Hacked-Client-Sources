package org.m0jang.crystal.Mod.Collection.Movement;

import com.darkmagician6.eventapi.EventTarget;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import org.m0jang.crystal.Events.EventPacketSend;
import org.m0jang.crystal.Events.EventTick;
import org.m0jang.crystal.Mod.Category;
import org.m0jang.crystal.Mod.Module;
import org.m0jang.crystal.Utils.TimeHelper;

public class Blink extends Module {
   private long deltaTime;
   private long startTime;
   TimeHelper timer;
   private EntityOtherPlayerMP player;
   private ArrayList packets = new ArrayList();

   public Blink() {
      super("Blink", Category.Movement, false);
   }

   public void onEnable() {
      this.deltaTime = 0L;
      this.timer = new TimeHelper();
      super.onEnable();
      (this.player = new EntityOtherPlayerMP(Minecraft.theWorld, Minecraft.thePlayer.getGameProfile())).clonePlayer(Minecraft.thePlayer, true);
      this.player.setPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY, Minecraft.thePlayer.posZ);
      this.player.rotationYawHead = Minecraft.thePlayer.rotationYaw;
      this.player.rotationPitch = Minecraft.thePlayer.rotationPitch;
      this.player.setSneaking(Minecraft.thePlayer.isSneaking());
      Minecraft.theWorld.addEntityToWorld(-1337, this.player);
      this.startTime = this.timer.getTime();
   }

   @EventTarget
   public void onTick(EventTick event) {
      this.setRenderName(String.format("%s \247f%sms", this.getName(), this.deltaTime));
   }

   @EventTarget
   public void onPacketSend(EventPacketSend event) {
      event.setCancelled(true);
      if (event.packet instanceof C03PacketPlayer.C04PacketPlayerPosition || event.packet instanceof C03PacketPlayer.C06PacketPlayerPosLook) {
         this.packets.add(event.packet);
         this.deltaTime = this.timer.getTime() - this.startTime;
      }

   }

   public void onDisable() {
      super.onDisable();
      Iterator var2 = this.packets.iterator();

      while(var2.hasNext()) {
         Packet packet = (Packet)var2.next();
         this.mc.getNetHandler().getNetworkManager().sendPacket(packet);
      }

      Minecraft.theWorld.removeEntity(this.player);
   }
}
