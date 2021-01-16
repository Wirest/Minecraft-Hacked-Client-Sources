package org.m0jang.crystal.Mod.Collection.Combat;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import org.m0jang.crystal.Events.EventState;
import org.m0jang.crystal.Events.EventUpdate;
import org.m0jang.crystal.Mod.Category;
import org.m0jang.crystal.Mod.Module;
import org.m0jang.crystal.Utils.BlockUtils;
import org.m0jang.crystal.Utils.TimeHelper;
import org.m0jang.crystal.Values.Value;

public class Regen extends Module {
   private TimeHelper timer = new TimeHelper();
   public static Value mode = new Value("Regen", String.class, "Mode", "Potion", new String[]{"Potion", "Mineplex", "Infinite"});

   public Regen() {
      super("Regen", Category.Combat, mode);
   }

   @EventTarget
   private void onUpdate(EventUpdate event) {
      if (event.state == EventState.PRE) {
         int i;
         if (mode.getSelectedOption().equals("Potion") && Minecraft.thePlayer.getActivePotionEffect(Potion.regeneration) != null && (Minecraft.thePlayer.onGround || BlockUtils.isOnLadder() || BlockUtils.isInLiquid() || BlockUtils.isOnLiquid()) && Minecraft.thePlayer.getHealth() < Minecraft.thePlayer.getMaxHealth()) {
            for(i = 0; (float)i < Minecraft.thePlayer.getMaxHealth() - Minecraft.thePlayer.getHealth() && Minecraft.thePlayer.getActivePotionEffect(Potion.regeneration) != null; ++i) {
               this.mc.getNetHandler().addToSendQueue(new C03PacketPlayer(event.onGround));
            }
         }

         if (mode.getSelectedOption().equals("Mineplex") && Minecraft.thePlayer.onGround && Minecraft.thePlayer.getHealth() < Minecraft.thePlayer.getMaxHealth()) {
            for(i = 0; i < 10; ++i) {
               this.mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(Minecraft.thePlayer.rotationYaw, Minecraft.thePlayer.rotationPitch, true));
            }
         }

         if (mode.getSelectedOption().equals("Infinite") && !Minecraft.thePlayer.capabilities.isCreativeMode && Minecraft.thePlayer.getFoodStats().getFoodLevel() > 17 && Minecraft.thePlayer.getHealth() < 20.0F && Minecraft.thePlayer.getHealth() != 0.0F && Minecraft.thePlayer.onGround) {
            for(i = 0; i < 50; ++i) {
               Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer());
            }
         }

      }
   }
}
