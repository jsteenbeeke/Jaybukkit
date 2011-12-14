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
		String folderFilename = System.getProperty("users.file");

		users = new Properties();
		try {
			if (folderFilename != null) {
				users.loadFromXML(new FileInputStream(folderFilename));
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
