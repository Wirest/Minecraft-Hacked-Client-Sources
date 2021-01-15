package me.onlyeli.ice.modules;

import java.awt.Color;

import me.onlyeli.ice.events.EventListener;
import me.onlyeli.ice.Category;
import me.onlyeli.ice.Module;
import me.onlyeli.ice.events.EventEntityVelocity;
import me.onlyeli.ice.events.ModData;
import me.onlyeli.ice.utils.Wrapper;

import org.lwjgl.input.Keyboard;

public class AntiKnockback extends Module {

	public AntiKnockback() {
		super("AntiKnockback", Keyboard.KEY_NONE, Category.COMBAT);
	}
	
	@EventListener
	public void onEntityVelocity(EventEntityVelocity event){
		if(event.getEntity() != Wrapper.mc.thePlayer) return;
		event.setMotionX(0);
		event.setMotionY(0);
		event.setMotionZ(0);
	}
	
}
