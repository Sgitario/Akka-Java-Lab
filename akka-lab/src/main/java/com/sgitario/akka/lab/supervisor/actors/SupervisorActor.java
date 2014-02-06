package com.sgitario.akka.lab.supervisor.actors;

import static akka.actor.SupervisorStrategy.escalate;
import static akka.actor.SupervisorStrategy.restart;
import static akka.actor.SupervisorStrategy.resume;
import static akka.actor.SupervisorStrategy.stop;

import com.sgitario.akka.lab.supervisor.utils.Result;

import scala.Option;
import scala.concurrent.duration.Duration;
import akka.actor.ActorRef;
import akka.actor.OneForOneStrategy;
import akka.actor.Props;
import akka.actor.SupervisorStrategy;
import akka.actor.SupervisorStrategy.Directive;
import akka.actor.UntypedActor;
import akka.japi.Function;

public class SupervisorActor extends UntypedActor {

	public ActorRef childActor;

	public SupervisorActor() {
		childActor = getContext().actorOf(Props.create(WorkerActor.class),
				"workerActor");
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

	@Override
	public SupervisorStrategy supervisorStrategy() {
		return strategy;
	}

	public void onReceive(Object o) throws Exception {
		if (o instanceof Result) {
			childActor.tell(o, getSender());
		} else {
			childActor.tell(o, getSelf());
		}
	}
	
	@Override
	public void preRestart(Throwable reason, Option<Object> message)
			throws Exception {
		// do not kill all children, which is the default here
	}
}
