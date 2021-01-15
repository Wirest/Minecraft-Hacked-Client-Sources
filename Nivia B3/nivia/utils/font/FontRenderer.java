package nivia.utils.font;

import java.awt.*;

/**
     * Created by Zeb on 9/23/2016.
     */
    public interface FontRenderer {

        /**
         * Draw a string.
         *
         * @param text text to be drawn.
         * @param x where the text should be on x axis.
         * @param y where the text should be on y axis.
         * @param color what color the text should be.
         */
        void drawString(String text, float x, float y, Color color);

        /**
         * Draw a string with shadow/
         *
         * @param text text to be drawn.
         * @param x where the text should be on x axis.
         * @param y where the text should be on y axis.
         * @param color what color the text should be.
         */
        void drawStringWithShadow(String text, float x, float y, Color color);

        /**
         * Get the height of the given text.
         *
         * @param text text to get the height of.
         * @return height of the text.
         */
        float getHeight(String text);

        /**
         * Get the width of the given text.
         *
         * @param text text to get the width of.
         * @return width of the text.
         */
        float getWidth(String text);

    }