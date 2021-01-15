package me.xatzdevelopments.xatz.client.settings;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import me.xatzdevelopments.xatz.client.main.Xatz;
import me.xatzdevelopments.xatz.gui.Level;
import me.xatzdevelopments.xatz.gui.Notification;

public class ClientSettings {
	public static boolean hackerDetectAutoNotify = false;
	public static boolean hackerDetectMoreInfo = false;
	public static double TpAuraAPS = 6;
	public static double TpAurarange = 200;
	public static int TpAuramaxTargets = 50;
	public static double AutoClickerhitPercent = 0.8;
	public static int AutoClickermin = 1;
	public static int AutoClickermax = 10;
	public static double ExtendedReachrange = 6f;
	public static boolean flightkick = false;
	public static boolean Flightsmooth = false;
	public static double FlightdefaultSpeed = 1;
	public static double KillauraRange = 4.3;
	public static double BedNukerRange = 4.3;
	public static double autopothealth = 6;
	public static double invmanagerdelay = 1;  
	public static double invmanagermaxblocks = 64;
	public static double invmanagermaxfood = 16;
	public static boolean invmanagerarchery = false; 
	public static boolean BlinkFly = false;
	public static double autoarmordelay = 1;
	public static double scaffoldexpand = 0.23;
	public static double scaffoldlegitspeed = 1;
	public static boolean smallItems = true;
	public static boolean blinkBhop = false;
	public static String exhiBlockType = "Swank";
	public static double scaffoldaactowerspeed = 0.2;  
	public static double scaffoldcustomdelay = 100;
	public static double nametagscale = 4;
	public static double hudcolorred = 0.5;  
	public static double hudcolorgreen = 0.5;
	public static double hudcolorblue = 0.5;
	public static double scaffolddelay = 100; 
	public static double scaffoldtimerspeed = 1;
	public static double scaffoldjitterfactor = 0.1;
	public static boolean scaffoldeagle = false;
	public static boolean antiafktoggle = false;
	public static boolean KillauraHeadsnap = false; 
	public static double autogapplehealth = 4; 
	public static int KillauraAPS = 12; 
	public static int Killaura2range = 4;
	public static int Killaura2delay = 100; 
	public static int Killaura2delayrandom = 50;
	public static int Killaura2yawspeed = 90;
	public static int Killaura2pitchspeed = 90;
	public static int Killaura2fov = 180;
	public static boolean scaffoldsprint = false;
	public static boolean flyDamage = false;
	public static boolean scaffoldsneak = false;
	public static boolean scaffoldsilent = false;
	public static boolean scaffoldjitter = false;
	public static boolean scaffoldtimer = false;
	public static boolean killaura2teams = false; 
	public static boolean killaura2walls = false;      
	public static boolean killaura2players = true;
	public static boolean killaura2animals = true;
	public static boolean killaura2keepsprint = false; 
	public static boolean killaura2stopsprint = false;
	public static boolean killaura2checkrotations = false;
	public static boolean scaffoldsamey = false;
	public static boolean scaffoldrot = false; 
	public static boolean scaffoldtowermove = false; 
	public static boolean scaffoldnoswing = false;
	public static boolean scaffoldpicker = false;
	public static int MultiUseamount = 100;
	public static float Nametagssize = 5;
	public static int Nukerradius = 4;
	public static double Phasedistance = 1.5D;
	public static double Regenspeed = 200;
	public static double VanillaspeedFactor = 1;
	public static double VanillaSpeedFactor = 20;
	public static double Spinspeed = 30;
	public static double stepHeight = 1f;
	public static double TPrange = 1.5;
	public static double Timerspeed = 2;
	public static boolean chestStealerAura = false;
	public static boolean scaffoldtower = true;
	public static boolean autopotspeed = true;
	public static boolean autopotregen = true; 
	public static boolean autopotpredict = true; 
	public static boolean playerESP = true;
	public static boolean mobsESP;
	public static boolean animalESP;
	public static boolean blockHuntESP;
	public static boolean chestESP;
	public static boolean blockHuntTracers;
	public static boolean playerTracers = true;
	public static boolean mobsTracers;
	public static boolean animalTracers;
	public static boolean chestTracers;
	public static boolean mainMenuParticles = false;
	public static int clickGuiFontSize = 13;
	public static boolean clickGuiTint = true;
	public static boolean lockGuiScale = false;
	public static boolean espFade = true;
	public static double KBHorizontal = 0.0;
	public static double KBVertical = 0.0;
	public static int bgImage = 0;
	public static double itemSize = 1.0;
	public static boolean notificationModulesEnable = true;
	public static boolean notificationModulesDisable = false;
	public static boolean notificationModuleError = true;
	public static double savePosHeight = 3.0;
	public static boolean glideDmg = false;
	public static boolean onGroundSpoofFlight = false;
	public static boolean chestStealDelay = true;
	public static double autoMineBlockLimit = 13;
	public static boolean areaMineFastBreak = false;
	public static boolean flightCubecraftKillAnticheat = true;
	public static boolean Swank = true;
	public static boolean Swang = false;
	public static boolean Swong = false;
	public static boolean Swing = false;
	public static boolean bigWaterMark = false;
	public static boolean tabGui = false;
	public static boolean smoothAim = false;
	public static double smoothAimSpeed = 2.5;
	public static boolean viewBobbing = false;
	public static boolean flyBoost = false;

