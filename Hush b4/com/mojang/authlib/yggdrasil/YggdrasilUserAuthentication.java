// 
// Decompiled by Procyon v0.5.36
// 

package com.mojang.authlib.yggdrasil;

import org.apache.logging.log4j.LogManager;
import com.mojang.authlib.AuthenticationService;
import java.util.Arrays;
import java.util.Map;
import com.mojang.authlib.yggdrasil.response.Response;
import com.mojang.authlib.yggdrasil.request.ValidateRequest;
import com.mojang.authlib.yggdrasil.response.RefreshResponse;
import com.mojang.authlib.yggdrasil.request.RefreshRequest;
import com.mojang.authlib.properties.Property;
import com.google.common.collect.Multimap;
import com.mojang.authlib.yggdrasil.response.User;
import org.apache.commons.lang3.ArrayUtils;
import com.mojang.authlib.UserType;
import com.mojang.authlib.yggdrasil.response.AuthenticationResponse;
import com.mojang.authlib.yggdrasil.request.AuthenticationRequest;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.exceptions.InvalidCredentialsException;
import org.apache.commons.lang3.StringUtils;
import com.mojang.authlib.HttpAuthenticationService;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.Agent;
import java.net.URL;
import org.apache.logging.log4j.Logger;
import com.mojang.authlib.HttpUserAuthentication;

public class YggdrasilUserAuthentication extends HttpUserAuthentication
{
    private static final Logger LOGGER;
    private static final String BASE_URL = "https://authserver.mojang.com/";
    private static final URL ROUTE_AUTHENTICATE;
    private static final URL ROUTE_REFRESH;
    private static final URL ROUTE_VALIDATE;
    private static final URL ROUTE_INVALIDATE;
    private static final URL ROUTE_SIGNOUT;
    private static final String STORAGE_KEY_ACCESS_TOKEN = "accessToken";
    private final Agent agent;
    private GameProfile[] profiles;
    private String accessToken;
    private boolean isOnline;
    
    public YggdrasilUserAuthentication(final YggdrasilAuthenticationService authenticationService, final Agent agent) {
        super(authenticationService);
        this.agent = agent;
    }
    
    @Override
    public boolean canLogIn() {
        return !this.canPlayOnline() && StringUtils.isNotBlank(this.getUsername()) && (StringUtils.isNotBlank(this.getPassword()) || StringUtils.isNotBlank(this.getAuthenticatedToken()));
    }
    
    @Override
    public void logIn() throws AuthenticationException {
        if (StringUtils.isBlank(this.getUsername())) {
            throw new InvalidCredentialsException("Invalid username");
        }
        if (StringUtils.isNotBlank(this.getAuthenticatedToken())) {
            this.logInWithToken();
        }
        else {
            if (!StringUtils.isNotBlank(this.getPassword())) {
                throw new InvalidCredentialsException("Invalid password");
            }
            this.logInWithPassword();
        }
    }
    
    protected void logInWithPassword() throws AuthenticationException {
        if (StringUtils.isBlank(this.getUsername())) {
            throw new InvalidCredentialsException("Invalid username");
        }
        if (StringUtils.isBlank(this.getPassword())) {
            throw new InvalidCredentialsException("Invalid password");
        }
        YggdrasilUserAuthentication.LOGGER.info("Logging in with username & password");
        final AuthenticationRequest request = new AuthenticationRequest(this, this.getUsername(), this.getPassword());
        final AuthenticationResponse response = this.getAuthenticationService().makeRequest(YggdrasilUserAuthentication.ROUTE_AUTHENTICATE, request, AuthenticationResponse.class);
        if (!response.getClientToken().equals(this.getAuthenticationService().getClientToken())) {
            throw new AuthenticationException("Server requested we change our client token. Don't know how to handle this!");
        }
        if (response.getSelectedProfile() != null) {
            this.setUserType(response.getSelectedProfile().isLegacy() ? UserType.LEGACY : UserType.MOJANG);
        }
        else if (ArrayUtils.isNotEmpty(response.getAvailableProfiles())) {
            this.setUserType(response.getAvailableProfiles()[0].isLegacy() ? UserType.LEGACY : UserType.MOJANG);
        }
        final User user = response.getUser();
        if (user != null && user.getId() != null) {
            this.setUserid(user.getId());
        }
        else {
            this.setUserid(this.getUsername());
        }
        this.isOnline = true;
        this.accessToken = response.getAccessToken();
        this.profiles = response.getAvailableProfiles();
        this.setSelectedProfile(response.getSelectedProfile());
        this.getModifiableUserProperties().clear();
        this.updateUserProperties(user);
    }
    
    protected void updateUserProperties(final User user) {
        if (user == null) {
            return;
        }
        if (user.getProperties() != null) {
            this.getModifiableUserProperties().putAll(user.getProperties());
        }
    }
    
