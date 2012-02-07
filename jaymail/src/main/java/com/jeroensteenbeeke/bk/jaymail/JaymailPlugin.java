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
package com.jeroensteenbeeke.bk.jaymail;

import java.util.List;
import java.util.logging.Logger;

import javax.persistence.PersistenceException;

import org.bukkit.entity.Player;

import com.jeroensteenbeeke.bk.basics.JSPlugin;
import com.jeroensteenbeeke.bk.basics.util.Messages;
import com.jeroensteenbeeke.bk.jaymail.commands.MailDeleteCommandHandler;
import com.jeroensteenbeeke.bk.jaymail.commands.MailListCommandHandler;
import com.jeroensteenbeeke.bk.jaymail.commands.MailSendCommandHandler;
import com.jeroensteenbeeke.bk.jaymail.commands.MailViewCommandHandler;
import com.jeroensteenbeeke.bk.jaymail.entities.JayMail;
import com.jeroensteenbeeke.bk.jaymail.listeners.MailLoginListener;

public class JaymailPlugin extends JSPlugin {
	private Logger logger = Logger.getLogger("Minecraft");

	public static final String PERMISSION_MAIL = "jaymail.mail";

	@Override
	public void onEnable() {
		logger.info("Enabled jaymail plugin");

		setupDatabase();

		addCommandHandler(new MailListCommandHandler(this));
		addCommandHandler(new MailViewCommandHandler(this));
		addCommandHandler(new MailSendCommandHandler(this));
		addCommandHandler(new MailDeleteCommandHandler(this));

		addListener(new MailLoginListener(this));

	}

	@Override
	public void onDisable() {

	}

	@Override
	public List<Class<?>> getDatabaseClasses() {

		List<Class<?>> classes = super.getDatabaseClasses();

		classes.add(JayMail.class);

		return classes;
	}

	private void setupDatabase() {
		try {
			getDatabase().find(JayMail.class).findRowCount();
		} catch (PersistenceException ex) {
			logger.info("Installing jaymail database");
			installDDL();
		}
	}

	public void viewMails(Player player) {
		List<JayMail> mails = getDatabase().find(JayMail.class).where()
				.eq("recipient", player.getName()).findList();

		Messages.send(player,
				String.format("&aYou have &e%s&a messages:", mails.size()));
		StringBuilder results = new StringBuilder();
		results.append("  ");
		for (JayMail mail : mails) {
			if (results.length() > 2) {
				results.append("&a, ");
			}
			results.append("&e");
			results.append(mail.getId());

		}
		Messages.send(player, results.toString());
	}
}
