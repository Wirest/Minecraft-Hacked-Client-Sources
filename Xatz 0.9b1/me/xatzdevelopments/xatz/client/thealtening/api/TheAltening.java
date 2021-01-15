package me.xatzdevelopments.xatz.client.thealtening.api;

import me.xatzdevelopments.xatz.client.thealtening.api.utils.*;
import java.util.logging.*;
import me.xatzdevelopments.xatz.client.thealtening.api.utils.exceptions.*;
import com.google.gson.*;
import java.io.*;
import me.xatzdevelopments.xatz.client.thealtening.api.data.*;
import java.util.concurrent.*;

public class TheAltening extends HttpUtils
{
    private final String apiKey;
    private final String endpoint = "http://api.thealtening.com/v1/";
    private final Logger logger;
    private final Gson gson;
    
    public TheAltening(final String apiKey) {
        this.logger = Logger.getLogger("TheAltening");
        this.gson = new Gson();
        this.apiKey = apiKey;
    }
    
    public LicenseData getLicenseData() throws TheAlteningException {
        try {
            final String connectionData = this.connect("http://api.thealtening.com/v1/license?token=" + this.apiKey);
            final JsonObject jsonObject = (JsonObject)this.gson.fromJson(connectionData, (Class)JsonObject.class);
            if (jsonObject == null) {
                throw new TheAlteningException("JSON", "Unable to parse JSON data, here's what is in there: \n" + connectionData);
            }
            if (jsonObject.has("error") && jsonObject.has("errorMessage")) {
                throw new TheAlteningException(jsonObject.get("error").getAsString(), jsonObject.get("errorMessage").getAsString());
            }
            return (LicenseData)this.gson.fromJson((JsonElement)jsonObject, (Class)LicenseData.class);
        }
        catch (IOException e) {
            throw new TheAlteningException("IOException", "Unable to establish a connection, here's why: \n" + e.getCause());
        }
    }
    
    public AccountData getAccountData() throws TheAlteningException {
        try {
            final String connectionData = this.connect("http://api.thealtening.com/v1/generate?info=true&token=" + this.apiKey);
            final JsonObject jsonObject = (JsonObject)this.gson.fromJson(connectionData, (Class)JsonObject.class);
            if (jsonObject == null) {
                throw new TheAlteningException("JSON", "Unable to parse JSON data, here's what is in there: \n" + connectionData);
            }
            if (jsonObject.has("error") && jsonObject.has("errorMessage")) {
                throw new TheAlteningException(jsonObject.get("error").getAsString(), jsonObject.get("errorMessage").getAsString());
            }
            return (AccountData)this.gson.fromJson((JsonElement)jsonObject, (Class)AccountData.class);
        }
        catch (IOException e) {
            throw new TheAlteningException("IOException", "Unable to establish a connection, here's why: \n" + e.getCause());
        }
    }
    
    public boolean isPrivate(final String token) throws TheAlteningException {
        try {
            final String connectionData = this.connect("http://api.thealtening.com/v1/private?acctoken=" + token + "&token=" + this.apiKey);
            final JsonObject jsonObject = (JsonObject)this.gson.fromJson(connectionData, (Class)JsonObject.class);
            if (jsonObject == null) {
                throw new TheAlteningException("JSON", "Unable to parse JSON data, here's what is in there: \n" + connectionData);
            }
            if (jsonObject.has("success")) {
                return jsonObject.get("success").getAsBoolean();
            }
            if (jsonObject.has("error") && jsonObject.has("errorMessage")) {
                throw new TheAlteningException(jsonObject.get("error").getAsString(), jsonObject.get("errorMessage").getAsString());
            }
            return false;
        }
        catch (IOException e) {
            throw new TheAlteningException("IOException", "Unable to establish a connection, here's why: \n" + e.getCause());
        }
    }
    
    public boolean isFavorite(final String token) throws TheAlteningException {
        try {
            final String connectionData = this.connect("http://api.thealtening.com/v1/favorite?acctoken=" + token + "&token=" + this.apiKey);
            final JsonObject jsonObject = (JsonObject)this.gson.fromJson(connectionData, (Class)JsonObject.class);
            if (jsonObject == null) {
                throw new TheAlteningException("JSON", "Unable to parse JSON data, here's what is in there: \n" + connectionData);
            }
            if (jsonObject.has("success")) {
                return jsonObject.get("success").getAsBoolean();
            }
            if (jsonObject.has("error") && jsonObject.has("errorMessage")) {
                throw new TheAlteningException(jsonObject.get("error").getAsString(), jsonObject.get("errorMessage").getAsString());
            }
            return false;
        }
        catch (IOException e) {
            throw new TheAlteningException("IOException", "Unable to establish a connection, here's why: \n" + e.getCause());
        }
    }
    
    public static class Asynchronous
    {
        private final TheAltening instance;
        
        public Asynchronous(final TheAltening instance) {
            this.instance = instance;
        }
        
        public CompletableFuture<LicenseData> getLicenseData() {
            final CompletableFuture<LicenseData> returnValue = new CompletableFuture<LicenseData>();
            try {
                returnValue.complete(this.instance.getLicenseData());
            }
            catch (TheAlteningException exception) {
                returnValue.completeExceptionally(exception);
            }
            return returnValue;
        }
        
        public CompletableFuture<AccountData> getAccountData() {
            final CompletableFuture<AccountData> returnValue = new CompletableFuture<AccountData>();
            try {
                returnValue.complete(this.instance.getAccountData());
            }
            catch (TheAlteningException exception) {
                returnValue.completeExceptionally(exception);
            }
            return returnValue;
        }
        
        public CompletableFuture<Boolean> isPrivate(final String token) {
            final CompletableFuture<Boolean> returnValue = new CompletableFuture<Boolean>();
            try {
                returnValue.complete(this.instance.isPrivate(token));
            }
            catch (TheAlteningException exception) {
                returnValue.completeExceptionally(exception);
            }
            return returnValue;
        }
        
        public CompletableFuture<Boolean> isFavorite(final String token) {
            final CompletableFuture<Boolean> returnValue = new CompletableFuture<Boolean>();
            try {
                returnValue.complete(this.instance.isFavorite(token));
            }
            catch (TheAlteningException exception) {
                returnValue.completeExceptionally(exception);
            }
            return returnValue;
        }
    }
}
