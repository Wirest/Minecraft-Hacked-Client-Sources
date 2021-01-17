// 
// Decompiled by Procyon v0.5.36
// 

package com.mojang.authlib.yggdrasil;

import com.google.common.collect.ForwardingMultimap;
import org.apache.logging.log4j.LogManager;
import com.mojang.authlib.AuthenticationService;
import java.net.URISyntaxException;
import java.net.URI;
import com.mojang.authlib.yggdrasil.response.MinecraftProfilePropertiesResponse;
import java.util.Iterator;
import com.google.gson.JsonParseException;
import com.mojang.authlib.yggdrasil.response.MinecraftTexturesPayload;
import org.apache.commons.codec.Charsets;
import org.apache.commons.codec.binary.Base64;
import com.mojang.authlib.minecraft.InsecureTextureException;
import com.google.common.collect.Iterables;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import java.util.Map;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import com.mojang.authlib.properties.Property;
import com.google.common.collect.Multimap;
import com.mojang.authlib.yggdrasil.response.HasJoinedMinecraftServerResponse;
import java.util.HashMap;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.response.Response;
import com.mojang.authlib.yggdrasil.request.JoinMinecraftServerRequest;
import java.security.spec.KeySpec;
import java.security.KeyFactory;
import java.security.spec.X509EncodedKeySpec;
import org.apache.commons.io.IOUtils;
import com.google.common.cache.CacheLoader;
import java.util.concurrent.TimeUnit;
import com.google.common.cache.CacheBuilder;
import java.lang.reflect.Type;
import com.mojang.util.UUIDTypeAdapter;
import java.util.UUID;
import com.google.gson.GsonBuilder;
import com.mojang.authlib.HttpAuthenticationService;
import com.mojang.authlib.GameProfile;
import com.google.common.cache.LoadingCache;
import com.google.gson.Gson;
import java.security.PublicKey;
import java.net.URL;
import org.apache.logging.log4j.Logger;
import com.mojang.authlib.minecraft.HttpMinecraftSessionService;

public class YggdrasilMinecraftSessionService extends HttpMinecraftSessionService
{
    private static final String[] WHITELISTED_DOMAINS;
    private static final Logger LOGGER;
    private static final String BASE_URL = "https://sessionserver.mojang.com/session/minecraft/";
    private static final URL JOIN_URL;
    private static final URL CHECK_URL;
    private final PublicKey publicKey;
    private final Gson gson;
    private final LoadingCache<GameProfile, GameProfile> insecureProfiles;
    
    protected YggdrasilMinecraftSessionService(final YggdrasilAuthenticationService authenticationService) {
        super(authenticationService);
        this.gson = new GsonBuilder().registerTypeAdapter(UUID.class, new UUIDTypeAdapter()).create();
        this.insecureProfiles = CacheBuilder.newBuilder().expireAfterWrite(6L, TimeUnit.HOURS).build((CacheLoader<? super GameProfile, GameProfile>)new CacheLoader<GameProfile, GameProfile>() {
            @Override
            public GameProfile load(final GameProfile key) throws Exception {
                return YggdrasilMinecraftSessionService.this.fillGameProfile(key, false);
            }
        });
        try {
            final X509EncodedKeySpec spec = new X509EncodedKeySpec(IOUtils.toByteArray(YggdrasilMinecraftSessionService.class.getResourceAsStream("/yggdrasil_session_pubkey.der")));
            final KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            this.publicKey = keyFactory.generatePublic(spec);
        }
        catch (Exception e) {
            throw new Error("Missing/invalid yggdrasil public key!");
        }
    }
    
    @Override
    public void joinServer(final GameProfile profile, final String authenticationToken, final String serverId) throws AuthenticationException {
        final JoinMinecraftServerRequest request = new JoinMinecraftServerRequest();
        request.accessToken = authenticationToken;
        request.selectedProfile = profile.getId();
        request.serverId = serverId;
        this.getAuthenticationService().makeRequest(YggdrasilMinecraftSessionService.JOIN_URL, request, Response.class);
    }
    
    @Override
    public GameProfile hasJoinedServer(final GameProfile user, final String serverId) throws AuthenticationUnavailableException {
        final Map<String, Object> arguments = new HashMap<String, Object>();
        arguments.put("username", user.getName());
        arguments.put("serverId", serverId);
        final URL url = HttpAuthenticationService.concatenateURL(YggdrasilMinecraftSessionService.CHECK_URL, HttpAuthenticationService.buildQuery(arguments));
        try {
            final HasJoinedMinecraftServerResponse response = this.getAuthenticationService().makeRequest(url, null, HasJoinedMinecraftServerResponse.class);
            if (response != null && response.getId() != null) {
                final GameProfile result = new GameProfile(response.getId(), user.getName());
                if (response.getProperties() != null) {
                    result.getProperties().putAll(response.getProperties());
                }
                return result;
            }
            return null;
        }
        catch (AuthenticationUnavailableException e) {
            throw e;
        }
        catch (AuthenticationException e2) {
            return null;
        }
    }
    
