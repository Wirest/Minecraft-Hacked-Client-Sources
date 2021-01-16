package org.m0jang.crystal.Mod.Collection.Misc;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.network.play.client.C03PacketPlayer;
import org.m0jang.crystal.Events.EventPacketSend;
import org.m0jang.crystal.Events.EventTick;
import org.m0jang.crystal.Mod.Category;
import org.m0jang.crystal.Mod.Module;

public class Ghost extends Module {
   private boolean bypassdeath = true;

   public Ghost() {
      super("Ghost", Category.Misc, false);
   }

   public void onEnable() {
      super.onEnable();
   }

   public void onDisable() {
      super.onDisable();
      Minecraft.thePlayer.respawnPlayer();
      this.bypassdeath = false;
   }

   @EventTarget
   public void onTick(EventTick event) {
      if (Minecraft.theWorld != null) {
         if (Minecraft.thePlayer.getHealth() == 0.0F) {
            Minecraft.thePlayer.setHealth(20.0F);
            Minecraft.thePlayer.isDead = false;
            this.bypassdeath = true;
            this.mc.displayGuiScreen((GuiScreen)null);
            Minecraft.thePlayer.setPositionAndUpdate(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY, Minecraft.thePlayer.posZ);
         }

      }
   }

   @EventTarget
   public void onPacketSend(EventPacketSend packet) {
      if (this.bypassdeath && packet.packet instanceof C03PacketPlayer) {
         packet.setCancelled(true);
      }

   }
}
