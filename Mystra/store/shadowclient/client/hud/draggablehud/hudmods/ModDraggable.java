package store.shadowclient.client.hud.draggablehud.hudmods;

import store.shadowclient.client.hud.draggablehud.IRenderer;
import store.shadowclient.client.hud.draggablehud.ScreenPosition;

public abstract class ModDraggable extends Mod implements IRenderer {
	public final int getLineOffset(ScreenPosition pos, int lineNum) {
		return pos.getAbsoluteY() + getLineOffset(lineNum);
	}
	private int getLineOffset(int lineNum) {
		return (font.FONT_HEIGHT + 3) * lineNum;
	}
}
