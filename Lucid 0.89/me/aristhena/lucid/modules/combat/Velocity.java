/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.multiplayer.WorldClient
 *  net.minecraft.entity.Entity
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.server.S12PacketEntityVelocity
 */
package me.aristhena.lucid.modules.combat;

import me.aristhena.lucid.eventapi.EventTarget;
import me.aristhena.lucid.eventapi.events.PacketReceiveEvent;
import me.aristhena.lucid.eventapi.events.TickEvent;
import me.aristhena.lucid.management.module.Mod;
import me.aristhena.lucid.management.module.Module;
import me.aristhena.lucid.management.module.ModuleManager;
import me.aristhena.lucid.management.option.Option;
import me.aristhena.lucid.management.option.OptionManager;
import me.aristhena.lucid.management.value.Val;
import me.aristhena.lucid.modules.render.HUD;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S12PacketEntityVelocity;

@Mod
public class Velocity
extends Module {
    @Val(min=0.0, max=200.0, increment=5.0)
    private double percent = 0.0;

    @EventTarget
    private void onTick(TickEvent event) {
        Character colorFormatCharacter = new Character('\u00a7');
        this.suffix = OptionManager.getOption((String)"Hyphen", (Module)ModuleManager.getModule(HUD.class)).value ? colorFormatCharacter + "7 - " + this.percent + "%" : colorFormatCharacter + "7 " + this.percent + "%";
    }

    @EventTarget
    private void onPacketReceive(PacketReceiveEvent event) {
        S12PacketEntityVelocity packet;
        if (event.packet instanceof S12PacketEntityVelocity && this.mc.theWorld.getEntityByID((packet = (S12PacketEntityVelocity)event.packet).func_149412_c()) == this.mc.thePlayer) {
            if (this.percent > 0.0) {
                packet.field_149415_b = (int)((double)packet.field_149415_b * (this.percent / 100.0));
                packet.field_149416_c = (int)((double)packet.field_149416_c * (this.percent / 100.0));
                packet.field_149414_d = (int)((double)packet.field_149414_d * (this.percent / 100.0));
            } else {
                event.setCancelled(true);
            }
        }
    }
}

