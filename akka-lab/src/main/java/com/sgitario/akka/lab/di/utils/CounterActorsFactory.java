package com.sgitario.akka.lab.di.utils;

import akka.actor.ActorRef;
import akka.actor.Props;

public class CounterActorsFactory {
	
	private SpringAkkaApplicationContextAware context;
	
	public CounterActorsFactory(SpringAkkaApplicationContextAware context) {
		this.context = context;
	}
	
	public ActorRef createCountingActor() {
		return this.context.getSystem().actorOf(Props.create(
				SpringActorProducer.class, this.context.getApplicationContext(), "CounterActor"));
	}
}
