package skyline.specc.mods.move.fly;

import static net.minecraft.client.Mineman.thePlayer;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.MoveEvents.EventMoveRaw;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialTransparent;
import net.minecraft.client.Mineman;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.EventMotion;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.EventPlayerUpdate;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.main.EventListener;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.main.EventType;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.ModMode;
import skyline.specc.SkyLine;
import skyline.specc.mods.combat.KillAuraMod;
import skyline.specc.mods.move.Fly;
import skyline.specc.utils.UtilClient;

public class Ghostly extends ModMode<Fly>
{

	public Ghostly(Fly parent, String name) {
		super(parent, name);
	}

    @Override
    public void onEnable() {
        if (p.onGround) {
            p.setPosition(p.posX, p.posY + 1.2, p.posZ);
        }
    }

    @EventListener
    public void onMotionUpdate(EventMotion event) {
        if (mc.thePlayer.isMoving()) {
            mc.thePlayer.onGround = true;
        }
        mc.thePlayer.setSpeed(mc.thePlayer.getSpeed() + 0.002F);
        mc.timer.timerSpeed = 1.12F;
        mc.thePlayer.motionY = (p.ticksExisted % 80 == 0 ? +0.4F : -0.01F);
        if (mc.thePlayer.ticksExisted % 80 == 0) {
        	event.setPosY(-0.8);
        }
    }
    public static double getBaseMoveSpeed() {
        double baseSpeed = 0.2873;
        if (p.isPotionActive(Potion.moveSpeed)) {
            final int amplifier = p.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (amplifier + 1);
        }
        return baseSpeed;
    }

}