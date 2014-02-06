package com.sgitario.akka.lab.clustering.utils;

import java.io.Serializable;

public class Command implements Serializable {
	private static final long serialVersionUID = 1L;
	private final String text;
	
	public Command(String text) {
		this.text = text;
	}
	
	public String getText() {
		return text;
	}
}
