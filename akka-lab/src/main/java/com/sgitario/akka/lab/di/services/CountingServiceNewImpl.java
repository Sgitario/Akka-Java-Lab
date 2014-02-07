package com.sgitario.akka.lab.di.services;

public class CountingServiceNewImpl implements CountingService {
	public int increment(int count) {
		return count + 2;
	}
}
