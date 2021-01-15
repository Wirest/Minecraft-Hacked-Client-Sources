package dev.astroclient.client.ui.menu.click.component.components.buttons;

import dev.astroclient.client.Client;
import dev.astroclient.client.feature.ToggleableFeature;
import dev.astroclient.client.ui.menu.click.component.Category;
import dev.astroclient.client.ui.menu.click.component.Component;
import dev.astroclient.client.ui.menu.click.component.components.GroupBox;
import dev.astroclient.client.util.MouseUtil;
import dev.astroclient.client.util.render.Render2DUtil;
import com.google.common.collect.Sets;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * made by oHare for Ayyware
 *
 * @since 4/10/2019
 **/
public class KeybindButton extends Component {
    private ToggleableFeature mod;
    private boolean binding;
    private String bind;

    private final Set<Integer> disallowedKeys = Sets.newHashSet(Keyboard.KEY_ESCAPE, Keyboard.KEY_RETURN, Keyboard.KEY_BACK, Keyboard.KEY_LSHIFT, Keyboard.KEY_LCONTROL, Keyboard.KEY_RCONTROL, Keyboard.KEY_NUMLOCK, Keyboard.KEY_LMETA, Keyboard.KEY_APPS, Keyboard.KEY_RMENU, Keyboard.KEY_LMENU, Keyboard.KEY_UP, Keyboard.KEY_DOWN, Keyboard.KEY_LEFT, Keyboard.KEY_RIGHT, Keyboard.KEY_END, Keyboard.KEY_DELETE, Keyboard.KEY_HOME, Keyboard.KEY_SCROLL, Keyboard.KEY_PAUSE, Keyboard.KEY_SYSRQ, Keyboard.KEY_SLASH, Keyboard.KEY_SPACE);

    private Map<String, String> replace = new HashMap<>();

    public KeybindButton(Category parent, String label, int posX, int posY, int width, int height, ToggleableFeature mod) {
        super(parent, label, posX, posY, width, height);
        this.mod = mod;

        replace.put("GRAVE", "`");
        replace.put("EQUALS", "=");
        replace.put("MINUS", "-");
        replace.put("ADD", "+");
        replace.put("SUBTRACT", "-");
        replace.put("MULTIPLY", "*");
        replace.put("DIVIDE", "/");
        replace.put("APOSTROPHE", "'");
        replace.put("SEMICOLON", ";");
        replace.put("RBRACKET", "]");
        replace.put("LBRACKET", "[");
        replace.put("DECIMAL", ".");
        replace.put("PRIOR", "PUP");
        replace.put("NEXT", "PDOW");
        replace.put("CAPITAL", "CAPS");

        for (int i = 0; i < 10; i++)
            replace.put("NUMPAD" + 1, String.valueOf(i));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        float posX = getParent().getParent().getPosX() + getParent().getPosX() + 50 + getPosX() - Client.INSTANCE.smallFontRenderer.getStringWidth(bind);
        float posY = getParent().getParent().getPosY() + getParent().getPosY() + 9 + getPosY() + ((GroupBox) getParent()).getScrollY();
        if (binding) {
            bind = "[...]";
        } else {
            String keybind = Keyboard.getKeyName(mod.getBind());
            if (replace.containsKey(keybind)) {
                keybind = replace.get(keybind);
            }
            if (mod.getBind() == 0) {
                bind = "[-]";
            } else {
                bind = "[" + keybind + "]";
            }
        }
        if (MouseUtil.mouseWithinBounds(mouseX, mouseY, posX - 8, posY - 1, posX + 8 + Client.INSTANCE.smallFontRenderer.getStringWidth(bind), posY + 6)) {
            Render2DUtil.drawRect(posX - 8, posY, posX - 8 + Client.INSTANCE.smallFontRenderer.getStringWidth(bind), posY + 7, new Color(92, 104, 97, Math.min(getParent().getParent().getParent().getAlpha(), 190)).getRGB());
        }
        Client.INSTANCE.smallFontRenderer.drawStringWithOutline(bind, posX - 8, posY + 1.5, new Color(210, 210, 210, Math.min(getParent().getParent().getParent().getAlpha(), 255)).getRGB());
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        float posX = getParent().getParent().getPosX() + getParent().getPosX() + 50 + getPosX() - Client.INSTANCE.smallFontRenderer.getStringWidth(bind);
        float posY = getParent().getParent().getPosY() + getParent().getPosY() + 9 + getPosY() + ((GroupBox) getParent()).getScrollY();
        if (MouseUtil.mouseWithinBounds(mouseX, mouseY, posX - 8, posY - 1, posX - 8 + Client.INSTANCE.smallFontRenderer.getStringWidth(bind), posY + 6) && mouseButton == 0) {
            binding = !binding;
            getParent().getParent().dragging = false;
        }

    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {

    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        super.keyTyped(typedChar, keyCode);
        if (binding) {
            if (!disallowedKeys.contains(keyCode)) {
                mod.setBind(keyCode);
                binding = false;
            } else {
                mod.setBind(Keyboard.KEY_NONE);
                binding = false;
            }
        }
    }
}
