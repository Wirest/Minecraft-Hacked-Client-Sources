package me.robbanrobbin.jigsaw.client.modules;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.network.AbstractPacket;
import net.minecraft.network.play.server.S02PacketChat;

public class Admins extends Module {

	ArrayList<String> staff;

	public Admins() {
		super("Admins", Keyboard.KEY_NONE, Category.MISC,
				"Alerts you if a staffmember/important player joins your lobby");
	}

	@Override
	public void onClientLoad() {

		staff = new ArrayList<String>();
		addStaff();
		super.onClientLoad();
	}

	@Override
	public void onDisable() {

		super.onDisable();
	}

	@Override
	public void onEnable() {

		super.onEnable();
	}

	@Override
	public void onUpdate() {

		super.onUpdate();
	}

	@Override
	public void onPacketRecieved(AbstractPacket packetIn) {
		if (!(packetIn instanceof S02PacketChat)) {
			return;
		}
		String unformattedTextForChat = ((S02PacketChat) packetIn).getChatComponent().getUnformattedTextForChat();
		if (unformattedTextForChat.toUpperCase().indexOf("Join".toUpperCase()) != -1) {
			for (String name : staff) {
				if (unformattedTextForChat.toUpperCase().indexOf(name.toUpperCase()) != -1) {
					for (int i = 0; i < 10; i++) {
						Jigsaw.chatMessage("A staffmember/important player just joined!! (" + name + ")");
					}
					break;
				}
			}
		}
		super.onPacketRecieved(packetIn);
	}

