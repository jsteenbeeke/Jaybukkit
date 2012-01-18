package com.jeroensteenbeeke.bk.jayop.commands;

import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.jeroensteenbeeke.bk.basics.commands.CommandMatcher;
import com.jeroensteenbeeke.bk.basics.commands.ParameterIntegrityChecker;
import com.jeroensteenbeeke.bk.basics.commands.PermissibleCommandHandler;
import com.jeroensteenbeeke.bk.basics.util.Messages;
import com.jeroensteenbeeke.bk.jayop.JayOp;

public class ListFrozenCommandHandler extends PermissibleCommandHandler {

	private JayOp jayop;

	public ListFrozenCommandHandler(JayOp jayop) {
		super(JayOp.PERMISSION_ENFORCEMENT);
		this.jayop = jayop;
	}

	@Override
	public CommandMatcher getMatcher() {
		return ifNameIs("listfrozen").itMatches();
	}

	@Override
	public ParameterIntegrityChecker getParameterChecker() {
		return ifArgCountIs(0).itIsProper();
	}

	@Override
	public void onAuthorized(CommandSender sender, Command command,
			String label, String[] args) {
		StringBuilder online = new StringBuilder();
		online.append("&fThe following online players are &cfrozen&f: [");
		online.append(compilePlayerList(jayop.getServer(), jayop.getFrozen()));
		online.append("&f]");

		Messages.send(sender, online.toString());

	}

	public String compilePlayerList(Server server, Set<String> frozen) {
		StringBuilder builder = new StringBuilder();

		int i = 0;

		for (String pname : frozen) {
			Player player = server.getPlayerExact(pname);

			if (i++ > 0) {
				builder.append("&f, ");
			}
			builder.append(getColoredPlayerName(player));
		}

		return builder.toString();
	}

	private static String getColoredPlayerName(Player player) {
		for (ChatColor color : ChatColor.values()) {
			if (player
					.hasPermission("colornames." + color.name().toLowerCase())) {
				return color.toString() + player.getDisplayName();
			}
		}

		return player.getName();
	}
}
