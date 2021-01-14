package me.Corbis.Execution.Replay;

import net.minecraft.client.Minecraft;

import java.util.ArrayList;

public class ReplayManager {

    public long elapsed = 0;
    public ArrayList<Replay> replays = new ArrayList<>();
    public void playReplay(){

        Replay currentReplay = replays.get(0);
        Minecraft mc = Minecraft.getMinecraft();
        elapsed++;
        for(Point p : currentReplay.points){
            if(p.timeElapsed == elapsed){
                mc.thePlayer.setPosition(p.posX, p.posY, p.posZ);
                mc.thePlayer.rotationYaw = p.rotationYaw;
                mc.thePlayer.rotationPitch = p.rotationYaw;
            }
        }


    }

    public void init(){
        Replay currentReplay = replays.get(0);
        Minecraft.getMinecraft().loadWorld(currentReplay.world);
    }

}
