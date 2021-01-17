// 
// Decompiled by Procyon v0.5.36
// 

package com.mojang.realmsclient.client;

import org.apache.logging.log4j.LogManager;
import com.mojang.realmsclient.exception.RealmsHttpException;
import com.mojang.realmsclient.exception.RetryCallException;
import com.mojang.realmsclient.RealmsVersion;
import net.minecraft.realms.RealmsSharedConstants;
import java.net.URISyntaxException;
import java.net.URI;
import com.mojang.realmsclient.dto.PingResult;
import com.mojang.realmsclient.dto.UploadInfo;
import com.mojang.realmsclient.dto.RealmsState;
import com.mojang.realmsclient.dto.PendingInvitesList;
import com.mojang.realmsclient.dto.Subscription;
import com.mojang.realmsclient.dto.Ops;
import com.mojang.realmsclient.dto.WorldTemplateList;
import com.mojang.realmsclient.dto.RealmsOptions;
import java.io.UnsupportedEncodingException;
import com.mojang.realmsclient.dto.BackupList;
import com.mojang.realmsclient.dto.RealmsServerAddress;
import com.mojang.realmsclient.dto.ServerActivityList;
import com.mojang.realmsclient.dto.RealmsServer;
import java.io.IOException;
import com.mojang.realmsclient.exception.RealmsServiceException;
import com.mojang.realmsclient.dto.RealmsServerList;
import java.net.Proxy;
import net.minecraft.realms.Realms;
import com.google.gson.Gson;
import org.apache.logging.log4j.Logger;

public class RealmsClient
{
    private static final Logger LOGGER;
    private final String sessionId;
    private final String username;
    private static String baseUrl;
    private static final String WORLDS_RESOURCE_PATH = "worlds";
    private static final String INVITES_RESOURCE_PATH = "invites";
    private static final String MCO_RESOURCE_PATH = "mco";
    private static final String SUBSCRIPTION_RESOURCE = "subscriptions";
    private static final String ACTIVITIES_RESOURCE = "activities";
    private static final String OPS_RESOURCE = "ops";
    private static final String REGIONS_RESOURCE = "regions/ping/stat";
    private static final String TRIALS_RESOURCE = "trial";
    private static final String PATH_INITIALIZE = "/$WORLD_ID/initialize";
    private static final String PATH_GET_ACTIVTIES = "/$WORLD_ID";
    private static final String PATH_GET_SUBSCRIPTION = "/$WORLD_ID";
    private static final String PATH_GET_MINIGAMES = "/minigames";
    private static final String PATH_OP = "/$WORLD_ID";
    private static final String PATH_PUT_INTO_MINIGAMES_MODE = "/minigames/$MINIGAME_ID/$WORLD_ID";
    private static final String PATH_AVAILABLE = "/available";
    private static final String PATH_TEMPLATES = "/templates";
    private static final String PATH_WORLD_JOIN = "/$ID/join";
    private static final String PATH_WORLD_GET = "/$ID";
    private static final String PATH_WORLD_INVITES = "/$WORLD_ID/invite";
    private static final String PATH_WORLD_UNINVITE = "/$WORLD_ID/invite/$UUID";
    private static final String PATH_PENDING_INVITES_COUNT = "/count/pending";
    private static final String PATH_PENDING_INVITES = "/pending";
    private static final String PATH_ACCEPT_INVITE = "/accept/$INVITATION_ID";
    private static final String PATH_REJECT_INVITE = "/reject/$INVITATION_ID";
    private static final String PATH_UNINVITE_MYSELF = "/$WORLD_ID";
    private static final String PATH_WORLD_UPDATE = "/$WORLD_ID";
    private static final String PATH_SLOT_UPDATE = "/$WORLD_ID/slot";
    private static final String PATH_SLOT_SWITCH = "/$WORLD_ID/slot/$SLOT_ID";
    private static final String PATH_WORLD_OPEN = "/$WORLD_ID/open";
    private static final String PATH_WORLD_CLOSE = "/$WORLD_ID/close";
    private static final String PATH_WORLD_RESET = "/$WORLD_ID/reset";
    private static final String PATH_DELETE_WORLD = "/$WORLD_ID";
    private static final String PATH_WORLD_BACKUPS = "/$WORLD_ID/backups";
    private static final String PATH_WORLD_DOWNLOAD = "/$WORLD_ID/backups/download";
    private static final String PATH_WORLD_UPLOAD = "/$WORLD_ID/backups/upload";
    private static final String PATH_WORLD_UPLOAD_FINISHED = "/$WORLD_ID/backups/upload/finished";
    private static final String PATH_WORLD_UPLOAD_CANCELLED = "/$WORLD_ID/backups/upload/cancelled";
    private static final String PATH_CLIENT_COMPATIBLE = "/client/compatible";
    private static final String PATH_TOS_AGREED = "/tos/agreed";
    private static final String PATH_MCO_BUY = "/buy";
    private static final String PATH_STAGE_AVAILABLE = "/stageAvailable";
    private static Gson gson;
    
