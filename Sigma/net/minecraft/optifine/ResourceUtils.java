package net.minecraft.optifine;

import java.io.File;

import net.minecraft.client.resources.AbstractResourcePack;

public class ResourceUtils {
    private static ReflectorClass ForgeAbstractResourcePack = new ReflectorClass(AbstractResourcePack.class);
    private static ReflectorField ForgeAbstractResourcePack_resourcePackFile = new ReflectorField(ResourceUtils.ForgeAbstractResourcePack, "resourcePackFile");
    private static boolean directAccessValid = true;

    public static File getResourcePackFile(AbstractResourcePack arp) {
        if (ResourceUtils.directAccessValid) {
            try {
                return arp.resourcePackFile;
            } catch (IllegalAccessError var2) {
                ResourceUtils.directAccessValid = false;

                if (!ResourceUtils.ForgeAbstractResourcePack_resourcePackFile.exists()) {
                    throw var2;
                }
            }
        }

        return (File) Reflector.getFieldValue(arp, ResourceUtils.ForgeAbstractResourcePack_resourcePackFile);
    }
}
