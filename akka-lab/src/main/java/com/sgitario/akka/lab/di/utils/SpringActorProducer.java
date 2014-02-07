package com.sgitario.akka.lab.di.utils;

import java.util.ArrayList;

import akka.actor.Actor;
import akka.actor.ActorRef;
import akka.actor.IndirectActorProducer;

import org.springframework.context.ApplicationContext;

/**
 * An actor producer that lets Spring create the Actor instances.
 */
public class SpringActorProducer implements IndirectActorProducer {
	private final ApplicationContext applicationContext;
	private final String actorBeanName;
	private ArrayList<ActorRef> args;
	
	public SpringActorProducer(ApplicationContext applicationContext,
			String actorBeanName, ArrayList<ActorRef> actors) {
		this(applicationContext, actorBeanName);
		this.args = actors;
	}
	
	public SpringActorProducer(ApplicationContext applicationContext,
			String actorBeanName) {
		this.applicationContext = applicationContext;
		this.actorBeanName = actorBeanName;
	}

	@Override
	public Actor produce() {
		Actor actor = null;
		if (args != null) {
			actor = (Actor) applicationContext.getBean(actorBeanName, args);
		} else {
			actor = (Actor) applicationContext.getBean(actorBeanName);
		}
		
		return actor;
	}

	@Override
	public Class<? extends Actor> actorClass() {
		return (Class<? extends Actor>) applicationContext
				.getType(actorBeanName);
	}
}