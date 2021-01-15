package me.onlyeli.ice.modules;

import java.util.Iterator;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.C03PacketPlayer;
import org.lwjgl.input.Keyboard;
import me.onlyeli.ice.utils.ChatUtils;
import me.onlyeli.ice.utils.Wrapper;
import me.onlyeli.ice.Category;
import me.onlyeli.ice.Module;

public class Aura extends Module {
	
	public Aura() {
		super("Aura", Keyboard.KEY_F, Category.COMBAT);
	}

	@Override
	public void onUpdate() {
		
		if(!this.isToggled())
			return;
		
		for(Iterator<Object> entities = Wrapper.mc.theWorld.loadedEntityList.iterator(); entities.hasNext();) {
			Object theObject = entities.next();
			if(theObject instanceof EntityLivingBase) {
				EntityLivingBase entity = (EntityLivingBase) theObject;
				
				if(entity instanceof EntityPlayerSP) continue;
				
				if(Wrapper.mc.thePlayer.getDistanceToEntity(entity) <= 6.2173613F) {
					if(entity.isEntityAlive()) {
						Wrapper.mc.playerController.attackEntity(Wrapper.mc.thePlayer, entity);
						Wrapper.mc.thePlayer.swingItem();
						Wrapper.mc.thePlayer.isBlocking();
						continue;
					}
				}
			}
		}
		
		super.onUpdate();
	}

}
