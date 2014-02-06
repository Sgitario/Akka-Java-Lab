package com.sgitario.akka.lab.become;

import com.sgitario.akka.lab.become.actors.PingPongActor;
import com.sgitario.akka.lab.become.utils.PingPongUtils;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class Main {
	public static void main(String[] args) throws Exception {

		ActorSystem system = ActorSystem.create("become");
		ActorRef pingPongActor = system
				.actorOf(Props.create(PingPongActor.class));

		pingPongActor.tell(PingPongUtils.PING, ActorRef.noSender());

		Thread.sleep(2000);

		system.shutdown();
	}
}
