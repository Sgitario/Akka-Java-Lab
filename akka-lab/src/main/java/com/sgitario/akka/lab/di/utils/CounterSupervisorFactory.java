package com.sgitario.akka.lab.di.utils;

import java.util.List;

import akka.actor.ActorRef;
import akka.actor.Props;

public class CounterSupervisorFactory {

	private SpringAkkaApplicationContextAware context;
	private List<ActorRef> counters;

	public CounterSupervisorFactory(SpringAkkaApplicationContextAware context, List<ActorRef> counters) {
		this.context = context;
		this.counters = counters;
	}

	public ActorRef createCountingSupervisor() {		
		return this.context.getSystem().actorOf(
				Props.create(SpringActorProducer.class,
						this.context.getApplicationContext(), "SupervisorCounterActor", counters));
	}

}
