// 
// Decompiled by Procyon v0.5.30
// 

package cf.euphoria.euphorical.Mod.Collection.Player;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.potion.PotionEffect;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import cf.euphoria.euphorical.Utils.BlockUtils;
import cf.euphoria.euphorical.Events.EventUpdate;
import com.darkmagician6.eventapi.EventManager;
import cf.euphoria.euphorical.Mod.Category;
import cf.euphoria.euphorical.Mod.Mod;

public class Zoot extends Mod
{
    public Zoot() {
        super("Zoot", Category.PLAYER);
    }
    
    @Override
    public void onEnable() {
        EventManager.register(this);
    }
    
    @Override
    public void onDisable() {
        EventManager.unregister(this);
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
        if (!BlockUtils.isOnLiquid()) {
            if (this.mc.thePlayer.isPotionActive(Potion.blindness.getId())) {
                this.mc.thePlayer.removePotionEffect(Potion.blindness.getId());
            }
            if (this.mc.thePlayer.isPotionActive(Potion.confusion.getId())) {
                this.mc.thePlayer.removePotionEffect(Potion.confusion.getId());
            }
            if (this.mc.thePlayer.isPotionActive(Potion.digSlowdown.getId())) {
                this.mc.thePlayer.removePotionEffect(Potion.digSlowdown.getId());
            }
            if (this.mc.thePlayer.isBurning()) {
                for (int x = 0; x < 100; ++x) {
                    this.mc.getNetHandler().getNetworkManager().dispatchPacket(new C03PacketPlayer(), null);
                }
            }
            Potion[] potionTypes;
            for (int length = (potionTypes = Potion.potionTypes).length, i = 0; i < length; ++i) {
                final Potion potion = potionTypes[i];
                if (potion != null && potion.isBadEffect() && this.mc.thePlayer.isPotionActive(potion)) {
                    final PotionEffect effect = this.mc.thePlayer.getActivePotionEffect(potion);
                    for (int x2 = 0; x2 < effect.getDuration() / 20; ++x2) {
                        this.mc.getNetHandler().getNetworkManager().dispatchPacket(new C03PacketPlayer(), null);
                    }
                }
            }
        }
    }
}
