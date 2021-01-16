/*
 * Decompiled with CFR 0_122.
 */
package me.razerboy420.weepcraft.module.modules.combat;

import darkmagician6.EventTarget;
import darkmagician6.events.EventPrePlayerUpdate;
import java.util.Random;
import me.razerboy420.weepcraft.module.Module;
import me.razerboy420.weepcraft.util.Wrapper;
import me.razerboy420.weepcraft.value.Value;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;

@Module.Mod(category=Module.Category.COMBAT, description="Clicks for you", key=0, name="AutoClicker")
public class AutoClicker
extends Module {
    public static Value cps = new Value("autoclicker_CPS", Float.valueOf(8.0f), Float.valueOf(2.0f), Float.valueOf(20.0f), Float.valueOf(0.1f));
    public static Value randomization = new Value("autoclicker_Randomization", Float.valueOf(3.0f), Float.valueOf(0.0f), Float.valueOf(5.0f), Float.valueOf(0.1f));
    public static Value block = new Value("autoclicker_Block", true);
    public static Value autoaps = new Value("autoclicker_AutoAPS", true);
    public int ticks;
    double random;
    public boolean negative;

    @EventTarget
    public void onUpdate(EventPrePlayerUpdate event) {
        boolean isOverAMan = Minecraft.getMinecraft().objectMouseOver != null && Minecraft.getMinecraft().objectMouseOver.entityHit instanceof EntityLivingBase;
        Random r = new Random();
        ++this.ticks;
        if (Wrapper.mc().gameSettings.keyBindAttack.pressed) {
            if (AutoClicker.autoaps.boolvalue) {
                if (Wrapper.getPlayer().getCooledAttackStrength(0.0f) == 1.0f) {
                    Wrapper.getPlayer().swingArm(EnumHand.MAIN_HAND);
                    if (isOverAMan) {
                        if (Wrapper.getPlayer().isActiveItemStackBlocking()) {
                            Wrapper.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                        }
                        Wrapper.mc().playerController.attackEntity(Wrapper.getPlayer(), Minecraft.getMinecraft().objectMouseOver.entityHit);
                    }
                    this.random = Math.random() * (double)AutoClicker.randomization.value.floatValue();
                    this.negative = r.nextBoolean();
                    this.ticks = 0;
                }
            } else if ((double)this.ticks >= 20.0 / ((double)AutoClicker.cps.value.floatValue() + (this.negative ? - this.random : this.random))) {
                Wrapper.getPlayer().swingArm(EnumHand.MAIN_HAND);
                if (isOverAMan) {
                    if (Wrapper.getPlayer().isActiveItemStackBlocking()) {
                        Wrapper.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                    }
                    Wrapper.mc().playerController.attackEntity(Wrapper.getPlayer(), Minecraft.getMinecraft().objectMouseOver.entityHit);
                }
                this.random = Math.random() * (double)AutoClicker.randomization.value.floatValue();
                this.negative = r.nextBoolean();
                this.ticks = 0;
            }
        }
    }
}

