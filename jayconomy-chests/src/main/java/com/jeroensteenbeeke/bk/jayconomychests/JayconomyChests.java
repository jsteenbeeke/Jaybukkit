package com.jeroensteenbeeke.bk.jayconomychests;

import java.util.regex.Pattern;

import org.bukkit.block.Sign;

import com.jeroensteenbeeke.bk.basics.util.SignMatcher;
import com.jeroensteenbeeke.bk.jayconomy.Jayconomy;
import com.jeroensteenbeeke.bk.jayconomy.JayconomyAwarePlugin;

public class JayconomyChests extends JayconomyAwarePlugin {
	private Jayconomy jayconomy;

	static final Pattern PATTERN_DOLLAR = Pattern
			.compile("^\\$\\s*(\\d+(\\.\\d+)?)$");

	private final SignMatcher matcher;

	public JayconomyChests() {
		this.matcher = SignMatcher.whereEquals(0, "[shop]")
				.andValidPlayerName(1)
				.andMatches(2, "^\\$\\s*(\\d+(\\.\\d+)?)$").andEmpty(3)
				.create();
	}

	@Override
	public void onJayconomyInitialized(Jayconomy jayconomy) {
		this.jayconomy = jayconomy;

	}

	@Override
	public void onDisable() {

	}

	public boolean isJayconomyChestSign(Sign sign) {
		return matcher.matches(sign);
	}
}
