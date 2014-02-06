package com.sgitario.akka.lab.inbox.actors;

import akka.actor.ActorRef;
import akka.actor.Inbox;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class FamilyActor extends UntypedActor {

	private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	private final Inbox inbox = Inbox.create(getContext().system());
	private final ActorRef postman;
	
	public FamilyActor(ActorRef postman) {
		this.postman = postman;
	}
	
	@Override
	public void preStart() throws Exception {
		super.preStart();
		
		inbox.watch(this.postman);
	}
	
	@Override
	public void onReceive(Object message) throws Exception {
		log.info("Family received a post: " + message);
	}

}
