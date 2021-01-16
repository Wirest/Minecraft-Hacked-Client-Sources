/*
 * Decompiled with CFR 0_122.
 */
package me.razerboy420.weepcraft.irc.pircBot;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;
import me.razerboy420.weepcraft.irc.IrcLine;
import me.razerboy420.weepcraft.irc.pircBot.DccChat;
import me.razerboy420.weepcraft.irc.pircBot.DccFileTransfer;
import me.razerboy420.weepcraft.irc.pircBot.DccManager;
import me.razerboy420.weepcraft.irc.pircBot.IdentServer;
import me.razerboy420.weepcraft.irc.pircBot.InputThread;
import me.razerboy420.weepcraft.irc.pircBot.IrcException;
import me.razerboy420.weepcraft.irc.pircBot.NickAlreadyInUseException;
import me.razerboy420.weepcraft.irc.pircBot.OutputThread;
import me.razerboy420.weepcraft.irc.pircBot.Queue;
import me.razerboy420.weepcraft.irc.pircBot.ReplyConstants;
import me.razerboy420.weepcraft.irc.pircBot.User;
import net.minecraft.server.MinecraftServer;

public abstract class PircBot
implements ReplyConstants {
    public static final String VERSION = "1.5.0";
    private static final int OP_ADD = 1;
    private static final int OP_REMOVE = 2;
    private static final int VOICE_ADD = 3;
    private static final int VOICE_REMOVE = 4;
    private InputThread _inputThread = null;
    private OutputThread _outputThread = null;
    private String _charset = null;
    private InetAddress _inetAddress = null;
    private String _server = null;
    private int _port = -1;
    private String _password = null;
    private Queue _outQueue = new Queue();
    private long _messageDelay = 650;
    private Hashtable _channels = new Hashtable();
    private Hashtable _topics = new Hashtable();
    private DccManager _dccManager;
    private int[] _dccPorts;
    private InetAddress _dccInetAddress;
    private boolean _autoNickChange;
    private boolean _verbose;
    private String _name;
    private String _nick;
    private String _login;
    private String _version;
    private String _finger;
    private String _channelPrefixes;
    private ArrayList<IrcLine> lines;
    private int chatLine;
    private boolean messagesAwaiting;

    public PircBot() {
        this._dccManager = new DccManager(this);
        this._dccPorts = null;
        this._dccInetAddress = null;
        this._autoNickChange = false;
        this._verbose = false;
        this._name = "PircBot";
        this._nick = "PircBot";
        this._login = "PircBot";
        this._version = "PircBot 1.5.0 Java IRC Bot - www.jibble.org";
        this._finger = "You ought to be arrested for fingering a bot!";
        this._channelPrefixes = "#&+!";
        this.lines = new ArrayList();
        this.chatLine = 0;
    }

    public final synchronized void connect(String hostname) throws IOException, IrcException, NickAlreadyInUseException {
        this.connect(hostname, 6667, null);
    }

    public final synchronized void connect(String hostname, int port) throws IOException, IrcException, NickAlreadyInUseException {
        this.connect(hostname, port, null);
    }

    public final synchronized void connect(String hostname, int port, String password) throws IOException, IrcException, NickAlreadyInUseException {
        this._server = hostname;
        this._port = port;
        this._password = password;
        if (this.isConnected()) {
            throw new IOException("The PircBot is already connected to an IRC server.  Disconnect first.");
        }
        this.removeAllChannels();
        Socket socket = new Socket(hostname, port);
        this.log("*** Connected to server.");
        this._inetAddress = socket.getLocalAddress();
        InputStreamReader inputStreamReader = null;
        OutputStreamWriter outputStreamWriter = null;
        if (this.getEncoding() != null) {
            inputStreamReader = new InputStreamReader(socket.getInputStream(), this.getEncoding());
            outputStreamWriter = new OutputStreamWriter(socket.getOutputStream(), this.getEncoding());
        } else {
            inputStreamReader = new InputStreamReader(socket.getInputStream());
            outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());
        }
        BufferedReader breader = new BufferedReader(inputStreamReader);
        BufferedWriter bwriter = new BufferedWriter(outputStreamWriter);
        if (password != null && !password.equals("")) {
            OutputThread.sendRawLine(this, bwriter, "PASS " + password);
        }
        String nick = this.getName();
        OutputThread.sendRawLine(this, bwriter, "NICK " + nick);
        OutputThread.sendRawLine(this, bwriter, "USER " + this.getLogin() + " 8 * :" + this.getVersion());
        this._inputThread = new InputThread(this, socket, breader, bwriter);
        String line = null;
        int tries = 1;
        while ((line = breader.readLine()) != null) {
            this.handleLine(line);
            int firstSpace = line.indexOf(" ");
            int secondSpace = line.indexOf(" ", firstSpace + 1);
            if (secondSpace >= 0) {
                String code = line.substring(firstSpace + 1, secondSpace);
                if (code.equals("004")) break;
                if (code.equals("433")) {
                    if (!this._autoNickChange) {
                        socket.close();
                        this._inputThread = null;
                        throw new NickAlreadyInUseException(line);
                    }
                    nick = String.valueOf(String.valueOf(this.getName())) + ++tries;
                    OutputThread.sendRawLine(this, bwriter, "NICK " + nick);
                } else if (!code.equals("439") && (code.startsWith("5") || code.startsWith("4"))) {
                    socket.close();
                    this._inputThread = null;
                    throw new IrcException("Could not log into the IRC server: " + line);
                }
            }
            this.setNick(nick);
        }
        this.log("*** Logged onto server.");
        socket.setSoTimeout(300000);
        this._inputThread.start();
        if (this._outputThread == null) {
            this._outputThread = new OutputThread(this, this._outQueue);
            this._outputThread.start();
        }
        this.onConnect();
    }

    public final synchronized void reconnect() throws IOException, IrcException, NickAlreadyInUseException {
        if (this.getServer() == null) {
            throw new IrcException("Cannot reconnect to an IRC server because we were never connected to one previously!");
        }
        this.connect(this.getServer(), this.getPort(), this.getPassword());
    }

    public final synchronized void disconnect() {
        this.quitServer();
    }

    public void setAutoNickChange(boolean autoNickChange) {
        this._autoNickChange = autoNickChange;
    }

    public final void startIdentServer() {
        new me.razerboy420.weepcraft.irc.pircBot.IdentServer(this, this.getLogin());
    }

    public final void joinChannel(String channel) {
        this.sendRawLine("JOIN " + channel);
    }

    public final void joinChannel(String channel, String key) {
        this.joinChannel(String.valueOf(String.valueOf(channel)) + " " + key);
    }

    public final void partChannel(String channel) {
        this.sendRawLine("PART " + channel);
    }

    public final void partChannel(String channel, String reason) {
        this.sendRawLine("PART " + channel + " :" + reason);
    }

    public final void quitServer() {
        this.quitServer("");
    }

    public final void quitServer(String reason) {
        this.sendRawLine("QUIT :" + reason);
    }

    public final synchronized void sendRawLine(String line) {
        if (this.isConnected()) {
            this._inputThread.sendRawLine(line);
        }
    }

    public final synchronized void sendRawLineViaQueue(String line) {
        if (line == null) {
            throw new NullPointerException("Cannot send null messages to server");
        }
        if (this.isConnected()) {
            this._outQueue.add(line);
        }
    }

    public final void sendMessage(String target, String message) {
        this._outQueue.add("PRIVMSG " + target + " :" + message);
    }

    public final void sendAction(String target, String action) {
        this.sendCTCPCommand(target, "ACTION " + action);
    }

    public final void sendNotice(String target, String notice) {
        this._outQueue.add("NOTICE " + target + " :" + notice);
    }

    public final void sendCTCPCommand(String target, String command) {
        this._outQueue.add("PRIVMSG " + target + " :\u0001" + command + "\u0001");
    }

    public final void changeNick(String newNick) {
        this.sendRawLine("NICK " + newNick);
    }

    public final void identify(String password) {
        this.sendRawLine("NICKSERV IDENTIFY " + password);
    }

    public final void setMode(String channel, String mode) {
        this.sendRawLine("MODE " + channel + " " + mode);
    }

    public final void sendInvite(String nick, String channel) {
        this.sendRawLine("INVITE " + nick + " :" + channel);
    }

    public final void ban(String channel, String hostmask) {
        this.sendRawLine("MODE " + channel + " +b " + hostmask);
    }

    public final void unBan(String channel, String hostmask) {
        this.sendRawLine("MODE " + channel + " -b " + hostmask);
    }

    public final void op(String channel, String nick) {
        this.setMode(channel, "+o " + nick);
    }

    public final void deOp(String channel, String nick) {
        this.setMode(channel, "-o " + nick);
    }

    public final void voice(String channel, String nick) {
        this.setMode(channel, "+v " + nick);
    }

    public final void deVoice(String channel, String nick) {
        this.setMode(channel, "-v " + nick);
    }

    public final void setTopic(String channel, String topic) {
        this.sendRawLine("TOPIC " + channel + " :" + topic);
    }

    public final void kick(String channel, String nick) {
        this.kick(channel, nick, "");
    }

    public final void kick(String channel, String nick, String reason) {
        this.sendRawLine("KICK " + channel + " " + nick + " :" + reason);
    }

    public final void listChannels() {
        this.listChannels(null);
    }

    public final void listChannels(String parameters) {
        if (parameters == null) {
            this.sendRawLine("LIST");
        } else {
            this.sendRawLine("LIST " + parameters);
        }
    }

    public final DccFileTransfer dccSendFile(File file, String nick, int timeout) {
        DccFileTransfer transfer = new DccFileTransfer(this, this._dccManager, file, nick, timeout);
        transfer.doSend(true);
        return transfer;
    }

    protected final void dccReceiveFile(File file, long address, int port, int size) {
        throw new RuntimeException("dccReceiveFile is deprecated, please use sendFile");
    }

    public final DccChat dccSendChatRequest(String nick, int timeout) {
        DccChat chat = null;
        try {
            ServerSocket ss = null;
            int[] ports = this.getDccPorts();
            if (ports == null) {
                ss = new ServerSocket(0);
            } else {
                int i = 0;
                while (i < ports.length) {
                    try {
                        ss = new ServerSocket(ports[i]);
                        break;
                    }
                    catch (Exception var7_8) {
                        ++i;
                    }
                }
                if (ss == null) {
                    throw new IOException("All ports returned by getDccPorts() are in use.");
                }
            }
            ss.setSoTimeout(timeout);
            int port = ss.getLocalPort();
            InetAddress inetAddress = this.getDccInetAddress();
            if (inetAddress == null) {
                inetAddress = this.getInetAddress();
            }
            byte[] ip = inetAddress.getAddress();
            long ipNum = this.ipToLong(ip);
            this.sendCTCPCommand(nick, "DCC CHAT chat " + ipNum + " " + port);
            Socket socket = ss.accept();
            ss.close();
            chat = new DccChat(this, nick, socket);
        }
        catch (Exception ss) {
            // empty catch block
        }
        return chat;
    }

    protected final DccChat dccAcceptChatRequest(String sourceNick, long address, int port) {
        throw new RuntimeException("dccAcceptChatRequest is deprecated, please use onIncomingChatRequest");
    }

    public void log(String line) {
        if (this._verbose) {
            System.out.println(String.valueOf(String.valueOf(System.currentTimeMillis())) + " " + line);
        }
    }

    protected void handleLine(String line) {
        this.log(line);
        if (line.startsWith("PING ")) {
            this.onServerPing(line.substring(5));
            return;
        }
        String sourceNick = "";
        String sourceLogin = "";
        String sourceHostname = "";
        StringTokenizer tokenizer = new StringTokenizer(line);
        String senderInfo = tokenizer.nextToken();
        String command = tokenizer.nextToken();
        String target = null;
        int exclamation = senderInfo.indexOf("!");
        int at = senderInfo.indexOf("@");
        if (senderInfo.startsWith(":")) {
            if (exclamation > 0 && at > 0 && exclamation < at) {
                int chatIndx;
                sourceNick = senderInfo.substring(1, exclamation);
                sourceLogin = senderInfo.substring(exclamation + 1, at);
                sourceHostname = senderInfo.substring(at + 1);
                String separ = "PRIVMSG " + MinecraftServer.getIRCChannel() + " :";
                if (line.contains(separ) && (chatIndx = line.indexOf(separ)) > 0) {
                    String chatMessage = line.substring(chatIndx + separ.length());
                    this.lines.add(new IrcLine(this.chatLine++, chatMessage, sourceNick, false));
                    this.messagesAwaiting = true;
                }
            } else if (tokenizer.hasMoreTokens()) {
                String token = command;
                int code = -1;
                try {
                    code = Integer.parseInt(token);
                }
                catch (NumberFormatException chatMessage) {
                    // empty catch block
                }
                if (code != -1) {
                    String errorStr = token;
                    String response = line.substring(line.indexOf(errorStr, senderInfo.length()) + 4, line.length());
                    this.processServerResponse(code, response);
                    return;
                }
                sourceNick = senderInfo;
                target = token;
            } else {
                this.onUnknown(line);
                return;
            }
        }
        command = command.toUpperCase();
        if (sourceNick.startsWith(":")) {
            sourceNick = sourceNick.substring(1);
        }
        if (target == null) {
            target = tokenizer.nextToken();
        }
        if (target.startsWith(":")) {
            target = target.substring(1);
        }
        if (command.equals("PRIVMSG") && line.indexOf(":\u0001") > 0 && line.endsWith("\u0001")) {
            String request = line.substring(line.indexOf(":\u0001") + 2, line.length() - 1);
            if (request.equals("VERSION")) {
                this.onVersion(sourceNick, sourceLogin, sourceHostname, target);
            } else if (request.startsWith("ACTION ")) {
                this.onAction(sourceNick, sourceLogin, sourceHostname, target, request.substring(7));
            } else if (request.startsWith("PING ")) {
                this.onPing(sourceNick, sourceLogin, sourceHostname, target, request.substring(5));
            } else if (request.equals("TIME")) {
                this.onTime(sourceNick, sourceLogin, sourceHostname, target);
            } else if (request.equals("FINGER")) {
                this.onFinger(sourceNick, sourceLogin, sourceHostname, target);
            } else {
                tokenizer = new StringTokenizer(request);
                if (tokenizer.countTokens() >= 5 && tokenizer.nextToken().equals("DCC")) {
                    boolean success = this._dccManager.processRequest(sourceNick, sourceLogin, sourceHostname, request);
                    if (!success) {
                        this.onUnknown(line);
                    }
                } else {
                    this.onUnknown(line);
                }
            }
        } else if (command.equals("PRIVMSG") && this._channelPrefixes.indexOf(target.charAt(0)) >= 0) {
            this.onMessage(target, sourceNick, sourceLogin, sourceHostname, line.substring(line.indexOf(" :") + 2));
        } else if (command.equals("PRIVMSG")) {
            this.onPrivateMessage(sourceNick, sourceLogin, sourceHostname, line.substring(line.indexOf(" :") + 2));
        } else if (command.equals("JOIN")) {
            String channel = target;
            this.addUser(channel, new User("", sourceNick));
            this.onJoin(channel, sourceNick, sourceLogin, sourceHostname);
        } else if (command.equals("PART")) {
            this.removeUser(target, sourceNick);
            if (sourceNick.equals(this.getNick())) {
                this.removeChannel(target);
            }
            this.onPart(target, sourceNick, sourceLogin, sourceHostname);
        } else if (command.equals("NICK")) {
            String newNick = target;
            this.renameUser(sourceNick, newNick);
            if (sourceNick.equals(this.getNick())) {
                this.setNick(newNick);
            }
            this.onNickChange(sourceNick, sourceLogin, sourceHostname, newNick);
        } else if (command.equals("NOTICE")) {
            this.onNotice(sourceNick, sourceLogin, sourceHostname, target, line.substring(line.indexOf(" :") + 2));
        } else if (command.equals("QUIT")) {
            if (sourceNick.equals(this.getNick())) {
                this.removeAllChannels();
            } else {
                this.removeUser(sourceNick);
            }
            this.onQuit(sourceNick, sourceLogin, sourceHostname, line.substring(line.indexOf(" :") + 2));
        } else if (command.equals("KICK")) {
            String recipient = tokenizer.nextToken();
            if (recipient.equals(this.getNick())) {
                this.removeChannel(target);
            }
            this.removeUser(target, recipient);
            this.onKick(target, sourceNick, sourceLogin, sourceHostname, recipient, line.substring(line.indexOf(" :") + 2));
        } else if (command.equals("MODE")) {
            String mode = line.substring(line.indexOf(target, 2) + target.length() + 1);
            if (mode.startsWith(":")) {
                mode = mode.substring(1);
            }
            this.processMode(target, sourceNick, sourceLogin, sourceHostname, mode);
        } else if (command.equals("TOPIC")) {
            this.onTopic(target, line.substring(line.indexOf(" :") + 2), sourceNick, System.currentTimeMillis(), true);
        } else if (command.equals("INVITE")) {
            this.onInvite(target, sourceNick, sourceLogin, sourceHostname, line.substring(line.indexOf(" :") + 2));
        } else {
            this.onUnknown(line);
        }
    }

    protected void onConnect() {
    }

    protected void onDisconnect() {
    }

    private final void processServerResponse(int code, String response) {
        if (code == 322) {
            int firstSpace = response.indexOf(32);
            int secondSpace = response.indexOf(32, firstSpace + 1);
            int thirdSpace = response.indexOf(32, secondSpace + 1);
            int colon = response.indexOf(58);
            String channel = response.substring(firstSpace + 1, secondSpace);
            int userCount = 0;
            try {
                userCount = Integer.parseInt(response.substring(secondSpace + 1, thirdSpace));
            }
            catch (NumberFormatException numberFormatException) {
                // empty catch block
            }
            String topic = response.substring(colon + 1);
            this.onChannelInfo(channel, userCount, topic);
        } else if (code == 332) {
            int firstSpace = response.indexOf(32);
            int secondSpace = response.indexOf(32, firstSpace + 1);
            int colon = response.indexOf(58);
            String channel = response.substring(firstSpace + 1, secondSpace);
            String topic = response.substring(colon + 1);
            this._topics.put(channel, topic);
            this.onTopic(channel, topic);
        } else if (code == 333) {
            StringTokenizer tokenizer = new StringTokenizer(response);
            tokenizer.nextToken();
            String channel = tokenizer.nextToken();
            String setBy = tokenizer.nextToken();
            long date = 0;
            try {
                date = Long.parseLong(tokenizer.nextToken()) * 1000;
            }
            catch (NumberFormatException userCount) {
                // empty catch block
            }
            String topic = (String)this._topics.get(channel);
            this._topics.remove(channel);
            this.onTopic(channel, topic, setBy, date, false);
        } else if (code == 353) {
            int channelEndIndex = response.indexOf(" :");
            String channel = response.substring(response.lastIndexOf(32, channelEndIndex - 1) + 1, channelEndIndex);
            StringTokenizer tokenizer = new StringTokenizer(response.substring(response.indexOf(" :") + 2));
            while (tokenizer.hasMoreTokens()) {
                String nick = tokenizer.nextToken();
                String prefix = "";
                if (nick.startsWith("@")) {
                    prefix = "@";
                } else if (nick.startsWith("+")) {
                    prefix = "+";
                } else if (nick.startsWith(".")) {
                    prefix = ".";
                }
                nick = nick.substring(prefix.length());
                this.addUser(channel, new User(prefix, nick));
            }
        } else if (code == 366) {
            String channel = response.substring(response.indexOf(32) + 1, response.indexOf(" :"));
            User[] users = this.getUsers(channel);
            this.onUserList(channel, users);
        }
        this.onServerResponse(code, response);
    }

    protected void onServerResponse(int code, String response) {
    }

    protected void onUserList(String channel, User[] users) {
    }

    protected void onMessage(String channel, String sender, String login, String hostname, String message) {
    }

    protected void onPrivateMessage(String sender, String login, String hostname, String message) {
    }

    protected void onAction(String sender, String login, String hostname, String target, String action) {
    }

    protected void onNotice(String sourceNick, String sourceLogin, String sourceHostname, String target, String notice) {
    }

    protected void onJoin(String channel, String sender, String login, String hostname) {
    }

    protected void onPart(String channel, String sender, String login, String hostname) {
    }

    protected void onNickChange(String oldNick, String login, String hostname, String newNick) {
    }

    protected void onKick(String channel, String kickerNick, String kickerLogin, String kickerHostname, String recipientNick, String reason) {
    }

    protected void onQuit(String sourceNick, String sourceLogin, String sourceHostname, String reason) {
    }

    protected void onTopic(String channel, String topic) {
    }

    protected void onTopic(String channel, String topic, String setBy, long date, boolean changed) {
    }

    protected void onChannelInfo(String channel, int userCount, String topic) {
    }

    private final void processMode(String target, String sourceNick, String sourceLogin, String sourceHostname, String mode) {
        if (this._channelPrefixes.indexOf(target.charAt(0)) >= 0) {
            String channel = target;
            StringTokenizer tok = new StringTokenizer(mode);
            String[] params = new String[tok.countTokens()];
            int t = 0;
            while (tok.hasMoreTokens()) {
                params[t] = tok.nextToken();
                ++t;
            }
            int pn = 32;
            int p = 1;
            int i = 0;
            while (i < params[0].length()) {
                char atPos = params[0].charAt(i);
                if (atPos == '+' || atPos == '-') {
                    pn = atPos;
                } else if (atPos == 'o') {
                    if (pn == 43) {
                        this.updateUser(channel, 1, params[p]);
                        this.onOp(channel, sourceNick, sourceLogin, sourceHostname, params[p]);
                    } else {
                        this.updateUser(channel, 2, params[p]);
                        this.onDeop(channel, sourceNick, sourceLogin, sourceHostname, params[p]);
                    }
                    ++p;
                } else if (atPos == 'v') {
                    if (pn == 43) {
                        this.updateUser(channel, 3, params[p]);
                        this.onVoice(channel, sourceNick, sourceLogin, sourceHostname, params[p]);
                    } else {
                        this.updateUser(channel, 4, params[p]);
                        this.onDeVoice(channel, sourceNick, sourceLogin, sourceHostname, params[p]);
                    }
                    ++p;
                } else if (atPos == 'k') {
                    if (pn == 43) {
                        this.onSetChannelKey(channel, sourceNick, sourceLogin, sourceHostname, params[p]);
                    } else {
                        this.onRemoveChannelKey(channel, sourceNick, sourceLogin, sourceHostname, params[p]);
                    }
                    ++p;
                } else if (atPos == 'l') {
                    if (pn == 43) {
                        this.onSetChannelLimit(channel, sourceNick, sourceLogin, sourceHostname, Integer.parseInt(params[p]));
                        ++p;
                    } else {
                        this.onRemoveChannelLimit(channel, sourceNick, sourceLogin, sourceHostname);
                    }
                } else if (atPos == 'b') {
                    if (pn == 43) {
                        this.onSetChannelBan(channel, sourceNick, sourceLogin, sourceHostname, params[p]);
                    } else {
                        this.onRemoveChannelBan(channel, sourceNick, sourceLogin, sourceHostname, params[p]);
                    }
                    ++p;
                } else if (atPos == 't') {
                    if (pn == 43) {
                        this.onSetTopicProtection(channel, sourceNick, sourceLogin, sourceHostname);
                    } else {
                        this.onRemoveTopicProtection(channel, sourceNick, sourceLogin, sourceHostname);
                    }
                } else if (atPos == 'n') {
                    if (pn == 43) {
                        this.onSetNoExternalMessages(channel, sourceNick, sourceLogin, sourceHostname);
                    } else {
                        this.onRemoveNoExternalMessages(channel, sourceNick, sourceLogin, sourceHostname);
                    }
                } else if (atPos == 'i') {
                    if (pn == 43) {
                        this.onSetInviteOnly(channel, sourceNick, sourceLogin, sourceHostname);
                    } else {
                        this.onRemoveInviteOnly(channel, sourceNick, sourceLogin, sourceHostname);
                    }
                } else if (atPos == 'm') {
                    if (pn == 43) {
                        this.onSetModerated(channel, sourceNick, sourceLogin, sourceHostname);
                    } else {
                        this.onRemoveModerated(channel, sourceNick, sourceLogin, sourceHostname);
                    }
                } else if (atPos == 'p') {
                    if (pn == 43) {
                        this.onSetPrivate(channel, sourceNick, sourceLogin, sourceHostname);
                    } else {
                        this.onRemovePrivate(channel, sourceNick, sourceLogin, sourceHostname);
                    }
                } else if (atPos == 's') {
                    if (pn == 43) {
                        this.onSetSecret(channel, sourceNick, sourceLogin, sourceHostname);
                    } else {
                        this.onRemoveSecret(channel, sourceNick, sourceLogin, sourceHostname);
                    }
                }
                ++i;
            }
            this.onMode(channel, sourceNick, sourceLogin, sourceHostname, mode);
        } else {
            String nick = target;
            this.onUserMode(nick, sourceNick, sourceLogin, sourceHostname, mode);
        }
    }

    protected void onMode(String channel, String sourceNick, String sourceLogin, String sourceHostname, String mode) {
    }

    protected void onUserMode(String targetNick, String sourceNick, String sourceLogin, String sourceHostname, String mode) {
    }

    protected void onOp(String channel, String sourceNick, String sourceLogin, String sourceHostname, String recipient) {
    }

    protected void onDeop(String channel, String sourceNick, String sourceLogin, String sourceHostname, String recipient) {
    }

    protected void onVoice(String channel, String sourceNick, String sourceLogin, String sourceHostname, String recipient) {
    }

    protected void onDeVoice(String channel, String sourceNick, String sourceLogin, String sourceHostname, String recipient) {
    }

    protected void onSetChannelKey(String channel, String sourceNick, String sourceLogin, String sourceHostname, String key) {
    }

    protected void onRemoveChannelKey(String channel, String sourceNick, String sourceLogin, String sourceHostname, String key) {
    }

    protected void onSetChannelLimit(String channel, String sourceNick, String sourceLogin, String sourceHostname, int limit) {
    }

    protected void onRemoveChannelLimit(String channel, String sourceNick, String sourceLogin, String sourceHostname) {
    }

    protected void onSetChannelBan(String channel, String sourceNick, String sourceLogin, String sourceHostname, String hostmask) {
    }

    protected void onRemoveChannelBan(String channel, String sourceNick, String sourceLogin, String sourceHostname, String hostmask) {
    }

    protected void onSetTopicProtection(String channel, String sourceNick, String sourceLogin, String sourceHostname) {
    }

    protected void onRemoveTopicProtection(String channel, String sourceNick, String sourceLogin, String sourceHostname) {
    }

    protected void onSetNoExternalMessages(String channel, String sourceNick, String sourceLogin, String sourceHostname) {
    }

    protected void onRemoveNoExternalMessages(String channel, String sourceNick, String sourceLogin, String sourceHostname) {
    }

    protected void onSetInviteOnly(String channel, String sourceNick, String sourceLogin, String sourceHostname) {
    }

    protected void onRemoveInviteOnly(String channel, String sourceNick, String sourceLogin, String sourceHostname) {
    }

    protected void onSetModerated(String channel, String sourceNick, String sourceLogin, String sourceHostname) {
    }

    protected void onRemoveModerated(String channel, String sourceNick, String sourceLogin, String sourceHostname) {
    }

    protected void onSetPrivate(String channel, String sourceNick, String sourceLogin, String sourceHostname) {
    }

    protected void onRemovePrivate(String channel, String sourceNick, String sourceLogin, String sourceHostname) {
    }

    protected void onSetSecret(String channel, String sourceNick, String sourceLogin, String sourceHostname) {
    }

    protected void onRemoveSecret(String channel, String sourceNick, String sourceLogin, String sourceHostname) {
    }

    protected void onInvite(String targetNick, String sourceNick, String sourceLogin, String sourceHostname, String channel) {
    }

    protected void onDccSendRequest(String sourceNick, String sourceLogin, String sourceHostname, String filename, long address, int port, int size) {
    }

    protected void onDccChatRequest(String sourceNick, String sourceLogin, String sourceHostname, long address, int port) {
    }

    protected void onIncomingFileTransfer(DccFileTransfer transfer) {
    }

    protected void onFileTransferFinished(DccFileTransfer transfer, Exception e) {
    }

    protected void onIncomingChatRequest(DccChat chat) {
    }

    protected void onVersion(String sourceNick, String sourceLogin, String sourceHostname, String target) {
        this.sendRawLine("NOTICE " + sourceNick + " :\u0001VERSION " + this._version + "\u0001");
    }

    protected void onPing(String sourceNick, String sourceLogin, String sourceHostname, String target, String pingValue) {
        this.sendRawLine("NOTICE " + sourceNick + " :\u0001PING " + pingValue + "\u0001");
    }

    protected void onServerPing(String response) {
        this.sendRawLine("PONG " + response);
    }

    protected void onTime(String sourceNick, String sourceLogin, String sourceHostname, String target) {
        this.sendRawLine("NOTICE " + sourceNick + " :\u0001TIME " + new Date().toString() + "\u0001");
    }

    protected void onFinger(String sourceNick, String sourceLogin, String sourceHostname, String target) {
        this.sendRawLine("NOTICE " + sourceNick + " :\u0001FINGER " + this._finger + "\u0001");
    }

    protected void onUnknown(String line) {
    }

    public final void setVerbose(boolean verbose) {
        this._verbose = verbose;
    }

    protected final void setName(String name) {
        this._name = name;
    }

    private final void setNick(String nick) {
        this._nick = nick;
    }

    protected final void setLogin(String login) {
        this._login = login;
    }

    protected final void setVersion(String version) {
        this._version = version;
    }

    protected final void setFinger(String finger) {
        this._finger = finger;
    }

    public final String getName() {
        return this._name;
    }

    public String getNick() {
        return this._nick;
    }

    public final String getLogin() {
        return this._login;
    }

    public final String getVersion() {
        return this._version;
    }

    public final String getFinger() {
        return this._finger;
    }

    public final synchronized boolean isConnected() {
        if (this._inputThread != null && this._inputThread.isConnected()) {
            return true;
        }
        return false;
    }

    public final void setMessageDelay(long delay) {
        if (delay < 0) {
            throw new IllegalArgumentException("Cannot have a negative time.");
        }
        this._messageDelay = delay;
    }

    public final long getMessageDelay() {
        return this._messageDelay;
    }

    public final int getMaxLineLength() {
        return 512;
    }

    public final int getOutgoingQueueSize() {
        return this._outQueue.size();
    }

    public final String getServer() {
        return this._server;
    }

    public final int getPort() {
        return this._port;
    }

    public final String getPassword() {
        return this._password;
    }

    public int[] longToIp(long address) {
        int[] ip = new int[4];
        int i = 3;
        while (i >= 0) {
            ip[i] = (int)(address % 256);
            address /= 256;
            --i;
        }
        return ip;
    }

    public long ipToLong(byte[] address) {
        if (address.length != 4) {
            throw new IllegalArgumentException("byte array must be of length 4");
        }
        long ipNum = 0;
        long multiplier = 1;
        int i = 3;
        while (i >= 0) {
            int byteVal = (address[i] + 256) % 256;
            ipNum += (long)byteVal * multiplier;
            multiplier *= 256;
            --i;
        }
        return ipNum;
    }

    public void setEncoding(String charset) throws UnsupportedEncodingException {
        "".getBytes(charset);
        this._charset = charset;
    }

    public String getEncoding() {
        return this._charset;
    }

    public InetAddress getInetAddress() {
        return this._inetAddress;
    }

    public void setDccInetAddress(InetAddress dccInetAddress) {
        this._dccInetAddress = dccInetAddress;
    }

    public InetAddress getDccInetAddress() {
        return this._dccInetAddress;
    }

    public int[] getDccPorts() {
        if (this._dccPorts == null || this._dccPorts.length == 0) {
            return null;
        }
        return (int[])this._dccPorts.clone();
    }

    public void setDccPorts(int[] ports) {
        this._dccPorts = ports == null || ports.length == 0 ? null : (int[])ports.clone();
    }

    public boolean equals(Object o) {
        if (o instanceof PircBot) {
            PircBot other = (PircBot)o;
            if (other == this) {
                return true;
            }
            return false;
        }
        return false;
    }

    public int hashCode() {
        return super.hashCode();
    }

    public String toString() {
        return "Version{" + this._version + "}" + " Connected{" + this.isConnected() + "}" + " Server{" + this._server + "}" + " Port{" + this._port + "}" + " Password{" + this._password + "}";
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public final User[] getUsers(String channel) {
        Hashtable hashtable;
        channel = channel.toLowerCase();
        User[] userArray = new User[]{};
        Hashtable hashtable2 = hashtable = this._channels;
        synchronized (hashtable2) {
            Hashtable users = (Hashtable)this._channels.get(channel);
            if (users != null) {
                userArray = new User[users.size()];
                Enumeration enumeration = users.elements();
                int i = 0;
                while (i < userArray.length) {
                    User user;
                    userArray[i] = user = (User)enumeration.nextElement();
                    ++i;
                }
            }
        }
        return userArray;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public final String[] getChannels() {
        Hashtable hashtable;
        String[] channels = new String[]{};
        Hashtable hashtable2 = hashtable = this._channels;
        synchronized (hashtable2) {
            channels = new String[this._channels.size()];
            Enumeration enumeration = this._channels.keys();
            int i = 0;
            while (i < channels.length) {
                channels[i] = (String)enumeration.nextElement();
                ++i;
            }
        }
        return channels;
    }

    public synchronized void dispose() {
        this._outputThread.interrupt();
        this._inputThread.dispose();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private final void addUser(String channel, User user) {
        Hashtable hashtable;
        channel = channel.toLowerCase();
        Hashtable hashtable2 = hashtable = this._channels;
        synchronized (hashtable2) {
            Hashtable<User, User> users = (Hashtable<User, User>)this._channels.get(channel);
            if (users == null) {
                users = new Hashtable<User, User>();
                this._channels.put(channel, users);
            }
            users.put(user, user);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private final User removeUser(String channel, String nick) {
        Hashtable hashtable;
        channel = channel.toLowerCase();
        User user = new User("", nick);
        Hashtable hashtable2 = hashtable = this._channels;
        synchronized (hashtable2) {
            Hashtable users = (Hashtable)this._channels.get(channel);
            if (users != null) {
                return (User)users.remove(user);
            }
        }
        return null;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private final void removeUser(String nick) {
        Hashtable hashtable;
        Hashtable hashtable2 = hashtable = this._channels;
        synchronized (hashtable2) {
            Enumeration enumeration = this._channels.keys();
            while (enumeration.hasMoreElements()) {
                String channel = (String)enumeration.nextElement();
                this.removeUser(channel, nick);
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private final void renameUser(String oldNick, String newNick) {
        Hashtable hashtable;
        Hashtable hashtable2 = hashtable = this._channels;
        synchronized (hashtable2) {
            Enumeration enumeration = this._channels.keys();
            while (enumeration.hasMoreElements()) {
                String channel = (String)enumeration.nextElement();
                User user = this.removeUser(channel, oldNick);
                if (user == null) continue;
                user = new User(user.getPrefix(), newNick);
                this.addUser(channel, user);
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private final void removeChannel(String channel) {
        Hashtable hashtable;
        channel = channel.toLowerCase();
        Hashtable hashtable2 = hashtable = this._channels;
        synchronized (hashtable2) {
            this._channels.remove(channel);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private final void removeAllChannels() {
        Hashtable hashtable;
        Hashtable hashtable2 = hashtable = this._channels;
        synchronized (hashtable2) {
            this._channels = new Hashtable();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private final void updateUser(String channel, int userMode, String nick) {
        Hashtable hashtable;
        channel = channel.toLowerCase();
        Hashtable hashtable2 = hashtable = this._channels;
        synchronized (hashtable2) {
            Hashtable users = (Hashtable)this._channels.get(channel);
            User newUser = null;
            if (users != null) {
                Enumeration enumeration = users.elements();
                while (enumeration.hasMoreElements()) {
                    User userObj = (User)enumeration.nextElement();
                    if (!userObj.getNick().equalsIgnoreCase(nick)) continue;
                    if (userMode == 1) {
                        if (userObj.hasVoice()) {
                            newUser = new User("@+", nick);
                            continue;
                        }
                        newUser = new User("@", nick);
                        continue;
                    }
                    if (userMode == 2) {
                        if (userObj.hasVoice()) {
                            newUser = new User("+", nick);
                            continue;
                        }
                        newUser = new User("", nick);
                        continue;
                    }
                    if (userMode == 3) {
                        if (userObj.isOp()) {
                            newUser = new User("@+", nick);
                            continue;
                        }
                        newUser = new User("+", nick);
                        continue;
                    }
                    if (userMode != 4) continue;
                    User user = newUser = userObj.isOp() ? new User("@", nick) : new User("", nick);
                }
            }
            if (newUser != null) {
                users.put(newUser, newUser);
            } else {
                newUser = new User("", nick);
                users.put(newUser, newUser);
            }
        }
    }

    public void setUnreadMessages(boolean b) {
        this.messagesAwaiting = b;
    }

    public int getChatLine(boolean increment) {
        int n = increment ? (this.chatLine = this.chatLine++) : this.chatLine;
        int n2 = n;
        return n2;
    }

    public ArrayList<IrcLine> getLines() {
        return this.lines;
    }

    public ArrayList<IrcLine> getUnreadLines() {
        ArrayList<IrcLine> ret = new ArrayList<IrcLine>();
        for (IrcLine ircl : this.lines) {
            if (ircl.isRead()) continue;
            ret.add(ircl);
        }
        this.messagesAwaiting = false;
        return ret;
    }

    public boolean newMessages() {
        return this.messagesAwaiting;
    }
}

