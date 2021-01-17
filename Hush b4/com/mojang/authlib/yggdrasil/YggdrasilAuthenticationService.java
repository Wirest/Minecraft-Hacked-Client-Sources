// 
// Decompiled by Procyon v0.5.36
// 

package com.mojang.authlib.yggdrasil;

import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonParseException;
import java.io.IOException;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.exceptions.InvalidCredentialsException;
import com.mojang.authlib.exceptions.UserMigratedException;
import org.apache.commons.lang3.StringUtils;
import com.mojang.authlib.yggdrasil.response.Response;
import java.net.URL;
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.UserAuthentication;
import com.mojang.authlib.Agent;
import com.mojang.authlib.yggdrasil.response.ProfileSearchResultsResponse;
import com.mojang.util.UUIDTypeAdapter;
import java.util.UUID;
import com.mojang.authlib.properties.PropertyMap;
import java.lang.reflect.Type;
import com.mojang.authlib.GameProfile;
import com.google.gson.GsonBuilder;
import java.net.Proxy;
import com.google.gson.Gson;
import com.mojang.authlib.HttpAuthenticationService;

public class YggdrasilAuthenticationService extends HttpAuthenticationService
{
    private final String clientToken;
    private final Gson gson;
    
    public YggdrasilAuthenticationService(final Proxy proxy, final String clientToken) {
        super(proxy);
        this.clientToken = clientToken;
        final GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(GameProfile.class, new GameProfileSerializer());
        builder.registerTypeAdapter(PropertyMap.class, new PropertyMap.Serializer());
        builder.registerTypeAdapter(UUID.class, new UUIDTypeAdapter());
        builder.registerTypeAdapter(ProfileSearchResultsResponse.class, new ProfileSearchResultsResponse.Serializer());
        this.gson = builder.create();
    }
    
    @Override
    public UserAuthentication createUserAuthentication(final Agent agent) {
        return new YggdrasilUserAuthentication(this, agent);
    }
    
    @Override
    public MinecraftSessionService createMinecraftSessionService() {
        return new YggdrasilMinecraftSessionService(this);
    }
    
    @Override
    public GameProfileRepository createProfileRepository() {
        return new YggdrasilGameProfileRepository(this);
    }
    
    protected <T extends Response> T makeRequest(final URL url, final Object input, final Class<T> classOfT) throws AuthenticationException {
        try {
            final String jsonResult = (input == null) ? this.performGetRequest(url) : this.performPostRequest(url, this.gson.toJson(input), "application/json");
            final T result = this.gson.fromJson(jsonResult, classOfT);
            if (result == null) {
                return null;
            }
            if (!StringUtils.isNotBlank(result.getError())) {
                return result;
            }
            if ("UserMigratedException".equals(result.getCause())) {
                throw new UserMigratedException(result.getErrorMessage());
            }
            if (result.getError().equals("ForbiddenOperationException")) {
                throw new InvalidCredentialsException(result.getErrorMessage());
            }
            throw new AuthenticationException(result.getErrorMessage());
        }
        catch (IOException e) {
            throw new AuthenticationUnavailableException("Cannot contact authentication server", e);
        }
        catch (IllegalStateException e2) {
            throw new AuthenticationUnavailableException("Cannot contact authentication server", e2);
        }
        catch (JsonParseException e3) {
            throw new AuthenticationUnavailableException("Cannot contact authentication server", e3);
        }
    }
    
    public String getClientToken() {
        return this.clientToken;
    }
    
    private static class GameProfileSerializer implements JsonSerializer<GameProfile>, JsonDeserializer<GameProfile>
    {
        @Override
        public GameProfile deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
            final JsonObject object = (JsonObject)json;
            final UUID id = object.has("id") ? context.deserialize(object.get("id"), UUID.class) : null;
            final String name = object.has("name") ? object.getAsJsonPrimitive("name").getAsString() : null;
            return new GameProfile(id, name);
        }
        
        @Override
        public JsonElement serialize(final GameProfile src, final Type typeOfSrc, final JsonSerializationContext context) {
            final JsonObject result = new JsonObject();
            if (src.getId() != null) {
                result.add("id", context.serialize(src.getId()));
            }
            if (src.getName() != null) {
                result.addProperty("name", src.getName());
            }
            return result;
        }
    }
}
