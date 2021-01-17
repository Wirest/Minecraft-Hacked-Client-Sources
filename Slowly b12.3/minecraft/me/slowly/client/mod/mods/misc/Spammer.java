package me.slowly.client.mod.mods.misc;

import java.io.IOException;
import java.util.Random;
import me.slowly.client.ui.notifiactions.ClientNotification;
import com.darkmagician6.eventapi.EventTarget;
import me.slowly.client.events.UpdateEvent;
import me.slowly.client.mod.Mod;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.Colors;
import me.slowly.client.util.TimeHelper;
import me.slowly.client.util.command.cmds.CommandSpammer;
import net.minecraft.util.Timer;
import me.slowly.client.value.Value;

public class Spammer extends Mod {
	
	TimeHelper timer = new TimeHelper();
	private Value<Double> delay = new Value<Double>("Spammer_Delay", Double.valueOf(1.0D), Double.valueOf(0.1D), Double.valueOf(10.0D), 0.1D);
	private Value<Double> random = new Value<Double>("Spammer_Random", Double.valueOf(6D), Double.valueOf(1D), Double.valueOf(36D), 1D);
	
	public static String message = "Cyka Blyat Client";

    public Spammer() {
        super("Spammer", Category.PLAYER, Colors.YELLOW.c);
        try {
            CommandSpammer.loadMessage();;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @EventTarget
    public void onUpdate(UpdateEvent e) {
    	if(this.timer.isDelayComplete(((Double)this.delay.getValueState()).longValue() * 1000L)) {
    		this.mc.thePlayer.sendChatMessage(" [" + getRandomString(this.random.getValueState().doubleValue()) + "]" + message + " [" + getRandomString(this.random.getValueState().doubleValue()) + "]");
    		timer.reset();
    	}
    }
    
    public static String getRandomString(double d)
    {
      String str = "zxcvbnmlkjhgfdsaqwertyuiopQWERTYUIOPASDFGHJKLZXCVBNM1234567890";
      Random random = new Random();
      StringBuffer sb = new StringBuffer();
      for (int i = 0; i < d; i++)
      {
        int number = random.nextInt(62);
        sb.append(str.charAt(number));
      }
      return sb.toString();
    }
    @Override
    public void onDisable() {
        super.onDisable();
        ClientUtil.sendClientMessage("Spammer Disable", ClientNotification.Type.ERROR);
    }
    public void onEnable() {
    	super.isEnabled();
        ClientUtil.sendClientMessage("Spammer Enable", ClientNotification.Type.SUCCESS);
    }
}
