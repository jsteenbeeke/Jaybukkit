package com.jeroensteenbeeke.bk.basics.util;

import java.security.MessageDigest;
import java.util.logging.Logger;

public final class HashUtil {
	private static final Logger logger = Logger.getLogger("Minecraft");

	private HashUtil() {

	}

	public static String sha1Hash(String input) {
		try {
			MessageDigest sha1Digest = MessageDigest.getInstance("SHA-1");
			sha1Digest.update(input.getBytes());
			return byteArrayToString(sha1Digest.digest());
		} catch (Exception e) {
			logger.severe(e.getMessage());
		}

		return "";
	}

	static String byteArrayToString(byte[] array) {
		StringBuilder builder = new StringBuilder();

		for (byte next : array) {
			byte lower = (byte) (next & 0x0F);

			byte higher = (byte) ((next & 0xF0) >>> 4);

			builder.append(getHexChar(higher));
			builder.append(getHexChar(lower));
		}

		return builder.toString();
	}

	static char getHexChar(byte convertible) {
		switch (convertible) {
		case 10:
			return 'a';
		case 11:
			return 'b';
		case 12:
			return 'c';
		case 13:
			return 'd';
		case 14:
			return 'e';
		case 15:
			return 'f';
		default:
			return Integer.toString(convertible).charAt(0);
		}
	}

}
