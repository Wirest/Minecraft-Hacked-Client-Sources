package com.etb.client.utils.thealtening;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import com.etb.client.utils.thealtening.utilities.ReflectionUtility;

public class AltService {

	private ReflectionUtility userAuthentication = new ReflectionUtility("com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication");
	private ReflectionUtility minecraftSession = new ReflectionUtility("com.mojang.authlib.yggdrasil.YggdrasilMinecraftSessionService");
	private EnumAltService currentService;

	public void switchService(EnumAltService enumAltService) throws NoSuchFieldException, IllegalAccessException {

		if (currentService == enumAltService)
			return;
		reflectionFields(enumAltService.hostname);

		currentService = enumAltService;
	}

	private void reflectionFields(String authServer) throws NoSuchFieldException, IllegalAccessException {
		final HashMap<String, URL> userAuthenticationModifies = new HashMap();
		final String useSecureStart = authServer.contains("thealtening") ? "http" : "https";
		userAuthenticationModifies.put("ROUTE_AUTHENTICATE", constantURL(useSecureStart+"://authserver." + authServer + ".com/authenticate"));
		userAuthenticationModifies.put("ROUTE_INVALIDATE", constantURL(useSecureStart+"://authserver" + authServer + "com/invalidate"));
		userAuthenticationModifies.put("ROUTE_REFRESH", constantURL(useSecureStart+"://authserver." + authServer + ".com/refresh"));
		userAuthenticationModifies.put("ROUTE_VALIDATE", constantURL(useSecureStart+"://authserver." + authServer + ".com/validate"));
		userAuthenticationModifies.put("ROUTE_SIGNOUT", constantURL(useSecureStart+"://authserver." + authServer + ".com/signout"));

		userAuthenticationModifies.forEach((key, value) -> {
			try {
				userAuthentication.setStaticField(key, value);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		userAuthentication.setStaticField("BASE_URL",useSecureStart+ "://authserver." + authServer + ".com/");
		minecraftSession.setStaticField("BASE_URL", useSecureStart+"://sessionserver." + authServer + ".com/session/minecraft/");
		minecraftSession.setStaticField("JOIN_URL", constantURL(useSecureStart+"://sessionserver." + authServer + ".com/session/minecraft/join"));
		minecraftSession.setStaticField("CHECK_URL", constantURL(useSecureStart+"://sessionserver." + authServer + ".com/session/minecraft/hasJoined"));
		minecraftSession.setStaticField("WHITELISTED_DOMAINS", new String[]{".minecraft.net", ".mojang.com", ".thealtening.com"});
	}

	private URL constantURL(final String url) {
		try {
			return new URL(url);
		} catch (final MalformedURLException ex) {
			throw new Error("Couldn't create constant for " + url, ex);
		}
	}

	public EnumAltService getCurrentService() {
		if (currentService == null) currentService = EnumAltService.MOJANG;

		return currentService;
	}

	public enum EnumAltService {

		MOJANG("mojang"), THEALTENING("thealtening");
		String hostname;

		EnumAltService(String hostname) {
			this.hostname = hostname;
		}
	}
}