    @Override
    public Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> getTextures(final GameProfile profile, final boolean requireSecure) {
        final Property textureProperty = Iterables.getFirst((Iterable<? extends Property>)((ForwardingMultimap<String, Object>)profile.getProperties()).get("textures"), (Property)null);
        if (textureProperty == null) {
            return new HashMap<MinecraftProfileTexture.Type, MinecraftProfileTexture>();
        }
        if (requireSecure) {
            if (!textureProperty.hasSignature()) {
                YggdrasilMinecraftSessionService.LOGGER.error("Signature is missing from textures payload");
                throw new InsecureTextureException("Signature is missing from textures payload");
            }
            if (!textureProperty.isSignatureValid(this.publicKey)) {
                YggdrasilMinecraftSessionService.LOGGER.error("Textures payload has been tampered with (signature invalid)");
                throw new InsecureTextureException("Textures payload has been tampered with (signature invalid)");
            }
        }
        MinecraftTexturesPayload result;
        try {
            final String json = new String(Base64.decodeBase64(textureProperty.getValue()), Charsets.UTF_8);
            result = this.gson.fromJson(json, MinecraftTexturesPayload.class);
        }
        catch (JsonParseException e) {
            YggdrasilMinecraftSessionService.LOGGER.error("Could not decode textures payload", e);
            return new HashMap<MinecraftProfileTexture.Type, MinecraftProfileTexture>();
        }
        if (result.getTextures() == null) {
            return new HashMap<MinecraftProfileTexture.Type, MinecraftProfileTexture>();
        }
        for (final Map.Entry<MinecraftProfileTexture.Type, MinecraftProfileTexture> entry : result.getTextures().entrySet()) {
            if (!isWhitelistedDomain(entry.getValue().getUrl())) {
                YggdrasilMinecraftSessionService.LOGGER.error("Textures payload has been tampered with (non-whitelisted domain)");
                return new HashMap<MinecraftProfileTexture.Type, MinecraftProfileTexture>();
            }
        }
        return result.getTextures();
    }
    
    @Override
    public GameProfile fillProfileProperties(final GameProfile profile, final boolean requireSecure) {
        if (profile.getId() == null) {
            return profile;
        }
        if (!requireSecure) {
            return this.insecureProfiles.getUnchecked(profile);
        }
        return this.fillGameProfile(profile, true);
    }
    
    protected GameProfile fillGameProfile(final GameProfile profile, final boolean requireSecure) {
        try {
            URL url = HttpAuthenticationService.constantURL("https://sessionserver.mojang.com/session/minecraft/profile/" + UUIDTypeAdapter.fromUUID(profile.getId()));
            url = HttpAuthenticationService.concatenateURL(url, "unsigned=" + !requireSecure);
            final MinecraftProfilePropertiesResponse response = this.getAuthenticationService().makeRequest(url, null, MinecraftProfilePropertiesResponse.class);
            if (response == null) {
                YggdrasilMinecraftSessionService.LOGGER.debug("Couldn't fetch profile properties for " + profile + " as the profile does not exist");
                return profile;
            }
            final GameProfile result = new GameProfile(response.getId(), response.getName());
            result.getProperties().putAll(response.getProperties());
            profile.getProperties().putAll(response.getProperties());
            YggdrasilMinecraftSessionService.LOGGER.debug("Successfully fetched profile properties for " + profile);
            return result;
        }
        catch (AuthenticationException e) {
            YggdrasilMinecraftSessionService.LOGGER.warn("Couldn't look up profile properties for " + profile, e);
            return profile;
        }
    }
    
    @Override
    public YggdrasilAuthenticationService getAuthenticationService() {
        return (YggdrasilAuthenticationService)super.getAuthenticationService();
    }
    
    private static boolean isWhitelistedDomain(final String url) {
        URI uri = null;
        try {
            uri = new URI(url);
        }
        catch (URISyntaxException e) {
            throw new IllegalArgumentException("Invalid URL '" + url + "'");
        }
        final String domain = uri.getHost();
        for (int i = 0; i < YggdrasilMinecraftSessionService.WHITELISTED_DOMAINS.length; ++i) {
            if (domain.endsWith(YggdrasilMinecraftSessionService.WHITELISTED_DOMAINS[i])) {
                return true;
            }
        }
        return false;
    }
    
    static {
        WHITELISTED_DOMAINS = new String[] { ".minecraft.net", ".mojang.com" };
        LOGGER = LogManager.getLogger();
        JOIN_URL = HttpAuthenticationService.constantURL("https://sessionserver.mojang.com/session/minecraft/join");
        CHECK_URL = HttpAuthenticationService.constantURL("https://sessionserver.mojang.com/session/minecraft/hasJoined");
    }
}