    public static RealmsClient createRealmsClient() {
        final String username = Realms.userName();
        final String sessionId = Realms.sessionId();
        if (username == null || sessionId == null) {
            return null;
        }
        return new RealmsClient(sessionId, username, Realms.getProxy());
    }
    
    public static void switchToStage() {
        RealmsClient.baseUrl = "mcoapi-stage.minecraft.net";
    }
    
    public static void switchToProd() {
        RealmsClient.baseUrl = "mcoapi.minecraft.net";
    }
    
    public RealmsClient(final String sessionId, final String username, final Proxy proxy) {
        this.sessionId = sessionId;
        this.username = username;
        RealmsClientConfig.setProxy(proxy);
    }
    
    public RealmsServerList listWorlds() throws RealmsServiceException, IOException {
        final String asciiUrl = this.url("worlds");
        final String json = this.execute(Request.get(asciiUrl));
        return RealmsServerList.parse(json);
    }
    
    public RealmsServer getOwnWorld(final long worldId) throws RealmsServiceException, IOException {
        final String asciiUrl = this.url("worlds" + "/$ID".replace("$ID", String.valueOf(worldId)));
        final String json = this.execute(Request.get(asciiUrl));
        return RealmsServer.parse(json);
    }
    
    public ServerActivityList getActivity(final long worldId) throws RealmsServiceException {
        final String asciiUrl = this.url("activities" + "/$WORLD_ID".replace("$WORLD_ID", String.valueOf(worldId)));
        final String json = this.execute(Request.get(asciiUrl));
        return ServerActivityList.parse(json);
    }
    
    public RealmsServerAddress join(final long worldId) throws RealmsServiceException, IOException {
        final String asciiUrl = this.url("worlds" + "/$ID/join".replace("$ID", "" + worldId));
        final String json = this.execute(Request.get(asciiUrl, 5000, 30000));
        return RealmsServerAddress.parse(json);
    }
    
    public void initializeWorld(final long worldId, final String name, final String motd) throws RealmsServiceException, IOException {
        final String queryString = QueryBuilder.of("name", name).with("motd", motd).toQueryString();
        final String asciiUrl = this.url("worlds" + "/$WORLD_ID/initialize".replace("$WORLD_ID", String.valueOf(worldId)), queryString);
        this.execute(Request.put(asciiUrl, "", 5000, 10000));
    }
    
    public Boolean mcoEnabled() throws RealmsServiceException, IOException {
        final String asciiUrl = this.url("mco/available");
        final String json = this.execute(Request.get(asciiUrl));
        return Boolean.valueOf(json);
    }
    
    public Boolean stageAvailable() throws RealmsServiceException, IOException {
        final String asciiUrl = this.url("mco/stageAvailable");
        final String json = this.execute(Request.get(asciiUrl));
        return Boolean.valueOf(json);
    }
    
    public CompatibleVersionResponse clientCompatible() throws RealmsServiceException, IOException {
        final String asciiUrl = this.url("mco/client/compatible");
        final String response = this.execute(Request.get(asciiUrl));
        CompatibleVersionResponse result;
        try {
            result = CompatibleVersionResponse.valueOf(response);
        }
        catch (IllegalArgumentException e) {
            throw new RealmsServiceException(500, "Could not check compatible version, got response: " + response, -1, "");
        }
        return result;
    }
    
    public void uninvite(final long worldId, final String profileUuid) throws RealmsServiceException {
        final String asciiUrl = this.url("invites" + "/$WORLD_ID/invite/$UUID".replace("$WORLD_ID", String.valueOf(worldId)).replace("$UUID", profileUuid));
        this.execute(Request.delete(asciiUrl));
    }
    
