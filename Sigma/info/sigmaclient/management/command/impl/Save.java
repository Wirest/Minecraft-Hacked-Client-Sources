package info.sigmaclient.management.command.impl;

import info.sigmaclient.event.Event;
import info.sigmaclient.management.command.Command;
import info.sigmaclient.module.Module;
import info.sigmaclient.util.misc.ChatUtil;

/**
 * Created by cool1 on 1/12/2017.
 */
public class Save extends Command {

    /**
     * @param names
     * @param description
     */
    public Save(String[] names, String description) {
        super(names, description);
        // TODO Auto-generated constructor stub
    }

    /* (non-Javadoc)
     * @see me.arithmo.command.Fireable#fire(java.lang.String[])
     */
    @Override
    public void fire(String[] args) {
        Module.saveStatus();
        Module.saveSettings();
        ChatUtil.printChat(chatPrefix + "Saved configurations.");
    }

    /* (non-Javadoc)
     * @see me.arithmo.command.Command#getUsage()
     */
    @Override
    public String getUsage() {
        // TODO Auto-generated method stub
        return "Saves config.";
    }

    @Override
    public void onEvent(Event event) {

    }
}

