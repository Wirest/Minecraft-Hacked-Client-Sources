/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.slowly.client.mod.mods.world;

import com.darkmagician6.eventapi.EventTarget;
import me.slowly.client.events.EventPreMotion;
import me.slowly.client.mod.Mod;
import me.slowly.client.ui.notifiactions.ClientNotification;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.Colors;
import me.slowly.client.util.PlayerUtil;
import me.slowly.client.util.TimeHelper;
import me.slowly.client.value.Value;
import net.minecraft.block.Block;
import net.minecraft.block.BlockWeb;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.BlockPos;

public class NoWeb
extends Mod {
    private Value<Boolean> up = new Value<Boolean>("NoWeb_Glide", true);
    private Value<Double> speed = new Value<Double>("NoWeb_Speed", 0.45, 0.1, 0.6, 0.01);
    private Value<Double> y = new Value<Double>("NoWeb_posY", 0.3, 0.0, 1.0, 0.01);
    private TimeHelper timer = new TimeHelper();

    public NoWeb() {
        super("NoWeb", Mod.Category.WORLD, Colors.WHITE.c);
    }

    @EventTarget
    public void onPre(EventPreMotion event) {
        this.setColor(Colors.DARKBLUE.c);
        BlockPos pos = new BlockPos(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 0.5, this.mc.thePlayer.posZ);
        if (this.mc.thePlayer.isInWeb) {
            if (PlayerUtil.MovementInput()) {
                PlayerUtil.setSpeed(this.mc.thePlayer.onGround ? this.speed.getValueState() : this.speed.getValueState() * 2.0);
            }
            if (this.up.getValueState().booleanValue() && this.mc.thePlayer.onGround && this.mc.theWorld.getBlockState(pos).getBlock() instanceof BlockWeb) {
                this.mc.thePlayer.setPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + this.y.getValueState(), this.mc.thePlayer.posZ);
                this.timer.reset();
            }
            if (!this.mc.thePlayer.onGround) {
                this.mc.thePlayer.motionY = 0.0;
            }
            if (this.mc.thePlayer.isCollidedHorizontally && PlayerUtil.MovementInput()) {
                this.mc.thePlayer.setPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 0.1, this.mc.thePlayer.posZ);
            }
        }
    }
    @Override
    public void onDisable() {
        super.onDisable();
        ClientUtil.sendClientMessage("NoWeb Disable", ClientNotification.Type.ERROR);
    }
    public void onEnable() {
    	super.isEnabled();
        ClientUtil.sendClientMessage("NoWeb Enable", ClientNotification.Type.SUCCESS);
    }
}

