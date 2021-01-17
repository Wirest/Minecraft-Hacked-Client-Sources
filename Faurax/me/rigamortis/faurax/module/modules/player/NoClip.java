package me.rigamortis.faurax.module.modules.player;

import me.rigamortis.faurax.module.helpers.*;
import me.rigamortis.faurax.module.*;
import me.rigamortis.faurax.*;
import me.rigamortis.faurax.values.*;
import com.darkmagician6.eventapi.*;
import net.minecraft.entity.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;
import net.minecraft.client.entity.*;
import me.rigamortis.faurax.events.*;

public class NoClip extends Module implements PlayerHelper, MovementHelper
{
    public int delay;
    public static Value mode;
    
    static {
        NoClip.mode = new Value("Phase", String.class, "Mode", "Current", new String[] { "Skip", "Current", "Old", "Sand", "Vanilla", "Vanilla Old" });
    }
    
    public NoClip() {
        this.setName("Phase");
        this.setKey("X");
        this.setColor(-15104089);
        this.setType(ModuleType.PLAYER);
        this.setModInfo("");
        this.setVisible(true);
        Client.getValues();
        ValueManager.values.add(NoClip.mode);
    }
    
    @EventTarget
    public void pushOutOfBlocks(final EventPushOutOfBlock e) {
        if (this.isToggled()) {
            e.setCancelled(true);
        }
    }
    
    @EventTarget
    public void blockBoundingbox(final EventBlockBoundingbox e) {
        if (this.isToggled()) {
            if (!NoClip.mode.getSelectedOption().equalsIgnoreCase("Sand") && !NoClip.mode.getSelectedOption().equalsIgnoreCase("Vanilla") && !NoClip.mode.getSelectedOption().equalsIgnoreCase("Current")) {
                if (NoClip.mc.thePlayer.isCollidedHorizontally) {
                    e.setCancelled(true);
                }
                else {
                    e.setCancelled(false);
                }
            }
            if (e.getY() > NoClip.mc.thePlayer.posY - 1.0) {
                e.boundingBox = null;
            }
            if (NoClip.mc.thePlayer.isCollidedHorizontally && e.getY() > NoClip.mc.thePlayer.boundingBox.minY - 0.4) {
                e.boundingBox = null;
            }
            if (NoClip.mode.getSelectedOption().equalsIgnoreCase("Sand")) {
                e.setCancelled(true);
            }
        }
    }
    
