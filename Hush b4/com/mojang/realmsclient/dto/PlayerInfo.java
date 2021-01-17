// 
// Decompiled by Procyon v0.5.36
// 

package com.mojang.realmsclient.dto;

public class PlayerInfo
{
    private String name;
    private String uuid;
    private boolean operator;
    private boolean accepted;
    
    public PlayerInfo() {
        this.operator = false;
        this.accepted = false;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public String getUuid() {
        return this.uuid;
    }
    
    public void setUuid(final String uuid) {
        this.uuid = uuid;
    }
    
    public boolean isOperator() {
        return this.operator;
    }
    
    public void setOperator(final boolean operator) {
        this.operator = operator;
    }
    
    public boolean getAccepted() {
        return this.accepted;
    }
    
    public void setAccepted(final boolean accepted) {
        this.accepted = accepted;
    }
}
