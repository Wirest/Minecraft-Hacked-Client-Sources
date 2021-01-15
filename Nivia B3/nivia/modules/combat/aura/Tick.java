package nivia.modules.combat.aura;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import nivia.Pandora;
import nivia.events.events.EventPacketSend;
import nivia.events.events.EventPostMotionUpdates;
import nivia.events.events.EventPreMotionUpdates;
import nivia.events.events.EventTick;
import nivia.modules.combat.AutoPot;
import nivia.modules.combat.Criticals;
import nivia.modules.combat.KillAura;
import nivia.modules.combat.KillAura.AuraMode;
import nivia.utils.Helper;
import nivia.utils.Logger;
import nivia.utils.utils.Timer;


public class Tick extends AuraMode {

 
	public Tick(KillAura killAura) {
        super("Tick", killAura);
    }

    public static boolean attack;
    public float yaw, pitch;
    public int ticks;
    private  boolean potted, nextHit;
    private boolean override;
    private int currentTarget;
    private Timer attackTimer = new Timer();

    @Override
    public void onPreMotion(EventPreMotionUpdates e) {
        if (currentTarget >= ka.attackList.size())
            currentTarget = 0;
        if (!canAttack()) {
            attack = false;
            ka.nextTick = false;
            override = true;
            return;
        }

        if(AutoPot.doPot) {
            potted = true;
            return;
        }

        ka.sortTargets();
        attack = true;
        e.setYaw(Helper.combatUtils().faceTarget(getTarget(), 1000, 1000, false)[0]);
        e.setPitch(Helper.combatUtils().faceTarget(getTarget(), 1000, 1000, false)[1]);

        yaw = e.getYaw();
        pitch = e.getPitch();
        if (ka.lockview.value) {
            mc.thePlayer.rotationYaw = yaw;
            mc.thePlayer.rotationPitch = pitch;
        }
        ka.nextTick = false;
    }
    @Override
    public void onPostMotion(EventPostMotionUpdates e) {

    }
    int d = 467, i = 0;
    @Override
    public void onRunTick(EventTick e) {
        int delay = 493;
        if (attack) {
            if (attackTimer.hasTimeElapsed(delay, true) || override) {
                ka.nextTick = true;
                doAttack(getTarget());
                changeTargets();
                override = potted = false;
            }
        }
        if(ka.canBlock() &&  (ka.hasPlayerNear() || !ka.attackList.isEmpty()))
            mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getCurrentEquippedItem());
    }
    public void doAttack(Entity target) {
        if(AutoPot.doPot)
            return;

        if (ka.armorbreaker.value) {
            Helper.mc().thePlayer.swingItem(); if(ka.noswing.value) mc.thePlayer.swingProgressInt = 10;
            Helper.inventoryUtils().swap(9, mc.thePlayer.inventory.currentItem);
            ka.doCritAttack(target, false);
            ka.doCritAttack(target, false);
            ka.doCritAttack(target, true);
            Helper.inventoryUtils().swap(9, mc.thePlayer.inventory.currentItem);
            ka.doCritAttack(target, false);
            ka.doCritAttack(target, true);
        } else {
            if (Helper.player().isBlocking())
                Helper.sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(0, 0, 0), EnumFacing.UP));
            Criticals.doCrit();
            Helper.player().swingItem();
            Helper.sendPacket(new C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK));
        }
    }
    public boolean canAttack() {
        if (!ka.attackList.isEmpty() && !Pandora.getModManager().getModState("Freecam") && getTarget() != null)
            return true;
        return false;
    }

    private void changeTargets(){
        if(ka.attackList.size() == 1)
            return;
        currentTarget += 1;
    }
    @Override
    public EntityLivingBase getTarget() {
        return (EntityLivingBase) ka.attackList.get(currentTarget);
    }

    @Override
    public void onPacketSend(EventPacketSend e) {

    }


    @Override
    public void onDisabled() {
        currentTarget = ticks = 0;
        attack = false;
        override = true;
    }

    @Override
    public void onEnabled() {

    }

}