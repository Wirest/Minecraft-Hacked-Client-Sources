package skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.TabGui.Simple;

import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.render.RenderUtil;
import skyline.specc.extras.tabgui.TabPanel;
import skyline.specc.render.modules.tabgui.main.TabPartRenderer;

public class SimpleTabPanelRenderer extends TabPartRenderer<TabPanel> {

	public SimpleTabPanelRenderer() {
		super(TabPanel.class);
	}

	@Override
	public void render(TabPanel object) {
		SimpleTabTheme theme = (SimpleTabTheme) this.getTheme();
		float line = theme.getProperties().getPanelOutlineWidth();
		RenderUtil.drawRect(-line, -line, object.getWidth() + line, object.getHeight() + line, theme.getProperties().getPanelColor());
	}
	
}
