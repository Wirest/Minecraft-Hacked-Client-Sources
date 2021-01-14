package me.Corbis.Execution.module.implementations;

import de.Hero.settings.Setting;
import me.Corbis.Execution.Execution;
import me.Corbis.Execution.event.Event;
import me.Corbis.Execution.event.EventTarget;
import me.Corbis.Execution.event.events.EventMotionUpdate;
import me.Corbis.Execution.module.Category;
import me.Corbis.Execution.module.Module;
import net.minecraft.block.BlockAir;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;
import org.lwjgl.input.Keyboard;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class NoFall extends Module {
    public Setting mode;
    public NoFall(){
        super("NoFall", Keyboard.KEY_N, Category.PLAYER);
        ArrayList<String> options = new ArrayList<>();
        options.add("Hypixel");
        options.add("Edit");
        Execution.instance.settingsManager.rSetting(mode = new Setting("NoFall mode", this, "Hypixel", options));
    }

    @EventTarget
    public void onMotionUpdate(EventMotionUpdate event){
        this.setDisplayName("NoFall §f[" + mode.getValString() + "]");
        if(event.getState() == Event.State.PRE){
            if(mode.getValString().equalsIgnoreCase("Hypixel")) {
                if(mc.thePlayer.fallDistance > 3 && (isBlockUnder() || Execution.instance.moduleManager.getModule(HighJump.class).isEnabled) && !Execution.instance.moduleManager.getModule(Longjump.class).isEnabled){
                    event.setOnGround(false);
                    mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
                }
            }else {
                if(mc.thePlayer.fallDistance > 2.9){
                    event.setOnGround(mc.thePlayer.ticksExisted % 2 == 0);

                }
            }
        }
    }
    private boolean isBlockUnder() {
        for (int i = (int) (mc.thePlayer.posY - 1.0); i > 0; --i) {
            BlockPos pos = new BlockPos(mc.thePlayer.posX, i, mc.thePlayer.posZ);
            if (mc.theWorld.getBlockState(pos).getBlock() instanceof BlockAir) continue;
            return true;
        }
        return false;
    }
}
