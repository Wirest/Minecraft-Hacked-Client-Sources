// 
// Decompiled by Procyon v0.5.36
// 

package com.mojang.realmsclient.gui;

import java.io.IOException;
import com.mojang.realmsclient.exception.RealmsServiceException;
import com.mojang.realmsclient.client.RealmsClient;
import org.apache.logging.log4j.LogManager;
import org.lwjgl.opengl.Display;
import java.util.Comparator;
import java.util.Collections;
import net.minecraft.realms.Realms;
import java.util.concurrent.TimeUnit;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.concurrent.Executors;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.List;
import com.mojang.realmsclient.dto.RealmsServer;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import org.apache.logging.log4j.Logger;

public class RealmsDataFetcher
{
    private static final Logger LOGGER;
    private final ScheduledExecutorService scheduler;
    private static final int SERVER_UPDATE_INTERVAL = 60;
    private static final int PENDING_INVITES_INTERVAL = 10;
    private static final int TRIAL_UPDATE_INTERVAL = 60;
    private volatile boolean stopped;
    private ServerListUpdateTask serverListUpdateTask;
    private PendingInviteUpdateTask pendingInviteUpdateTask;
    private TrialAvailabilityTask trialAvailabilityTask;
    private Set<RealmsServer> removedServers;
    private List<RealmsServer> servers;
    private int pendingInvitesCount;
    private boolean trialAvailable;
    private ScheduledFuture<?> serverListScheduledFuture;
    private ScheduledFuture<?> pendingInviteScheduledFuture;
    private ScheduledFuture<?> trialAvailableScheduledFuture;
    private Map<String, Boolean> fetchStatus;
    
    public RealmsDataFetcher() {
        this.scheduler = Executors.newScheduledThreadPool(3);
        this.stopped = true;
        this.serverListUpdateTask = new ServerListUpdateTask();
        this.pendingInviteUpdateTask = new PendingInviteUpdateTask();
        this.trialAvailabilityTask = new TrialAvailabilityTask();
        this.removedServers = (Set<RealmsServer>)Sets.newHashSet();
        this.servers = (List<RealmsServer>)Lists.newArrayList();
        this.trialAvailable = false;
        this.fetchStatus = new ConcurrentHashMap<String, Boolean>(Task.values().length);
        this.scheduleTasks();
    }
    
    public synchronized void init() {
        if (this.stopped) {
            this.stopped = false;
            this.cancelTasks();
            this.scheduleTasks();
        }
    }
    
    public synchronized boolean isFetchedSinceLastTry(final Task task) {
        final Boolean result = this.fetchStatus.get(task.toString());
        return result != null && result;
    }
    
    public synchronized void markClean() {
        for (final String task : this.fetchStatus.keySet()) {
            this.fetchStatus.put(task, false);
        }
    }
    
    public synchronized void forceUpdate() {
        this.stop();
        this.init();
    }
    
    public synchronized List<RealmsServer> getServers() {
        return (List<RealmsServer>)Lists.newArrayList((Iterable<?>)this.servers);
    }
    
    public int getPendingInvitesCount() {
        return this.pendingInvitesCount;
    }
    
    public boolean isTrialAvailable() {
        return this.trialAvailable;
    }
    
    public synchronized void stop() {
        this.stopped = true;
        this.cancelTasks();
    }
    
    private void scheduleTasks() {
        for (final Task task : Task.values()) {
            this.fetchStatus.put(task.toString(), false);
        }
        this.serverListScheduledFuture = this.scheduler.scheduleAtFixedRate(this.serverListUpdateTask, 0L, 60L, TimeUnit.SECONDS);
        this.pendingInviteScheduledFuture = this.scheduler.scheduleAtFixedRate(this.pendingInviteUpdateTask, 0L, 10L, TimeUnit.SECONDS);
        this.trialAvailableScheduledFuture = this.scheduler.scheduleAtFixedRate(this.trialAvailabilityTask, 0L, 60L, TimeUnit.SECONDS);
    }
    
