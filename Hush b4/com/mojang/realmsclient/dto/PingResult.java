// 
// Decompiled by Procyon v0.5.36
// 

package com.mojang.realmsclient.dto;

import java.util.ArrayList;
import java.util.List;

public class PingResult
{
    public List<RegionPingResult> pingResults;
    public List<Long> worldIds;
    
    public PingResult() {
        this.pingResults = new ArrayList<RegionPingResult>();
        this.worldIds = new ArrayList<Long>();
    }
}
