package com.sgitario.akka.lab.clustering.actors;

import com.sgitario.akka.lab.clustering.utils.Command;

import akka.actor.ActorSelection;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class ClientActor extends UntypedActor {

	LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	private ActorSelection serverActor;
	
	@Override
	public void preStart() throws Exception {
		super.preStart();
		
		log.info("Creating a reference to remote actor");
		// creating a reference to the remote ServerActor
		// by passing the complete remote actor path
		serverActor = getContext().actorSelection("akka.tcp://ServerSys@127.0.0.1:2555/user/serverActor");
		
		log.info("ServerActor with hashcode #" + serverActor.hashCode());
	}

	@Override
	public void onReceive(Object message) throws Exception {

		if (message instanceof Command) {
			serverActor.tell(((Command)message).getText(), getSelf());
		} else if (message instanceof String) {
			log.info((String) message);
		}
	}

}
