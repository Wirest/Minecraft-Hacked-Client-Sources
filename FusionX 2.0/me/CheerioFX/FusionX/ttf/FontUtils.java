// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.ttf;

import java.awt.Color;
import java.awt.Font;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

import net.minecraft.client.Minecraft;
import net.minecraft.util.StringUtils;

public class FontUtils {
	private Minecraft mc;
	private final UnicodeFont unicodeFont;
	private final int[] colorCodes;
	private int fontType;
	private int size;
	private String fontName;
	private float kerning;

	public FontUtils(final String fontName, final int fontType, final int size) {
		this(fontName, fontType, size, 0.0f);
	}

	public FontUtils(final String fontName, final int fontType, final int size, final float kerning) {
		this.mc = Minecraft.getMinecraft();
		this.colorCodes = new int[32];
		this.fontName = fontName;
		this.fontType = fontType;
		this.size = size;
		this.unicodeFont = new UnicodeFont(new Font(fontName, fontType, size));
		this.kerning = kerning;
		this.unicodeFont.addAsciiGlyphs();
		this.unicodeFont.getEffects().add(new ColorEffect(Color.WHITE));
		try {
			this.unicodeFont.loadGlyphs();
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (int i = 0; i < 32; ++i) {
			final int shadow = (i >> 3 & 0x1) * 85;
			int red = (i >> 2 & 0x1) * 170 + shadow;
			int green = (i >> 1 & 0x1) * 170 + shadow;
			int blue = (i >> 0 & 0x1) * 170 + shadow;
			if (i == 6) {
				red += 85;
			}
			if (i >= 16) {
				red /= 4;
				green /= 4;
				blue /= 4;
			}
			this.colorCodes[i] = ((red & 0xFF) << 16 | (green & 0xFF) << 8 | (blue & 0xFF));
		}
	}

	public int drawString(final String text, float x, float y, final int color) {
		x *= 2.0f;
		y *= 2.0f;
		final float originalX = x;
		GL11.glPushMatrix();
		GL11.glScaled(0.5, 0.5, 0.5);
		final boolean blend = GL11.glIsEnabled(3042);
		final boolean lighting = GL11.glIsEnabled(2896);
		final boolean texture = GL11.glIsEnabled(3553);
		if (!blend) {
			GL11.glEnable(3042);
		}
		if (lighting) {
			GL11.glDisable(2896);
		}
		if (texture) {
			GL11.glDisable(3553);
		}
		int currentColor = color;
		final char[] characters = text.toCharArray();
		int index = 0;
		char[] array;
		for (int length = (array = characters).length, i = 0; i < length; ++i) {
			final char c = array[i];
			if (c == '\r') {
				x = originalX;
			}
			if (c == '\n') {
				y += this.getHeight(Character.toString(c)) * 2.0f;
			}
			if (c != '§' && (index == 0 || index == characters.length - 1 || characters[index - 1] != '§')) {
				this.unicodeFont.drawString(x, y, Character.toString(c), new org.newdawn.slick.Color(currentColor));
				x += this.getWidth(Character.toString(c)) * 2.0f;
			} else if (c == ' ') {
				x += this.unicodeFont.getSpaceWidth();
			} else if (c == '§' && index != characters.length - 1) {
				final int codeIndex = "0123456789abcdefg".indexOf(text.charAt(index + 1));
				if (codeIndex < 0) {
					continue;
				}
				final int col = currentColor = this.colorCodes[codeIndex];
			}
			++index;
		}
		GL11.glScaled(2.0, 2.0, 2.0);
		if (texture) {
			GL11.glEnable(3553);
		}
		if (lighting) {
			GL11.glEnable(2896);
		}
		if (!blend) {
			GL11.glDisable(3042);
		}
		GL11.glPopMatrix();
		return (int) x;
	}

	public int drawStringWithShadow(final String text, final float x, final float y, final int color) {
		this.drawString(StringUtils.stripControlCodes(text), x + 0.5f, y + 0.5f, 0);
		return this.drawString(text, x, y, color);
	}

	public void drawCenteredString(final String text, final float x, final float y, final int color) {
		this.drawString(text, x - (int) this.getWidth(text) / 2, y, color);
	}

	public void drawCenteredStringWithShadow(final String text, final float x, final float y, final int color) {
		this.drawCenteredString(StringUtils.stripControlCodes(text), x + 0.5f, y + 0.5f, color);
		this.drawCenteredString(text, x, y, color);
	}

	public float getWidth(final String s) {
		float width = 0.0f;
		final String str = StringUtils.stripControlCodes(s);
		char[] charArray;
		for (int length = (charArray = str.toCharArray()).length, i = 0; i < length; ++i) {
			final char c = charArray[i];
			width += this.unicodeFont.getWidth(Character.toString(c)) + this.kerning;
		}
		return width / 2.0f;
	}

	public float getCharWidth(final char c) {
		return this.unicodeFont.getWidth(String.valueOf(c));
	}

	public float getHeight(final String s) {
		return this.unicodeFont.getHeight(s) / 2.0f;
	}

	public UnicodeFont getFont() {
		return this.unicodeFont;
	}

	public String trimStringToWidth(final String par1Str, final int par2) {
		final StringBuilder var4 = new StringBuilder();
		float var5 = 0.0f;
		final int var6 = 0;
		final int var7 = 1;
		boolean var8 = false;
		boolean var9 = false;
		for (int var10 = var6; var10 >= 0 && var10 < par1Str.length() && var5 < par2; var10 += var7) {
			final char var11 = par1Str.charAt(var10);
			final float var12 = this.getCharWidth(var11);
			if (var8) {
				var8 = false;
				if (var11 != 'l' && var11 != 'L') {
					if (var11 == 'r' || var11 == 'R') {
						var9 = false;
					}
				} else {
					var9 = true;
				}
			} else if (var12 < 0.0f) {
				var8 = true;
			} else {
				var5 += var12;
				if (var9) {
					++var5;
				}
			}
			if (var5 > par2) {
				break;
			}
			var4.append(var11);
		}
		return var4.toString();
	}

	public int getWidthInt(final String s) {
		int width = 0;
		final String s2 = StringUtils.stripControlCodes(s);
		char[] charArray;
		for (int length = (charArray = s2.toCharArray()).length, i = 0; i < length; ++i) {
			final char c = charArray[i];
			width += this.unicodeFont.getWidth(Character.toString(c));
		}
		return width / 2;
	}
}
