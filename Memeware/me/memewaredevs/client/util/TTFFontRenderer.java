/*
 * Decompiled with CFR 0.145.
 *
 * Could not load the following classes:
 *  org.lwjgl.BufferUtils
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.util.vector.Vector2f
 */
package me.memewaredevs.client.util;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.Locale;

public class TTFFontRenderer {
    private final boolean antiAlias;
    private final Font font;
    private boolean fractionalMetrics;
    private final CharacterData[] regularData;
    private final CharacterData[] boldData;
    private final CharacterData[] italicsData;
    private final int[] colorCodes = new int[32];
    private static int RANDOM_OFFSET = 1;

    public TTFFontRenderer(final Font font) {
        this(font, 256);
    }

    public TTFFontRenderer(final Font font, final int characterCount) {
        this(font, characterCount, true);
    }

    public TTFFontRenderer(final Font font, final int characterCount, final boolean antiAlias) {
        this.font = font;
        this.fractionalMetrics = true;
        this.antiAlias = antiAlias;
        this.regularData = this.setup(new CharacterData[characterCount], 0);
        this.boldData = this.setup(new CharacterData[characterCount], 1);
        this.italicsData = this.setup(new CharacterData[characterCount], 2);
    }

    private CharacterData[] setup(final CharacterData[] characterData, final int type) {
        this.generateColors();
        final Font font = this.font.deriveFont(type);
        final BufferedImage utilityImage = new BufferedImage(1, 1, 2);
        final Graphics2D utilityGraphics = (Graphics2D) utilityImage.getGraphics();
        utilityGraphics.setFont(font);
        final FontMetrics fontMetrics = utilityGraphics.getFontMetrics();
        for (int index = 0; index < characterData.length; ++index) {
            final char character = (char) index;
            final Rectangle2D characterBounds = fontMetrics.getStringBounds(String.valueOf(character), utilityGraphics);
            final float width = (float) characterBounds.getWidth() + 8.0f;
            final float height = (float) characterBounds.getHeight();
            final BufferedImage characterImage = new BufferedImage(MathHelper.ceiling_double_int(width),
                    MathHelper.ceiling_double_int(height), 2);
            final Graphics2D graphics = (Graphics2D) characterImage.getGraphics();
            graphics.setFont(font);
            graphics.setColor(new Color(255, 255, 255, 0));
            graphics.fillRect(0, 0, characterImage.getWidth(), characterImage.getHeight());
            graphics.setColor(Color.WHITE);
            if (this.antiAlias) {
                graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
                        this.fractionalMetrics ? RenderingHints.VALUE_FRACTIONALMETRICS_ON
                                : RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
            }
            graphics.drawString(String.valueOf(character), 4, fontMetrics.getAscent());
            final int textureId = GL11.glGenTextures();
            this.createTexture(textureId, characterImage);
            characterData[index] = new CharacterData(character, characterImage.getWidth(), characterImage.getHeight(),
                    textureId);
        }
        return characterData;
    }

