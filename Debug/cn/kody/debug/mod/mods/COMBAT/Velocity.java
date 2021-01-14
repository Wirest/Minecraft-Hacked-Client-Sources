package cn.kody.debug.mod.mods.COMBAT;

import cn.kody.debug.events.EventPacket;
import cn.kody.debug.mod.Category;
import cn.kody.debug.mod.Mod;
import cn.kody.debug.value.Value;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;

public class Velocity
extends Mod {
    private Value mode = new Value("Velocity", "Mode", 0);
    private Value horizontal = new Value<Double>("Velocity_Horizontal", 100.0, 0.0, 100.0, 1.0);
    private Value vertical = new Value<Double>("Velocity_Vertical", 100.0, 0.0, 100.0, 1.0);
    private double motionX;
    private double motionZ;

    public Velocity() {
        super("Velocity", Category.COMBAT);
        this.mode.addValue("Normal");
        this.mode.addValue("Mineplex");
        this.mode.addValue("AAC Pull");
        this.mode.addValue("AAC Ultra Pull");
    }

    public int getH() {
        return ((Double)this.horizontal.getValueState()).intValue();
    }

    public int getV() {
        return ((Double)this.vertical.getValueState()).intValue();
    }

    @EventTarget
    public void onPacket(EventPacket event) {
        if (this.mode.isCurrentMode("Normal") || this.mode.isCurrentMode("Mineplex")) {
            if (this.mode.isCurrentMode("Normal")) {
                this.setDisplayName("Normal");
            } else {
                this.setDisplayName("Mineplex");
            }
            if (event.packet instanceof S12PacketEntityVelocity) {
                S12PacketEntityVelocity exp1 = (S12PacketEntityVelocity)event.packet;
                if (((Double)this.vertical.getValueState()).intValue() == 0 && ((Double)this.horizontal.getValueState()).intValue() == 0) {
                    event.setCancelled(true);
                } else {
                    int var10000 = exp1.getEntityID();
                    Minecraft.getMinecraft();
                    if (var10000 == Minecraft.thePlayer.getEntityId()) {
                        exp1.motionX = exp1.getMotionX() * (this.getH() / 100);
                        exp1.motionY = exp1.getMotionY() * (this.getV() / 100);
                        exp1.motionZ = exp1.getMotionZ() * (this.getH() / 100);
                    }
                }
            }
            if (event.packet instanceof S27PacketExplosion) {
                S27PacketExplosion exp11 = (S27PacketExplosion)event.packet;
                if (this.getH() == 0 && this.getV() == 0) {
                    event.setCancelled(true);
                } else {
                    exp11.field_149152_f = exp11.func_149149_c() * (float)(this.getH() / 100);
                    exp11.field_149153_g = exp11.func_149144_d() * (float)(this.getV() / 100);
                    exp11.field_149159_h = exp11.func_149147_e() * (float)(this.getH() / 100);
                }
            }
        }
    }

    @EventTarget
    public void onUpdate(EventPacket event) {
        if (this.mode.isCurrentMode("AAC Pull")) {
            this.setDisplayName("AAC Pull");
            if (Minecraft.thePlayer.hurtTime == 9) {
                this.motionX = Minecraft.thePlayer.motionX;
                this.motionZ = Minecraft.thePlayer.motionZ;
            } else if (Minecraft.thePlayer.hurtTime == 4) {
                Minecraft.thePlayer.motionX = (- this.motionX) * 0.6;
                Minecraft.thePlayer.motionZ = (- this.motionZ) * 0.6;
            }
        }
        if (this.mode.isCurrentMode("AAC Test2")) {
            this.setDisplayName("AAC Test2");
            if (Minecraft.thePlayer.hurtTime == 9) {
                this.motionX = Minecraft.thePlayer.motionX;
                this.motionZ = Minecraft.thePlayer.motionZ;
            } else if (Minecraft.thePlayer.hurtTime == 8) {
                Minecraft.thePlayer.motionX = (- this.motionX) * 0.45;
                Minecraft.thePlayer.motionZ = (- this.motionZ) * 0.45;
            }
        }
    }
}

