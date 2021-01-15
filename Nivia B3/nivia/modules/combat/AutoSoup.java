package nivia.modules.combat;

import net.minecraft.item.ItemSoup;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.server.S06PacketUpdateHealth;
import nivia.commands.Command;
import nivia.events.EventTarget;
import nivia.events.events.EventPacketReceive;
import nivia.events.events.EventPostMotionUpdates;
import nivia.events.events.EventPreMotionUpdates;
import nivia.managers.CommandManager;
import nivia.managers.PropertyManager.DoubleProperty;
import nivia.modules.Module;
import nivia.utils.Helper;
import nivia.utils.Logger;
import nivia.utils.utils.Timer;

public class AutoSoup extends Module {
    public static int pots;
    private static boolean souping = false;
    private final Timer timer = new Timer();
    public DoubleProperty delay = new DoubleProperty(this, "Delay", 350L, 1, 1000);
    public DoubleProperty health = new DoubleProperty(this, "Health", 10, 1, 25);
    private float oldYaw, oldPitch;
    private boolean needsToPot = false;
    private int stage = 1;

    public AutoSoup() {
        super("AutoSoup", 0, 0xFFCCAADD, Category.COMBAT, "Throws potions automatically",
                new String[]{"as", "autos", "asoup"}, true);
    }

    public static boolean isSouping() {
        return souping;
    }

    @EventTarget
    public void EventPreMotionUpdates(EventPreMotionUpdates event) {
        setSuffix(String.valueOf(pots));
        if (this.updateCounter() == 0) {
            souping = false;
            return;
        }
        if (this.timer.hasTimeElapsed((long) this.delay.getValue(), false) && this.needsToPot) {
            if (this.doesHotbarHaveSoups())
                souping = true;

        }
    }

    @EventTarget
    public void onPost(EventPostMotionUpdates event) {
        this.needsToPot = mc.thePlayer.getHealth() <= this.health.getValue();
        if (this.needsToPot) {
            if (this.doesHotbarHaveSoups()) {
                if (souping) {
                    if (this.needsToPot) {
                        for (int i = 36; i < 45; i++) {
                            final ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                            if (stack != null) {
                                if (this.isSoup(stack)) {
                                    final int oldSlot = mc.thePlayer.inventory.currentItem;
                                    if (stage == 1) {
                                        if (this.timer.hasTimeElapsed((long) (this.delay.getValue()), true)) {
                                            stage++;
                                        }
                                    } else if (stage == 2) {
                                        stage = 1;
                                        Helper.sendPacket(new C09PacketHeldItemChange(i - 36));
                                        Helper.sendPacket(new C08PacketPlayerBlockPlacement(stack));
                                        Helper.sendPacket(new C09PacketHeldItemChange(oldSlot));
                               
                                        this.needsToPot = false;
                                        souping = false;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    this.timer.reset();
                }
            } else
                getSoupsFromInventory();
        }
    }

    @EventTarget
    public void onPacketReceive(EventPacketReceive e) {
        final EventPacketReceive packet = e;
        if (packet.getPacket() instanceof S06PacketUpdateHealth) {
            final S06PacketUpdateHealth packetUpdateHealth = (S06PacketUpdateHealth) packet.getPacket();
            if (!needsToPot)
                this.needsToPot = packetUpdateHealth.getHealth() <= this.health.getValue();

        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        souping = false;
        this.needsToPot = false;
    }

    private boolean doesHotbarHaveSoups() {
        for (int i = 36; i < 45; i++) {
            final ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (stack != null) {
                if (this.isSoup(stack))
                    return true;
            }
        }
        return false;
    }

    private void getSoupsFromInventory() {
        for (int i = 9; i < 36; i++) {
            final ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (stack != null) {
                if (this.isSoup(stack)) {
                    mc.playerController.windowClick(mc.thePlayer.openContainer.windowId, i, 1, 2, mc.thePlayer);
                    break;
                }
            }
        }
    }

    private boolean isSoup(final ItemStack stack) {
        if (stack != null) {
            if (stack.getItem() instanceof ItemSoup)
                return true;
        }
        return false;
    }

    private int updateCounter() {
        pots = 0;
        for (int i = 9; i < 45; i++) {
            final ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (stack != null) {
                if (isSoup(stack))
                    pots += stack.stackSize;
            }
        }
        return pots;
    }

    protected void addCommand() {
        CommandManager.cmds.add(new Command("AutoSoup", "Manages AutoSoup stuff",
                Logger.LogExecutionFail("Option, Options:", new String[]{"Health", "Delay", "Values"}), "as",
                "autos", "asoup") {
            @Override
            public void execute(String commandName, String[] arguments) {
                String message = arguments[1];
                switch (message) {
                    case "health":
                    case "hp":
                    case "Health":
                    case "h":
                        try {
                            String message2 = arguments[2];
                            switch (message2) {
                                case "actual":
                                case "value":
                                    logValue(health);
                                    break;
                                case "reset":
                                    health.value = health.getDefaultValue();
                                    Logger.logSetMessage("AutoSoup", "Health", health);
                                    break;
                                default:
                                    Double nHP = Double.parseDouble(message2);
                                    health.setValue(nHP);
                                    Logger.logSetMessage("AutoSoup", "Health", health);
                                    break;
                            }
                            break;
                        } catch (Exception e) {
                        }

                    case "Delay":
                    case "delay":
                    case "d":
                        String message21 = arguments[2];
                        switch (message21) {
                            case "actual":
                            case "value":
                                logValue(delay);
                                break;
                            case "reset":
                                delay.reset();
                                Logger.logSetMessage("AutoSoup", "delay", delay);
                                break;
                            default:
                                Long nD = Long.parseLong(message21);
                                delay.setValue(nD);
                                Logger.logSetMessage("AutoSoup", "delay", delay);
                                break;
                        }
                        break;
                    case "values":
                    case "actual":
                        logValues();
                        break;
                    default:
                        Logger.LogExecutionFail("Option, Options:", new String[]{"Health", "Delay", "Values"});
                        break;

                }
            }
        });
    }
}