package VenusClient.online.Utils.TargetFinder.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;

public class AliveCheck {
	
	 public boolean validate(Entity entity) {
		return (entity.isEntityAlive() || (Minecraft.getMinecraft().getCurrentServerData()).serverIP.contains("mineplex"));
	}
	 
}
