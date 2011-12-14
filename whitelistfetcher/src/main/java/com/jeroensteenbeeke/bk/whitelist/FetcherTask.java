package com.jeroensteenbeeke.bk.whitelist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.OfflinePlayer;

import com.jeroensteenbeeke.bk.basics.util.Messages;

public class FetcherTask implements Runnable {
	private Logger log = Logger.getLogger("Minecraft");

	private final WhitelistFetcher plugin;
	private final String url;

	public FetcherTask(WhitelistFetcher plugin, String url) {
		this.plugin = plugin;
		this.url = url;
	}

	@Override
	public void run() {
		try {
			URLConnection conn = new URL(url).openConnection();

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));

			List<String> newWhiteList = new LinkedList<String>();

			String next = null;

			while ((next = reader.readLine()) != null) {
				newWhiteList.add(next);
			}

			for (OfflinePlayer op : plugin.getServer().getWhitelistedPlayers()) {
				op.setWhitelisted(false);
			}

			for (String playerName : newWhiteList) {
				OfflinePlayer player = plugin.getServer().getOfflinePlayer(
						playerName);
				player.setWhitelisted(true);
			}

			Messages.broadcast(plugin.getServer(), "&aWhitelist updated");
		} catch (IOException e) {
			Messages.broadcast(plugin.getServer(),
					"&cFailed to fetch whitelist");
			log.log(Level.SEVERE, e.getMessage(), e);
		}

	}
}
