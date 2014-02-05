package com.sgitario.akka.lab.helloworld.actors;

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
		// create the greeter actor
		final ActorRef greeter = getContext().actorOf(
				Props.create(GreeterActor.class), "greeter");
		// tell it to perform the greeting
		greeter.tell(GreeterActor.Msg.GREET, getSelf());
	}

	@Override
	public void onReceive(Object msg) {
		if (msg == GreeterActor.Msg.DONE) {
			// when the greeter is done, stop this actor and with it the
			// application
			message = "Hello World!";
			
			log.info("Received String message: {}", message);
			
			getContext().system().shutdown();
		} else
			unhandled(msg);
	}
}
