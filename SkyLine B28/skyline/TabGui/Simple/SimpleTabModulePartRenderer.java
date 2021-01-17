package skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.TabGui.Simple;

import java.awt.Color;

import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.TabGui.Simple.SimpleTabThemeProperties.SimpleTabAlignment;
import skyline.specc.extras.tabgui.TabModulePart;
import skyline.specc.render.modules.tabgui.main.TabPartRenderer;

public class SimpleTabModulePartRenderer extends TabPartRenderer<TabModulePart> {

	public boolean arrows = false;
	
    public SimpleTabModulePartRenderer() {
        super(TabModulePart.class);
    }

    @Override
    public void render(TabModulePart object) {
        SimpleTabTheme theme = (SimpleTabTheme) this.getTheme();  
        if (theme.getProperties().getAlignment() == SimpleTabAlignment.CENTER) {
            this.getTheme().getFontRenderer().drawStringWithShadow(object.getText(),
                    object.getParent().getWidth() / 2 - this.getTheme().getFontRenderer().getStringWidth(object.getText()) / 2,
                    2, object.getModule().getState() ? theme.getProperties().getTextColor().getRGB() :theme.getProperties().getTextColor().darker().getRGB());
        } 
    }
}

