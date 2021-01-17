package me.rigamortis.faurax.module.modules.world;

import me.rigamortis.faurax.module.helpers.*;
import me.rigamortis.faurax.module.*;
import me.rigamortis.faurax.*;
import me.rigamortis.faurax.values.*;
import net.minecraft.tileentity.*;
import net.minecraft.client.gui.inventory.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;
import net.minecraft.inventory.*;
import net.minecraft.entity.player.*;
import java.util.*;
import com.darkmagician6.eventapi.*;
import me.rigamortis.faurax.events.*;

public class ChestStealer extends Module implements WorldHelper, PlayerHelper
{
    public TileEntityChest toCheck;
    public ArrayList<TileEntityChest> chests;
    public ArrayList<TileEntityChest> checked;
    public ArrayList<TileEntityChest> finshed;
    public int stealDelay;
    public static int delay;
    public double posX;
    public double posY;
    public double posZ;
    private float oldYaw;
    private float oldPitch;
    public static Value autoClose;
    public static Value autoDrop;
    public static Value autoOpen;
    public static Value silent;
    public static Value stealDelayValue;
    
    static {
        ChestStealer.autoClose = new Value("ChestStealer", Boolean.TYPE, "AutoClose", true);
        ChestStealer.autoDrop = new Value("ChestStealer", Boolean.TYPE, "AutoDrop", false);
        ChestStealer.autoOpen = new Value("ChestStealer", Boolean.TYPE, "AutoOpen", true);
        ChestStealer.silent = new Value("ChestStealer", Boolean.TYPE, "Silent", false);
        ChestStealer.stealDelayValue = new Value("ChestStealer", Float.TYPE, "StealDelay", 2.0f, 0.0f, 10.0f);
    }
    
    public ChestStealer() {
        this.toCheck = null;
        this.chests = new ArrayList<TileEntityChest>();
        this.checked = new ArrayList<TileEntityChest>();
        this.finshed = new ArrayList<TileEntityChest>();
        this.setType(ModuleType.WORLD);
        this.setName("ChestStealer");
        this.setKey("");
        this.setColor(-6402356);
        this.setModInfo("");
        this.setVisible(true);
        Client.getValues();
        ValueManager.values.add(ChestStealer.autoClose);
        Client.getValues();
        ValueManager.values.add(ChestStealer.autoDrop);
        Client.getValues();
        ValueManager.values.add(ChestStealer.autoOpen);
        Client.getValues();
        ValueManager.values.add(ChestStealer.silent);
        Client.getValues();
        ValueManager.values.add(ChestStealer.stealDelayValue);
    }
    
    @Override
    public void onToggled() {
        super.onToggled();
        this.toCheck = null;
        ChestStealer.delay = 0;
        this.checked.clear();
        this.finshed.clear();
        this.chests.clear();
    }
    
    @EventTarget(4)
    public void preTick(final EventPreTick e) {
        if (this.isToggled()) {
            if (ChestStealer.autoOpen.getBooleanValue()) {
                this.oldPitch = ChestStealer.mc.thePlayer.rotationPitch;
                this.oldYaw = ChestStealer.mc.thePlayer.rotationYaw;
                for (final Object o : ChestStealer.mc.theWorld.loadedTileEntityList) {
                    final TileEntity tileEntity = (TileEntity)o;
                    if (tileEntity instanceof TileEntityChest) {
                        final double x = tileEntity.getPos().getX();
                        final double y = tileEntity.getPos().getY();
                        final double z = tileEntity.getPos().getZ();
                        if (!Client.getClientHelper().canReach(x, y, z, 5.0f) || this.chests.contains(tileEntity) || this.finshed.contains(tileEntity) || this.checked.contains(tileEntity)) {
                            continue;
                        }
                        this.chests.add((TileEntityChest)tileEntity);
                    }
                }
                final Iterator<TileEntityChest> iterator2 = this.chests.iterator();
                if (iterator2.hasNext()) {
                    final TileEntityChest chest = iterator2.next();
                    this.toCheck = chest;
                }
                if (this.toCheck != null && !(ChestStealer.mc.currentScreen instanceof GuiChest)) {
                    this.posX = this.toCheck.getPos().getX();
                    this.posY = this.toCheck.getPos().getY();
                    this.posZ = this.toCheck.getPos().getZ();
                    Client.getClientHelper().faceBlock(this.posX + 0.5, this.posY + 0.5, this.posZ + 0.5);
                    ++ChestStealer.delay;
                    if (ChestStealer.delay >= 5) {
                        ChestStealer.mc.thePlayer.swingItem();
                        ChestStealer.mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(Client.getClientHelper().getBlockPos(this.posX, this.posY, this.posZ), Client.getClientHelper().getEnumFacing((float)this.posX, (float)this.posY, (float)this.posZ).getIndex(), ChestStealer.mc.thePlayer.getCurrentEquippedItem(), 0.0f, 0.0f, 0.0f));
                        this.chests.remove(this.toCheck);
                        this.checked.add(this.toCheck);
                        ChestStealer.delay = 0;
                        this.toCheck = null;
                    }
                }
            }
            if (!Client.getClientHelper().isContainerEmpty(ChestStealer.mc.thePlayer.openContainer) && ChestStealer.mc.thePlayer.openContainer instanceof ContainerChest) {
                if (ChestStealer.silent.getBooleanValue()) {
                    ChestStealer.mc.setIngameFocus();
                }
                final int slotId = Client.getClientHelper().getNextSlotInContainer(ChestStealer.mc.thePlayer.openContainer);
                this.setModInfo(" §7Stealing");
                if (ChestStealer.stealDelayValue.getFloatValue() <= 0.0f) {
                    for (int i = 0; i < 36; ++i) {
                        if (ChestStealer.autoDrop.getBooleanValue()) {
                            ChestStealer.mc.playerController.windowClick(ChestStealer.mc.thePlayer.openContainer.windowId, slotId + i, 1, 4, ChestStealer.mc.thePlayer);
                        }
                        else {
                            ChestStealer.mc.playerController.windowClick(ChestStealer.mc.thePlayer.openContainer.windowId, slotId + i, 1, 1, ChestStealer.mc.thePlayer);
                        }
                    }
                }
                else {
                    ++this.stealDelay;
                    if (this.stealDelay >= ChestStealer.stealDelayValue.getFloatValue()) {
                        if (ChestStealer.autoDrop.getBooleanValue()) {
                            ChestStealer.mc.playerController.windowClick(ChestStealer.mc.thePlayer.openContainer.windowId, slotId, 1, 4, ChestStealer.mc.thePlayer);
                        }
                        else {
                            ChestStealer.mc.playerController.windowClick(ChestStealer.mc.thePlayer.openContainer.windowId, slotId, 1, 1, ChestStealer.mc.thePlayer);
                        }
                        this.stealDelay = 0;
                    }
                }
            }
            else {
                this.setModInfo(" §7Done");
                if (ChestStealer.autoClose.getBooleanValue() && ChestStealer.mc.currentScreen instanceof GuiChest) {
                    ChestStealer.mc.thePlayer.closeScreen();
                }
            }
        }
    }
    
    @EventTarget(0)
    public void postTick(final EventPostTick e) {
        if (this.isToggled() && ChestStealer.autoOpen.getBooleanValue()) {
            ChestStealer.mc.thePlayer.rotationPitch = this.oldPitch;
            ChestStealer.mc.thePlayer.rotationYaw = this.oldYaw;
        }
    }
}
