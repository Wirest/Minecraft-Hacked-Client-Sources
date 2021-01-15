package info.spicyclient;

import net.minecraft.util.*;

public enum RandomBackgrounds {
	
	LAVAFLOWGLOW(new ResourceLocation("spicy/splash/lavaflowglow.png")),
	FLOOFYFOX1(new ResourceLocation("spicy/splash/FloofyFox1.png")),
	SPICYCLIENT(new ResourceLocation("spicy/splash/SpicyClient1.png"));
	
	public ResourceLocation image;
	
	RandomBackgrounds(ResourceLocation image) {
		
		// Uses 512x512 images
		this.image = image;
		// Uses 512x512 images
		
	}
	
}
