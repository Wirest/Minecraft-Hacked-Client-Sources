package nivia.modules.player;


import org.lwjgl.input.Keyboard;
import nivia.Pandora;
import nivia.commands.Command;
import nivia.events.EventTarget;
import nivia.events.events.EventPreMotionUpdates;
import nivia.managers.PropertyManager;
import nivia.modules.Module;
import nivia.utils.Helper;
import nivia.utils.Logger;

public class Timer extends Module {

    public Timer() {
        super("Timer", Keyboard.CHAR_NONE, 0xE6B800, Category.PLAYER, "Change timerino m8erino",
                new String[] { "tim", "slowmo", "ti" }, false);
    }
    public PropertyManager.DoubleProperty timerino = new PropertyManager.DoubleProperty(this, "Timerino", 0.1, 0.035, 20, 1);
    @EventTarget
    public void onPre(EventPreMotionUpdates event) {
    	if (Helper.playerUtils().MovementInput())
    		mc.timer.timerSpeed = (float) timerino.getValue();
    	else
    		mc.timer.timerSpeed = 1.0F;
    }

    @Override
    protected void onDisable() {
        super.onDisable();
        mc.timer.timerSpeed = 1.0F;
    }
    protected void addCommand() {
        Pandora.getCommandManager().cmds.add(new Command("Timer", "Manages timer values",
                Logger.LogExecutionFail("Option, Options:", new String[] { "delay" }), "tim", "slowmo", "ti") {
            @Override
            public void execute(String commandName, String[] arguments) {
                String message = arguments[1];
                switch (message.toLowerCase()) {
                    case "timer":
                    case "delay":
                    case "t":
                    case "d":
                        try {
                            String message2 = arguments[2];
                            Double d = Double.parseDouble(message2);
                            timerino.setValue(d);
                            Logger.logSetMessage("Timer", "Delay", timerino);
                        } catch (Exception e) {
                            Logger.LogExecutionFail("Value!");
                        }
                        break;
                    case "values":
                    case "actual":
                        logValues();
                        break;
                    default:
                        Logger.logChat(this.getError());
                        break;
                }
            }
        });
    }
}
