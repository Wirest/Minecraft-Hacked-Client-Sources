package store.shadowclient.client.cosmetics;

import net.minecraft.client.entity.AbstractClientPlayer;

public class CosmeticController {
	public static boolean shouldRenderTopHat(AbstractClientPlayer player) {
		return true;
	}
	public static float[] getTopHatColor(AbstractClientPlayer player) {
		return new float[] {0, 0, 1};
	}
}
