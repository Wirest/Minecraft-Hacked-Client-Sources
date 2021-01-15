package nivia.utils.font;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import nivia.utils.Helper;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.Locale;

/**
 * Made by MemesValkyrie for Faint.
 *
 * Just a little fontrenderer for minecraft I wrote. Should work with any font size
 * without any graphical glitches, but because of this setup takes forever. Feel free to make any edits.
 *
 * Created by Zeb on 12/19/2016.
 */
public class TTFFontRenderer implements FontRenderer {

    /**
     * The font to be drawn.
     */
    private Font font;

    /**
     * If fractional metrics should be used in the font renderer.
     */
    private boolean fractionalMetrics = false;

    /**
     * All the character data information (regular).
     */
    private CharacterData[] regularData;

    /**
     * All the character data information (bold).
     */
    private CharacterData[] boldData;

    /**
     * All the character data information (italics).
     */
    private CharacterData[] italicsData;

    /**
     * All the color codes used in minecraft.
     */
    private int[] colorCodes = new int[32];

    /**
     * The margin on each texture.
     */
    private static final int MARGIN = 4;

    /**
     * The character that invokes color in a string when rendered.
     */
    private static final char COLOR_INVOKER = '\247';

    /**
     * The random offset in obfuscated text.
     */
    private static int RANDOM_OFFSET = 1;

    public TTFFontRenderer(Font font) {
        this(font, 256);
    }

    public TTFFontRenderer(Font font, int characterCount) {
        this(font, characterCount, false);
    }

    public TTFFontRenderer(Font font, int characterCount, boolean fractionalMetrics) {
        this.font = font;
        this.fractionalMetrics = fractionalMetrics;

        // Generates all the character textures.
        this.regularData = setup(new CharacterData[characterCount], Font.PLAIN);
        this.boldData = setup(new CharacterData[characterCount], Font.BOLD);
        this.italicsData = setup(new CharacterData[characterCount], Font.ITALIC);
    }

    /**
     * Sets up the character data and textures.
     *
     * @param characterData The array of character data that should be filled.
     * @param type The font type. (Regular, Bold, and Italics)
     */
    private CharacterData[] setup(CharacterData[] characterData, int type){
        // Quickly generates the colors.
        generateColors();

        // Changes the type of the font to the given type.
        Font font = this.font.deriveFont(type);

        // An image just to get font data.
        BufferedImage utilityImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);

        // The graphics of the utility image.
        Graphics2D utilityGraphics = (Graphics2D) utilityImage.getGraphics();

        // Sets the font of the utility image to the font.
        utilityGraphics.setFont(font);

        // The font metrics of the utility image.
        FontMetrics fontMetrics = utilityGraphics.getFontMetrics();

        // Iterates through all the characters in the character set of the font renderer.
        for(int index = 0; index < characterData.length; index++){
            // The character at the current index.
            char character = (char) index;

            // The width and height of the character according to the font.
            Rectangle2D characterBounds = fontMetrics.getStringBounds(character + "", utilityGraphics);

            // The width of the character texture.
            float width = (float) characterBounds.getWidth() + (2 * MARGIN);

            // The height of the character texture.
            float height = (float) characterBounds.getHeight();

            // The image that the character will be rendered to.
            BufferedImage characterImage = new BufferedImage(MathHelper.ceiling_float_int(width), MathHelper.ceiling_float_int(height), BufferedImage.TYPE_INT_ARGB);

            // The graphics of the character image.
            Graphics2D graphics = (Graphics2D) characterImage.getGraphics();

            // Sets the font to the input font/
            graphics.setFont(font);

            // Sets the color to white with no alpha.
            graphics.setColor(new Color(255, 255, 255, 0));

            // Fills the entire image with the color above, makes it transparent.
            graphics.fillRect(0, 0, characterImage.getWidth(), characterImage.getHeight());

            // Sets the color to white to draw the character.
            graphics.setColor(Color.WHITE);

            // Enables anti-aliasing so the font doesn't have aliasing.
            graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, this.fractionalMetrics ? RenderingHints.VALUE_FRACTIONALMETRICS_ON : RenderingHints.VALUE_FRACTIONALMETRICS_OFF);

            // Draws the character.
            graphics.drawString(character + "", MARGIN, fontMetrics.getAscent());

            // Generates a new texture id.
            int textureId = GlStateManager.func_179146_y();

            // Allocates the texture in opengl.
            createTexture(textureId, characterImage);

