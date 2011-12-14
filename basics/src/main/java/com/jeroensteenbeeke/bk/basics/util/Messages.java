package com.jeroensteenbeeke.bk.basics.util;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Server;
import org.bukkit.command.CommandSender;

public final class Messages {
	private static final int MAX_LENGTH = 119;

	private static final Logger log = Logger.getLogger("Minecraft");

	private Messages() {
	}

	public static void send(CommandSender sender, String message) {

		List<String> messages = split(message);

		for (String msg : messages) {
			String m = msg.replaceAll("&", "\u00A7") + "\u00A7f";
			sender.sendMessage(m);
			log.info(String.format("To %s: %s", sender.getName(), m));
		}

	}

	public static void broadcast(Server server, String message) {
		List<String> messages = split(message);

		for (String msg : messages) {
			String m = msg.replaceAll("&", "\u00A7") + "\u00A7f";
			server.broadcastMessage(m);
		}
	}

	private static List<String> split(String message) {
		List<String> result = new LinkedList<String>();
		String[] allParts = message.split(" ");

		StringBuilder current = new StringBuilder();
		for (int i = 0; i < allParts.length; i++) {
			int newLen = current.length() + allParts[i].length();

			if (newLen <= MAX_LENGTH) {
				current.append(" ");
				current.append(allParts[i]);
			} else {
				result.add(current.toString());
				current = new StringBuilder();
				current.append(allParts[i]);
			}
		}

		if (current.length() > 0) {
			result.add(current.toString());
		}

		return result;
	}

}
