package skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.TabGui.Simple;

import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.TabGui.Simple.SimpleTabThemeProperties.SimpleTabAlignment;
import skyline.specc.extras.tabgui.TabTypePart;
import skyline.specc.render.modules.tabgui.main.TabPartRenderer;

public class SimpleTabTypeRenderer extends TabPartRenderer<TabTypePart> {

    public SimpleTabTypeRenderer() {
        super(TabTypePart.class);
    }

    @Override
    public void render(TabTypePart object) {
        SimpleTabTheme theme = (SimpleTabTheme) this.getTheme();

        if (theme.getProperties().getAlignment() == SimpleTabAlignment.CENTER) {
            this.getTheme().getFontRenderer().drawStringWithShadow(object.getText(),
                    object.getParent().getWidth() / 2 - this.getTheme().getFontRenderer().getStringWidth(object.getText()) / 2,
                    2, theme.getProperties().getTextColor().getRGB());
        } else {
            if (object.isSelected()) {
                this.getTheme().getFontRenderer().drawStringWithShadow(object.getText(),
                        4,
                        2, theme.getProperties().getTextColor().getRGB());
            } else {
                this.getTheme().getFontRenderer().drawStringWithShadow(object.getText(),
                        4,
                        2, theme.getProperties().getTextColor().getRGB());
            }
        }
    }

}