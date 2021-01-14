package store.shadowclient.client.module.render;

import java.awt.Color;

import store.shadowclient.client.Shadow;
import store.shadowclient.client.module.Category;
import store.shadowclient.client.module.Module;
import store.shadowclient.client.utils.render.RenderUtils;
import net.minecraft.tileentity.TileEntityChest;

public class ChestESP extends Module {

	public ChestESP() {
		super("ChestESP", 0, Category.RENDER);
	}
	
	public void onRender() {
		if(Shadow.instance.moduleManager.getModuleByName("ChestESP").isToggled())
			for(Object o: mc.theWorld.loadedTileEntityList){
				if(o instanceof TileEntityChest){
					final int[] counter = {1};
					RenderUtils.blockESPBox(((TileEntityChest)o).getPos(), novoline(counter[0] * 300));
					counter[0]++;
			}
		}
	}
	
	public static int novoline(int delay) {
	      double novolineState = Math.ceil((System.currentTimeMillis() + delay) / 20.0);
	      novolineState %= 360;
	      return Color.getHSBColor((float) (novolineState / 360.0f), 0.8f, 0.7f).getRGB();
	}
}
