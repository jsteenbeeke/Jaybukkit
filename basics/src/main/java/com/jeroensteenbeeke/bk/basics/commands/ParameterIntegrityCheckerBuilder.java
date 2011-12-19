package com.jeroensteenbeeke.bk.basics.commands;

public interface ParameterIntegrityCheckerBuilder {
	/**
	 * @deprecated In most cases you do not want one command handling multiple
	 *             argument lengths
	 */
	@Deprecated
	ParameterIntegrityCheckerBuilder andArgCountAtMost(int count);

	ParameterIntegrityCheckerBuilder andArgumentEquals(int index,
			String... possibleValues);

	/**
	 * @deprecated In most cases you do not want one command handling multiple
	 *             argument lengths
	 */
	@Deprecated
	ParameterIntegrityCheckerBuilder andArgumentEqualsIfExists(int index,
			String... possibleValues);

	ParameterIntegrityCheckerBuilder andArgumentLike(int index,
			String... possibleRegex);

	/**
	 * @deprecated In most cases you do not want one command handling multiple
	 *             argument lengths
	 */
	@Deprecated
	ParameterIntegrityCheckerBuilder andArgumentLikeIfExists(int index,
			String... possibleRegex);

	ParameterIntegrityCheckerBuilder andArgumentIsValidPlayerName(int index);

	ParameterIntegrityChecker itIsProper();
}
