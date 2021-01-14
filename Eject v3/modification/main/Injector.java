package modification.main;

import net.minecraft.client.Minecraft;
import net.minecraft.client.main.GameConfiguration;
import org.lwjgl.opengl.Display;

public final class Injector
        extends Minecraft {
    public Injector(GameConfiguration paramGameConfiguration) {
        super(paramGameConfiguration);
    }

    protected void checkGLError(String paramString) {
        super.checkGLError(paramString);
        switch (paramString) {
            case "Pre startup":
                new Modification().initialize();
                break;
            case "Post startup":
                Display.setTitle("Eject".concat(" ").concat("v3.5.3"));
        }
    }
}




