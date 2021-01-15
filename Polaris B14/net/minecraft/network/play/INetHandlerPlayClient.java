package net.minecraft.network.play;

import net.minecraft.network.INetHandler;
import net.minecraft.network.play.server.S00PacketKeepAlive;
import net.minecraft.network.play.server.S01PacketJoinGame;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S03PacketTimeUpdate;
import net.minecraft.network.play.server.S04PacketEntityEquipment;
import net.minecraft.network.play.server.S05PacketSpawnPosition;
import net.minecraft.network.play.server.S06PacketUpdateHealth;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S09PacketHeldItemChange;
import net.minecraft.network.play.server.S0APacketUseBed;
import net.minecraft.network.play.server.S0BPacketAnimation;
import net.minecraft.network.play.server.S0CPacketSpawnPlayer;
import net.minecraft.network.play.server.S0DPacketCollectItem;
import net.minecraft.network.play.server.S0EPacketSpawnObject;
import net.minecraft.network.play.server.S0FPacketSpawnMob;
import net.minecraft.network.play.server.S10PacketSpawnPainting;
import net.minecraft.network.play.server.S11PacketSpawnExperienceOrb;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S13PacketDestroyEntities;
import net.minecraft.network.play.server.S14PacketEntity;
import net.minecraft.network.play.server.S18PacketEntityTeleport;
import net.minecraft.network.play.server.S19PacketEntityHeadLook;
import net.minecraft.network.play.server.S19PacketEntityStatus;
import net.minecraft.network.play.server.S1BPacketEntityAttach;
import net.minecraft.network.play.server.S1CPacketEntityMetadata;
import net.minecraft.network.play.server.S1DPacketEntityEffect;
import net.minecraft.network.play.server.S1EPacketRemoveEntityEffect;
import net.minecraft.network.play.server.S1FPacketSetExperience;
import net.minecraft.network.play.server.S20PacketEntityProperties;
import net.minecraft.network.play.server.S21PacketChunkData;
import net.minecraft.network.play.server.S22PacketMultiBlockChange;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.network.play.server.S24PacketBlockAction;
import net.minecraft.network.play.server.S25PacketBlockBreakAnim;
import net.minecraft.network.play.server.S26PacketMapChunkBulk;
import net.minecraft.network.play.server.S27PacketExplosion;
import net.minecraft.network.play.server.S28PacketEffect;
import net.minecraft.network.play.server.S29PacketSoundEffect;
import net.minecraft.network.play.server.S2APacketParticles;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.network.play.server.S2CPacketSpawnGlobalEntity;
import net.minecraft.network.play.server.S2DPacketOpenWindow;
import net.minecraft.network.play.server.S2EPacketCloseWindow;
import net.minecraft.network.play.server.S2FPacketSetSlot;
import net.minecraft.network.play.server.S30PacketWindowItems;
import net.minecraft.network.play.server.S31PacketWindowProperty;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;
import net.minecraft.network.play.server.S33PacketUpdateSign;
import net.minecraft.network.play.server.S34PacketMaps;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.network.play.server.S36PacketSignEditorOpen;
import net.minecraft.network.play.server.S37PacketStatistics;
import net.minecraft.network.play.server.S38PacketPlayerListItem;
import net.minecraft.network.play.server.S39PacketPlayerAbilities;
import net.minecraft.network.play.server.S3APacketTabComplete;
import net.minecraft.network.play.server.S3BPacketScoreboardObjective;
import net.minecraft.network.play.server.S3CPacketUpdateScore;
import net.minecraft.network.play.server.S3DPacketDisplayScoreboard;
import net.minecraft.network.play.server.S3EPacketTeams;
import net.minecraft.network.play.server.S3FPacketCustomPayload;
import net.minecraft.network.play.server.S40PacketDisconnect;
import net.minecraft.network.play.server.S41PacketServerDifficulty;
import net.minecraft.network.play.server.S42PacketCombatEvent;
import net.minecraft.network.play.server.S43PacketCamera;
import net.minecraft.network.play.server.S44PacketWorldBorder;
import net.minecraft.network.play.server.S45PacketTitle;
import net.minecraft.network.play.server.S46PacketSetCompressionLevel;
import net.minecraft.network.play.server.S47PacketPlayerListHeaderFooter;
import net.minecraft.network.play.server.S48PacketResourcePackSend;
import net.minecraft.network.play.server.S49PacketUpdateEntityNBT;

