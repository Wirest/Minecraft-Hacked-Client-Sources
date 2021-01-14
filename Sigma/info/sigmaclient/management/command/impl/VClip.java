/**
 * Time: 10:14:03 PM
 * Date: Jan 4, 2017
 * Creator: cool1
 */
package info.sigmaclient.management.command.impl;

import info.sigmaclient.event.Event;
import info.sigmaclient.management.command.Command;
import info.sigmaclient.util.StringConversions;

public class VClip extends Command {

    public VClip(String[] names, String description) {
        super(names, description);

    }

    @Override
    public void fire(String[] args) {
        if (args == null) {
            printUsage();
            return;
        }
        if (args.length == 1) {
            if (StringConversions.isNumeric(args[0])) {
                mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + Double.parseDouble(args[0]), mc.thePlayer.posZ);
                return;
            }
        }
        printUsage();
    }

    @Override
    public String getUsage() {
        return "vclip <distance>";
    }

    @Override
    public void onEvent(Event event) {

    }
}
