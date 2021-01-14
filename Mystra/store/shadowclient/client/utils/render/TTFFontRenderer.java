package store.shadowclient.client.utils.render;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.Locale;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

public class TTFFontRenderer {
    private Font font;
    private boolean fractionalMetrics = false;
    private CharacterData[] regularData;
    private CharacterData[] boldData;
    private CharacterData[] italicsData;
    private int[] colorCodes = new int[32];
    private static final int MARGIN = 4;
    private static final char COLOR_INVOKER = '\u00a7';
    private static int RANDOM_OFFSET = 1;

    public TTFFontRenderer(Font font) {
        this(font, 256);
    }

    public TTFFontRenderer(Font font, int characterCount) {
        this(font, characterCount, true);
    }

    public TTFFontRenderer(Font font, boolean fractionalMetrics) {
        this(font, 256, fractionalMetrics);
    }

    public TTFFontRenderer(Font font, int characterCount, boolean fractionalMetrics) {
        this.font = font;
        this.fractionalMetrics = fractionalMetrics;
        this.regularData = this.setup(new CharacterData[characterCount], 0);
        this.boldData = this.setup(new CharacterData[characterCount], 1);
        this.italicsData = this.setup(new CharacterData[characterCount], 2);
    }

    private CharacterData[] setup(CharacterData[] characterData, int type) {
        this.generateColors();
        Font font = this.font.deriveFont(type);
        BufferedImage utilityImage = new BufferedImage(1, 1, 2);
        Graphics2D utilityGraphics = (Graphics2D)utilityImage.getGraphics();
        utilityGraphics.setFont(font);
        FontMetrics fontMetrics = utilityGraphics.getFontMetrics();
        for (int index = 0; index < characterData.length; ++index) {
            char character = (char)index;
            Rectangle2D characterBounds = fontMetrics.getStringBounds("" + character + "", utilityGraphics);
            float width = (float)characterBounds.getWidth() + 8.0f;
            float height = (float)characterBounds.getHeight();
            BufferedImage characterImage = new BufferedImage(MathHelper.ceiling_double_int(width), MathHelper.ceiling_double_int(height), 2);
            Graphics2D graphics = (Graphics2D)characterImage.getGraphics();
            graphics.setFont(font);
            graphics.setColor(new Color(255, 255, 255, 0));
            graphics.fillRect(0, 0, characterImage.getWidth(), characterImage.getHeight());
            graphics.setColor(Color.WHITE);
            graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, this.fractionalMetrics ? RenderingHints.VALUE_FRACTIONALMETRICS_ON : RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
            graphics.drawString("" + character + "", 4, fontMetrics.getAscent());
            int textureId = GL11.glGenTextures();
            this.createTexture(textureId, characterImage);
            characterData[index] = new CharacterData(character, characterImage.getWidth(), characterImage.getHeight(), textureId);
        }
        return characterData;
    }

