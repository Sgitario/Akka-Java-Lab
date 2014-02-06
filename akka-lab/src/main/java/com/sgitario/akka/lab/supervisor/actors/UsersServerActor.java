package com.sgitario.akka.lab.supervisor.actors;

import java.util.ArrayList;
import java.util.List;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;

public class UsersServerActor extends UntypedActor {

	private List<ActorRef> users = new ArrayList<ActorRef>();
	
	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof ActorRef) {
			ActorRef user = (ActorRef) message;
			
			boolean found = false;
			int index = 0;
			while (index < users.size() && !found) {
				if (users.get(index).hashCode() == user.hashCode()) {
					found = true;
				}
				
				index++;
			}
			
			if (!found) {
				users.add(user);
			}
		} else if (message instanceof String) {
			for (ActorRef user : users) {
				if (user.hashCode() != getSender().hashCode()) {
					user.tell(message, getSelf());
				}
			}
		}
	}

}
