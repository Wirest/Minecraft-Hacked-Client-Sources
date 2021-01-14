/**
 * Time: 4:40:32 PM
 * Date: Dec 16, 2016
 * Creator: cool1
 */
package info.sigmaclient.management.command.impl;

import info.sigmaclient.event.Event;
import info.sigmaclient.Client;
import info.sigmaclient.management.command.Command;
import info.sigmaclient.module.Module;
import info.sigmaclient.util.misc.ChatUtil;

/**
 * @author cool1
 */
public class Toggle extends Command {

    /**
     * @param names
     * @param description
     */
    public Toggle(String[] names, String description) {
        super(names, description);
        // TODO Auto-generated constructor stub
    }

    /*
     * (non-Javadoc)
     *
     * @see me.arithmo.command.Fireable#fire(java.lang.String[])
     */
    @Override
    public void fire(String[] args) {
        if (args == null) {
            printUsage();
            return;
        }
        Module module = null;
        if (args.length > 0) {
            module = Client.getModuleManager().get(args[0]);
        }
        if (module == null) {
            printUsage();
            return;
        }
        if (args.length == 1) {
            module.toggle();
            ChatUtil.printChat(chatPrefix + module.getName() + " has been" + (module.isEnabled() ? "\247a Enabled." : "\247c Disabled."));
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see me.arithmo.command.Command#getUsage()
     */
    @Override
    public String getUsage() {

        return "toggle <module name>";
    }

    @Override
    public void onEvent(Event event) {

    }
}