	public static void saveSettings() {
		JsonObject json = new JsonObject();
		for (Field field : ClientSettings.class.getFields()) {
			try {
				String toAdd = null;
				if (field.getType().getSimpleName().equals("boolean")) {
					boolean a = field.getBoolean(ClientSettings.class);
					json.addProperty(field.getName(), a);
				}
				if (field.getType().getSimpleName().equals("double")) {
					double a = field.getDouble(ClientSettings.class);
					json.addProperty(field.getName(), a);
				}
				if (field.getType().getSimpleName().equals("int")) {
					int a = field.getInt(ClientSettings.class);
					json.addProperty(field.getName(), a);
				}
				if (field.getType().getSimpleName().equals("float")) {
					float a = field.getFloat(ClientSettings.class);
					json.addProperty(field.getName(), a);
				}
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		try {
			FileWriter fw = new FileWriter(Xatz.getFileMananger().settingsDir);
			fw.write(json.toString().replaceAll("\\s", ""));
			fw.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void loadSettings() {
		JsonElement obj = null;
		try {
			FileReader reader = new FileReader(Xatz.getFileMananger().settingsDir);
			obj = new JsonParser().parse(reader);
			reader.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			return;
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (obj.isJsonNull()) {
			System.out.println("Json null");
			return;
		}
		if(obj.isJsonPrimitive()) {
			Xatz.getFileMananger().xatzDir.delete();
			Xatz.getNotificationManager().addNotification(new Notification(Level.ERROR, "All Xatz settings were reset to prevent crash."
					+ " If this message still pops up, please delete the 'Xatz' folder in .minecraft!"));
			return;
		}
		JsonObject json = (JsonObject) obj;
		for (Field field : ClientSettings.class.getFields()) {
			try {
				if (field.getType().getSimpleName().equals("boolean")) {
					field.set(ClientSettings.class, json.get(field.getName()).getAsBoolean());
				}
				if (field.getType().getSimpleName().equals("double")) {
					field.set(ClientSettings.class, json.get(field.getName()).getAsDouble());
				}
				if (field.getType().getSimpleName().equals("int")) {
					field.set(ClientSettings.class, json.get(field.getName()).getAsInt());
				}
				if (field.getType().getSimpleName().equals("float")) {
					field.set(ClientSettings.class, json.get(field.getName()).getAsFloat());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public static Color framebgColor1 = new Color(1f, 0.2f, 0.2f, 0.6f);
	public static Color frameHeadColor = new Color(1f, 0.3f, 0.3f, 1f);
	public static Color buttonSelectedColor = new Color(60, 60, 60, 200);
	public static Color guiBackgroundColor = new Color(50, 50, 50, 170);
	public static boolean invmanagerfood = true;


}
