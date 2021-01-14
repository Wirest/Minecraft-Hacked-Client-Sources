package info.sigmaclient.management.command.impl;

import info.sigmaclient.event.Event;
import info.sigmaclient.management.command.Command;
import info.sigmaclient.module.impl.combat.Killaura;
import info.sigmaclient.util.misc.ChatUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;

public class Target extends Command {

    protected final Minecraft mc = Minecraft.getMinecraft();

    public Target(String[] names, String description) {
        super(names, description);
    }

    @Override
    public void fire(String[] args) {
        if (args == null) {
            printUsage();
            return;
        }
        if (args.length > 0) {
            String name = args[0];
            if (mc.theWorld.getPlayerEntityByName(name) != null) {
                EntityLivingBase vip = mc.theWorld.getPlayerEntityByName(name);
                Killaura.vip = vip;
                ChatUtil.printChat(chatPrefix
                        + "Now targeting " + args[0]);
                return;
            } else {
                Killaura.vip = null;
                ChatUtil.printChat(chatPrefix
                        + "No entity with the name " + "\"" + args[0] + "\"" + " currently exists.");
            }
        }
        printUsage();
        return;
    }

    @Override
    public String getUsage() {
        // TODO Auto-generated method stub
        return "Target <Target>";
    }

    @Override
    public void onEvent(Event event) {

    }
}
