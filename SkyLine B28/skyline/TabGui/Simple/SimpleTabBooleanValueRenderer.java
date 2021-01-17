package skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.TabGui.Simple;

import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.TabGui.Simple.SimpleTabThemeProperties.SimpleTabAlignment;
import skyline.specc.extras.tabgui.TabBooleanValuePart;
import skyline.specc.render.modules.tabgui.main.TabPartRenderer;

public class SimpleTabBooleanValueRenderer extends TabPartRenderer<TabBooleanValuePart> {

	public SimpleTabBooleanValueRenderer() {
		super(TabBooleanValuePart.class);
	}

	@Override
	public void render(TabBooleanValuePart object) {
		SimpleTabTheme theme = (SimpleTabTheme) this.getTheme();

		if(theme.getProperties().getAlignment() == SimpleTabAlignment.CENTER){
			this.getTheme().getFontRenderer().drawStringWithShadow(object.getValue().getValue() + "",
					object.getParent().getWidth()/2 - this.getTheme().getFontRenderer().getStringWidth(object.getValue().getValue() + "")/2,
					2, object.getValue().getValue() ? theme.getProperties().getTextColor().getRGB() :theme.getProperties().getTextColor().darker().getRGB());
		}else{
			this.getTheme().getFontRenderer().drawStringWithShadow(object.getValue().getValue() + "",
					4,
					2, object.getValue().getValue() ? theme.getProperties().getTextColor().getRGB() :theme.getProperties().getTextColor().darker().getRGB());
		}
	}
	
}
