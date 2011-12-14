/**
 * This file is part of Jaybukkit.
 *
 * Jaybukkit is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Jaybukkit is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Jaybukkit.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.jeroensteenbeeke.bk.playerbasics.commands;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.jeroensteenbeeke.bk.basics.commands.CommandHandler;
import com.jeroensteenbeeke.bk.basics.util.Messages;
import com.jeroensteenbeeke.bk.playerbasics.PlayerBasics;

public class RulesHandler implements CommandHandler {

	private List<String> rules;

	@SuppressWarnings("deprecation")
	public RulesHandler(PlayerBasics plugin) {
		rules = new LinkedList<String>();
		String ruleConfig = plugin.getConfiguration().getString("rules", "");
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < ruleConfig.length(); i++) {
			char next = ruleConfig.charAt(i);
			if (next == '-') {
				if (builder.length() > 0) {
					rules.add(builder.toString());
				}
				builder = new StringBuilder();
			} else {
				builder.append(next);
			}
		}

		if (builder.length() > 0) {
			rules.add(builder.toString());
		}

	}

	@Override
	public boolean matches(Command command, String[] args) {
		return "rules".equals(command.getName());
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (sender
				.hasPermission(com.jeroensteenbeeke.bk.playerbasics.PlayerBasics.PERMISSION_RULES)) {
			Messages.send(sender, "&1Server rules: ");
			for (String rule : rules) {
				Messages.send(sender, "  &1- &e" + rule);
			}

			return true;
		} else {
			sender.sendMessage("\u00A7cYou do not have permission to use this command\u00A7f");
			return true;
		}
	}

}
