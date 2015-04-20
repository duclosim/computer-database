package com.excilys.persistence;

public class PersistenceException extends RuntimeException {
	private static final long serialVersionUID = -6677597838589002665L;

	public PersistenceException() {
		super();
	}

    public PersistenceException(String message) {
		super(message);
	}

}
