/*
 * Decompiled with CFR 0_122.
 */
package me.razerboy420.weepcraft.module.modules.movement;

import darkmagician6.EventTarget;
import darkmagician6.events.EventPreMotionUpdates;
import darkmagician6.events.EventPrePlayerUpdate;
import me.razerboy420.weepcraft.module.Module;
import me.razerboy420.weepcraft.util.Wrapper;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.FoodStats;

@Module.Mod(category=Module.Category.MOVEMENT, description="NCP test", key=47, name="SPEED test")

public class Speed
extends Module {
	 @Override
	    public void onEnable() {
			Wrapper.getPlayer().setSprinting(true);
			Wrapper.getPlayer().motionY = 0.1;
	 }
}

