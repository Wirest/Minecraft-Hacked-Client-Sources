package moonx.ohare.client.utils.font;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public class Fonts {
    public static final MCFontRenderer tabGuiIconFont = new MCFontRenderer(fontFromTTF(new ResourceLocation("textures/client/Icon-Font.ttf"),22,0), true, true);
    public static final MCFontRenderer clickGuiIconFont = new MCFontRenderer(fontFromTTF(new ResourceLocation("textures/client/Icon-Font.ttf"),36,0), true, true);
    public static final MCFontRenderer clickGuiTitleFont = new MCFontRenderer(new Font("Arial", Font.BOLD,24),true,true);
    public static final MCFontRenderer clickGuiFont = new MCFontRenderer(new Font("Arial", Font.PLAIN,18),true,true);
    public static final MCFontRenderer clickGuiSmallFont = new MCFontRenderer(new Font("Arial", Font.PLAIN,12),true,true);
    private static Font fontFromTTF(ResourceLocation fontLocation, float fontSize, int fontType) {
        Font output = null;
        try {
            output = Font.createFont(fontType, Minecraft.getMinecraft().getResourceManager().getResource(fontLocation).getInputStream());
            output = output.deriveFont(fontSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }
}
