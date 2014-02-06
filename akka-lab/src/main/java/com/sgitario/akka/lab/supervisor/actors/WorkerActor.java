package com.sgitario.akka.lab.supervisor.actors;

import scala.Option;

import com.sgitario.akka.lab.supervisor.utils.Result;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class WorkerActor extends UntypedActor {
	LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	private int state = 0;

	@Override
	public void preStart() {
		log.info("Starting WorkerActor instance hashcode # {}", this.hashCode());
	}
	
	@Override
	public void preRestart(Throwable reason, Option<Object> message)
			throws Exception {
		super.preRestart(reason, message);
		
		log.info("Worker preRestart");
	}
	
	@Override
	public void postRestart(Throwable reason) throws Exception {
		super.postRestart(reason);
		
		log.info("Worker postRestart");
	}

	public void onReceive(Object o) throws Exception {
		if (o instanceof String) {
			throw new NullPointerException("String Value Passed");
		} else if (o instanceof Integer) {
			Integer value = (Integer) o;
			if (value <= 0) {
				throw new ArithmeticException("Number equal or less than zero");
			} else {
				state = value + 1;
			}
				
		} else if (o instanceof Result) {
			getSender().tell(state, getSelf());
		} else {
			throw new IllegalArgumentException("Wrong Argument");
		}
	}

	@Override
	public void postStop() {
		log.info("Stopping WorkerActor instance hashcode # {}", this.hashCode());
	}
}

