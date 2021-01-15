package dev.astroclient.client.feature;

import net.minecraft.client.Minecraft;

/**
 * @author Zane for PublicBase
 * @since 10/23/19
 */

public interface Feature {

    Minecraft mc = Minecraft.getMinecraft();

    String getLabel();

    Category getCategory();

    String getDisplayLabel();

    String getDescription();

    boolean isHidden();

    void setHidden(boolean hidden);

    String getSuffix();
}
