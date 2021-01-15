package me.xatzdevelopments.xatz.client.modules;

import org.lwjgl.input.Keyboard;

import me.xatzdevelopments.xatz.client.main.Xatz;
import me.xatzdevelopments.xatz.client.module.state.Category;
import me.xatzdevelopments.xatz.gui.ScreenPos;
import me.xatzdevelopments.xatz.module.Module;
import me.xatzdevelopments.xatz.utils.WaitTimer;
import net.minecraft.network.AbstractPacket;

public class Lag extends Module {

	public static WaitTimer timer = new WaitTimer();

	public Lag() {
		super("Lag", Keyboard.KEY_NONE, Category.HIDDEN);
	}

	@Override
	public void onToggle() {
		timer.reset();
		super.onToggle();
	}

	@Override
	public void onPacketRecieved(AbstractPacket packetIn) {
		timer.reset();
		super.onPacketRecieved(packetIn);
	}

	@Override
	public void onRender() {
		if (timer.getTime() > 1000) {
			Xatz.getUIRenderer().addToQueue(String.valueOf("§cLag: §4§l" + timer.getTime()), ScreenPos.LEFTUP);
		}
		super.onRender();
	}

	@Override
	public boolean dontToggleOnLoadModules() {
		return true;
	}

	@Override
	public boolean getEnableAtStartup() {
		return true;
	}

}
