package me.Corbis.Execution.module.implementations;

import de.Hero.settings.Setting;
import me.Corbis.Execution.Execution;
import me.Corbis.Execution.event.EventTarget;
import me.Corbis.Execution.event.events.EventRenderScoreboard;
import me.Corbis.Execution.module.Category;
import me.Corbis.Execution.module.Module;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.scoreboard.Scoreboard;
import org.lwjgl.input.Keyboard;

public class ScoreboardMover extends Module
{

    public Setting x;
    public Setting y;

    public ScoreboardMover(){
        super("ScoreboardMover", Keyboard.KEY_NONE, Category.RENDER);
        Execution.instance.settingsManager.rSetting(x = new Setting("Scoreboard X", this, 0, 0, 1920, true));
        Execution.instance.settingsManager.rSetting(y = new Setting("Scoreboard Y", this, 0, 0, 1920, true));
    }

    @EventTarget
    public void onRenderScoreboard(EventRenderScoreboard event){
        if(event.isPre()){
            GlStateManager.translate(-x.getValDouble(), y.getValDouble(), 1.0D);
        }else {
            GlStateManager.translate(x.getValDouble(), -y.getValDouble(), 1.0D);
        }


    }

}
