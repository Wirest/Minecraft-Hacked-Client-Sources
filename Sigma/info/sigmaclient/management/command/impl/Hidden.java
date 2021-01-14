package info.sigmaclient.management.command.impl;

import info.sigmaclient.Client;
import info.sigmaclient.event.Event;
import info.sigmaclient.management.command.Command;
import info.sigmaclient.module.Module;
import info.sigmaclient.util.misc.ChatUtil;

/**
 * Created by Arithmo on 5/24/2017 at 7:31 PM.
 */
public class Hidden extends Command {

    public Hidden(String[] names, String description) {
        super(names, description);
    }

    @Override
    public void fire(String[] args) {
        if (args != null) {
            if (args.length == 1) {
                for (Module module : Client.getModuleManager().getArray()) {
                    if (module.getName().equalsIgnoreCase(args[0])) {
                        module.setHidden(!module.isHidden());
                        String xd = module.isHidden() ? "\247cHidden" : "\247aVisible";
                        ChatUtil.printChat(chatPrefix + module.getName() + " is now " + xd + "\2477.");
                        return;
                    }
                }
            }
        }
        printUsage();
    }

    @Override
    public void onEvent(Event event) {

    }

    @Override
    public String getUsage() {
        return "visible <module name>";
    }

}
