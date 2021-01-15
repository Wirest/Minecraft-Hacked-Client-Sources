// 
// Decompiled by Procyon v0.5.30
// 

package org.darkstorm.minecraft.gui.theme.simple;

import org.darkstorm.minecraft.gui.theme.AbstractComponentUI;
import org.darkstorm.minecraft.gui.font.UnicodeFontRenderer;
import java.awt.Font;
import net.minecraft.client.gui.FontRenderer;
import org.darkstorm.minecraft.gui.theme.AbstractTheme;

public class SimpleTheme extends AbstractTheme
{
    private final FontRenderer fontRenderer;
    
    public SimpleTheme() {
        this.fontRenderer = new UnicodeFontRenderer(new Font("Helvetic Neue Light", 0, 15));
        this.installUI(new SimpleFrameUI(this));
        this.installUI(new SimplePanelUI(this));
        this.installUI(new SimpleLabelUI(this));
        this.installUI(new SimpleButtonUI(this));
        this.installUI(new SimpleCheckButtonUI(this));
        this.installUI(new SimpleComboBoxUI(this));
        this.installUI(new SimpleSliderUI(this));
        this.installUI(new SimpleProgressBarUI(this));
    }
    
    public FontRenderer getFontRenderer() {
        return this.fontRenderer;
    }
}
