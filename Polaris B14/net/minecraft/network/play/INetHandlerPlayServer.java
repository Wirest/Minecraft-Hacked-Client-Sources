package net.minecraft.network.play;

import net.minecraft.network.INetHandler;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0CPacketInput;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
import net.minecraft.network.play.client.C11PacketEnchantItem;
import net.minecraft.network.play.client.C12PacketUpdateSign;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;
import net.minecraft.network.play.client.C14PacketTabComplete;
import net.minecraft.network.play.client.C15PacketClientSettings;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.network.play.client.C18PacketSpectate;
import net.minecraft.network.play.client.C19PacketResourcePackStatus;

public abstract interface INetHandlerPlayServer
  extends INetHandler
{
  public abstract void handleAnimation(C0APacketAnimation paramC0APacketAnimation);
  
  public abstract void processChatMessage(C01PacketChatMessage paramC01PacketChatMessage);
  
  public abstract void processTabComplete(C14PacketTabComplete paramC14PacketTabComplete);
  
  public abstract void processClientStatus(C16PacketClientStatus paramC16PacketClientStatus);
  
  public abstract void processClientSettings(C15PacketClientSettings paramC15PacketClientSettings);
  
  public abstract void processConfirmTransaction(C0FPacketConfirmTransaction paramC0FPacketConfirmTransaction);
  
  public abstract void processEnchantItem(C11PacketEnchantItem paramC11PacketEnchantItem);
  
  public abstract void processClickWindow(C0EPacketClickWindow paramC0EPacketClickWindow);
  
  public abstract void processCloseWindow(C0DPacketCloseWindow paramC0DPacketCloseWindow);
  
  public abstract void processVanilla250Packet(C17PacketCustomPayload paramC17PacketCustomPayload);
  
  public abstract void processUseEntity(C02PacketUseEntity paramC02PacketUseEntity);
  
  public abstract void processKeepAlive(C00PacketKeepAlive paramC00PacketKeepAlive);
  
  public abstract void processPlayer(C03PacketPlayer paramC03PacketPlayer);
  
  public abstract void processPlayerAbilities(C13PacketPlayerAbilities paramC13PacketPlayerAbilities);
  
  public abstract void processPlayerDigging(C07PacketPlayerDigging paramC07PacketPlayerDigging);
  
  public abstract void processEntityAction(C0BPacketEntityAction paramC0BPacketEntityAction);
  
  public abstract void processInput(C0CPacketInput paramC0CPacketInput);
  
  public abstract void processHeldItemChange(C09PacketHeldItemChange paramC09PacketHeldItemChange);
  
  public abstract void processCreativeInventoryAction(C10PacketCreativeInventoryAction paramC10PacketCreativeInventoryAction);
  
  public abstract void processUpdateSign(C12PacketUpdateSign paramC12PacketUpdateSign);
  
  public abstract void processPlayerBlockPlacement(C08PacketPlayerBlockPlacement paramC08PacketPlayerBlockPlacement);
  
  public abstract void handleSpectate(C18PacketSpectate paramC18PacketSpectate);
  
  public abstract void handleResourcePackStatus(C19PacketResourcePackStatus paramC19PacketResourcePackStatus);
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\network\play\INetHandlerPlayServer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */