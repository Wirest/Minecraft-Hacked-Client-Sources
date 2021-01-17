package skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.TabGui.Simple;

import java.awt.Font;

import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.render.VitalFontRenderer;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.render.VitalFontRenderer.FontObjectType;
import skyline.specc.mods.render.TabTheme;
import skyline.specc.utils.FontUtil;

public class SimpleTabTheme extends TabTheme {
	

	private final SimpleTabThemeProperties properties;
	
	public SimpleTabTheme(VitalFontRenderer fontRenderer, SimpleTabThemeProperties properties) {
		super(FontUtil.roboto);
		this.properties = properties;
		this.addPartRenderer(new SimpleTabPanelRenderer());
		this.addPartRenderer(new SimpleTabSelectionBoxRenderer());
		this.addPartRenderer(new SimpleTabTypeRenderer());
		this.addPartRenderer(new SimpleTabModulePartRenderer());
		this.addPartRenderer(new SimpleTabModeRenderer());
        this.addPartRenderer(new SimpleTabValueRenderer());
        this.addPartRenderer(new SimpleTabRestrictedValueRenderer());
        this.addPartRenderer(new SimpleTabBooleanValueRenderer());
	}

	public SimpleTabThemeProperties getProperties() {
		return properties;
	}
	
}
