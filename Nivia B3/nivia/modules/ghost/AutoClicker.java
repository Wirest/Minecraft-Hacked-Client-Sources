package nivia.modules.ghost;

import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import nivia.Pandora;
import nivia.commands.Command;
import nivia.events.EventTarget;
import nivia.events.events.EventTick;
import nivia.managers.PropertyManager.DoubleProperty;
import nivia.modules.Module;
import nivia.utils.Helper;
import nivia.utils.Logger;
import nivia.utils.utils.Timer;

import java.util.Random;

public class AutoClicker extends Module {
    private Timer timer = new Timer();
    private Random rand = new Random();
    public DoubleProperty CPS = new DoubleProperty(this, "CPS", 10, 1, 20);
    public AutoClicker() {
        super("AutoClicker", 0, 0xE6B800, Category.GHOST, "Clicks Automatically.", new String[]{"aclicker", "ac", "autoc", "aclick"}, true);
    }

    @EventTarget
    public void onUpdate(EventTick event){
        if(mc.gameSettings.keyBindAttack.getIsKeyPressed()){
            Random random = new Random();

            if(timer.hasTimeElapsed((long) ((long) 1000 / CPS.getValue() - random.nextInt(17)), true)) {
                if(Helper.player().isBlocking()) {
                    Helper.sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(0, 0, 0), EnumFacing.UP));
                    Helper.player().swingItem();
                    Helper.sendPacket(new C02PacketUseEntity(mc.objectMouseOver.entityHit, C02PacketUseEntity.Action.ATTACK));
                } else {
                    Helper.player().swingItem();
                    Helper.sendPacket(new C02PacketUseEntity(mc.objectMouseOver.entityHit, C02PacketUseEntity.Action.ATTACK));
                }
            }
        }
    }
    protected void addCommand() {
		Pandora.getCommandManager().cmds
				.add(new Command("AutoClicker", "Manages autoclicker", Logger.LogExecutionFail("Option, Options:",
						new String[] { "CPS" }), "aclicker", "aclick") {
					@Override
					public void execute(String commandName, String[] arguments) {
		                String message = arguments[1];
		                switch (message.toLowerCase()) {
		                    case "cps":
		                    case "aps":
		                        try {
		                            String message2 = arguments[2];
		                            Double d = Double.parseDouble(message2);
		                            CPS.setValue(d);
		                            Logger.logSetMessage("AutoClicker", "CPS", CPS);
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