    private void createTexture(final int textureId, final BufferedImage image) {
        final int[] pixels = new int[image.getWidth() * image.getHeight()];
        image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());
        final ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * 4);
        for (int y2 = 0; y2 < image.getHeight(); ++y2) {
            for (int x = 0; x < image.getWidth(); ++x) {
                final int pixel = pixels[y2 * image.getWidth() + x];
                buffer.put((byte) (pixel >> 16 & 255));
                buffer.put((byte) (pixel >> 8 & 255));
                buffer.put((byte) (pixel & 255));
                buffer.put((byte) (pixel >> 24 & 255));
            }
        }
        buffer.flip();
        GlStateManager.func_179144_i(textureId);
        GL11.glTexParameteri(3553, 10241, 9728);
        GL11.glTexParameteri(3553, 10240, 9728);
        GL11.glTexImage2D(3553, 0, 6408, image.getWidth(), image.getHeight(), 0, 6408, 5121, buffer);
    }

    public void drawString(final String text, final float x, final float y2, final int color) {
        this.renderString(text, x, y2, color, false);
    }

    public void drawCenteredString(final String text, final float x, final float y2, final int color) {
        final float width = this.getWidth(text) / 2.0f;
        this.renderString(text, x - width, y2, color, false);
    }

    public void drawStringWithShadow(final String text, final float x, final float y2, final int color) {
        GL11.glTranslated(0.5, 0.5, 0.0);
        this.renderString(text, x, y2, color, true);
        GL11.glTranslated(-0.5, -0.5, 0.0);
        this.renderString(text, x, y2, color, false);
    }

    private void renderString(final String text, float x, float y2, final int color, final boolean shadow) {
        if (text == "" || text.length() == 0) {
            return;
        }
        GL11.glPushMatrix();
        GlStateManager.scale(0.5, 0.5, 1.0);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        x -= 2.0f;
        y2 -= 2.0f;
        x += 0.5f;
        y2 += 0.5f;
        x *= 2.0f;
        y2 *= 2.0f;
        CharacterData[] characterData = this.regularData;
        boolean underlined = false;
        boolean strikethrough = false;
        boolean obfuscated = false;
        final int length = text.length();
        final double multiplier = 255.0 * (shadow ? 4 : 1);
        final Color c2 = new Color(color);
        GL11.glColor4d(c2.getRed() / multiplier, c2.getGreen() / multiplier, c2.getBlue() / multiplier,
                (color >> 24 & 255) / 255.0);
        for (int i = 0; i < length; ++i) {
            int previous;
            char character = text.charAt(i);
            previous = i > 0 ? (int) text.charAt(i - 1) : 46;
            if (previous == 167) {
                continue;
            }
            if (character == '\u00a7' && i < length) {
                int index = "0123456789abcdefklmnor".indexOf(text.toLowerCase(Locale.ENGLISH).charAt(i + 1));
                if (index < 16) {
                    obfuscated = false;
                    strikethrough = false;
                    underlined = false;
                    characterData = this.regularData;
                    if (index < 0 || index > 15) {
                        index = 15;
                    }
                    if (shadow) {
                        index += 16;
                    }
                    final int textColor = this.colorCodes[index];
                    GL11.glColor4d((textColor >> 16) / 255.0, (textColor >> 8 & 255) / 255.0, (textColor & 255) / 255.0,
                            (color >> 24 & 255) / 255.0);
                    continue;
                }
                if (index == 16) {
                    obfuscated = true;
                    continue;
                }
                if (index == 17) {
                    characterData = this.boldData;
                    continue;
                }
                if (index == 18) {
                    strikethrough = true;
                    continue;
                }
                if (index == 19) {
                    underlined = true;
                    continue;
                }
                if (index == 20) {
                    characterData = this.italicsData;
                    continue;
                }
                if (index != 21) {
                    continue;
                }
                obfuscated = false;
                strikethrough = false;
                underlined = false;
                characterData = this.regularData;
                GL11.glColor4d(1.0 * (shadow ? 0.25 : 1.0), 1.0 * (shadow ? 0.25 : 1.0), 1.0 * (shadow ? 0.25 : 1.0),
                        (color >> 24 & 255) / 255.0);
                continue;
            }
            if (character > '\u00ff') {
                continue;
            }
            if (obfuscated) {
                character = (char) (character + (char) RANDOM_OFFSET);
            }
            this.drawChar(character, characterData, x, y2);
            final CharacterData charData = characterData[character];
            if (strikethrough) {
                this.drawLine(new Vector2f(0.0f, charData.height / 2.0f),
                        new Vector2f(charData.width, charData.height / 2.0f), 3.0f);
            }
            if (underlined) {
                this.drawLine(new Vector2f(0.0f, charData.height - 15.0f),
                        new Vector2f(charData.width, charData.height - 15.0f), 3.0f);
            }
            x += charData.width - 8.0f;
        }
        GL11.glPopMatrix();
        GlStateManager.disableBlend();
        GlStateManager.func_179144_i(0);
        GL11.glColor4d(1.0, 1.0, 1.0, 1.0);
    }

    public float getWidth(final String text) {
        float width = 0.0f;
        CharacterData[] characterData = this.regularData;
        final int length = text.length();
        for (int i = 0; i < length; ++i) {
            int previous;
            final char character = text.charAt(i);
            previous = i > 0 ? (int) text.charAt(i - 1) : 46;
            if (previous == 167) {
                continue;
            }
            if (character == '\u00a7' && i < length) {
                final int index = "0123456789abcdefklmnor".indexOf(text.toLowerCase(Locale.ENGLISH).charAt(i + 1));
                if (index == 17) {
                    characterData = this.boldData;
                    continue;
                }
                if (index == 20) {
                    characterData = this.italicsData;
                    continue;
                }
                if (index != 21) {
                    continue;
                }
                characterData = this.regularData;
                continue;
            }
            if (character > '\u00ff') {
                continue;
            }
            final CharacterData charData = characterData[character];
            width += (charData.width - 8.0f) / 2.0f;
        }
        return width + 2.0f;
    }

    public float getHeight(final String text) {
        float height = 0.0f;
        CharacterData[] characterData = this.regularData;
        final int length = text.length();
        for (int i = 0; i < length; ++i) {
            int previous;
            final char character = text.charAt(i);
            previous = i > 0 ? (int) text.charAt(i - 1) : 46;
            if (previous == 167) {
                continue;
            }
            if (character == '\u00a7' && i < length) {
                final int index = "0123456789abcdefklmnor".indexOf(text.toLowerCase(Locale.ENGLISH).charAt(i + 1));
                if (index == 17) {
                    characterData = this.boldData;
                    continue;
                }
                if (index == 20) {
                    characterData = this.italicsData;
                    continue;
                }
                if (index != 21) {
                    continue;
                }
                characterData = this.regularData;
                continue;
            }
            if (character > '\u00ff') {
                continue;
            }
            final CharacterData charData = characterData[character];
            height = Math.max(height, charData.height);
        }
        return height / 2.0f - 2.0f;
    }

    private void drawChar(final char character, final CharacterData[] characterData, final float x, final float y2) {
        final CharacterData charData = characterData[character];
        charData.bind();
        GL11.glBegin(7);
        GL11.glTexCoord2f(0.0f, 0.0f);
        GL11.glVertex2d(x, y2);
        GL11.glTexCoord2f(0.0f, 1.0f);
        GL11.glVertex2d(x, y2 + charData.height);
        GL11.glTexCoord2f(1.0f, 1.0f);
        GL11.glVertex2d(x + charData.width, y2 + charData.height);
        GL11.glTexCoord2f(1.0f, 0.0f);
        GL11.glVertex2d(x + charData.width, y2);
        GL11.glEnd();
    }

    private void drawLine(final Vector2f start, final Vector2f end, final float width) {
        GL11.glDisable(3553);
        GL11.glLineWidth(width);
        GL11.glBegin(1);
        GL11.glVertex2f(start.x, start.y);
        GL11.glVertex2f(end.x, end.y);
        GL11.glEnd();
        GL11.glEnable(3553);
    }

    private void generateColors() {
        for (int i = 0; i < 32; ++i) {
            final int thingy = (i >> 3 & 1) * 85;
            int red = (i >> 2 & 1) * 170 + thingy;
            int green = (i >> 1 & 1) * 170 + thingy;
            int blue = (i >> 0 & 1) * 170 + thingy;
            if (i == 6) {
                red += 85;
            }
            if (i >= 16) {
                red /= 4;
                green /= 4;
                blue /= 4;
            }
            this.colorCodes[i] = (red & 255) << 16 | (green & 255) << 8 | blue & 255;
        }
    }

    class CharacterData {
        public char character;
        public float width;
        public float height;
        private final int textureId;

        public CharacterData(final char character, final float width, final float height, final int textureId) {
            this.character = character;
            this.width = width;
            this.height = height;
            this.textureId = textureId;
        }

        public void bind() {
            GL11.glBindTexture(3553, this.textureId);
        }
    }

}
