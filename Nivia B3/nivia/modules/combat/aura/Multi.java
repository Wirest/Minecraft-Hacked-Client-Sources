package nivia.modules.combat.aura;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
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
import nivia.utils.utils.Timer;


public class Multi extends AuraMode {

    public Multi(KillAura killAura) {
        super("Multi", killAura);
    }

    public static boolean attack;
    public float yaw, pitch;
    public int ticks;
    public int delay;
    public boolean override, nextTick;
    public int currentTarget;
    public Timer attackTimer = new Timer();

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

        if(ka.canBlock() && !mc.gameSettings.keyBindUseItem.getIsKeyPressed())
            mc.thePlayer.setItemInUse(mc.thePlayer.getCurrentEquippedItem(), 71999);

        yaw = e.getYaw();
        pitch = e.getPitch();
        if (ka.lockview.value) {
            mc.thePlayer.rotationYaw = yaw;
            mc.thePlayer.rotationPitch = pitch;
        }
    }
    @Override
    public void onRunTick(EventTick e) {

    }
    public void doAttack(Entity target) {
        if (Helper.player().isBlocking())
            Helper.sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(0, 0, 0), EnumFacing.UP));
        ka.nextTick = true;
        Criticals.doCrit();
        if(ka.noswing.value) Helper.sendPacket(new C0APacketAnimation());
        else Helper.player().swingItem();
        Helper.sendPacket(new C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK));
    }
    private void changeTargets(){
        if(ka.attackList.size() == 1)
            return;
        currentTarget += 1;
    }
   
    @Override
    public void onPostMotion(EventPostMotionUpdates e) {
        if (!attack)
            return;
        if (attackTimer.hasTimeElapsed((long) ka.multiDelay.getValue(), true) || override) {
            for (int i = 0; i < ka.attackList.size(); i++) {
                if (i > ka.targets.getValue()) break;
                doAttack(getTarget());
                changeTargets();
            }
            nextTick = !nextTick;
            override = false;
        }
        if (ka.canBlock() || mc.thePlayer.isBlocking())
            mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.getCurrentEquippedItem()));
    }

    @Override
    public void onPacketSend(EventPacketSend e) {

    }
    @Override
    public  EntityLivingBase getTarget() {
        return (EntityLivingBase) ka.attackList.get(currentTarget);
    }
    public boolean canAttack() {
        if (!ka.attackList.isEmpty() && !Pandora.getModManager().getModState("Freecam") && getTarget() != null && !AutoPot.doPot)
            return true;
        return false;
    }
    @Override
    public void onDisabled() {
        currentTarget = ticks = delay = 0;
        attack = false;
        override = true;
    }

    @Override
    public void onEnabled() {

    }

}
