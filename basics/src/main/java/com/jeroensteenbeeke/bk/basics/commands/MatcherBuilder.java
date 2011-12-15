package com.jeroensteenbeeke.bk.basics.commands;

public interface MatcherBuilder {

	MatcherBuilder andNameIs(String name);

	MatcherBuilder andArgIs(int index, String value);

	MatcherBuilder andArgLike(int index, String regex);

	CommandMatcher itMatches();

}