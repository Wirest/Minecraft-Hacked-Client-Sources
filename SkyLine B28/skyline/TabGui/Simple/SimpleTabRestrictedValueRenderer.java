package skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.TabGui.Simple;

import java.awt.Color;

import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.TabGui.Simple.SimpleTabThemeProperties.SimpleTabAlignment;
import skyline.specc.extras.tabgui.TabRestrictedValuePart;
import skyline.specc.render.modules.tabgui.main.TabPartRenderer;


public class SimpleTabRestrictedValueRenderer extends TabPartRenderer<TabRestrictedValuePart> {

	public SimpleTabRestrictedValueRenderer() {
		super(TabRestrictedValuePart.class);
	}

	@Override
	public void render(TabRestrictedValuePart object) {
		SimpleTabTheme theme = (SimpleTabTheme) this.getTheme();

		if(theme.getProperties().getAlignment() == SimpleTabAlignment.CENTER){
			this.getTheme().getFontRenderer().drawStringWithShadow(object.getValue().getValue() + "",
					object.getParent().getWidth()/2 - this.getTheme().getFontRenderer().getStringWidth(object.getValue().getValue() + "")/2,
					2, Color.WHITE.getRGB());
		}else{
			this.getTheme().getFontRenderer().drawStringWithShadow(object.getValue().getValue() + "",
					4,
					2, Color.WHITE.getRGB());
		}
	}
	
}
