// 
// Decompiled by Procyon v0.5.36
// 

package tv.twitch.broadcast;

public class IngestList
{
    protected IngestServer[] servers;
    protected IngestServer defaultServer;
    
    public IngestServer[] getServers() {
        return this.servers;
    }
    
    public IngestServer getDefaultServer() {
        return this.defaultServer;
    }
    
    public IngestList(final IngestServer[] array) {
        this.servers = null;
        this.defaultServer = null;
        if (array == null) {
            this.servers = new IngestServer[0];
        }
        else {
            this.servers = new IngestServer[array.length];
            for (int i = 0; i < array.length; ++i) {
                this.servers[i] = array[i];
                if (this.servers[i].defaultServer) {
                    this.defaultServer = this.servers[i];
                }
            }
            if (this.defaultServer == null && this.servers.length > 0) {
                this.defaultServer = this.servers[0];
            }
        }
    }
    
    public IngestServer getBestServer() {
        if (this.servers == null || this.servers.length == 0) {
            return null;
        }
        IngestServer ingestServer = this.servers[0];
        for (int i = 1; i < this.servers.length; ++i) {
            if (ingestServer.bitrateKbps < this.servers[i].bitrateKbps) {
                ingestServer = this.servers[i];
            }
        }
        return ingestServer;
    }
}
