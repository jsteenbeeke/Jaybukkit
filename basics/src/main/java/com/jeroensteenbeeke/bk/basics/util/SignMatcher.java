package com.jeroensteenbeeke.bk.basics.util;

import java.util.List;
import java.util.regex.Pattern;

import org.bukkit.block.Sign;

import com.google.common.collect.Lists;

public final class SignMatcher {

	private final List<SignMatcherRule> rules;

	private SignMatcher(List<SignMatcherRule> rules) {
		this.rules = rules;
	}

	public boolean matches(Sign sign) {
		for (SignMatcherRule rule : rules) {
			int line = rule.getLine();

			if (line < 0 || line > 3)
				return false;

			if (!rule.matches(sign.getLine(line)))
				return false;
		}

		return true;
	}

	public static SignMatcherBuilder whereEquals(int line, String equals) {
		return new SignMatcherBuilder().andEquals(line, equals);
	}

	public static SignMatcherBuilder whereEqualsIgnoreCase(int line,
			String equals) {
		return new SignMatcherBuilder().andEqualsIgnoreCase(line, equals);
	}

	public static SignMatcherBuilder whereMatches(int line, String pattern) {
		return new SignMatcherBuilder().andMatches(line, pattern);
	}

	public static SignMatcherBuilder whereEmpty(int line) {
		return new SignMatcherBuilder().andEmpty(line);
	}

	private static interface SignMatcherRule {
		int getLine();

		boolean matches(String input);
	}

	private static abstract class DefaultSignMatcherRule implements
			SignMatcherRule {
		private final int line;

		public DefaultSignMatcherRule(int line) {
			super();
			this.line = line;
		}

		@Override
		public int getLine() {
			return line;
		}
	}

	private static class EqualityRule extends DefaultSignMatcherRule {
		private final String expected;

		private EqualityRule(int line, String expected) {
			super(line);
			this.expected = expected;
		}

		@Override
		public boolean matches(String input) {
			return expected != null && expected.equals(input);
		}
	}

	private static class CaseInsensitiveEqualityRule extends
			DefaultSignMatcherRule {
		private final String expected;

		private CaseInsensitiveEqualityRule(int line, String expected) {
			super(line);
			this.expected = expected;
		}

		@Override
		public boolean matches(String input) {
			return expected != null && expected.equalsIgnoreCase(input);
		}
	}

	private static class MatcherRule extends DefaultSignMatcherRule {
		private final Pattern pattern;

		private MatcherRule(int line, String pattern) {
			super(line);
			this.pattern = Pattern.compile(pattern);
		}

		@Override
		public boolean matches(String input) {
			return pattern.matcher(input).matches();
		}
	}

	private static class EmptyRule extends DefaultSignMatcherRule {

		private EmptyRule(int line) {
			super(line);
		}

		@Override
		public boolean matches(String input) {

			return input == null || input.trim().isEmpty();
		}

	}

	public static class SignMatcherBuilder {
		private final List<SignMatcherRule> rules;

		private SignMatcherBuilder() {
			this.rules = Lists.newLinkedList();
		}

		public SignMatcherBuilder andEmpty(int line) {
			rules.add(new EmptyRule(line));
			return this;
		}

		public SignMatcherBuilder andEquals(int line, String equals) {
			rules.add(new EqualityRule(line, equals));
			return this;
		}

		public SignMatcherBuilder andEqualsIgnoreCase(int line,
				String equals) {
			rules.add(new CaseInsensitiveEqualityRule(line, equals));
			return this;
		}

		public SignMatcherBuilder andMatches(int line, String pattern) {
			rules.add(new MatcherRule(line, pattern));
			return this;
		}

		public SignMatcherBuilder andValidPlayerName(int line) {
			return andMatches(line, "^[a-zA-Z0-9_]+$");
		}

		public SignMatcher create() {
			return new SignMatcher(rules);
		}

	}
}
