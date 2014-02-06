package com.sgitario.akka.lab.inbox.actors;

import java.util.ArrayList;
import java.util.List;

import akka.actor.ActorRef;
import akka.actor.Inbox;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class PostManActor extends UntypedActor {

	private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	private final Inbox inbox = Inbox.create(getContext().system());
	private final List<ActorRef> families = new ArrayList<ActorRef>();
	private int count = 0;
	
	@Override
	public void onReceive(Object message) throws Exception {
		
		if (message instanceof ActorRef) {
			families.add((ActorRef) message);
		} else {
			// Send message all families
			for (ActorRef family : families) {
				inbox.send(family, "Message " + count);
				log.info("PostMan sent the message " + count);
				
				count ++;
			}
		}
	}
}
