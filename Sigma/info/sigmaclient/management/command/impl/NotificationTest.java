/**
 * Time: 9:49:26 PM
 * Date: Jan 6, 2017
 * Creator: cool1
 */
package info.sigmaclient.management.command.impl;

import info.sigmaclient.Client;
import info.sigmaclient.event.Event;
import info.sigmaclient.gui.click.ClickGui;
import info.sigmaclient.management.command.Command;
import info.sigmaclient.management.notifications.Notifications;
import info.sigmaclient.management.notifications.Notifications.Type;
import info.sigmaclient.util.misc.ChatUtil;

/**
 * @author cool1
 */
public class NotificationTest extends Command {

    /**
     * @param names
     * @param description
     */
    public NotificationTest(String[] names, String description) {
        super(names, description);
    }

    /* (non-Javadoc)
     * @see me.arithmo.command.Fireable#fire(java.lang.String[])
     */
    @Override
    public void fire(String[] args) {
        if (args == null) {
            printUsage();
            return;
        }
        Notifications not = Notifications.getManager();
        if (args[0].equalsIgnoreCase("notify")) {
            not.post("Player Warning", "Some one called you a \247chacker!", 2500L, Type.NOTIFY);
        } else if (args[0].equalsIgnoreCase("warning")) {
            not.post("Warning Alert", "\247cBob \247fis now \2476Vanished!", 2500L, Type.WARNING);
        } else if (args[0].equalsIgnoreCase("info")) {
            not.post("Friend Info", "\247bArithmo \247fhas \247cdied!", 2500L, Type.INFO);
        } else if (args[0].equalsIgnoreCase("f")) {
            not.post("Friend Info", "\247aA \247fG \247cD!", 2500L, Type.INFO);
        } else if (args[0].equalsIgnoreCase("cgui")) {
            Client.clickGui = new ClickGui();
        } else {
            ChatUtil.printChat(chatPrefix + "This command is for debugging.");
        }
    }

    /* (non-Javadoc)
     * @see me.arithmo.command.Command#getUsage()
     */
    @Override
    public String getUsage() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onEvent(Event event) {

    }
}
