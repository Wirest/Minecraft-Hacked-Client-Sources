package me.Corbis.Execution.Replay;

import me.Corbis.Execution.Execution;
import me.Corbis.Execution.utils.MoveUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;

import java.util.ArrayList;

public class Replay {
    boolean stopped = false;
    public WorldClient world;
    Minecraft mc = Minecraft.getMinecraft();
    ArrayList<Point> points = new ArrayList<>();
    long timeElapsed = 0;

    public Replay(WorldClient world) {
        stopped = false;
        timeElapsed = 0;
        this.world = world;
    }

    public void recordSession(WorldClient world, EntityPlayerSP thePlayer){
        if(world == null){
            save();
            stopped = true;
            Execution.instance.isRecording = false;
            return;
        }
        if(stopped){
            return;
        }
        timeElapsed++;
        if(MoveUtils.isMoving() || (thePlayer.rotationYaw - thePlayer.lastReportedYaw) != 0 || (thePlayer.rotationPitch - thePlayer.lastReportedPitch) != 0){
            this.points.add(new Point(thePlayer.posX, thePlayer.posY, thePlayer.posZ, thePlayer.rotationYaw, thePlayer.rotationPitch, timeElapsed));
        }
    }

    public void save(){

    }
}

