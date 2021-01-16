/*
 * Decompiled with CFR 0_122.
 */
package me.razerboy420.weepcraft.module.modules.player;

import darkmagician6.EventTarget;
import darkmagician6.events.EventCollideCheck;
import me.razerboy420.weepcraft.module.Module;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockEnderChest;

@Module.Mod(category=Module.Category.PLAYER, description="Open chests through blocks", key=0, name="GhostHand")
public class GhostHand
extends Module {
    @EventTarget
    public void onCheck(EventCollideCheck event) {
        if (!(event.block instanceof BlockChest) && !(event.block instanceof BlockEnderChest)) {
            event.setCancelled(true);
        }
    }
}

