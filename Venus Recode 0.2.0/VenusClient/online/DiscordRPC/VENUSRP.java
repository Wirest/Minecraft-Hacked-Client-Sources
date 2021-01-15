package VenusClient.online.DiscordRPC;

public class VENUSRP {

    public static final VENUSRP Instance = new VENUSRP();

    public static final VENUSRP getInstance(){
        return Instance;
    }
    private DiscordRP discordRP = new DiscordRP();

    public void init(){
        discordRP.start();
    }

    public void shutdown(){
        discordRP.shutdown();
    }

    public DiscordRP getDiscordRP() {
        return discordRP;
    }
}
