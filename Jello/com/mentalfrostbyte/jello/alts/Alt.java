package com.mentalfrostbyte.jello.alts;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import com.mojang.authlib.GameProfile;

import net.minecraft.client.Minecraft;

public class Alt
{
    private String mask;
    public String email;
    public String password;
    
    public String username = "";
    public String uuid = "";
    
    public boolean loggedIn;
    public boolean failed;
    public boolean started;
    
    public float failedFade;
    public float succeedFade;
    public float loadFade;
    
    public float selectedAltTrans;
    public float slideTrans;
    
    public void callback(GameProfile g){
    	if(username.isEmpty() || uuid.isEmpty())
    		username = g.getName();
    		uuid = g.getId().toString();
    		started = false;
    		loggedIn = true;
    		failed = false;
    		succeedFade = 0;
    		loadFade = 15;
    		AltFile.save();
        	//System.out.println("succeeded");
    }
    
    public void start(){
    	started = true;
    	failed = false;
    	loggedIn = false;
    	succeedFade = 0;
    	loadFade = 0;
    	//System.out.println("started");
    }
    
    public void fail(){
    	failed = true;
    	started = false;
    	loggedIn = false;
    	failedFade = 60 + 15;
    	loadFade = 15;
    	succeedFade = 0;
    	//System.out.println("failed");
    }
    
    public Alt(final String username, final String password) {
        this.email = username;
        this.password = password;
    }
    
    public Alt(final String email, final String password, String username, String uuid) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.uuid = uuid;
    }
    
    public String getPassword() {
        return this.password;
    }
    
    public String getUsername() {
        return this.email;
    }
    
    public void setMask(final String mask) {
        this.mask = mask;
    }
}
