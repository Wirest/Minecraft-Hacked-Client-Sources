package tv.twitch.broadcast;

public class IngestList {
    protected IngestServer[] servers = null;
    protected IngestServer defaultServer = null;

    public IngestList(IngestServer[] paramArrayOfIngestServer) {
        if (paramArrayOfIngestServer == null) {
            this.servers = new IngestServer[0];
        } else {
            this.servers = new IngestServer[paramArrayOfIngestServer.length];
            for (int i = 0; i < paramArrayOfIngestServer.length; i++) {
                this.servers[i] = paramArrayOfIngestServer[i];
                if (this.servers[i].defaultServer) {
                    this.defaultServer = this.servers[i];
                }
            }
            if ((this.defaultServer == null) && (this.servers.length > 0)) {
                this.defaultServer = this.servers[0];
            }
        }
    }

    public IngestServer[] getServers() {
        return this.servers;
    }

    public IngestServer getDefaultServer() {
        return this.defaultServer;
    }

    public IngestServer getBestServer() {
        if ((this.servers == null) || (this.servers.length == 0)) {
            return null;
        }
        IngestServer localIngestServer = this.servers[0];
        for (int i = 1; i < this.servers.length; i++) {
            if (localIngestServer.bitrateKbps < this.servers[i].bitrateKbps) {
                localIngestServer = this.servers[i];
            }
        }
        return localIngestServer;
    }
}




