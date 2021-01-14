package me.Corbis.Execution.module.implementations;

import de.Hero.settings.Setting;
import me.Corbis.Execution.Execution;
import me.Corbis.Execution.event.EventTarget;
import me.Corbis.Execution.event.events.EventAttack;
import me.Corbis.Execution.event.events.EventMotion;
import me.Corbis.Execution.event.events.EventMotionUpdate;
import me.Corbis.Execution.event.events.EventSendPacket;
import me.Corbis.Execution.module.Category;
import me.Corbis.Execution.module.Module;
import me.Corbis.Execution.utils.TimeHelper;
import net.minecraft.block.BlockAir;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;
import optifine.MathUtils;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.Random;

public class Criticals extends Module {
    public Setting mode;
    float FallStack;
    static TimeHelper timer = new TimeHelper();
    public Criticals(){
        super("Criticals", Keyboard.KEY_NONE, Category.COMBAT);
        ArrayList<String> options = new ArrayList<>();
        options.add("Minis");
        options.add("Hypixel");
        Execution.instance.settingsManager.rSetting(mode = new Setting("Criticals Mode", this, "Hypixel", options));
    }
    private boolean isBlockUnder() {
        for (int i = (int) (mc.thePlayer.posY - 1.0); i > 0; --i) {
            BlockPos pos = new BlockPos(mc.thePlayer.posX, i, mc.thePlayer.posZ);
            if (mc.theWorld.getBlockState(pos).getBlock() instanceof BlockAir) continue;
            return true;
        }
        return false;
    }


    public static void criticals(EventMotionUpdate event){
        if(timer.hasReached(350)) {
            final double[] watchdogOffsets = {0.056f, 0.013f, 0.001f};
            for(double i : watchdogOffsets){
                event.setY(event.getY() + i);
            }
            timer.reset();

        }
    }

    public static void postCriticals() {
    }

    @EventTarget
    public void onAttack(EventAttack event){
        if(mode.getValString().equalsIgnoreCase("Minis")){
            if(mc.thePlayer.onGround){
                mc.thePlayer.motionY = 0.11;
            }
        }
    }

    public  double getRandomInRange(double min, double max) {
        Random random = new Random();
        double range = max - min;
        double scaled = random.nextDouble() * range;
        if (scaled > max) {
            scaled = max;
        }
        double shifted = scaled + min;

        if (shifted > max) {
            shifted = max;
        }
        return shifted;
    }

    @EventTarget
    public void onSendPacket(EventSendPacket event){

    }


}
