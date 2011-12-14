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
package com.jeroensteenbeeke.carrier.receiver;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

public enum TokenRepository {
	INST;

	private final static int REQUIRED_SIZE = 40;

	private Logger log = LoggerFactory.getLogger(TokenRepository.class);

	private Properties users;

	private Map<String, String> tokens;

	private Random rnd = new Random();

	private TokenRepository() {
		String userFileName = System.getProperty("users.file");

		users = new Properties();
		try {
			if (userFileName != null) {
				users.loadFromXML(new FileInputStream(userFileName));
			} else {
				log.error("NO USER FILE INDICATED! ALL WEB SERVICE CALLS WILL FAIL!");
				log.error("Please start this server with -Dusers.file=/location/of/user/file");
			}
		} catch (IOException ioe) {
			log.error("Failed to load users from properties");
		}

		tokens = Maps.newTreeMap();
	}

	public String generateToken(String username) {

		StringBuilder tokenBuilder = new StringBuilder();
		for (int i = 0; i < REQUIRED_SIZE; i++) {
			tokenBuilder.append(getRandomChar());
		}

		String token = tokenBuilder.toString();

		if (users.containsKey(username)) {
			tokens.put(token, username);
		}

		return token;
	}

	public boolean verifySignature(String token, String subject,
			String signature) {
		if (tokens.containsKey(token)) {
			String username = tokens.get(token);

			if (users.containsKey(username)) {
				String expected = getExpectedSignature(token, subject,
						users.getProperty(username));

				tokens.remove(token);

				return signature.equals(expected);
			}

		}

		return false;
	}

	private String getExpectedSignature(String token, String subject,
			String password) {
		StringBuilder input = new StringBuilder();
		input.append(token);
		input.append('!');
		input.append(subject);
		input.append('!');
		input.append(password);

		return HashUtil.sha1Hash(input.toString());
	}

	private char getRandomChar() {
		boolean alpha = rnd.nextBoolean();

		if (alpha) {
			return (char) (rnd.nextInt(6) + 97);
		} else {
			return (char) (rnd.nextInt(10) + 48);
		}
	}
}
