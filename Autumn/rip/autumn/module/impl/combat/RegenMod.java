package rip.autumn.module.impl.combat;

import me.zane.basicbus.api.annotations.Listener;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import rip.autumn.annotations.Label;
import rip.autumn.events.game.TickEvent;
import rip.autumn.module.Module;
import rip.autumn.module.ModuleCategory;
import rip.autumn.module.annotations.Category;
import rip.autumn.module.option.Option;
import rip.autumn.module.option.impl.DoubleOption;
import rip.autumn.module.option.impl.EnumOption;

@Label("Regen")
@Category(ModuleCategory.COMBAT)
public final class RegenMod extends Module {
   public static final EnumOption mode;
   public static final DoubleOption packets;

   public RegenMod() {
      this.setMode(mode);
      this.addOptions(new Option[]{mode, packets});
   }

   @Listener(TickEvent.class)
   public final void onTick() {
      switch((RegenMod.Mode)mode.getValue()) {
      case PACKET:
         this.packetRegen(((Double)packets.getValue()).intValue());
         break;
      case POTION:
         if (mc.thePlayer.isPotionActive(Potion.regeneration)) {
            this.packetRegen((int)((Double)packets.getValue() / 2.0D * (double)mc.thePlayer.getActivePotionEffect(Potion.regeneration).getAmplifier()));
         }
      }

   }

   private void packetRegen(int packets) {
      if (mc.thePlayer.onGround) {
         for(int i = 0; i < packets; ++i) {
            mc.getNetHandler().addToSendQueueSilent(new C03PacketPlayer(true));
         }
      }

   }

   static {
      mode = new EnumOption("Mode", RegenMod.Mode.POTION);
      packets = new DoubleOption("Packets", 10.0D, 1.0D, 1000.0D, 1.0D);
   }

   private static enum Mode {
      PACKET,
      POTION;
   }
}
