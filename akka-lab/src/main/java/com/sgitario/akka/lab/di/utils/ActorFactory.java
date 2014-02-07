package com.sgitario.akka.lab.di.utils;

import akka.actor.ActorSystem;

public class ActorFactory {
	public ActorSystem createActorSystem() {
		return ActorSystem.create("dependencyInjection");
	}
}
