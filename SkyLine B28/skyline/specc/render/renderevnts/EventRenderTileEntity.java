package skyline.specc.render.renderevnts;

import net.minecraft.tileentity.TileEntity;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.main.Event;

public class EventRenderTileEntity extends Event {

	private TileEntity tileEntity;

	public EventRenderTileEntity(TileEntity tileEntity) {
		this.tileEntity = tileEntity;
	}

	public TileEntity getTileEntity() {
		return tileEntity;
	}
		
}
