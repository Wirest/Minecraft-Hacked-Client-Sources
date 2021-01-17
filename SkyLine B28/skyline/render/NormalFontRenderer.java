package skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.render;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;

public class NormalFontRenderer extends FontRenderer {

	private VitalFontRenderer font = null;

	public NormalFontRenderer(GameSettings p_i1035_1_, ResourceLocation p_i1035_2_, TextureManager p_i1035_3_, boolean p_i1035_4_) {
		super(p_i1035_1_, p_i1035_2_, p_i1035_3_, p_i1035_4_);
	}

	@Override
	public int drawString(String text, float x, float y, int color) {
		if(font != null)
			return font.drawString(text, x, y + 1.5f, color);
		return super.drawString(text, x, y, color);
	}

	@Override
	public int drawStringWithShadow(String text, float x, float y, int color) {
		if(this.font != null)
			return font.drawStringWithShadow(text, x, y + 1.5f, color);
		return super.drawStringWithShadow(text, x, y, color);
	}

	@Override
	public int getStringWidth(String p_78256_1_) {
		if(this.font != null)
			return font.getStringWidth(p_78256_1_);
		return super.getStringWidth(p_78256_1_);
	}

	@Override
	public int getHeight() {
		if(font != null)
			return font.getHeight();
		return this.FONT_HEIGHT;
	}

	public int drawStringNORMAL(String text, float x, float y, int color) {
		return super.drawString(text, x, y, color);
	}

	public int drawStringWithShadowNORMAL(String text, float x, float y, int color) {
		return super.drawStringWithShadow(text, x, y, color);
	}

	public int getStringWidthNORMAL(String p_78256_1_) {
		return super.getStringWidth(p_78256_1_);
	}

	public int getHeightNORMAL() {
		return this.FONT_HEIGHT;
	}

	public VitalFontRenderer getFont(){
		return this.font;
	}
	
	public void setFont(VitalFontRenderer font){
		this.font = font;
	}

	public int drawStringShadowed(final String text, final float f, final float g, final int color) {
        return this.enabled ? this.func_175065_a(text, f, g, color, true) : 0;
    }
	
}
