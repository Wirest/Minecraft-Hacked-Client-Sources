/*
 * Decompiled with CFR 0_122.
 */
package me.razerboy420.weepcraft.module.modules.player;

import darkmagician6.EventTarget;
import darkmagician6.events.EventPreMotionUpdates;
import java.util.Collection;
import me.razerboy420.weepcraft.module.Module;
import me.razerboy420.weepcraft.util.Wrapper;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.RegistryNamespaced;

@Module.Mod(category=Module.Category.PLAYER, description="Gets rid of bad potion effects (Might not work on 1.10 servers) I think I broked it :D", key=0, name="Zoot")
public class Zoot
extends Module {
    @EventTarget
    public void Event(EventPreMotionUpdates event) {
        if (Wrapper.getPlayer().isCollidedVertically && (Wrapper.getPlayer().getActiveItemStack() == null || !(Wrapper.getPlayer().getActiveItemStack().getItem() instanceof ItemFood))) {
            for (Potion potion : Potion.REGISTRY) {
                PotionEffect effect;
                if (potion == null || !potion.isBadEffect() || (effect = Wrapper.getPlayer().getActivePotionEffect(potion)) == null || effect.getIsPotionDurationMax()) continue;
                int index = 0;
                while (index < effect.getDuration()) {
                    Wrapper.sendPacket(new CPacketPlayer(Wrapper.getPlayer().onGround));
                    for (PotionEffect o : Wrapper.getPlayer().getActivePotionEffects()) {
                        if (!(o instanceof PotionEffect)) continue;
                        PotionEffect effect2 = o;
                        effect2.deincrementDuration();
                    }
                    ++index;
                }
            }
        }
    }
}

