package com.sgitario.akka.lab.helloworld.actors;

import scala.Option;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class HelloWorldActor extends UntypedActor {
	LoggingAdapter log = Logging.getLogger(getContext().system(), this);

	private String message;

	public String getMessage() {
		return message;
	}

	@Override
	public void preStart() {
		log.info("preStart");

		// create the greeter actor
		final ActorRef greeter = getContext().actorOf(
				Props.create(GreeterActor.class), "greeter");
		// tell it to perform the greeting
		greeter.tell(GreeterActor.Msg.GREET, getSelf());
	}

	@Override
	public void postStop() throws Exception {
		log.info("postStop");
	}

	@Override
	public void onReceive(Object msg) {
		if (msg == GreeterActor.Msg.DONE) {
			// when the greeter is done, stop this actor and with it the
			// application
			message = "Hello World!";

			log.info("Received String message: {}", message);

		} else if (msg == GreeterActor.Msg.GOODBYE) {
			getContext().stop(getSelf());
		} else {
			unhandled(msg);
		}

	}
}
