package skyline.specc.mods.move.fly;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialTransparent;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.EventMotion;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.EventPlayerUpdate;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.main.EventListener;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.main.EventType;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.ModMode;
import skyline.specc.mods.move.Fly;
import skyline.specc.utils.UtilClient;

public class Hypixel extends ModMode<Fly>
{

	public Hypixel(Fly parent, String name) {
		super(parent, name);
	}
	
	
	
	@Override
    public void onDisable() {
    }
	
	@EventListener
    public void onMotionUpdate(EventMotion event) {
        mc.thePlayer.motionY = 0;
        if (mc.thePlayer.ticksExisted % 2 == 0 && !mc.thePlayer.isCollidedVertically) {
            mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY - 1.28E-11D, mc.thePlayer.posZ);
        } else {
            mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 1.28E-10D, mc.thePlayer.posZ);
        }
    }
	public static double getBaseMoveSpeed() {
        double baseSpeed = 0.2793;
        if (p.isPotionActive(Potion.moveSpeed)) {
            final int amplifier = p.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (amplifier + 1);
        }
        return baseSpeed;
    }
}