    private void cancelTasks() {
        try {
            this.serverListScheduledFuture.cancel(false);
            this.pendingInviteScheduledFuture.cancel(false);
            this.trialAvailableScheduledFuture.cancel(false);
        }
        catch (Exception e) {
            RealmsDataFetcher.LOGGER.error("Failed to cancel Realms tasks");
        }
    }
    
    private synchronized void setServers(final List<RealmsServer> newServers) {
        int removedCnt = 0;
        for (final RealmsServer server : this.removedServers) {
            if (newServers.remove(server)) {
                ++removedCnt;
            }
        }
        if (removedCnt == 0) {
            this.removedServers.clear();
        }
        this.servers = newServers;
    }
    
    private synchronized void setTrialAvailabile(final boolean trialAvailabile) {
        this.trialAvailable = trialAvailabile;
    }
    
    public synchronized void removeItem(final RealmsServer server) {
        this.servers.remove(server);
        this.removedServers.add(server);
    }
    
    private void sort(final List<RealmsServer> servers) {
        Collections.sort(servers, new RealmsServer.McoServerComparator(Realms.getName()));
    }
    
    private boolean isActive() {
        return !this.stopped && Display.isActive();
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
    
    private class ServerListUpdateTask implements Runnable
    {
        @Override
        public void run() {
            if (RealmsDataFetcher.this.isActive()) {
                this.updateServersList();
            }
        }
        
        private void updateServersList() {
            try {
                final RealmsClient client = RealmsClient.createRealmsClient();
                if (client != null) {
                    final List<RealmsServer> servers = client.listWorlds().servers;
                    if (servers != null) {
                        RealmsDataFetcher.this.sort(servers);
                        RealmsDataFetcher.this.setServers(servers);
                        RealmsDataFetcher.this.fetchStatus.put(Task.SERVER_LIST.toString(), true);
                    }
                    else {
                        RealmsDataFetcher.LOGGER.warn("Realms server list was null or empty");
                    }
                }
            }
            catch (RealmsServiceException e) {
                RealmsDataFetcher.LOGGER.error("Couldn't get server list", e);
            }
            catch (IOException e2) {
                RealmsDataFetcher.LOGGER.error("Couldn't parse response from server getting list");
            }
        }
    }
    
    private class PendingInviteUpdateTask implements Runnable
    {
        @Override
        public void run() {
            if (RealmsDataFetcher.this.isActive()) {
                this.updatePendingInvites();
            }
        }
        
        private void updatePendingInvites() {
            try {
                final RealmsClient client = RealmsClient.createRealmsClient();
                if (client != null) {
                    RealmsDataFetcher.this.pendingInvitesCount = client.pendingInvitesCount();
                    RealmsDataFetcher.this.fetchStatus.put(Task.PENDING_INVITE.toString(), true);
                }
            }
            catch (RealmsServiceException e) {
                RealmsDataFetcher.LOGGER.error("Couldn't get pending invite count", e);
            }
        }
    }
    
    private class TrialAvailabilityTask implements Runnable
    {
        @Override
        public void run() {
            if (RealmsDataFetcher.this.isActive()) {
                this.getTrialAvailable();
            }
        }
        
        private void getTrialAvailable() {
            try {
                final RealmsClient client = RealmsClient.createRealmsClient();
                if (client != null) {
                    RealmsDataFetcher.this.trialAvailable = client.trialAvailable();
                    RealmsDataFetcher.this.fetchStatus.put(Task.TRIAL_AVAILABLE.toString(), true);
                }
            }
            catch (RealmsServiceException e) {
                RealmsDataFetcher.LOGGER.error("Couldn't get trial availability", e);
            }
            catch (IOException e2) {
                RealmsDataFetcher.LOGGER.error("Couldn't parse response from checking trial availability");
            }
        }
    }
    
    public enum Task
    {
        SERVER_LIST, 
        PENDING_INVITE, 
        TRIAL_AVAILABLE;
    }
}
