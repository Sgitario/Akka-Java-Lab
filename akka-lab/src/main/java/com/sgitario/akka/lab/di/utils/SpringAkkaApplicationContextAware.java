package com.sgitario.akka.lab.di.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class SpringAkkaApplicationContextAware implements ApplicationContextAware {
	private ApplicationContext appContext;
	
	private ActorSystem system;
	
	public SpringAkkaApplicationContextAware(ActorSystem actorSystem) {
		this.system = actorSystem;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		appContext = applicationContext;
	}
	
	public ApplicationContext getApplicationContext() {
		return appContext;
	}
	
	public ActorSystem getSystem() {
		return system;
	}
	
	public ActorRef createCountingActor() {
		return system.actorOf(Props.create(
				SpringActorProducer.class, appContext, "CounterActor"));
	}
}
