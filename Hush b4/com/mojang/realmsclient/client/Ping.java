// 
// Decompiled by Procyon v0.5.36
// 

package com.mojang.realmsclient.client;

import java.net.SocketAddress;
import java.net.Socket;
import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.Comparator;
import java.util.ArrayList;
import com.mojang.realmsclient.dto.RegionPingResult;
import java.util.List;

public class Ping
{
    public static List<RegionPingResult> ping(final Region... regions) {
        for (final Region region : regions) {
            ping(region.endpoint);
        }
        final List<RegionPingResult> results = new ArrayList<RegionPingResult>();
        for (final Region region2 : regions) {
            results.add(new RegionPingResult(region2.name, ping(region2.endpoint)));
        }
        Collections.sort(results, new Comparator<RegionPingResult>() {
            @Override
            public int compare(final RegionPingResult o1, final RegionPingResult o2) {
                return o1.ping() - o2.ping();
            }
        });
        return results;
    }
    
    private static int ping(final String host) {
        final int timeout = 700;
        long sum = 0L;
        Socket socket = null;
        for (int i = 0; i < 5; ++i) {
            try {
                final SocketAddress sockAddr = new InetSocketAddress(host, 80);
                socket = new Socket();
                final long t1 = now();
                socket.connect(sockAddr, 700);
                sum += now() - t1;
            }
            catch (Exception e) {
                sum += 700L;
            }
            finally {
                close(socket);
            }
        }
        return (int)(sum / 5.0);
    }
    
    private static void close(final Socket socket) {
        try {
            if (socket != null) {
                socket.close();
            }
        }
        catch (Throwable t) {}
    }
    
    private static long now() {
        return System.currentTimeMillis();
    }
    
    public static List<RegionPingResult> pingAllRegions() {
        return ping(Region.values());
    }
    
    enum Region
    {
        US_EAST_1("us-east-1", "ec2.us-east-1.amazonaws.com"), 
        US_WEST_2("us-west-2", "ec2.us-west-2.amazonaws.com"), 
        US_WEST_1("us-west-1", "ec2.us-west-1.amazonaws.com"), 
        EU_WEST_1("eu-west-1", "ec2.eu-west-1.amazonaws.com"), 
        AP_SOUTHEAST_1("ap-southeast-1", "ec2.ap-southeast-1.amazonaws.com"), 
        AP_SOUTHEAST_2("ap-southeast-2", "ec2.ap-southeast-2.amazonaws.com"), 
        AP_NORTHEAST_1("ap-northeast-1", "ec2.ap-northeast-1.amazonaws.com"), 
        SA_EAST_1("sa-east-1", "ec2.sa-east-1.amazonaws.com");
        
        private final String name;
        private final String endpoint;
        
        private Region(final String name, final String endpoint) {
            this.name = name;
            this.endpoint = endpoint;
        }
    }
}
