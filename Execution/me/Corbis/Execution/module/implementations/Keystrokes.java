package me.Corbis.Execution.module.implementations;

import me.Corbis.Execution.event.EventTarget;
import me.Corbis.Execution.event.events.Event2D;
import me.Corbis.Execution.module.Category;
import me.Corbis.Execution.module.Module;
import me.Corbis.Execution.ui.UnicodeFontRenderer;
import me.Corbis.Execution.utils.MoveUtils;
import me.Corbis.Execution.utils.RenderingUtil;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.entity.Render;
import org.lwjgl.input.Keyboard;

import java.awt.*;

public class Keystrokes extends Module {

    public Keystrokes(){
        super("Keystrokes", Keyboard.KEY_NONE, Category.RENDER);
    }
    int lastA = 0;
    int lastW = 0;
    int lastS = 0;
    int lastD = 0;
    double lastX = 0;
    double lastZ = 0;
    UnicodeFontRenderer ufr;

    @EventTarget
    public void onRender(Event2D event){
        if(ufr == null){
            ufr = UnicodeFontRenderer.getFontFromAssets("Roboto-Light", 20, 0,1.0f, 1.0f);
        }
        boolean A = mc.gameSettings.keyBindLeft.pressed;
        boolean W = mc.gameSettings.keyBindForward.pressed;
        boolean S = mc.gameSettings.keyBindBack.pressed;
        boolean D = mc.gameSettings.keyBindRight.pressed;
        int alphaA = A ? 255 : 0;
        int alphaW = W ? 255 : 0;
        int alphaS = S ? 255 : 0;
        int alphaD = D ? 255 : 0;

        if(lastA != alphaA){
            float diff = alphaA - lastA;
            lastA += diff / 15;
        }
        if(lastW != alphaW){
            float diff = alphaW - lastW;
            lastW += diff / 15;
        }
        if(lastS != alphaS){
            float diff = alphaS - lastS;
            lastS += diff / 15;
        }
        if(lastD != alphaD){
            float diff = alphaD - lastD;
            lastD += diff / 15;
        }

        Gui.drawRect(5, 50 - 1, 25, 70 - 1, new Color(lastA, lastA, lastA, 150).getRGB());
        ufr.drawCenteredString("A", 15,54 - 1,new Color(flop(lastA, 255), flop(lastA, 255), flop(lastA, 255), 255).getRGB());

        Gui.drawRect(5 + 22, 27, 27 + 20, 47, new Color(lastW, lastW, lastW, 150).getRGB());
        ufr.drawCenteredString("W", 37,32,new Color(flop(lastW, 255), flop(lastW, 255), flop(lastW, 255), 255).getRGB());

        Gui.drawRect(5 + 22, 25 + 24, 27 + 20, 45 + 24, new Color(lastS, lastS, lastS, 150).getRGB());
        ufr.drawCenteredString("S", 37,54,new Color(flop(lastS, 255), flop(lastS, 255), flop(lastS, 255), 255).getRGB());

        Gui.drawRect(5 + 22 + 22, 25 + 24, 27 + 20 + 22, 45 + 24, new Color(lastD, lastD, lastD, 150).getRGB());
        ufr.drawCenteredString("D", 37 + 22,54,new Color(flop(lastD, 255), flop(lastD, 255), flop(lastD, 255), 255).getRGB());

        Gui.drawRect(5, 75, 5 + 64, 75 + 64, new Color(0, 0, 0, 150).getRGB());

        float diffX = mc.thePlayer.rotationYaw - mc.thePlayer.lastYaw;
        float diffY = mc.thePlayer.rotationPitch - mc.thePlayer.lastPitch;


        Gui.drawRect(5, 74 + ((75+64) - 75) / 2, 5 + 64, 75 + ((75+64) - 74) / 2, new Color(255, 255, 255, 150).getRGB());
        Gui.drawRect(3 + ((5 + 64) / 2), 75, 4 + ((5 + 64) / 2), 75 + 64, new Color(255, 255, 255, 150).getRGB());
        RenderingUtil.drawCircle(4 + (int)diffX / 3 + ((5 + 64) / 2), 74 + (int)diffY / 3 + ((75+64) - 75) / 2, 7, 6, new Color(255, 255, 255, 200).getRGB());

        //motion

        Gui.drawRect(5, 75 + 66, 5 + 64, 75 + 64 + 66, new Color(0, 0, 0, 150).getRGB());
        Gui.drawRect(5, 74 + 66 + ((75+64) - 75) / 2, 5 + 64, 75  + 66 + ((75+64) - 74) / 2, new Color(255, 255, 255, 150).getRGB());
        Gui.drawRect(3 + ((5 + 64) / 2), 75 + 66, 4 + ((5 + 64) / 2), 75 + 64 + 66, new Color(255, 255, 255, 150).getRGB());
        double prevX = mc.thePlayer.posX - mc.thePlayer.prevPosX;
        double prevZ = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
        double lastDist = Math.sqrt(prevX * prevX + prevZ * prevZ);
        double motionX = mc.thePlayer.moveStrafing * (lastDist * 200);
        double motionZ = mc.thePlayer.moveForward  * (lastDist * 200);
        lastX += (motionX - lastX) / 4;
        lastZ += (motionZ - lastZ) / 4;
        RenderingUtil.drawCircle(4 + (int)lastX / 3 + ((5 + 64) / 2), 74 + (int)lastZ / 3  + 66 + ((75+64) - 74) / 2,  7, 50, new Color(255, 255, 255, 200).getRGB());


        double currSpeed = lastDist * 15.3571428571;
        String speed = String.format("BPS: %.2f", currSpeed);
        ufr.drawString(speed,6, 75+66+66, 0xFFFFFFFF);
        /*
        Diviners - Savannah (feat. Philly K) [NCS Release]
Different Heaven - Far Away [NCS Release]
         */
    }

    public int flop(int a, int b){
        return b - a;
    }
}
