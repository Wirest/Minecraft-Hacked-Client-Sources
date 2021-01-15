package nivia.modules.combat.aura;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.*;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import nivia.Pandora;
import nivia.events.events.EventPacketSend;
import nivia.events.events.EventPostMotionUpdates;
import nivia.events.events.EventPreMotionUpdates;
import nivia.events.events.EventTick;
import nivia.modules.combat.AutoPot;
import nivia.modules.combat.Criticals;
import nivia.modules.combat.Criticals.CritMode;
import nivia.modules.combat.KillAura;
import nivia.modules.combat.KillAura.AuraMode;
import nivia.modules.movement.NoSlow;
import nivia.utils.Helper;
import nivia.utils.Logger;
import nivia.utils.utils.Timer;

import java.util.Objects;

public class Switch extends AuraMode {

    public Switch(KillAura killAura) {
        super("Switch", killAura);
    }
    public static boolean attack;
    public float yaw, pitch;
    public int ticks;
    public int delay;
    public boolean override;
    public int currentTarget;
    public static Timer potTimer = new Timer();
    public static Timer duraTimer = new Timer();
    public static Timer critTimer = new Timer();

    @Override
    public void onPreMotion(EventPreMotionUpdates e) {
        if (currentTarget >= ka.attackList.size())
            currentTarget = 0;
        ticks++;
        if (ticks > 20) ticks = 20;
        if (!canAttack()) {
            attack = false;
            ka.nextTick = false;
            override = true;
            return;
        }
        ka.nextTick = false;
        ka.sortTargets();
        attack = true;

        e.setYaw(Helper.combatUtils().faceTarget(getTarget(), 1000, 1000, false)[0]);
        e.setPitch(Helper.combatUtils().faceTarget(getTarget(), 1000, 1000, false)[1]);
        if(mc.thePlayer.getDistanceToEntity(getTarget()) <= 0.39) {
            e.setPitch(90F);
        }
        yaw = e.getYaw();
        pitch = e.getPitch();
        if (ka.lockview.value) {
            mc.thePlayer.rotationYaw = yaw;
            mc.thePlayer.rotationPitch = pitch;
        }
    }
    @Override
    public void onRunTick(EventTick e) {
        if(attack) {
            boolean useRandom = ka.rnd.getValue() > 0;
            int cd = Criticals.getCrits().cMode.value.equals(CritMode.PACKET) ? 490 : 0;
            double attackDelay = useRandom ? (ka.APS.getValue() - Helper.mathUtils().getRandom((int) ka.rnd.getValue())) : ka.APS.getValue();
            if (ticks >= (20 / attackDelay)) {
                delay++;
                if (critTimer.hasTimeElapsed((long) cd, true)) {
                    if (Criticals.getCrits().cMode.value.equals(Criticals.CritMode.PACKET) && !ka.armorbreaker.value)
                        Criticals.doCrit();
                    else ka.nextTick = true;
                }
                if (potTimer.hasTimeElapsed(10, false))
                    doAttack(getTarget());
                ticks = 0;
                changeTargets();
            }
        }
        if (ka.canBlock() && (ka.hasPlayerNear() || !ka.attackList.isEmpty()))
            mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getCurrentEquippedItem());
    }
    public void doAttack(Entity target) {
        if(mc.thePlayer.isBlocking() && !NoSlow.noslowing)
            Helper.sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.UP));

        if (duraTimer.hasTimeElapsed(370L, true) && target instanceof EntityPlayer && ka.armorbreaker.value) {
            ka.nextTick = false;
            ka.doCritAttack(target, false);
            ka.doCritAttack(target, true);
            return;
        }
        Helper.player().swingItem();
        Helper.sendPacket(new C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK));
    }
    private void changeTargets(){
        if(ka.attackList.size() == 1)
            return;
        if(ka.sdelay.getValue() == 0)
            currentTarget += 1;
        if(delay >= ka.sdelay.getValue()){
            currentTarget += 1;
            delay = 0;
        }
        attack = false;
    }
    public  EntityLivingBase getTarget() {
        return (EntityLivingBase) ka.attackList.get(currentTarget);
    }
    @Override
    public void onPostMotion(EventPostMotionUpdates en) {

    }

    @Override
    public void onPacketSend(EventPacketSend e) {

    }


    @Override
    public void onDisabled() {
        currentTarget = ticks = 0;
        attack = false;
        override = true;
        if(!Helper.playerUtils().MovementInput())
            Helper.sendPacket(new C03PacketPlayer.C05PacketPlayerLook(mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, false));
    }

    @Override
    public void onEnabled() {

    }
    public boolean canAttack() {
        if (!ka.attackList.isEmpty() && !Pandora.getModManager().getModState("Freecam") && getTarget() != null && !AutoPot.doPot)
            return true;
        return false;
    }

    public void updateProperties() {
        if(ka.mode.value.equals(this)) {
            Pandora.getPropertyManager().addProperty(ka.APS);
            Pandora.getPropertyManager().addProperty(ka.rnd);
            Pandora.getPropertyManager().addProperty(ka.sdelay);
        } else {
            Pandora.getPropertyManager().removeProperty(ka.APS);
            Pandora.getPropertyManager().removeProperty(ka.rnd);
            Pandora.getPropertyManager().removeProperty(ka.sdelay);
        }
        if (ka.mode.value.equals(ka.multiMode)) {
        	Pandora.getPropertyManager().addProperty(ka.multiDelay);
        	Pandora.getPropertyManager().addProperty(ka.targets);
        } else {
        	Pandora.getPropertyManager().removeProperty(ka.multiDelay);
        	Pandora.getPropertyManager().removeProperty(ka.targets);
        }
    }
}
