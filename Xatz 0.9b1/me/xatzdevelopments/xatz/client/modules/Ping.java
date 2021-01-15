package me.xatzdevelopments.xatz.client.modules;

import org.lwjgl.input.Keyboard;

import me.xatzdevelopments.xatz.client.main.Xatz;
import me.xatzdevelopments.xatz.client.module.state.Category;
import me.xatzdevelopments.xatz.gui.ScreenPos;
import me.xatzdevelopments.xatz.module.Module;

public class Ping extends Module {

	public  int ping;
	
	public Ping() {
		super("Ping", Keyboard.KEY_NONE, Category.RENDER, "Shows your Ping.");
	}

	@Override
	public void onDisable() {
		super.onDisable();
	}

	@Override
	public void onUpdate() {

		super.onUpdate();
	}

	@Override
	public void onRender() {
  if(!Xatz.getModuleByName("ModernHotbar").isToggled()) {
	  try {
	  if (!mc.isSingleplayer()) {
          this.ping = mc.thePlayer.sendQueue.getPlayerInfo(mc.thePlayer.getUniqueID()).getResponseTime();
      }
	  }catch (NullPointerException e) {
          this.ping = 0;
      }
  
  
		Xatz.getUIRenderer().addToQueue(String.valueOf("§dPing: §r" + ping), ScreenPos.LEFTUP);
 
  }
		super.onRender();
	}

}
