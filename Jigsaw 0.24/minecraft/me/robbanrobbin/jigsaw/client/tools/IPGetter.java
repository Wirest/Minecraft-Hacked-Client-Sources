package me.robbanrobbin.jigsaw.client.tools;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class IPGetter {

	public static String getIpForPlayer(String name) throws NullPointerException {
		Document doc = null;
		try {
			doc = Jsoup.connect("http://mcresolver.pw/" + name)
					.userAgent("Mozilla/5.0 (Windows NT 5.1; rv:19.0) Gecko/20100101 Firefox/19.0").get();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (doc == null) {
			throw new NullPointerException("Could not obtain HTML document of " + "https://mcresolver.pw/" + name);
		}
		Elements elmts = doc.select("body > div > div.ips > div.list > ul");
		String ips = "";
		for (Element elmt : elmts) {
			if (elmt.children().isEmpty()) {
				return "No IPs found related to §c" + name;
			}
			String ip = elmt.child(0).text();
			if (ips.isEmpty()) {
				ips += ip;
			} else {
				ips += ", " + ip;
			}
		}
		return ips;
	}

}
