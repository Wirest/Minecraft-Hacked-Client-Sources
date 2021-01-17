package skyline.specc.mods.combat.velocity;

import net.minecraft.client.Mineman;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.eventtypes.EventPacket;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.main.EventListener;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.ModMode;
import skyline.specc.mods.combat.Velocity;

/** Leaking this client will VOID your TOS and ability to use it!*/
public class Percent extends ModMode<Velocity>
{
    public Percent(Velocity parent, String name) {
        super(parent, name);
    }

    private double percent = parent.percentage.getValue();

    @EventListener
    private void onPacketReceive(EventPacket event) {
       if (event.getPacket() instanceof S12PacketEntityVelocity) {
          S12PacketEntityVelocity packet = (S12PacketEntityVelocity)event.getPacket();
          if (Mineman.theWorld.getEntityByID(packet.func_149412_c()) == Mineman.thePlayer) {
             if (percent > 0.0D) {
                packet.field_149415_b *= (int)(percent / 100.0D);
                packet.field_149416_c *= (int)(percent / 100.0D);
                packet.field_149416_c *= (int)(percent / 100.0D);
             } else {
                event.setCancelled(true);
             }
          }
       } else if (event.getPacket() instanceof S27PacketExplosion) {
          S27PacketExplosion s27PacketExplosion;
          S27PacketExplosion packet2 = s27PacketExplosion = (S27PacketExplosion)event.getPacket();
          s27PacketExplosion.field_149152_f *= (float)(percent / 100.0D);
          packet2.field_149153_g *= (float)(percent / 100.0D);
          packet2.field_149159_h *= (float)(percent / 100.0D);
       }

    }
 }