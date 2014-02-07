package com.sgitario.akka.lab.di.services;

import org.springframework.stereotype.Service;

@Service
public class CountingServiceImpl implements CountingService {
	public int increment(int count) {
		return count + 1;
	}
}
