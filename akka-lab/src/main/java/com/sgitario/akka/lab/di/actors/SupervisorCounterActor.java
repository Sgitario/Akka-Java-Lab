package com.sgitario.akka.lab.di.actors;

import static akka.actor.SupervisorStrategy.escalate;
import static akka.actor.SupervisorStrategy.restart;
import static akka.actor.SupervisorStrategy.resume;
import static akka.actor.SupervisorStrategy.stop;

import java.util.List;

import javax.inject.Named;

import org.springframework.context.annotation.Scope;

import scala.concurrent.duration.Duration;

import com.sgitario.akka.lab.di.utils.MessageEnum;

import akka.actor.ActorRef;
import akka.actor.OneForOneStrategy;
import akka.actor.SupervisorStrategy;
import akka.actor.SupervisorStrategy.Directive;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Function;

/**
 * Akka Actor available for injection and declared with a prototype scope. As
 * stated for CountingService Spring will associate a singleton scope by
 * default. Singletons have no place in Akka when it comes to creating Actors
 * (you can have millions of Actors).
 * 
 * @param countingService
 *            the service that will be automatically injected. We will use this
 *            service to increment a number.
 */
@Named("SupervisorCounterActor")
@Scope("prototype")
public class SupervisorCounterActor extends UntypedActor {

	LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	private List<ActorRef> actors;

	private Integer total = 0;

	public SupervisorCounterActor(List<ActorRef> actors) {
		this.actors = actors;
	}

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof Integer) {
			log.info("Supervisor Update Total");
			total += (Integer) message;
		} else if (message instanceof MessageEnum) {
			MessageEnum action = (MessageEnum) message;
			if (action == MessageEnum.Tick) {
				log.info("Supervisor Tick");
				for (ActorRef actor : actors) {
					actor.tell(MessageEnum.Tick, getSelf());
					actor.tell(MessageEnum.Get, getSelf());
				}
			} else if (action == MessageEnum.Get) {
				log.info("Supervisor Get");
				getSender().tell(total, getSelf());
			}
		} 
	}
	
	@Override
	public SupervisorStrategy supervisorStrategy() {
		return strategy;
	}

	private static SupervisorStrategy strategy = new OneForOneStrategy(10,
			Duration.create("1 minute"), new Function<Throwable, Directive>() {
				public Directive apply(Throwable t) {
					if (t instanceof ArithmeticException) {
						return resume();
					} else if (t instanceof NullPointerException) {
						return restart();
					} else if (t instanceof IllegalArgumentException) {
						return stop();
					} else {
						return escalate();
					}
				}
			});
}
