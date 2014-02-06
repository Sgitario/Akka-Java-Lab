package com.sgitario.akka.lab.di.services;

import javax.inject.Named;

@Named("CountingService")
public class CountingService {
	public int increment(int count) {
		return count + 1;
	}
}
