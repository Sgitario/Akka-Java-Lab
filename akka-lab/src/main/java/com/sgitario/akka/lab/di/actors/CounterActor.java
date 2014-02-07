package com.sgitario.akka.lab.di.actors;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.context.annotation.Scope;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import com.sgitario.akka.lab.di.services.CountingService;
import com.sgitario.akka.lab.di.utils.MessageEnum;

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
@Named("CounterActor")
@Scope("prototype")
public class CounterActor extends UntypedActor {

	LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	private CountingService countingService;

	private int count = 0;

	@Inject
	public CounterActor(CountingService countingService) {
		this.countingService = countingService;
	}

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof MessageEnum) {
			MessageEnum messageEnum = (MessageEnum) message;

			if (messageEnum == MessageEnum.Tick) {
				log.info("Counter Tick");
				count = countingService.increment(count);
			} else if (messageEnum == MessageEnum.Get) {
				log.info("Counter Get");
				getSender().tell(count, getSelf());
			}
		}
	}

}