            // Initiates the character data and stores it in the data array.
            characterData[index] = new CharacterData(character, characterImage.getWidth(), characterImage.getHeight(), textureId);
        }

        // Returns the filled character data array.
        return characterData;
    }

    /**
     * Uploads the opengl texture.
     *
     * @param textureId The texture id to upload to.
     * @param image The image to upload.
     */
    private void createTexture(int textureId, BufferedImage image){
        int[] pixels = new int[image.getWidth() * image.getHeight()];
        image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());

        ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * 4); //4 for RGBA, 3 for RGB

        for(int y = 0; y < image.getHeight(); y++){
            for(int x = 0; x < image.getWidth(); x++){
                int pixel = pixels[y * image.getWidth() + x];
                buffer.put((byte) ((pixel >> 16) & 0xFF));     // Red component
                buffer.put((byte) ((pixel >> 8) & 0xFF));      // Green component
                buffer.put((byte) (pixel & 0xFF));               // Blue component
                buffer.put((byte) ((pixel >> 24) & 0xFF));    // Alpha component. Only for RGBA
            }
        }

        buffer.flip();

        // Binds the opengl texture by the texture id.
        GlStateManager.setAndBindTexture(textureId);

        // Sets the texture parameter stuff.
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

        // Uploads the texture to opengl.
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA,image.getWidth(), image.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);

        // Binds the opengl texture 0.
        GlStateManager.setAndBindTexture(0);
    }
    /**
     * Renders the given string.
     *
     * @param text The text to be rendered.
     * @param x The x position of the text.
     * @param y The y position of the text.
     * @param color The color of the text.
     */
    public void drawString(String text, float x, float y, Color color){
        makeRandom();
        renderString(text, x, y, color, false);
    }

    /**
     * Renders the given string.
     *
     * @param text The text to be rendered.
     * @param x The x position of the text.
     * @param y The y position of the text.
     * @param color The color of the text.
     */
    public void drawStringWithShadow(String text, float x, float y, Color color){
        makeRandom();
        GL11.glTranslated(0.5, 0.5, 0);
        renderString(text, x, y, color, true);
        GL11.glTranslated(-0.5, -0.5, 0);
        renderString(text, x, y, color, false);
    }

    /**
     * Renders the given string.
     *
     * @param text The text to be rendered.
     * @param x The x position of the text.
     * @param y The y position of the text.
     * @param shadow If the text should be rendered with the shadow color.
     * @param color The color of the text.
     */
    private void renderString(String text, float x, float y, Color color, boolean shadow){
        // Returns if the text is empty.
        if(text == "" || text.length() == 0) return;

        // Pushes the matrix to store gl values.
        GL11.glPushMatrix();

        // Scales down to make the font look better.
        GlStateManager.scale(0.5, 0.5, 1);

        // Enables blending.
        GlStateManager.enableBlend();

        // Sets the blending function.
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        // Removes half the margin to render in the right spot.
        x -= MARGIN/2;
        y -= MARGIN/2;

        // Adds 0.5 to x and y.
        x += 0.5f;
        y += 0.5f;

        // Doubles the position because of the scaling.
        x *= 2;
        y *= 2;

        // The character texture set to be used. (Regular by default)
        CharacterData[] characterData = regularData;

        // Booleans to handle the style.
        boolean underlined = false;
        boolean strikethrough = false;
        boolean obfuscated = false;

        // The length of the text used for the draw loop.
        int length = text.length();

        // The multiplier.
        double multiplier = 255d * (shadow ? 4 : 1);

        // Sets the color.
        GL11.glColor4d(color.getRed()/multiplier, color.getGreen()/multiplier, color.getBlue()/multiplier, color.getAlpha()/255d);

        // Loops through the text.
        for(int i = 0; i < length ;i++){
            // The character at the index of 'i'.
            char character = text.charAt(i);

            // The previous character.
            char previous = i > 0 ? text.charAt(i - 1) : '.';

            // Continues if the previous color was the color invoker.
            if(previous == COLOR_INVOKER) continue;

            // Sets the color if the character is the color invoker and the character index is less than the length.
            if(character == COLOR_INVOKER && i < length){

                // The color index of the character after the current character.
                int index = "0123456789abcdefklmnor".indexOf(text.toLowerCase(Locale.ENGLISH).charAt(i + 1));

                // If the color character index is of the normal color invoking characters.
                if (index < 16)
                {
                    // Resets all the styles.
                    obfuscated = false;
                    strikethrough = false;
                    underlined = false;

                    // Sets the character data to the regular type.
                    characterData = regularData;

                    // Clamps the index just to be safe in case an odd character somehow gets in here.
                    if(index < 0 || index > 15) index = 15;

                    // Adds 16 to the color index to get the darker shadow color.
                    if(shadow) index += 16;

                    // Gets the text color from the color codes array.
                    int textColor = this.colorCodes[index];

                    // Sets the current color.
                    GL11.glColor4d((textColor >> 16) / 255d, (textColor >> 8 & 255) / 255d, (textColor & 255) / 255d, color.getAlpha()/255d);
                }else if (index == 16)
                    obfuscated = true;
                else if (index == 17)
                    // Sets the character data to the bold type.
                    characterData = boldData;
                else if (index == 18)
                    strikethrough = true;
                else if (index == 19)
                    underlined = true;
                else if (index == 20)
                    // Sets the character data to the italics type.
                    characterData = italicsData;
                else if (index == 21){
                    // Resets the style.
                    obfuscated = false;
                    strikethrough = false;
                    underlined = false;

                    // Sets the character data to the regular type.
                    characterData = regularData;

                    // Sets the color to white
                    GL11.glColor4d(1 * (shadow ? 0.25 : 1), 1 * (shadow ? 0.25 : 1), 1 * (shadow ? 0.25 : 1), color.getAlpha()/255d);
                }
            }else{
                // Sets the character to a random char if obfuscated is enabled.
                if(obfuscated)
                    character = (char) (((int) character) + RANDOM_OFFSET);

                // Draws the character.
                drawChar(character, characterData, x, y);

                // The character data for the given character.
                CharacterData charData = characterData[character];

                // Draws the strikethrough line if enabled.
                if(strikethrough)
                    drawLine(new Vec2f(0, charData.height / 2f), new Vec2f(charData.width, charData.height / 2f), 3);

                // Draws the underline if enabled.
                if(underlined)
                    drawLine(new Vec2f(0, charData.height - 15), new Vec2f(charData.width, charData.height - 15), 3);

                // Adds to the offset.
                x += charData.width - (2 * MARGIN);
            }
        }

        // Restores previous values.
        GL11.glPopMatrix();

        // Sets the color back to white so no odd rendering problems happen.
        GL11.glColor4d(1,1,1,1);
    }

    /**
     * Gets the width of the given text.
     *
     * @param text The text to get the width of.
     * @return The width of the given text.
     */
    public float getWidth(String text){

        // The width of the string.
        float width = 0;

        // The character texture set to be used. (Regular by default)
        CharacterData[] characterData = regularData;

        // The length of the text.
        int length = text.length();

        // Loops through the text.
        for(int i = 0; i < length ;i++){
            // The character at the index of 'i'.
            char character = text.charAt(i);

            // The previous character.
            char previous = i > 0 ? text.charAt(i - 1) : '.';

            // Continues if the previous color was the color invoker.
            if(previous == COLOR_INVOKER) continue;

            // Sets the color if the character is the color invoker and the character index is less than the length.
            if(character == COLOR_INVOKER && i < length){

                // The color index of the character after the current character.
                int index = "0123456789abcdefklmnor".indexOf(text.toLowerCase(Locale.ENGLISH).charAt(i + 1));

                if (index == 17)
                    // Sets the character data to the bold type.
                    characterData = boldData;
                else if (index == 20)
                    // Sets the character data to the italics type.
                    characterData = italicsData;
                else if (index == 21)
                    // Sets the character data to the regular type.
                    characterData = regularData;
            }else{
                // The character data for the given character.
                CharacterData charData = characterData[character];

                // Adds to the offset.
                width += (charData.width - (2 * MARGIN))/2;
            }
        }

        // Returns the width.
        return width + MARGIN/2;
    }

    /**
     * Gets the height of the given text.
     *
     * @param text The text to get the height of.
     * @return The height of the given text.
     */
    public float getHeight(String text){

        // The height of the string.
        float height = 0;

        // The character texture set to be used. (Regular by default)
        CharacterData[] characterData = regularData;

        // The length of the text.
        int length = text.length();

        // Loops through the text.
        for(int i = 0; i < length ;i++){
            // The character at the index of 'i'.
            char character = text.charAt(i);

            // The previous character.
            char previous = i > 0 ? text.charAt(i - 1) : '.';

            // Continues if the previous color was the color invoker.
            if(previous == COLOR_INVOKER) continue;

            // Sets the color if the character is the color invoker and the character index is less than the length.
            if(character == COLOR_INVOKER && i < length){

                // The color index of the character after the current character.
                int index = "0123456789abcdefklmnor".indexOf(text.toLowerCase(Locale.ENGLISH).charAt(i + 1));

                if (index == 17)
                    // Sets the character data to the bold type.
                    characterData = boldData;
                else if (index == 20)
                    // Sets the character data to the italics type.
                    characterData = italicsData;
                else if (index == 21)
                    // Sets the character data to the regular type.
                    characterData = regularData;
            }else{
                // The character data for the given character.
                CharacterData charData = characterData[character];

                // Sets the height if its bigger.
                height = Math.max(height, charData.height);
            }
        }

        // Returns the height.
        return height/2 - MARGIN/2;
    }

    /**
     * Draws the character.
     *
     * @param character The character to be drawn.
     * @param characterData The character texture set to be used.
     */
    private void drawChar(char character, CharacterData[] characterData, float x, float y){
        // The char data that stores the character data.
        CharacterData charData = characterData[character];

        // Binds the character data texture.
        charData.bind();

        // Begins drawing the quad.
        GL11.glBegin(GL11.GL_QUADS);
        {
            // Maps out where the texture should be drawn.
            GL11.glTexCoord2f(0, 0);
            GL11.glVertex2d(x, y);
            GL11.glTexCoord2f(0, 1);
            GL11.glVertex2d(x, y +charData.height);
            GL11.glTexCoord2f(1, 1);
            GL11.glVertex2d(x + charData.width, y + charData.height);
            GL11.glTexCoord2f(1, 0);
            GL11.glVertex2d(x +charData.width, y);
        }
        // Ends the quad.
        GL11.glEnd();
    }

    /**
     * Draws a line from start to end with the given width.
     *
     * @param start The starting point of the line.
     * @param end The ending point of the line.
     * @param width The thickness of the line.
     */
    private void drawLine(Vec2f start, Vec2f end, float width){
        // Disables textures so we can draw a solid line.
        GL11.glDisable(GL11.GL_TEXTURE_2D);

        // Sets the width.
        GL11.glLineWidth(width);

        // Begins drawing the line.
        GL11.glBegin(GL11.GL_LINES);
        {
            GL11.glVertex2f(start.x, start.y);
            GL11.glVertex2f(end.x, end.y);
        }
        // Ends drawing the line.
        GL11.glEnd();

        // Enables texturing back on.
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    /**
     * Generates all the colors.
     */
    private void generateColors(){
        // Iterates through 32 colors.
        for (int i = 0; i < 32; i++)
        {
            // Not sure what this variable is.
            int thingy = (i >> 3 & 1) * 85;

            // The red value of the color.
            int red = (i >> 2 & 1) * 170 + thingy;

            // The green value of the color.
            int green = (i >> 1 & 1) * 170 + thingy;

            // The blue value of the color.
            int blue = (i >> 0 & 1) * 170 + thingy;

            // Increments the red by 85, not sure why does this in minecraft's font renderer.
            if (i == 6) red += 85;

            // Used to make the shadow darker.
            if (i >= 16){
                red /= 4;
                green /= 4;
                blue /= 4;
            }

            // Sets the color in the color code at the index of 'i'.
            this.colorCodes[i] = (red & 255) << 16 | (green & 255) << 8 | blue & 255;
        }
    }

    /**
     * Sets the random offset.
     */
    public void makeRandom(){
        // Sets the random offset.
        RANDOM_OFFSET = Helper.mathUtils().getRandom(3) - 2;

        while(RANDOM_OFFSET == 0)
            // Sets the random offset.
            RANDOM_OFFSET = Helper.mathUtils().getRandom(3) - 2;
    }

    /**
     * Class that holds the data for each character.
     */
    class CharacterData {

        /**
         * The character the data belongs to.
         */
        public char character;

        /**
         * The width of the character.
         */
        public float width;

        /**
         * The height of the character.
         */
        public float height;

        /**
         * The id of the character texture.
         */
        private int textureId;

        public CharacterData(char character, float width, float height, int textureId) {
            this.character = character;
            this.width = width;
            this.height = height;
            this.textureId = textureId;
        }

        /**
         * Binds the texture.
         */
        public void bind(){
            // Binds the opengl texture by the texture id.
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
        }

    }
   
}