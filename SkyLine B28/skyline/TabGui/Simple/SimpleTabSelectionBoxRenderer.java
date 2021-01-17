package skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.TabGui.Simple;

import java.awt.Color;

import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.render.PrizonRenderUtils;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.render.RenderUtil;
import skyline.specc.extras.tabgui.TabSelectionBox;
import skyline.specc.render.modules.tabgui.main.TabPartRenderer;

public class SimpleTabSelectionBoxRenderer extends TabPartRenderer<TabSelectionBox> {
	
	public float hue = 0.0f;

	public SimpleTabSelectionBoxRenderer() {
		super(TabSelectionBox.class);
	}

	@Override
	public void render(TabSelectionBox object) {
		SimpleTabTheme theme = (SimpleTabTheme) this.getTheme();
		
        float h = this.hue;
        
		this.hue += 0.1;

		if (this.hue > 255.0f) {
			this.hue = 0.0f;
		}
		
		final Color color = Color.getHSBColor(h / 255.0f, 1.0f, 1.0f);

		PrizonRenderUtils.drawBorderedGradientRect(0, 0, object.getParent().getWidth() + 0.0,
				this.getTheme().getFontRenderer().getHeight() + 3.5, new Color(0,0,0).getRGB(), new Color( 75, 220, 220).getRGB(), new Color( 75, 220, 220).getRGB());
	}

}
