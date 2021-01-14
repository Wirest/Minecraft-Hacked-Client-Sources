package cedo.modules.combat;

import cedo.Fan;
import cedo.events.Event;
import cedo.events.listeners.EventMotion;
import cedo.events.listeners.EventPacket;
import cedo.modules.Module;
import cedo.settings.impl.NumberSetting;
import cedo.util.Timer;
import cedo.util.movement.MovementUtil;
import net.minecraft.block.BlockAir;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.BlockPos;
import org.lwjgl.input.Keyboard;

public class Criticals extends Module {

    public Timer timer = new Timer();
    public int groundTicks;
    private double[] offsets = new double[]{0.051, 0.011511, 0.001, 0.001};
    public NumberSetting critTicks = new NumberSetting("Crit Ticks", 250, 10,600, 10);

    public Criticals() {
        super("Criticals", Keyboard.KEY_NONE, Category.COMBAT);
        addSettings(critTicks);
    }


    public void onEvent(Event e) {
        if(Fan.targetHud.size.is("Script")){
            this.setSuffix("\247cMichealXF");
        }else
            this.setSuffix("");
        if (e instanceof EventMotion && e.isPre()) {
            if (mc.thePlayer.onGround)
                groundTicks++;
            else
                groundTicks = 0;
        }
        if (e instanceof EventPacket && e.isOutgoing()) {
            if (((EventPacket) e).getPacket() instanceof C0APacketAnimation) {
                boolean falseModules = Fan.speed.isEnabled() || Fan.fly.isEnabled();
                boolean canCrit = !falseModules && mc.thePlayer.onGround &&
                        !mc.gameSettings.keyBindJump.isKeyDown() && !mc.thePlayer.isInWater() &&
                        !mc.thePlayer.isOnLadder() && MovementUtil.getBlockAtPos(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY + 1, mc.thePlayer.posZ)) instanceof BlockAir;
                if (canCrit)
                    if (Fan.killaura.target != null || mc.objectMouseOver.entityHit != null)
                        this.crit();
            }
        }
    }

    private void crit() {
        if (timer.hasTimeElapsed((long) critTicks.getValue(), true) && groundTicks > 1) {
            for (double offset : offsets)
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + offset, mc.thePlayer.posZ, false));
        }
    }

}