	public void addStaff() {
		staff.add("1mao");
		staff.add("Adventist");
		staff.add("Anairda");
		staff.add("Arimzon");
		staff.add("BasicallyAnn");
		staff.add("BasicallyCombo");
		staff.add("Blazespot");
		staff.add("BudderStormMC");
		staff.add("Calamity9");
		staff.add("CardTrick");
		staff.add("Caveguyy");
		staff.add("ConnorHasPals");
		staff.add("CookieWoo");
		staff.add("Corzya");
		staff.add("CosmoLink");
		staff.add("Creath");
		staff.add("Crutan");
		staff.add("Dapherino");
		staff.add("Dominicide");
		staff.add("DraZZelxCaMZZ");
		staff.add("DWJosh");
		staff.add("EmeraldMiner233");
		staff.add("Eric_1337i");
		staff.add("evanmoney");
		staff.add("Evz");
		staff.add("hannahxxrose");
		staff.add("HoneyBully");
		staff.add("ilybabe");
		staff.add("ImPauLNotPaul");
		staff.add("KitanaTheMoo");
		staff.add("L0onitick");
		staff.add("lanadelxray");
		staff.add("Loxo");
		staff.add("Miksuu");
		staff.add("Mimic");
		staff.add("Musa");
		staff.add("MushroomCube");
		staff.add("NinjaXWaffles99");
		staff.add("nwang888");
		staff.add("Patmin");
		staff.add("Physicist_");
		staff.add("Poogry");
		staff.add("Pqblo");
		staff.add("R739");
		staff.add("Ramxirez");
		staff.add("Rebel_Guy");
		staff.add("Reconciliation");
		staff.add("RobotWizard_");
		staff.add("S4disticSheep");
		staff.add("Secrea");
		staff.add("ShadyPieLover");
		staff.add("Shedd");
		staff.add("ShesCute");
		staff.add("Soigne");
		staff.add("sourdog");
		staff.add("Sparrowking");
		staff.add("StoneColdKiller");
		staff.add("TheFatSanta");
		staff.add("Thejoester300");
		staff.add("TheNeonLazer");
		staff.add("Unique_Guy");
		staff.add("Valinax");
		staff.add("Valk");
		staff.add("Viewless");
		staff.add("Whif");
		staff.add("xCrusaderElf_IV");
		staff.add("xGravity");
		staff.add("xHero");
		staff.add("XML");
		staff.add("Y55");
		staff.add("YouGotRekt_By_Me");
		staff.add("YouLostYourLife");
		staff.add("Yunyii");
		staff.add("defek7");
		staff.add("Chiss");
		staff.add("sterling_");
		staff.add("AppleG");
		staff.add("B2_mp");
		staff.add("Strutt20");
		staff.add("Phinary");
		staff.add("RustyRoo");
		staff.add("Sigils");
		staff.add("syclonesjs");
		staff.add("captiansparklez");
		staff.add("Parker_Games");
		staff.add("BlueBeetleHD");
		staff.add("Diar");
		staff.add("Jarvis");
		staff.add("Relyh");
		staff.add("SamitoD");
		staff.add("ShinyRukii");
		staff.add("t3hero");
		staff.add("WebGlitch");
		staff.add("Benz");
		staff.add("epicbluej");

		staff.add("Impulse");

		staff.add("JunaidMC");

		staff.add("lolpaul");

		staff.add("Naidemoc");

		staff.add("Nogh25");

		staff.add("PaceOfCake");

		staff.add("Resistant");

		staff.add("SamitoD");

		staff.add("Spencer");

		staff.add("StefanAdR");

		staff.add("TaylorStyl");

		staff.add("_Pikachu");

		staff.add("aaiach");

		staff.add("BillNye");

		staff.add("Bunnni/B");

		staff.add("Giovanna");

		staff.add("Matamex");

		staff.add("PenguinHi5");

		staff.add("RealMC");

		staff.add("ShinyRukii");

		staff.add("TrueSquid");
		staff.add("Strutt20");

		staff.add("cherdy8s");

		staff.add("Esther");

		staff.add("mannalou");

		staff.add("Timmy");
		staff.add("AlconW");

		staff.add("Axegirl");

		staff.add("DuttyGilbe");

		staff.add("FriMat98");

		staff.add("Jarvis");

		staff.add("Nuclear_Po");

		staff.add("SaltyJenBo");

		staff.add("Wanderer");
		staff.add("Ashlyn");

		staff.add("BlueBeetle");

		staff.add("JackAddaw");

		staff.add("Matthew_");

		staff.add("PleaseTeam");

		staff.add("RavenWings");

		staff.add("starslays");

		staff.add("Toki");

		staff.add("xNaster");
		staff.add("Artix");

		staff.add("Artu_(Gol");

		staff.add("Destro");

		staff.add("I_Call_Hac");

		staff.add("Janda");

		staff.add("KFCGraphic");

		staff.add("NightWolfy");

		staff.add("Nouri");

		staff.add("Pr0pH3T123");

		staff.add("Relyh");

		staff.add("Smaland47");

		staff.add("Snow_Sparr");

		staff.add("Theminersc");

		staff.add("Vial");
		staff.add("CatchingFi");

		staff.add("Crumplex");

		staff.add("Dozzy");

		staff.add("Emiliee");

		staff.add("Esther");

		staff.add("Gaming_");

		staff.add("Harv");

		staff.add("Its3nder");

		staff.add("KiraBerry");

		staff.add("millenium2");

		staff.add("Notus");

		staff.add("TheDairyCo");

		staff.add("TheMegaTon");

		staff.add("WebGlitch");
		staff.add("AgentCorgi");

		staff.add("AquaTechMC");

		staff.add("CanYaNot");

		staff.add("Cookiez");

		staff.add("Crescent");

		staff.add("Dean&trade");

		staff.add("Diar");

		staff.add("FireStar89");

		staff.add("Geothermal");

		staff.add("metalwolf1");

		staff.add("mikelikesm");

		staff.add("Scharf");

		staff.add("SpookyUnic");

		staff.add("UntoldFury");

		staff.add("WowJay");

		staff.add("WowKay");
		staff.add("11studios");

		staff.add("5Doum");

		staff.add("6sin");

		staff.add("Adcm");

		staff.add("AddictiveP");

		staff.add("Adire");

		staff.add("Adriannaa|");

		staff.add("AeroxNZ");

		staff.add("Alex|Rac");

		staff.add("AlexTheBlo");

		staff.add("alfi3003");

		staff.add("AlphaAbsol");

		staff.add("amyisfabul");

		staff.add("Announceme");

		staff.add("Anzu");

		staff.add("ASTChristo");

		staff.add("Aussie");

		staff.add("Awquard|");

		staff.add("Azezal");

		staff.add("A_Y_Y_Y_L_");

		staff.add("Beta_1123");

		staff.add("BlackAceSt");

		staff.add("boomerzap");

		staff.add("BornAssass");

		staff.add("BTSInDarkn");

		staff.add("BurpingBob");

		staff.add("Caleb-Sk");

		staff.add("Castr0wild");

		staff.add("Charcoal_I");

		staff.add("ChrisTheKi");

		staff.add("CMod|Whi");

		staff.add("CombatMedi");

		staff.add("Cosmic_");

		staff.add("craftertha");

		staff.add("Craftmanph");

		staff.add("cyber700");

		staff.add("Cyrexal");

		staff.add("daJavaCup");

		staff.add("Dani3lblu3");

		staff.add("DannyDog");

		staff.add("DanyStormB");

		staff.add("Detemmined");

		staff.add("DJ2470");

		staff.add("djnsmd");

		staff.add("Dogester");

		staff.add("DragonBull");

		staff.add("Drifteh");

		staff.add("DWJosh");

		staff.add("edgardme3");

		staff.add("Elmo");

		staff.add("ErgWantsCh");

		staff.add("EricTheFox");

		staff.add("Ery");

		staff.add("Evilbartje");

		staff.add("Exclusive_");

		staff.add("f1racing6");

		staff.add("felix12200");

		staff.add("Fenriselfi");

		staff.add("fertiletur");

		staff.add("Fetch");

		staff.add("FlippinPig");

		staff.add("flyingplat");

		staff.add("Gamil");

		staff.add("GarrettThe");

		staff.add("GetLow");

		staff.add("Ghost10000");

		staff.add("glop?ie");

		staff.add("GoldenHawk");

		staff.add("GoldSeb");

		staff.add("graza");

		staff.add("GreenTiger");

		staff.add("Hastingscr");

		staff.add("hazeae24");

		staff.add("Heavens");

		staff.add("heyyoitsja");

		staff.add("HOME_Slice");

		staff.add("IAmADiamon");

		staff.add("IDONTHAVE1");

		staff.add("IfYouEver");

		staff.add("iMinecraft");

		staff.add("ImJustKind");

		staff.add("Inertia");

		staff.add("Infinate50");

		staff.add("iPBJSammic");

		staff.add("ItsCrit");

		staff.add("Ivanss");

		staff.add("Ivqn|Iva");

		staff.add("IzeyFro");

		staff.add("j0jo");

		staff.add("Jake861");

		staff.add("Jalen3zx");

		staff.add("JaneLane_");

		staff.add("JanelleIAm");

		staff.add("Jeanned");

		staff.add("Jimbo487");

		staff.add("JimmyCraft");

		staff.add("Joeytemast");

		staff.add("JonocraftF");

		staff.add("Josiah");

		staff.add("Jp78");

		staff.add("jtonpc");

		staff.add("Judemsx(B");

		staff.add("Jumpydeerb");

		staff.add("Kaitlynn_");

		staff.add("Kanebridge");

		staff.add("kathytheca");

		staff.add("Katnip");

		staff.add("Kcvin");

		staff.add("KevinBoba");

		staff.add("Killa");

		staff.add("KingCrazy_");

		staff.add("KingMC");

		staff.add("Ksoai");

		staff.add("Kurbay|K");

		staff.add("LeonhartGa");

		staff.add("lilcampoo");

		staff.add("LilySnape");

		staff.add("LinkdaLege");

		staff.add("linnyf54");

		staff.add("LordBoogl");

		staff.add("Lpnt-ElP");

		staff.add("MCluvgames");

		staff.add("MCrusherM");

		staff.add("mepman9");

		staff.add("MightyDuck");

		staff.add("mishymolly");

		staff.add("Mistr_");

		staff.add("MjukOst");

		staff.add("Monet9");

		staff.add("Monkeyhead");

		staff.add("MooCowIsaa");

		staff.add("mrcrazydud");

		staff.add("notmonkey");

		staff.add("nsjp");

		staff.add("ntmsmb");

		staff.add("OakLuminar");

		staff.add("Orebit");

		staff.add("OtherSwimm");

		staff.add("Pabulous");

		staff.add("Pamphlet");

		staff.add("Persis");

		staff.add("Phil");

		staff.add("Phoenix");

		staff.add("pikachomp");

		staff.add("PizzaMan31");

		staff.add("Plobnob");

		staff.add("Pluto1099");

		staff.add("PowerCraft");

		staff.add("Primal_For");

		staff.add("PrincessLi");

		staff.add("Prismatic_");

		staff.add("protex08");

		staff.add("ProtonOran");

		staff.add("Pupserpwn");

		staff.add("purpleswor");

		staff.add("PyxelVirus");

		staff.add("QuantumCra");

		staff.add("QuietProfe");

		staff.add("Rediceberg");

		staff.add("RedXTech");

		staff.add("RenegadeWa");

		staff.add("ronansays");

		staff.add("RossStill");

		staff.add("Ryan???");

		staff.add("RyanTheMlg");

		staff.add("SandersFor");

		staff.add("Satoria");

		staff.add("SavGhost");

		staff.add("Seety|xC");

		staff.add("Shadowfox3");

		staff.add("SheSoCooki");

		staff.add("Shipopi");

		staff.add("Silica");

		staff.add("SirPat");

		staff.add("SirSugar");

		staff.add("SlyD0g");

		staff.add("Snowflake_");

		staff.add("Soigne");

		staff.add("SoleFern");

		staff.add("SophiaRose");

		staff.add("Spencer99a");

		staff.add("Squabbles");

		staff.add("Squow&bul");

		staff.add("St3ven");

		staff.add("Summmit");

		staff.add("Suspici0us");

		staff.add("Swimmer_");

		staff.add("TangyChees");

		staff.add("TheChessMa");

		staff.add("TheIronLov");

		staff.add("TheMouldyP");

		staff.add("TheUnknown");

		staff.add("Thom");

		staff.add("TimmyTam");

		staff.add("TobitheG");

		staff.add("Tomp");

		staff.add("Tompers");

		staff.add("Tort");

		staff.add("TotalMvp");

		staff.add("TruePengui");

		staff.add("Uknowit");

		staff.add("Ulfur");

		staff.add("UltraCat");

		staff.add("Undefined&");

		staff.add("Vhisper");

		staff.add("Virtual_Wo");

		staff.add("Whif");

		staff.add("WhiteLionQ");

		staff.add("will177");

		staff.add("WillFunkyT");

		staff.add("xChickenNu");

		staff.add("Xeroxie");

		staff.add("xFantaa");

		staff.add("xLouis");

		staff.add("xMini");

		staff.add("Y0U_G0T_R3");

		staff.add("Yash");

		staff.add("ZacGamesMC");

		staff.add("zaidynzm95");

		staff.add("zdemon98");

		staff.add("Zepyth");

		staff.add("Zombo_");

		staff.add("0____0");

		staff.add("AbbyPc");

		staff.add("Benjaboot");

		staff.add("Ben_Loe");

		staff.add("Bowie");

		staff.add("Briskers");

		staff.add("Briyla");

		staff.add("CallMeCass");

		staff.add("cavs02");

		staff.add("ChibiCryst");

		staff.add("Cobruh_");

		staff.add("coolrach");

		staff.add("DeanBeanba");

		staff.add("Fireflydes");

		staff.add("Fluffy_End");

		staff.add("FoggyIO");

		staff.add("FullyCanad");

		staff.add("GangstaCat");

		staff.add("Ghast_Blas");

		staff.add("HiddenPixe");

		staff.add("Horizon_");

		staff.add("HybridStra");

		staff.add("ItsKassy");

		staff.add("Jason_Keno");

		staff.add("JessiieeBa");

		staff.add("JJCunningC");

		staff.add("Jorjie");

		staff.add("JustZerooo");

		staff.add("Katniss");

		staff.add("LAQURED");

		staff.add("Lchogies");

		staff.add("LeAragog");

		staff.add("LukeAteDal");

		staff.add("Manning");

		staff.add("Mirthless");

		staff.add("MythicalCa");

		staff.add("NinjaKoopa");

		staff.add("PegasusOfL");

		staff.add("Phamtastic");

		staff.add("PixelHeart");

		staff.add("Poker1st");

		staff.add("Raech|Ra");

		staff.add("Scraptonix");

		staff.add("Semisonic");

		staff.add("SimplyBran");

		staff.add("soccerstri");

		staff.add("Sonic_Nigh");

		staff.add("SquidletHD");

		staff.add("StormRazer");

		staff.add("Stryze");

		staff.add("TheAssassi");

		staff.add("TheKareBea");

		staff.add("TheOnlyOll");

		staff.add("TheThunder");

		staff.add("Tropical_M");

		staff.add("Vanessa137");

		staff.add("Wacky");

		staff.add("WowPepsil");

		staff.add("xAbbiex");

		staff.add("xConstrain");

		staff.add("xEni_");

		staff.add("xMinteh");

		staff.add("xSoldier");

		staff.add("xZach_");

		staff.add("Zannieh_");

		staff.add("ZigaTheKid");

		staff.add("_MirrorImage");
	}

}
