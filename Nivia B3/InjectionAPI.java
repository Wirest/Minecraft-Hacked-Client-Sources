import net.minecraft.client.main.Main;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.File;
import java.io.FileNotFoundException;

public class InjectionAPI {
	public static void inject() throws Exception {
		String userHome = System.getProperty("user.home", ".");
		File workingDirectory;
		switch (getPlatform()) {
		case LINUX:
			workingDirectory = new File(userHome, ".minecraft/");
			break;
		case WINDOWS:
			String applicationData = System.getenv("APPDATA");
			String folder = applicationData != null ? applicationData : userHome;
			workingDirectory = new File(folder, ".minecraft/");
			break;
		case MACOS:
			workingDirectory = new File(userHome, "Library/Application Support/minecraft");
			break;
		default:
			workingDirectory = new File(userHome, "minecraft/");
		}
		try {
			Main.main(new String[] { "--version", "Nivia Premium", "--accessToken", "0", "--assetIndex", "1.8",
					"--userProperties", "{}", "--gameDir", new File(workingDirectory, ".").getAbsolutePath(),
					"--assetsDir", new File(workingDirectory, "assets/").getAbsolutePath() });

		} catch (Exception e1) {
			try {
				PrintWriter writer = new PrintWriter("C:\\AntiLeak\\error.txt", "UTF-8");
				writer.println(e1);
				writer.close();
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
	}

	public static OS getPlatform() {
		String s = System.getProperty("os.name").toLowerCase();
		return s.contains("win") ? OS.WINDOWS
				: (s.contains("mac") ? OS.MACOS
						: (s.contains("solaris") ? OS.SOLARIS
								: (s.contains("sunos") ? OS.SOLARIS
										: (s.contains("linux") ? OS.LINUX
												: (s.contains("unix") ? OS.LINUX : OS.UNKNOWN)))));
	}

	public static enum OS {
		LINUX, SOLARIS, WINDOWS, MACOS, UNKNOWN;
	}
}