package store.shadowclient.client.management;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class Wrapper {

	public static Minecraft mc()
    {
        return Minecraft.getMinecraft();
    }
	
	
}
