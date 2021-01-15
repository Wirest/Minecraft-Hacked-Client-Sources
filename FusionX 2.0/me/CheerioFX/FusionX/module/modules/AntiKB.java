// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.module.modules;

import com.darkmagician6.eventapi.EventTarget;

import me.CheerioFX.FusionX.events.EventPacketSent;
import me.CheerioFX.FusionX.events.EventPushOutOfBlock;
import me.CheerioFX.FusionX.module.Category;
import me.CheerioFX.FusionX.module.Module;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;

public class AntiKB extends Module {
	public static boolean enabled;
	public static boolean bypass;

	static {
		AntiKB.enabled = false;
		AntiKB.bypass = true;
	}

	public AntiKB() {
		super("AntiKB", 0, Category.COMBAT);
	}

	@Override
	public void onEnable() {
		AntiKB.enabled = true;
		super.onEnable();
	}

	@Override
	public void onDisable() {
		AntiKB.enabled = false;
		super.onDisable();
	}

	@EventTarget
	public void onPacketSent(final EventPacketSent sent) {
		if (sent.getPacket() instanceof S12PacketEntityVelocity || sent.getPacket() instanceof S27PacketExplosion) {
			sent.setCancelled(true);
		}
	}

	@EventTarget
	public void onEvent(final EventPushOutOfBlock event) {
		event.setCancelled(true);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
	}
}