public abstract interface INetHandlerPlayClient
  extends INetHandler
{
  public abstract void handleSpawnObject(S0EPacketSpawnObject paramS0EPacketSpawnObject);
  
  public abstract void handleSpawnExperienceOrb(S11PacketSpawnExperienceOrb paramS11PacketSpawnExperienceOrb);
  
  public abstract void handleSpawnGlobalEntity(S2CPacketSpawnGlobalEntity paramS2CPacketSpawnGlobalEntity);
  
  public abstract void handleSpawnMob(S0FPacketSpawnMob paramS0FPacketSpawnMob);
  
  public abstract void handleScoreboardObjective(S3BPacketScoreboardObjective paramS3BPacketScoreboardObjective);
  
  public abstract void handleSpawnPainting(S10PacketSpawnPainting paramS10PacketSpawnPainting);
  
  public abstract void handleSpawnPlayer(S0CPacketSpawnPlayer paramS0CPacketSpawnPlayer);
  
  public abstract void handleAnimation(S0BPacketAnimation paramS0BPacketAnimation);
  
  public abstract void handleStatistics(S37PacketStatistics paramS37PacketStatistics);
  
  public abstract void handleBlockBreakAnim(S25PacketBlockBreakAnim paramS25PacketBlockBreakAnim);
  
  public abstract void handleSignEditorOpen(S36PacketSignEditorOpen paramS36PacketSignEditorOpen);
  
  public abstract void handleUpdateTileEntity(S35PacketUpdateTileEntity paramS35PacketUpdateTileEntity);
  
  public abstract void handleBlockAction(S24PacketBlockAction paramS24PacketBlockAction);
  
  public abstract void handleBlockChange(S23PacketBlockChange paramS23PacketBlockChange);
  
  public abstract void handleChat(S02PacketChat paramS02PacketChat);
  
  public abstract void handleTabComplete(S3APacketTabComplete paramS3APacketTabComplete);
  
  public abstract void handleMultiBlockChange(S22PacketMultiBlockChange paramS22PacketMultiBlockChange);
  
  public abstract void handleMaps(S34PacketMaps paramS34PacketMaps);
  
  public abstract void handleConfirmTransaction(S32PacketConfirmTransaction paramS32PacketConfirmTransaction);
  
  public abstract void handleCloseWindow(S2EPacketCloseWindow paramS2EPacketCloseWindow);
  
  public abstract void handleWindowItems(S30PacketWindowItems paramS30PacketWindowItems);
  
  public abstract void handleOpenWindow(S2DPacketOpenWindow paramS2DPacketOpenWindow);
  
  public abstract void handleWindowProperty(S31PacketWindowProperty paramS31PacketWindowProperty);
  
  public abstract void handleSetSlot(S2FPacketSetSlot paramS2FPacketSetSlot);
  
  public abstract void handleCustomPayload(S3FPacketCustomPayload paramS3FPacketCustomPayload);
  
  public abstract void handleDisconnect(S40PacketDisconnect paramS40PacketDisconnect);
  
  public abstract void handleUseBed(S0APacketUseBed paramS0APacketUseBed);
  
  public abstract void handleEntityStatus(S19PacketEntityStatus paramS19PacketEntityStatus);
  
  public abstract void handleEntityAttach(S1BPacketEntityAttach paramS1BPacketEntityAttach);
  
  public abstract void handleExplosion(S27PacketExplosion paramS27PacketExplosion);
  
  public abstract void handleChangeGameState(S2BPacketChangeGameState paramS2BPacketChangeGameState);
  
  public abstract void handleKeepAlive(S00PacketKeepAlive paramS00PacketKeepAlive);
  
  public abstract void handleChunkData(S21PacketChunkData paramS21PacketChunkData);
  
  public abstract void handleMapChunkBulk(S26PacketMapChunkBulk paramS26PacketMapChunkBulk);
  
  public abstract void handleEffect(S28PacketEffect paramS28PacketEffect);
  
  public abstract void handleJoinGame(S01PacketJoinGame paramS01PacketJoinGame);
  
  public abstract void handleEntityMovement(S14PacketEntity paramS14PacketEntity);
  
  public abstract void handlePlayerPosLook(S08PacketPlayerPosLook paramS08PacketPlayerPosLook);
  
  public abstract void handleParticles(S2APacketParticles paramS2APacketParticles);
  
  public abstract void handlePlayerAbilities(S39PacketPlayerAbilities paramS39PacketPlayerAbilities);
  
  public abstract void handlePlayerListItem(S38PacketPlayerListItem paramS38PacketPlayerListItem);
  
  public abstract void handleDestroyEntities(S13PacketDestroyEntities paramS13PacketDestroyEntities);
  
  public abstract void handleRemoveEntityEffect(S1EPacketRemoveEntityEffect paramS1EPacketRemoveEntityEffect);
  
  public abstract void handleRespawn(S07PacketRespawn paramS07PacketRespawn);
  
  public abstract void handleEntityHeadLook(S19PacketEntityHeadLook paramS19PacketEntityHeadLook);
  
  public abstract void handleHeldItemChange(S09PacketHeldItemChange paramS09PacketHeldItemChange);
  
  public abstract void handleDisplayScoreboard(S3DPacketDisplayScoreboard paramS3DPacketDisplayScoreboard);
  
  public abstract void handleEntityMetadata(S1CPacketEntityMetadata paramS1CPacketEntityMetadata);
  
  public abstract void handleEntityVelocity(S12PacketEntityVelocity paramS12PacketEntityVelocity);
  
  public abstract void handleEntityEquipment(S04PacketEntityEquipment paramS04PacketEntityEquipment);
  
  public abstract void handleSetExperience(S1FPacketSetExperience paramS1FPacketSetExperience);
  
  public abstract void handleUpdateHealth(S06PacketUpdateHealth paramS06PacketUpdateHealth);
  
  public abstract void handleTeams(S3EPacketTeams paramS3EPacketTeams);
  
  public abstract void handleUpdateScore(S3CPacketUpdateScore paramS3CPacketUpdateScore);
  
  public abstract void handleSpawnPosition(S05PacketSpawnPosition paramS05PacketSpawnPosition);
  
  public abstract void handleTimeUpdate(S03PacketTimeUpdate paramS03PacketTimeUpdate);
  
  public abstract void handleUpdateSign(S33PacketUpdateSign paramS33PacketUpdateSign);
  
  public abstract void handleSoundEffect(S29PacketSoundEffect paramS29PacketSoundEffect);
  
  public abstract void handleCollectItem(S0DPacketCollectItem paramS0DPacketCollectItem);
  
  public abstract void handleEntityTeleport(S18PacketEntityTeleport paramS18PacketEntityTeleport);
  
  public abstract void handleEntityProperties(S20PacketEntityProperties paramS20PacketEntityProperties);
  
  public abstract void handleEntityEffect(S1DPacketEntityEffect paramS1DPacketEntityEffect);
  
  public abstract void handleCombatEvent(S42PacketCombatEvent paramS42PacketCombatEvent);
  
  public abstract void handleServerDifficulty(S41PacketServerDifficulty paramS41PacketServerDifficulty);
  
  public abstract void handleCamera(S43PacketCamera paramS43PacketCamera);
  
  public abstract void handleWorldBorder(S44PacketWorldBorder paramS44PacketWorldBorder);
  
  public abstract void handleTitle(S45PacketTitle paramS45PacketTitle);
  
  public abstract void handleSetCompressionLevel(S46PacketSetCompressionLevel paramS46PacketSetCompressionLevel);
  
  public abstract void handlePlayerListHeaderFooter(S47PacketPlayerListHeaderFooter paramS47PacketPlayerListHeaderFooter);
  
  public abstract void handleResourcePack(S48PacketResourcePackSend paramS48PacketResourcePackSend);
  
  public abstract void handleEntityNBT(S49PacketUpdateEntityNBT paramS49PacketUpdateEntityNBT);
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\network\play\INetHandlerPlayClient.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */