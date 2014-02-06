package com.sgitario.akka.lab.router;

import com.sgitario.akka.lab.router.actors.MsgEchoActor;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.routing.SmallestMailboxRouter;

public class Main {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ActorSystem system = ActorSystem.create("Routing1Example");
		ActorRef smallestMailBoxRouter = system.actorOf(Props.create(MsgEchoActor.class)
				.withRouter(new SmallestMailboxRouter(5)),"mySmallestMailBoxRouterActor");
		
		for (int i = 1; i <= 10; i++) {
			// works like roundrobin but tries to rebalance the load based on size of actor's mailbox
			smallestMailBoxRouter.tell(i, ActorRef.noSender());
		}
		
		system.shutdown();
	}
}