    private void createTexture(int textureId, BufferedImage image) {
        int[] pixels = new int[image.getWidth() * image.getHeight()];
        image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());
        ByteBuffer buffer = BufferUtils.createByteBuffer((int)(image.getWidth() * image.getHeight() * 4));
        for (int y = 0; y < image.getHeight(); ++y) {
            for (int x = 0; x < image.getWidth(); ++x) {
                int pixel = pixels[y * image.getWidth() + x];
                buffer.put((byte)(pixel >> 16 & 255));
                buffer.put((byte)(pixel >> 8 & 255));
                buffer.put((byte)(pixel & 255));
                buffer.put((byte)(pixel >> 24 & 255));
            }
        }
        buffer.flip();
        GlStateManager.bindTexture(textureId);
        GL11.glTexParameteri((int)3553, (int)10241, (int)9728);
        GL11.glTexParameteri((int)3553, (int)10240, (int)9728);
        GL11.glTexImage2D((int)3553, (int)0, (int)6408, (int)image.getWidth(), (int)image.getHeight(), (int)0, (int)6408, (int)5121, (ByteBuffer)buffer);
    }

    public void drawString(String text, float x, float y, int color) {
        this.renderString(text, x, y, color, false);
    }
    
    public void drawString(String text, double d, double e, int color) {
        this.renderString(text, d, e, color, false);
    }

    public void drawCenteredString(String text, float x, float y, int color) {
        float width = this.getWidth(text) / 2.0f;
        this.renderString(text, x - width, y, color, false);
    }

    public void drawStringWithShadow(String text, float x, float y, int color) {
        GL11.glTranslated((double)0.5, (double)0.5, (double)0.0);
        this.renderString(text, x, y, color, true);
        GL11.glTranslated((double)-0.5, (double)-0.5, (double)0.0);
        this.renderString(text, x, y, color, false);
    }

    private void renderString(String text, float x, float y, int color, boolean shadow) {
        if (text == "" || text.length() == 0) {
            return;
        }
        GL11.glPushMatrix();
        GlStateManager.scale(0.5, 0.5, 1.0);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        x -= 2.0f;
        y -= 2.0f;
        x += 0.5f;
        y += 0.5f;
        x *= 2.0f;
        y *= 2.0f;
        CharacterData[] characterData = this.regularData;
        boolean underlined = false;
        boolean strikethrough = false;
        boolean obfuscated = false;
        int length = text.length();
        double multiplier = 255.0 * (double)(shadow ? 4 : 1);
        Color c = new Color(color);
        GL11.glColor4d((double)((double)c.getRed() / multiplier), (double)((double)c.getGreen() / multiplier), (double)((double)c.getBlue() / multiplier), (double)c.getAlpha());
        for (int i = 0; i < length; ++i) {
            int previous;
            char character = text.charAt(i);
            int n = previous = i > 0 ? (int)text.charAt(i - 1) : 46;
            if (previous == 167) continue;
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
                    int textColor = this.colorCodes[index];
                    GL11.glColor4d((double)((double)(textColor >> 16) / 255.0), (double)((double)(textColor >> 8 & 255) / 255.0), (double)((double)(textColor & 255) / 255.0), (double)((double)(color >> 24 & 255) / 255.0));
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
                if (index != 21) continue;
                obfuscated = false;
                strikethrough = false;
                underlined = false;
                characterData = this.regularData;
                GL11.glColor4d((double)(1.0 * (shadow ? 0.25 : 1.0)), (double)(1.0 * (shadow ? 0.25 : 1.0)), (double)(1.0 * (shadow ? 0.25 : 1.0)), (double)((double)(color >> 24 & 255) / 255.0));
                continue;
            }
            if (character > '\u00ff') continue;
            if (obfuscated) {
                character = (char)(character + RANDOM_OFFSET);
            }
            this.drawChar(character, characterData, x, y);
            CharacterData charData = characterData[character];
            if (strikethrough) {
                this.drawLine(new Vector2f(0.0f, charData.height / 2.0f), new Vector2f(charData.width, charData.height / 2.0f), 3.0f);
            }
            if (underlined) {
                this.drawLine(new Vector2f(0.0f, charData.height - 15.0f), new Vector2f(charData.width, charData.height - 15.0f), 3.0f);
            }
            x += charData.width - 8.0f;
        }
        GL11.glPopMatrix();
        GlStateManager.disableBlend();
        GlStateManager.bindTexture(0);
        GL11.glColor4d((double)1.0, (double)1.0, (double)1.0, (double)1.0);
    }
    
    private void renderString(String text, double d, double e, int color, boolean shadow) {
        if (text == "" || text.length() == 0) {
            return;
        }
        GL11.glPushMatrix();
        GlStateManager.scale(0.5, 0.5, 1.0);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        d -= 2.0f;
        e -= 2.0f;
        d += 0.5f;
        e += 0.5f;
        d *= 2.0f;
        e *= 2.0f;
        CharacterData[] characterData = this.regularData;
        boolean underlined = false;
        boolean strikethrough = false;
        boolean obfuscated = false;
        int length = text.length();
        double multiplier = 255.0 * (double)(shadow ? 4 : 1);
        Color c = new Color(color);
        GL11.glColor4d((double)((double)c.getRed() / multiplier), (double)((double)c.getGreen() / multiplier), (double)((double)c.getBlue() / multiplier), (double)c.getAlpha());
        for (int i = 0; i < length; ++i) {
            int previous;
            char character = text.charAt(i);
            int n = previous = i > 0 ? (int)text.charAt(i - 1) : 46;
            if (previous == 167) continue;
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
                    int textColor = this.colorCodes[index];
                    GL11.glColor4d((double)((double)(textColor >> 16) / 255.0), (double)((double)(textColor >> 8 & 255) / 255.0), (double)((double)(textColor & 255) / 255.0), (double)((double)(color >> 24 & 255) / 255.0));
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
                if (index != 21) continue;
                obfuscated = false;
                strikethrough = false;
                underlined = false;
                characterData = this.regularData;
                GL11.glColor4d((double)(1.0 * (shadow ? 0.25 : 1.0)), (double)(1.0 * (shadow ? 0.25 : 1.0)), (double)(1.0 * (shadow ? 0.25 : 1.0)), (double)((double)(color >> 24 & 255) / 255.0));
                continue;
            }
            if (character > '\u00ff') continue;
            if (obfuscated) {
                character = (char)(character + RANDOM_OFFSET);
            }
            this.drawChar(character, characterData, d, e);
            CharacterData charData = characterData[character];
            if (strikethrough) {
                this.drawLine(new Vector2f(0.0f, charData.height / 2.0f), new Vector2f(charData.width, charData.height / 2.0f), 3.0f);
            }
            if (underlined) {
                this.drawLine(new Vector2f(0.0f, charData.height - 15.0f), new Vector2f(charData.width, charData.height - 15.0f), 3.0f);
            }
            d += charData.width - 8.0f;
        }
        GL11.glPopMatrix();
        GlStateManager.disableBlend();
        GlStateManager.bindTexture(0);
        GL11.glColor4d((double)1.0, (double)1.0, (double)1.0, (double)1.0);
    }

    public float getWidth(String text) {
        float width = 0.0f;
        CharacterData[] characterData = this.regularData;
        int length = text.length();
        for (int i = 0; i < length; ++i) {
            int previous;
            char character = text.charAt(i);
            int n = previous = i > 0 ? (int)text.charAt(i - 1) : 46;
            if (previous == 167) continue;
            if (character == '\u00a7' && i < length) {
                int index = "0123456789abcdefklmnor".indexOf(text.toLowerCase(Locale.ENGLISH).charAt(i + 1));
                if (index == 17) {
                    characterData = this.boldData;
                    continue;
                }
                if (index == 20) {
                    characterData = this.italicsData;
                    continue;
                }
                if (index != 21) continue;
                characterData = this.regularData;
                continue;
            }
            if (character > '\u00ff') continue;
            CharacterData charData = characterData[character];
            width += (charData.width - 8.0f) / 2.0f;
        }
        return width + 2.0f;
    }

    public float getHeight(String text) {
        float height = 0.0f;
        CharacterData[] characterData = this.regularData;
        int length = text.length();
        for (int i = 0; i < length; ++i) {
            int previous;
            char character = text.charAt(i);
            int n = previous = i > 0 ? (int)text.charAt(i - 1) : 46;
            if (previous == 167) continue;
            if (character == '\u00a7' && i < length) {
                int index = "0123456789abcdefklmnor".indexOf(text.toLowerCase(Locale.ENGLISH).charAt(i + 1));
                if (index == 17) {
                    characterData = this.boldData;
                    continue;
                }
                if (index == 20) {
                    characterData = this.italicsData;
                    continue;
                }
                if (index != 21) continue;
                characterData = this.regularData;
                continue;
            }
            if (character > '\u00ff') continue;
            CharacterData charData = characterData[character];
            height = Math.max(height, charData.height);
        }
        return height / 2.0f - 2.0f;
    }

    private void drawChar(char character, CharacterData[] characterData, float x, float y) {
        CharacterData charData = characterData[character];
        charData.bind();
        GL11.glBegin((int)7);
        GL11.glTexCoord2f((float)0.0f, (float)0.0f);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glTexCoord2f((float)0.0f, (float)1.0f);
        GL11.glVertex2d((double)x, (double)(y + charData.height));
        GL11.glTexCoord2f((float)1.0f, (float)1.0f);
        GL11.glVertex2d((double)(x + charData.width), (double)(y + charData.height));
        GL11.glTexCoord2f((float)1.0f, (float)0.0f);
        GL11.glVertex2d((double)(x + charData.width), (double)y);
        GL11.glEnd();
    }
    
    private void drawChar(char character, CharacterData[] characterData, double d, double e) {
        CharacterData charData = characterData[character];
        charData.bind();
        GL11.glBegin((int)7);
        GL11.glTexCoord2f((float)0.0f, (float)0.0f);
        GL11.glVertex2d((double)d, (double)e);
        GL11.glTexCoord2f((float)0.0f, (float)1.0f);
        GL11.glVertex2d((double)d, (double)(e + charData.height));
        GL11.glTexCoord2f((float)1.0f, (float)1.0f);
        GL11.glVertex2d((double)(d + charData.width), (double)(e + charData.height));
        GL11.glTexCoord2f((float)1.0f, (float)0.0f);
        GL11.glVertex2d((double)(d + charData.width), (double)e);
        GL11.glEnd();
    }

    private void drawLine(Vector2f start, Vector2f end, float width) {
        GL11.glDisable((int)3553);
        GL11.glLineWidth((float)width);
        GL11.glBegin((int)1);
        GL11.glVertex2f((float)start.x, (float)start.y);
        GL11.glVertex2f((float)end.x, (float)end.y);
        GL11.glEnd();
        GL11.glEnable((int)3553);
    }

    private void generateColors() {
        for (int i = 0; i < 32; ++i) {
            int thingy = (i >> 3 & 1) * 85;
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
        private int textureId;

        public CharacterData(char character, float width, float height, int textureId) {
            this.character = character;
            this.width = width;
            this.height = height;
            this.textureId = textureId;
        }

        public void bind() {
            GL11.glBindTexture((int)3553, (int)this.textureId);
        }
    }

}

