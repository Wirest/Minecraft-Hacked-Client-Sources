package me.rigamortis.faurax.module.modules.render;

import me.rigamortis.faurax.module.*;
import me.rigamortis.faurax.events.*;
import net.minecraft.potion.*;
import com.darkmagician6.eventapi.*;
import me.rigamortis.faurax.*;
import net.minecraft.client.settings.*;

public class Brightness extends Module implements Runnable
{
    public static float oldGamma;
    public static boolean state;
    
    public Brightness() {
        this.setName("Brightness");
        this.setKey("");
        this.setType(ModuleType.RENDER);
        this.setColor(-15096001);
        this.setModInfo("");
        this.setVisible(false);
    }
    
    @Override
    public void onEnabled() {
        Brightness.oldGamma = Brightness.mc.gameSettings.gammaSetting;
        Brightness.state = true;
        new Thread(new Brightness()).start();
    }
    
    @Override
    public void onDisabled() {
        Brightness.state = false;
        new Thread(new Brightness()).start();
    }
    
    @EventTarget
    public void preTick(final EventPreTick e) {
        if (this.isToggled()) {
            Brightness.mc.thePlayer.removePotionEffect(Potion.blindness.id);
            Brightness.mc.thePlayer.removePotionEffect(Potion.confusion.id);
        }
    }
    
    @Override
    public void run() {
        if (Brightness.state) {
            while (Brightness.mc.gameSettings.gammaSetting < 25.0f) {
                if (!Client.getModules().isModToggled("Brightness")) {
                    break;
                }
                final GameSettings gameSettings = Brightness.mc.gameSettings;
                gameSettings.gammaSetting += 0.1f;
                try {
                    Thread.sleep(20L);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        else {
            while (Brightness.mc.gameSettings.gammaSetting > Brightness.oldGamma) {
                if (Client.getModules().isModToggled("Brightness")) {
                    break;
                }
                final GameSettings gameSettings2 = Brightness.mc.gameSettings;
                gameSettings2.gammaSetting -= 0.1f;
                try {
                    Thread.sleep(20L);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