    @EventTarget
    public void preTick(final EventPreTick e) {
        if (this.isToggled()) {
            if (NoClip.mode.getSelectedOption().equalsIgnoreCase("Sand")) {
                this.setModInfo(" §7Sand");
                NoClip.mc.thePlayer.motionY = 0.0;
                if (NoClip.mc.gameSettings.keyBindJump.pressed) {
                    final EntityPlayerSP thePlayer = NoClip.mc.thePlayer;
                    thePlayer.motionY += 0.10000000149011612;
                }
                if (NoClip.mc.gameSettings.keyBindSneak.pressed) {
                    final EntityPlayerSP thePlayer2 = NoClip.mc.thePlayer;
                    thePlayer2.motionY -= 0.10000000149011612;
                }
            }
            if (NoClip.mode.getSelectedOption().equalsIgnoreCase("Vanilla")) {
                this.setModInfo(" §7Vanilla");
                final float dist = 2.0f;
                if (NoClip.mc.thePlayer.isCollidedHorizontally && NoClip.mc.thePlayer.moveForward != 0.0f) {
                    ++this.delay;
                    if (Client.getClientHelper().direction(NoClip.mc.thePlayer).equalsIgnoreCase("south")) {
                        NoClip.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(NoClip.mc.thePlayer.posX, NoClip.mc.thePlayer.posY, NoClip.mc.thePlayer.posZ + 9.999999747378752E-6, false));
                    }
                    if (Client.getClientHelper().direction(NoClip.mc.thePlayer).equalsIgnoreCase("west")) {
                        NoClip.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(NoClip.mc.thePlayer.posX - 9.999999747378752E-6, NoClip.mc.thePlayer.posY, NoClip.mc.thePlayer.posZ, false));
                    }
                    if (Client.getClientHelper().direction(NoClip.mc.thePlayer).equalsIgnoreCase("north")) {
                        NoClip.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(NoClip.mc.thePlayer.posX, NoClip.mc.thePlayer.posY, NoClip.mc.thePlayer.posZ - 9.999999747378752E-6, false));
                    }
                    if (Client.getClientHelper().direction(NoClip.mc.thePlayer).equalsIgnoreCase("east")) {
                        NoClip.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(NoClip.mc.thePlayer.posX + 9.999999747378752E-6, NoClip.mc.thePlayer.posY, NoClip.mc.thePlayer.posZ, false));
                    }
                    if (this.delay >= 1) {
                        if (Client.getClientHelper().direction(NoClip.mc.thePlayer).equalsIgnoreCase("south")) {
                            NoClip.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(NoClip.mc.thePlayer.posX, NoClip.mc.thePlayer.posY, NoClip.mc.thePlayer.posZ + dist, false));
                        }
                        if (Client.getClientHelper().direction(NoClip.mc.thePlayer).equalsIgnoreCase("west")) {
                            NoClip.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(NoClip.mc.thePlayer.posX - dist, NoClip.mc.thePlayer.posY, NoClip.mc.thePlayer.posZ, false));
                        }
                        if (Client.getClientHelper().direction(NoClip.mc.thePlayer).equalsIgnoreCase("north")) {
                            NoClip.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(NoClip.mc.thePlayer.posX, NoClip.mc.thePlayer.posY, NoClip.mc.thePlayer.posZ - dist, false));
                        }
                        if (Client.getClientHelper().direction(NoClip.mc.thePlayer).equalsIgnoreCase("east")) {
                            NoClip.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(NoClip.mc.thePlayer.posX + dist, NoClip.mc.thePlayer.posY, NoClip.mc.thePlayer.posZ, false));
                        }
                        this.delay = 0;
                    }
                }
            }
            if (NoClip.mode.getSelectedOption().equalsIgnoreCase("Skip")) {
                this.setModInfo(" §7Skip");
                final float dist = 0.4f;
                if (NoClip.mc.thePlayer.isCollidedHorizontally) {
                    ++this.delay;
                }
                if (NoClip.mc.thePlayer.isCollidedHorizontally && NoClip.mc.thePlayer.moveForward != 0.0f) {
                    if (Client.getClientHelper().direction(NoClip.mc.thePlayer).equalsIgnoreCase("south")) {
                        NoClip.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(NoClip.mc.thePlayer.posX, NoClip.mc.thePlayer.posY, NoClip.mc.thePlayer.posZ + dist, true));
                    }
                    if (Client.getClientHelper().direction(NoClip.mc.thePlayer).equalsIgnoreCase("west")) {
                        NoClip.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(NoClip.mc.thePlayer.posX - dist, NoClip.mc.thePlayer.posY, NoClip.mc.thePlayer.posZ, true));
                    }
                    if (Client.getClientHelper().direction(NoClip.mc.thePlayer).equalsIgnoreCase("north")) {
                        NoClip.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(NoClip.mc.thePlayer.posX, NoClip.mc.thePlayer.posY, NoClip.mc.thePlayer.posZ - dist, true));
                    }
                    if (Client.getClientHelper().direction(NoClip.mc.thePlayer).equalsIgnoreCase("east")) {
                        NoClip.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(NoClip.mc.thePlayer.posX + dist, NoClip.mc.thePlayer.posY, NoClip.mc.thePlayer.posZ, true));
                    }
                }
                if (this.delay >= 1) {
                    NoClip.mc.thePlayer.motionY = 0.0;
                    NoClip.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(NoClip.mc.thePlayer.posX, NoClip.mc.thePlayer.posY - 0.10000000149011612, NoClip.mc.thePlayer.posZ, true));
                    NoClip.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(NoClip.mc.thePlayer.posX, NoClip.mc.thePlayer.posY, NoClip.mc.thePlayer.posZ, true));
                    this.delay = 0;
                }
            }
            if (NoClip.mode.getSelectedOption().equalsIgnoreCase("Old")) {
                this.setModInfo(" §7Old");
                float dist = 0.3f;
                if (NoClip.mc.thePlayer.isCollidedHorizontally) {
                    ++this.delay;
                }
                if (NoClip.mc.thePlayer.isCollidedHorizontally && NoClip.mc.thePlayer.moveForward != 0.0f) {
                    if (Client.getClientHelper().direction(NoClip.mc.thePlayer).equalsIgnoreCase("south")) {
                        NoClip.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(NoClip.mc.thePlayer.posX, NoClip.mc.thePlayer.posY, NoClip.mc.thePlayer.posZ + dist, true));
                    }
                    if (Client.getClientHelper().direction(NoClip.mc.thePlayer).equalsIgnoreCase("west")) {
                        NoClip.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(NoClip.mc.thePlayer.posX - dist, NoClip.mc.thePlayer.posY, NoClip.mc.thePlayer.posZ, true));
                    }
                    if (Client.getClientHelper().direction(NoClip.mc.thePlayer).equalsIgnoreCase("north")) {
                        NoClip.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(NoClip.mc.thePlayer.posX, NoClip.mc.thePlayer.posY, NoClip.mc.thePlayer.posZ - dist, true));
                    }
                    if (Client.getClientHelper().direction(NoClip.mc.thePlayer).equalsIgnoreCase("east")) {
                        NoClip.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(NoClip.mc.thePlayer.posX + dist, NoClip.mc.thePlayer.posY, NoClip.mc.thePlayer.posZ, true));
                    }
                }
                if (this.delay >= 1) {
                    dist = 0.1f;
                    if (NoClip.mc.thePlayer.isCollidedHorizontally && NoClip.mc.thePlayer.moveForward != 0.0f) {
                        if (Client.getClientHelper().direction(NoClip.mc.thePlayer).equalsIgnoreCase("south")) {
                            NoClip.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(NoClip.mc.thePlayer.posX, 2.147483647E9, NoClip.mc.thePlayer.posZ + dist, true));
                        }
                        if (Client.getClientHelper().direction(NoClip.mc.thePlayer).equalsIgnoreCase("west")) {
                            NoClip.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(NoClip.mc.thePlayer.posX - dist, 2.147483647E9, NoClip.mc.thePlayer.posZ, true));
                        }
                        if (Client.getClientHelper().direction(NoClip.mc.thePlayer).equalsIgnoreCase("north")) {
                            NoClip.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(NoClip.mc.thePlayer.posX, 2.147483647E9, NoClip.mc.thePlayer.posZ - dist, true));
                        }
                        if (Client.getClientHelper().direction(NoClip.mc.thePlayer).equalsIgnoreCase("east")) {
                            NoClip.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(NoClip.mc.thePlayer.posX + dist, 2.147483647E9, NoClip.mc.thePlayer.posZ, true));
                        }
                    }
                }
                if (this.delay >= 2) {
                    dist = 0.2f;
                    if (NoClip.mc.thePlayer.isCollidedHorizontally && NoClip.mc.thePlayer.moveForward != 0.0f) {
                        if (Client.getClientHelper().direction(NoClip.mc.thePlayer).equalsIgnoreCase("south")) {
                            NoClip.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(NoClip.mc.thePlayer.posX, -2.147483647E9, NoClip.mc.thePlayer.posZ + dist, true));
                        }
                        if (Client.getClientHelper().direction(NoClip.mc.thePlayer).equalsIgnoreCase("west")) {
                            NoClip.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(NoClip.mc.thePlayer.posX - dist, -2.147483647E9, NoClip.mc.thePlayer.posZ, true));
                        }
                        if (Client.getClientHelper().direction(NoClip.mc.thePlayer).equalsIgnoreCase("north")) {
                            NoClip.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(NoClip.mc.thePlayer.posX, -2.147483647E9, NoClip.mc.thePlayer.posZ - dist, true));
                        }
                        if (Client.getClientHelper().direction(NoClip.mc.thePlayer).equalsIgnoreCase("east")) {
                            NoClip.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(NoClip.mc.thePlayer.posX + dist, -2.147483647E9, NoClip.mc.thePlayer.posZ, true));
                        }
                    }
                }
                if (this.delay >= 3) {
                    dist = 0.3f;
                    if (NoClip.mc.thePlayer.isCollidedHorizontally && NoClip.mc.thePlayer.moveForward != 0.0f) {
                        if (Client.getClientHelper().direction(NoClip.mc.thePlayer).equalsIgnoreCase("south")) {
                            NoClip.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(NoClip.mc.thePlayer.posX, NoClip.mc.thePlayer.posY, NoClip.mc.thePlayer.posZ + dist, true));
                        }
                        if (Client.getClientHelper().direction(NoClip.mc.thePlayer).equalsIgnoreCase("west")) {
                            NoClip.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(NoClip.mc.thePlayer.posX - dist, NoClip.mc.thePlayer.posY, NoClip.mc.thePlayer.posZ, true));
                        }
                        if (Client.getClientHelper().direction(NoClip.mc.thePlayer).equalsIgnoreCase("north")) {
                            NoClip.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(NoClip.mc.thePlayer.posX, NoClip.mc.thePlayer.posY, NoClip.mc.thePlayer.posZ - dist, true));
                        }
                        if (Client.getClientHelper().direction(NoClip.mc.thePlayer).equalsIgnoreCase("east")) {
                            NoClip.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(NoClip.mc.thePlayer.posX + dist, NoClip.mc.thePlayer.posY, NoClip.mc.thePlayer.posZ, true));
                        }
                    }
                    this.delay = 0;
                }
            }
        }
        if (NoClip.mode.getSelectedOption().equalsIgnoreCase("Current")) {
            this.setModInfo(" §7Current");
            if (NoClip.mc.thePlayer.isCollidedHorizontally) {
                ++this.delay;
            }
            if (NoClip.mc.thePlayer.motionY >= 0.30000001192092896) {
                NoClip.mc.thePlayer.motionY = -0.30000001192092896;
            }
            if (NoClip.mc.thePlayer.onGround) {
                NoClip.mc.thePlayer.jump();
            }
            if (NoClip.mc.thePlayer.isCollidedHorizontally && NoClip.mc.thePlayer.moveForward != 0.0f) {
                final double mx2 = Math.cos(Math.toRadians(NoClip.mc.thePlayer.rotationYaw + 90.0f));
                final double mz2 = Math.sin(Math.toRadians(NoClip.mc.thePlayer.rotationYaw + 90.0f));
                final double x = NoClip.mc.thePlayer.movementInput.moveForward * 0.3 * mx2 + NoClip.mc.thePlayer.movementInput.moveStrafe * 0.3 * mz2;
                final double z = NoClip.mc.thePlayer.movementInput.moveForward * 0.3 * mz2 - NoClip.mc.thePlayer.movementInput.moveStrafe * 0.3 * mx2;
                if (this.delay >= 3) {
                    NoClip.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(NoClip.mc.thePlayer.posX + x, NoClip.mc.thePlayer.posY, NoClip.mc.thePlayer.posZ + z, true));
                    for (int i = 0; i < 1; ++i) {
                        NoClip.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(NoClip.mc.thePlayer.posX + x, NoClip.mc.thePlayer.posY, NoClip.mc.thePlayer.posZ + z, true));
                    }
                    NoClip.mc.thePlayer.setPosition(NoClip.mc.thePlayer.posX + x, NoClip.mc.thePlayer.posY, NoClip.mc.thePlayer.posZ + z);
                    this.delay = 0;
                }
            }
        }
        if (NoClip.mode.getSelectedOption().equalsIgnoreCase("Vanilla Old")) {
            this.setModInfo(" §7Vanilla Old");
            final float dist = 3.0f;
            if (NoClip.mc.thePlayer.isCollidedHorizontally && NoClip.mc.thePlayer.moveForward != 0.0f) {
                if (Client.getClientHelper().direction(NoClip.mc.thePlayer).equalsIgnoreCase("south")) {
                    NoClip.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(NoClip.mc.thePlayer.posX, NoClip.mc.thePlayer.posY, NoClip.mc.thePlayer.posZ + dist, false));
                }
                if (Client.getClientHelper().direction(NoClip.mc.thePlayer).equalsIgnoreCase("west")) {
                    NoClip.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(NoClip.mc.thePlayer.posX - dist, NoClip.mc.thePlayer.posY, NoClip.mc.thePlayer.posZ, false));
                }
                if (Client.getClientHelper().direction(NoClip.mc.thePlayer).equalsIgnoreCase("north")) {
                    NoClip.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(NoClip.mc.thePlayer.posX, NoClip.mc.thePlayer.posY, NoClip.mc.thePlayer.posZ - dist, false));
                }
                if (Client.getClientHelper().direction(NoClip.mc.thePlayer).equalsIgnoreCase("east")) {
                    NoClip.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(NoClip.mc.thePlayer.posX + dist, NoClip.mc.thePlayer.posY, NoClip.mc.thePlayer.posZ, false));
                }
            }
        }
    }
    
    @EventTarget
    public void postTick(final EventPostTick e) {
        if (this.isToggled()) {
            NoClip.mode.getSelectedOption().equalsIgnoreCase("Current");
        }
    }
    
    @Override
    public void onEnabled() {
        super.onEnabled();
        if (NoClip.mode.getSelectedOption().equalsIgnoreCase("Sand")) {
            NoClip.mc.thePlayer.capabilities.isFlying = true;
        }
    }
    
    @Override
    public void onDisabled() {
        super.onDisabled();
        if (NoClip.mode.getSelectedOption().equalsIgnoreCase("Sand")) {
            NoClip.mc.thePlayer.capabilities.isFlying = false;
        }
        NoClip.mc.thePlayer.noClip = false;
    }
}