    public void uninviteMyselfFrom(final long worldId) throws RealmsServiceException {
        final String asciiUrl = this.url("invites" + "/$WORLD_ID".replace("$WORLD_ID", String.valueOf(worldId)));
        this.execute(Request.delete(asciiUrl));
    }
    
    public RealmsServer invite(final long worldId, final String profileName) throws RealmsServiceException, IOException {
        final String queryString = QueryBuilder.of("profileName", profileName).toQueryString();
        final String asciiUrl = this.url("invites" + "/$WORLD_ID/invite".replace("$WORLD_ID", String.valueOf(worldId)), queryString);
        final String json = this.execute(Request.put(asciiUrl, ""));
        return RealmsServer.parse(json);
    }
    
    public BackupList backupsFor(final long worldId) throws RealmsServiceException {
        final String asciiUrl = this.url("worlds" + "/$WORLD_ID/backups".replace("$WORLD_ID", String.valueOf(worldId)));
        final String json = this.execute(Request.get(asciiUrl));
        return BackupList.parse(json);
    }
    
    public void update(final long worldId, final String name, final String motd) throws RealmsServiceException, UnsupportedEncodingException {
        QueryBuilder qb = QueryBuilder.of("name", name);
        if (motd != null) {
            qb = qb.with("motd", motd);
        }
        final String queryString = qb.toQueryString();
        final String asciiUrl = this.url("worlds" + "/$WORLD_ID".replace("$WORLD_ID", String.valueOf(worldId)), queryString);
        this.execute(Request.put(asciiUrl, ""));
    }
    
    public void updateSlot(final long worldId, final RealmsOptions options) throws RealmsServiceException, UnsupportedEncodingException {
        final QueryBuilder qb = QueryBuilder.of("options", options.toJson());
        final String queryString = qb.toQueryString();
        final String asciiUrl = this.url("worlds" + "/$WORLD_ID/slot".replace("$WORLD_ID", String.valueOf(worldId)), queryString);
        this.execute(Request.put(asciiUrl, ""));
    }
    
    public boolean switchSlot(final long worldId, final int slot) throws RealmsServiceException {
        final String asciiUrl = this.url("worlds" + "/$WORLD_ID/slot/$SLOT_ID".replace("$WORLD_ID", String.valueOf(worldId)).replace("$SLOT_ID", String.valueOf(slot)));
        final String json = this.execute(Request.put(asciiUrl, ""));
        return Boolean.valueOf(json);
    }
    
    public void restoreWorld(final long worldId, final String backupId) throws RealmsServiceException {
        final String asciiUrl = this.url("worlds" + "/$WORLD_ID/backups".replace("$WORLD_ID", String.valueOf(worldId)), "backupId=" + backupId);
        this.execute(Request.put(asciiUrl, "", 40000, 40000));
    }
    
    public WorldTemplateList fetchWorldTemplates() throws RealmsServiceException {
        final String asciiUrl = this.url("worlds/templates");
        final String json = this.execute(Request.get(asciiUrl));
        return WorldTemplateList.parse(json);
    }
    
    public WorldTemplateList fetchMinigames() throws RealmsServiceException {
        final String asciiUrl = this.url("worlds/minigames");
        final String json = this.execute(Request.get(asciiUrl));
        return WorldTemplateList.parse(json);
    }
    
    public Boolean putIntoMinigameMode(final long worldId, final String minigameId) throws RealmsServiceException {
        final String path = "/minigames/$MINIGAME_ID/$WORLD_ID".replace("$MINIGAME_ID", minigameId).replace("$WORLD_ID", String.valueOf(worldId));
        final String asciiUrl = this.url("worlds" + path);
        return Boolean.valueOf(this.execute(Request.put(asciiUrl, "")));
    }
    
    public Ops op(final long worldId, final String profileName) throws RealmsServiceException {
        final String queryString = QueryBuilder.of("profileName", profileName).toQueryString();
        final String path = "/$WORLD_ID".replace("$WORLD_ID", String.valueOf(worldId));
        final String asciiUrl = this.url("ops" + path, queryString);
        return Ops.parse(this.execute(Request.post(asciiUrl, "")));
    }
    
    public Ops deop(final long worldId, final String profileName) throws RealmsServiceException {
        final String queryString = QueryBuilder.of("profileName", profileName).toQueryString();
        final String path = "/$WORLD_ID".replace("$WORLD_ID", String.valueOf(worldId));
        final String asciiUrl = this.url("ops" + path, queryString);
        return Ops.parse(this.execute(Request.delete(asciiUrl)));
    }
    
