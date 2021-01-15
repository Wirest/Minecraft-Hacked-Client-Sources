package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.WaitTimer;
import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.gui.ScreenPos;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.network.AbstractPacket;

public class Ping extends Module {

	public static WaitTimer timer = new WaitTimer();

	public Ping() {
		super("Ping", Keyboard.KEY_NONE, Category.HIDDEN);
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
			Jigsaw.getUIRenderer().addToQueue(String.valueOf("§cPing: §4§l" + timer.getTime()), ScreenPos.LEFTUP);
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
