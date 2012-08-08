package com.jeroensteenbeeke.bk.jaylock.commands;

public class UnsuitableLocationException extends Exception {
	private static final long serialVersionUID = 1L;

	public UnsuitableLocationException(String message) {
		super(message);
	}
}