    public Boolean open(final long worldId) throws RealmsServiceException, IOException {
        final String asciiUrl = this.url("worlds" + "/$WORLD_ID/open".replace("$WORLD_ID", String.valueOf(worldId)));
        final String json = this.execute(Request.put(asciiUrl, ""));
        return Boolean.valueOf(json);
    }
    
    public Boolean close(final long worldId) throws RealmsServiceException, IOException {
        final String asciiUrl = this.url("worlds" + "/$WORLD_ID/close".replace("$WORLD_ID", String.valueOf(worldId)));
        final String json = this.execute(Request.put(asciiUrl, ""));
        return Boolean.valueOf(json);
    }
    
    public Boolean resetWorldWithSeed(final long worldId, final String seed, final Integer levelType, final boolean generateStructures) throws RealmsServiceException, IOException {
        QueryBuilder qb = QueryBuilder.empty();
        if (seed != null && seed.length() > 0) {
            qb = qb.with("seed", seed);
        }
        qb = qb.with("levelType", levelType).with("generateStructures", generateStructures);
        final String asciiUrl = this.url("worlds" + "/$WORLD_ID/reset".replace("$WORLD_ID", String.valueOf(worldId)), qb.toQueryString());
        final String json = this.execute(Request.put(asciiUrl, "", 30000, 80000));
        return Boolean.valueOf(json);
    }
    
    public Boolean resetWorldWithTemplate(final long worldId, final String worldTemplateId) throws RealmsServiceException, IOException {
        QueryBuilder qb = QueryBuilder.empty();
        if (worldTemplateId != null) {
            qb = qb.with("template", worldTemplateId);
        }
        final String asciiUrl = this.url("worlds" + "/$WORLD_ID/reset".replace("$WORLD_ID", String.valueOf(worldId)), qb.toQueryString());
        final String json = this.execute(Request.put(asciiUrl, "", 30000, 80000));
        return Boolean.valueOf(json);
    }
    
    public Subscription subscriptionFor(final long worldId) throws RealmsServiceException, IOException {
        final String asciiUrl = this.url("subscriptions" + "/$WORLD_ID".replace("$WORLD_ID", String.valueOf(worldId)));
        final String json = this.execute(Request.get(asciiUrl));
        return Subscription.parse(json);
    }
    
    public int pendingInvitesCount() throws RealmsServiceException {
        final String asciiUrl = this.url("invites/count/pending");
        final String json = this.execute(Request.get(asciiUrl));
        return Integer.parseInt(json);
    }
    
    public PendingInvitesList pendingInvites() throws RealmsServiceException {
        final String asciiUrl = this.url("invites/pending");
        final String json = this.execute(Request.get(asciiUrl));
        return PendingInvitesList.parse(json);
    }
    
    public void acceptInvitation(final String invitationId) throws RealmsServiceException {
        final String asciiUrl = this.url("invites" + "/accept/$INVITATION_ID".replace("$INVITATION_ID", invitationId));
        this.execute(Request.put(asciiUrl, ""));
    }
    
    public RealmsState fetchRealmsState() throws RealmsServiceException {
        final String asciiUrl = this.url("mco/buy");
        final String json = this.execute(Request.get(asciiUrl));
        return RealmsState.parse(json);
    }
    
    public String download(final long worldId) throws RealmsServiceException {
        final String asciiUrl = this.url("worlds" + "/$WORLD_ID/backups/download".replace("$WORLD_ID", String.valueOf(worldId)));
        return this.execute(Request.get(asciiUrl));
    }
    
    public UploadInfo upload(final long worldId, final String uploadToken) throws RealmsServiceException {
        final String asciiUrl = this.url("worlds" + "/$WORLD_ID/backups/upload".replace("$WORLD_ID", String.valueOf(worldId)));
        final UploadInfo oldUploadInfo = new UploadInfo();
        if (uploadToken != null) {
            oldUploadInfo.setToken(uploadToken);
        }
        final String content = RealmsClient.gson.toJson(oldUploadInfo);
        return UploadInfo.parse(this.execute(Request.put(asciiUrl, content)));
    }
    
