package dev.astroclient.client.feature;

import java.awt.*;

/**
* @author Zane for PublicBase
* @since 10/24/19
*/

public enum Category {
    COMBAT(new Color(255, 119, 116), 'C'),
    MOVEMENT(new Color(158, 255, 112), 'G'),
    VISUALS(new Color(255, 148, 111), 'E'),
    MISC(new Color(212, 162, 255), 'H'),
    EXPLOITS(new Color(129, 230, 255), 'F');

    public Color getColor() {
        return color;
    }

    private Color color;

    public char getCharacter() {
        return character;
    }

    private char character;

    Category(Color color, char character) {
        this.color = color;
        this.character = character;
    }
}