    protected void logInWithToken() throws AuthenticationException {
        if (StringUtils.isBlank(this.getUserID())) {
            if (!StringUtils.isBlank(this.getUsername())) {
                throw new InvalidCredentialsException("Invalid uuid & username");
            }
            this.setUserid(this.getUsername());
        }
        if (StringUtils.isBlank(this.getAuthenticatedToken())) {
            throw new InvalidCredentialsException("Invalid access token");
        }
        YggdrasilUserAuthentication.LOGGER.info("Logging in with access token");
        if (this.checkTokenValidity()) {
            YggdrasilUserAuthentication.LOGGER.debug("Skipping refresh call as we're safely logged in.");
            this.isOnline = true;
            return;
        }
        final RefreshRequest request = new RefreshRequest(this);
        final RefreshResponse response = this.getAuthenticationService().makeRequest(YggdrasilUserAuthentication.ROUTE_REFRESH, request, RefreshResponse.class);
        if (!response.getClientToken().equals(this.getAuthenticationService().getClientToken())) {
            throw new AuthenticationException("Server requested we change our client token. Don't know how to handle this!");
        }
        if (response.getSelectedProfile() != null) {
            this.setUserType(response.getSelectedProfile().isLegacy() ? UserType.LEGACY : UserType.MOJANG);
        }
        else if (ArrayUtils.isNotEmpty(response.getAvailableProfiles())) {
            this.setUserType(response.getAvailableProfiles()[0].isLegacy() ? UserType.LEGACY : UserType.MOJANG);
        }
        if (response.getUser() != null && response.getUser().getId() != null) {
            this.setUserid(response.getUser().getId());
        }
        else {
            this.setUserid(this.getUsername());
        }
        this.isOnline = true;
        this.accessToken = response.getAccessToken();
        this.profiles = response.getAvailableProfiles();
        this.setSelectedProfile(response.getSelectedProfile());
        this.getModifiableUserProperties().clear();
        this.updateUserProperties(response.getUser());
    }
    
    protected boolean checkTokenValidity() throws AuthenticationException {
        final ValidateRequest request = new ValidateRequest(this);
        try {
            this.getAuthenticationService().makeRequest(YggdrasilUserAuthentication.ROUTE_VALIDATE, request, Response.class);
            return true;
        }
        catch (AuthenticationException ex) {
            return false;
        }
    }
    
    @Override
    public void logOut() {
        super.logOut();
        this.accessToken = null;
        this.profiles = null;
        this.isOnline = false;
    }
    
    @Override
    public GameProfile[] getAvailableProfiles() {
        return this.profiles;
    }
    
    @Override
    public boolean isLoggedIn() {
        return StringUtils.isNotBlank(this.accessToken);
    }
    
    @Override
    public boolean canPlayOnline() {
        return this.isLoggedIn() && this.getSelectedProfile() != null && this.isOnline;
    }
    
    @Override
    public void selectGameProfile(final GameProfile profile) throws AuthenticationException {
        if (!this.isLoggedIn()) {
            throw new AuthenticationException("Cannot change game profile whilst not logged in");
        }
        if (this.getSelectedProfile() != null) {
            throw new AuthenticationException("Cannot change game profile. You must log out and back in.");
        }
        if (profile == null || !ArrayUtils.contains(this.profiles, profile)) {
            throw new IllegalArgumentException("Invalid profile '" + profile + "'");
        }
        final RefreshRequest request = new RefreshRequest(this, profile);
        final RefreshResponse response = this.getAuthenticationService().makeRequest(YggdrasilUserAuthentication.ROUTE_REFRESH, request, RefreshResponse.class);
        if (!response.getClientToken().equals(this.getAuthenticationService().getClientToken())) {
            throw new AuthenticationException("Server requested we change our client token. Don't know how to handle this!");
        }
        this.isOnline = true;
        this.accessToken = response.getAccessToken();
        this.setSelectedProfile(response.getSelectedProfile());
    }
    
    @Override
    public void loadFromStorage(final Map<String, Object> credentials) {
        super.loadFromStorage(credentials);
        this.accessToken = String.valueOf(credentials.get("accessToken"));
    }
    
    @Override
    public Map<String, Object> saveForStorage() {
        final Map<String, Object> result = super.saveForStorage();
        if (StringUtils.isNotBlank(this.getAuthenticatedToken())) {
            result.put("accessToken", this.getAuthenticatedToken());
        }
        return result;
    }
    
    @Deprecated
    public String getSessionToken() {
        if (this.isLoggedIn() && this.getSelectedProfile() != null && this.canPlayOnline()) {
            return String.format("token:%s:%s", this.getAuthenticatedToken(), this.getSelectedProfile().getId());
        }
        return null;
    }
    
    @Override
    public String getAuthenticatedToken() {
        return this.accessToken;
    }
    
    public Agent getAgent() {
        return this.agent;
    }
    
    @Override
    public String toString() {
        return "YggdrasilAuthenticationService{agent=" + this.agent + ", profiles=" + Arrays.toString(this.profiles) + ", selectedProfile=" + this.getSelectedProfile() + ", username='" + this.getUsername() + '\'' + ", isLoggedIn=" + this.isLoggedIn() + ", userType=" + this.getUserType() + ", canPlayOnline=" + this.canPlayOnline() + ", accessToken='" + this.accessToken + '\'' + ", clientToken='" + this.getAuthenticationService().getClientToken() + '\'' + '}';
    }
    
    @Override
    public YggdrasilAuthenticationService getAuthenticationService() {
        return (YggdrasilAuthenticationService)super.getAuthenticationService();
    }
    
    static {
        LOGGER = LogManager.getLogger();
        ROUTE_AUTHENTICATE = HttpAuthenticationService.constantURL("https://authserver.mojang.com/authenticate");
        ROUTE_REFRESH = HttpAuthenticationService.constantURL("https://authserver.mojang.com/refresh");
        ROUTE_VALIDATE = HttpAuthenticationService.constantURL("https://authserver.mojang.com/validate");
        ROUTE_INVALIDATE = HttpAuthenticationService.constantURL("https://authserver.mojang.com/invalidate");
        ROUTE_SIGNOUT = HttpAuthenticationService.constantURL("https://authserver.mojang.com/signout");
    }
}
