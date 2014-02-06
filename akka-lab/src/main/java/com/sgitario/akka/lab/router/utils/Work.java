package com.sgitario.akka.lab.router.utils;

import java.io.Serializable;

public class Work implements Serializable {
	private static final long serialVersionUID = 1L;
	public final String payload;

	public Work(String payload) {
		this.payload = payload;
	}
}
