package com.sgitario.akka.lab.become;

import com.sgitario.akka.lab.become.actors.PingPongUsingStackActor;
import com.sgitario.akka.lab.become.utils.PingPongUtils;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class MainStack {
	public static void main(String[] args) throws Exception {

		ActorSystem system = ActorSystem.create("become");
		ActorRef pingPongActor = system
				.actorOf(Props.create(PingPongUsingStackActor.class));

		pingPongActor.tell(PingPongUtils.PING, ActorRef.noSender());

		Thread.sleep(2000);

		system.shutdown();
	}
}
