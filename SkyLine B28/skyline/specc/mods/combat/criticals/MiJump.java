package skyline.specc.mods.combat.criticals;

import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.util.EnumParticleTypes;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.EventMotion;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.eventtypes.EventPacket;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.main.EventListener;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.ModMode;
import skyline.specc.SkyLine;
import skyline.specc.mods.combat.Criticals;
import skyline.specc.mods.move.Fly;
import skyline.specc.mods.move.NoSlowMod;
import skyline.specc.utils.TimerUtils;

import static net.minecraft.client.Mineman.thePlayer;

import net.minecraft.client.Mineman;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class MiJump extends ModMode<Criticals>{

	public MiJump(Criticals parent, String name) {
		super(parent, name);
	}
    private Mineman mc = Mineman.getMinecraft();
    private TimerUtils timer = new TimerUtils();

    @EventListener
    public void onAttack(EventPacket e, EntityLivingBase entity,EventMotion event) {
        if (((e.getPacket() instanceof C02PacketUseEntity))) {
        	if (mc.thePlayer.isCollidedVertically) {
                mc.thePlayer.motionY = 0.3D;
        		mc.thePlayer.jump();
        		event.setGround(false);
        		event.setGround(true);
                mc.effectRenderer.func_178926_a(entity, EnumParticleTypes.CRIT);
            }
        }
    }
}