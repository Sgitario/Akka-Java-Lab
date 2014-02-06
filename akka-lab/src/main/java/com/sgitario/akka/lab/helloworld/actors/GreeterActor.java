package com.sgitario.akka.lab.helloworld.actors;

import akka.actor.UntypedActor;

public class GreeterActor extends UntypedActor {
	
	public static enum Msg {
		GREET, DONE, GOODBYE;
	}

	@Override
	public void onReceive(Object msg) {
		if (msg == Msg.GREET) {			
			getSender().tell(Msg.DONE, getSelf());
		} else
			unhandled(msg);
	}
}
