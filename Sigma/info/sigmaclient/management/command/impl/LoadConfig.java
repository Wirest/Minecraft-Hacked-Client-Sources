/**
 * Time: 8:05:43 PM
 * Date: Jan 9, 2017
 * Creator: cool1
 */
package info.sigmaclient.management.command.impl;

import info.sigmaclient.event.Event;
import info.sigmaclient.management.command.Command;
import info.sigmaclient.module.Module;
import info.sigmaclient.util.misc.ChatUtil;

/**
 * @author cool1
 */
public class LoadConfig extends Command {

    /**
     * @param names
     * @param description
     */
    public LoadConfig(String[] names, String description) {
        super(names, description);
        // TODO Auto-generated constructor stub
    }

    /* (non-Javadoc)
     * @see me.arithmo.command.Fireable#fire(java.lang.String[])
     */
    @Override
    public void fire(String[] args) {
        Module.loadStatus();
        Module.loadSettings();
        ChatUtil.printChat("Loaded configurations.");
    }

    /* (non-Javadoc)
     * @see me.arithmo.command.Command#getUsage()
     */
    @Override
    public String getUsage() {
        // TODO Auto-generated method stub
        return "Loads config.";
    }

    @Override
    public void onEvent(Event event) {

    }
}
