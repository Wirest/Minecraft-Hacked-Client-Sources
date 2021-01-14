package info.sigmaclient.management.users;

import info.sigmaclient.json.Json;
import info.sigmaclient.json.JsonObject;
import info.sigmaclient.json.JsonValue;
import info.sigmaclient.management.agora.Agora;
import info.sigmaclient.management.users.impl.*;
import info.sigmaclient.Client;
import info.sigmaclient.util.security.Crypto;
import info.sigmaclient.util.security.HardwareUtil;

import java.io.*;
import java.net.*;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Created by Arithmo on 8/11/2017 at 9:57 PM.
 */
public class UserManager {

    private User userStatus = new Default();

    public User getUser() {
        return userStatus;
    }

    private String initialHWID;
    private String username;
    private String versionString = "unknown";
    private boolean updateAvailable = false;
    private boolean updateNeeded = false;
    private String newVersionName = "";
    private ArrayList<String> newChangelog = new ArrayList<>();
    private int updateProgress = 0;

    private String firstHWID;
    private String secondHWID;
    private boolean loginNeeded = false;
    private boolean premium = false;
    private boolean trolled = false;
    private boolean finishedLoginSequence = false;
    private String userSerialNumber = HardwareUtil.getUserSerialNumber();
    private String session = UUID.randomUUID().toString().replaceAll("-", "");
    private String premsTimestamp = null;
    private Challenge lastChallenge = null;

    private byte[] hwid;

    public UserManager() {
    }

