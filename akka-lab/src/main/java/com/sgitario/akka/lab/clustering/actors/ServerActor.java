package com.sgitario.akka.lab.clustering.actors;


import static akka.actor.SupervisorStrategy.escalate;
import static akka.actor.SupervisorStrategy.restart;
import static akka.actor.SupervisorStrategy.resume;
import static akka.actor.SupervisorStrategy.stop;

import java.util.ArrayList;
import java.util.List;

import com.sgitario.akka.lab.supervisor.actors.UsersServerActor;
import com.sgitario.akka.lab.supervisor.actors.WorkerActor;

import scala.concurrent.duration.Duration;
import akka.actor.ActorRef;
import akka.actor.OneForOneStrategy;
import akka.actor.PoisonPill;
import akka.actor.Props;
import akka.actor.SupervisorStrategy;
import akka.actor.UntypedActor;
import akka.actor.SupervisorStrategy.Directive;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Function;

public class ServerActor extends UntypedActor {

	LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	private static int instanceCounter = 0;
	private final ActorRef usersServerActor;

	public ServerActor() {
		usersServerActor = getContext().actorOf(Props.create(UsersServerActor.class),
				"usersServerActor");
	}
	
	@Override
	public void preStart() {
		instanceCounter++;
		log.info("Starting ServerActor instance #" + instanceCounter
				+ ", hashcode #" + this.hashCode());
	}
	
	@Override
	public SupervisorStrategy supervisorStrategy() {
		return strategy;
	}

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof String) {
			// Audit user
			usersServerActor.tell(getSender(), getSelf());			
			
			// Attend command
			String command = (String) message;
			if (command == ":users") {
				// StringBuilder builder = new StringBuilder();
				
			} else {
				// propagate to all users
				usersServerActor.tell(command, getSender());
			}
			
			getSender().tell(message + " got something", getSelf());
		} else if (message instanceof PoisonPill) {
			getContext().system().shutdown();
		}
	}

	@Override
	public void postStop() {
		log.info("Stoping ServerActor instance #" + instanceCounter
				+ ", hashcode #" + this.hashCode());
		instanceCounter--;
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
