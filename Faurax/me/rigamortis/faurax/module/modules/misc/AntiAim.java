package me.rigamortis.faurax.module.modules.misc;

import me.rigamortis.faurax.module.helpers.*;
import me.rigamortis.faurax.module.*;
import me.rigamortis.faurax.*;
import me.rigamortis.faurax.values.*;
import net.minecraft.network.play.client.*;
import net.minecraft.entity.*;
import net.minecraft.network.*;
import net.minecraft.network.play.server.*;
import com.darkmagician6.eventapi.*;
import me.rigamortis.faurax.module.modules.player.*;
import me.rigamortis.faurax.events.*;

public class AntiAim extends Module implements PlayerHelper
{
    public int speed;
    private float oldYaw;
    private float oldPitch;
    public int delay;
    public static Value sneak;
    public static Value rotation;
    public static Value noHead;
    
    static {
        AntiAim.sneak = new Value("AntiAim", Boolean.TYPE, "Sneak", true);
        AntiAim.rotation = new Value("AntiAim", Boolean.TYPE, "Rotation", true);
        AntiAim.noHead = new Value("AntiAim", Boolean.TYPE, "No Head", true);
    }
    
    public AntiAim() {
        this.setName("AntiAim");
        this.setKey("HOME");
        this.setType(ModuleType.MISC);
        this.setColor(-2836728);
        this.setModInfo("");
        this.setVisible(true);
        Client.getValues();
        ValueManager.values.add(AntiAim.sneak);
        Client.getValues();
        ValueManager.values.add(AntiAim.rotation);
        Client.getValues();
        ValueManager.values.add(AntiAim.noHead);
    }
    
    @Override
    public void onDisabled() {
        super.onDisabled();
        AntiAim.mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(AntiAim.mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
    }
    
    @EventTarget
    public void receivePacket(final EventReceivePacket event) {
        if (this.isToggled() && (AntiAim.mc.thePlayer != null || AntiAim.mc.theWorld != null) && event.getPacket() instanceof S08PacketPlayerPosLook) {
            final S08PacketPlayerPosLook poslook = (S08PacketPlayerPosLook)event.getPacket();
            if (AntiAim.mc.thePlayer.rotationYaw != -180.0f && AntiAim.mc.thePlayer.rotationPitch != 0.0f) {
                poslook.field_148936_d = AntiAim.mc.thePlayer.rotationYaw;
                poslook.field_148937_e = AntiAim.mc.thePlayer.rotationPitch;
            }
        }
    }
    
    @EventTarget(0)
    public void preTick(final EventPreTick e) {
        if (this.isToggled()) {
            if (FreeCam.enabled) {
                return;
            }
            this.oldPitch = AntiAim.mc.thePlayer.rotationPitch;
            this.oldYaw = AntiAim.mc.thePlayer.rotationYaw;
            if (AntiAim.rotation.getBooleanValue()) {
                if (AntiAim.mc.thePlayer.isUsingItem()) {
                    return;
                }
                this.speed += 40;
                AntiAim.mc.thePlayer.rotationYaw = this.speed;
            }
            if (AntiAim.noHead.getBooleanValue()) {
                AntiAim.mc.thePlayer.rotationPitch = 90.0f;
            }
            if (AntiAim.sneak.getBooleanValue()) {
                ++this.delay;
                if (this.delay >= 0) {
                    AntiAim.mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(AntiAim.mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
                }
            }
        }
    }
    
    @EventTarget(4)
    public void postTick(final EventPostTick e) {
        if (this.isToggled()) {
            if (FreeCam.enabled) {
                return;
            }
            AntiAim.mc.thePlayer.rotationPitch = this.oldPitch;
            AntiAim.mc.thePlayer.rotationYaw = this.oldYaw;
            if (AntiAim.sneak.getBooleanValue()) {
                ++this.delay;
                if (this.delay >= 3) {
                    AntiAim.mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(AntiAim.mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
                    this.delay = 0;
                }
            }
        }
    }
}