    public void uploadCancelled(final long worldId, final String uploadToken) throws RealmsServiceException {
        final String asciiUrl = this.url("worlds" + "/$WORLD_ID/backups/upload/cancelled".replace("$WORLD_ID", String.valueOf(worldId)));
        final UploadInfo oldUploadInfo = new UploadInfo();
        oldUploadInfo.setToken(uploadToken);
        final String content = RealmsClient.gson.toJson(oldUploadInfo);
        this.execute(Request.put(asciiUrl, content));
    }
    
    public void uploadFinished(final long worldId) throws RealmsServiceException {
        final String asciiUrl = this.url("worlds" + "/$WORLD_ID/backups/upload/finished".replace("$WORLD_ID", String.valueOf(worldId)));
        this.execute(Request.put(asciiUrl, ""));
    }
    
    public void rejectInvitation(final String invitationId) throws RealmsServiceException {
        final String asciiUrl = this.url("invites" + "/reject/$INVITATION_ID".replace("$INVITATION_ID", invitationId));
        this.execute(Request.put(asciiUrl, ""));
    }
    
    public void agreeToTos() throws RealmsServiceException {
        final String asciiUrl = this.url("mco/tos/agreed");
        this.execute(Request.post(asciiUrl, ""));
    }
    
    public void sendPingResults(final PingResult pingResult) throws RealmsServiceException {
        final String asciiUrl = this.url("regions/ping/stat");
        this.execute(Request.post(asciiUrl, RealmsClient.gson.toJson(pingResult)));
    }
    
    public Boolean trialAvailable() throws RealmsServiceException, IOException {
        final String asciiUrl = this.url("trial");
        final String json = this.execute(Request.get(asciiUrl));
        return Boolean.valueOf(json);
    }
    
    public Boolean createTrial() throws RealmsServiceException, IOException {
        final String asciiUrl = this.url("trial");
        final String json = this.execute(Request.put(asciiUrl, ""));
        return Boolean.valueOf(json);
    }
    
    public void deleteWorld(final long worldId) throws RealmsServiceException, IOException {
        final String asciiUrl = this.url("worlds" + "/$WORLD_ID".replace("$WORLD_ID", String.valueOf(worldId)));
        this.execute(Request.delete(asciiUrl));
    }
    
    private String url(final String path) {
        return this.url(path, null);
    }
    
    private String url(final String path, final String queryString) {
        try {
            final URI uri = new URI("https", RealmsClient.baseUrl, "/" + path, queryString, null);
            return uri.toASCIIString();
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private String execute(final Request<?> r) throws RealmsServiceException {
        r.cookie("sid", this.sessionId);
        r.cookie("user", this.username);
        r.cookie("version", RealmsSharedConstants.VERSION_STRING);
        final String realmsVersion = RealmsVersion.getVersion();
        if (realmsVersion != null) {
            r.cookie("realms_version", realmsVersion);
        }
        try {
            final int responseCode = r.responseCode();
            if (responseCode == 503) {
                final int pauseTime = r.getRetryAfterHeader();
                throw new RetryCallException(pauseTime);
            }
            final String responseText = r.text();
            if (responseCode >= 200 && responseCode < 300) {
                return responseText;
            }
            if (responseCode == 401) {
                final String authenticationHeader = r.getHeader("WWW-Authenticate");
                RealmsClient.LOGGER.info("Could not authorize you against Realms server: " + authenticationHeader);
                throw new RealmsServiceException(responseCode, authenticationHeader, -1, authenticationHeader);
            }
            if (responseText == null || responseText.length() == 0) {
                RealmsClient.LOGGER.error("Realms error code: " + responseCode + " message: " + responseText);
                throw new RealmsServiceException(responseCode, responseText, responseCode, "");
            }
            final RealmsError error = new RealmsError(responseText);
            RealmsClient.LOGGER.error("Realms http code: " + responseCode + " -  error code: " + error.getErrorCode() + " -  message: " + error.getErrorMessage());
            throw new RealmsServiceException(responseCode, responseText, error);
        }
        catch (RealmsHttpException e) {
            throw new RealmsServiceException(500, "Could not connect to Realms: " + e.getMessage(), -1, "");
        }
    }
    
    static {
        LOGGER = LogManager.getLogger();
        RealmsClient.baseUrl = "mcoapi.minecraft.net";
        RealmsClient.gson = new Gson();
    }
    
    public enum CompatibleVersionResponse
    {
        COMPATIBLE, 
        OUTDATED, 
        OTHER;
    }
}