    public void computeOldHwid() {
        try {
            if (getOSType() != Crypto.EnumOS.WINDOWS) {
                return;
            }
            secondHWID = Crypto.encrypt(Crypto.getSecretOLD(), HardwareUtil.getOLDWINDOWS());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void computeNewHwid() {
        switch (getOSType()) {
            case WINDOWS:
                try {
                    initialHWID = HardwareUtil.getWindowsSerialNumber();
                } catch (IOException ignored) {
                }
                break;
            case LINUX:
                initialHWID = "linux";//HardwareUtil.getLinuxSerialNumber();
                break;
            case OSX:
                initialHWID = "macosx";//HardwareUtil.getMacSerialNumber();
                break;
        }
        try {
            firstHWID = Crypto.encrypt(Crypto.getSecret(), initialHWID);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*public void checkUserStatus(String username) {
        this.username = username;
        switch (getOSType()) {
            case WINDOWS:
                try {
                    initialHWID = HardwareUtil.getWindowsSerialNumber();
                } catch (IOException ignored) {
                }
                break;
            case LINUX:
                initialHWID = HardwareUtil.getLinuxSerialNumber();
                break;
            case OSX:
                initialHWID = HardwareUtil.getMacSerialNumber();
                break;
        }
        try {
            firstUsername = Crypto.encrypt(Crypto.getSecret(), username);
            secondUsername = Crypto.encrypt(Crypto.getSecretOLD(), username);
            firstHWID = Crypto.encrypt(Crypto.getSecret(), initialHWID);
            secondHWID = Crypto.encrypt(Crypto.getSecretOLD(), HardwareUtil.getOLDWINDOWS());
        } catch (Exception ignored) {

        }
        //Do HWID Checking, if there's no valid user found, they're default.
        //statusCheck();
        loadPremiumsClasses();
    }*/

    public void loadVersionString() {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("v.svf");
        if (inputStream != null) {
            try (BufferedReader bufferedInputStream = new BufferedReader(new InputStreamReader(inputStream))) {
                versionString = bufferedInputStream.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            versionString = "live";
        }
    }

   /* public void statusCheck() {
        applyStatus(initialHWID, username);
    }

    private void applyStatus(String userHWID, String username) {
        (new Thread(() -> {
            String site = "http://pastebin.com/raw.php?i=AXyxaaSf";
            String onlineCheck = "www.pastebin.com";
            boolean connected;
            Socket sock = new Socket();
            InetSocketAddress addr = new InetSocketAddress(onlineCheck, 80);
            try {
                sock.connect(addr, 6000);
                connected = true;
            } catch (IOException e) {
                connected = false;
            } finally {
                try {
                    sock.close();
                } catch (final IOException e) {
                }
            }

            if (connected) {
                try {
                    final URL obj = new URL(site);
                    HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
                    conn.setReadTimeout(5000);
                    conn.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
                    conn.addRequestProperty("User-Agent", "Mozilla");
                    conn.addRequestProperty("Referer", "google.com");
                    boolean redirect = false;
                    // normally, 3xx is redirect
                    final int status = conn.getResponseCode();
                    if (status != HttpURLConnection.HTTP_OK) {
                        if (status == HttpURLConnection.HTTP_MOVED_TEMP || status == HttpURLConnection.HTTP_MOVED_PERM || status == HttpURLConnection.HTTP_SEE_OTHER) {
                            redirect = true;
                        }
                    }
                    if (redirect) {
                        String newUrl = conn.getHeaderField("Location");
                        String cookies = conn.getHeaderField("Set-Cookie");
                        conn = (HttpURLConnection) new URL(newUrl).openConnection();
                        conn.setRequestProperty("Cookie", cookies);
                        conn.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
                        conn.addRequestProperty("User-Agent", "Mozilla");
                        conn.addRequestProperty("Referer", "google.com");
                    }
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String line;
                    while ((line = in.readLine()) != null) {
                        String[] split = line.split(":");
                        if ((split[0].equalsIgnoreCase(firstUsername) || split[0].equalsIgnoreCase(secondUsername))
                                && (split[1].equalsIgnoreCase(firstHWID) || split[1].equalsIgnoreCase(secondHWID))) {
                            switch (split[2]) {
                                case "Staff": {
                                    userStatus = new Staff(username, userHWID);
                                    break;
                                }
                                case "Blacklisted": {
                                    userStatus = new Blacklisted();
                                    break;
                                }
                                case "Upgraded": {
                                    userStatus = new Upgraded(username, userHWID);
                                    break;
                                }
                            }
                        }
                    }
                    in.close();
                } catch (final Exception ignored) {
                }
            } else {
                Client.instance = null;
            }
        })).start();
    }*/


    public static Crypto.EnumOS getOSType() {
        String var0 = System.getProperty("os.name").toLowerCase();
        return var0.contains("win") ? Crypto.EnumOS.WINDOWS : (var0.contains("mac") ? Crypto.EnumOS.OSX : (var0.contains("solaris") ? Crypto.EnumOS.SOLARIS : (var0.contains("sunos") ? Crypto.EnumOS.SOLARIS : (var0.contains("linux") ? Crypto.EnumOS.LINUX : (var0.contains("unix") ? Crypto.EnumOS.LINUX : Crypto.EnumOS.UNKNOWN)))));
    }

    public boolean loadPremiumsClasses() {
        return new PremiumLoader().load();
    }

    public boolean checkVersion() {
        loadVersionString();
        String url = "https://" + "sabrinaprg" + '.' + "sigmaclient.info" + "/" + versionString + bytesToHex(TTSystem.getInstance().getTT()) + "/checkVersion";
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestProperty("AuthorizationNEW", secondHWID);
            connection.setRequestProperty("Session", session);
            //System.out.println("Out tattoo test " + TattooSystem.getInstance().getTattoo()[0]);
            if (connection.getResponseCode() == 200) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder text = new StringBuilder();
                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    text.append(line);
                }
                JsonObject responseJson = Json.parse(text.toString()).asObject();
                updateAvailable = responseJson.getBoolean("updateAvailable", false);
                updateNeeded = responseJson.getBoolean("updateNeeded", false);
                newChangelog.clear();
                if (updateAvailable || updateNeeded) {
                    newVersionName = responseJson.getString("newVerTitle", null);
                    for (Iterator<JsonValue> it = responseJson.get("newVerChangelog").asArray().iterator(); it.hasNext(); ) {
                        newChangelog.add(it.next().asString());
                    }
                }
                Client.version = responseJson.getString("currentVerTitle", null);
                Client.changelog.clear();
                for (Iterator<JsonValue> it = responseJson.get("currentVerChangelog").asArray().iterator(); it.hasNext(); ) {
                    Client.changelog.add(it.next().asString());
                }

                if (updateNeeded) {
                    update();
                }
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void update() {
        //TODO Mettre à jours le client
        updateProgress = 1;
        String url = "https://" + "sabrinaprg" + '.' + "sigmaclient.info" + "/" + versionString + bytesToHex(TTSystem.getInstance().getTT()) + "/downloadVersion";
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestProperty("Session", session);
            if (connection.getResponseCode() == 200) {
                CodeSource codeSource = UserManager.class
                        .getClassLoader()
                        .getClass()
                        .getProtectionDomain()
                        .getCodeSource();
                File actualInstance = new File(URLDecoder.decode(codeSource
                        .getLocation()
                        .toURI()
                        .getPath(), "UTF-8"));
                File tempInstance = new File(actualInstance.getParent(), "Sigma.jar.part");
                /*File downloadedInstance = new File(actualInstance.getParent(), "Sigma.jar.part");
                try(FileOutputStream fos = new FileOutputStream(downloadedInstance)) {
                    byte[] buff = new by
                }*/
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                int i = 0;
                byte[] buff = new byte[1024];
                int readed = 0;
                while ((readed = connection.getInputStream().read(buff)) > -1) {
                    baos.write(buff, 0, readed);
                    i++;
                    if (i % 1024 == 0 && updateProgress < 50) {
                        updateProgress++;
                    }
                }
                connection.getInputStream().close();
                updateProgress = 50;
                try (ZipOutputStream out = new ZipOutputStream(new FileOutputStream(tempInstance))) {
                    TreeSet<String> files = new TreeSet<>();
                    try (ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(baos.toByteArray()))) {
                        for (ZipEntry ze = zis.getNextEntry(); ze != null; ze = zis.getNextEntry()) {
                            files.add(ze.getName());
                            out.putNextEntry(new ZipEntry(ze.getName()));
                            for (int read = zis.read(buff); read > -1; read = zis.read(buff)) {
                                out.write(buff, 0, read);
                            }
                            out.closeEntry();
                        }
                    }

                    updateProgress = 90;

                    try (ZipInputStream zis = new ZipInputStream(new FileInputStream(actualInstance))) {
                        for (ZipEntry ze = zis.getNextEntry(); ze != null; ze = zis.getNextEntry()) {
                            if (!files.contains(ze.getName()) && !ze.getName().endsWith(".class")) {
                                out.putNextEntry(new ZipEntry(ze.getName()));
                                for (int read = zis.read(buff); read > -1; read = zis.read(buff)) {
                                    out.write(buff, 0, read);
                                }
                                out.closeEntry();
                            }
                        }
                    }
                }
                updateProgress = 95;
                copy(tempInstance, actualInstance);
                tempInstance.delete();
                updateProgress = 100;
                System.exit(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private static void copy(File source, File destination) {
        try (FileInputStream fis = new FileInputStream(source); FileOutputStream fos = new FileOutputStream(destination)) {
            byte[] buff = new byte[1024];
            int readedLen = 0;
            while ((readedLen = fis.read(buff)) > 0) {
                fos.write(buff, 0, readedLen);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getLoginLink() {
        String url = "https://" + "sabrinaprg" + '.' + "sigmaclient.info" + "/" + versionString + bytesToHex(TTSystem.getInstance().getTT()) + "/login";
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestProperty("AuthorizationNEW", secondHWID);
            connection.setRequestProperty("AuthorizationOLD", firstHWID);
            connection.setRequestProperty("USN", userSerialNumber);
            connection.setRequestProperty("Session", session);
            if (connection.getResponseCode() == 403) {
                userStatus = new Blacklisted();
            } else if (connection.getResponseCode() == 200) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder text = new StringBuilder();
                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    text.append(line);
                }
                JsonObject responseJson = Json.parse(text.toString()).asObject();
                if (responseJson.get("trolled") != null) {
                    trolled = true;
                }
                if (responseJson.get("username") != null) {
                    loginNeeded = false;

                    new Agora(responseJson.getString("token", null));

                    username = responseJson.getString("username", null);
                    if (responseJson.get("prems") != null) {
                        premium = true;
                        userStatus = new Upgraded(username, secondHWID);

                    } else {
                        userStatus = new Normal(username, secondHWID);
                    }
                    if (responseJson.get("tdt") != null) {
                        premsTimestamp = responseJson.getString("tdt", null);
                    }
                } else {
                    loginNeeded = true;
                    return responseJson.getString("url", null);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (premium) {
            loadPremiumsClasses();
        }
        return null;
    }

    public Challenge getChallenge() {
        if (lastChallenge != null && lastChallenge.isValid()) {
            return lastChallenge;
        }
        String url = "https://" + "sabrinaprg" + '.' + "sigmaclient.info" + "/" + versionString + bytesToHex(TTSystem.getInstance().getTT()) + "/getChallenge";
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestProperty("Session", session);
            if (connection.getResponseCode() == 200) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder text = new StringBuilder();
                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    text.append(line);
                }
                JsonObject responseJson = Json.parse(text.toString()).asObject();
                String uid = responseJson.getString("uid", null);
                boolean captcha = responseJson.getBoolean("captcha", false);
                lastChallenge = new Challenge(uid, captcha);
                return lastChallenge;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String authenticate(Challenge challenge, String username, String password) {
        String url = "https://" + "sabrinaprg" + '.' + "sigmaclient.info" + "/" + versionString + bytesToHex(TTSystem.getInstance().getTT()) + "/authenticate";
        challenge.setValid(false);
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestProperty("USN", userSerialNumber);
            connection.setRequestProperty("Session", session);

            connection.setDoOutput(true);
            String data = "challengeUid=" + challenge.getUid() + "&challengeAnswer=" + URLEncoder.encode(challenge.getAnswer(), "UTF-8") + "&username=" + URLEncoder.encode(username, "UTF-8") + "&password=" + URLEncoder.encode(password, "UTF-8");
            connection.getOutputStream().write(data.getBytes("UTF-8"));
            if (connection.getResponseCode() == 200) {
                try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    StringBuilder text = new StringBuilder();
                    String line = null;
                    while ((line = bufferedReader.readLine()) != null) {
                        text.append(line);
                    }
                    JsonObject responseJson = Json.parse(text.toString()).asObject();
                    if (responseJson.getBoolean("success", false)) {
                        return null;
                    } else {
                        return responseJson.getString("error", "Unexpected error");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (premium) {
            loadPremiumsClasses();
        }
        return "Unable to connect to server";
    }

    public String register(Challenge challenge, String username, String password, String mail) {
        String url = "https://" + "sabrinaprg" + '.' + "sigmaclient.info" + "/" + versionString + bytesToHex(TTSystem.getInstance().getTT()) + "/register";
        challenge.setValid(false);
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestProperty("USN", userSerialNumber);
            connection.setRequestProperty("AuthorizationNEW", secondHWID);
            connection.setRequestProperty("AuthorizationOLD", firstHWID);
            connection.setRequestProperty("Session", session);

            connection.setDoOutput(true);
            String data = "challengeUid=" + challenge.getUid() + "&challengeAnswer=" + URLEncoder.encode(challenge.getAnswer(), "UTF-8") + "&username=" + URLEncoder.encode(username, "UTF-8") + "&password=" + URLEncoder.encode(password, "UTF-8") + "&mail=" + URLEncoder.encode(mail, "UTF-8");
            connection.getOutputStream().write(data.getBytes("UTF-8"));
            if (connection.getResponseCode() == 200) {
                try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    StringBuilder text = new StringBuilder();
                    String line = null;
                    while ((line = bufferedReader.readLine()) != null) {
                        text.append(line);
                    }
                    JsonObject responseJson = Json.parse(text.toString()).asObject();
                    if (responseJson.getBoolean("success", false)) {
                        return null;
                    } else {
                        return responseJson.getString("error", "Unexpected error");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (premium) {
            loadPremiumsClasses();
        }
        return "Unable to connect to server";
    }

    public String claimPremium(Challenge challenge, String key) {
        String url = "https://" + "sabrinaprg" + '.' + "sigmaclient.info" + "/" + versionString + bytesToHex(TTSystem.getInstance().getTT()) + "/claimPremium";
        challenge.setValid(false);
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestProperty("Session", session);

            connection.setDoOutput(true);
            String data = "challengeUid=" + challenge.getUid() + "&challengeAnswer=" + URLEncoder.encode(challenge.getAnswer(), "UTF-8") + "&key=" + URLEncoder.encode(key, "UTF-8");
            connection.getOutputStream().write(data.getBytes("UTF-8"));
            if (connection.getResponseCode() == 200) {
                try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    StringBuilder text = new StringBuilder();
                    String line = null;
                    while ((line = bufferedReader.readLine()) != null) {
                        text.append(line);
                    }
                    JsonObject responseJson = Json.parse(text.toString()).asObject();
                    if (responseJson.getBoolean("success", false)) {
                        return null;
                    } else {
                        return responseJson.getString("error", "Unexpected error");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (premium) {
            loadPremiumsClasses();
        }
        return "Unable to connect to server";
    }

    public boolean isUpdateAvailable() {
        return updateAvailable;
    }

    public boolean isUpdateNeeded() {
        return updateNeeded;
    }

    public ArrayList<String> getNewChangelog() {
        return newChangelog;
    }

    public String getNewVersionName() {
        return newVersionName;
    }

    public int getUpdateProgress() {
        return updateProgress;
    }

    public boolean isLoginNeeded() {
        return loginNeeded;
    }

    public boolean isFinishedLoginSequence() {
        return finishedLoginSequence;
    }

    public void setFinishedLoginSequence() {
        finishedLoginSequence = true;
    }

    public String bytesToHex(byte[] hash) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public String getVersionString() {
        return versionString;
    }

    public String getSecondHWID() {
        return secondHWID;
    }

    public String getSession() {
        return session;
    }

    public boolean isTrolled() {
        return trolled;
    }

    public String getUserSerialNumber() {
        return userSerialNumber;
    }

    public String getUsername() {
        return username;
    }

    public String getPremsTimestamp() {
        return premsTimestamp;
    }

    public boolean isPremium() {
        return premium;
    }
}
