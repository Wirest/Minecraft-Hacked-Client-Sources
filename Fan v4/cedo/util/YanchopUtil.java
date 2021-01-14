//yanchop's search engine integration code v2

package cedo.util;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C01PacketChatMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class YanchopUtil {

    /*
     * Previous integration code was pretty bad, cleaned it up, added some stuff, improved in general
     * Delete all previous search engine code when replacing it with this (also delete septhread)
     *
     * Some ideas for client devs:
     * yanchop killsults
     * autoYanchop module (yanchops random ppl on server automatically)
     * allow other people on the server to yanchop, by having them say the command in chat and you yanchopping for them
     *
     */

    //keep these public incase there is external stuff that uses the search engine, like killsults
    public ArrayList<String> mojangQueue = new ArrayList<String>();
    public ArrayList<String> searchQueue = new ArrayList<String>();

    boolean threadRunning = false;
    boolean postMode = false;
    boolean bypassFilter = true;
    String key = "0Qrc6lJcGK1W";

    /*
     * Syntax: cmd term		OR		cmd type term
     *
     * "-yanchop notch" should pass in a single argument, "notch"
     * "-yanchop ign notch" should pass in two arguments, "ign" and "notch"
     *
     * if you do not pass in arguments properly, somehow... the code wont work!
     */

    /****************************************************************/
    public static void sendMsg(String msg) {
        for (int n = 0; n < msg.length(); n++) {
            if (msg.charAt(n) != ' ') {
                String pre = msg.substring(0, n);
                String end = msg.substring(n);
                msg = pre + "§7" + end;
                n += 2;
            }
        }
        //Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§7[§6" + Fan.fullname + "§7]§7 " + msg));
        Logger.ingameInfo(msg);
    }


    //configure the code below to best suit your client, or just delete it

    public void yanchop(String[] args) {//your command should call this function
        if (!threadRunning) {
            startThread();
            //attempt to load search key from file
        }

        if (args[0].startsWith("key=")) {
            key = args[0].substring(4);
            sendMsg("key updated");
            //fileutils save key in file, you do this part since every client has its own file utils
            return;
        }

        if (key.length() == 0) {
            //attempt to load key from file, if it cant, msg line below
            sendMsg("Error: key missing, please do key=(key)");
            return;
        }

        //do not edit this
        if (args[0].equals("post=false")) {
            postMode = false;
            sendMsg("Post mode set to false");
            return;
        }
        if (args[0].equals("post=true")) {
            postMode = true;
            sendMsg("Post mode set to true, you will now post the search results in public chat");
            return;
        }
        if (args[0].equals("bypass=true")) {
            bypassFilter = true;
            sendMsg("Chat bypass mode set to true");
            return;
        }
        if (args[0].equals("bypass=false")) {
            bypassFilter = false;
            sendMsg("Chat bypass mode set to false");
            return;
        }
        if (args[0].startsWith("!check") || args[0].startsWith("!status")) {
            if (key.length() > 8) checkStatus();
            else sendMsg("Please activate a key by doing key=(key)");
            return;
        }


        //process search input
        if (args.length == 1) {
            mojangQueue.add(args[0]);
            return;
        }
        if (args.length == 2) {
            searchQueue.add(args[0] + ":" + args[1]);
            return;
        }
        sendMsg("Error: bad arguments");
    }

    public String getCmd() {
        return "yanchop";//make this whatever command you want, like search, scan, dox, lookup, ect...
    }

    /****************************************************************/


    //The code below should NOT be edited

    //processes searches
    void startThread() {//do not edit this function
        threadRunning = true;
        new Thread(() -> {
            while (true) {

                if (mojangQueue.size() != 0) {
                    String req = mojangQueue.get(0);
                    mojangQueue.remove(0);

                    String uuid = IGNToUUID(req);
                    if (uuid.equals("ERROR_FAILED_IGN_TO_UUID")) {
                        sendMsg("Error: failed ign to uuid");
                        continue;
                    }

                    ArrayList<String> pastIGNs = UUIDToPreviousIGNs(uuid);
                    if (pastIGNs == null) {
                        sendMsg("Error: failed uuid to ign list");
                        continue;
                    }

                    String request = "player:" + uuid;
                    for (String ign : pastIGNs) {
                        if ((request.length() + ign.length()) < 7900) {
                            request += "," + ign;
                        } else {
                            sendMsg("Error: request too large, skipping: " + ign);
                        }
                    }
                    execRequest(request, req);
                }

                if (searchQueue.size() != 0) {
                    execRequest(searchQueue.get(0), searchQueue.get(0));
                    searchQueue.remove(0);
                }

                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    //sends request to search engine
    void execRequest(String result, String originalRequest) {//do not edit this function
        String link = "http://elgoog.ga/cgi-bin/se.fcgi?" + key + ":" + result;
        try {

            URL url = new URL(link);
            URLConnection connection = url.openConnection();
            connection.setRequestProperty("http.user_agent", "Bot");
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));

            // TODO: fix postMode and DM update to Yanchop

            String line;
            if (!postMode) {

                sendMsg("Search results for: " + originalRequest);
                while ((line = reader.readLine()) != null) {
                    sendMsg(line);
                }
                sendMsg("");
            } else {
                String reply = originalRequest;
                while ((line = reader.readLine()) != null) {
                    if (!line.contains("Finished in")) {
                        reply += line + " ";

                        if (bypassFilter) {//edit this if you want, i think its sufficient
                            reply = reply.replaceAll(".", "-");
                            reply = reply.replaceAll("@", "AT");
                            reply.replace("ips[", "");
                            reply.replace("emails[", "");
                            reply.replace("forumNames[", "");
                            reply.replace("buycraftLogs[", "");
                        }
                    }
                }
                if (!reply.equals(originalRequest))
                    Minecraft.getMinecraft().getNetHandler().addToSendQueue(new C01PacketChatMessage(reply));
                else
                    sendMsg("Nothing found on " + originalRequest);
            }

            reader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    String IGNToUUID(String playername) {//do not edit this function
        String link = "https://api.mojang.com/users/profiles/minecraft/" + playername;
        try {
            URL url = new URL(link);
            URLConnection connection = url.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.7; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String page = "";
            String line;
            while ((line = reader.readLine()) != null) {
                page = line;
            }
            reader.close();
            if (page.contains("name")) {
                int length = playername.length();
                return page.substring(17 + length, page.length() - 2);
            } else {
                return "ERROR_FAILED_IGN_TO_UUID";
            }

        } catch (IOException e) {
            e.printStackTrace();
            return "ERROR_FAILED_IGN_TO_UUID";
        }
    }


    ArrayList<String> UUIDToPreviousIGNs(String uuid) {//do not edit this function
        String page = "";
        String link = "https://api.mojang.com/user/profiles/" + uuid + "/names";
        ArrayList<String> names = new ArrayList<String>();

        try {
            URL url = new URL(link);
            URLConnection connection = url.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.7; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                page = line.substring(1, line.length() - 1);
            }
            reader.close();
            String[] temp = page.split(",");
            temp[0] = temp[0].replace("}", "");

            for (String n : temp) {
                if (n.contains("name")) {
                    if (!names.contains(n.substring(9, n.length() - 1))) {
                        names.add(n.substring(9, n.length() - 1));
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return names;
    }

    void checkStatus() {//do not edit this function
        String link = "http://elgoog.ga/cgi-bin/se.fcgi?" + key + ":" + "status";
        try {

            URL url = new URL(link);
            URLConnection connection = url.openConnection();
            connection.setRequestProperty("http.user_agent", "Bot");
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));

            String line = "";
            while ((line = reader.readLine()) != null) {
                if (!line.startsWith("#")) {
                    System.out.println(line);
                    String[] parts = line.split("\t");
                    String[] vars = new String[]{"Initial days: ", "Requests left: ", "Expires in: "};
                    for (int i = 1; i < parts.length - 1; i++) {
                        if (i == 3) {
                            //yeah it looks bad and yeah i dont care
                            long currentTime = System.currentTimeMillis() / 1000;
                            long endTime = Long.valueOf(parts[i]);
                            long diff = endTime - currentTime;
                            int minutes = (int) (diff / 60);
                            int hours = minutes / 60;
                            int days = hours / 24;
                            minutes = minutes % 60;
                            hours = hours % 60;
                            sendMsg(vars[i - 1] + " " + days + " days, " + hours + " hours, " + minutes + " minutes");
                            continue;
                        }
                        sendMsg(vars[i - 1] + " " + parts[i]);
                    }
                }
            }
            reader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
