package VenusClient.online.DiscordRPC;

import VenusClient.online.Client;
import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;
import net.arikia.dev.drpc.DiscordUser;
import net.arikia.dev.drpc.callbacks.ReadyCallback;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.security.NoSuchAlgorithmException;

public class DiscordRP {

    private boolean running = true;
    private long created = 0;

    public String user;
    public String tag;
    public String format;

    public void start(){

    this.created = System.currentTimeMillis();

        DiscordEventHandlers handlers = new DiscordEventHandlers.Builder().setReadyEventHandler(new ReadyCallback() {
            @Override
            public void apply(DiscordUser user) {
                System.out.println("Hello " + user.username + "#" + user.discriminator + ".");
                setUser(user.username);
                setTag(user.discriminator);
                setFormat(user.username + "#" + user.discriminator);
            }
        }).build();

        DiscordRPC.discordInitialize("739149747557761055", handlers,true);

        new Thread("Discord RPC Callback"){
            @Override
            public void run(){

                while (running){
                    DiscordRPC.discordRunCallbacks();
                }

            }
        }.start();

        }



    public void shutdown(){
        running = false;
        DiscordRPC.discordShutdown();
    }

    public void update(String firstline, String secondline) throws UnsupportedEncodingException, NoSuchAlgorithmException, MalformedURLException {
        DiscordRichPresence a = new DiscordRichPresence();
        DiscordRichPresence.Builder b = new DiscordRichPresence.Builder(secondline);
        b.setBigImage("newlogo", "Venus Client" + " " + Client.client_Version);
        b.setDetails(firstline);
        b.setStartTimestamps(created);

        DiscordRPC.discordUpdatePresence(b.build());
    }

    public String getUser() {
        return user;
    }

    public String getTag() {
        return tag;
    }

    public String getFormat() {
        return format;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setFormat(String format) {
        this.format = format;
    }

}
