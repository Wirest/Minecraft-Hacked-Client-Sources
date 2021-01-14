package info.sigmaclient.management.users.impl;

import info.sigmaclient.management.users.User;
import net.minecraft.client.Minecraft;

/**
 * Created by Arithmo on 8/12/2017 at 12:01 AM.
 */
public class Blacklisted extends User {

    public Blacklisted() {
        Minecraft.getMinecraft().shutdownMinecraftApplet();
    }

    @Override
    public void upgradeFeatures() {
        throw new RuntimeException();
    }

}
