package me.rigamortis.faurax.module.modules.misc;

import me.rigamortis.faurax.module.helpers.*;
import me.rigamortis.faurax.module.*;
import net.minecraft.util.*;
import net.minecraft.network.*;
import me.rigamortis.faurax.*;
import com.darkmagician6.eventapi.*;
import net.minecraft.item.*;
import net.minecraft.network.play.client.*;
import me.rigamortis.faurax.events.*;

public class CivBreak extends Module implements WorldHelper
{
    private C07PacketPlayerDigging packet;
    private BlockPos pos;
    private boolean sendClick;
    private float oldPitch;
    private float oldYaw;
    
    public CivBreak() {
        this.sendClick = false;
        this.setKey("");
        this.setName("CivBreak");
        this.setType(ModuleType.PLAYER);
        this.setColor(-2836728);
        this.setModInfo("");
        this.setVisible(true);
    }
    
    @EventTarget(4)
    public void preTick(final EventPreTick e) {
        if (this.isToggled()) {
            this.oldPitch = CivBreak.mc.thePlayer.rotationPitch;
            this.oldYaw = CivBreak.mc.thePlayer.rotationYaw;
            if (this.pos != null) {
                final float distance = MathHelper.sqrt_double(CivBreak.mc.thePlayer.getDistanceSq(this.pos));
                if (distance > 4.0) {
                    if (this.packet != null) {
                        this.packet = null;
                        CivBreak.mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(0, 0, 0), EnumFacing.UP));
                    }
                    this.sendClick = true;
                    CivBreak.mc.playerController.func_180512_c(new BlockPos(0, 0, 0), EnumFacing.UP);
                    this.sendClick = false;
                    return;
                }
            }
            if (this.pos != null) {
                Client.getClientHelper().faceBlock(this.pos.getX() + 0.5f, this.pos.getY() + 0.5f, this.pos.getZ() + 0.5f);
            }
        }
    }
    
    @EventTarget(0)
    public void postTick(final EventPostTick e) {
        if (this.isToggled() && this.pos != null) {
            if (this.pos != null && this.packet != null && this.pos.toString().equals(this.packet.field_179717_a.toString())) {
                CivBreak.mc.getNetHandler().addToSendQueue(new C0APacketAnimation());
                CivBreak.mc.getNetHandler().addToSendQueue(this.packet);
            }
            else {
                this.packet = null;
            }
            if (this.pos != null && this.packet == null) {
                this.sendClick = true;
                CivBreak.mc.getNetHandler().addToSendQueue(new C0APacketAnimation());
                CivBreak.mc.playerController.func_180512_c(this.pos, EnumFacing.UP);
                CivBreak.mc.thePlayer.swingItem();
                this.sendClick = false;
            }
            if (!(CivBreak.mc.thePlayer.getHeldItem().getItem() instanceof ItemBlock)) {
                CivBreak.mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(this.pos, -1, CivBreak.mc.thePlayer.getCurrentEquippedItem(), 0.0f, 0.0f, 0.0f));
            }
            CivBreak.mc.thePlayer.rotationPitch = this.oldPitch;
            CivBreak.mc.thePlayer.rotationYaw = this.oldYaw;
        }
    }
    
    @EventTarget
    public void sendPacket(final EventSendPacket event) {
        if (this.isToggled() && event.getPacket() instanceof C07PacketPlayerDigging) {
            final C07PacketPlayerDigging packet = (C07PacketPlayerDigging)event.getPacket();
            final C07PacketPlayerDigging.Action func_180762_c = packet.func_180762_c();
            packet.func_180762_c();
            if (func_180762_c == C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK) {
                this.packet = (C07PacketPlayerDigging)event.getPacket();
            }
        }
    }
    
    @EventTarget
    public void onClickBlock(final EventClickBlock event) {
        if (this.isToggled() && !this.sendClick) {
            this.pos = event.getBlockPos();
        }
    }
    
    @EventTarget
    public void onDamageBlock(final EventDamageBlock event) {
        this.isToggled();
    }
}
