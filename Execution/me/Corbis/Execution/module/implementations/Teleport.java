package me.Corbis.Execution.module.implementations;

import de.Hero.settings.Setting;
import me.Corbis.Execution.Execution;
import me.Corbis.Execution.event.EventTarget;
import me.Corbis.Execution.event.events.EventMotion;
import me.Corbis.Execution.event.events.EventMotionUpdate;
import me.Corbis.Execution.event.events.EventReceivePacket;
import me.Corbis.Execution.module.Category;
import me.Corbis.Execution.module.Module;
import me.Corbis.Execution.ui.Notifications.Notification;
import me.Corbis.Execution.ui.Notifications.NotificationType;
import me.Corbis.Execution.utils.AStarCustomPathFinder;
import me.Corbis.Execution.utils.MoveUtils;
import me.Corbis.Execution.utils.Position;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Teleport extends Module {
    boolean canFly = false;
    int stage = 0;
    public Setting mode;
    BlockPos pos;
    public Teleport(){
        super("Teleport", Keyboard.KEY_I, Category.MOVEMENT);
        ArrayList<String> options = new ArrayList<>();
        options.add("Hypixel");
        options.add("Vanilla");
        Execution.instance.settingsManager.rSetting(mode = new Setting("Teleport mode", this, "Vanilla", options));
    }



    @EventTarget
    public void onMotion(EventMotion event){
        if(mode.getValString().equalsIgnoreCase("Hypixel")) {
            if (!canFly) {
                mc.timer.timerSpeed = 32767f;
                canFly = true;
            } else {
                mc.timer.timerSpeed = 1.0f;
                canFly = false;
                this.toggle();
            }
        }else {
            MovingObjectPosition mop = mc.thePlayer.rayTrace(200, 1.0f);
            if(Mouse.isButtonDown(1)){
                if(mop.getBlockPos().getBlock() instanceof BlockAir){
                    return;
                }
                mc.thePlayer.setPositionAndUpdate(mop.getBlockPos().getX(), mop.getBlockPos().getY() + 1, mop.getBlockPos().getZ());
            }
        }
    }





    @Override
    public void onEnable() {
        super.onEnable();
    }
}
