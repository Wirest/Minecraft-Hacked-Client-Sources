package me.rigamortis.faurax.module.modules.misc;

import me.rigamortis.faurax.module.*;
import me.rigamortis.faurax.events.*;
import net.minecraft.item.*;
import net.minecraft.network.*;
import net.minecraft.network.play.client.*;
import net.minecraft.util.*;
import com.darkmagician6.eventapi.*;

public class NoGlitchBlocks extends Module
{
    public NoGlitchBlocks() {
        this.setType(ModuleType.WORLD);
        this.setName("NoGlitchBlocks");
        this.setKey("");
        this.setModInfo("");
        this.setColor(-6402356);
        this.setVisible(true);
    }
    
    @EventTarget
    public void onDamageBlock(final EventDamageBlock e) {
        if (this.isToggled() && !(NoGlitchBlocks.mc.thePlayer.getHeldItem().getItem() instanceof ItemBlock)) {
            NoGlitchBlocks.mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(e.getBlockPos(), -1, NoGlitchBlocks.mc.thePlayer.getCurrentEquippedItem(), 0.0f, 0.0f, 0.0f));
            NoGlitchBlocks.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(-1, -1, -1), EnumFacing.fromAngle(-1.0)));
        }
    }
    
    @Override
    public void onDisabled() {
        super.onDisabled();
    }
}
