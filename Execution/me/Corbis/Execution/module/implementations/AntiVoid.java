package me.Corbis.Execution.module.implementations;

import de.Hero.settings.Setting;
import me.Corbis.Execution.Execution;
import me.Corbis.Execution.event.EventTarget;
import me.Corbis.Execution.event.events.EventMotionUpdate;
import me.Corbis.Execution.module.Category;
import me.Corbis.Execution.module.Module;
import me.Corbis.Execution.utils.Position;
import me.Corbis.Execution.utils.TimeHelper;
import net.minecraft.block.BlockAir;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

public class AntiVoid extends Module {
    public Setting mode;
    public AntiVoid(){
        super("AntiVoid", Keyboard.KEY_Y, Category.MOVEMENT );
        ArrayList<String> options = new ArrayList<>();
        options.add("Hypixel");
        options.add("Cubecraft");
        options.add("Mineplex");
        Execution.instance.settingsManager.rSetting(mode = new Setting("AntiVoid mode",  this, "Hypixel", options));
    }
    TimeHelper timer = new TimeHelper();
    Position prevPos;
    public void damagePlayer(){
        for (int i = 0; i < 10; i++) //Imagine flagging to NCP.
            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));

        double fallDistance = 3.0125; //add 0.0125 to ensure we get the fall dmg
        while (fallDistance > 0) {
            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.0624986421, mc.thePlayer.posZ, false));
            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.0625      , mc.thePlayer.posZ, false));
            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.0624986421, mc.thePlayer.posZ, false));
            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.0000013579, mc.thePlayer.posZ, false));
            fallDistance -= 0.0624986421;
        }
        mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));

        mc.thePlayer.jump();
        mc.thePlayer.posY += 0.42F; // Visual
    }
    public void damagePlayerCubecraft(){
        if(mc.thePlayer.onGround) {
            double x = mc.thePlayer.posX;
            double y = mc.thePlayer.posY;
            double z = mc.thePlayer.posZ;

            for(int i = 0; i < 65; i++) {
                mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.049, z, false));
                mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false));
            }
            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, true));
        }
    }
    @EventTarget
    public void onMotionUpdate(EventMotionUpdate event){
        if(event.isPre()) {
            if(mode.getValString().equalsIgnoreCase("Hypixel")) {


                if (getMc().thePlayer.fallDistance > 4 && !isBlockUnder() && !Execution.instance.moduleManager.getModule(HighJump.class).isEnabled && !Execution.instance.moduleManager.getModule(Flight.class).isEnabled && timer.hasReached(250) && !Execution.instance.moduleManager.getModule(Longjump.class).isEnabled) {
                    mc.getNetHandler().addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 5, mc.thePlayer.posZ, false));
                    timer.reset();
                }
            }else if(mode.getValString().equalsIgnoreCase("Cubecraft")){
                if(mc.thePlayer.fallDistance > 3 && !isBlockUnder()) {
                    mc.thePlayer.motionY = -0.1;

                }
            }else {
                if(mc.thePlayer.fallDistance > 3 && !isBlockUnder()){
                    if(!timer.hasReached(3000)){
                        mc.thePlayer.motionY = -10;
                    }else {
                        mc.thePlayer.setPosition(prevPos.getX(), prevPos.getY() + 5, prevPos.getZ() );
                    }
                }else {
                    if(mc.thePlayer.onGround){
                        prevPos = mc.thePlayer.getPositionAsPosition();
                        timer.reset();
                    }

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
