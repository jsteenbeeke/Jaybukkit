package com.jeroensteenbeeke.bk.sitestats;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.logging.Logger;

import org.bukkit.entity.Player;

import com.jeroensteenbeeke.bk.basics.util.HashUtil;

public class UpdateSiteTask implements Runnable {
	private final Logger logger = Logger.getLogger("Minecraft");

	private final SiteStats siteStats;

	public UpdateSiteTask(SiteStats siteStats) {
		this.siteStats = siteStats;
	}

	@Override
	public void run() {
		try {

			Player[] players = siteStats.getServer().getOnlinePlayers();
			StringBuilder q = new StringBuilder();

			int i = 0;

			for (Player p : players) {
				if (q.length() > 0) {
					q.append('&');
				}
				q.append('p');
				q.append(i++);
				q.append('=');
				q.append(URLEncoder.encode(p.getName(), "UTF-8"));
			}

			String key = siteStats.getConfig().getString("key");

			String baseUrl = siteStats.getConfig().getString("targetUrl");

			StringBuilder query = new StringBuilder();
			query.append(baseUrl);
			query.append('?');
			query.append(q);
			if (q.length() > 0) {
				query.append("&");
			}
			query.append("verify=");
			query.append(HashUtil.sha1Hash(key + '!' + q.toString()));

			URL url = new URL(query.toString());
			URLConnection connection = url.openConnection();

			StringBuilder content = new StringBuilder();

			BufferedReader br = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));

			String next;

			while ((next = br.readLine()) != null) {
				content.append(next);
			}

			if ("OK".equals(content.toString())) {
				logger.fine(String.format("Updated listing at %s", baseUrl));
			} else if ("FAIL".equals(content.toString())) {
				logger.severe("Update failed: invalid checksum");
			} else {
				logger.severe("Update failed: " + content);
			}

		} catch (Exception e) {
			logger.severe("Exception trying to push users online to site: "
					+ e.getMessage());
		}

	}
}
